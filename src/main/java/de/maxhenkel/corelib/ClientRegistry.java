package de.maxhenkel.corelib;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientRegistry {

    /**
     * Registers a new keybinding
     *
     * @param name     the translation key
     * @param category the category
     * @param keyCode  the keycode
     * @return the keybinding
     */
    public static KeyMapping registerKeyBinding(String name, String category, int keyCode) {
        KeyMapping keyBinding = new KeyMapping(name, keyCode, category);
        net.minecraftforge.fmlclient.registry.ClientRegistry.registerKeyBinding(keyBinding);
        return keyBinding;
    }

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
