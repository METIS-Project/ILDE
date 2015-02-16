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

var Intersection = {

    findIntersection: function(from, to){
        if ((from.x == to.x) ||
            (Math.abs((from.y - to.y) / (from.x - to.x)) > Math.abs(to.height / to.width))) {
            // horizontal segments
            return {
                x: to.x + to.height * (from.x - to.x) / Math.abs(2 * (from.y - to.y)),
                y: to.y + (from.y < to.y ?  -to.height/2 : to.height/2)
            };
        } else {
            return {
                x: to.x + (from.x < to.x ? -to.width/2 : to.width/2),
                y: to.y + to.width * (from.y - to.y) / Math.abs(2 * (from.x - to.x))
            }
        }
    },


    intersect: function(points) {
        if (points[0].width) {
            for (var i = 1; i < points.length; i++) {
                if (this.isOutside(points[0], points[i])) {
                    var intersection = this.findIntersection(points[i], points[0]);
                    if (points[i].keepX) {
                        intersection.x = points[i].x;
                    } else if (points[i].keepY) {
                        intersection.y = points[i].y;
                    }
                
                    points.splice(0, i, intersection);
                    break;
                }
            }
        }
        
        if (points[points.length - 1].width) {
            for (i = points.length - 2; i >= 0; i--) {
                if (this.isOutside(points[points.length - 1], points[i])) {
                    intersection = this.findIntersection(points[i], points[points.length - 1]);
                    /*if (points[i].keepX) {
                        intersection.x = points[i + 1].x;
                    } else if (points[i].keepY) {
                        intersection.y = points[i + 1].y;
                    }*/

                    points.splice(i + 1, points.length - i - 1, intersection);
                    break;
                }
            }
        }
    },

    isOutside: function(to, point) {
        return Math.abs(to.x - point.x) > .5 * to.width || Math.abs(to.y - point.y) > .5 * to.height;
    }
}


