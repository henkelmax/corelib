package de.maxhenkel.corelib.death;

import com.mojang.serialization.Codec;
import de.maxhenkel.corelib.codec.EquipmentItem;
import de.maxhenkel.corelib.codec.ValueInputOutputUtils;
import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.corelib.player.PlayerUtils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.*;
import java.util.stream.Collectors;

public class Death {

    private UUID id;
    private UUID playerUUID;
    private String playerName;

    private NonNullList<ItemStack> mainInventory = NonNullList.withSize(36, ItemStack.EMPTY);
    private NonNullList<ItemStack> armorInventory = NonNullList.withSize(4, ItemStack.EMPTY);
    private NonNullList<ItemStack> offHandInventory = NonNullList.withSize(1, ItemStack.EMPTY);
    private NonNullList<ItemStack> additionalItems = NonNullList.create();

    private EnumMap<EquipmentSlot, ItemStack> equipment = new EnumMap<>(EquipmentSlot.class);

    private long timestamp;
    private int experience;
    private double posX;
    private double posY;
    private double posZ;
    private String dimension;
    private byte model;

    private Death() {

    }

    public UUID getId() {
        return id;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public NonNullList<ItemStack> getMainInventory() {
        return mainInventory;
    }

    public NonNullList<ItemStack> getArmorInventory() {
        return armorInventory;
    }

    public NonNullList<ItemStack> getOffHandInventory() {
        return offHandInventory;
    }

    public NonNullList<ItemStack> getAdditionalItems() {
        return additionalItems;
    }

    public EnumMap<EquipmentSlot, ItemStack> getEquipment() {
        return equipment;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getExperience() {
        return experience;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public BlockPos getBlockPos() {
        return BlockPos.containing(posX, posY, posZ);
    }

    public String getDimension() {
        return dimension;
    }

    public byte getModel() {
        return model;
    }

    @Override
    public String toString() {
        return "Death{name=" + playerName + "timestamp=" + timestamp + "}";
    }

    public static Death fromPlayer(Player player) {
        Death death = new Death();
        death.id = UUID.randomUUID();
        death.playerUUID = player.getUUID();
        death.playerName = player.getName().getString();

        for (int i = 0; i < death.mainInventory.size(); i++) {
            death.mainInventory.set(i, player.getInventory().getNonEquipmentItems().get(i));
        }
        death.armorInventory.set(EquipmentSlot.FEET.getIndex(), player.getItemBySlot(EquipmentSlot.FEET));
        death.armorInventory.set(EquipmentSlot.LEGS.getIndex(), player.getItemBySlot(EquipmentSlot.LEGS));
        death.armorInventory.set(EquipmentSlot.CHEST.getIndex(), player.getItemBySlot(EquipmentSlot.CHEST));
        death.armorInventory.set(EquipmentSlot.HEAD.getIndex(), player.getItemBySlot(EquipmentSlot.HEAD));

        death.offHandInventory.set(0, player.getItemBySlot(EquipmentSlot.OFFHAND));

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemBySlot = player.getItemBySlot(slot);
            if (itemBySlot.isEmpty()) {
                continue;
            }
            death.equipment.put(slot, itemBySlot.copy());
        }

        death.timestamp = System.currentTimeMillis();
        death.experience = player.experienceLevel;
        death.posX = player.getX();
        death.posY = Math.max(player.getY(), player.getRootVehicle().getY());
        death.posZ = player.getZ();
        death.dimension = player.level().dimension().location().toString();
        death.model = PlayerUtils.getModel(player);
        return death;
    }

    /**
     * Matches the actual drops with the items the player had on death.
     *
     * @param items the drops list
     */
    public void processDrops(Collection<ItemEntity> items) {
        List<ItemStack> drops = items.stream()
                .filter(Objects::nonNull)
                .map(ItemEntity::getItem)
                .filter(itemStack -> !itemStack.isEmpty())
                .collect(Collectors.toList());
        processInventory(drops, mainInventory);
        processInventory(drops, armorInventory);
        processInventory(drops, offHandInventory);
        additionalItems.addAll(drops);
    }

    private void processInventory(List<ItemStack> drops, NonNullList<ItemStack> inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack itemStack = inventory.get(i);
            if (itemStack.isEmpty()) {
                continue;
            }
            if (drops.contains(itemStack)) {
                drops.remove(itemStack);
            } else {
                inventory.set(i, ItemStack.EMPTY);
            }
        }
    }

    public NonNullList<ItemStack> getAllItems() {
        NonNullList<ItemStack> items = NonNullList.create();
        items.addAll(filterList(mainInventory));
        items.addAll(filterList(armorInventory));
        items.addAll(filterList(offHandInventory));
        items.addAll(filterList(additionalItems));
        return items;
    }

    private List<ItemStack> filterList(List<ItemStack> stacks) {
        return stacks.stream().filter(itemStack -> !itemStack.isEmpty()).collect(Collectors.toList());
    }

    public static Death read(HolderLookup.Provider registryAccess, CompoundTag tag) {
        TagValueInput valueInput = ValueInputOutputUtils.createValueInput("death", registryAccess, tag);
        return read(valueInput);
    }

    public static Death read(ValueInput valueInput, String key) {
        return read(valueInput.childOrEmpty(key));
    }

    public static Death read(ValueInput valueInput) {
        Death death = new Death();
        death.id = valueInput.read("Id", UUIDUtil.CODEC).orElseGet(UUID::randomUUID);
        death.playerUUID = valueInput.read("PlayerUuid", UUIDUtil.CODEC).orElse(Util.NIL_UUID);

        death.playerName = valueInput.getStringOr("PlayerName", "");

        ItemUtils.readInventory(valueInput, "MainInventory", death.mainInventory);
        ItemUtils.readInventory(valueInput, "ArmorInventory", death.armorInventory);
        ItemUtils.readInventory(valueInput, "OffHandInventory", death.offHandInventory);

        death.additionalItems = ItemUtils.readItemList(valueInput, "Items");

        Optional<List<EquipmentItem>> optionalEquipment = valueInput.read("Equipment", Codec.list(EquipmentItem.CODEC));

        if (optionalEquipment.isPresent()) {
            for (EquipmentItem equipmentItem : optionalEquipment.get()) {
                death.equipment.put(equipmentItem.slot(), equipmentItem.item());
            }
        } else {
            Optional<CompoundTag> optionalEquipmentTag = ValueInputOutputUtils.getTag(valueInput, "Equipment");
            if (optionalEquipmentTag.isPresent()) {
                CompoundTag equipmentTag = optionalEquipmentTag.get();
                for (Map.Entry<String, Tag> entry : equipmentTag.entrySet()) {
                    if (entry.getValue() instanceof CompoundTag tag) {
                        try {
                            TagValueInput equipmentInput = ValueInputOutputUtils.createValueInput("death", valueInput.lookup(), tag);
                            Optional<ItemStack> item = equipmentInput.read(ItemStack.MAP_CODEC);
                            EquipmentSlot slot = EquipmentSlot.byName(entry.getKey());
                            item.ifPresent(stack -> death.equipment.put(slot, stack));
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        }

        death.timestamp = valueInput.getLongOr("Timestamp", 0L);
        death.experience = valueInput.getIntOr("Experience", 0);
        death.posX = valueInput.getDoubleOr("PosX", 0.0);
        death.posY = valueInput.getDoubleOr("PosY", 0.0);
        death.posZ = valueInput.getDoubleOr("PosZ", 0.0);
        death.dimension = valueInput.getStringOr("Dimension", "minecraft:overworld");
        death.model = valueInput.getByteOr("Model", (byte) 0);

        return death;
    }

    public void write(ValueOutput valueOutput) {
        write(valueOutput, true);
    }

    public void write(ValueOutput valueOutput, String key, boolean withItems) {
        write(valueOutput.child(key), withItems);
    }

    public void write(ValueOutput valueOutput, String key) {
        write(valueOutput, key, true);
    }

    public CompoundTag write(HolderLookup.Provider registryAccess) {
        return write(registryAccess, true);
    }

    public CompoundTag write(HolderLookup.Provider registryAccess, boolean withItems) {
        TagValueOutput valueOutput = ValueInputOutputUtils.createValueOutput("death", registryAccess);
        write(valueOutput, withItems);
        return ValueInputOutputUtils.toTag(valueOutput);
    }

    public void write(ValueOutput valueOutput, boolean withItems) {
        valueOutput.store("Id", UUIDUtil.CODEC, id);
        valueOutput.store("PlayerUuid", UUIDUtil.CODEC, playerUUID);
        valueOutput.putString("PlayerName", playerName);

        if (withItems) {
            ItemUtils.saveInventory(valueOutput, "MainInventory", mainInventory);
            ItemUtils.saveInventory(valueOutput, "ArmorInventory", armorInventory);
            ItemUtils.saveInventory(valueOutput, "OffHandInventory", offHandInventory);
            ItemUtils.saveItemList(valueOutput, "Items", additionalItems);
        }

        ValueOutput.TypedOutputList<EquipmentItem> equipmentOut = valueOutput.list("Equipment", EquipmentItem.CODEC);
        for (Map.Entry<EquipmentSlot, ItemStack> entry : equipment.entrySet()) {
            ItemStack equipmentStack = entry.getValue();
            if (equipmentStack.isEmpty()) {
                continue;
            }
            equipmentOut.add(new EquipmentItem(entry.getKey(), equipmentStack));
        }

        valueOutput.putLong("Timestamp", timestamp);
        valueOutput.putInt("Experience", experience);
        valueOutput.putDouble("PosX", posX);
        valueOutput.putDouble("PosY", posY);
        valueOutput.putDouble("PosZ", posZ);
        valueOutput.putString("Dimension", dimension);
        valueOutput.putByte("Model", model);
    }

    public static class Builder {
        private Death death;

        public Builder(UUID playerUUID, UUID id) {
            death = new Death();
            death.dimension = "";
            death.playerName = "";
            death.playerUUID = playerUUID;
            death.id = id;
        }

        public Death build() {
            return death;
        }

        public Builder id(UUID uuid) {
            death.id = uuid;
            return this;
        }

        public Builder playerUUID(UUID uuid) {
            death.playerUUID = uuid;
            return this;
        }

        public Builder playerName(String name) {
            death.playerName = name;
            return this;
        }

        public Builder mainInventory(NonNullList<ItemStack> list) {
            death.mainInventory = list;
            return this;
        }

        public Builder armorInventory(NonNullList<ItemStack> list) {
            death.armorInventory = list;
            return this;
        }

        public Builder offHandInventory(NonNullList<ItemStack> list) {
            death.offHandInventory = list;
            return this;
        }

        public Builder additionalItems(NonNullList<ItemStack> list) {
            death.additionalItems = list;
            return this;
        }

        public Builder equipment(EnumMap<EquipmentSlot, ItemStack> list) {
            death.equipment = list;
            return this;
        }

        public Builder timestamp(long timestamp) {
            death.timestamp = timestamp;
            return this;
        }

        public Builder experience(int experience) {
            death.experience = experience;
            return this;
        }

        public Builder posX(double posX) {
            death.posX = posX;
            return this;
        }

        public Builder posY(double posY) {
            death.posY = posY;
            return this;
        }

        public Builder posZ(double posZ) {
            death.posZ = posZ;
            return this;
        }

        public Builder dimension(String dimension) {
            death.dimension = dimension;
            return this;
        }

        public Builder model(byte model) {
            death.model = model;
            return this;
        }
    }

}
