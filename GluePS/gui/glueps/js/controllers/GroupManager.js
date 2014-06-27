/**
 * Funcionalidad asociada a la gestión de los grupos
 */
var GroupManager = {
		
		/**
		 * Borra un grupo de una actividad y sus recursos asignados
		 * @param activity Actividad de la que se borra el grupo
		 * @param instancedActivity Instancia de actividad del grupo que se borra
		 * @returns {Boolean} Indica si ha habido errores en el borrado
		 */
		deleteActivityGroup: function(activity, instancedActivity)
		{
			var errors = false;
			var resources = instancedActivity.getResources();
			//borramos los recursos
			for (var i=0; i < resources.length; i++)
			{
				ResourceManager.deleteResourceGroup(activity, instancedActivity, resources[i]);
			}
			//borramos las instancias de herramientas
			var toolInstances = instancedActivity.getToolInstances();
			for (var i=0; i < toolInstances.length; i++)
			{
				var ok = ToolInstanceManager.deleteToolInstanceGroup(activity, instancedActivity, toolInstances[i]);
				//Mostrar información si ha fallado el borrado?
				if (!ok)
				{
					errors = true;
				}
			}
			//Si se ha hecho correctamente se borra también la instancia de la actividad
			if (!errors)
			{
				InstancedActivityContainer.deleteInstancedActivity(instancedActivity);
				return true;
			}
			else{
				return false;
			}
		},
		
		/**
		 * Borra un grupo y le borra de todas las actividades a las que está asignado
		 * @param group Grupo que se desea borrar
		 * @returns {Boolean} Indica si ha habido errores en el borrado
		 */
		deleteGroup: function(group)
		{
			var errors = false;
			var activities = ActivityContainer.getFinalActivities();
			for (var i=0; i < activities.length; i++)
			{
				var instanced = activities[i].getInstancedActivities();
				for (var j=0; j < instanced.length; j++)
				{
					if (instanced[j].getGroup().getId() == group.getId())
					{
						var ok = GroupManager.deleteActivityGroup(activities[i], instanced[j]);
						if (!ok)
						{
							errors = true;
						}
					}
				}
			}
			GroupContainer.deleteGroup(group);
			return errors;
		}
}