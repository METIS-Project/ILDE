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