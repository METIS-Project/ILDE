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

var GroupPatternsDialog = {

    dlg : null,
    actid : null,
    clfpid : null,
    instanceid : null,

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
        
    },
    initAvailablePatterns : function(type, list, containerid) {
        var table = "<table>";

        for(var i = 0; i < list.length; i++) {
            //table += '<tr><td>' + list[i].getTitle() + '</td><td><div id="' + containerid + '_' + list[i].getId() + '"></div></td></tr>';
            table += '<tr><td>' + list[i].getTitle() + '</td><td><div id="' + containerid + '_' + list[i].getId() + '"></div></td>';
            table += '<td><img id="' + containerid + '_' + list[i].getId() + '_help' + '" src="images/icons/help.png" height="16" width="16" style="margin-left:10px;"></td></tr>';
        }

        table += "</table>"

        var gnnode = dojo.byId(containerid);
        
        if (gnnode.hasChildNodes()){
            while (gnnode.childNodes.length>=1){
                gnnode.removeChild(gnnode.firstChild);
            }
        }
        
        gnnode.innerHTML = table;

        for(var i = 0; i < list.length; i++) {
            var id = containerid + "_" + list[i].getId();
            if (dijit.byId(id)){
                dijit.byId(id).destroyRecursive(true);
            }
            var button = new dijit.form.Button({
                label : i18n.get("common.select"),
                type : "submit"
            }, id);

            dojo.connect(dojo.byId(id), "onclick", {
                type : type,
                id: list[i].getId()
            }, function() {
                GroupPatternsDialog.addPattern(this.type, this.id);
            });
            var id_help = id + "_help";
            if (dijit.byId("tt_"+ id_help)){
                dijit.byId("tt_"+ id_help).destroyRecursive(true);
            }
            new dijit.Tooltip({
                    id : "tt_" + id_help,
                    label : list[i].getInfo(),
                    connectId : id_help,
                    showDelay : 10
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

    open : function(actid, clfpid, instanceid) {
        this.actid = actid;
        this.clfpid = clfpid;
        this.instanceid = instanceid;
        
        this.showPatterns();
        
        //this.initAvailablePatterns("gn", GroupPatternManager.getFactories("gn"), "groupPatternsNewGN");
        this.initAvailablePatterns("gn", GroupPatternManager.getFactoriesSupportedAct("gn", actid, clfpid), "groupPatternsNewGN");
        //this.initAvailablePatterns("pa", GroupPatternManager.getFactories("pa"), "groupPatternsNewPA");
        this.initAvailablePatterns("pa", GroupPatternManager.getFactoriesSupportedAct("pa",actid, clfpid), "groupPatternsNewPA");

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
            //if (patternlist[i].instanceid == this.instanceid){
                tl.push({
                    obj : patternlist[i],
                    state : "original"
                });
            //}
        }
        return tl;
    },

    reopen : function() {
        this.updateList("gn");
        this.updateList("pa");
    },
    
    /**
     * Display only the group pattern types available for this clfp and phase and hide the others buttons and table elements
     */
    showPatterns: function(){
        var listGnPatterns = GroupPatternManager.getFactoriesSupportedAct("gn", this.actid, this.clfpid);
                    
        var tableButtons = dojo.byId("tableGpmButtons");
        var tdTableButtons = tableButtons.getElementsByTagName("td");
            
        var tableList = dojo.byId("tableGpmList");
        var tdTableList = tableList.getElementsByTagName("td");
            
        if (listGnPatterns.length == 0){
            tdTableButtons[0].style.display="none";
            tdTableList[0].style.display="none";
        }else{
            tdTableButtons[0].style.display="";
            tdTableList[0].style.display="";
        }
        
        var listPaPatterns = GroupPatternManager.getFactoriesSupportedAct("pa", this.actid, this.clfpid);

        if (listPaPatterns.length == 0){
            tdTableButtons[2].style.display="none";
            tdTableList[2].style.display="none";
        }else{
            tdTableButtons[2].style.display="";
            tdTableList[2].style.display="";
        }
        
        if (listGnPatterns.length == 0 || listPaPatterns.length == 0){
            tdTableButtons[1].style.display="none";
            tdTableList[1].style.display="none";
        }else{
            tdTableButtons[1].style.display="";
            tdTableList[1].style.display="";
        }

    },
    
    updateList : function(type) {
        var ids = [];
        var titles = [];

        var table = "";
        for(var i = 0; i < this.templists[type].length; i++) {
            if (this.templists[type][i].state=="added" || this.templists[type][i].obj.instanceid == this.instanceid){
                var id = "groupPatternsDialog_item_" + type + "_" + i;
                ids[i] = id;
                var title = this.templists[type][i].obj.getTitle();
                titles[i] = title;

                var p = this.templists[type][i];
                table += '<tr><td id="' + id + '" style="color:' + this.colors[p.state] + '">';
                table += title;
                table += '</td></tr>';
            }
        }
        this.tables[type].innerHTML = table;

        //for(var i = 0; i < ids.length; i++) {
        for(var i = 0; i < this.templists[type].length; i++) {
            if (this.templists[type][i].state=="added" || this.templists[type][i].obj.instanceid == this.instanceid){
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
        }
    },

    /* manage patterns */

    //addPattern : function(type, index) {
    addPattern : function(type, id) {
        var alreadyExists = false;
        //var factory = GroupPatternManager.getFactory(type, index);
        var factory = GroupPatternManager.getFactoryById(type, id);
        var factoryId = factory.getId();
        for (var i =0; i < this.templists[type].length; i++){
            if ( (typeof this.templists[type][i].obj.patternid!="undefined" && this.templists[type][i].obj.patternid == factoryId  && this.templists[type][i].obj.instanceid == this.instanceid && this.templists[type][i].obj.patternid == factoryId) || (typeof this.templists[type][i].obj.getId=="function" && this.templists[type][i].obj.getId() == factoryId)){
                alreadyExists = true;
                break;
            }
        }
        if (!alreadyExists){
            this.templists[type].push({
                //obj : GroupPatternManager.getFactory(type, index),
                obj : GroupPatternManager.getFactoryById(type, id),
                state : "added"
            });
            this.updateList(type);
        }
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
        Loader.save("");
    },
    savePatternList : function(type) {
        
        for(var ti = 0; ti < this.templists[type].length; ti++) {
            var tp = this.templists[type][ti];
            if(tp.state == "added") {
                var newObj = tp.obj.newPattern(this.actid, this.instanceid);
                tp.obj = newObj;
            }
        }

        for(var ti = this.templists[type].length - 1; ti >= 0; ti--) {
            var tp = this.templists[type][ti];
            if(tp.state == "removed") {
                this.templists[type].splice(ti, 1);
            }
        }
        
        for (var pi = 0; pi < this.templists[type].length; pi++){
            this.patterns[type][pi] = this.templists[type][pi].obj;
        }

        if (this.patterns[type].length > this.templists[type].length){
            this.patterns[type].splice(this.templists[type].length, this.patterns[type].length - this.templists[type].length);
        }
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

