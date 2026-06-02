package de.maxhenkel.corelib;

import net.minecraft.network.chat.TextColor;
import net.minecraft.util.ARGB;

public class FontColorUtils {

    public static final int BLACK = getFontColor(TextColor.BLACK);
    public static final int WHITE = getFontColor(TextColor.WHITE);

    public static int getFontColor(TextColor color) {
        int rgba = color.getValue();
        return ARGB.color(255, rgba >> 16 & 0xFF, rgba >> 8 & 0xFF, rgba & 0xFF);
    }

    public static int rgbToArgb(int rgb) {
        return ARGB.color(255, rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF);
    }

}
