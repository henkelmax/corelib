package de.maxhenkel.corelib.item;

import de.maxhenkel.corelib.codec.CodecUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

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
     * @param valueOutput the value output
     * @param name        the name of the tag list in the compound
     * @param inv         the inventory
     */
    public static void saveInventory(ValueOutput valueOutput, String name, Container inv) {
        ValueOutput.TypedOutputList<ItemStackWithSlot> list = valueOutput.list(name, ItemStackWithSlot.CODEC);
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack itemstack = inv.getItem(i);
            if (!itemstack.isEmpty()) {
                list.add(new ItemStackWithSlot(i, itemstack));
            }
        }
    }

    /**
     * Stores the provided item list to the provided compound under the given name
     *
     * @param valueOutput the value output
     * @param name        the name of the tag list in the compound
     * @param inv         the item list
     */
    public static void saveInventory(ValueOutput valueOutput, String name, NonNullList<ItemStack> inv) {
        ValueOutput.TypedOutputList<ItemStackWithSlot> list = valueOutput.list(name, ItemStackWithSlot.CODEC);
        for (int i = 0; i < inv.size(); i++) {
            ItemStack itemstack = inv.get(i);
            if (!itemstack.isEmpty()) {
                list.add(new ItemStackWithSlot(i, itemstack));
            }
        }
    }

    /**
     * Stores the provided item list to the provided compound under the given name
     *
     * @param valueOutput the value output
     * @param name        the name of the tag list in the compound
     * @param list        the item list
     */
    public static void saveItemList(ValueOutput valueOutput, String name, NonNullList<ItemStack> list) {
        ValueOutput.TypedOutputList<ItemStack> out = valueOutput.list(name, ItemStack.CODEC);
        for (ItemStack stack : list) {
            if (stack.isEmpty()) {
                continue;
            }
            out.add(stack);
        }
    }

    /**
     * Loads the provided compound to the provided inventory
     * Does not clear inventory - empty stacks will not overwrite existing items
     *
     * @param valueInput the value input
     * @param name       the name of the tag list in the compound
     * @param inv        the inventory
     */
    public static void readInventory(ValueInput valueInput, String name, Container inv) {
        for (ItemStackWithSlot stack : valueInput.listOrEmpty(name, ItemStackWithSlot.CODEC)) {
            if (stack.isValidInContainer(inv.getContainerSize())) {
                inv.setItem(stack.slot(), stack.stack());
            }
        }
    }

    /**
     * Loads the provided compound to the provided item list
     * Does not clear the list - empty stacks will not overwrite existing items
     *
     * @param valueInput the value input
     * @param name       the name of the tag list in the compound
     * @param inv        the item list
     */
    public static void readInventory(ValueInput valueInput, String name, NonNullList<ItemStack> inv) {
        for (ItemStackWithSlot stack : valueInput.listOrEmpty(name, ItemStackWithSlot.CODEC)) {
            if (stack.isValidInContainer(inv.size())) {
                inv.set(stack.slot(), stack.stack());
            }
        }
    }

    /**
     * Loads the provided compound to the provided item list
     *
     * @param valueInput   the value input
     * @param name         the name of the tag list in the compound
     * @param includeEmpty if empty stacks should be included
     * @return the item list
     */
    public static NonNullList<ItemStack> readItemList(ValueInput valueInput, String name, boolean includeEmpty) {
        NonNullList<ItemStack> items = NonNullList.create();
        for (ItemStack stack : valueInput.listOrEmpty(name, ItemStack.CODEC)) {
            items.add(stack);
        }
        return items;
    }

    /**
     * Loads the provided compound to the provided item list
     * Includes empty items
     *
     * @param valueInput the value input
     * @param name       the name of the tag list in the compound
     * @return the item list
     */
    public static NonNullList<ItemStack> readItemList(ValueInput valueInput, String name) {
        return readItemList(valueInput, name, true);
    }

    /**
     * Reads the compound into the item list
     * Only fills the list to its maximum capacity
     *
     * @param valueInput the value input
     * @param name       the name of the tag list in the compound
     * @param list       the item list
     */
    public static void readItemList(ValueInput valueInput, String name, NonNullList<ItemStack> list) {
        List<ItemStack> itemList = valueInput.listOrEmpty(name, ItemStack.CODEC).stream().toList();
        for (int i = 0; i < itemList.size(); i++) {
            if (i >= list.size()) {
                break;
            }
            list.set(i, itemList.get(i));
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
     * @param compound the compound to store the stack in
     * @param stack    the stack to store
     * @return the provided compound
     */
    public static CompoundTag writeOverstackedItem(CompoundTag compound, ItemStack stack) {
        CompoundTag itemStackTag = CodecUtils.toNBT(ItemStack.CODEC, stack).filter(CompoundTag.class::isInstance).map(CompoundTag.class::cast).orElseGet(CompoundTag::new);
        compound.merge(itemStackTag);
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
    public static ItemStack readOverstackedItem(CompoundTag compound) {
        CompoundTag data = compound.copy();
        int count = data.getIntOr("Count", 0);
        data.remove("Count");
        data.putByte("Count", (byte) 1);
        ItemStack stack = CodecUtils.fromNBT(ItemStack.CODEC, data).orElse(ItemStack.EMPTY);
        stack.setCount(count);
        return stack;
    }
}