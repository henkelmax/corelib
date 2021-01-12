package de.maxhenkel.corelib.death;

import de.maxhenkel.corelib.item.ItemUtils;
import de.maxhenkel.corelib.player.PlayerUtils;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Death {

    private UUID id;
    private UUID playerUUID;
    private String playerName;

    private NonNullList<ItemStack> mainInventory = NonNullList.withSize(36, ItemStack.EMPTY);
    private NonNullList<ItemStack> armorInventory = NonNullList.withSize(4, ItemStack.EMPTY);
    private NonNullList<ItemStack> offHandInventory = NonNullList.withSize(1, ItemStack.EMPTY);
    private NonNullList<ItemStack> additionalItems = NonNullList.create();

    private NonNullList<ItemStack> equipment = NonNullList.withSize(EquipmentSlotType.values().length, ItemStack.EMPTY);

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

    public NonNullList<ItemStack> getEquipment() {
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
        return new BlockPos(posX, posY, posZ);
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

    public static Death fromPlayer(PlayerEntity player) {
        Death death = new Death();
        death.id = UUID.randomUUID();
        death.playerUUID = player.getUniqueID();
        death.playerName = player.getName().getUnformattedComponentText();

        for (int i = 0; i < death.mainInventory.size(); i++) {
            death.mainInventory.set(i, player.inventory.mainInventory.get(i));
        }
        for (int i = 0; i < death.armorInventory.size(); i++) {
            death.armorInventory.set(i, player.inventory.armorInventory.get(i));
        }
        for (int i = 0; i < death.offHandInventory.size(); i++) {
            death.offHandInventory.set(i, player.inventory.offHandInventory.get(i));
        }
        death.equipment = NonNullList.withSize(EquipmentSlotType.values().length, ItemStack.EMPTY);
        for (int i = 0; i < death.equipment.size(); i++) {
            death.equipment.set(i, player.getItemStackFromSlot(EquipmentSlotType.values()[i]).copy());
        }

        death.timestamp = System.currentTimeMillis();
        death.experience = player.experienceLevel;
        death.posX = player.getPosX();
        death.posY = player.getPosY();
        death.posZ = player.getPosZ();
        death.dimension = DimensionType.getKey(player.dimension).toString();
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

    public static Death fromNBT(CompoundNBT compound) {
        Death death = new Death();
        //TODO replace with put UUID
        death.id = new UUID(compound.getLong("IdMost"), compound.getLong("IdLeast"));
        death.playerUUID = new UUID(compound.getLong("PlayerUuidMost"), compound.getLong("PlayerUuidLeast"));
        death.playerName = compound.getString("PlayerName");

        ItemUtils.readInventory(compound, "MainInventory", death.mainInventory);
        ItemUtils.readInventory(compound, "ArmorInventory", death.armorInventory);
        ItemUtils.readInventory(compound, "OffHandInventory", death.offHandInventory);

        death.additionalItems = ItemUtils.readItemList(compound, "Items");

        ItemUtils.readItemList(compound, "Equipment", death.equipment);

        death.timestamp = compound.getLong("Timestamp");
        death.experience = compound.getInt("Experience");
        death.posX = compound.getDouble("PosX");
        death.posY = compound.getDouble("PosY");
        death.posZ = compound.getDouble("PosZ");
        death.dimension = compound.getString("Dimension");
        death.model = compound.getByte("Model");

        return death;
    }

    public CompoundNBT toNBT() {
        return toNBT(true);
    }

    public CompoundNBT toNBT(boolean withItems) {
        CompoundNBT compound = new CompoundNBT();
        //TODO replace with put UUID
        compound.putLong("IdMost", id.getMostSignificantBits());
        compound.putLong("IdLeast", id.getLeastSignificantBits());
        compound.putLong("PlayerUuidMost", playerUUID.getMostSignificantBits());
        compound.putLong("PlayerUuidLeast", playerUUID.getLeastSignificantBits());
        compound.putString("PlayerName", playerName);

        if (withItems) {
            ItemUtils.saveInventory(compound, "MainInventory", mainInventory);
            ItemUtils.saveInventory(compound, "ArmorInventory", armorInventory);
            ItemUtils.saveInventory(compound, "OffHandInventory", offHandInventory);
            ItemUtils.saveItemList(compound, "Items", additionalItems);
        }

        ItemUtils.saveItemList(compound, "Equipment", equipment);

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

        public Builder equipment(NonNullList<ItemStack> list) {
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
