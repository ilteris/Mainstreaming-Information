class Smallcircle {
    constructor(p5Instance, sizeVec, locVec, totalHeight, width, arrayIndex) {
        this.p = p5Instance;
        this.size = sizeVec.copy();       // Current size, animated
        this.destSize = sizeVec.copy();   // Target size for animation
        this.curSize = sizeVec.copy();    // Original/resting size, for stretchBack

        this.loc = locVec.copy();         // Location of the parent Latte (top-left corner)
        this.latteHeight = totalHeight;   // Total height of the parent Latte
        this.latteWidth = width;          // Width of the parent Latte (where circle is drawn relative to)
        this.arrayIndex = arrayIndex;     // Index (0-7) for rotation in drawLine

        this.vel = this.p.createVector(0, 0);
        this.acc = this.p.createVector(0, 0);

        this.damp = 0.5; // Damping factor for spring
        this.mass = 10;  // Mass for spring physics
        this.k = 1;      // Spring constant
    }

    draw() {
        this.springMove();
        this.drawCircle();
        this.drawLine();
    }

    springMove() {
        let force = p5.Vector.sub(this.size, this.destSize);
        force.mult(-this.k);
        force.div(this.mass);
        this.acc.set(0, 0); // Reset acceleration
        this.acc.add(force);
        this.vel.add(this.acc);
        this.vel.mult(this.damp);
        this.size.add(this.vel);
    }

    drawCircle() {
        // Calculate the center of the circle relative to the parent Latte's location
        // Java version: loc.x + width*3/4, loc.y + height/2
        // this.latteWidth is the "width" param from constructor, used for positioning area.
        // this.latteHeight is the "totalHeight" param.
        let centerX = this.loc.x + (this.latteWidth * 3) / 4;
        let centerY = this.loc.y + this.latteHeight / 2;

        this.p.noFill();
        this.p.stroke(239, 189, 189); // Pinkish stroke
        this.p.strokeWeight(1);
        this.p.ellipseMode(this.p.CENTER);
        this.p.ellipse(centerX, centerY, this.size.x, this.size.y);

        // Small center dot
        this.p.push();
        this.p.translate(centerX, centerY);
        this.p.fill(239, 189, 189); // Fill for the center dot
        this.p.noStroke(); // No stroke for the dot
        this.p.ellipse(0, 0, 4, 4);
        this.p.pop();

        this.p.noStroke(); // Reset stroke
    }

    drawLine() {
        let centerX = this.loc.x + (this.latteWidth * 3) / 4;
        let centerY = this.loc.y + this.latteHeight / 2;

        this.p.noFill();
        this.p.stroke(239, 189, 189);
        this.p.strokeWeight(1);

        this.p.push();
        this.p.translate(centerX, centerY);
        // Rotate based on arrayIndex (0-7 for 8 directions)
        this.p.rotate(this.p.TWO_PI / 8 * this.arrayIndex);
        // Line from center to the edge of the circle (current animated size)
        this.p.line(0, 0, this.size.x / 2, 0);
        this.p.pop();

        this.p.noStroke(); // Reset stroke
    }

    // Sets the destination size for the spring animation (assumes x and y are the same for a circle)
    setDestPoint(val) {
        this.destSize.set(val, val);
    }

    // Example action: make the circle temporarily larger
    stretch() {
        this.setDestPoint(60); // Target diameter of 60
    }

    // Example action: return circle to its original/resting size
    stretchBack() {
        // this.curSize holds the initial size passed to constructor
        this.setDestPoint(this.curSize.x);
    }

    // More generic way to change size to a specific vector (if non-uniform x/y needed)
    changeSize(destSizeVec) {
        if (destSizeVec instanceof p5.Vector) {
            this.destSize = destSizeVec.copy();
        } else {
            console.error("Smallcircle.changeSize expects a p5.Vector");
        }
    }
}
