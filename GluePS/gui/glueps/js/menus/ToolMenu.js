/**
 * Funcionalidad asociada al menú de una herramienta
 */

var ToolMenu = {
		
		init: function(){
			dojo.connect(dojo.byId("dialogConfirmDeleteToolOk"), "onclick", function() {
				var idTool = dijit.byId("deleteToolId").getValue();
				ToolMenu.deleteTool(idTool);
			});
			
			dojo.connect(dojo.byId("dialogConfirmDeleteToolCancel"), "onclick", function() {
				ToolMenu.hideDeleteTool();
			});
		},
		
		/**
		 * Menú de opciones de una herramienta
		 * 
		 * @param data
		 */
		getToolMenu : function(data) {
			var items = new Array();
			items.push({
				label :i18n.get("resourceMenuEdit"),
				icon: "edit",
				onClick : function(data) {
					GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
					EditResourceDialog.showDialog(data.tool.getId());
				},
				data : data,
				help : i18n.get("resourceMenuEditHelp")
			});
			if (!StateManager.isSelectedTool(data.tool))
			{
				items.push({
					label : i18n.get("resourceMenuActivity"),
					icon: "tool_icon.png",
					onClick : function(data) {
						StateManager.changeToolState(data.tool);
						var trTool = dojo.byId("tr" + ResourcesPainter.identifiers[data.tool.getId()]);
						trTool.setAttribute("class", "toolSelected");
					},
					data : data,
					help : i18n.get("resourceMenuActivityToolHelp")
				});
			}
			else{
				items.push({
					label : i18n.get("resourceMenuActivity"),
					icon: "tool_icon.png",
					onClick : function(data) {
						StateManager.changeToolState(data.tool);
						var trTool = dojo.byId("tr" + ResourcesPainter.identifiers[data.tool.getId()]);
						trTool.setAttribute("class", "");
					},
					data : data,
					help : i18n.get("resourceMenuActivityToolHelpUnselect")
				});
			}
			items.push({
				label : i18n.get("resourceMenuDelete"),
				icon: "delete",
				onClick : function(data) {
					ToolMenu.showDeleteTool(data.tool);
					
				},
				data : data,
				help : i18n.get("resourceMenuDeleteHelp")
			});
			return items;
		},
		
		/**
		 * Muestra la ventana de confirmación del borrado de una herramienta
		 * @param resource Recurso a borrar
		 */
		showDeleteTool: function(tool)
		{
	        var dlg = dijit.byId("dialogConfirmDeleteTool");
	        dlg.titleNode.innerHTML = i18n.get("confirmDelete");
	        dojo.byId("dialogConfirmDeleteToolInfo").innerHTML= i18n.get("confirmDeleteTool");
	        dlg.show();
			dijit.byId("deleteToolId").setValue(tool.getId());
		},
		
		/**
		 * OCulta la ventana de confirmación del borrado de una herramienta
		 */
		hideDeleteTool: function()
		{
			dijit.byId("dialogConfirmDeleteTool").hide();
		},
		
		
		deleteTool: function(toolId)
		{
			ToolMenu.hideDeleteTool();
			var tool = ToolContainer.getTool(toolId);			
			var activities = ActivityContainer.getFinalActivities();
			for (var i=0; i < activities.length; i++)
			{
				var instanced = activities[i].getInstancedActivities();
				for (var j=0; j < instanced.length; j++)
				{
					var toolInstances = instanced[j].getToolInstances();
					for (var k = 0; k < toolInstances.length; k++)
					{
						//Comprobar si la instancia de herramienta es de ese tipo
						if (toolInstances[k].getTool().getId()==tool.getId())
						{
							var ok = ToolInstanceManager.deleteToolInstanceGroup(activities[i], instanced[j], toolInstances[k]);
							if (!ok)
							{
								//Determinar cómo tratar los errores con instancias creadas en las que ha fallado el DELETE
								//Posible política de reintentos
							}
						}
					}
				}
			}
			//Borrar la herramienta
			ToolContainer.deleteTool(tool);
			JsonDB.notifyChanges();
		}
}