package model;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class PrintResults {

	private static final String FILE_NAME = "results";
	
	private static final String FILE_PATH = "../" + FILE_NAME + ".txt";

	PrintWriter pw;

	public PrintResults() throws FileNotFoundException {
		pw = new PrintWriter( new File(FILE_PATH) );
		pw.println("Results from the Turring machine :");
	}

	public void addResult(String result) {
		pw.println(result);
	}

	public void close() {
		pw.close();
	}

}