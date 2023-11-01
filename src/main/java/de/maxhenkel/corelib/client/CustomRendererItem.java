package de.maxhenkel.corelib.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import java.util.function.Consumer;

public class CustomRendererItem extends Item {

    public CustomRendererItem(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        ItemRenderer renderer = createItemRenderer();
        if (renderer != null) {
            consumer.accept(new IClientItemExtensions() {
                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                    return renderer.getRenderer();
                }
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    public ItemRenderer createItemRenderer() {
        return null;
    }
}
