package test;

import junit.framework.TestCase;
import model.State;
import model.Transition;

import org.junit.Test;


public class TestState extends TestCase{

	@Test
	public static void testConstructor(){
		State q1 = new State("q1");
		assertEquals(q1.getName(),"q1");
		assertNotNull(q1.getTransitions());
	}
	
	public static void testAddTransition(){
		State q1 = new State("q1");
		State q2 = new State("q2");
		q1.addTransition('a', new Transition(q2, 'c', 'R'));
		
		assertNotNull(q1.getTransitions('a'));
		assertNull(q1.getTransitions('c'));
	}
	
}
