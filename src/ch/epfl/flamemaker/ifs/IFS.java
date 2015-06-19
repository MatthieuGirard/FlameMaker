package ch.epfl.flamemaker.ifs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Iterated Function System.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 *
 */
public final class IFS {

    /**
     * Contains all transformations.
     */
	private final List<AffineTransformation> transformations;

	/**
	 * Builds a fractal defined by the given transformations.
	 * @param t a list of all the transformations
	 */
	public IFS(final List<AffineTransformation> t) {

		this.transformations = new ArrayList<AffineTransformation>();

		for (int i = 0; i < t.size(); i++) {
            this.transformations.add(t.get(i));
        }
	}

	/**
	 * Computes with the chaos algorithm
     * (<a href="http://fractalfoundation.org/resources/what-is-chaos-theory/">
     * http://fractalfoundation.org/resources/what-is-chaos-theory/</a>) the
     * fractal in the frame.
	 * @param frame where to compute the fractal
	 * @param width accumulator's width
	 * @param height accumulator's height
	 * @param density the density / quality
	 * @return the new accumulator
	 */
	public IFSAccumulator compute(final Rectangle frame,
	        final int width, final int height,
			final int density) {

		Point p = new Point(0, 0);
		final int n = transformations.size();
		final Random random = new Random();

		for (int a = 0; a < 20; a++) {
			final int i = random.nextInt(n);
			p = transformations.get(i).transformPoint(p);
		}

		final IFSAccumulatorBuilder preIFSAccumulator =
		        new IFSAccumulatorBuilder(
				frame, width, height);

		for (int m = width * height * density; m > 0; m--) {
			final int i = random.nextInt(n);
			p = transformations.get(i).transformPoint(p);
			preIFSAccumulator.hit(p);
		}

		return preIFSAccumulator.build();
	}

}