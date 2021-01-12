package de.maxhenkel.corelib.client.obj;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class OBJEntityRenderer<T extends Entity> extends EntityRenderer<T> {

    protected OBJEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    public abstract List<OBJModelInstance<T>> getModels(T entity);

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return null;
    }

    @Override
    public void render(T entity, float yaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        renderModels(entity, yaw, partialTicks, matrixStack, buffer, packedLight);
        super.render(entity, yaw, partialTicks, matrixStack, buffer, packedLight);
    }

    protected void renderModels(T entity, float yaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        List<OBJModelInstance<T>> models = getModels(entity);

        matrixStack.push();

        setupYaw(entity, yaw, matrixStack);
        setupPitch(entity, partialTicks, matrixStack);

        for (OBJModelInstance<T> model : models) {
            matrixStack.push();

            matrixStack.translate(model.getOptions().getOffset().x, model.getOptions().getOffset().y, model.getOptions().getOffset().z);

            if (model.getOptions().getRotation() != null) {
                model.getOptions().getRotation().applyRotation(matrixStack);
            }

            if (model.getOptions().getOnRender() != null) {
                model.getOptions().getOnRender().onRender(entity, matrixStack, partialTicks);
            }

            model.getModel().render(model.getOptions().getTexture(), matrixStack, buffer, packedLight);
            matrixStack.pop();
        }
        matrixStack.pop();
    }

    protected void setupYaw(T entity, float yaw, MatrixStack matrixStack) {
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180F - yaw));
    }

    protected void setupPitch(T entity, float partialTicks, MatrixStack matrixStack) {
        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        matrixStack.rotate(Vector3f.XN.rotationDegrees(pitch));
    }

}