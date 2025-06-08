class HistogramLine {
    constructor(p5Instance, loc_) {
        this.p = p5Instance;
        this.loc = loc_.copy();
        this.destLoc = loc_.copy();
        this.vel = this.p.createVector(0, 0, 0); // p5.Vector
        this.acc = this.p.createVector(0, 0, 0); // p5.Vector
        this.maxvel = 7;
        this.damper = this.p.random(0.2, 0.8);
        this.damp = 0.9;
        this.mass = 15;
        this.k = 1;
    }

    spring() {
        let force = p5.Vector.sub(this.loc, this.destLoc);
        force.mult(-this.k);
        force.div(this.mass);
        this.acc.set(0, 0); // Assuming 2D for now
        this.acc.add(force);
        this.vel.add(this.acc);
        this.vel.mult(this.damp);
        this.loc.add(this.vel);
    }

    setDestLoc(destLoc_) {
        this.destLoc = destLoc_.copy();
    }

    update() {
        this.vel = p5.Vector.sub(this.destLoc, this.loc);
        this.vel.mult(this.damper);
        this.vel.mult(0.3); // This was originally mult(0.03) but seems too small, using 0.3 as in Island.java's direct use
        this.loc.add(this.vel);
        this.vel.limit(this.maxvel); // In Processing, limit was on PVector, p5.js vel.limit is correct
    }

    // Added for drawing, not in original spec but useful for testing
    display() {
        this.p.ellipse(this.loc.x, this.loc.y, 5, 5);
    }
}
