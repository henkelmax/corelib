package de.maxhenkel.corelib.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;

import java.util.List;

public class NamedTagWrapper<T> implements Tag.Named<T> {

    private Tag<T> tag;
    private ResourceLocation name;

    public NamedTagWrapper(Tag<T> tag, ResourceLocation name) {
        this.tag = tag;
        this.name = name;
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public boolean contains(T element) {
        return tag.contains(element);
    }

    @Override
    public List<T> getValues() {
        return tag.getValues();
    }
}
