package de.maxhenkel.corelib.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CustomRendererBlockItem extends BlockItem {

    protected Supplier<ItemRenderer> itemRenderer;

    public CustomRendererBlockItem(Block block, Properties properties, Supplier<ItemRenderer> itemRenderer) {
        super(block, properties);
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
