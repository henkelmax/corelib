package de.maxhenkel.corelib.dataserializers;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.item.ItemStack;

public class DataSerializerItemList {

    public static EntityDataSerializer<NonNullList<ItemStack>> create() {
        return new EntityDataSerializer<>() {

            @Override
            public void write(FriendlyByteBuf packetBuffer, NonNullList<ItemStack> itemStacks) {
                packetBuffer.writeInt(itemStacks.size());

                for (ItemStack itemStack : itemStacks) {
                    packetBuffer.writeItem(itemStack);
                }
            }

            @Override
            public NonNullList<ItemStack> read(FriendlyByteBuf buf) {
                int length = buf.readInt();
                NonNullList<ItemStack> list = NonNullList.withSize(length, ItemStack.EMPTY);
                for (int i = 0; i < list.size(); i++) {
                    list.set(i, buf.readItem());
                }
                return list;
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
