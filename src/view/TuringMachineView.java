/*
 * M4105C - Théorie du langage
 *
 * class TuringMachineView.java
 */

package view;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagLayout;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

import model.TuringMachine;
import control.ButtonsListener;
import control.HoverListener;

/**
 * This class is main view of the application.
 * It implements observer because it observes the Turing machine.
 * It's a part of the Observer pattern.
 *
 * @version 1.0 - 02/03/15
 * @author BUSSENEAU Alexis - GRANIER Tristan - SAURAY Antoine
 * 
 * @see control.ButtonsListener
 * @see control.HoverListener
 * @see model.TuringMachine
 */
public class TuringMachineView extends JFrame implements Observer {
	
	/*	----- ATTRIBUTES -----	*/

	/**
	 * The Turing machine observed.
	 */
	private TuringMachine model;

	/**
	 * The JTollBar of the view containing the button.
	 */
	private JToolBar menuBar;

	/**
	 * The new configuration button.
	 */
	private JButton buttonNew;

	/**
	 * The load configuration button.
	 */
	private JButton buttonLoad;

	/**
	 * The save configuration button.
	 */
	private JButton buttonSave;

	/**
	 * The run or stop Turing machine button.
	 */
	private JButton buttonRunStop;

	/**
	 * The button start or pause Turing machine button.
	 */
	private JButton buttonStartPause;

	/**
	 * The state mode button.
	 */
	private JButton buttonStateMode;

	/**
	 * The step Turing machine button.
	 */
	private JButton buttonStep;

	/**
	 * The results file button.
	 */
	private JButton buttonResults;

	/**
	 * The slider delay of the Turing machine.
	 */
	private JSlider delay;

	/**
	 * The text area of the Turing machine configuration.
	 */
	private JTextArea configuration;

	/**
	 * The main panel of the view.
	 */
	private JPanel centerPanel;

	/**
	 * The panel of the current state of the ribbon.
	 */
	private JPanel ribbonStatePanel;

	/**
	 * The panel of the current state.
	 */
	private JPanel currentStatePanel;

	/**
	 * The panel of the ribbon.
	 */
	private JPanel ribbonPanel;

	/**
	 * The scroll pane of the text area configuration.
	 */
	private JScrollPane configurationScroll;

	/**
	 * The scroll pane of the ribbon.
	 */
	private JScrollPane ribbonScroll;

	/**
	 * The list of cases of the ribbon.
	 */
	private ArrayList<JTextField> ribbon;

	/**
	 * The current state of the Turing machine.
	 */
	private JTextField currentState;

	/**
	 * The title of the current state.
	 */
	private JLabel currentStateText;

	/**
	 * The value of the text area configuration before the launching of the Turing machine.
	 */
	private String xmlAreaSave;

	
	/*	----- CONSTRUCTOR -----	*/

	/**
	 * Creates the view from the Turing machine.
	 * 
	 * @param model The Turing machine to observe.
	 */
	public TuringMachineView(TuringMachine model) {
		this.model = model;
		// Adds the view to the observable Turing machine.
		model.addObserver(this);

		// Settings of the JFrame
		setTitle("Turing machine");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize( new Dimension(915, 465) );
		setPreferredSize( getMinimumSize() );
		setLocationRelativeTo(null);
		setVisible(true);

		init();
		addStyle();
		createLayout();
		pack();
	}
	
	
	/*	----- OTHER METHODS -----	*/

	/**
	 * Initializes the components of the view.
	 */
	private void init() {
		menuBar = new JToolBar();
			buttonNew = new JButton();
			buttonLoad = new JButton();
			buttonSave = new JButton();
			buttonRunStop = new JButton();
			buttonStartPause = new JButton();
			buttonStateMode = new JButton();
			buttonStep = new JButton();
			buttonResults = new JButton();
			delay = new JSlider();

		centerPanel = new JPanel();
			configurationScroll = new JScrollPane();
				configuration = new JTextArea();
				
			ribbonStatePanel = new JPanel();
				currentStatePanel = new JPanel();
					currentStateText = new JLabel();
					currentState = new JTextField();
				ribbonScroll = new JScrollPane();
					ribbonPanel = new JPanel();
						ribbon = new ArrayList<JTextField>();
	}

	/**
	 * Sets the components style.
	 */
	private void addStyle() {
		menuBar.setFloatable(false);
		menuBar.setBorder( new EmptyBorder(7, 0, 5, 0) );
		menuBar.setBackground(Color.WHITE);

		buttonNew.setFocusPainted(false);
		buttonNew.setIcon( new ImageIcon( getClass().getClassLoader().getResource("newIcon.png") ) );
		buttonNew.setBackground(Color.WHITE);
		buttonNew.setToolTipText("Generate a new configuration");
		buttonNew.setBorder( new EmptyBorder(5, 5, 5, 5) );

		buttonLoad.setFocusPainted(false);
		buttonLoad.setIcon( new ImageIcon( getClass().getClassLoader().getResource("loadIcon.png") ) );
		buttonLoad.setBackground(Color.WHITE);
		buttonLoad.setToolTipText("Load a configuration saved...");
		buttonLoad.setBorder( new EmptyBorder(5, 5, 5, 5) );

		buttonSave.setFocusPainted(false);
		buttonSave.setIcon( new ImageIcon( getClass().getClassLoader().getResource("saveIcon.png") ) );
		buttonSave.setBackground(Color.WHITE);
		buttonSave.setToolTipText("Save configuration as...");
		buttonSave.setBorder( new EmptyBorder(5, 5, 5, 5) );

		buttonRunStop.setFocusPainted(false);
		buttonRunStop.setBackground(Color.WHITE);
		buttonRunStop.setMaximumSize( new Dimension(100, 40) );
		buttonRunStop.setBorder( new EmptyBorder(5, 5, 5, 5) );

		buttonStep.setText("Single step");
		buttonStep.setFocusPainted(false);
		buttonStep.setIcon( new ImageIcon( getClass().getClassLoader().getResource("stepIcon.png") ) );
		buttonStep.setBackground(Color.WHITE);
		buttonStep.setToolTipText("Execute one step");
		buttonStep.setBorder( new EmptyBorder(5, 5, 5, 5) );

		buttonStartPause.setFocusPainted(false);
		buttonStartPause.setBackground(Color.WHITE);
		buttonStartPause.setBorder( new EmptyBorder(5, 5, 5, 5) );

		buttonStateMode.setText("State mode");
		buttonStateMode.setFocusPainted(false);
		buttonStateMode.setIcon( new ImageIcon( getClass().getClassLoader().getResource("stateIcon.png") ) );
		buttonStateMode.setToolTipText("Set the state mode");
		buttonStateMode.setBorder( new EmptyBorder(5, 5, 5, 5) );
		setStateMode();

		buttonResults.setText("Results file");
		buttonResults.setFocusPainted(false);
		buttonResults.setIcon( new ImageIcon( getClass().getClassLoader().getResource("resultsIcon.png") ) );
		buttonResults.setBackground(Color.WHITE);
		buttonResults.setToolTipText("See the results file");
		buttonResults.setBorder( new EmptyBorder(5, 5, 5, 5) );
		buttonResults.setEnabled(false);
		
		// Inverts the delay to have the slow value on left.
		delay.setInverted(true);
		delay.setPaintTrack(false);
		// Delay minimum value.
		delay.setMinimum(0);
		// Delay maximum value.
		delay.setMaximum(2000);
		delay.setMinorTickSpacing(250);
		delay.setMajorTickSpacing(500);
		delay.setPaintTicks(true);
		delay.setSnapToTicks(true);
		// Sets the ticks values of the delay.
		Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
		table.put( delay.getMinimum(), new JLabel("Fast") );
		table.put( delay.getMaximum(), new JLabel("Slow") );
		delay.setLabelTable(table);
		delay.setPaintLabels(true);
		delay.setBackground(Color.WHITE);
		delay.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( new Color(153, 153, 153) ), BorderFactory.createEmptyBorder(2, 2, 0, 0) ) );
		delay.setPreferredSize( new Dimension(165, 50) );
		delay.setMaximumSize( delay.getPreferredSize() );
		delay.setToolTipText("Delay between each step");
		delay.setBorder( new EmptyBorder(5, 5, 5, 5) );

		configurationScroll.setViewportView(configuration);
		configurationScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		configurationScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		configurationScroll.setBorder( new EmptyBorder(5, 5, 5, 5) );

		configuration.setTabSize(2);
		configuration.setText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
							  "<!-- The root node must be called \"TuringMachine\" -->\n" +
							  "<TuringMachine>\n" +
							  "	<!-- Ribbon contains symbols defined in Σ alphabet -->\n" +
							  "	<Ribbon></Ribbon>\n" +
							  "	<!-- Σ alphabet contains all symbols recognized by the Turing machine -->\n" +
							  "	<Σ></Σ>\n" +
							  "	<!-- Q is the set of states of the Turing machine -->\n" +
							  "	<Q>\n" +
							  "		<!-- <State>q0</State> -->\n" +
							  "	</Q>" +
							  "	<!-- First state of the Turing machine -->\n" +
							  "	<InitialState></InitialState>\n" +
							  "	<!-- δ is the set of transition functions of the Turing machine. Must be complete for each state. -->\n" +
							  "	<!-- Puts \"qAcc\" for the accepting state and \"qRej\" for the rejecting state -->" +
							  "	<δ>\n" +
							  "		<!-- <Function>(q0, ⊔)=(qAcc, ⊔, R)</Function> -->\n" +
							  "	</δ>\n" +
							  "	<!-- The states where the Turing machine must stop. Can be empty. -->\n" +
							  "	<BreakpointStates>\n" +
							  "		<!-- <State>q0</State> -->\n" +
							  "	</BreakpointStates>\n" +
							  "</TuringMachine>");

		currentStatePanel.setBorder( new EmptyBorder(10, 5, 5, 5) );

		currentStateText.setText("Current state");
		currentStateText.setBorder( new EmptyBorder(10, 0, 3, 0) );

		currentState.setHorizontalAlignment(JTextField.CENTER);
		currentState.setBorder( BorderFactory.createLineBorder(Color.BLACK) );
		currentState.setDisabledTextColor(Color.BLACK);
		currentState.setEnabled(false);
		currentState.setMaximumSize( new Dimension(200, 20) );
		currentState.setPreferredSize( new Dimension(0, 20) );
		// Sets a bold text.
		Font boldFont = new Font( currentState.getFont().getName(), Font.BOLD, currentState.getFont().getSize() );
		currentState.setFont(boldFont);

		ribbonScroll.setViewportView(ribbonPanel);
		ribbonScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// Avoids some graphic bugs.
		ribbonScroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		ribbonScroll.setBorder( new EmptyBorder(0, 5, 0, 5) );
		
		ribbonPanel.setBorder( new EmptyBorder(5, 0, 5, 0) );
		
		// Inputs a blank symbol by default and puts the head on it.
		addCase('⊔');
		ribbon.get(0).setMinimumSize( new Dimension(50, 50) );
		ribbon.get(0).setPreferredSize( new Dimension(50, 50) );
		
		// Sets the stop mode by default.
		setStopMode();
	}

	/**
	 * Sets the layout of the components.
	 */
	private void createLayout() {
			menuBar.addSeparator();
			menuBar.add(buttonNew);
			menuBar.add(buttonLoad);
			menuBar.add(buttonSave);
			menuBar.addSeparator();
			menuBar.add(buttonRunStop);
			menuBar.addSeparator();
			menuBar.add(buttonStartPause);
			menuBar.add(buttonStateMode);
			menuBar.add(buttonStep);
			menuBar.addSeparator();
			menuBar.add(buttonResults);
			menuBar.addSeparator();
			menuBar.add(delay);
			menuBar.addSeparator();
		add(menuBar, BorderLayout.NORTH);

				currentStatePanel.setLayout( new BoxLayout(currentStatePanel, BoxLayout.Y_AXIS) );
				currentStatePanel.add(currentStateText);
				currentStatePanel.add(currentState);
				ribbonStatePanel.setLayout( new BorderLayout() );
				ribbonStatePanel.add(currentStatePanel, BorderLayout.WEST);
				ribbonStatePanel.add(ribbonScroll, BorderLayout.CENTER);
					ribbonPanel.setLayout( new GridBagLayout() );
			centerPanel.setLayout( new BorderLayout() );
			centerPanel.add(ribbonStatePanel, BorderLayout.NORTH);
			centerPanel.add(configurationScroll, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);
	}

	/**
	 * Attaches the listener to the components.
	 * 
	 * @param listener The listener of the components.
	 */
	public void initListeners(ButtonsListener listener) {
		buttonNew.addActionListener(listener);
		buttonLoad.addActionListener(listener);
		buttonSave.addActionListener(listener);
		buttonRunStop.addActionListener(listener);
		buttonStartPause.addActionListener(listener);
		buttonStateMode.addActionListener(listener);
		buttonStep.addActionListener(listener);
		buttonResults.addActionListener(listener);
		delay.addChangeListener(listener);
		
		buttonNew.addMouseListener( HoverListener.getInstance() );
		buttonLoad.addMouseListener( HoverListener.getInstance() );
		buttonSave.addMouseListener( HoverListener.getInstance() );
		buttonRunStop.addMouseListener( HoverListener.getInstance() );
		buttonStartPause.addMouseListener( HoverListener.getInstance() );
		buttonStateMode.addMouseListener( HoverListener.getInstance() );
		buttonStep.addMouseListener( HoverListener.getInstance() );
		buttonResults.addMouseListener( HoverListener.getInstance() );
	}

	/**
	 * Displays in mathematics the configuration of the Turing machine.
	 * 
	 * @param ribbon The ribbon of the Turing machine.
	 * @param sigma The sigma alphabet of the Turing machine.
	 * @param states The list of states of the Turing machine.
	 * @param initialState The initial states of the Turing machine.
	 * @param transitions The list of transitions of the Turing machine.
	 */
	public void diplayConfiguration(String ribbon, String sigma, String states, String initialState, String transitions) {
		String textToDisplay = "Ribbon = {" + ribbon + "}\nΣ = {" + sigma + "}\nΓ = Σ ∪ {⊔}\nQ = {" + states + "}\nInitial state = {" + initialState + "}\nAccepting state = {qAcc}\nRejecting state = {qRej}\nδ = { " + transitions + " }";
		configuration.setText(textToDisplay);
	}
	
	/**
	 * Sets the run mode.
	 */
	public void setRunMode() {
		// Changes the button.
		buttonRunStop.setText("Stop	");
		buttonRunStop.setIcon( new ImageIcon( getClass().getClassLoader().getResource("stopIcon.png") ) );
		buttonRunStop.setToolTipText("Stop Turing machine");

		// Sets the enabling buttons.
		buttonNew.setEnabled(false);
		buttonLoad.setEnabled(false);
		buttonSave.setEnabled(false);
		buttonStartPause.setEnabled(true);
		buttonStep.setEnabled(true);
		buttonStateMode.setEnabled(true);
		buttonResults.setEnabled(true);
		configuration.setEditable(false);
		configuration.setBackground(null);
		
		// Clears the current state background.
		currentState.setBackground(Color.WHITE);
		// Saves the text contained in the xml configuration.
		xmlAreaSave = configuration.getText();
		ribbon.clear();
		ribbonPanel.removeAll();
		ribbonScroll.repaint();
	}

	/**
	 * Sets the stop mode.
	 */
	public void setStopMode() {
		// Changes the button.
		buttonRunStop.setText("Run");
		buttonRunStop.setIcon( new ImageIcon( getClass().getClassLoader().getResource("runIcon.png") ) );
		buttonRunStop.setToolTipText("Run Turing machine");
		
		// Puts the start button by default.
		buttonStartPause.setText("Start");
		buttonStartPause.setIcon( new ImageIcon( getClass().getClassLoader().getResource("startIcon.png") ) );
		buttonStartPause.setToolTipText("Start execution");

		// Sets the enabling buttons.
		buttonNew.setEnabled(true);
		buttonLoad.setEnabled(true);
		buttonSave.setEnabled(true);
		buttonStartPause.setEnabled(false);
		buttonStep.setEnabled(false);
		buttonStateMode.setEnabled(false);
		configuration.setEditable(true);
		configuration.setBackground(Color.WHITE);
	}
	
	/**
	 * Sets the state mode.
	 */
	public void setStateMode() {
		buttonStateMode.setBackground(Color.WHITE);
	}
	
	/**
	 * Unsets the state mode.
	 */
	public void unsetStateMode() {
		buttonStateMode.setBackground(Color.GREEN);
	}

	/**
	 * Sets start mode.
	 */
	public void setStartMode() {
		buttonStartPause.setText("Start");
		buttonStartPause.setIcon( new ImageIcon( getClass().getClassLoader().getResource("startIcon.png") ) );
		buttonStartPause.setToolTipText("Start execution");

		buttonStateMode.setEnabled(true);
		buttonStep.setEnabled(true);
	}
	
	/**
	 * Sets pause mode.
	 */
	public void setPauseMode() {
		buttonStartPause.setText("Pause");
		buttonStartPause.setIcon( new ImageIcon( getClass().getClassLoader().getResource("pauseIcon.png") ) );
		buttonStartPause.setToolTipText("Pause execution");

		buttonStateMode.setEnabled(false);
		buttonStep.setEnabled(false);
	}

	/**
	 * Sets the text in the XML area.
	 * 
	 * @param newText The text to input.
	 */
	public void setXMLArea(String newText) {
		configuration.setText(newText);
	}
	
	/**
	 * Displays the XML configuration saved.
	 */
	public void displayXMLAreaSaved() {
		configuration.setText(xmlAreaSave);
	}

	@Override
	public void update(Observable o, Object arg) {
		// If it's the update notification.
		if ( arg.equals("Update") ) {
			// Updates the current symbol.
			JTextField currentSymbol = ribbon.get( model.getHead() );
			currentSymbol.setText( Character.toString( model.getCurrentSymbol() ) );
			currentSymbol.setMinimumSize( new Dimension(30, 30) );
			currentSymbol.setPreferredSize( new Dimension(30, 30) );
		}
		// If it's the move notification.
		else if ( arg.equals("Move") ) {
			// Changes the current state.
			currentState.setText( model.getCurrentState().getName() );
			
			// And puts a background color if the Turing machine is over.
			if ( currentState.getText().equals("qAcc") ) {
				currentState.setBackground(Color.GREEN);
			}
			else if ( currentState.getText().equals("qRej") ) {
				currentState.setBackground(Color.RED);
			}
			
			// Changes the current symbol.
			JTextField currentSymbol = ribbon.get( model.getHead() );
			currentSymbol.setMinimumSize( new Dimension(50, 50) );
			currentSymbol.setPreferredSize( new Dimension(50, 50) );
			
			ribbonPanel.revalidate();
		}
		// If it's the pause notification.
		else if ( arg.equals("Pause") ) {
			setStartMode();
		}
		// If it's the stop notification.
		else if ( arg.equals("Stop") ) {
			setStopMode();
			// Loads the XML configuration.
			displayXMLAreaSaved();
		}
		// If a new symbol is added in the ribbon.
		else {
			addCase( (Character)arg );
		}
	}
	
	/**
	 * Adds a case into the ribbon view.
	 * 
	 * @param symbolToDisplay The symbol to add in the ribbon.
	 */
	private void addCase(char symbolToDisplay) {
		JTextField newCase = new JTextField();
		newCase.setMinimumSize( new Dimension(30, 30) );
		newCase.setPreferredSize( new Dimension(30, 30) );
		newCase.setMaximumSize( newCase.getPreferredSize() );
		newCase.setHorizontalAlignment(JTextField.CENTER);
		newCase.setEnabled(false);
		newCase.setDisabledTextColor(Color.BLACK);
		newCase.setText( Character.toString(symbolToDisplay) );
		ribbon.add(newCase);
		ribbonPanel.add(newCase);
	}
	
	
	/*	----- ACCESSORS -----	*/

	/**
	 * Gets the XML area.
	 * 
	 * @return The XML area.
	 */
	public String getXMLArea() {
		return configuration.getText();
	}
	
	/**
	 * Gets the delay slider.
	 * 
	 * @return The delay slider.
	 */
	public int getDelayValue() {
		return delay.getValue();
	}
	
	/**
	 * Gets the new button.
	 * 
	 * @return The new button.
	 */
	public JButton getButtonNew() {
		return buttonNew;
	}

	/**
	 * Gets the load button.
	 * 
	 * @return The load button.
	 */
	public JButton getButtonLoad() {
		return buttonLoad;
	}

	/**
	 * Gets the save button.
	 * 
	 * @return The save button.
	 */
	public JButton getButtonSave() {
		return buttonSave;
	}

	/**
	 * Gets the run/stop button.
	 * 
	 * @return The run/stop button.
	 */
	public JButton getButtonRunStop() {
		return buttonRunStop;
	}

	/**
	 * Gets the start/pause button.
	 * 
	 * @return The start/pause button.
	 */
	public JButton getButtonStartPause() {
		return buttonStartPause;
	}

	/**
	 * Gets the state mode button.
	 * 
	 * @return The state mode button.
	 */
	public JButton getButtonStateMode() {
		return buttonStateMode;
	}

	/**
	 * Gets the step button.
	 * 
	 * @return The step button.
	 */
	public JButton getButtonStep() {
		return buttonStep;
	}

	/**
	 * Gets the results button.
	 * 
	 * @return The results button.
	 */
	public JButton getButtonResults() {
		return buttonResults;
	}

	/**
	 * Gets the current state field.
	 * 
	 * @return The current state field.
	 */
	public JTextField getCurrentState() {
		return currentState;
	}

}