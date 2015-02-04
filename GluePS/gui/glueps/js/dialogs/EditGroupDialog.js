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