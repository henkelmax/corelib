package de.maxhenkel.corelib.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class CustomRenderItemExtension implements IClientItemExtensions {

    protected ItemRenderer renderer;

    public CustomRenderItemExtension(ItemRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return renderer.getRenderer();
    }
}
