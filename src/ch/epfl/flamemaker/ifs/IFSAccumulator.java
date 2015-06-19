package ch.epfl.flamemaker.ifs;

final class IFSAccumulator {

	final private boolean[][] accumulator;

	public IFSAccumulator(boolean[][] isHit) {

		accumulator = new boolean[isHit.length][isHit[0].length];

		for (int i = 0; i < isHit.length; i++)
			for (int j = 0; j < isHit[0].length; j++)
				accumulator[i][j] = isHit[i][j];
	}

	/** @return la hauteur (en nombre de cases) de l'accumulateur */
	public int height() {

		return accumulator[0].length;
	}

	/**
	 * @return vrai si et seulement si la case de l'accumulateur aux coordonnées
	 *         données contient au moins un point de S
	 */
	public boolean isHit(int x, int y) {

		if (x < 0 || y < 0 || x >= width() || y >= height())
			// existante
			// pour
			// l'équivalent?
			throw new IndexOutOfBoundsException("Coordonnées invalides");

		return accumulator[x][y] == true;
	}

	/** @return la largeur (en nombre de cases) de l'accumulateur */
	public int width() {

		return accumulator.length;
	}
}