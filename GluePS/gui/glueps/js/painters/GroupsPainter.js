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
 *  Funcionalidad necesaria para pintar la información de los grupos
 */

var GroupsPainter = {

	/**
	 * Guarda la relación existente entre los id de los grupos y los ID que se muestran en la interfaz
	 */
	identifiers : {}, 
	
	/**
	 * Guarda el estado actual de los grupos (en cuales se muestra participantes y en cuales no)
	 */
	currentState: {},

	init : function() {	
	},

	/**
	 * Pinta la información de los grupos
	 */
	paint : function() {
		GroupsPainter.buildIdentifiers();
		dojo.byId("participantsTitle").innerHTML=i18n.get("participantsTitle");
		dojo.byId("groupTitle").innerHTML=i18n.get("groupTitle");
		var divParticipants = dojo.byId("divParticipants");
		while (divParticipants.childNodes.length > 0)
		{
			divParticipants.removeChild(divParticipants.childNodes[0]);
		}
		var divGroups = dojo.byId("divGroups");
		while (divGroups.childNodes.length > 0)
		{
			divGroups.removeChild(divGroups.childNodes[0]);
		}
		var table = document.createElement("table");
		table.setAttribute("id", "allParticipantsTable");
		table.setAttribute("class", "tableInfo");
		divParticipants.appendChild(table);
		

		var tr = document.createElement("tr");
		table.appendChild(tr);

		GroupsPainter.appendAllParticipants(table);
		
		GroupsPainter.addUpdateImage(tr);
		// Añadir icono y eventos de mostrar los participantes disponibles
		GroupsPainter.addShowAllParticipants(tr);
		GroupsPainter.setCurrentVisibilityStateAll();
		
		var td = document.createElement("td");
		td.setAttribute("class", "item");
		td.innerHTML = i18n.get("allParticipants");
		tr.appendChild(td);
		
		var table = document.createElement("table");
		table.setAttribute("id", "groupTable");
		table.setAttribute("class", "tableInfo");
		divGroups.appendChild(table);
		
		var tr = document.createElement("tr");
		table.appendChild(tr);
		var th = document.createElement("th");
		th.innerHTML = "";
		th.setAttribute("class", "wb");
		tr.appendChild(th);
		
		var th = document.createElement("th");
		th.innerHTML = "";
		th.setAttribute("class", "wb");
		tr.appendChild(th);

		var th = document.createElement("th");
		th.innerHTML = i18n.get("groupTableName");
		tr.appendChild(th);
		
		var groups = GroupContainer.getAllGroups();
		for ( var i = 0; i < groups.length; i++) {
			var tr = document.createElement("tr");
			tr.setAttribute("id", "tr"
					+ GroupsPainter.identifiers[groups[i].getId()]);
			table.appendChild(tr);
			GroupsPainter.addGroupEvents(tr, groups[i]);
			//Imagen y funcionalidad del grupo
			var td = document.createElement("td");
			tr.appendChild(td);
			GroupsPainter.addGroupImage(td, groups[i]);
			GroupsPainter.addGroupToolTips(td, groups[i]);
			GroupsPainter.selectGroupEvents(td, groups[i]);
			
			var participants = groups[i].getParticipants();
			GroupsPainter.appendGroupParticipants(table, groups[i]);
			//Se establece el estado de visibilidad de los participantes del grupo
			GroupsPainter.setCurrentVisibilityState(groups[i]);
			
			if (participants.length > 0) {
				// Añadir icono y eventos de mostrar los participantes del grupo
				GroupsPainter.addShowParticipants(tr, groups[i]);
			}
			else{
				var td = document.createElement("td");
				td.setAttribute("class", "wb");
				tr.appendChild(td);
				GroupsPainter.addGroupToolTips(td, groups[i]);
				GroupsPainter.selectGroupEvents(td, groups[i]);
			}
			
			var td = document.createElement("td");
			td.setAttribute("id", "tdName" + GroupsPainter.identifiers[groups[i].getId()]);
			td.innerHTML = groups[i].getName();
			tr.appendChild(td);
			GroupsPainter.addGroupToolTips(td, groups[i]);
			GroupsPainter.selectGroupEvents(td, groups[i]);
			//Check if the group name is repeated. If so, show an exclamation icon
			var repeatedGroup = false;
			for (var j = 0; j < groups.length; j++){
				if ( i!=j && groups[i].getName().toLowerCase()==groups[j].getName().toLowerCase()){
					repeatedGroup = true;
					break;
				}
			}
			if (repeatedGroup){
				//GroupsPainter.addFixGroupMenu(tr, groups[i]);
				GroupsPainter.addRepeatedGroupExclamation(tr, groups[i]);
			}else{
				var td = document.createElement("td");
				td.setAttribute("class", "wb");
				tr.appendChild(td);
			}
			
			
		}
		GroupsPainter.addGroupIcon(divGroups);
	},
	
	/**
	 * Añade icono de mostrar todos los participantes y le asocia eventos
	 * @param tr Fila de la tabla a la que se añade el icono
	 */
	addShowAllParticipants : function(tr) {
		var td = document.createElement("td");
		var showGroupIcon = document.createElement("img");
		var id = "showGroup" + "AllParticipants";
		showGroupIcon.setAttribute("id", id);
		td.setAttribute("class", "wb");
		showGroupIcon.setAttribute("src", "images/icons/down.gif");
		showGroupIcon.setAttribute("width", "20");
		showGroupIcon.setAttribute("height", "20");
		td.appendChild(showGroupIcon);
		tr.appendChild(td);

		// Asigno funcionalidad
		dojo.connect(showGroupIcon, "onclick", function() {
			if (GroupsPainter.areVisibleAllParticipants() == false) {
				GroupsPainter.showAllParticipants();
				showGroupIcon.setAttribute("src", "images/icons/up.gif");
			} else {
				GroupsPainter.hideAllParticipants();
				showGroupIcon.setAttribute("src", "images/icons/down.gif");
			}
		});
		dojo.connect(showGroupIcon, "onmouseover", function() {
			var src = showGroupIcon.getAttribute("src");
			var newSrc = src.split(".gif")[0] + "_over.gif";
			showGroupIcon.setAttribute("src", newSrc);
		});
		dojo.connect(showGroupIcon, "onmouseout", function() {
			var src = showGroupIcon.getAttribute("src");
			var newSrc = src.split("_over.gif")[0] + ".gif";
			showGroupIcon.setAttribute("src", newSrc);
		});
	},

	/**
	 * Añade icono de mostrar los participantes de un grupo
	 * @param tr Fila de la tabla a la que se añade el icono
	 * @param group Grupo del que se mostrarán los participantes
	 */
	addShowParticipants : function(tr, group) {
		var td = document.createElement("td");
		var showGroupIcon = document.createElement("img");
		var id = "showGroup" + group.getId();
		showGroupIcon.setAttribute("id", id);
		td.setAttribute("class", "wb");
		showGroupIcon.setAttribute("src", "images/icons/down.gif");
		showGroupIcon.setAttribute("width", "20");
		showGroupIcon.setAttribute("height", "20");
		td.appendChild(showGroupIcon);
		tr.appendChild(td);

		// Asigno funcionalidad
		dojo.connect(showGroupIcon, "onclick", function() {
			if (GroupsPainter.areVisibleGroupParticipants(group) == false) {
				GroupsPainter.showGroupParticipants(group);
				showGroupIcon.setAttribute("src", "images/icons/up.gif");
			} else {
				GroupsPainter.hideGroupParticipants(group);
				showGroupIcon.setAttribute("src", "images/icons/down.gif");
			}
		});
		dojo.connect(showGroupIcon, "onmouseover", function() {
			var src = showGroupIcon.getAttribute("src");
			var newSrc = src.split(".gif")[0] + "_over.gif";
			showGroupIcon.setAttribute("src", newSrc);
		});
		dojo.connect(showGroupIcon, "onmouseout", function() {
			var src = showGroupIcon.getAttribute("src");
			var newSrc = src.split("_over.gif")[0] + ".gif";
			showGroupIcon.setAttribute("src", newSrc);
		});
	},
	
	/**
	 * Add an repair icon to indicate that there is another group with the same name and show
	 * the available options to solve the problem
	 * @param tr row of the table where the exclamation will be added
	 * @param group the group the repair functionality refers to
	 */
	addFixGroupMenu : function(tr, group) {
		var td = document.createElement("td");
		var repairIcon = document.createElement("img");
		td.setAttribute("class", "wb");
		td.setAttribute("id", "fixRepGroup_" + group.getId());
		repairIcon.setAttribute("src", "images/icons/repair.png");
		repairIcon.setAttribute("width", "20");
		repairIcon.setAttribute("height", "20");
		td.appendChild(repairIcon);
		tr.appendChild(td);
		
		tooltip = new dijit.Tooltip({
			connectId: "fixRepGroup_" + group.getId(),
			label: i18n.get("fixRepeatedGroup"),
			showDelay: 500
		});
		
		MenuManager.registerThing(repairIcon, {
			getItems : function(data) {
				return RepeatedGroupMenu.getRepeatedGroupMenu(data);
			},
			data : {
				group: group
			},
			onClick : function(data) {
			},
			menuStyle : "default",
			title : i18n.get("repeatedGroupMenu")
		});
	},
	
	/**
	 * Add an exclamation icon to indicate that there is a problem with the group
	 * @param tr row of the table where the exclamation will be added
	 * @param group the group the warning refers to
	 */
	addRepeatedGroupExclamation : function(tr, group) {
		var td = document.createElement("td");
		var exclamationIcon = document.createElement("img");
		td.setAttribute("class", "wb");
		td.setAttribute("id", "repGroupExclamation_" + group.getId());
		exclamationIcon.setAttribute("src", "images/icons/exclamation.png");
		exclamationIcon.setAttribute("width", "20");
		exclamationIcon.setAttribute("height", "20");
		td.appendChild(exclamationIcon);
		tr.appendChild(td);
		
		var exclamationInfo= i18n.get("addParticipantHelp");
		tooltip = new dijit.Tooltip({
			connectId: "repGroupExclamation_" + group.getId(),
			//label: i18n.get("repeatedGroup"),
			label: i18n.get("fixRepeatedGroup"),
			showDelay: 500
		});
		
		MenuManager.registerThing(exclamationIcon, {
			getItems : function(data) {
				return RepeatedGroupMenu.getRepeatedGroupMenu(data);
			},
			data : {
				group: group
			},
			onClick : function(data) {
			},
			menuStyle : "default",
			title : i18n.get("repeatedGroupMenu")
		});
	},
	
	/**
	 * A�ade imagen de actualizar y le asocia los eventos
	 * @param tr Fila de la tabla a la que se a�ade la imagen
	 */
	addUpdateImage: function(tr)
	{
		var td = document.createElement("td");
		var updateIcon = document.createElement("img");
		td.setAttribute("class", "wb");
		var src = "images/icons/update.png";
		updateIcon.setAttribute("src", src);
		updateIcon.setAttribute("width", "25");
		updateIcon.setAttribute("height", "25");
		updateIcon.style.cursor="pointer"; 
		updateIcon.id = "updateParticipantListIcon";
		td.appendChild(updateIcon);
		tr.appendChild(td);		
		
		var updateParticipantListInfo= i18n.get("addParticipantHelp");
		tooltip = new dijit.Tooltip({
			connectId: "updateParticipantListIcon",
			label: i18n.get("updateParticipantList"),
			showDelay: 500
		});
				
        dojo.connect(updateIcon, "onclick", function(){
        	ParticipantManager.getCourseParticipants();       	
        });	
	},
	
	/**
	 * Añade imagen de grupo y le asocia los eventos
	 * @param td Columnna de la tabla a la que se añade la imagen
	 * @param group Grupo al que se asocia la funcionalidad
	 */
	addGroupImage: function(td, group)
	{
		var groupIcon = document.createElement("img");
		var id = "group" + group.getId();
		groupIcon.setAttribute("id", id);
		td.setAttribute("class", "wb");
		var src = GroupsPainter.getGroupIconSrc(group);
		groupIcon.setAttribute("src", src);
		groupIcon.setAttribute("width", "20");
		groupIcon.setAttribute("height", "20");
		td.appendChild(groupIcon);
		
		MenuManager.registerThing(groupIcon, {
			getItems : function(data) {
				return GroupMenu.getGroupMenu(data);
			},
			data : {
				group: group
			},
			onClick : function(data) {
			},
			menuStyle : "default"
		});
	},
	
	/**
	 * Crea una nueva fila en la tabla que contendrá todos los participantes disponibles
	 * @param table Tabla en la que se añade la fila con los participantes
	 */
	appendAllParticipants : function(table) {
		var participants = ParticipantContainer.getAllParticipants();
		var tr = document.createElement("tr");
		tr.setAttribute("class", "participants");
		// Asignar identificador para la fila
		tr.setAttribute("id", "trp" + "G0");
		table.appendChild(tr);
		
		var td = document.createElement("td");
		tr.appendChild(td);
		var td = document.createElement("td");
		tr.appendChild(td);
		var td = document.createElement("td");
		//td.setAttribute("class", "wb");
		tr.appendChild(td);
		
		var div = document.createElement("div");
		div.setAttribute("style", "padding:0px;max-height:200px;overflow:auto;");
		td.appendChild(div);
		var tablaAnidada = document.createElement("table");
		tablaAnidada.setAttribute("class", "tableAllParticipants");
		tablaAnidada.setAttribute("style", "border-spacing:4px;");
		div.appendChild(tablaAnidada);
		for ( var i = 0; i < participants.length; i++) {
			if (i%2==0){
				var tr = document.createElement("tr");
				tablaAnidada.appendChild(tr);
			}
			
			var td = document.createElement("td");
			var participantIcon = document.createElement("img");

			//td.setAttribute("class", "wb");
			var src = GroupsPainter.getParticipantIconSrc(participants[i]);
			participantIcon.setAttribute("src", src);
			participantIcon.setAttribute("width", "12");
			participantIcon.setAttribute("height", "20");
			td.setAttribute("style", "padding-right:0px;");
			td.appendChild(participantIcon);
			tr.appendChild(td);
			
			this.addParticipant(tr, participants[i]);
			
			this.addDeleteParticipant(tr, participants[i]);

		}
	},
	
	addParticipant: function(tr, participant)
	{
		var td = document.createElement("td");
		td.innerHTML = participant.getName();
		td.setAttribute("id", "participant" + participant.getId());
		td.setAttribute("class", "participant");
		tr.appendChild(td);
       
		// Añadir tooltip
		var addParticipantInfo= i18n.get("addParticipantHelp");
		tooltip = new dijit.Tooltip({
			connectId: ["participant" + participant.getId()],
			label: addParticipantInfo,
			showDelay: 500
		});
		
		//Le asociamos su tooltip
		td.tooltip = tooltip;		
		
		dojo.connect(td, "onmouseover", function() {
			if (StateManager.isSelectedParticipant(participant)==false)
			{
				td.tooltip.setAttribute("label",i18n.get("addParticipantHelp"));
			}
		else{
				td.tooltip.setAttribute("label",i18n.get("unselectParticipantHelp"));
			}
		});
		
		dojo.connect(td, "onclick", function() {
			StateManager.changeParticipantState(participant);
			if (StateManager.isSelectedParticipant(participant)==false){
				td.setAttribute("class", "participant");
			}
			else{
				td.setAttribute("class", "participantSelected");
			}
		});
	},
	
	/**
	 * Añade un participante de un grupo y le asocia los eventos
	 * @param tr
	 * @param participant
	 */
	addGroupParticipant: function(tr, group, participant)
	{
		var td = document.createElement("td");
		td.innerHTML = participant.getName();
		td.setAttribute("id", "group" + group.getId() + "_participant" + participant.getId());
		td.setAttribute("class", "participant");
		tr.appendChild(td);
       
		// Añadir tooltip
		var addParticipantInfo= i18n.get("copyParticipantHelp");
		tooltip = new dijit.Tooltip({
			connectId: ["group" + group.getId() + "_participant" + participant.getId()],
			label: addParticipantInfo,
			showDelay: 500
		});
		
		//Le asociamos su tooltip
		td.tooltip = tooltip;		
		
		dojo.connect(td, "onmouseover", function() {
			if (StateManager.isSelectedParticipantCopy(participant)==false)
			{
				td.tooltip.setAttribute("label",i18n.get("copyParticipantHelp"));
			}
		else{
				td.tooltip.setAttribute("label",i18n.get("unselectParticipantCopyHelp"));
			}
		});
		
		dojo.connect(td, "onclick", function() {
			StateManager.changeParticipantStateCopy(participant, group);
			if (StateManager.isSelectedParticipantCopy(participant)==false){
				td.setAttribute("class", "participant");			
			}
			else{
				td.setAttribute("class", "participantSelected");
			}
		});
	},

	/**
	 * Crea una nueva fila en la tabla que contendrá los participantes de un grupo
	 * @param table Tabla en la que se añade la fila con los participantes
	 * @param group Grupo del que incluyen los participantes
	 */
	appendGroupParticipants : function(table, group) {
		var participants = group.getParticipants();
		var tr = document.createElement("tr");
		tr.setAttribute("class", "participants");
		// Asignar identificador para la fila
		tr.setAttribute("id", "trp" + GroupsPainter.identifiers[group.getId()]);
		table.appendChild(tr);
		var td = document.createElement("td");
		td.setAttribute("class", "wb");
		tr.appendChild(td);
		
		var td = document.createElement("td");
		td.setAttribute("class", "wb");
		tr.appendChild(td);
				
		tr.appendChild(td);
		var td = document.createElement("td");
		td.setAttribute("class", "wb");
		td.setAttribute("colspan", "1");
		tr.appendChild(td);
		var div = document.createElement("div");
		div.setAttribute("style", "padding:0px;max-height:200px;overflow:auto;");
		td.appendChild(div);
		var tablaAnidada = document.createElement("table");
		tablaAnidada.setAttribute("class", "tableAllParticipants");
		tablaAnidada.setAttribute("style", "border-spacing:4px;");
		div.appendChild(tablaAnidada);
		for ( var i = 0; i < participants.length; i++) {
			if (i%2==0){
				var tr = document.createElement("tr");
				tablaAnidada.appendChild(tr);
			}
			
			this.addGroupParticipant(tr, group, participants[i]);
			
			GroupsPainter.addDeleteParticipantGroup(tr, group, participants[i]);
		}
	},
	
	/**
	 * Añade a una fila de la tabla el icono y los eventos asociados a borrar un participante
	 * @param tr Fila de la tabla
	 * @param group Grupo del participante
	 * @param participant Participante al que se asocian los eventos
	 */
	addDeleteParticipantGroup : function(tr, group, participant) {
		var td = document.createElement("td");
		var showDeleteIcon = document.createElement("img");
		var id = "deleteParticipant" + participant.getId();
		showDeleteIcon.setAttribute("id", id);
		showDeleteIcon.setAttribute("class", "deleteImg");
		td.setAttribute("class", "wb");
		td.setAttribute("style", "padding-left:0px;");
		showDeleteIcon.setAttribute("src", "images/icons/delete.png");
		showDeleteIcon.setAttribute("title", i18n.get("deleteParticipant"));
		td.appendChild(showDeleteIcon);
		tr.appendChild(td);

		// Asigno funcionalidad
		dojo.connect(showDeleteIcon, "onclick", function() {
			//Guardar el estado actual previo al borrado
			GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
			//Borrar participante
			group.deleteParticipant(participant);
			//Notificar cambios
			JsonDB.notifyChanges();
		});
		
		dojo.connect(showDeleteIcon, "onmouseover", function() {
			showDeleteIcon.setAttribute("src", "images/icons/deleteover.png");
		});
		dojo.connect(showDeleteIcon, "onmouseout", function() {
			showDeleteIcon.setAttribute("src", "images/icons/delete.png");
		});
	},
	
	
	/**
	 * Añade a una fila de la tabla el icono y los eventos asociados a borrar un participante de los disponibles
	 * @param tr Fila de la tabla
	 * @param participant Participante al que se asocian los eventos
	 */
	addDeleteParticipant : function(tr, participant) {
		var td = document.createElement("td");
		var showDeleteIcon = document.createElement("img");
		//var id = "deleteParticipant" + participant.getId();
		//showDeleteIcon.setAttribute("id", id);
		showDeleteIcon.setAttribute("class", "deleteImg");
		td.setAttribute("class", "wb");
		td.setAttribute("style", "padding-left:0px;");
		showDeleteIcon.setAttribute("src", "images/icons/delete.png");
		showDeleteIcon.setAttribute("title", i18n.get("deleteParticipant"));
		td.appendChild(showDeleteIcon);
		tr.appendChild(td);

		// Asigno funcionalidad
		dojo.connect(showDeleteIcon, "onclick", function() {
			DeleteParticipantDialog.showDeleteParticipant(participant);
		});
		
		dojo.connect(showDeleteIcon, "onmouseover", function() {
			showDeleteIcon.setAttribute("src", "images/icons/deleteover.png");
		});
		dojo.connect(showDeleteIcon, "onmouseout", function() {
			showDeleteIcon.setAttribute("src", "images/icons/delete.png");
		});
	},
	
	/**
	 * Asocia a la fila de la tabla eventos relacionados con el grupo
	 * @param tr Fila de la tabla
	 * @param group Grupo al que se asocian los eventos
	 */
	addGroupEvents: function(tr, group) {		
		//Le asociamos los eventos
		dojo.connect(tr, "onmouseover", function(){
			for (var actId in ActivityPainter.positions)
			{
				for (var instActId in ActivityPainter.positions[actId].instAct)
					{
						var groupInfo = ActivityPainter.positions[actId].instAct[instActId].group;
						if (groupInfo && group.getId()== groupInfo.id)
						{
							var src = GroupsPainter.getGroupIconSrc(group);
							var img = groupInfo.img;
							img.xoriginal = img.getShape().x;
							img.setShape({
							width : ActivityPainter.groupInfo.width + 10,  //Aumentamos el ancho
							height : ActivityPainter.groupInfo.height + 10, //Aumentamos el alto
							x : img.getShape().x - 10/2,
							y : img.getShape().y,
							src : src
							});
							
							var text = groupInfo.text;
							text.setFont(ActivityPainter.textHighlighted.font).setFill(ActivityPainter.textHighlighted.fill);
						}
					}
			}
		});
		dojo.connect(tr, "onmouseout", function(){
			for (var actId in ActivityPainter.positions)
			{
				for (var instActId in ActivityPainter.positions[actId].instAct)
					{
						var groupInfo = ActivityPainter.positions[actId].instAct[instActId].group;
						if (groupInfo && group.getId()== groupInfo.id)
						{
							var src = GroupsPainter.getGroupIconSrc(group);
							var img = groupInfo.img;
							img.setShape({
								width : ActivityPainter.groupInfo.width,
								height : ActivityPainter.groupInfo.height,
								x : img.xoriginal,
								y : img.getShape().y,
								src : src
							});
							
							var text = groupInfo.text;
							text.setFont(ActivityPainter.text.font).setFill(ActivityPainter.text.fill);
						}
					}
			}
		});
	},
	
	/**
	 * Añade un grupo a una fila de la tabla y su funcionalidad asociada
	 * @param tr Fila de la tabla en la que se añade una columna para el grupo
	 * @param group Grupo que se añade a la fila de la tabla
	 */
	addGroupToolTips: function(td, group) {			
		// Añadir tooltip
		var addParticipantsGroupInfo= i18n.get("addParticipantHereHelp");
		var tooltip = new dijit.Tooltip({
			connectId:  "tr" + GroupsPainter.identifiers[group.getId()],
			label: addParticipantsGroupInfo,
			showDelay: 5000
		});
		//Le asociamos su tooltip
		td.tooltip = tooltip;
		
		dojo.connect(td, "onmouseover", function(){
			td.setAttribute("style", "cursor:default;");
			if (StateManager.isStateAddParticipant())
			{
				td.tooltip.setAttribute("label",i18n.get("addParticipantHereHelp"));
				td.tooltip.open();
				td.setAttribute("style", "cursor:url(images/cursors/student.cur),auto;");
			}
			else{
				if (StateManager.isStateCopyParticipant())
				{
					td.tooltip.setAttribute("label",i18n.get("copyParticipantHereHelp"));
					td.tooltip.open();
					td.setAttribute("style", "cursor:url(images/cursors/student.cur),auto;");
				}
				else {
					//En cualquier otro caso: info de selección o deselección de grupo
					if (!StateManager.isSelectedGroup(group))
					{
						td.tooltip.setAttribute("label",i18n.get("groupMenuActivityHelp"));
						td.setAttribute("style", "cursor:pointer;");
						td.tooltip.open();						
					}
					else{
						td.tooltip.setAttribute("label",i18n.get("groupMenuActivityHelpUnselect"));
						td.setAttribute("style", "cursor:pointer;");
						td.tooltip.open();		
					}
					/*else{
						td.tooltip.close();
						td.style.cursor="default";
					}*/
				}
			}
		});
	},
		
	selectGroupEvents: function(td, group) {	
		dojo.connect(td, "onclick", function() {
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
			else if (StateManager.isStateCopyParticipant())
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
			else{
				if (!StateManager.isSelectedGroup(group))
				{
					//Selección de grupo
					StateManager.changeGroupState(group);
					GroupsPainter.selectedGroup(group);
				}
				else{
					//Deselección de grupo
					StateManager.changeGroupState(group);
					GroupsPainter.unselectedGroup(group);
				}
			}
		});
	},

	/**
	 * Indica si se están mostrando los participantes del grupo
	 * @param grupo Grupo del que se desea saber si se muestran sus participantes
	 * return boolean Los participantes se muestran o no
	 */
	areVisibleGroupParticipants : function(group) {
		var nodo = dojo.byId("trp" + GroupsPainter.identifiers[group.getId()]);
		return nodo.style.display == "";
	},

	/**
	 * Muestra los participantes del grupo
	 * @param grupo Grupo del que se muestran sus participantes
	 */
	showGroupParticipants : function(group) {
		var nodo = dojo.byId("trp" + GroupsPainter.identifiers[group.getId()]);
		nodo.style.display = "";
	},

	/**
	 * Oculta los participantes del grupo
	 * @param grupo Grupo del que se ocultan sus participantes
	 */
	hideGroupParticipants : function(group) {
		var nodo = dojo.byId("trp" + GroupsPainter.identifiers[group.getId()]);
		nodo.style.display = "none";
	},
	
	/**
	 * Indica si se están mostrando los participantes disponibles
	 * return boolean Los participantes se muestran o no
	 */
	areVisibleAllParticipants : function() {
		var nodo = dojo.byId("trp" + "G0");
		return nodo.style.display == "";
	},
	
	/**
	 * Muestra los participantes disponibles
	 */
	showAllParticipants : function() {
		var nodo = dojo.byId("trp" + "G0");
		nodo.style.display = "";
	},

	/**
	 * Oculta los participantes disponibles
	 */
	hideAllParticipants : function() {
		var nodo = dojo.byId("trp" + "G0");
		nodo.style.display = "none";
	},

	/** 
	 * Construye los identificadores que relacionan los ids de los grupos con los de la interfaz
	 */
	buildIdentifiers : function() {
		var groups = GroupContainer.getAllGroups();
		for ( var i = 0; i < groups.length; i++) {
			GroupsPainter.identifiers[groups[i].getId()] = "G" + (i + 1);
		}
	},

	getAllParticipantsIconSrc : function() {
		var src;
		if (ParticipantContainer.getAllParticipants().length > 0) {
			src = "images/icons/students.png";
		} else {
			src = "images/icons/studentsEmpty.png";
		}
		return src;
	},

	getParticipantIconSrc : function(participant) {
		var src;
		if (GroupsPainter.isAssignedAnyGroup(participant)) {
			src = "images/icons/student.png";
		} else {
			src = "images/icons/studentEmpty2.png";
		}
		return src;
	},
	
	getGroupIconSrc : function(group) {
		var src;
		if (group.getParticipants().length > 0) {
			src = "images/icons/students.png";
		} else {
			src = "images/icons/studentsEmpty.png";
		}
		return src;
	},
	
	/** Establece el estado actual de visibilidad de los participantes de un grupo al estado de visibilidad que tenía previamente
	 *  En caso contrario, no se muestran los participantes del grupo
	 * @param group
	 */
	setCurrentVisibilityState: function(group){
		if (GroupsPainter.currentState[group.getId()] && group.getParticipants().length > 0)
		{
			GroupsPainter.showGroupParticipants(group);
		}
		else{
			GroupsPainter.hideGroupParticipants(group);
		}
	},
	
	setCurrentVisibilityStateAll: function(){
		if (GroupsPainter.currentState["AllParticipants"])
		{
			GroupsPainter.showAllParticipants();
		}
		else{
			GroupsPainter.hideAllParticipants();
		}
	},
	
	updateCurrentStateInfo: function(){
		var info = new Object();
		var groups = GroupContainer.getAllGroups();
		for (var i=0; i< groups.length; i++){
			info[groups[i].getId()] = GroupsPainter.areVisibleGroupParticipants(groups[i]);
		}
		info["AllParticipants"] = GroupsPainter.areVisibleAllParticipants();
		return info;
	},
	
	isAssignedAnyGroup: function(participant){
		var groups = GroupContainer.getAllGroups();
		for (var i=0; i < groups.length; i++)
		{
			if (groups[i].containsParticipant(participant))
			{
				return true;
			}
		}
		return false;
	},
	
	addGroupIcon: function(divParent){
		var div = document.createElement("div");
		// Icono de añadir recursos
		var addResourceIcon = document.createElement("img");
		var id = "addResource";
		addResourceIcon.setAttribute("id", id);
		addResourceIcon.setAttribute("src", "images/icons/add.png");
		//addResourceIcon.setAttribute("title", "Añadir grupo");
		addResourceIcon.setAttribute("class", "addIcon");
		div.appendChild(addResourceIcon);
		var textNode = document.createElement("span");
		textNode.innerHTML = i18n.get("createGroup");
		textNode.setAttribute("class", "clickableTextNode");
		div.appendChild(textNode);
		divParent.appendChild(div);
		
		// Asigno funcionalidad
		dojo.connect(addResourceIcon, "onclick", function() {
			GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
			GroupDialog.showDialog();
		});
		
		dojo.connect(textNode, "onclick", function() {
			GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
			GroupDialog.showDialog();
		});
	},
	
	selectedGroup: function(group){
		var id = "tdName" + GroupsPainter.identifiers[group.getId()];
		dojo.byId(id).setAttribute("class", "groupSelected");
	},
	
	unselectedGroup: function(group){
		var id = "tdName" + GroupsPainter.identifiers[group.getId()];
		dojo.byId(id).setAttribute("class", "");
	}	
	
}