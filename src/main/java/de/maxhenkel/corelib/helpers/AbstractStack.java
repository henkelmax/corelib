package de.maxhenkel.corelib.helpers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public abstract class AbstractStack<T> {

    protected T stack;

    public AbstractStack(T stack) {
        this.stack = stack;
    }

    public abstract void render(GuiGraphics guiGraphics, int x, int y);

    public abstract List<Component> getTooltip();

    public abstract Component getDisplayName();

    public abstract boolean isEmpty();
}
