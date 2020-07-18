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

import java.util.function.Consumer;

public class CommonRegistry {

    /**
     * Creates a new network channel
     * @param modId the mod ID
     * @param name the name of the channel
     * @return the channel
     */
    public static SimpleChannel registerChannel(String modId, String name) {
        return NetworkRegistry.newSimpleChannel(new ResourceLocation(modId, name), () -> "1.0.0", s -> true, s -> true);
    }

    /**
     * Registers a new message on the provided network channel
     * @param channel the channel to register the message on
     * @param id the packed id (has to be unique)
     * @param message the message
     */
    public static <T extends Message<?>> void registerMessage(SimpleChannel channel, int id, Class<T> message) {
        channel.registerMessage(id, (Class) message, Message::toBytes, (buf) -> {
            try {
                Message<?> msg = message.newInstance();
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
     * Note that the entity class has to have a constructor with the parameters {@link EntityType} and {@link World} or a constructor with the parameter {@link World}
     *
     * @param modId           the mod ID
     * @param name            the entity name
     * @param classification  the entity classification
     * @param entityClass     the entity class
     * @param builderConsumer a consumer that provides the builder to apply properties
     * @return the entity type of the registered entity
     */
    public static <T extends Entity> EntityType<T> registerEntity(String modId, String name, EntityClassification classification, Class<? extends Entity> entityClass, Consumer<EntityType.Builder<T>> builderConsumer) {
        EntityType.Builder<T> builder = EntityType.Builder
                .create((type, world) -> {
                    try {
                        try {
                            return (T) entityClass.getConstructor(EntityType.class, World.class).newInstance(type, world);
                        } catch (NoSuchMethodException e) {
                            return (T) entityClass.getConstructor(World.class).newInstance(world);
                        }
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }, classification);
        builderConsumer.accept(builder);
        EntityType<T> type = builder.build(modId + ":" + name);
        type.setRegistryName(new ResourceLocation(modId, name));
        return type;
    }

}
