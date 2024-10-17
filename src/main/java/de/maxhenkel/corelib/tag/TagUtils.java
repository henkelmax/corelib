package de.maxhenkel.corelib.tag;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.Optional;

public class TagUtils {

    public static Tag<Block> AIR_BLOCK_TAG = new SingleElementTag<>(BuiltInRegistries.BLOCK.getKey(Blocks.AIR), Blocks.AIR);
    public static Tag<Item> AIR_ITEM_TAG = new SingleElementTag<>(BuiltInRegistries.ITEM.getKey(Items.AIR), Items.AIR);
    public static Tag<Fluid> AIR_FLUID_TAG = new SingleElementTag<>(BuiltInRegistries.FLUID.getKey(Fluids.EMPTY), Fluids.EMPTY);

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
            ResourceLocation id = ResourceLocation.tryParse(name.substring(1));
            if (id == null) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_BLOCK_TAG;
                }
            }
            Optional<HolderSet.Named<Block>> tag = BuiltInRegistries.BLOCK.get(TagKey.create(Registries.BLOCK, id));
            if (tag.isEmpty()) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_BLOCK_TAG;
                }
            }
            return new BlockTag(tag.get());
        } else {
            ResourceLocation resourceLocation = ResourceLocation.tryParse(name);
            if (resourceLocation == null) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_BLOCK_TAG;
                }
            }
            if (!BuiltInRegistries.BLOCK.containsKey(resourceLocation)) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_BLOCK_TAG;
                }
            }
            return BuiltInRegistries.BLOCK
                    .get(resourceLocation)
                    .map(ref -> (Tag<Block>) new SingleElementTag<>(resourceLocation, ref.value()))
                    .orElseGet(() -> nullIfNotExists ? null : AIR_BLOCK_TAG);
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
        Optional<HolderSet.Named<Block>> tag = BuiltInRegistries.BLOCK.get(BlockTags.create(name));
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
            ResourceLocation id = ResourceLocation.tryParse(name.substring(1));
            if (id == null) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_ITEM_TAG;
                }
            }
            Optional<HolderSet.Named<Item>> tag = BuiltInRegistries.ITEM.get(TagKey.create(Registries.ITEM, id));
            if (tag.isEmpty()) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_ITEM_TAG;
                }
            }
            return new ItemTag(tag.get());
        } else {
            ResourceLocation resourceLocation = ResourceLocation.tryParse(name);
            if (resourceLocation == null) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_ITEM_TAG;
                }
            }
            if (!BuiltInRegistries.ITEM.containsKey(resourceLocation)) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_ITEM_TAG;
                }
            }
            return BuiltInRegistries.ITEM
                    .get(resourceLocation)
                    .map(ref -> (Tag<Item>) new SingleElementTag<>(resourceLocation, ref.value()))
                    .orElseGet(() -> nullIfNotExists ? null : AIR_ITEM_TAG);
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
        Optional<HolderSet.Named<Item>> tag = BuiltInRegistries.ITEM.get(ItemTags.create(name));
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
            ResourceLocation id = ResourceLocation.tryParse(name.substring(1));
            if (id == null) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_FLUID_TAG;
                }
            }
            Optional<HolderSet.Named<Fluid>> tag = BuiltInRegistries.FLUID.get(TagKey.create(Registries.FLUID, id));
            if (tag.isEmpty()) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_FLUID_TAG;
                }
            }
            return new FluidTag(tag.get());
        } else {
            ResourceLocation resourceLocation = ResourceLocation.tryParse(name);
            if (resourceLocation == null) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_FLUID_TAG;
                }
            }
            if (!BuiltInRegistries.FLUID.containsKey(resourceLocation)) {
                if (nullIfNotExists) {
                    return null;
                } else {
                    return AIR_FLUID_TAG;
                }
            }

            return BuiltInRegistries.FLUID
                    .get(resourceLocation)
                    .map(ref -> (Tag<Fluid>) new SingleElementTag<>(resourceLocation, ref.value()))
                    .orElseGet(() -> nullIfNotExists ? null : AIR_FLUID_TAG);
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
        Optional<HolderSet.Named<Fluid>> tag = BuiltInRegistries.FLUID.get(FluidTags.create(name));
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
