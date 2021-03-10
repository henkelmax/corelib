package de.maxhenkel.corelib.fluid;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public class FluidUtils {

    /**
     * Returns if there is a fluid handler at this position and direction
     *
     * @param world     the world
     * @param pos       the position
     * @param direction the direction
     * @return if there is a fluid handler at this position and direction
     */
    public static boolean isFluidHandler(IBlockReader world, BlockPos pos, Direction direction) {
        return getFluidHandler(world, pos, direction) != null;
    }

    /**
     * Returns if there is a fluid handler offset to the specified direction
     *
     * @param world     the world
     * @param pos       the position
     * @param direction the direction
     * @return if there is a fluid handler offset to the specified direction
     */
    public static boolean isFluidHandlerOffset(IBlockReader world, BlockPos pos, Direction direction) {
        return getFluidHandlerOffset(world, pos, direction) != null;
    }

    /**
     * Gets the fluid handler at the provided location and direction
     *
     * @param world     the world
     * @param pos       the position
     * @param direction the direction
     * @return the fluid handler
     */
    @Nullable
    public static IFluidHandler getFluidHandler(IBlockReader world, BlockPos pos, Direction direction) {
        TileEntity te = world.getBlockEntity(pos);
        if (te == null) {
            return null;
        }
        return te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).orElse(null);
    }

    /**
     * Gets the fluid handler offset to the specified direction
     *
     * @param world     the world
     * @param pos       the position
     * @param direction the direction
     * @return the fluid handler
     */
    @Nullable
    public static IFluidHandler getFluidHandlerOffset(IBlockReader world, BlockPos pos, Direction direction) {
        return getFluidHandler(world, pos.relative(direction), direction.getOpposite());
    }

    /**
     * Transfers fluid from the specified source to the specified destination
     *
     * @param fluidDestination the destination
     * @param fluidSource      the source
     * @param maxAmount        the maximum amount to transfer
     * @param doTransfer       false to simulate
     * @param filter           the fluid that should get transferred
     * @return the transferred fluid stack
     */
    public static FluidStack tryFluidTransfer(IFluidHandler fluidDestination, IFluidHandler fluidSource, int maxAmount, boolean doTransfer, @Nullable Fluid filter) {
        FluidStack drainable;
        if (filter == null) {
            drainable = fluidSource.drain(maxAmount, IFluidHandler.FluidAction.SIMULATE);
        } else {
            drainable = fluidSource.drain(new FluidStack(filter, maxAmount), IFluidHandler.FluidAction.SIMULATE);
        }
        if (drainable.getAmount() > 0) {
            int fillableAmount = fluidDestination.fill(drainable, IFluidHandler.FluidAction.SIMULATE);
            if (fillableAmount > 0) {
                if (doTransfer) {
                    FluidStack drained;
                    if (filter == null) {
                        drained = fluidSource.drain(fillableAmount, IFluidHandler.FluidAction.EXECUTE);
                    } else {
                        drained = fluidSource.drain(new FluidStack(filter, fillableAmount), IFluidHandler.FluidAction.EXECUTE);
                    }
                    drained.setAmount(fluidDestination.fill(drained, IFluidHandler.FluidAction.EXECUTE));
                    return drained;
                } else {
                    drainable.setAmount(fillableAmount);
                    return drainable;
                }
            }
        }
        return FluidStack.EMPTY;
    }

    /**
     * Transfers fluid from the specified source to the specified destination
     *
     * @param fluidDestination the destination
     * @param fluidSource      the source
     * @param maxAmount        the maximum amount to transfer
     * @param doTransfer       false to simulate
     * @return the transferred fluid stack
     */
    public static FluidStack tryFluidTransfer(IFluidHandler fluidDestination, IFluidHandler fluidSource, int maxAmount, boolean doTransfer) {
        return FluidUtil.tryFluidTransfer(fluidDestination, fluidSource, maxAmount, doTransfer);
    }

    /**
     * Tries to fill or empty the held item of the player
     *
     * @param player the player
     * @param hand   the hand
     * @param world  the world
     * @param pos    the position of the fluid handler
     * @return if it was successful
     */
    public static boolean tryFluidInteraction(PlayerEntity player, Hand hand, World world, BlockPos pos) {
        ItemStack stack = player.getItemInHand(hand);

        FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);

        if (!fluidStack.isEmpty()) {
            if (handleEmpty(world, pos, player, hand)) {
                return true;
            }
        }
        IFluidHandler handler = FluidUtil.getFluidHandler(stack).orElse(null);

        if (handler != null) {
            if (handleFill(world, pos, player, hand)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Empties the held item to the fluid handler at the provided position
     *
     * @param world  the world
     * @param pos    the position of the fluid handler
     * @param player the player
     * @param hand   the hand
     * @return if it was successful
     */
    public static boolean handleEmpty(IBlockReader world, BlockPos pos, PlayerEntity player, Hand hand) {
        TileEntity te = world.getBlockEntity(pos);

        if (!(te instanceof IFluidHandler)) {
            return false;
        }

        IFluidHandler handler = (IFluidHandler) te;

        IItemHandler inv = new InvWrapper(player.inventory);

        ItemStack stack = player.getItemInHand(hand);

        FluidActionResult res = FluidUtil.tryEmptyContainerAndStow(stack, handler, inv, Integer.MAX_VALUE, player, true);

        if (res.isSuccess()) {
            player.setItemInHand(hand, res.result);
            return true;
        }

        return false;
    }

    /**
     * Fills the held item from the fluid handler at the provided position
     *
     * @param world  the world
     * @param pos    the position of the fluid handler
     * @param player the player
     * @param hand   the hand
     * @return if it was successful
     */
    public static boolean handleFill(IBlockReader world, BlockPos pos, PlayerEntity player, Hand hand) {
        TileEntity te = world.getBlockEntity(pos);

        if (!(te instanceof IFluidHandler)) {
            return false;
        }

        IFluidHandler blockHandler = (IFluidHandler) te;

        IItemHandler inv = new InvWrapper(player.inventory);

        ItemStack stack = player.getItemInHand(hand);

        FluidActionResult result = FluidUtil.tryFillContainerAndStow(stack, blockHandler, inv, Integer.MAX_VALUE, player, true);

        if (result.isSuccess()) {
            player.setItemInHand(hand, result.result);
            return true;
        }

        return false;
    }

}
