/**
 * Funcionalidad asociada a las instancias de actividades
 */

var InstancedActivityContainer = {

	/**
	 * Obtiene una instancia de actividad
	 * @param instancedActivityId Identificador de la instancia de actividad
	 * @returns La instancia de actividad
	 */
	getInstancedActivity : function(instancedActivityId) {
		if (JsonDB.deploy.instancedActivities) {
			var instanced = JsonDB.deploy.instancedActivities;
			for ( var i = 0; i < instanced.length; i++) {
				if (instanced[i].id == instancedActivityId) {
					return (new InstancedActivity(instanced[i]));
				}
			}
		}
		return false;
	},

	/**
	 * Obtiene todas las instancias de una actividad
	 * @param activityId Identificador de la actividad 
	 * @returns {Array} Las instancias de la actividad
	 */
	getInstancedActivities : function(activityId) {
		var instancedList = new Array();
		if (JsonDB.deploy.instancedActivities) {
			var instanced = JsonDB.deploy.instancedActivities;
			for ( var i = 0; i < instanced.length; i++) {
				if (instanced[i].activityId == activityId) {
					instancedList.push(new InstancedActivity(instanced[i]));
				}
			}
		}
		return instancedList;
	},

	/**
	 * Obtiene todas las instancias de actividades
	 * @returns {Array} Todas las instancias de actividades
	 */
	getAllInstancedActivities : function() {
		var instancedList = new Array();
		if (JsonDB.deploy.instancedActivities) {
			var instanced = JsonDB.deploy.instancedActivities;
			for ( var i = 0; i < instanced.length; i++) {
				instancedList.push(new InstancedActivity(instanced[i]));
			}
		}
		return instancedList;
	},
	
	/**
	 * Crea una nueva instancia de la actividad
	 * @param activity Actividad de la que se crea la instancia
	 * @param group Grupo de la instancia
	 * @returns La instancia de la actividad creada
	 */
	addInstancedActivity : function(activity, group) {
		var data = new Object();
		data.id = this.getNextInstancedActivityId();
		data.activityId = activity.getId();
		data.deployId = JsonDB.getDeploymentId();
		data.groupId = group.getId();
		if (JsonDB.deploy.instancedActivities) {
			JsonDB.deploy.instancedActivities.push(data);
		} else {
			JsonDB.deploy.instancedActivities = new Array();
			JsonDB.deploy.instancedActivities.push(data);
		}
		return this.getInstancedActivity(data.id);
	},
	
	/**
	 * Borra la instancia de actividad 
	 * @param instancedActivity Instancia de actividad a borrar
	 * @returns {Boolean} La instancia se ha borrado con éxito
	 */
	deleteInstancedActivity : function(instancedActivity) {
		if (JsonDB.deploy.instancedActivities) {
			var instanced = JsonDB.deploy.instancedActivities;
			for ( var i = 0; i < instanced.length; i++) {
				if (instanced[i].id == instancedActivity.getId()) {
					instanced.splice(i, 1);
					return true;
				}
			}
		}
		return false;
	},
	
	/**
	 * Obtiene el siguiente identificador de instancia de actividad disponible
	 * @returns {String} Nuevo identificador de instancia de actividad
	 */
	getNextInstancedActivityId : function() {
		var maxId = 0;
		if (JsonDB.deploy.instancedActivities) {
			var instanced = JsonDB.deploy.instancedActivities;
			for ( var i = 0; i < instanced.length; i++) {
				var id = instanced[i].id;
				if (instanced[i].id.length > 4
						&& instanced[i].id.substring(0, 4) == "IAI_") {
					var id = parseInt(instanced[i].id.substring(4));
					if (id > maxId) {
						maxId = id;
					}
				}
			}
		}
		return "IAI_" + parseInt(maxId + 1);
	},
	
	/**
	 * Devuelve un array con las instancias de la actividad ordenadas
	 * @param activityId Identificador de la actividad
	 * @returns {String} Instancias de la actividad ordenadas
	 */
	sortInstancedActivitiesActivity: function(activityId) {	
		var instancedList = new Array();
		if (JsonDB.deploy.instancedActivities) {
			var instanced = JsonDB.deploy.instancedActivities;
			for ( var i = 0; i < instanced.length; i++) {
				if (instanced[i].activityId == activityId) {
					instancedList.push(instanced[i]);
				}
			}
		}
		return instancedList.sort(natcompareByGroupName);
	},
	
	/**
	 * Efectúa una ordenación de todas las instancias de actividad
	 */
	sortInstancedActivities: function(){
		var orderedList = new Array();
		var finalActivities = ActivityContainer.getFinalActivities();
		//Para cada actividad final se ordenan las instancias de la actividad
		for (var i=0; i < finalActivities.length; i++)
		{
			var ordered = InstancedActivityContainer.sortInstancedActivitiesActivity(finalActivities[i].getId());
			//Se añaden al array las instancias ordenadas
			for (var j=0; j < ordered.length; j++)
			{
				orderedList.push(ordered[j]);
			}
		}
		if (JsonDB.deploy.instancedActivities){
			for (var i=0; i < JsonDB.deploy.instancedActivities.length; i++)
			{
				//Las instancias no finales se añaden al array según aparición
				if (ActivityContainer.getFinalActivity(JsonDB.deploy.instancedActivities[i].activityId)==false)
				{
					orderedList.push(JsonDB.deploy.instancedActivities[i]);
				}
			}
			JsonDB.deploy.instancedActivities = orderedList;
		}
	}
}