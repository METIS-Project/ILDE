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
 * Funcionalidad asociada a los grupos
 */

var GroupContainer = {

	getGroup : function(groupId) {
		if (JsonDB.deploy.groups) {
			var groups = JsonDB.deploy.groups;
			for ( var i = 0; i < groups.length; i++) {
				if (groups[i].id == groupId) {
					return new Group(groups[i]);
				}
			}
		}
		return false;
	},

	getAllGroups : function() {
		var groupList = new Array();
		if (JsonDB.deploy.groups) {
			var groups = JsonDB.deploy.groups;
			for ( var i = 0; i < groups.length; i++) {
				groupList.push(new Group(groups[i]));
			}
		}
		return groupList;
	},

	addGroup : function(name) {
		var data = new Object();
		data.id = this.getNextGroupId();
		data.name = name;
		if (JsonDB.deploy.groups) {
			JsonDB.deploy.groups.push(data);
			GroupContainer.sortGroups();
		} else {
			JsonDB.deploy.groups = new Array();
			JsonDB.deploy.groups.push(data);
		}
	},

	deleteGroup : function(group) {
		if (JsonDB.deploy.groups) {
			var groups = JsonDB.deploy.groups;
			for ( var i = 0; i < groups.length; i++) {
				if (groups[i].id == group.getId()) {
					groups.splice(i, 1);
					return true;
				}
			}
		}
		return false;
	},

	getNextGroupId : function() {
		var maxId = 0;
		if (JsonDB.deploy.groups) {
			var groups = JsonDB.deploy.groups;
			for ( var i = 0; i < groups.length; i++) {
				var id = groups[i].id;
				if (groups[i].id.length > 4
						&& groups[i].id.substring(0, 4) == "GID_") {
					var id = parseInt(groups[i].id.substring(4));
					if (id > maxId) {
						maxId = id;
					}
				}
			}
		}
		return "GID_" + parseInt(maxId + 1);
	},
	
	sortGroups: function() {
		if (JsonDB.deploy.groups) {
			JsonDB.deploy.groups.sort(natcompareByName);
		}
	}
	
}