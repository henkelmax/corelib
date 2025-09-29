package de.maxhenkel.corelib.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.phys.Vec2;
import org.joml.Vector3f;

public class RenderUtils {

    public static int getArgb(int a, int red, int green, int blue) {
        return a << 24 | red << 16 | green << 8 | blue;
    }

    public static int getAlpha(int argb) {
        return (argb >> 24) & 0xFF;
    }

    public static int getRed(int argb) {
        return (argb >> 16) & 0xFF;
    }

    public static int getGreen(int argb) {
        return (argb >> 8) & 0xFF;
    }

    public static int getBlue(int argb) {
        return argb & 0xFF;
    }

    public static float getAlphaFloat(int argb) {
        return (float) getAlpha(argb) / 255F;
    }

    public static float getRedFloat(int argb) {
        return (float) getRed(argb) / 255F;
    }

    public static float getGreenFloat(int argb) {
        return (float) getGreen(argb) / 255F;
    }

    public static float getBlueFloat(int argb) {
        return (float) getBlue(argb) / 255F;
    }

    public static void vertex(VertexConsumer builder, PoseStack.Pose pose, Vector3f position, Vec2 texCoord, Vector3f normal, int light, int overlay) {
        vertex(builder, pose, position.x(), position.y(), position.z(), texCoord.x, texCoord.y, normal.x(), normal.y(), normal.z(), 255, 255, 255, light, overlay);
    }

    public static void vertex(VertexConsumer builder, PoseStack.Pose pose, float posX, float posY, float posZ, float texX, float texY, int red, int green, int blue, int light, int overlay) {
        vertex(builder, pose, posX, posY, posZ, texX, texY, 0F, 0F, -1F, red, green, blue, light, overlay);
    }

    public static void vertex(VertexConsumer builder, PoseStack.Pose pose, float posX, float posY, float posZ, float texX, float texY, int light, int overlay) {
        vertex(builder, pose, posX, posY, posZ, texX, texY, 0F, 0F, -1F, 255, 255, 255, light, overlay);
    }

    public static void vertex(VertexConsumer builder, PoseStack.Pose pose, float posX, float posY, float posZ, float texX, float texY, float norX, float norY, float norZ, int red, int green, int blue, int light, int overlay) {
        builder.addVertex(pose.pose(), posX, posY, posZ)
                .setColor(red, green, blue, 255)
                .setUv(texX, texY)
                .setOverlay(overlay)
                .setLight(light)
                .setNormal(pose, norX, norY, norZ);
    }

}
