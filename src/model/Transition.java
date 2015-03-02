package model;

public class Transition {

	private State state;
	private char replacingCharacter;
	private char direction;
	
	public Transition(State state, char replacingCharacter, char direction){
		
		if(direction != 'R' && direction != 'L'){
			throw new IllegalArgumentException("The direction provided is not good. It should be 'R' or 'L'");
		}
		
		if(state == null){
			throw new IllegalArgumentException("The state provided is null");
		}

		
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