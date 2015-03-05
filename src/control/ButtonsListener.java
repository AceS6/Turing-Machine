/*
 * M4105C - Théorie du langage
 *
 * class ButtonsListener.java
 */

package control;

import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.jdom2.Document;
import org.jdom2.JDOMException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import model.PrintResults;
import model.TuringMachine;
import model.XMLReader;
import model.XMLWriter;
import model.XMLChecker;
import view.TuringMachineView;

/**
 * This class is the main controller of the application.
 * It implements ActionListener to handle button actions and implements ChangeListener to handle slider actions.
 *
 * @version 1.0 - 02/03/15
 * @author BUSSENEAU Alexis - GRANIER Tristan - SAURAY Antoine
 *
 * @see model.TuringMachine
 * @see model.XMLChecker
 * @see model.XMLReader
 * @see model.XMLWriter
 * @see view.TuringMachineView
 */
public class ButtonsListener implements ActionListener, ChangeListener {

 	/*	----- ATTRIBUTES -----	*/
	
	/**
	 * The Turing machine handled.
	 */
    private TuringMachine model;

    /**
     * The view of the Turing machine.
     */
    private TuringMachineView view;

    /**
     * The file chooser of the application.
     */
    private JFileChooser fileChooser;
    
    
 	/*	----- CONSTRUCTOR -----	*/
    
    /**
     * Creates a controller for a Turing machine with its view.
     * 
     * @param model The Turing machine.
     * @param view The Turing machine view.
     */
    public ButtonsListener(TuringMachine model, TuringMachineView view) {
    	this.model = model;
    	this.view = view;
    	// Adds the controller to the components.
        this.view.initListeners(this);

        fileChooser = new JFileChooser();
        // Sets the XML filter for the fileChooser
        fileChooser.setFileFilter( new FileNameExtensionFilter("xml files (*.xml)", "xml") );
    }
    
    
 	/*	----- BUTTON ACTIONS -----	*/
    
    /**
     * Handles the actions from the user clicks on the buttons.
     * 
     * @param e The click event.
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if ( source == view.getButtonNew() )
            actionButtonNew();
        else if ( source == view.getButtonLoad() )
            actionButtonLoad();
        else if ( source == view.getButtonSave() )
            actionButtonSave();
        else if ( source == view.getButtonRunStop() )
            actionButtonRunStop();
        else if ( source == view.getButtonStartPause() )
            actionButtonStartPause();
        else if ( source == view.getButtonStateMode() )
            actionButtonStateMode();
        else if ( source == view.getButtonStep() )
            actionButtonStep();
        else if ( source == view.getButtonResults() )
            actionButtonResults();
    }

    /**
     * Action of the new button.
     */
    private void actionButtonNew() {
        String text = "";

        try {
            XMLReader reader = new XMLReader();
            // Loads the example XML file into a string.
            text = reader.xmlToString( new File("data/Example.xml") );
        }
        catch (JDOMException|IOException e) {
            JOptionPane.showMessageDialog(view, "The sample file is not found", "Sample file not found", JOptionPane.ERROR_MESSAGE);
        }

        // Displays in the text area the XML file loaded.
        view.setXMLArea(text);
    }

    /**
     * Action of the load button.
     */
    private void actionButtonLoad() {
        fileChooser.setDialogTitle("Open a configuration file");
        boolean stop = false;

        // Forces to choose a valid XML file.
        while (!stop) {
        	// Checks if the user has selected a file.
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            	// Checks if the selected file exists.
                if ( fileChooser.getSelectedFile().exists() )
                	// Checks if the selected file has the XML format.
                    if ( fileChooser.getSelectedFile().getName().endsWith(".xml") ) {
                        String text;
    
                        try {
                            XMLReader reader = new XMLReader();
                            // Loads the selected XML file into a string.
                            text = reader.xmlToString( fileChooser.getSelectedFile() );
                        }
                        catch (JDOMException|IOException e) {
                            text = "";
                        }

                        // Displays the content of the XML file in the XML Area.
                        view.setXMLArea(text);
                        stop = true;
                    }                    
                    else
                        JOptionPane.showMessageDialog(fileChooser, "The file must be .xml format.", "File format error", JOptionPane.ERROR_MESSAGE);
                else
                    JOptionPane.showMessageDialog(fileChooser, "The file doesn't exist.", "Open Error", JOptionPane.ERROR_MESSAGE);
            else
                stop = true;
        }
    }

    /**
     * Action of the save button.
     */
    private void actionButtonSave() {
        fileChooser.setDialogTitle("Save a configuration");
        boolean stop = false;

        while (!stop) {
        	// Checks if the user has choose a path to save the file.
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
            	// Checks if the path to save the file is of XML format.
                if ( fileChooser.getSelectedFile().getName().endsWith(".xml") ) {

                    try {
                    	// Prepares the write in XML from the selected path.
                        XMLWriter writer = new XMLWriter( fileChooser.getSelectedFile() );
                        // Writes the content from XML area into the selected path.
                        writer.print( view.getXMLArea() );
                    }
                    catch (FileNotFoundException e) {
                        JOptionPane.showMessageDialog(view, "The path file is not found.", "File not found", JOptionPane.ERROR_MESSAGE);
                    }

                    stop = true;
                }                    
                else
                    JOptionPane.showMessageDialog(fileChooser, "The file must be .xml format.", "File format error", JOptionPane.ERROR_MESSAGE);
            else
                stop = true;
        }
    }

    /**
     * Action of run or stop button.
     */
    private void actionButtonRunStop() {
        try {
        	// If it's the run button.
            if ( view.getButtonRunStop().getText().equals("Run") ) {
                XMLReader reader = new XMLReader();
                // Creates a XML Document from the text contained in the XML area.
                Document xmlDocument = reader.stringToXml( view.getXMLArea() );
                
                // Prepares the checks for the XML Document loaded.
                XMLChecker checker = new XMLChecker(xmlDocument);
                // Checks if the XML Document has a valid structure.
                checker.checkConfigStructure();
                // Checks if the XML Document has the correct values.
                checker.checkConfigValues();
                
                view.setRunMode();
                // Displays the Turing machine configuration in mathematics.
                view.diplayConfiguration( checker.getRibbon(), checker.getSigma(), checker.getStates(), checker.getInitialState(), checker.getTransitionFunctions() );
                // Initializes the Turing machine with the configuration loaded.
                model.init( checker.getRibbonArrayList(), checker.getInitialStateObject(), checker.getBreakpointStatesArrayList() );
            }
            // If it's the stop button.
            else {
            	// Displays the XML saved before the execution of the Turing machine.
            	view.displayXMLAreaSaved();
                view.setStopMode();
            }
        }
        catch (RuntimeException e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (JDOMException|IOException e) {
            JOptionPane.showMessageDialog(view, "The configuration has not root node. Please insert <TuringMachine></TuringMachine>.", "Root node not found", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Action of the start or pause button.
     */
    private void actionButtonStartPause() {
    	// If it's the start button.
    	if ( view.getButtonStartPause().getText().equals("Start") ) {
	        view.setPauseMode();
	        // Runs the Turing machine.
	        model.loop();
    	}
    	// If it's the pause button.
    	else {
    		view.setStartMode();
    		// Stops the Turing machine.
    		model.pauseThread();
    	}
    }
    
    /**
     * Action of the state mode button.
     */
    private void actionButtonStateMode() {
    	// If the state mode button is off.
        if ( model.getStateMode() ) {
        	// Turns the state mode button to on.
        	view.setStateMode();
        	// Applies the state mode on Turing machine.
        	model.setStateMode();
        }
        // If the state mode button is on.
        else {
        	// Turns the state mode button to off.
        	view.unsetStateMode();
        	// Applies the state mode on Turing machine.
        	model.setStateMode();
        }
    }

    /**
     * Action of the step button.
     */
    private void actionButtonStep() {
    	try {
    		// Executes the Turing machine one time.
    		model.step();
    	}
    	catch (InterruptedException e) {}
    }
    
    /**
     * Action of the results button.
     */
    private void actionButtonResults() {
    	try {
    		// Opens the results file with the text editor of the user by default.
			Desktop.getDesktop().open( new File(PrintResults.FILE_PATH) );
		} catch (IOException e) {
			JOptionPane.showMessageDialog(view, "The results file is not found.", "Results file not found", JOptionPane.ERROR_MESSAGE);
		}
    }
    
    
 	/*	----- SLIDER ACTIONS -----	*/
    
    /**
     * Handles the actions from the slider.
     */
    public void stateChanged(ChangeEvent ev) {
    	// Updates the Turing machine speed with the value from the slider.
    	model.setSpeed( view.getDelayValue() );
	}

}