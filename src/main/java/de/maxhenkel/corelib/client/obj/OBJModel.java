package de.maxhenkel.corelib.client.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import de.maxhenkel.corelib.client.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class OBJModel {

    private ResourceLocation model;

    private OBJModelData data;

    public OBJModel(ResourceLocation model) {
        this.model = model;
    }

    @OnlyIn(Dist.CLIENT)
    private void load() {
        if (data == null) {
            data = OBJLoader.load(model);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void render(ResourceLocation texture, PoseStack matrixStack, MultiBufferSource buffer, int light) {
        load();
        matrixStack.pushPose();

        VertexConsumer builder = buffer.getBuffer(OBJRenderUtils.ENTITY_CUTOUT_TRIANGLES.apply(texture));

        for (int i = 0; i < data.faces.size(); i++) {
            int[][] face = data.faces.get(i);
            RenderUtils.vertex(builder, matrixStack, data.positions.get(face[0][0]), data.texCoords.get(face[0][1]), data.normals.get(face[0][2]), light, OverlayTexture.NO_OVERLAY);
            RenderUtils.vertex(builder, matrixStack, data.positions.get(face[1][0]), data.texCoords.get(face[1][1]), data.normals.get(face[1][2]), light, OverlayTexture.NO_OVERLAY);
            RenderUtils.vertex(builder, matrixStack, data.positions.get(face[2][0]), data.texCoords.get(face[2][1]), data.normals.get(face[2][2]), light, OverlayTexture.NO_OVERLAY);
        }
        matrixStack.popPose();
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
