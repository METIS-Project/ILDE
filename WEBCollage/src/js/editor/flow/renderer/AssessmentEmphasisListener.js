
var AssessmentEmphasisManager = {
    listeners: null,

    reset: function() {
        this.listeners = {};
    },

    getListener: function(id) {
        if (! (id in this.listeners)) {
            this.listeners[id] = new AssessmentEmphasisListener();
        }

        return this.listeners[id];
    },

    registerImage: function(assessment, shape, data) {
        var listener = this.getListener(assessment.id);
        listener.addImage(shape, data);
    },

    registerShape: function(assessment, shape, strokes) {
        var listener = this.getListener(assessment.id);
        listener.addShape(shape, strokes);
    }
};

function AssessmentEmphasisListener() {
    this.images = [];
    this.shapes = [];
}

AssessmentEmphasisListener.prototype.addImage = function(shape, data) {
    this.images.push({
        shape: shape,
        data: data
    });
    shape.connect("onmouseover", this, "emphasis");
    shape.connect("onmouseout", this, "normal");
};

AssessmentEmphasisListener.prototype.addShape = function(shape, strokes) {
    this.shapes.push({
        shape: shape,
        strokes: strokes
    });
    shape.connect("onmouseover", this, "emphasis");
    shape.connect("onmouseout", this, "normal");
};

AssessmentEmphasisListener.prototype.normal = function() {
    for (var i = 0; i < this.images.length; i++) {
        this.images[i].shape.setShape({
            src: this.images[i].data.src
        });
    }

    for (i = 0; i < this.shapes.length; i++) {
        this.shapes[i].shape.setStroke(this.shapes[i].strokes.normal.fill);
        this.shapes[i].shape.underlayingShapeAsBorder.setStroke(this.shapes[i].strokes.normal.border);
    }
};

AssessmentEmphasisListener.prototype.emphasis = function() {
    for (var i = 0; i < this.images.length; i++) {
        this.images[i].shape.setShape({
            src: this.images[i].data.emp
        });
    }

    for (i = 0; i < this.shapes.length; i++) {
        this.shapes[i].shape.setStroke(this.shapes[i].strokes.emphasis.fill);
        this.shapes[i].shape.underlayingShapeAsBorder.setStroke(this.shapes[i].strokes.emphasis.border);
    }
};


