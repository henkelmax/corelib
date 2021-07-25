package de.maxhenkel.corelib.client.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import de.maxhenkel.corelib.math.Rotation;
import net.minecraft.resources.ResourceLocation;

public class OBJModelOptions<T> {

    private ResourceLocation texture;
    private Vector3d offset;
    private Rotation rotation;
    private RenderListener<T> onRender;

    public OBJModelOptions(ResourceLocation texture, Vector3d offset, Rotation rotation, RenderListener<T> onRender) {
        this.texture = texture;
        this.offset = offset;
        this.rotation = rotation;
        this.onRender = onRender;
    }

    public OBJModelOptions(ResourceLocation texture, Vector3d offset, Rotation rotation) {
        this(texture, offset, rotation, null);
    }

    public OBJModelOptions(ResourceLocation texture, Vector3d offset) {
        this(texture, offset, null, null);
    }

    public OBJModelOptions(ResourceLocation texture, Vector3d offset, RenderListener<T> onRender) {
        this(texture, offset, null, onRender);
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public void setTexture(ResourceLocation texture) {
        this.texture = texture;
    }

    public Vector3d getOffset() {
        return offset;
    }

    public OBJModelOptions<T> setOffset(Vector3d offset) {
        this.offset = offset;
        return this;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public OBJModelOptions<T> setRotation(Rotation rotation) {
        this.rotation = rotation;
        return this;
    }

    public RenderListener<T> getOnRender() {
        return onRender;
    }

    public interface RenderListener<T> {
        void onRender(T object, PoseStack matrixStack, float partialTicks);
    }

}
