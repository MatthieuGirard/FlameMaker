package ch.epfl.flamemaker.geometry2d;

/**
 * Models a 2D rectangle.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 */
public final class Rectangle {

	/**
	 * The rectangle's center.
	 */
	private final Point center;

	/**
	 * The rectangle's sizes.
	 */
	private final double width, height;

	/**
	 *
	 * @param c
	 *            rectangle's center
	 * @param w
	 *            rectangle's width
	 * @param h
	 *            rectangle's height
	 *
	 * @throws IllegalArgumentException
	 *             if the width or the height isn't strictly positive
	 */
	public Rectangle(final Point c, final double w, final double h) {
		if (w < 0 || h < 0) {
            throw new IllegalArgumentException(
					"Bad width or height");
        }
		this.center = c;
		this.width = w;
		this.height = h;
	}

	/**
	 * @return a simple representation of the form
	 *         "((centerX, centerY), width, height)"
	 */
	@Override
	public String toString() {
		return "(" + center.toString() + ", " + width + ", " + height + ")";
	}

	/**
	 * @param p
	 *            a point
	 * @return <code>true</code> iff p is inside the rectangle
	 */
	public boolean contains(final Point p) {
		return p.x() >= left() && p.x() < right() && p.y() >= bottom()
				&& p.y() < top();
	}

	/**
	 *
	 * @return the smallest x coordinate of the rectangle
	 */
	public double left() {
		return center.x() - width / 2;
	}

	/**
	 *
	 * @return the biggest x coordinate of the rectangle
	 */
	public double right() {
		return center.x() + width / 2;
	}

	/**
	 * @return the smallest y coordinate of the rectangle
	 */
	public double bottom() {
		return center.y() - height / 2;
	}

	/**
	 *
	 * @return the biggest y coordinate of the rectangle
	 */
	public double top() {
		return center.y() + height / 2;
	}

	/**
	 * @param aspectRatio
	 *            the value with / height
	 * @return
	 *         the smallest rectangle having the same center as the receptor,
	 *             the same aspectratio and containing the receptor.
	 * @throws IllegalArgumentException
	 *             if the ratio is not strictly positive
	 * */
	public Rectangle expandToAspectRatio(final double aspectRatio)
			throws IllegalArgumentException {
		if (aspectRatio <= 0) {
            throw new IllegalArgumentException(
					"Ratio must be strictly positive");
        }

		double lF = width;
		double hF = height;

		if (aspectRatio < this.aspectRatio()) {
			hF = width / aspectRatio;
		} else if (aspectRatio > this.aspectRatio()) {
			lF = aspectRatio * height;
		} else {
			lF = width;
			hF = height;
		}

		return new Rectangle(center, lF, hF);
	}

	/**
	 *
	 * @return recatangle's width
	 */
	public double width() {
		return width;
	}

	/**
	 *
	 * @return recatangle's height
	 */
	public double height() {
		return height;
	}

	/**
	 * @return the aspect ratio of the rectangle
	 */
	public double aspectRatio() {
		return width / height;
	}

	/**
	 * @return the center of the rectangle
	 */
	public Point center() {
		return center;
	}
}
