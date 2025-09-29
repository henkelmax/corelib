package de.maxhenkel.corelib.helpers;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class WrappedFluidStack extends AbstractStack<FluidStack> {

    public WrappedFluidStack(FluidStack stack) {
        super(stack);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y) {
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(stack.getFluid());
        TextureAtlasSprite texture = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS).getSprite(extensions.getStillTexture(stack));
        int color = extensions.getTintColor(stack);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, texture, x, y, 16, 16, color);
    }

    @Override
    public List<Component> getTooltip() {
        List<Component> tooltip = new ArrayList<>();

        tooltip.add(getDisplayName());

        if (Minecraft.getInstance().options.advancedItemTooltips) {
            if (BuiltInRegistries.FLUID.containsValue(stack.getFluid())) {
                ResourceLocation registryName = BuiltInRegistries.FLUID.getKey(stack.getFluid());
                tooltip.add((Component.literal(registryName.toString())).withStyle(ChatFormatting.DARK_GRAY));
            }
            if (!stack.getComponentsPatch().isEmpty()) {
                tooltip.add((Component.translatable("item.nbt_tags", stack.getComponentsPatch().size())).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        return tooltip;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("").append(stack.getHoverName()).withStyle(stack.getFluid().getFluidType().getRarity().getStyleModifier());
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

}
