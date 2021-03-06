/*
 * M4105C - Théorie du langage
 *
 * class XMLChecker.java
 */

package model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.jdom2.Document;
import org.jdom2.Element;

/**
 * This class checks if an XML file has a valid configuration for the Turing machine.
 * It uses the JDOM library.
 *
 * @version 1.0 - 02/03/15
 * @author BUSSENEAU Alexis - GRANIER Tristan - SAURAY Antoine
 */
public class XMLChecker {
	
	/*	----- ATTRIBUTES -----	*/

	/**
	 * The ribbon node name.
	 */
	private static final String RIBBON = "Ribbon";
	
	/**
	 * The sigma node name.
	 */
	private static final String SIGMA = "Σ";
	
	/**
	 * The states node name.
	 */
	private static final String STATES = "Q";
	
	/**
	 * The initial state node name.
	 */
	private static final String INITIAL_STATE = "InitialState";
	
	/**
	 * The transition functions node name.
	 */
	private static final String TRANSITION_FUNCTIONS = "δ";
	
	/**
	 * The breakpoint states node name.
	 */
	private static final String BREAKPOINT_STATES = "BreakpointStates";
	
	/**
	 * The root node of the document file.
	 */
	private Element root;
	
	/**
	 * The initialState loaded.
	 */
	private State initialState;
	
	/**
	 * The list of breakpoint states loaded.
	 */
	private ArrayList<State> breakpointStates;

	
	/*	----- CONSTRUCTOR -----	*/
	
	/**
	 * Creates an XMLchecker from the specified XML document to check.
	 * 
	 * @param configuration The XML document to check.
	 */
	public XMLChecker(Document configuration) {
		// Gets the root element from the document.
		root = configuration.getRootElement();
		breakpointStates = new ArrayList<State>();
	}

	/**
	 * Checks the configuration structure.
	 * 
	 * @throws RuntimeException If the configuration structure is not valid. (One node is missing or there are another nodes).
	 */
	public void checkConfigStructure() throws RuntimeException {
		if ( !root.getName().equals("TuringMachine") )
			throw new RuntimeException("The root node must have \"TuringMachine\" as name. Please correct it.");
		if ( root.getChild(RIBBON) == null )
			throw new RuntimeException("The node " + RIBBON + " is missing. Please insert it like that : <" + RIBBON + "></" + RIBBON + ">");
		if ( root.getChild(SIGMA) == null )
			throw new RuntimeException("The node " + SIGMA + " is missing. Please insert it like that : <" + SIGMA + "></" + SIGMA + ">");
		if ( root.getChild(STATES) == null )
			throw new RuntimeException("The node " + STATES + " is missing. Please insert it like that : <" + STATES + "></" + STATES + ">");
		if ( root.getChild(INITIAL_STATE) == null )
			throw new RuntimeException("The node " + INITIAL_STATE + " is missing. Please insert it like that : <" + INITIAL_STATE + "></" + INITIAL_STATE + ">");
		if ( root.getChild(TRANSITION_FUNCTIONS) == null )
			throw new RuntimeException("The node " + TRANSITION_FUNCTIONS + " is missing. Please insert it like that : <" + TRANSITION_FUNCTIONS + "></" + TRANSITION_FUNCTIONS + ">");
		if ( root.getChild(BREAKPOINT_STATES) == null )
			throw new RuntimeException("The node " + BREAKPOINT_STATES + " is missing. Please insert it like that : <" + BREAKPOINT_STATES + "></" + BREAKPOINT_STATES + ">");
		if ( root.getChildren().size() != 6 )
			throw new RuntimeException("There are another nodes in the XML. Please delete it.");
	}

	/**
	 * Checks if every values containing (or not) in the XML is valid or missing.
	 * 
	 * @throws RuntimeException If an error is found.
	 */
	public void checkConfigValues() throws RuntimeException {
		// Gets ribbon value and alphabet values.
		String ribbon = root.getChild(RIBBON).getText();
		String sigma = root.getChild(SIGMA).getText();
		
		// Checks if the blank symbol is in Σ alphabet.
		if ( !ribbon.equals("") && sigma.equals("") ) 
			throw new RuntimeException("The " + SIGMA + " alphabet is empty while the ribbon is filled.");
		
		// Avoids to fetch "" in the ribbon when the program checks if the ribbon is in Σ alphabet.
		if (sigma.indexOf('⊔') != -1) 
			throw new RuntimeException("The blank symbol '⊔' can't be in " + SIGMA + " alphabet.");

		// Checks if there are duplicated symbols in Σ alphabet.
		// The secret of this loop is the formula to find duplicated symbols.
		// (Length of the alphabet) - (Length of the alphabet without the fetched symbol) = 1 if the symbol is unique in Σ alphabet.
		for (int i = 0; i < sigma.length(); i++)
			if (sigma.length() - sigma.replace( Character.toString( sigma.charAt(i) ), "").length() > 1 )
				throw new RuntimeException("The symbol '" + sigma.charAt(i) + "' is duplicated in " + SIGMA + " aplhabet.");

		// Checks if every symbols from the ribbon is contained in Σ alphabet.
		for (int i = 0; i < ribbon.length(); i++)
			if (sigma.indexOf( ribbon.charAt(i) ) == -1)
				throw new RuntimeException("The symbol '" + ribbon.charAt(i) + "' from the ribbon is not defined in " + SIGMA + " alphabet.");


		// Gets Q, the states set.
		List<Element> listStates = root.getChild(STATES).getChildren();
		HashMap<String, State> states = new HashMap<String, State>();

		// Checks if every state is unique in Q (set of state) and if the name is valid.
		// And puts the states found in a HashMap for an easy access.
		for (Element currentElement : listStates) {
			String stateName = currentElement.getText();

			if ( states.containsKey(stateName) )
				throw new RuntimeException("\"" + stateName + "\" is duplicated in " + STATES + ".");
			else if ( stateName.equals("") )
				throw new RuntimeException("A state contained in " + STATES + " has no name.");

			states.put( stateName, new State(stateName) );
		}


		// Checks if there is at least one state inputed in Q.
		if ( listStates.isEmpty() ) {
			throw new RuntimeException(STATES + " must have at least one state.");
		}


		// Checks if the initial state is inputed.
		if ( root.getChild(INITIAL_STATE).getText().equals("") ) {
			throw new RuntimeException("The initial state must be defined with a state from " + STATES + ".");
		}

		// Checks if the initial state is define in Q.
		if ( !states.containsKey( root.getChild(INITIAL_STATE).getText() ) )
			throw new RuntimeException("The initial state \"" + root.getChild(INITIAL_STATE).getText() + "\" is not contained in " + STATES + ".");


		// Gets the functions transition set.
		List<Element> listTransitions = root.getChild(TRANSITION_FUNCTIONS).getChildren();
		// Initializes the regular expression to get the information from the function.
		Pattern p = Pattern.compile("\\s*\\((.*),\\s*(.)\\)\\s*=\\s*\\((.*),\\s*(.),\\s*(R|L)\\)\\s*");

		// Checks if every transition is correct.
		// If yes, the program adds to each state the matched transition.
		for (Element currentElement : listTransitions) {
			Matcher m = p.matcher( currentElement.getText() );

			// Checks the correct form of the transition.
			if ( m.find() ) {
				Transition aTransition;

				// Checks if the transition symbol is in Σ alphabet or equals to ⊔ (blank symbol).
				if (sigma.indexOf( m.group(4) ) != -1 || m.group(4).charAt(0) == '⊔')
					// Checks if the transition direction is equals to R or L (right and left).
					if ( m.group(5).equals("R") || m.group(5).equals("L") ) {
						State transitionState;

						// Checks the transition state.
						// If it is the accepting state, then the application puts this unique state.
						if ( m.group(3).equals("qAcc") )
							transitionState = State.QACC;
						// The same for rejecting state.
						else if ( m.group(3).equals("qRej") )
							transitionState = State.QREJ;
						// Or puts another state from Q.
						else if ( states.containsKey( m.group(3) ) )
							transitionState = states.get( m.group(3) );
						else
							throw new RuntimeException("\""  + currentElement.getText() + "\", \"" + m.group(3) + "\" is not the accepting state \"qAcc\", the rejecting state \"qRej\" or a state contained in " + STATES + ".");

						// Creates the transition if all is all right.
						aTransition = new Transition( transitionState, m.group(4).charAt(0), m.group(5).charAt(0) );
					}
					else
						throw new RuntimeException("\""  + currentElement.getText() + "\", '" + m.group(5) + "' is not a correct move. Please input 'R' for right or 'L' for left.");
				else
					throw new RuntimeException("\""  + currentElement.getText() + "\", '" + m.group(4) + "' is not a symbol in " + SIGMA + " alphabet or the blank symbol '⊔'.");

				// Checks if the state is defined in Q.
				if ( states.containsKey( m.group(1) ) ) {
					// Checks if the symbol is in Σ alphabet or equals to ⊔ (blank symbol).
					if (sigma.indexOf( m.group(2) ) != -1 || m.group(2).charAt(0) == '⊔')
						states.get( m.group(1) ).addTransition(m.group(2).charAt(0), aTransition);
					else
						throw new RuntimeException("\""  + currentElement.getText() + "\", '" + m.group(2) + "' is not a symbol in " + SIGMA + " alphabet or the blank symbol '⊔'.");
				}
				else
					throw new RuntimeException("\""  + currentElement.getText() + "\", \"" + m.group(1) + "\" is not a state contained in " + STATES + ".");
			}
			else
				throw new RuntimeException("\""  + currentElement.getText() + "\" has not the correct form. \nPlease input this transition like that for instance : (aStateName, a)=(qAcc, ⊔, R).");
		}

		// Checks if every state has a transition for every symbol contained in Σ alphabet and for the blank symbol '⊔'.
		for ( Entry<String, State> currentState : states.entrySet() ) {
			// Checks for every symbol contained in Σ alphabet.
			for (int i = 0; i < sigma.length(); i++) 
				if (currentState.getValue().getTransitions( sigma.charAt(i) ) == null)
					throw new RuntimeException("The transition with the symbol '" + sigma.charAt(i) + "' for the state \"" + currentState.getKey() + "\" is missing.");
			// Checks for the blank symbol '⊔'.
			if ( currentState.getValue().getTransitions('⊔') == null)
				throw new RuntimeException("The transition with the blank symbol '⊔' for the state \"" + currentState.getKey() + "\" is missing.");
		}


		// Gets the breakpoint states set.
		List<Element> listBreakpointStates = root.getChild(BREAKPOINT_STATES).getChildren();

		for (Element currentElement : listBreakpointStates)
			if ( currentElement.getText().equals("") )
				throw new RuntimeException("A state contained in the breakpoint states has no name.");
			else if ( !states.containsKey( currentElement.getText() ) )
				throw new RuntimeException("The breakpoint state \"" + currentElement.getText() + "\" is not a state contained in " + STATES + ".");
			else
				breakpointStates.add( states.get( currentElement.getText() ) );

		// Initializes the initial state object from the initial state got previously.
		initialState = states.get( root.getChild(INITIAL_STATE).getText() );
	}
	
	
	/*	----- ACCESSORS -----	*/

	/**
	 * Gets the ribbon symbols from the XML configuration.
	 * 
	 * @return The list of ribbon symbol.
	 */
	public ArrayList<Character> getRibbonArrayList() {
		ArrayList<Character> ret = new ArrayList<Character>();

		// Converts the ribbon string in array and puts it in the ArrayList with the foreach.
		for ( char currentChar : root.getChild(RIBBON).getText().toCharArray() )
			ret.add(currentChar);

		return ret;
	}
	
	/**
	 * Gets the list of the breakpoint states from the XML configuration.
	 * 
	 * @return The list of the breakpoint states.
	 */
	public ArrayList<State> getBreakpointStatesArrayList(){
		return breakpointStates;
	}

	/**
	 * Gets the initial state of the XML configuration.
	 * 
	 * @return The initial state.
	 */
	public State getInitialStateObject() {
		return initialState;
	}
	
	/**
	 * Gets the ribbon into a string.
	 * 
	 * @return The ribbon in string format.
	 */
	public String getRibbon() {
		return root.getChild(RIBBON).getText();
	}

	/**
	 * Gets the sigma alphabet into a string.
	 * 
	 * @return The sigma alphabet in string format.
	 */
	public String getSigma() {
		String sigma = root.getChild(SIGMA).getText();
		String ret = "";
		
		// Avoids to fetch into an empty alphabet.
		if ( !sigma.equals("") ) {
			// Gets the first symbol of the alphabet.
			sigma += Character.toString( sigma.charAt(0) );
		
			// And add the others if exist.
			for (int i = 1; i < sigma.length(); i++)
				ret += ", " + Character.toString( sigma.charAt(i) );
		}

		return ret;
	}

	/**
	 * Gets the list of states name into a string.
	 * 
	 * @return The list of states name in string format.
	 */
	public String getStates() {
		// Gets the list of states.
		List<Element> listStates = root.getChild(STATES).getChildren();
		String ret = listStates.get(0).getText();

		// And gets its name for each state of the list.
		for (int i = 1; i < listStates.size(); i++)
			ret += ", " + listStates.get(i).getText();

		return ret;
	}

	/**
	 * Gets the initial state name into a string.
	 * 
	 * @return The initial state name in string format.
	 */
	public String getInitialState() {
		return root.getChild(INITIAL_STATE).getText();
	}

	/**
	 * Gets the transition functions into a string.
	 * 
	 * @return The transition functions in string format.
	 */
	public String getTransitionFunctions() {
		List<Element> listTransitions = root.getChild(TRANSITION_FUNCTIONS).getChildren();
		String ret = listTransitions.get(0).getText();

		for (int i = 1; i < listTransitions.size(); i++)
			ret += ",\n	    " + listTransitions.get(i).getText();

		return ret;
	}

}