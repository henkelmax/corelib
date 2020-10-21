package de.maxhenkel.corelib.death;

import de.maxhenkel.corelib.CommonUtils;
import de.maxhenkel.corelib.Logger;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.FolderName;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class DeathManager {

    /**
     * The name of the folder that stores all player deaths
     */
    public static FolderName DEATHS = new FolderName("deaths");

    /**
     * Adds a death to the deaths folder as the provided player
     *
     * @param player the player under which the death should get stored
     * @param death  the death to store
     */
    public static void addDeath(ServerPlayerEntity player, Death death) {
        try {
            File deathFile = getDeathFile(player, death.getId());
            deathFile.getParentFile().mkdirs();
            CompressedStreamTools.write(death.toNBT(), deathFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the death with the provided ID of a specific player
     *
     * @param player the player
     * @param id     the death ID
     * @return the death
     */
    @Nullable
    public static Death getDeath(ServerPlayerEntity player, UUID id) {
        return getDeath(player.getServerWorld(), player.getUniqueID(), id);
    }

    /**
     * Gets the death with the provided ID of a specific player
     *
     * @param world      the world
     * @param playerUUID the players UUID
     * @param id         the death ID
     * @return the death
     */
    @Nullable
    public static Death getDeath(ServerWorld world, UUID playerUUID, UUID id) {
        try {
            CompoundNBT data = CompressedStreamTools.read(getDeathFile(world, playerUUID, id));
            if (data == null) {
                return null;
            }
            return Death.fromNBT(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads the death from the provided file
     *
     * @param file the death file
     * @return the death
     */
    @Nullable
    public static Death getDeath(File file) {
        try {
            CompoundNBT data = CompressedStreamTools.read(file);
            if (data == null) {
                return null;
            }
            return Death.fromNBT(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a death from its ID
     * Note that this searches for every players deaths
     *
     * @param world the world
     * @param id    the death id
     * @return the death
     */
    @Nullable
    public static Death getDeath(ServerWorld world, UUID id) {
        File deathFolder = getDeathFolder(world);
        File[] players = deathFolder.listFiles((dir, name) -> name.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));

        if (players == null) {
            return null;
        }

        for (File f : players) {
            if (!f.isDirectory()) {
                continue;
            }
            File[] files = f.listFiles((dir, name) -> name.equals(id.toString() + ".dat"));
            if (files != null && files.length > 0) {
                return getDeath(world, UUID.fromString(f.getName()), id);
            }
        }
        return null;
    }

    /**
     * Gets all deaths of a player
     *
     * @param player the player
     * @return all deaths of the player
     */
    public static List<Death> getDeaths(ServerPlayerEntity player) {
        return getDeaths(player);
    }

    /**
     * Gets all deaths of a player
     *
     * @param world      the world - used to get the death folder location
     * @param playerUUID the UUID of the player
     * @return a list containing all deaths of the player
     */
    public static List<Death> getDeaths(ServerWorld world, UUID playerUUID) {
        File playerDeathFolder = getPlayerDeathFolder(world, playerUUID);

        if (!playerDeathFolder.exists()) {
            return Collections.emptyList();
        }

        File[] deaths = playerDeathFolder.listFiles((dir, name) -> name.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.dat$"));

        if (deaths == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(deaths)
                .map(f -> {
                    try {
                        return Death.fromNBT(CompressedStreamTools.read(f));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(Death::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Deletes the death file from the filesystem
     *
     * @param world the world
     * @param death the death to delete
     * @return if the death was deleted from the filesystem
     */
    public static boolean removeDeath(ServerWorld world, Death death) {
        File file = getDeathFile(world, death.getPlayerUUID(), death.getId());
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    /**
     * Deletes all deaths that are older than the provided time
     *
     * @param world the world
     * @param age   the age in milliseconds
     */
    public static void removeDeathsOlderThan(ServerWorld world, long age) {
        long now = System.currentTimeMillis();
        File deathFolder = getDeathFolder(world);
        File[] players = deathFolder.listFiles((dir, name) -> name.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));

        if (players == null) {
            return;
        }

        for (File f : players) {
            if (!f.isDirectory()) {
                continue;
            }
            File[] deaths = f.listFiles(file -> (now - file.lastModified()) > age);
            if (deaths == null) {
                continue;
            }
            for (File d : deaths) {
                Death death = getDeath(d);
                if (death == null) {
                    continue;
                }
                if ((now - death.getTimestamp()) > age) {
                    if (d.delete()) {
                        Logger.INSTANCE.info("Removed death file of '{}' ({}), since it is older than {} milliseconds", death.getPlayerName(), death.getId().toString(), age);
                    }
                }
            }
        }
    }

    /**
     * Gets the death file of a player with the provided death ID
     *
     * @param player the player
     * @param id     the death ID
     * @return the death file
     */
    public static File getDeathFile(ServerPlayerEntity player, UUID id) {
        return new File(getPlayerDeathFolder(player), id.toString() + ".dat");
    }

    /**
     * Gets the death file of a player with the provided death ID
     *
     * @param world      the world
     * @param playerUUID the players UUID
     * @param id         the death ID
     * @return the death file
     */
    public static File getDeathFile(ServerWorld world, UUID playerUUID, UUID id) {
        return new File(getPlayerDeathFolder(world, playerUUID), id.toString() + ".dat");
    }

    /**
     * Get the deaths folder of a player
     *
     * @param player the player
     * @return the deaths folder
     */
    public static File getPlayerDeathFolder(ServerPlayerEntity player) {
        return getPlayerDeathFolder(player.getServerWorld(), player.getUniqueID());
    }

    /**
     * Get the deaths folder of a player
     *
     * @param world the world - used to get the death folder location
     * @param uuid  the UUID of the player
     * @return the deaths folder
     */
    public static File getPlayerDeathFolder(ServerWorld world, UUID uuid) {
        return new File(getDeathFolder(world), uuid.toString());
    }

    /**
     * Gets the folder containing the players deaths
     *
     * @param world the world - used to get the death folder location
     * @return the player deaths folder
     */
    public static File getDeathFolder(ServerWorld world) {
        return CommonUtils.getWorldFolder(world, DEATHS);
    }
}
