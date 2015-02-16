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

/**
 * @author Eloy
 */
/*
 * clfp: clfp subBlocks size neededHeight yGap
 */

String.prototype.visualLength = function() {
	var ruler = document.getElementById("ruler");
	ruler.innerHTML = this;
	return ruler.offsetWidth;
};

String.prototype.visualHeight = function() {
	var ruler = document.getElementById("ruler");
	ruler.innerHTML = this;
	return ruler.offsetHeight;
};
/**
 * Creación del render de un clfp
 */
function LearningFlowRenderClfp(clfp, uiListener) {
	this.clfp = clfp;
	this.subBlocks = new Array();

	this.isActive = uiListener.displayInfo.activeClfpId == clfp.id || uiListener.displayInfo.activeClfpId == "all";

	this.scale = this.isActive ? 1 : .5;
	this.fullsize = clfp.getRenderer().getSize(clfp, uiListener);
	//Obtiene el tamaño en función de la escala
	this.size = {
		width : this.fullsize.width * this.scale,
		height : this.fullsize.height * this.scale
	};

	//Si dentro del clfp hay más subbloques les añade
	for(var i = 0; i < clfp.flow.length; i++) {
		var act = clfp.flow[i];
		if(act.type == "clfpact") {
			this.subBlocks.push(new LearningFlowRenderBlock(act, this, i, uiListener));
		}
	}

	this.subBlocks.sort(function(a, b) {
		return a.wantsToBe - b.wantsToBe;
	});
}

LearningFlowRenderClfp.prototype.position = function() {
	this.yGap = 0;

	var needToRearrange = false;
	var totalHeight = 0;

	for(var i = 0; i < this.subBlocks.length; i++) {
		var subBlock = this.subBlocks[i];
		subBlock.position();
		/* after position(), there is neededHeight, wantsToBe is updated */
	}

	for(var i = 0; i < this.subBlocks.length; i++) {
		subBlock = this.subBlocks[i];
		totalHeight += subBlock.neededHeight;
		if(i < this.subBlocks.length - 1) {
			if(subBlock.neededHeight + subBlock.wantsToBe + LearningFlowRendererOptions.gapBetweenBlocks >= this.subBlocks[i + 1].wantsToBe) {
				needToRearrange = true;
			}
		}
	}

	if(needToRearrange) {
		totalHeight += Math.max(0, LearningFlowRendererOptions.gapBetweenBlocks * (this.subBlocks.length - 1));
		var y = 0;
		for(var i = 0; i < this.subBlocks.length; i++) {
			subBlock = this.subBlocks[i];
			subBlock.wantsToBe = y;
			y += subBlock.neededHeight + LearningFlowRendererOptions.gapBetweenBlocks;
		}

		var yJump = .5 * (this.size.height - totalHeight);
		this.yGap = Math.max(-yJump, 0);
		yJump = Math.max(yJump, 0);

		for(var i = 0; i < this.subBlocks.length; i++) {
			subBlock = this.subBlocks[i];
			subBlock.wantsToBe += yJump;
		}
	} else if(this.subBlocks.length > 0) {
		var last = this.subBlocks[this.subBlocks.length - 1];
		totalHeight = last.neededHeight;
	}

	if(this.subBlocks.length > 0) {
		var first = this.subBlocks[0].wantsToBe;
		if(first < 0) {
			for(var i = 0; i < this.subBlocks.length; i++) {
				subBlock = this.subBlocks[i];
				subBlock.wantsToBe -= first;
			}

			this.yGap -= first;
		}
	}
	//Altura necesaria para representar el clfp con sus posibles subbloques
	this.neededHeight = Math.max(this.size.height + this.yGap, totalHeight);
};

LearningFlowRenderClfp.prototype.reckonWidths = function(level, widths) {
	this.level = level;

	if(!widths[level]) {
		widths[level] = this.size.width;
	} else {
		widths[level] = Math.max(widths[level], this.size.width);
	}

	for(var i = 0; i < this.subBlocks.length; i++) {
		this.subBlocks[i].reckonWidths(level + 1, widths);
	}
};

LearningFlowRenderClfp.prototype.finalPosition = function(level, positions, y) {
	this.position = {
		x : positions[level] - .5 * this.size.width,
		y : y + this.yGap
	};

	for(var i = 0; i < this.subBlocks.length; i++) {
		this.subBlocks[i].finalPosition(level + 1, positions, y);
	}
};
LearningFlowRenderClfp.prototype.paintSubclfpHeading = function(renderer, subblock, pos, count) {
	var headingtransform = [dojox.gfx.matrix.translate(this.position.x, this.position.y)];
	var headinggroup = renderer.bigGroup.createGroup().setTransform(headingtransform);
	renderer.animator.addNoAnimStuff(headinggroup);

	var summary = -5;
	var width = this.clfp.getRenderer().getSize(this.clfp, renderer.uiListener).width * this.scale;
	var centerx = width / 2;

	var polygon = headinggroup.createRect({
		x : 0,
		y : -32,
		width : width,
		height : 32
	}).setFill([0, 0, 0, 0]);

	var headingTextData = {
		x : centerx,
		y : summary - 16,
		text : (pos + 1) + " / " + count,
		align : "middle"
	};

	var headingText = headinggroup.createText(headingTextData).setFont({
		family : "Arial",
		size : "9pt",
		weight : "bold"
	}).setFill([100, 100, 100]);

	var len = headingTextData.text.visualLength();

	var textData = {
		x : centerx,
		y : summary,
		text : this.getMessage(subblock),
		align : "middle"
	};

	var text = headinggroup.createText(textData).setFont({
		family : "Arial",
		size : "7pt",
		weight : "bold"
	}).setFill([150, 150, 150]);

	var imageLeftData = {
		src : "images/icons/arrowBlank.png",
		x : centerx - 16 - 10 - len / 2,
		y : summary - 27,
		width : 16,
		height : 16
	};

	var imageLeft = headinggroup.createImage(imageLeftData);
	new PersistentImageSourceListener(headinggroup, this.clfp.id, "images/icons/arrowBlank.png", "images/icons/arrowLeftGrey.png", imageLeft, "images/icons/arrowLeft.png");
	imageLeft.connect("onclick", {
		clfpid : this.clfp.id
	}, function(event) {
		LearningFlow.activatePreviousInstance(this.clfpid);
	});
	var imageRightData = {
		src : "images/icons/arrowBlank.png",
		x : centerx + 10 + len / 2,
		y : summary - 27,
		width : 16,
		height : 16
	};

	var imageRight = headinggroup.createImage(imageRightData);
	new PersistentImageSourceListener(headinggroup, this.clfp.id, "images/icons/arrowBlank.png", "images/icons/arrowRightGrey.png", imageRight, "images/icons/arrowRight.png");
	imageRight.connect("onclick", {
		clfpid : this.clfp.id
	}, function(event) {
		LearningFlow.activateNextInstance(this.clfpid);
	});
};

LearningFlowRenderClfp.prototype.paint = function(renderer, how, subblock, isFirstInsideBlock) {
	this.transform = [dojox.gfx.matrix.translate(this.position.x, this.position.y), dojox.gfx.matrix.scale({
		x : this.scale,
		y : this.scale
	})];

	this.paintdata = this.clfp.getRenderer().paint(renderer.bigGroup, this.clfp, this.transform, this.isActive, renderer.uiListener, renderer.animator);
        ActivitiesPainter.paintActs(this.clfp, this.paintdata, renderer.uiListener, renderer.animator, this.isActive);
        
	if(!ClfpsCommon.isInMain(this.clfp) && isFirstInsideBlock) {

		var instanciaDepende = IDPool.getObject(renderer.uiListener.instanceid[this.clfp.id]);
		var g = DesignInstance.grupoInstancia(instanciaDepende);
		//Obtenemos las instancias que proceden de la misma
		var ip = DesignInstance.instanciaProcede(instanciaDepende);
		if(ip != null) {
			var igmp = DesignInstance.instanciasGrupoProcedenInstancia(g.roleid, ip.id);
		} else {
			igmp = g.instances;
		}

		for(var j = 0; j < igmp.length; j++) {
			if(igmp[j].id == instanciaDepende.id) {
				this.paintSubclfpHeading(renderer, subblock, j, igmp.length);
				break;
			}
		}
	}

	if(how.paintAlerts) {
		ClipRenderer.paint(renderer.bigGroup, renderer.animator, true, this.clfp.id, {
			x : this.position.x,
			y : this.position.y + 20
		});
	}

	for(var i = 0; i < this.subBlocks.length; i++) {
		this.subBlocks[i].paint(renderer, how);
		renderer.paintLineToSub(this, this.subBlocks[i]);
	}
};

LearningFlowRenderClfp.prototype.getMessage = function(subBlock) {
	return subBlock.parentRenderClfp.clfp.title + ": " + subBlock.clfpact.title;
};

LearningFlowRenderClfp.prototype.getRenderClfp = function(clfpid) {
	if(this.clfp.id == clfpid) {
		return this;
	} else {
		for(var i = 0; i < this.subBlocks.length; i++) {
			var result = this.subBlocks[i].getRenderClfp(clfpid);
			if(result) {
				return result;
			}
		}
	}

	return null;
};
/*
 * LearningFlowRenderBlock: has wantsToBe: number parentClfp: clfp
 * @param parentrenderclfp render del clfp padre
 * @param parentphase Identificador de la fase padre
 * parentRenderClfp parentPhase: int
 */
function LearningFlowRenderBlock(clfpact, parentrenderclfp, parentphase, uiListener) {
	if(parentrenderclfp) {
		this.desiredPosition = parentrenderclfp.clfp.getRenderer().getDesiredPositionForSubplay(parentrenderclfp.clfp, parentphase, uiListener);

		//Ajusta las posiciones a la escala
		this.desiredPosition.x *= parentrenderclfp.scale;
		this.desiredPosition.y *= parentrenderclfp.scale;
		this.wantsToBe = this.desiredPosition.y;
		//Indica el render del clfp padre
		this.parentRenderClfp = parentrenderclfp;
		//Indica la fase padre
		this.parentphase = parentphase;

	} else {
		this.wantsToBe = 0;
	}

	this.renderClfps = new Array();
	this.clfpact = clfpact;

	//Para cada clfp al mismo nivel
	for(var i in clfpact.clfps) {
		var clfp = clfpact.clfps[i];
		this.renderClfps.push(new LearningFlowRenderClfp(clfp, uiListener));
	}
}

LearningFlowRenderBlock.prototype.position = function() {
	/* update wantsToBe to indicate the top of the bounding box */
	//Obtiene la altura necesaria para el bloque
	this.neededHeight = 0;
	for(var i = 0; i < this.renderClfps.length; i++) {
		var renderClfp = this.renderClfps[i];
		renderClfp.position();
		this.neededHeight += renderClfp.neededHeight;
	}

	this.neededHeight += Math.max(0, (this.renderClfps.length - 1) * LearningFlowRendererOptions.gapBetweenClfps) + LearningFlowRendererOptions.gapBetweenBlocks;
	if(this.parentRenderClfp) {
		this.wantsToBe -= .5 * this.neededHeight;
	}
};

LearningFlowRenderBlock.prototype.reckonWidths = function(level, widths) {
	this.level = level;
	for(var i = 0; i < this.renderClfps.length; i++) {
		this.renderClfps[i].reckonWidths(level, widths);
	}
};

LearningFlowRenderBlock.prototype.finalPosition = function(level, positions, y) {
	if(!y) {
		y = LearningFlowRendererOptions.topGap;
	}
	y += this.wantsToBe;

	this.furthestPoint = y;

	for(var i = 0; i < this.renderClfps.length; i++) {
		var renderClfp = this.renderClfps[i];
		renderClfp.finalPosition(level, positions, y);

		var furthest = renderClfp.position.y + renderClfp.neededHeight - renderClfp.yGap;
		if(furthest > this.furthestPoint) {
			this.furthestPoint = furthest;
		}
		y += this.renderClfps[i].neededHeight + LearningFlowRendererOptions.gapBetweenClfps;
	}
};

LearningFlowRenderBlock.prototype.paint = function(renderer, how) {
	//if (isFirst) {
	//  how.patternSequenceNumber = 0;
	//}

	for(var i = 0; i < this.renderClfps.length; i++) {
		this.renderClfps[i].paint(renderer, how, this, i == 0);
	}
};

LearningFlowRenderBlock.prototype.getY = function() {
	return this.renderClfps.length > 0 ? this.renderClfps[0].position.y : 0;
};

LearningFlowRenderBlock.prototype.getBottom = function() {
	if(this.renderClfps.length > 0) {
		var renderClfp = this.renderClfps[this.renderClfps.length - 1];
		return renderClfp.position.y + renderClfp.size.height;
	} else {
		return 0;
	}
	// return this.renderClfps[this.rende].position.y + this.neededHeight -
	// this.renderClfps[0].yGap;
};

LearningFlowRenderBlock.prototype.getRenderClfp = function(clfpid) {
	for(var i = 0; i < this.renderClfps.length; i++) {
		var result = this.renderClfps[i].getRenderClfp(clfpid);
		if(result) {
			return result;
		}
	}

	return null;
};

LearningFlowRenderBlock.prototype.getSeparators = function() {
	var clfps = [];
	this.getClfps(clfps, 0);

	var separators = [];

	for(var i = 0; i < clfps.length; i++) {
		var separator = [];
		if(clfps[0].length > 0) {
			separator.push(clfps[i][0].position.y - LearningFlowRendererOptions.gapBetweenClfps);

			for(var j = 1; j < clfps[i].length; j++) {
				var y = .5 * (clfps[i][j].position.y + clfps[i][j - 1].position.y + clfps[i][j - 1].size.height);
				separator.push(y);
			}

			separator.push(clfps[i][clfps[i].length - 1].position.y + clfps[i][clfps[i].length - 1].size.height + LearningFlowRendererOptions.gapBetweenClfps);
		}
		separators[i] = separator;
	}

	return separators;
};

LearningFlowRenderBlock.prototype.getClfps = function(clfps, level) {
	if(!clfps[level]) {
		clfps[level] = [];
	}

	for(var i = 0; i < this.renderClfps.length; i++) {
		clfps[level].push(this.renderClfps[i]);
		for(var j = 0; j < this.renderClfps[i].subBlocks.length; j++) {
			this.renderClfps[i].subBlocks[j].getClfps(clfps, level + 1);
		}
	}
};
