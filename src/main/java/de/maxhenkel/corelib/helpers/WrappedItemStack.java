package de.maxhenkel.corelib.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class WrappedItemStack extends AbstractStack<ItemStack> {

    public WrappedItemStack(ItemStack stack) {
        super(stack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.renderItem(stack, x, y, 0);
        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, stack, x, y);
    }

    @OnlyIn(Dist.CLIENT)
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
