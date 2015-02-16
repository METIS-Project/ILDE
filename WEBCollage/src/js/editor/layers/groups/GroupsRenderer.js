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

/*global GFXTooltip, LinePainter, DesignInstance, i18n */
var GroupsRenderer = {
    style : {
        linecolor : "black",
        fill : [248, 248, 248, 1.0],
        arrow : [130, 130, 130, 1.0]
    },

    paint : function(flowRenderer, participantFlow) {

        this.linesgroup = flowRenderer.bigGroup.createGroup();
        this.circlelinegroup = flowRenderer.bigGroup.createGroup();
        this.linefillgroup = flowRenderer.bigGroup.createGroup();
        this.circlefillgroup = flowRenderer.bigGroup.createGroup();

        this.participantFlow = participantFlow;

        flowRenderer.animator.addNoAnimStuff(this.linesgroup);
        flowRenderer.animator.addNoAnimStuff(this.circlelinegroup);
        flowRenderer.animator.addNoAnimStuff(this.linefillgroup);
        flowRenderer.animator.addNoAnimStuff(this.circlefillgroup);

        GroupPaintManager.reset();

        this.paintLinks();
        this.paintGroups();
    },
    paintGroups : function(groups, linesgroup, fillgroup) {
        for (var i in this.participantFlow.groups) {
            var group = this.participantFlow.groups[i];
            GroupPaintManager.addGroup(group);
        }
    },
    paintLinks : function() {
        for (var i in this.participantFlow.links) {
            var link = this.participantFlow.links[i];

            this.linesgroup.createLine({
                x1 : link.x1,
                y1 : link.y1,
                x2 : link.x2,
                y2 : link.y2
            }).setStroke({
                color : this.style.linecolor,
                width : 6
            });

            var linefill = this.linefillgroup.createLine({
                x1 : link.x1,
                y1 : link.y1,
                x2 : link.x2,
                y2 : link.y2
            }).setStroke({
                color : this.style.fill,
                width : 4
            });
            var info = this.getTooltipAssignedStudents(link.participants);
            GFXTooltip.register(linefill, info);

            var angle = Math.atan2(link.y2 - link.y1, link.x2 - link.x1);
            var dx = 10 * Math.cos(angle), dy = 10 * Math.sin(angle);
            var points = [{
                x : link.x - dx,
                y : link.y - dy
            }, {
                x : link.x + dx,
                y : link.y + dy
            }];
            LinePainter.paintLine(points, this.linefillgroup, {
                fill : this.style.arrow,
                witdh : 2
            }, false, {
                position : "end",
                length : 2
            });
        }
    },

    /**
     * Muestra en un tooltip los alumnos asignados a la instancia de grupo
     * @param participants Array con los participantes
     */
    getTooltipAssignedStudents : function(participants) {
        var tooltipStudents = [];
        //Se van incluyendo los alumnos que existen con un salto de línea en cada alumno introducido
        for (var i = 0; i < participants.length; i++) {
            tooltipStudents.push("<br>" + DesignInstance.getParticipant(participants[i]).name);
        }
        //Se añade al principio del array la cantidad de alumnos que existen para poder mostrarlo en el campo help del menú
        tooltipStudents.unshift(i18n.get("participants.perteneciente") + " " + tooltipStudents.length);
        return tooltipStudents;
    },
    getGroupMenu : function(data) {
        var items = [];

        for (var i = 0; i < data.participants.length; i++) {
            items.push({
                label : DesignInstance.getParticipant(data.participants[i]).name,
                help : i18n.get("grouplayer.menu.itemhelp"),
                onClick : function(data) {
                    GroupPaintManager.participantClicked(data);
                },
                data : data.participants[i]
            });
        }

        return items;
    }
};

var GroupPaintManager = {

    groups : {},
    participants : {},
    pathOccupiedLinks : {},
    h : Math.random(),

    reset : function() {
        this.groups = {};
        this.participants = {};
        this.pathOccupiedLinks = {};
    },

    addGroup : function(group) {
        var text = group.participants.length;
        var r = 0.5 * text.toString().visualLength() + 5;

        var data = {
            isBig : false,
            mustBeBig : false,
            isStill : true,
            isSelected : false,
            group : group
        };

        data.outcircle = GroupsRenderer.circlelinegroup.createCircle({
            cx : group.x,
            cy : group.y,
            r : r + 1
        });
        data.outcircle.setFill(GroupsRenderer.style.fill).setStroke({
            color : GroupsRenderer.style.linecolor,
            width : 1
        });

        data.ingroup = GroupsRenderer.circlefillgroup.createGroup();
        MenuManager.registerThing(data.ingroup, {
            getItems : function(data) {
                return GroupsRenderer.getGroupMenu(data);
            },
            data : group,
            title : i18n.get("grouplayer.menu.title"),
            menuStyle : "default"
        });

        data.incircle = data.ingroup.createCircle({
            cx : group.x,
            cy : group.y,
            r : r
        });
        data.incircle.setFill(GroupsRenderer.style.fill);

        data.intext = data.ingroup.createText({
            x : group.x,
            y : group.y + 3.5, //Ajustamos la posición vertical del texto al círculo
            text : text,
            align : "middle"
        });
        data.intext.setFont({
            family : "Arial",
            size : "8pt",
            weight : "bold"
        }).setFill("black");

        data.participantgroup = this.paintGroupMembers(data.ingroup, group);

        this.groups[group.key] = data;

        new GroupCircleListener(data.outcircle, data.ingroup, group.key);
    },
    paintGroupMembers : function(surface, group) {
        var pg = surface.createGroup();
        pg.removeShape();
        var l = group.participants.length;
        var c = Math.ceil(Math.sqrt(l));
        var r = Math.ceil(l / c);

        var gap = 9, side = 5;
        for (var i = 0; i < l; i++) {
            var p = group.participants[i];
            if (!this.participants[p]) {
                this.participants[p] = {
                    path : null,
                    rects : []
                };
            }

            var x = group.x + ((i % c) - (c - 1) / 2) * gap;
            var y = group.y + (Math.floor(i / c) - (r - 1) / 2) * gap;

            var rect = pg.createRect({
                x : x - side / 2,
                y : y - side / 2,
                width : side,
                height : side
            }).setStroke("black").setFill("white");
            this.participants[p].rects.push(rect);

            var info = "" + p;
            GFXTooltip.register(rect, info);

            rect.connect("onclick", p, function() {
                GroupPaintManager.participantClicked(this);
            });
        }

        return pg;
    },

    mouseOver : function(key) {
        this.makeBig(key);

    },
    mouseOut : function(key) {
        this.makeSmall(key);
    },

    makeBig : function(key) {
        this.groups[key].mustBeBig = true;
        this.animate(key);
    },

    makeSmall : function(key) {
        this.groups[key].mustBeBig = false;
        this.animate(key);
    },

    pathsForLink : function(link) {
        if (!this.pathOccupiedLinks[link.key]) {
            this.pathOccupiedLinks[link.key] = [];
        }
        return this.pathOccupiedLinks[link.key];
    },
    positionForNewPathInLink : function(link, participant) {
        var pl = this.pathsForLink(link);
        var i;
        for ( i = 0; i < pl.length; i++) {
            if (!pl[i]) {
                break;
            }
        }
        pl[i] = participant;
        return i;
    },
    removePathFromLinks : function(links, participant) {
        for (var i = 0; i < links.length; i++) {
            var pl = this.pathsForLink(links[i]);
            var a = pl.indexOf(participant);
            if (a >= 0) {
                pl[a] = null;
            }
        }
    },
    toggleStudentPath : function(key) {
        if (this.participants[key].path) {
            this.participants[key].path.removeShape();
            this.participants[key].path = null;
            for (var i = 0; i < this.participants[key].rects.length; i++) {
                this.participants[key].rects[i].setFill("white");
            }
            var ls = GroupsRenderer.participantFlow.participants[key].links;
            this.removePathFromLinks(ls, key);
        } else {
            this.participants[key].path = GroupsRenderer.linefillgroup.createGroup();
            var ls = GroupsRenderer.participantFlow.participants[key].links;

            this.h = (this.h + 0.618033988749895) % 1;
            var color = hsvToRgb(this.h * 360, 100, 85);

            for (var i = 0; i < ls.length; i++) {
                var f = ls[i].from;
                var t = ls[i].to;
                var linewidth = 2;
                var line = {
                    x1 : f.x,
                    y1 : f.y,
                    x2 : t.x,
                    y2 : t.y
                };
                var n = this.positionForNewPathInLink(ls[i], key);
                if (n > 0) {
                    var angle = Math.atan2(line.y2 - line.y1, line.x2 - line.x1) + Math.PI * (n % 2 == 0 ? .5 : 1.5);
                    var d = linewidth * Math.floor((n + 1) / 2);
                    var dx = d * Math.cos(angle);
                    var dy = d * Math.sin(angle);
                    line.x1 += dx;
                    line.x2 += dx;
                    line.y1 += dy;
                    line.y2 += dy;
                }
                this.participants[key].path.createLine(line).setStroke({
                    color : color,
                    width : linewidth
                });
            }

            for (var i = 0; i < this.participants[key].rects.length; i++) {
                this.participants[key].rects[i].setFill(color);
            }
        }
    },

    animate : function(key) {
        var data = this.groups[key];

        if (data.isStill && data.isBig != (data.mustBeBig || data.isSelected)) {
            data.isStill = false;

            data.participantgroup.removeShape();
            data.intext.removeShape();

            var scales = data.mustBeBig ? [1, 3] : [3, 1];
            var x = data.outcircle.shape.cx;
            var y = data.outcircle.shape.cy;
            var transform = [{
                name : "scaleAt",
                start : [scales[0], x, y],
                end : [scales[1], x, y]
            }];
            var anims = [];
            anims.push(dojox.gfx.fx.animateTransform({
                shape : data.outcircle,
                duration : 100,
                transform : transform
            }));
            anims.push(dojox.gfx.fx.animateTransform({
                shape : data.incircle,
                duration : 100,
                transform : transform
            }));
            var megaanim = dojo.fx.combine(anims);
            dojo.connect(megaanim, "onEnd", key, function() {
                GroupPaintManager.animationEnded(this);
            });
            megaanim.play();
        }
    },
    animationEnded : function(key) {
        var data = this.groups[key];
        data.isBig = !data.isBig;
        data.ingroup.add(data.isBig ? data.participantgroup : data.intext);
        data.isStill = true;
        this.animate(key);
    },

    groupClicked : function(key) {
    },

    participantClicked : function(participant) {
        this.toggleStudentPath("" + participant);
    }
};

GroupCircleListener = function(outside, ingroup, key) {
    this.outside = outside;
    this.ingroup = ingroup;
    this.key = key;

    this.ingroup.connect("onmouseout", this, "leave");
    this.ingroup.connect("onmouseover", this, "enter");
    this.outside.connect("onmouseout", this, "leave");
    this.outside.connect("onmouseover", this, "enter");

    this.ingroup.connect("onclick", this, function() {
        GroupPaintManager.groupClicked(this.key);
    });
}

GroupCircleListener.prototype.enter = function(event) {
    if (this.externalElement(event.fromElement)) {
        GroupPaintManager.mouseOver(this.key);
    }
}

GroupCircleListener.prototype.leave = function(event) {
    if (this.externalElement(event.toElement)) {
        GroupPaintManager.mouseOut(this.key);
    }
};

GroupCircleListener.prototype.externalElement = function(element) {
    if (element == this.outside.rawNode) {
        return false;
    } else {
        while (element) {
            if (element == this.ingroup.rawNode) {
                return false;
            } else {
                element = element.parentElement;
            }
        }
    }
    return true;
};
