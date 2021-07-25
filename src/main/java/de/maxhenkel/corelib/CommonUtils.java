package de.maxhenkel.corelib;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;

public class CommonUtils {

    /**
     * Gets a folder located in the world directory
     *
     * @param serverWorld the world
     * @param folderName  the name of the folder
     * @return a file pointing to folder in the world directory
     */
    public static File getWorldFolder(ServerLevel serverWorld, LevelResource folderName) {
        return serverWorld.getServer().getWorldPath(folderName).toFile();
    }

}
