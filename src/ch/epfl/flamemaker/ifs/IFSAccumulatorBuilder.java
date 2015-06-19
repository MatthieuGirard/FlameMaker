package ch.epfl.flamemaker.ifs;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class IFSAccumulatorBuilder {

	private final Rectangle frame;
	private final boolean[][] preAccumulator;
	private AffineTransformation transformations; // t

	public IFSAccumulatorBuilder(Rectangle frame, int width, int height) {

		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException(
					"La largeur et la hauteur doivent être strictement positives");

		transformations = AffineTransformation.newScaling(
				width / frame.width(), height / frame.height());
		transformations = transformations.composeWith(AffineTransformation
				.newTranslation(-frame.left(), -frame.bottom()));

		preAccumulator = new boolean[width][height];
		this.frame = frame;

	}

	/**
	 * @return un accumulateur contenant les points collectés jusqu'à présent
	 */
	public IFSAccumulator build() {

		return new IFSAccumulator(preAccumulator);
	}

	/**
	 * met à vrai la case de l'accumulateur correspondant au point du plan
	 * donné. Ne fait rien si le point est en dehors de la région du plan
	 * couverte par l'accumulateur, c-à-d le cadre frame passé au constructeur
	 * de cette classe
	 */
	public void hit(Point p) {

		if (frame.contains(p)) {

			final Point q = transformations.transformPoint(p);

			final int newX = (int) Math.floor(q.x());
			final int newY = (int) Math.floor(q.y());

			// System.out.println(newX + ", " + newY);

			preAccumulator[newX][newY] = true;
		}
	}
}