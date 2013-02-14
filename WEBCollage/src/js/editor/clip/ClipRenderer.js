var ClipRenderer = {

	alertSize : {
		width : 20,
		height : 20
	},

	image : {
		src : "images/alert.png",
		emphasis : "images/alert_e.png"
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
			dojo.attr(element, "src", "images/alert.png");

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
