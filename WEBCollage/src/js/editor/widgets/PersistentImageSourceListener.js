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
