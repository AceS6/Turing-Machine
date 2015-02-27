package model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.jdom2.Document;
import org.jdom2.Element;

public class XMLChecker {

	private static final String RIBBON = "Ribbon";

	private static final String SIGMA = "Σ";

	private static final String STATES = "Q";

	private static final String INITIAL_STATE = "InitialState";

	private static final String TRANSITION_FUNCTIONS = "δ";

	private static final String BREAKPOINT_STATES = "BreackpointStates";

	private Document configuration;

	private Element root;

	private State initialState;

	public XMLChecker(Document configuration) {
		this.configuration = configuration;
		root = configuration.getRootElement();
	}

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
		if ( root.getContentSize() != 13 )
			throw new RuntimeException("There are another nodes in the XML. Please delete it.");
	}

	public void checkConfigValues() throws RuntimeException {
		// Gets ribbon value and alphabet values.
		String ribbon = root.getChild(RIBBON).getText();
		String sigma = root.getChild(SIGMA).getText();

		// Avoids to fetch "" in the ribbon when the program checks if the ribbon is in Σ alphabet.
		if ( !ribbon.equals("") && sigma.equals("") ) 
			throw new RuntimeException("Error : The alphabet Σ is empty while the ribbon is filled.");

		// Checks if there are duplicated symbols in Σ alphabet.
		// The secret of this loop is the formula to find duplicated symbols.
		// (Length of the alphabet) - (Length of the alphabet without the fetched symbol) = 1 if the symbol is unique in Σ alphabet.
		for (int i = 0; i < sigma.length(); i++)
			if (sigma.length() - sigma.replace( Character.toString( sigma.charAt(i) ), "").length() > 1 )
				throw new RuntimeException("Error : The symbol '" + sigma.charAt(i) + "' is duplicated in Σ aplhabet.");

		// Checks if every symbols from the ribbon is contained in sigma alphabet.
		for (int i = 0; i < ribbon.length(); i++)
			if (sigma.indexOf( ribbon.charAt(i) ) == -1)
				throw new RuntimeException("Error : The symbol '" + ribbon.charAt(i) + "' from the ribbon is not defined in Σ alphabet.");


		// Gets Q, the states set.
		List<Element> listStates = root.getChild(STATES).getChildren();
		HashMap<String, State> states = new HashMap<String, State>();

		// Checks if every state is unique in Q (set of state) and if the name is valid.
		// And puts the states found in a HashMap for an easy access.
		for (Element currentElement : listStates) {
			String stateName = currentElement.getText();

			if ( states.containsKey(stateName) )
				throw new RuntimeException("Error : \"" + stateName + "\" is duplicated in Q.");
			else if ( stateName.equals("") )
				throw new RuntimeException("Error : A state have no name. Please input a name.");

			states.put( stateName, new State(stateName) );
		}


		// Checks if there is at least one state inputted in Q.
		if ( listStates.isEmpty() ) {
			throw new RuntimeException("Error : Q must have at least one state.");
		}


		// Checks if the inital state is inputted.
		if ( root.getChild(INITIAL_STATE).getText().equals("") ) {
			throw new RuntimeException("Error : q0 must be defined.");
		}

		// Checks if the initial state is define in Q.
		if ( !states.containsKey( root.getChild(INITIAL_STATE).getText() ) )
			throw new RuntimeException("Error : The initial state \"" + root.getChild(INITIAL_STATE).getText() + "\" is not contained in Q.");


		// Gets the functions transistion set.
		List<Element> listTransitions = root.getChild(TRANSITION_FUNCTIONS).getChildren();
		// Initializes the regular expression to get the information from the fonction.
		Pattern p = Pattern.compile("\\s*\\((.*),\\s*(.)\\)\\s*=\\s*\\((.*),\\s*(.),\\s*(R|L)\\)\\s*");

		// Checks if every transition is correct.
		// If yes, the programm adds to each state the matched transition.
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
						// If it is the accepting state, then the programm puts this unique state.
						if ( m.group(3).equals("qAcc") )
							transitionState = State.QACC;
						// The same for rejecting state.
						else if ( m.group(3).equals("qRej") )
							transitionState = State.QREJ;
						// Or puts another state from Q.
						else if ( states.containsKey( m.group(3) ) )
							transitionState = states.get( m.group(3) );
						else
							throw new RuntimeException("Error : \""  + currentElement.getText() + "\", \"" + m.group(3) + "\" is not the accepting state (qAcc), the rejecting state (qRej) or a state contained in Q.");

						// Creates the transition if all is alright.
						aTransition = new Transition( transitionState, m.group(4).charAt(0), m.group(5).charAt(0) );
					}
					else
						throw new RuntimeException("Error : \""  + currentElement.getText() + "\", '" + m.group(5) + "' is not a right move.");
				else
					throw new RuntimeException("Error : \""  + currentElement.getText() + "\", '" + m.group(4) + "' is not a symbol in Σ alphabet or the blank symbol '⊔'.");

				// Checks if the state is defined in Q.
				if ( states.containsKey( m.group(1) ) ) {
					// Checks if the symbol is in Σ alphabet or equals to ⊔ (blank symbol).
					if (sigma.indexOf( m.group(2) ) != -1 || m.group(2).charAt(0) == '⊔')
						states.get( m.group(1) ).addTransition(m.group(2).charAt(0), aTransition);
					else
						throw new RuntimeException("Error : \""  + currentElement.getText() + "\", '" + m.group(2) + "' is not a symbol in Σ alphabet or the blank symbol '⊔'.");
				}
				else
					throw new RuntimeException("Error : \""  + currentElement.getText() + "\", \"" + m.group(1) + "\" is not a state contained in Q.");
			}
			else
				throw new RuntimeException("Error : \""  + currentElement.getText() + "\" has not the correct form. \nPlease input this transition like that for instance	 : (aStateName, a)=(qAcc, ⊔, R). \nDo not forget to put only a symbol or ⊔ for the blank symbol.");
		}

		// Checks if every state has a transition for every symbol contained in Σ alphabet and for the blank symbol '⊔'.
		for ( Entry<String, State> currentState : states.entrySet() ) {
			// Checks for every symbol contained in Σ alphabet.
			for (int i = 0; i < sigma.length(); i++) 
				if (currentState.getValue().getTransitions( sigma.charAt(i) ) == null)
					throw new RuntimeException("Error : The transition with the symbol '" + sigma.charAt(i) + "' for the state \"" + currentState.getKey() + "\" is missing.");
			// Checks for the blank symbol '⊔'.
			if ( currentState.getValue().getTransitions('⊔') == null)
				throw new RuntimeException("Error : The transition with the blank symbol '⊔' for the state \"" + currentState.getKey() + "\" is missing.");
		}


		// Gets the breakpoint states set.
		List<Element> listBreakpointStates = root.getChild(BREAKPOINT_STATES).getChildren();

		for (Element currentElement : listBreakpointStates)
			if ( !states.containsKey( currentElement.getText() ) )
				throw new RuntimeException("Error : \"" + currentElement.getText() + "\" is not a state contained in Q.");

		// Initializes the initial state object from the initial state got previously.
		initialState = states.get( root.getChild(INITIAL_STATE).getText() );
	}

	public ArrayList<Character> getRibbonArrayList() {
		ArrayList<Character> ret = new ArrayList<Character>();

		for ( char currentChar : root.getChild(RIBBON).getText().toCharArray() )
			ret.add(currentChar);

		return ret;
	}
	
	public ArrayList<State> getBreakPointStates(){
		return null;
	}

	public State getInitialStateObject() {
		return initialState;
	}
	
	public String getRibbon() {
		return root.getChild(RIBBON).getText();
	}

	public String getSigma() {
		String sigma = root.getChild(SIGMA).getText();
		String ret = Character.toString( sigma.charAt(0) );

		for (int i = 1; i < sigma.length(); i++)
			ret += ", " + Character.toString( sigma.charAt(i) );

		return ret;
	}

	public String getStates() {
		List<Element> listStates = root.getChild(STATES).getChildren();
		String ret = listStates.get(0).getText();

		for (int i = 1; i < listStates.size(); i++)
			ret += ", " + listStates.get(i).getText();

		return ret;
	}

	public String getInitialState() {
		return root.getChild(INITIAL_STATE).getText();
	}

	public String getTransitionFunctions() {
		List<Element> listTransitions = root.getChild(TRANSITION_FUNCTIONS).getChildren();
		String ret = listTransitions.get(0).getText();

		for (int i = 1; i < listTransitions.size(); i++)
			ret += ",\n	    " + listTransitions.get(i).getText();

		return ret;
	}

}