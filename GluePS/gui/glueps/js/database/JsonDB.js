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

var JsonDB = {
		   	   
	deploy: null,
		   
	init: function(urlDeploy){
		this.getDeployInit(urlDeploy);
	},
	
	/**
	 * Función para notificarla de cambios en el modelo
	 */
	notifyChanges: function()
	{
		UndoManager.disableButtons();
		StateManager.reset();
		this.putDeploy();
		UndoManager.showSaving();
	},
	
	getDeployInit : function(urlDeploy) {
		
		var bindArgs = {
			url : urlDeploy,
			handleAs : "json", // Manejamos la respuesta del get como un json
			headers : { // Indicar en la cabecera que es json
		        "Content-Type": "application/json",
		        "Accept" : "application/json"		
			},
			load : function(data) {
				JsonDB.deploy = data;
				//Ordenar las instancias de las actividades
				//InstancedActivityContainer.sortInstancedActivities();
				//Ordenar los grupos
				GroupContainer.sortGroups();
				//Ordenar los recursos (documentos y herramientas)
				ResourceContainer.sortResources();
				DeploymentPainter.showInformationCompleted();
				DeploymentPainter.paint();
				//Actualizar el valor de ldshakeFrameOrigin
				if (LdShakeManager.ldShakeMode){
					LdShakeManager.setFrameOrigin();
				}
			},
			error : function(error, ioargs) {
				var message = "";
				message = ErrorCodes.errores(ioargs.xhr.status);
				if (ioargs.xhr.status == 401)
				{
					//Si permanece abierta, recargar la página de la que procede
					if (window.opener)
					{
						window.opener.location.reload();
					}
					window.close();
				}
				else{
					InformativeDialogs.showAlertDialog(i18n.get("warning"), message);
				}
			}
		}
		dojo.xhrGet(bindArgs);
	},
	
	/**
	 *  Realiza un PUT para deshacer el último cambio
	 */
	undoDeploy : function() {
		var url = JsonDB.getDeploymentId() + "/undo";
		if (LdShakeManager.ldShakeMode){
			var url = LdShakeManager.buildLdshakeUrl(url);
		}
		var bindArgs = {
			url : url,
			method: "PUT",
			putData : dojo.toJson(JsonDB.deploy),
			handleAs : "json", // Manejamos la respuesta del get como un json
			headers : { // Indicar en la cabecera que es json
				"Content-Type" : "application/json",
				"Accept" : "application/json"
			},         
			load : function(data) {
				JsonDB.deploy = data;	
				DeploymentPainter.showInformationCompleted();
				DeploymentPainter.paint();
            	//Indicamos al undo manager que se ha realizado una operación de deshacer
            	UndoManager.undone();
			},
			error : function(error, ioargs) {
				var message = "";
				message = ErrorCodes.errores(ioargs.xhr.status);
				InformativeDialogs.showAlertDialog(i18n.get("warning"), message);
			}
		}
		dojo.xhrPut(bindArgs);
	},
	
	/**
	 *  Realiza un PUT para rehacer el último cambio
	 */
	redoDeploy : function() {
		var url = JsonDB.getDeploymentId() + "/redo";
		if (LdShakeManager.ldShakeMode){
			var url = LdShakeManager.buildLdshakeUrl(url);
		}
		var bindArgs = {
			url : url,
			method: "PUT",
			putData : dojo.toJson(JsonDB.deploy),
			handleAs : "json", // Manejamos la respuesta del get como un json
			headers : { // Indicar en la cabecera que es json
				"Content-Type" : "application/json",
				"Accept" : "application/json"
			},
			load : function(data) {
				JsonDB.deploy = data;	
				DeploymentPainter.showInformationCompleted();
				DeploymentPainter.paint();
            	//Indicamos al undo manager que se ha realizado una operación de rehacer
            	UndoManager.redone();
			},
			error : function(error, ioargs) {
				var message = "";
				message = ErrorCodes.errores(ioargs.xhr.status);
				InformativeDialogs.showAlertDialog(i18n.get("warning"), message);
			}
		}
		dojo.xhrPut(bindArgs);
	},
	
	/**
	 *  Realiza un GET para obtener el documento de despliegue
	 */
	getDeploy : function(urlDeploy) {
		var bindArgs = {
			url : urlDeploy,
			handleAs : "json", // Manejamos la respuesta del get como un json
			headers : { // Indicar en la cabecera que es json
				"Content-Type" : "application/json",
				"Accept" : "application/json"
			},
			load : function(data) {
				JsonDB.deploy = data;	
				DeploymentPainter.showInformationCompleted();
				DeploymentPainter.paint();
			},
			error : function(error, ioargs) {
				var message = "";
				message = ErrorCodes.errores(ioargs.xhr.status);
				InformativeDialogs.showAlertDialog(i18n.get("warning"), message);
			}
		}
		dojo.xhrGet(bindArgs);
	},
	
	getDeployEnd : function(urlDeploy) {
		var bindArgs = {
			url : urlDeploy,
			handleAs : "json", // Manejamos la respuesta del get como un json
			headers : { // Indicar en la cabecera que es json
				"Content-Type" : "application/json",
				"Accept" : "application/json"
			},
			load : function(data) {
				JsonDB.deploy = data;
				DeploymentController.showDeployVle();
			},
			error : function(error, ioargs) {
				var message = "";
				message = ErrorCodes.errores(ioargs.xhr.status);
				InformativeDialogs.showAlertDialog(i18n.get("warning"), message);
			}
		}
		dojo.xhrGet(bindArgs);
	},
	
    /**
	 *  Realiza un PUT para actualizar el contenido del documento de despliegue
	 */
    putDeploy : function() {
        var url = JsonDB.deploy.id;
		if (LdShakeManager.ldShakeMode){
			var url = LdShakeManager.buildLdshakeUrl(url);
		}
        var xhrArgs = {
            url : url,
            method : "PUT",
            putData : dojo.toJson(JsonDB.deploy),
            handleAs : "json",
            headers : {
                "Content-Type" : "application/json"
            },
            load : function(data) {
            	DeploymentPainter.paint();
            	//Indicamos al undo manager que se ha realizado una nueva acción (para que se pueda deshacer)
            	UndoManager.newEditionAction();
            },
            error : function(error, ioargs) {
            	//alert("put Error");
            }
        }
        var deferred = dojo.xhrPut(xhrArgs);
    },
    
    getDesignName: function() {
    	return JsonDB.deploy.design.name;
    },
    
    getDeploymentName: function() {
    	return JsonDB.deploy.name;
    },
    
    getDeploymentId: function() {
    	return JsonDB.deploy.id;
    },
    
    getDeploymentType: function() {
    	return JsonDB.deploy.type;
    },
    
    getCourseId: function(){
    	if (JsonDB.deploy.course)
    	{
    		return JsonDB.deploy.course.id;
    	}
    	else{
    		return false;
    	}
    },
    
    getCourseName: function(){
    	if (JsonDB.deploy.course)
    	{
    		return JsonDB.deploy.course.name;
    	}
    	else{
    		return false;
    	}
    },
    
    /**  Guarda la url del despliegue
	 */
    setDeployURL : function(url) {
    	JsonDB.deploy.deployURL = url;
    },
    
    getDeployURL : function() {
    	if (JsonDB.deploy.deployURL)
    	{
    		return JsonDB.deploy.deployURL;
    	}
    	else{
    		return false;
    	}
    },
    
    getStartingSection : function() {
    	if (JsonDB.deploy.deployData)
    	{  		
    		return JsonDB.deploy.deployData.split("=")[1].split(";")[0];
    	}
    	else{
    		return false;
    	}
    },
    
    setStartingSection : function(number) {
    	JsonDB.deploy.deployData = "startingSection=" + number + ";";
    },
    
    unsetStartingSection : function(){
    	delete JsonDB.deploy.deployData;
    },
    
    getParticipants : function(){
    	if (typeof (JsonDB.deploy.participants) !== "undefined"){
    		return JsonDB.deploy.participants;
    	}
    	else{
    		return new Array();
    	}
    }
    
   };