package ch.epfl.flamemaker.color;

/**
 * Models a palette.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 */
public interface Palette {
	/**
	 *
	 * @param index
	 *            un index de couleur
	 * @return la couleur associée à l'index de couleur donné
	 * @throws IllegalArgumentException
	 *             si l'index est invalide
	 */
	Color colorForIndex(double index);
}
