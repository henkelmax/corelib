package de.maxhenkel.corelib.tag;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemTag implements Tag<Item> {

    private final HolderSet.Named<Item> holderSet;

    public ItemTag(HolderSet.Named<Item> tagKey) {
        this.holderSet = tagKey;
    }

    @Override
    public Identifier getName() {
        return holderSet.key().location();
    }

    @Override
    public boolean contains(Item block) {
        return block.builtInRegistryHolder().is(holderSet.key());
    }

    @Override
    public List<Item> getAll() {
        return holderSet.stream().map(Holder::value).collect(Collectors.toList());
    }
}
