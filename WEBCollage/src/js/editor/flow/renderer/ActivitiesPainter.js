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

var ActivitiesPainter = {

    topPadding : 10,
    bPadding : 10,
    vPadding : 20,
    activityHeight : 40,
    extraWidth : 20,
    activityGap : 10,
    activityStyle : {
        fill : [200, 200, 240, 1.0],
        emphasisFill : [220, 220, 255, 1.0],
        stroke : {
            color : "black",
            width : 1,
            cap : "butt",
            join : 1
        }
    },
    supportActivityStyle : {
        fill : [200, 240, 200, 1.0],
        emphasisFill : [220, 255, 220, 1.0],
        stroke : {
            color : "black",
            width : 1,
            cap : "butt",
            join : 1
        }
    },
    arrowColors : {
        border : "black",
        fill : "#fff0f0",
        width : 5,
        emphasis : {
            border : "black",
            fill : "#efe0e0",
            width : 7
        }
    },
    bgStyle : {
        fill : "white",
        stroke : {
            color : "gray",
            style : "dot",
            width : 1
        }
    },
    text : {
        font : {
            family : "Arial",
            size : "7pt"
        },
        fill : [10, 10, 10, 1.0],
        emphasisFill : [100, 100, 100, 1.0]
    },

    groupImage : {
        src : "images/students.png",
        size : 15
    },
    postitImagesrc : "images/postit.svg",

    actHeight : function(act) {
        var max = 0;
        for(var i = 0; i < act.roleparts.length; i++) {
            max = Math.max(max, act.roleparts[i].activities.length);
        }
        return this.topPadding + 2 * this.vPadding + max * this.activityHeight + Math.max(0, max - 1) * this.activityGap;
    },
    paintActs : function(clfp, paintdata, uilistener, animator, activeLinks) {
        for(var i = 0; i < clfp.getFlow().length; i++) {
            var act = clfp.getFlow()[i];

            if(act.type == "act" && uilistener.displayInfo.openActIds[act.id]) {
                var height = this.actHeight(act);
                var actsize = paintdata.size.acts[act.id];
                var actpaintdata = paintdata.acts[act.id];
                actpaintdata.activitiesgroup = paintdata.group.createGroup().setTransform([dojox.gfx.matrix.translate(actsize.pre.x, actsize.pre.y + actsize.pre.height)]);
                animator.addNoAnimStuff(actpaintdata.activitiesgroup);
                //actpaintdata.activitiesgroup.moveToBack();

                this.paintActActivities(act, actpaintdata, height, uilistener, activeLinks);
                this.paintBackground(paintdata.size.width, height, actpaintdata);
                //				this.paintPostitBackgound(paintdata.size.width, height, actpaintdata);
            }
        }
    },
    paintActActivities : function(act, actpaintdata, height, uilistener, activeLinks) {
        actpaintdata.activitiesxs = {
            minx : 100000,
            maxx : -10000
        };
        actpaintdata.activitiessize = {};

        for(var i = 0; i < act.roleparts.length; i++) {
            var rolepart = act.roleparts[i];
            var rolepaintdata = actpaintdata.roles[rolepart.roleId];
            var bbox = rolepaintdata.bbox;

            var minx = bbox.x + bbox.width;
            var maxx = bbox.x;

            for(var iid in rolepaintdata.instances) {
                var acx = rolepaintdata.instances[iid].x + .5 * rolepaintdata.instances[iid].width;
                if(acx < minx) {
                    minx = acx;
                }

                if(acx > maxx) {
                    maxx = acx;
                }

                var line = LinePainter.paintLine([{
                    x : acx,
                    y : this.topPadding
                }, {
                    x : acx,
                    y : height
                }], actpaintdata.activitiesgroup, this.arrowColors, false, {
                    position : "end",
                    length : 5
                }, false);

                if(rolepart.activities.length == 0) {// && activeLinks) {
                    MenuManager.registerThing(line, {
                        getItems : function(data) {
                            return ActivitiesPainter.createMenuWithNoActivities(data);
                        },
                        data : {
                            actid : act.id,
                            roleid : rolepart.roleId
                        },
                        followCursor : true
                    });
                }
            }

            if(minx < actpaintdata.activitiesxs.minx) {
                actpaintdata.activitiesxs.minx = minx;
            }
            if(maxx > actpaintdata.activitiesxs.maxx) {
                actpaintdata.activitiesxs.maxx = maxx;
            }

            var y = this.topPadding + this.vPadding;

            for(var j = 0; j < rolepart.activities.length; j++, y += this.activityHeight + this.activityGap) {
                var activity = rolepart.activities[j];
                actpaintdata.activitiessize[activity.id] = {
                    rect : {
                        x : minx - this.extraWidth,
                        y : y,
                        width : maxx - minx + 2 * this.extraWidth,
                        height : this.activityHeight
                    },
                    instances : {}
                };
                var activitygroup = actpaintdata.activitiesgroup.createGroup();
                var style = activity.subtype == "learning" ? this.activityStyle : this.supportActivityStyle;
                var shape = activitygroup.createRect(actpaintdata.activitiessize[activity.id].rect).setFill(style.fill).setStroke(style.stroke);

                var textshape = activitygroup.createText({
                    x : actpaintdata.activitiessize[activity.id].rect.x + 5,
                    y : actpaintdata.activitiessize[activity.id].rect.y + actpaintdata.activitiessize[activity.id].rect.height - 4,
                    text : activity.title
                }).setFont(this.text.font).setFill(this.text.fill);
                new FillListener(textshape, this.text.fill, this.text.emphasisFill);
                if(activeLinks) {
                    RenameElementDialog.registerElement(textshape, activity);
                }
                new FillListener(shape, style.fill, style.emphasisFill);

                if(activeLinks) {
                    uilistener.registerActiveElement(shape, {
                        actid : act.id,
                        roleid : rolepart.roleId,
                        index : j,
                        id : activity.id,
                        activitycount : rolepart.activities.length
                    });
                }
                TooltipFormat.addTooltip(shape, activity);

                //Almacenar y pintar las posiciones de las instancias en la actividad
                for(var aiid in rolepaintdata.instances) {
                    var pos = {
                        x : rolepaintdata.instances[aiid].x + .5 * (rolepaintdata.instances[aiid].width - this.groupImage.size),
                        y : y + .5 * (this.activityHeight - this.groupImage.size) - 3,
                        width : this.groupImage.size,
                        height : this.groupImage.size
                    };
                    actpaintdata.activitiessize[activity.id].instances[aiid] = pos;

                    actpaintdata.activitiesgroup.createImage({
                        x : pos.x,
                        y : pos.y,
                        width : pos.width,
                        height : pos.height,
                        src : this.groupImage.src
                    });
                }
            }
        }
    },
    paintBackground : function(width, height, actpaintdata) {
        var x0 = actpaintdata.activitiesxs.minx - this.extraWidth - this.bPadding;
        var x1 = actpaintdata.activitiesxs.maxx + this.extraWidth + this.bPadding;

        var cx = Math.min(x1 - 20, Math.max(x0 + 20, width / 2));

        var points = LinePainter.transformLine([{
            x : cx,
            y : this.topPadding + this.bPadding / 2
        }, {
            x : cx,
            y : this.topPadding + this.bPadding

        }, {
            x : x0,
            y : this.topPadding + this.bPadding
        }, {
            x : x0,
            y : height - this.bPadding
        }, {
            x : x1,
            y : height - this.bPadding
        }, {
            x : x1,
            y : this.topPadding + this.bPadding
        }, {
            x : cx,
            y : this.topPadding + this.bPadding
        }, {
            x : cx,
            y : this.topPadding + this.bPadding / 2
        }], 10, 1);

        actpaintdata.activitiesgroup.createPolyline(points).setStroke("black").setFill("white").moveToBack();
        actpaintdata.activitiesgroup.createPolyline(points).setFill([0, 0, 0, .4]).setTransform([dojox.gfx.matrix.translate(-5, 3)]).moveToBack();
    },
    createMenu : function(data) {
        var items = new Array();

        var isLearner = IDPool.getObject(data.roleid).subtype == "learner";

        items.push({
            label : i18n.get("activity.edit"),
            icon : isLearner ? "editlearningactivity" : "editsupportactivity",
            onClick : function(data) {
                ActivitiesPainter.editActivity(data.actid, data.roleid, data.index);
                /* before */
            },
            data : data,
            help : i18n.get("help.activity.edit")
        });

        items.push({
            isSeparator : true
        });

        items.push({
            icon : isLearner ? "addlearningactivity" : "addsupportactivity",
            label : i18n.get("flow.activities.add.relative")
        });

        items.push({
            label : i18n.get("flow.activities.add.before"),
            onClick : function(data) {
                ActivitiesPainter.createNewActivity(data.actid, data.roleid, data.index);
                /* before */
            },
            data : data,
            isSubMenu : true,
            help : i18n.get("help.flow.activities.add.before")
        });

        items.push({
            label : i18n.get("flow.activities.add.after"),
            onClick : function(data) {
                ActivitiesPainter.createNewActivity(data.actid, data.roleid, data.index);
                /* after */
            },
            data : {
                index : data.index + 1,
                roleid : data.roleid,
                actid : data.actid
            },
            isSubMenu : true,
            help : i18n.get("help.flow.activities.add.after")
        });

        /* assessment */
        items.push({
            isSeparator : true
        });

        /*items.push({
            label : i18n.get("flow.activities.assess"),
            icon : "assessactivity",
            onClick : function(data) {
                //ActivitiesPainter.assessActivity(data.actid, data.roleid, data.index);
            },
            data : data,
            help : i18n.get("help.flow.activities.assess")
        });*/
        items.push({
            label : i18n.get("flow.activities.makeassessor"),
            icon : "assessactivity",
            onClick : function(data) {
                ActivitiesPainter.makeAssessorsActivity(data.actid, data.roleid, data.index);
            },
            data : data,
            help : i18n.get("help.flow.activities.makeassessor")
        });
        /* other */

        if(data.index > 0 || data.index < data.activitycount - 1) {
            items.push({
                isSeparator : true
            });
        }
        if(data.index > 0) {
            items.push({
                label : i18n.get("flow.activities.move.up"),
                icon : "up",
                onClick : function() {
                    ActivitiesPainter.moveUp(data.actid, data.roleid, data.index);
                },
                data : data,
                help : i18n.get("help.flow.activities.move.up")
            });
        }
        if(data.index < data.activitycount - 1) {
            items.push({
                label : i18n.get("flow.activities.move.down"),
                icon : "down",
                onClick : function() {
                    ActivitiesPainter.moveDown(data.actid, data.roleid, data.index);
                },
                data : data,
                help : i18n.get("help.flow.activities.move.down")
            });
        }

        items.push({
            isSeparator : true
        });

        items.push({
            label : i18n.get("flow.activities.delete"),
            icon : "delete",
            onClick : function() {
                ActivitiesPainter.remove(data.actid, data.roleid, data.index);
            },
            data : data,
            help : i18n.get("help.flow.activities.delete")
        });
        return items;
    },
    getActivity : function(actid, roleid, index) {
        var activities = IDPool.getObject(actid).getActivitiesForRoleId(roleid);
        var activity = activities[index];
        return activity;
    },
    editActivity : function(actid, roleid, index) {
        var activity = this.getActivity(actid, roleid, index);
        EditActivityDialog.open(activity);
    },
    createMenuWithNoActivities : function(data) {
        var items = new Array();
        items.push({
            label : i18n.get("flow.activities.add"),
            onClick : function(data) {
                ActivitiesPainter.createNewActivity(data.actid, data.roleid, 0);
                /* after */
            },
            data : {
                index : data.index + 1,
                roleid : data.roleid,
                actid : data.actid
            },
            help : i18n.get("help.flow.activities.add")
        });
        return items;
    },
    createNewActivity : function(actid, roleid, position) {
        LearningDesign.createActivity(actid, roleid, position, i18n.get("flow.activities.added.new"));
    },
    moveUp : function(actid, roleid, index) {
        var act = IDPool.getObject(actid);
        var activities = act.getActivitiesForRoleId(roleid);
        var tmp = activities[index];
        activities[index] = activities[index - 1];
        activities[index - 1] = tmp;
        ChangeManager.actEdited(act);
    },
    moveDown : function(actid, roleid, index) {
        var act = IDPool.getObject(actid);
        var activities = IDPool.getObject(actid).getActivitiesForRoleId(roleid);
        var tmp = activities[index];
        activities[index] = activities[index + 1];
        activities[index + 1] = tmp;
        ChangeManager.actEdited(act);
    },
    remove : function(actid, roleid, index) {
        var activities = IDPool.getObject(actid).getActivitiesForRoleId(roleid);
        var activity = activities.splice(index, 1);
        ChangeManager.activityDeleted(activity);
    },
    assessActivity : function(actid, roleid, index) {
        var activity = this.getActivity(actid, roleid, index);
        AssessmentManager.createAssessment(activity);
    },
    makeAssessorsActivity : function(actid, roleid, index) {
        var activity = this.getActivity(actid, roleid, index);
        AssessmentManager.createAssessment(activity);
    }
};
