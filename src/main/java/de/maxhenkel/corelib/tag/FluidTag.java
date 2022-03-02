package de.maxhenkel.corelib.tag;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.util.Collection;
import java.util.stream.Collectors;

public class FluidTag implements Tag<Fluid> {

    private final HolderSet.Named<Fluid> holderSet;

    public FluidTag(HolderSet.Named<Fluid> tagKey) {
        this.holderSet = tagKey;
    }

    @Override
    public ResourceLocation getName() {
        return holderSet.key().location();
    }

    @Override
    public boolean contains(Fluid block) {
        return block.builtInRegistryHolder().is(holderSet.key());
    }

    @Override
    public Collection<Fluid> getAll() {
        return holderSet.stream().map(Holder::value).collect(Collectors.toList());
    }
}
