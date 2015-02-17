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
var StateManager = {
		
		init: function(){
			this.reset();
		},
		
		reset: function(){
			this.resetSelectedToolInstances();
			this.data = new Object();
			this.data.state = "normal";
			this.data.participants = new Array(); //Participantes (de todos) a añadir a un grupo
			this.data.participantsMove = new Array(); //Participantes a mover a otro grupo	
			this.data.participantsMoveGroup = null; //Grupo del que se mueven los participantes
			this.data.participantsCopy = new Array(); //Participantes a copiar a otro grupo
			this.data.participantsCopyGroup = null; //Grupo del que se copian los participantes
			this.data.groups = new Array(); //Grupo que se van a añadir a una actividad
			this.data.resources = new Array(); //Recursos que se van a añadir a una actividad o instancia de actividad
			this.data.tools = new Array(); //Herramienta que se va a añadir a una actividad o instancia de actividad
			
			this.data.toolInstance = null; //Instancia de herramienta que se va a reutilizar en otra actividad
			//this.data.instancedActivity = null;
			//this.data.activity = null;
			
			/** Todos los elementos previamente seleccionados vuelven a su estado original*/
			var participants = $('.participantSelected');
			for (var i=0; i<participants.length; i++)
			{
				participants[i].setAttribute("class", "participant");
			}
			var groups = $('.groupSelected');
			for (var i=0; i<groups.length; i++)
			{
				groups[i].setAttribute("class", "participant");
			}
			var resources = $('.resourceSelected');
			for (var i=0; i<resources.length; i++)
			{
				resources[i].setAttribute("class", "");
			}
			var tools = $('.toolSelected');
			for (var i=0; i<tools.length; i++)
			{
				tools[i].setAttribute("class", "");
			}
		},
		
		resetSelectedToolInstances: function(){
			if (this.data && this.data.toolInstance)
			{
				var toolInstances = ActivityPainter.positions[StateManager.data.activity.getId()].instAct[StateManager.data.instancedActivity.getId()].toolInstances;
				for (var i=0; i < toolInstances.length; i++)
				{
					if (toolInstances[i].id == StateManager.data.toolInstance.getId())
					{
						var text = toolInstances[i].text;
						text.setFill("black");
						break;
					}
				}
			}
		},
		
		/**
		 * Estado en el que se añaden participantes (de todos los participantes) a un grupo
		 */
		setStateAddParticipant: function(){
			this.reset();
			this.data.state="addParticipant";
		},		
		
		/**
		 * Estado en el que se mueven participantes de un grupo a otro grupo
		 */
		setStateMoveParticipant: function(){
			this.reset();
			this.data.state="moveParticipant";
		},	
		
		/**
		 * Estado en el que se copian participantes de un grupo a otro grupo
		 */
		setStateCopyParticipant: function(){
			this.reset();
			this.data.state="copyParticipant";
		},	
		
		/**
		 * Estado en el que se añade un grupo a una instancia de actividad
		 */
		setStateAddGroup: function(){
			this.reset();
			this.data.state="addGroup";
		},	
		
		/**
		 * Estado en el que se añade un recurso a un grupo
		 */
		setStateAddResource: function(){
			this.reset();
			this.data.state="addResource";
		},	
		
		/**
		 * Estado en el que se añade una herramienta a un grupo creándose la correspondiente instancia de herramienta
		 */
		setStateAddTool: function(){
			this.reset();
			this.data.state="addTool";
		},
		
		/**
		 * Estado en el que se reutiliza una instancia de herramienta en otra actividad
		 */
		setStateReuseToolInstance: function(){
			this.reset();
			this.data.state="reuseToolInstance";
		},
		
		isSelectedParticipant: function(participant)
		{
			for (var i=0; i<this.data.participants.length;i++)
			{
				if (this.data.participants[i].getId()==participant.getId()){
					return true;
				}
			}
			return false;			
		},
		
		isSelectedParticipantMove: function(participant)
		{
			for (var i=0; i<this.data.participantsMove.length;i++)
			{
				if (this.data.participantsMove[i].getId()==participant.getId()){
					return true;
				}
			}
			return false;			
		},
		
		isSelectedParticipantCopy: function(participant)
		{
			for (var i=0; i<this.data.participantsCopy.length;i++)
			{
				if (this.data.participantsCopy[i].getId()==participant.getId()){
					return true;
				}
			}
			return false;			
		},
		
		isSelectedTool: function(tool)
		{
			for (var i=0; i<this.data.tools.length;i++)
			{
				if (this.data.tools[i].getId()==tool.getId()){
					return true;
				}
			}
			return false;			
		},
		
		isSelectedResource: function(resource)
		{
			for (var i=0; i<this.data.resources.length;i++)
			{
				if (this.data.resources[i].getId()==resource.getId()){
					return true;
				}
			}
			return false;			
		},
		
		isSelectedGroup: function(group)
		{
			for (var i=0; i<this.data.groups.length;i++)
			{
				if (this.data.groups[i].getId()==group.getId()){
					return true;
				}
			}
			return false;			
		},
		
		/**
		 * Cambia el estado de selección de una herramienta
		 * @param tool
		 */
		changeToolState: function(tool)
		{
			if (this.isSelectedTool(tool)==false)
			{
				this.selectTool(tool);
			}
			else{
				this.unselectTool(tool);
			}
		},
		
		/**
		 * Cambia el estado de selección de un grupo
		 * @param group
		 */
		changeGroupState: function(group)
		{
			if (this.isSelectedGroup(group)==false)
			{
				this.selectGroup(group);
			}
			else{
				this.unselectGroup(group);
			}
		},
		
		/**
		 * Selección de una herramienta para ser asignada a una actividad
		 * @param tool Herramienta a asignar
		 */
		selectTool: function(tool)
		{
			//Si es necesario, se activa el modo selección de herramientas
			if (this.isStateAddTool()==false)
			{
				this.setStateAddTool();
			}
			this.data.tools.push(tool);			
		},
		
		unselectTool: function(tool)
		{
			for (var i=0; i<this.data.tools.length;i++)
			{
				if (this.data.tools[i].getId()==tool.getId()){
					this.data.tools.splice(i,1);
					//Si no queda ninguno se desactiva el modo selección de herramientas
					if (this.data.tools.length==0)
					{
						StateManager.reset();
					}
					return true;
				}
			}
			return false;			
		},
		
		unselectGroup: function(group)
		{
			for (var i=0; i<this.data.groups.length;i++)
			{
				if (this.data.groups[i].getId()==group.getId()){
					this.data.groups.splice(i,1);
					//Si no queda ninguno se desactiva el modo selección de grupos
					if (this.data.groups.length==0)
					{
						StateManager.reset();
					}
					return true;
				}
			}
			return false;			
		},
		
		/**
		 * Cambia el estado de selección de un documento
		 * @param tool
		 */
		changeResourceState: function(resource)
		{
			if (this.isSelectedResource(resource)==false)
			{
				this.selectResource(resource);
			}
			else{
				this.unselectResource(resource);
			}
		},
		
		/**
		 * Selección de un documento para ser asignado a una actividad
		 * @param resource Documento a asignar
		 */
		selectResource: function(resource)
		{
			//Si es necesario, se activa el modo selección de documentos
			if (this.isStateAddResource()==false)
			{
				this.setStateAddResource();
			}
			this.data.resources.push(resource);	
		},
		
		unselectResource: function(resource)
		{
			for (var i=0; i<this.data.resources.length;i++)
			{
				if (this.data.resources[i].getId()==resource.getId()){
					this.data.resources.splice(i,1);
					//Si no queda ninguno se desactiva el modo selección de recursos
					if (this.data.resources.length==0)
					{
						StateManager.reset();
					}
					return true;
				}
			}
			return false;			
		},
		
		/**
		 * Cambia el estado de selección de un participante
		 * @param participant
		 */
		changeParticipantState: function(participant)
		{
			if (this.isSelectedParticipant(participant)==false)
			{
				this.selectParticipant(participant);
			}
			else{
				this.unselectParticipant(participant);
			}
		},
		
		/**
		 * Selección de un participante para ser asignado a un grupo
		 * @param participant Participante a asignar
		 */
		selectParticipant: function(participant)
		{
			//Si es necesario, se activa el modo selección de participantes
			if (this.isStateAddParticipant()==false)
			{
				this.setStateAddParticipant();
			}
			this.data.participants.push(participant);			
		},
		
		unselectParticipant: function(participant)
		{
			for (var i=0; i<this.data.participants.length;i++)
			{
				if (this.data.participants[i].getId()==participant.getId()){
					this.data.participants.splice(i,1);
					//Si no queda ninguno se desactiva el modo selección de participantes
					if (this.data.participants.length==0)
					{
						StateManager.reset();
					}
					return true;
				}
			}
			return false;
			
		},
		
		/**
		 * Selección de un participante para ser movido a un grupo
		 * @param participant Participante a mover
		 * @param group Grupo desde el que se desea mover el participante
		 */
		selectParticipantMove: function(participant, group)
		{
			//Si es necesario, se activa el modo selección de participantes del grupo a mover
			//Si cambia el grupo desde el que se mueven los participantes se activa de nuevo
			if (this.isStateMoveParticipant()==false || ( this.isStateMoveParticipant() && this.data.participantsMoveGroup.getId()!=group.getId()) )
			{
				this.setStateMoveParticipant();
			}
			this.data.participantsMove.push(participant);
			this.data.participantsMoveGroup = group;
		},
		
		unselectParticipantMove: function(participant)
		{
			for (var i=0; i<this.data.participantsMove.length;i++)
			{
				if (this.data.participantsMove[i].getId()==participant.getId()){
					this.data.participantsMove.splice(i,1);
					//Si no queda ninguno se desactiva el modo selección de participantes
					if (this.data.participantsMove.length==0)
					{
						StateManager.reset();
					}
					return true;
				}
			}
			return false;			
		},
		
		/**
		 * Cambia el estado de selección de un participante de un grupo
		 * @param participant
		 */
		changeParticipantStateMove: function(participant, group)
		{
			if (this.isSelectedParticipantMove(participant)==false)
			{
				this.selectParticipantMove(participant, group);
			}
			else{
				this.unselectParticipantMove(participant);
			}
		},
				
		/**
		 * Selección de un participante para ser copiado a un grupo
		 * @param participant Participante a copia
		 * @param group Grupo desde el que se desea copiar el participante
		 */
		selectParticipantCopy: function(participant, group)
		{
			//Si es necesario, se activa el modo selección de participantes del grupo a copiar
			//Si cambia el grupo desde el que se copian los participantes se activa de nuevo
			if (this.isStateCopyParticipant()==false || ( this.isStateCopyParticipant() && this.data.participantsCopyGroup.getId()!=group.getId()) )
			{
				this.setStateCopyParticipant();
			}
			this.data.participantsCopy.push(participant);
			this.data.participantsCopyGroup = group;
		},
		
		unselectParticipantCopy: function(participant)
		{
			for (var i=0; i<this.data.participantsCopy.length;i++)
			{
				if (this.data.participantsCopy[i].getId()==participant.getId()){
					this.data.participantsCopy.splice(i,1);
					//Si no queda ninguno se desactiva el modo selección de participantes
					if (this.data.participantsCopy.length==0)
					{
						StateManager.reset();
					}
					return true;
				}
			}
			return false;			
		},
		
		/**
		 * Cambia el estado de selección de un participante de un grupo
		 * @param participant
		 */
		changeParticipantStateCopy: function(participant, group)
		{
			if (this.isSelectedParticipantCopy(participant)==false)
			{
				this.selectParticipantCopy(participant, group);
			}
			else{
				this.unselectParticipantCopy(participant);
			}
		},
		
		isStateNormal: function(){
			return this.data.state=="normal";
		},
				
		isStateAddParticipant: function(){
			return this.data.state=="addParticipant";
		},
		
		isStateMoveParticipant: function(){
			return this.data.state=="moveParticipant";
		},
		
		isStateCopyParticipant: function(){
			return this.data.state=="copyParticipant";
		},
		
		isStateAddGroup: function(){
			return this.data.state=="addGroup";
		},
		
		isStateAddResource: function(){
			return this.data.state=="addResource";
		},
		
		isStateAddTool: function(){
			return this.data.state=="addTool";
		},
		
		isStateReuseToolInstance: function(){
			return this.data.state=="reuseToolInstance";
		},
		
		/**
		 * Selección de un grupo para ser asignado a una actividad
		 * @param group Grupo a asignar
		 */
		selectGroup: function(group)
		{
			if (this.isStateAddGroup()==false)
			{
				this.setStateAddGroup();
			}
			this.data.groups.push(group);		
		},
		
		/**
		 * Selección de una instancia de herramienta para ser reutilizada en otra actividad
		 * @param toolInstance Instancia de herramienta a reutilizar en otra actividad
		 */
		selectToolInstanceReuse: function(toolInstance, activity, instancedActivity)
		{
			this.setStateReuseToolInstance();
			this.data.toolInstance = toolInstance;	
			this.data.activity = activity;
			this.data.instancedActivity = instancedActivity;
		},
		
		/**
		 * Deselecciona la instancia de herramienta a ser reutilizada en otra actividad
		 */
		unselectToolInstanceReuse: function()
		{
			StateManager.reset();
			return true;		
		},
		
		/**
		 * Cambia el estado de selección de una instancia de herramienta a ser reutilizada
		 */
		changeToolInstanceReuse: function(toolInstance, activity, instancedActivity)
		{
			if (this.isStateReuseToolInstance()==false || this.data.toolInstance.getId()!= toolInstance.getId())
			{
				//No se estaba en modo reutilización de herramientas o se cambia a reutilizar otra diferente
				this.selectToolInstanceReuse(toolInstance, activity, instancedActivity);
			}
			else{
				this.unselectToolInstanceReuse();
			}
		}
	
}