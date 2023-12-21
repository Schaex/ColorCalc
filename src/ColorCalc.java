import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ColorCalc {
    /**
     * Utility Class for all calculations.

     * Frequently used constants.
     */
    public static final String newline = System.getProperty("line.separator");
    public static final char[] HEXChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String percentToDecimal (int percentValue) {
        float decimalValue = ((float) percentValue / 100f);
        StringBuilder nicerDecimalValue = new StringBuilder(String.valueOf(decimalValue));

        while (nicerDecimalValue.length() < 4) {
            nicerDecimalValue.append("0");
        }

        return nicerDecimalValue.toString();
    }

    /**
     * Always returns a string representation of a decimal with 4 characters (e.g. 1.00 or 0.92).
     */
    public static String normalizeDecimal (float decimal) {
        BigDecimal intermediate = BigDecimal.valueOf(decimal).setScale(2, RoundingMode.HALF_UP);
        StringBuilder nicerDecimalValue = new StringBuilder(intermediate.toPlainString());

        while (nicerDecimalValue.length() < 4) {
            nicerDecimalValue.append("0");
        }

        return nicerDecimalValue.toString();
    }

    /**
     * Methods that convert all inputs to a color.
     */
    public static Color RGBtupleToColor (String oldRGBtuple) {
        String rgbTuple = oldRGBtuple.replaceAll("[() ]", "");
        int comma1 = rgbTuple.indexOf(",");
        int comma2 = rgbTuple.lastIndexOf(",");
        int red = Integer.parseInt(rgbTuple.substring(0, comma1));
        int green = Integer.parseInt(rgbTuple.substring(comma1 + 1, comma2));
        int blue = Integer.parseInt(rgbTuple.substring(comma2 + 1));

        return new Color(red, green, blue);
    }

    public static Color HEXtoColor (String hexVal) {
        String hex = hexVal.toUpperCase();
        hex = hex.replaceAll("#|0X", "");
        if (hex.length() > 6) {
            hex = hex.substring(2, 7);
        }
        int i0 = HEXcharToInt(hex.charAt(0));
        int i1 = HEXcharToInt(hex.charAt(1));
        int i2 = HEXcharToInt(hex.charAt(2));
        int i3 = HEXcharToInt(hex.charAt(3));
        int i4 = HEXcharToInt(hex.charAt(4));
        int i5 = HEXcharToInt(hex.charAt(5));

        return new Color(16 * i0 + i1, 16 * i2 + i3, 16 * i4 + i5);
    }

    /**
     * Utility method that converts single hexadecimal values to their respective integer values.
     */
    public static int HEXcharToInt (char c) {
        for (int i = 0; i <= 15; i++) {
            if (HEXChars[i] == c) {
                return i;
            }
        }

        return -1;
    }

    public static Color DECtoColor (String decString) {
        int dec = Integer.parseInt(decString);

        int red = dec / 65536;
        int i = dec % 65536;
        int green = i / 256;
        int blue = i % 256;

        return new Color(red, green, blue);
    }

    public static Color HSBHSVtupleToColor (String oldHSBHSVtuple) {
        String hslTuple = oldHSBHSVtuple.replaceAll("[()%°]", "").replaceAll(" ", "");
        int comma1 = hslTuple.indexOf(",");
        int comma2 = hslTuple.lastIndexOf(",");
        float hue = CyclicPeriodicity(Float.parseFloat(hslTuple.substring(0, comma1))) / 360f;
        float sat = Float.parseFloat(hslTuple.substring(comma1 + 1, comma2)) / 100f;
        float brt = Float.parseFloat(hslTuple.substring(comma2 + 1)) / 100f;

        return new Color(Color.HSBtoRGB(hue, sat, brt));
    }

    /**
     * Source: <a href="https://en.wikipedia.org/wiki/HSL_and_HSV#HSL_to_RGB">Wikipedia: HSL and HSV</a>.
     */
    public static Color HSLtupleToColor (String oldHSLtuple) {
        String hslTuple = oldHSLtuple.replaceAll("[()%°]", "").replaceAll(" ", "");
        int comma1 = hslTuple.indexOf(",");
        int comma2 = hslTuple.lastIndexOf(",");
        float hue = CyclicPeriodicity(Float.parseFloat(hslTuple.substring(0, comma1)));
        float huePrime = hue / 60f;
        float sat = Float.parseFloat(hslTuple.substring(comma1 + 1, comma2)) / 100f;
        float lgt = Float.parseFloat(hslTuple.substring(comma2 + 1)) / 100f;

        float chroma = (1f - Math.abs(2f * lgt - 1f)) * sat;
        float componentX = chroma * (1f - Math.abs((huePrime % 2) - 1f));
        float[] rgbValues;

        if (0 <= huePrime && huePrime < 1) {
            rgbValues = new float[]{chroma, componentX, 0};
        } else if (1 <= huePrime && huePrime < 2) {
            rgbValues = new float[]{componentX, chroma, 0};
        } else if (2 <= huePrime && huePrime < 3) {
            rgbValues = new float[]{0, chroma, componentX};
        } else if (3 <= huePrime && huePrime < 4) {
            rgbValues = new float[]{0, componentX, chroma};
        } else if (4 <= huePrime && huePrime < 5) {
            rgbValues = new float[]{componentX, 0, chroma};
        } else {
            rgbValues = new float[]{chroma, 0, componentX};
        }

        float corrector = lgt - (chroma / 2f);

        int red = Math.round((rgbValues[0] + corrector) * 255);
        int green = Math.round((rgbValues[1] + corrector) * 255);
        int blue = Math.round((rgbValues[2] + corrector) * 255);

        return new Color(red, green, blue);
    }

    /**
     * Utility method to take into account that the color circle repeats with a period of 360 degrees.
     * Normalizes angles to range [0, 360) (0 inclusive to 360 exclusive).
     */
    public static float CyclicPeriodicity (float f) {
        while (f >= 360f) {
            f -= 360f;
        }

        while (f < 0) {
            f += 360f;
        }

        return f;
    }

    /**
     * Source: <a href="https://www.rapidtables.com/convert/color/cmyk-to-rgb.html">rapidtables</a>.
     */
    public static Color CMYKtupleToColor (String oldCMYKtuple) {
        String cmykTuple = oldCMYKtuple.replaceAll("[()%]", "").replaceAll(" ", "");
        int comma1 = cmykTuple.indexOf(",");
        float cyan = Float.parseFloat(cmykTuple.substring(0, comma1));
        cmykTuple = cmykTuple.substring(comma1 + 1);
        int comma2 = cmykTuple.indexOf(",");
        int comma3 = cmykTuple.lastIndexOf(",");
        float magenta = Float.parseFloat(cmykTuple.substring(0, comma1));
        float yellow = Float.parseFloat(cmykTuple.substring(comma2 + 1, comma3));
        float blackKey = Float.parseFloat(cmykTuple.substring(comma3 + 1));

        int red = Math.round(255f * (1f - cyan) * (1f - blackKey));
        int green = Math.round(255f * (1f - magenta) * (1f - blackKey));
        int blue = Math.round(255f * (1f - yellow) * (1f - blackKey));

        return new Color(red, green, blue);
    }

    /**
     * Methods that convert a color into any color representation.
     */
    public static String ColorToRGB (Color color) {
        return "In RGB:     [red = " + color.getRed() + ", green = " + color.getGreen() + ", blue = " + color.getBlue() + "] " +
                " (" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")" + newline;
    }

    public static String ColorToHEX (Color color) {
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
        String hex = red + green + blue;
        return "In HEX:     0x" + hex + "  #" + hex + "  " + hex + newline;
    }

    public static String ColorToDEC (Color color) {
        int dec = 65536 * color.getRed() + 256 * color.getGreen() + color.getBlue();
        return "In DEC:     " + dec + newline;
    }

    public static String ColorToHSBHSV (Color color) {
        float[] prehsbvals = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        int[] hsbvals = new int[3];
        hsbvals[0] = Math.round(prehsbvals[0] * 360f);
        hsbvals[1] = Math.round(prehsbvals[1] * 100f);
        hsbvals[2] = Math.round(prehsbvals[2] * 100f);

        return "In HSB/HSV: [hue = " + hsbvals[0] + ", saturation = " + hsbvals[1] + "%, brightness = " + hsbvals[2] + "%] " +
                " (" + hsbvals[0] + ", " + hsbvals[1] + "%, " + hsbvals[2] + "%) " +
                " (" + hsbvals[0] + ", " + percentToDecimal(hsbvals[1]) + ", " + percentToDecimal(hsbvals[2]) + ")" + newline;
    }

    /**
     * Source: <a href="https://en.wikipedia.org/wiki/HSL_and_HSV#HSV_to_HSL">Wikipedia: HSL_and_HSV</a>.
     */
    public static String ColorToHSL (Color color) {
        float[] prehsbvals = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float[] prehslvals = new float[3];
        prehslvals[0] = prehsbvals[0];
        prehslvals[2] = (2f - prehsbvals[1]) * prehsbvals[2] / 2f;
        prehslvals[1] = SInHSBtoHSL(prehsbvals[2], prehslvals[2]);
        int[] hslvals = new int[3];
        hslvals[0] = Math.round(prehslvals[0] * 360f);
        hslvals[1] = Math.round(prehslvals[1] * 100f);
        hslvals[2] = Math.round(prehslvals[2] * 100f);

        return "In HSL:     [hue = " + hslvals[0] + ", saturation = " + hslvals[1] + "%, lightness = " + hslvals[2] + "%] " +
                " (" + hslvals[0] + ", " + hslvals[1] + "%, " + hslvals[2] + "%) " +
                " (" + hslvals[0] + ", " + percentToDecimal(hslvals[1]) + ", " + percentToDecimal(hslvals[2]) + ")" + newline;
    }

    /**
     * Utility method that calculates "Saturation" for HSL color space from HSB/HSV color space.
     */
    public static float SInHSBtoHSL (float b, float l) {
        float s = 0f;
        if (l == 0f || l == 1f) {
            return s;
        } else {
            s = (b - l) / Math.min(l, 1f - l);
        }

        return s;
    }

    /**
     * Source: <a href="https://www.rapidtables.com/convert/color/rgb-to-cmyk.html">rapidtables</a>.
     */
    public static String ColorToCMYK (Color color) {
        float redPrime = ((float) color.getRed()) / 255f;
        float greenPrime = ((float) color.getGreen()) / 255f;
        float bluePrime = ((float) color.getBlue()) / 255f;

        float blackKey = 1f - Math.max(Math.max(redPrime, greenPrime), bluePrime);

        float cyan = 0f;
        float magenta = 0f;
        float yellow = 0f;

        if (blackKey != 1f) {
            cyan = (1f - redPrime - blackKey) / (1f - blackKey);
            magenta = (1f - greenPrime - blackKey) / (1f - blackKey);
            yellow = (1f - bluePrime - blackKey) / (1f - blackKey);
        }

        return "In CMYK:    [cyan = " + normalizeDecimal(cyan) + ", magenta = " + normalizeDecimal(magenta) + ", yellow = " + normalizeDecimal(yellow) + ", black key = " + normalizeDecimal(blackKey) + "] " +
                " (" + normalizeDecimal(cyan) + ", " + normalizeDecimal(magenta) + ", " + normalizeDecimal(yellow) + ", " + normalizeDecimal(blackKey) + ") " +
                " (" + Math.round(cyan * 100f) + "%, " + Math.round(magenta * 100f) + "%, " + Math.round(yellow * 100f) + "%, " + Math.round(blackKey * 100f) + "%)";
    }
}
