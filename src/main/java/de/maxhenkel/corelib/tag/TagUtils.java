package de.maxhenkel.corelib.tag;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class TagUtils {

    /**
     * Gets the tag of the provided registry name
     *
     * @param name the registry name of the block or a block tag starting with '#'
     * @return the tag
     */
    public static ITag<Block> getBlock(String name) {
        if (name.startsWith("#")) {
            return BlockTags.getCollection().get(new ResourceLocation(name.substring(1)));
        } else {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
            if (block == null) {
                return null;
            } else {
                return new SingleElementTag<>(block);
            }
        }
    }

    /**
     * Gets the tag of the provided registry name
     *
     * @param name the registry name of the item or a item tag starting with '#'
     * @return the tag
     */
    public static ITag<Item> getItem(String name) {
        if (name.startsWith("#")) {
            return ItemTags.getCollection().get(new ResourceLocation(name.substring(1)));
        } else {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
            if (item == null) {
                return null;
            } else {
                return new SingleElementTag<>(item);
            }
        }
    }

    /**
     * Gets the tag of the provided registry name
     *
     * @param name the registry name of the fluid or a fluid tag starting with '#'
     * @return the tag
     */
    public static ITag<Fluid> getFluid(String name) {
        if (name.startsWith("#")) {
            return FluidTags.getCollection().get(new ResourceLocation(name.substring(1)));
        } else {
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(name));
            if (fluid == null) {
                return null;
            } else {
                return new SingleElementTag<>(fluid);
            }
        }
    }

}
