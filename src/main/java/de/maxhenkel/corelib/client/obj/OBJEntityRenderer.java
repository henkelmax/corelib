package de.maxhenkel.corelib.client.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.Entity;

import java.util.List;

public abstract class OBJEntityRenderer<T extends Entity, S extends EntityRenderState> extends EntityRenderer<T, S> {

    protected OBJEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    public abstract List<OBJModelInstance<S>> getModels(S entity);

    @Override
    public void submit(S state, PoseStack pose, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        super.submit(state, pose, submitNodeCollector, cameraRenderState);
        submitModels(state, pose, submitNodeCollector, cameraRenderState);
    }

    protected void submitModels(S state, PoseStack pose, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        List<OBJModelInstance<S>> models = getModels(state);

        pose.pushPose();

        //TODO Interpolate yaw
        setupYaw(state, pose);
        setupPitch(state, pose);

        for (OBJModelInstance<S> model : models) {
            pose.pushPose();

            pose.translate(model.getOptions().getOffset().x, model.getOptions().getOffset().y, model.getOptions().getOffset().z);

            if (model.getOptions().getRotation() != null) {
                model.getOptions().getRotation().applyRotation(pose);
            }

            if (model.getOptions().getOnRender() != null) {
                model.getOptions().getOnRender().onRender(state, pose);
            }

            model.getModel().submitModels(model.getOptions().getTexture(), pose, submitNodeCollector, cameraRenderState, state.lightCoords);
            pose.popPose();
        }
        pose.popPose();
    }

    protected void setupYaw(S state, PoseStack pose) {
        //TODO Check if this is correct
    }

    protected void rotateYaw(PoseStack pose, float yaw) {
        pose.mulPose(Axis.YP.rotationDegrees(180F - yaw));
    }

    protected void setupPitch(S state, PoseStack matrixStack) {
        //TODO Check if this is needed
        /*float pitch = state.xRotO + (state.getXRot() - state.xRotO) * state.partialTick;
        matrixStack.mulPose(Axis.XN.rotationDegrees(pitch));*/
    }

}