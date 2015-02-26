package model;

public class Transition {

	private State state;
	private char replacingCharacter;
	private char direction;
	
	public Transition(State state, char replacingCharacter, char direction) {
		this.state = state;
		this.replacingCharacter = replacingCharacter;
		this.direction = direction;
	}
	
	public State getState() {
		return state; 
	}
	
	public char getReplacingCharacter() {
		return replacingCharacter;
	}

	public int getMove() {
		int ret = 1;

		if (direction == 'L')
			ret = -1;

		return ret;
	}
	
}