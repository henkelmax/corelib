package de.maxhenkel.corelib;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.FolderName;

import java.io.File;

public class CommonUtils {

    /**
     * Gets a folder located in the world directory
     *
     * @param serverWorld the world
     * @param folderName  the name of the folder
     * @return a file pointing to folder in the world directory
     */
    public static File getWorldFolder(ServerWorld serverWorld, FolderName folderName) {
        return serverWorld.getServer().getWorldPath(folderName).toFile();
    }

}
