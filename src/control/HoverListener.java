/*
 * M4105C - Théorie du langage
 *
 * class HoverListener.java
 */

package control;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 * This class is used to show the user when he is hovering over a JButton in the main view
 * It listens to every button in the view's tool bar
 * 
 * @version 1.0 - 02/03/15
 * @author BUSSENEAU Alexis - GRANIER Tristan - SAURAY Antoine
 */
public class HoverListener extends MouseAdapter {
	
	/**
	 * The only instance of this class
	 */
	private static HoverListener INSTANCE = new HoverListener();

	/**
	 * The constructor of this class, not accessible to other classes
	 */
	private HoverListener() {}
	
	/**
	 * Make the hovered button visible by changing its background
	 */
	public void mouseEntered(MouseEvent arg0) {
		JButton btn = (JButton)( arg0.getSource() );
		
		if ( btn.isEnabled() )
			if ( btn.getBackground().equals(Color.GREEN) )
				btn.setBackground( new Color(0, 200, 0) );
			else 
				btn.setBackground( new Color(230, 230, 230) );
	}

	/**
	 * Change the style of the button back to its container background
	 */
	public void mouseExited(MouseEvent arg0) {
		JButton btn = (JButton)( arg0.getSource() );
		
		if ( btn.getBackground().equals(Color.GREEN) || btn.getBackground().equals( new Color(0, 200, 0) ) )
			btn.setBackground(Color.GREEN);
		else
			btn.setBackground(Color.WHITE);
	}
	
	/**
	 * Returns the only instance of this class, and if it hasn't been created yet it construct it once
	 * 
	 * @return the only instance of this class
	 */
	public static HoverListener getInstance() {
		return INSTANCE;
	}

}