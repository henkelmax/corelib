package de.maxhenkel.corelib.energy;

import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;

@Deprecated(forRemoval = true)
public class UsableEnergyStorage extends SimpleEnergyHandler {

    public UsableEnergyStorage(int capacity) {
        super(capacity);
    }

    public UsableEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public UsableEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public UsableEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int useEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energy, maxExtract);
        if (!simulate) {
            energy -= energyExtracted;
        }
        return energyExtracted;
    }
}
