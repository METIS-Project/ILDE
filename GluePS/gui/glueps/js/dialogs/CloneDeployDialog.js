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