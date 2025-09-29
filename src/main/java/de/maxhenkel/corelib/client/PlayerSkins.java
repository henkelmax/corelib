package de.maxhenkel.corelib.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.component.ResolvableProfile;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerSkins {

    private static final Map<UUID, GameProfile> PLAYERS = new ConcurrentHashMap<>();

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
        PlayerSkinRenderCache.RenderInfo renderInfo = Minecraft.getInstance().playerSkinRenderCache().getOrDefault(ResolvableProfile.createResolved(gameProfile));
        return renderInfo.playerSkin();
    }

    /**
     * Gets and resolves the game profile of the provided player UUID and name
     *
     * @param uuid the UUID of the player
     * @return the game profile
     */
    public static GameProfile getGameProfile(UUID uuid) {
        if (PLAYERS.containsKey(uuid)) {
            return PLAYERS.get(uuid);
        }

        ResolvableProfile resolvableProfile = ResolvableProfile.createUnresolved(uuid);
        GameProfile gameProfile = resolvableProfile.partialProfile();
        PLAYERS.put(uuid, gameProfile);


        resolvableProfile.resolveProfile(Minecraft.getInstance().services().profileResolver()).thenAcceptAsync(profile -> {
            PLAYERS.put(uuid, profile);
        });
        return gameProfile;
    }

    /**
     * Returns if the skin is slim
     *
     * @param uuid the UUID of the player
     * @return if the skin is slim
     */
    public static boolean isSlim(UUID uuid) {
        return PlayerModelType.SLIM.equals(getSkin(uuid).model());
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
