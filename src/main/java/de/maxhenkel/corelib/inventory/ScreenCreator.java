package de.maxhenkel.corelib.inventory;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

@FunctionalInterface
public interface ScreenCreator<C extends Container, S extends ContainerScreen<C>> {
    S getScreen(C container, PlayerInventory playerInventory, ITextComponent title);
}
