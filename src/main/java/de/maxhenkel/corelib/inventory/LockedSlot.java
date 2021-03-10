package de.maxhenkel.corelib.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class LockedSlot extends Slot {

    protected boolean inputLocked;
    protected boolean outputLocked;

    public LockedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, boolean inputLocked, boolean outputLocked) {
        super(inventoryIn, index, xPosition, yPosition);
        this.inputLocked = inputLocked;
        this.outputLocked = outputLocked;
    }

    public LockedSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        this(inventoryIn, index, xPosition, yPosition, false, true);
    }

    @Override
    public boolean mayPickup(PlayerEntity playerIn) {
        if (outputLocked) {
            return false;
        } else {
            return super.mayPickup(playerIn);
        }
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (inputLocked) {
            return false;
        } else {
            return super.mayPlace(stack);
        }
    }

}