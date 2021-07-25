package de.maxhenkel.corelib.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class RestrictedItemStackHandler extends ItemStackHandler {

    private ItemValidator itemValidator;

    public RestrictedItemStackHandler(NonNullList<ItemStack> stacks, ItemValidator itemValidator) {
        super(stacks);
        this.itemValidator = itemValidator;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return itemValidator.isValid(slot, stack);
    }

    public interface ItemValidator {
        boolean isValid(int slot, ItemStack stack);
    }

}
