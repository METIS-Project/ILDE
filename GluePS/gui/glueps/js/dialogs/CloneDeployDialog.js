/**
 * 
 */

var CloneDeployDialog = {
		
		dialogId: "cloneDeployDialog",
		
		/**
		 * Identifier of the deploy that will be cloned
		 */
		deployId: null,
		/**
		 * Name of the group that will be cloned
		 */
		deployName: null,
		
		init: function(){
			
			dojo.connect(dojo.byId("cloneDeployDialogOk"), "onclick", function() {
				CloneDeployDialog.check();
			});
			
			dojo.connect(dojo.byId("cloneDeployDialogCancel"), "onclick", function() {
				CloneDeployDialog.hideDialog();
			});
			
		},
		
		showDialog : function(deployId, deployName){
			this.deployId = deployId;
			this.deployName = deployName;
			this.resetElements();
			this.internationalize();
			var dlg = dijit.byId(this.dialogId);
			dlg.show();
		},
		
		hideDialog : function(){
			this.deployId = null;
			this.deployName = null;
			var dlg = dijit.byId(this.dialogId);
			dlg.hide();
		},
		
		check : function(){
			var name = dijit.byId("deployName").getValue();
			if (name=="")
			{
				dojo.byId("cloneDeployError").innerHTML=i18n.get("cloneDeployErrorNoName");
			}else{
					CloneDeployDialog.cloneDeploy();
			}
		},
		
		resetElements: function(){
			dijit.byId("deployName").setValue(this.deployName);
			dojo.byId("cloneDeployError").innerHTML="";
		},
		
		internationalize: function(){
			dijit.byId("cloneDeployDialog").titleNode.innerHTML=i18n.get("cloneDeployDialogTitle");
			dojo.byId("labelDeployName").innerHTML=i18n.get("cloneDeployDialogDeployName");
			dijit.byId("cloneDeployDialogOk").attr("label", i18n.get("okButton"));
			dijit.byId("cloneDeployDialogCancel").attr("label", i18n.get("cancelButton"));
		},
				
		cloneDeploy: function(){
			var name = dijit.byId("deployName").getValue();
			DeployMenu.postDeploy(this.deployId, name);
			CloneDeployDialog.hideDialog();
		}		
}