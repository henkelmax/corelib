package de.maxhenkel.corelib.tag;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.stream.Collectors;

public class BlockTag implements Tag<Block> {

    private final HolderSet.Named<Block> holderSet;

    public BlockTag(HolderSet.Named<Block> tagKey) {
        this.holderSet = tagKey;
    }

    @Override
    public ResourceLocation getName() {
        return holderSet.key().location();
    }

    @Override
    public boolean contains(Block block) {
        return block.builtInRegistryHolder().is(holderSet.key());
    }

    @Override
    public List<Block> getAll() {
        return holderSet.stream().map(Holder::value).collect(Collectors.toList());
    }
}
