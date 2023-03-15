package de.maxhenkel.corelib.helpers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public class WrappedItemStack extends AbstractStack<ItemStack> {

    public WrappedItemStack(ItemStack stack) {
        super(stack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(PoseStack matrixStack, int x, int y) {
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(matrixStack, stack, x, y, 0);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Component> getTooltip(Screen screen) {
        return screen.getTooltipFromItem(stack);
    }

    @Override
    public Component getDisplayName() {
        return stack.getHoverName();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
