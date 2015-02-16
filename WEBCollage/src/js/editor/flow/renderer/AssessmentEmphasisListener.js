/**
Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Web Collage.

Web Collage is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Web Collage is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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


