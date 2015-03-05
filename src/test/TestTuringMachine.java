package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import model.State;
import model.Transition;
import model.TuringMachine;

import org.junit.Test;

public class TestTuringMachine {

	@Test
	public void testInit(){
		TuringMachine tm = new TuringMachine();
		
		ArrayList<Character> ribbon = new ArrayList<Character>();
		ribbon.add('a');
		ribbon.add('b');
		
		State bp1 = new State("bp1");
		State bp2 = new State("bp2");
		
		State initialState = new State("q1");
		initialState.addTransition('⊔', new Transition(bp1, 'b', 'L'));
		initialState.addTransition('a', new Transition(bp1, 'a', 'R'));
		

		
		ArrayList<State> breakpointStates = new ArrayList<State>();
		breakpointStates.add(bp1);
		breakpointStates.add(bp2);
		
		
		tm.init(new ArrayList<Character>(), initialState, breakpointStates);
		tm.addSymbol('a');
		tm.addSymbol('b');
		
		assertEquals(tm.getHead(), 0);
		assertEquals(tm.getCurrentSymbol(), '⊔');
		assertEquals(tm.getCurrentState(), initialState);
		
		assertEquals(tm.getSpeed(), 0);
		
		assertFalse(tm.getStateMode());
		tm.setStateMode();
		assertTrue(tm.getStateMode());
		

		
		try {
			tm.step();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(tm.getCurrentSymbol(), 'b');
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testInitPreCondition(){
		TuringMachine tm = new TuringMachine();
		
		ArrayList<Character> ribbon = new ArrayList<Character>();
		ribbon.add('a');
		ribbon.add('b');
		
		State initialState = new State("q1");
		
		State bp1 = new State("bp1");
		State bp2 = new State("bp1");
		
		ArrayList<State> breakpointStates = new ArrayList<State>();
		breakpointStates.add(bp1);
		breakpointStates.add(bp2);
		
		
		tm.init(new ArrayList<Character>('c'), initialState, breakpointStates);
	
		assertEquals(tm.getHead(), 0);
		assertEquals(tm.getCurrentSymbol(), '⊔');
		assertEquals(tm.getCurrentState(), initialState);
		
		assertEquals(tm.getSpeed(), 0);
		
		assertFalse(tm.getStateMode());
		tm.setStateMode();
		assertTrue(tm.getStateMode());
		
		tm.addSymbol('b');
		
		try {
			tm.step();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertEquals(tm.getCurrentSymbol(), 'b');
	}
	
}
