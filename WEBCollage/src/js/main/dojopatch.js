var DojoPatch = {
	patch : function() {
		if(dojox.gfx) {
			if(dojox.gfx.Group) {
				dojox.gfx.Group.prototype.getBoundingBox = function() {
					return this.rawNode.getBBox();
				};
			}

			if(dojox.gfx.Text) {
				dojox.gfx.Text.prototype.getBoundingBox = function() {
					return this.rawNode.getBBox();
				};
			}
		}
	}
};
