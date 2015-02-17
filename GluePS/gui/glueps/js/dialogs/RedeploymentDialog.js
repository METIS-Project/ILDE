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
 * Dialog that is shown when we want to deploy (only in Blogger by now) and the deploy had been deployed previously
 */
var RedeploymentDialog = {
		
		dialogId: "redeploymentDialog",
		
		init: function(){
			
			dojo.connect(dojo.byId("redeploymentDialogRedeploy"), "onclick", function() {
				RedeploymentDialog.hideDialog();
				var leValue = LearningEnvironment.getLearningEnvironmentId();
			    if (LearningEnvironment.getLearningEnvironmentType()=="Blogger" && OauthManager.oauthRequired(leValue)){
	    			var callerMethod = "postdep";
	    			var deployId = JsonDB.getDeploymentId();
	    			//start the oauth process in order to have an updated access token which let us deploy the course
	    			//we provide a name for the callerMethod and the id of the deploy
	    			OauthManager.startOauth("callerMethod=" + callerMethod + "&leId=" + leValue + "&isNewDeploy=false&isRedeploy=true");
		    	}else{
		    		DeploymentController.completeDeployment(false, true);
		    	}
			});
			
			dojo.connect(dojo.byId("redeploymentDialogNewDeploy"), "onclick", function() {
				RedeploymentDialog.hideDialog();
				var leValue = LearningEnvironment.getLearningEnvironmentId();
			    if (LearningEnvironment.getLearningEnvironmentType()=="Blogger" && OauthManager.oauthRequired(leValue)){
	    			var callerMethod = "postdep";
	    			var deployId = JsonDB.getDeploymentId();
	    			//start the oauth process in order to have an updated access token which let us deploy the course
	    			//we provide a name for the callerMethod and the id of the deploy
	    			OauthManager.startOauth("callerMethod=" + callerMethod + "&leId=" + leValue + "&isNewDeploy=true&isRedeploy=false");
		    	}else{
		    		DeploymentController.completeDeployment(true, false);
		    	}
			});
			
			dojo.connect(dojo.byId("redeploymentDialogCancel"), "onclick", function() {
				RedeploymentDialog.hideDialog();
			});
		},
		
		showDialog : function(){
			this.internationalize();
			var dlg = dijit.byId(this.dialogId);
			dlg.show();
		},
		
		hideDialog : function(){
			var dlg = dijit.byId(this.dialogId);
			dlg.hide();
		},	
		
		internationalize: function(){
			dijit.byId("redeploymentDialog").titleNode.innerHTML=i18n.get("redeploymentDialogTitle");
			dojo.byId("redeploymentDialogInfo").innerHTML=i18n.get("redeploymentDialogInfo");
			dijit.byId("redeploymentDialogNewDeploy").setLabel(i18n.get("redeploymentDialogNewDeploy"));
			dijit.byId("redeploymentDialogRedeploy").setLabel(i18n.get("redeploymentDialogRedeploy"));
			dijit.byId("redeploymentDialogCancel").setLabel(i18n.get("cancelButton"));
		}	
}