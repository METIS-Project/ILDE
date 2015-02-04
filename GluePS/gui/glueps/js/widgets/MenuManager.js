/**
 * @author Eloy
 */
var MenuManager = {

    floating : null,
    content : null,
    title : null,
    currentFocusedObject : null,
    fadeOutAnimation : null,
    minOpacity : 0.75,

    tooltipLinkId : 0,

    init : function() {
        this.floating = dojo.byId("FloatingMenu");
        this.content = dojo.byId("FloatingMenuContent");
        this.title = dojo.byId("FloatingMenuTitle");
        dojo.connect(this.floating, "onmousemove", this, "mousemovefloat");
        dojo.connect(this.floating, "onmouseover", this, "mouseoverfloat");
        dojo.connect(this.floating, "onmouseout", this, "exitedfloat");
    },
    registerThing : function(object, how) {
        new MenuManagerFloatingListener(object, how);
    },
    getBaseCoords : function() {
        return dojo.coords(dojo.body());
    },
    mouseoverfloat : function() {
        this.cancelFloatOutAnimation();
    },
    mousemovefloat : function(event) {
        var a = Math.min(event.layerX, event.layerY);
        a = Math.min(a, this.floating.offsetWidth - event.layerX);
        a = Math.min(a, this.floating.offsetHeight - event.layerY);
        var k = 15;
        var t = a < k ? this.minOpacity + (1 - this.minOpacity) * (a / k) : 1.0;
        dojo.style(this.floating, {
            "opacity" : t
        });
    },
    exitedfloat : function(event) {
        this.fadeFloatOut();
    },
    fadeFloatOut : function() {
        this.fadeOutAnimation = dojo.anim(this.floating, {
            "opacity" : 0
        }, 100, dojo.fx.easing.linear, function() {
            MenuManager.fadedFloatOut();
        }, 50);
    },
    fadedFloatOut : function() {
        dojo.style(MenuManager.floating, {
            "visibility" : "hidden"
        });
        this.currentFocusedObject = null;
        this.fadeOutAnimation = null;
    },
    cancelFloatOutAnimation : function() {
        if(MenuManager.fadeOutAnimation != null) {
            MenuManager.fadeOutAnimation.stop(false);
            MenuManager.fadeOutAnimation = null;
        }
    },
    hideFloatMenu : function() {
        this.cancelFloatOutAnimation();
        this.fadedFloatOut();
    },
    formatMenuTitle : function(how) {
        if(how.title) {
            var title = how.title;
        } else {
            switch(how.menuStyle) {
                default:
                    title = i18n.get("menuTitleDefault");
            }
        }

        this.title.innerHTML = title;
        this.title.className = "floatingMenuTitle floatingMenuTitle_" + (how.menuStyle ? how.menuStyle : "default");
    },
    formatMenu : function(how) {
        var items = how.getItems(how.data);
        if(items == null) {
            return false;
        } else {

            this.formatMenuTitle(how);
            this.content.innerHTML = "";

            for(var i = 0; i < items.length; i++) {
                var element = this.content.appendChild(document.createElement("div"));
                if(items[i].isSeparator) {
                    element.className = "floatingMenuSeparator";
                } else {
                    if(items[i].isSubMenu) {
                        dojo.addClass(element, "floatingMenuSubMenuItem");
                    }

                    if(items[i].icon) {
                        var icon = element.appendChild(document.createElement("img"));
                        dojo.attr(icon, "src", Icons.getSrc(items[i].icon));
                        dojo.addClass(icon, "menuItemIcon");
                    }
                    element.appendChild(document.createTextNode(items[i].label));

                    if(items[i].onClick) {
                        dojo.addClass(element, "floatingMenuItem");
                        dojo.connect(element, "onclick", this, "hideFloatMenu");
                        new MenuManagerClickListener(element, items[i].onClick, items[i].data);
                    }

                    element.id = "menuElementId_" + (this.tooltipLinkId++);

                    if(items[i].tooltip) {
                        new dijit.Tooltip({
                            label : items[i].tooltip,
                            connectId : [element.id],
                            showDelay : 400
                        });
                        
                    } else if(items[i].help) {
                        new dijit.Tooltip({
                            label : "<div class='helpTooltip'>" + items[i].help + "</div>",
                            connectId : [element.id],
                            showDelay : 400
                        });
                    }
                    
					 if (items[i].isDisabled) {
						 element.setAttribute("class", "floatingMenuItemDisabled");
					 }

                /*
					 if (items[i].classes) {
					 dojo.addClass(element, items[i].classes);
					 }
					 */
                }
            }
            
            //if (how.hasToolTip)
            //{
            //GFXTooltip.register(dojo.byId("FloatingMenuTitle"), i18n.get("DeleteTool"));
            //}

            return true;
        }
    },
    
    showToolTipMenu: function(how){
    	if (how.toolTipMenu)
    	{
    		this.toolTipMenu = new dijit.Tooltip({
    			label : how.toolTipMenu,
    			connectId : ["FloatingMenuTitle"],
    			showDelay : 10
    		});
    		this.toolTipMenu.open();
    	}
    	else{
    		this.toolTipMenu = null;
    	}
    },
    
    hideToolTipMenu: function(){
    	if (this.toolTipMenu){
    		this.toolTipMenu.destroyRecursive();
    	}
    }
};

var MenuManagerClickListener = function(element, callback, data) {
    this.callback = callback;
    this.data = data;
    dojo.connect(element, "onclick", this, "clicked");
};

MenuManagerClickListener.prototype.clicked = function() {
    MenuManager.fadeFloatOut();
    this.callback(this.data);
};
var MenuManagerFloatingListener = function(object, how) {
    this.object = object;
    this.how = how;

    if( typeof object.connect == "undefined") {
        dojo.connect(object, "onmouseover", this, "entered");
        dojo.connect(object, "onmouseout", this, "exited");
        dojo.connect(object, "onmousemove", this, "moved");

    } else {
        object.connect("onmouseover", this, "entered");
        object.connect("onmouseout", this, "exited");
        object.connect("onmousemove", this, "moved");
    }

    /*AÃ±adido en Web Instance Collage para manejar el evento onclick*/
    if(this.how.onClick) {
        if( typeof object.connect == "undefined") {
            dojo.connect(object, "onclick", this, "onclick");

        } else {
            object.connect("onclick", this, "onclick");
        }
    }
};

MenuManagerFloatingListener.prototype.onclick = function(event) {
    this.how.onClick(this.how.data);
};

MenuManagerFloatingListener.prototype.moved = function(event) {
    if(this.how.followCursor && MenuManager.currentFocusedObject == this.object) {
        var left = dojo.style(MenuManager.floating, "left");
        var top = dojo.style(MenuManager.floating, "top");
        var dx = left - event.clientX;
        var dy = top - event.clientY;
        var d = Math.sqrt(dx * dx + dy * dy);
        var k = 15;
        if(d > k) {
            top = Math.round(event.clientY + k * dy / d);
            left = Math.round(event.clientX + k * dx / d);
            dojo.style(MenuManager.floating, {
                "left" : left + "px",
                "top" : top + "px"
            });
        }
    } else if(MenuManager.currentFocusedObject == null) {
        this.entered(event);
    }
};

MenuManagerFloatingListener.prototype.entered = function(event) {
    if(MenuManager.currentFocusedObject != null && this.how.ignoreWithOther) {
        return;
    } else {
        // MenuManager.cancelFloatOutAnimation();
        MenuManager.hideFloatMenu();

        if(MenuManager.currentFocusedObject != this.object) {

            MenuManager.hideFloatMenu();
            if(MenuManager.formatMenu(this.how)) {

                var bodypos = MenuManager.getBaseCoords();

                MenuManager.currentFocusedObject = this.object;
                if(this.how.followCursor) {
                    this.setPosition({
                        x : event.clientX - bodypos.x,
                        y : event.clientY - bodypos.y,
                        width : 0,
                        height : 0,
                        dy : 20
                    });
                } else {
                    var pos = this.getBoundingBox();
                    pos.x -= bodypos.x;
                    pos.y -= bodypos.y;
                    pos.dy = 0;
                    this.setPosition(pos);
                }
                
                //Tooltip del menú
                MenuManager.showToolTipMenu(this.how);
            }
        }
    }
};

MenuManagerFloatingListener.prototype.exited = function(event) {
    if(MenuManager.currentFocusedObject == this.object) {
        MenuManager.fadeFloatOut();
        MenuManager.hideToolTipMenu();
    }
};

MenuManagerFloatingListener.prototype.getBoundingBox = function() {
    var node = this.object;
    if(node.getBoundingBox) {
        var nodePos = dojo.coords(this.getDOMParent());
        var box = node.getTransformedBoundingBox();

        return {
            x : nodePos.x + box[0].x,
            y : nodePos.y + box[0].y,
            width : box[2].x - box[0].x,
            height : box[2].y - box[0].y
        };
    } else {
        var pos = dojo.coords(node);
        return {
            x : pos.x,
            y : pos.y,
            width : pos.w,
            height : pos.h
        };
    }
};

MenuManagerFloatingListener.prototype.getDOMParent = function(node) {
    if(!node) {
        node = this.object;
    }

    if(node._parent) {
        return node._parent;
    } else {
        return this.getDOMParent(node.parent);
    }
};

MenuManagerFloatingListener.prototype.setPosition = function(targetPosition) {
    var bodyHeight = dojo.body().offsetHeight;
    var menuHeight = MenuManager.floating.offsetHeight;

    var y;
    if(targetPosition.y + targetPosition.height + menuHeight > bodyHeight || this.doesPrefferAbove()) {
        dojo.removeClass(MenuManager.floating, "dijitTooltipBelow");
        dojo.addClass(MenuManager.floating, "dijitTooltipAbove");
        y = targetPosition.y - menuHeight - targetPosition.dy;
    } else {
        dojo.removeClass(MenuManager.floating, "dijitTooltipAbove");
        dojo.addClass(MenuManager.floating, "dijitTooltipBelow");
        y = targetPosition.y + targetPosition.height + targetPosition.dy;
    }

    dojo.style(MenuManager.floating, {
        //"left" : (targetPosition.x + .5 * targetPosition.width + 10) + "px",
    	//"left" : targetPosition.x + 10 + "px",
    	"left" : (targetPosition.x + .5 * targetPosition.width - 13) + "px",
        "top" : y + "px",
        "visibility" : "visible",
        "opacity" : MenuManager.minOpacity
    });
};

MenuManagerFloatingListener.prototype.doesPrefferAbove = function() {
    return this.how.menuStyle == "role" || this.how.menuStyle == "groupPatternAlerts";
};
