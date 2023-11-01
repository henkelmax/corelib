package de.maxhenkel.corelib.net;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent.Context;
import net.neoforged.api.distmarker.Dist;

public interface Message<T extends Message> {

    Dist getExecutingSide();

    default void executeServerSide(Context context) {

    }

    default void executeClientSide(Context context) {

    }

    T fromBytes(FriendlyByteBuf buf);

    void toBytes(FriendlyByteBuf buf);

}
