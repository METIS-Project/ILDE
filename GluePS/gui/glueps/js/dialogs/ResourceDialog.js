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
 * Gestión del diálogo de creación de un nuevo recurso
 */

var ResourceDialog = {
		
		dialogId: "createResourceDialog",
		
		init: function(){
			
			dojo.connect(dojo.byId("createResourceDialogOk"), "onclick", function() {
				ResourceDialog.check();
			});
			
			dojo.connect(dojo.byId("createResourceDialogCancel"), "onclick", function() {
				ResourceDialog.hideDialog();
			});
			
			dojo.connect(dojo.byId("rbResource"), "onclick", function() {
				ResourceDialog.showResource();
			});
			
			dojo.connect(dojo.byId("rbResource"), "onchange", function() {
				dojo.byId("createResourceError").innerHTML="";
			});
			
			dojo.connect(dojo.byId("rbTool"), "onclick", function() {
				ResourceDialog.showTool();
			});
			
			dojo.connect(dojo.byId("rbTool"), "onchange", function() {
				dojo.byId("createResourceError").innerHTML="";
			});
		},
		
		/**
		 * Genera las opciones del select donde se elige el tipo de herramienta
		 */
		buildSelectToolType: function(){
			var internalTools = LearningEnvironment.getInternalTools();
			var externalTools = LearningEnvironment.getExternalTools();
			
			internalTools.sort(natcompareByToolName);
			externalTools.sort(natcompareByToolName);
			
	        var toolTypeSelect = dijit.byId("selectToolType");
	        while (toolTypeSelect.getOptions(0)!=null){
	        	toolTypeSelect.removeOption(toolTypeSelect.getOptions(0));
	        }
	        toolTypeSelect.addOption({
	            label: i18n.get("selectToolType"),
	            value: ""
	        });
	        for (var i=0; i < internalTools.length; i++) {
	            var it = internalTools[i];
	            toolTypeSelect.addOption({
	                label: it.value,
	                value: it.key
	            });
	        }
	        for (var i=0; i < externalTools.length; i++) {
	            var et = externalTools[i];
	            toolTypeSelect.addOption({
	                label: et.value,
	                value: et.key
	            });
	        }
			
		},
		
		showDialog : function(){
			this.resetElements();
			this.internationalize();
			var dlg = dijit.byId(this.dialogId);
			this.buildSelectToolType();
			this.showResource();
			dlg.show();
			
		},
		
		showResource: function(){
			dojo.style("createResourceDivResource","display","");
			dojo.style("createResourceDivTool","display","none");			
		},
		
		showTool: function(){
			dojo.style("createResourceDivResource","display","none");
			dojo.style("createResourceDivTool","display","");			
		},
		
		hideDialog : function(){
			var dlg = dijit.byId(this.dialogId);
			dlg.hide();
		},
		
		check: function(){
			if (dojo.byId("rbResource").checked)
			{
				this.checkResource();
			}
			else{
				this.checkTool();
			}
		},
		
		checkResource : function(){
			var error = false;
			var name = dijit.byId("resourceName").getValue();
			if (name=="" && !error)
			{
				dojo.byId("createResourceError").innerHTML=i18n.get("noResourceName");
				error =true;
			}
			var location = dijit.byId("resourceLocation").getValue();
			if (location=="" && !error)
			{
				dojo.byId("createResourceError").innerHTML=i18n.get("noLocation");
				error =true;
			}
			
			if (!error){
				ResourceDialog.addResource();
			}
		},
		
		checkTool : function(){
			var error = false;
			var name = dijit.byId("toolName").getValue();
			if (name=="" && !error)
			{
				dojo.byId("createResourceError").innerHTML=i18n.get("noToolName");
				error =true;
			}
			var toolType = dijit.byId("selectToolType").getValue();
			if (toolType=="" && !error)
			{
				dojo.byId("createResourceError").innerHTML=i18n.get("noToolType");
				error =true;
			}
			
			if (!error){
				ResourceDialog.addTool();
			}
		},
		
		resetElements: function(){
			dojo.byId("rbResource").checked=true;
			dojo.byId("rbTool").checked=false;
			dijit.byId("resourceName").setValue("");
			dijit.byId("resourceLocation").setValue("");
			dijit.byId("toolName").setValue("");
			dojo.byId("createResourceError").innerHTML="";
		},		
		
		internationalize: function(){
			dojo.byId("labelRadioCreateResource").innerHTML=i18n.get("labelRadioCreateResource");
			dojo.byId("labelRbResource").innerHTML=i18n.get("rbResource");
			dojo.byId("labelRbTool").innerHTML=i18n.get("rbTool");
			dojo.byId("labelResourceName").innerHTML=i18n.get("resourceName");
			dojo.byId("labelResourceLocation").innerHTML=i18n.get("resourceLocation");
			dojo.byId("labelToolName").innerHTML=i18n.get("toolName");
			dojo.byId("labelToolType").innerHTML=i18n.get("toolType");
			dijit.byId("createResourceDialog").titleNode.innerHTML=i18n.get("createResourceTitle");
			dijit.byId("createResourceDialogOk").setLabel(i18n.get("okButton"));
			dijit.byId("createResourceDialogCancel").setLabel(i18n.get("cancelButton"));
		},
				
		addResource: function(){
			var name = dijit.byId("resourceName").getValue();
			var location = dijit.byId("resourceLocation").getValue();
			ResourceContainer.addResource(name, location);
			JsonDB.notifyChanges();
			ResourceDialog.hideDialog();
		},
		
		addTool: function(){
			var toolType = dijit.byId("selectToolType").getValue();
			var name = dijit.byId("toolName").getValue();
			if (LearningEnvironment.isInternal(toolType))
			{
				var toolKind = "internal";
			}
			else{
				if (LearningEnvironment.isExternal(toolType)){
					var toolKind = "external";
				}
				else{
					var toolKind = "undefined";
				}
			}
			ToolContainer.addTool(name, toolKind, toolType);
			JsonDB.notifyChanges();
			ResourceDialog.hideDialog();
		}	
}