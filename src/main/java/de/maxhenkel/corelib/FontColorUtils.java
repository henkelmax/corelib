package de.maxhenkel.corelib;

import net.minecraft.ChatFormatting;
import net.minecraft.util.ARGB;

public class FontColorUtils {

    public static final int BLACK = getFontColor(ChatFormatting.BLACK);
    public static final int WHITE = getFontColor(ChatFormatting.WHITE);

    public static int getFontColor(ChatFormatting color) {
        if (color.getColor() == null) {
            return ARGB.colorFromFloat(1F, 0F, 0F, 0F);
        }
        int rgba = color.getColor();
        return ARGB.color(255, rgba >> 16 & 0xFF, rgba >> 8 & 0xFF, rgba & 0xFF);
    }

    public static int rgbToArgb(int rgb) {
        return ARGB.color(255, rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF);
    }

}
