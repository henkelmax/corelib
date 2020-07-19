package de.maxhenkel.corelib.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class EnergyUtils {

    /**
     * Pushes energy between two energy storages
     *
     * @param provider  the energy provider
     * @param receiver  the energy receiver
     * @param maxAmount the maximum amount to push
     * @return the amount that actually got transferred
     */
    public static int pushEnergy(IEnergyStorage provider, IEnergyStorage receiver, int maxAmount) {
        int energySim = provider.extractEnergy(maxAmount, true);

        int receivedSim = receiver.receiveEnergy(energySim, true);

        int energy = provider.extractEnergy(receivedSim, false);

        receiver.receiveEnergy(energy, false);
        return energy;
    }

    /**
     * Gets the energy storage at the specified position and direction
     *
     * @param world the world
     * @param pos   the position
     * @param side  the side
     * @return the energy storage
     */
    @Nullable
    public static IEnergyStorage getEnergyStorage(IWorldReader world, BlockPos pos, Direction side) {
        TileEntity te = world.getTileEntity(pos);

        if (te == null) {
            return null;
        }

        return te.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).orElse(null);
    }

    /**
     * Gets the energy storage at the specified position offset one block to the specified direction
     *
     * @param world the world
     * @param pos   the position
     * @param side  the side
     * @return the energy storage
     */
    @Nullable
    public static IEnergyStorage getEnergyStorageOffset(IWorldReader world, BlockPos pos, Direction side) {
        return getEnergyStorage(world, pos.offset(side), side.getOpposite());
    }

}
