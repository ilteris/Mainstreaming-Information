/* Moint.java 
 * Created on Dec 15, 2006 
 * by ilteris
 */ 
package six; 

import processing.core.PApplet;

public class Moint {
	PApplet	p;
	Vector3D	loc, vel, acc;
	Vector3D  destLoc;
	
	private float maxvel;
	private float	damper;

	private float damp,mass,k;
	public Moint(PApplet p_, Vector3D loc_, float scale) {
		p = p_;
		loc = loc_;
		destLoc = loc.copy();
		acc = new Vector3D(0.0f, 0, 0);
		maxvel = 7;
		damper = scale;
		
		damp = 0.9f; 
		mass = 15; 
		k = 1;

	
	}
	
	public void draw() {
		update();
	}
	
	public void update() {
		vel = Vector3D.sub(destLoc, loc);
		vel.mult(damper);
		vel.mult(0.3f);
		loc.add(vel);
		
		vel.limit(maxvel);
	}
	
public void setDestLoc(Vector3D destLoc_) {
		
		destLoc = destLoc_;
	}
	
}
 