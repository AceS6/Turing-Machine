package model;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class XMLWriter {

	private PrintWriter pw;

	public XMLWriter(File xmlFile) throws FileNotFoundException {
		pw = new PrintWriter(xmlFile);
	}

	public void print(String configuration) {
		pw.print(configuration);
		pw.close();
	}

}