package de.maxhenkel.corelib.tag;

import net.minecraft.resources.Identifier;

import java.util.List;

public interface Tag<T> {

    Identifier getName();

    boolean contains(T t);

    List<T> getAll();

}
