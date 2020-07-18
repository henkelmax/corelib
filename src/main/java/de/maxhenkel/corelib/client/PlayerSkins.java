package de.maxhenkel.corelib.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.ResourceLocation;

public class PlayerSkins {

    private static HashMap<String, GameProfile> players = new HashMap<>();

    /**
     * Gets the resource location of the skin of provided player UUID and name.
     * Defaults to the steve and alex skin.
     *
     * @param uuid the UUID of the player
     * @param name the name of the player
     * @return the resource location to the skin
     */
    public static ResourceLocation getSkin(UUID uuid, String name) {
        return getSkin(getGameProfile(uuid, name));
    }

    /**
     * Gets the resource location of the skin of the provided player
     *
     * @param player the player to get the skin of
     * @return the resource location to the skin
     */
    public static ResourceLocation getSkin(PlayerEntity player) {
        return getSkin(player.getGameProfile());
    }

    /**
     * Gets the resource location of the skin of the provided game profile
     *
     * @param gameProfile the game profile of the player
     * @return the resource location to the skin
     */
    public static ResourceLocation getSkin(GameProfile gameProfile) {
        Minecraft minecraft = Minecraft.getInstance();
        Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(gameProfile);

        if (map.containsKey(Type.SKIN)) {
            return minecraft.getSkinManager().loadSkin(map.get(Type.SKIN), Type.SKIN);
        } else {
            return DefaultPlayerSkin.getDefaultSkin(gameProfile.getId());
        }
    }

    /**
     * Gets the game profile of the provided player UUID and name
     *
     * @param uuid the UUID of the player
     * @param name the name of the player
     * @return the game profile
     */
    public static GameProfile getGameProfile(UUID uuid, String name) {
        if (players.containsKey(uuid.toString())) {
            return players.get(uuid.toString());
        } else {
            GameProfile profile = SkullTileEntity.updateGameProfile(new GameProfile(uuid, name));
            players.put(uuid.toString(), profile);
            return profile;
        }
    }

    /**
     * Returns if the skin is slim
     *
     * @param uuid the UUID of the player
     * @return if the skin is slim
     */
    public static boolean isSlim(UUID uuid) {
        NetworkPlayerInfo networkplayerinfo = Minecraft.getInstance().getConnection().getPlayerInfo(uuid);
        return networkplayerinfo == null ? (uuid.hashCode() & 1) == 1 : networkplayerinfo.getSkinType().equals("slim");
    }

    /**
     * Returns if the skin is slim
     *
     * @param player the player
     * @return if the skin is slim
     */
    public static boolean isSlim(PlayerEntity player) {
        return isSlim(player.getUniqueID());
    }

}
