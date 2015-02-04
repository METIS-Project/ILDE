/**
 * Funcionalidad asociada a la gesti�n de las instancias de herramientas
 */
var ToolInstanceManager = {

	/**
	 * Borra una instancia de herramienta de una instancia de actividad
	 * 
	 * @param activity Actividad de la que se borra la instancia de herramienta
	 * @param instancedActivity Instancia de la actividad de la que se borra la instancia de herramienta
	 * @param toolInstance Instancia de herramienta a borrar
	 */
	deleteToolInstanceGroup : function(activity, instancedActivity, toolInstance) {
		if (ToolInstanceReuse.reusesToolInstance(toolInstance)
				|| toolInstance.getLocation() == false) {
			// Estado en que no se ha creado la instancia
			ToolInstanceManager.deleteToolInstance(activity, instancedActivity, toolInstance);
			return true;
		} else {
			// Estado en el que se ha creado la instancia
			return ToolInstanceManager.deleteToolInstanceCreated(activity, instancedActivity, toolInstance);
		}
	},

	/**
	 * Borra una instancia de herramienta de una instancia de actividad cuando se reutiliza o no se ha creado la instancia
	 * @param activity Actividad de la que se borra la instancia de herramienta
	 * @param instancedActivity Instancia de la actividad de la que se borra la instancia de herramienta
	 * @param toolInstance Instancia de herramienta a borrar
	 */
	deleteToolInstance : function(activity, instancedActivity, toolInstance) {
		var tool = toolInstance.getTool();
		// Borrar como instancia de herramienta de esta instancia de la
		// actividad
		instancedActivity.deleteToolInstance(toolInstance);
		// Borrar referencias a la instancia de herramienta
		ToolInstanceManager.deleteReferencesToolInstance(toolInstance);
		// Borrar la instancia de herramienta
		ToolInstanceContainer.deleteToolInstance(toolInstance);
		// Si no se usa la herramienta en otra instancia de la actividad se
		// borra como herramienta de la actividad
		var instanced = activity.getInstancedActivities();
		var used = false;
		for ( var i = 0; i < instanced.length; i++) {
			if (instanced[i].usesTool(tool)) {
				used = true;
			}
		}
		if (used == false) {
			activity.deleteTool(tool);
		}
	},

	/**
	 * Elimina una instancia de herramienta y todas las referencias a la instancia de herramienta.
	 * @param toolInstance Instancia de herramienta a eliminar
	 */
	deleteReferencesToolInstance : function(toolInstance) {
		var tool = toolInstance.getTool();
		var activities = ActivityContainer.getFinalActivities();
		for ( var i = 0; i < activities.length; i++) {
			var instanced = activities[i].getInstancedActivities();
			for ( var j = 0; j < instanced.length; j++) {
				var toolInstances = instanced[j].getToolInstances();
				for ( var k = 0; k < toolInstances.length; k++) {
					// El identificador debe ser el mismo
					if (toolInstance.getId() == toolInstances[k].getLocation()) {
						toolInstances[k].deleteLocation();
					}
				}
			}
		}
	},

	/**
	 * Borra la instancia de herramienta creada mediante un DELETE. 
	 * A continuaci�n, borra la informaci�n de la misma contenida en el despliegue
	 * 
	 */
	deleteToolInstanceCreated : function(activity, instancedActivity, toolInstance) {
		var url = toolInstance.getId();
		if (LdShakeManager.ldShakeMode){
			var url = LdShakeManager.buildLdshakeUrl(url);
		}
		var ok; // Variable donde se almacenar� el resultado
		var xhrArgs = {
			url : url,
			handleAs : "text",
			sync : true, // Lo hacemos s�ncrona
			timeout : 10000, // tiempo m�ximo de expera de 10 segundos
			load : function(data) {
				ok = true;
				ToolInstanceManager.deleteToolInstance(activity, instancedActivity, toolInstance);
			},
			error : function() {
				ok = false;
				//Deber�a estar fuera de aqu�!!!
				InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n
						.get("notDeletedToolInstance"));
			}
		}
		var deferred = dojo.xhrDelete(xhrArgs);
		return ok;
	},
	
	
	
	/**
	 * Borra todas las instancias creadas de una herramienta en una actividad
	 * @param activity Actividad de la que se borran la instancias de la herramienta
	 * @param toolInstance Herramienta de la que se borran las instancias creadas en la actividad
	 */
	deleteToolInstancesActivity : function(activityId, toolId) {
		var activity = ActivityContainer.getActivity(activityId);
		var tool = ToolContainer.getTool(toolId);
		var toolInstancesDelete = new Array();
		var instancedAct = activity.getInstancedActivities();
		for (var i =0; i < instancedAct.length; i++)
		{
			var toolInst = instancedAct[i].getToolInstances();
			for (var j = 0; j < toolInst.length; j++){
				var toolThis = toolInst[j].getTool();
				//Comprobar que es del mismo tipo y tiene una instancia creada (no reutiliza otra)
				if (tool.getId()== toolThis.getId() && toolThis.isExternal() && toolInst[j].getLocation() && !ToolInstanceReuse.reusesToolInstance(toolInst[j]))
				{
					toolInstancesDelete.push(toolInst[j]);
				}				
			}
		}
		var deletedInst = 0;
		for (var i = 0; i < toolInstancesDelete.length; i++)
		{
			if (ToolInstanceManager.callDeleteToolInstanceActivity(toolInstancesDelete[i]))
			{
				deletedInst++;
			}
		}
		
		if (deletedInst == toolInstancesDelete.length){
			InformativeDialogs.showAlertDialog(i18n.get("info"), i18n.get("deletedAllToolInstance"));
		}
		else{
			InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("notDeletedAllToolInstance"));
		}
		//Sólo se actualiza si ha habido cambios
		if (deletedInst > 0){
			JsonDB.notifyChanges();
		}
	},
	
	/**
	 * Realiza la petición delete para borrar la instancia de la herramienta
	 * @param toolInstance Instancia de herramienta de la que se borra la instancia creada
	 * @return boolean El resultado de la petición delete
	 */
	callDeleteToolInstanceActivity : function(toolInstance) {
		var ok; // Variable donde se almacenará el resultado
		var url = toolInstance.getId();
		if (LdShakeManager.ldShakeMode){
			var url = LdShakeManager.buildLdshakeUrl(url);
		}
		var xhrArgs = {
			url : url,
			handleAs : "text",
			sync : true, // Lo hacemos síncrona
			timeout : 10000, // tiempo máximo de expera de 10 segundos
			load : function(data) {
				ok = true;
				toolInstance.deleteLocation();
			},
			error : function() {
				ok = false;
			}
		}
		var deferred = dojo.xhrDelete(xhrArgs);
		return ok;
	}

}