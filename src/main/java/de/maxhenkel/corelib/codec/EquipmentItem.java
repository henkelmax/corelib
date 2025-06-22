package de.maxhenkel.corelib.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public record EquipmentItem(EquipmentSlot slot, ItemStack item) {

    public static final Codec<EquipmentItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("slot").forGetter(o -> o.slot.getName()),
            ItemStack.CODEC.fieldOf("item").forGetter(EquipmentItem::item)).apply(instance, (s, itemStack) -> new EquipmentItem(EquipmentSlot.byName(s), itemStack)));

}
