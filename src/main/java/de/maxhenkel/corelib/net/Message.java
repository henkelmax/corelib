package de.maxhenkel.corelib.net;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface Message<T extends Message<T>> extends CustomPacketPayload {

    PacketFlow getExecutingSide();

    default void executeServerSide(IPayloadContext context) {

    }

    default void executeClientSide(IPayloadContext context) {

    }

    T fromBytes(RegistryFriendlyByteBuf buf);

    void toBytes(RegistryFriendlyByteBuf buf);

    @Override
    Type<T> type();

}
