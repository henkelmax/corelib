package de.maxhenkel.corelib.tag;

import net.minecraft.tags.Tag;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Collections;
import java.util.List;

public class SingleElementTag<T extends ForgeRegistryEntry<?>> extends Tag<T> {

    private final T element;

    public SingleElementTag(T element) {
        super(element.getRegistryName());
        this.element = element;
    }

    @Override
    public boolean contains(T element) {
        return this.element == element;
    }

    @Override
    public List<T> getAllElements() {
        return Collections.singletonList(element);
    }

}
