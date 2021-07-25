package de.maxhenkel.corelib.sound;

import net.minecraft.world.level.Level;

public class SoundUtils {

    public static float getVariatedPitch(Level world) {
        return world.random.nextFloat() * 0.1F + 0.9F;
    }

}
