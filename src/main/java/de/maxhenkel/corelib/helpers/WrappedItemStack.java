package de.maxhenkel.corelib.helpers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class WrappedItemStack extends AbstractStack<ItemStack> {

    public WrappedItemStack(ItemStack stack) {
        super(stack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(MatrixStack matrixStack, int x, int y) {
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(Minecraft.getInstance().player, stack, x, y);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<ITextComponent> getTooltip(Screen screen) {
        return screen.getTooltipFromItem(stack);
    }

    @Override
    public ITextComponent getDisplayName() {
        return stack.getDisplayName();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
