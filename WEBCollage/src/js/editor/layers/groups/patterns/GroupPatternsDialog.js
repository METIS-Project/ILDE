var GroupPatternsDialog = {

    dlg : null,
    actid : null,

    patterns : null,
    templists : null,
    tables : null,

    colors : {
        original : "black",
        removed : "red",
        added : "green"
    },

    init : function() {
        this.dlg = dijit.byId("groupPatternsDialog");

        this.tables = {
            "gn" : dojo.byId("groupPatternsGNList"),
            "pa" : dojo.byId("groupPatternsPAList")
        };
        this.initAvailablePatterns("gn", GroupPatternManager.getFactories("gn"), "groupPatternsNewGN");
        this.initAvailablePatterns("pa", GroupPatternManager.getFactories("pa"), "groupPatternsNewPA");
    },
    initAvailablePatterns : function(type, list, containerid) {
        var table = "<table>";

        for(var i = 0; i < list.length; i++) {
            table += '<tr><td>' + list[i].getTitle() + '</td><td><div id="' + containerid + '_' + i + '"></div></td></tr>';
        }

        table += "</table>"

        var gnnode = dojo.byId(containerid);
        gnnode.innerHTML = table;

        for(var i = 0; i < list.length; i++) {
            var id = containerid + "_" + i;
            var button = new dijit.form.Button({
                label : i18n.get("common.select"),
                type : "submit"
            }, id);

            dojo.connect(dojo.byId(id), "onclick", {
                type : type,
                index : i
            }, function() {
                GroupPatternsDialog.addPattern(this.type, this.index);
            });
        }
    },
    getMenu : function(data) {
        var items = [];

        if(data.index > 0) {
            items.push({
                label : i18n.get("groupPattern.management.move.up"),
                icon : "up",
                onClick : function(data) {
                    GroupPatternsDialog.moveUp(data.type, data.index);
                },
                data : data,
                help : i18n.get("help.groupPattern.management.move.up")
            });
        }
        if(data.index < this.templists[data.type].length - 1) {
            items.push({
                label : i18n.get("groupPattern.management.move.down"),
                icon : "down",
                onClick : function(data) {
                    GroupPatternsDialog.moveDown(data.type, data.index);
                },
                data : data,
                help : i18n.get("help.groupPattern.management.move.down")
            });
        }

        if(items.length > 0) {
            items.push({
                isSeparator : true
            });
        }

        if(this.templists[data.type][data.index].state == "removed") {
            items.push({
                label : i18n.get("groupPattern.management.undelete"),
                icon : "add",
                onClick : function(data) {
                    GroupPatternsDialog.undelete(data.type, data.index);
                },
                data : data,
                help : i18n.get("help.groupPattern.management.undelete")
            });
        } else {
            items.push({
                label : i18n.get("groupPattern.management.delete"),
                icon : "delete",
                onClick : function(data) {
                    GroupPatternsDialog.remove(data.type, data.index);
                },
                data : data,
                help : i18n.get("help.groupPattern.management.delete")
            });
        }

        return items;
    },

    open : function(actid) {
        this.actid = actid;

        this.patterns = GroupPatternManager.getPatternsForAct(actid);
        this.templists = {
            "gn" : this.createTempList(this.patterns.gn),
            "pa" : this.createTempList(this.patterns.pa)
        };

        this.reopen();
        this.dlg.show();
    },
    createTempList : function(patternlist) {
        var tl = [];
        for(var i = 0; i < patternlist.length; i++) {
            tl.push({
                obj : patternlist[i],
                state : "original"
            });
        }
        return tl;
    },

    reopen : function() {
        this.updateList("gn");
        this.updateList("pa");
    },
    updateList : function(type) {
        var ids = [];
        var titles = [];

        var table = "";
        for(var i = 0; i < this.templists[type].length; i++) {
            var id = "groupPatternsDialog_item_" + type + "_" + i;
            ids[i] = id;
            var title = this.templists[type][i].obj.getTitle();
            titles[i] = title;

            var p = this.templists[type][i];
            table += '<tr><td id="' + id + '" style="color:' + this.colors[p.state] + '">';
            table += title;
            table += '</td></tr>';
        }
        this.tables[type].innerHTML = table;

        for(var i = 0; i < ids.length; i++) {
            MenuManager.registerThing(dojo.byId(ids[i]), {
                getItems : function(data) {
                    return GroupPatternsDialog.getMenu(data);
                },
                data : {
                    type : type,
                    index : i
                },
                title : titles[i],
                menuStyle : "default"
            });
        }
    },

    /* manage patterns */

    addPattern : function(type, index) {
        this.templists[type].push({
            obj : GroupPatternManager.getFactory(type, index),
            state : "added"
        });
        this.updateList(type);
    },

    remove : function(type, index) {
        var tp = this.templists[type][index];
        if(tp.state == "added") {
            this.templists[type].splice(index, 1);
        } else {
            tp.state = "removed";
        }
        this.reopen();
    },
    undelete : function(type, index) {
        var tp = this.templists[type][index];
        tp.state = "original";
        this.reopen();
    },

    moveUp : function(type, index) {
        var tp = this.templists[type][index];
        this.templists[type][index] =  this.templists[type][index-1];
        this.templists[type][index-1] =  tp;
        this.reopen();

    },

    moveDown : function(type, index) {
        var tp = this.templists[type][index];
        this.templists[type][index] =  this.templists[type][index+1];
        this.templists[type][index+1] =  tp;
        this.reopen();
    },

    /* bye bye dialog */

    save : function() {
        this.savePatternList("gn");
        this.savePatternList("pa");
        GroupPatternManager.init();
    },
    savePatternList : function(type) {
        
        for(var ti = 0; ti < this.templists[type].length; ti++) {
            var tp = this.templists[type][ti];
            if(tp.state == "added") {
                this.patterns[type].splice(ti, 0, tp.obj.newPattern(this.actid));
            }
        }

        for(var ti = this.templists[type].length - 1; ti >= 0; ti--) {
            var tp = this.templists[type][ti];
            if(tp.state == "removed") {
                this.patterns[type].splice(ti, 1);
            }
        }

        Loader.save("");
    },

    ok : function() {
        this.save();
        this.close();
    },

    cancel : function() {
        this.close();
    },

    close : function() {
        this.dlg.hide();
    }
};

