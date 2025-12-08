package de.maxhenkel.corelib.tag;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.stream.Collectors;

public class FluidTag implements Tag<Fluid> {

    private final HolderSet.Named<Fluid> holderSet;

    public FluidTag(HolderSet.Named<Fluid> tagKey) {
        this.holderSet = tagKey;
    }

    @Override
    public Identifier getName() {
        return holderSet.key().location();
    }

    @Override
    public boolean contains(Fluid block) {
        return block.builtInRegistryHolder().is(holderSet.key());
    }

    @Override
    public List<Fluid> getAll() {
        return holderSet.stream().map(Holder::value).collect(Collectors.toList());
    }
}
