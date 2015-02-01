/**
 * @author Eloy
 */
var ActFlowVars = {
    mainLineColors : {
        border : "#70707080",
        fill : "#f0f0ff",
        width : 12
    },

    minSectionWidth : 250,
    deltaY : 30
};

var ActFlowPainter = function(rolesNode, activitiesNode, callback) {
    this.activitiesNode = activitiesNode;
    this.arrowPainter = new ArrowPainter(activitiesNode, dojox.gfx
        .createSurface(activitiesNode, 1000, 1000));
    this.rolesNode = rolesNode;
    this.deltaX = 0;
    this.callback = callback;
};

ActFlowPainter.prototype.clear = function() {
    this.arrowPainter.clear();
    dojo.query("#" + this.activitiesNode.id + " > div").orphan();
    dojo.query("#" + this.rolesNode.id + " > div").orphan();
};

ActFlowPainter.prototype.paint = function(act, what) {
    this.clear();
    var elements = {};
    this.buildRoleList(act, elements);
    this.buildAllActivityList(act, what, elements);
    return elements;
};

ActFlowPainter.prototype.buildAllActivityList = function(act, what, elements) {
    var assessmentLinks = new Array();
    var activityElements = new Array();

    for ( var i = 0; i < act.learners.length; i++) {
        this.buildActivityList(act, act.learners[i], i, assessmentLinks,
            activityElements, what);
    }

    for ( var j = 0; j < act.staff.length; i++, j++) {
        this.buildActivityList(act, act.staff[j], i, assessmentLinks,
            activityElements, what);
    }

    if (what.paintAssessment) {
        this.addAssessmentElements(assessmentLinks, activityElements);
    }

    for ( i in activityElements) {
        elements[i] = activityElements[i];
    }
};

ActFlowPainter.prototype.buildRoleList = function(act) {
    var container = this.rolesNode;

    var containerWidth = container.offsetWidth;

    this.nSections = act.learners.length + act.staff.length;
    this.sectionWidth = this.nSections > 0 ? Math.max(
        ActFlowVars.minSectionWidth, containerWidth / this.nSections)
    : ActFlowVars.minSectionWidth;

    containerWidth = Math.max(containerWidth, this.nSections
        * this.sectionWidth);
    this.deltaX = .5 * (containerWidth - this.nSections * this.sectionWidth);

    dojo.query(container).style( {
        width : containerWidth + "px"
    });
    dojo.query(this.activitiesNode).style( {
        width : containerWidth + "px"
    });

    for ( var i = 0; i < act.learners.length; i++) {
        //Se añaden los roles de tipo learner
        this.addRole(act.learners[i], i);
    }

    for ( var j = 0; j < act.staff.length; i++, j++) {
        //Se añaden los roles de tipo staff
        this.addRole(act.staff[j], i);
    }

    this.arrowPainter.surface.createLine({
        x1: 15,
        y1: 17,
        x2: containerWidth - 15,
        y2: 17
    }).setStroke({
        color: "gray",
        width: 1,
        style: "dash"
    });
};

ActFlowPainter.prototype.addRole = function(roleid, position) {
    var container = this.rolesNode;
    var center = {
        x : this.deltaX + (position + 0.5) * this.sectionWidth,
        y : 30
    };
    var role = IDPool.getObject(roleid);
    var elements = ElementFormat.formatElement(container, role, {
        centerAt : center
    });

    TooltipFormat.addTooltip(elements.main, role);

    if (this.callback) {
        this.callback(elements.main, roleid, {
            roleid : roleid,
            isRole : true
        });
    }
};

ActFlowPainter.prototype.addGap = function(container, actid, roleid, index,
    center, activityElements) {
    var elements = ElementFormat.formatGap(container, {
        centerAt : center,
        topY: true
    });
    
    this.callback(elements.main, actid, {
        index : index,
        id : actid,
        roleid : roleid,
        isGap : true
    });
    elements.elementid = actid + "_" + roleid + "_" + index;
    activityElements[elements.elementid] = elements;
    center.y += elements.main.offsetHeight + 10;
};

ActFlowPainter.prototype.buildActivityList = function(act, roleid, position,
    assessmentLinks, activityElements, what) {

    var container = this.activitiesNode;
    var list = act.getActivitiesForRoleId(roleid);

    var center = {
        x : this.deltaX + (position + 0.5) * this.sectionWidth,
        y : ActFlowVars.deltaY
    };

    if (what.paintGaps) {
        this.addGap(container, act.id, roleid, 0, center, activityElements);
    }

    for ( var i = 0; i < list.length; i++) {
        var elements = ElementFormat.formatElement(container, list[i], {
            assessment : true,
            centerAt : center,
            topY: true
        });

        TooltipFormat.addTooltip(elements.main, list[i]);

        elements.position = position;
        elements.elementid = list[i].id;
        activityElements[elements.elementid] = elements;

        var linkedIds = elements.mainAssessmentId == null ? [list[i].id] : [list[i].id, elements.mainAssessmentId];

        if (what.paintAlerts) {
            ClipRenderer.paint(this.arrowPainter.surface, null, false, linkedIds, {
                x: center.x - 80,
                y: center.y
            });
        }

        center.y += elements.main.offsetHeight + 10;

        if (what.paintGaps) {
            this.addGap(container, act.id, roleid, i + 1, center,
                activityElements);
        } else {
            this.callback(elements.main, linkedIds[0], {
                roleid : roleid,
                index : i,
                ids: linkedIds
            });
        }

        for ( var j = 0; j < elements.assessments.length; j++) {
            assessmentLinks.push(elements.assessments[j]);
        }
    }

    if (center.y + 30 > container.offsetHeight) {
        dojo.style(container, {
            height : (30 + center.y) + "px"
        });
    }

    this.paintFlowLine(center.x, center.y + 15);
};

ActFlowPainter.prototype.addAssessmentElements = function(assessmentLinks,
    activityElements) {
    var container = this.activitiesNode;

    var ordered = {};
    for ( var i in assessmentLinks) {
        var link = assessmentLinks[i];
        if (!ordered[link.assessment.id]) {
            ordered[link.assessment.id] = {
                mainLink : null,
                mainRel : null,
                all : new Array()
            };
        }

        ordered[link.assessment.id].all.push(link);
    }

    /* ordered: one item per different assessment */

    var elementCount = {};

    for (var assessmentId in ordered) {
        var linksOfAssessment = ordered[assessmentId];

        for (i in linksOfAssessment.all) {
            link = linksOfAssessment.all[i];
            if (AssessmentManager.weightOfRel(link.rel) > AssessmentManager
                .weightOfRel(linksOfAssessment.mainRel)) {
                linksOfAssessment.mainLink = link;
                linksOfAssessment.mainRel = link.rel;
            }
        }

        var assessmentIsAnActivity = false;

        if (linksOfAssessment.mainRel != "assessment") {
            var activityElement = activityElements[linksOfAssessment.mainLink.activityId];
            var gap = (linksOfAssessment.mainRel == "assessed" ? 0.5 : -0.5);

            var pos = activityElement.position + "_" + gap;
            var step = 0;
            if (elementCount[pos]) {
                step = elementCount[pos]++;
            } else {
                elementCount[pos] = 1;
            }

            var center = {
                x : Math.round(this.deltaX
                    + (activityElement.position + 0.5 + gap)
                    * this.sectionWidth + 4 * step),
                y : Math.round(activityElement.main.offsetTop
                    + activityElement.main.offsetHeight / 2 + 10 * step)
            };

            var assessment = linksOfAssessment.mainLink.assessment;

            var assessmentElement = ElementFormat.formatElement(container,
                assessment, {
                    empty : !assessment.isDefined(),
                    centerAt : center,
                    small : true,
                    activityTooltip : true
                });

            this.callback(assessmentElement.main, assessment.id, {
                ids : [assessment.id],
                activityIsExternal: true
            });

        } else {
            assessmentElement = activityElements[linksOfAssessment.mainLink.activityId];
            assessmentIsAnActivity = true;
        }

        for (i in linksOfAssessment.all) {
            link = linksOfAssessment.all[i];
            if (link.rel != "assessment") {
                var nodeA = link.rel == "assessed" ? assessmentElement
                : activityElements[link.activityId];
                var nodeB = link.rel == "assessed" ? activityElements[link.activityId]
                : assessmentElement;
                if (assessmentIsAnActivity && nodeA.position == nodeB.position) {
                    this.paintCurvedLink(nodeA.main, nodeB.main);
                } else {
                    this.paintStraightLink(nodeA.main, nodeB.main);
                }
            }
        }
    }
};

/* paint ************ */

ActFlowPainter.prototype.paintStraightLink = function(nodeA, nodeB) {
    this.arrowPainter.paintStraightArrow(nodeA, nodeB,
        AssessmentLineRenderer.assessmentLineColors["assessed"]);
};

ActFlowPainter.prototype.paintCurvedLink = function(nodeA, nodeB) {
    this.arrowPainter.paintCurvedRightArrow(nodeA, nodeB,
        AssessmentLineRenderer.assessmentLineColors["assessed"]);
};

ActFlowPainter.prototype.paintFlowLine = function(x, y) {
    var points = [ {
        x : x,
        y : 20
    }, {
        x : x,
        y : y
    } ];
    LinePainter.paintLine(points, this.arrowPainter.surface,
        ActFlowVars.mainLineColors, false, {
            position : "end",
            length : 15
        }, false);
};
