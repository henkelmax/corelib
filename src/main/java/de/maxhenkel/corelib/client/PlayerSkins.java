package de.maxhenkel.corelib.client;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class PlayerSkins {

    private static final Map<UUID, ResolvableProfile> PLAYERS = new ConcurrentHashMap<>();

    /**
     * Gets the resource location of the skin of provided player UUID and name.
     * Defaults to the steve and alex skin.
     *
     * @param uuid the UUID of the player
     * @return the skin
     */
    public static PlayerSkin getSkin(UUID uuid) {
        return getSkin(getGameProfile(uuid));
    }

    /**
     * Gets the resource location of the skin of the provided player
     *
     * @param player the player to get the skin of
     * @return the skin
     */
    public static PlayerSkin getSkin(Player player) {
        return getSkin(player.getGameProfile());
    }

    /**
     * Gets the resource location of the skin of the provided game profile
     *
     * @param gameProfile the game profile of the player
     * @return the skin
     */
    public static PlayerSkin getSkin(GameProfile gameProfile) {
        return Minecraft.getInstance().getSkinManager().getInsecureSkin(gameProfile);
    }

    /**
     * Gets and resolves the game profile of the provided player UUID and name
     *
     * @param uuid the UUID of the player
     * @return the game profile
     */
    public static GameProfile getGameProfile(UUID uuid) {
        if (PLAYERS.containsKey(uuid)) {
            return PLAYERS.get(uuid).gameProfile();
        }

        ResolvableProfile resolvableProfile = new ResolvableProfile(Optional.empty(), Optional.ofNullable(uuid), new PropertyMap());

        PLAYERS.put(uuid, resolvableProfile);

        if (!resolvableProfile.isResolved()) {
            resolvableProfile.resolve().thenAcceptAsync(profile -> {
                PLAYERS.put(uuid, profile);
            }, getMainExecutor());
        }

        return resolvableProfile.gameProfile();
    }

    private static Executor getMainExecutor() {
        if (FMLEnvironment.dist.isClient()) {
            return getClientExecutor();
        } else {
            throw new IllegalStateException("Skins can only be fetched on the client");
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static Executor getClientExecutor() {
        return Minecraft.getInstance();
    }

    /**
     * Returns if the skin is slim
     *
     * @param uuid the UUID of the player
     * @return if the skin is slim
     */
    public static boolean isSlim(UUID uuid) {
        return PlayerSkin.Model.SLIM.equals(getSkin(uuid).model());
    }

    /**
     * Returns if the skin is slim
     *
     * @param player the player
     * @return if the skin is slim
     */
    public static boolean isSlim(Player player) {
        return isSlim(player.getUUID());
    }

}
