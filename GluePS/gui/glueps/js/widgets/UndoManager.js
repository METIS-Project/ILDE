/*
Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Glue!PS.

Glue!PS is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Glue!PS is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

var UndoManager = {
	
    undoCount: 0,
    redoCount: 0,
    undoButton: null,
    redoButton: null,
    undoalert: false,

    init: function() {
        this.undoButton = dijit.byId("toolbar.undo");
        this.redoButton = dijit.byId("toolbar.redo");

        dojo.connect(dojo.byId("toolbar.undo"), "onclick", function(){
        	if (UndoManager.undoalert == true){
        		UndoManager.showUndoDialog();
        	}
        	else{
        		UndoManager.undo();
        	}
        });
        dojo.connect(dojo.byId("toolbar.redo"), "onclick", function(){
            UndoManager.redo();
        });
        this.reset();
        
        dojo.connect(dojo.byId("dialogConfirmUndoOk"), "onclick", function(){
        	UndoManager.hideUndoDialog();
        	UndoManager.undo();
        });
        
        dojo.connect(dojo.byId("dialogConfirmUndoCancel"), "onclick", function(){
        	UndoManager.hideUndoDialog();
        });
    },
	
    reset: function() {
        this.undoCount = this.redoCount = 0;
        this.updateButtons();
    },
    
    disableButtons: function(){
        this.undoButton.setAttribute("disabled", true);
        this.redoButton.setAttribute("disabled", true);
    },

    updateButtons: function() {
        this.undoButton.setAttribute("disabled", this.undoCount == 0);
        this.redoButton.setAttribute("disabled", this.redoCount == 0);
        if (JsonDB.deploy && (JsonDB.deploy.staticDeployURL || JsonDB.deploy.liveDeployURL)){
        	this.undoButton.setAttribute("disabled",true);
        	this.redoButton.setAttribute("disabled",true);
        	
        }
    },
	
    newEditionAction: function() {
        this.undoCount++;
        this.redoCount = 0;
        //this.updateButtons();
        this.getMetadata();
    },
	
    undo: function() {
        if (this.undoCount > 0) {
            this.disableButtons();
    		StateManager.reset();
    		JsonDB.undoDeploy();
    		this.showSaving();
        }
    },

    redo: function() {
        if (this.redoCount > 0) {
            this.disableButtons();
    		StateManager.reset();
    		JsonDB.redoDeploy();
    		this.showSaving();
        }
    },
	
    undone: function() {
        this.undoCount--;
        this.redoCount++;
        //this.updateButtons();
        this.getMetadata();
    },

    redone: function() {
        this.undoCount++;
        this.redoCount--;
        //this.updateButtons();
        this.getMetadata();
    },
    
    showSaving: function(){
    	dojo.byId("savingGif").setAttribute("src", "images/loader.gif");
    	dojo.byId("savingGif").setAttribute("class", "saving");
    },
    
    hideSaving: function(){
    	dojo.byId("savingGif").setAttribute("src", "images/icons/saved.png");
    	dojo.byId("savingGif").setAttribute("class", "saved");
    },
    
	showUndoDialog: function()
	{
        var dlg = dijit.byId("dialogConfirmUndo");
        dlg.titleNode.innerHTML = i18n.get("confirmUndo");
        dojo.byId("dialogConfirmUndoInfo").innerHTML= i18n.get("confirmUndoInfo");
        dojo.byId("dialogConfirmUndoOk").innerHTML = i18n.get("okButton");
        dojo.byId("dialogConfirmUndoCancel").innerHTML = i18n.get("cancelButton");
        dlg.show();
	},
	
	hideUndoDialog: function()
	{
		dijit.byId("dialogConfirmUndo").hide();
	},
	
	/**
	 *  Realiza un GET para obtener información adicional sobre la versión del despliegue
	 *  Permite saber si debe mostrar la alerta al deshacer
	 */
	getMetadata : function() {
        var url = JsonDB.getDeploymentId() + "/metadata";
		if (LdShakeManager.ldShakeMode){
			var url = LdShakeManager.buildLdshakeUrl(url);
		}
		var bindArgs = {
			url : url,
			handleAs : "json", // Manejamos la respuesta del get como un json
			headers : { // Indicar en la cabecera que es json
				"Content-Type" : "application/json",
				"Accept" : "application/json"
			},
			load : function(data) {
	        	if (data.undoalert == "true"){
	        		UndoManager.undoalert = true;
	        	}
	        	else{
	        		UndoManager.undoalert = false;
	        	}
	        	UndoManager.updateButtons();
	        	UndoManager.hideSaving();
			},
			error : function(error, ioargs) {
				UndoManager.hideSaving();
				var message = "";
				message = ErrorCodes.errores(ioargs.xhr.status);
				InformativeDialogs.showAlertDialog(i18n.get("warning"), message);
			}
		}
		dojo.xhrGet(bindArgs);
	}
};
