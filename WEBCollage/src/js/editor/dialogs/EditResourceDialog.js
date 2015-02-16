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

var ResourceManager = {

    dlgCallback : null,
    dlgResource : null,
    
    tmpResource: null,

    init : function() {
        dojo.connect(EditDocumentDialog, "close", this, "dlgClosed");
        dojo.connect(EditToolDialog, "close", this, "dlgClosed");
        dojo.connect(dojo.byId("deleteDocumentDialogOk"), "onclick", function(){
            ResourceManager.closeConfirmDialog();
            ResourceManager.remove(ResourceManager.tmpResource);
        });        
        dojo.connect(dojo.byId("deleteDocumentDialogCancel"), "onclick", function(){
            ResourceManager.closeConfirmDialog();
        });
    },
    
    newDocument : function(callback) {
        this.dlgCallback = callback;
        this.dlgResource = null;
        EditDocumentDialog.open();
    },
    newTool : function(callback) {
        this.dlgCallback = callback;
        this.dlgResource = null;
        EditToolDialog.open();
    },
    edit : function(resource) {
        if(resource.subtype == "tool") {
            EditToolDialog.open(resource);
        } else {
            EditDocumentDialog.open(resource);
        }
    },
    
    openDeleteResourceDialog: function(resource){
        this.tmpResource = resource;
        dijit.byId("deleteDocumentDialog").show();
    },
    
    closeConfirmDialog: function(){
        dijit.byId("deleteDocumentDialog").hide();
    },
    
    
    remove : function(resource) {
        LearningDesign.removeResource(resource);
        this.tmpResource = null;
    },
    dlgClosed : function() {
        if(this.dlgCallback) {
            this.dlgCallback(this.dlgResource);
        }
    },
    setResourceEdited : function(resource) {
        this.dlgResource = resource;
    }
};

var EditDocumentDialog = {
    resource : null,
    dlg : null,
    task : null,

    init : function() {
        this.dlg = dijit.byId("LDDocumentResourceEditDialog");
    },
    open : function(resource) {
        if(resource) {
            this.resource = resource;
            this.task = "edit";
            dijit.byId("LDDocumentResourceEditName").setValue(resource.title);
            dijit.byId("LDDocumentResourceLink").setValue(resource.link);
            this.dlg.titleNode.innerHTML = i18n.get("resources.editDoc");
        } else {
            this.task = "new";
            dijit.byId("LDDocumentResourceEditName").setValue("");
            dijit.byId("LDDocumentResourceLink").setValue("");
            this.dlg.titleNode.innerHTML = i18n.get("resources.newDoc");
        }
        dojo.byId("LDDocumentResourceEditDialogErrorReport").innerHTML = "";

        this.dlg.show();
    },
    save : function(resource) {
        if(this.task == "new") {
            LearningDesign.addResource(resource);
        } else {
            ChangeManager.resourceEdited(resource);
        }
        ResourceManager.setResourceEdited(resource);
        this.close();
    },
    
    check: function(){
        var resource = this.task == "new" ? new Resource("", "doc") : this.resource;
        resource.title = dijit.byId("LDDocumentResourceEditName").getValue();
        resource.link = dijit.byId("LDDocumentResourceLink").getValue();

        if (resource.title.length == 0){
            dojo.byId("LDDocumentResourceEditDialogErrorReport").innerHTML = i18n.get("resources.editDoc.name.error.empty");
        }
        else if (resource.link.length == 0){
            dojo.byId("LDDocumentResourceEditDialogErrorReport").innerHTML = i18n.get("resources.editDoc.link.error.empty");
        }
        else if (/*!dojox.validate.web.isUrl(resource.link) || */!EditDocumentDialog.validateURL(resource.link)){
            dojo.byId("LDDocumentResourceEditDialogErrorReport").innerHTML = i18n.get("resources.editDoc.link.error.notvalid");
        }
        else{
            this.save(resource);
        }
    },
    
    ok : function() {
        this.check();
    },
    cancel : function() {
        this.close();
    },
    close : function() {
        this.dlg.hide();
    },
    
    validateURL: function(textval) {
      var urlregex = new RegExp(
            "^(http|https|ftp)\://([a-zA-Z0-9\.\-]+(\:[a-zA-Z0-9\.&amp;%\$\-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0-9\-]+\.)*[a-zA-Z0-9\-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(\:[0-9]+)*(/($|[a-zA-Z0-9\.\,\?\'\\\+&amp;%\$#\=~_\-]+))*$");
      return urlregex.test(textval);
    }
    
};


var EditToolVLEDialog = {
    url : null,

    init : function() {
        this.dlg = dijit.byId("LDDocumentToolVLEEditDialog");
        this.resetSelect();
    },
    open : function() {
        //Comprobar que se está trabajando con una instalación del tipo GluePS
        this.resetSelect();
        this.disableOKButton();
        //LoaderIcon.show("loadingSelectToolVLUE");
        this.dlg.show();
        this.updateSelectTool();
    },
    reopen : function() {
    },
    ok : function() {
        var tool_select = dijit.byId("selectToolVLUE");
        var url = tool_select.get("value");
        if(url != "0") {
            EditToolDialog.toolSearchResult("gluepsid", null, null, url, tool_select.get("displayedValue"));
        }
        this.close();
    },
    cancel : function() {
        this.close();
    },
    close : function() {
        this.dlg.hide();
    },
    
    disableOKButton: function(){     
        dijit.byId("selectToolVLUE").set('disabled', true);
    },
    
    enableOKButton: function(){     
        dijit.byId("selectToolVLUE").set('disabled', false);
    },
    
    resetSelect : function() {
        var tool_select = dijit.byId("selectToolVLUE");
        while(tool_select.getOptions(0) != null) {
            tool_select.removeOption(tool_select.getOptions(0));
        }
        tool_select.addOption({
            label : i18n.get("resources.tool.vle.selectinfo"),
            value : "0"
        });
    },
    availableVLE : function() {
        var GLUEPS = "4";
        var bindArgs = {
            url : "manager/manageLms.php",
            handleAs : "json",
            content : {
                task : "obtener_installation",
                idlms_installation : DesignInstance.data.instObj.id
            },
            load : function(data) {
                if(data.ok == true && data.installation.idlms == GLUEPS) {
                    EditToolVLEDialog.getTools(DesignInstance.data.lmsObj.id, data.installation.user, data.installation.pass);
                } else {
                    DesignInstance.data.vleTools = new Array();
                    Loader.save("");
                //LoaderIcon.hide("loadingSelectToolVLUE");
                }
            }
        };
        dojo.xhrPost(bindArgs);
    },
    getTools : function(uri, user, pass) {
        var bindArgs = {
            url : "manager/tools/toolVLE.php",
            handleAs : "json",
            content : {
                task : "get_tools",
                uri : uri,
                user: user,
                pass: pass
            },
            load : function(data) {
                DesignInstance.data.vleTools = new Array();
                if(data.ok == true) {
                    for(var i = 0; i < data.tools.length; i++) {
                        var tool = {
                            toolId: data.tools[i].id,
                            toolName: data.tools[i].name
                        };
                        DesignInstance.data.vleTools.push(tool);
                    }
                }
                Loader.save("");
            //LoaderIcon.hide("loadingSelectToolVLUE");               
            }
        }
        dojo.xhrPost(bindArgs);
    },
    
    updateSelectTool: function(){
        var tool_select = dijit.byId("selectToolVLUE");
        for(var i = 0; i < DesignInstance.data.vleTools.length; i++) {
            tool_select.addOption({
                label : DesignInstance.data.vleTools[i].toolName,
                value : DesignInstance.data.vleTools[i].toolId
            });
        }
        EditToolVLEDialog.enableOKButton();
        if(EditToolDialog.data.gluepsid) {
            dijit.byId('selectToolVLUE').attr("value", EditToolDialog.data.gluepsid);
        }
    }
};

var EditToolKeywordDialog = {
    dlg : null,
    nextpetition : 0,
    waitingforanswer : -1,
    results : null,

    init : function() {
        this.dlg = dijit.byId("LDDocumentToolKeywordDialog");
    },
    open : function() {
        this.clear();
        this.dlg.show();
    },
    reopen : function() {

    },
    clear : function() {
        var element = dojo.byId("LDDocumentToolKeywordsResults");
        element.innerHTML = "";
    },
    doSearch : function() {
        this.waitingforanswer = this.nextpetition++;
        var bindArgs = {
            url : "manager/tools/keywordsearch.php",
            handleAs : "json",
            content : {
                petition : this.waitingforanswer,
                keywords : dijit.byId("LDDocumentToolKeywords").getValue()
            },
            load : function(data) {
                EditToolKeywordDialog.searchResult(data);
            },
            error : function(error) {
                EditToolKeywordDialog.searchResult(null);
            }
        };
        dojo.xhrPost(bindArgs);

    },
    searchResult : function(data) {
        this.results = data.list;
        this.clear();

        var element = dojo.byId("LDDocumentToolKeywordsResults");
        var list = dojo.create("ul", {}, element);

        for(var i = 0; i < data.list.length; i++) {
            var item = dojo.create("li", {}, list);
            item.innerHTML = "<b>" + this.results[i].description.title + "</b><br/>" + this.results[i].description.summary;
            var link = dojo.create("div", {
                innerHTML : i18n.get("resources.tool.ontool.select"),
                style : {
                    color : "blue",
                    cursor : "pointer"
                }
            }, item);

            dojo.connect(link, "onclick", {
                item : i
            }, function() {
                EditToolKeywordDialog.selected(this.item);
            });
        }

        return;
    },
    selected : function(item) {
        this.close();
        EditToolDialog.toolSearchResult("ontoolurl", this.results[item].url, null, null);
    },
    cancel : function() {
        this.close();
    },
    close : function() {
        this.waitingforanswer = -1;
        this.dlg.hide();
    }
};

var EditToolDialog = {
    resource : null,
    dlg : null,
    task : null,

    data : {
        tooltype : null,
        url : null,
        ontoolquery : null,
        gluepsid : null,
        toolname : null
    },
    
    showSearchInVlueButton: function(){
        var bindArgs = {
            url: "manager/manageLms.php",
            handleAs: "json",
            content: {
                task: "obtener_installation_lms",
                idlms_installation: DesignInstance.data.instObj.id
            },
            load: function(data){
                //El tipo de lms 4 se corresponde GluePS
                if (data.ok && data.installation.idlms=="4"){
                    dijit.byId("searchToolVle").set("disabled",false);
                }
                else{
                    //if (Loader.ldShakeMode && DesignInstance.data.vleTools.length > 0){
                    if (DesignInstance.data.vleTools.length > 0){
                        dijit.byId("searchToolVle").set("disabled",false);
                    }
                    else{
                        dijit.byId("searchToolVle").set("disabled",true);
                    }
                }
                //Ocultar los botones ontoolsearch y keyword si WIC está contenido en ldshake
                if (Loader.ldShakeMode){
                    dijit.byId("searchOntoolSearch").domNode.style.display = "none";
                    dijit.byId("searchKeyword").domNode.style.display = "none";
                }
            }
        };
        dojo.xhrPost(bindArgs);
    },  

    init : function() {
        this.dlg = dijit.byId("LDDocumentToolEditDialog");
    },
    open : function(resource) {
        this.showSearchInVlueButton();
        if(resource) {
            this.task = "edit";
            this.resource = resource;
            this.dlg.titleNode.innerHTML = i18n.get("resources.editTool");
            this.data.tooltype = resource.tooltype;
            this.data.url = resource.url;
            this.data.ontoolquery = resource.ontoolquery;
            this.data.gluepsid = resource.gluepsid;
            this.data.toolname = resource.toolname;
            dijit.byId("LDDocumentToolEditName").setValue(resource.title);
            this.setDescription();
        } else {
            this.task = "new";
            this.dlg.titleNode.innerHTML = i18n.get("resources.newTool");

            this.data.url = this.data.ontoolquery = this.data.tooltype = this.data.gluepsid = null;

            dijit.byId("LDDocumentToolEditName").setValue("");
            dojo.byId("LDDocumentToolDescription").innerHTML = "";
        }
        dojo.byId("LDDocumentToolEditNameErrorReport").innerHTML="";
        this.dlg.show();
    },
    setDescription: function() {
        dojo.byId("LDDocumentToolDescription").innerHTML = ResourceHelper.getDescriptionForTool(this.data);
    },
    reopen : function() {
    },
    save : function() {
        var resource = this.task == "new" ? new Resource("", "tool") : this.resource;

        resource.title = dijit.byId("LDDocumentToolEditName").getValue();
        resource.url = this.data.url;
        resource.ontoolquery = this.data.ontoolquery;
        resource.gluepsid = this.data.gluepsid;
        resource.tooltype = this.data.tooltype;
        resource.toolname = this.data.toolname;

        if(this.task == "new") {
            LearningDesign.addResource(resource);
        } else {
            ChangeManager.resourceEdited(resource);
        }
        ResourceManager.setResourceEdited(resource);
    },
    
    checkTitle: function() {
        if (dijit.byId("LDDocumentToolEditName").getValue()=="")
        {
            dojo.byId("LDDocumentToolEditNameErrorReport").innerHTML=i18n.get("toolNameError");
        }
        else{
            this.ok();
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
    },
    openVLEDialog : function() {
        EditToolVLEDialog.open();
    },
    openOntoolSearch : function() {
        OntoolsearchDialog.open();
    },
    openKeyWordSearch : function() {
        EditToolKeywordDialog.open();
    },
    toolSearchResult : function(tooltype, url, query, gluepsid, toolname) {
        this.data.tooltype = tooltype;
        switch(tooltype) {
            case "ontoolurl":
                this.data.url = url;
                if (query) {
                    this.data.ontoolquery = query;
                }
                break;
            case "ontoolquery":
                this.data.ontoolquery = query;
                break;
            case "gluepsid":
                this.data.gluepsid = gluepsid;
                this.data.toolname = toolname;
                break;
            default:
                break;
        }
        this.setDescription();
    },
    getCurrentQuery : function() {
        return this.data.ontoolquery;
    }
};
