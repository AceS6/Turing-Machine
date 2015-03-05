/*
 * M4105C - Théorie du langage
 *
 * class State.java
 */

package model;

import java.util.HashMap;

/**
 * This class models a state of Turing machine.
 *
 * @version 1.0 - 02/03/15
 * @author BUSSENEAU Alexis - GRANIER Tristan - SAURAY Antoine
 * 
 * @see model.Transition
 */
public class State {
	
 	/*	----- ATTRIBUTES -----	*/
	
	/**
	 * The name of the state.
	 */
	private String name;

	/**
	 * The list of transition functions of the state.
	 */
	private HashMap<Character, Transition> transitions;

	/**
	 * The unique accepting state of Turing machine.
	 */
	public static final State QACC = new State("qAcc");

	/**
	 * the unique rejecting state of Turing machine.
	 */
	public static final State QREJ = new State("qRej");
	
	
 	/*	----- CONSTRUCTOR -----	*/
	
	/**
	 * Creates a state without its transitions.
	 * 
	 * @param name The name of the Turing machine.
	 */
	public State(String name) {
		this.name = name;
		transitions = new HashMap<Character, Transition>();
	}
	
	
	/*	----- MUTATORS -----	*/
	
	/**
	 * Adds a transition from the specified symbol and the specified transition.
	 * 
	 * @param symbol The associated symbol of the transition.
	 * @param transition The transition of the symbol.
	 */
	public void addTransition(Character symbol, Transition transition) {
		transitions.put(symbol, transition);
	}
	
	
	/*	----- ACCESSORS -----	*/
	
	/**
	 * Gets the name of the state.
	 * 
	 * @return The state name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the list of the transitions of the state.
	 * 
	 * @return The list of the transitions.
	 */
	public HashMap<Character, Transition> getTransitions() {
		return transitions;
	}
	
	/**
	 * Gets a transition from a specified symbol.
	 * 
	 * @param symbol The associated symbol with a transition..
	 * 
	 * @return The desired transition.
	 */
	public Transition getTransitions(Character symbol) {
		return transitions.get(symbol);
	}
	
}