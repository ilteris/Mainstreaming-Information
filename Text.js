class Text {
    constructor(p) {
        this.p = p; // p5 instance, if needed later
        this.curArray = [];
    }

    parseLines(linesArray) {
        this.curArray = linesArray.map(line =>
            line.split(' ').map(numStr => parseInt(numStr, 10))
        );
    }

    getTotal() {
        if (this.curArray.length > 0 && this.curArray[0].length > 0) {
            return this.curArray[0][0];
        }
        return undefined; // Or throw error
    }

    getOverall(i) {
        if (this.curArray.length > i) {
            return this.curArray[i];
        }
        return undefined; // Or throw error
    }

    getAreaOneTotal() {
        if (this.curArray.length > 1 && this.curArray[1].length > 0) {
            return this.curArray[1][0];
        }
        return undefined; // Or throw error
    }

    getAreaTwoTotal() {
        if (this.curArray.length > 1 && this.curArray[1].length > 1) {
            return this.curArray[1][1];
        }
        return undefined; // Or throw error
    }

    getMostIntense(i) {
        if (this.curArray.length > i && this.curArray[i].length > 0) {
            const arr = this.curArray[i];
            let maxVal = arr[0];
            let maxIndex = 0;
            for (let j = 1; j < arr.length; j++) {
                if (arr[j] > maxVal) {
                    maxVal = arr[j];
                    maxIndex = j;
                }
            }
            return maxIndex;
        }
        return undefined; // Or throw error
    }

    getIntenseItself(i) {
        if (this.curArray.length > i && this.curArray[i].length > 0) {
            const arr = this.curArray[i];
            let maxVal = arr[0];
            for (let j = 1; j < arr.length; j++) {
                if (arr[j] > maxVal) {
                    maxVal = arr[j];
                }
            }
            return maxVal;
        }
        return undefined; // Or throw error
    }
}
