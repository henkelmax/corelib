package de.maxhenkel.corelib.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public interface Message<T extends Message<T>> extends CustomPacketPayload {

    PacketFlow getExecutingSide();

    default void executeServerSide(PlayPayloadContext context) {

    }

    default void executeClientSide(PlayPayloadContext context) {

    }

    T fromBytes(FriendlyByteBuf buf);

    void toBytes(FriendlyByteBuf buf);

    @Override
    default void write(FriendlyByteBuf buf) {
        toBytes(buf);
    }

    ResourceLocation id();

}
