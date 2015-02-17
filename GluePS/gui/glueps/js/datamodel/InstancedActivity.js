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

 * Funcionalidad asociada a una instancia de actividad
 */

var InstancedActivity = function(data){
	
	var data = data;
	
	this.getData = function(){
		return data;
	}
	
	this.getId = function(){
		return data.id;
	},
	
	this.getActivityId = function(){
		return data.activityId;
	},
	
	/**
	 * Obtiene el grupo asociado a la actividad instanciada
	 */
	this.getGroup = function(){
		return GroupContainer.getGroup(data.groupId);		
	},

	/**
	 * Obtiene los recursos que se usan en la actividad instanciada
	 */
	this.getResources  = function() {
		if (data.resourceIds) {
			var resources = new Array();
			for (var i=0; i<data.resourceIds.length;i++)
			{
				var res = ResourceContainer.getResource(data.resourceIds[i]);
				resources.push(res);
			}
			return resources;
		} else {
			return false;
		}
	},
	
	/**
	 * Obtiene las instancias de herramientas que se usan en la actividad instanciada
	 */
	this.getToolInstances = function() {
		if (data.instancedToolIds) {
			var toolInstances = new Array();
			for (var i=0; i<data.instancedToolIds.length;i++)
			{
				var toolInst = ToolInstanceContainer.getToolInstance(data.instancedToolIds[i]);
				toolInstances.push(toolInst);
			}
			return toolInstances;
		} else {
			return false;
		}
	},
	
	/** El recurso especificado se usa o no en la actividad instanciada
	 * @param resource Recurso del que se desea conocer si se usa o no en la actividad instanciada
	 * 
	 */
	this.usesResource = function(resource) {
		if (data.resourceIds) {
			for (var i=0; i<data.resourceIds.length;i++)
			{
				if (data.resourceIds[i] == resource.getId())
				{
					return true;
				}
			}
		} 
		return false;
	},
	
	/** La herramienta especificada se usa o no en la actividad instanciada
	 * @param tool Herramienta de la que se desea conocer si se usa o no en la actividad instanciada
	 * 
	 */
	this.usesTool = function(tool) {
		if (data.instancedToolIds) {
			for (var i=0; i<data.instancedToolIds.length;i++)
			{
				var toolInst = ToolInstanceContainer.getToolInstance(data.instancedToolIds[i]);
				var currentTool = toolInst.getTool();
				if (currentTool.getId() == tool.getId())
				{
					return true;
				}
			}
		} 
		return false;
	},
	
	/**
	 * Se añade un recurso existente a la instancia de la actividad
	 * @param resource Recurso a añadir a la actividad
	 */
	this.addResource =  function(resource) {
		if (data.resourceIds) {
			data.resourceIds.push(resource.getId());
		} else {
			data.resourceIds = new Array();
			data.resourceIds.push(resource.getId());
		}
	},
	
	/**
	 * Se borra un recurso de la instancia de la actividad
	 * @param resource Recurso a añadir a la actividad
	 */
	this.deleteResource =  function(resource) {
		if (data.resourceIds) {
			for (var i=0; i<data.resourceIds.length;i++)
			{
				if (data.resourceIds[i] == resource.getId())
				{
					data.resourceIds.splice(i, 1);
					return true;
				}
			}
		} 
		return false;
	},
	
	/**
	 * Se borra una instancia de herramienta de la instancia de la actividad
	 * @param resource Recurso a añadir a la actividad
	 */
	this.deleteToolInstance =  function(toolInstance) {
		if (data.instancedToolIds) {
			for (var i=0; i<data.instancedToolIds.length;i++)
			{
				if (data.instancedToolIds[i] == toolInstance.getId())
				{
					data.instancedToolIds.splice(i, 1);
					return true;
				}
			}
		} 
		return false;
	},
	
	/**
	 * Se borra una instancia de herramienta de la instancia de la actividad
	 */
	this.deleteToolInstanceId =  function(toolInstanceId, position) {
		if (data.instancedToolIds) {
			if (data.instancedToolIds[position] == toolInstanceId)
			{
				data.instancedToolIds.splice(position, 1);
				return true;
			}
		} 
		return false;
	},
	
	/**
	 * Cambia la referencia de una instancia de herramienta de la instancia de la actividad
	 */
	this.setToolInstanceId =  function(position, toolInstanceId) {
		if (data.instancedToolIds && data.instancedToolIds.length > position) {
			data.instancedToolIds[position] = toolInstanceId;
			return true;
		} 
		return false;
	},
	
	/**
	 * Se crea y añade una instancia de herramienta a la instancia de la actividad
	 * @param tool Herramienta de la que se creará la instancia que se va a añadir a la actividad
	 */
	this.addToolInstance =  function(tool) {
		if (!data.instancedToolIds) {
			data.instancedToolIds = new Array();
		}
		var group = GroupContainer.getGroup(data.groupId);
		//El nombre de la instancia de herramienta será el nombre de la herramienta y entre paréntesis su grupo
		var name = tool.getName() + " (" + group.getName() + ")";
		var created = ToolInstanceContainer.addToolInstance(name, tool);
		data.instancedToolIds.push(created.getId());
		return created;
	},
	
	/**
	 * Add an existing tool instance to the activity
	 * @param toolInstance the tool instance to be added
	 * 
	 */
	this.addExistingToolInstance =  function(toolInstance) {
		if (!data.instancedToolIds) {
			data.instancedToolIds = new Array();
		}
		data.instancedToolIds.push(toolInstance.getId());
	}

}