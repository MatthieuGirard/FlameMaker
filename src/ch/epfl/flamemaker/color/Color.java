package ch.epfl.flamemaker.color;

/**
 * Models a color.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 * */
public final class Color {

    /**
     * The color black.
     */
    public static final Color BLACK = new Color(0, 0, 0);

    /**
     * The color blue.
     */
    public static final Color BLUE = new Color(0, 0, 1);

    /**
     * The color green.
     */
    public static final Color GREEN = new Color(0, 1, 0);

    /**
     * The color red.
     */
    public static final Color RED = new Color(1, 0, 0);

    /**
     * The color white.
     */
    public static final Color WHITE = new Color(1, 1, 1);

    /**
     * In rgb format, color components are in the interval [0, 255].
     */
    private static final int COLOR_MAX_VALUE = 255;

    /**
     * Need 8 bits to store integers up to 255.
     */
    private static final int COLOR_COMPONENT_BIT_SIZE = 8;

    /**
     * See: https://en.wikipedia.org/wiki/SRGB.
     */
    private static final double SRGB_C_LINEAR_THRESHOLD = 0.0031308,
            SRGB_PHI = 12.92, SRGB_EXPONENT = 2.4;

    /**
     * The color components.
     */
    private final double red, green, blue;

    /**
     * Builds a color based on rgb values.
     *
     * @param r
     *            the amount of red
     * @param g
     *            the amount of green
     * @param b
     *            the amount of blue
     *
     * @throws IllegalArgumentException
     *             if a color component was not in the interval [0, 1]
     */
    public Color(final double r, final double g, final double b) {
        if (r < 0 || g < 0 || b < 0 || r > 1 || g > 1 || b > 1) {
            throw new IllegalArgumentException(
                    "The parameters must be in [0, 1]");
        }

        red = r;
        green = g;
        blue = b;
    }

    /**
     *
     * @return the red value
     */
    public double red() {
        return red;
    }

    /**
     *
     * @return the green value
     */
    public double green() {
        return green;
    }

    /**
     *
     * @return the blue value
     */
    public double blue() {
        return blue;
    }

    /**
     *
     * @return the encoded color in a 24-bits integer
     */
    public int asPackedRGB() {

        final double[] composantes = { red(), green(), blue() };
        String binaryOutput = "";
        for (final double c : composantes) {
            binaryOutput += toEightBits(sRGBEncode(c, COLOR_MAX_VALUE));
        }

        return Integer.parseInt(binaryOutput, 2);
    }

    /**
     * @param i
     *            an integer ranging from 0 to 255
     * @return an 8-bits String
     * @throws IllegalArgumentException
     *             if the argument is invalid
     */
    private String toEightBits(final int i) {
        if (i < 0 || i > COLOR_MAX_VALUE) {
            throw new
                IllegalArgumentException("Input must range from 0 to 255");
        }

        String bin = Integer.toBinaryString(i);

        while (bin.length() < COLOR_COMPONENT_BIT_SIZE) {
            bin = '0' + bin;
        }

        return bin;
    }

    /**
     * @param v
     *            value to encode
     * @param max
     *            the maximum
     * @return the value v gamma-encoded according to sRGB formula, then
     *          transformed in an integer ranging from 0 to <code>max</code>
     */
    public static int sRGBEncode(final double v, final int max) {

        double toEncode = v;
        if (toEncode <= SRGB_C_LINEAR_THRESHOLD) {
            toEncode *= SRGB_PHI;
        } else {
            toEncode = Math.pow(toEncode, 1 / SRGB_EXPONENT);
        }
        toEncode *= max;
        return (int) Math.floor(toEncode);
    }

    /**
     * Linearly interpolates two colors.
     *
     * @param that
     *            the color to mix with <code>this</code>
     * @param alpha
     *            the quantity of <code>that</code> to use
     * @return the new color
     * @throws IllegalArgumentException
     *             If alpha is not in the interval [0;1]
     */
    public Color mixWith(final Color that, final double alpha) {
        if (alpha < 0 || alpha > 1) {
            throw new IllegalArgumentException(
                    "alpha must be in the interval [0, 1]");
        }

        final double newRed = (1 - alpha) * that.red() + alpha
                * red();
        final double newGreen = (1 - alpha) * that.green() + alpha
                * green();
        final double newBlue = (1 - alpha) * that.blue() + alpha
                * blue();

        return new Color(newRed, newGreen, newBlue);
    }


    @Override
    public String toString() {
        return "(" + red + ", " + green + ", " + blue + ")";
    }

}
