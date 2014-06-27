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
		     	var url = window.location.href.split("/GLUEPSManager")[0];
		     	url = url + "/GLUEPSManager/ldshake/courses";
		        var content = {
		        		type : JsonDB.deploy.learningEnvironment.type,
		        		accessLocation: JsonDB.deploy.learningEnvironment.accessLocation,
		        		creduser: JsonDB.deploy.learningEnvironment.creduser,
		        		credsecret: JsonDB.deploy.learningEnvironment.credsecret,
		        		course: JsonDB.deploy.course.id.substring(JsonDB.deploy.course.id.lastIndexOf("/courses/")+9),
		        		sectoken: LdShakeManager.sectoken,
		        		deployId: JsonDB.getDeploymentId().substring(JsonDB.getDeploymentId().lastIndexOf("/deploys/")+9)
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
	            	var participantsRemain = ParticipantManager.getParticipantsRemain(participantsAdd, participantsDelete);
	            	ParticipantManagementDialog.showParticipantsRemain(participantsRemain);
	            	
	            	ParticipantManagementDialog.showParticipantManagement();
	            	
	            },
	            error: function(error, ioargs) {  	   
	         	   InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("ErrorUpdatingParticipants"));
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
	    
	    getParticipantsRemain: function(participantsAdd, participantsDelete){
	    	var participantsRemain = new Array();
	    	var participantsOld = ParticipantContainer.getAllParticipants();
	    	for (var i = 0; i < participantsOld.length; i++){
	    		var found = false;
	    		var j = 0;
	    		while (found == false && j < participantsAdd.length){
	    			if (participantsOld[i].getId()== participantsAdd[j].id){
	    				found = true;
	    			}
	    			j++;
	    		}
	    		j = 0;
	    		while (found == false && j < participantsDelete.length){
	    			if (participantsOld[i].getId() == participantsDelete[j].getId()){
	    				found = true;
	    			}
	    			j++;
	    		}
	    		if (found == false){
	    			participantsRemain.push(participantsOld[i]);
	    		}
	    	}
	    	return participantsRemain;
	    }
		
}