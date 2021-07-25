package de.maxhenkel.corelib.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;

public class BlockUtils {

    /**
     * Returns if the provided block is an air block
     *
     * @param block the block
     * @return if the block is an air block
     */
    public static boolean isAir(Block block) {
        return block.defaultBlockState().getMaterial() == Material.AIR;
    }

}
