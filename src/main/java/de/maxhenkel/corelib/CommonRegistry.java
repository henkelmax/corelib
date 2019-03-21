package de.maxhenkel.corelib;

import de.maxhenkel.corelib.net.Message;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class CommonRegistry {

    public static SimpleChannel registerChannel(String modId, String name) {
        return NetworkRegistry.newSimpleChannel(new ResourceLocation(modId, name), () -> "1.0.0", s -> true, s -> true);
    }

    public static <T extends Message> void registerMessage(SimpleChannel channel, int id, Class<? extends Message> message) {
        channel.registerMessage(id, Message.class, (msg, buf) -> msg.toBytes(buf), (buf) -> {
            try {
                Message msg = message.newInstance();
                return msg.fromBytes(buf);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, (msg, fun) -> {
            if (msg.getExecutingSide().equals(Dist.CLIENT)) {
                msg.executeClientSide(fun.get());
            } else if (msg.getExecutingSide().equals(Dist.DEDICATED_SERVER)) {
                msg.executeServerSide(fun.get());
            }
        });
    }

    public static EntityType registerEntity(String modId, String name, Class<? extends Entity> entityClass, int range, int updateFrequency, boolean sendVelocityUpdates) {
        return EntityType.register(modId + ":" + name, EntityType.Builder.create(entityClass, (world -> {
            try {
                return entityClass.getConstructor(World.class).newInstance(world);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })).tracker(range, updateFrequency, sendVelocityUpdates));
    }

}
