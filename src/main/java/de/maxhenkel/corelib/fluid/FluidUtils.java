package de.maxhenkel.corelib.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import javax.annotation.Nullable;

@Deprecated(forRemoval = true)
public class FluidUtils {

    /**
     * Returns if there is a fluid handler at this position and direction
     *
     * @param world     the world
     * @param pos       the position
     * @param direction the direction
     * @return if there is a fluid handler at this position and direction
     */
    public static boolean isFluidHandler(Level world, BlockPos pos, Direction direction) {
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
    public static boolean isFluidHandlerOffset(Level world, BlockPos pos, Direction direction) {
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
    public static ResourceHandler<FluidResource> getFluidHandler(Level world, BlockPos pos, Direction direction) {
        return world.getCapability(Capabilities.Fluid.BLOCK, pos, direction);
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
    public static ResourceHandler<FluidResource> getFluidHandlerOffset(Level world, BlockPos pos, Direction direction) {
        return getFluidHandler(world, pos.relative(direction), direction.getOpposite());
    }

}
