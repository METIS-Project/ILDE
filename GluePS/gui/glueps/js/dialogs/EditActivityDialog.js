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