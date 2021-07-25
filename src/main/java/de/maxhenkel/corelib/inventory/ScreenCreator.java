package de.maxhenkel.corelib.inventory;


import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

@FunctionalInterface
public interface ScreenCreator<C extends AbstractContainerMenu, S extends AbstractContainerScreen<C>> {
    S getScreen(C container, Inventory playerInventory, Component title);
}
