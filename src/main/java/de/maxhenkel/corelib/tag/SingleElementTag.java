package de.maxhenkel.corelib.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Collections;
import java.util.List;

public class SingleElementTag<T extends ForgeRegistryEntry<?>> implements Tag.Named<T> {

    private final T element;

    public SingleElementTag(T element) {
        this.element = element;
    }

    @Override
    public ResourceLocation getName() {
        return element.getRegistryName();
    }

    @Override
    public boolean contains(T element) {
        return this.element == element;
    }

    @Override
    public List<T> getValues() {
        return Collections.singletonList(element);
    }

    public T getElement() {
        return element;
    }

}
