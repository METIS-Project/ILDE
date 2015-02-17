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

var EditGroupDialog = {
		
		dialogId: "editGroupDialog",
		
		/**
		 * Identificador del grupo que se edita
		 */
		groupId: null,
		
		init: function(){
			
			dojo.connect(dojo.byId("editGroupDialogOk"), "onclick", function() {
				EditGroupDialog.check();
			});
			
			dojo.connect(dojo.byId("editGroupDialogCancel"), "onclick", function() {
				EditGroupDialog.hideDialog();
			});
			
		},
		
		showDialog : function(groupId){
			this.groupId = groupId;
			this.resetElements();
			this.internationalize();
			var dlg = dijit.byId(this.dialogId);
			dlg.show();
		},
		
		hideDialog : function(){
			this.groupId = null;
			var dlg = dijit.byId(this.dialogId);
			dlg.hide();
		},
		
		check : function(){
			var name = dijit.byId("groupNameEdit").getValue();
			if (name=="")
			{
				dojo.byId("editGroupError").innerHTML=i18n.get("noGroupName");
			}else{
				var group = GroupContainer.getGroup(this.groupId);
				var currentName = group.getName();
				group.setName(name);
				if (RepeatedGroups.groupsSameName(group).length > 0){
					dojo.byId("editGroupError").innerHTML=i18n.get("repeatedGroupName");
					//restore the previous name
					group.setName(currentName);
				}else{
					EditGroupDialog.editGroup();
				}
			}
		},
		
		resetElements: function(){
			var group = GroupContainer.getGroup(this.groupId);
			var name = group.getName();
			dijit.byId("groupNameEdit").setValue(name);
			dojo.byId("editGroupError").innerHTML="";
		},
		
		internationalize: function(){
			dojo.byId("labelGroupNameEdit").innerHTML=i18n.get("editGroupName");
			dijit.byId("editGroupDialog").titleNode.innerHTML=i18n.get("editGroupTitle");
			dijit.byId("editGroupDialogOk").attr("label", i18n.get("okButton"));
			dijit.byId("editGroupDialogCancel").attr("label", i18n.get("cancelButton"));
		},
				
		editGroup: function(){
			var name = dijit.byId("groupNameEdit").getValue();
			var group = GroupContainer.getGroup(this.groupId);
			group.setName(name);
			JsonDB.notifyChanges();
			EditGroupDialog.hideDialog();
		}		
}