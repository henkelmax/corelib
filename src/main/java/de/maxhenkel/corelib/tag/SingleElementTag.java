package de.maxhenkel.corelib.tag;

import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class SingleElementTag<T> implements Tag<T> {

    private final ResourceLocation name;
    private final T element;
    private final List<T> list;

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
    public List<T> getAll() {
        return list;
    }
}
