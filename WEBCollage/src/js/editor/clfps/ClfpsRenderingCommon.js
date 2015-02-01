/*global dojox, PersistentImageSourceListener, FillListener, IDPool, ActivitiesPainter*/
var ClfpsRenderingCommon = {
    trailingGroupText : {
        font : {
            family : "Arial",
            size : "9pt",
            weight : "bold"
        },
        fill : [10, 10, 10, 1.0],
        emphasisFill : [100, 100, 100, 1.0]
    },
    trailingGroupLineStroke : {
        color : "black",
        width : 2,
        cap : "butt",
        join : 2
    },

    createActPaintData : function(clfpgroup, rect, actid, uiListener) {
        var actpaintdata = {
            group : clfpgroup.createGroup().setTransform([dojox.gfx.matrix.translate(rect.pre.x, rect.pre.y)]),
            activitiesgroup : null,
            trailinggroup : clfpgroup.createGroup().setTransform([dojox.gfx.matrix.translate(rect.post.x, rect.post.y)]),
            roles : {}
        };

        actpaintdata.group.createRect({
            x : 0,
            y : 0,
            width : rect.pre.width,
            height : rect.pre.height
        }).setFill([0, 0, 0, 0]);

        return actpaintdata;
    },
    addRoleInstanceData : function(actpaintdata, roleid, instanceid, shape) {
        if(!actpaintdata.roles[roleid]) {
            actpaintdata.roles[roleid] = {
                bbox : null,
                instances : {}
            };
        }

        var newrect = shape.getBoundingBox();

        if(!actpaintdata.roles[roleid].instances[instanceid]) {
            actpaintdata.roles[roleid].instances[instanceid] = newrect;
        } else {
            actpaintdata.roles[roleid].instances[instanceid] = this.includeRectInRect(actpaintdata.roles[roleid].instances[instanceid], newrect);
        }

        if(!actpaintdata.roles[roleid].bbox) {
            actpaintdata.roles[roleid].bbox = actpaintdata.roles[roleid].instances[instanceid];
        } else {
            actpaintdata.roles[roleid].bbox = this.includeRectInRect(actpaintdata.roles[roleid].bbox, actpaintdata.roles[roleid].instances[instanceid]);
        }
    },
    addActButton : function(actid, group, actrect, uiListener) {
        var button = group.createImage({
            x : actrect.pre.width / 2 - 12,
            y : actrect.pre.height - 12,
            width : 24,
            height : 24,
            src : "images/icons/arrowBigBlank.png"
        });
        var grey = IDPool.getObject(actid).type == "act" ? (uiListener.displayInfo.openActIds[actid] ? "images/icons/arrowBigUpGrey.png" : "images/icons/arrowBigDownGrey.png") : ("images/icons/arrowBigRightGrey.png");
        var blue = IDPool.getObject(actid).type == "act" ? (uiListener.displayInfo.openActIds[actid] ? "images/icons/arrowBigUp.png" : "images/icons/arrowBigDown.png") : ("images/icons/arrowBigRight.png");
        var listener = new PersistentImageSourceListener(group, actid, "images/icons/arrowBigBlank.png", grey, button, blue);
        return button;
    },
    getOpenActSize : function(actid, uiListener) {
        var act = IDPool.getObject(actid);
        return (act.type == "act" && uiListener.displayInfo.openActIds[actid]) ? {
            width : 50,
            height : ActivitiesPainter.actHeight(act)
        } : {
            width : 0,
            height : 0
        };
    },
    getAllOpenActsSize : function(clfp, uiListener) {
        var size = {
            width : 0,
            height : 0
        };

        for(var i = 0; i < clfp.getFlow().length; i++) {
            var actsize = this.getOpenActSize(clfp.getFlow()[i].id, uiListener);
            size.width = Math.max(size.width, actsize.width);
            size.height += actsize.height;
        }

        return size;
    },
    includeRectInRect : function(bigrect, smallrect) {
        var maxx = Math.max(bigrect.x + bigrect.width, smallrect.x + smallrect.width);
        var maxy = Math.max(bigrect.y + bigrect.height, smallrect.y + smallrect.height);

        var minx = Math.min(bigrect.x, smallrect.x);
        var miny = Math.min(bigrect.y, smallrect.y);

        return {
            x : minx,
            y : miny,
            width : maxx - minx,
            height : maxy - miny
        };
    },
    createTrailingGroupArt : function(trailinggroup, x1, x2, y, act, allowChangeTitle, align, textsize) {
        trailinggroup.createLine({
            x1 : x1,
            y1 : y,
            x2 : x2,
            y2 : y
        }).setStroke(this.trailingGroupLineStroke);

        var textShape = trailinggroup.createText({
            width : 100,
            height : 10,
            x : x1 + 5,
            y : y - 5,
            text : act.getTitle()
        }).setFont(this.trailingGroupText.font).setFill(this.trailingGroupText.fill);
        new FillListener(textShape, this.trailingGroupText.fill, this.trailingGroupText.emphasisFill);
        if (allowChangeTitle) {
            RenameElementDialog.registerElement(textShape, act);
        }
    }
};
