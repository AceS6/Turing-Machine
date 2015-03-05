/*
 * M4105C - Théorie du langage
 *
 * class Transition.java
 */

package model;

/**
 * This class models a transition of a state of a Turing machine.
 *
 * @version 1.0 - 02/03/15
 * @author BUSSENEAU Alexis - GRANIER Tristan - SAURAY Antoine
 * 
 * @see model.State
 */
public class Transition {

 	/*	----- ATTRIBUTES -----	*/
	
	/**
	 * The state of the transition.
	 */
	private State state;
	
	/**
	 * The symbol of the transition.
	 */
	private char replacingSymbol;
	
	/**
	 * The direction of the transition.
	 */
	private char direction;
	
	
 	/*	----- CONSTRUCTOR -----	*/
	
	/**
	 * Creates a transition with the specified values.
	 * 
	 * @param state The state of the transition.
	 * @param replacingSymbol The symbol of the transition.
	 * @param direction The direction of the transition.
	 * 
	 * @throws IllegalArgumentException If the direction is not 'R' or 'L' or if the state is null.
	 */
	public Transition(State state, char replacingSymbol, char direction) {
		if(direction != 'R' && direction != 'L')
			throw new IllegalArgumentException("The direction provided is not good. It should be 'R' or 'L'");
		if(state == null)
			throw new IllegalArgumentException("The state provided is null");
		
		this.state = state;
		this.replacingSymbol = replacingSymbol;
		this.direction = direction;
	}
	
	
 	/*	----- ACCESSORS -----	*/
	
	/**
	 * Gets the state of the transition.
	 * 
	 * @return The state of the transition.
	 */
	public State getState() {
		return state; 
	}
	
	/**
	 * Gets the symbol of the transition.
	 * 
	 * @return The symbol of the transition.
	 */
	public char getReplacingSymbol() {
		return replacingSymbol;
	}

	/**
	 * Converts the move of the transition in position and returns it.
	 * 
	 * @return The position move of the transition.
	 */
	public int getMove() {
		int ret = 1;

		if (direction == 'L')
			ret = -1;

		return ret;
	}
	
}