package de.maxhenkel.corelib.death;

import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.corelib.player.PlayerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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

    public static Death fromNBT(HolderLookup.Provider provider, CompoundTag compound) {
        Death death = new Death();
        if (compound.contains("IdMost") && compound.contains("IdLeast")) {
            death.id = new UUID(compound.getLong("IdMost").orElseThrow(), compound.getLong("IdLeast").orElseThrow());
        } else {
            death.id = compound.read("Id", UUIDUtil.CODEC).orElseGet(UUID::randomUUID);
        }
        if (compound.contains("PlayerUuidMost") && compound.contains("PlayerUuidLeast")) {
            death.playerUUID = new UUID(compound.getLong("PlayerUuidMost").orElseThrow(), compound.getLong("PlayerUuidLeast").orElseThrow());
        } else {
            death.playerUUID = compound.read("PlayerUuid", UUIDUtil.CODEC).orElseGet(UUID::randomUUID);
        }
        death.playerName = compound.getStringOr("PlayerName", "");

        ItemUtils.readInventory(provider, compound, "MainInventory", death.mainInventory);
        ItemUtils.readInventory(provider, compound, "ArmorInventory", death.armorInventory);
        ItemUtils.readInventory(provider, compound, "OffHandInventory", death.offHandInventory);

        death.additionalItems = ItemUtils.readItemList(provider, compound, "Items");

        Optional<CompoundTag> optionalEquipmentTag = compound.getCompound("Equipment");
        if (optionalEquipmentTag.isPresent()) {
            CompoundTag equipmentTag = optionalEquipmentTag.get();
            for (Map.Entry<String, Tag> entry : equipmentTag.entrySet()) {
                try {
                    EquipmentSlot slot = EquipmentSlot.byName(entry.getKey());
                    ItemStack.parse(provider, entry.getValue()).ifPresent(stack -> death.equipment.put(slot, stack));
                } catch (Exception ignored) {
                }
            }
        } else {
            Optional<ListTag> legacyList = compound.getList("Equipment");
            if (legacyList.isPresent()) {
                ListTag itemList = legacyList.get();
                if (!itemList.isEmpty()) {
                    ItemStack.parse(provider, itemList.getFirst()).ifPresent(stack -> death.equipment.put(EquipmentSlot.MAINHAND, stack));
                }
            }
            death.equipment.put(EquipmentSlot.OFFHAND, death.offHandInventory.getFirst());
            death.equipment.put(EquipmentSlot.FEET, death.armorInventory.get(EquipmentSlot.FEET.getIndex()));
            death.equipment.put(EquipmentSlot.LEGS, death.armorInventory.get(EquipmentSlot.LEGS.getIndex()));
            death.equipment.put(EquipmentSlot.CHEST, death.armorInventory.get(EquipmentSlot.CHEST.getIndex()));
            death.equipment.put(EquipmentSlot.HEAD, death.armorInventory.get(EquipmentSlot.HEAD.getIndex()));
            death.equipment.entrySet().removeIf(e -> e.getValue().isEmpty());
        }

        death.timestamp = compound.getLongOr("Timestamp", 0L);
        death.experience = compound.getIntOr("Experience", 0);
        death.posX = compound.getDoubleOr("PosX", 0.0);
        death.posY = compound.getDoubleOr("PosY", 0.0);
        death.posZ = compound.getDoubleOr("PosZ", 0.0);
        death.dimension = compound.getStringOr("Dimension", "minecraft:overworld");
        death.model = compound.getByteOr("Model", (byte) 0);

        return death;
    }

    public CompoundTag toNBT(HolderLookup.Provider provider) {
        return toNBT(provider, true);
    }

    public CompoundTag toNBT(HolderLookup.Provider provider, boolean withItems) {
        CompoundTag compound = new CompoundTag();
        compound.store("Id", UUIDUtil.CODEC, id);
        compound.store("PlayerUuid", UUIDUtil.CODEC, playerUUID);
        compound.putString("PlayerName", playerName);

        if (withItems) {
            ItemUtils.saveInventory(provider, compound, "MainInventory", mainInventory);
            ItemUtils.saveInventory(provider, compound, "ArmorInventory", armorInventory);
            ItemUtils.saveInventory(provider, compound, "OffHandInventory", offHandInventory);
            ItemUtils.saveItemList(provider, compound, "Items", additionalItems);
        }

        CompoundTag equipmentTag = new CompoundTag();
        for (Map.Entry<EquipmentSlot, ItemStack> entry : equipment.entrySet()) {
            ItemStack equipmentStack = entry.getValue();
            if (equipmentStack.isEmpty()) {
                continue;
            }
            equipmentTag.put(entry.getKey().getName(), equipmentStack.save(provider));
        }
        compound.put("Equipment", equipmentTag);

        compound.putLong("Timestamp", timestamp);
        compound.putInt("Experience", experience);
        compound.putDouble("PosX", posX);
        compound.putDouble("PosY", posY);
        compound.putDouble("PosZ", posZ);
        compound.putString("Dimension", dimension);
        compound.putByte("Model", model);

        return compound;
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
