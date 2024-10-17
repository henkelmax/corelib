package de.maxhenkel.corelib.client;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@Deprecated
public class CustomRendererBlockItem extends BlockItem {

    public CustomRendererBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    //TODO Check
    /*@Override
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
    }*/

    @OnlyIn(Dist.CLIENT)
    public ItemRenderer createItemRenderer() {
        return null;
    }
}
