package de.maxhenkel.corelib.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;

public class ItemUtils {

    /**
     * Changes the item stack amount. If a player is provided and the player is in Creative Mode, the stack wont be changed
     *
     * @param amount the amount to change
     * @param stack  the Item Stack
     * @param player the player (Can be null)
     * @return the resulting stack
     */
    public static ItemStack itemStackAmount(int amount, ItemStack stack, PlayerEntity player) {
        if (stack == null || stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (player != null && player.abilities.isCreativeMode) {
            return stack;
        }

        stack.setCount(stack.getCount() + amount);
        if (stack.getCount() <= 0) {
            stack.setCount(0);
            return ItemStack.EMPTY;
        }

        if (stack.getCount() > stack.getMaxStackSize()) {
            stack.setCount(stack.getMaxStackSize());
        }

        return stack;
    }

    public static ItemStack decrItemStack(ItemStack stack, PlayerEntity player) {
        return itemStackAmount(-1, stack, player);
    }

    public static ItemStack incrItemStack(ItemStack stack, PlayerEntity player) {
        return itemStackAmount(1, stack, player);
    }

    /**
     * Compares two stacks except its NBT data
     *
     * @param stack1 the first stack
     * @param stack2 the second stack
     * @return if the two stacks are equal
     */
    public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null || stack2 == null) {
            return false;
        }
        if (stack1.getItem() == stack2.getItem()) {
            return stack1.getDamage() == stack2.getDamage();
        }
        return false;
    }

    /**
     * Stores the provided inventory to the provided compound under the given name
     *
     * @param compound the compound to save the inventory to
     * @param name     the name of the tag list in the compound
     * @param inv      the inventory
     */
    public static void saveInventory(CompoundNBT compound, String name, IInventory inv) {
        ListNBT tagList = new ListNBT();

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (!inv.getStackInSlot(i).isEmpty()) {
                CompoundNBT slot = new CompoundNBT();
                slot.putInt("Slot", i);
                inv.getStackInSlot(i).write(slot);
                tagList.add(slot);
            }
        }

        compound.put(name, tagList);
    }

    /**
     * Stores the provided item list to the provided compound under the given name
     *
     * @param compound the compound to save the inventory to
     * @param name     the name of the tag list in the compound
     * @param inv      the item list
     */
    public static void saveInventory(CompoundNBT compound, String name, NonNullList<ItemStack> inv) {
        ListNBT tagList = new ListNBT();
        for (int i = 0; i < inv.size(); i++) {
            if (!inv.get(i).isEmpty()) {
                CompoundNBT slot = new CompoundNBT();
                slot.putInt("Slot", i);
                inv.get(i).write(slot);
                tagList.add(slot);
            }
        }

        compound.put(name, tagList);
    }

    /**
     * Stores the provided item list to the provided compound under the given name
     * Stores empty items
     *
     * @param compound the compound to save the inventory to
     * @param name     the name of the tag list in the compound
     * @param list     the item list
     */
    public static void saveItemList(CompoundNBT compound, String name, NonNullList<ItemStack> list) {
        ListNBT itemList = new ListNBT();
        for (ItemStack stack : list) {
            itemList.add(stack.write(new CompoundNBT()));
        }
        compound.put(name, itemList);
    }

    /**
     * Loads the provided compound to the provided inventory
     * Does not clear inventory - empty stacks will not overwrite existing items
     *
     * @param compound the compound to read the inventory from
     * @param name     the name of the tag list in the compound
     * @param inv      the inventory
     */
    public static void readInventory(CompoundNBT compound, String name, IInventory inv) {
        if (!compound.contains(name)) {
            return;
        }

        ListNBT tagList = compound.getList(name, 10);

        for (int i = 0; i < tagList.size(); i++) {
            CompoundNBT slot = tagList.getCompound(i);
            int j = slot.getInt("Slot");

            if (j >= 0 && j < inv.getSizeInventory()) {
                inv.setInventorySlotContents(j, ItemStack.read(slot));
            }
        }
    }

    /**
     * Loads the provided compound to the provided item list
     * Does not clear the list - empty stacks will not overwrite existing items
     *
     * @param compound the compound to read the inventory from
     * @param name     the name of the tag list in the compound
     * @param inv      the item list
     */
    public static void readInventory(CompoundNBT compound, String name, NonNullList<ItemStack> inv) {
        if (!compound.contains(name)) {
            return;
        }

        ListNBT tagList = compound.getList(name, 10);

        for (int i = 0; i < tagList.size(); i++) {
            CompoundNBT slot = tagList.getCompound(i);
            int j = slot.getInt("Slot");

            if (j >= 0 && j < inv.size()) {
                inv.set(j, ItemStack.read(slot));
            }
        }
    }

    /**
     * Loads the provided compound to the provided item list
     *
     * @param compound the compound to read the item list from
     * @param name     the name of the tag list in the compound
     * @return the item list
     */
    public static NonNullList<ItemStack> readItemList(CompoundNBT compound, String name) {
        NonNullList<ItemStack> items = NonNullList.create();
        if (!compound.contains(name)) {
            return items;
        }

        ListNBT itemList = compound.getList(name, 10);
        for (int i = 0; i < itemList.size(); i++) {
            items.add(ItemStack.read(itemList.getCompound(i)));
        }
        return items;
    }

    /**
     * Reads the compound into the item list
     * Only fills the list to its maximum capacity
     *
     * @param compound the compound to read the item list from
     * @param name     the name of the tag list in the compound
     * @param list     the item list
     */
    public static void readItemList(CompoundNBT compound, String name, NonNullList<ItemStack> list) {
        if (!compound.contains(name)) {
            return;
        }

        ListNBT itemList = compound.getList(name, 10);
        for (int i = 0; i < itemList.size(); i++) {
            if (i >= list.size()) {
                break;
            }
            list.set(i, ItemStack.read(itemList.getCompound(i)));
        }
    }

    /**
     * Removes the provided slot from the provided inventory
     *
     * @param inventory the inventory
     * @param index     the slot index
     */
    public static void removeStackFromSlot(IInventory inventory, int index) {
        inventory.setInventorySlotContents(index, ItemStack.EMPTY);
    }

    /**
     * Returns if the provided stacks are stackable on each other
     *
     * @param stack1 the first stack
     * @param stack2 the second stack
     * @return if the provided stacks are stackable on each other
     */
    public static boolean isStackable(ItemStack stack1, ItemStack stack2) {
        return stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2) && stack1.getDamage() == stack2.getDamage();
    }

    /**
     * Writes the provided item stack to the provided NBT compound.
     * Ignores the maximum stack size of the stack.
     * Can stack up to the integer limit.
     *
     * @param compound the compound to store the stack in
     * @param stack    the stack to store
     * @return the provided compound
     */
    public static CompoundNBT writeOverstackedItem(CompoundNBT compound, ItemStack stack) {
        stack.write(compound);
        compound.remove("Count");
        compound.putInt("Count", stack.getCount());
        return compound;
    }

    /**
     * Reads the provided item stack from the provided NBT compound.
     * Ignores the maximum stack size of the stack.
     * Can stack up to the integer limit.
     *
     * @param compound the compound to store the stack in
     * @return the deserialized stack
     */
    public static ItemStack readOverstackedItem(CompoundNBT compound) {
        CompoundNBT data = compound.copy();
        int count = data.getInt("Count");
        data.remove("Count");
        data.putByte("Count", (byte) 1);
        ItemStack stack = ItemStack.read(data);
        stack.setCount(count);
        return stack;
    }
}