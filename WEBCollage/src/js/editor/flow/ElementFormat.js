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
var ElementFormat = {

    idPrefix : 0,

    formatAssessmentFunctionElement : function(where, assessmentFunction, how) {
        var elements = {};
        var container = where.appendChild(document.createElement("div"));
        dojo.query(container).addClass("functionContainer");

        var first = container.appendChild(document.createElement("div"));
        first.innerHTML = assessmentFunction.getTitle();

        var second = container.appendChild(document.createElement("div"));

        if (assessmentFunction.subtype == "summative") {
            if (assessmentFunction.name) {
                elements = ElementFormat.formatSummativeElement(second, assessmentFunction.name, {
                    small : true
                });
            } else {
                elements = ElementFormat.formatEmptyElement(second, i18n.get("assessment.edit.summative.name.empty"), {
                    small : true
                });
            }
        } else {
            var linkedActivity = IDPool.getObject(assessmentFunction.link);
            if (linkedActivity && linkedActivity.type == "activity") {
                elements = ElementFormat.formatElement(second,
                    linkedActivity, {
                        small : true,
                        activityTooltip : true
                    });
            } else {
                elements = ElementFormat.formatEmptyElement(second, i18n
                    .get("assessment.link.choose"), {
                        small : true
                    });
            }
        }

        elements.container = container;

        this.position(container, how);
        return elements;
    },

    position : function(element, how) {
        if (how.centerAt) {
            var left = Math.round(how.centerAt.x - .5 * element.offsetWidth);
            var top = how.topY ? how.centerAt.y : Math.round(how.centerAt.y - .5 * element.offsetHeight);

            dojo.style(element, {
                "position" : "absolute",
                "left" : left + "px",
                "top" : top + "px"
            });
        }
    },

    formatGap : function(where, how) {
        var elements = {};
        elements.main = where.appendChild(document.createElement("div"));
        dojo.query(elements.main).addClass("element");
        dojo.query(elements.main).addClass("elementGap");

        elements.main.innerHTML = i18n.get("flow.activities.gap");
        this.position(elements.main, how);
        return elements;
    },

    formatEmptyElement : function(where, text, how) {
        var elements = {};
        elements.main = where.appendChild(document.createElement("div"));
        dojo.query(elements.main).addClass("element");
        dojo.query(elements.main).addClass("elementEmpty");

        elements.main.innerHTML = text;
        this.position(elements.main, how);
        return elements;
    },

    formatSummativeElement: function(where, text, how) {
        var elements = {};
        elements.main = where.appendChild(document.createElement("div"));
        dojo.query(elements.main).addClass("element");
        dojo.query(elements.main).addClass("elementSummative");

        elements.main.innerHTML = text;
        this.position(elements.main, how);
        return elements;
    },

    formatElement : function(where, object, how) {
        var elements = {};
        elements.main = where.appendChild(document.createElement("div"));

        if (object.type == "assessmentflow") {
            var typeClass = "assessmentElement";
        } else if (object.type == "role") {
            typeClass = object.subtype == "learner" ? "learnerElement"
            : "staffElement";
        } else if (object.type == "activity") {
            typeClass = object.subtype == "learning" ? "learningElement"
            : "supportElement";

            elements.mainAssessmentId = null;

            if (how.assessment) {
                elements.assessments = new Array();
                var assessmentLinks = AssessmentManager
                .getAssessmentsFor(object);
                for ( var i in assessmentLinks) {
                    elements.assessments.push(assessmentLinks[i]);

                    if (assessmentLinks[i].rel == "assessment") {
                        typeClass = "assessmentElement";
                        elements.mainAssessmentId = assessmentLinks[i].assessment.id;
                    }
                }
            }
        }

        var lookForId = object.id;
        if (object.type == "assessmentflow"
            && object.getAssessmentActivity()) {
            lookForId = object.getAssessmentActivity().id;
        }

        elements.main.id = (this.idPrefix++) + "_" + lookForId;

        if (how.activityTooltip) {
            var location = LearningDesign
            .findActParentOfActivity(object.id);
            if (location != null) {
                var label = dojo.string.substitute(i18n
                    .get("activity.shortDescription"), {
                        act : location.act.title,
                        clfp : location.clfp.title,
                        role : IDPool.getObject(location.roleId).title
                    });
                new dijit.Tooltip( {
                    label : label,
                    connectId : [ elements.main.id ],
                    showDelay : 100
                });
            }
        }

        dojo.query(elements.main).addClass("element");
        dojo.query(elements.main).addClass(typeClass);
        if (how.empty) {
            dojo.query(elements.main).addClass("elementEmpty");
        }
        var head = elements.main;//.appendChild(document.createElement("div"));
        dojo.query(head).addClass("elementHead");

        this.addTitle(head, object, typeClass == "assessmentElement");

        if (how.small) {
            dojo.query(elements.main).addClass("elementSmall");
        } else {
        /*
         * var data =
         * elements.main.appendChild(document.createElement("div"));
         * dojo.query(data).addClass("elementData");
         *
         * this.addButton(head, data); this.addDescription(data, object);
         */
        }

        if (how.classes) {
            dojo.query(elements.main).addClass(how.classes);
        }
        
        this.position(elements.main, how);

        return elements;
    },

    addTitle : function(container, object, assessmentTask) {
        var element = container.appendChild(document.createElement("span"));
        dojo.query(element).addClass("elementTitle");
        if (object.type == "role") {
            var img = element.appendChild(document.createElement("img"));
            dojo.addClass(img, "elementRoleIcon");
            dojo.attr(img, "src", object.subtype == "learner" ? "images/students.png" : "images/teacher.png");
        }
        if (assessmentTask) {
            element.appendChild(document.createTextNode(i18n.get("assessment.task")));
            element.appendChild(document.createElement("br"));
        }

        element.appendChild(document.createTextNode(object.getTitle()));
    },

    addButton : function(container, extra) {
        var element = container.appendChild(document.createElement("div"));
        dojo.query(element).addClass("elementButton");
        new ElementButtonListener(element, extra);
    },

    addDescription : function(container, object) {
        var element = container.appendChild(document.createElement("div"));
        dojo.query(element).addClass("elementDescription");
        element.innerHTML = object.description;
    }

};

function ElementButtonListener(button, data) {
    this.button = button;
    this.data = data;
    this.show = false;

    dojo.query(this.data).style( {
        "visibility" : "hidden"
    });

    this.button.innerHTML = "+";

    dojo.connect(button, "onclick", this, "toggle");
}

ElementButtonListener.prototype.toggle = function() {
    this.show = !this.show;

    if (this.show) {
        dojo.fadeIn(this.data);
    } else {
        dojo.fadeOut(this.data);
    }

    this.button.innerHTML = this.show ? "-" : "+";
};
