class Graph {
    constructor(p5Instance, title, id, fonts, loc, textObject) {
        this.p = p5Instance;
        this.title = title;
        this.id = id; // This is the index for textObject.getOverall(id)
        this.fonts = fonts; // Placeholder for actual font objects
        this.loc = loc.copy();
        this.t = textObject;

        this.vel = []; // Array of p5.Vectors
        // this.acc = this.p.createVector(0, 0); // Not directly used in update methods shown

        this.maxvel = 7;
        this.damper = this.p.random(0.2, 0.8);
        // this.destLoc = this.loc.copy(); // Present in Java, seems unused for Graph's direct behavior

        this.names = [
            "ANXIETY", "RELAXATION", "CONCENTRATION",
            "ENERGY", "EXCITEMENT", "CONFIDENCE",
            "PRODUCTIVITY", "SENSITIVITY"
        ];
        this.colors = [ // Copied from Circlem, though not directly used in Graph.java methods
            [255, 0, 0], [0, 255, 0], [0, 0, 255], [255, 255, 0],
            [255, 0, 255], [0, 255, 255], [255, 128, 0], [128, 0, 255]
        ];

        if (!this.t || typeof this.t.getOverall !== 'function' || !this.t.getOverall(this.id)) {
            console.error("Text object or data for ID " + this.id + " is not valid.");
            this.overall = Array(8).fill(0); // Default to an array of zeros
        } else {
            this.overall = this.t.getOverall(this.id).slice(); // Use .slice() for a copy
        }

        this.cur = [];
        let dataLength = this.overall.length; // Use the length of the actual data

        for (let i = 0; i < dataLength; i++) {
            this.cur[i] = this.p.createVector(0, this.loc.y + i * 9);
            this.vel[i] = this.p.createVector(0, 0); // Initialize velocities
        }

        this.buildGraph();
    }

    buildGraph() {
        this.dest = [];
        let dataLength = this.overall.length;
        for (let i = 0; i < dataLength; i++) {
            // Ensure overall[i] is a number, default to 0 if not
            let val = typeof this.overall[i] === 'number' ? this.overall[i] : 0;
            this.dest[i] = this.p.createVector(val, this.loc.y + i * 9);
        }
    }

    draw() {
        this.drawMainTitle();
        this.drawTitles();
        this.drawGraphsBG();
        this.update(); // Call update before drawing the graphs themselves
        this.drawGraphs();
        this.drawNumbers();
    }

    drawMainTitle() {
        this.p.fill(180); // Slightly darker than original for visibility
        this.p.textFont(this.fonts[4] || 'Arial'); // Fallback font
        this.p.textSize(14);
        this.p.textAlign(this.p.LEFT, this.p.BOTTOM);
        this.p.text(this.title, this.loc.x + 50, this.loc.y - 5);
    }

    drawTitles() {
        this.p.fill(180);
        this.p.textFont(this.fonts[6] || 'Arial'); // Fallback font
        this.p.textSize(8); // Smaller for list items
        this.p.textAlign(this.p.LEFT, this.p.TOP);
        for (let i = 0; i < this.names.length; i++) {
            // let j = i + 1; // Original used j for y-offset, direct mapping here
            this.p.text(this.names[i], this.loc.x, this.loc.y + i * 9);
        }
    }

    update() {
        let dataLength = this.overall.length;
        for (let i = 0; i < dataLength; i++) {
            // Ensure this.dest[i] and this.cur[i] are valid p5.Vectors
            if (!this.dest[i] || !this.cur[i]) continue;

            let v = p5.Vector.sub(this.dest[i], this.cur[i]);
            v.mult(this.damper);
            // In Java, damper was 0.2-0.8, then vel was mult by 0.03.
            // Here, matching HistogramLine's update which used 0.3.
            // If damper is ~0.5, 0.5 * 0.3 = 0.15. Original was 0.03. Let's try a smaller factor.
            v.mult(0.1); // Adjusted damping factor

            // this.vel[i] = v; // Store velocity if planning to use this.vel array
            // this.vel[i].limit(this.maxvel); // Limit it
            // this.cur[i].add(this.vel[i]); // Add limited velocity

            this.cur[i].add(v); // Simpler approach without storing separate vel array for this update logic
        }
    }

    getIntenseItself(cur_array) {
        if (!cur_array || cur_array.length === 0) return 0;
        let nGreatest = cur_array[0];
        for (let k = 1; k < cur_array.length; ++k) {
            if (cur_array[k] > nGreatest) {
                nGreatest = cur_array[k];
            }
        }
        return nGreatest;
    }

    drawGraphs() {
        this.p.fill(200); // Light gray for graph bars
        this.p.rectMode(this.p.CORNER);
        this.p.noStroke();

        let m = this.getIntenseItself(this.overall); // Get max from the target values for scaling
        let m1 = (m === 0) ? 1 : m / 100.0; // Scale factor: 100px bar width for max value

        let dataLength = this.cur.length; // Use this.cur.length
        for (let i = 0; i < dataLength; i++) {
            if (!this.cur[i]) continue;
            let f = this.cur[i].x; // Current animated value
            let result = f / m1; // Scale the bar length
            this.p.rect(50 + this.loc.x, this.cur[i].y, result, 8);
        }
    }

    drawGraphsBG() {
        this.p.noFill();
        this.p.stroke(220); // Light stroke for background guides
        this.p.strokeWeight(1);
        this.p.rectMode(this.p.CORNER);
        let dataLength = this.names.length; // Based on number of labels
        for (let i = 0; i < dataLength; i++) {
            // y-position should match this.cur[i].y if available, or loc.y + i*9
            let yPos = (this.cur[i] && this.cur[i].y !== undefined) ? this.cur[i].y : this.loc.y + i * 9;
            this.p.rect(50 + this.loc.x, yPos, 100, 8); // 100px wide background bar
        }
        this.p.noStroke();
    }

    changeSize(overall_) {
        if (!overall_ || overall_.length === 0) {
            console.warn("Graph.changeSize called with invalid data.");
            return;
        }
        this.overall = overall_.slice(); // Make a copy
        let dataLength = Math.min(this.overall.length, this.dest.length); // Ensure we don't go out of bounds
        for (let i = 0; i < dataLength; i++) {
            if (this.dest[i]) { // Check if dest[i] exists
                 // Ensure overall_[i] is a number, default to 0 if not
                let val = typeof overall_[i] === 'number' ? overall_[i] : 0;
                this.dest[i].set(val, this.loc.y + i * 9);
            }
        }
         // If overall_ is longer than current dest, need to rebuild part of dest/cur
        if (overall_.length > this.dest.length) {
            for (let i = this.dest.length; i < overall_.length; i++) {
                let val = typeof overall_[i] === 'number' ? overall_[i] : 0;
                this.dest[i] = this.p.createVector(val, this.loc.y + i * 9);
                if (!this.cur[i]) { // If new data means new bars
                    this.cur[i] = this.p.createVector(0, this.loc.y + i * 9); // Start from 0 width
                }
            }
        }
        // If overall_ is shorter, existing dest/cur arrays will just not be updated for those higher indices.
        // This is fine if names array also shrinks or if those bars just stay at old values.
        // For this project, overall array length is fixed at 8.
    }

    drawNumbers() {
        this.p.fill(150); // Darker gray for numbers
        this.p.textFont(this.fonts[2] || 'Arial'); // Fallback font
        this.p.textSize(10); // Smaller size for numbers
        this.p.textAlign(this.p.LEFT, this.p.CENTER);

        let dataLength = this.overall.length;
        for (let i = 0; i < dataLength; i++) {
            // let j = i + 1; // Original y-offset logic
            // this.p.text(this.overall[i], this.loc.x + 150 + 5, this.loc.y + j * 9);
            // Align text with the bar's vertical center
            let yPos = (this.cur[i] && this.cur[i].y !== undefined) ? this.cur[i].y + 4 : this.loc.y + i * 9 + 4; // +4 for ~middle of 8px bar
            let valueToDisplay = (typeof this.overall[i] === 'number') ? this.p.round(this.overall[i]) : 'N/A';
            this.p.text(valueToDisplay, this.loc.x + 150 + 10, yPos); // +5px padding from 100px bar + 50px offset
        }
    }
}
