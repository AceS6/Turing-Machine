/*
 * M4105C - Théorie du langage
 *
 * class XMLWriter.java
 */

package model;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

/**
 * This class writes an XML configuration into a file from a string.
 *
 * @version 1.0 - 02/03/15
 * @author BUSSENEAU Alexis - GRANIER Tristan - SAURAY Antoine
 */
public class XMLWriter {
	
	/*	----- ATTRIBUTE -----	*/

	/**
	 * The PrintWriter which prints the configuration.
	 */
	private PrintWriter pw;

	
	/*	----- CONSTRUCTOR -----	*/
	
	/**
	 * Creates a XMLWriter from the save file.
	 * 
	 * @param xmlFile The XML file where the configuration will be saved.
	 * 
	 * @throws FileNotFoundException If the file path is not found.
	 */
	public XMLWriter(File xmlFile) throws FileNotFoundException {
		pw = new PrintWriter(xmlFile);
	}
	
	
	/*	----- OTHER METHODS -----	*/

	/**
	 * Prints on the file the specified string.
	 * 
	 * @param configuration The XML configuration to print on the file.
	 */
	public void print(String configuration) {
		pw.print(configuration);
		pw.close();
	}

}