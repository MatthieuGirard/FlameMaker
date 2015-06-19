package ch.epfl.flamemaker.geometry2d;

/**
 * Models a simple 2d Point.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 */
public final class Point {

	/**
	 * The origin.
	 */
	public static final Point ORIGIN = new Point(0, 0);

	/**
	 * The point's coordinates.
	 */
	private final double x, y;

	/**
	 * Speed optimization.
	 */
	private final double rPow2;

	/**
	 *
	 * @param pX
	 *            horizontal coordinate
	 * @param pY
	 *            verticals coordinate
	 */
	public Point(final double pX, final double pY) {
		this.x = pX;
		this.y = pY;
		rPow2 = Math.pow(r(), 2);
	}

	/**
	 * @return le rayon de la coordonnée polaire au carré
	 */
	public double rPow2() {
		return rPow2;
	}

	/**
	 *
	 * @return the radius (polar coordinates)
	 */
	public double r() {
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * @return an (x, y) representation of the point
	 */
	@Override
	public String toString() {
		return "(" + x() + "," + y() + ")";
	}

	/**
	 *
	 * @return the angle (polar coordinate)
	 */
	public double theta() {
		return Math.atan2(y, x);
	}

	/**
	 *
	 * @return the x coordinate of the point
	 */
	public double x() {
		return x;
	}

	/**
	 *
	 * @return the y coordinate of the point
	 */
	public double y() {
		return y;
	}

}