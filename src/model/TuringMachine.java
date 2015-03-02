package model;

import java.util.ArrayList;
import java.util.Observable;

public class TuringMachine extends Observable {

	private ArrayList<Character> ribbon;
	private State currentState;
	private ArrayList<State> breakpointStates;
	private int head;
	private int speed;
	private boolean stateMode;
	private Thread thread;
	private PrintResults print;
	
	public TuringMachine() {
		stateMode = false;
	}

	public void init(ArrayList<Character> ribbon, State initialState, ArrayList<State> breakpointStates) {
		this.ribbon = new ArrayList<Character>();
		for (char currentChar : ribbon)
			addSymbol(currentChar);
			
		if(initialState.getTransitions().size() == 0){
			throw new IllegalArgumentException("The initial state has no transitions declared. Please provide a State with initialised transitions.");
		}
		this.currentState = initialState;
		this.breakpointStates = breakpointStates;
		
		head = 0;
		addSymbol('⊔');
		
		setChanged();
        notifyObservers("Move");
		
        print = new PrintResults();
		printAResult();
	}
	
	public void addSymbol(char symbol) {
		this.ribbon.add(symbol);
		setChanged();
        notifyObservers(symbol);
	}

	public void step() throws InterruptedException {
		Transition transition = currentState.getTransitions( ribbon.get(head) );
		ribbon.set(head, transition.getReplacingCharacter() );
		
		setChanged();
        notifyObservers("Update");
        
		currentState = transition.getState();
		head += transition.getMove();
		
		if (head < 0) 
			head = 0;
		else if ( head == ribbon.size() ) {
			addSymbol('⊔');
		}
		
		setChanged();
        notifyObservers("Move");
        
        printAResult();

		// Checks if the Turing machine is over.
		if (currentState == State.QACC || currentState == State.QREJ) {
			setChanged();
       		notifyObservers("Stop");
       		throw new InterruptedException();
		}
	}
	
	public void loop() {
		thread = new Thread() {
			
			public void run() {
				while ( !isInterrupted() ) {
					try	{
						step();
						sleep(speed);
						
						if ( stateMode && breakpointStates.contains(currentState) ) {
							pauseThread();
						}
					}
					catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		};
		
		thread.start();
	}
	
	public void pauseThread() {
		setChanged();
        notifyObservers("Pause");
		thread.interrupt();
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
	
	public int getHead() {
		return head;
	}
	
	public char getCurrentSymbol() {
		return ribbon.get(head);
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setStateMode() {
		stateMode = !stateMode;
	}
	
	public boolean getStateMode() {
		return stateMode;
	}
}