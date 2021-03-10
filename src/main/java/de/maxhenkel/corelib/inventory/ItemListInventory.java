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
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack itemstack = ItemStackHelper.removeItem(items, index, count);
        if (!itemstack.isEmpty()) {
            setChanged();
        }
        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStackHelper.takeItem(items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public void setChanged() {
        if (onMarkDirty != null) {
            onMarkDirty.run();
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        if (onIsUsableByPlayer != null) {
            return onIsUsableByPlayer.apply(player);
        } else {
            return true;
        }
    }

    @Override
    public void clearContent() {
        items.clear();
    }

}
