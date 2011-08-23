/* Circle.java 
 * Created on Dec 1, 2006 
 * by ilteris
 */ 
package six; 

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class Circlem {
 PApplet p;
private int	width;
private int	height;
 Vector3D  size;
 Vector3D destSize;
 
 PFont [] fonts;
 String[] names;
 int[][] colors;
 private int arrayIndex; // pass this to the drawLine so that it can draw its line that way!
 

 private float damp,mass,k;
      
 Vector3D	 vel, acc;
 Text t; 
 Circlem(PApplet p_, Vector3D size_, PFont[] fonts_, int arrayIndex_, Text t_) {
	 t = t_;
	 p 		= p_; 
	 fonts = fonts_;
	 destSize = size_;
	 size = new Vector3D(0,0);
	 width 	= p.width - 30;
	 height = p.height - 40;
	 
	 vel = new Vector3D(0.0f, 0, 0);
	 acc = new Vector3D(0.0f, 0, 0);
		damp = 0.9f; 
		mass = 10; 
		k = 1f;
		
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
	 
 }
 
 public void draw() {
	spring();
	drawCircle();
	//drawHairline();
	drawTitles();
	// drawLine();
 }
 
 
 public void spring() {
		
	    Vector3D force = Vector3D.sub(size,destSize);
	    force.mult(-k);
	    force.div(mass);

	    acc.setXY(0,0);
	    acc.add(force);
	    vel.add(acc);
	    vel.mult(damp);
	    size.add(vel);

	  
}
 
 public void drawCircle() {
	 
	//p.noFill();
	 p.fill(255,255,255);
	 
	 p.stroke(colors[t.getMostIntense(2)][0],colors[t.getMostIntense(2)][1],colors[t.getMostIntense(2)][2],50);
		
	 p.ellipseMode(p.CENTER);
	 p.ellipse(width/2,height/2,size.x,size.y);
	 p.noStroke();
	 
 }
	
 
 
 
 public void drawTitles() {
	 	p.fill(220,220,220);
	 	//p.fill(200,200,200);
		p.textFont(fonts[4], 15);
		for (int i = 0 ; i<names.length; i++) {
			p.pushMatrix();
		    p.translate(width/2, height/2);
		    p.rotate(PConstants.TWO_PI/8*i);
		    
		   
		    p.pushMatrix();
		    String s = names[i];
		    float sw = p.textWidth(s);
	/* since this is drawing according to the size of 
	 * the ellipse, I should put a if/else to see if goes
	 * below certain threshold don't move the texts
	 * orr they will get so close and ugly.
	 */
		    
		    p.translate(PApplet.round(size.x/2-20),-sw/2);
		    p.rotate(PConstants.PI/2);
		    p.text(names[i], 0 , i);
		    p.popMatrix();
			p.popMatrix();
		}
 }

 public void drawLine() {
	 p.noFill();
	 p.stroke(239,189,189);
	 p.pushMatrix();
	 p.translate(width/2,height/2);
	 // get which one is the intense.
	 p.rotate(PConstants.TWO_PI/8*arrayIndex); // pass this to the drawLine so that it can draw its line that way!
	  

	 // arrayIndex
	 p.line(0,0,size.x/2,0);
	 
	 
	 p.popMatrix();
	 
	 p.noStroke();
 }
 
 public void drawHairline() {
	 p.stroke(239,189,189,80);
	 p.line(0, height/2, width, height/2);
	 p.noStroke();
	 
 }
 
 public void changeSize(Vector3D destSize_) {
	 destSize = destSize_;
	 
	 
 }


 
 
 
}
 