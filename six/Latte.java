/* Previous.java 
 * Created on Dec 4, 2006 
 * by ilteris
 */
package six;

import processing.core.PApplet;
import processing.core.PFont;

public class Latte {
	PApplet	p;
	Vector3D loc;
	PFont fonts[];
	int tHeight;
	int bHeight;
	
	int latteHeight;
	
	int latteWidth;
	int spacer;
	String names[];
	
	
	int miIndex;
	// we are creating 4 lattes and each is assigned to different
	// values which we already pulled from the dtabase. (for 
	//30 mins, for 1 hours..etc. and each of them has different
	// array index. it is good to pass this at the const.
	
	Text t;
	
	Smallisland s;
	Island i;
	String labels[];
	private int	label;
	private boolean	inbounds;
	private boolean	over;
	private boolean	pressed;
	
	Latte(PApplet p_, Vector3D loc_, PFont[] fonts_, Text t_, int miIndex_, int label_, Island i_) {
		i = i_;
		label = label_;
		t = t_;
		p = p_;
		loc = loc_;
		fonts  = fonts_;
		latteWidth  = 145;
		tHeight= 15;
		bHeight= 27;
		spacer = 2;
		latteHeight = tHeight + bHeight + spacer;
		
		miIndex = miIndex_;
		names = new String[] {"CALM", "SAD", "ANGRY", "TIRED", "SICK", "HAPPY",
				"ENERGETIC", "LOVED"};
		
		labels = new String[] {"LAST 30 MINS",
				"LAST 1 HOURS",
				"LAST 6 HOURS",
				"LAST 12 HOURS",
				"LAST 24 HOURS"
				};
				
		over = false;
		s = new Smallisland(p, t, loc, latteHeight, latteWidth,  miIndex);
	}
	
	

	public void draw() {
		drawTitle();
		drawBody();
		
		if(over) {
			s.stretch();
			if(pressed) {
				doSomething();
			}
		} else {
			s.stretchBack();
		}
		s.draw();
	}

	private void doSomething() {
		i.setLines(t.getOverall(miIndex));
		//float tempLines = t[0]
		i.setDest();
		i.t = t;
		i.setGraph(t.getOverall(miIndex));
		//p.println(label);
		pressed = false;
		
	}



	private void drawTitle() {
		p.rectMode(p.CORNER);
		//fillOption();
		if(over){
			p.fill(255,0,0);
		} else {
			p.fill(211,211,211);
		}
		p.rect(loc.x, loc.y, latteWidth,tHeight);
		p.fill(255,255,255);
		
		p.textFont(fonts[2], 12);
		//TODO text should come from an array that we can pass through cons.
		p.text(labels[label], loc.x + spacer, loc.y+tHeight-spacer);		

	}
	
	
	private void drawBody() {
		p.rectMode(p.CORNER);
		fillOption();
		p.rect(loc.x, loc.y + tHeight + spacer, latteWidth,bHeight);
		p.fill(255,255,255);
		p.textFont(fonts[2], 12);
		p.textLeading(12);
		p.text("COUNT: " + t.getTotal() + "\nMOOD: " + names[t.getMostIntense(miIndex)], loc.x + spacer, loc.y+tHeight+bHeight/2+spacer);		
		

	}



	private void fillOption() {
		if(over) {
			p.fill(100,100,100);
		} else {
			p.fill(211,211,211);
		}
		
	}
	
	
	
	
	
	public void mouseCheck() {
		if(inbounds()) {
			over = true;
		} else { 
			over = false;
		}
	}
	
	public void mousePressed() {
		if(inbounds()) {
			pressed = true;
		} else { 
			pressed = false;
		}
	}

	private boolean inbounds() {
		Vector3D mouseCoord = new Vector3D(p.mouseX, p.mouseY);
		Vector3D newLoc 	= new Vector3D(loc.x + latteWidth/2, loc.y + latteHeight/2);
		
		Vector3D diff = Vector3D.sub(mouseCoord, newLoc);
		if(Math.abs(diff.x) < latteWidth/2 && Math.abs(diff.y) < latteHeight/2) {
			return true;
		} else {
			return false;
		}
	}
	
	
	

}
