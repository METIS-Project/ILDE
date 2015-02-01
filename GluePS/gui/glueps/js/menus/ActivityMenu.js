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