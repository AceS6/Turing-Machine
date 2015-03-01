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

public class TuringMachineView extends JFrame implements Observer {

	private TuringMachine model;

	private JToolBar menuBar;

	private JButton buttonNew;

	private JButton buttonLoad;

	private JButton buttonSave;

	private JButton buttonRunStop;

	private JButton buttonStartPause;

	private JButton buttonStateMode;

	private JButton buttonStep;

	private JButton buttonResults;

	private JSlider delay;

	private JTextArea configuration;

	private JPanel centerPanel;

	private JPanel ribbonStatePanel;

	private JPanel currentStatePanel;

	private JPanel ribbonPanel;

	private JScrollPane configurationScroll;

	private JScrollPane ribbonScroll;

	private ArrayList<JTextField> ribbon;

	private JTextField currentState;

	private JLabel currentStateText;
	
	private String xmlAreaSave;

	public TuringMachineView(TuringMachine model) {
		this.model = model;
		model.addObserver(this);

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

	private void addStyle() {
		menuBar.setFloatable(false);
		menuBar.setBorder( new EmptyBorder(7, 0, 5, 0) );
		menuBar.setBackground(Color.WHITE);

		buttonNew.setFocusPainted(false);
		buttonNew.setIcon( new ImageIcon("img/newIcon.png") );
		buttonNew.setBackground(Color.WHITE);
		buttonNew.setToolTipText("Generate a new configuration");
		buttonNew.setBorder( new EmptyBorder(5, 5, 5, 5) );

		buttonLoad.setFocusPainted(false);
		buttonLoad.setIcon( new ImageIcon("img/loadIcon.png") );
		buttonLoad.setBackground(Color.WHITE);
		buttonLoad.setToolTipText("Load a configuration saved...");
		buttonLoad.setBorder( new EmptyBorder(5, 5, 5, 5) );

		buttonSave.setFocusPainted(false);
		buttonSave.setIcon( new ImageIcon("img/saveIcon.png") );
		buttonSave.setBackground(Color.WHITE);
		buttonSave.setToolTipText("Save configuration as...");
		buttonSave.setBorder( new EmptyBorder(5, 5, 5, 5) );

		buttonRunStop.setFocusPainted(false);
		buttonRunStop.setBackground(Color.WHITE);
		buttonRunStop.setMaximumSize( new Dimension(100, 40) );
		buttonRunStop.setBorder( new EmptyBorder(5, 5, 5, 5) );

		buttonStep.setText("Single step");
		buttonStep.setFocusPainted(false);
		buttonStep.setIcon( new ImageIcon("img/stepIcon.png") );
		buttonStep.setBackground(Color.WHITE);
		buttonStep.setToolTipText("Execute one step");
		buttonStep.setBorder( new EmptyBorder(5, 5, 5, 5) );

		buttonStartPause.setFocusPainted(false);
		buttonStartPause.setBackground(Color.WHITE);
		buttonStartPause.setBorder( new EmptyBorder(5, 5, 5, 5) );

		buttonStateMode.setText("State mode");
		buttonStateMode.setFocusPainted(false);
		buttonStateMode.setIcon( new ImageIcon("img/stateIcon.png") );
		buttonStateMode.setToolTipText("Set the state mode");
		buttonStateMode.setBorder( new EmptyBorder(5, 5, 5, 5) );
		setStateMode();

		buttonResults.setText("Results file");
		buttonResults.setFocusPainted(false);
		buttonResults.setIcon( new ImageIcon("img/resultsIcon.png") );
		buttonResults.setBackground(Color.WHITE);
		buttonResults.setToolTipText("See the results file");
		buttonResults.setBorder( new EmptyBorder(5, 5, 5, 5) );
		buttonResults.setEnabled(false);

		// Initializes buttons.
		setStopMode();
		
		delay.setInverted(true);
		delay.setPaintTrack(false);
		delay.setMinimum(0);
		delay.setMaximum(2000);
		delay.setMinorTickSpacing(250);
		delay.setMajorTickSpacing(500);
		delay.setPaintTicks(true);
		delay.setSnapToTicks(true);
		Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
		table.put( 0, new JLabel("Fast") );
		table.put( 2000, new JLabel("Slow") );
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
							  "	<BreackpointStates>\n" +
							  "		<!-- <State>q0</State> -->\n" +
							  "	</BreackpointStates>\n" +
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
		Font boldFont = new Font( currentState.getFont().getName(), Font.BOLD, currentState.getFont().getSize() );
		currentState.setFont(boldFont);

		ribbonScroll.setViewportView(ribbonPanel);
		ribbonScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		ribbonScroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		ribbonScroll.setBorder( new EmptyBorder(0, 5, 0, 5) );
		
		ribbonPanel.setBorder( new EmptyBorder(5, 0, 5, 0) );
		
		addCase('⊔');
		ribbon.get(0).setMinimumSize( new Dimension(50, 50) );
		ribbon.get(0).setPreferredSize( new Dimension(50, 50) );
	}

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
	}

	public void diplayConfiguration(String ribbon, String sigma, String states, String initialState, String transitions) {
		String textToDisplay = "Ribbon = {" + ribbon + "}\nΣ = {" + sigma + "}\nΓ = Σ ∪ {⊔}\nQ = {" + states + "}\nInitial state = {" + initialState + "}\nAccepting state = {qAcc}\nRejecting state = {qRej}\nδ = { " + transitions + " }";
		configuration.setText(textToDisplay);
	}
	
	public void setRunMode() {
		buttonRunStop.setText("Stop	");
		buttonRunStop.setIcon( new ImageIcon("img/stopIcon.png") );
		buttonRunStop.setToolTipText("Stop Turing machine");

		buttonNew.setEnabled(false);
		buttonLoad.setEnabled(false);
		buttonSave.setEnabled(false);
		buttonStartPause.setEnabled(true);
		buttonStep.setEnabled(true);
		buttonStateMode.setEnabled(true);
		buttonResults.setEnabled(true);
		configuration.setEditable(false);
		configuration.setBackground(null);
		
		currentState.setBackground(Color.WHITE);
		xmlAreaSave = configuration.getText();
		ribbon.clear();
		ribbonPanel.removeAll();
		ribbonScroll.repaint();
	}

	public void setStopMode() {
		buttonRunStop.setText("Run");
		buttonRunStop.setIcon( new ImageIcon("img/runIcon.png") );
		buttonRunStop.setToolTipText("Run Turing machine");
		
		buttonStartPause.setText("Start");
		buttonStartPause.setIcon( new ImageIcon("img/startIcon.png") );
		buttonStartPause.setToolTipText("Start execution");

		buttonNew.setEnabled(true);
		buttonLoad.setEnabled(true);
		buttonSave.setEnabled(true);
		buttonStartPause.setEnabled(false);
		buttonStep.setEnabled(false);
		buttonStateMode.setEnabled(false);
		configuration.setEditable(true);
		configuration.setBackground(Color.WHITE);
	}
	
	public void setStateMode() {
		buttonStateMode.setBackground(Color.WHITE);
	}
	
	public void unsetStateMode() {
		buttonStateMode.setBackground(Color.GREEN);
	}

	public void setStartMode() {
		buttonStartPause.setText("Start");
		buttonStartPause.setIcon( new ImageIcon("img/startIcon.png") );
		buttonStartPause.setToolTipText("Start execution");

		buttonStateMode.setEnabled(true);
		buttonStep.setEnabled(true);
	}
	
	public void setPauseMode() {
		buttonStartPause.setText("Pause");
		buttonStartPause.setIcon( new ImageIcon("img/pauseIcon.png") );
		buttonStartPause.setToolTipText("Pause execution");

		buttonStateMode.setEnabled(false);
		buttonStep.setEnabled(false);
	}

	public void setXMLArea(String newText) {
		configuration.setText(newText);
	}

	public void update(Observable o, Object arg) {
		if ( arg.equals("Update") ) {			
			JTextField currentSymbol = ribbon.get( model.getHead() );
			currentSymbol.setText( Character.toString( model.getCurrentSymbol() ) );
			currentSymbol.setMinimumSize( new Dimension(30, 30) );
			currentSymbol.setPreferredSize( new Dimension(30, 30) );
		}
		else if ( arg.equals("Move") ) {
			currentState.setText( model.getCurrentState().getName() );
			
			if ( currentState.getText().equals("qAcc") ) {
				currentState.setBackground(Color.GREEN);
			}
			else if ( currentState.getText().equals("qRej") ) {
				currentState.setBackground(Color.RED);
			}
			
			JTextField currentSymbol = ribbon.get( model.getHead() );
			currentSymbol.setMinimumSize( new Dimension(50, 50) );
			currentSymbol.setPreferredSize( new Dimension(50, 50) );
			
			ribbonPanel.revalidate();
		}
		else if ( arg.equals("Pause") ) {
			setStartMode();
		}
		else if ( arg.equals("Stop") ) {
			setStopMode();
			displayXMLAreaSaved();
		}
		else {
			addCase( (Character) arg);
		}
	}
	
	private void addCase(char charToDisplay) {
		JTextField newCase = new JTextField();
		newCase.setMinimumSize( new Dimension(30, 30) );
		newCase.setPreferredSize( new Dimension(30, 30) );
		newCase.setMaximumSize( newCase.getPreferredSize() );
		newCase.setHorizontalAlignment(JTextField.CENTER);
		newCase.setEnabled(false);
		newCase.setDisabledTextColor(Color.BLACK);
		newCase.setText( Character.toString( charToDisplay ) );
		ribbon.add(newCase);
		ribbonPanel.add(newCase);
	}

	public String getXMLArea() {
		return configuration.getText();
	}
	
	public void displayXMLAreaSaved() {
		configuration.setText(xmlAreaSave);
	}
	
	public int getDelayValue() {
		return delay.getValue();
	}
	
	public JButton getButtonNew() {
		return buttonNew;
	}

	public JButton getButtonLoad() {
		return buttonLoad;
	}

	public JButton getButtonSave() {
		return buttonSave;
	}

	public JButton getButtonRunStop() {
		return buttonRunStop;
	}

	public JButton getButtonStartPause() {
		return buttonStartPause;
	}

	public JButton getButtonStateMode() {
		return buttonStateMode;
	}

	public JButton getButtonStep() {
		return buttonStep;
	}

	public JButton getButtonResults() {
		return buttonResults;
	}

	public JTextField getCurrentState() {
		return currentState;
	}

}