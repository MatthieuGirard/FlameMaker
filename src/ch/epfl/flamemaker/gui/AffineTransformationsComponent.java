package ch.epfl.flamemaker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;
import ch.epfl.flamemaker.gui.ObservableFlameBuilder.Observer;

/**
 * The component of affine transformations
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 *
 */
@SuppressWarnings("serial")
public class AffineTransformationsComponent extends JComponent {

    /**
     * An instanciation of the builder pattern.
     */
	private final ObservableFlameBuilder builder;

	/**
	 * The region of the plane where the fractal is displayed.
	 */
	private final Rectangle frame;

	/**
	 * Index of the selected transformation (displayed in red int he GUI).
	 */
	private int redTransIndex;

	/**
	 *
	 * @param b
	 *            the fractal builder
	 * @param f
	 *            the region of the plane in which the fractal has been computed
	 */
	public AffineTransformationsComponent(final ObservableFlameBuilder b,
			final Rectangle f) {
		this.builder = b;
		this.frame = f;
		b.addObserver(new Observer() {

			@Override
			public void update() {
				repaint();
			}

		});
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(300, 200);
	}

	@Override
	public void paintComponent(Graphics g1) {

		final Graphics2D g = (Graphics2D) g1;

		final Rectangle newFrame = frame.expandToAspectRatio((double) getWidth()
				/ (double) getHeight());

		final AffineTransformation adapt = AffineTransformation
				.newScaling(getWidth() / newFrame.width(),
						getHeight() / newFrame.height())
				.composeWith(
						AffineTransformation.newTranslation(-newFrame.center()
								.x(), -newFrame.center().y()))
				.composeWith(
						AffineTransformation.newTranslation(-newFrame.left(),
								-newFrame.top()));

		// Vertical lines of the grid
		for (int i = 0; i < newFrame.width(); i++) {
			final double j = Math.floor(newFrame.left()) + i + 1;

			if (j == 0) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(new Color((float) 0.9, (float) 0.9, (float) 0.9));
            }

			Point p = new Point(j, 0);
			p = adapt.transformPoint(p);

			g.draw(new Line2D.Double(p.x(), 0, p.x(), getHeight()));
		}

		// Horizontal lines of the grid
		for (int i = 0; i < newFrame.height(); i++) {
			final double j = Math.floor(newFrame.bottom()) + i + 1;

			if (j == 0) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(new Color((float) 0.9, (float) 0.9, (float) 0.9));
            }

			Point p = new Point(0, j);
			p = adapt.transformPoint(p);

			g.draw(new Line2D.Double(0, -p.y(), getWidth(), -p.y()));
		}

		// Draw arrows
		for (int i = 0; i < builder.transformationCount(); i++) {
            if (i != redTransIndex) {
                drawAT(g, builder.affineTransformation(i), false, adapt);
            }
        }

		// Draw the selected transformation arrow
		drawAT(g, builder.affineTransformation(redTransIndex), true, adapt);

	}

	/**
	 * Draw an affine transformation
	 *
	 * @param g
	 *            the graphical context
	 * @param tAffine
	 *            la transformation affine à dessiner
	 * @param toHighlight
	 *            Si définit à <code>true</code>, l'objet dessiné sera mit en
	 *            évidence en rouge. Une transformation mise en évidence à la
	 *            garantie d'être toujours visible (dessinée en dernier).
	 * @param adapt
	 *            la transformation affine permettant de passer des coordonnées
	 *            du plan à celles du composant
	 */
	private void drawAT(final Graphics2D g, final AffineTransformation tAffine,
			final boolean toHighlight, final AffineTransformation adapt) {
		// Create base arrows
		final List<Point> arrays = Arrays.asList(new Point(-1, 0), new Point(1, 0),
				new Point(0.9, 0.1), new Point(0.9, -0.1), null, null, null,
				null);

		for (int j = 0; j < 4; j++) {
			final Point p = AffineTransformation.newRotation(Math.PI / 2.0)
					.transformPoint(arrays.get(j));
			arrays.set(j + 4, new Point(p.x(), p.y()));
		}

		// Transforms the arrows
		for (int j = 0; j < arrays.size(); j++) {
			arrays.set(j, tAffine.transformPoint(arrays.get(j)));
			arrays.set(j, adapt.transformPoint(arrays.get(j)));
		}

		// Defines color of the drawing
		if (toHighlight) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }

		// Display arrows
		for (int j = 0; j < 5; j += 4) {
			g.draw(new Line2D.Double(arrays.get(j).x(), -arrays.get(j).y(),
					arrays.get(j + 1).x(), -arrays.get(j + 1).y()));
			g.draw(new Line2D.Double(arrays.get(j + 2).x(), -arrays.get(j + 2)
					.y(), arrays.get(j + 1).x(), -arrays.get(j + 1).y()));
			g.draw(new Line2D.Double(arrays.get(j + 3).x(), -arrays.get(j + 3)
					.y(), arrays.get(j + 1).x(), -arrays.get(j + 1).y()));
		}
	}

	/**
	 *
	 * @return the index of the selected transformation
	 */
	public final int getRedTransIndex() {
		return redTransIndex;
	}

	/**
	 *
	 * @param newIndex
	 *            the index of the new selected transformation
	 */
	public final void setRedTransIndex(final int newIndex) {
		redTransIndex = newIndex;
		this.repaint();
	}
}
