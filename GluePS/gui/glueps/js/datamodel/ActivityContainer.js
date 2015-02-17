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
 *  Funcionalidad asociada a las actividades
 */

var ActivityContainer = {

	/** Obtiene un array con todas las actividades finales (aquellas que no tienen actividades hijas)
	 */
	getFinalActivities : function() {
		var activityList = new Array();
		var activity = JsonDB.deploy.design.rootActivity;
		this.getActivitiesChild(activity, activityList);
		return activityList;
	},

	getActivitiesChild : function(activity, activityList) {
		if (activity.childrenActivities) {
			var children = activity.childrenActivities;
			for ( var i = 0; i < children.length; i++) {
				this.getActivitiesChild(children[i], activityList);
			}
		} else {
			//Asegurarse de que no es la actividad rootActivity
			if (activity.id != JsonDB.deploy.design.rootActivity.id)
			{
				activityList.push(new Activity(activity));
			}
		}
	},		
	
	getFinalActivity : function(activityId) {
		var finalActivities = this.getFinalActivities();
		for (var i=0; i < finalActivities.length; i++)
		{
			if (finalActivities[i].getId()==activityId)
			{
				return finalActivities[i];
			}
		}
		return false;
	},
	
	getActivity : function(activityId) {
		var activity = JsonDB.deploy.design.rootActivity;
		return this.searchActivity(activity, activityId);
	},
	
	searchActivity : function(activity, activityId) {
		if (activity.id == activityId)
		{
			return new Activity(activity);
		}
		else{
			if (activity.childrenActivities) {
				var children = activity.childrenActivities;
				for ( var i = 0; i < children.length; i++) {
					var result = this.searchActivity(children[i], activityId);
					if (result != false)
					{
						return result;
					}
				}
			}
		}
		return false;
	},	
	
	/**
	 * Borra la actividad cuyo id se proporciona
	 * @param activityIdDelete Identificador de la actividad a borrar
	 * @returns {bool} La actividad ha sido borrada o no
	 */
	deleteFinalActivity: function(activityIdDelete)
	{
		var rootActivity = JsonDB.deploy.design.rootActivity;
		var deleted = this.deleteFinalActivityChildren(null, rootActivity, activityIdDelete);
		return deleted;
	},
	
	/**
	 * Función recursiva que busca la actividad a borrar entre los hijos de una dada
	 * @param parentActivity Padre de la actividad 
	 * @param activity Actividad entre cuyos hijos se buscará la que se desea borrar
	 * @param activityIdDelete Identificador de la actividad a borrar
	 */
	deleteFinalActivityChildren: function(parentActivity, activity, activityIdDelete){
		if (activity.childrenActivities) {
			var children = activity.childrenActivities;
			for ( var i = 0; i < children.length; i++) {
				var deleted = this.deleteFinalActivityChildren(activity, children[i], activityIdDelete);
				if (deleted == true)
				{
					return true;
				}
			}			
		}
		else{
			//Es una actividad final
			//Comprobar si su id es el de la actividad a borrar
			if (activity.id == activityIdDelete && parentActivity!=null)
			{
				var children = parentActivity.childrenActivities;
				for ( var i = 0; i < children.length; i++) {
					if (children[i].id == activityIdDelete)
					{
						parentActivity.childrenActivities.splice(i,1);
						//Si parentActivity no tiene mas hijos la borramos también. En caso contrario, esta se convertiría en una nueva actividad final
						if (parentActivity.childrenActivities.length == 0 && parentActivity.id!=JsonDB.deploy.design.rootActivity.id )
						{
							parentActivity.childrenActivities = null;
							return this.deleteFinalActivity(parentActivity.id);
							
						}
						else{
							return true;
						}
					}
				}
			}
		}
		return false;
	},
	
	/**
	 * Actualiza la información asociada a una actividad final
	 * @param oldId Valor del id que tenía la actividad cuya información se va a actualizar
	 * @pram oldParentId Valor del parentActivityId que tenía la actividad cuya información se va a actualizar
	 */
	updateFinalActivity : function(oldId, oldParentId, newData) {
		var parentActivity = ActivityContainer.getActivity(oldParentId);
		if (parentActivity) {
			var children = parentActivity.getData().childrenActivities;
			for ( var i = 0; i < children.length; i++) {
				if (children[i].id == oldId)
				{
					parentActivity.getData().childrenActivities[i] = newData;
					return this.getFinalActivity(newData.id);
				}
			}
		}
		return false;
	},
	
	addFinalActivity : function(name, description, referenceActivity, after) {
		var data = new Object();
		data.id = this.getNextFinalActivityId();
		data.name = name;
		data.description = description;
		//Campos con idéntico valor que el de la actividad respecto a la que se añade
		data.childrenSequenceMode = referenceActivity.getChildrenSequenceMode();
		data.toDeploy = referenceActivity.getToDeploy();
		var parentActivity = referenceActivity.getParentActivity();
		if (parentActivity) {
			data.parentActivityId = parentActivity.getId();
			var children = parentActivity.getData().childrenActivities;
			for ( var i = 0; i < children.length; i++) {
				if (children[i].id == referenceActivity.getId())
				{
					if (after)
					{
						//Añade la actividad después
						parentActivity.getData().childrenActivities.splice(i+1, 0, data);
					}
					else{
						//Añade la actividad antes
						parentActivity.getData().childrenActivities.splice(i, 0, data);
					}
					return this.getFinalActivity(data.id);
				}
			}
		}
		return false;
	},
	
	/**
	 *  Añadir una actividad final cuando no tiene a ninguna otra actividad padre como referencia
	 */
	addFirstFinalActivity : function(name, description) {
		var data = new Object();
		data.id = this.getNextFinalActivityId();
		data.name = name;
		data.description = description;
		//Damos un valor por defecto a los campos 
		data.childrenSequenceMode = 0;
		data.toDeploy = true;
		var parentActivity = JsonDB.deploy.design.rootActivity;
		if (parentActivity) {
			data.parentActivityId = parentActivity.id;
			parentActivity.childrenActivities = new Array();
			parentActivity.childrenActivities.push(data);
			return this.getFinalActivity(data.id);
		}
		return false;
	},
	
	/**
	 * Obtiene el siguiente identificador de actividad disponible
	 * @returns {String} Siguiente identificador de actividad disponible
	 */
	getNextFinalActivityId : function() {
		var maxId = 0;
		var finalActivities = this.getFinalActivities();
		for (var i=0; i < finalActivities.length; i++)
		{
			var ident = finalActivities[i].getId();
			if (ident.length > 4 && ident.substring(0, 4) == "AID_") {
				var id = parseInt(ident.substring(4));
				if (id > maxId) {
					maxId = id;
				}
			}
		}
		return "AID_" + parseInt(maxId + 1);
	}
}