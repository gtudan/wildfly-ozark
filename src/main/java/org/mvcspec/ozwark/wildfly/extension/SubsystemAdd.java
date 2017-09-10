package org.mvcspec.ozwark.wildfly.extension;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.dmr.ModelNode;
import org.mvcspec.ozwark.wildfly.deployment.SubsystemDeploymentProcessor;

/**
 * Handler responsible for adding the subsystem resource to the model
 */
class SubsystemAdd extends AbstractBoottimeAddStepHandler {

    static final SubsystemAdd INSTANCE = new SubsystemAdd();

    private SubsystemAdd() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performBoottime(OperationContext context, ModelNode operation, ModelNode model)   throws OperationFailedException {
        context.addStep(new AbstractDeploymentChainStep() {
            public void execute(DeploymentProcessorTarget processorTarget) {
                processorTarget.addDeploymentProcessor(SubsystemExtension.SUBSYSTEM_NAME,
                        SubsystemDeploymentProcessor.PHASE,
                        SubsystemDeploymentProcessor.PRIORITY,
                        new SubsystemDeploymentProcessor());

            }
        }, OperationContext.Stage.RUNTIME);

    }
}
