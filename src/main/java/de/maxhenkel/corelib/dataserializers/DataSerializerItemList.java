package de.maxhenkel.corelib.dataserializers;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.DataSerializerEntry;

/**
 * Note that this serializer has to be registered before using it!
 * <p>
 * Use {@link #register} for registration.
 */
public class DataSerializerItemList {

    public static final IDataSerializer<NonNullList<ItemStack>> ITEM_LIST = new IDataSerializer<NonNullList<ItemStack>>() {

        @Override
        public void write(PacketBuffer packetBuffer, NonNullList<ItemStack> itemStacks) {
            packetBuffer.writeInt(itemStacks.size());

            for (ItemStack itemStack : itemStacks) {
                packetBuffer.writeItem(itemStack);
            }
        }

        public NonNullList<ItemStack> read(PacketBuffer buf) {
            int length = buf.readInt();
            NonNullList<ItemStack> list = NonNullList.withSize(length, ItemStack.EMPTY);
            for (int i = 0; i < list.size(); i++) {
                list.set(i, buf.readItem());
            }
            return list;
        }

        public DataParameter<NonNullList<ItemStack>> createKey(int id) {
            return new DataParameter<>(id, this);
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
