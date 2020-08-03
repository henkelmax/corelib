package de.maxhenkel.corelib.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.function.Function;

public class ItemListInventory implements IInventory {

    protected NonNullList<ItemStack> items;
    private Runnable onMarkDirty;
    private Function<PlayerEntity, Boolean> onIsUsableByPlayer;

    public ItemListInventory(NonNullList<ItemStack> items, Runnable onMarkDirty, Function<PlayerEntity, Boolean> onIsUsableByPlayer) {
        this.items = items;
        this.onMarkDirty = onMarkDirty;
        this.onIsUsableByPlayer = onIsUsableByPlayer;
    }

    public ItemListInventory(NonNullList<ItemStack> items, Runnable onMarkDirty) {
        this(items, onMarkDirty, null);
    }

    public ItemListInventory(NonNullList<ItemStack> items) {
        this(items, null);
    }

    @Override
    public int getSizeInventory() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(items, index, count);
        if (!itemstack.isEmpty()) {
            markDirty();
        }
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        items.set(index, stack);
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
        items.clear();
    }

}
