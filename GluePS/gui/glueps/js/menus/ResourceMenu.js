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
 * Funcionalidad asociada al menú de un recurso
 */

var ResourceMenu = {
		
		init: function(){
			dojo.connect(dojo.byId("dialogConfirmDeleteResourceOk"), "onclick", function() {
				var idResource = dijit.byId("deleteResourceId").getValue();
				ResourceMenu.deleteResource(idResource);
			});
			
			dojo.connect(dojo.byId("dialogConfirmDeleteResourceCancel"), "onclick", function() {
				ResourceMenu.hideDeleteResource();
			});
		},
		
		/**
		 * Menú de opciones de un recurso
		 * 
		 * @param data
		 */
		getResourceMenu : function(data) {
			var items = new Array();
			items.push({
				label :i18n.get("resourceMenuEdit"),
				icon: "edit",
				onClick : function(data) {
					GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
					EditResourceDialog.showDialog(data.resource.getId());
				},
				data : data,
				help : i18n.get("resourceMenuEditHelp")
			});
			if (!StateManager.isSelectedResource(data.resource))
			{
				items.push({
					label : i18n.get("resourceMenuActivity"),
					icon: "document_ico.png",
					onClick : function(data) {					
						StateManager.changeResourceState(data.resource);
						var trResource = dojo.byId("tr" + ResourcesPainter.identifiers[data.resource.getId()]);
						trResource.setAttribute("class", "resourceSelected");
					},
					data : data,
					help : i18n.get("resourceMenuActivityResourceHelp")
				});
			}
			else{
				items.push({
					label : i18n.get("resourceMenuActivity"),
					icon: "document_ico.png",
					onClick : function(data) {
						StateManager.changeResourceState(data.resource);
						var trResource = dojo.byId("tr" + ResourcesPainter.identifiers[data.resource.getId()]);
						trResource.setAttribute("class", "");
					},
					data : data,
					help : i18n.get("resourceMenuActivityResourceHelpUnselect")
				});
			}
			items.push({
				label : i18n.get("resourceMenuDelete"),
				icon: "delete",
				onClick : function(data) {
					ResourceMenu.showDeleteResource(data.resource);
				},
				data : data,
				help : i18n.get("resourceMenuDeleteHelp")
			});
			return items;
		},
		
		
		/**
		 * Muestra la ventana de confirmación del borrado de un recurso
		 * @param resource Recurso a borrar
		 */
		showDeleteResource: function(resource)
		{
	        var dlg = dijit.byId("dialogConfirmDeleteResource");
	        dlg.titleNode.innerHTML = i18n.get("confirmDelete");
	        dojo.byId("dialogConfirmDeleteResourceInfo").innerHTML= i18n.get("confirmDeleteResource");
	        dlg.show();
			dijit.byId("deleteResourceId").setValue(resource.getId());
		},
		
		/**
		 * OCulta la ventana de confirmación del borrado de un recurso
		 */
		hideDeleteResource: function()
		{
			dijit.byId("dialogConfirmDeleteResource").hide();
		},
		
		
		deleteResource: function(resourceId)
		{
			ResourceMenu.hideDeleteResource();
			var resource = ResourceContainer.getResource(resourceId);			
			ResourceManager.deleteResource(resource);
			JsonDB.notifyChanges();
		}
}