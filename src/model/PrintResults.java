/*
 * M4105C - Théorie du langage
 *
 * class PrintResults.java
 */

package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

/**
 * This class prints into a file, the results from the Turing machine.
 *
 * @version 1.0 - 02/03/15
 * @author BUSSENEAU Alexis - GRANIER Tristan - SAURAY Antoine
 */
public class PrintResults {
	
 	/*	----- ATTRIBUTES -----	*/

	/**
	 * The name of the results file.
	 */
	private static final String FILE_NAME = "results";
	
	/**
	 * The path of the results file.
	 */
	public static final String FILE_PATH = "../" + FILE_NAME + ".txt";

	/**
	 * The results file.
	 */
	private File resultsFile;

	
 	/*	----- CONSTRUCTOR -----	*/
	
	/**
	 * Creates a results file from the path of the results file.
	 */
	public PrintResults() {
		resultsFile = new File(FILE_PATH);
		
		try {
			// Writes a title in the results file.
			PrintWriter pw = new PrintWriter(resultsFile);
			pw.println("Results from the Turing machine :");
			pw.close();
		}
		catch (FileNotFoundException e) {}
	}
	
	
 	/*	----- OTHER METHODS -----	*/

	/**
	 * Adds a results in the results file.
	 * 
	 * @param result The result to print in the results file.
	 */
	public void addResult(String result) {
		try {
			// Writes in the results file, the results specified.
			// We use a FileOutputStream with the "true" parameter to add a line (not overwrite).
			PrintWriter pw = new PrintWriter( new FileOutputStream(resultsFile, true) );
			pw.println(result);
			pw.close();
		}
		catch (FileNotFoundException e) {}
	}

}