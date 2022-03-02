package de.maxhenkel.corelib.tag;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;

public class SingleElementTag<T> implements Tag<T> {

    private final ResourceLocation name;
    private final T element;
    private final Collection<T> list;

    public SingleElementTag(ResourceLocation name, T element) {
        this.name = name;
        this.element = element;
        this.list = Collections.singletonList(element);
    }

    public T getElement() {
        return element;
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public boolean contains(T t) {
        return element == t;
    }

    @Override
    public Collection<T> getAll() {
        return list;
    }
}
