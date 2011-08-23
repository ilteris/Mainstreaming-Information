/* Circle.java 
 * Created on Dec 1, 2006 
 * by ilteris
 */ 
package six; 

import processing.core.PApplet;
import processing.core.PConstants;

public class Smallcircle {
 PApplet p;
 Vector3D vel;
private int	width;
private int	height;
 Vector3D  size;
 Vector3D destSize;
 Vector3D loc;
private  int latteHeight;
private int latteWidth;
 
private int arrayIndex; // pass this to the drawLine so that it can draw its line that way!
  
private float damp,mass,k;
Vector3D	  acc;

Vector3D 	  curSize;
 Smallcircle(PApplet p_, Vector3D size_, Vector3D loc_, int totalHeight_, int width_, int arrayIndex_) {
	 p 		= p_; 
	 size = size_;
	 destSize = size.copy();
	 loc = loc_;
	 latteHeight = totalHeight_;
	 latteWidth  = width_;
	 curSize = size.copy();
	 
	 vel = new Vector3D(0.0f, 0, 0);
	 acc = new Vector3D(0.0f, 0, 0);
	 
	 
	 arrayIndex = arrayIndex_;
	 damp = 0.5f; 
	 mass = 10; 
	 k = 1;
 }
 
 public void draw() { 
	 springMove();
	// moveCircle();
	drawCircle();
	drawLine();
	//drawHairline();

 }
 
 public void springMove() {
		
	    Vector3D force = Vector3D.sub(size,destSize);
	    force.mult(-k);
	    force.div(mass);

	    acc.setXY(0,0);
	    acc.add(force);
	    vel.add(acc);
	    vel.mult(damp);
	    size.add(vel);

	  
}
 
 
 public void stretch() {
	 setDestPoint(60);
	 
 }
 private void setDestPoint(float i ) {
	 destSize.x = i;
	 destSize.y = i;
}
 
 public void stretchBack() {
	 setDestPoint(curSize.x);
	 
 }

public void moveCircle() {
	 vel = Vector3D.sub(destSize, size);
	 vel.mult(0.6f);
	 vel.mult(0.2f);
	 size.add(vel);
 }
 
 
 public void drawCircle() {
	
	
	 p.noFill();
	 p.stroke(239,189,189);
	 p.ellipseMode(p.CENTER);
	 
	 p.ellipse(loc.x+(latteWidth*3)/4,loc.y+latteHeight/2,size.x,size.y);
	 p.pushMatrix();
	 p.translate(loc.x+(latteWidth*3)/4,loc.y+latteHeight/2);
	 p.ellipse(0,0,4,4);
	 p.popMatrix();
	 
	 p.noStroke();
	 
	 
 }
	
 

 public void drawLine() {
	 p.noFill();
	 p.stroke(239,189,189);
	 p.pushMatrix();
	 p.translate(loc.x+(latteWidth*3)/4,loc.y+latteHeight/2);
	 // get which one is the intense.
	 p.rotate(PConstants.TWO_PI/8*arrayIndex);
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
 