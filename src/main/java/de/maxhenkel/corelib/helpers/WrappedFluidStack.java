package de.maxhenkel.corelib.helpers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.maxhenkel.corelib.client.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class WrappedFluidStack extends AbstractStack<FluidStack> {

    public WrappedFluidStack(FluidStack stack) {
        super(stack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(MatrixStack matrixStack, int x, int y) {
        TextureAtlasSprite texture = Minecraft.getInstance().getModelManager().getAtlasTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE).getSprite(stack.getFluid().getAttributes().getStillTexture());
        Minecraft.getInstance().getTextureManager().bindTexture(texture.getAtlasTexture().getTextureLocation());
        fluidBlit(matrixStack, x, y, 16, 16, texture, stack.getFluid().getAttributes().getColor());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<ITextComponent> getTooltip(Screen screen) {
        List<ITextComponent> tooltip = new ArrayList<>();

        tooltip.add(getDisplayName());

        if (Minecraft.getInstance().gameSettings.advancedItemTooltips) {
            ResourceLocation registryName = ForgeRegistries.FLUIDS.getKey(stack.getFluid());
            if (registryName != null) {
                tooltip.add((new StringTextComponent(registryName.toString())).mergeStyle(TextFormatting.DARK_GRAY));
            }
            if (stack.hasTag()) {
                tooltip.add((new TranslationTextComponent("item.nbt_tags", stack.getTag().keySet().size())).mergeStyle(TextFormatting.DARK_GRAY));
            }
        }

        return tooltip;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("").append(stack.getDisplayName()).mergeStyle(stack.getFluid().getAttributes().getRarity().color);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @OnlyIn(Dist.CLIENT)
    public static void fluidBlit(MatrixStack matrixStack, int x, int y, int width, int height, TextureAtlasSprite sprite, int color) {
        innerBlit(matrixStack.getLast().getMatrix(), x, x + width, y, y + height, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), (sprite.getMaxV() - sprite.getMinV()) * (float) height / 16F + sprite.getMinV(), color);
    }

    @OnlyIn(Dist.CLIENT)
    private static void innerBlit(Matrix4f matrix, int x1, int x2, int y1, int y2, float minU, float maxU, float minV, float maxV, int color) {
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(matrix, (float) x1, (float) y2, 0F).tex(minU, maxV).color(RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), 255).endVertex();
        bufferbuilder.pos(matrix, (float) x2, (float) y2, 0F).tex(maxU, maxV).color(RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), 255).endVertex();
        bufferbuilder.pos(matrix, (float) x2, (float) y1, 0F).tex(maxU, minV).color(RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), 255).endVertex();
        bufferbuilder.pos(matrix, (float) x1, (float) y1, 0F).tex(minU, minV).color(RenderUtils.getRed(color), RenderUtils.getGreen(color), RenderUtils.getBlue(color), 255).endVertex();
        bufferbuilder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(bufferbuilder);
    }

}
