var ClipDisplay = {

    clip : null,

    data : null,

    idIndex : 0,

    isOpen : false,
    listening : true,

    dragPoint : null,
    moved : false,

    init : function() {
        this.clip = dojo.byId("ClipPane");

        Nifty("div#ClipPane", "big transparent");
        Nifty("div#ClipContents", "transparent");
        Nifty("div#ClipTitleContent", "small transparent");
        dojo.connect(dojo.byId("toolbar.clip"), "onclick", function() {
            ClipDisplay.toggle();
        });

        dojo.connect(dojo.byId("ClipCloseButton"), "onclick", function() {
            ClipDisplay.open(false);
        });

        dojo.connect(dojo.byId("ClipTitle"), "onmousedown", function(event) {
            ClipDisplay.startDrag(event);
        });
        dojo.connect(dojo.byId("ClipPane"), "onmousemove", function(event) {
            ClipDisplay.drag(event);
        });

        dojo.connect(dojo.byId("ClipPane"), "onmouseup", function() {
            ClipDisplay.endDrag();
        });

        dojo.connect(dojo.byId("ClipPane"), "onmouseout", function(event) {
            var toElement = event.toElement ? event.toElement : event.relatedTarget;
            if(this.dragPoint && ClipDisplay.outOfClip(toElement)) {
                ClipDisplay.endDrag();
            }
        });
    },
    startDrag : function(event) {
        this.dragPoint = {
            x : event.clientX,
            y : event.clientY
        };
        var x = 1;
        var x = 2;
        y = x;
        
        
    },
    drag : function(event) {
        if(this.dragPoint) {
            this.moved = true;
            var dx = event.clientX - this.dragPoint.x;
            var dy = event.clientY - this.dragPoint.y;

            var cx = dojo.style(this.clip, "left");
            var cy = dojo.style(this.clip, "top");

            dojo.style(this.clip, {
                "left" : (cx + dx) + "px",
                "top" : Math.max(cy + dy, 0) + "px"
            });

            this.dragPoint.x = event.clientX;
            this.dragPoint.y = event.clientY;
        }
    },
    endDrag : function() {
        this.dragPoint = null;
    },
    toggle : function() {
        if(this.listening) {
            this.listening = false;
            this.open(!this.isOpen);
        }
    },
    open : function(open) {
        this.isOpen = open;
        var onEnd = function() {
            ClipDisplay.animationEnded();
        };
        if(this.moved) {
            if(this.isOpen) {
                dojo.style(this.clip, {
                    "display" : "inline"
                });

                dojo.fadeIn({
                    node : this.clip,
                    onEnd : onEnd
                }).play();
            } else {
                dojo.fadeOut({
                    node : this.clip,
                    onEnd : onEnd
                }).play();
            }
        } else {
            var width = this.clip.offsetWidth;
            var from = open ? -width : 0;
            var to = open ? 0 : -width;

            dojo.animateProperty({
                node : this.clip,
                properties : {
                    left : {
                        start : from,
                        end : to,
                        unit : "px"
                    }
                },
                onEnd : onEnd
            }).play();
        }
    },
    outOfClip : function(element) {
        if(!element) {
            return true;
        } else if(element == this.clip) {
            return false;
        } else {
            return this.outOfClip(element.parentNode);
        }
    },
    animationEnded : function() {
        this.listening = true;
        if(!this.isOpen) {
            if(this.moved) {
                dojo.style(this.clip, {
                    "display" : "none"
                });
            }
        }
    },
    update : function(data, showAlert) {
        this.data = data;

        dojo.query("#ClipContent > *").orphan();
        var container = dojo.byId("ClipContent");
        if (container){
            var ul = container.appendChild(document.createElement("ul"));
            for(var i in data.groups) {
                var group = data.groups[i];
                var element = ul.appendChild(document.createElement("li"));
                element.innerHTML = group.text;
                dojo.addClass(element, "clipGroup");

                MenuManager.registerThing(element, {
                    getItems : function(data) {
                        return ClipDisplay.getMenu(data);
                    },
                    data : {
                        isGroup : true,
                        group : group
                    },
                    title : i18n.get("clip.menu.title"),
                    menuStyle : "default"
                });

                var subul = ul.appendChild(document.createElement("ul"));

                for(var j = 0; j < group.contents.length; j++) {
                    var action = group.contents[j];
                    var subelement = subul.appendChild(document.createElement("li"));
                    subelement.innerHTML = action.reftext ? action.reftext : action.text;

                    dojo.addClass(subelement, "clipItem");
                    dojo.addClass(subelement, this.getAdditionalClasses(action));

                    if(action.reasonText) {
                        subelement.id = "clipitem_" + (this.idIndex++);
                        new dijit.Tooltip({
                            label : action.reasonText,
                            connectId : [subelement.id],
                            showDelay : 10
                        });
                    }

                    MenuManager.registerThing(subelement, {
                        getItems : function(data) {
                            return ClipDisplay.getMenu(data);
                        },
                        data : {
                            action : action
                        },
                        onClick : function(data) {
                            ClipActions.activate(data.action);
                        },
                        title : i18n.get("clip.menu.title"),
                        menuStyle : "default"

                    });
                }
            }

            if(showAlert) {
                dijit.byId('ChangeToaster').setContent(i18n.get("clip.new.actions"));
                dijit.byId('ChangeToaster').show();
            }

            this.updateClipIcon();
        }
    },
    updateClipIcon : function() {
        var button = dijit.byId("toolbar.clip");
        dojo.removeClass(button.iconNode, ClipManager.todo.length == 0 ? "clipIcon" : "clipOkIcon");
        dojo.addClass(button.iconNode, ClipManager.todo.length == 0 ? "clipOkIcon" : "clipIcon");
    },
    getAdditionalClasses : function(action) {
        return "clip" + action.state + (action.modified ? (" clip" + action.modified) : "");
    },
    getMenu : function(data) {
        var items = new Array();
        if(data.isGroup) {
            items.push({
                label : i18n.get("clip.menu.ignore.all"),
                icon : "ignore",
                onClick : function(data) {
                    ClipActions.ignoreAllInGroup(data.group);
                },
                data : data,
                help : i18n.get("help.clip.menu.ignore.all")
            });
        } else {
            items.push({
                label : i18n.get("clip.menu.goto.action"),
                icon : "goto",
                onClick : function(data) {
                    ClipActions.activate(data.action);
                },
                data : data,
                help : i18n.get("help.clip.menu.goto.action")
            });

            items.push({
                isSeparator : true
            });

            items.push({
                label : i18n.get("clip.menu.ignore.action"),
                icon : "ignore",
                onClick : function(data) {
                    ClipActions.ignoreAction(data.action);
                },
                data : data,
                help : i18n.get("help.clip.menu.ignore.action")
            });
        }

        return items;
    },
    getAtWorkMenu : function(data) {
        var items = new Array();
        var needMenu = false;

        if(data.isGroup && this.hasPendingAction(data.group)) {
            needMenu = true;
            var group = this.data.groups[data.group];

            for(var i = 0; i < group.contents.length; i++) {
                var action = group.contents[i];
                items.push({
                    label : action.reftext ? action.reftext : action.text,
                    icon : "goto",
                    tooltip : action.reasonText,
                    classes : this.getAdditionalClasses(action),
                    onClick : function(data) {
                        ClipActions.activate(data.action);
                    },
                    data : {
                        action : action
                    },
                    help : i18n.get("help.clip.menu.goto.action")
                });
            }

            items.push({
                isSeparator : true
            });

            items.push({
                label : i18n.get("clip.menu.ignore.all"),
                icon : "ignore",
                onClick : function(data) {
                    ClipActions.ignoreAllInGroup(data.group);
                },
                data : {
                    group : group
                },
                help : i18n.get("help.clip.menu.ignore.all")
            });

        } else {

            for( i = 0; i < data.group.length; i++) {
                if(this.actionIsPending(data.group[i])) {
                    action = this.data.actions[data.group[i]];
                    needMenu = true;

                    items.push({
                        label : action.reftext ? action.reftext : action.text,
                        icon : "goto",
                        tooltip : action.reasonText,
                        classes : this.getAdditionalClasses(action),
                        onClick : function(data) {
                            ClipActions.activate(data.action);
                        },
                        data : {
                            action : action
                        },
                        help : i18n.get("help.clip.menu.goto.action")
                    });
                }
            }

            items.push({
                isSeparator : true
            });

            items.push({
                label : i18n.get("clip.menu.ignore.all"),
                icon : "ignore",
                onClick : function(data) {
                    ClipActions.ignoreAllInArray(data.group);
                },
                data : {
                    group : data.group
                },
                help : i18n.get("help.clip.menu.ignore.all")
            });
        }

        return needMenu ? items : null;
    },
    hasPendingAction : function(groupId) {
        var group = this.data.groups[groupId];
        if(group) {
            for(var i in group.contents) {
                if(group.contents[i].state == "pending") {
                    return true;
                }
            }
        }

        return false;
    },
    actionIsPending : function(actionLink) {
        var action = this.data.actions[actionLink];
        return (action && action.state == "pending");
    }
};
