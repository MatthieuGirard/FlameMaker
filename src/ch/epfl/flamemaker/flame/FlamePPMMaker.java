package ch.epfl.flamemaker.flame;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * Fractal maker.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 */
public class FlamePPMMaker {

    /**
     * @param args for console use
     */
	public static void main(final String[] args) {

		System.out.println("Début du programme...");
		final List<Color> listColor = new ArrayList<Color>();
		listColor.add(Color.RED);
		listColor.add(Color.GREEN);
		listColor.add(Color.BLUE);

		createSharkFin(listColor);
		createTurbulence(listColor);

		System.out.println("\nProgramme terminé");
	}

	/**
	 * Creates a shark-fin fractal.
	 *
	 * @param listColor
	 *            a color list
	 */
	private static void createSharkFin(final List<Color> listColor) {
		final String fractaleName = "shark-fin";
		System.out.println("Création de la fractale " + fractaleName
				+ ".ppm...");
		final FlameTransformation a1 = new FlameTransformation(
				new AffineTransformation(-0.4113504, -0.7124804, -0.4,
						0.7124795, -0.4113508, 0.8), new double[] { 1.0, 0.1,
						0, 0, 0, 0 });

		final FlameTransformation a2 = new FlameTransformation(
				new AffineTransformation(-0.3957339, 0, -1.6, 0, -0.3957337,
						0.2), new double[] { 0, 0, 0, 0, 0.8, 1 });

		final FlameTransformation a3 = new FlameTransformation(
				new AffineTransformation(0.4810169, 0, 1, 0, 0.4810169, 0.9),
				new double[] { 1, 0, 0, 0, 0, 0 });

		final Rectangle frame = new Rectangle(new Point(-0.25, 0), 5, 4);
		final List<FlameTransformation> flameTransformationArray =
		        new ArrayList<FlameTransformation>();

		flameTransformationArray.add(a1);
		flameTransformationArray.add(a2);
		flameTransformationArray.add(a3);

		final Flame sharkFin = new Flame(flameTransformationArray);
		// Flame SharkFin = new Flame

		try {
			writeIn(sharkFin.compute(frame, 500, 400, 50),
					new InterpolatedPalette(listColor), fractaleName + ".ppm");
		} catch (final FileNotFoundException e) {
			System.err.println("fichier introuvable");
		}
	}

	/**
	 * Creates a turbulence fractal.
	 *
	 * @param listColor
	 *            a color list
	 */
	private static void createTurbulence(final List<Color> listColor) {
		final String fractaleName = "turbulence";
		System.out.println("Création de la fractale " + fractaleName
				+ ".ppm...");
		final FlameTransformation a1 = new FlameTransformation(
				new AffineTransformation(0.7124807, -0.4113509, -0.3,
						0.4113513, 0.7124808, -0.7), new double[] { 0.5, 0, 0,
						0.4, 0, 0 });

		final FlameTransformation a2 = new FlameTransformation(
				new AffineTransformation(0.3731079, -0.6462417, 0.4, 0.6462414,
						0.3731076, 0.3), new double[] { 1, 0, 0.1, 0, 0, 0 });

		final FlameTransformation a3 = new FlameTransformation(
				new AffineTransformation(0.0842641, -0.314478, -0.1, 0.314478,
						0.0842641, 0.3), new double[] { 1, 0, 0, 0, 0, 0 });

		final Rectangle frame = new Rectangle(new Point(0.1, 0.1), 3, 3);
		final List<FlameTransformation> flameTransformationArray =
		        new ArrayList<FlameTransformation>();

		flameTransformationArray.add(a1);
		flameTransformationArray.add(a2);
		flameTransformationArray.add(a3);

		final Flame turbulence = new Flame(flameTransformationArray);

		try {
			writeIn(turbulence.compute(frame, 500, 500, 50),
					new InterpolatedPalette(listColor), fractaleName + ".ppm");
		} catch (final FileNotFoundException e) {
			System.err.println("fichier introuvable");
		}
	}

	/**
	 *
	 * @param accumulator
	 *            the accumulator to use
	 * @param palette
	 *            the palette to use
	 * @param filename
	 *            the file name, without its extension
	 *
	 * @throws FileNotFoundException
	 *             if an error happened with I/O
	 */
	private static void writeIn(final FlameAccumulator accumulator,
	        final Palette palette, final String filename)
	                throws FileNotFoundException {

		System.out.println("	Ecriture du fichier en cours...");

		final PrintStream fichier = new PrintStream(filename);

		fichier.println("P3");
		fichier.println(accumulator.width() + " " + accumulator.height());
		fichier.println("100");

		for (int j = accumulator.height() - 1; j >= 0; j--) {

			for (int i = 0; i < accumulator.width(); i++) {

				fichier.print(Color.sRGBEncode(
						accumulator.color(palette, Color.BLACK, i, j).red(),
						100) + " ");
				fichier.print(Color.sRGBEncode(
						accumulator.color(palette, Color.BLACK, i, j).green(),
						100) + " ");
				fichier.print(Color.sRGBEncode(
						accumulator.color(palette, Color.BLACK, i, j).blue(),
						100) + " ");

			}
			fichier.println();
		}
		fichier.close();
	}
}
