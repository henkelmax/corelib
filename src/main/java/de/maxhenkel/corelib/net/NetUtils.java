package de.maxhenkel.corelib.net;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.SimpleChannel;

public class NetUtils {

    /**
     * Sends the provided message to the provided client
     *
     * @param channel the network channel
     * @param player  the player to send the message to
     * @param message the message to send
     */
    public static void sendTo(SimpleChannel channel, ServerPlayer player, Message<?> message) {
        channel.send(message, player.connection.getConnection());
    }

    @OnlyIn(Dist.CLIENT)
    public static void sendToServer(SimpleChannel channel, Message<?> message) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            channel.send(message, connection.getConnection());
        }
    }

}
