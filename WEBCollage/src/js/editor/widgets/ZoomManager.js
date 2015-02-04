
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


