/**
 * 
 */

var EditResourceDialog = {
		
		dialogId: "editResourceDialog",
		
		/**
		 * Identificador del recurso que se edita
		 */
		resourceId: null,
		
		init: function(){
			
			dojo.connect(dojo.byId("editResourceDialogOk"), "onclick", function() {
				EditResourceDialog.check();
			});
			
			dojo.connect(dojo.byId("editResourceDialogCancel"), "onclick", function() {
				EditResourceDialog.hideDialog();
			});
			
			dojo.connect(dojo.byId("rbResourceEdit"), "onclick", function() {
				EditResourceDialog.showResource();
			});
			
			dojo.connect(dojo.byId("rbResourceEdit"), "onchange", function() {
				dojo.byId("createResourceError").innerHTML="";
			});
			
			dojo.connect(dojo.byId("rbToolEdit"), "onclick", function() {
				EditResourceDialog.showTool();
			});
			
			dojo.connect(dojo.byId("rbToolEdit"), "onchange", function() {
				dojo.byId("createResourceError").innerHTML="";
			});
		},
		
		buildSelectToolType: function(){
			var internalTools = LearningEnvironment.getInternalTools();
			var externalTools = LearningEnvironment.getExternalTools();
			
			internalTools.sort(natcompareByToolName);
			externalTools.sort(natcompareByToolName);
			
	        var toolTypeSelect = dijit.byId("selectToolTypeEdit");
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
	        //Si el recurso es una herramienta aparece seleccionada dicho tipo de herramienta
	        tool = ToolContainer.getTool(this.resourceId);
	        if (tool){
	        	toolTypeSelect.attr("value", tool.getToolType());
	        }
			
		},
		
		showDialog : function(resourceId){
			this.resourceId = resourceId;
			this.resetElements();
			this.internationalize();
			var dlg = dijit.byId(this.dialogId);
			this.buildSelectToolType();
			if (ResourceContainer.getResource(this.resourceId))
			{
				this.showResource();
			}
			else{
				this.showTool();
			}
			dlg.show();
			
		},
		
		showResource: function(){
			dojo.style("editResourceDivResource","display","");
			dojo.style("editResourceDivTool","display","none");			
		},
		
		showTool: function(){
			dojo.style("editResourceDivResource","display","none");
			dojo.style("editResourceDivTool","display","");			
		},
		
		hideDialog : function(){
			var dlg = dijit.byId(this.dialogId);
			dlg.hide();
		},
		
		check: function(){
			if (dojo.byId("rbResourceEdit").checked)
			{
				this.checkResource();
			}
			else{
				this.checkTool();
			}
		},
		
		checkResource : function(){
			var error = false;
			var name = dijit.byId("resourceNameEdit").getValue();
			if (name=="" && !error)
			{
				dojo.byId("editResourceError").innerHTML=i18n.get("noResourceName");
				error =true;
			}
			var location = dijit.byId("resourceLocationEdit").getValue();
			if (location=="" && !error)
			{
				dojo.byId("editResourceError").innerHTML=i18n.get("noLocation");
				error =true;
			}
			
			if (!error){
				EditResourceDialog.editAsResource();
			}
		},
		
		checkTool : function(){
			var error = false;
			var name = dijit.byId("toolNameEdit").getValue();
			if (name=="" && !error)
			{
				dojo.byId("editResourceError").innerHTML=i18n.get("noToolName");
				error =true;
			}
			var toolType = dijit.byId("selectToolTypeEdit").getValue();
			if (toolType=="" && !error)
			{
				dojo.byId("editResourceError").innerHTML=i18n.get("noToolType");
				error =true;
			}
			
			if (!error){
				EditResourceDialog.editAsTool();
			}
		},
		
		resetElements: function(){
			var resource = ResourceContainer.getResource(this.resourceId);
			dojo.byId("rbResourceEdit").disabled=false;
			dojo.byId("rbToolEdit").disabled=false;
			dijit.byId("selectToolTypeEdit").setAttribute('disabled', false);
			dojo.byId("editResourceError").innerHTML="";
			dojo.style("editResourceInfo","display","none");
			dojo.byId("labelEditResourceInfo").innerHTML = "";
			if (resource)
			{
				dojo.byId("rbResourceEdit").checked=true;
				dojo.byId("rbToolEdit").checked=false;
				dijit.byId("resourceNameEdit").setValue(resource.getName());
				dijit.byId("resourceLocationEdit").setValue(resource.getLocation());
				dijit.byId("toolNameEdit").setValue("");
			}
			else{
					var tool = ToolContainer.getTool(this.resourceId);
					//Si se han creado instancias de ese tipo se deshabilita el cambio a recurso
					if (this.createdInstancesTool(tool))
					{
						dojo.byId("rbResourceEdit").disabled=true;
						dijit.byId("selectToolTypeEdit").setAttribute('disabled', true);
						dojo.byId("labelEditResourceInfo").innerHTML = i18n.get("editResourceInfoCreated");
						dojo.style("editResourceInfo","display","");
					}
					dojo.byId("rbResourceEdit").checked=false;
					dojo.byId("rbToolEdit").checked=true;
					dijit.byId("resourceNameEdit").setValue("");
					dijit.byId("resourceLocationEdit").setValue("");
					dijit.byId("toolNameEdit").setValue(tool.getName());
			}
		},	
		
		createdInstancesTool: function(toolCheck){
			var toolInstances = ToolInstanceContainer.getAllToolInstances();
			for (var i=0; i < toolInstances.length; i++)
			{
				var tool = toolInstances[i].getTool();
				if (toolCheck.getId()== tool.getId() && tool.getToolKind()=="external" && toolInstances[i].getLocation() && !ToolInstanceReuse.reusesToolInstance(toolInstances[i]))
				{
					return true;
				}
			}
			return false;
		},
		
		internationalize: function(){
			dojo.byId("labelRadioEditResource").innerHTML=i18n.get("labelRadioCreateResource");
			dojo.byId("labelRbResourceEdit").innerHTML=i18n.get("rbResource");
			dojo.byId("labelRbToolEdit").innerHTML=i18n.get("rbTool");
			dojo.byId("labelResourceNameEdit").innerHTML=i18n.get("resourceName");
			dojo.byId("labelResourceLocationEdit").innerHTML=i18n.get("resourceLocation");
			dojo.byId("labelToolNameEdit").innerHTML=i18n.get("toolName");
			dojo.byId("labelToolTypeEdit").innerHTML=i18n.get("toolType");
			dijit.byId("editResourceDialog").titleNode.innerHTML=i18n.get("editResourceTitle");
			dijit.byId("editResourceDialogOk").setLabel(i18n.get("okButton"));
			dijit.byId("editResourceDialogCancel").setLabel(i18n.get("cancelButton"));
		},
				
		editAsResource: function(){
			var name = dijit.byId("resourceNameEdit").getValue();
			var location = dijit.byId("resourceLocationEdit").getValue();
			var resource = ResourceContainer.getResource(this.resourceId);
			if (resource){
				resource.setName(name);
				resource.setLocation(location);
			}
			else{
				//Convertir de herramienta a recurso
				var tool = ToolContainer.getTool(this.resourceId);
				//Crear el documento por el que se va a sustituir
				var resource = ResourceContainer.addResource(name, location);
				ResourceManager.changeToolToResource(tool, resource);
			}
			JsonDB.notifyChanges();
			EditResourceDialog.hideDialog();
		},
		
		editAsTool: function(){
			var toolType = dijit.byId("selectToolTypeEdit").getValue();
			var name = dijit.byId("toolNameEdit").getValue();
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
			//ToolContainer.addTool(name, toolKind, toolType);
			var tool = ToolContainer.getTool(this.resourceId);
			if (tool){
				tool.setName(name);
				tool.setToolKind(toolKind);
				tool.setToolType(toolType);
			}
			else{
				//Convertir de recurso a herramienta
				var resource = ResourceContainer.getResource(this.resourceId);
				//Crear la herramienta por la que se va a sustituir
				tool = ToolContainer.addTool(name, toolKind, toolType);
				ResourceManager.changeResourceToTool(resource, tool);
			}
			JsonDB.notifyChanges();
			EditResourceDialog.hideDialog();
		}	
}