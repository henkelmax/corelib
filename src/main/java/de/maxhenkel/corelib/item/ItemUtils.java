package de.maxhenkel.corelib.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ItemUtils {

    //TODO Re-add
    /**
     * Does not compare compound entries, just if they are the same hashcode
     */
    /*public static final Comparator<ItemStack> ITEM_COMPARATOR = (item1, item2) -> {
        int cmp = item2.getItem().hashCode() - item1.getItem().hashCode();
        if (cmp != 0) {
            return cmp;
        }
        cmp = item2.getDamageValue() - item1.getDamageValue();
        if (cmp != 0) {
            return cmp;
        }
        CompoundTag c1 = item1.getTag();
        CompoundTag c2 = item2.getTag();

        if (c1 == null && c2 == null) {
            return 0;
        } else if (c1 == null) {
            return 1;
        } else if (c2 == null) {
            return -1;
        }

        return c1.hashCode() - c2.hashCode();
    };*/

    /**
     * Changes the item stack amount. If a player is provided and the player is in Creative Mode, the stack wont be changed
     *
     * @param amount the amount to change
     * @param stack  the Item Stack
     * @param player the player (Can be null)
     * @return the resulting stack
     */
    public static ItemStack itemStackAmount(int amount, ItemStack stack, Player player) {
        if (stack == null || stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        if (player != null && player.getAbilities().instabuild) {
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

    public static ItemStack decrItemStack(ItemStack stack, Player player) {
        return itemStackAmount(-1, stack, player);
    }

    public static ItemStack incrItemStack(ItemStack stack, Player player) {
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
            return stack1.getDamageValue() == stack2.getDamageValue();
        }
        return false;
    }

    /**
     * Stores the provided inventory to the provided compound under the given name
     *
     * @param provider the provider
     * @param compound the compound to save the inventory to
     * @param name     the name of the tag list in the compound
     * @param inv      the inventory
     */
    public static void saveInventory(HolderLookup.Provider provider, CompoundTag compound, String name, Container inv) {
        ListTag tagList = new ListTag();

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (!item.isEmpty()) {
                CompoundTag slot = new CompoundTag();
                slot.putInt("Slot", i);
                tagList.add(item.save(provider, slot));
            }
        }

        compound.put(name, tagList);
    }

    /**
     * Stores the provided item list to the provided compound under the given name
     *
     * @param provider the provider
     * @param compound the compound to save the inventory to
     * @param name     the name of the tag list in the compound
     * @param inv      the item list
     */
    public static void saveInventory(HolderLookup.Provider provider, CompoundTag compound, String name, NonNullList<ItemStack> inv) {
        ListTag tagList = new ListTag();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack item = inv.get(i);
            if (!item.isEmpty()) {
                CompoundTag slot = new CompoundTag();
                slot.putInt("Slot", i);
                tagList.add(item.save(provider, slot));
            }
        }

        compound.put(name, tagList);
    }

    /**
     * Stores the provided item list to the provided compound under the given name
     *
     * @param provider the provider
     * @param compound the compound to save the inventory to
     * @param name     the name of the tag list in the compound
     * @param list     the item list
     */
    public static void saveItemList(HolderLookup.Provider provider, CompoundTag compound, String name, NonNullList<ItemStack> list) {
        ListTag itemList = new ListTag();
        for (ItemStack stack : list) {
            if (stack.isEmpty()) {
                continue;
            }
            itemList.add(stack.save(provider));
        }
        compound.put(name, itemList);
    }

    /**
     * Loads the provided compound to the provided inventory
     * Does not clear inventory - empty stacks will not overwrite existing items
     *
     * @param provider the provider
     * @param compound the compound to read the inventory from
     * @param name     the name of the tag list in the compound
     * @param inv      the inventory
     */
    public static void readInventory(HolderLookup.Provider provider, CompoundTag compound, String name, Container inv) {
        if (!compound.contains(name)) {
            return;
        }

        ListTag tagList = compound.getListOrEmpty(name);

        for (int i = 0; i < tagList.size(); i++) {
            Optional<CompoundTag> slot = tagList.getCompound(i);
            if (slot.isEmpty()) {
                continue;
            }
            Optional<Integer> slotId = slot.get().getInt("Slot");
            if (slotId.isEmpty()) {
                continue;
            }
            int j = slotId.get();

            if (j >= 0 && j < inv.getContainerSize()) {
                inv.setItem(j, ItemStack.parse(provider, slot.get()).orElse(ItemStack.EMPTY));
            }
        }
    }

    /**
     * Loads the provided compound to the provided item list
     * Does not clear the list - empty stacks will not overwrite existing items
     *
     * @param provider the provider
     * @param compound the compound to read the inventory from
     * @param name     the name of the tag list in the compound
     * @param inv      the item list
     */
    public static void readInventory(HolderLookup.Provider provider, CompoundTag compound, String name, NonNullList<ItemStack> inv) {
        if (!compound.contains(name)) {
            return;
        }

        ListTag tagList = compound.getListOrEmpty(name);

        for (int i = 0; i < tagList.size(); i++) {
            Optional<CompoundTag> slot = tagList.getCompound(i);
            if (slot.isEmpty()) {
                continue;
            }
            Optional<Integer> slotId = slot.get().getInt("Slot");
            if (slotId.isEmpty()) {
                continue;
            }
            int j = slotId.get();

            if (j >= 0 && j < inv.size()) {
                inv.set(j, ItemStack.parse(provider, slot.get()).orElse(ItemStack.EMPTY));
            }
        }
    }

    /**
     * Loads the provided compound to the provided item list
     *
     * @param provider     the provider
     * @param compound     the compound to read the item list from
     * @param name         the name of the tag list in the compound
     * @param includeEmpty if empty stacks should be included
     * @return the item list
     */
    public static NonNullList<ItemStack> readItemList(HolderLookup.Provider provider, CompoundTag compound, String name, boolean includeEmpty) {
        NonNullList<ItemStack> items = NonNullList.create();
        if (!compound.contains(name)) {
            return items;
        }

        ListTag itemList = compound.getListOrEmpty(name);
        for (int i = 0; i < itemList.size(); i++) {
            Optional<ItemStack> optionalItem = ItemStack.parse(provider, itemList.get(i));
            if (optionalItem.isEmpty()) {
                continue;
            }
            ItemStack item = optionalItem.get();
            if (!includeEmpty) {
                if (!item.isEmpty()) {
                    items.add(item);
                }
            } else {
                items.add(item);
            }
        }
        return items;
    }

    /**
     * Loads the provided compound to the provided item list
     * Includes empty items
     *
     * @param provider the provider
     * @param compound the compound to read the item list from
     * @param name     the name of the tag list in the compound
     * @return the item list
     */
    public static NonNullList<ItemStack> readItemList(HolderLookup.Provider provider, CompoundTag compound, String name) {
        return readItemList(provider, compound, name, true);
    }

    /**
     * Reads the compound into the item list
     * Only fills the list to its maximum capacity
     *
     * @param provider the provider
     * @param compound the compound to read the item list from
     * @param name     the name of the tag list in the compound
     * @param list     the item list
     */
    public static void readItemList(HolderLookup.Provider provider, CompoundTag compound, String name, NonNullList<ItemStack> list) {
        if (!compound.contains(name)) {
            return;
        }

        ListTag itemList = compound.getListOrEmpty(name);
        for (int i = 0; i < itemList.size(); i++) {
            if (i >= list.size()) {
                break;
            }
            list.set(i, ItemStack.parse(provider, itemList.get(i)).orElse(ItemStack.EMPTY));
        }
    }

    /**
     * Removes the provided slot from the provided inventory
     *
     * @param inventory the inventory
     * @param index     the slot index
     */
    public static void removeStackFromSlot(Container inventory, int index) {
        inventory.setItem(index, ItemStack.EMPTY);
    }

    /**
     * Returns if the provided stacks are stackable on each other
     *
     * @param stack1 the first stack
     * @param stack2 the second stack
     * @return if the provided stacks are stackable on each other
     */
    public static boolean isStackable(ItemStack stack1, ItemStack stack2) {
        return ItemStack.isSameItemSameComponents(stack1, stack2);
    }

    /**
     * Writes the provided item stack to the provided NBT compound.
     * Ignores the maximum stack size of the stack.
     * Can stack up to the integer limit.
     *
     * @param provider the provider
     * @param compound the compound to store the stack in
     * @param stack    the stack to store
     * @return the provided compound
     */
    public static CompoundTag writeOverstackedItem(HolderLookup.Provider provider, CompoundTag compound, ItemStack stack) {
        stack.save(provider, compound);
        compound.remove("Count");
        compound.putInt("Count", stack.getCount());
        return compound;
    }

    /**
     * Reads the provided item stack from the provided NBT compound.
     * Ignores the maximum stack size of the stack.
     * Can stack up to the integer limit.
     *
     * @param provider the provider
     * @param compound the compound to store the stack in
     * @return the deserialized stack
     */
    public static ItemStack readOverstackedItem(HolderLookup.Provider provider, CompoundTag compound) {
        CompoundTag data = compound.copy();
        int count = data.getIntOr("Count", 0);
        data.remove("Count");
        data.putByte("Count", (byte) 1);
        ItemStack stack = ItemStack.parse(provider, data).orElse(ItemStack.EMPTY);
        stack.setCount(count);
        return stack;
    }
}