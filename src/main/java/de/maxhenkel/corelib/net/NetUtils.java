package de.maxhenkel.corelib.net;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * @deprecated use {@link PacketDistributor}
 */
@Deprecated
public class NetUtils {

    /**
     * Sends the provided message to the provided client
     *
     * @param player  the player to send the message to
     * @param message the message to send
     * @deprecated use {@link PacketDistributor}
     */
    @Deprecated
    public static void sendTo(ServerPlayer player, Message<?> message) {
        PacketDistributor.PLAYER.with(player).send(message);
    }

    /**
     * @param message the message to send
     * @deprecated use {@link PacketDistributor}
     */
    @OnlyIn(Dist.CLIENT)
    @Deprecated
    public static void sendToServer(Message<?> message) {
        PacketDistributor.SERVER.noArg().send(message);
    }

}
