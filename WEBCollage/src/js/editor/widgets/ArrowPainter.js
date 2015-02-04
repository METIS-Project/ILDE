/**
 * @author Eloy
 */
function ArrowPainter(node, surface){
    this.node = node;
    this.surface = surface; 
}

ArrowPainter.prototype.paintStraightArrow = function(from, to, colors){
    var posA = this.getPosition(from);
    var posB = this.getPosition(to);
    
    var iA = Intersection.findIntersection(posA, posB);
    var iB = Intersection.findIntersection(posB, posA);
    var points = [iA, iB];
    
    LinePainter.paintLine(points, this.surface, colors, false, {
        position: "end",
        length: 5
    }, false);
    
//    this.surface.createLine(line).setStroke(stroke).setFill(stroke.fill);
//    this.paintArrow(iB, posA, posB, stroke);
};

ArrowPainter.prototype.createArc = function(center, width, height, angleStart, angleEnd){
    var n = 20;
    angleStart *= Math.PI / 180;
    angleEnd *= Math.PI / 180;
    
    var step = (angleEnd - angleStart) / (n - 1);
    
    var points = [];
    for (var i = 0; i < n; i++) {
        var angle = angleStart + i * step;
        points[i] = {
            x: center.x + .5 * width * Math.cos(angle),
            y: center.y - .5 * height * Math.sin(angle)
        };
    }
    
    return points;
};

ArrowPainter.prototype.paintCurvedRightArrow = function(to, from, colors){
    var posA = this.getPosition(from);
    var posB = this.getPosition(to);
    
    var height = Math.abs(posA.y - posB.y);
    var width = .7 * height;
    var center = {
        x: Math.max(posA.x + .5 * posA.width, posB.x + .5 * posB.width) + .1 * width,
        y: Math.min(posA.y, posB.y) + .5 * height
    };
    
    var points = [];
    points.push({
        x: posA.x + .5 * posA.width,
        y: posA.y
    });
    var arcPoints = this.createArc(center, width, height, posB.y > posA.y ? 90 : -90, posB.y > posA.y ? -90 : 90);
    for (var i = 0; i < arcPoints.length; i++) {
        points.push(arcPoints[i]);
    }
    
    points.push({
        x: posB.x + .5 * posB.width,
        y: posB.y
    });
    
    LinePainter.paintLine(points, this.surface, colors, false, {
        position: "end",
        length: colors.width * 1.25
    }, false);
};

ArrowPainter.prototype.paintCurvedBelowArrow = function(to, from, colors) {
    var posA = this.getPosition(from);
    var posB = this.getPosition(to);
    
    var width = Math.abs(posA.x - posB.x);
    var height = .5 * width;
    var center = {
        x: Math.min(posA.x, posB.x) + .5 * width,
        y: Math.max(posA.y + .5 * posA.height, posB.y + .5 * posB.height) + .1 * height
    };
    
    var points = [];
    points.push({
        x: posA.x,
        y: posA.y + .5 * posA.height
    });
    
    var arcPoints = this.createArc(center, width, height, posB.x > posA.x ? -180 : 0, posB.x > posA.x ? 0 : 180);
    for (var i = 0; i < arcPoints.length; i++) {
        points.push(arcPoints[i]);
    }
    
    points.push({
        x: posB.x,
        y: posB.y + .5 * posB.height
    });
    
    LinePainter.paintLine(points, this.surface, colors, false, {
        position: "end",
        length: colors.width * 1.25
    }, false);
};

ArrowPainter.prototype.paintArrow = function(where, to, from, stroke){
    var angle = Math.atan2(from.y - to.y, from.x - to.x);
    var sweep = 0.6;
    //var length = 6 * scale;
    var length = 6;
    
    var points = [{
        x: where.x,
        y: where.y
    }, {
        x: where.x + length * Math.cos(angle + sweep),
        y: where.y + length * Math.sin(angle + sweep)
    }, {
        x: where.x + length * Math.cos(angle - sweep),
        y: where.y + length * Math.sin(angle - sweep)
    }, {
        x: where.x,
        y: where.y
    }];
    
    this.surface.createPolyline(points).setStroke(stroke).setFill(stroke.fill);
};

ArrowPainter.prototype.getPosition = function(thenode){
    var pp = dojo.coords(this.node);
    var op = dojo.coords(thenode);
    
    return {
        x: op.x - pp.x + thenode.offsetWidth / 2,
        y: op.y - pp.y + thenode.offsetHeight / 2,
        width: thenode.offsetWidth,
        height: thenode.offsetHeight
    };
};


ArrowPainter.prototype.clear = function(){
    this.surface.clear();
};
