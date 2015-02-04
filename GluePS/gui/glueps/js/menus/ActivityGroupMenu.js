/**
 * Menú opciones de un grupo dentro de una instancia de actividad
 */

var ActivityGroupMenu = {

		init: function(){
			dojo.connect(dojo.byId("dialogConfirmDeleteGroupActivityOk"), "onclick", function() {
				var idActivity = dijit.byId("deleteGroupActivityId").getValue();
				var idInstancedActivity = dijit.byId("deleteGroupInstancedActivityId").getValue();
				ActivityGroupMenu.deleteGroupActivity(idActivity, idInstancedActivity);
			});
			
			dojo.connect(dojo.byId("dialogConfirmDeleteGroupActivityCancel"), "onclick", function() {
				ActivityGroupMenu.hideDeleteGroupActivity();
			});
		},
		
	/**
	 * Menú de opciones de un group
	 * 
	 * @param data
	 */
	getGroupMenu : function(data) {
		var items = new Array();
			items.push({
				label : i18n.get("activityGroupMenuEdit"),
				icon : "edit",
				onClick : function(data) {
					var groupId = data.group.getId();
					EditGroupDialog.showDialog(groupId);
				},
				data : data,
				help : i18n.get("activityGroupMenuEditHelp")
			});
			/*items.push({
				label : i18n.get("activityGroupMenuMembers"),
				icon: "students_icon",
				data : data,
				help : ActivityGroupMenu.showTooltipGroupParticipants(data.group)
			});*/
			items.push({
				label : i18n.get("activityGroupMenuDelete"),
				icon : "delete",
				onClick : function(data) {
					ActivityGroupMenu.showDeleteGroupActivity(data.activity, data.instancedActivity);
				},
				data : data,
				help : i18n.get("activityGroupMenuDeleteHelp")
			});

		return items;
	},

	/**
	 * Muestra en un tooltip los participantes pertenecientes al grupo
	 * 
	 * @param group
	 *            Grupo del que se mostrarán sus participantes
	 */
	showTooltipGroupParticipants : function(group) {
		var tooltipParticipants = new Array();
		var participants = group.getParticipants();
		// Se van incluyendo los participantes con un salto de línea en cada
		// alumno introducido
		for ( var i = 0; i < participants.length; i++) {
			tooltipParticipants.push("<br />" + participants[i].getName());
		}
		// Se añade al principio del array la cantidad de participantes para
		// poder mostrarlo en el campo help del menú
		tooltipParticipants.unshift("Participantes asignados al grupo: "
				+ participants.length);
		return tooltipParticipants;
	},
	
	/**
	 * Muestra la ventana de confirmación del borrado de un grupo de la actividad
	 */
	showDeleteGroupActivity: function(activity, instancedActivity)
	{
        var dlg = dijit.byId("dialogConfirmDeleteGroupActivity");
        dlg.titleNode.innerHTML = i18n.get("confirmDelete");
        dojo.byId("dialogConfirmDeleteGroupActivityInfo").innerHTML= i18n.get("confirmDeleteGroup");
        dlg.show();
		dijit.byId("deleteGroupActivityId").setValue(activity.getId());
		dijit.byId("deleteGroupInstancedActivityId").setValue(instancedActivity.getId());
	},
	
	/**
	 * OCulta la ventana de confirmación del borrado de un grupo
	 */
	hideDeleteGroupActivity: function()
	{
		dijit.byId("dialogConfirmDeleteGroupActivity").hide();
	},
	
	
	deleteGroupActivity: function(activityId, instancedActivityId)
	{
		ActivityGroupMenu.hideDeleteGroupActivity();
		var activity = ActivityContainer.getFinalActivity(activityId);
		var instancedActivity = InstancedActivityContainer.getInstancedActivity(instancedActivityId);
		GroupManager.deleteActivityGroup(activity, instancedActivity);
		JsonDB.notifyChanges();
	}
}