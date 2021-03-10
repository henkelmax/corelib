package de.maxhenkel.corelib.net;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetUtils {

    /**
     * Sends the provided message to the provided client
     *
     * @param channel the network channel
     * @param player  the player to send the message to
     * @param message the message to send
     */
    public static void sendTo(SimpleChannel channel, ServerPlayerEntity player, Message<?> message) {
        channel.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

}
