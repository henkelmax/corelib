package de.maxhenkel.corelib.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ScreenBase<T extends Container> extends ContainerScreen<T> {

    protected static final int FONT_COLOR = 4210752;

    protected ResourceLocation texture;

    public ScreenBase(ResourceLocation texture, T container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.texture = texture;
    }

    @Override
    public void render(int x, int y, float partialTicks) {
        renderBackground();
        super.render(x, y, partialTicks);
        renderHoveredToolTip(x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        minecraft.getTextureManager().bindTexture(texture);

        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    }
}
