package model;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class XMLWriter {

	private XMLWriter() {}

	public static void write(File xmlFile, String configuration) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(xmlFile);
		pw.print(configuration);
		pw.close();
	}

}