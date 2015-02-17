/*
Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Glue!PS.

Glue!PS is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Glue!PS is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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
            top: (pos.top - this.tooltip.offsetHeight + 7) + "px"
            //top: pos.top + "px"
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
        left: nodePos.x + box[2].x,
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

