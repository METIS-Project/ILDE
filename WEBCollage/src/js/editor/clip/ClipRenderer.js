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

var ClipRenderer = {

	alertSize : {
		width : 20,
		height : 20
	},

	image : {
		//src : "images/alert.png",
		//emphasis : "images/alert_e.png"
                src : "images/icons/help.png",
		emphasis : "images/icons/help_e.png"
	},

	paint : function(surface, animator, isGroup, group, position) {
		var paint = false;

		if(isGroup) {
			paint = ClipDisplay.hasPendingAction(group);
		} else {
			for(var i in group) {
				if(ClipDisplay.actionIsPending(group[i])) {
					paint = true;
					break;
				}
			}
		}

		if(paint) {
			var shapeData = {
				x : position.x,
				y : position.y,
				width : this.alertSize.width,
				height : this.alertSize.height,
				src : this.image.src
			};

			var shape = surface.createImage(shapeData);
			shape.moveToFront();
			MenuManager.registerThing(shape, {
				getItems : function(data) {
					return ClipDisplay.getAtWorkMenu(data);
				},
				data : {
					isGroup : isGroup,
					group : group
				},
				title : i18n.get("clip.menu.title"),
				menuStyle : "default"
			});
			new ImageSourceListener(shape, this.image.src, this.image.emphasis);
			if(animator) {
				animator.addNoAnimStuff(shape);
			}
		}
	},
	display : function(containerId, link) {
		dojo.query("#" + containerId + " > .alertBlock").orphan();

		if(ClipDisplay.actionIsPending(link)) {
			var element = dojo.byId(containerId).appendChild(document.createElement("img"));
			dojo.addClass(element, "alertBlock");
			dojo.attr(element, "src", "images/icons/help.png");

			MenuManager.registerThing(element, {
				getItems : function(data) {
					return ClipDisplay.getAtWorkMenu(data);
				},
				data : {
					group : [link]
				},
				title : i18n.get("clip.menu.title"),
				menuStyle : "default"
			});
		}
	}
};
