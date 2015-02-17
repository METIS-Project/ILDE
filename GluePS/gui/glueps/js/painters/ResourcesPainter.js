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
var ResourcesPainter = {

	identifiers : {},

	init : function() {

	},

	paint : function() {
		ResourcesPainter.buildIdentifiers();
		dojo.byId("resourceTitle").innerHTML=i18n.get("resourceTitle");
		var divResources = dojo.byId("divResources");
		while (divResources.childNodes.length > 0)
		{
			divResources.removeChild(divResources.childNodes[0]);
		}
		var table = document.createElement("table");
		table.setAttribute("class", "tableInfo");
		divResources.appendChild(table);
		var tr = document.createElement("tr");
		table.appendChild(tr);
		var th = document.createElement("th");
		th.innerHTML = "";
		th.setAttribute("class", "wb");
		tr.appendChild(th);
		var th = document.createElement("th");
		th.innerHTML = i18n.get("resourceTableId");
		tr.appendChild(th);
		var th = document.createElement("th");
		th.innerHTML = i18n.get("resourceTableName");
		tr.appendChild(th);
		var th = document.createElement("th");
		th.innerHTML = i18n.get("resourceTableType");
		tr.appendChild(th);

		// Recursos
		var resources = ResourceContainer.getAllResources();
		for ( var i = 0; i < resources.length; i++) {
			var tr = document.createElement("tr");
			tr.setAttribute("id", "tr" + ResourcesPainter.identifiers[resources[i].getId()]);
			table.appendChild(tr);
			ResourcesPainter.addResourceEvents(tr, resources[i]);			
			ResourcesPainter.addResourceImage(tr, resources[i]);
			
			var td = document.createElement("td");
			td.setAttribute("class", "item");
			td.innerHTML = ResourcesPainter.identifiers[resources[i].getId()];
			tr.appendChild(td);

			var td = document.createElement("td");
			td.setAttribute("id", "tdResource" + resources[i].getId());
			td.innerHTML = resources[i].getName();
			tr.appendChild(td);
			ResourcesPainter.selectResourceEvents(tr, resources[i]);	
			
			var td = document.createElement("td");
			tr.appendChild(td);
			
			var td = document.createElement("td");
			td.setAttribute("class", "counter");
			var number = ResourcesPainter.numberOfUsesResource(resources[i]);
			if (number > 1){
				td.innerHTML = "x " + number;
			}
			tr.appendChild(td);
			
		}
		// Herramientas
		var tools = ToolContainer.getAllTools();
		for ( var i = 0; i < tools.length; i++) {

			var tr = document.createElement("tr");
			tr.setAttribute("id", "tr" + ResourcesPainter.identifiers[tools[i].getId()]);
			table.appendChild(tr);
			ResourcesPainter.addToolEvents(tr, tools[i]);
			ResourcesPainter.addToolImage(tr, tools[i]);

			var td = document.createElement("td");
			td.setAttribute("class", "item");
			td.innerHTML = ResourcesPainter.identifiers[tools[i].getId()];
			tr.appendChild(td);

			var td = document.createElement("td");
			td.setAttribute("id", "tdTool" + tools[i].getId());
			td.innerHTML = tools[i].getName();
			tr.appendChild(td);
			ResourcesPainter.selectToolEvents(tr, tools[i]);	

			var td = document.createElement("td");
			td.innerHTML = tools[i].getToolVle().value;
			tr.appendChild(td);
			
			var number = ResourcesPainter.numberOfUsesTool(tools[i]);
			if (number > 1){
				var td = document.createElement("td");
				td.setAttribute("class", "counter");
				td.innerHTML = "x " + number;
				tr.appendChild(td);
			}
			//Si hay herramientas sin tipo definido se añade una alerta
			if (tools[i].getToolVle()==false)
			{
				ResourcesPainter.addExclamationImage(tr, tools[i]);
				
			}
		}
		ResourcesPainter.addResourceIcon(divResources);
	
	},
	
	addResourceImage: function(tr, resource){
		var td = document.createElement("td");
		var resourceIcon = document.createElement("img");
		var id = "resource" + resource.getId();
		resourceIcon.setAttribute("id", id);
		var src = ResourcesPainter.getResourceIconSrc(resource);
		resourceIcon.setAttribute("src", src);
		resourceIcon.setAttribute("width", "20");
		resourceIcon.setAttribute("height", "20");
		td.setAttribute("class", "wb");
		td.appendChild(resourceIcon);
		tr.appendChild(td);
		
		MenuManager.registerThing(resourceIcon, {
			getItems : function(data) {
				return ResourceMenu.getResourceMenu(data);
			},
			data : {
				resource: resource
			},
			onClick : function(data) {
			},
			menuStyle : "default"
		});
	},
	
	addToolImage: function(tr, tool){
		var td = document.createElement("td");
		var toolIcon = document.createElement("img");
		var id = "tool" + tool.getId();
		toolIcon.setAttribute("id", id);
		var src = ResourcesPainter.getToolIconSrc(tool);
		toolIcon.setAttribute("src", src);
		toolIcon.setAttribute("width", "20");
		toolIcon.setAttribute("height", "20");
		td.setAttribute("class", "wb");
		td.appendChild(toolIcon);
		tr.appendChild(td);
		
		MenuManager.registerThing(toolIcon, {
			getItems : function(data) {
				return ToolMenu.getToolMenu(data);
			},
			data : {
				tool: tool
			},
			onClick : function(data) {
			},
			menuStyle : "default"
		});
	},
	
	addExclamationImage: function(tr, tool){
		var td = document.createElement("td");
		td.style.paddingLeft = 0;
		var exclamationIcon = document.createElement("img");
		var id = "exclamationTool" + tool.getId();
		exclamationIcon.setAttribute("id", id);
		exclamationIcon.setAttribute("src", "images/icons/exclamation.png");
		td.setAttribute("class", "wb");
		exclamationIcon.setAttribute("width", "12");
		exclamationIcon.setAttribute("height", "12");
		td.appendChild(exclamationIcon);
		tr.appendChild(td);
		
		var tooltip = new dijit.Tooltip({
			connectId:  [id],
			label: i18n.get("toolTypeInfo")
		});
	},
	

	
	addResourceIcon: function(divParent){
		var div = document.createElement("div");
		// Icono de añadir recursos
		var addResourceIcon = document.createElement("img");
		var id = "addResource";
		addResourceIcon.setAttribute("id", id);
		addResourceIcon.setAttribute("src", "images/icons/add.png");
		//addResourceIcon.setAttribute("title", "Añadir recurso");
		addResourceIcon.setAttribute("class", "addIcon");
		div.appendChild(addResourceIcon);
		var textNode = document.createElement("span");
		textNode.innerHTML = i18n.get("createResource");
		textNode.setAttribute("class", "clickableTextNode");
		div.appendChild(textNode);
		divParent.appendChild(div);
		
		// Asigno funcionalidad
		dojo.connect(addResourceIcon, "onclick", function() {
			GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
			ResourceDialog.showDialog();
		});
		
		dojo.connect(textNode, "onclick", function() {
			GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
			ResourceDialog.showDialog();
		});
	},
	
	addResourceEvents: function(tr, resource) {		
		//Le asociamos los eventos
		dojo.connect(tr, "onmouseover", function(){
			for (var actId in ActivityPainter.positions)
			{
				for (var instActId in ActivityPainter.positions[actId].instAct)
					{
						var resources = ActivityPainter.positions[actId].instAct[instActId].resources;
						if (resources)
						{
							for (var i=0; i<resources.length;i++)
							{
								var resourceInfo = ActivityPainter.positions[actId].instAct[instActId].resources[i];
								if (resourceInfo && resource.getId()== resourceInfo.id)
								{
									var src = ResourcesPainter.getResourceIconSrc(resource);
									var img = resourceInfo.img;
									img.xoriginal = img.getShape().x;
									img.yoriginal = img.getShape().y;
									img.setShape({
										width : ActivityPainter.resourceInfo.width + 10,  //Aumentamos el ancho
										height : ActivityPainter.resourceInfo.height + 10, //Aumentamos el alto
										x : img.getShape().x - 10/2,
										y : img.getShape().y - 10,
										src : src
									});
							
									var text = resourceInfo.text;
									text.setFont(ActivityPainter.textHighlighted.font).setFill(ActivityPainter.textHighlighted.fill);
								}
							}
						}
					}
			}
		});
		dojo.connect(tr, "onmouseout", function(){
			for (var actId in ActivityPainter.positions)
			{
				for (var instActId in ActivityPainter.positions[actId].instAct)
				{
					var resources = ActivityPainter.positions[actId].instAct[instActId].resources;
					if (resources)
					{
						for (var i=0; i<resources.length;i++)
						{
							var resourceInfo = ActivityPainter.positions[actId].instAct[instActId].resources[i];
							if (resourceInfo && resource.getId()== resourceInfo.id)
							{
								var src = ResourcesPainter.getResourceIconSrc(resource);
								var img = resourceInfo.img;
								img.setShape({
									width : ActivityPainter.resourceInfo.width,
									height : ActivityPainter.resourceInfo.height,
									x : img.xoriginal,
									y : img.yoriginal,
									src : src
								});
							
								var text = resourceInfo.text;
								text.setFont(ActivityPainter.text.font).setFill(ActivityPainter.text.fill);
							}
						}	
					}
				}
			}
		});
	},
	
	addToolEvents: function(tr, tool) {		
		//Le asociamos los eventos
		dojo.connect(tr, "onmouseover", function(){
			for (var actId in ActivityPainter.positions)
			{
				for (var instActId in ActivityPainter.positions[actId].instAct)
					{
						var toolInstances = ActivityPainter.positions[actId].instAct[instActId].toolInstances;
						if (toolInstances)
						{
							for (var i=0; i<toolInstances.length;i++)
							{
								var toolInstanceInfo = ActivityPainter.positions[actId].instAct[instActId].toolInstances[i];
								if (tool.getId()== ToolInstanceContainer.getToolInstance(toolInstanceInfo.id).getTool().getId())
								{
									var src = ResourcesPainter.getToolIconSrc(tool);
									var img = toolInstanceInfo.img;
									img.xoriginal = img.getShape().x;
									img.yoriginal = img.getShape().y;
									img.setShape({
										width : ActivityPainter.toolInstanceInfo.width + 10,  //Aumentamos el ancho
										height : ActivityPainter.toolInstanceInfo.height + 10, //Aumentamos el alto
										x : img.getShape().x - 10/2,
										y : img.getShape().y - 10,
										src : src
									});
							
									var text = toolInstanceInfo.text;
									text.fillOriginal = text.getFill();
									text.setFont(ActivityPainter.textHighlighted.font).setFill(ActivityPainter.textHighlighted.fill);
								}
							}
						}
					}
			}
		});
		dojo.connect(tr, "onmouseout", function(){
			for (var actId in ActivityPainter.positions)
			{
				for (var instActId in ActivityPainter.positions[actId].instAct)
				{
					var toolInstances = ActivityPainter.positions[actId].instAct[instActId].toolInstances;
					if (toolInstances)
					{
						for (var i=0; i<toolInstances.length;i++)
						{
							var toolInstanceInfo = ActivityPainter.positions[actId].instAct[instActId].toolInstances[i];
							if (tool.getId()== ToolInstanceContainer.getToolInstance(toolInstanceInfo.id).getTool().getId())
							{
								var src = ResourcesPainter.getToolIconSrc(tool);
								var img = toolInstanceInfo.img;
								img.setShape({
									width : ActivityPainter.toolInstanceInfo.width,
									height : ActivityPainter.toolInstanceInfo.height,
									x : img.xoriginal,
									y : img.yoriginal,
									src : src
								});
							
								var text = toolInstanceInfo.text;
								text.setFont(ActivityPainter.text.font).setFill(text.fillOriginal);
							}
						}	
					}
				}
			}
		});
	},
	
	/**
	 * Añade los eventos que se asocian a la selección/deselección del documento
	 * @param tr Fila del recurso
	 * @param resource Recurso al que se asocian los eventos
	 */
	selectResourceEvents: function(tr, resource) {			
		// Añadir tooltip
		var selectResourceInfo= i18n.get("resourceMenuActivityResourceHelp");
		var tooltip = new dijit.Tooltip({
			connectId:  "tr" + ResourcesPainter.identifiers[resource.getId()],
			label: selectResourceInfo,
			position: ["before", "above", "after"],
			showDelay: 1000
		});
		//Le asociamos su tooltip
		tr.tooltip = tooltip;
		
		//Le asociamos los eventos
		dojo.connect(tr, "onmouseover", function(){
			if (!StateManager.isSelectedResource(resource))
			{
				tr.tooltip.setAttribute("label",i18n.get("resourceMenuActivityResourceHelp"));
				tr.setAttribute("style", "cursor:pointer;");
				tr.tooltip.open();	
			}
			else{
				tr.tooltip.setAttribute("label",i18n.get("resourceMenuActivityResourceHelpUnselect"));
				tr.setAttribute("style", "cursor:pointer;");
				tr.tooltip.open();		
			}
		});
		
		dojo.connect(tr, "onclick", function() {
				if (!StateManager.isSelectedResource(resource))
				{
					//Selección de recurso
					StateManager.changeResourceState(resource);
					tr.setAttribute("class", "resourceSelected");
				}
				else{
					//Deselección de recurso
					StateManager.changeResourceState(resource);
					tr.setAttribute("class", "");
				}
		});		
	},
	
	/**
	 * Añade los eventos que se asocian a la selección/deselección de la herramienta
	 * @param tr Fila de la herramienta
	 * @param tool Herramienta a la que se asocian los eventos
	 */
	selectToolEvents: function(tr, tool) {			
		// Añadir tooltip
		var selectToolInfo= i18n.get("resourceMenuActivityToolHelp");
		var tooltip = new dijit.Tooltip({
			connectId:  "tr" + ResourcesPainter.identifiers[tool.getId()],
			label: selectToolInfo,
			position: ["before", "above", "after"],
			showDelay: 1000
		});
		//Le asociamos su tooltip
		tr.tooltip = tooltip;
		
		//Le asociamos los eventos
		dojo.connect(tr, "onmouseover", function(){
			if (!StateManager.isSelectedTool(tool))
			{
				tr.tooltip.setAttribute("label",i18n.get("resourceMenuActivityToolHelp"));
				tr.setAttribute("style", "cursor:pointer;");
				tr.tooltip.open();	
			}
			else{
				tr.tooltip.setAttribute("label",i18n.get("resourceMenuActivityToolHelpUnselect"));
				tr.setAttribute("style", "cursor:pointer;");
				tr.tooltip.open();		
			}
		});
		
		dojo.connect(tr, "onclick", function() {
				if (!StateManager.isSelectedTool(tool))
				{
					//Selección de herramienta
					StateManager.changeToolState(tool);
					tr.setAttribute("class", "toolSelected");
				}
				else{
					//Deselección de herramienta
					StateManager.changeToolState(tool);
					tr.setAttribute("class", "");
				}
		});		
	},
	
	getResourceIconSrc : function(resource) {
		var src;
		var extension ="";
		var location = resource.getLocation();
		var pointPosition = location.lastIndexOf(".");
		if (pointPosition!=-1)
		{
			extension = location.substring(pointPosition);
		}
		switch (extension) {
		case ".pdf": {
			src = "images/icons/pdf.png";
			break;
		}
		case ".doc": {
			src = "images/icons/doc.png";
			break;
		}
		case ".xls": {
			src = "images/icons/xls.png";
			break;
		}
		case ".pps": {
			src = "images/icons/pps.png";
			break;
		}
		case ".zip": {
			src = "images/icons/zip_icon.png";
			break;
		}
		default: {
			src = "images/icons/document.png";
			}
		}
		return src;
	},

	getToolIconSrc : function(tool) {
		var src;
		var toolType = tool.getToolVle().value;
		switch (toolType) {
		case "Google Presentations": {
			src = "images/icons/google_presentations.png";
			break;
		}
		case "Google Documents": {
			src = "images/icons/google_documents.png";
			break;
		}
		case "Google Spreadsheets": {
			src = "images/icons/google_spreadsheets.png";
			break;
		}
		case "Doodle": {
			src = "images/icons/doodle.png";
			break;
		}
		case "Wiki Page (MediaWiki)": {
			src = "images/icons/mediawiki.png";
			break;
		}
		default: {
			src = "images/icons/tool.png";
			}
		}
		return src;
	},
	
	numberOfUsesResource: function(resource) {
		var number = 0;
		//Sólo consideramos las actividades finales, que son las que se muestran
		var finalAct = ActivityContainer.getFinalActivities();
		for (var i=0; i< finalAct.length; i++)
		{
			var instActivities = finalAct[i].getInstancedActivities();
			for (var j=0; j<instActivities.length;j++)
			{
				if (instActivities[j].usesResource(resource))
				{
					number++;
				}
			}
		}
		return number;
	},
	
	numberOfUsesTool: function(tool) {
		var number = 0;
		//Sólo consideramos las actividades finales, que son las que se muestran
		var finalAct = ActivityContainer.getFinalActivities();
		for (var i=0; i < finalAct.length; i++)
		{
			var instActivities = finalAct[i].getInstancedActivities();
			for (var j=0; j<instActivities.length;j++)
			{
				var toolInstances = instActivities[j].getToolInstances();
				for (var k=0; k < toolInstances.length; k++)
				{
					if (toolInstances[k].getTool().getId()==tool.getId())
					{
						number++;
					}
				}
			}
		}
		return number;
	},

	buildIdentifiers : function() {
		// Recursos
		var resources = ResourceContainer.getAllResources();
		for ( var i = 0; i < resources.length; i++) {
			ResourcesPainter.identifiers[resources[i].getId()] = "R" + (i + 1);
		}
		// Herramientas
		var tools = ToolContainer.getAllTools();
		for ( var i = 0; i < tools.length; i++) {
			ResourcesPainter.identifiers[tools[i].getId()] = "H" + (i + 1);
		}
	}
}