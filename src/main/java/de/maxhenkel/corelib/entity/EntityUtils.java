package de.maxhenkel.corelib.entity;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Consumer;

public class EntityUtils {

    public static void forEachPlayerAround(ServerWorld world, BlockPos pos, double radius, Consumer<ServerPlayerEntity> playerEntityConsumer) {
        world.getPlayers(player -> player.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) <= radius * radius).forEach(playerEntityConsumer);
    }

}
