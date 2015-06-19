package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Models a Flame Accumulator.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 *
 */
public final class FlameAccumulator {

    /**
     * Bidimensinal array containing the sum of the color index for each cell.
     */
	private final double[][] colorIndexSum;

	/**
	 * Used to compute the intensity.
	 */
	private final double denominator;

	/**
	 * Counts the number of hits (points inside).
	 */
	private final int[][] hitCount;

	/**
	 *
	 * @param hc
	 *            Bidimensional array counting the number of hits per cell
	 * @param cis
	 *            Bidimensinal array containing the sum of the
	 *             color index for each cell
	 */
	private FlameAccumulator(final int[][] hc, final double[][] cis) {
		this.hitCount = new int[hc.length][hc[0].length];
		int max = 0;
		for (int i = 0; i < hc.length; i++) {
            for (int j = 0; j < hc[0].length; j++) {
				if (hc[i][j] > max) {
                    max = hc[i][j];
                }
				this.hitCount[i][j] = hc[i][j];
			}
        }

		denominator = Math.log(max + 1);

		this.colorIndexSum = new double[cis.length][cis[0].length];
		for (int i = 0; i < cis.length; i++) {
            for (int j = 0; j < cis[0].length; j++) {
                this.colorIndexSum[i][j] = cis[i][j];
            }
        }
	}


	/**
	 *
	 * @param palette the palette used to work with the colors
	 * @param background the color used as the background
	 * @param x the accumulator's x coordinate
	 * @param y the accumulator's y coordinate
	 * @return the color of the accumulator cell at the given coordinates
	 * @throws IndexOutOfBoundsException if (x, y) coordinates are invalid
	 */
	public Color color(final Palette palette,
	        final Color background, final int x, final int y)
			throws IndexOutOfBoundsException {
		if (!(x >= 0 && x <= width() && y >= 0 && y <= height())) {
            throw new IndexOutOfBoundsException("Bad coordinates");
        }
		double average;
		if (hitCount[x][y] == 0) {
            return background;
        } else {
			average = colorIndexSum[x][y] / hitCount[x][y];
			return palette.colorForIndex(average).mixWith(background,
				intensity(x, y));
		}
	}

	/**
	 * @param x
	 *            the horizontal coordinate
	 * @param y
	 *            the vertical coordinate
	 * @return the intensity of this cell
	 * @throws IndexOutOfBoundsException
	 *             si l'une des deux coordonnées est invalide
	 */
	public double intensity(final int x, final int y) {

		if (x < 0 || y < 0 || x > width() || y > height()) {
            throw new IndexOutOfBoundsException(
					"Bad coordinates");
        }
		return Math.log(hitCount[x][y] + 1) / denominator;
	}

	/**
	 * @return the width, in the number of cells, of the accumulator
	 */
	public int height() {
		return hitCount[0].length;
	}

	/**
	 * @return the height, in the number of cells, of the accumulator
	 */
	public int width() {
		return hitCount.length;
	}

	/**
	 * Simple Builder pattern.
	 *
	 */
	public static class Builder {

	    /**
	     * Bidimensinal array containing the sum of the color index
	     *     for each cell.
	     */
		private final double[][] colorIndexSum;

		/**
		 * The region of the plane where to compute the fractal.
		 */
		private final Rectangle frame;

		/**
		 * Counts the number of hits.
		 */
		private final int[][] preAccumulator;

		/**
		 * Contains all the transformations defining this fractal.
		 */
		private final AffineTransformation transformations;

		/**
		 *
		 * @param f
		 *            the region of the plane where to compute the fractal
		 * @param width
		 *            the fractal's width
		 * @param height
		 *            the fractal's height
		 *
		 * @throws IllegalArgumentException
		 *             If the height or the width is not strictly positive.
		 */
		public Builder(final Rectangle f, final int width, final int height) {

			if (!(width > 0 && height > 0)) {
                throw new IllegalArgumentException(
						"La largeur et la hauteur doivent être positives");
            }
			transformations = AffineTransformation.newScaling(
					width / f.width(), height / f.height())
					.composeWith(
							AffineTransformation.newTranslation(-f.left(),
									-f.bottom()));
			this.frame = f;
			preAccumulator = new int[width][height];
			colorIndexSum = new double[width][height];
		}

		/**
		 * @return a flame accumulator containing all the points
		 *    connellected to this point.
		 */
		public final FlameAccumulator build() {
			return new FlameAccumulator(preAccumulator, colorIndexSum);
		}

		/**
		 * Increments the counter in case of a hit.
		 *
		 * @param p a point to hit
		 * @param indexColor a color index
		 */
		public final void hit(final Point p, final double indexColor) {
			if (frame.contains(p)) {
				final Point q = transformations.transformPoint(p);
				final int newX = (int) Math.floor(q.x());
				final int newY = (int) Math.floor(q.y());
				preAccumulator[newX][newY]++;

				colorIndexSum[newX][newY] += indexColor;
			}
		}
	}
}
