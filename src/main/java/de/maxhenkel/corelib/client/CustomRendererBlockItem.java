package de.maxhenkel.corelib.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import java.util.function.Consumer;

public class CustomRendererBlockItem extends BlockItem {

    public CustomRendererBlockItem(Block block, Properties properties) {
        super(block, properties);
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
