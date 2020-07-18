package de.maxhenkel.corelib.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ItemTools {

    /**
     * Changes the item stack amount. If a player is provided and the player is in Creative Mode, the stack wont be changed
     *
     * @param amount the amount to change
     * @param stack  the Item Stack
     * @param player the player (Can be null)
     * @return the amount left
     */
    public static int itemStackAmount(int amount, ItemStack stack, PlayerEntity player) {
        if (stack == null || stack.isEmpty()) {
            return 0;
        }

        if (player == null || !player.abilities.isCreativeMode) {
            stack.setCount(stack.getCount() + amount);
            if (stack.getCount() <= 0) {
                stack.setCount(0);
            }
        }

        if (stack.getCount() > stack.getMaxStackSize()) {
            stack.setCount(stack.getMaxStackSize());
        }

        return stack.getCount();
    }

}
