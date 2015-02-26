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

	public JButton buttonNew, buttonLoad, buttonSave, buttonRunStop, buttonStartPause, buttonStateMode, buttonStep, buttonResults;

	public JSlider delay;

	private JTextArea configuration;

	private JPanel centerPanel, ribbonStatePanel, currentStatePanel, ribbonPanel;

	private JScrollPane ribbonScroll;

	private ArrayList<JTextField> ribbon;

	private JTextField currentState;

	private JLabel currentStateText;

	public TuringMachineView(TuringMachine model) {
		this.model = model;
		model.addObserver(this);

		setTitle("Turing machine");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize( new Dimension(900, 450) );
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

		buttonNew.setFocusPainted(false);
		buttonNew.setIcon( new ImageIcon("../img/newIcon.png") );
		buttonNew.setBackground(Color.WHITE);
		buttonNew.setToolTipText("New configuration...");

		buttonLoad.setFocusPainted(false);
		buttonLoad.setIcon( new ImageIcon("../img/loadIcon.png") );
		buttonLoad.setBackground(Color.WHITE);
		buttonLoad.setToolTipText("Load configuration...");

		buttonSave.setFocusPainted(false);
		buttonSave.setIcon( new ImageIcon("../img/saveIcon.png") );
		buttonSave.setBackground(Color.WHITE);
		buttonSave.setToolTipText("Save configuration as...");

		buttonRunStop.setFocusPainted(false);
		buttonRunStop.setBackground(Color.WHITE);

		buttonStep.setText("Single step");
		buttonStep.setFocusPainted(false);
		buttonStep.setIcon( new ImageIcon("../img/stepIcon.png") );
		buttonStep.setBackground(Color.WHITE);
		buttonStep.setToolTipText("Next step");

		buttonStartPause.setFocusPainted(false);
		buttonStartPause.setBackground(Color.WHITE);
		

		buttonStateMode.setText("State mode");
		buttonStateMode.setFocusPainted(false);
		buttonStateMode.setIcon( new ImageIcon("../img/stateIcon.png") );
		buttonStateMode.setBackground(Color.WHITE);
		buttonStateMode.setToolTipText("Launch the state mode");

		buttonResults.setText("Results file");
		buttonResults.setFocusPainted(false);
		buttonResults.setIcon( new ImageIcon("../img/resultsIcon.png") );
		buttonResults.setBackground(Color.WHITE);
		buttonResults.setToolTipText("See the results file");
		buttonResults.setEnabled(false);

		// Initializes buttons.
		switchButtonStartPause();
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

		configuration.setTabSize(2);

		currentStatePanel.setBorder( new EmptyBorder(5, 5, 5, 5) );

		currentStateText.setText("Current state :");

		currentState.setHorizontalAlignment(JTextField.CENTER);
		currentState.setBorder( BorderFactory.createLineBorder(Color.BLACK) );
		currentState.setDisabledTextColor(Color.BLACK);
		currentState.setEnabled(false);

		ribbonScroll.setViewportView(ribbonPanel);
		ribbonScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		ribbonScroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		
		ribbonPanel.setBorder( new EmptyBorder(5, 5, 5, 5) );
	}

	private void createLayout() {
			menuBar.addSeparator();
			menuBar.add(buttonNew);
			menuBar.add(buttonLoad);
			menuBar.add(buttonSave);
			menuBar.addSeparator();
			menuBar.add(buttonRunStop);
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
			centerPanel.add(configuration, BorderLayout.CENTER);
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
	}

	public void diplayConfiguration(String ribbon, String sigma, String states, String initialState, String transitions) {
		String textToDisplay = "Ribbon = {" + ribbon + "}\nΣ = {" + sigma + "}\nΓ = Σ ∪ {⊔}\nQ = {" + states + "}\nInitial state = {" + initialState + "}\nAccepting state = {qAcc}\nRejecting state = {qRej}\nδ = { " + transitions + " }";
		configuration.setText(textToDisplay);
	}

	private void addCase() {
		JTextField newCase = new JTextField();
		newCase.setPreferredSize( new Dimension(30, 30) );
		newCase.setMaximumSize( newCase.getPreferredSize() );
		newCase.setHorizontalAlignment(JTextField.CENTER);
		newCase.setEnabled(false);
		newCase.setDisabledTextColor(Color.BLACK);
		ribbon.add(newCase);
		ribbonPanel.add(newCase);
	}

	public void setStopMode() {
		buttonRunStop.setText("Run");
		buttonRunStop.setIcon( new ImageIcon("../img/runIcon.png") );
		buttonRunStop.setToolTipText("Launch Turing machine");

		buttonStartPause.setEnabled(false);
		buttonStep.setEnabled(false);
		buttonStateMode.setEnabled(false);
		configuration.setEditable(true);
		configuration.setBackground(Color.WHITE);
	}

	public void setRunMode() {
		buttonRunStop.setText("Stop");
		buttonRunStop.setIcon( new ImageIcon("../img/stopIcon.png") );
		buttonRunStop.setToolTipText("Stop Turing machine");

		buttonStartPause.setEnabled(true);
		buttonStep.setEnabled(true);
		buttonStateMode.setEnabled(true);
		configuration.setEditable(false);
		configuration.setBackground(null);
	}

	public void switchButtonStartPause() {
		if ( !buttonStartPause.getText().equals("Start") ) {
			buttonStartPause.setText("Start");
			buttonStartPause.setIcon( new ImageIcon("../img/startIcon.png") );
			buttonStartPause.setToolTipText("Start execution");

			buttonStateMode.setEnabled(true);
			buttonStep.setEnabled(true);
		}
		else {
			buttonStartPause.setText("Pause");
			buttonStartPause.setIcon( new ImageIcon("../img/pauseIcon.png") );
			buttonStartPause.setToolTipText("Pause execution");

			buttonStateMode.setEnabled(false);
			buttonStep.setEnabled(false);
		}
	}

	public void update(Observable o, Object arg) {
		System.out.println("UPDATE !");
	}

	public void setXMLArea(String newText) {
		configuration.setText(newText);
	}

	public String getXMLArea() {
		return configuration.getText();
	}

}