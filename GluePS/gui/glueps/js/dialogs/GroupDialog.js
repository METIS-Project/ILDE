/**
 * 
 */

var GroupDialog = {
		
		dialogId: "createGroupDialog",
		
		init: function(){
			
			dojo.connect(dojo.byId("createGroupDialogOk"), "onclick", function() {
				GroupDialog.check();
			});
			
			dojo.connect(dojo.byId("createGroupDialogCancel"), "onclick", function() {
				GroupDialog.hideDialog();
			});
		},
		
		showDialog : function(){
			this.resetElements();
			this.internationalize();
			var dlg = dijit.byId(this.dialogId);
			dlg.show();
		},
		
		hideDialog : function(){
			var dlg = dijit.byId(this.dialogId);
			dlg.hide();
		},
		
		check : function(){
			var name = dijit.byId("groupName").getValue();
			if (name=="")
			{
				dojo.byId("createGroupError").innerHTML=i18n.get("noGroupName");
			}
			else{
				var repeated = false;
				var groups = GroupContainer.getAllGroups();
				for (var i = 0; i < groups.length; i++){
					if (groups[i].getName().toLowerCase()==name.toLowerCase()){
						repeated = true;
						break;
					}
				}
				if (repeated==true){
					dojo.byId("createGroupError").innerHTML=i18n.get("repeatedGroupName");
				}else{
					GroupDialog.addGroup();
				}
			}
		},
		
		resetElements: function(){
			dijit.byId("groupName").setValue("");
			dojo.byId("createGroupError").innerHTML="";
		},		
		
		internationalize: function(){
			dojo.byId("labelGroupName").innerHTML=i18n.get("groupName");
			dijit.byId("createGroupDialog").titleNode.innerHTML=i18n.get("createGroupTitle");
			dijit.byId("createGroupDialogOk").setLabel(i18n.get("okButton"));
			dijit.byId("createGroupDialogCancel").setLabel(i18n.get("cancelButton"));
		},
				
		addGroup: function(){
			var name = dijit.byId("groupName").getValue();
			GroupContainer.addGroup(name);
			JsonDB.notifyChanges();
			GroupDialog.hideDialog();
		}		
}