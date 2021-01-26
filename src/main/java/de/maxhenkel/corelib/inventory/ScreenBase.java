package de.maxhenkel.corelib.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ScreenBase<T extends Container> extends ContainerScreen<T> {

    protected static final int FONT_COLOR = 4210752;

    protected ResourceLocation texture;
    protected List<HoverArea> hoverAreas;

    public ScreenBase(ResourceLocation texture, T container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.texture = texture;
        this.hoverAreas = new ArrayList<>();
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, x, y, partialTicks);
        renderHoveredTooltip(matrixStack, x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        minecraft.getTextureManager().bindTexture(texture);

        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {

    }

    public void drawHoverAreas(MatrixStack matrixStack, int mouseX, int mouseY) {
        for (HoverArea hoverArea : hoverAreas) {
            if (mouseX >= guiLeft + hoverArea.posX && mouseX < guiLeft + hoverArea.posX + hoverArea.width) {
                if (mouseY >= guiTop + hoverArea.posY && mouseY < guiTop + hoverArea.posY + hoverArea.height) {
                    renderTooltip(matrixStack, hoverArea.tooltip.get(), mouseX - guiLeft, mouseY - guiTop);
                }
            }
        }
    }

    public int getBlitSize(int amount, int max, int size) {
        return size - (int) (((float) amount / (float) max) * (float) size);
    }

    public static class HoverArea {
        private final int posX, posY;
        private final int width, height;
        private final Supplier<List<IReorderingProcessor>> tooltip;

        public HoverArea(int posX, int posY, int width, int height, Supplier<List<IReorderingProcessor>> tooltip) {
            this.posX = posX;
            this.posY = posY;
            this.width = width;
            this.height = height;
            this.tooltip = tooltip;
        }
    }

}
