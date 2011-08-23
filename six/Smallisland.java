/* Smallisland.java 
 * Created on Dec 4, 2006 
 * by ilteris
 */
package six;

import processing.core.PApplet;

public class Smallisland {
	PApplet			p;
	int[]			curArr	; // starting size
	int[]			destArr	; // starting size
	
	HistogramLine[]		lines	; // generic line values we pass from arrays.
	Vector3D[]		destLines; // destination Lines
	
	
	Vector3D		loc		; // location Vector3D
	Vector3D		destLoc ; // destLocation
	
	Text			t;		
	/* t that we are going to pass is according to the
	 timestamp. so might just need to create another text object
	  to pull that data.
	 */   
	Smallcircle	c;		 // circle for the small island.
	Vector3D	locRect;	 // location of the rectangle to position our island.

	static int MAX = 9;
	
	int weight;
	
	int mIAIndex; // get the array index you are looking for. pass this to the circle.
	int latteWidth; // we need this again to position the circle, island.
	int latteHeight; // total height of our Latte
	// we need it in order to calculate the midpoint to position our ellipse
	
	
	Smallisland(PApplet p_, Text t_, Vector3D loc_, int totalHeight_, int width_, int mIAIndex_) {
		mIAIndex	= mIAIndex_;
		t			= t_;
		locRect		= loc_;
		latteHeight = totalHeight_;
		p			= p_;
		latteWidth	= width_;
		
		lines		= new HistogramLine[MAX];
		destLines	= new Vector3D[MAX];
		
		curArr		= new int[MAX];
		destArr		= new int[MAX];
		
		curArr		= t.getOverall(2); // arbitrary for now.
		
		weight 		= 3; // how big it is going to be.
		//p.println(t.getMostIntense(3) );
		buildLines(curArr);
		
		c = new Smallcircle(p, new Vector3D(50, 50), locRect, latteHeight, latteWidth, t.getMostIntense(2) );
		// g = new Graph(p, "Area 1", 3, fonts, new Vector3D(12,p.height/2-50), t);
		// g1 = new Graph(p,"Area 2",  4, fonts,new Vector3D(12,p.height/2+50), t);
		// gtotal = new Graph(p,"total", 2, fonts,new Vector3D(12,p.height/2+40), t);
		// width 	= p.width - 30;
		// height = p.height - 40;
		
	}
	
	public void draw() {
		for (int i = 0; i < 8; i++) {
			lines[i].update();

		}
		
		
		drawIsland();
		c.draw();
	}
	
	public void stretch() {
		setDestPoint(20);
		c.stretch();
	}
	
	private void drawIsland() {
		p.noStroke();
		p.pushMatrix();
		p.translate(locRect.x+(latteWidth*3)/4, locRect.y+latteHeight/2); // translation for center.
		
		p.fill(232,232,232);
		
		p.beginShape();
		for (int i = 7; i >= 0; i--) {
			p.vertex(lines[i].loc.x/weight, lines[i].loc.y/weight);
		}
		p.endShape(PApplet.CLOSE);
	
		p.popMatrix();
	}
		
		
	private void buildLines(int arr[] ) {
		
		lines[7] = new HistogramLine(p,new Vector3D(+ PApplet.sqrt(arr[7] * arr[7] / 2),  -PApplet.sqrt(arr[7] * arr[7] / 2)));
		lines[6] = new HistogramLine(p,new Vector3D(0, -arr[6] ));
		lines[5] = new HistogramLine(p,new Vector3D(- PApplet.sqrt(arr[5] * arr[5] / 2) , -PApplet.sqrt(arr[5] * arr[5] / 2)));
		lines[4] = new HistogramLine(p,new Vector3D(- arr[4], 0));
		lines[3] = new HistogramLine(p,new Vector3D(- PApplet.sqrt(arr[3] * arr[3] / 2),  PApplet.sqrt(arr[3] * arr[3] / 2)));
		lines[2] = new HistogramLine(p,new Vector3D(0 , arr[2]));
		lines[1] = new HistogramLine(p,new Vector3D( PApplet.sqrt(arr[1] * arr[1] / 2),   PApplet.sqrt(arr[1] * arr[1] / 2)));
		lines[0] = new HistogramLine(p,new Vector3D(arr[0], 0));
		
	}
	
	
	private void setLines(int arr[] ) {
		
		destLines[7] = new Vector3D(+ PApplet.sqrt(arr[7] * arr[7] / 2),  -PApplet.sqrt(arr[7] * arr[7] / 2));
		destLines[6] = new Vector3D(0, -arr[6] );
		destLines[5] = new Vector3D(- PApplet.sqrt(arr[5] * arr[5] / 2) , -PApplet.sqrt(arr[5] * arr[5] / 2));
		destLines[4] = new Vector3D(- arr[4], 0);
		destLines[3] = new Vector3D(- PApplet.sqrt(arr[3] * arr[3] / 2),  PApplet.sqrt(arr[3] * arr[3] / 2));
		destLines[2] = new Vector3D(0 , arr[2]);
		destLines[1] = new Vector3D( PApplet.sqrt(arr[1] * arr[1] / 2),   PApplet.sqrt(arr[1] * arr[1] / 2));
		destLines[0] = new Vector3D(arr[0], 0);
		
	}
	
	
	private void setDestPoint(int k) {
		for (int i = 0; i < curArr.length; i++) {
			destArr[i]		= curArr[i] + k;
		}
		
		setLines(destArr);
		
		for (int i = 0; i < curArr.length; i++) {
			lines[i].setDestLoc(destLines[i]);
			
		}
		
		
		
	}

	public void stretchBack() {
		setDestPoint(0);
		c.stretchBack();
		
	}
	

}
