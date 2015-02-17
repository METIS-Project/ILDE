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
 *  Funcionalidad asociada a una actividad
 */

var Activity = function(data){
	
	var data = data;
	
	this.getData = function(){
		return data;
	},
	
	/**
	 * Obtiene el identificador de la actividad
	 * @return Id de la actividad
	 */
	this.getId = function(){
		return data.id;
	},
	
	/**
	 * Obtiene el nombre de la actividad
	 * @return Nombre de la actividad
	 */
	this.getName = function(){
		if (data.name)
		{
			return data.name;
		}
		else
		{
			return "";
		}
		return data.name;
	},
	
	/** 
	 * Asigna un nombre a la actividad
	 * @param name Nombre a asignar a la actividad
	 */
	this.setName = function(name){
		data.name = name;
	},
	
	/**
	 * Obtiene la descripción de la actividad
	 */
	this.getDescription = function(){
		if (data.description)
		{
			return data.description;
		}
		else
		{
			return "";
		}
	},
	
	
	/**
	 * Asigna una descripción a la actividad
	 * @paran description Descripción de la actividad 
	 */
	this.setDescription = function(description){
		data.description = description;
	},
	
	/**
	 * Actividad desplegable o no
	 * return {boolean}
	 */
	this.getToDeploy = function(){
		if (typeof(data.toDeploy) != "undefined")
		{		
			return data.toDeploy;
		}
		else{
			//Por defecto, la actividad será desplegable
			return true;
		}
	},
	
	/**
	 * Indica si la actividad es desplegable o no
	 * @param toDeploy Actividad desplegable o no
	 */
	this.setToDeploy = function(toDeploy){
		data.toDeploy = toDeploy;
	},
	
	this.getChildrenSequenceMode = function(){
		return data.childrenSequenceMode;
	},
	
	/**
	 * Obtiene la actividad padre de la actividad
	 * @return Actividad padre o false si no tiene
	 */
	this.getParentActivity = function(){
		if (typeof(data.parentActivityId) != "undefined")
		{		
			return ActivityContainer.getActivity(data.parentActivityId);
		}
		else{
			return false;
		}
	}
	
	/**
	 * Obtiene las instancias de la actividad
	 * @return Instancias de la actividad
	 */
	this.getInstancedActivities = function() {
		return InstancedActivityContainer.getInstancedActivities(data.id);
	},
	
	
	/**
	 * Alguna de las instancias de la actividad tiene como grupo el especificado
	 * @param group Grupo del que se quiere saber si alguna de las instancias de la actividad le tiene
	 */
	this.containsGroup = function(group){
		var instanced = InstancedActivityContainer.getInstancedActivities(data.id);
		for (var i=0; i<instanced.length;i++)
		{
			if (instanced[i].getGroup().getId()==group.getId())
			{
				return true;
			}
		}
		return false;
	},
	
	/**
	 * Obtiene las herramientas de la actividad
	 * @return Herramientas de la actividad
	 */	
	this.getTools = function()
	{
		var tools = new Array();
		if (data.resourceIds)
		{
			for (var i=0; i < data.resourceIds.length; i++)
			{
				var tool = ToolContainer.getTool(data.resourceIds[i]);
				if (tool)
				{
					tools.push(tool);
				}
				
			}
			
		}
		return tools;
	},
	
	/**
	 * Obtiene los recursos de la actividad
	 * @return Recursos de la actividad
	 */
	this.getResources = function()
	{
		var resources = new Array();
		if (data.resourceIds)
		{
			for (var i=0; i < data.resourceIds.length; i++)
			{
				var resource = ResourceContainer.getResource(data.resourceIds[i]);
				if (resource)
				{
					resources.push(resource);
				}
				
			}
			
		}
		return resources;
	},
	
	/**
	 * Indica si el recurso se utiliza  en la actividad
	 * @param Recurso que se desea saber si se utiliza en la actividad
	 * @return {bool} El recurso se usa o no en la actividad
	 */
	this.availableResource = function(resource){
		if (data.resourceIds)
		{
			for (var i=0; i < data.resourceIds.length; i++)
			{
				if (data.resourceIds[i] == resource.getId())
				{
					var resource = ResourceContainer.getResource(data.resourceIds[i]);
					//Comprobamos que es de tipo recurso
					if (resource)
					{
						return true;
					}
				
				}			
			}
		}
		return false;
	},
	
	/**
	 * Indica si la herramienta se utiliza  en la actividad
	 * @param Recurso que se desea saber si se utiliza en la actividad
	 * @return {bool} El recurso se usa o no en la actividad
	 */
	this.availableTool = function(tool){
		if (data.resourceIds)
		{
			for (var i=0; i < data.resourceIds.length; i++)
			{
				if (data.resourceIds[i] == tool.getId())
				{
					var tool = ToolContainer.getTool(data.resourceIds[i]);
					//Comprobamos que es de tipo herramienta
					if (tool)
					{
						return true;
					}
				
				}			
			}
		}
		return false;
	},
	
	/**
	 * Añade un recurso a la actividad
	 * @param resource Recurso a añadir a la actividad
	 */
	this.addResource = function(resource){
		if (!data.resourceIds)
		{
			data.resourceIds = new Array();
		}
		data.resourceIds.push(resource.getId());
	},
	
	/**
	 * Añade una herramienta a la actividad
	 * @param tool Herramienta a añadir a la actividad
	 */
	this.addTool = function(tool){
		if (!data.resourceIds)
		{
			data.resourceIds = new Array();
		}
		data.resourceIds.push(tool.getId());
	},
	
	/**
	 * Borra un recurso de la actividad
	 * @param resource Recurso a borrar de la actividad
	 */
	this.deleteResource = function(resource){
		if (data.resourceIds)
		{
			for (var i=0; i < data.resourceIds.length; i++)
			{
				if (data.resourceIds[i] == resource.getId())
				{
					//Comprobamos que es de tipo recurso
					var resource = ResourceContainer.getResource(data.resourceIds[i]);
					if (resource)
					{
						data.resourceIds.splice(i,1);
						return true;
					}
				
				}			
			}
		}
		return false;
	},
	
	/**
	 * Borra una herramienta de la actividad
	 * @param tool Herramienta a borrar de la actividad
	 */
	this.deleteTool = function(tool){
		if (data.resourceIds)
		{
			for (var i=0; i < data.resourceIds.length; i++)
			{
				if (data.resourceIds[i] == tool.getId())
				{
					//Comprobamos que es de tipo herramienta
					var tool = ToolContainer.getTool(data.resourceIds[i]);
					if (tool)
					{
						data.resourceIds.splice(i,1);
						return true;
					}
				
				}			
			}
		}
		return false;
	}
}