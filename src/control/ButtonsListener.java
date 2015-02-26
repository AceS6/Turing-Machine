package control;

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

import model.TuringMachine;
import model.XMLReader;
import model.XMLWriter;
import model.XMLChecker;
import view.TuringMachineView;

public class ButtonsListener implements ActionListener {

    private TuringMachine model;

    private TuringMachineView view;

    private JFileChooser fileChooser;
    
    public ButtonsListener(TuringMachine model, TuringMachineView view) {
    	this.model = model;
    	this.view = view;
        this.view.initListeners(this);

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter( new FileNameExtensionFilter("xml files (*.xml)", "xml") );
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == view.buttonNew)
            actionButtonNew();
        else if (source == view.buttonLoad)
            actionButtonLoad();
        else if (source == view.buttonSave)
            actionButtonSave();
        else if (source == view.buttonRunStop)
            actionButtonRunStop();
        else if (source == view.buttonStateMode)
            actionButtonStateMode();
        else if (source == view.buttonStartPause)
            actionButtonStartPause();
        else if (source == view.buttonStep)
            actionButtonStep();
    }

    private void actionButtonNew() {
        String text = "";

        try {
            XMLReader reader = new XMLReader(); 
            text = reader.xmlToString( new File("../data/Example.xml") );
        }
        catch (JDOMException|IOException e) {
            JOptionPane.showMessageDialog(view, "The sample file is not found", "Sample file not found", JOptionPane.WARNING_MESSAGE);
        }

        view.setXMLArea(text);
    }

    private void actionButtonLoad() {
        fileChooser.setDialogTitle("Open a configuration file");
            boolean stop = false;

        while (!stop) {
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                if ( fileChooser.getSelectedFile().exists() )
                    if ( fileChooser.getSelectedFile().getName().endsWith(".xml") ) {
                        String text;
    
                        try {
                            XMLReader reader = new XMLReader();
                            text = reader.xmlToString( fileChooser.getSelectedFile() );
                        }
                        catch (JDOMException|IOException e) {
                            text = "";
                        }

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

    private void actionButtonSave() {
        fileChooser.setDialogTitle("Save a configuration");
        boolean stop = false;

        while (!stop) {
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
                if ( fileChooser.getSelectedFile().getName().endsWith(".xml") ) {

                    try {
                        XMLWriter writer = new XMLWriter( fileChooser.getSelectedFile() );
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

    private void actionButtonRunStop() {
        try {
            if ( view.buttonRunStop.getText().equals("Run") ) {
                XMLReader reader = new XMLReader();
                Document xmlDocument = reader.stringToXml( view.getXMLArea() );
                XMLChecker checker = new XMLChecker(xmlDocument);
                checker.checkConfigStructure();
                checker.checkConfigValues();
                view.diplayConfiguration( checker.getRibbon(), checker.getSigma(), checker.getStates(), checker.getInitialState(), checker.getTransitionFunctions() );
                view.setRunMode();
                model.init( checker.getRibbonArrayList(), checker.getInitialStateObject() );
            }
            else {
                view.setStopMode();
            }
        }
        catch (RuntimeException e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (JDOMException|IOException e) {
            JOptionPane.showMessageDialog(view, "The configuration has not root node. Please insert <TuringMachine></TuringMachine>", "No configuration found", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionButtonStateMode() {
        
    }

    private void actionButtonStartPause() {
        view.switchButtonStartPause();
    }

    private void actionButtonStep() {
        
    }

}