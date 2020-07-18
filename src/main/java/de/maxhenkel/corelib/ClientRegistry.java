package de.maxhenkel.corelib;

import net.minecraft.client.settings.KeyBinding;
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

}
