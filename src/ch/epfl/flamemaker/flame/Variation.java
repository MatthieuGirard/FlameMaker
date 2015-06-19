package ch.epfl.flamemaker.flame;

import java.util.Arrays;
import java.util.List;

import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Transformation;

/**
 * Models variations.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 */
public abstract class Variation implements Transformation {

	/**
	 * Contains every variations.
	 */
	public static final List<Variation> ALL_VARIATIONS = Arrays.asList(

			new Variation(0, "Linear") {
				@Override
				public Point transformPoint(Point p) {
					return new Point(p.x(), p.y());
				}
			},

			new Variation(1, "Sinusoidal") {
				@Override
				public Point transformPoint(Point p) {
					return new Point(Math.sin(p.x()), Math.sin(p.y()));
				}
			},

			new Variation(2, "Spherical") {
				@Override
				public Point transformPoint(Point p) {
					final double newX = p.x() / p.rPow2();
					final double newY = p.y() / p.rPow2();

					return new Point(newX, newY);
				}
			},

			new Variation(3, "Swirl") {
				@Override
				public Point transformPoint(Point p) {
					double newX = p.x() * Math.sin(p.rPow2());
					newX -= p.y() * Math.cos(p.rPow2());

					double newY = p.x() * Math.cos(p.rPow2());
					newY += p.y() * Math.sin(p.rPow2());

					return new Point(newX, newY);
				}
			},

			new Variation(4, "Horseshoe") {
				@Override
				public Point transformPoint(Point p) {
					double newX = (p.x() - p.y()) * (p.x() + p.y());
					newX /= p.r();

					double newY = 2 * p.x() * p.y();
					newY /= p.r();

					return new Point(newX, newY);
				}
			},

			new Variation(5, "Bubble") {
				@Override
				public Point transformPoint(Point p) {
					double newX = 4 * p.x();
					newX /= p.rPow2() + 4;

					double newY = 4 * p.y();
					newY /= p.rPow2() + 4;

					return new Point(newX, newY);
				}
			});

	/**
	 * Variation index.
	 */
	private final int index;

	/**
	 * Variation name.
	 */
	private final String name;

	/**
	 * @param i
	 *            variation's index
	 * @param n
	 *            variation's name
	 */
	private Variation(final int i, final String n) {
		this.index = i;
		this.name = n;
	}

	@Override
	abstract public Point transformPoint(Point p);

	/**
	 *
	 * @return variation's index
	 */
	public int index() {
		return index;
	}

	/**
	 *
	 * @return variation's name
	 */
	public String name() {
		return name;
	}
}