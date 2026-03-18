package de.maxhenkel.corelib.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class WrappedItemStack extends AbstractStack<ItemStack> {

    public WrappedItemStack(ItemStack stack) {
        super(stack);
    }

    @Override
    public void render(GuiGraphicsExtractor guiGraphics, int x, int y) {
        guiGraphics.item(stack, x, y, 0);
        guiGraphics.itemDecorations(Minecraft.getInstance().font, stack, x, y);
    }

    @Override
    public List<Component> getTooltip() {
        return Screen.getTooltipFromItem(Minecraft.getInstance(), stack);
    }

    @Override
    public Component getDisplayName() {
        return stack.getHoverName();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
