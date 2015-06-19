package ch.epfl.flamemaker.ifs;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

public class testFractales {

	public static void main(String[] args) {


		final AffineTransformation a1 = new AffineTransformation(0, 0, 0, 0,
				0.16, 0);
		final AffineTransformation a2 = new AffineTransformation(0.2, -0.26, 0,
				0.23, 0.22, 1.6);
		final AffineTransformation a3 = new AffineTransformation(-0.15, 0.28,
				0, 0.26, 0.24, 0.44);
		final AffineTransformation a4 = new AffineTransformation(0.85, 0.04, 0,
				-0.04, 0.85, 1.6);

		final Rectangle frame1 = new Rectangle(new Point(0, 4.5), 6, 10);

		final List<AffineTransformation> transformations =
		        new ArrayList<AffineTransformation>();
		transformations.add(a1);
		transformations.add(a2);
		transformations.add(a3);
		transformations.add(a4);

		final IFS barnsley = new IFS(transformations);

		try {

			writeIn(barnsley.compute(frame1, 350, 600, 150));
		} catch (final FileNotFoundException e) {
			System.out.println("Erreur: fichier introuvable");
		}
	}

	/**
	 * Creates a fractal in a PBM file.
	 * @param accumulator the accumulator
	 * @throws FileNotFoundException if an error occured with I/O
	 */
	static void writeIn(final IFSAccumulator accumulator)
			throws FileNotFoundException {

		final PrintStream fichier = new PrintStream("Barnsley.PBM");

		fichier.println("P1");
		fichier.println(accumulator.width() + " " + accumulator.height());

		for (int i = accumulator.height() - 1; i >= 0; i--) {

			for (int j = 0; j < accumulator.width(); j++) {
                if (accumulator.isHit(j, i)) {
                    fichier.print("1 ");
                } else {
                    fichier.print("0 ");
                }
            }

			fichier.print("\n");
		}

		fichier.close();
	}

}
