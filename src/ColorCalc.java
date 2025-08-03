import java.awt.*;
import java.util.Locale;

public final class ColorCalc {
    /**
     * Utility Class for all calculations.
     */
    private ColorCalc() {}

    private static String percentToDecimal(int percentValue) {
        final double decimal = ((double) percentValue / 100f);

        return normalizeDecimal(decimal);
    }

    /**
     * Always returns a string representation of a decimal with 4 characters (e.g. 1.00 or 0.92).
     */
    private static String normalizeDecimal(double decimal) {
        return String.format(Locale.US, "%.2f", decimal);
    }

    /**
     * Methods that convert all inputs to a color.
     */
    public static Color rgbTupleToColor(String tupleString) {
        final String[] tuple = tupleString.replaceAll("[() ]", "").split(",");

        final int r = Integer.parseInt(tuple[0]);
        final int g = Integer.parseInt(tuple[1]);
        final int b = Integer.parseInt(tuple[2]);

        return new Color(r, g, b);
    }

    public static Color hexTocolor(String hexVal) {
        hexVal = hexVal.replaceAll("#|0X|0x| ", "");
        final int rgb = Integer.parseUnsignedInt(hexVal, 16);

        return new Color(rgb);
    }

    public static Color decToColor(String decString) {
        decString = decString.replaceAll("[^0-9]", "");
        final int rgb = Integer.parseInt(decString);

        return new Color(rgb);
    }

    public static Color hsbhsvTupleToColor(String tupleString) {
        final String[] tuple = tupleString.replaceAll("[()%° ]", "").split(",");

        final float hue = cyclicPeriodicity(Integer.parseInt(tuple[0])) / 360f;
        final float sat = Float.parseFloat(tuple[1]) / 100f;
        final float brt = Float.parseFloat(tuple[2]) / 100f;

        return new Color(Color.HSBtoRGB(hue, sat, brt));
    }

    /**
     * Source: <a href="https://en.wikipedia.org/wiki/HSL_and_HSV#HSL_to_RGB">Wikipedia: HSL and HSV</a>.
     */
    public static Color hslTupleToColor(String tupleString) {
        final String[] tuple = tupleString.replaceAll("[()%° ]", "").split(",");

        final int hue = cyclicPeriodicity(Integer.parseInt(tuple[0]));
        final float huePrime = hue / 60f;
        final float sat = Float.parseFloat(tuple[1]) / 100f;
        final float lgt = Float.parseFloat(tuple[2]) / 100f;

        final float chroma = (1f - Math.abs(2f * lgt - 1f)) * sat;
        final float componentX = chroma * (1f - Math.abs((huePrime % 2) - 1f));
        final float[] rgbValues = switch ((int) Math.floor(huePrime)) {
            case 0 -> new float[]{chroma, componentX, 0};
            case 1 -> new float[]{componentX, chroma, 0};
            case 2 -> new float[]{0, chroma, componentX};
            case 3 -> new float[]{0, componentX, chroma};
            case 4 -> new float[]{componentX, 0, chroma};
            default -> new float[]{chroma, 0, componentX};
        };

        final float corrector = lgt - (chroma / 2f);

        final int r = Math.round((rgbValues[0] + corrector) * 255);
        final int g = Math.round((rgbValues[1] + corrector) * 255);
        final int b = Math.round((rgbValues[2] + corrector) * 255);

        return new Color(r, g, b);
    }

    /**
     * Utility method to take into account that the color circle repeats with a period of 360 degrees.
     * Normalizes angles to range [0, 360) (0 inclusive to 360 exclusive).
     */
    private static int cyclicPeriodicity(int hue) {
        hue %= 360;

        return hue >= 0 ? hue : hue + 360;
    }

    /**
     * Source: <a href="https://www.rapidtables.com/convert/color/cmyk-to-rgb.html">rapidtables</a>.
     */
    public static Color cmykTupleToColor(String tupleString) {
        final String[] tuple = tupleString.replaceAll("[()% ]", "").split(",");
        final float c = Float.parseFloat(tuple[0]);
        final float m = Float.parseFloat(tuple[1]);
        final float y = Float.parseFloat(tuple[2]);
        final float k = Float.parseFloat(tuple[3]);

        final int r = Math.round(255f * (1f - c) * (1f - k));
        final int g = Math.round(255f * (1f - m) * (1f - k));
        final int b = Math.round(255f * (1f - y) * (1f - k));

        return new Color(r, g, b);
    }

    /**
     * Methods that convert a color into any color representation.
     */
    public static Color removeAlpha(Color color) {
        final int rgb = color.getRGB() | 0xFF000000;

        return new Color(rgb);
    }


    public static String[] colorToRGB(Color color) {
        return new String[]{
                "[red = " + color.getRed() + ", green = " + color.getGreen() + ", blue = " + color.getBlue() + "]",
                "(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")"
        };
    }

    public static String[] colorToHEX(Color color) {
        String red = Integer.toHexString(color.getRed()).toUpperCase();
        if (red.length() < 2) {
            red = "0" + red;
        }

        String green = Integer.toHexString(color.getGreen()).toUpperCase();
        if (green.length() < 2) {
            green = "0" + green;
        }

        String blue = Integer.toHexString(color.getBlue()).toUpperCase();
        if (blue.length() < 2) {
            blue = "0" + blue;
        }

        final String hex = red + green + blue;

        return new String[]{
                "0x" + hex,
                "#" + hex,
                hex
        };
    }

    public static String[] colorToDEC(Color color) {
        final int dec = color.getRGB() & 0x00FFFFFF;

        return new String[]{Integer.toString(dec)};
    }

    public static String[] colorToHSBHSV(Color color) {
        final float[] normalizedVals = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        final int[] hsbVals = new int[3];
        hsbVals[0] = Math.round(normalizedVals[0] * 360f);
        hsbVals[1] = Math.round(normalizedVals[1] * 100f);
        hsbVals[2] = Math.round(normalizedVals[2] * 100f);

        return new String[]{
                "[hue = " + hsbVals[0] + "°, saturation = " + hsbVals[1] + "%, brightness = " + hsbVals[2] + "%]",
                "(" + hsbVals[0] + ", " + hsbVals[1] + ", " + hsbVals[2] + ")",
                "(" + hsbVals[0] + ", " + percentToDecimal(hsbVals[1]) + ", " + percentToDecimal(hsbVals[2]) +")"
        };
    }

    /**
     * Source: <a href="https://en.wikipedia.org/wiki/HSL_and_HSV#HSV_to_HSL">Wikipedia: HSL_and_HSV</a>.
     */
    public static String[] colorToHSL(Color color) {
        final float[] normalizedVals = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        final float[] auxValues = new float[3];
        auxValues[0] = normalizedVals[0];
        auxValues[2] = (2f - normalizedVals[1]) * normalizedVals[2] / 2f;
        auxValues[1] = sInHSBtoHSL(normalizedVals[2], auxValues[2]);
        final int[] hslValues = new int[3];
        hslValues[0] = Math.round(auxValues[0] * 360f);
        hslValues[1] = Math.round(auxValues[1] * 100f);
        hslValues[2] = Math.round(auxValues[2] * 100f);

        return new String[]{
                "[hue = " + hslValues[0] + "°, saturation = " + hslValues[1] + "%, lightness = " + hslValues[2] + "%] ",
                "(" + hslValues[0] + ", " + hslValues[1] + ", " + hslValues[2] + ") ",
                "(" + hslValues[0] + ", " + percentToDecimal(hslValues[1]) + ", " + percentToDecimal(hslValues[2]) + ")"
        };
    }

    /**
     * Utility method that calculates "Saturation" for HSL color space from HSB/HSV color space.
     */
    private static float sInHSBtoHSL(float b, float l) {
        if (l == 0f || l == 1f) {
            return 0f;
        } else {
            return (b - l) / Math.min(l, 1f - l);
        }
    }

    /**
     * Source: <a href="https://www.rapidtables.com/convert/color/rgb-to-cmyk.html">rapidtables</a>.
     */
    public static String[] colorToCMYK(Color color) {
        final float redPrime = ((float) color.getRed()) / 255f;
        final float greenPrime = ((float) color.getGreen()) / 255f;
        final float bluePrime = ((float) color.getBlue()) / 255f;

        final float blackKey = 1f - Math.max(Math.max(redPrime, greenPrime), bluePrime);

        float cyan = 0f;
        float magenta = 0f;
        float yellow = 0f;

        if (blackKey != 1f) {
            cyan = (1f - redPrime - blackKey) / (1f - blackKey);
            magenta = (1f - greenPrime - blackKey) / (1f - blackKey);
            yellow = (1f - bluePrime - blackKey) / (1f - blackKey);
        }

        return new String[]{
                "[cyan = " + normalizeDecimal(cyan) + ", magenta = " + normalizeDecimal(magenta) + ", yellow = " + normalizeDecimal(yellow) + ", black key = " + normalizeDecimal(blackKey) + "]",
                "(" + normalizeDecimal(cyan) + ", " + normalizeDecimal(magenta) + ", " + normalizeDecimal(yellow) + ", " + normalizeDecimal(blackKey) + ")",
                "(" + Math.round(cyan * 100f) + "%, " + Math.round(magenta * 100f) + "%, " + Math.round(yellow * 100f) + "%, " + Math.round(blackKey * 100f) + "%)"
        };
    }
}
