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