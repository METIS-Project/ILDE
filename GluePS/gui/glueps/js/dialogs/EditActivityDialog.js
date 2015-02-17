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

var EditActivityDialog = {
		
		dialogId: "editActivityDialog",
		
		/**
		 * Identificador de la actividad que se edita
		 */
		activityId: null,
		
		init: function(){
			
			dojo.connect(dojo.byId("editActivityDialogOk"), "onclick", function() {
				EditActivityDialog.check();
			});
			
			dojo.connect(dojo.byId("editActivityDialogCancel"), "onclick", function() {
				EditActivityDialog.hideDialog();
			});
			
		},
		
		showDialog : function(activityId){
			this.activityId = activityId;
			this.resetElements();
			this.internationalize();
			var dlg = dijit.byId(this.dialogId);
			dlg.show();
		},
		
		hideDialog : function(){
			this.activityId = null;
			var dlg = dijit.byId(this.dialogId);
			dlg.hide();
		},
		
		check : function(){
			var name = dijit.byId("activityNameEdit").getValue();
			if (name=="")
			{
				dojo.byId("editActivityError").innerHTML=i18n.get("noActivityName");
			}
			else{
				EditActivityDialog.editActivity();
			}
		},
		
		resetElements: function(){
			dojo.byId("editActivityError").innerHTML="";
			var activity = ActivityContainer.getFinalActivity(this.activityId);
			var name = activity.getName();
			dijit.byId("activityNameEdit").setValue(name);
			var description = activity.getDescription();
			dijit.byId("activityDescriptionEdit").setValue(description);
		},
		
		internationalize: function(){
			dojo.byId("labelActivityNameEdit").innerHTML=i18n.get("editActivityName");
			dojo.byId("labelActivityDescriptionEdit").innerHTML=i18n.get("editActivityDescription");
			dijit.byId("editActivityDialog").titleNode.innerHTML=i18n.get("editActivityTitle");
			dijit.byId("editActivityDialogOk").attr("label", i18n.get("okButton"));
			dijit.byId("editActivityDialogCancel").attr("label", i18n.get("cancelButton"));
		},
				
		editActivity: function(){
			var name = dijit.byId("activityNameEdit").getValue();
			var activity = ActivityContainer.getFinalActivity(this.activityId);
			activity.setName(name);
			var description = dijit.byId("activityDescriptionEdit").getValue();
			activity.setDescription(description);
			JsonDB.notifyChanges();
			EditActivityDialog.hideDialog();
		}		
}