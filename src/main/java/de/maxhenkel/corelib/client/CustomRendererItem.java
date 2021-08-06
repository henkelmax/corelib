package de.maxhenkel.corelib.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class CustomRendererItem extends Item {

    public CustomRendererItem(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        ItemRenderer renderer = createItemRenderer();
        if (renderer != null) {
            consumer.accept(new IItemRenderProperties() {
                @Override
                public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
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
