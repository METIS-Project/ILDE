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