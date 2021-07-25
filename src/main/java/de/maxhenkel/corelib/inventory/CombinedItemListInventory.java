package de.maxhenkel.corelib.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.function.Function;

/**
 * This class is not yet finished - use at own risk
 */
@Deprecated
public class CombinedItemListInventory implements Container {

    protected NonNullList<ItemStack>[] items;
    private Runnable onMarkDirty;
    private Function<Player, Boolean> onIsUsableByPlayer;

    public CombinedItemListInventory(Runnable onMarkDirty, Function<Player, Boolean> onIsUsableByPlayer, NonNullList<ItemStack>... items) {
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
    public int getContainerSize() {
        return Arrays.stream(items).map(NonNullList::size).reduce(0, Integer::sum);
    }

    @Override
    public boolean isEmpty() {
        return Arrays.stream(items).allMatch(itemStacks -> itemStacks.stream().allMatch(ItemStack::isEmpty));
    }

    @Override
    public ItemStack getItem(int index) {
        return items[getListIndex(index)].get(getLocalIndex(index));
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack itemstack = ContainerHelper.removeItem(items[getListIndex(index)], getLocalIndex(index), count);
        if (!itemstack.isEmpty()) {
            setChanged();
        }
        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(items[getListIndex(index)], getLocalIndex(index));
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        items[getListIndex(index)].set(getLocalIndex(index), stack);
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
    public boolean stillValid(Player player) {
        if (onIsUsableByPlayer != null) {
            return onIsUsableByPlayer.apply(player);
        } else {
            return true;
        }
    }

    @Override
    public void clearContent() {
        Arrays.stream(items).forEach(NonNullList::clear);
    }

}
