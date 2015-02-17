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
 * Funcionalidad asociada al learning environment
 */

var LearningEnvironment = {

	getLearningEnvironmentId: function() {
		return JsonDB.deploy.learningEnvironment.id;
	},
	
	getLearningEnvironmentName: function() {
		return JsonDB.deploy.learningEnvironment.name;
	},
	
	getLearningEnvironmentType: function() {
		return JsonDB.deploy.learningEnvironment.type;
	},
	
	getLearningEnvironmentParameters: function() {
		return JsonDB.deploy.learningEnvironment.parameters;
	},
	
	getCreduser: function() {
		return JsonDB.deploy.learningEnvironment.creduser;
	},
	
	getInternalTools : function() {
		var toolList = new Array();
		var internalTools = JsonDB.deploy.learningEnvironment.internalTools;
		for ( var it in internalTools) {
			var obj = {
					key: it,
					value: internalTools[it]
			};
			toolList.push(obj);
		}
		return toolList;
	},
	
	getExternalTools : function() {
		var toolList = new Array();
		var externalTools = JsonDB.deploy.learningEnvironment.externalTools;
		for ( var it in externalTools) {
			var obj = {
					key: it,
					value: externalTools[it]
			};
			toolList.push(obj);
		}
		return toolList;
	},
	
	getTool: function(toolId){
		var internalTools = this.getInternalTools();
		for (var i=0; i<internalTools.length;i++)
		{
			if (internalTools[i].key==toolId)
				{
				return internalTools[i];
				}
		}
		var externalTools = this.getExternalTools();
		for (var i=0; i<externalTools.length;i++)
		{
			if (externalTools[i].key==toolId)
				{
				return externalTools[i];
				}
		}
		return false;
	},
	
	isInternal: function(toolId){
		var internalTools = this.getInternalTools();
		for (var i=0; i<internalTools.length;i++)
		{
			if (internalTools[i].key==toolId)
				{
				return true;
				}
		}
		return false;
	},
	
	isExternal: function(toolId){
		var externalTools = this.getExternalTools();
		for (var i=0; i<externalTools.length;i++)
		{
			if (externalTools[i].key==toolId)
				{
				return true;
				}
		}
		return false;
	},
	getShowAr: function(){
		if (typeof(JsonDB.deploy.learningEnvironment.showAr) != "undefined")
		{		
			return JsonDB.deploy.learningEnvironment.showAr;
		}
		else{
			//Por defecto, no se ven los controles AR
			return false;
		}
	}
}