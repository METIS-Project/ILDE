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
 * Funcionalidad asociada a un grupo
 */

var Group = function(data) {

	var data = data;

	this.getId = function() {
		return data.id;
	},

	this.getName = function() {
		return data.name;
	},
	
	this.setName = function(name) {
		data.name = name;
	},

	this.getParticipants = function() {
		var participantList = new Array();
		if (data.participantIds)
		{
			for ( var i = 0; i < data.participantIds.length; i++) {
				var participant = ParticipantContainer.getParticipant(data.participantIds[i]);
				if (participant !=false){
					participantList.push(participant);
				}
			}
		}
		return participantList;
	},
	
	this.deleteParticipant = function(participant) {
		if (data.participantIds)
		{
			for ( var i = 0; i < data.participantIds.length; i++) {
				if (data.participantIds[i]==participant.getId())
				{
					data.participantIds.splice(i,1);
					return true;
				}
			}
		}
		return false;
	},
	
	this.addParticipant = function(participant) {
		if (data.participantIds)
		{
			data.participantIds.push(participant.getId());
		}
		else{
			data.participantIds = new Array();
			data.participantIds.push(participant.getId());
		}
	},
	
	this.containsParticipant = function(participant)
	{
		if (data.participantIds)
		{
			for ( var i = 0; i < data.participantIds.length; i++) {
				if (data.participantIds[i] == participant.getId())
				{
					return true;
				}
			}
		}
		return false;
	}
}