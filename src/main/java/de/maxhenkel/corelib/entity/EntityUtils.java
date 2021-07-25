package de.maxhenkel.corelib.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public class EntityUtils {

    public static void forEachPlayerAround(ServerLevel world, BlockPos pos, double radius, Consumer<ServerPlayer> playerEntityConsumer) {
        world.getPlayers(player -> player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= radius * radius).forEach(playerEntityConsumer);
    }

}
