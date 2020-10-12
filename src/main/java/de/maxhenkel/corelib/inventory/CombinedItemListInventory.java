package de.maxhenkel.corelib.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Arrays;
import java.util.function.Function;

/**
 * This class is not yet finished - use at own risk
 */
@Deprecated
public class CombinedItemListInventory implements IInventory {

    protected NonNullList<ItemStack>[] items;
    private Runnable onMarkDirty;
    private Function<PlayerEntity, Boolean> onIsUsableByPlayer;

    public CombinedItemListInventory(Runnable onMarkDirty, Function<PlayerEntity, Boolean> onIsUsableByPlayer, NonNullList<ItemStack>... items) {
        this.items = items;
        this.onMarkDirty = onMarkDirty;
        this.onIsUsableByPlayer = onIsUsableByPlayer;
    }

    public CombinedItemListInventory(Runnable onMarkDirty, NonNullList<ItemStack>... items) {
        this(onMarkDirty, null, items);
    }

    public CombinedItemListInventory(NonNullList<ItemStack>... items) {
        this(null, items);
    }

    private int getListIndex(int slot) {
        int index = 0;
        for (int i = 0; i < items.length; i++) {
            NonNullList<ItemStack> list = items[i];
            if (slot >= index && slot < index + list.size()) {
                return i;
            }
            index += list.size();
        }
        return -1;
    }

    private int getLocalIndex(int slot) {
        int index = 0;
        for (int i = 0; i < items.length; i++) {
            NonNullList<ItemStack> list = items[i];
            if (slot >= index && slot < index + list.size()) {
                return slot - index;
            }
            index += list.size();
        }
        return -1;
    }

    @Override
    public int getSizeInventory() {
        return Arrays.stream(items).map(NonNullList::size).reduce(0, Integer::sum);
    }

    @Override
    public boolean isEmpty() {
        return Arrays.stream(items).allMatch(itemStacks -> itemStacks.stream().allMatch(ItemStack::isEmpty));
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return items[getListIndex(index)].get(getLocalIndex(index));
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(items[getListIndex(index)], getLocalIndex(index), count);
        if (!itemstack.isEmpty()) {
            markDirty();
        }
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(items[getListIndex(index)], getLocalIndex(index));
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        items[getListIndex(index)].set(getLocalIndex(index), stack);
        if (stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }
        markDirty();
    }

    @Override
    public void markDirty() {
        if (onMarkDirty != null) {
            onMarkDirty.run();
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (onIsUsableByPlayer != null) {
            return onIsUsableByPlayer.apply(player);
        } else {
            return true;
        }
    }

    @Override
    public void clear() {
        Arrays.stream(items).forEach(NonNullList::clear);
    }

}
