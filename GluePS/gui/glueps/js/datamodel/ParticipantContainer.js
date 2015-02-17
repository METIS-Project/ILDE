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
 * Funcionalidad asociada a los participantes
 */

var ParticipantContainer = {

	getParticipant : function(participantId) {
		var participants = JsonDB.getParticipants();
		for ( var i = 0; i < participants.length; i++) {
			if (participants[i].id == participantId) {
				return new Participant(participants[i]);
			}
		}
		return false;
	},

	getAllParticipants : function() {
		var participantList = new Array();
		var participants = JsonDB.getParticipants();
		for ( var i = 0; i < participants.length; i++) {
			participantList.push(new Participant(participants[i]));
		}

		return participantList;
	},
	
	getTeachers: function(){
		var teacherList = new Array();
		var participants = JsonDB.getParticipants();
		for ( var i = 0; i < participants.length; i++) {
			if (participants[i].isStaff)
			{
				teacherList.push(new Participant(participants[i]));
				
			}
		}
		return teacherList;
	},
	
	getStudents: function(){
		var studentList = new Array();
		var participants = JsonDB.getParticipants();
		for ( var i = 0; i < participants.length; i++) {
			if (participants[i].isStaff==false)
			{
				studentList.push(new Participant(participants[i]));
			}
		}
		return studentList;
	},
	
	deleteParticipant : function(participant) {
		var participants = JsonDB.getParticipants();
		for ( var i = 0; i < participants.length; i++) {
			if (participants[i].id == participant.getId()) {
				participants.splice(i, 1);
				return true;
			}
		}
		return false;
	},
	
	addParticipant : function(data) {		
    	if (typeof (JsonDB.deploy.participants) == "undefined"){
    		JsonDB.deploy.participants = new Array();
    	}
    	JsonDB.deploy.participants.push(data);
	}
}