class Circlem {
    constructor(p5Instance, size_, fonts_, arrayIndex_, textObject) {
        this.p = p5Instance;
        this.t = textObject;
        this.fonts = fonts_; // Actual font objects will be loaded later

        // Assuming size_ is a p5.Vector, if not, it needs to be created as one
        this.destSize = size_.copy();
        this.size = this.p.createVector(0, 0);

        this.width = this.p.width - 30;
        this.height = this.p.height - 40;

        this.vel = this.p.createVector(0, 0);
        this.acc = this.p.createVector(0, 0);

        this.damp = 0.9;
        this.mass = 10;
        this.k = 1;

        this.arrayIndex = arrayIndex_;

        this.names = [
            "ANXIETY", "RELAXATION", "CONCENTRATION",
            "ENERGY", "EXCITEMENT", "CONFIDENCE",
            "PRODUCTIVITY", "SENSITIVITY"
        ];

        this.colors = [
            [255, 0, 0],    // ANXIETY (Red)
            [0, 255, 0],    // RELAXATION (Green)
            [0, 0, 255],    // CONCENTRATION (Blue)
            [255, 255, 0],  // ENERGY (Yellow)
            [255, 0, 255],  // EXCITEMENT (Magenta)
            [0, 255, 255],  // CONFIDENCE (Cyan)
            [255, 128, 0],  // PRODUCTIVITY (Orange)
            [128, 0, 255]   // SENSITIVITY (Purple)
        ];
    }

    draw() {
        this.spring();
        this.drawCircle();
        this.drawTitles();
    }

    spring() {
        let force = p5.Vector.sub(this.size, this.destSize);
        force.mult(-this.k);
        force.div(this.mass);
        this.acc.set(0, 0);
        this.acc.add(force);
        this.vel.add(this.acc);
        this.vel.mult(this.damp);
        this.size.add(this.vel);
    }

    drawCircle() {
        this.p.fill(255, 255, 255);
        // Ensure textObject and its methods are valid before calling
        if (this.t && typeof this.t.getMostIntense === 'function' && this.t.getOverall(2) /* check if data exists */) {
            let mostIntenseIdx = this.t.getMostIntense(2); // Assuming overall moods are at index 2
            if (mostIntenseIdx !== undefined && mostIntenseIdx < this.colors.length) {
                let mostIntenseColor = this.colors[mostIntenseIdx];
                this.p.stroke(mostIntenseColor[0], mostIntenseColor[1], mostIntenseColor[2], 150); // Added alpha
            } else {
                this.p.stroke(0, 50); // Default stroke if data is not available
            }
        } else {
            this.p.stroke(0, 50); // Default stroke if textObject is not proper
        }
        this.p.strokeWeight(10); // Added for visibility
        this.p.ellipseMode(this.p.CENTER);
        this.p.ellipse(this.width / 2, this.height / 2, this.size.x, this.size.y);
        this.p.noStroke();
    }

    drawTitles() {
        this.p.fill(100); // Darker fill for better contrast
        // this.p.textFont(this.fonts[4]); // Actual font loading needed
        this.p.textFont('Arial'); // Using a default available font
        this.p.textSize(12); // Adjusted size

        for (let i = 0; i < this.names.length; i++) {
            this.p.push();
            this.p.translate(this.width / 2, this.height / 2);
            this.p.rotate(this.p.TWO_PI / this.names.length * i); // Use names.length for dynamic calculation

            this.p.push();
            let s = this.names[i];
            let sw = this.p.textWidth(s);
            // Adjust translation to place text outside the circle, along its circumference
            let radiusOffset = Math.max(this.size.x, this.size.y) / 2 + 15; // Place text 15px outside the larger radius
            this.p.translate(radiusOffset, 0); // Translate along the rotated x-axis
            this.p.rotate(this.p.PI / 2); // Rotate text to be perpendicular to the radius line
            this.p.textAlign(this.p.CENTER, this.p.CENTER); // Align text centrally
            this.p.text(s, 0, 0); // Draw text at the new origin
            this.p.pop();

            this.p.pop();
        }
    }

    changeSize(destSize_) {
        if (destSize_ instanceof p5.Vector) {
            this.destSize = destSize_.copy();
        } else {
            console.error("Circlem.changeSize expects a p5.Vector");
        }
    }
}
