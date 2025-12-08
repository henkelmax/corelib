package de.maxhenkel.corelib.tag;

import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.List;

public class SingleElementTag<T> implements Tag<T> {

    private final Identifier name;
    private final T element;
    private final List<T> list;

    public SingleElementTag(Identifier name, T element) {
        this.name = name;
        this.element = element;
        this.list = Collections.singletonList(element);
    }

    public T getElement() {
        return element;
    }

    @Override
    public Identifier getName() {
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
