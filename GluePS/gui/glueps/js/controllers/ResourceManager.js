/**
 * Funcionalidad asociada a la gestión de los recursos
 */
var ResourceManager = {
		
		/**
		 * Borra un recurso de una instancia de actividad
		 * @param activity Actividad de la que se borra el recurso
		 * @param instancedActivity Instancia de la actividad de la que se borra el recurso
		 * @param resource	Recurso a borrar
		 */
		deleteResourceGroup: function(activity, instancedActivity, resource)
		{
			instancedActivity.deleteResource(resource);
			var instanced = activity.getInstancedActivities();
			var used = false;
			for (var i=0; i< instanced.length; i++)
			{
				if (instanced[i].usesResource(resource))
				{
					used = true;
					break;
				}
			}
			if (used==false)
			{
				activity.deleteResource(resource);
			}
		},
		
		/**
		 * Borra un recurso y le borra de todas las actividades a las que está asignado
		 * @param resource Recurso que se desea borrar
		 */		
		deleteResource: function(resource){
			//Borrar las utilizaciones del recurso
			var activities = ActivityContainer.getFinalActivities();
			for (var i=0; i<activities.length; i++)
			{
				var instanced = activities[i].getInstancedActivities();
				for (var j=0; j < instanced.length; j++)
				{
					if (instanced[j].usesResource(resource))
					{
						instanced[j].deleteResource(resource);
					}
				}
				if (activities[i].availableResource(resource))
				{
					activities[i].deleteResource(resource);
				}
			}
			//Borrar el recurso
			ResourceContainer.deleteResource(resource);			
		},
		
		/**
		 * Cambia todas las utilizaciones de un recurso por las de una herramienta existente y borra el recurso
		 * @param resource Recurso que se desea borrar y cambiar por una herramienta
		 * @param tool Herramienta que sustituye al recurso
		 */
		changeResourceToTool: function(resource, tool){
			var activities = ActivityContainer.getFinalActivities();
			for (var i=0; i<activities.length; i++)
			{
				var instanced = activities[i].getInstancedActivities();
				for (var j=0; j < instanced.length; j++)
				{
					if (instanced[j].usesResource(resource))
					{
						//Borrar el recurso
						instanced[j].deleteResource(resource);
						//Añadir la herramienta
						instanced[j].addToolInstance(tool);
						if (activities[i].availableTool(tool)==false)
						{
							activities[i].addTool(tool);
						}
					}
				}
				if (activities[i].availableResource(resource))
				{
					activities[i].deleteResource(resource);
				}
			}
			//Borrar el recurso
			ResourceContainer.deleteResource(resource);	
		},
		
		/**
		 * Cambia todas las utilizaciones de una herramienta por las de un recurso existente y borra la herramienta
		 * @param tool Herramienta que se desea borrar y cambiar por un recurso (de tipo documento)
		 * @param tool Recurso que sustituye a la herramienta
		 */
		changeToolToResource: function(tool, resource){
			var activities = ActivityContainer.getFinalActivities();
			for (var i=0; i<activities.length; i++)
			{
				var instanced = activities[i].getInstancedActivities();
				for (var j=0; j < instanced.length; j++)
				{
					if (instanced[j].usesTool(tool))
					{
						var toolInstances = instanced[j].getToolInstances();
						for (var k = 0; k < toolInstances.length; k++)
						{
							//Comprobar si la instancia de herramienta es de ese tipo
							if (toolInstances[k].getTool().getId()==tool.getId())
							{
								//Borrar la instancia de la herramienta del grupo (y de la actividad si es necesario)
								ToolInstanceManager.deleteToolInstanceGroup(activities[i], instanced[j], toolInstances[k]);
							}
						}
						//añadir el recurso
						instanced[j].addResource(resource);						
						if (activities[i].availableResource(resource)==false)
						{
							activities[i].addResource(resource);
						}
					}
				}
				//No necesario
				if (activities[i].availableTool(tool))
				{
					activities[i].deleteTool(tool);
				}
			}
			//Borrar la herramienta
			ToolContainer.deleteTool(tool);	
		}
		
		

}