// TODO:
// -Correct GroupLayout bug
// -This file is huge and ugly. It needs refactoring.
package ch.epfl.flamemaker.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import ch.epfl.flamemaker.color.Color;
import ch.epfl.flamemaker.color.InterpolatedPalette;
import ch.epfl.flamemaker.color.Palette;
import ch.epfl.flamemaker.flame.Flame;
import ch.epfl.flamemaker.flame.FlameTransformation;
import ch.epfl.flamemaker.flame.Variation;
import ch.epfl.flamemaker.geometry2d.AffineTransformation;
import ch.epfl.flamemaker.geometry2d.Point;
import ch.epfl.flamemaker.geometry2d.Rectangle;

/**
 * FlameMaker's GUI.
 *
 * @author Robin Genolet n°227358
 * @author Matthieu Girard n°217661
 *
 */
public class FlameMakerGUI {


	/**
	 * Background color.
	 */
	private final Color backgroundColor = Color.BLACK;

	/**
	 * Palette used to work with colors.
	 */
	private Palette palette;

	/**
	 * The frame of the drawing.
	 */
	private final Rectangle drawFrame =
	        new Rectangle(new Point(-0.25, 0), 5, 4);

	/**
	 * The density, directly correlated to the quality.
	 */
	private final int density = 50;

	/**
	 * Index of the currently selected transformation
	 * (display in red in the GUI).
	 */
	private int selectedTransformationIndex = 0;

	/**
	 * Holds the observers.
	 */
	private final ArrayList<ObserverInterface> observers =
	        new ArrayList<ObserverInterface>();

	/**
	 * Holds the FlameTransformations used by the builder.
	 */
	private final List<FlameTransformation> flameTransformations =
	        Arrays.asList(
			new FlameTransformation(new AffineTransformation(-0.4113504,
					-0.7124804, -0.4, 0.7124795, -0.4113508, 0.8),
					new double[] { 1, 0.1, 0, 0, 0, 0 }),
			new FlameTransformation(new AffineTransformation(-0.3957339, 0,
					-1.6, 0, -0.3957337, 0.2), new double[] { 0, 0, 0, 0, 0.8,
					1 }), new FlameTransformation(new AffineTransformation(
					0.4810169, 0, 1, 0, 0.4810169, 0.9), new double[] { 1, 0,
					0, 0, 0, 0 }));

	/**
	 * An instanciation of the builder.
	 */
	private ObservableFlameBuilder builder;

	/**
	 * Builds and starts the GUI.
	 */
	public void start() {

		/** La liste utilisée pour l'instanciation de la palette */
		final List<Color> listColor = new ArrayList<Color>();
		listColor.add(Color.RED);
		listColor.add(Color.GREEN);
		listColor.add(Color.BLUE);

		// Utilisé pour forcer l'utilisation du point comme séparateur pour les
		// double
		final DecimalFormatSymbols formatSymbol = new DecimalFormatSymbols();
		formatSymbol.setDecimalSeparator('.');

		palette = new InterpolatedPalette(listColor);

		builder = new ObservableFlameBuilder(new Flame(flameTransformations));

		// Création de l'instance de JFrame
		final JFrame frame = new JFrame("Flame Maker");

		// Configuration de l'action à effectuer lorsque la fenêtre est fermée
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Gestion du logo de l'application
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"flamemaker.png"));

		// On évite que l'utilisateur puisse trop réduire sa fenêtre et cacher
		// des éléments du programme
		frame.setMinimumSize(new Dimension(600, 500));

		frame.getContentPane().setLayout(new BorderLayout());

		/** Panneau supérieur */
		final JPanel supPanel = new JPanel();

		/** Panneau des transformations */
		final JPanel transPanel = new JPanel();

		/** Panneau de la fractale */
		final JPanel flamePanel = new JPanel();

		/** Panneau d'édition des transformations */
		final JPanel edittingTransformationsPanel = new JPanel();

		/** Panneau inférieur */
		final JPanel inferiorPanel = new JPanel();

		/** Panneau de gestion des données */
		final JPanel dataManagementPanel = new JPanel();

		/** Instance du modèle de liste */
		final TransformationsListModel transformationsListModel =
		        new TransformationsListModel();

		/** L'instance de JList nécessaire à la construction du JScrollPane */
		@SuppressWarnings("unchecked")
		final JList<String> jList = new JList<String>(transformationsListModel);

		// Configuration de la liste
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.setVisibleRowCount(3);
		jList.setSelectedIndex(0);
		jList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(final ListSelectionEvent arg0) {
				setSelectedTransformationIndex(jList.getSelectedIndex());
			}

		});

		/** Liste des transformations */
		final JScrollPane transformationsList = new JScrollPane(jList);

		/** Panneau de boutons */
		final JPanel buttonPanel = new JPanel();

		/** Le bouton supprimer */
		final JButton deleteButton = new JButton("Supprimer");

		/** Le bouton ajouter */
		final JButton addButton = new JButton("Ajouter");

		/** Panneau d'édition de la transformation Flame sélectionnée */
		final JPanel flameTransformationEditionPanel = new JPanel();

		/** Panneau d'édition de la partie affine */
		final JPanel affineTransformationEditionPanel = new JPanel();

		/** Panneau d'édition des poids des variations */
		final JPanel variationWeightEditionPanel = new JPanel();

		/** Panneau des bouteaux pour "Autres options" */
		final JPanel buttonPanelOtherOption = new JPanel();

		// Configuration du menu
		JMenuBar menuBar;
		JMenu menuFichier;
		JMenuItem saveItem, loadItem, fullScreenItem, quitItem, menuAbout;

		// Create the menu bar.
		menuBar = new JMenuBar();

		// Créé le menu "Fichier"
		menuFichier = new JMenu("File");
		menuFichier.setMnemonic(KeyEvent.VK_A);
		menuFichier.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		menuBar.add(menuFichier);

		// a group of JMenuItems
		saveItem = new JMenuItem("Save", KeyEvent.VK_T);
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		saveItem.getAccessibleContext().setAccessibleDescription(
				"This doesn't really do anything");

		menuFichier.add(saveItem);

		loadItem = new JMenuItem("Load", KeyEvent.VK_T);
		loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.CTRL_MASK));
		loadItem.getAccessibleContext().setAccessibleDescription(
				"This doesn't really do anything");
		menuFichier.add(loadItem);

		loadItem.setMnemonic(KeyEvent.VK_D);
		menuFichier.add(loadItem);

		menuFichier.addSeparator();

		fullScreenItem = new JMenuItem("Full screen", KeyEvent.VK_T);
		fullScreenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11,
				ActionEvent.CTRL_MASK));
		fullScreenItem.getAccessibleContext().setAccessibleDescription(
				"This doesn't really do anything");
		menuFichier.add(fullScreenItem);

		menuFichier.addSeparator();
		quitItem = new JMenuItem("Quit", KeyEvent.VK_T);
		quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));
		quitItem.getAccessibleContext().setAccessibleDescription(
				"This doesn't really do anything");
		menuFichier.add(quitItem);

		// About menu
		menuAbout = new JMenuItem("About", KeyEvent.VK_T);
		menuAbout.setMnemonic(KeyEvent.VK_N);
		menuAbout.getAccessibleContext().setAccessibleDescription("");

		saveItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent E) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FmFilter());
				File file = null;

				if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                }
				String neededExtension = "";

				try {
					if (!(file.getName().endsWith(".fm"))) {
                        neededExtension += ".fm";
                    }
					final PrintStream fichier = new PrintStream(file
							.getAbsolutePath() + neededExtension);

					fichier.println("#FLAMEMAKER CONFIG FILE \n#Do NOT"
					        + " edit unless you know what you are doing");
					for (int i = 0; i < builder.transformationCount(); i++) {
						fichier.println("\nT" + i);
						fichier.println(builder.affineTransformation(i));

						for
						(int j = 0; j < Variation.ALL_VARIATIONS.size(); j++) {
							fichier.print(builder.variationWeight(i,
									Variation.ALL_VARIATIONS.get(j)));
							if (j != Variation.ALL_VARIATIONS.size() - 1) {
                                fichier.print(" ");
                            }
						}
						fichier.print(" ");
					}
					fichier.println("\nEND OF CONFIG");
					fichier.close();
					JOptionPane
							.showMessageDialog(
									frame,
									"Fractal saved.\n"
											+ file + " successfully created!",
									"Success", JOptionPane.INFORMATION_MESSAGE);
				} catch (final FileNotFoundException filNotFound) {
					JOptionPane.showMessageDialog(frame, "File not found",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				catch (final NullPointerException nullPointer) {
					// nothing
				}
			}
		});

		loadItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FmFilter());
				File file = null;

				if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                }

				try {
					final InputStream flux =
					        new FileInputStream(file.toString());
					final InputStreamReader lecture =
					        new InputStreamReader(flux);
					final BufferedReader buff = new BufferedReader(lecture);
					String line;
					final ArrayList<Double> transformationParams =
					        new ArrayList<Double>();
					final ArrayList<Double> transformationWeights =
					        new ArrayList<Double>();
					String transformationParam = "";
					boolean lineIsAffine = false, lineIsWeights = false;

					while ((line = buff.readLine()) != null) {

						if (line.startsWith("END")) {
							System.out.println("\nEND");
							break;
						}

						if (!line.startsWith("#")) {
							if (line.startsWith("T")) {
								line = buff.readLine();
								lineIsAffine = true;
							}
							if (lineIsAffine) {
								for (int i = 0; i < line.length(); i++) {
                                    if (line.charAt(i) != ' ') {
                                        transformationParam += line.charAt(i);
                                    } else {
										transformationParams.add(Double
												.parseDouble(
												        transformationParam));
										transformationParam = "";
									}
                                }
								lineIsAffine = false;
								lineIsWeights = true;
							} else if (lineIsWeights) {
								for (int i = 0; i < line.length(); i++) {
                                    if (line.charAt(i) != ' ') {
                                        transformationParam += line.charAt(i);
                                    } else {
										transformationWeights.add(Double
												.parseDouble(
												        transformationParam));
										transformationParam = "";
									}
                                }
								lineIsWeights = false;
							}
						}
					}

					buff.close();

					if (transformationParams.size() % 6 != 0) {
                        throw new IOException("Fichier corrompu");
                    }
					final int amountOfAT = transformationParams.size() / 6;
					final ArrayList<AffineTransformation>
				        affineTransformations =
				            new ArrayList<AffineTransformation>();
					int affineParamIndex = -1;
					for (int j = 0; j < amountOfAT; j++) {
						affineTransformations.add(new AffineTransformation(
								transformationParams.get(++affineParamIndex),
								transformationParams.get(++affineParamIndex),
								transformationParams.get(++affineParamIndex),
								transformationParams.get(++affineParamIndex),
								transformationParams.get(++affineParamIndex),
								transformationParams.get(++affineParamIndex)));
					}
					if (transformationWeights.size() % 6 != 0) {
                        throw new IOException("Fichier corrompu");
                    }
					final int amountOfWeights = transformationWeights.size() / 6;
					assert amountOfWeights == amountOfAT;

					System.out.println(affineTransformations);

					int weightsParamIndex = -1;

					final ArrayList<FlameTransformation>
					    flameTransformations =
					        new ArrayList<FlameTransformation>();
					for (int i = 0; i < amountOfWeights; i++) {
						flameTransformations.add(new FlameTransformation(
								affineTransformations.get(i), new double[] {
										transformationWeights
												.get(++weightsParamIndex),
										transformationWeights
												.get(++weightsParamIndex),
										transformationWeights
												.get(++weightsParamIndex),
										transformationWeights
												.get(++weightsParamIndex),
										transformationWeights
												.get(++weightsParamIndex),
										transformationWeights
												.get(++weightsParamIndex) }));
					}

					builder = new ObservableFlameBuilder(new Flame(
							flameTransformations));

				} catch (final FileNotFoundException fileNotFound) {
					JOptionPane.showMessageDialog(frame, "Fichier introuvable",
							"Erreur", JOptionPane.ERROR_MESSAGE);
				} catch (final IOException iOEx) {
					// TODO Auto-generated catch block
					iOEx.printStackTrace();
				} catch (final NullPointerException nullPoint) {
					// L'utilisateur à fermé la fenêtre de chargement: pas
					// besoin d'ouvrir un fichier
				}
			}

		});

		fullScreenItem.addActionListener(new ActionListener() {

			@SuppressWarnings("serial")
			@Override
			public void actionPerformed(final ActionEvent e) {
				final Dimension tailleEcran = java.awt.Toolkit
						.getDefaultToolkit().getScreenSize();
				final int width = (int) tailleEcran.getWidth();
				final int height = (int) tailleEcran.getHeight();
				final JFrame fullScreenFractal = new JFrame("");
				final FlameBuilderPreviewComponent fullFractal =
				        new FlameBuilderPreviewComponent(
						builder, backgroundColor, palette, drawFrame, density);
				fullScreenFractal.setUndecorated(true);
				// Le Panel qui contiendra la fractale
				final JPanel fractalContainer = new JPanel();
				fractalContainer.setLayout(new BoxLayout(fractalContainer,
						BoxLayout.X_AXIS));
				fractalContainer.add(fullFractal);
				fullScreenFractal.getContentPane().add(fractalContainer);
				fractalContainer.setPreferredSize(new Dimension(width, height));

				final Action killFrame = new AbstractAction() {
					@Override
					public void actionPerformed(final ActionEvent e) {
						// Retire toutes les ressources allouées à la JFrame
						fullScreenFractal.dispose();
					}
				};

				final Action exit =
				        new AbstractAction() {

					@Override
					public void actionPerformed(final ActionEvent arg0) {
						System.exit(0);
					}

				};

				final int replay = JOptionPane
						.showConfirmDialog(
								frame,
								"Entering full screen. "
								+ "Use CTRL+F11 or ESCAPE to quit."
										+ "\nVery long computation "
										+ "time possible",
								"Mode plein écran", JOptionPane.YES_NO_OPTION,
								JOptionPane.INFORMATION_MESSAGE);

				if (replay == JOptionPane.YES_OPTION) {

					fractalContainer.getInputMap().put(
							KeyStroke.getKeyStroke("ESCAPE"), "killFrame");
					fractalContainer.getActionMap().put("killFrame", killFrame);

					fractalContainer.getInputMap().put(
							KeyStroke.getKeyStroke(KeyEvent.VK_F11,
									ActionEvent.CTRL_MASK), "killFrame");
					fractalContainer.getActionMap().put("killFrame", killFrame);

					// On donne aussi la possibilité de quitter totalement le
					// programme
					fractalContainer.getInputMap().put(
							KeyStroke.getKeyStroke(KeyEvent.VK_Q,
									ActionEvent.CTRL_MASK), "exit");
					fractalContainer.getActionMap().put("exit", exit);

					fullScreenFractal.setVisible(true);
					fullScreenFractal.pack();
				}
			}
		});

		quitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				System.exit(0);
			}

		});

		menuAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				JOptionPane
						.showMessageDialog(
								frame,
								"FlameMaker is a fractal creation software"
								+ " created by Robin Genolet Matthieu Girard",
								"About", JOptionPane.INFORMATION_MESSAGE);

			}

		});

		menuBar.add(menuAbout);

		frame.setJMenuBar(menuBar);

		// Fin de la configuration du menu

		// Événement à effectuer lorsque l'on clique sur le bouton "supprimer"
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {

				final int tempSelectedIndex = selectedTransformationIndex;

				if (selectedTransformationIndex == transformationsListModel
						.getSize() - 1) {
                    jList.setSelectedIndex(selectedTransformationIndex - 1);
                }

				transformationsListModel
						.removeTransformation(tempSelectedIndex);

				// Si la suppression de la transformation rend la liste composée
				// d'un seul élément, on désactive le bouton "Supprimer"
				if (transformationsListModel.getSize() <= 1) {
                    deleteButton.setEnabled(false);
                }
			}
		});

		// Événement à effectuer lorsque l'on clique sur le bouton "ajouter"
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					transformationsListModel
							.addTransformation(FlameTransformation.IDENTITY);

					jList.setSelectedIndex(
					        transformationsListModel.getSize() - 1);
					deleteButton.setEnabled(true);
				} catch (final Exception exception) {
					System.out.println("caca");
				}
			}
		});

		// ajout des deux boutons précédents au panneau de boutons
		buttonPanel.add(deleteButton);
		buttonPanel.add(addButton);

		// Ajout des layout aux différents panneaux
		supPanel.setLayout(new GridLayout(1, 2));
		transPanel.setLayout(new BorderLayout());
		flamePanel.setLayout(new BorderLayout());
		buttonPanel.setLayout(new GridLayout(1, 2));
		buttonPanelOtherOption.setLayout(new GridLayout(1, 3));
		inferiorPanel.setLayout(new BoxLayout(inferiorPanel,
				BoxLayout.LINE_AXIS));
		dataManagementPanel.setLayout(new BoxLayout(dataManagementPanel,
				BoxLayout.PAGE_AXIS));
		edittingTransformationsPanel.setLayout(new BorderLayout());

		flameTransformationEditionPanel.setLayout(new BoxLayout(
				flameTransformationEditionPanel, BoxLayout.PAGE_AXIS));

		/** Le layout de affineTransformationEditionPanel */
		final GroupLayout affineTGroupLayout = new GroupLayout(
				affineTransformationEditionPanel);
		affineTransformationEditionPanel.setLayout(affineTGroupLayout);

		// Ajout des panneaux au panneau de contenu
		frame.getContentPane().add(supPanel, BorderLayout.NORTH);
		frame.getContentPane().add(inferiorPanel, BorderLayout.CENTER);
		frame.getContentPane().add(dataManagementPanel, BorderLayout.SOUTH);

		supPanel.add(transPanel);
		supPanel.add(flamePanel);

		dataManagementPanel.add(buttonPanelOtherOption);

		edittingTransformationsPanel.add(transformationsList,
				BorderLayout.CENTER);
		edittingTransformationsPanel.add(buttonPanel, BorderLayout.PAGE_END);

		inferiorPanel.add(edittingTransformationsPanel);
		inferiorPanel.add(flameTransformationEditionPanel);

		flameTransformationEditionPanel.add(affineTransformationEditionPanel);
		flameTransformationEditionPanel.add(new JSeparator());
		flameTransformationEditionPanel.add(variationWeightEditionPanel);

		// //////////////////////////////////////////////////////
		// Configuration de l'interface de modification affine
		// //////////////////////////////////////////////////////

		// Contient tous les symbols nécessaires à la création des boutons de
		// l'interface de modification affine
		final String[] buttonsAffineInterfaceSymbol =
		    { "←", "→", "↑", "↓", "↺", "↻",
				"+ ↔", "- ↔", "+ ↕", "- ↕" };

		// On créer la classe qui étend InputVerifier de façon anonyme car on ne
		// l'utilise qu'une seule fois
		final InputVerifier verifierNoZero = new InputVerifier() {

			@Override
			public boolean verify(JComponent input) {
				final JFormattedTextField jft = (JFormattedTextField) input;

				// #YOLO, méthode plus intuitive et plus courte
				try {
					if (Double.parseDouble(jft.getText()) == 0) {
						// l'utilisateur a entré la valeur 0 => on réinitialise
						// le champ avec la dernière entrée valide
						jft.setValue(jft.getValue());
						input = jft;
					}

				} catch (final NumberFormatException e) {
					// l'utilisateur a entrée une valeur de type 0.0 ou 0.0000
					// etc... => on réinitialise le champ avec la dernière
					// entrée valide
					jft.setValue(jft.getValue());
					input = jft;
				}

				return true;
			}
		};

		// Création des labels
		final JLabel[] affineModificationsLabels = new JLabel[4];
		affineModificationsLabels[0] = new JLabel("Translation");
		affineModificationsLabels[1] = new JLabel("Rotation");
		affineModificationsLabels[2] = new JLabel("Dilatation");
		affineModificationsLabels[3] = new JLabel("Transvection");

		// Création des champs de texte
		final JFormattedTextField[] affineEditTextFields =
		        createAffineEditTextFields(
				formatSymbol, verifierNoZero);

		// Créations des boutons et assignation d'un listener à ces derniers
		final AffineInterfaceListener translationListener =
		        new AffineInterfaceListener();
		final JButton[] affineInterfaceButtons = new JButton[14];
		for (int i = 0; i < affineInterfaceButtons.length; i++) {
			if (i < 10) {
                affineInterfaceButtons[i] = new JButton(
						buttonsAffineInterfaceSymbol[i]);
            } else {
                affineInterfaceButtons[i] = new JButton(
						buttonsAffineInterfaceSymbol[i - 10]);
            }

			// Pour les translations
			if (i < 4) {
                affineInterfaceButtons[i].addActionListener(translationListener
						.newActionListener(affineEditTextFields[0], i));
            } else if (i > 3 && i < 6) {
                affineInterfaceButtons[i].addActionListener(translationListener
						.newActionListener(affineEditTextFields[1], i));
            } else if (i > 5 && i < 10) {
                affineInterfaceButtons[i].addActionListener(translationListener
						.newActionListener(affineEditTextFields[2], i));
            } else {
                affineInterfaceButtons[i].addActionListener(translationListener
						.newActionListener(affineEditTextFields[3], i));
            }

		}

		// Espace les boutons et les containers (améliore l'esthétique)
		affineTGroupLayout.setAutoCreateGaps(true);
		affineTGroupLayout.setAutoCreateContainerGaps(true);

		final GroupLayout.SequentialGroup hGroup = affineTGroupLayout
				.createSequentialGroup();

		final GroupLayout.SequentialGroup vGroup = affineTGroupLayout
				.createSequentialGroup();

		createNewHGroupsForAffineEdition(hGroup, affineTGroupLayout,
				affineModificationsLabels, affineEditTextFields,
				affineInterfaceButtons);

		createNewVGroupsForAffineEdition(vGroup, affineTGroupLayout,
				affineModificationsLabels, affineEditTextFields,
				affineInterfaceButtons);

		affineTGroupLayout.setHorizontalGroup(hGroup);
		affineTGroupLayout.setVerticalGroup(vGroup);

		// ////////////////////////////////////////////////////////
		// END Configuration de l'interface de modification affine
		// ///////////////////////////////////////////////////////

		// BEGIN configuration de l'interface de modification des poids

		// Création du tableau contenant tous les labels et de celui contenant
		// les champs de texte
		final JLabel[] variationWeightLabels =
		        new JLabel[Variation.ALL_VARIATIONS
				.size()];
		final JFormattedTextField[] variationInterfaceTextFields =
		        new JFormattedTextField[Variation.ALL_VARIATIONS
				.size()];

		// Attribution des noms aux labels et création des champs de texte
		for (int i = 0; i < Variation.ALL_VARIATIONS.size(); i++) {
			variationWeightLabels[i] = new JLabel(Variation.ALL_VARIATIONS.get(
					i).name());
			variationInterfaceTextFields[i] = new JFormattedTextField(
					new DecimalFormat("#0.##", formatSymbol));
			variationInterfaceTextFields[i].setValue(builder.variationWeight(
					getSelectedTransformationIndex(),
					Variation.ALL_VARIATIONS.get(i)));
			variationInterfaceTextFields[i]
					.setHorizontalAlignment(SwingConstants.RIGHT);
		}

		// On place les boutons grâce à un GroupLayout

		/** Le layout de variationWeightEditionPanel */
		final GroupLayout variationWeightEditionGroupLayout = new GroupLayout(
				variationWeightEditionPanel);
		variationWeightEditionPanel
				.setLayout(variationWeightEditionGroupLayout);

		final GroupLayout.SequentialGroup hGroupVW =
		        variationWeightEditionGroupLayout
				.createSequentialGroup();

		final GroupLayout.SequentialGroup vGroupVW =
		        variationWeightEditionGroupLayout
				.createSequentialGroup();

		createNewHGroupsForWeightsEdition(hGroupVW,
				variationWeightEditionGroupLayout, variationWeightLabels,
				variationInterfaceTextFields);

		createNewVGroupsForWeightsEdition(vGroupVW,
				variationWeightEditionGroupLayout, variationWeightLabels,
				variationInterfaceTextFields);

		variationWeightEditionGroupLayout.setHorizontalGroup(hGroupVW);
		variationWeightEditionGroupLayout.setVerticalGroup(vGroupVW);

		// END configuration de l'interface de modification des poids

		// Ajout des bordures aux composants
		final Border transBorder = BorderFactory
				.createTitledBorder("Transformations affines");
		final Border flameBorder = BorderFactory.createTitledBorder("Fractale");
		final Border listBorder =
		        BorderFactory.createTitledBorder("Transformations");
		final Border flameTransformationEditionPanelBorder = BorderFactory
				.createTitledBorder("Transformation courante");

		transPanel.setBorder(transBorder);
		flamePanel.setBorder(flameBorder);
		edittingTransformationsPanel.setBorder(listBorder);
		flameTransformationEditionPanel
				.setBorder(flameTransformationEditionPanelBorder);

		flamePanel.add(new FlameBuilderPreviewComponent(builder,
				backgroundColor, palette, drawFrame, density),
				BorderLayout.CENTER);

		final AffineTransformationsComponent affineTransformationComponent =
		        new AffineTransformationsComponent(
				builder, drawFrame);
		addObserver(new ObserverInterface() {

			@Override
			public void update() {
				affineTransformationComponent
						.setRedTransIndex(selectedTransformationIndex);
			}
		});
		transPanel.add(affineTransformationComponent, BorderLayout.CENTER);

		// S'assure que la taille de la fenêtre est appropriée au contenu et
		// affiche cette dernière
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Creates text fiels for the affine modifications area.
	 *
	 * @param formatSymbol
	 * @param verifierNoZero
	 * @return
	 */
	private JFormattedTextField[] createAffineEditTextFields(
			final DecimalFormatSymbols formatSymbol, final InputVerifier verifierNoZero) {
		final JFormattedTextField affineEditTextFields[] = new JFormattedTextField[4];
		affineEditTextFields[0] = new JFormattedTextField(new DecimalFormat(
				"#0.##", formatSymbol));
		affineEditTextFields[0].setValue(0.1);
		// Aligne le nombre à droite de la cellule
		affineEditTextFields[0].setHorizontalAlignment(SwingConstants.RIGHT);
		// Définit une dimension au bouton (le
		// GroupLayout s'occupe de donner la
		// même tailles aux autres champs de texte)
		affineEditTextFields[0].setPreferredSize(new Dimension(40, 10));
		affineEditTextFields[1] = new JFormattedTextField(new DecimalFormat(
				"#0.##", formatSymbol));
		affineEditTextFields[1].setValue((double) 15);
		affineEditTextFields[1].setHorizontalAlignment(SwingConstants.RIGHT);
		affineEditTextFields[2] = new JFormattedTextField(new DecimalFormat(
				"#0.##", formatSymbol));
		affineEditTextFields[2].setValue(1.05);
		affineEditTextFields[2].setHorizontalAlignment(SwingConstants.RIGHT);
		affineEditTextFields[2].setInputVerifier(verifierNoZero);
		affineEditTextFields[3] = new JFormattedTextField(new DecimalFormat(
				"#0.##", formatSymbol));
		affineEditTextFields[3].setValue(0.1);
		affineEditTextFields[3].setHorizontalAlignment(SwingConstants.RIGHT);

		return affineEditTextFields;
	}

	private void createNewVGroupsForWeightsEdition(final SequentialGroup vGroupVW,
			final GroupLayout variationWeightEditionGroupLayout,
			final JLabel[] variationWeightLabels,
			final JFormattedTextField[] variationInterfaceTextFields) {
		// v1 :
		vGroupVW.addGroup(variationWeightEditionGroupLayout
				.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(variationWeightLabels[0])
				.addComponent(variationInterfaceTextFields[0])
				.addComponent(variationWeightLabels[1])
				.addComponent(variationInterfaceTextFields[1])
				.addComponent(variationWeightLabels[2])
				.addComponent(variationInterfaceTextFields[2]));

		// v2 :
		vGroupVW.addGroup(variationWeightEditionGroupLayout
				.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(variationWeightLabels[3])
				.addComponent(variationInterfaceTextFields[3])
				.addComponent(variationWeightLabels[4])
				.addComponent(variationInterfaceTextFields[4])
				.addComponent(variationWeightLabels[5])
				.addComponent(variationInterfaceTextFields[5]));

	}

	private void createNewVGroupsForAffineEdition(final SequentialGroup vGroup,
			final GroupLayout affineTGroupLayout, JLabel[] affineModificationsLabels,
			final JFormattedTextField[] affineEditTextFields,
			final JButton[] affineInterfaceButtons) {


		// v1 :
		vGroup.addGroup(affineTGroupLayout
				.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(affineModificationsLabels[0])
				.addComponent(affineEditTextFields[0])
				.addComponent(affineInterfaceButtons[0])
				.addComponent(affineInterfaceButtons[1])
				.addComponent(affineInterfaceButtons[2])
				.addComponent(affineInterfaceButtons[3]));

		// v2 :
		vGroup.addGroup(affineTGroupLayout
				.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(affineModificationsLabels[1])
				.addComponent(affineEditTextFields[1])
				.addComponent(affineInterfaceButtons[4])
				.addComponent(affineInterfaceButtons[5]));

		// v3 :
		vGroup.addGroup(affineTGroupLayout
				.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(affineModificationsLabels[2])
				.addComponent(affineEditTextFields[2])
				.addComponent(affineInterfaceButtons[6])
				.addComponent(affineInterfaceButtons[7])
				.addComponent(affineInterfaceButtons[8])
				.addComponent(affineInterfaceButtons[9]));

		// v4 :
		vGroup.addGroup(affineTGroupLayout
				.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(affineModificationsLabels[3])
				.addComponent(affineEditTextFields[3])
				.addComponent(affineInterfaceButtons[10])
				.addComponent(affineInterfaceButtons[11])
				.addComponent(affineInterfaceButtons[12])
				.addComponent(affineInterfaceButtons[13]));

	}

	private void createNewHGroupsForWeightsEdition(SequentialGroup hGroupVW,
			GroupLayout variationWeightEditionGroupLayout,
			JLabel[] variationWeightLabels,
			JFormattedTextField[] variationInterfaceTextFields) {

		// h1 :
		hGroupVW.addGroup(variationWeightEditionGroupLayout
				.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(variationWeightLabels[0])
				.addComponent(variationWeightLabels[3]));
		// h2 :
		hGroupVW.addGroup(
				variationWeightEditionGroupLayout.createParallelGroup()
						.addComponent(variationInterfaceTextFields[0])
						.addComponent(variationInterfaceTextFields[3]))
				.addPreferredGap(ComponentPlacement.UNRELATED);

		// h3 :
		hGroupVW.addGroup(variationWeightEditionGroupLayout
				.createParallelGroup().addComponent(variationWeightLabels[1])
				.addComponent(variationWeightLabels[4]));

		// h4 :
		hGroupVW.addGroup(
				variationWeightEditionGroupLayout.createParallelGroup()
						.addComponent(variationInterfaceTextFields[1])
						.addComponent(variationInterfaceTextFields[4]))
				.addPreferredGap(ComponentPlacement.UNRELATED);

		// h5 :
		hGroupVW.addGroup(variationWeightEditionGroupLayout
				.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(variationWeightLabels[2])
				.addComponent(variationWeightLabels[5]));

		// h6 :
		hGroupVW.addGroup(variationWeightEditionGroupLayout
				.createParallelGroup()
				.addComponent(variationInterfaceTextFields[2])
				.addComponent(variationInterfaceTextFields[5]));

	}

	/**
	 * Creates horizontal groups.
	 *
	 * @param hGroup
	 * @param affineTGroupLayout
	 * @param affineModificationsLabels
	 * @param affineEditTextFields
	 * @param affineInterfaceButtons
	 */
	private void createNewHGroupsForAffineEdition(final SequentialGroup hGroup,
			GroupLayout affineTGroupLayout, JLabel[] affineModificationsLabels,
			JFormattedTextField[] affineEditTextFields,
			JButton[] affineInterfaceButtons) {
		// Ajout des 6 groupes h1, h2, ... , h6 au groupe horizonzal hGroup

		// h1 :
		hGroup.addGroup(affineTGroupLayout
				.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(affineModificationsLabels[0])
				.addComponent(affineModificationsLabels[1])
				.addComponent(affineModificationsLabels[2])
				.addComponent(affineModificationsLabels[3]));

		// h2 :
		hGroup.addGroup(affineTGroupLayout.createParallelGroup()
				.addComponent(affineEditTextFields[0])
				.addComponent(affineEditTextFields[1])
				.addComponent(affineEditTextFields[2])
				.addComponent(affineEditTextFields[3]));

		// h3 :
		hGroup.addGroup(affineTGroupLayout.createParallelGroup()
				.addComponent(affineInterfaceButtons[0])
				.addComponent(affineInterfaceButtons[4])
				.addComponent(affineInterfaceButtons[6])
				.addComponent(affineInterfaceButtons[10]));

		// h4 :
		hGroup.addGroup(affineTGroupLayout.createParallelGroup()
				.addComponent(affineInterfaceButtons[1])
				.addComponent(affineInterfaceButtons[5])
				.addComponent(affineInterfaceButtons[7])
				.addComponent(affineInterfaceButtons[11]));

		// h5 :
		hGroup.addGroup(affineTGroupLayout.createParallelGroup()
				.addComponent(affineInterfaceButtons[2])
				.addComponent(affineInterfaceButtons[8])
				.addComponent(affineInterfaceButtons[12]));

		// h6 :
		hGroup.addGroup(affineTGroupLayout.createParallelGroup()
				.addComponent(affineInterfaceButtons[3])
				.addComponent(affineInterfaceButtons[9])
				.addComponent(affineInterfaceButtons[13]));

	}

	/**
	 *
	 * @return the index of the selected transformation
	 */
	public final int getSelectedTransformationIndex() {
		return selectedTransformationIndex;
	}

	/**
	 *
	 * @param st
	 *            the index of the selected transformation
	 */
	public final void setSelectedTransformationIndex(final int st) {
		this.selectedTransformationIndex = st;
		notifyObservers();
	}

	/**
	 * Notify all observers.
	 */
	public final void notifyObservers() {
		for (final ObserverInterface s : observers) {
            s.update();
        }
	}

	/**
	 * Adds an observer.
	 *
	 * @param s
	 *            the new observer
	 * @return <code>true</code> iff the observer has been added
	 *     and was new.
	 */
	public final boolean addObserver(final ObserverInterface s) {
		return observers.add(s);
	}

	/**
	 * Remove an observer.
	 *
	 * @param s
	 *            the observer to remove
	 * @return <code>true</code> iff the observer has been removed
     *     and was in the list.
	 */
	public final boolean removeObeserver(final ObserverInterface s) {
		return observers.remove(s);
	}

	@SuppressWarnings({ "serial", "rawtypes" })
	class TransformationsListModel extends AbstractListModel {

		@Override
		public int getSize() {
			return builder.transformationCount();
		}

		@Override
		public String getElementAt(final int index) {
			return "Transformation n° " + (index + 1);
		}

		/**
		 *
		 * @param newAT
		 *            the flame transformation to add
		 */
		public void addTransformation(final FlameTransformation newAT) {
			builder.addTransformation(newAT);
			fireIntervalAdded(this, getSize() - 1, getSize() - 1);
		}

		/**
		 *
		 * @param index
		 *            the flame transformation's index to to remove
		 */
		public void removeTransformation(final int index) {
			builder.removeTransformation(index);
			fireIntervalRemoved(this, getSize(), getSize());
		}
	}

	/**
	 * Interface for the observers.
	 */
	private interface ObserverInterface {

		/**
		 * Called at each new selection.
		 */
		void update();
	}

	/**
	 * Interface for the use of the Strategy pattern
     * (for the translation/transvection buttons).
	 *
	 */
	interface StrategyAffineInterface {
		ActionListener newActionListener(JFormattedTextField textField, int d);
	}

	/**
	 * Utilisé (pour éviter des répétitions de code) par la méthode
	 * addActionListener des boutons des translations
	 * <p>
	 * Implémente le patron Strategy
	 */
	private class AffineInterfaceListener implements StrategyAffineInterface {

		private AffineTransformation translation;

		@Override
		public ActionListener newActionListener(
				final JFormattedTextField textField, final int d) {

			return new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					boolean isTranslation = false;
					final AffineTransformation initialTransformation = builder
							.affineTransformation(getSelectedTransformationIndex());

					switch (d) {
					case 0:
						isTranslation = true;
						translation = AffineTransformation.newTranslation(
								-Double.parseDouble(textField.getText()), 0);
						break;
					case 1:
						isTranslation = true;
						translation = AffineTransformation.newTranslation(
								Double.parseDouble(textField.getText()), 0);
						break;
					case 2:
						isTranslation = true;
						translation = AffineTransformation.newTranslation(0,
								Double.parseDouble(textField.getText()));
						break;
					case 3:
						isTranslation = true;
						translation = AffineTransformation.newTranslation(0,
								-Double.parseDouble(textField.getText()));
						break;
					case 4:
						translation = AffineTransformation.newRotation(Math.PI
								* (Double.parseDouble(textField.getText()))
								/ 180);
						break;
					case 5:
						translation = AffineTransformation.newRotation(-Math.PI
								* (Double.parseDouble(textField.getText()))
								/ 180);
						break;
					case 6:
						translation = AffineTransformation.newScaling(
								Double.parseDouble(textField.getText()), 1);
						break;
					case 7:
						translation = AffineTransformation.newScaling(
								1 / Double.parseDouble(textField.getText()), 1);
						break;
					case 8:
						translation = AffineTransformation.newScaling(1,
								Double.parseDouble(textField.getText()));
						break;
					case 9:
						translation = AffineTransformation.newScaling(1,
								1 / Double.parseDouble(textField.getText()));
						break;
					case 10:
						translation = AffineTransformation.newShearX(-Double
								.parseDouble(textField.getText()));
						break;
					case 11:
						translation = AffineTransformation.newShearX(Double
								.parseDouble(textField.getText()));
						break;
					case 12:
						translation = AffineTransformation.newShearY(Double
								.parseDouble(textField.getText()));
						break;
					case 13:
						translation = AffineTransformation.newShearY(-Double
								.parseDouble(textField.getText()));
						break;

					default:
						throw new IllegalArgumentException(
								"Argument invalide: '"
										+ d
										+ "' ne devrait logiquement jamais se produire");

					}

					// Compose la transformation avec celle du builder.
					// On a besoin du boolean isTranslation pour décider si on
					// on compose le builder avec la transformation ou
					// vice-versa
					if (isTranslation) {
                        builder.setAffineTransformation(
								getSelectedTransformationIndex(),
								translation.composeWith(initialTransformation));
                    } else {
                        builder.setAffineTransformation(
								getSelectedTransformationIndex(),
								initialTransformation.composeWith(translation));
                    }
				}
			};
		}
	}

	/**
	 * Used by FmFilter.
	 */
	public static class Utils {

		@SuppressWarnings("javadoc")
		public final static String fm = "fm";

		/**
		 * Returns the extension of a file.
		 *
		 * @param f a file name
		 * @return the file extension, or NULL if none
		 */
		public static String getExtension(final File f) {
			String ext = null;
			final String s = f.getName();
			final int i = s.lastIndexOf('.');

			if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
			return ext;
		}
	}

	/**
	 *
	 */
	public class FmFilter extends FileFilter {

		// Accept directories and .fm files only
		@Override
		public boolean accept(final File f) {
			if (f.isDirectory()) {
                return true;
            }

			final String extension = Utils.getExtension(f);
			if (extension != null) {
                return extension.equals(Utils.fm);
            }

			return false;
		}

		// The description of this filter
		@Override
		public String getDescription() {
			return "FlameMaker files .fm";
		}
	}
}