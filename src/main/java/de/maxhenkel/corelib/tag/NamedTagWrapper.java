package de.maxhenkel.corelib.tag;

import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class NamedTagWrapper<T> implements ITag.INamedTag<T> {

    private ITag<T> tag;
    private ResourceLocation name;

    public NamedTagWrapper(ITag<T> tag, ResourceLocation name) {
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
