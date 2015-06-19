package ch.epfl.flamemaker.gui;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main class.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 *
 */
public abstract class FlameMaker {

	/**
	 * @param args for console use
	 * @throws UnsupportedLookAndFeelException thrown when the requested look
	 *             isn't available
	 * @throws IllegalAccessException when something goes horribly wrong
	 * @throws InstantiationException when something goes horribly wrong
	 * @throws ClassNotFoundException when something goes horribly wrong
	 */
	public static void main(final String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {

		// Try to use nimbus
		try {
			for (final LookAndFeelInfo info
			        : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
            }
		} catch (final Exception e) {
			// Nimbus unavailable, use other
			for (final LookAndFeelInfo info
			        : UIManager.getInstalledLookAndFeels()) {
				UIManager.setLookAndFeel(info.getClassName());
				break;
			}
		}

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new FlameMakerGUI().start();
			}
		});
	}

}
