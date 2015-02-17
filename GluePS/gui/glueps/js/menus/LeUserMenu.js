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

/**
 * Menú de un LE de un usuario
 */

var LeUserMenu = {
		
	init: function(){			
		// Asignar Funcionalidad a botón de confirmación de borrado de LE
		dojo.connect(dojo.byId("dialogConfirmDeleteLeOk"), "onclick", function(){
			var idLe = dijit.byId("deleteLeId").getValue();
			LeUserMenu.deleteLe(idLe);
		});
			
		// Asignar Funcionalidad a botón de cancelar borrado de LE
		dojo.connect(dojo.byId("dialogConfirmDeleteLeCancel"), "onclick", function(){
			LeUserMenu.hideDeleteLe();
		});
	},

	/**
	 * Menú Desplegable para editar y eliminar LEs del usuario
	 */
	getLeUserOptions : function(data) {
		var options = new Array();
		options.push({
			label : i18n.get("MenuEditLe"),
			icon : "editlo",
			onClick : function(data) {
				var idLe = data.res.getElementsByTagName("id")[0].childNodes[0].nodeValue;
				EditLeDialog.showDialog(idLe);
			},
			data : data,
			help : i18n.get("MenuEditLeInfo")
		});
		options.push({
			isSeparator : true
		});
		options.push({
			label : i18n.get("MenuDeleteLe"),
			icon : "delete",
			onClick : function(data) {
				var idLe = data.res.getElementsByTagName("id")[0].childNodes[0].nodeValue;
				//idLe = idLe.substring(idLe.lastIndexOf("/") + 1);
				LeUserMenu.showDeleteLe(idLe);
			},
			data : data,
			help : i18n.get("MenuDeleteInfo"),
		});
		return options;
	},
	
	/**
	 * Borra el LE cuyo id se proporciona
	 * @param idLe Identificador del LE que se quiere borrar
	 */
	deleteLe: function(idLe)
	{
		//var baseUrl = window.location.href.split("/GLUEPSManager")[0];
		//DeployMenu.hideDeleteDeploy();
		Glueps.showLoadingDialog(i18n.get("deletingDeploy"));
	    var xhrArgs = {
	    	//url : baseUrl + "/GLUEPSManager/learningEnvironments/" + idLe,
	    	url: idLe,
	        handleAs : "text",
	        load : function(data) {
	        	dijit.byId("loading").hide();
	        	LeUserMenu.hideDeleteLe();
	            //Se actualiza la pantalla de los LE
	            VleManagement.displayLeUser();
	            //Se actualiza los LE de subir despliegue
	            ImportDeploy.getLEnvironments();
	            ImportDeploy.resetSelectClassVle();
	        },
	        error : function() {
	        	dijit.byId("loading").hide();
	        	Glueps.showAlertDialog(i18n.get("warning"), i18n.get("notDeletedDeploy"));
	        }
	    };
	    var deferred = dojo.xhrDelete(xhrArgs);    
	},
	
	/**
	 * Muestra la ventana de confirmación del borrado de un LE
	 * @param idLe Identificador del LE que se quiere borrar
	 */
	showDeleteLe: function(idLe)
	{
        var dlg = dijit.byId("dialogConfirmDeleteLe");
        dlg.titleNode.innerHTML = i18n.get("confirmDelete");
        dojo.byId("dialogConfirmDeleteLeInfo").innerHTML= i18n.get("confirmDeleteLe");
        dojo.byId("dialogConfirmDeleteLeOk").innerHTML = i18n.get("okButton");
        dojo.byId("dialogConfirmDeleteLeCancel").innerHTML = i18n.get("cancelButton");
        dlg.show();
		dijit.byId("deleteLeId").setValue(idLe);
	},
	
	/**
	 * Oculta la ventana de confirmación del borrado de un LE
	 */
	hideDeleteLe: function()
	{
		dijit.byId("dialogConfirmDeleteLe").hide();
	}

}