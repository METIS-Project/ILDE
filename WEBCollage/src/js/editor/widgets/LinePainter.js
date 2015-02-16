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

/**
 * @author Eloy
 */
var LinePainter = {

    paintLine: function(points, surface, colors, makeItCute, arrow, isNoAnimStuff, animator, noEmphasis) {
        if (arrow) {
            if (! arrow.length) {
                arrow.length = 6;
            }
            this.removeArrowSegment(points, arrow);
        }

        if (makeItCute) {
            points = this.transformLine(points);
        }
        
        this.appendArrow(points, arrow);
        
        var strokes = this.getStrokes(colors);
        
        if (strokes.normal.border) {
            var border = surface.createPolyline(points).setStroke(strokes.normal.border);
            if (isNoAnimStuff) {
                animator.addNoAnimStuff(border);
            }
        } else {
            border = null;
        }
        
        var fill = surface.createPolyline(points).setStroke(strokes.normal.fill);
        if (isNoAnimStuff) {
            animator.addNoAnimStuff(fill);
        }

        fill.underlayingShapeAsBorder = border;
        
        if (strokes.emphasis && ! noEmphasis) {
            new LineEmphasisListener(strokes, fill, border);
        }
        
        return fill;
    },
     
    transformLine: function(points, maxRadius, distanceK){
        if (points.length < 3) {
            return points;
        }
        
        var newpoints = [];
        newpoints.push(points[0]);
        
        for (var i = 1; i < points.length - 1; i++) {
        
            var d1 = {
                x: points[i].x - points[i - 1].x,
                y: points[i].y - points[i - 1].y
            };
            var d2 = {
                x: points[i].x - points[i + 1].x,
                y: points[i].y - points[i + 1].y
            };
            
            var minLength2 = Math.min(d1.x * d1.x + d1.y * d1.y, d2.x * d2.x + d2.y * d2.y);
            var g = Math.min(maxRadius ? maxRadius : 60, (distanceK ? 2 * distanceK : .7) * Math.sqrt(minLength2));
                        
            var angle1 = Math.atan2(points[i - 1].y - points[i].y, points[i - 1].x - points[i].x);
            var angle2 = Math.atan2(points[i + 1].y - points[i].y, points[i + 1].x - points[i].x);
            
            var angle = angle2 - angle1;
            var oppose = false;
            var oppose2 = false;
            if (angle > Math.PI) {
                angle += 2 * Math.PI;
                oppose = true;
            } else if (angle < -Math.PI) {
                angle -= -2 * Math.PI;
                oppose2 = true;
            }
            
            var beta1 = angle1 + (oppose ? -1 : 1) * (oppose2 ? -1 : 1) * (angle2 < angle1 ? .5 : -.5) * Math.PI + (!oppose && !oppose2 && angle1 < angle2 ? 2 * Math.PI : 0);
            var beta2 = angle2 + (oppose ? -1 : 1) * (oppose2 ? -1 : 1) * (angle2 < angle1 ? -.5 : .5) * Math.PI + (!oppose && !oppose2 && angle2 < angle1 ? 2 * Math.PI : 0);
            
            var halfangle = .5 * angle;
            var bAngle = angle1 + halfangle;
            var coshalfangle = Math.cos(halfangle);
            
            var center = {
                x: points[i].x + g * Math.cos(bAngle),
                y: points[i].y + g * Math.sin(bAngle)
            };
            
            var p1 = {
                x: points[i].x + g * Math.cos(angle1) * coshalfangle,
                y: points[i].y + g * Math.sin(angle1) * coshalfangle
            };
            
            var dd = {
                x: p1.x - center.x,
                y: p1.y - center.y
            };
            
            
            var r = Math.sqrt(dd.x * dd.x + dd.y * dd.y);
            
            var arc = this.createArc(center, r, beta1, beta2);
            
            //           newpoints.push(p1);
            for (var j = 0; j < arc.length; j++) {
                newpoints.push(arc[j]);
            }
        //         newpoints.push(p2);
        }
        
        newpoints.push(points[points.length - 1]);
        return newpoints;
    },
    
    getStrokes: function(colors) {
        var base = colors.base ? colors.base() : colors;
        return {
            normal: {
                border: base.border ? {
                    color: base.border,
                    width: colors.width,
                    cap: "round",
                    join: "round"
                } : null,
                fill: {
                    color: base.fill,
                    width: base.border ? colors.width - (colors.borderWidth ? colors.borderWidth : 2) : colors.width,
                    cap: "round",
                    join: "round"
                }
            },
            emphasis: base.emphasis ? {
                border: base.emphasis.border ? {
                    color: base.emphasis.border,
                    width: colors.width,
                    cap: "round",
                    join: "round"
                } : null,
                fill: {
                    color: base.emphasis.fill,
                    width: base.emphasis.border ? colors.width - (colors.borderWidth ? colors.borderWidth : 2) : colors.width,
                    cap: "round",
                    join: "round"
                }
            } : null
        };
    },
    
    markpoint: function(point, surface){
        surface.createCircle({
            cx: point.x,
            cy: point.y,
            r: 3
        }).setFill("blue");
    },
    
    
    
    createArc: function(center, r, angleStart, angleEnd){
        var n = 20;
        
        var step = (angleEnd - angleStart) / (n - 1);
        
        var points = [];
        for (var i = 0; i < n; i++) {
            var angle = angleStart + i * step;
            points[i] = {
                x: center.x + r * Math.cos(angle),
                y: center.y + r * Math.sin(angle)
            };
        }
        
        return points;
    },

    removeArrowSegment: function(points, arrow) {
        if (points.length < 3) {
            return;
        } else if (arrow.position == "end") {
            var from = points[points.length - 2];
            var to = points[points.length - 1];
            var toDelete = points.length - 1;
        } else if (arrow.position == "start") {
            from = points[1];
            to = points[0];
            toDelete = 0;
        }

        var dx = from.x - to.x;
        var dy = from.y - to.y;
        if (dx * dx + dy * dy < 2 * arrow.length * arrow.length) {
            points.splice(toDelete, 1);
        }
    },
    
    appendArrow: function(points, arrow){
        if (arrow.position == "end") {
            var from = points[points.length - 2];
            var to = points[points.length - 1];
        } else if (arrow.position == "start") {
            from = points[1];
            to = points[0];
        } else {
            return;
        }
        
        var angle = Math.atan2(from.y - to.y, from.x - to.x);
        var sweep = 0.6;
        
        if (arrow.position == "end") {
            points.push({
                x: to.x + arrow.length * Math.cos(angle + sweep),
                y: to.y + arrow.length * Math.sin(angle + sweep)
            });
            points.push({
                x: to.x,
                y: to.y
            });
            points.push({
                x: to.x + arrow.length * Math.cos(angle - sweep),
                y: to.y + arrow.length * Math.sin(angle - sweep)
            });
        } else {
            points.unshift({
                x: to.x + arrow.length * Math.cos(angle + sweep),
                y: to.y + arrow.length * Math.sin(angle + sweep)
            }, {
                x: to.x,
                y: to.y
            }, {
                x: to.x + arrow.length * Math.cos(angle - sweep),
                y: to.y + arrow.length * Math.sin(angle - sweep)
            });
        }
    }
};


function LineEmphasisListener(strokes, fill, border){
    this.strokes = strokes;
    this.fill = fill;
    this.border = border;
    
    fill.connect("onmouseover", this, "emphasis");
    fill.connect("onmouseout", this, "normal");
}

LineEmphasisListener.prototype.emphasis = function(){
    this.fill.setStroke(this.strokes.emphasis.fill);
    if (this.border) {
        this.border.setStroke(this.strokes.emphasis.border);
    }
};

LineEmphasisListener.prototype.normal = function(){
    this.fill.setStroke(this.strokes.normal.fill);
    if (this.border) {
        this.border.setStroke(this.strokes.normal.border);
    }
};
