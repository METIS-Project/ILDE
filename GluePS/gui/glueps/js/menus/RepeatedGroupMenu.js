/**
 * Options menu to solve the problems with a group whose name is repeated
 */

var RepeatedGroupMenu = {

	/**
	 * make the options menu to solve the problems with a group whose name is repeated
	 * @param data Group information
	 * @return menu items
	 */
	getRepeatedGroupMenu : function(data) {
		var items = new Array();
		if (RepeatedGroups.groupsSameName(data.group).length > 0){
			items.push({
				label : i18n.get("repeatedGroupChangeName"),
				//icon : "edit",
				onClick : function(data) {
					var groupId = data.group.getId();
					EditGroupDialog.showDialog(groupId);
				},
				data : data,
				help : i18n.get("repeatedGroupChangeNameHelp")
			});
		}
		if (RepeatedGroups.groupsSameNameAndParticipants(data.group).length > 0){
			items.push({
				label : i18n.get("repeatedGroupUnify"),
				//icon : "edit",
				onClick : function(data) {
					RepeatedGroups.mergeGroupsSameNameAndParticipants(data.group);
					//save the result of the merge process
					JsonDB.notifyChanges();
				},
				data : data,
				help : i18n.get("repeatedGroupUnifyHelp"),
			});
		}
		if (RepeatedGroups.groupsSameName(data.group).length > 0){
			items.push({
				label : i18n.get("repeatedGroupRename"),
				//icon : "edit",
				onClick : function(data) {
					RepeatedGroups.renameGroupsSameName(data.group);
					//save the result of the renaming process
					JsonDB.notifyChanges();
				},
				data : data,
				help : i18n.get("repeatedGroupRenameHelp"),
			});
		}
		return items;
	}
}