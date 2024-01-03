package de.maxhenkel.corelib;

import de.maxhenkel.corelib.config.ConfigBase;
import de.maxhenkel.corelib.config.DynamicConfig;
import de.maxhenkel.corelib.net.Message;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLConfig;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

import java.nio.file.Path;
import java.util.function.Consumer;

public class CommonRegistry {

    private static final LevelResource SERVERCONFIG = new LevelResource("serverconfig");
    private static final Path DEFAULT_CONFIG_PATH = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath());

    /**
     * Registers a new message on the provided network channel
     *
     * @param registrar the pre-configured registrar
     * @param message   the message
     */
    public static <T extends Message> void registerMessage(IPayloadRegistrar registrar, Class<T> message) {
        try {
            T dummy = message.getDeclaredConstructor().newInstance();
            registrar.play(dummy.id(), byteBuf -> {
                try {
                    T msg = message.getDeclaredConstructor().newInstance();
                    return msg.fromBytes(byteBuf);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, (payload, context) -> {
                if (!payload.getExecutingSide().equals(context.flow())) {
                    return;
                }

                if (PacketFlow.CLIENTBOUND.equals(context.flow())) {
                    context.workHandler().execute(() -> {
                        payload.executeClientSide(context);
                    });
                } else {
                    context.workHandler().execute(() -> {
                        payload.executeServerSide(context);
                    });
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Registers the provided entity
     * Note that the entity class has to have a constructor with the parameters {@link EntityType} and {@link Level} or a constructor with the parameter {@link Level}
     *
     * @param modId           the mod ID
     * @param name            the entity name
     * @param classification  the entity classification
     * @param entityClass     the entity class
     * @param builderConsumer a consumer that provides the builder to apply properties
     * @return the entity type of the registered entity
     */
    public static <T extends Entity> EntityType<T> registerEntity(String modId, String name, MobCategory classification, Class<? extends Entity> entityClass, Consumer<EntityType.Builder<T>> builderConsumer) {
        EntityType.Builder<T> builder = EntityType.Builder
                .of((type, world) -> {
                    try {
                        try {
                            return (T) entityClass.getConstructor(EntityType.class, Level.class).newInstance(type, world);
                        } catch (NoSuchMethodException e) {
                            return (T) entityClass.getConstructor(Level.class).newInstance(world);
                        }
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }, classification);
        builderConsumer.accept(builder);
        EntityType<T> type = builder.build(modId + ":" + name);
//        type.setRegistryName(new ResourceLocation(modId, name)); //TODO
        return type;
    }

    /**
     * Registers a config for the provided config type
     *
     * @param type             the config type
     * @param configClass      the config class
     * @param registerListener if a config reload listener should be registered
     * @return the instantiated config
     */
    public static <T extends ConfigBase> T registerConfig(ModConfig.Type type, Class<T> configClass, boolean registerListener) {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        T config;
        try {
            config = configClass.getConstructor(ModConfigSpec.Builder.class).newInstance(builder);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        ModConfigSpec spec = builder.build();
        ModLoadingContext.get().registerConfig(type, spec);
        config.setConfigSpec(spec);
        if (registerListener) {
            Consumer<ModConfigEvent> consumer = evt -> {
                if (evt.getConfig().getType() == type) {
                    config.onReload(evt);
                }
            };
            ModLoadingContext.get().getActiveContainer().getEventBus().addListener(consumer);
        }
        return config;
    }

    /**
     * Registers a config for the provided config type
     *
     * @param type        the config type
     * @param configClass the config class
     * @return the instantiated config
     */
    public static <T extends ConfigBase> T registerConfig(ModConfig.Type type, Class<T> configClass) {
        return registerConfig(type, configClass, false);
    }

    /**
     * Registers a config that can have dynamic config keys
     *
     * @param type        the config type
     * @param folderName  the name of the folder that contains the config
     * @param configName  the name of the config (without extension)
     * @param configClass the config class
     * @param <T>         the config
     * @return the config
     */
    public static <T extends DynamicConfig> T registerDynamicConfig(DynamicConfig.DynamicConfigType type, String folderName, String configName, Class<T> configClass) {
        try {
            T config = configClass.getDeclaredConstructor().newInstance();
            String configFileName = configName + ".toml";
            if (type.equals(DynamicConfig.DynamicConfigType.SERVER)) {
                Consumer<ServerAboutToStartEvent> consumer = event -> {
                    Path serverConfig = event.getServer().getWorldPath(SERVERCONFIG).resolve(folderName);
                    serverConfig.toFile().mkdirs();
                    Path configPath = serverConfig.resolve(configFileName);
                    Path defaultPath = DEFAULT_CONFIG_PATH.resolve(folderName).resolve(configFileName);
                    config.init(configPath, defaultPath);
                };
                NeoForge.EVENT_BUS.addListener(consumer);
            } else {
                Path commonConfig = FMLPaths.CONFIGDIR.get().resolve(folderName);
                commonConfig.toFile().mkdirs();
                Path configPath = commonConfig.resolve(configFileName);
                config.init(configPath);
            }

            return config;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
