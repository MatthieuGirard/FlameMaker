package ch.epfl.flamemaker.flame;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Transformation;

/**
 * Models a flame transformation.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 */
public final class FlameTransformation implements Transformation {

    /**
     * The identity flame transformation.
     * Used for adding transformations in <code>FlameMakerGUI</code>
     */
	public static final FlameTransformation IDENTITY = new FlameTransformation(
			AffineTransformation.IDENTITY, new double[] { 1, 0, 0, 0, 0, 0 });

	/**
	 * Affine component of the transformation.
	 */
	private final AffineTransformation affineTransformation;

	/**
	 * Weight array of the different transformations.
	 */
	private final double[] variationWeight;

	/**
	 *
	 * @param at
	 *            an affine transformation
	 * @param vw
	 *            an array containing the weights of the transformations
	 *
	 * @throws IllegalArgumentException
	 *             if the array has a bad size
	 */
	public FlameTransformation(final AffineTransformation at,
			final double[] vw) {

		this.affineTransformation = at;

		if (vw.length != Variation.ALL_VARIATIONS.size()) {
            throw new IllegalArgumentException(
					"Bad array size");
        } else {
            this.variationWeight = vw.clone();
        }
	}

	@Override
	public Point transformPoint(final Point p) {

		Point pFinal = Point.ORIGIN;

		for (int j = 0; j < Variation.ALL_VARIATIONS.size(); j++) {
			if (variationWeight[j] != 0) {
				final Point provisoire = Variation.ALL_VARIATIONS.get(j)
						.transformPoint(affineTransformation.transformPoint(p));
				pFinal = new Point(provisoire.x() * variationWeight[j]
						+ pFinal.x(), provisoire.y() * variationWeight[j]
						+ pFinal.y());
			}
        }

		return pFinal;
	}

	/**
	 * Simple Builder pattern.
	 */
	public static class Builder {

	    /**
	     * The affine transformation defining this transformation.
	     */
		private AffineTransformation affineTransformation;

		/**
		 * The array containing the weights of each variatons.
		 */
		private final double[] variationWeight;

		/**
		 *
		 * @param flameTransformation
		 *            a flame transformation
		 */
		public Builder(final FlameTransformation flameTransformation) {
			affineTransformation = flameTransformation.affineTransformation;
			variationWeight = flameTransformation.variationWeight.clone();
		}

		/**
		 *
		 * @return the affine transformation
		 */
		public AffineTransformation getAffineTransformation() {
			return affineTransformation;
		}

		/**
		 *
		 * @param variation
		 *            the variaton we wish to obtain the weight of
		 * @return the weight of this variation
		 */
		public double getVariationWeight(Variation variation) {
			return variationWeight[variation.index()];
		}

		/**
		 *
		 * @param newTransformation
		 *            the new transformation to use
		 */
		public void setAffineTransformation(
				AffineTransformation newTransformation) {

				affineTransformation = newTransformation;
		}

		/**
		 *
		 * @param variation a variation
		 * @param newWeight its new weight
		 */
		public void setVariationWeight(Variation variation,
				double newWeight) {

			variationWeight[variation.index()] = newWeight;
		}

		public FlameTransformation build() {
			return new FlameTransformation(affineTransformation, variationWeight);
		}
	}

}
