package de.maxhenkel.corelib.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CustomRendererItem extends Item {

    protected Supplier<ItemRenderer> itemRenderer;

    public CustomRendererItem(Properties properties, Supplier<ItemRenderer> itemRenderer) {
        super(properties);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return itemRenderer.get();
            }
        });
    }

}
