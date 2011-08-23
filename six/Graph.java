/* Graph.java 
 * Created on Dec 2, 2006 
 * by ilteris
 */
package six;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class Graph {

	
	PApplet		p;
	String		title;
	Text t;
	int id;
	PFont[]		fonts;
	String[]	names;
	int[] len;
	Vector3D[] cur;
	Vector3D[] dest;
	
	int[][] colors;
	Vector3D loc;
	Vector3D[] vel;
	Vector3D acc;
	Vector3D destLoc;
	private float	damper;
	private float	maxvel;
	private int[]	overall;
	Graph(PApplet p_, String title_, int id_, PFont[] fonts_, Vector3D loc_, Text t_) {
		p = p_;
		t = t_;
		id = id_;
		title = title_;
		fonts = fonts_;
		
		acc = new Vector3D(0,0);
		vel = 	new Vector3D[] {
				new Vector3D(0,0),
				new Vector3D(0,0),
				new Vector3D(0,0),
				new Vector3D(0,0),
				new Vector3D(0,0),
				new Vector3D(0,0),
				new Vector3D(0,0),
				new Vector3D(0,0)
				};
		
		maxvel = 7;
		damper = p.random(0.2f,0.8f);
		
		loc = loc_;
		destLoc = loc_.copy();
		
		
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
			{254,8,8}		 // loved
			
		};
		overall = new int[t.getOverall(id).length];
		cur  = new Vector3D[t.getOverall(id).length];
		for (int i = 0; i < t.getOverall(id).length; i++) {
			cur[i] = new Vector3D(0,loc.y + i * 9);
			overall[i] = t.getOverall(id)[i];
		}
		
			
		buildGraph();
	//	changeSize(overall);
		
	}

	private void buildGraph() {
		dest = new Vector3D[t.getOverall(id).length];
		
		for(int i = 0; i < t.getOverall(id).length; i++){
			dest[i] =new Vector3D(t.getOverall(id)[i], loc.y + i * 9) ;
		}
	}


	public void draw() {
		drawMainTitle();
		drawTitles();
		drawGraphsBG();
		update();
		
		drawGraphs();
		
		drawNumbers();
	}
	
	public void drawMainTitle() {
		p.fill(200, 200, 200);
		p.textFont(fonts[4], 14);
		p.text(title, loc.x + 50, loc.y-5);
	}
	
	
	
	public void setFill(int i) {
		p.fill(colors[i][0], colors[i][1], colors[i][2]);
	}
	
	

	public void drawTitles() {
		
		p.fill(200, 200, 200);
		p.textFont(fonts[6], 10);

		for (int i = 0; i < names.length; i++) {
		// float m = font1.descent();
		///p.println(m);
			int j = i+1;
			p.text(names[i], loc.x, loc.y + j*9 );
		}

	}

	public void passLevels(int[] x) {
		len = x;
	}
	
	public void update() {
		for (int i = 0; i < t.getOverall(id).length; i++) {
		vel[i] = Vector3D.sub(dest[i], cur[i]);
		vel[i].mult(damper);
		vel[i].mult(0.3f);
		cur[i].add(vel[i]);
		
	//	vel.limit(maxvel);
		}
		
	}
	
	 	public int getIntenseItself(int[] cur_) {
		int nGreatest = 0;
		int indexer	  = 0;
		for (int k=0; k < cur_.length; ++k) {
			int cur_1 = cur_[k];
			if(cur_1>nGreatest) {
				nGreatest = cur_1;
				
			}
			
		}
		return nGreatest;
	}
	 	
	
	public void drawGraphs() {
		p.fill(220, 220, 220);
		p.rectMode(PConstants.CORNER);
		
		float m = getIntenseItself(overall); // get the most
	//	p.println(m);
		float m1 = m/100; // divide by 100 to get the step
		//p.println(m1);
		for (int i = 0; i < names.length; i++) {
			float f =cur[i].x;
			
			float result = f/m1; // get each value scaled by 100.
			p.rect(50+loc.x, cur[i].y, result, 8); // use it.
			//p.println(Math.round(result));
		}
	}


	public void drawGraphsBG() {
		p.noFill();
		p.stroke(200,200,200);
		//p.fill(150, 150, 150);
		p.rectMode(PConstants.CORNER);

		for (int i = 0; i < names.length; i++) {
			p.rect(50+loc.x, loc.y + i * 9, 100, 8);
		}
		p.noStroke();
	}
	
	public void changeSize(int[] overall_) {
		overall = overall_;
		for(int i = 0; i < overall_.length; i++){
			dest[i] =new Vector3D(overall_[i], loc.y + i * 9) ;
			
			
			
		}
		 
		 
	 }
	

	public void drawNumbers() {
	p.fill(200, 200, 200);
	p.textFont(fonts[2], 12);

	for (int i = 0; i < names.length; i++) {
	// float m = font1.descent();
	///p.println(m);
		int j = i+1;
		p.text( overall[i], loc.x + 150 + 5 , loc.y +  j*9 );
	}

}

}