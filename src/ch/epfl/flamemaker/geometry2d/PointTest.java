package ch.epfl.flamemaker.geometry2d;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PointTest {

	private static double DELTA = 0.000000001;

	@Test
	public void testPoint() {
		new Point(5, 6);
	}

	@Test
	public void testR() {
		final Point p = new Point(3, 4);
		assertEquals(p.r(), 5, DELTA);
	}

	@Test
	public void testTheta() {
		final Point p = new Point(2, 2);
		assertEquals(p.theta(), Math.PI / 4, DELTA);

	}

	@Test
	public void testX() {
		final Point p = new Point(-5, 7);
		assertEquals(p.x(), -5, DELTA);
	}

}
