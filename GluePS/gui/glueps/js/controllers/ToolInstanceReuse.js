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
 * 
 */
var ToolInstanceReuse = {

	/**
	 * Indica si una instancia de herramienta es reutilizada por otra
	 * 
	 * @param toolInstance
	 * @boolean La instancia de herramienta es reutilizada o no
	 */
	isReused : function(toolInstance) {
		var toolInstances = ToolInstanceContainer
				.getToolInstanceTool(toolInstance.getTool());
		for ( var i = 0; i < toolInstances.length; i++) {
			// Comprobar si una instancia diferente tiene como location el id de
			// esta
			if (toolInstances[i].getId() != toolInstance.getId()
					&& toolInstances[i].getLocation() == toolInstance.getId()) {
				return true;
			}
		}
		return false;
	},

	reusesToolInstance : function(toolInstance) {
		var location = toolInstance.getLocation();
		if (location
				&& ToolInstanceContainer.getToolInstance(location) != false) {
			return true;
		} else {
			return false;
		}
	},

	/**
	 * Recorre el conjunto de reutilizaciones entre instancias de herramientas
	 * desde una dada hasta llegar a la original
	 * 
	 * @param toolInstance
	 *            Instancia de herramienta desde la que se recorrerán las
	 *            reutilizaciones
	 * @return ToolInstance origen de las referencias
	 */
	getOriginalToolInstance : function(toolInstance) {
		if (toolInstance.getLocation()) {
			if (ToolInstanceReuse.reusesToolInstance(toolInstance)) {
				// Es una reutilización. Seguir recorriendo reutilizaciones
				return ToolInstanceReuse
						.getOriginalToolInstance(ToolInstanceContainer
								.getToolInstance(toolInstance.getLocation()));
			} else {
				// Es una instancia de herramienta creada (un gluelet). No hay
				// más reutilizaciones
				return toolInstance;
			}
		} else {
			// No se referencia a ninguna otra
			return toolInstance;
		}
	},

	/**
	 * Elimina una instancia de herramienta y todas las referencias a la
	 * instancia de herramienta.
	 * 
	 * @param toolInstance
	 *            Instancia de herramienta
	 */
	deleteReferencesToolInstanceRecursive : function(toolInstance) {
		var tool = toolInstance.getTool();
		var activities = ActivityContainer.getFinalActivities();
		for ( var i = 0; i < activities.length; i++) {
			var instanced = activities[i].getInstancedActivities();
			for ( var j = 0; j < instanced.length; j++) {
				var toolInstances = instanced[j].getToolInstances();
				for ( var k = 0; k < toolInstances.length; k++) {
					// El identificador debe ser el mismo
					if (toolInstance.getId() == toolInstances[k].getLocation()) {
						ToolInstanceReuse
								.deleteReferencesToolInstanceRecursive(toolInstances[k]);
					}
				}
			}
		}
		// !!No borramos la instancia sino el location
		toolInstance.deleteLocation();
	},

	/**
	 * Obtiene las instancias de una herramienta que han aparecido previamente a
	 * una instancia dada
	 * 
	 * @param toolInstance
	 *            Instancia de herramienta
	 * @param position
	 *            Posición de la instancia de herramienta entre las instancias
	 *            que hay en la actividad instanciada
	 * @param activity
	 *            Actividad en la que se encuentra la instancia de herramienta
	 * @param instancedActivity
	 *            Actividad instanciada en la que se encuentra la instancia de
	 *            herramienta
	 * @returns {Array}
	 */
	getToolInstanceToolPreviously : function(toolInstance, position, activity,
			instancedActivity) {
		var tool = toolInstance.getTool();
		var tiPreviously = new Array();
		var activities = ActivityContainer.getFinalActivities();
		for ( var i = 0; i < activities.length; i++) {
			var instanced = activities[i].getInstancedActivities();
			for ( var j = 0; j < instanced.length; j++) {
				var toolInstances = instanced[j].getToolInstances();
				for ( var k = 0; k < toolInstances.length; k++) {
					// El ID de herramienta debe ser el mismo
					if (tool.getId() == toolInstances[k].getTool().getId()) {
						// Si la posición es exactamente la misma hemos
						// terminado
						if (activities[i].getId() == activity.getId()
								&& instanced[j].getId() == instancedActivity
										.getId() && k == position) {
							return tiPreviously;
						}
						// Hemos encontrado otra herramienta más
						else {
							tiPreviously.push(toolInstances[k]);
						}
					}
				}

			}
		}
		return tiPreviously;
	},

	/**
	 * Obtiene las instancias de herramientas del mismo tipo que han aparecido
	 * previamente a una instancia dada
	 * 
	 * @param toolInstance
	 *            Instancia de herramienta
	 * @param position
	 *            Posición de la instancia de herramienta entre las instancias
	 *            que hay en la actividad instanciada
	 * @param activity
	 *            Actividad en la que se encuentra la instancia de herramienta
	 * @param instancedActivity
	 *            Actividad instanciada en la que se encuentra la instancia de
	 *            herramienta
	 * @returns {Array}
	 */
	getToolInstanceTypePreviously : function(toolInstance, position, activity,
			instancedActivity) {
		var tool = toolInstance.getTool();
		var tiPreviously = new Array();
		var activities = ActivityContainer.getFinalActivities();
		for ( var i = 0; i < activities.length; i++) {
			var instanced = activities[i].getInstancedActivities();
			for ( var j = 0; j < instanced.length; j++) {
				var toolInstances = instanced[j].getToolInstances();
				for ( var k = 0; k < toolInstances.length; k++) {
					// El TIPO de herramienta debe ser el mismo
					if (tool.getToolType() == toolInstances[k].getTool()
							.getToolType()) {
						// Si la posición es exactamente la misma hemos
						// terminado
						if (activities[i].getId() == activity.getId()
								&& instanced[j].getId() == instancedActivity
										.getId() && k == position) {
							return tiPreviously;
						}
						// Hemos encontrado otra herramienta más
						else {
							tiPreviously.push(toolInstances[k]);
						}
					}
				}

			}
		}
		return tiPreviously;
	},

	/**
	 * Indica si la instancia de herramienta puede reutilizarse en la instancia
	 * de la actividad
	 * 
	 * @param instancedActivity
	 *            Instancia de la actividad en la que se quiere reutilizar
	 * @param toolInstance
	 *            Instancia de herramienta que se quiere reutilizar
	 * @return booleano que indica si es posible reutilizar o no la herramienta
	 */
	canReuseToolInstance : function(instancedActivity, toolInstance) {
		// Ha aparecido con anterioridad o en la actividad instanciada la
		// instancia de herramienta a reutilizar
		var appeared = false;
		var activities = ActivityContainer.getFinalActivities();
		for ( var i = 0; i < activities.length; i++) {
			var instanced = activities[i].getInstancedActivities();
			for ( var j = 0; j < instanced.length; j++) {

				if (instancedActivity.getId() == instanced[j].getId()) {
					// Estoy en la actividad instanciada y no ha aparecido la
					// instancia de herramienta a reutilizar
					if (appeared == false) {
						return false;
					} else {
						return true;
					}
				}
				
				var toolInstances = instanced[j].getToolInstances();
				for ( var k = 0; k < toolInstances.length; k++) {
					if (toolInstances[k].getId() == toolInstance.getId()) {
						appeared = true;
						break;
					}
				}
			}
		}
		return false;
	},	

	/**
	 * Obtiene del xml el location de la instancia de la herramienta
	 * 
	 * @param xmlData
	 *            contenido del xml en el que se encuentra el location
	 * @return location
	 */
	getLocation : function(xmlData) {
		var jsdom = dojox.xml.parser.parse(xmlData);
		var node = jsdom.getElementsByTagName("id")[0];
		var location = node.childNodes[0].nodeValue;
		return location;
	},
	
	/**
	 * Recorre las reutilizaciones que se producen desde dos instancias de herramientas para determinar si se produciría un
	 * ciclo si se quiere que la instancia origen reutilice una instancia destino 
	 *
	 * @param toolInstanceSource Instancia de herramienta que va a reutilizar a otra
	 * @param toolInstanceTarge La instancia de herramienta que se va a reutilizar
	 * @return boolean Es posible reutilizar o no sin que haya ciclos
	 */
	toolInstanceCanReuseAnother : function(toolInstanceSource, toolInstanceTarget) {
		if (toolInstanceSource.getId() != toolInstanceTarget.getId()){
			if (toolInstanceTarget.getLocation()) {
				if (ToolInstanceReuse.reusesToolInstance(toolInstanceTarget)) {
					// Es una reutilización. Seguir recorriendo reutilizaciones
					return ToolInstanceReuse.toolInstanceCanReuseAnother(toolInstanceSource, ToolInstanceContainer.getToolInstance(toolInstanceTarget.getLocation()));
				} else {
					// Es una instancia de herramienta creada (un gluelet). No hay más reutilizaciones y no hemos llegado a la instancia original
					return true;
				}
			} else {
				// No se referencia a ninguna otra y no hemos llegado a la instancia original
				return true;
			}
		}
		else{
			//Se produciría un ciclo al reutilizar. Puesto que se llega a la instancia original
			return false;
		}
	}

}
