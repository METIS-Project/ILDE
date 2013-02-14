function PersistentImageSourceListener(target, targetid, source, emphasis, shape, shapeemphasis) {
    this.target = target;
    this.targetid = targetid;
    this.source = source;
    this.emphasis = emphasis;
    this.shape = shape != null ? shape : target;
    this.shown = false;
    
    if (shapeemphasis) {
        this.shapeemphasis = shapeemphasis;
        shape.connect("onmouseover", this, "setShapeEmphasisSource");
    } else {
        this.shapeemphasis = null;
    }
    
    target.connect("onmouseover", this, "setEmphasisSource");
    target.connect("onmouseout", this, "setNormalSource");
    

    if(PersistentImageSourceListenerManager.isActive(targetid)) {
        this.shape.setShape({
            src : (shapeemphasis && PersistentImageSourceListenerManager.useShapeEmphasis()) ? this.shapeemphasis : this.emphasis
        });
        this.shown = true;
    }
}

PersistentImageSourceListener.prototype.setShapeEmphasisSource = function() {
    this.shape.setShape({
        src : this.shapeemphasis
    });

    PersistentImageSourceListenerManager.activateSE(this.targetid);
    this.shown = true;
}

PersistentImageSourceListener.prototype.setNormalSource = function() {
    this.shape.setShape({
        src : this.source
    });

    PersistentImageSourceListenerManager.deactivate(this.targetid);
    this.shown = false;
};

PersistentImageSourceListener.prototype.setEmphasisSource = function() {
    if (!this.shown || !PersistentImageSourceListenerManager.isActive(this.targetid)) {
        this.shape.setShape({
            src : this.emphasis
        });

        PersistentImageSourceListenerManager.activate(this.targetid);
    }
};
PersistentImageSourceListenerManager = {
    activeId : null,
    shapeEmphsis : false,
    
    activate : function(id) {
        this.activeId = id;
        this.shapeEmphsis = false;
    },
    activateSE : function(id) {
        this.activeId = id;
        this.shapeEmphsis = true;
    },
    deactivate : function(id) {
        if(this.activeId == id) {
            this.activeId = null;
        }
        this.shapeEmphsis = false;
    },
    isActive : function(id) {
        return this.activeId == id;
    },
    useShapeEmphasis : function() {
        return this.shapeEmphsis;
    }
};
