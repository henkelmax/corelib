package de.maxhenkel.corelib;

import de.maxhenkel.corelib.net.Message;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
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

    /**
     * Registers the provided entity
     * Note that the entity class has to have a constructor with the parameters {@link EntityType} and {@link World}
     *
     * @param modId               the mod ID
     * @param name                the entity name
     * @param classification      the entity classification
     * @param entityClass         the entity class
     * @param range               the range in blocks where the entity is being updated
     * @param updateFrequency     the frequency in tich in which te entity gets updated
     * @param sendVelocityUpdates if the entity should send velocity updates
     * @return the entity type of the registered entity
     */
    public static <T extends Entity> EntityType<T> registerEntity(String modId, String name, EntityClassification classification, Class<? extends Entity> entityClass, int range, int updateFrequency, boolean sendVelocityUpdates) {
        return EntityType.Builder
                .<T>create((type, world) -> {
                    try {
                        return (T) entityClass.getConstructor(EntityType.class, World.class).newInstance(type, world);
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }, classification)
                .setTrackingRange(range)
                .setUpdateInterval(updateFrequency)
                .setShouldReceiveVelocityUpdates(sendVelocityUpdates).build(modId + ":" + name);
    }

}
