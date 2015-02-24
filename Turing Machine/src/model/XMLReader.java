package model;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.JDOMException;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.IOException;

public class XMLReader {

	private static final SAXBuilder SXB = new SAXBuilder();

	private XMLReader() {}

	public static Document xmlToDocument(File xmlFile) throws JDOMException, IOException {
		return SXB.build(xmlFile);
	}

	public static String xmlToString(File xmlFile) throws JDOMException, IOException {
		XMLOutputter outputFile = new XMLOutputter();

		return outputFile.outputString( XMLReader.xmlToDocument(xmlFile) );
	}

	public static Document StringToXml(String configuration) throws JDOMException, IOException {
		return SXB.build(configuration);
	}

}