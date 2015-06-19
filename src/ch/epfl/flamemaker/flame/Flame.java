package ch.epfl.flamemaker.flame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Models fractals of type Flame.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 */
public final class Flame {

    /**
     * Need the software to be deterministic.
     */
    private static final long SEED = 2013;
    /**
     * Contains all transformations of type flame.
     */
	private final List<FlameTransformation> transformations;

	/**
	 *
	 * @param t a list of flame transformations
	 */
	public Flame(final List<FlameTransformation> t) {
		this.transformations = new ArrayList<FlameTransformation>();
		for (int i = 0; i < t.size(); i++) {
			this.transformations.add(t.get(i));
        }
	}

	/**
	 * Computes the fractal in the region given by the frame and
	 * stores the result in a new accumulator.
	 *
	 * @param frame a region of the plane where to compute the fractal
	 * @param width
	 *            accumulator's width
	 * @param height
	 *            accumulator's height
	 * @param density
	 *            the higher the density, the better the quality
	 *
	 * @return a new accumulator
	 */
	public FlameAccumulator compute(final Rectangle frame, final int width,
	        final int height, final int density) {

		final int maxIter = 20;
		Point p = Point.ORIGIN;

		final int n = transformations.size();

		int i;
		final int m = width * height * density;
		final Random random = new Random(SEED);

		double indexColor = 0;
		final double[] getIndexC = setIndexC(n + 1);

		// Generates a point from the origin
		for (int a = 0; a < maxIter; a++) {
			i = random.nextInt(n);
			p = transformations.get(i).transformPoint(p);

			indexColor = (getIndexC[i] + indexColor) / 2;
		}

		final FlameAccumulator.Builder preFlameAccumulator =
		        new FlameAccumulator.Builder(frame, width, height);

		// Generates points and keep them if they hit the accumulator
		for (int j = m; j > 0; j--) {
			i = random.nextInt(n);
			p = transformations.get(i).transformPoint(p);

			indexColor = (getIndexC[i] + indexColor) / 2;
			preFlameAccumulator.hit(p, indexColor);
		}

		return preFlameAccumulator.build();
	}

	/**
	 * @param lastC
	 *            the size of the returned array
	 * @return an array containing the color indexes
	 */
	private double[] setIndexC(final int lastC) {

		final double[] output = new double[lastC];
		output[0] = 0;
		output[1] = 1;
		int num = 1;
		int denom = 2;
		for (int i = 2; i < lastC; i++) {
			if (num > denom) {
				num = 1;
				denom *= 2;
			}
			output[i] = (double) num / (double) denom;
			num += 2;
		}
		return output;
	}

	/**
	 * Simple Builder pattern.
	 */
	public static class Builder {

	    /**
	     * Contains all the transformations.
	     */
		private final List<FlameTransformation> transformationsToBuild;

		/**
		 * @param flame a flame fractal to initialize the builder
		 */
		public Builder(final Flame flame) {
			transformationsToBuild = new ArrayList<FlameTransformation>();
			for (int i = 0; i < flame.transformations.size(); i++) {
				transformationsToBuild.add(flame.transformations.get(i));
            }
		}

		/**
		 *
		 * @param newTransformation the transformation to add
		 */
		public void addTransformation(final FlameTransformation
		        newTransformation) {
			transformationsToBuild.add(newTransformation);
		}

		/**
		 *
		 * @param index
		 *            the transformation's index to get
		 *
		 * @return the affine component of the transformation matching
		 *            the index
		 * @throws IndexOutOfBoundsException
		 *             if the index is invalid
		 */
		public final AffineTransformation affineTransformation(final int index)
				throws IndexOutOfBoundsException {
			if (!validIndex(index)) {
			    throw new IndexOutOfBoundsException("Bad index");
			}
			return new FlameTransformation.Builder(
					transformationsToBuild.get(index))
					.getAffineTransformation();
		}

		/**
		 *
		 * @param index an index we wish to validate
		 * @return <code>true</code> if the index is valid,
		 *            <code>false</code> otherwise
		 */
		private boolean validIndex(final int index) {
			return index >= 0 && index < transformationsToBuild.size();
		}

		/**
		 *
		 * @return a flame fractal
		 */
		public final Flame build() {
			return new Flame(transformationsToBuild);
		}

		/**
		 *
		 * @param index
		 *            the index of the transformation to remove
		 *
		 * @throws IndexOutOfBoundsException
		 *             if the index is bad
		 */
		public void removeTransformation(int index)
				throws IndexOutOfBoundsException {
			if (!validIndex(index)) {
			    throw new IndexOutOfBoundsException("Bad index");
			}
			transformationsToBuild.remove(index);
		}

		/**
		 *
		 * @param index
		 *            the index of the transformation to modify
		 * @param newTransformation
		 *            an affine transformation
		 *
		 * @throws IndexOutOfBoundsException
		 *             if the index is bad
		 */
		public void setAffineTransformation(final int index,
				final AffineTransformation newTransformation)
				throws IndexOutOfBoundsException {
			if (!validIndex(index)) {
			    throw new IndexOutOfBoundsException("Bad index");
			}

			final FlameTransformation.Builder toSet =
			        new FlameTransformation.Builder(
					transformationsToBuild.get(index));
			toSet.setAffineTransformation(newTransformation);

			transformationsToBuild.set(index, toSet.build());
		}

		/**
		 *
		 * @param index the index of the transformation to edit
		 * @param variation a variation
		 * @param newWeight a new weight
		 *
		 * @throws IndexOutOfBoundsException
		 *             if the index is bad
		 */
		public void setVariationWeight(final int index,
		        final Variation variation, final double newWeight)
		                throws IndexOutOfBoundsException {
		    if (!validIndex(index)) {
                throw new IndexOutOfBoundsException("Bad index");
            }

			final FlameTransformation.Builder toSet =
			        new FlameTransformation.Builder(
					transformationsToBuild.get(index));
			toSet.setVariationWeight(variation, newWeight);

			transformationsToBuild.set(index, toSet.build());

		}

		/**
		 *
		 * @return the number of transformations defining the fractal
		 */
		public final int transformationCount() {
			return transformationsToBuild.size();
		}

		/**
		 * @param index the index of the transformation
		 * @param variation a variation
		 * @return the weight of the given variaton for the
		 *            given Flame transformation
		 * @throws IndexOutOfBoundsException
		 *             if the index was bad
		 */
		public final double variationWeight(final int index,
		        final Variation variation)
				throws IndexOutOfBoundsException {
			validIndex(index);
			return new FlameTransformation.Builder(
					transformationsToBuild.get(index))
					.getVariationWeight(variation);
		}
	}
}
