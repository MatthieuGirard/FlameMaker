package ch.epfl.flamemaker.geometry2d;

/**
 * Models affine transformations.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 */
public final class AffineTransformation implements Transformation {

	/**
	 * The identity affine transformation.
	 */
	public static final AffineTransformation IDENTITY =
	        new AffineTransformation(1, 0, 0, 0, 1, 0);

	/**
	 * Respectivement les coefficients 11, 12, 13, 21, 22, 23 de la matrice 3x3
	 * d'une transformation affine. (Les coefficients 31, 32 et 33 sont
	 * implicites et sont toujours égaux à 0, 0, 1)
	 */
	private final double a, b, c, d, e, f;

	/**
	 *
	 */
	@Override
	public String toString() {
		return a + " " + b + " " + c + " " + d + " " + e + " " + f + " ";
	}

	/**
	 * Constructeur
	 * <p>
	 * Construit une transformation affine avec les coefficients d'une matrice
	 *
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 *            Respectivement les coefficients 11, 12, 13, 21, 22, 23 de la
	 *            matrice 3x3 représentant une transformation affine.
	 *
	 */
	public AffineTransformation(double a, double b, double c, double d,
			double e, double f) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
	}

	/**
	 * @return la composition de cette transformation avec une autre
	 * @param that
	 *            Une autre transformation
	 * */
	public AffineTransformation composeWith(AffineTransformation that) {

		// On obtient cette formule en multipliant 2 matrices 3x3 "à la main".
		// Les 6 premières valeurs de la matrice obtenues sont ainsi utilisées
		// pour créer une nouvelle transformation affine

		return new AffineTransformation(a * that.a + b * that.d, a * that.b + b
				* that.e, a * that.c + b * that.f + c, d * that.a + e * that.d,
				d * that.b + e * that.e, d * that.c + e * that.f + f);
	}

	/**
	 * @param p
	 *            Un Point
	 * @return le point p transformé par cette transformation
	 */
	@Override
	public Point transformPoint(Point p) {
		return new Point(a * p.x() + b * p.y() + c, d * p.x() + e * p.y() + f);
	}

	/**
	 *
	 * @param theta
	 *            l'angle en radian qui détermine la rotation
	 * @return une transformation représentant une rotation d'angle theta (en
	 *         radians) autour de l'origine
	 */
	public static AffineTransformation newRotation(double theta) {
		final double cosTheta = Math.cos(theta);
		final double sinTheta = Math.sin(theta);
		return new AffineTransformation(cosTheta, -sinTheta, 0, sinTheta,
				cosTheta, 0);
	}

	/**
	 *
	 * @param sx
	 *            le facteur de dilatation parallèle à l'abscisse
	 * @param sy
	 *            le facteur de dilatation parallèle à ordonnée
	 * @return une transformation représentant une dilatation d'un facteur sx
	 *         parallélement à l'abscisse et d'un facteur sy parallélement à
	 *         l'ordonnée
	 */
	public static AffineTransformation newScaling(double sx, double sy) {
		return new AffineTransformation(sx, 0, 0, 0, sy, 0);
	}

	/**
	 * @param sx
	 *            le facteur de transvection à effectuer
	 * @return une transformation affine représentant une transvection d'un
	 *         facteur sx par rapport à l'origine
	 */
	public static AffineTransformation newShearX(double sx) {
		return new AffineTransformation(1, sx, 0, 0, 1, 0);
	}

	/**
	 * @param sy
	 *            le facteur de la transvection à effectuer
	 * @return une transformation affine représentant une transvection d'un
	 *         facteur sy par rapport à l'ordonnée
	 */
	public static AffineTransformation newShearY(double sy) {
		return new AffineTransformation(1, 0, 0, sy, 1, 0);
	}

	/**
	 * Translation de dx unités parallèlement à l'abscisse et dy unités
	 * parallèlement à l'ordonnée
	 *
	 * @param dx
	 *            déplacement par rapport à l'abscisse
	 * @param dy
	 *            déplacement par rapport à l'ordonnée
	 * @return une nouvelle transformation affine (une translation)
	 */
	public static AffineTransformation newTranslation(double dx, double dy) {
		return new AffineTransformation(1, 0, dx, 0, 1, dy);
	}

	/**
	 * Getter
	 *
	 * @return la composante horizontale de la translation
	 */
	public double translationX() {
		return c;
	}

	/**
	 * Getter
	 *
	 * @return la composante verticale de la translation
	 */
	public double translationY() {
		return f;
	}
}
