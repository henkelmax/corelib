package de.maxhenkel.corelib.dataserializers;

import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.item.ItemStack;

public class DataSerializerItemList {

    private static final StreamCodec<RegistryFriendlyByteBuf, NonNullList<ItemStack>> CODEC = new StreamCodec<>() {

        @Override
        public void encode(RegistryFriendlyByteBuf buf, NonNullList<ItemStack> itemStacks) {
            buf.writeInt(itemStacks.size());

            for (ItemStack itemStack : itemStacks) {
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, itemStack);
            }
        }

        @Override
        public NonNullList<ItemStack> decode(RegistryFriendlyByteBuf itemStacks) {
            int length = itemStacks.readInt();
            NonNullList<ItemStack> list = NonNullList.withSize(length, ItemStack.EMPTY);
            for (int i = 0; i < list.size(); i++) {
                list.set(i, ItemStack.OPTIONAL_STREAM_CODEC.decode(itemStacks));
            }
            return list;
        }
    };

    public static EntityDataSerializer<NonNullList<ItemStack>> create() {
        return new EntityDataSerializer<>() {
            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, NonNullList<ItemStack>> codec() {
                return CODEC;
            }

            @Override
            public NonNullList<ItemStack> copy(NonNullList<ItemStack> itemStacks) {
                NonNullList<ItemStack> list = NonNullList.withSize(itemStacks.size(), ItemStack.EMPTY);
                for (int i = 0; i < itemStacks.size(); i++) {
                    list.set(i, itemStacks.get(i).copy());
                }
                return list;
            }
        };
    }
}
