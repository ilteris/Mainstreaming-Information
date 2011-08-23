package six;

import six.HistogramLine;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;

/* Island.java 
 * Created on Oct 25, 2006 
 * by ilteris
 */
public class Island {
	PApplet					p;
	int[]	 		curArr	; // starting size
	int[]			destArr	; // starting size
	
	
	static int				MAX		= 9;

	HistogramLine[]		lines	; // generic line values we pass from arrays.
	Vector3D[]		destLines; // destination Lines
	
	
	

	Circlem					c;
	Graph g, g1;
	
	Text t;

	int						width;
	int						height;
	PFont[] 	fonts;
	int colors[][];
	private Graph	gtotal;
	PImage b;
	String names[];
	Island(PApplet p_,  PFont[] fonts_, Text t_, PImage b_) {
		t = t_;
		b = b_;
		p = p_;
		fonts = fonts_;
		
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
		
		
		
		
		
		
		c = new Circlem(p, new Vector3D(t.getTotal(),t.getTotal()), fonts, t.getMostIntense(2),t);
		
		g = new Graph(p, "area one", 3, fonts, new Vector3D(p.width/2+220,p.height/2-140), t);
		g1 = new Graph(p,"area two",  4, fonts,new Vector3D(p.width/2+220,p.height/2+40), t);
		
		gtotal = new Graph(p,"total", 2, fonts,new Vector3D(12,p.height/2-140), t);
		
		lines		= new HistogramLine[MAX];
		destLines	= new Vector3D[MAX];
		
		destArr		= new int[MAX];
		curArr		= new int[] {0,0,0,0,0,0,0,0};
		
		destArr 	= t.getOverall(2);
		
		
	
		buildLines(curArr);
		setLines(destArr);
		setDest();
		
		 width 	= p.width - 30;
		 height = p.height - 40;
		

		
		
		// size and loc should be coming
		// through the calculations 
		// about the size of the total
		// voted. 
		
		// for example it could be 
		// 800 people total which goes from here 
		// to the circle object.
		// so I need to pass those values while I am
		// creating theses meaning
		// they are going to be in their separate
		// classes and I am going to call them first
		// and pass through an island contructor
		// and then go from there.
		
		

	}

	public void draw() {

		for (int i = 0; i < 8; i++) {
			lines[i].spring();

		}
		c.draw();
		g.draw();
		g1.draw();
		gtotal.draw();
		
		drawIsland();
	}

	
	
	
	void drawIsland() {
		p.noStroke();
		
		int in = t.getMostIntense(2); // returns the highest emotion in the array
		
		//p.println(in);
		setFill(in); // fills it according to its RGB scheme in the array.
		//p.fill(216, 245, 37);
		
		p.pushMatrix();
		p.translate(width/2, height/2); // translation for center.
		
		
		
		p.beginShape();

		for (int i = 7; i >= 0; i--) {
			p.vertex(lines[i].loc.x, lines[i].loc.y);
		}

		p.endShape(PApplet.CLOSE);
		
		p.ellipseMode(PApplet.CENTER);
		p.fill(255);
		p.ellipse(0, 0, 10, 10);

		p.popMatrix();

	}

public  void buildLines(int arr[] ) {
	
		
		lines[7] = new HistogramLine(p,new Vector3D(+ PApplet.sqrt(arr[7] * arr[7] / 2),  -PApplet.sqrt(arr[7] * arr[7] / 2)));
		lines[6] = new HistogramLine(p,new Vector3D(0, -arr[6] ));
		lines[5] = new HistogramLine(p,new Vector3D(- PApplet.sqrt(arr[5] * arr[5] / 2) , -PApplet.sqrt(arr[5] * arr[5] / 2)));
		lines[4] = new HistogramLine(p,new Vector3D(- arr[4], 0));
		lines[3] = new HistogramLine(p,new Vector3D(- PApplet.sqrt(arr[3] * arr[3] / 2),  PApplet.sqrt(arr[3] * arr[3] / 2)));
		lines[2] = new HistogramLine(p,new Vector3D(0 , arr[2]));
		lines[1] = new HistogramLine(p,new Vector3D( PApplet.sqrt(arr[1] * arr[1] / 2),   PApplet.sqrt(arr[1] * arr[1] / 2)));
		lines[0] = new HistogramLine(p,new Vector3D(arr[0], 0));
		
	}

public void setLines(int arr[] ) {
		
	
	destLines[7] = new Vector3D(+ PApplet.sqrt(arr[7] * arr[7] / 2),  -PApplet.sqrt(arr[7] * arr[7] / 2));
	destLines[6] = new Vector3D(0, -arr[6] );
	destLines[5] = new Vector3D(- PApplet.sqrt(arr[5] * arr[5] / 2) , -PApplet.sqrt(arr[5] * arr[5] / 2));
	destLines[4] = new Vector3D(- arr[4], 0);
	destLines[3] = new Vector3D(- PApplet.sqrt(arr[3] * arr[3] / 2),  PApplet.sqrt(arr[3] * arr[3] / 2));
	destLines[2] = new Vector3D(0 , arr[2]);
	destLines[1] = new Vector3D( PApplet.sqrt(arr[1] * arr[1] / 2),   PApplet.sqrt(arr[1] * arr[1] / 2));
	destLines[0] = new Vector3D(arr[0], 0);
	
}



public void setDest() {
	for (int i = 0; i < curArr.length; i++) {
		lines[i].setDestLoc(destLines[i]);
	}
	
	
	float m, m1;
	float f = t.getTotal();
	if(f > 400) {
		float f1 = f/400;
	 	m = t.getTotal();
	 	m1 = m/f1;
	} else {
		m1 = t.getTotal();
	}
	// p.println(Math.round(m1));
	c.destSize.setXY(Math.round(m1), Math.round(m1));
}

public void setFill(int i) {
	//p.noFill();		
	p.fill(colors[i][0], colors[i][1], colors[i][2]);
		}

public void drawTitles() {
	
//		rectMode(CORNER);
	 	p.fill(colors[t.getMostIntense(2)][0],colors[t.getMostIntense(2)][1],colors[t.getMostIntense(2)][2]);
	 	p.noStroke();
	 	p.rect(0, 0, 800, 50);
		//rect(0, 590, 800, 590);
	 	p.image(b, 12, 21);
		
	 	p.fill(220,220,220);
	 	p.textFont(fonts[7], 30); 
	 	p.text("current mood: ", 10, height-100);
	 	p.fill(colors[t.getMostIntense(2)][0],colors[t.getMostIntense(2)][1],colors[t.getMostIntense(2)][2]);
	 	p.text(names[t.getMostIntense(2)], 160, height-100);
	
		
	 	p.fill(colors[t.getMostIntense(2)][0],colors[t.getMostIntense(2)][1],colors[t.getMostIntense(2)][2]);
	 	p.textFont(fonts[5], 20); 
	 	p.textAlign(PConstants.LEFT);
	 	String s = Integer.toString(t.getTotal());
	 	p.text(s, 12, 70);
	 	float sw = p.textWidth(s);
	 	p.textAlign(PConstants.LEFT);
	 	p.fill(180,180,180);
	 	p.text("EMOTIONS  RELEASED  SO  FAR.", sw + 20, 70);
		
	 	p.textFont(fonts[6], 10); 
	 	p.text("NEGATIVE  EMOTIONS : 43", 50, 80);
	 	p.text("POSITIVE  EMOTIONS : 43", 150, 80);
 
	
}

public void setGraph(int[] overall) {
	
		g.changeSize(overall);
		g1.changeSize(overall);
		gtotal.changeSize(overall);
		
	}
	
}
	

 

