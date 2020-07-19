package de.maxhenkel.corelib.math;

public class MathUtils {

    /**
     * Checks if the provided number is in the specified bounds with a tolerance
     *
     * @param number    the number
     * @param bound     the bound
     * @param tolerance the tolerance
     * @return if it is in the tolerance
     */
    public static boolean isInBounds(float number, float bound, float tolerance) {
        if (number > bound - tolerance && number < bound + tolerance) {
            return true;
        }
        return false;
    }

    /**
     * Subtracts from the provided number, but does not cross zero
     *
     * @param num the number
     * @param sub the amount to subtract
     * @return the resulting number
     */
    public static float subtractToZero(float num, float sub) {
        float erg;
        if (num < 0F) {
            erg = num + sub;
            if (erg > 0F) {
                erg = 0F;
            }
        } else {
            erg = num - sub;
            if (erg < 0F) {
                erg = 0F;
            }
        }

        return erg;
    }

    /**
     * Rounds the provided number to the provided scale
     *
     * @param value the number to round
     * @param scale the number of digits after the dot
     * @return the rounded number
     */
    public static double round(double value, int scale) {
        return Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale);
    }

    /**
     * Rounds the provided number to the provided scale
     *
     * @param value the number to round
     * @param scale the number of digits after the dot
     * @return the rounded number
     */
    public static float round(float value, int scale) {
        return (float) (Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale));
    }

}
