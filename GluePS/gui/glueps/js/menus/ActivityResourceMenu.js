/**
 * 
 */

var ActivityResourceMenu = {
				
		init: function(){
			dojo.connect(dojo.byId("dialogConfirmDeleteResourceActivityOk"), "onclick", function() {
				var idActivity = dijit.byId("deleteResourceActivityId").getValue();
				var idInstancedActivity = dijit.byId("deleteResourceInstancedActivityId").getValue();
				var idResource = dijit.byId("deleteResourceResourceId").getValue();
				ActivityResourceMenu.deleteResourceActivity(idActivity, idInstancedActivity, idResource);
			});
			
			dojo.connect(dojo.byId("dialogConfirmDeleteResourceActivityCancel"), "onclick", function() {
				ActivityResourceMenu.hideDeleteResourceActivity();
			});
		},
		
		/**
		 * Menú de opciones de un recurso
		 * 
		 * @param data
		 */
		getResourceMenu : function(data) {
			var items = new Array();
			/*items.push({
				label : i18n.get("activityResourceMenuEdit"),
				icon: "edit",
				onClick : function(data) {

				},
				data : data,
				help : i18n.get("activityResourceMenuEditHelp")
			});*/
	        items.push({
	            label: i18n.get("activityResourceMenuOpen"),
	            icon: "lupa",
				onClick : function(data) {
					ActivityResourceMenu.openResource(data.resource);
				},
	            data: data,
	            help: i18n.get("activityResourceMenuOpenHelp")
	        });
			//Juan: Meter QR code en menú
			items.push({
				label: i18n.get("activityResourceMenuQR"),
				icon: "qr.png",
				onClick: function(data) {
		    		var location = data.resource.getLocation();
		    		var url = "http://qrickit.com/api/qr?qrsize=200&d=" + location;
					 window.open(url); 
				},
				data: data,
				help: i18n.get("activityResourceMenuQRHelp")
			});
			//Juan: Hasta aquí modificación
			items.push({
				label : i18n.get("activityResourceMenuDelete"),
				icon: "delete",
				onClick : function(data) {
					ActivityResourceMenu.showDeleteResourceActivity(data.activity, data.instancedActivity, data.resource);
				},
				data : data,
				help : i18n.get("activityResourceMenuDeleteHelp")
			});
			return items;
		},
		
		openResource: function(resource){
			//Los documentos pueden abrirse siempre
    		var url = resource.getLocation();
    		window.open(url);
		},
		
		/**
		 * Muestra la ventana de confirmación de un recurso de la actividad
		 */
		showDeleteResourceActivity: function(activity, instancedActivity, resource)
		{
	        var dlg = dijit.byId("dialogConfirmDeleteResourceActivity");
	        dlg.titleNode.innerHTML = i18n.get("confirmDelete");
	        dojo.byId("dialogConfirmDeleteResourceActivityInfo").innerHTML= i18n.get("confirmDeleteResourceActivity");
	        dlg.show();
			dijit.byId("deleteResourceActivityId").setValue(activity.getId());
			dijit.byId("deleteResourceInstancedActivityId").setValue(instancedActivity.getId());
			dijit.byId("deleteResourceResourceId").setValue(resource.getId());
		},
		
		/**
		 * OCulta la ventana de confirmación del borrado de un grupo
		 */
		hideDeleteResourceActivity: function()
		{
			dijit.byId("dialogConfirmDeleteResourceActivity").hide();
		},
		
		
		deleteResourceActivity: function(activityId, instancedActivityId, resourceId)
		{
			ActivityResourceMenu.hideDeleteResourceActivity();
			var activity = ActivityContainer.getFinalActivity(activityId);
			var instancedActivity = InstancedActivityContainer.getInstancedActivity(instancedActivityId);
			var resource = ResourceContainer.getResource(resourceId);
			ResourceManager.deleteResourceGroup(activity, instancedActivity, resource);
			JsonDB.notifyChanges();
		}
}