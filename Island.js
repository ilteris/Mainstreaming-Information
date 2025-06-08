class Island {
    constructor(p5Instance, fonts, textObject, titleImage) {
        this.p = p5Instance;
        this.fonts = fonts;
        this.t = textObject;
        this.b = titleImage; // background/title image

        this.names = [
            "ANXIETY", "RELAXATION", "CONCENTRATION",
            "ENERGY", "EXCITEMENT", "CONFIDENCE",
            "PRODUCTIVITY", "SENSITIVITY"
        ];
        this.colors = [ // Ensure these match Circlem's colors if they are to be consistent
            [255, 0, 0], [0, 255, 0], [0, 0, 255], [255, 255, 0],
            [255, 0, 255], [0, 255, 255], [255, 128, 0], [128, 0, 255]
        ];

        this.width = this.p.width - 30; // Usable width
        this.height = this.p.height - 40; // Usable height

        // Initialize child objects, ensuring textObject is valid
        if (!this.t || typeof this.t.getTotal !== 'function') {
            console.error("Island: Invalid textObject provided. Using dummy data.");
            // Create a dummy textObject to prevent crashes during setup
            this.t = new Text(this.p);
            this.t.parseLines([ "0", "0 0", "0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0", "0 0 0 0 0 0 0 0"]);
        }

        let initialCircleSize = this.t.getTotal() ? this.t.getTotal() : 0;
        let mostIntenseOverall = this.t.getMostIntense(2) !== undefined ? this.t.getMostIntense(2) : 0;

        this.c = new Circlem(this.p, this.p.createVector(initialCircleSize, initialCircleSize), this.fonts, mostIntenseOverall, this.t);

        // Graph locations might need adjustment based on canvas size vs original 800x600
        this.g = new Graph(this.p, "Area One", 3, this.fonts, this.p.createVector(this.p.width - 280, this.p.height / 2 - 150), this.t); // id 3 for area one
        this.g1 = new Graph(this.p, "Area Two", 4, this.fonts, this.p.createVector(this.p.width - 280, this.p.height / 2 + 30), this.t); // id 4 for area two
        this.gtotal = new Graph(this.p, "Overall", 2, this.fonts, this.p.createVector(12, this.p.height / 2 - 150), this.t); // id 2 for overall

        this.lines = new Array(8);
        this.destLines = new Array(8);

        this.curArr = [0, 0, 0, 0, 0, 0, 0, 0]; // Initial state for lines (center)
        this.destArr = this.t.getOverall(2) ? this.t.getOverall(2).slice() : this.curArr.slice(); // Target state from text data

        this.buildLines(this.curArr); // Build lines at initial (0,0...0) state
        this.setLines(this.destArr);  // Define destination vectors for lines based on data
        this.setDest();               // Trigger initial animation towards destination
    }

    draw() {
        for (let i = 0; i < this.lines.length; i++) {
            if (this.lines[i]) this.lines[i].spring();
        }
        if (this.c) this.c.draw();
        if (this.g) this.g.draw();
        if (this.g1) this.g1.draw();
        if (this.gtotal) this.gtotal.draw();

        this.drawIsland();
        this.drawTitles(); // This is Island's own drawTitles, not Circlem's
    }

    drawIsland() {
        this.p.noStroke();
        let mostIntenseIdx = this.t.getMostIntense(2) !== undefined ? this.t.getMostIntense(2) : 0;
        this.setFill(mostIntenseIdx);

        this.p.push();
        this.p.translate(this.width / 2, this.height / 2); // Translate to center of usable area
        this.p.beginShape();
        // The vertex order in Java was lines[7] down to lines[0].
        // If this.lines maps 0-7 to Java's 0-7, then we iterate i=0 to 7 for vertex order.
        // Original Processing code: vertex(lines[7].loc.x, lines[7].loc.y); ... vertex(lines[0].loc.x, lines[0].loc.y);
        // This means we should use lines[0]...lines[7] in order for p5.js if mapping is direct.
        // The loop in Java was `for(int i = 7; i >= 0; i--)`, which means lines[7], lines[6], ..., lines[0].
        // To match this, if our JS lines[i] corresponds to Java lines[i]:
        for (let i = 0; i < 8; i++) { // Or this.lines.length
             if (this.lines[i] && this.lines[i].loc) {
                this.p.vertex(this.lines[i].loc.x, this.lines[i].loc.y);
             } else {
                 // Draw a default point if line is missing, to avoid breaking shape
                 this.p.vertex(0,0);
             }
        }
        this.p.endShape(this.p.CLOSE);

        this.p.ellipseMode(this.p.CENTER);
        this.p.fill(255); // White center dot
        this.p.ellipse(0, 0, 10, 10);
        this.p.pop();
    }

    // Helper for sqrt(val*val/2) which is abs(val)/sqrt(2)
    _sqrtHalfSq(val) {
        return (val >= 0 ? val : -val) * Math.sqrt(0.5);
    }

    buildLines(arr) {
        // arr should have 8 values. If not, this will error or behave unexpectedly.
        // Ensure arr has a length of 8, padding with 0 if necessary.
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
        // Ensure arr has a length of 8.
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

        // After setting new destLines, also update destArr
        this.destArr = safeArr.slice();
    }

    setDest() {
        for (let i = 0; i < this.lines.length; i++) {
            if (this.lines[i] && this.destLines[i]) {
                this.lines[i].setDestLoc(this.destLines[i]);
            }
        }

        let total = this.t.getTotal();
        let scaledTotal = total;
        if (total > 400) {
            // This logic means if total > 400, scaledTotal becomes 400.
            // Example: total = 500. f1 = 500/400 = 1.25. scaledTotal = 500 / 1.25 = 400.
            // Example: total = 800. f1 = 800/400 = 2. scaledTotal = 800 / 2 = 400.
            scaledTotal = 400;
        } else if (total === undefined || isNaN(total)) {
            scaledTotal = 0;
        }

        if (this.c && this.c.destSize) {
             this.c.changeSize(this.p.createVector(Math.round(scaledTotal), Math.round(scaledTotal)));
        }

        // Update graph data as well
        if (this.t.getOverall(3)) this.g.changeSize(this.t.getOverall(3));
        if (this.t.getOverall(4)) this.g1.changeSize(this.t.getOverall(4));
        if (this.t.getOverall(2)) this.gtotal.changeSize(this.t.getOverall(2));
    }

    setFill(index) {
        if (index !== undefined && index >= 0 && index < this.colors.length) {
            this.p.fill(this.colors[index][0], this.colors[index][1], this.colors[index][2]);
        } else {
            this.p.fill(128); // Default gray if index is invalid
        }
    }

    drawTitles() {
        // Top Bar
        let mostIntenseColorOverall = this.colors[this.t.getMostIntense(2) !== undefined ? this.t.getMostIntense(2) : 0];
        this.p.fill(mostIntenseColorOverall[0], mostIntenseColorOverall[1], mostIntenseColorOverall[2]);
        this.p.noStroke();
        this.p.rect(0, 0, this.p.width, 50); // Use p.width for full responsive bar

        // Title Image
        if (this.b) {
            this.p.image(this.b, 12, 12, this.b.width/2, this.b.height/2); // Scale image if too large
        }

        // Text on top bar
        this.p.textFont(this.fonts[0] || 'Arial'); // Fallback font
        this.p.fill(0, 0, 0); // Black text
        this.p.textSize(14);
        this.p.textAlign(this.p.LEFT, this.p.TOP);
        this.p.text("visualisation of real time emotional response", 150, 15); // Adjusted Y for TOP alignment
        this.p.text("www.emotionvis.com", 150, 30); // Adjusted Y

        // Date and Time (using p5.js date/time functions)
        let now = new Date(); // Get current date/time
        let dateString = `${this.p.day()}/${this.p.month()}/${this.p.year()}`;
        let timeString = `${this.p.hour()}:${this.p.nf(this.p.minute(),2)}:${this.p.nf(this.p.second(),2)}`; // nf for padding

        this.p.textAlign(this.p.RIGHT, this.p.TOP); // Align to the right
        this.p.text(dateString, this.p.width - 15, 15);
        this.p.text(timeString, this.p.width - 15, 30);
    }

    // Method to update data for all components
    updateData(newTextObject) {
        this.t = newTextObject; // Update internal text object

        // Update island shape lines
        let newDestArr = this.t.getOverall(2) ? this.t.getOverall(2).slice() : Array(8).fill(0);
        this.setLines(newDestArr);

        // Trigger animations/updates in child components
        this.setDest();
        // Note: setDest() already calls changeSize on graphs and circle.
        // If specific graph data needs to be passed differently than what setDest assumes,
        // then call graph.changeSize() separately here.
    }
}
