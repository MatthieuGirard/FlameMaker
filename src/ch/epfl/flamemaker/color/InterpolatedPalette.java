package ch.epfl.flamemaker.color;

import java.util.ArrayList;
import java.util.List;

/**
 * This kind of <code>Palette</code> interpolates between several colors.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 */
public class InterpolatedPalette implements Palette {

    /**
     * Holds the colors of the palette.
     */
	private final ArrayList<Color> colors;

	/**
	 * Creates a <code>Palette</code> that interpolates between the colors
	 * of the given list.
	 *
	 * @param c
	 *            a list of colors
	 *
	 * @throws IllegalArgumentException
	 *             if the list's size is smaller than 2
	 */
	public InterpolatedPalette(final List<Color> c)
			throws IllegalArgumentException {
		if (c.size() < 2) {
            throw new IllegalArgumentException(
					"Need at least two colors in the list");
        }

		// deep copy
		this.colors = new ArrayList<Color>();
		for (int i = 0; i < c.size(); i++) {
            this.colors.add(c.get(i));
        }

	}

	@Override
	public final Color colorForIndex(final double index)
	        throws IllegalArgumentException {
		if (!(index >= 0 && index <= 1)) {
			throw new IllegalArgumentException("Index not in the "
			        + "interval [0, 1]");
		}

		final double realIndex = index * (colors.size() - 1);

		if (realIndex == 0) {
            return colors.get(0);
        } else if (realIndex == colors.size() - 1) {
            return colors.get(colors.size() - 1);
        } else {
			final double proportion = realIndex - Math.floor(realIndex);
			final Color bottom = colors.get((int) Math.floor(realIndex));
			final Color top = colors.get((int) Math.floor(realIndex) + 1);

			return top.mixWith(bottom, proportion);
		}

	}
}
