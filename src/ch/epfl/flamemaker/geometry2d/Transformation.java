package ch.epfl.flamemaker.geometry2d;

/**
 * Models the concept of a transformation.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 */
public interface Transformation {
	/**
	 *
	 * @param p
	 *            a point
	 * @return the transformed point
	 */
	Point transformPoint(Point p);
}
