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