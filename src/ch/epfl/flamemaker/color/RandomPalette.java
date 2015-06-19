package ch.epfl.flamemaker.color;

import java.util.ArrayList;

/**
 * Behaves like an <code>InterpolatedPalette</code>, but the colors
 * defining it are chosen uniformly at random.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 */
public class RandomPalette implements Palette {

    /**
     * What defines this <code>Palette</code>.
     */
	private ArrayList<Color> colors;

	/**
	 * Decorates an <code>InterpolatedPalette</code>.
	 */
	private final InterpolatedPalette palette;

	/**
	 *
	 * @param n
	 *            the number of colors defining the <code>RandomPalette</code>
	 * @throws IllegalArgumentException
	 *             if n is smaller than 2
	 */
	public RandomPalette(final int n) throws IllegalArgumentException {
		if (n < 2) {
            throw new IllegalArgumentException(
					"Need at least 2 colors");
        }

		for (int i = 0; i < n; i++) {
			colors.add(new Color(Math.random(), Math.random(), Math.random()));
        }

		palette = new InterpolatedPalette(colors);
	}

	@Override
	public final Color colorForIndex(final double index) {
		return palette.colorForIndex(index);
	}
}