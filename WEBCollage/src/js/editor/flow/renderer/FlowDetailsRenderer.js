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

var FlowDetailsRenderer = {
	hightlightPhase : {
		src : "images/here.png",
		width : 67.5,
		height : 67.5
	},

	gapPosition : {
		src : "images/gapLocation.png",
		width : 100,
		height : 35.5
	},

	actPosition : {
		src : "images/actLocation.png",
		width : 50,
		height : 50
	},

	clfpSelectStroke : {
		stroke : {
			color : [200, 200, 200, 1.0],
			width : 3,
			style : "dash",
			cap : "round",
			join : "round"
		}
	},

	moveLineColors : {
		fill : [200, 250, 150, .7],
		width : 35,
		border : [50, 50, 50, .7],
		borderWidth : 6,

		emphasis : {
			fill : [200, 250, 150, 1],
			border : [50, 50, 50, 1],
			borderWidth : 6,
			width : 35
		}
	},

	highlightPhase : function(phaseId, renderer) {
		var location = LearningDesign.findClfpParentOf(phaseId);
		var renderClfp = renderer.renderData.mainBlock.getRenderClfp(location.clfp.id);

		var desiredPosition = location.clfp.getRenderer().getDesiredPositionForSubplay(location.clfp, location.index, renderer.uiListener);

		var center = {
			x : renderClfp.position.x + (desiredPosition.x + 50) * renderClfp.scale,
			y : renderClfp.position.y + desiredPosition.y * renderClfp.scale
		};

		renderer.addIcon(center, this.hightlightPhase, 1);
	},
	highlightClfp : function(clfpId, renderer) {
		var renderClfp = renderer.renderData.mainBlock.getRenderClfp(clfpId);

		var rect = renderer.bigGroup.createRect({
			x : renderClfp.position.x,
			y : renderClfp.position.y,
			width : renderClfp.size.width,
			height : renderClfp.size.height
		}).setStroke(this.clfpSelectStroke.stroke);
		renderer.animator.addNoAnimStuff(rect);
	},
	paintPotentialClfpPositions : function(renderer, exceptForClfpId, uiListener) {
		this.paintPotentialClfpPositionsInBlock(renderer.renderData.mainBlock, exceptForClfpId, renderer, uiListener);
	},
	paintPotentialClfpPositionsInBlock : function(block, exceptForClfpId, renderer, uiListener) {
		for(var i = 0; i < block.renderClfps.length; i++) {
			var renderClfp = block.renderClfps[i];
			var clfp = renderClfp.clfp;

			if(clfp.id != exceptForClfpId) {
				var center = {
					x : renderClfp.position.x + .5 * renderClfp.size.width,
					y : -1
				};

				if(i == 0) {
					center.y = renderClfp.position.y - .5 * LearningFlowRendererOptions.gapBetweenClfps;
				} else if(block.renderClfps[i - 1].clfp.id != exceptForClfpId) {
					center.y = .5 * (block.renderClfps[i - 1].position.y + block.renderClfps[i - 1].size.height + renderClfp.position.y);
				}

				if(center.y >= 0) {
					uiListener.registerActiveElement(renderer.addIcon(center, this.gapPosition, 1), {
						isGap : true,
						isReplace : false,
						act : block.clfpact,
						index : i,
						center : center
					});
				}

				for(var j = 0; j < clfp.flow.length; j++) {
					var act = clfp.flow[j];
					if(act.type == "act") {
						var desiredPosition = clfp.getRenderer().getDesiredPositionForSubplay(clfp, j, uiListener);
						center = {
							x : renderClfp.position.x + (desiredPosition.x + 20) * renderClfp.scale,
							y : renderClfp.position.y + desiredPosition.y * renderClfp.scale
						};

						uiListener.registerActiveElement(renderer.addIcon(center, this.actPosition, 1), {
							isGap : true,
							isReplace : true,
							act : act,
							center : center,
							index : j
						});
					}
				}

				for( j = 0; j < renderClfp.subBlocks.length; j++) {
					this.paintPotentialClfpPositionsInBlock(renderClfp.subBlocks[j], exceptForClfpId, renderer, uiListener);
				}

				if(i == block.renderClfps.length - 1) {
					center = {
						x : renderClfp.position.x + .5 * renderClfp.size.width,
						y : renderClfp.position.y + renderClfp.size.height + .5 * LearningFlowRendererOptions.gapBetweenClfps
					};
					uiListener.registerActiveElement(renderer.addIcon(center, this.gapPosition, 1), {
						isGap : true,
						isReplace : false,
						act : block.clfpact,
						index : i + 1,
						center : center
					});
				}
			}
		}
	},
	paintMoveArrow : function(fromClfpID, position, renderer) {
		var renderClfp = renderer.renderData.mainBlock.getRenderClfp(fromClfpID);

		var points = [position.center, {
			x : renderClfp.position.x + .5 * renderClfp.size.width,
			y : renderClfp.position.y + .5 * renderClfp.size.height
		}];

		Intersection.intersect(points);

		LinePainter.paintLine(points, renderer.bigGroup, this.moveLineColors, false, {
			position : "start",
			length : 40
		}, true, renderer.animator);
	},
	dimSurface : function(renderer) {
		var shape = renderer.bigGroup.createRect({
			x : 0,
			y : 0,
			width : renderer.renderData.mainBlock.easterMostPoint,
			height : renderer.renderData.mainBlock.furthestPoint
		}).setFill([255, 255, 255, .5]);

		renderer.animator.addNoAnimStuff(shape);
	}
};
