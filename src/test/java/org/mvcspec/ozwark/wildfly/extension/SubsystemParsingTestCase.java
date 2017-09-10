package org.mvcspec.ozwark.wildfly.extension;


import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;

import java.util.List;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.subsystem.test.AbstractSubsystemTest;
import org.jboss.as.subsystem.test.KernelServices;
import org.jboss.dmr.ModelNode;
import org.junit.Assert;
import org.junit.Test;

public class SubsystemParsingTestCase extends AbstractSubsystemTest {

    public SubsystemParsingTestCase() {
        super(SubsystemExtension.SUBSYSTEM_NAME, new SubsystemExtension());
    }

    /**
     * Tests that the xml is parsed into the correct operations
     */
    @Test
    public void testParseSubsystem() throws Exception {
        //Parse the subsystem xml into operations
        String subsystemXml = "<subsystem xmlns=\"" + SubsystemExtension.NAMESPACE + "\"/>";
        List<ModelNode> operations = super.parse(subsystemXml);

        ///Check that we have the expected number of operations
        Assert.assertEquals(1, operations.size());

        //Check that each operation has the correct content
        ModelNode addSubsystem = operations.get(0);
        Assert.assertEquals(ADD, addSubsystem.get(OP).asString());
        PathAddress address = PathAddress.pathAddress(addSubsystem.get(OP_ADDR));
        Assert.assertEquals(1, address.size());
        PathElement element = address.getElement(0);
        Assert.assertEquals(SUBSYSTEM, element.getKey());
        Assert.assertEquals(SubsystemExtension.SUBSYSTEM_NAME, element.getValue());
    }

    /**
     * Test that the model created from the xml looks as expected
     */
    @Test
    public void testInstallIntoController() throws Exception {
        //Parse the subsystem xml and install into the controller
        String subsystemXml = "<subsystem xmlns=\"" + SubsystemExtension.NAMESPACE + "\"/>";
        KernelServices services = super.createKernelServicesBuilder(null).setSubsystemXml(subsystemXml).build();

        //Read the whole model and make sure it looks as expected
        ModelNode model = services.readWholeModel();
        Assert.assertTrue(model.get(SUBSYSTEM).hasDefined(SubsystemExtension.SUBSYSTEM_NAME));
    }

    /**
     * Starts a controller with a given subsystem xml and then checks that a second
     * controller started with the xml marshaled from the first one results in the same model
     */
    @Test
    public void testParseAndMarshalModel() throws Exception {
        //Parse the subsystem xml and install into the first controller
        String subsystemXml = "<subsystem xmlns=\"" + SubsystemExtension.NAMESPACE + "\"/>";
        KernelServices servicesA = super.createKernelServicesBuilder(null).setSubsystemXml(subsystemXml).build();
        //Get the model and the persisted xml from the first controller
        ModelNode modelA = servicesA.readWholeModel();
        String marshaled = servicesA.getPersistedSubsystemXml();

        //Install the persisted xml from the first controller into a second controller
        KernelServices servicesB = super.createKernelServicesBuilder(null).setSubsystemXml(marshaled).build();
        ModelNode modelB = servicesB.readWholeModel();

        //Make sure the models from the two controllers are identical
        super.compare(modelA, modelB);
    }

    /**
     * Tests that the subsystem can be removed
     */
    @Test
    public void testSubsystemRemoval() throws Exception {
        //Parse the subsystem xml and install into the first controller
        String subsystemXml = "<subsystem xmlns=\"" + SubsystemExtension.NAMESPACE + "\"/>";
        KernelServices services = super.createKernelServicesBuilder(null).setSubsystemXml(subsystemXml).build();
        //Checks that the subsystem was removed from the model
        super.assertRemoveSubsystemResources(services);
    }
}
