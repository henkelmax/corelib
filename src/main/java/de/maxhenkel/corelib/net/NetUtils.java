package de.maxhenkel.corelib.net;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PlayNetworkDirection;
import net.neoforged.neoforge.network.simple.SimpleChannel;

public class NetUtils {

    /**
     * Sends the provided message to the provided client
     *
     * @param channel the network channel
     * @param player  the player to send the message to
     * @param message the message to send
     */
    public static void sendTo(SimpleChannel channel, ServerPlayer player, Message<?> message) {
        channel.sendTo(message, player.connection.connection, PlayNetworkDirection.PLAY_TO_CLIENT);
    }

    @OnlyIn(Dist.CLIENT)
    public static void sendToServer(SimpleChannel channel, Message<?> message) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            channel.sendToServer(message);
        }
    }

}
