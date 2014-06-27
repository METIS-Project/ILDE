/**
 * Menú opciones de un grupo
 */

var GroupMenu = {
		
	init: function(){
		dojo.connect(dojo.byId("dialogConfirmDeleteGroupOk"), "onclick", function() {
			var idGroup = dijit.byId("deleteGroupId").getValue();
			GroupMenu.deleteGroup(idGroup);
		});
		
		dojo.connect(dojo.byId("dialogConfirmDeleteGroupCancel"), "onclick", function() {
			GroupMenu.hideDeleteGroup();
		});
	},

	/**
	 * Construye el menú de opciones de un group
	 * 
	 * @param data
	 *            Información del grupo
	 * @return Items del menú
	 */
	getGroupMenu : function(data) {
		var items = new Array();
			items.push({
				label : i18n.get("groupMenuEdit"),
				icon : "edit",
				onClick : function(data) {
					var groupId = data.group.getId();
					EditGroupDialog.showDialog(groupId);
				},
				data : data,
				help : i18n.get("groupMenuEditHelp")
			});
			if (!StateManager.isSelectedGroup(data.group))
			{
				items.push({
					label : i18n.get("groupMenuActivity"),
					icon : "students_icon.png",
					onClick : function(data) {	
						//Selección de grupo
						StateManager.changeGroupState(data.group);
						GroupsPainter.selectedGroup(data.group);
					},
					data : data,
					help : i18n.get("groupMenuActivityHelp")
				});
			}
			else
			{
				items.push({
					label : i18n.get("groupMenuActivity"),
					icon : "students_icon.png",
					onClick : function(data) {	
						//Deselección de grupo
						StateManager.changeGroupState(data.group);
						GroupsPainter.unselectedGroup(data.group);
					},
					data : data,
					help : i18n.get("groupMenuActivityHelpUnselect")
				});
			}
			items.push({
				label : i18n.get("groupMenuDelete"),
				icon : "delete",
				onClick : function(data) {
					GroupMenu.showDeleteGroup(data.group);
				},
				data : data,
				help : i18n.get("groupMenuDeleteHelp")
			});
		return items;
	},
	
	/**
	 * Muestra la ventana de confirmación del borrado de un grupo
	 * @param group Grupo a borrar
	 */
	showDeleteGroup: function(group)
	{
        var dlg = dijit.byId("dialogConfirmDeleteGroup");
        dlg.titleNode.innerHTML = i18n.get("confirmDelete");
        dojo.byId("dialogConfirmDeleteGroupInfo").innerHTML= i18n.get("confirmDeleteGroup");
        dlg.show();
		dijit.byId("deleteGroupId").setValue(group.getId());
	},
	
	/**
	 * OCulta la ventana de confirmación del borrado de un grupo
	 */
	hideDeleteGroup: function()
	{
		dijit.byId("dialogConfirmDeleteGroup").hide();
	},
	
	
	deleteGroup: function(groupId)
	{
		var group = GroupContainer.getGroup(groupId);
		var resultado = GroupManager.deleteGroup(group);
		GroupMenu.hideDeleteGroup();
		JsonDB.notifyChanges();
	}
}