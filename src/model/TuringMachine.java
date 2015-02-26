package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.Observable;

import java.io.FileNotFoundException;

import model.exception.TuringAcceptedException;
import model.exception.TuringRejectedException;

public class TuringMachine extends Observable {

	private ArrayList<Character> ribbon;
	private State currentState;
	private int head;
	private PrintResults print;
	
	public TuringMachine () throws FileNotFoundException {
		print = new PrintResults();
	}

	public void init(ArrayList<Character> ribbon, State initialState) {
		this.ribbon = ribbon;
		this.currentState = initialState;
		head = 0;
		ribbon.add('⊔');
		printAResult();
	}

	public void step() throws TuringAcceptedException, TuringRejectedException {
		Transition transition = currentState.getTransitions( ribbon.get(head) );
		ribbon.set(head, transition.getReplacingCharacter() );
		currentState = transition.getState();

		head += transition.getMove();
		if (head < 0) 
			head = 0;
		else if ( head == ribbon.size() ) {
			ribbon.add('⊔');
		}

		printAResult();

		// Checks if the Turing machine is over.
		if (currentState == State.QACC)
			throw new TuringAcceptedException();
		else if (currentState == State.QREJ)
			throw new TuringRejectedException();
	}
	
	public void loop() throws TuringAcceptedException, TuringRejectedException {
		while (true) {
			step();
		}
	}

	private void printAResult() {
		String result = "";

		for (int i = 0; i < head; i++)
			result += ribbon.get(i);

		result += "\"" + currentState.getName() + "\"";

		for (int i = head; i < ribbon.size(); i++)
			result += ribbon.get(i);

		print.addResult(result);
	}
	
}