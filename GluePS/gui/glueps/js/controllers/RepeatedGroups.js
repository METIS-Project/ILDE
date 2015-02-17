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
 * Functionality related to groups with the same name
 */
var RepeatedGroups = {
		
		
		/**
		 * Get the groups with the same name that the indicated one
		 * @param group the indicated group
		 * @returns the list of groups with the same name that the indicated group
		 */
		groupsSameName: function(group){
			var groupList = new Array();
			var groups = GroupContainer.getAllGroups();
			for (var i = 0; i < groups.length; i++){
				if ( group.getId()!= groups[i].getId() && this.haveSameName(group, groups[i])){
					groupList.push(groups[i]);
				}
			}
			return groupList;
		},
		
		/**
		 * Get the groups with the same name and participants that the indicated one
		 * @param group the indicated group
		 * @returns the list of groups with the same name and participants that the indicated group
		 */
		groupsSameNameAndParticipants: function(group){
			var groupList = new Array();
			var groups = GroupContainer.getAllGroups();
			for (var i = 0; i < groups.length; i++){
				if ( group.getId()!= groups[i].getId() && this.haveSameNameAndParticipants(group, groups[i])){
					groupList.push(groups[i]);
				}
			}
			return groupList;
		},
		
		/**
		 * Check if two groups have the same and participants
		 * @param group1 the first group
		 * @param group2 the second group
		 * @returns the groups have the same name and participants or not
		 */
		haveSameNameAndParticipants: function(group1, group2){
			return ( this.haveSameName(group1,group2) && this.haveSameParticipants(group1,group2));
		},
		
		/**
		 * Check if two groups have the same name
		 * @param group1 the first group
		 * @param group2 the second group
		 * @returns the groups have the same name or not
		 */
		haveSameName: function(group1, group2){
			return (group1.getName().toLowerCase()==group2.getName().toLowerCase());
		},
		
		/**
		 * Check if two groups have the same participants
		 * @param group1 the first group
		 * @param group2 the second group
		 * @returns the groups have the same participants or not
		 */		
		haveSameParticipants: function(group1, group2){
			var participantsG1 = group1.getParticipants();
			var participantsG2 = group2.getParticipants();
			if (participantsG1.length == participantsG2.length){
				for (var i = 0; i < participantsG1.length; i++){
					var found = false;
					for (var j = 0; j < participantsG2.length; j++){
						if (participantsG1[i].getName().toLowerCase()==participantsG2[j].getName().toLowerCase()){
							found = true;
							break;
						}
					}
					if (!found){
						return false;
					}
				}
				return true;
			}else{
				return false;
			}
		},
		
		/**
		 * Merge two different groups in only one. The first one will contain the resources and tool instances of the second one
		 * @param group1 the destination group of the merge
		 * @param group2 the source group of the merge
		 */
		mergeTwoGroups: function(group1, group2){
			var activities = ActivityContainer.getFinalActivities();
			for (var i = 0; i < activities.length; i++){
				var instances = activities[i].getInstancedActivities();
				var indexGroup1 = -1;
				var indexGroup2 = -1;
				for (var j = 0; j < instances.length; j++){
					if (instances[j].getGroup().getId()==group1.getId()){
						indexGroup1 = j;
					}else if (instances[j].getGroup().getId()==group2.getId()){
						indexGroup2 = j;
					}
				}				
				if (indexGroup1 == -1 && indexGroup2 != -1){
					//Only the second group is present in the activity. 
					//just change the group id of the instanced activity from the second one to the first one
					instances[indexGroup2].getData().groupId = group1.getId();
				}else if (indexGroup1 != -1 && indexGroup2 != -1){
					//both groups are present in the activity
					//add the resources of the second one in the first one
					var resourcesG2 = instances[indexGroup2].getResources();
					for (var j = 0; j < resourcesG2.length; j++){
						//check that the resource is not already included 
						if (instances[indexGroup1].usesResource(resourcesG2[j])==false){
							instances[indexGroup1].addResource(resourcesG2[j]);
						}
					}
					//add the tool instances of the second one to the first one
					var toolInstG2 = instances[indexGroup2].getToolInstances();
					for (var j = 0; j < toolInstG2.length; j++){
						instances[indexGroup1].addExistingToolInstance(toolInstG2[j]);
					}
					//delete the second one without deleting its tool instances
					InstancedActivityContainer.deleteInstancedActivity(instances[indexGroup2]);
				}//Otherwise, we don't have anything to do in this activity
			}
			//finally, delete the second group
			GroupContainer.deleteGroup(group2);			
		},
		
		
		/**
		 * Merge the different groups with the same name and participants that the specified one in this
		 * @param group the destination group of the merge
		 */
		mergeGroupsSameNameAndParticipants: function(group){
			var groupsToMerge = this.groupsSameNameAndParticipants(group);
			for (var i = 0; i < groupsToMerge.length; i++){
				this.mergeTwoGroups(group, groupsToMerge[i]);
			}
			
		},
		
		/**
		 * Merge the groups with the same name and participants
		 */
		mergeGroups: function(){
			var groups = GroupContainer.getAllGroups();
			var groupsToMerge = new Array();
			for (var i=0; i < groups.length; i++){
				var repeated = false;
				for (var j = 0; j < i; j++){
					if (this.haveSameName(groups[i], groups[j])){
						repeated = true;
						break;
					}
				}
				if (!repeated){
					groupsToMerge.push(groups[i]);
				}
			}
			for (var i=0; i < groupsToMerge.length; i++){
				//merge the group with the rest of groups with the same name and participants, if there are
				this.mergeGroupsSameNameAndParticipants(groupsToMerge[i]);
			}
			//save the result of the merge process
			//JsonDB.notifyChanges();
		},
		
		/**
		 * Rename automatically the name of the group if there is any groups with the same name
		 * @param group the group whose name wants to be renamed
		 */
		renameRepeatedGroup: function(group){
			var originalName = group.getName();
			var i = 1;
			while(this.groupsSameName(group).length > 0 ){
				var newName = originalName + "_(" + i + ")";
				group.setName(newName);
				i++;
			}
		},
		
		/**
		 * Rename the groups with the same name that the indicated one
		 * @parama group the indicated group
		 */
		renameGroupsSameName: function(group){
			var groupsToRename = this.groupsSameName(group);
			for (var i = 0; i < groupsToRename.length; i++){
				this.renameRepeatedGroup(groupsToRename[i]);
			}			
		},
		
		/**
		 * Rename automatically the name of those groups whose name is repeated
		 */
		renameGroups: function(){
			var groups = GroupContainer.getAllGroups();
			var groupsToRename = new Array();
			for (var i=0; i < groups.length; i++){
				var repeated = false;
				for (var j = 0; j < i; j++){
					if (this.haveSameName(groups[i], groups[j])){
						repeated = true;
						break;
					}
				}
				if (!repeated){
					groupsToRename.push(groups[i]);
				}
			}
			for (var i=0; i < groupsToRename.length; i++){
				this.renameGroupsSameName(groupsToRename[i]);
			}
			//save the result of the renaming process
			//JsonDB.notifyChanges();
		}
		
}