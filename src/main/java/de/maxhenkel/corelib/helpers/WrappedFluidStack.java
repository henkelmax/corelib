package de.maxhenkel.corelib.helpers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import de.maxhenkel.corelib.client.RenderUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class WrappedFluidStack extends AbstractStack<FluidStack> {

    public WrappedFluidStack(FluidStack stack) {
        super(stack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(GuiGraphics guiGraphics, int x, int y) {
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(stack.getFluid());
        TextureAtlasSprite texture = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(extensions.getStillTexture(stack));
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        int color = extensions.getTintColor(stack);
        RenderSystem.setShaderColor(RenderUtils.getRedFloat(color), RenderUtils.getGreenFloat(color), RenderUtils.getBlueFloat(color), RenderUtils.getAlphaFloat(color));
        RenderSystem.setShaderTexture(0, texture.atlasLocation());
        fluidBlit(guiGraphics, x, y, 16, 16, texture, color);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Component> getTooltip() {
        List<Component> tooltip = new ArrayList<>();

        tooltip.add(getDisplayName());

        if (Minecraft.getInstance().options.advancedItemTooltips) {
            if (BuiltInRegistries.FLUID.containsValue(stack.getFluid())) {
                ResourceLocation registryName = BuiltInRegistries.FLUID.getKey(stack.getFluid());
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
        return Component.literal("").append(stack.getDisplayName()).withStyle(stack.getFluid().getFluidType().getRarity().getStyleModifier());
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @OnlyIn(Dist.CLIENT)
    public static void fluidBlit(GuiGraphics guiGraphics, int x, int y, int width, int height, TextureAtlasSprite sprite, int color) {
        innerBlit(guiGraphics.pose().last().pose(), x, x + width, y, y + height, sprite.getU0(), sprite.getU1(), sprite.getV0(), (sprite.getV1() - sprite.getV0()) * (float) height / 16F + sprite.getV0(), color);
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
