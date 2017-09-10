package org.mvcspec.ozwark.wildfly.deployment;

import org.jboss.as.server.deployment.*;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.jboss.as.server.deployment.module.ModuleDependency;
import org.jboss.as.server.deployment.module.ModuleSpecification;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.DotName;
import org.jboss.logging.Logger;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;

import java.util.List;


public class SubsystemDeploymentProcessor implements DeploymentUnitProcessor {

    private static final ModuleIdentifier MVC_API = ModuleIdentifier.create("javax.mvc.api");
    private static final ModuleIdentifier OZARK = ModuleIdentifier.create("org.mvc-spec.ozark.core");
    private static final ModuleIdentifier OZARK_RESTEASY = ModuleIdentifier.create("org.mvc-spec.ozark.resteasy");
    private static final DotName CONTROLLER = DotName.createSimple("javax.mvc.annotation.Controller");

    private final Logger log = Logger.getLogger(SubsystemDeploymentProcessor.class);

    public static final Phase PHASE = Phase.DEPENDENCIES;
    public static final int PRIORITY = Phase.DEPENDENCIES_JAXRS;

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final ModuleSpecification moduleSpecification = deploymentUnit.getAttachment(Attachments.MODULE_SPECIFICATION);
        final ModuleLoader moduleLoader = Module.getBootModuleLoader();

        // all modules get the API dependency
        moduleSpecification.addSystemDependency(new ModuleDependency(moduleLoader, MVC_API, false, false, false, false));

        if (!isMVCDeployment(deploymentUnit)) {
            return;
        }

        log.debugf("Initializing Ozark for deployment %s", deploymentUnit.getName());
        moduleSpecification.addLocalDependency(new ModuleDependency(moduleLoader, OZARK, false, false, true, false));
        moduleSpecification.addLocalDependency(new ModuleDependency(moduleLoader, OZARK_RESTEASY, false, false, true, false));
    }

    private boolean isMVCDeployment(DeploymentUnit deploymentUnit) {
        final CompositeIndex index = deploymentUnit.getAttachment(Attachments.COMPOSITE_ANNOTATION_INDEX);

        final List<AnnotationInstance> annotations = index.getAnnotations(CONTROLLER);
        return !annotations.isEmpty();
    }

    @Override
    public void undeploy(DeploymentUnit context) {
    }

}
