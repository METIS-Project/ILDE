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
				participantList.push(ParticipantContainer.getParticipant(data.participantIds[i]));
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