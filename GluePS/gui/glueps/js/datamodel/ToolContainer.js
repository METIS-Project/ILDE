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
 * Funcionalidad asociada a las herramientas
 */

var ToolContainer = {

	getTool : function(toolId) {
		var tools = JsonDB.deploy.design.resources;
		for ( var i = 0; i < tools.length; i++) {
			if (tools[i].id == toolId && tools[i].instantiable==true) {
				return new Tool(tools[i]);
			}
		}
		return false;
	},

	getAllTools : function() {
		var toolList = new Array();
		var tools = JsonDB.deploy.design.resources;
		if (tools){
			for ( var i = 0; i < tools.length; i++) {
				//Las herramientas son instanciables
				if (tools[i].instantiable==true){
					toolList.push(new Tool(tools[i]));
				}
			}
		}
		return toolList;
	},
	
	
	addTool : function(name, toolKind, toolType) {
		var data = new Object();
		data.id = this.getNextToolId();
		data.instantiable = true; //Las herramientas son instanciables
		data.name = name;
		data.toolKind = toolKind;
		data.toolType = toolType;
		if (JsonDB.deploy.design.resources) {
			JsonDB.deploy.design.resources.push(data);
			//Ordena todos los recursos (documentos y herramientas)
			ResourceContainer.sortResources();
		} else {
			JsonDB.deploy.design.resources = new Array();
			JsonDB.deploy.design.resources.push(data);
		}
		return ToolContainer.getTool(data.id);
	},
	
	getNextToolId : function() {
		var maxId = 0;
		if (JsonDB.deploy.design.resources) {
			var resources = JsonDB.deploy.design.resources;
			for ( var i = 0; i < resources.length; i++) {
				var id = resources[i].id;
				if (resources[i].id.length > 4
						&& resources[i].id.substring(0, 4) == "TID_") {
					var id = parseInt(resources[i].id.substring(4));
					if (id > maxId) {
						maxId = id;
					}
				}
			}
		}
		return "TID_" + parseInt(maxId + 1);
	},
	
	deleteTool : function(tool) {
		if (JsonDB.deploy.design.resources) {
			var resources = JsonDB.deploy.design.resources;
			for ( var i = 0; i < resources.length; i++) {
				if (resources[i].id == tool.getId()) {
					resources.splice(i, 1);
					return true;
				}
			}
		}
		return false;
	}
}
