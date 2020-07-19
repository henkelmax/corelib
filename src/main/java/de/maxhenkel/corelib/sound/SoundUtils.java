package de.maxhenkel.corelib.sound;

import net.minecraft.world.World;

public class SoundUtils {

    public static float getVariatedPitch(World world) {
        return world.rand.nextFloat() * 0.1F + 0.9F;
    }

}
