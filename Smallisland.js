class Smallisland {
    constructor(p5Instance, textObject, locVec, totalHeight, width, mostIntenseAreaIndex) {
        this.p = p5Instance;
        this.t = textObject; // Text object for data

        this.locRect = locVec.copy();     // Location of the parent Latte element (top-left)
        this.latteHeight = totalHeight;   // Height of the Latte
        this.latteWidth = width;          // Width of the Latte (area for drawing Smallisland)
        this.mIAIndex = mostIntenseAreaIndex; // Index of the most intense area (e.g., overall, area1, area2)

        this.lines = new Array(8);      // Array for HistogramLine objects
        this.destLines = new Array(8);  // Array for destination vectors of lines

        // Ensure textObject and its data are valid
        if (!this.t || typeof this.t.getOverall !== 'function' || !this.t.getOverall(2)) {
            console.error("Smallisland: Invalid textObject or missing 'overall' data (index 2). Using default data.");
            this.curArr = Array(8).fill(10); // Default to small non-zero values for visibility
        } else {
            this.curArr = this.t.getOverall(2).slice(); // Initial data from textObject (overall mood)
        }

        // Ensure curArr has 8 elements for line calculations
        if (this.curArr.length !== 8) {
            console.warn(`Smallisland: curArr (overall data index 2) does not have 8 elements. Received ${this.curArr.length}. Padding/truncating.`);
            let tempArr = Array(8).fill(10);
            for (let i = 0; i < Math.min(this.curArr.length, 8); i++) {
                tempArr[i] = this.curArr[i];
            }
            this.curArr = tempArr;
        }

        this.destArr = this.curArr.slice(); // Destination data, initially same as current

        this.weight = 3; // Scaling factor for drawing the island shape (makes it smaller)

        this.buildLines(this.curArr); // Initialize HistogramLine objects based on curArr

        // Initialize Smallcircle associated with this Smallisland
        // The arrayIndex for Smallcircle's line rotation uses getMostIntense(2) from textObject
        let mostIntenseOverallMoodIndex = (this.t && typeof this.t.getMostIntense === 'function') ? this.t.getMostIntense(2) : 0;
        if (mostIntenseOverallMoodIndex === undefined) mostIntenseOverallMoodIndex = 0;

        this.c = new Smallcircle(
            this.p,
            this.p.createVector(30, 30), // Initial size of the small circle
            this.locRect,                // Pass Latte's location
            this.latteHeight,            // Pass Latte's height
            this.latteWidth,             // Pass Latte's width (for positioning circle center)
            mostIntenseOverallMoodIndex  // Index for rotating line in Smallcircle
        );

        // Set initial destination for lines (which is current state, so no animation yet)
        this.setLines(this.destArr);
        for (let i = 0; i < this.lines.length; i++) {
            if(this.lines[i] && this.destLines[i]) {
                this.lines[i].setDestLoc(this.destLines[i]);
            }
        }
    }

    draw() {
        for (let i = 0; i < this.lines.length; i++) {
            if (this.lines[i]) {
                this.lines[i].update(); // Use .update() for direct movement (not spring)
            }
        }
        this.drawIsland();
        if (this.c) {
            this.c.draw();
        }
    }

    drawIsland() {
        this.p.noStroke();
        this.p.push();
        // Translate to the center point where the Smallcircle is also centered
        this.p.translate(this.locRect.x + (this.latteWidth * 3) / 4, this.locRect.y + this.latteHeight / 2);

        this.p.fill(232, 232, 232); // Light gray fill for the island shape

        this.p.beginShape();
        // Vertex order from Java: lines[7] down to lines[0].
        // If this.lines maps 0-7 to Java's 0-7, iterate i=0 to 7.
        // The original loop was `for(int i = 7; i >= 0; i--)`, so lines[7], then [6], ..., then [0].
        // To match this, if JS lines[i] corresponds to Java lines[i]:
        for (let i = 0; i < 8; i++) {
            if (this.lines[i] && this.lines[i].loc) {
                this.p.vertex(this.lines[i].loc.x / this.weight, this.lines[i].loc.y / this.weight);
            } else {
                this.p.vertex(0,0); // Default if line missing
            }
        }
        this.p.endShape(this.p.CLOSE);
        this.p.pop();
    }

    // Helper for sqrt(val*val/2) which is abs(val)/sqrt(2)
    _sqrtHalfSq(val) {
        return (val >= 0 ? val : -val) * Math.sqrt(0.5);
    }

    buildLines(arr) {
        // Ensure arr has 8 elements for safety
        let safeArr = Array(8).fill(0);
        for(let i=0; i < Math.min(arr.length, 8); i++) safeArr[i] = arr[i];

        this.lines[0] = new HistogramLine(this.p, this.p.createVector(safeArr[0], 0));
        this.lines[1] = new HistogramLine(this.p, this.p.createVector(this._sqrtHalfSq(safeArr[1]), this._sqrtHalfSq(safeArr[1])));
        this.lines[2] = new HistogramLine(this.p, this.p.createVector(0, safeArr[2]));
        this.lines[3] = new HistogramLine(this.p, this.p.createVector(-this._sqrtHalfSq(safeArr[3]), this._sqrtHalfSq(safeArr[3])));
        this.lines[4] = new HistogramLine(this.p, this.p.createVector(-safeArr[4], 0));
        this.lines[5] = new HistogramLine(this.p, this.p.createVector(-this._sqrtHalfSq(safeArr[5]), -this._sqrtHalfSq(safeArr[5])));
        this.lines[6] = new HistogramLine(this.p, this.p.createVector(0, -safeArr[6]));
        this.lines[7] = new HistogramLine(this.p, this.p.createVector(this._sqrtHalfSq(safeArr[7]), -this._sqrtHalfSq(safeArr[7])));
    }

    setLines(arr) {
        // Ensure arr has 8 elements.
        let safeArr = Array(8).fill(0);
        for(let i=0; i < Math.min(arr.length, 8); i++) safeArr[i] = arr[i];

        this.destLines[0] = this.p.createVector(safeArr[0], 0);
        this.destLines[1] = this.p.createVector(this._sqrtHalfSq(safeArr[1]), this._sqrtHalfSq(safeArr[1]));
        this.destLines[2] = this.p.createVector(0, safeArr[2]);
        this.destLines[3] = this.p.createVector(-this._sqrtHalfSq(safeArr[3]), this._sqrtHalfSq(safeArr[3]));
        this.destLines[4] = this.p.createVector(-safeArr[4], 0);
        this.destLines[5] = this.p.createVector(-this._sqrtHalfSq(safeArr[5]), -this._sqrtHalfSq(safeArr[5]));
        this.destLines[6] = this.p.createVector(0, -safeArr[6]);
        this.destLines[7] = this.p.createVector(this._sqrtHalfSq(safeArr[7]), -this._sqrtHalfSq(safeArr[7]));
    }

    setDestPoint(k) {
        // k is an offset to be added to each element of curArr to get destArr
        this.destArr = this.curArr.map(val => Math.max(0, val + k)); // Ensure values don't go below 0

        this.setLines(this.destArr); // Recalculate destLine vectors based on new destArr values

        for (let i = 0; i < this.lines.length; i++) {
            if (this.lines[i] && this.destLines[i]) {
                this.lines[i].setDestLoc(this.destLines[i]);
            }
        }
    }

    stretch() {
        this.setDestPoint(20); // Add 20 to all current line magnitudes
        if (this.c) {
            this.c.stretch();
        }
    }

    stretchBack() {
        this.setDestPoint(0); // Reset to original magnitudes (k=0 offset from curArr)
        if (this.c) {
            this.c.stretchBack();
        }
    }

    // Call this if the underlying data in textObject changes
    updateData(newTextObject) {
        this.t = newTextObject;
        this.curArr = this.t.getOverall(2) ? this.t.getOverall(2).slice() : Array(8).fill(10);
        // Ensure curArr has 8 elements
        if (this.curArr.length !== 8) {
            let tempArr = Array(8).fill(10);
            for (let i = 0; i < Math.min(this.curArr.length, 8); i++) tempArr[i] = this.curArr[i];
            this.curArr = tempArr;
        }
        this.destArr = this.curArr.slice(); // Reset destArr too
        this.setLines(this.destArr); // Update destLines
        for (let i = 0; i < this.lines.length; i++) { // Apply new destinations to HistogramLines
             if(this.lines[i] && this.destLines[i]) {
                this.lines[i].setDestLoc(this.destLines[i]);
             }
        }
        // Update Smallcircle's arrayIndex if most intense mood changed
        let mostIntenseOverallMoodIndex = (this.t && typeof this.t.getMostIntense === 'function') ? this.t.getMostIntense(2) : 0;
        if (mostIntenseOverallMoodIndex === undefined) mostIntenseOverallMoodIndex = 0;
        if (this.c) {
            this.c.arrayIndex = mostIntenseOverallMoodIndex;
        }
    }
}
