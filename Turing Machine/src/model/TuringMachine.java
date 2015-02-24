package model;

import java.util.Observable;

public class TuringMachine extends Observable {

	private boolean existe;

	public void setExiste(boolean existe) {
		this.existe = existe;
		setChanged();
		notifyObservers();
	} 

	public boolean getExiste() {
		return existe;
	}   
}