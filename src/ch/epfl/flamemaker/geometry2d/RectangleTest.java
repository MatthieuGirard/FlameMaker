package ch.epfl.flamemaker.geometry2d;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RectangleTest {
	private static double DELTA = 0.000000001;

	@Test
	public void testAspectRatio() {
		final Rectangle r = new Rectangle(Point.ORIGIN, 16, 9);
		assertEquals(r.aspectRatio(), 16.0 / 9.0, DELTA);
	}

	@Test
	public void testBottom() {
		final Rectangle r = new Rectangle(Point.ORIGIN, 2, 2);
		assertEquals(r.bottom(), -1, DELTA);
	}

	@Test
	public void testCenter() {
		final Rectangle r = new Rectangle(new Point(1, 2), 1, 1);
		final Point c = r.center();
		assertEquals(c.x(), 1, DELTA);
		assertEquals(c.y(), 2, DELTA);
	}

	@Test
	public void testContains() {
		final Rectangle r = new Rectangle(Point.ORIGIN, 2, 2);
		for (double x = -1; x < 1; x += 0.1)
			for (double y = -1; y < 1; y += 0.1)
				assertTrue(r.contains(new Point(x, y)));
		assertTrue(r.contains(new Point(0.99999999, 0.99999999)));
		for (double x = -2; x <= 2; x += 0.1)
			assertFalse(r.contains(new Point(x, 1)));
		for (double y = -2; y <= 2; y += 0.1)
			assertFalse(r.contains(new Point(1, y)));
	}

	@Test
	public void testExpandToAspectRatio() {
		final Rectangle r = new Rectangle(Point.ORIGIN, 1, 1);
		final double[] newAspectRatios = new double[] { 0.5, 1, 2 };

		for (final double newAR : newAspectRatios) {
			final Rectangle r1 = r.expandToAspectRatio(newAR);

			// The expanded rectangle must:
			// 1. have the requested aspect ratio
			assertEquals(newAR, r1.aspectRatio(), DELTA);

			// 2. have the same center as the original
			assertEquals(r.center().x(), r1.center().x(), DELTA);
			assertEquals(r.center().y(), r1.center().y(), DELTA);

			// 3. have at least one side of the same length
			assertTrue(Math.abs(r.width() - r1.width()) <= DELTA
					|| Math.abs(r.height() - r1.height()) <= DELTA);

			// 4. not have any side of a shorter length.
			assertFalse(r1.width() < r.width());
			assertFalse(r1.height() < r.height());
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExpandToAspectRatioNegativeRatio() {
		new Rectangle(Point.ORIGIN, 1, 1).expandToAspectRatio(-12);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExpandToAspectRatioZeroRatio() {
		new Rectangle(Point.ORIGIN, 1, 1).expandToAspectRatio(0);
	}

	@Test
	public void testHeight() {
		final Rectangle r = new Rectangle(Point.ORIGIN, 1, 10);
		assertEquals(r.height(), 10, DELTA);
	}

	@Test
	public void testLeft() {
		final Rectangle r = new Rectangle(Point.ORIGIN, 2, 2);
		assertEquals(r.left(), -1, DELTA);
	}

	@Test
	public void testRectangle() {
		new Rectangle(Point.ORIGIN, 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRectangleNegativeHeight() {
		new Rectangle(Point.ORIGIN, 10, -0.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRectangleNegativeWidth() {
		new Rectangle(Point.ORIGIN, -0.1, 10);
	}

	@Test
	public void testRight() {
		final Rectangle r = new Rectangle(Point.ORIGIN, 2, 2);
		assertEquals(r.right(), 1, DELTA);
	}

	@Test
	public void testTop() {
		final Rectangle r = new Rectangle(Point.ORIGIN, 2, 2);
		assertEquals(r.top(), 1, DELTA);
	}

	@Test
	public void testWidth() {
		final Rectangle r = new Rectangle(Point.ORIGIN, 10, 1);
		assertEquals(r.width(), 10, DELTA);
	}

	// Note: we do not test toString, since the string representation of
	// floating-point numbers is hard to predict.
}
