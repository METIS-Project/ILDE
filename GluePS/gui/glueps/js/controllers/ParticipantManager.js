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
 * Funcionalidad asociada a la gestión de los participantes
 */
var ParticipantManager = {
		
		/**
		 * Borra un participante del listado de participantes y de los grupos a los que pertenece 
		 * @param participant Participante que se desea borrar
		 */
		deleteParticipant: function(participant){
			//Borrarle de los grupos en los que esté
			var groups = GroupContainer.getAllGroups();
			for (var i = 0; i < groups.length; i++){
				if (groups[i].containsParticipant(participant)){
					groups[i].deleteParticipant(participant);
				}				
			}
			ParticipantContainer.deleteParticipant(participant);
		},
		
	     /**
	      * Obtiene los participantes de un curso
	      */
	     getCourseParticipants:function() {
		 	if (LdShakeManager.ldShakeMode){
		 		var version = gupUrl("http://localhost?" + LearningEnvironment.getLearningEnvironmentParameters(), "version");
		 		var wstoken = gupUrl("http://localhost?" + LearningEnvironment.getLearningEnvironmentParameters(), "wstoken");
		     	var url = window.location.href.split("/GLUEPSManager")[0];
		     	url = url + "/GLUEPSManager/ldshake/courses";
		        var content = {
		        		type : JsonDB.deploy.learningEnvironment.type,
		        		accessLocation: JsonDB.deploy.learningEnvironment.accessLocation,
		        		creduser: JsonDB.deploy.learningEnvironment.creduser,
		        		credsecret: JsonDB.deploy.learningEnvironment.credsecret,
		        		course: JsonDB.deploy.course.id.substring(JsonDB.deploy.course.id.lastIndexOf("/courses/")+9),
		        		sectoken: LdShakeManager.sectoken,
		        		deployId: JsonDB.getDeploymentId().substring(JsonDB.getDeploymentId().lastIndexOf("/deploys/")+9),
		        		version: version,
		        		wstoken: wstoken
		        };
		 	}
		 	else{
		 		url = JsonDB.getCourseId();
		 		var content = {};
		 	}
	        var xhrArgs = {
	            url: url,
	            content: content,
				handleAs : "text",// Tipo de dato de la respuesta del Get,
	            load: function(data) {
	            	//Obtener los participantes nuevos que aparecen
	            	var participantsUpdated = ParticipantManager.getParticipantsFromXML(data);
	            	var participantsAdd = ParticipantManager.getParticipantsAdd(participantsUpdated);
	            	ParticipantManagementDialog.showParticipantsAdd(participantsAdd);
	            	
	            	//Obtener los participantes antiguos que desaparecen
	            	var participantsDelete = ParticipantManager.getParticipantsDelete(participantsUpdated);
	            	ParticipantManagementDialog.showParticipantsDelete(participantsDelete);
	            	
	            	//Obtener los participantes que permanecen
	            	var participantsRemain = ParticipantManager.getParticipantsRemain(participantsUpdated, participantsAdd, participantsDelete);
	            	ParticipantManagementDialog.showParticipantsRemain(participantsRemain);
	            	
	            	ParticipantManagementDialog.showParticipantManagement();
	            	
	            },
	            error: function(error, ioargs) {
	            	if (ioargs.xhr.status == 403){//wrong credentials
	            		InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("ErrorUpdatingParticipants.credentialError"));
					}else if (ioargs.xhr.status == 404){//the user doesn't have permission to get the course and its participants or the course no longer exists
						InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("ErrorUpdatingParticipants.courseError"));
					}else{ //Another internal error
						InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("ErrorUpdatingParticipants.internalError"));
					}
	            }
	        };

	        //Call the asynchronous xhrGet
	        var deferred = dojo.xhrGet(xhrArgs);
	    },
	    
	     /**
	      * Dado el xml obtiene los participantes y los devuelve en un array
	      * @param xml XML con información de los participantes
	      * @return Array con el identificador y el nombre de cada clase
	      */
	     getParticipantsFromXML: function(xml)
	     {
	    	 var jsdom = dojox.xml.DomParser.parse(xml);
	    	 var participants = new Array();
	    	 var entries = jsdom.getElementsByTagName("entry");

	    	 for (var i = 0; i < entries.length; i++)
	    	 {
	    		 var participant = new Object();
	    		 participant.id = entries[i].getElementsByTagName("value")[0].getElementsByTagName("id")[0].childNodes[0].nodeValue;
	    		 participant.name = entries[i].getElementsByTagName("value")[0].getElementsByTagName("name")[0].childNodes[0].nodeValue;
	    		 if (entries[i].getElementsByTagName("value")[0].getElementsByTagName("learningEnvironmentData").length > 0){
	    			 participant.learningEnvironmentData = entries[i].getElementsByTagName("value")[0].getElementsByTagName("learningEnvironmentData")[0].childNodes[0].nodeValue;
	    		 }
	    		 if (entries[i].getElementsByTagName("value")[0].getElementsByTagName("staff")[0].childNodes[0].nodeValue=="true"){
		    		 participant.isStaff = true;
	    		 }
	    		 else{
	    			 participant.isStaff = false;
	    		 }
	    		 //Asociarle el identificador del despliegue. Quizá debería hacerlo el servidor en su lugar
	    		 participant.deployId = JsonDB.getDeploymentId();
	    		 participants.push(participant);
	    	 }
	    		 
	    	 return participants;
	     },
	     
	    getParticipantsAdd: function(participantsUpdated){		     
	     	//Obtener los participantes nuevos que aparecen
	     	var participantsAdd = new Array();
	     	for (var i = 0; i < participantsUpdated.length; i++){
	     		if (ParticipantContainer.getParticipant(participantsUpdated[i].id)==false){
	     			participantsAdd.push(participantsUpdated[i]);
	     		}
	     	}
	     	return participantsAdd;
	    },
	    
	    getParticipantsDelete: function(participantsUpdated){
	    	//Obtener los participantes antiguos que desaparecen
	    	var participantsDelete = new Array();
	    	var participantsOld = ParticipantContainer.getAllParticipants();
	    	for (var i = 0; i < participantsOld.length; i++){
	    		var found = false;
	    		var j = 0;
	    		while (found == false && j < participantsUpdated.length){
	    			if (participantsOld[i].getId() == participantsUpdated[j].id){
	    				found = true;
	    			}
	    			j++;
	    		}
	    		if (found == false){
	    			participantsDelete.push(participantsOld[i]);
	    		}
	    	}
	    	return participantsDelete;
	    },
	    
	    getParticipantsRemain: function(participantsUpdated, participantsAdd, participantsDelete){
	    	var participantsRemain = new Array();
	    	for (var i = 0; i < participantsUpdated.length; i++){
	    		var found = false;
	    		var j = 0;
	    		while (found == false && j < participantsAdd.length){
	    			if (participantsUpdated[i].id == participantsAdd[j].id){
	    				found = true;
	    			}
	    			j++;
	    		}
	    		j = 0;
	    		while (found == false && j < participantsDelete.length){
	    			if (participantsUpdated[i].id == participantsDelete[j].getId()){
	    				found = true;
	    			}
	    			j++;
	    		}
	    		if (found == false){
	    			participantsRemain.push(new Participant(participantsUpdated[i]));
	    		}
	    	}
	    	return participantsRemain;
	    }
		
}