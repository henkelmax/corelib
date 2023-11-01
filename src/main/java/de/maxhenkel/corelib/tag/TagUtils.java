package de.maxhenkel.corelib.tag;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.ForgeRegistries;
import javax.annotation.Nullable;
import java.util.Optional;

public class TagUtils {

    public static Tag<Block> AIR_BLOCK_TAG = new SingleElementTag<>(ForgeRegistries.BLOCKS.getKey(Blocks.AIR), Blocks.AIR);
    public static Tag<Item> AIR_ITEM_TAG = new SingleElementTag<>(ForgeRegistries.ITEMS.getKey(Items.AIR), Items.AIR);
    public static Tag<Fluid> AIR_FLUID_TAG = new SingleElementTag<>(ForgeRegistries.FLUIDS.getKey(Fluids.EMPTY), Fluids.EMPTY);

    /**
     * Gets the tag of the provided registry name
     *
     * @param name            the registry name of the block or a block tag starting with '#'
     * @param nullIfNotExists if the method should return null instead of air
     * @return the tag
     */
    @Nullable
    public static Tag<Block> getBlock(String name, boolean nullIfNotExists) {
        if (name.startsWith("#")) {
            ResourceLocation id = new ResourceLocation(name.substring(1));
            Optional<HolderSet.Named<Block>> tag = BuiltInRegistries.BLOCK.getTag(TagKey.create(Registries.BLOCK, id));
            if (tag.isEmpty()) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_BLOCK_TAG;
                }
            }
            return new BlockTag(tag.get());
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
                return new SingleElementTag<>(resourceLocation, block);
            }
        }
    }

    /**
     * Gets the tag of the provided registry name
     *
     * @param name the registry name of the block or a block tag starting with '#'
     * @return the tag
     */
    public static Tag<Block> getBlock(String name) {
        return getBlock(name, false);
    }

    public static Tag<Block> getBlockTag(ResourceLocation name, boolean nullIfNotExists) {
        Optional<HolderSet.Named<Block>> tag = BuiltInRegistries.BLOCK.getTag(BlockTags.create(name));
        if (tag.isEmpty()) {
            if (nullIfNotExists) {
                return null;
            } else {
                return AIR_BLOCK_TAG;
            }
        }
        return new BlockTag(tag.get());
    }

    public static Tag<Block> getBlockTag(ResourceLocation name) {
        return getBlockTag(name, false);
    }

    /**
     * Gets the tag of the provided registry name
     *
     * @param name            the registry name of the item or a item tag starting with '#'
     * @param nullIfNotExists if the method should return null instead of air
     * @return the tag
     */
    @Nullable
    public static Tag<Item> getItem(String name, boolean nullIfNotExists) {
        if (name.startsWith("#")) {
            ResourceLocation id = new ResourceLocation(name.substring(1));
            Optional<HolderSet.Named<Item>> tag = BuiltInRegistries.ITEM.getTag(TagKey.create(Registries.ITEM, id));
            if (tag.isEmpty()) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_ITEM_TAG;
                }
            }
            return new ItemTag(tag.get());
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
                return new SingleElementTag<>(resourceLocation, item);
            }
        }
    }

    /**
     * Gets the tag of the provided registry name
     *
     * @param name the registry name of the item or a item tag starting with '#'
     * @return the tag
     */
    public static Tag<Item> getItem(String name) {
        return getItem(name, false);
    }

    public static Tag<Item> getItemTag(ResourceLocation name, boolean nullIfNotExists) {
        Optional<HolderSet.Named<Item>> tag = BuiltInRegistries.ITEM.getTag(ItemTags.create(name));
        if (tag.isEmpty()) {
            if (nullIfNotExists) {
                return null;
            } else {
                return AIR_ITEM_TAG;
            }
        }
        return new ItemTag(tag.get());
    }

    public static Tag<Item> getItemTag(ResourceLocation name) {
        return getItemTag(name, false);
    }

    /**
     * Gets the tag of the provided registry name
     *
     * @param name            the registry name of the fluid or a fluid tag starting with '#'
     * @param nullIfNotExists if the method should return null instead of an empty fluid
     * @return the tag
     */
    @Nullable
    public static Tag<Fluid> getFluid(String name, boolean nullIfNotExists) {
        if (name.startsWith("#")) {
            ResourceLocation id = new ResourceLocation(name.substring(1));
            Optional<HolderSet.Named<Fluid>> tag = BuiltInRegistries.FLUID.getTag(TagKey.create(Registries.FLUID, id));
            if (tag.isEmpty()) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_FLUID_TAG;
                }
            }
            return new FluidTag(tag.get());
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
                return new SingleElementTag<>(resourceLocation, fluid);
            }
        }
    }

    /**
     * Gets the tag of the provided registry name
     *
     * @param name the registry name of the fluid or a fluid tag starting with '#'
     * @return the tag
     */
    public static Tag<Fluid> getFluid(String name) {
        return getFluid(name, false);
    }

    public static Tag<Fluid> getFluidTag(ResourceLocation name, boolean nullIfNotExists) {
        Optional<HolderSet.Named<Fluid>> tag = BuiltInRegistries.FLUID.getTag(FluidTags.create(name));
        if (tag.isEmpty()) {
            if (nullIfNotExists) {
                return null;
            } else {
                return AIR_FLUID_TAG;
            }
        }
        return new FluidTag(tag.get());
    }

    public static Tag<Fluid> getFluidTag(ResourceLocation name) {
        return getFluidTag(name, false);
    }

}
