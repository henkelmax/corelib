package de.maxhenkel.corelib.client;

public class RenderUtils {

    public static int getArgb(int a, int red, int green, int blue) {
        return a << 24 | red << 16 | green << 8 | blue;
    }

    public static int getAlpha(int argb) {
        return (argb >> 24) & 0xFF;
    }

    public static int getRed(int argb) {
        return (argb >> 16) & 0xFF;
    }

    public static int getGreen(int argb) {
        return (argb >> 8) & 0xFF;
    }

    public static int getBlue(int argb) {
        return argb & 0xFF;
    }

}
