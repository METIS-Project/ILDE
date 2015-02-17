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
 * Funcionalidad asociada a una herramienta
 */

var Tool = function(data)
{
	
	var data = data;
		
	this.getId =  function(){
		return data.id;
	},
	
	this.getName =  function(){
		return data.name;
	},	
	
	this.getToolKind =  function(){
		return data.toolKind;
	},
	
	this.getToolType =  function(){
		return data.toolType;
	},
	
	this.getToolVle = function(){
		return LearningEnvironment.getTool(data.toolType);
	},
	
	this.setName = function(name){
		data.name = name;
	},
	
	this.setToolKind = function(toolKind){
		data.toolKind = toolKind;
	},
	
	this.setToolType = function(toolType){
		data.toolType = toolType;
	},
		
	this.isExternal = function(){
		return (data.toolKind=="external");
	},
	
	this.isInternal = function(){
		return (data.toolKind=="internal");
	}
	
}





