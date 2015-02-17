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

var ActivityPainter = {

	positions: {},
	/**
	 * Posici�n inicial desde la que se pinta
	 */
	startPosition : {
		x : 50,//30
		y : 37//17
	},

	verticalSeparation : 50,

	separationBetweenGroups : 30,

	separationBetweenResources : 16,

	separationBetweenTools : 16,

	groupInfo : {
		width : 20,
		height : 20
	},

	resourceInfo : {
		width : 20,
		height : 20
	},

	toolInstanceInfo : {
		width : 20,
		height : 20
	},

	InstancedActivityPaint : {
		fill : [ 255, 255, 255, 1.0 ],
		emphasisFill : [ 255, 255, 255, 1.0 ],
		stroke : {
			color : "black",
			width : 0.5,
			cap : "butt",
			join : 2,
			style : "ShortDash"
		},
		emphasisStroke : {
			color : "green",
			width : 1.25,
			cap : "butt",
			join : 2,
			style : "ShortDash"
		},
		emphasisStrokeReuse : {
			color : "blue",
			width : 1.25,
			cap : "butt",
			join : 2,
			style : "ShortDash"
		}
	},
	
	ActivityPaint : {
		fill : [ 255, 255, 255, 0 ],
		emphasisFill : [ 255, 255, 255, 0 ],
		stroke : {
			color : "black",
			width : 0.5,
			cap : "butt",
			join : 2,
			style : "Long-Dash"
		},		
		emphasisStroke : {
			color : "black",
			width : 1,
			cap : "butt",
			join : 2,
			style : "Long-Dash"
		}
	},

	lineStroke : {
		color : "black",
		width : 1.5,
		cap : "round",
		join : 2,
		style : "Solid"
	},

	text : {
		font : {
			family : "Arial",
			size : "8pt",
			weight : "bold"
		},
		fill : "black"
	},
	
	textHighlighted : {
		font : {
			family : "Arial",
			size : "10pt",
			weight : "bold"
		},
		
		fill : [0, 0, 139, 1.0]
	},
	
	surfaceHeight: 150,

	init : function() {
		//Ordenar las instancias de las actividades
		InstancedActivityContainer.sortInstancedActivities();
		this.positions = {};
		this.notReuse = new Object();
		var tools = ToolContainer.getAllTools();
		for (var i=0; i < tools.length; i++)
		{
			this.notReuse[tools[i].getId()] = new Array();
		}
		var activities = ActivityContainer.getFinalActivities();
		for (var i = 0; i < activities.length; i++){
			var instances = activities[i].getInstancedActivities();
			for ( var j = 0; j < instances.length; j++) {
				var toolInstances = instances[j].getToolInstances();
				for (var k = 0; k < toolInstances.length; k++)
				{
					var tool = toolInstances[k].getTool();
					//No reutiliza. Guardamos el identificador
					if (!ToolInstanceReuse.reusesToolInstance(toolInstances[k]))
					{
						this.notReuse[tool.getId()].push(toolInstances[k].getId());							
					}
				}
			}
		}
		
	},
	
	/**
	 * Devuelve el estado (abierto/cerrado) de cada uno de los TitlePane que contienen las actividades
	 * @returns Array con el estado de cada TitlePane
	 */
	saveTitlePaneState: function(){
		var tpState = new Object();
		var activities = ActivityContainer.getFinalActivities();
		for (var i=0; i < activities.length; i++)
		{
			var id = "tp" + activities[i].getId();
			var tp = dijit.byId(id);
			if (tp)
			{
				tpState[id] = tp.open;
			}
			else{
				//Por defecto, se muestra abierto
				tpState[id] = true;
			}
		}
		return tpState;				
	},

	/**
	 * Pinta las actividades
	 */
	paint : function() {
		this.init();
		
		var tpState = this.saveTitlePaneState();
		
		var surface = new Array();
		dojo.byId("activityTitle").innerHTML=i18n.get("activityTitle");
		divPaintActivities = dojo.byId("divPaintActivities");
		while (divPaintActivities.childNodes.length > 0)
		{
			divPaintActivities.removeChild(divPaintActivities.childNodes[0]);
		}
		var activities = ActivityContainer.getFinalActivities();
		var divNode = new Array();
		var maxWidth = 200; // Almacena el ancho máximo de las superficies donde se pintan las actividades
		if (activities.length == 0)
		{
			this.addFirstActivity(divPaintActivities);
		}
		for ( var i = 0; i < activities.length; i++) {
			divNode[i] = document.createElement("div");
			var idDiv = "activity" + activities[i].getId();
			divNode[i].setAttribute("id", idDiv);

			surface[i] = dojox.gfx.createSurface(divNode[i], this.surfaceHeight, 150);
			this.paintActivity(activities[i], surface[i]);
			this.addDivDeployActivity(divNode[i], activities[i]);
			
			var dim = surface[i].getDimensions();
			if (parseInt(dim.width) > parseInt(maxWidth)) {
				maxWidth = parseInt(dim.width);
			}
		}
		maxWidth = maxWidth + 25;
		//maxWidth = maxWidth + 22;
		for ( var i = 0; i < activities.length; i++) {
			
			var id = "tp" + activities[i].getId();
			var tpOld = dijit.byId("tp" + activities[i].getId());
			//En caso de existir previamente se borra el TitlePane
			if (tpOld)
			{
				tpOld.destroy();
			}
			
			var tp;
			tp = new dijit.TitlePane({
				title : activities[i].getName(),
				open: tpState[id],
				id: id,
				content : divNode[i],
				style : {
					"padding" : "12px 15px",
					//"min-width" : maxWidth + "px" // Todos tendr�n de tama�o como m�nimo el ancho m�ximo,
					"width" : parseInt(maxWidth + 10) + "px" // Todos tendr�n de tama�o el ancho m�ximo
				}
			});

			divPaintActivities.appendChild(tp.domNode);		
			
			divNode[i].parentNode.setAttribute("style", "padding:5px;");
			
			surface[i].setDimensions(maxWidth, this.surfaceHeight);
			this.addActivityEvents(activities[i], this.positions[activities[i].getId()].bigGroup, maxWidth);
			
			// dojo.style(idDiv, "overflow", "auto");
		}
		this.moveImagesFront();
	},

	/**
	 * Pinta las instancias de una actividad
	 * 
	 * @param activity
	 *            Actividad que se debe pintar
	 * @param surface
	 *            Superficie en la que se pintar�
	 */
	paintActivity : function(activity, surface) {
		var activityId = activity.getId();

		//var positions = {}; //Almacena las posiciones de los elementos gr�ficos

		var instances = activity.getInstancedActivities();
		
		this.positions[activityId]={
				instAct: {}
		};

		//var bigGroup = surface.createGroup().setTransform([dojox.gfx.matrix.translate(10,10)]);
		var bigGroup = surface.createGroup();
		this.positions[activityId].bigGroup = bigGroup;
		var xini = this.startPosition.x;
		var yini = this.startPosition.y;
		
		this.addEditActivity(activity, bigGroup, 0, 0);
		this.addDeleteActivity(activity, bigGroup, 22, 0);
		this.addNewActivity(activity, bigGroup, 44, 0);
		this.addMoveUpActivity(activity, bigGroup, 66, 0);
		this.addMoveDownActivity(activity, bigGroup, 88, 0);		


		for ( var j = 0; j < instances.length; j++) {

			var instActId = instances[j].getId();
			this.positions[activityId].instAct[instActId]={};
			
			// Obtener y pintar los recursos
			var resources = instances[j].getResources();
			if (resources) {
				var totalResources = resources.length;
				this.positions[activityId].instAct[instActId].resources = new Array();
				for ( var i = 0; i < resources.length; i++) {
					var x = xini
							+ i
							* (this.resourceInfo.width + this.separationBetweenResources);
					var y = yini + this.verticalSeparation;
					var resourceImage = this.paintResource(activity, instances[j], resources[i], bigGroup, x, y, i);
					this.positions[activityId].instAct[instActId].resources.push({
						id : resources[i].getId(),
						x : x,
						y : y,
						img: resourceImage
					});
				}
			} else {
				var totalResources = 0;
			}
			var total = totalResources;
			// Obtener y pintar las instancias de herramientas
			var toolInst = instances[j].getToolInstances();
			if (toolInst) {
				var totalToolInstances = toolInst.length;
				this.positions[activityId].instAct[instActId].toolInstances = new Array();
				for ( var i = 0; i < toolInst.length; i++) {
					var x = xini
							+ (i + totalResources)
							* (this.toolInstanceInfo.width + this.separationBetweenResources);
					var y = yini + this.verticalSeparation;
					var toolInstanceImage = this.paintToolInstance(activity, instances[j], toolInst[i], i, bigGroup, x, y);
					this.paintToolInstanceState(activity, instances[j], toolInst[i], i, bigGroup, x, y+35);
					this.positions[activityId].instAct[instActId].toolInstances.push({
						id : toolInst[i].getId(),
						x : x,
						y : y,
						img: toolInstanceImage
					});
				}
			} else {
				var totalToolInstances = 0;
			}
			total = total + totalToolInstances;

			if (total > 0) {
				xfin = xini
						+ (total - 1)
						* (this.resourceInfo.width + this.separationBetweenResources)
						+ this.resourceInfo.width;
			} else {
				xfin = xini + this.resourceInfo.width;
			}

			var len = instances[j].getGroup().getName().visualLength();
			if (xini + len > xfin)
			{
				xfin = xini + len;
			}
			
			// Pintar el recuadro de la actividad instanciada
			var polygon = this.paintInstancedActivity(activity, instances[j], bigGroup, xini, xfin, yini);			
			this.positions[activityId].instAct[instActId].polygon = polygon;
			
			// Obtener y pintar los grupos
			var group = instances[j].getGroup();
			var x = (xini + xfin) / 2 - this.groupInfo.width / 2;
			var y = yini;
			var groupImage = this.paintGroup(activity, instances[j], group, bigGroup, x, y, polygon);
			this.positions[activityId].instAct[instActId].group = {
				id : group.getId(),
				x : x,
				y : y,
				img: groupImage
			};

			// Pintar las l�neas
			this.paintLines(this.positions[activityId].instAct[instActId], bigGroup);

			// Pintar las etiquetas de los nombres
			this.paintLabels(this.positions[activityId].instAct[instActId], bigGroup);
	
			var totalWidth = xfin - xini;
			xini = xini + totalWidth + this.separationBetweenGroups;
		}

		surface.setDimensions(xini, this.surfaceHeight);
	},
	
	addDivDeployActivity: function(parentNode, activity){
		var divDepAct = document.createElement("div");			
		var inputCheckBox = document.createElement("input");
		var id = "cb" + activity.getId();
		inputCheckBox.setAttribute("id", id);
		inputCheckBox.setAttribute("type", "checkbox");
		inputCheckBox.checked = activity.getToDeploy();
		divDepAct.appendChild(inputCheckBox);
		var labelCheckBox = document.createElement("label");
		labelCheckBox.style.marginLeft="5px";
		labelCheckBox.innerHTML = i18n.get("deployThisActivity");
		divDepAct.appendChild(labelCheckBox);			
		parentNode.appendChild(divDepAct);	
		
		dojo.connect(inputCheckBox, "onclick", function(){
			activity.setToDeploy(inputCheckBox.checked);
			GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
			JsonDB.notifyChanges();
		});
	},
	

	
	addEditActivity: function(activity, bigGroup, x, y)
	{
		var data = {
				width : 18,
				height : 18,
				x : x,
				y : y,
				src : "images/icons/edit.png"
		};
		var image = bigGroup.createImage(data);
		image.rawNode.setAttribute("style", "cursor:pointer;");
		GFXTooltip.register(image, i18n.get("activityEditHelp"));
		image.connect("onclick", this, function(){
			EditActivityDialog.showDialog(activity.getId());
		});		
	},
	
	/**
	 * Añade icono y funcionalidad de borrar una actividad
	 */
	addDeleteActivity: function(activity, bigGroup, x, y)
	{
		var data = {
				width : 18,
				height : 18,
				x : x,
				y : y,
				src : "images/icons/delete.png"
		};
		var image = bigGroup.createImage(data);
		image.rawNode.setAttribute("style", "cursor:pointer;");
		GFXTooltip.register(image, i18n.get("activityDeleteHelp"));
		image.connect("onclick", this, function(){
			ActivityMenu.showDeleteActivity(activity);
		});		
	},
	
	/**
	 * Añade icono y funcionalidad de añadir una actividad
	 */
	addNewActivity: function(activity, bigGroup, x, y)
	{
		var data = {
				width : 18,
				height : 18,
				x : x,
				y : y,
				src : "images/icons/add.png"
		};
		var image = bigGroup.createImage(data);
		GFXTooltip.register(image, i18n.get("activityCreateHelp"));
		
		MenuManager.registerThing(image, {
			getItems : function(data) {
				return CreateActivityMenu.getCreateActivityMenu(data);
			},
			data : {
				activity: activity,
			},
			menuStyle : "default",
			title : i18n.get("createActivityMenuAdd")
		});
		
		/*image.connect("onclick", this, function(){
			//ActivityContainer.addFinalActivity("name", "description", activity, true);
			//JsonDB.notifyChanges();
			CreateActivityDialog.showDialog(activity.getId());			
		});	*/	
	},
	
	/**
	 * A�ade icono y funcionalidad de mover hacia arriba una actividad
	 */
	addMoveUpActivity: function(activity, bigGroup, x, y)
	{
		var activities = ActivityContainer.getFinalActivities();
		for ( var i = 0; i < activities.length; i++) {
			if (activities[i].getId()== activity.getId()){	
				if (i==0){
					return;
				}
			}
		}
		var data = {
				width : 18,
				height : 18,
				x : x,
				y : y,
				src : "images/icons/up_icon.png"
		};
		var image = bigGroup.createImage(data);
		image.rawNode.setAttribute("style", "cursor:pointer;");
		GFXTooltip.register(image, i18n.get("activityMoveUpHelp"));
		
		image.connect("onclick", this, function(){
			var activities = ActivityContainer.getFinalActivities();
			for ( var i = 0; i < activities.length; i++) {
				if (activities[i].getId()== activity.getId()){
					var dataPrevious = activities[i-1].getData();
					var prevActId = dataPrevious.id;
					var prevActParentId = dataPrevious.parentActivityId;
					var dataActivity = activities[i].getData();
					var currentActId = dataActivity.id;
					var currentActParentId = dataActivity.parentActivityId;
					dataPrevious.parentActivityId = currentActParentId;
					dataActivity.parentActivityId = prevActParentId;
					ActivityContainer.updateFinalActivity(prevActId, prevActParentId,dataActivity);
					ActivityContainer.updateFinalActivity(currentActId, currentActParentId,dataPrevious);
					break;
				}			
			}
			JsonDB.notifyChanges();
		});
	},
	
	/**
	 * A�ade icono y funcionalidad de mover hacia abajo una actividad
	 */
	addMoveDownActivity: function(activity, bigGroup, x, y)
	{
		var activities = ActivityContainer.getFinalActivities();
		for ( var i = 0; i < activities.length; i++) {
			if (activities[i].getId()== activity.getId()){	
				if (i==(activities.length-1)){
					return;
				}
				else if (i==0){
					x = x - 22;
				}
			}
		}
		var data = {
				width : 18,
				height : 18,
				x : x,
				y : y,
				src : "images/icons/down_icon.png"
		};
		var image = bigGroup.createImage(data);
		image.rawNode.setAttribute("style", "cursor:pointer;");
		GFXTooltip.register(image, i18n.get("activityMoveDownHelp"));
		
		image.connect("onclick", this, function(){
			var activities = ActivityContainer.getFinalActivities();
			for ( var i = 0; i < activities.length; i++) {
				if (activities[i].getId()== activity.getId()){
					var dataActivity = activities[i].getData();
					var currentActId = dataActivity.id;
					var currentActParentId = dataActivity.parentActivityId;
					var dataNext = activities[i+1].getData();
					var nextActId = dataNext.id;
					var nextActParentId = dataNext.parentActivityId;
					dataActivity.parentActivityId = nextActParentId;
					dataNext.parentActivityId = currentActParentId;
					ActivityContainer.updateFinalActivity(currentActId, currentActParentId,dataNext);
					ActivityContainer.updateFinalActivity(nextActId, nextActParentId,dataActivity);
					break;
				}			
			}
			JsonDB.notifyChanges();
		});
	},
	
	/**
	 * Añade un icono y funcionalidad de añadir la primera actividad (cuando no hay ninguna otra)
	 */
	addFirstActivity: function(divParent){
		var div = document.createElement("div");
		// Icono de añadir actividad
		var addActivityIcon = document.createElement("img");
		var id = "addActivity";
		addActivityIcon.setAttribute("id", id);
		addActivityIcon.setAttribute("src", "images/icons/add.png");
		addActivityIcon.setAttribute("class", "addIcon");
		div.appendChild(addActivityIcon);
		var textNode = document.createElement("span");
		textNode.innerHTML = i18n.get("createActivity");
		textNode.setAttribute("class", "clickableTextNode");
		div.appendChild(textNode);
		divParent.appendChild(div);
		
		// Asigno funcionalidad
		dojo.connect(addActivityIcon, "onclick", function() {
			CreateActivityDialog.showDialog(null, false);
		});
		
		dojo.connect(textNode, "onclick", function() {
			CreateActivityDialog.showDialog(null, false);
		});
	},

	paintGroup : function(activity, instancedActivity, group, bigGroup, x, y, polygon) {
		var src = GroupsPainter.getGroupIconSrc(group);
		var data = {
			width : this.groupInfo.width,
			height : this.groupInfo.height,
			x : x,
			y : y,
			src : src
		};
		
		var title = group.getName();
		var toolTipMenu = ActivityGroupMenu.showTooltipGroupParticipants(group);

		var image = bigGroup.createImage(data);
		
		MenuManager.registerThing(image, {
			getItems : function(data) {
				return ActivityGroupMenu.getGroupMenu(data);
			},
			data : {
				activity: activity,
				group: group,
				instancedActivity: instancedActivity
			},
			onClick : function(data) {
							
				if (StateManager.isStateNormal())
				{
					var groupId = data.group.getId();
					EditGroupDialog.showDialog(groupId);
				}
				
				if (StateManager.isStateAddTool())
				{
						GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
						for (var i=0; i < StateManager.data.tools.length; i++)
						{
							instancedActivity.addToolInstance(StateManager.data.tools[i]);					
							if (activity.availableTool(StateManager.data.tools[i])==false)
							{
								activity.addTool(StateManager.data.tools[i]);
							}
						}
						JsonDB.notifyChanges();
				}
				
				if (StateManager.isStateAddResource())
				{
					GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
					for (var i=0; i < StateManager.data.resources.length; i++)
					{
						if (instancedActivity.usesResource(StateManager.data.resources[i]) == false) {
							instancedActivity.addResource(StateManager.data.resources[i]);						
							if (activity.availableResource(StateManager.data.resources[i])==false)
							{
								activity.addResource(StateManager.data.resources[i]);
							}
						}
					}
					JsonDB.notifyChanges();
				}
				
				if (StateManager.isStateAddTool())
				{
						GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
						instancedActivity.addToolInstance(StateManager.data.tool);
						
						if (activity.availableTool(StateManager.data.tool)==false)
						{
							activity.addTool(StateManager.data.tool);
						}
						JsonDB.notifyChanges();
				}
				
				if (StateManager.isStateAddParticipant())
				{
					GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
					for (var i=0; i<StateManager.data.participants.length;i++)
					{
						if (group.containsParticipant(StateManager.data.participants[i]) == false) {
							group.addParticipant(StateManager.data.participants[i]);
						}
					}
					JsonDB.notifyChanges();
				}
				
				if (StateManager.isStateCopyParticipant())
				{
					GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
					for (var i=0; i<StateManager.data.participantsCopy.length;i++)
					{
						if (group.containsParticipant(StateManager.data.participantsCopy[i]) == false) {
							group.addParticipant(StateManager.data.participantsCopy[i]);
						}
					}
					JsonDB.notifyChanges();
				}
				
				if (StateManager.isStateReuseToolInstance())
				{
					var canReuse = ToolInstanceReuse.canReuseToolInstance(data.instancedActivity, StateManager.data.toolInstance);
					if (canReuse){
						var tool = StateManager.data.toolInstance.getTool();
						GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
						var created = data.instancedActivity.addToolInstance(tool);
					
						if (activity.availableTool(tool)==false)
						{
							activity.addTool(tool);
						}
						//La instancia de herramienta creada tendrá como location el id de la instancia de herramienta que reutiliza
						created.setLocation(StateManager.data.toolInstance.getId());
						//Juan: Añado el position de la tool instance original
						created.setPosition(StateManager.data.toolInstance.getPosition());
						created.setMaxdistance(StateManager.data.toolInstance.getMaxdistance());
						created.setPositionType(StateManager.data.toolInstance.getPositionType());
						created.setScale(StateManager.data.toolInstance.getScale());
		//				created.setOrientation(StateManager.data.toolInstance.getOrientation());
						//Juan: Hasta aquí modificación
						JsonDB.notifyChanges();
					}
				}
			},
			menuStyle : "default",
			title: title,
			toolTipMenu: toolTipMenu
		});
		
		//Le asociamos los eventos
		image.connect("onmouseover", this, function(){
			image.rawNode.setAttribute("style", "cursor:default;");
			polygon.setStroke(ActivityPainter.InstancedActivityPaint.stroke);
			
			this.visible = GroupsPainter.areVisibleGroupParticipants(group);
			var id = "tr" + GroupsPainter.identifiers[group.getId()];
			var node = dojo.byId(id);
			node.style.backgroundColor="rgb(210, 220, 250)";
			//Si es necesario se muestran momentaneamente los participantes del grupo
			/*if (this.visible==false){
				GroupsPainter.showGroupParticipants(group);
			}*/
			
			if (StateManager.isStateAddResource())
			{   
				var canAdd = false;
				var resources = StateManager.data.resources;
				for (var i = 0; i < resources.length; i++)
				{
					if (instancedActivity.usesResource(resources[i])==false)
					{
						canAdd = true;
						break;
					}
				}
				if (canAdd)
				{
					image.rawNode.setAttribute("style", "cursor:url(images/cursors/document.cur),auto;");
					polygon.setStroke(ActivityPainter.InstancedActivityPaint.emphasisStroke);
				}
			}
			
			if (StateManager.isStateAddTool())
			{
				image.rawNode.setAttribute("style", "cursor:url(images/cursors/tool.cur),auto;");
				polygon.setStroke(ActivityPainter.InstancedActivityPaint.emphasisStroke);
			}
			
			if (StateManager.isStateReuseToolInstance())
			{
				if (ToolInstanceReuse.canReuseToolInstance(instancedActivity, StateManager.data.toolInstance)==true)
				{

					image.rawNode.setAttribute("style", "cursor:url(images/cursors/reset.cur),auto;");
				}
			}
			
			if (StateManager.isStateAddParticipant())
			{
				image.rawNode.setAttribute("style", "cursor:url(images/cursors/student.cur),auto;");
			}
				
			if (StateManager.isStateCopyParticipant())
			{
				image.rawNode.setAttribute("style", "cursor:url(images/cursors/student.cur),auto;");
			}
			
		});
		
		image.connect("onmouseout", this, function(){
			polygon.setStroke(ActivityPainter.InstancedActivityPaint.stroke);
			
			var id = "tr" + GroupsPainter.identifiers[group.getId()];
			var node = dojo.byId(id);
			node.style.backgroundColor="rgb(230, 240, 250)";
			if (this.visible==false){
				GroupsPainter.hideGroupParticipants(group);
			}
		});
		
		return image;
	},

	paintResource : function(activity, instancedActivity, resource, bigGroup, x, y, pos) {
		var src = ResourcesPainter.getResourceIconSrc(resource);
		var data = {
			width : this.resourceInfo.width,
			height : this.resourceInfo.height,
			x : x,
			y : y,
			src : src
		};
		var image = bigGroup.createImage(data);
		var imgId = "img" + instancedActivity.getId() + "_" + pos;
		image.rawNode.id= imgId;
		MenuManager.registerThing(image, {
			getItems : function(data) {
				return ActivityResourceMenu.getResourceMenu(data);
			},
			data : {
				activity: activity,
				instancedActivity: instancedActivity,
				resource: resource
			},
			onClick : function(data) {			
				if (StateManager.isStateNormal()){
					ActivityResourceMenu.openResource(data.resource);
				}	
			},
			menuStyle : "default",
			title : resource.getName()
		});
		
		//Le asociamos los eventos
		image.connect("onmouseover", this, function(){
			var id = "tr" + ResourcesPainter.identifiers[resource.getId()];
			var node = dojo.byId(id);
			this.bc = node.style.backgroundColor;
			node.style.backgroundColor="rgb(210, 220, 250)";
			//image.tooltip.open();
		});
		
		image.connect("onmouseout", this, function(){
			var id = "tr" + ResourcesPainter.identifiers[resource.getId()];
			var node = dojo.byId(id);
			node.style.backgroundColor=this.bc;
			//image.tooltip.close();
		});
		
		return image;
	},

	paintToolInstance : function(activity, instancedActivity, toolInstance, pos, bigGroup, x, y) {
		var tool = toolInstance.getTool();
		var src = ResourcesPainter.getToolIconSrc(tool);
		var data = {
			width : this.toolInstanceInfo.width,
			height : this.toolInstanceInfo.height,
			x : x,
			y : y,
			src : src
		};
		var image = bigGroup.createImage(data);
		
		var padding = 2;
		
		var points = [ {
			x : x - padding,
			y : y - padding
		}, {
			x : x + this.toolInstanceInfo.width + padding,
			y : y  -padding
		}, {
			x : x + this.toolInstanceInfo.width + padding,
			y : y + this.toolInstanceInfo.height + padding
		}, {
			x : x - padding,
			y : y + this.toolInstanceInfo.height + padding
		}, {
			x : x - padding,
			y : y - padding
		} ];

		var polygon = bigGroup.createPolyline(points);

		
		//Juan: incluído para	mostrar el mapa o el marcador de junaio

		var mapaOmarker = null;
		var markerId = toolInstance.getValidJunaioMarkerPosition();
		var position = toolInstance.getPosition();
		var coordenadas = toolInstance.getValidGeoposition();
		var hasQrCodePosition = toolInstance.hasValidQrCodePosition();
		if (markerId != null){			
			mapaOmarker = i18n.get("activityResourceMenuJunaiomarkerShowMarker") + markerId  + "<br><img src=\"" + position + "\" width=\"256\" height=\"258\"></img>";
		} else if (coordenadas != null){
			mapaOmarker = i18n.get("activityResourceMenuGeopositionShowMap")  + coordenadas +  "<br><img src=\"http://maps.google.com/maps/api/staticmap?zoom=15&size=310x200&maptype=roadmap&markers=color:blue|label:|" + coordenadas + "&sensor=false\"></img>";
		} else if (hasQrCodePosition) {
			mapaOmarker = "<br><img src=\"" + position + "\" width=\"256\" height=\"258\"></img>";
		}
		
		
		
		MenuManager.registerThing(image, {
			getItems : function(data) {
				return ActivityToolInstanceMenu.getToolInstanceMenu(data);
			},
			data : {
				activity: activity,
				instancedActivity: instancedActivity,
				toolInstance: toolInstance,
				position: pos
			},
			menuStyle : "default",
			title : toolInstance.getName(),
			//Juan: incluído para	mostrar el mapa o el marcador de junaio
			toolTipMenu : mapaOmarker
		});
		
		
		//Le asociamos los eventos
		image.connect("onclick", this, function(){			
			if (StateManager.isStateNormal())
			{
				if (tool.getToolKind()=="external" && toolInstance.getLocation() && !ToolInstanceReuse.reusesToolInstance(toolInstance)){
					ActivityToolInstanceMenu.openLocation(toolInstance);
				}
				else{
					var toolInstanceOriginal = ToolInstanceReuse.getOriginalToolInstance(toolInstance);
					var toolOriginal = toolInstanceOriginal.getTool();
					if (toolOriginal.getToolKind()=="external" && toolInstanceOriginal.getLocation() && !ToolInstanceReuse.reusesToolInstance(toolInstanceOriginal))
					{
						ActivityToolInstanceMenu.openLocation(toolInstanceOriginal);
					}
					else{
						if (ToolInstanceReuse.reusesToolInstance(toolInstance))
						{
							//nada
						}
						else{
							ToolInstanceConfiguration.toolInstanceConfiguration(toolInstance, pos, activity, instancedActivity);
						}
					}						
				}
			}					
				
			if (StateManager.isStateReuseToolInstance())
			{
				//Comprobar además que no se ha creado una instancia para esta
				var canBeReused = ToolInstanceReuse.toolInstanceCanReuseAnother(toolInstance, StateManager.data.toolInstance);
				if ( canBeReused && (!toolInstance.getLocation() || ToolInstanceReuse.reusesToolInstance(toolInstance)) )
				{
					toolInstance.setLocation(StateManager.data.toolInstance.getId());
					//Juan: Añado el position de la tool instance original
					toolInstance.setPosition(StateManager.data.toolInstance.getPosition());
					toolInstance.setMaxdistance(StateManager.data.toolInstance.getMaxdistance());
					toolInstance.setPositionType(StateManager.data.toolInstance.getPositionType());
					toolInstance.setScale(StateManager.data.toolInstance.getScale());
		//			toolInstance.setOrientation(StateManager.data.toolInstance.getOrientation());
					//Juan: Hasta aquí modificación
					//Comprobar si cambia la herramienta para actualizar el identificador del recurso
					var sourceTool = toolInstance.getTool();
					var destinyTool = StateManager.data.toolInstance.getTool();
					if (sourceTool.getId()!=destinyTool.getId())
					{
						toolInstance.setTool(destinyTool);
						//Si no se usa la herramienta en otra instancia de la actividad se borra como herramienta de la actividad
						var instanced = activity.getInstancedActivities();
						var used = false;
						for (var i=0; i< instanced.length; i++)
						{
							if (instanced[i].usesTool(sourceTool))
							{
								used = true;
							}
						}
						if (used==false)
						{
							activity.deleteTool(sourceTool);
						}	
					}
					JsonDB.notifyChanges();
				}
			}
		});
		
		//Le asociamos los eventos
		image.connect("onmouseover", this, function(){
			
			image.rawNode.setAttribute("style", "cursor:default;");
			
			//Resaltar la correspondiente entrada en la tabla de recursos
			var id = "tr" + ResourcesPainter.identifiers[tool.getId()];
			var node = dojo.byId(id);
			this.bc = node.style.backgroundColor;
			node.style.backgroundColor = "rgb(210, 220, 250)";
			
			if (StateManager.isStateReuseToolInstance())
			{
				var canBeReused = ToolInstanceReuse.toolInstanceCanReuseAnother(toolInstance, StateManager.data.toolInstance);
				//Comprobar además que no se ha creado una instancia para esta
				if ( canBeReused && (!toolInstance.getLocation() || ToolInstanceReuse.reusesToolInstance(toolInstance)) )
				{
					image.rawNode.setAttribute("style", "cursor:url(images/cursors/reset.cur),auto;");
					polygon.setStroke(this.InstancedActivityPaint.emphasisStrokeReuse);
				}
			}

		});
		
		image.connect("onmouseout", this, function(){
			var id = "tr" + ResourcesPainter.identifiers[tool.getId()];
			var node = dojo.byId(id);
			node.style.backgroundColor= this.bc;
			polygon.setStroke(null);
		});
		
		var toolInstanceOriginal = ToolInstanceReuse.getOriginalToolInstance(toolInstance);
		var toolOriginal = toolInstanceOriginal.getTool();
		if (toolOriginal.getToolKind()=="external" && toolInstanceOriginal.getLocation() && !ToolInstanceReuse.reusesToolInstance(toolInstanceOriginal))
		{
			ActivityPainter.addEventsToolInstanceReuse(image, toolInstance);				
		}
		else{
				if (ToolInstanceReuse.reusesToolInstance(toolInstance))
				{
					ActivityPainter.addEventsToolInstanceReuse(image, toolInstance);	
				}
		}
		
		return image;
	},
	
	paintToolInstanceState: function(activity, instancedActivity, toolInstance, pos, bigGroup, x, y) {
		var tool = toolInstance.getTool();
		//JUAN: Added new icons for AR positioning

		var positionType = toolInstance.getPositionType();
		var markerId = toolInstance.getValidJunaioMarkerPosition();
		var coordenadas = toolInstance.getValidGeoposition();
		var hasQrCodePosition = toolInstance.hasValidQrCodePosition();
		if (tool.getToolKind()=="external" && toolInstance.getLocation() && !ToolInstanceReuse.reusesToolInstance(toolInstance))
		{
			if (positionType == "junaiomarker"){
				if (markerId != null ) {
					src = "images/icons/document_c_junaiomarker.png";
				} else {
					src = "images/icons/document_c_junaiomarker_error.png";
				}
				
			} else if (positionType == "geoposition") {
				if (coordenadas != null) {
					src = "images/icons/document_c_geoposition.png";
				} else {
					src = "images/icons/document_c_geoposition_error.png";
				}
			} else if (positionType == "qrcode" && hasQrCodePosition) {
				src = "images/icons/document_c_qrcode.png";			
			} else {
				src = "images/icons/document_c.png";
			}
			var data = {
					width : 20,
					height : 20,
					x : x,
					y : y,
					src : src
				};
			var image = bigGroup.createImage(data);
			
			GFXTooltip.register(image, i18n.get("activityResourceMenuOpenHelp"));
			image.connect("onclick", this, function() {
				ActivityToolInstanceMenu.openLocation(toolInstance);
		    });

		}
		else{
			//Obtenemos la instancia de herramienta origen de las referencias
			var toolInstanceOriginal = ToolInstanceReuse.getOriginalToolInstance(toolInstance);
			var toolOriginal = toolInstanceOriginal.getTool();
			if (toolOriginal.getToolKind()=="external" && toolInstanceOriginal.getLocation() && !ToolInstanceReuse.reusesToolInstance(toolInstanceOriginal))
			{
				if (positionType == "junaiomarker"){
					if (markerId != null ) {
						src = "images/icons/document_cr_junaiomarker.png";
					} else {
						src = "images/icons/document_cr_junaiomarker_error.png";
					}
					
				} else if (positionType == "geoposition") {
					if (coordenadas != null) {
						src = "images/icons/document_cr_geoposition.png";
					} else {
						src = "images/icons/document_cr_geoposition_error.png";
					}
				} else if (positionType == "qrcode" && hasQrCodePosition) {
					src = "images/icons/document_cr_qrcode.png";
				} else {
					src = "images/icons/document_cr.png";
				}
				
				var data = {
					width : 20,
					height : 20,
					x : x,
					y : y,
					src : src
				};
				var image = bigGroup.createImage(data);
				GFXTooltip.register(image, i18n.get("activityResourceMenuOpenHelp"));
				
		        //Asigno funcionalidad
				image.connect("onclick", this, function() {
					var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(toolInstance);
					ActivityToolInstanceMenu.openLocation(toolInstanceOrigen);
		        });
				ActivityPainter.addEventsToolInstanceReuse(image, toolInstance);
			}
			else{
				if (ToolInstanceReuse.reusesToolInstance(toolInstance))
				{
					if (positionType == "junaiomarker"){
						src = "images/icons/reset_junaiomarker.png";		
					} else if (positionType == "geoposition") {
						src = "images/icons/reset_geoposition.png";
					} else {
						src = "images/icons/reset.png";
					}
					var data = {
							width : 20,
							height : 20,
							x : x,
							y : y,
							src : src
						};
					var image = bigGroup.createImage(data);					
					ActivityPainter.addEventsToolInstanceReuse(image, toolInstance);
					
				}
				else{
					
					if (toolOriginal.getToolKind()=="external")
					{
						src = "images/icons/edit.png";
						var data = {
								width : 18,
								height : 18,
								x : x,
								y : y,
								src : src
							};
						var image = bigGroup.createImage(data);
						GFXTooltip.register(image, i18n.get("ConfigureToolInfo"));
						
				        //Asigno funcionalidad
						image.connect("onclick", this, function() {
							ToolInstanceConfiguration.toolInstanceConfiguration(toolInstance, pos, activity, instancedActivity);
				        });						
						
					}
					//Si es de tipo webcontent se muestra una exclamación. La instancia no se creará de forma automática
					//JUAN: Añadido los tipos AR Image, 3D model, Google Forms (validation code) y Picasa
					if (toolInstance.getTool().getToolVle().value=="Web Content" 
						|| toolInstance.getTool().getToolVle().value=="AR Image"
						|| toolInstance.getTool().getToolVle().value=="3D Model"
						|| toolInstance.getTool().getToolVle().value=="Google Forms (validation code)"
						|| toolInstance.getTool().getToolVle().value=="Picasa")
					{
						src = "images/icons/exclamation.png";
						var data = {
							width : 12,
							height : 12,
							x : x + 19,
							y : y + 3,
							src : src
						};
						var image = bigGroup.createImage(data);
						//var id = "exclamation" + toolInstance.getId();
						
						GFXTooltip.register(image, i18n.get("webcontentInfo"));
						//Aparentemente dijit.tooltip no funciona aqu�
					}
				}
			}
		}
		if (image)
		{
			//Juan: incluído para	mostrar el mapa o el marcador de junaio
			var mapaOmarker = null;
			var markerId = toolInstance.getValidJunaioMarkerPosition();
			var position = toolInstance.getPosition();
			var coordenadas = toolInstance.getValidGeoposition();
			var hasQrCodePosition = toolInstance.hasValidQrCodePosition();
			if (markerId != null){			
				mapaOmarker = i18n.get("activityResourceMenuJunaiomarkerShowMarker") + markerId  + "<br><img src=\"" + position + "\" width=\"256\" height=\"258\"></img>";
			} else if (coordenadas != null){
				mapaOmarker = i18n.get("activityResourceMenuGeopositionShowMap")  + coordenadas +  "<br><img src=\"http://maps.google.com/maps/api/staticmap?zoom=15&size=310x200&maptype=roadmap&markers=color:blue|label:|" + coordenadas + "&sensor=false\"></img>";
			} else if (hasQrCodePosition) {
				mapaOmarker = "<br><img src=\"" + position + "\" width=\"256\" height=\"258\"></img>";
			}

			//Añadir eventos comunes a todas las instancias de herramientas. Independientemente de su estado
			ActivityPainter.addEventsToolInstance(image, toolInstance);
			//Añadir menú 
			MenuManager.registerThing(image, {
				getItems : function(data) {
					return ActivityToolInstanceMenu.getToolInstanceMenu(data);
				},
				data : {
					activity: activity,
					instancedActivity: instancedActivity,
					toolInstance: toolInstance,
					position: pos
				},
				menuStyle : "default",
				title : toolInstance.getName(),
				//Juan: incluído para	mostrar el mapa o el marcador de junaio
				toolTipMenu : mapaOmarker
			});
		}
	},
	
	/**
	 * Añade los eventos correspondientes a las images que representan el estado de la herramienta
	 * @param image
	 * @param toolInstance
	 */
	addEventsToolInstance: function(image, toolInstance){
	
		image.connect("onmouseover", this, function() {
			image.rawNode.setAttribute("style", "cursor:default;");
			var tool = toolInstance.getTool();
			//Resaltar la correspondiente entrada en la tabla de recursos
			var id = "tr" + ResourcesPainter.identifiers[tool.getId()];
			var node = dojo.byId(id);
			this.bc = node.style.backgroundColor;
			node.style.backgroundColor = "rgb(210, 220, 250)";
		});
		
		image.connect("onmouseout", this, function(){
			var tool = toolInstance.getTool();
			var id = "tr" + ResourcesPainter.identifiers[tool.getId()];
			var node = dojo.byId(id);
			node.style.backgroundColor= this.bc;
		});
	
	},
	
	/**
	 * Añade los eventos asociados a mostrar la instancia de herramienta que se reutiliza
	 * @param image
	 * @param toolInstance
	 */
	addEventsToolInstanceReuse: function(image, toolInstance){
	var tool = toolInstance.getTool();
    image.connect("onmouseover", this, function() {
		
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
							if (toolInstanceInfo.id == toolInstance.getLocation())
							{
								var src = ResourcesPainter.getToolIconSrc(tool);
								var img = toolInstanceInfo.img;
								img.moveToFront();
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
								text.setFont(ActivityPainter.textHighlighted.font).setFill(ActivityPainter.textHighlighted.fill);
							}
						}
					}
				}
		}
	    });
	    
		image.connect("onmouseout", this, function(){
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
							if (toolInstanceInfo.id == toolInstance.getLocation())
							{
								var src = ResourcesPainter.getToolIconSrc(tool);
								var img = toolInstanceInfo.img;
								if (img.xoriginal && img.yoriginal)
								{
									img.setShape({
										width : ActivityPainter.toolInstanceInfo.width,
										height : ActivityPainter.toolInstanceInfo.height,
										x : img.xoriginal,
										y : img.yoriginal,
										src : src
									});
								}
								var text = toolInstanceInfo.text;
								text.setFont(ActivityPainter.text.font).setFill(ActivityPainter.text.fill);
							}
						}	
					}
				}
			}
		});
	},

	paintLines : function(positions, bigGroup) {
		// Pintar lineas hacia los recursos
		if (positions.resources) {
			for ( var i = 0; i < positions.resources.length; i++) {
				var points = {
					x1 : positions.group.x + this.groupInfo.width / 2,
					y1 : positions.group.y + this.groupInfo.height,
					x2 : positions.resources[i].x + this.resourceInfo.width / 2,
					y2 : positions.resources[i].y
				};
				//bigGroup.createLine(points).setStroke(this.lineStroke).moveToBack();
				bigGroup.createLine(points).setStroke(this.lineStroke);
			}
		}
		// Pintar lineas hacia las herramientas
		if (positions.toolInstances) {
			for ( var i = 0; i < positions.toolInstances.length; i++) {
				var points = {
					x1 : positions.group.x + this.groupInfo.width / 2,
					y1 : positions.group.y + this.groupInfo.height,
					x2 : positions.toolInstances[i].x
							+ this.toolInstanceInfo.width / 2,
					y2 : positions.toolInstances[i].y
				};
				//bigGroup.createLine(points).setStroke(this.lineStroke).moveToBack();
				bigGroup.createLine(points).setStroke(this.lineStroke);
			}
		}
	},

	paintLabels : function(positions, bigGroup) {
		var xadd = 4;
		var yadd = 12;
		//Pintar el nombre del grupo
		if (positions.group){
			var group = GroupContainer.getGroup(positions.group.id);
			var id = group.getId();
			var text = bigGroup.createText(
			{
					x : positions.group.x + 10,// + xadd,
					y : positions.group.y - 4,
					//text : GroupsPainter.identifiers[id],
					text : GroupContainer.getGroup(positions.group.id).getName(),
					align : "middle"
			}).setFont(this.text.font).setFill(this.text.fill);
			//Añadir elemento texto a la información sobre posiciones
			positions.group.text = text;
			
		}
		// Pintar los nombres de los recursos
		if (positions.resources) {
			for ( var i = 0; i < positions.resources.length; i++) {
				var resource = ResourceContainer.getResource(positions.resources[i].id);
				var id = resource.getId();
				var text = bigGroup.createText(
						{
							x : positions.resources[i].x + xadd,
							y : positions.resources[i].y
									+ this.resourceInfo.height + yadd,
							text : ResourcesPainter.identifiers[id]
						}).setFont(this.text.font).setFill(this.text.fill);
				positions.resources[i].text = text;
			}
		}
		// Pintar los nombres de las herramientas
		if (positions.toolInstances) {
			
				for (var j = 0; j < positions.toolInstances.length; j++)
				{
					var toolInstance = ToolInstanceContainer.getToolInstance(positions.toolInstances[j].id);
					var tool = toolInstance.getTool();
					//No reutiliza. Guardamos la posici�n del identificador
					if (!ToolInstanceReuse.reusesToolInstance(toolInstance))
					{
						for (var k=0; k < this.notReuse[tool.getId()].length; k++)
						{
							if (this.notReuse[tool.getId()][k] ==toolInstance.getId())
							{
								pos = k+1;
								break;
							}
						}
					}
					else{
						//Reutiliza. Obtenemos el identificador del que reutiliza
						var origToolInstance = ToolInstanceReuse.getOriginalToolInstance(toolInstance);
						//Obtenemos su posición entre las instancias de la herramienta que se reutilizan
						for (var k=0; k < this.notReuse[tool.getId()].length; k++)
						{
							if (this.notReuse[tool.getId()][k] ==origToolInstance.getId())
							{
								pos = k+1;
								break;
							}
						}
					}
					
					var text = bigGroup.createText(
					{
						x : positions.toolInstances[j].x + xadd,
						y : positions.toolInstances[j].y + this.toolInstanceInfo.height + yadd,
						text : ResourcesPainter.identifiers[tool.getId()] + "-" + pos
						}).setFont(this.text.font).setFill(this.text.fill);
					positions.toolInstances[j].text = text;
				}
		}
	},

	paintInstancedActivity : function(activity, instancedActivity, bigGroup, xini, xfin, yini) {
		var points = [ {
			x : xini,
			y : yini
		}, {
			x : xfin,
			y : yini
		}, {
			x : xfin,
			y : yini + this.verticalSeparation + this.resourceInfo.height
		}, {
			x : xini,
			y : yini + this.verticalSeparation + this.resourceInfo.height
		}, {
			x : xini,
			y : yini
		} ];

		var polygon = bigGroup.createPolyline(points).setFill(
				this.InstancedActivityPaint.fill).setStroke(
				this.InstancedActivityPaint.stroke).moveToBack();
		
		MenuManager.registerThing(polygon, {
			getItems : function(data) {
				//return ActivityMenu.getActivityMenu(data);
			},
			data : {
				activity: activity,
				instancedActivity: instancedActivity
			},
			onClick : function(data) {
				if (StateManager.isStateAddResource())
				{
					GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
					for (var i=0; i < StateManager.data.resources.length; i++)
					{
						if (instancedActivity.usesResource(StateManager.data.resources[i]) == false) {
							instancedActivity.addResource(StateManager.data.resources[i]);						
							if (activity.availableResource(StateManager.data.resources[i])==false)
							{
								activity.addResource(StateManager.data.resources[i]);
							}
						}
					}
					JsonDB.notifyChanges();
				}
				
				if (StateManager.isStateAddTool())
				{
						GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
						for (var i=0; i < StateManager.data.tools.length; i++)
						{
							instancedActivity.addToolInstance(StateManager.data.tools[i]);					
							if (activity.availableTool(StateManager.data.tools[i])==false)
							{
								activity.addTool(StateManager.data.tools[i]);
							}
						}
						JsonDB.notifyChanges();
				}
				
				if (StateManager.isStateReuseToolInstance())
				{
					var canReuse = ToolInstanceReuse.canReuseToolInstance(data.instancedActivity,StateManager.data.toolInstance);
					if (canReuse){
						var tool = StateManager.data.toolInstance.getTool();
						GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
						var created = data.instancedActivity.addToolInstance(tool);
					
						if (activity.availableTool(tool)==false)
						{
							activity.addTool(tool);
						}
						//La instancia de herramienta creada tendrá como location el id de la instancia de herramienta que reutiliza
						created.setLocation(StateManager.data.toolInstance.getId());
						//Juan: Añado el position de la tool instance original
						created.setPosition(StateManager.data.toolInstance.getPosition());
						created.setMaxdistance(StateManager.data.toolInstance.getMaxdistance());
						created.setPositionType(StateManager.data.toolInstance.getPositionType());
						created.setScale(StateManager.data.toolInstance.getScale());
		//				created.setOrientation(StateManager.data.toolInstance.getOrientation());
						//Juan: Hasta aquí modificación
						//La instancia de herramienta creada tendr� como nombre el de la instancia de herramienta que reutiliza
						created.setName(StateManager.data.toolInstance.getName());
						JsonDB.notifyChanges();
					}
				}
			},
			menuStyle : "default"
		});
		
		dojo.connect(polygon.rawNode, "onmouseover", function(){
			
			polygon.setStroke(ActivityPainter.InstancedActivityPaint.stroke);
			polygon.rawNode.setAttribute("style", "cursor:default;");
			if (StateManager.isStateAddResource())
			{
				var canAdd = false;
				var resources = StateManager.data.resources;
				for (var i = 0; i < resources.length; i++)
				{
					if (instancedActivity.usesResource(resources[i])==false)
					{
						canAdd = true;
						break;
					}
				}
				if (canAdd)
				{
					polygon.rawNode.setAttribute("style", "cursor:url(images/cursors/document.cur),auto;");
					polygon.setStroke(ActivityPainter.InstancedActivityPaint.emphasisStroke);
				}
			}
			
			if (StateManager.isStateAddTool())
			{
					//Siempre es posible añadir una herramienta a una instancia de actividad
					polygon.rawNode.setAttribute("style", "cursor:url(images/cursors/tool.cur),auto;");
					polygon.setStroke(ActivityPainter.InstancedActivityPaint.emphasisStroke);
			}
			
			if (StateManager.isStateReuseToolInstance())
			{
				if (ToolInstanceReuse.canReuseToolInstance(instancedActivity, StateManager.data.toolInstance)==true)
				{

					polygon.rawNode.setAttribute("style", "cursor:url(images/cursors/reset.cur),auto;");
					polygon.setStroke(ActivityPainter.InstancedActivityPaint.emphasisStrokeReuse);
				}
			}		
		});
		
		dojo.connect(polygon.rawNode, "onmouseout", function(){
			polygon.setStroke(ActivityPainter.InstancedActivityPaint.stroke);
		});
		
		return polygon;
	},
	
	moveImagesFront: function()
	{
		var activities = ActivityContainer.getFinalActivities();
		for (var i = 0; i < activities.length; i++)
		{
			var instances = activities[i].getInstancedActivities();
			var activityId = activities[i].getId();
			for (var j = 0; j < instances.length; j++)
			{
				instActId = instances[j].getId();
				if (this.positions[activityId].instAct[instActId].group){
					this.positions[activityId].instAct[instActId].group.img.moveToFront();					
				}
				if (this.positions[activityId].instAct[instActId].resources){
					var resources = this.positions[activityId].instAct[instActId].resources;
					for (var k = 0; k < resources.length; k++)
					{
						resources[k].img.moveToFront();
					}
				}
				if (this.positions[activityId].instAct[instActId].toolInstances){
					var toolInstances = this.positions[activityId].instAct[instActId].toolInstances;
					for (var k = 0; k < toolInstances.length; k++)
					{
						toolInstances[k].img.moveToFront();
					}
				}
			}
		}		
	},
	
	
	addActivityEvents: function(activity, bigGroup, width){
				
		var points = [ {
			x : 0,
			y : 0
		}, {
			x : width,
			y : 0
		}, {
			x : width,
			y : this.surfaceHeight
		}, {
			x : 0,
			y : this.surfaceHeight
		}, {
			x : 0,
			y : 0
		} ];

		var polygon = bigGroup.createPolyline(points).setFill(this.ActivityPaint.fill).moveToBack();
		var activityId = activity.getId();
		
		dojo.connect(polygon.rawNode, "onmouseover", function(){
			
		var instances = activity.getInstancedActivities();
		for (var j=0; j < instances.length; j++)
		{
			var polygonInstAct = ActivityPainter.positions[activityId].instAct[instances[j].getId()].polygon;
			polygonInstAct.setStroke(ActivityPainter.InstancedActivityPaint.stroke);
		}
			
		polygon.rawNode.setAttribute("style", "cursor:default;");
		if (StateManager.isStateAddGroup()){
			var canAdd = false;
			for (var i=0; i < StateManager.data.groups.length; i++)
			{
				if (activity.containsGroup(StateManager.data.groups[i])==false)
				{
					canAdd = true;
					break;
				}
			}
			if (canAdd)
			{
				polygon.rawNode.setAttribute("style", "cursor:url(images/cursors/group.cur),auto;");
			}
		}
		if (StateManager.isStateAddResource()){
				var instances = activity.getInstancedActivities();
				//Sólo se puede añadir un recurso si la actividad tiene instancias
				if (instances.length>0)
				{
					var canAdd =false;
					var resources = StateManager.data.resources;
					for (var i = 0; i < resources.length; i++)
					{
						for (var j=0; j < instances.length; j++)
						{
							//Si el recurso no se usa en alguna instancia de la actividad es posible a�adirle a dicha instancia
							if (instances[j].usesResource(resources[i])==false)
							{
								canAdd = true;
								var polygonInstAct = ActivityPainter.positions[activityId].instAct[instances[j].getId()].polygon;
								polygonInstAct.setStroke(ActivityPainter.InstancedActivityPaint.emphasisStroke);
							}
						}
					}
					if (canAdd)
					{
						polygon.rawNode.setAttribute("style", "cursor:url(images/cursors/document.cur),auto;");
					}
				}
		}
		
		if (StateManager.isStateAddTool())
		{
				var instances = activity.getInstancedActivities();
				if (instances.length > 0)
				{
					for (var j=0; j < instances.length; j++)
					{
						var polygonInstAct = ActivityPainter.positions[activityId].instAct[instances[j].getId()].polygon;
						polygonInstAct.setStroke(ActivityPainter.InstancedActivityPaint.emphasisStroke);
					}
			    	//Sólo se puede añadir una herramienta si la actividad tiene instancias
			    	polygon.rawNode.setAttribute("style", "cursor:url(images/cursors/tool.cur),auto;");
				}
		}
		
		if (StateManager.isStateReuseToolInstance())
		{
			var canReuse = false;
			var instancedActivities = activity.getInstancedActivities();
			for (var ia=0; ia < instancedActivities.length; ia++)
			{
				if (ToolInstanceReuse.canReuseToolInstance(instancedActivities[ia], StateManager.data.toolInstance)==true)
				{
					canReuse = true; 
					var polygonInstAct = ActivityPainter.positions[activityId].instAct[instancedActivities[ia].getId()].polygon;
					polygonInstAct.setStroke(ActivityPainter.InstancedActivityPaint.emphasisStrokeReuse);
				}
				
			}
			if (canReuse ==true){
				polygon.rawNode.setAttribute("style", "cursor:url(images/cursors/reset.cur),auto;");
			}
		}
		
		});
		
		dojo.connect(polygon.rawNode, "onmouseout", function(){
			
			var instances = activity.getInstancedActivities();
			for (var j=0; j < instances.length; j++)
			{
				if (ActivityPainter.positions[activityId].instAct[instances[j].getId()])
				{
					var polygonInstAct = ActivityPainter.positions[activityId].instAct[instances[j].getId()].polygon;
					polygonInstAct.setStroke(ActivityPainter.InstancedActivityPaint.stroke);
				}
			}
		});
		
		dojo.connect(polygon.rawNode, "onclick", function(){
			if (StateManager.isStateAddGroup())
			{
				GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
				for (var i=0; i < StateManager.data.groups.length; i++){
					//Comprobamos previamente que no está ya asignado ese grupo a la actividad
					if (activity.containsGroup(StateManager.data.groups[i])==false)
					{
						//Añadir el grupo a la actividad
						var instancedActivity = InstancedActivityContainer.addInstancedActivity(activity, StateManager.data.groups[i]);
						//Añadir los recursos de tipo documento de la actividad al grupo
						var resources = activity.getResources();
						for (var j=0; j < resources.length; j++)
						{
							instancedActivity.addResource(resources[j]);						
						}
						//Añadir los recursos de tipo herramienta de la actividad al grupo
						var tools = activity.getTools();
						for (var j=0; j < tools.length; j++)
						{
							instancedActivity.addToolInstance(tools[j]);
						}
					}
				}
				JsonDB.notifyChanges();
			}
			
			if (StateManager.isStateAddResource())
			{
				//Variable indicativa de si hay cambios
				var changes = false; 
				var instanced = activity.getInstancedActivities();
				GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
				for (var i=0; i < instanced.length; i++)
				{					
					for (var j=0; j < StateManager.data.resources.length; j++)
					{
						if (instanced[i].usesResource(StateManager.data.resources[j]) == false) {
							instanced[i].addResource(StateManager.data.resources[j]);						
							if (activity.availableResource(StateManager.data.resources[j])==false)
							{
								activity.addResource(StateManager.data.resources[j]);
							}
							changes = true;
						}
					}
				}
				if (changes)
				{
					JsonDB.notifyChanges();
				}
			}
			
			if (StateManager.isStateAddTool())
			{
				var instanced = activity.getInstancedActivities();
				GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
				//Si no hay instancias de la actividad no hay nada que hacer
				if (instanced.length > 0)
				{
					for (var j=0; j < StateManager.data.tools.length; j++)
					{
						for (var i=0; i < instanced.length; i++)
						{
							instanced[i].addToolInstance(StateManager.data.tools[j]);	
						}
						if (activity.availableTool(StateManager.data.tools[j])==false)
						{
							activity.addTool(StateManager.data.tools[j]);
						}
					}
					JsonDB.notifyChanges();
				}
			}
			
			
			if (StateManager.isStateReuseToolInstance())
			{
				var instancedActivities = activity.getInstancedActivities();
				for (var ia=0; ia < instancedActivities.length; ia++)
				{
					var canReuse = ToolInstanceReuse.canReuseToolInstance(instancedActivities[ia], StateManager.data.toolInstance);
					if (canReuse){
						var tool = StateManager.data.toolInstance.getTool();
						GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
						var created = instancedActivities[ia].addToolInstance(tool);
				
						if (activity.availableTool(tool)==false)
						{
							activity.addTool(tool);
						}
						//La instancia de herramienta creada tendrá como location el id de la instancia de herramienta que reutiliza
						created.setLocation(StateManager.data.toolInstance.getId());
						//Juan: Añado el position de la tool instance original
						created.setPosition(StateManager.data.toolInstance.getPosition());
						created.setMaxdistance(StateManager.data.toolInstance.getMaxdistance());
						created.setPositionType(StateManager.data.toolInstance.getPositionType());
						created.setScale(StateManager.data.toolInstance.getScale());
			//			created.setOrientation(StateManager.data.toolInstance.getOrientation());
						//Juan: Hasta aquí modificación
					}
				}
				JsonDB.notifyChanges();
			}
			
			
		});
	}

}
