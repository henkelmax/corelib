package de.maxhenkel.corelib.helpers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public abstract class AbstractStack<T> {

    protected T stack;

    public AbstractStack(T stack) {
        this.stack = stack;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void render(PoseStack matrixStack, int x, int y);

    @OnlyIn(Dist.CLIENT)
    public abstract List<Component> getTooltip(Screen screen);

    public abstract Component getDisplayName();

    public abstract boolean isEmpty();
}
