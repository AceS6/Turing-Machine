package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class PrintResults {

	private static final String FILE_NAME = "results";
	
	public static final String FILE_PATH = "../" + FILE_NAME + ".txt";

	private File resultsFile;

	public PrintResults() {
		resultsFile = new File(FILE_PATH);
		
		try {
			PrintWriter pw = new PrintWriter(resultsFile);
			pw.println("Results from the Turring machine :");
			pw.close();
		}
		catch (FileNotFoundException e) {}
	}

	public void addResult(String result) {
		try {
			PrintWriter pw = new PrintWriter( new FileOutputStream(resultsFile, true) );
			pw.println(result);
			pw.close();
		}
		catch (FileNotFoundException e) {}
	}

}