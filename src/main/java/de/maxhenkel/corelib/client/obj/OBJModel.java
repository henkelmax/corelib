package de.maxhenkel.corelib.client.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.corelib.client.RenderUtils;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.neoforged.bus.api.IEventBus;
import org.joml.Vector3f;

import java.util.List;

public class OBJModel {

    private final ResourceLocation model;

    private OBJModelData data;

    public OBJModel(ResourceLocation model) {
        this.model = model;
    }

    private void load() {
        if (data == null) {
            data = OBJLoader.load(model);
        }
    }

    public void submitModels(ResourceLocation texture, PoseStack stack, SubmitNodeCollector collector, CameraRenderState cameraRenderState, int lightCoords) {
        load();
        stack.pushPose();

        collector.submitCustomGeometry(stack, OBJRenderUtils.ENTITY_CUTOUT_TRIANGLES.apply(texture), (pose, consumer) -> {
            for (int i = 0; i < data.faces.size(); i++) {
                int[][] face = data.faces.get(i);
                RenderUtils.vertex(consumer, pose, data.positions.get(face[0][0]), data.texCoords.get(face[0][1]), data.normals.get(face[0][2]), lightCoords, OverlayTexture.NO_OVERLAY);
                RenderUtils.vertex(consumer, pose, data.positions.get(face[1][0]), data.texCoords.get(face[1][1]), data.normals.get(face[1][2]), lightCoords, OverlayTexture.NO_OVERLAY);
                RenderUtils.vertex(consumer, pose, data.positions.get(face[2][0]), data.texCoords.get(face[2][1]), data.normals.get(face[2][2]), lightCoords, OverlayTexture.NO_OVERLAY);
            }
        });
        stack.popPose();
    }

    public static void registerRenderPipeline(IEventBus bus) {
        bus.addListener(OBJRenderUtils::onRegisterPipelines);
    }

    static class OBJModelData {
        private List<Vector3f> positions;
        private List<Vec2> texCoords;
        private List<Vector3f> normals;
        private List<int[][]> faces;

        public OBJModelData(List<Vector3f> positions, List<Vec2> texCoords, List<Vector3f> normals, List<int[][]> faces) {
            this.positions = positions;
            this.texCoords = texCoords;
            this.normals = normals;
            this.faces = faces;
        }

        public List<Vector3f> getPositions() {
            return positions;
        }

        public List<Vec2> getTexCoords() {
            return texCoords;
        }

        public List<Vector3f> getNormals() {
            return normals;
        }

        public List<int[][]> getFaces() {
            return faces;
        }
    }

}
