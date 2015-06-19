package ch.epfl.flamemaker.gui;

import java.util.ArrayList;

import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;

/**
 * Decorates Flame.Builder to let it observable.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 *
 */
public class ObservableFlameBuilder extends Flame.Builder {

	/**
	 * Holds the observers.
	 */
	private final ArrayList<Observer> observers = new ArrayList<Observer>();

	/**
	 *
	 * @param flame
	 *            the initial flame
	 */
	public ObservableFlameBuilder(final Flame flame) {
		super(flame);
	}

	/**
	 *
	 * @param o
	 *           the observer to add
	 * @return <code>true</code> iff the observer was actually added
	 */
	public final boolean addObserver(final Observer o) {
		return observers.add(o);
	}

	/**
	 *
	 * @param o
	 *            the observer to remove
	 * @return <code>true</code> iff the observer was actually removed
	 */
	public final boolean removeObeserver(final Observer o) {
		return observers.remove(o);
	}

	/**
	 * Notify all observers.
	 */
	public final void notifyObservers() {
		for (final Observer o : observers) {
            o.update();
        }
	}


	@Override
	public void addTransformation(FlameTransformation newTransformation) {
		super.addTransformation(newTransformation);
		notifyObservers();
	}

	@Override
	public void removeTransformation(int index) {
		super.removeTransformation(index);
		notifyObservers();
	}

	@Override
	public void setAffineTransformation(int index,
			AffineTransformation newTransformation) {
		super.setAffineTransformation(index, newTransformation);
		notifyObservers();
	}

	@Override
	public void setVariationWeight(int index, Variation variation,
			double newWeight) {
		if (super.variationWeight(index, variation) != newWeight) {
			super.setVariationWeight(index, variation, newWeight);
			notifyObservers();
		}
	}

	/**
	 * Interface of the observers of the super class.
	 */
	protected interface Observer {
		public void update();
	}
}
