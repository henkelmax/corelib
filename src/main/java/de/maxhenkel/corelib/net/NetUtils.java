package de.maxhenkel.corelib.net;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class NetUtils {

    /**
     * Sends the provided message to the provided client
     *
     * @param channel the network channel
     * @param player  the player to send the message to
     * @param message the message to send
     */
    public static void sendTo(SimpleChannel channel, ServerPlayer player, Message<?> message) {
        channel.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

}
