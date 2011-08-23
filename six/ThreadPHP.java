/* ThreadPHP.java 
 * Created on Dec 7, 2006 
 * by ilteris 
 * original thread example from Daniel Shiffman
 * see this url:
 * http://www.shiffman.net/teaching/programming-from-a-to-z/threads/
 * 
 * 
 */ 
package six; 

import processing.core.PApplet;

public class ThreadPHP extends Thread{
	private boolean running;           // Is the thread running?  Yes or no?
	private int wait;                  // How many milliseconds should we wait in between executions?
	private boolean available;         // Is new file available?
	public String[] s;
	public Text[] t;
	public Island i;
	
	PApplet p;
	public ThreadPHP(PApplet p_, int x, String[] s2, Text[] t_) {
		p = p_;
		t = t_;
		
		s = s2;
		wait		= x;
		running		= false;
		available	= false;
	}



	public void start() {
		// set running equal to true
		running = true;
		System.out.println("Starting thread (will execute every " + (wait/1000) + " seconds.)"); 
		// Do whatever start does in Thread, don't forget this!
		super.start();
	}
	
	

//	 We must implement run, this gets triggered by start()
	public void run () {
	  while (running){
	    System.out.println("reloading. . ."); 
	    check(); 
	    // calling this function internally will fill our T object
	    // and hopefully pass its value to other places..
	    
	    try {
	      sleep((long)(wait));
	    } 
	    catch (Exception e) {
	    }
	  }
	}
	
//	 Our method that quits the thread
	public void quit() {
	  System.out.println("Quitting."); 
	  running = false;  // Setting running to false ends the loop in run()
	  super.interrupt();
	}

	public boolean available() {
	  return available;
	}
	
	 public synchronized void setAvailable() {
		  	available = false;
		    notifyAll(); // let's notify everyone that available has changed
		      
	}
	  
	
	private synchronized void check() {
		for(int i =0; i < s.length; i++) {
			t[i].parsePHP(s[i]);
		
			
		}
		
		//t[0].getDifference(2); // only the main island.
		
		
	
	//	p.println(t[0].getOverall(2));
		
	    available = true;
	    notifyAll();  // let's notify everyone that the headlines have been updated
	}
}
