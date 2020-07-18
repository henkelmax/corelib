package de.maxhenkel.corelib;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientRegistry {

    /**
     * Registers a new keybinding
     *
     * @param name the translation key
     * @param category the category
     * @param keyCode the keycode
     * @return the keybinding
     */
    public static KeyBinding registerKeyBinding(String name, String category, int keyCode) {
        KeyBinding keyBinding = new KeyBinding(name, keyCode, category);
        net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(keyBinding);
        return keyBinding;
    }

    /**
     * Regisers a screen associated to a container
     *
     * @param containerType the container type
     * @param screenFactory the screen factory
     */
    public static <C extends Container, S extends ContainerScreen<C>> void registerScreen(ContainerType<C> containerType, ScreenManager.IScreenFactory<C, S> screenFactory){
        ScreenManager.registerFactory(containerType, screenFactory);
    }

}
