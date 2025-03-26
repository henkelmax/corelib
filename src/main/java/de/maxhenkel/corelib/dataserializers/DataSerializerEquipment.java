package de.maxhenkel.corelib.dataserializers;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.EnumMap;
import java.util.Map;

public class DataSerializerEquipment {

    private static final StreamCodec<RegistryFriendlyByteBuf, EnumMap<EquipmentSlot, ItemStack>> CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, EnumMap<EquipmentSlot, ItemStack> equipment) {
            buf.writeInt(equipment.size());

            for (Map.Entry<EquipmentSlot, ItemStack> entry : equipment.entrySet()) {
                buf.writeUtf(entry.getKey().getName());
                ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, entry.getValue());
            }
        }

        @Override
        public EnumMap<EquipmentSlot, ItemStack> decode(RegistryFriendlyByteBuf buf) {
            int length = buf.readInt();
            EnumMap<EquipmentSlot, ItemStack> map = new EnumMap<>(EquipmentSlot.class);
            for (int i = 0; i < length; i++) {
                String name = buf.readUtf();
                EquipmentSlot slot = EquipmentSlot.byName(name);
                map.put(slot, ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
            }
            return map;
        }
    };

    public static EntityDataSerializer<EnumMap<EquipmentSlot, ItemStack>> create() {
        return new EntityDataSerializer<>() {
            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, EnumMap<EquipmentSlot, ItemStack>> codec() {
                return CODEC;
            }

            @Override
            public EnumMap<EquipmentSlot, ItemStack> copy(EnumMap<EquipmentSlot, ItemStack> equipment) {
                EnumMap<EquipmentSlot, ItemStack> copy = new EnumMap<>(EquipmentSlot.class);
                for (Map.Entry<EquipmentSlot, ItemStack> entry : equipment.entrySet()) {
                    copy.put(entry.getKey(), entry.getValue().copy());
                }
                return copy;
            }
        };
    }

}
