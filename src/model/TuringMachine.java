/*
 * M4105C - Théorie du langage
 *
 * class TuringMachine.java
 */

package model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * This class models a Turing machine.
 * It implements Observable because it's the main data of the application.
 * The view has to observes this class to update it when the machine changed.
 * It's a part of the Observer pattern.
 *
 * @version 1.0 - 02/03/15
 * @author BUSSENEAU Alexis - GRANIER Tristan - SAURAY Antoine
 * 
 * @see view.TuringMachineView
 * @see model.PrintResults
 */
public class TuringMachine extends Observable {
	
 	/*	----- ATTRIBUTES -----	*/

	/**
	 * The ribbon of the Turing machine.
	 */
	private ArrayList<Character> ribbon;
	
	/**
	 * The current state of the Turing machine.
	 */
	private State currentState;
	
	/**
	 * The list of states where Turing machine has to stop.
	 */
	private ArrayList<State> breakpointStates;
	
	/**
	 * The position of the read head of the Turing machine.
	 */
	private int head;
	
	/**
	 * The speed in milliseconds of the Turing machine.
	 */
	private int speed;
	
	/**
	 * The state mode value.
	 */
	private boolean stateMode;
	
	/**
	 * The thread which executes the Turing machine.
	 */
	private Thread thread;
	
	/**
	 * The class which prints the results of the Turing machine.
	 */
	private PrintResults print;
	
	
 	/*	----- CONSTRUCTOR -----	*/
	
	public TuringMachine() {
		stateMode = false;
	}

	
 	/*	----- OTHER METHODS -----	*/
	
	/**
	 * Initializes the Turing machine with the specified values.
	 * 
	 * @param ribbon The initial ribbon.
	 * @param initialState The initial state.
	 * @param breakpointStates The list of state where the Turing machine has to stop.
	 * 
	 * @throws IllegalArgumentException If the initial state has no transitions declared.
	 */
	public void init(ArrayList<Character> ribbon, State initialState, ArrayList<State> breakpointStates) {
		if (initialState.getTransitions().size() == 0)
			throw new IllegalArgumentException("The initial state has no transitions declared. Please provide a State with initialised transitions.");
		
		// Fills the ribbon with the symbols specified.
		// It notifies each new symbol to the view to update it.
		this.ribbon = new ArrayList<Character>();
		for (char currentChar : ribbon)
			addSymbol(currentChar);
		
		// Initializes the current state by the initial state given by the user configuration.
		this.currentState = initialState;
		this.breakpointStates = breakpointStates;
		
		// Initializes the head on the first ribbon symbol.
		head = 0;
		// Adds a blank symbol to the end of the ribbon.
		addSymbol('⊔');
		
		// Initializes the current symbol and current state on the view.
		setChanged();
        notifyObservers("Move");
		
        // Creates and prints the initial result.
        print = new PrintResults();
		printAResult();
	}
	
	/**
	 * Adds a symbol into the ribbon.
	 * 
	 * @param symbol The symbol to add.
	 */
	public void addSymbol(char symbol) {
		this.ribbon.add(symbol);
		
		// Notifies the view that there is a new symbol in the ribbon.
		setChanged();
        notifyObservers(symbol);
	}

	/**
	 * Executes a move on the ribbon with the current symbol and the current state.
	 * 
	 * @throws InterruptedException When the Turing machine is over.
	 */
	public void step() throws InterruptedException {
		// Gets the transition from the current state and the current symbol.
		Transition transition = currentState.getTransitions( ribbon.get(head) );
		// Updates the ribbon with the new symbol from the transition.
		ribbon.set(head, transition.getReplacingSymbol() );
		
		// Notifies the view that the ribbon has been updated.
		setChanged();
        notifyObservers("Update");
        
        // Sets the new state.
		currentState = transition.getState();
		// Moves the head on the ribbon.
		head += transition.getMove();
		
		// Avoid to be outside of the ribbon.
		if (head < 0) 
			head = 0;
		// Adds a blank symbol if the head is on the last symbol of the ribbon.
		else if ( head == ribbon.size() ) {
			addSymbol('⊔');
		}
		
		// Notifies the view that there is a move.
		setChanged();
        notifyObservers("Move");
        
        // Prints the current result of the Turing machine.
        printAResult();

		// Checks if the Turing machine is over.
		if (currentState == State.QACC || currentState == State.QREJ) {
			// Notifies that the Turing machine is over.
			setChanged();
       		notifyObservers("Stop");
       		// Interrupts the machine.
       		throw new InterruptedException();
		}
	}
	
	/**
	 * Executes the Turing machine until it stops.
	 * It is executed in a thread to not lock the view.
	 * It is possible that this loop never stops.
	 */
	public void loop() {
		// Creation of the thread in anonymous class.
		thread = new Thread() {
			
			public void run() {
				// Stop if the Turing machine is interrupted.
				while ( !isInterrupted() ) {
					try	{
						// Executes a single execution.
						step();
						// Makes a pause with the speed specified by the slider.
						sleep(speed);
						
						// Stop the execution if the current state is a state where the Turing machine has to stop and the state mode is on.
						if ( stateMode && breakpointStates.contains(currentState) ) {
							pauseThread();
						}
					}
					catch (InterruptedException e) {
						// Interrupts the thread.
						Thread.currentThread().interrupt();
					}
				}
			}
		};
		
		// Launches the thread.
		thread.start();
	}
	
	/**
	 * Pauses the execution of the Turing machine.
	 */
	public void pauseThread() {
		// Notifies to the view that there is a pause in the execution.
		setChanged();
        notifyObservers("Pause");
        // And stop the machine.
		thread.interrupt();
	}

	/**
	 * Prints a result from the current Turing machine state.
	 */
	private void printAResult() {
		String result = "";

		// Gets 'u' the word containing the symbols until the current state.
		for (int i = 0; i < head; i++)
			result += ribbon.get(i);

		// Gets the name of the current state.
		result += "\"" + currentState.getName() + "\"";

		// Gets 'v' the word containing the symbols until the end of the ribbon.
		for (int i = head; i < ribbon.size(); i++)
			result += ribbon.get(i);

		// Adds the results to the results file.
		print.addResult(result);
	}
	
	
 	/*	----- ACCESSORS -----	*/
	
	/**
	 * Gets the head position.
	 * 
	 * @return The head position.
	 */
	public int getHead() {
		return head;
	}
	
	/**
	 * Gets the current symbol pointed by the head.
	 * 
	 * @return The current symbol.
	 */
	public char getCurrentSymbol() {
		return ribbon.get(head);
	}
	
	/**
	 * Gets the current state of the Turing machine.
	 * 
	 * @return The current state.
	 */
	public State getCurrentState() {
		return currentState;
	}
	
	/**
	 * Gets the speed of the machine.
	 * 
	 * @return The speed of the machine.
	 */
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * Gets the value of the state mode.
	 * 
	 * @return The state mode value.
	 */
	public boolean getStateMode() {
		return stateMode;
	}
	
	
 	/*	----- ACCESSORS -----	*/
	
	/**
	 * Sets the speed of the machine.
	 * 
	 * @param speed The speed of the Turing machine.
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	/**
	 * Changes the state mode.
	 */
	public void setStateMode() {
		stateMode = !stateMode;
	}

}