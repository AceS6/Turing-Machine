package control;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.jdom2.JDOMException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;

import model.TuringMachine;
import model.XMLReader;
import model.XMLWriter;
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

        if (source == view.buttonNew) {
            String text = "";

            try {
                text = XMLReader.xmlToString( new File("../data/Example.xml") );
            }
            catch (JDOMException|IOException exception) {
                JOptionPane.showMessageDialog(view, "The sample file is not found", "File not found", JOptionPane.WARNING_MESSAGE);
            }

            view.updateXMLArea(text);
        }
        else if (source == view.buttonLoad) {
            fileChooser.setDialogTitle("Open a configuration file");
            boolean stop = false;

            while (!stop) {
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                    if ( fileChooser.getSelectedFile().exists() )
                        if ( fileChooser.getSelectedFile().getName().endsWith(".xml") ) {
                            String text = "";
        
                            try {
                                text = XMLReader.xmlToString( fileChooser.getSelectedFile() );
                            }
                            catch (JDOMException|IOException exception) {
                                JOptionPane.showMessageDialog(view, "The xml file is empty", "File empty", JOptionPane.WARNING_MESSAGE);
                            }

                            view.updateXMLArea(text);
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
        else if (source == view.buttonSave) {
            fileChooser.setDialogTitle("Save a configuration");
            boolean stop = false;

            while (!stop) {
                if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
                    if ( fileChooser.getSelectedFile().getName().endsWith(".xml") ) {
                        try {
                            XMLWriter.write( fileChooser.getSelectedFile(), view.getXMLArea() );
                        }
                        catch (FileNotFoundException exception) {
                            JOptionPane.showMessageDialog(view, "The save file is not found.", "File not found", JOptionPane.ERROR_MESSAGE);
                        }

                        stop = true;
                    }                    
                    else
                        JOptionPane.showMessageDialog(fileChooser, "The file must be .xml format.", "File format error", JOptionPane.ERROR_MESSAGE);
                else
                    stop = true;
            }
        }
    }

}