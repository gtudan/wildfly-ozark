package org.mvcspec.ozwark.wildfly.deployment;

import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.deployment.*;
import org.jboss.as.server.deployment.module.ModuleDependency;
import org.jboss.as.server.deployment.module.ModuleSpecification;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;

public class SubsystemDeploymentProcessor implements DeploymentUnitProcessor {

    private static final ModuleIdentifier MVC_API = ModuleIdentifier.create("javax.mvc.api");
    private static final ModuleIdentifier OZARK = ModuleIdentifier.create("org.mvc-spec.ozark.core");
    private static final ModuleIdentifier OZARK_RESTEASY = ModuleIdentifier.create("org.mvc-spec.ozark.resteasy");

    private final Logger log = Logger.getLogger(SubsystemDeploymentProcessor.class);

    /**
     * See {@link Phase} for a description of the different phases
     */
    public static final Phase PHASE = Phase.DEPENDENCIES;

    public static final int PRIORITY = Phase.DEPENDENCIES_JAXRS;

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        log.debugf("Initializing Ozark for deployment {0}", deploymentUnit.getName());

        final ModuleSpecification moduleSpecification = deploymentUnit.getAttachment(Attachments.MODULE_SPECIFICATION);
        final ModuleLoader moduleLoader = Module.getBootModuleLoader();

        moduleSpecification.addLocalDependency(new ModuleDependency(moduleLoader, MVC_API, false, false, false, false));
        moduleSpecification.addLocalDependency(new ModuleDependency(moduleLoader, OZARK, false, true, true, false));
        moduleSpecification.addLocalDependency(new ModuleDependency(moduleLoader, OZARK_RESTEASY, false, true, true, false));
    }

    @Override
    public void undeploy(DeploymentUnit context) {
    }

}
