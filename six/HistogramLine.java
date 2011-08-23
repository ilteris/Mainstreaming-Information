package six;
/* HistogramLine.java 
 * Created on Oct 23, 2006 
 * by ilteris
 */
import processing.core.PApplet;

public class HistogramLine {
	PApplet	p;
	Vector3D	loc, vel, acc;
	Vector3D  destLoc;
	
	private float maxvel;
	private float	damper;

	private float damp,mass,k;
	public HistogramLine(PApplet $p, Vector3D loc_) {
		p = $p;
		loc = loc_;
		destLoc = loc.copy();
		vel = new Vector3D(0.0f, 0, 0);
		acc = new Vector3D(0.0f, 0, 0);
		maxvel = 7;
		damper = p.random(0.2f,0.8f);
		
		damp = 0.9f; 
		mass = 15; 
		k = 1;

	}

	

	public void update() {
		vel = Vector3D.sub(destLoc, loc);
		vel.mult(damper);
		vel.mult(0.3f);
		loc.add(vel);
		
		vel.limit(maxvel);
		 
	}

	public void spring() {
		
		    Vector3D force = Vector3D.sub(loc,destLoc);
		    force.mult(-k);
		    force.div(mass);

		    acc.setXY(0,0);
		    acc.add(force);
		    vel.add(acc);
		    vel.mult(damp);
		    loc.add(vel);

		  
	}

	public void setDestLoc(Vector3D destLoc_) {
		
		destLoc = destLoc_;
	}

}
