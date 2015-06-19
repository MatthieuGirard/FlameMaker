package ch.epfl.flamemaker.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.FlameAccumulator;
import ch.epfl.flamemaker.geometry2d.Rectangle;
import ch.epfl.flamemaker.gui.ObservableFlameBuilder.Observer;

/**
 * Component of the fractal.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 *
 */
@SuppressWarnings("serial")
public class FlameBuilderPreviewComponent extends JComponent {

    /**
     * Instanciation of the builder pattern.
     */
	private final ObservableFlameBuilder builder;

	/**
	 * The accumulator.
	 */
	private FlameAccumulator accu;

	/**
	 * The background color.
	 */
	private final Color backgroundColor;

	/**
	 * The palette used to work with the colors.
	 */
	private final Palette palette;

	/**
	 * The region of the plane in which the fractal is computed.
	 */
	private final Rectangle frame;

	/**
	 * Directly correlated to the quality (and speed) of the software.
	 */
	private final int density;

	/**
	 *
	 * @param b
	 *            the fractal builder
	 * @param bgColor
	 *            the background color
	 * @param p
	 *            the palette
	 * @param region
	 *            the region of the plane in which the
	 *            fractal is computed
	 * @param d
	 *            the density / quality
	 */
	public FlameBuilderPreviewComponent(final ObservableFlameBuilder b,
			final Color bgColor, final Palette p,
			final Rectangle region, final int d) {

		this.builder = b;
		this.backgroundColor = bgColor;
		this.palette = p;
		this.frame = region;
		this.density = d;

		b.addObserver(new Observer() {

			@Override
			public void update() {
				repaint();
			}

		});
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200, 100);
	}

	@Override
	public void paintComponent(Graphics g1) {

		final Graphics2D g = (Graphics2D) g1;

		final BufferedImage image = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_RGB);

		final Rectangle newFrame = frame.expandToAspectRatio((double) getWidth()
				/ (double) getHeight());

		accu = builder.build().compute(newFrame, getWidth(), getHeight(),
				density);

		// Draws the fractal, pixel by pixel
		for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                image.setRGB(x, getHeight() - y - 1,
						accu.color(palette, backgroundColor, x, y)
								.asPackedRGB());
            }
        }

		g.drawImage(image, 0, 0, null);
	}
}
