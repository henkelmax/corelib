package de.maxhenkel.corelib.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import javax.annotation.Nullable;

@Deprecated(forRemoval = true)
public class EnergyUtils {

    /**
     * Pushes energy between two energy storages
     *
     * @param provider  the energy provider
     * @param receiver  the energy receiver
     * @param maxAmount the maximum amount to push
     * @return the amount that actually got transferred
     */
    public static int pushEnergy(EnergyHandler provider, EnergyHandler receiver, int maxAmount, @Nullable TransactionContext transaction) {
        int energySim;
        int receivedSim;
        try (Transaction t = Transaction.open(transaction)) {
            energySim = provider.extract(maxAmount, t);
            receivedSim = receiver.insert(energySim, t);
        }
        int energy;
        try (Transaction t = Transaction.open(transaction)) {
            energy = provider.extract(receivedSim, t);
            receiver.insert(energy, t);
            t.commit();
        }
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
    public static EnergyHandler getEnergyStorage(Level world, BlockPos pos, Direction side) {
        return world.getCapability(Capabilities.Energy.BLOCK, pos, side.getOpposite());
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
    public static EnergyHandler getEnergyStorageOffset(Level world, BlockPos pos, Direction side) {
        return getEnergyStorage(world, pos.relative(side), side.getOpposite());
    }

}
