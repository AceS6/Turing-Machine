/*
 * M4105C - Théorie du langage
 *
 * class Launcher.java
 */

package test;

import model.TuringMachine;
import view.TuringMachineView;
import control.ButtonsListener;

/**
 * This class is the launcher of the application.
 *
 * @version 1.0 - 02/03/15
 * @author BUSSENEAU Alexis - GRANIER Tristan - SAURAY Antoine
 */
public class Launcher {

	public static void main(String[] arg) {
		// Creates the Turing machine.
		TuringMachine model = new TuringMachine();
		// And launches the controller and the view.
		new ButtonsListener( model, new TuringMachineView(model) );
	}

}