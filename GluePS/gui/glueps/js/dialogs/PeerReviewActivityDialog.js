/**
 * 
 */

var PeerReviewActivityDialog = {
		
		dialogId: "createPeerReviewActivityDialog",
		
		/**
		 * Identificador de la actividad que se crea
		 */
		activityId: null,
		
		init: function(){
			
			dojo.connect(dojo.byId("createPeerReviewActivityDialogOk"), "onclick", function() {
				PeerReviewActivityDialog.check();
			});
			
			dojo.connect(dojo.byId("createPeerReviewActivityDialogCancel"), "onclick", function() {
				PeerReviewActivityDialog.hideDialog();
			});
			
		},
		
		showDialog : function(activityId){
			this.activityId = activityId;
			this.resetElements();
			this.internationalize();
			this.addGroupsCheckBox();
			this.addResourcesCheckBox();
			this.addHowManySelect();
			var dlg = dijit.byId(this.dialogId);
			dlg.show();
		},
		
		hideDialog : function(){
			this.activityId = null;
			var dlg = dijit.byId(this.dialogId);
			dlg.hide();
		},
		
		/**
		 * Comprueba el formulario y si hay errores se muestran. En caso contrario se crea la actividad
		 */
		check : function(){
			var name = dijit.byId("peerReviewActivityNameCreate").getValue();
			if (name=="")
			{
				dojo.byId("createPeerReviewActivityError").innerHTML=i18n.get("noActivityName");
			}
			else{
				if (!this.enoughGroups())
				{
					dojo.byId("createPeerReviewActivityError").innerHTML=i18n.get("noEnoughGroups");
				}
				else{
					if (!this.enoughResources())
					{
						dojo.byId("createPeerReviewActivityError").innerHTML=i18n.get("noEnoughResources");
					}
					else{
						if (dijit.byId("selectHowManyGroups").getValue()==0)
						{
							dojo.byId("createPeerReviewActivityError").innerHTML=i18n.get("noHowManyGroupsSelected");
						}
						else{
							PeerReviewActivityDialog.createActivity();
						}						
					}					
				}
			}
		},
		
		resetElements: function(){
			dojo.byId("createPeerReviewActivityError").innerHTML="";
			var name = "";
			dijit.byId("peerReviewActivityNameCreate").setValue(name);
			var description = "";
			dijit.byId("peerReviewActivityDescriptionCreate").setValue(description);
		},
		
		internationalize: function(){
			dojo.byId("labelPeerReviewActivityNameCreate").innerHTML=i18n.get("createActivityName");
			dojo.byId("labelPeerReviewActivityDescriptionCreate").innerHTML=i18n.get("createActivityDescription");
			dijit.byId("createPeerReviewActivityDialog").titleNode.innerHTML=i18n.get("createPeerReviewActivityTitle");
			dijit.byId("createPeerReviewActivityDialogOk").attr("label", i18n.get("okButton"));
			dijit.byId("createPeerReviewActivityDialogCancel").attr("label", i18n.get("cancelButton"));
			
			dojo.byId("labelReviewMode").innerHTML=i18n.get("reviewMode");
			dojo.byId("circularMode").innerHTML=i18n.get("circularMode");
			dojo.byId("labelReviewGroups").innerHTML=i18n.get("reviewGroups");
			dojo.byId("labelReviewResources").innerHTML=i18n.get("reviewResources");
			dojo.byId("labelReviewHowManyGroups").innerHTML=i18n.get("reviewHowManyGroups");
		},
				
		/**
		 * Crea la actividad de revisión entre pares
		 */
		createActivity: function(){
			//Actividad a partir de la que se crea la actividad de revisión
			var activity = ActivityContainer.getFinalActivity(this.activityId);
			//Número de grupos que revisa cada uno
			var howManyReview = dijit.byId("selectHowManyGroups").getValue();
			var name = dijit.byId("peerReviewActivityNameCreate").getValue();
			var description = dijit.byId("peerReviewActivityDescriptionCreate").getValue();
			//Creamos una nueva actividad
			var newActivity = ActivityContainer.addFinalActivity(name, description, activity, true);
			var instancedActivities = activity.getInstancedActivities();
			for (var i = 0; i < instancedActivities.length; i++)
			{
				var group = instancedActivities[i].getGroup();
				var cb = dojo.byId("cb" + group.getId());
				//Añadir el grupo a la nueva actividad si participa en la revisión
				if (cb.checked)
				{
					var newInstancedActivity = InstancedActivityContainer.addInstancedActivity(newActivity, group);
				
					//Añadir los recursos que se revisan (reutilizan) circularmente
					for (var j = 1; j <= howManyReview; j++)
					{
						var reviewedIndex = (i+j)% instancedActivities.length;
						//Recursos de tipo documento
						var reviewedGroupResources = instancedActivities[reviewedIndex].getResources();
						for (var k=0; k < reviewedGroupResources.length; k++)
						{	var cb = dojo.byId("cb" + reviewedGroupResources[k].getId());
							//Añadir el documento al grupo si ese recurso debe revisarse
							if (cb.checked)
							{
								if (newInstancedActivity.usesResource(reviewedGroupResources[k]) == false) {
									newInstancedActivity.addResource(reviewedGroupResources[k]);
									//Si es necesario se añade también a la actividad
									if (newActivity.availableResource(reviewedGroupResources[k])==false)
									{
										newActivity.addResource(reviewedGroupResources[k]);
									}
								}
							}						
						}
						//Recursos de tipo herramienta
						var reviewedGroupToolInstances = instancedActivities[reviewedIndex].getToolInstances();
						for (var k=0; k < reviewedGroupToolInstances.length; k++)
						{
							var tool = reviewedGroupToolInstances[k].getTool();
							var cb = dojo.byId("cb" + tool.getId());
							//Reutilizar la herramienta en el grupo si ese recurso debe revisarse
							if (cb.checked)
							{							
								//Añadir una instancia de la herramienta al grupo
								var created = newInstancedActivity.addToolInstance(tool);							
								if (newActivity.availableTool(tool)==false)
								{
									//Si es necesario se añade la herramienta a la actividad
									newActivity.addTool(tool);
								}
								//La instancia de herramienta creada tendrá como location el id de la instancia de herramienta que reutiliza
								created.setLocation(reviewedGroupToolInstances[k].getId());
								//La instancia de herramienta creada tendrá como nombre el de la instancia de herramienta que reutiliza
								created.setName(reviewedGroupToolInstances[k].getName());																
							}
						}
					}
				}
			}
			GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
			JsonDB.notifyChanges();
			PeerReviewActivityDialog.hideDialog();
		},
		
		addGroupsCheckBox: function(){
			var divGroups = dojo.byId("divReviewGroups");
			while (divGroups.firstChild)
			{
				divGroups.removeChild(divGroups.firstChild);
			}
			var activity = ActivityContainer.getActivity(this.activityId);
			var instanced = activity.getInstancedActivities();
			var table = document.createElement("table");
			divGroups.appendChild(table);
			for (var i=0; i < instanced.length; i++)
			{
				if (i%3==0)
				{
					var tr = document.createElement("tr");
					table.appendChild(tr);
				}
				var td = document.createElement("td");
				tr.appendChild(td);
				var inputCheckBox = document.createElement("input");
				var id = "cb" + instanced[i].getGroup().getId();
				inputCheckBox.setAttribute("id", id);
				inputCheckBox.setAttribute("value", instanced[i].getGroup().getId());
				inputCheckBox.setAttribute("type", "checkbox");
				inputCheckBox.checked = false;
				dojo.connect(inputCheckBox, "onclick", function(){
					PeerReviewActivityDialog.addHowManySelect();
				});
				
				td.appendChild(inputCheckBox);
				var labelCheckBox = document.createElement("label");
				labelCheckBox.style.marginLeft="5px";
				labelCheckBox.innerHTML = instanced[i].getGroup().getName();
				td.appendChild(labelCheckBox);

			}
			
		},		
		
		addResourcesCheckBox: function(){
			var divResources = dojo.byId("divReviewResources");
			while (divResources.firstChild)
			{
				divResources.removeChild(divResources.firstChild);
			}
			var activity = ActivityContainer.getActivity(this.activityId);
			var table = document.createElement("table");
			divResources.appendChild(table);
			var resources = activity.getResources();
			var number = 0;
			for (var i = 0; i < resources.length; i++)
			{
				if (number%3==0)
				{
					var tr = document.createElement("tr");
					table.appendChild(tr);
				}
				var td = document.createElement("td");
				tr.appendChild(td);
				var inputCheckBox = document.createElement("input");
				var id = "cb" + resources[i].getId();
				inputCheckBox.setAttribute("id", id);
				inputCheckBox.setAttribute("value", resources[i].getId());
				inputCheckBox.setAttribute("type", "checkbox");
				inputCheckBox.checked = false;
				td.appendChild(inputCheckBox);
				var labelCheckBox = document.createElement("label");
				labelCheckBox.style.marginLeft="5px";
				labelCheckBox.innerHTML =  resources[i].getName() + " (" + ResourcesPainter.identifiers[resources[i].getId()] + ")";
				td.appendChild(labelCheckBox);
				number++;
			}
			var tools = activity.getTools();
			for (var i = 0; i < tools.length; i++)
			{
				if (number%3==0)
				{
					var tr = document.createElement("tr");
					table.appendChild(tr);
				}
				var td = document.createElement("td");
				tr.appendChild(td);
				var inputCheckBox = document.createElement("input");
				var id = "cb" + tools[i].getId();
				inputCheckBox.setAttribute("id", id);
				inputCheckBox.setAttribute("value", tools[i].getId());
				inputCheckBox.setAttribute("type", "checkbox");
				inputCheckBox.checked = false;
				td.appendChild(inputCheckBox);
				var labelCheckBox = document.createElement("label");
				labelCheckBox.style.marginLeft="5px";
				labelCheckBox.innerHTML =  tools[i].getName() + " (" + ResourcesPainter.identifiers[tools[i].getId()] + ")";
				td.appendChild(labelCheckBox);
				number++;
			}
		},
		
		addHowManySelect: function(){
			var howManySelect = dijit.byId("selectHowManyGroups");
			while (howManySelect.getOptions(0)!=null){
				howManySelect.removeOption(howManySelect.getOptions(0));
			}
			//Como máximo el número de grupos seleccionados menos 1 (puede revisar a todos excepto a sí mismo)
			var max = this.numberGroups();
			howManySelect.addOption({
				label: " -- Seleccione número -- ",
				value: "0"
			});
			for (var i=1; i < max; i++)
			{
				howManySelect.addOption({
					label: i.toString(),
					value: i.toString()
				});				
			}
		},
		
		numberGroups: function(){
			var numberOfGroups = 0;
			var activity = ActivityContainer.getActivity(this.activityId);
			var instanced = activity.getInstancedActivities();
			for (var i=0; i < instanced.length; i++)
			{
				var group = instanced[i].getGroup();
				var cb = dojo.byId("cb" + group.getId());
				if (cb.checked)
				{
					numberOfGroups++;
				}
			}
			return numberOfGroups;
		},
		
		/**
		 * Obtiene el número de recursos marcados para revisar
		 * @returns {Number} Número de recursos marcados para revisar
		 */
		numberResources: function(){
			var numberOfResources = 0;
			var activity = ActivityContainer.getActivity(this.activityId);
			var instanced = activity.getInstancedActivities();
			for (var i=0; i < instanced.length; i++)
			{
				var resources = instanced[i].getResources();
				for (var j=0; j < resources.length; j++)
				{
					var cb = dojo.byId("cb" + resources[j].getId());
					if (cb.checked)
					{
						numberOfResources++;
					}
				}
				var toolInstances = instanced[i].getToolInstances();
				for (var j=0; j < toolInstances.length; j++)
				{
					var cb = dojo.byId("cb" + toolInstances[j].getTool().getId());
					if (cb.checked)
					{
						numberOfResources++;
					}
				}
			}
			return numberOfResources;
		},
		
		enoughGroups: function(){
			//Al menos 2 grupos tienen que participar en la revisión
			var number = this.numberGroups();
			return number >= 2;
		},
		
		enoughResources: function(){
			//Al menos un recurso debe estar marcado para revisión
			var number = this.numberResources();
			return number >= 1;
		}
		
		
}