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
 * 
 */

var ActivityMenu = {
		
		init: function(){
			dojo.connect(dojo.byId("dialogConfirmDeleteActivityOk"), "onclick", function() {
				var activityId = dijit.byId("deleteActivityId").getValue();
				var activity = ActivityContainer.getFinalActivity(activityId);
				ActivityMenu.hideDeleteActivity();
				ActivityMenu.deleteActivity(activity);
			});
			
			dojo.connect(dojo.byId("dialogConfirmDeleteActivityCancel"), "onclick", function() {
				ActivityMenu.hideDeleteActivity();
			});
		},		
		
		/**
		 * Menú de opciones de una actividad
		 * 
		 * @param data
		 */
		getActivityMenu : function(data) {
			var items = new Array();
			items.push({
				label : i18n.get("activityMenuEdit"),
				icon: "edit",
				onClick : function(data) {

				},
				data : data,
				help : i18n.get("activityMenuEditHelp")
			});
	        items.push({
	            label: i18n.get("activityMenuDescription"),

	            data: data,
	            help: i18n.get("activityMenuDescriptionHelp")
	        });
			items.push({
				label : i18n.get("activityMenuAddBefore"),
				icon: "edit",
				onClick : function(data) {

				},
				data : data,
				help : i18n.get("activityMenuAddBeforeHelp")
			});
			items.push({
				label : i18n.get("activityMenuAddAfter"),
				icon: "edit",
				onClick : function(data) {

				},
				data : data,
				help : i18n.get("activityMenuAddAfterHelp")
			});
			items.push({
				label : i18n.get("activityMenuDelete"),
				icon: "delete",
				onClick : function(data) {

				},
				data : data,
				help : i18n.get("activityMenuDeleteHelp")
			});
			return items;
		},
		
		
		/**
		 * Muestra la ventana de confirmación del borrado de una actividad
		 */
		showDeleteActivity: function(activity)
		{
	        var dlg = dijit.byId("dialogConfirmDeleteActivity");
	        dlg.titleNode.innerHTML = i18n.get("confirmDelete");
	        dojo.byId("dialogConfirmDeleteActivityInfo").innerHTML= i18n.get("confirmDeleteActivity");
	        dlg.show();
			dijit.byId("deleteActivityId").setValue(activity.getId());
		},
		
		/**
		 * OCulta la ventana de confirmación del borrado de un grupo
		 */
		hideDeleteActivity: function()
		{
			dijit.byId("dialogConfirmDeleteActivity").hide();
		},
		
		deleteActivity: function(activity)
		{
			var instancedActivities = activity.getInstancedActivities();
			//Borramos todas las instancias de la actividad
			for (var i = 0; i < instancedActivities.length; i++)
			{
				//TODO asegurarse que todas las instancias se borran correctamente o mostrar un mensaje
				GroupManager.deleteActivityGroup(activity, instancedActivities[i]);
			}	
			ActivityContainer.deleteFinalActivity(activity.getId());
			JsonDB.notifyChanges();	
		}
}