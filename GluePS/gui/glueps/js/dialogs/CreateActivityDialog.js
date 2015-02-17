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

var CreateActivityDialog = {
		
		dialogId: "createActivityDialog",
		
		/**
		 * Identificador de la actividad respecto a la que se crea 
		 */
		activityId: null,
		/**
		 * La actividad se crea despúes de la actual (true) o antes (false)
		 */
		after: true,
		
		init: function(){
			
			dojo.connect(dojo.byId("createActivityDialogOk"), "onclick", function() {
				CreateActivityDialog.check();
			});
			
			dojo.connect(dojo.byId("createActivityDialogCancel"), "onclick", function() {
				CreateActivityDialog.hideDialog();
			});
			
		},
		
		showDialog : function(activityId, after){
			this.activityId = activityId;
			this.after = after;
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
			var name = dijit.byId("activityNameCreate").getValue();
			if (name=="")
			{
				dojo.byId("createActivityError").innerHTML=i18n.get("noActivityName");
			}
			else{
				CreateActivityDialog.createActivity();
			}
		},
		
		resetElements: function(){
			dojo.byId("createActivityError").innerHTML="";
			var name = "";
			dijit.byId("activityNameCreate").setValue(name);
			var description = "";
			dijit.byId("activityDescriptionCreate").setValue(description);
		},
		
		internationalize: function(){
			dojo.byId("labelActivityNameCreate").innerHTML=i18n.get("createActivityName");
			dojo.byId("labelActivityDescriptionCreate").innerHTML=i18n.get("createActivityDescription");
			dijit.byId("createActivityDialog").titleNode.innerHTML=i18n.get("createActivityTitle");
			dijit.byId("createActivityDialogOk").attr("label", i18n.get("okButton"));
			dijit.byId("createActivityDialogCancel").attr("label", i18n.get("cancelButton"));
		},
				
		createActivity: function(){
			var activity = ActivityContainer.getFinalActivity(this.activityId);
			
			var name = dijit.byId("activityNameCreate").getValue();
			var description = dijit.byId("activityDescriptionCreate").getValue();
			
			if (activity)
			{
				ActivityContainer.addFinalActivity(name, description, activity, this.after);
			}
			else{
				ActivityContainer.addFirstFinalActivity(name, description);
			}
			JsonDB.notifyChanges();
			
			CreateActivityDialog.hideDialog();
		}		
}