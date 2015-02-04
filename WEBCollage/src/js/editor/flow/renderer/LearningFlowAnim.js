/**
 * @author Eloy
 */

/**
 * Animador del flujo de aprendizaje
 * @param renderer Encargado de la renderización del flujo de aprendizaje
 */
function LearningFlowAnim(renderer) {
	this.renderer = renderer;
	this.noAnimStuff = new Array();
	this.duration = 500;
}

LearningFlowAnim.prototype.addNoAnimStuff = function(stuff) {
	this.noAnimStuff.push(stuff);
};

LearningFlowAnim.prototype.clear = function() {
	this.noAnimStuff = new Array();
};

LearningFlowAnim.prototype.animate = function(oldtree, newtree) {

	var olddata = this.getData(oldtree);
	var newdata = this.getData(newtree);

	var anims = [];

	for(var id in newdata) {
		var newclfp = newdata[id];
		var oldclfp = olddata[id];

		if(oldclfp != null) {

			if(oldclfp.scale != newclfp.scale || oldclfp.position.x != newclfp.position.x || oldclfp.position.y != newclfp.position.y) {
				anims.push(dojox.gfx.fx.animateTransform({
					shape : oldclfp.paintdata.group,
					duration : this.duration,
					transform : [{
						name : "scaleAt",
						start : [oldclfp.scale, oldclfp.position.x, oldclfp.position.y],
						end : [newclfp.scale, newclfp.position.x, newclfp.position.y]
					}, {
						name : "translate",
						start : [oldclfp.position.x, oldclfp.position.y],
						end : [newclfp.position.x, newclfp.position.y]
					}]
				}));
			}

			for(var actid in newclfp.fullsize.acts) {
				var oldrect = oldclfp.fullsize.acts[actid];
				var newrect = newclfp.fullsize.acts[actid];

				if(oldrect.pre.x != newrect.pre.x || oldrect.pre.y != newrect.pre.y) {
					anims.push(dojox.gfx.fx.animateTransform({
						shape : oldclfp.paintdata.acts[actid].group,
						duration : this.duration,
						transform : [{
							name : "translate",
							start : [oldrect.pre.x, oldrect.pre.y],
							end : [newrect.pre.x, newrect.pre.y]
						}]
					}));
				}
				
				if (oldrect.post.x != newrect.post.x || oldrect.post.y != newrect.post.y) {
					anims.push(dojox.gfx.fx.animateTransform({
						shape : oldclfp.paintdata.acts[actid].trailinggroup,
						duration : this.duration,
						transform : [{
							name : "translate",
							start : [oldrect.post.x, oldrect.post.y],
							end : [newrect.post.x, newrect.post.y]
						}]
					}));
				}
			}
		}

		this.renderer.surface.setDimensions(Math.max(oldtree.easterMostPoint, newtree.easterMostPoint), Math.max(oldtree.furthestPoint, newtree.furthestPoint));
	}

	if(anims.length > 0) {
		this.renderer.uiListener.stopUI();

		for(var i in this.noAnimStuff) {
			this.noAnimStuff[i].removeShape();
		}

		var megaanim = dojo.fx.combine(anims);
		dojo.connect(megaanim, "onEnd", this, "animationEnded");
		megaanim.play();
	} else {
		this.renderer.paint();
	}
};

LearningFlowAnim.prototype.getData = function(tree, data) {
	if(!data) {
		data = {};
	}
	for(var i = 0; i < tree.renderClfps.length; i++) {
		var renderClfp = tree.renderClfps[i];
		data[renderClfp.clfp.id] = renderClfp;

		for(var j = 0; j < renderClfp.subBlocks.length; j++) {
			var block = renderClfp.subBlocks[j];
			this.getData(block, data);
		}
	}
	return data;
};

LearningFlowAnim.prototype.animationEnded = function() {
	this.renderer.paint();
	this.renderer.uiListener.resumeUI();
};
