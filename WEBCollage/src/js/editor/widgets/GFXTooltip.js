

var GFXTooltip = {

    tooltip: null,
    content: null,
    anim: null,

    init: function() {
        this.tooltip = dojo.byId("GFXTooltip");
        this.content = dojo.byId("GFXTooltipContent");
    },

    register: function(shape, text) {
        new GFXTooltipListener(shape, text);
    },

    show: function(pos, text) {
        this.content.innerHTML = text;
        dojo.style(this.tooltip, "visibility", "visible");
        dojo.style(this.tooltip, {
            left: pos.left + "px",
            //top: (pos.top - this.tooltip.offsetHeight + 7) + "px"
            top: pos.top + "px"
        });

        if (this.anim) {
            this.anim.stop();
        }
        this.anim = dojo.fadeIn({
            node: this.tooltip,
            onEnd: function() {
                GFXTooltip.anim = null;
            }
        });
        this.anim.play();
    },

    hide: function() {
        if (this.anim) {
            this.anim.stop();
        }

        this.anim = dojo.fadeOut({
            node: this.tooltip,
            onEnd: function() {
                dojo.style(GFXTooltip.tooltip, "visibility", "hidden");
                GFXTooltip.anim = null;
            }
        });
        this.anim.play();
    }
};

function GFXTooltipListener(shape, text) {
    this.text = text;
    this.shape = shape;
    shape.connect("onmouseover", this, "entered");
    shape.connect("onmouseout", this, "exited");
}

GFXTooltipListener.prototype.entered = function() {
    var pos = this.getPosition();
    GFXTooltip.show(pos, this.text);
};

GFXTooltipListener.prototype.exited = function() {
    GFXTooltip.hide();
};


GFXTooltipListener.prototype.getPosition = function(){
    var nodePos = dojo.coords(this.getDOMParent());
    var box = this.shape.getTransformedBoundingBox();
    return {
        left: nodePos.x + box[2].x + 10,
        top: nodePos.y + box[0].y + .5 * (box[2].y - box[0].y)
    };
/*
    return {
        x: nodePos.x + box[0].x,
        y: nodePos.y + box[0].y,
        width: box[2].x - box[0].x,
        height: box[2].y - box[0].y
    };*/
};

GFXTooltipListener.prototype.getDOMParent = function(node){
    if (!node) {
        node = this.shape;
    }

    if (node._parent) {
        return node._parent;
    } else {
        return this.getDOMParent(node.parent);
    }
};

