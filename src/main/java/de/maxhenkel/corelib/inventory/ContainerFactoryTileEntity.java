package de.maxhenkel.corelib.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.IContainerFactory;

public class ContainerFactoryTileEntity<T extends Container, U extends TileEntity> implements IContainerFactory<T> {

    private final ContainerCreator<T, U> containerCreator;

    public ContainerFactoryTileEntity(ContainerCreator<T, U> containerCreator) {
        this.containerCreator = containerCreator;
    }

    @Override
    public T create(int windowId, PlayerInventory inv, PacketBuffer data) {
        TileEntity te = inv.player.world.getTileEntity(data.readBlockPos());
        try {
            return containerCreator.create(windowId, inv, (U) te);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public interface ContainerCreator<T extends Container, U extends TileEntity> {
        T create(int windowId, PlayerInventory inv, U tileEntity);
    }
}