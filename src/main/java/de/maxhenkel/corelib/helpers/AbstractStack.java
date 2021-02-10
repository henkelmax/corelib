package de.maxhenkel.corelib.helpers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public abstract class AbstractStack<T> {

    protected T stack;

    public AbstractStack(T stack) {
        this.stack = stack;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void render(MatrixStack matrixStack);

    @OnlyIn(Dist.CLIENT)
    public abstract List<ITextComponent> getTooltip(Screen screen);

    public abstract ITextComponent getDisplayName();

    public abstract boolean isEmpty();
}
