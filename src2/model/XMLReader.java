package model;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.JDOMException;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

public class XMLReader {

	private SAXBuilder sxb;

	public XMLReader() {
		sxb = new SAXBuilder();
	}

	public Document xmlToDocument(File xmlFile) throws JDOMException, IOException {
		return sxb.build(xmlFile);
	}

	public String xmlToString(File xmlFile) throws JDOMException, IOException {
		XMLOutputter outputFile = new XMLOutputter();

		return outputFile.outputString( xmlToDocument(xmlFile) );
	}

	public Document stringToXml(String configuration) throws JDOMException, IOException {
		StringReader stringReader = new StringReader(configuration);

		return sxb.build(stringReader);
	}

}