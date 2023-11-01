package de.maxhenkel.corelib;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientRegistry {

    /**
     * Regisers a screen associated to a container
     *
     * @param containerType the container type
     * @param screenFactory the screen factory
     */
    public static <C extends AbstractContainerMenu, S extends Screen & MenuAccess<C>> void registerScreen(MenuType<C> containerType, MenuScreens.ScreenConstructor<C, S> screenFactory) {
        MenuScreens.register(containerType, screenFactory);
    }

}
