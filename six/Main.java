
package six;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.opengl.*;


/* Main.java 
 * Created on Oct 23, 2006 
 * by ilteris
 */

//TODO try to implement spring movement as an alternative
//TODO put an introduction animation when showing island and graphs 
//TODO also transition when someone presses AREA1. 


//TODO could be more text about the local ones. to differentiate.

public class Main extends PApplet {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	Island i;
	
	
	Moint mt[];
	
	
	PFont[] fonts ;

	Latte  l[];	
	PImage b,b1;
	
	public int j = 0;

	Text t[];
	ThreadPHP php;
	String s[];
	
	String names[];
	private int	miIndex;
	int colors[][];
	
	public void setup() {
		 names = new String[] {"CALM", "SAD", "ANGRY", "TIRED", "SICK", "HAPPY",
					"ENERGETIC", "LOVED"};
		 
		 
		 colors = new int[][] { 
					{20,254,254},	 // calm
					{28,0,54},		 // sad
					{232,0,0},		 // angry
					{2,2,56},		 // tired
					{194,254,0},	 // sick
					{254,254,32},	 // happy
					{254,92,0},		 // energetic
					{254,8,163}		 // loved
					
				};
		 
		 
		 
		 
		s = new String[] {
				"http://itp.nyu.edu/~ik501/data/read.php",
				"http://itp.nyu.edu/~ik501/data/read.php",
				"http://itp.nyu.edu/~ik501/data/read.php",
				"http://itp.nyu.edu/~ik501/data/read.php",
				"http://itp.nyu.edu/~ik501/data/read.php",
				"http://itp.nyu.edu/~ik501/data/read.php"
				
				//"http://itp.nyu.edu/~ik501/data/loremmain.txt",
				//"http://itp.nyu.edu/~ik501/data/lorem30mins.txt",
				//"http://itp.nyu.edu/~ik501/data/lorem60mins.txt",
				//"http://itp.nyu.edu/~ik501/data/lorem6hours.txt",
				//"http://itp.nyu.edu/~ik501/data/lorem12hours.txt",
				//"http://itp.nyu.edu/~ik501/data/lorem24hours.txt"
		};
		
			t = new Text[s.length];
			             
		for(int i =0; i < s.length; i++) {
			
			t[i] = new Text(this);
			t[i].parsePHP(s[i]);
				
		}
		
		 php = new ThreadPHP(this, 100000, s, t);  // new thread every N milliseconds
		 php.start();  // start that thread
	
		
		size(800,600, OPENGL);
		frameRate(30);
		

		
		
		
		fonts = new PFont[] {
				loadFont("AkzidenzGroteskBE-Md-12.vlw"),
				loadFont("AkzidenzGroteskBE-Md-21.vlw"), 
				loadFont("AkzidenzGroteskBE-BoldCn-12.vlw"),
				loadFont("AkzidenzGroteskBE-BoldCn-15.vlw"),
				loadFont("AkzidenzGroteskBE-Md-25.vlw"),
				loadFont("AkzidenzGroteskBE-Md-40.vlw"),
				loadFont("AkzidenzGroteskBE-BoldCn-10.vlw"),
				loadFont("AkzidenzGroteskBE-BoldCn-30.vlw")
				
		};
		
		
		//miIndex = t[0].getAreaOneTotal();
		miIndex =2; // returning array[2] from the php.
		int latteSize = 162;
		// phpfilerunsmultiple functions whichreturns 
		// more arrays.
		//so you basically need to run those functions for different tobjects.
		// I'd just use 4different phpfiles for4different timestamps.
		// then run a for loop and build four different latte. andpass its label
		// with the constructor miIndex
		b = loadImage("titileps.png");
		i = new Island(this, fonts, t[0], b);
		
		l = new Latte[5];
	//	mt = new Moint[5];
	for(int ix =0; ix < l.length; ix++) {
		
		l[ix] = new Latte(this, new Vector3D(latteSize*ix , 540), fonts, t[ix+1], miIndex,ix,i );
		
		
		}
	}

	
     
	public void draw() {
		background(244,244,244); // gray background
		
		if(php.available()) {
			
			php.setAvailable();
		//	i.setLines(t[0].getOverall(2));
			if(t[0].getTotal() > 400) {
			scaleValues();
			}
			
		
		}
		
		
		
		
		
		i.draw();
		//
		i.drawTitles();
		
		for (int i = 0; i < l.length; i++) {
			l[i].draw();
		//	mt[i].draw();
		}
		
		
		fill(152,191,226);
		textFont(fonts[3], 14); 
		// get this from database?
		
		
		
		
		
	}
	
	private void scaleValues() {
		float f = t[0].getIntenseItself(2);
		float f1 = f/200;
		//println(f1);
		int[] tempArr = new int[t[0].getOverall(2).length];
		for (int i = 0; i< t[0].getOverall(2).length; i++) {
			float m = t[0].getOverall(2)[i];
			float m1 = m/f1;
			//println(Math.round(m1));
			tempArr[i] = Math.round(m1);
			
		}
		//i.setLines(tempArr);
		//float tempLines = t[0]
		//i.setDest();
		
		
	}



	public void mouseMoved() {
		for (int i = 0; i < l.length; i++) {
			l[i].mouseCheck();			
		}
	}
	
	
	public void mouseReleased() {
		for (int i = 0; i < l.length; i++) {
			l[i].mousePressed();			
		}
		
		// you should get the array of 8 here so that 
		// when someone press that you can 
		// pass it as an parameter.
	}
	
	 
	 
	 
	 public void stop() {
		 php.quit();
		 super.stop(); 
		}

	 
}
 