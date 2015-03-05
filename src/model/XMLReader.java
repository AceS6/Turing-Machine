/*
 * M4105C - Théorie du langage
 *
 * class XMLReader.java
 */

package model;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.JDOMException;
import org.jdom2.output.XMLOutputter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class reads an XML configuration for a Turing machine from different format.
 * It uses the JDOM library.
 *
 * @version 1.0 - 02/03/15
 * @author BUSSENEAU Alexis - GRANIER Tristan - SAURAY Antoine
 */
public class XMLReader {
	
	/*	----- ATTRIBUTE -----	*/

	/**
	 * The SAXBuilder which allows to read XML file.
	 */
	private SAXBuilder sxb;

	
	/*	----- CONSTRUCTOR -----	*/
	
	/**
	 * Creates a XMLReader.
	 */
	public XMLReader() {
		sxb = new SAXBuilder();
	}

	
	/*	----- OTHER METHODS -----	*/
	
	/**
	 * Converts an XML file into a Document (an XML object to use with JDOM).
	 * 
	 * @param xmlFile The XML file to convert.
	 * 
	 * @return The document builds from the XML file.
	 * 
	 * @throws JDOMException If the file is empty (there are no nodes in the XML file).
	 * @throws IOException If the file is not found.
	 */
	public Document xmlToDocument(File xmlFile) throws JDOMException, IOException {
		return sxb.build(xmlFile);
	}

	/**
	 * Converts an XML file into a string.
	 * 
	 * @param xmlFile The XML file to convert.
	 * 
	 * @return The XML file in string format.
	 * 
	 * @throws JDOMException If the file is empty (there are no nodes in the XML file).
	 * @throws IOException If the file is not found.
	 */
	public String xmlToString(File xmlFile) throws JDOMException, IOException {
		XMLOutputter outputFile = new XMLOutputter();

		// Converts the XMLOutputter in document and then in string.
		return outputFile.outputString( xmlToDocument(xmlFile) );
	}

	/**
	 * Converts a string in XML Document.
	 * 
	 * @param configuration The XML configuration in string format.
	 * 
	 * @return The XML Document of the configuration.
	 * 
	 * @throws JDOMException If there is an error during the building of the document.
	 * @throws IOException When an I/O error prevents during the building of the document.
	 */
	public Document stringToXml(String configuration) throws JDOMException, IOException {
		InputStream input = new ByteArrayInputStream( configuration.getBytes() );

		return sxb.build(input);
	}

}