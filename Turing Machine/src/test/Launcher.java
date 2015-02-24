package test;

import model.TuringMachine;
import view.TuringMachineView;
import control.ButtonsListener;

public class Launcher {

	public static void main(String[] arg) {
		TuringMachine model = new TuringMachine();
		new ButtonsListener( model, new TuringMachineView(model) );
	}

}