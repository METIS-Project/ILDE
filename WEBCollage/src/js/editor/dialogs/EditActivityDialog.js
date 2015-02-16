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

var EditActivityDialog = {
    dlg : null,
    activity : null,
    loManager : null,
    resourceManager : null,

    init : function() {
        this.dlg = dijit.byId("LDActivityEditDialog");
        this.loManager = new ListOfItemsManager("LDActivityLOsList");
        this.resourceManager = new ListOfItemsManager("LDActivityResourcesList");
        this.resourceManager.buildList = this.resourceManagerBuildList;
        
        dojo.connect(dojo.byId("LDSelectResourcesNewDoc_activity"), "onclick", function() {
            EditActivityDialog.newDocument();
        });
        dojo.connect(dojo.byId("LDSelectResourcesNewTool_activity"), "onclick", function() {
            EditActivityDialog.newTool();
        });        
    },
    open : function(activity) {
        this.activity = activity;
        this.loManager.listOfItems = LearningDesign.activitiesAndLOsMatches.getMatchesFor(activity.id);
        this.resourceManager.listOfItems = LearningDesign.activitiesAndResourcesMatches.getMatchesFor(activity.id);

        this.load();
        this.dlg.show();
    },
    ok : function() {
        this.save();
        this.close();
    },
    setActivityTitle : function(title) {
        dijit.byId("LDActivityTitle").setValue(title);
    },
    cancel : function() {
        this.close();
    },
    close : function() {
        this.dlg.hide();
    },
    load : function() {
        this.setActivityTitle(this.activity.title);
        dijit.byId("LDActivityDescription").setValue(this.activity.description);
        this.loManager.buildList();
        this.resourceManager.buildList();
        var displayLOs = this.activity.subtype != "support" ? "" : "none";
        dijit.byId("LDActivityLOContainer").domNode.style.display = displayLOs;
        //hide the new tool button if we are in the ldshake mode and information about the lms and class has not been provided
        if (Loader.ldShakeMode && (DesignInstance.data==null || (DesignInstance.data.instObj.id=="" && DesignInstance.data.lmsObj.id=="" && DesignInstance.data.classObj.id==""))) {
            dijit.byId("LDSelectResourcesNewTool_activity").domNode.style.display = "none";
        }
        return;
    },
    save : function() {
        this.activity.title = dijit.byId("LDActivityTitle").getValue();
        this.activity.description = dijit.byId("LDActivityDescription").getValue();

        LearningDesign.activitiesAndLOsMatches.setMatchesFor(this.activity.id, this.loManager.listOfItems);

        LearningDesign.activitiesAndResourcesMatches.setMatchesFor(this.activity.id, this.resourceManager.listOfItems);

        ChangeManager.activityEdited(this.activity);
    },
    /* los & resources */
    loadLOSelector : function() {
        SelectLOsDialog.open(this.loManager.listOfItems, "activity");
    },
    pickUpSelectedLOs : function() {
        for(var i in SelectLOsDialog.selectedLOs) {
            this.loManager.listOfItems.push(SelectLOsDialog.selectedLOs[i]);
        }
        this.loManager.buildList();
    },
    loadResourceSelector : function() {
        SelectResourcesDialog.open(this.resourceManager.listOfItems, "activity");
    },
    pickUpSelectedResources : function() {
        for(var i in SelectResourcesDialog.selectedResources) {
            this.resourceManager.listOfItems.push(SelectResourcesDialog.selectedResources[i]);
        }
        this.resourceManager.buildList();
    },
    /* resourceManagerStuff */
    resourceManagerBuildList : function() {
        var table = dojo.byId("LDActivityResourcesList");
        while(table.childNodes.length >= 1) {
            table.removeChild(table.firstChild);
        }

        var documents = table.appendChild(document.createElement("li"));
        documents.innerHTML = i18n.get("resources.inActivity.docs");
        var documentsList = documents.appendChild(document.createElement("ul"));
        var tools = table.appendChild(document.createElement("li"));
        tools.innerHTML = i18n.get("resources.inActivity.tools");
        var toolsList = tools.appendChild(document.createElement("ul"));

        for(var i in this.listOfItems) {
            var resource = IDPool.getObject(this.listOfItems[i]);
            if(resource.subtype == "doc") {
                var parent = documentsList;
                var extra = "link";
            } else {
                parent = toolsList;
                extra = "description";
            }

            var li = parent.appendChild(document.createElement("li"));
            li.className = "loListItem";

            var name = li.appendChild(document.createElement("span"));
            name.innerHTML = " " + resource.title + " ";
            name.setAttribute("title", resource[extra]);

            var byebye = li.appendChild(document.createElement("img"));
            byebye.setAttribute("src", "images/icons/delete.png");
            byebye.className = "generalButton";
            var listener = new DeleteActivityItemListener(resource.id, this);
            dojo.connect(byebye, "onclick", listener, "click");
        }
        if (Loader.ldShakeMode && (DesignInstance.data==null || (DesignInstance.data.instObj.id=="" && DesignInstance.data.lmsObj.id=="" && DesignInstance.data.classObj.id==""))) {
            tools.style.display = "none";
        }
    },
    newTool: function() {
        ResourceManager.newTool(function(resource) {
            EditActivityDialog.resourceCreated(resource);
        });
    },
    newDocument: function() {
        ResourceManager.newDocument(function(resource) {
            EditActivityDialog.resourceCreated(resource);
        });
    },
    resourceCreated: function(resource) {
        if (resource) {
            this.resourceManager.listOfItems.push(resource.id);
        }
        this.resourceManager.buildList();
        //this.load();
    }
};
