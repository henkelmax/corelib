package de.maxhenkel.corelib.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class TagUtils {

    public static Tag.Named<Block> AIR_BLOCK_TAG = new SingleElementTag<>(Blocks.AIR);
    public static Tag.Named<Item> AIR_ITEM_TAG = new SingleElementTag<>(Items.AIR);
    public static Tag.Named<Fluid> AIR_FLUID_TAG = new SingleElementTag<>(Fluids.EMPTY);

    /**
     * Gets the tag of the provided registry name
     *
     * @param name            the registry name of the block or a block tag starting with '#'
     * @param nullIfNotExists if the method should return null instead of air
     * @return the tag
     */
    @Nullable
    public static Tag.Named<Block> getBlock(String name, boolean nullIfNotExists) {
        if (name.startsWith("#")) {
            ResourceLocation id = new ResourceLocation(name.substring(1));
            Tag<Block> tag = BlockTags.getAllTags().getTag(id);
            if (tag == null) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_BLOCK_TAG;
                }
            }
            return new NamedTagWrapper<>(tag, id);
        } else {
            ResourceLocation resourceLocation = new ResourceLocation(name);
            if (!ForgeRegistries.BLOCKS.containsKey(resourceLocation)) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_BLOCK_TAG;
                }
            }
            Block block = ForgeRegistries.BLOCKS.getValue(resourceLocation);
            if (block == null) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_BLOCK_TAG;
                }
            } else {
                return new SingleElementTag<>(block);
            }
        }
    }

    /**
     * Gets the tag of the provided registry name
     *
     * @param name the registry name of the block or a block tag starting with '#'
     * @return the tag
     */
    public static Tag.Named<Block> getBlock(String name) {
        return getBlock(name, false);
    }

    /**
     * Gets the tag of the provided registry name
     *
     * @param name            the registry name of the item or a item tag starting with '#'
     * @param nullIfNotExists if the method should return null instead of air
     * @return the tag
     */
    @Nullable
    public static Tag.Named<Item> getItem(String name, boolean nullIfNotExists) {
        if (name.startsWith("#")) {
            ResourceLocation id = new ResourceLocation(name.substring(1));
            Tag<Item> tag = ItemTags.getAllTags().getTag(id);
            if (tag == null) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_ITEM_TAG;
                }
            }
            return new NamedTagWrapper<>(tag, id);
        } else {
            ResourceLocation resourceLocation = new ResourceLocation(name);
            if (!ForgeRegistries.ITEMS.containsKey(resourceLocation)) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_ITEM_TAG;
                }
            }
            Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
            if (item == null) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_ITEM_TAG;
                }
            } else {
                return new SingleElementTag<>(item);
            }
        }
    }

    /**
     * Gets the tag of the provided registry name
     *
     * @param name the registry name of the item or a item tag starting with '#'
     * @return the tag
     */
    public static Tag.Named<Item> getItem(String name) {
        return getItem(name, false);
    }

    /**
     * Gets the tag of the provided registry name
     *
     * @param name            the registry name of the fluid or a fluid tag starting with '#'
     * @param nullIfNotExists if the method should return null instead of an empty fluid
     * @return the tag
     */
    @Nullable
    public static Tag.Named<Fluid> getFluid(String name, boolean nullIfNotExists) {
        if (name.startsWith("#")) {
            ResourceLocation id = new ResourceLocation(name.substring(1));
            Tag<Fluid> tag = FluidTags.getAllTags().getTag(id);
            if (tag == null) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_FLUID_TAG;
                }
            }
            return new NamedTagWrapper<>(tag, id);
        } else {
            ResourceLocation resourceLocation = new ResourceLocation(name);
            if (!ForgeRegistries.FLUIDS.containsKey(resourceLocation)) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_FLUID_TAG;
                }
            }
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(name));
            if (fluid == null) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_FLUID_TAG;
                }
            } else {
                return new SingleElementTag<>(fluid);
            }
        }
    }

    /**
     * Gets the tag of the provided registry name
     *
     * @param name the registry name of the fluid or a fluid tag starting with '#'
     * @return the tag
     */
    public static Tag.Named<Fluid> getFluid(String name) {
        return getFluid(name, false);
    }


}
