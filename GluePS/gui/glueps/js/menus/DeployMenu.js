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

var DeployMenu = {
				 
	init : function() {
		
		// Asignar Funcionalidad a botón de confirmación de borrado de despliegue
		dojo.connect(dojo.byId("dialogConfirmDeleteDeployOk"), "onclick", function(){
			var deployId = dijit.byId("deleteDeployId").getValue();
			DeployMenu.deleteDeploy(deployId);
		});
		
		// Asignar Funcionalidad a botón de cancelar borrado de despliegue
		dojo.connect(dojo.byId("dialogConfirmDeleteDeployCancel"), "onclick", function(){
			DeployMenu.hideDeleteDeploy();
		});
	 },
	    
    /**
     * Menú de opciones de un despliegue
     * @param data
     */
    getDeployMenu: function(data) {
        var items = new Array();
        items.push({
            label: i18n.get("editDeployment"),
            icon: "edit",
            onClick: function(data) { 
            	window.open("deploy.html?deployId=" + data.idDeploy, "");
            },
            data: data,
            help: i18n.get("editDeploymentInfo")
        });
        items.push({
            label: i18n.get("deleteDeployment"),
            icon: "delete",
            onClick: function(data) {
            	DeployMenu.showDeleteDeploy(data.idDeploy);
            },
            data: data,
            help: i18n.get("deleteDeploymentInfo")
        }); 
        items.push({
            label: i18n.get("exportDeployment"),
            icon: "export_icon",
            onClick: function(data) {
            	var baseUrl = window.location.href.split("/GLUEPSManager")[0];
            	var url = baseUrl + "/GLUEPSManager/deploys/" + data.idDeploy;
            	window.open(url);
            },
            data: data,
            help: i18n.get("exportDeployInfo")
        });
        items.push({
            label: "clonar despliegue",
            icon: "clone",
            onClick: function(data) { 
            	CloneDeployDialog.showDialog(data.idDeploy, data.nameDeploy);
            },
            data: data,
            help: "clonar despliegue"
        }); 
        
        return items;
    },
    
	
	/**
	 * Muestra la ventana de confirmación del borrado de un despliegue
	 * @param idDeploy Identificador del despliegue que se quiere borrar
	 */
	showDeleteDeploy: function(idDeploy)
	{
        var dlg = dijit.byId("dialogConfirmDeleteDeploy");
        dlg.titleNode.innerHTML = i18n.get("confirmDelete");
        dojo.byId("dialogConfirmDeleteDeployInfo").innerHTML= i18n.get("confirmDeleteDeploy");
        dlg.show();
		dijit.byId("deleteDeployId").setValue(idDeploy);
	},
	
	/**
	 * Oculta la ventana de confirmación del borrado de un despliegue
	 */
	hideDeleteDeploy: function()
	{
		dijit.byId("dialogConfirmDeleteDeploy").hide();
	},
	

	/**
	 * Borra el despliegue cuyo id se proporciona
	 * @param idDeploy Identificador del despliegue que se quiere borrar 
	 */
	deleteDeploy: function(idDeploy)
	{
			var baseUrl = window.location.href.split("/GLUEPSManager")[0];
			DeployMenu.hideDeleteDeploy();
			Glueps.showLoadingDialog(i18n.get("deletingDeploy"));
	        var xhrArgs = {
	            url : baseUrl + "/GLUEPSManager/deploys/" + idDeploy,
	            handleAs : "text",
	            load : function(data) {
	        		dijit.byId("loading").hide();
	                //Se actualiza la pantalla de los diseños
	                Design.getJsonDesigns();
	            },
	            error : function(error, ioargs) {
	        		dijit.byId("loading").hide();	        		
					//There is a live deploy process running for that deploy
					if (ioargs.xhr.status == 503){
						Glueps.showAlertDialog(i18n.get("warning"), i18n.get("notDeletedDeployInProcess"));
					}else{
						//Another error in the deploy process
						Glueps.showAlertDialog(i18n.get("warning"), i18n.get("notDeletedDeploy"));
					}
					
	            }
	        };
	        var deferred = dojo.xhrDelete(xhrArgs);    
	},
	
    /**
	 *  Realiza un POST clonar un despliegue
	 */
    postDeploy : function(idDeploy, nameDeploy) {
    	var baseUrl = window.location.href.split("/GLUEPSManager")[0];
        var xhrArgs = {
            url : baseUrl + "/GLUEPSManager/deploys/" + idDeploy + "/clone",
            method : "POST",
            content: {
            	"NewDeployTitleName" : nameDeploy
            },
            handleAs : "json",
            load : function(data) {
            	var recentlyCreatedId=data.id.substring(data.id.lastIndexOf("/")+1)
            	Design.getJsonDesigns(recentlyCreatedId);
            },
            error : function(error, ioargs) {
            	Glueps.showAlertDialog(i18n.get("warning"), i18n.get("notDeletedDeploy"));
            }
        }
        var deferred = dojo.xhrPost(xhrArgs);
    }
	
};