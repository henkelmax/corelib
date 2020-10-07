package de.maxhenkel.corelib.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class PlayerUtils {

    /**
     * Returns the model flags for the player skin
     *
     * @param player the player
     * @return the model flags
     */
    public static byte getModel(PlayerEntity player) {
        try {
            Field flag = ObfuscationReflectionHelper.findField(PlayerEntity.class, "field_184827_bp");
            DataParameter<Byte> dataParameter = (DataParameter<Byte>) flag.get(null);
            return player.getDataManager().get(dataParameter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
