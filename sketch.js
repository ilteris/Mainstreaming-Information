let titleImage;
let textData;   // General text data, can be used for Island
let mockTextForSmallIsland; // Specific text object for Smallisland testing
// let mainIsland;
// let testSmallCircle;
let testSmallIsland; // For testing Smallisland

const mockDataSets = [
    [
        "500", "200 300", "10 20 30 40 50 60 70 80",
        "5 10 15 20 25 30 35 40", "80 70 60 50 40 30 20 10"
    ],
    [
        "300", "100 200", "80 70 60 50 40 30 20 10",
        "40 35 30 25 20 15 10 5", "5 10 15 20 25 30 35 40"
    ],
    [
        "700", "400 300", "30 40 50 60 70 80 10 20", // Overall (index 2)
        "20 25 30 35 40 45 50 55", "60 50 40 30 20 10 5 0"
    ]
];
let currentMockDataSetIndex = 0; // For cycling through datasets

const placeholderFonts = [
    'Arial', 'Verdana', 'Helvetica', 'Tahoma', 'Trebuchet MS',
    'Times New Roman', 'Georgia', 'Garamond', 'Courier New', 'Brush Script MT'
];

function preload() {
    titleImage = loadImage('assets/titileps.png');
}

function setup() {
    let canvas = createCanvas(800, 600);
    canvas.parent('main');

    // General textData for main Island (if uncommented)
    textData = new Text(this);
    textData.parseLines(mockDataSets[0]); // Initial data for main Island

    // Specific Text object for Smallisland testing
    mockTextForSmallIsland = new Text(this);
    mockTextForSmallIsland.parseLines(mockDataSets[1]); // Use a different dataset for Smallisland initially

    // Comment out Island initialization
    // if (titleImage && textData && placeholderFonts) {
    //     mainIsland = new Island(this, placeholderFonts, textData, titleImage);
    // }

    // Comment out direct Smallcircle test
    // let scLoc = createVector(100, 100);
    // let scSize = createVector(30, 30);
    // testSmallCircle = new Smallcircle(this, scSize, scLoc, 200, 150, 0);
    // testSmallCircle.stretch();

    // Test Smallisland
    // Parameters: p5, textObject, locVec (top-left of Latte), latteTotalHeight, latteWidth, mostIntenseAreaIndex (e.g. 2 for overall)
    let siLoc = createVector(50, 300); // Position for the mock "Latte" container
    // Ensure mockTextForSmallIsland is valid before passing
    if (mockTextForSmallIsland && mockTextForSmallIsland.getOverall(2)) {
         testSmallIsland = new Smallisland(this, mockTextForSmallIsland, siLoc, 100, 150, 2); // Using overall data (index 2) for mIAIndex
    } else {
        console.error("Failed to initialize Smallisland: mockTextForSmallIsland is not valid.");
        // Fallback: create a dummy text object
        let dummyText = new Text(this);
        dummyText.parseLines(["0","0 0","10 10 10 10 10 10 10 10","0 0 0 0 0 0 0 0","0 0 0 0 0 0 0 0"]);
        testSmallIsland = new Smallisland(this, dummyText, siLoc, 100, 150, 2);
    }


    canvas.mousePressed(() => {
        // Main island data update (if uncommented)
        // if (mainIsland) { ... }

        // Smallcircle direct test interaction (if uncommented)
        // if (testSmallCircle) { ... }

        if (testSmallIsland) {
            // Option 1: Toggle stretch/stretchBack
            // Simple toggle based on whether destArr is different from curArr (implies stretched)
            // This logic might need refinement based on how exactly stretch changes destArr
            let isStretched = false;
            if (testSmallIsland.destArr && testSmallIsland.curArr) {
                for(let i=0; i<testSmallIsland.destArr.length; i++) {
                    if (testSmallIsland.destArr[i] !== testSmallIsland.curArr[i]) {
                        isStretched = true;
                        break;
                    }
                }
            }

            if (isStretched) {
                testSmallIsland.stretchBack();
                console.log("Smallisland: stretchBack triggered");
            } else {
                testSmallIsland.stretch();
                console.log("Smallisland: stretch triggered");
            }

            // Option 2: Cycle through data for Smallisland (demonstrates updateData)
            // currentMockDataSetIndex = (currentMockDataSetIndex + 1) % mockDataSets.length;
            // let newTextForSI = new Text(this);
            // newTextForSI.parseLines(mockDataSets[currentMockDataSetIndex]);
            // testSmallIsland.updateData(newTextForSI);
            // console.log("Smallisland data updated to set:", currentMockDataSetIndex);
        }
    });
}

function draw() {
    background(230); // Slightly different background for testing

    // if (mainIsland) mainIsland.draw();
    // if (testSmallCircle) testSmallCircle.draw();
    if (testSmallIsland) testSmallIsland.draw();
}
