package ch.epfl.flamemaker.flame;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

@Deprecated
public class FlamePGMMaker {

    /**
     * Deprecated at this stage of the project.
     * @param args for console usage
     */
	public static void main(final String[] args) {
		System.out.println("Début du programme...");
		final double[] vWeightA1 = { 0.5, 0, 0.5, 0, 0, 0 };
		final FlameTransformation a1 = new FlameTransformation(
				new AffineTransformation(0.7124807, -0.4113509, -0.3,
						0.4113513, 0.7124808, -0.7), vWeightA1);

		final double[] vWeightA2 = { 1, 0, 0.1, 0, 0, 0 };
		final FlameTransformation a2 = new FlameTransformation(
				new AffineTransformation(0.3731079, -0.6462417, 0.4, 0.6462414,
						0.3731076, 0.3), vWeightA2);

		final double[] vWeightA3 = { 1, 0, 0, 0, 0, 0 };
		final FlameTransformation a3 = new FlameTransformation(
				new AffineTransformation(0.0842641, -0.314478, -0.1, 0.314478,
						0.0842641, 0.3), vWeightA3);

		final Rectangle frame = new Rectangle(new Point(0.1, 0.1), 3, 3);
		final List<FlameTransformation> flameTransformationArray =
		        new ArrayList<FlameTransformation>();

		flameTransformationArray.add(a1);
		flameTransformationArray.add(a2);
		flameTransformationArray.add(a3);

		final Flame fractale = new Flame(flameTransformationArray);

		try {
			writeIn(fractale.compute(frame, 1920, 1080, 50));
		} catch (final FileNotFoundException e) {
			System.err.println("fichier introuvable");
		}
	}

	/**
	 * Creates a fractal in a file with format PGM.
	 * @param accumulator the accumulator
	 * @throws FileNotFoundException if an error occurs with I/O
	 */
	private static void writeIn(final FlameAccumulator accumulator)
			throws FileNotFoundException {

		System.out.println("Ecriture du fichier en cours...");

		final PrintStream fichier = new PrintStream("TurbulenceRemix.PGM");

		fichier.println("P2");
		fichier.println(accumulator.width() + " " + accumulator.height());
		fichier.println("100");

		for (int j = accumulator.height() - 1; j >= 0; j--) {

			for (int i = 0; i < accumulator.width(); i++) {
                fichier.print((int) (accumulator.intensity(i, j) * 100) + " ");
            }
			fichier.print("\n");
		}
		fichier.print("\n");
		fichier.close();
		System.out.println("\n>>Fin du programme");
	}
}
