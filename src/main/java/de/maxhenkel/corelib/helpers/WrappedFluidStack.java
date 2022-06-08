package de.maxhenkel.corelib.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import de.maxhenkel.corelib.client.RenderUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class WrappedFluidStack extends AbstractStack<FluidStack> {

    public WrappedFluidStack(FluidStack stack) {
        super(stack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PoseStack matrixStack, int x, int y) {
        TextureAtlasSprite texture = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(stack.getFluid().getAttributes().getStillTexture());
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        int color = stack.getFluid().getAttributes().getColor(stack);
        RenderSystem.setShaderColor(RenderUtils.getRedFloat(color), RenderUtils.getGreenFloat(color), RenderUtils.getBlueFloat(color), RenderUtils.getAlphaFloat(color));
        RenderSystem.setShaderTexture(0, texture.atlas().location());
        fluidBlit(matrixStack, x, y, 16, 16, texture, stack.getFluid().getAttributes().getColor());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Component> getTooltip(Screen screen) {
        List<Component> tooltip = new ArrayList<>();

        tooltip.add(getDisplayName());

        if (Minecraft.getInstance().options.advancedItemTooltips) {
            ResourceLocation registryName = ForgeRegistries.FLUIDS.getKey(stack.getFluid());
            if (registryName != null) {
                tooltip.add((Component.literal(registryName.toString())).withStyle(ChatFormatting.DARK_GRAY));
            }
            if (stack.hasTag()) {
                tooltip.add((Component.translatable("item.nbt_tags", stack.getTag().getAllKeys().size())).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        return tooltip;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("").append(stack.getDisplayName()).withStyle(stack.getFluid().getAttributes().getRarity().color);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @OnlyIn(Dist.CLIENT)
    public static void fluidBlit(PoseStack matrixStack, int x, int y, int width, int height, TextureAtlasSprite sprite, int color) {
        innerBlit(matrixStack.last().pose(), x, x + width, y, y + height, sprite.getU0(), sprite.getU1(), sprite.getV0(), (sprite.getV1() - sprite.getV0()) * (float) height / 16F + sprite.getV0(), color);
    }

    @OnlyIn(Dist.CLIENT)
    private static void innerBlit(Matrix4f matrix, int x1, int x2, int y1, int y2, float minU, float maxU, float minV, float maxV, int color) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix, (float) x1, (float) y2, 0F).uv(minU, maxV).color(RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), 255).endVertex();
        bufferbuilder.vertex(matrix, (float) x2, (float) y2, 0F).uv(maxU, maxV).color(RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), 255).endVertex();
        bufferbuilder.vertex(matrix, (float) x2, (float) y1, 0F).uv(maxU, minV).color(RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), 255).endVertex();
        bufferbuilder.vertex(matrix, (float) x1, (float) y1, 0F).uv(minU, minV).color(RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), 255).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

}
