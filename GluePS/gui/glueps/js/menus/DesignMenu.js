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

var DesignMenu = {
				
		init: function(){			
			// Asignar Funcionalidad a botón de confirmación de borrado de diseño
			dojo.connect(dojo.byId("dialogConfirmDeleteDesignOk"), "onclick", function(){
				var designId = dijit.byId("deleteDesignId").getValue();
				DesignMenu.deleteDesign(designId);
			});
			
			// Asignar Funcionalidad a botón de cancelar borrado de diseño
			dojo.connect(dojo.byId("dialogConfirmDeleteDesignCancel"), "onclick", function(){
				DesignMenu.hideDeleteDesign();
			});
		},		
		
	    /**
	     * Menú de opciones de un diseño
	     * @param data
	     */
	    getDesignMenu: function(data) {
	        var items = new Array();
	        items.push({
	            label: i18n.get("MyListDesignNewCreateDeployButton"),
	            icon: "edit",
	            onClick: function(data) {
	            	Deploy.anadirTitle(data.id, data.design);
	            },
	            data: data,
	            help: i18n.get("newDeployInfo")
	        });
	        items.push({
	            label: i18n.get("deleteDesign"),
	            icon: "delete",
	            onClick: function(data) {
	            	DesignMenu.showDeleteDesign(data.id);
	            },
	            data: data,
	            help: i18n.get("deleteDesignInfo")
	        });
	        items.push({
	            label: i18n.get("exportDesign"),
	            icon: "export_icon",
	            onClick: function(data) {
	            	var baseUrl = window.location.href.split("/GLUEPSManager")[0];
	            	var url = baseUrl + "/GLUEPSManager/designs/" + data.id;
	            	window.open(url);
	            },
	            data: data,
	            help: i18n.get("exportDesignInfo")
	        }); 
	        return items;
	    },
				
		/**
		 * Muestra la ventana de confirmación del borrado de un diseño
		 * @param idDesign Identificador del diseño que se quiere borrar
		 */
		showDeleteDesign: function(idDesign)
		{
	        var dlg = dijit.byId("dialogConfirmDeleteDesign");
	        dlg.titleNode.innerHTML = i18n.get("confirmDelete");
	        dojo.byId("dialogConfirmDeleteDesignInfo").innerHTML= i18n.get("confirmDeleteDesign");
	        dlg.show();
			dijit.byId("deleteDesignId").setValue(idDesign);
		},
		
		/**
		 * Oculta la ventana de confirmación del borrado de un diseño
		 */
		hideDeleteDesign: function()
		{
			dijit.byId("dialogConfirmDeleteDesign").hide();
		},
		
		
		/**
		 * Borra el diseño cuyo id se proporciona
		 * @param idDesign Identificador del diseño que se quiere borrar
		 */
		deleteDesign: function(idDesign)
		{
				var baseUrl = window.location.href.split("/GLUEPSManager")[0];
				DesignMenu.hideDeleteDesign();
				Glueps.showLoadingDialog(i18n.get("deletingDesign"));
		        var xhrArgs = {
		            url : baseUrl + "/GLUEPSManager/designs/" + idDesign,
		            handleAs : "text",
		            load : function(data) {
		        		dijit.byId("loading").hide();
		                //Se actualiza la pantalla de los diseños
		                Design.getJsonDesigns();
		            },
		            error : function() {
		        		dijit.byId("loading").hide();
		        		Glueps.showAlertDialog(i18n.get("warning"), i18n.get("notDeletedDesign"));
		            }
		        };
		        var deferred = dojo.xhrDelete(xhrArgs);    
		}

}