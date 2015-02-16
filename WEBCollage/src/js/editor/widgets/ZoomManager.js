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

var ZoomManagerOptions = {
    centerGap: 1,
    max: 10
};

function ZoomManager(sliderId, valueId, wheelNodeId, zoomListener) {
    this.slider = dijit.byId(sliderId);
    this.valueNode = dojo.byId(valueId);
    this.zoomListener = zoomListener;

    dojo.style(this.valueNode, {
        opacity: 0
    });

    dojo.connect(this.slider, "onChange", this, function(value) {
        this.update(value);
    });

    dojo.connect(dojo.byId(wheelNodeId), dojo.isMozilla ? "DOMMouseScroll" : "onmousewheel", this, function(event) {
        var scroll = (dojo.isMozilla ? event.detail : event.wheelDelta) * (dojo.isMozilla ? -1 : 1);

        if (scroll > 0) {
            this.slider.increment(scroll);
        } else {
            this.slider.decrement(scroll);
        }

        dojo.stopEvent(event);
    });

    dojo.connect(dojo.byId(sliderId), "onmouseover", this, function() {
        dojo.animateProperty({
            node: this.valueNode,
            properties: {
                opacity: 1
            }
        }).play();
    });

    dojo.connect(dojo.byId(sliderId), "onmouseout", this, function() {
        dojo.animateProperty({
            node: this.valueNode,
            properties: {
                opacity: 0
            }
        }).play();
    });
}

ZoomManager.prototype.update = function(value) {
    var zoom = this.getZoom(value);
    this.valueNode.innerHTML = Math.round(100 * zoom) + " %";
    this.zoomListener.setZoom(zoom);
};

ZoomManager.prototype.getZoom = function(value) {
    var range = ZoomManagerOptions.max - ZoomManagerOptions.centerGap;
    var center = .5 * range;

    if (value > center + ZoomManagerOptions.centerGap) {
        value -= ZoomManagerOptions.centerGap;
    } else if (value >= center) {
        return 1;
    }
        
    return Math.exp(1.25 * (value - center) / center);
};


