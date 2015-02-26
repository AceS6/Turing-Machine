package model;

import java.util.HashMap;

public class State {

	private HashMap<Character, Transition> transitions;

	private String name;

	public static final State QACC = new State("qAcc");

	public static final State QREJ = new State("qRej");
	
	public State(String name) {
		this.name = name;
		transitions = new HashMap<Character, Transition>();
	}
	
	public void addTransition(Character character, Transition transition) {
		transitions.put(character, transition);
	}
	
	public Transition getTransitions(Character character) {
		return transitions.get(character);
	}
	
	public String getName() {
		return name;
	}
	
}