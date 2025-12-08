package de.maxhenkel.corelib.client.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.corelib.math.Rotation;
import net.minecraft.resources.Identifier;
import org.joml.Vector3d;

public class OBJModelOptions<T> {

    private Identifier texture;
    private Vector3d offset;
    private Rotation rotation;
    private RenderListener<T> onRender;

    public OBJModelOptions(Identifier texture, Vector3d offset, Rotation rotation, RenderListener<T> onRender) {
        this.texture = texture;
        this.offset = offset;
        this.rotation = rotation;
        this.onRender = onRender;
    }

    public OBJModelOptions(Identifier texture, Vector3d offset, Rotation rotation) {
        this(texture, offset, rotation, null);
    }

    public OBJModelOptions(Identifier texture, Vector3d offset) {
        this(texture, offset, null, null);
    }

    public OBJModelOptions(Identifier texture, Vector3d offset, RenderListener<T> onRender) {
        this(texture, offset, null, onRender);
    }

    public Identifier getTexture() {
        return texture;
    }

    public void setTexture(Identifier texture) {
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
        void onRender(T object, PoseStack pose);
    }

}
