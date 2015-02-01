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