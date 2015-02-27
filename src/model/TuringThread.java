package model;

import model.exception.TuringAcceptedException;
import model.exception.TuringRejectedException;



public class TuringThread extends Thread{
	
	private TuringMachine machine;
	private int mode;
	
	public static final int LOOP = 1, STATE = 2;
	
	public TuringThread(TuringMachine machine, int mode){
		this.machine = machine;
		this.mode = mode;
	}
	
	@Override
	public void run(){
		// TODO Auto-generated method stub
		try {
			if(mode == TuringThread.LOOP){
				machine.loop();
				try {
					this.sleep(machine.getSpeed());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(mode == TuringThread.STATE){
				machine.loopInStateMode();
			}
		} catch (TuringAcceptedException | TuringRejectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}