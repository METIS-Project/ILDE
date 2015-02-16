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
var AssessmentFlowRenderer = {
    smallsize : 17,
    bigsize : 24,

    style : {
        linecolor : "black",
        fill : [238, 238, 208, 1.0],
        arrow : [130, 130, 130, 1.0]
    },

    pointToLocationGap : 30, /* *= subClfpScale when in minimized clfp */
    gapBetweenAssessments : {
        dx : 25,
        dy : 10
    }, /* *= subClfpScale when in minimized clfp */
    image : {
        width : 35,
        height : 30
    },
    idForTooltip : 0,

    paint : function(renderer, paintAlerts) {
        var positions = {};

        for(var i in LearningDesign.data.assessments) {
            var assessment = LearningDesign.data.assessments[i];
            this.paintAssessment(assessment, renderer, positions, paintAlerts);
            //renderData.mainBlock, surface, positions, renderData.separators, verticalLines, paintAlerts);
        }
    },
    paintAssessment : function(assessment, renderer, positions, paintAlerts) {//block, surface, positions, separators, verticalLines, paintAlerts){
        var iconpositions = this.getIconPositions(assessment, renderer);

        var linesgroup = renderer.bigGroup.createGroup();
        var fillgroup = renderer.bigGroup.createGroup();
        
        renderer.animator.addNoAnimStuff(linesgroup);
        renderer.animator.addNoAnimStuff(fillgroup);
        
        for(var i in assessment.getFunctions()) {
            var af = assessment.getFunction(i);
            this.paintAssessmentLink(iconpositions.assessment, iconpositions.functions[af.id], linesgroup, fillgroup, AssessmentLineRenderer.assessmentLineColors[af.subtype], this.smallsize);
            this.paintAssessmentPart(assessment, iconpositions.functions[af.id], af.subtype, linesgroup, fillgroup, this.smallsize, renderer.uiListener);
        }

        this.paintAssessmentLink(iconpositions.assessed, iconpositions.assessment, linesgroup, fillgroup, AssessmentLineRenderer.assessmentLineColors.assessed, this.bigsize);
        this.paintAssessmentPart(assessment, iconpositions.assessment, this.getAssessmentIconName(assessment), linesgroup, fillgroup, this.bigsize, renderer.uiListener);
        this.paintAssessmentPart(assessment, iconpositions.assessed, "assessed", linesgroup, fillgroup, this.smallsize, renderer.uiListener);

    },
    getIconPositions : function(assessment, renderer) {
        var positions = {
            assessment : null,
            assessed : null,
            functions : {}
        };

        var p0 = this.getIconPosition(assessment.getAssessedActivity(), renderer);
        var p1 = this.getIconPosition(assessment.getAssessmentActivity(), renderer);
        var p2x = 0;
        var pfs = {};
        var functions = assessment.getFunctions();

        var fcount = 0;

        for(var i in functions) {
            var af = functions[i];
            var p = this.getIconPosition(af.getLinkedActivity(), renderer);
            pfs[af.id] = p;
            if(p.hasPosition) {
                fcount++;
                p2x += p.center;
            }
        }

        if(!p1.hasPosition) {
            positions.assessed = {
                x : p0.center,
                y : p0.y
            };
            positions.assessment = {
                x : p0.center + 20,
                y : p0.y
            };
        } else if(!p0.hasPosition) {
            if(fcount > 0) {
                p2x /= fcount;
                positions.assessment = {
                    x : this.getGoodPositionInRange(p2x, p1.min, p1.max),
                    y : p1.y
                };
            } else {
                positions.assessment = {
                    x : p1.center,
                    y : p1.y
                };
            }
            positions.assessed = {
                x : positions.assessment.x - 20,
                y : positions.assessment.y
            };
        } else {
            var bps = this.getBestPositions(p0, p1);
            positions.assessed = {
                x : bps[0],
                y : p0.y
            };
            positions.assessment = {
                x : bps[1],
                y : p1.y
            };
        }
        /*
         if(p1.hasPosition) {
         positions.assessment = {
         x : p1.max,
         y : p1.y
         };
         }
         if(p0.hasPosition) {
         positions.assessed = {
         x : p0.max,
         y : p0.y
         };
         }

         if(!p1.hasPosition) {
         positions.assessment = {
         x : p0.max + 20,
         y : p0.y
         };
         }

         if(!p0.hasPosition) {
         positions.assessed = {
         x : p1.max - 20,
         y : p1.y
         };
         }*/

        for(var i in pfs) {
            var pf = pfs[i];
            positions.functions[i] = pf.hasPosition ? {
                x : this.getGoodPositionInRange(positions.assessment.x, pf.min, pf.max),
                //                x : pf.max,
                y : pf.top
            } : {
                x : positions.assessment.x + 20,
                y : positions.assessment.y
            };
        }

        return positions;
    },
    getBestPositions : function(range1, range2) {
        if(range1.min > range2.max) {
            return [range1.min, range2.max];
        } else if(range1.max < range2.min) {
            return [range1.max, range2.min];
        } else {
            var min = Math.max(range1.min, range2.min);
            var max = Math.min(range1.max, range2.max);
            return [this.getGoodPositionInRange(range1.center, min, max), this.getGoodPositionInRange(range2.center, min, max)];
        }
    },
    getGoodPositionInRange : function(p, min, max) {
        if(p < min) {
            return min;
        } else if(p > max) {
            return max;
        } else {
            return p;
        }
    },
    getIconPosition : function(activity, renderer) {
        if(activity) {
            var activityGrahicData = renderer.graphicElements.activities[activity.id];
            var rect = null;

            if(activityGrahicData) {
                rect = activityGrahicData.rect;
            } else {
                var location = LearningDesign.findActParentOfActivity(activity.id);
                rect = renderer.graphicElements.acts[location.act.id].roles[location.roleId];
                inAct = true;
            }

            var hw = rect.width > 50 ? 0.5 * rect.width - 20 : 0.3 * rect.width;
            var c = rect.x + 0.5 * rect.width;

            return {
                hasPosition : true,
                min : c - hw,
                max : c + hw,
                top : rect.y + 0.25 * rect.height,
                y : rect.y + 0.75 * rect.height,
                center : c
            };
        } else {
            return {
                hasPosition : false
            };
        }
    },
    paintAssessmentPart : function(assessment, position, iconName, linesgroup, fillgroup, width, uiListener) {
        var backgroundwidth = width + 2;
        var linebackgroundwidth = backgroundwidth + 2;

        var iconData = AssessmentIcons.getDataFixedWidth2(iconName, width, position);

        /*linesgroup.createRect({
         x : position.x - linebackgroundwidth / 2,
         y : position.y - linebackgroundwidth / 2,
         width : linebackgroundwidth,
         height : linebackgroundwidth,
         }).setFill(this.style.fill).setStroke({
         color : this.style.linecolor,
         width : 1
         });
         fillgroup.createRect({
         x : position.x - backgroundwidth / 2,
         y : position.y - backgroundwidth / 2,
         width : backgroundwidth,
         height : backgroundwidth,
         }).setFill(this.style.fill);*/
        if (iconData!=null)
        {

            var image = fillgroup.createImage(iconData);
            uiListener.registerActiveElement(image, {
                id : assessment.id
            });
        }
    },
    getAssessmentIconName : function(assessment) {
        var patterns = assessment.getPatterns();

        for(var i in patterns) {
            if(patterns[i].definesAssessmentActivity()) {
                return patterns[i].getName();
            }
        }

        return "empty";
    },
    paintAssessmentLink : function(from, to, linesgroup, fillgroup, lineColors, endpointsize) {
        /*linesgroup.createLine({
         x1 : from.x,
         y1 : from.y,
         x2 : to.x,
         y2 : to.y
         }).setStroke({
         color : this.style.linecolor,
         width : 7
         });

         var linefill = fillgroup.createLine({
         x1 : from.x,
         y1 : from.y,
         x2 : to.x,
         y2 : to.y
         }).setStroke({
         color : this.style.fill,
         width : 5
         });

         var angle = Math.atan2(to.y - from.y, to.x - from.x);
         var dx = 10 * Math.cos(angle), dy = 10 * Math.sin(angle);
         var cy = 0.5 * (to.y + from.y), cx = 0.5 * (to.x + from.x);
         var points = [{
         x : cx - dx,
         y : cy - dy
         }, {
         x : cx + dx,
         y : cy + dy
         }];
         LinePainter.paintLine(points, fillgroup, {
         fill : this.style.arrow,
         witdh : 2
         }, false, {
         position : "end",
         length : 2
         });*/

        var dx = to.x - from.x;
        var dy = to.y - from.y;
        var d = Math.sqrt(dx * dx + dy * dy);
        if(d > 0) {
            var k = (d - endpointsize / 2) / d;
            var fp = {
                x : from.x + dx * k,
                y : from.y + dy * k
            };
            LinePainter.paintLine([from, fp], linesgroup, lineColors, false, {
                position : "end"
            });
        }

    }
};
