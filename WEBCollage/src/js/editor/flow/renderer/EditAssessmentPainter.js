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

var EditAssessmentPainter = {
    arrowPainter : null,
    mainSurface : null,
    secondarySurface : null,

    assessmentNode : null,
    assessedNode : null,
    functionNodes : null,
    functionTypes : null,

    standardWidth: 250.0,
    typicalWidth: 100.0,

    init : function() {
        var node = dojo.byId("LDAssessmentPaintSurface");
        this.mainSurface = dojox.gfx.createSurface(node, 600, 300);
        this.secondarySurface = dojox.gfx.createSurface(dojo.byId("PatternListInAssessmentContents"), 600, 72);
        this.arrowPainter = new ArrowPainter(node, this.mainSurface);
    },

    clear: function() {
        this.mainSurface.clear();
        this.secondarySurface.clear();
    },

    paint : function() {
        this.paintFlowLine();
        this.arrowPainter.paintCurvedBelowArrow(this.assessmentNode,
            this.assessedNode,
            AssessmentLineRenderer.thickAssessmentLineColors["assessed"]);

        var p0 = this.arrowPainter.getPosition(this.assessmentNode);
        p0.x += .5 * p0.width;

        for ( var i = 0; i < this.functionNodes.length; i++) {
            var p1 = this.arrowPainter.getPosition(this.functionNodes[i]);
            p1.x -= .5 * p1.width;

            var points = [ p0, {
                x: .5 * (p0.x + p1.x),
                y: p1.y
            }, p1];

            LinePainter.paintLine(points, this.arrowPainter.surface, AssessmentLineRenderer.thickAssessmentLineColors[this.functionTypes[i]], true, {
                position: "end",
                length: AssessmentLineRenderer.thickAssessmentLineColors[this.functionTypes[i]].width
            });
        }
    },
    

    paintPattern: function(center, pattern, scale, where) {
        var surface = where == "main" ? this.mainSurface : this.secondarySurface;
        
        var data = AssessmentIcons.getDataFixedWidth(pattern.getName(), 100, scale, center);

        var tooltip = "<div class='patternListTitle'>" + pattern.getTitle() + "<div class='patternListDescription'><span class='";
        tooltip += pattern.getType() == "analysis" ? ("patternListTechique'>" +  i18n.get("selector.assessment.technique")) : ("patternListConfiguration'>" + i18n.get("selector.assessment.configuration"));
        tooltip += "</span>" + pattern.getDescription() + "</div></div>";
        
        var image = surface.createImage(data);
        GFXTooltip.register(image, tooltip);
        return image;
    },

    paintFlowLine : function() {
        var where = dojo.byId("LDAssessmentPaintSurface");
        var points = [ {
            x : where.offsetWidth / 15,
            y : where.offsetHeight / 2
        }, {
            x : 14 * where.offsetWidth / 15,
            y : where.offsetHeight / 2
        } ];

        LinePainter.paintLine(points, this.mainSurface, CoolLineRenderer.mainLineColors, false, {
            position : "end",
            length : 15
        }, false).moveToBack();
    },

    paintAssessmentRole: function(center, assessmentActivity, isAssessor) {
        if (assessmentActivity) {
            var role = IDPool.getObject(LearningDesign.findActParentOfActivity(assessmentActivity.id).roleId);
            var size = 29;
            var data = {
                width: size,
                height: size,
                x: center.x - .5 * size,
                y: center.y - .5 * size,
                src: role.subtype == "learner" ? "images/students.png" : "images/teacher.png"
            };

            var image = this.mainSurface.createImage(data);
            var tooltip = i18n.get(isAssessor ? "assessment.edit.assessor" : "assessment.edit.assessee");
            tooltip += "<strong>" + role.getTitle() + "</strong>";
            GFXTooltip.register(image, tooltip);
        }
    }
};

