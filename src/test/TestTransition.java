package test;

import static org.junit.Assert.*;

import org.junit.Test;

import model.State;
import model.Transition;

public class TestTransition{
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor1(){
		Transition t1 = new Transition(new State("q1"), 'c', 'R');
		assertNotNull(t1.getState());
		assertEquals(t1.getReplacingSymbol(), 'c');
		assertEquals(t1.getMove(), 1);
		Transition t2 = new Transition(new State("q1"), 'c', 'L');
		assertEquals(t2.getMove(), -1);
		
		Transition t3 = new Transition(new State("q1"), 'c', 'V');
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor2(){
		Transition t3 = new Transition(null, 'c', 'V');
	}
	
	@Test
	public void testGetState(){
		State s1 = new State("q1");
		Transition t1 = new Transition(s1, 'c', 'R');
		assertEquals(s1, t1.getState());
	}
	
	@Test
	public void testGetReplacingCharacter(){
		State s1 = new State("q1");
		Transition t1 = new Transition(s1, 'c', 'R');
		assertEquals('c', t1.getReplacingSymbol());
	}
	
	@Test
	public void testGetMove(){
		State s1 = new State("q1");
		Transition t1 = new Transition(s1, 'c', 'R');
		assertEquals(1, t1.getMove());
		
		Transition t2 = new Transition(s1, 'c', 'L');
		assertEquals(-1, t2.getMove());
	}
	
}
