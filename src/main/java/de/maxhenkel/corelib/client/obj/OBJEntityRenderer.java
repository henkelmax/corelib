package de.maxhenkel.corelib.client.obj;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import java.util.List;

public abstract class OBJEntityRenderer<T extends Entity> extends EntityRenderer<T> {

    protected OBJEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    public abstract List<OBJModelInstance> getModels(T entity);

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
        List<OBJModelInstance> models = getModels(entity);

        matrixStack.push();
        setupRotation(entity, yaw, matrixStack);

        for (int i = 0; i < models.size(); i++) {
            matrixStack.push();

            matrixStack.translate(models.get(i).getOptions().getOffset().x, models.get(i).getOptions().getOffset().y, models.get(i).getOptions().getOffset().z);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-90F));

            if (models.get(i).getOptions().getRotation() != null) {
                models.get(i).getOptions().getRotation().applyRotation(matrixStack);
            }

            if (models.get(i).getOptions().getOnRender() != null) {
                models.get(i).getOptions().getOnRender().onRender(matrixStack, partialTicks);
            }

            models.get(i).getModel().render(models.get(i).getOptions().getTexture(), matrixStack, buffer, packedLight);
            matrixStack.pop();
        }
        matrixStack.pop();
    }

    protected void setupRotation(T entity, float yaw, MatrixStack matrixStack) {
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180F - yaw));
    }

}