package de.maxhenkel.corelib.dataserializers;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.DataSerializerEntry;

/**
 * Note that this serializer has to be registered before using it!
 * <p>
 * Use {@link #register} for registration.
 */
public class DataSerializerItemList {

    public static final EntityDataSerializer<NonNullList<ItemStack>> ITEM_LIST = new EntityDataSerializer<NonNullList<ItemStack>>() {


        @Override
        public void write(FriendlyByteBuf packetBuffer, NonNullList<ItemStack> itemStacks) {
            packetBuffer.writeInt(itemStacks.size());

            for (ItemStack itemStack : itemStacks) {
                packetBuffer.writeItem(itemStack);
            }
        }

        public NonNullList<ItemStack> read(FriendlyByteBuf buf) {
            int length = buf.readInt();
            NonNullList<ItemStack> list = NonNullList.withSize(length, ItemStack.EMPTY);
            for (int i = 0; i < list.size(); i++) {
                list.set(i, buf.readItem());
            }
            return list;
        }

        public EntityDataAccessor<NonNullList<ItemStack>> createKey(int id) {
            return new EntityDataAccessor<>(id, this);
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

    public static void register(RegistryEvent.Register<DataSerializerEntry> event, ResourceLocation registryName) {
        DataSerializerEntry dataSerializerEntryItemList = new DataSerializerEntry(DataSerializerItemList.ITEM_LIST);
        dataSerializerEntryItemList.setRegistryName(registryName);
        event.getRegistry().register(dataSerializerEntryItemList);
    }

}
