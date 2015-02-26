package test;

import model.TuringMachine;
import view.TuringMachineView;
import control.ButtonsListener;
import java.io.FileNotFoundException;

public class Launcher {

	public static void main(String[] arg) {
		try {
			TuringMachine model = new TuringMachine();
			new ButtonsListener( model, new TuringMachineView(model) );
		}
		catch (FileNotFoundException e) {
			System.out.println( e.getMessage() );
		}
	}

}