package de.maxhenkel.corelib.tag;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;

public interface Tag<T> {

    ResourceLocation getName();

    boolean contains(T t);

    Collection<T> getAll();

}
