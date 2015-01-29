/**
 * Di�logo de actualizaci�n del listado de participantes
 */

var ParticipantManagementDialog = {
		
	participantsAdd: new Array(),	
	participantsDelete: new Array(),
	participantsRemain: new Array(),
	
		
	init: function(){
		dojo.connect(dojo.byId("botonAceptarCambiosAlumnos"), "onclick", function() {
			ParticipantManagementDialog.updateParticipantList();
		});
		
		dojo.connect(dojo.byId("botonCancelarCambiosAlumnos"), "onclick", function() {
			ParticipantManagementDialog.hideParticipantManagement();
		});
	},
	
	reset: function(){
		ParticipantManagementDialog.participantsAdd = new Array();
		ParticipantManagementDialog.participantsDelete = new Array();
		ParticipantManagementDialog.participantsRemain = new Array();
	},
	
	/**
	 * Muestra la ventana de actualizaci�n del listado de participantes
	 */
	showParticipantManagement: function()
	{
        var dlg = dijit.byId("comparacionAlumnos");
        this.internationalize();
        dlg.show();
	},
	
	/**
	 * Oculta la ventana de actualizaci�n del listado de participantes
	 */
	hideParticipantManagement: function()
	{
		dijit.byId("comparacionAlumnos").hide();
	},
	
	
    /**
     * Muestra en una tabla el listado de participantes que se van a a�adir
     */
    showParticipantsAdd: function(participantsAdd){
    	ParticipantManagementDialog.participantsAdd = participantsAdd;
        var table = document.getElementById("addParticipantsTable");
        //Se elimina el contenido anterior de la tabla
        if (table.hasChildNodes()){
            while (table.childNodes.length>=1){
                table.removeChild(table.firstChild);
            }
        }
        if (participantsAdd.length>0){
        	table.className="tableAllParticipants";
            //Se construye la tabla con la nueva informaci�n
            for (var i = 0; i < participantsAdd.length; i++){
                    var tr = table.appendChild(document.createElement("tr"));
                    var td = tr.appendChild(document.createElement("td"));
                    td.setAttribute("class", "participant");
                    td.innerHTML = participantsAdd[i].name;
            }
        }
        var per=document.getElementById("numberAddedParticipants");
        per.innerHTML = i18n.get("participantsToAdd") + " : " + participantsAdd.length;
        per.style.fontWeight = 'bold';
    },
    
    /**
     * Muestra en una tabla el listado de participantes que se van a eliminar
     */
    showParticipantsDelete: function(participantsDelete){
    	ParticipantManagementDialog.participantsDelete = participantsDelete;
        var table = document.getElementById("deleteParticipantsTable");
        //Se elimina el contenido anterior de la tabla
        if (table.hasChildNodes()){
            while (table.childNodes.length>=1){
                table.removeChild(table.firstChild);
            }
        }
        if (participantsDelete.length>0){
        	table.className="tableAllParticipants";
            //Se construye la tabla con la nueva informaci�n
            for (var i = 0; i < participantsDelete.length; i++){
                    var tr = table.appendChild(document.createElement("tr"));
                    
        			var td = document.createElement("td");
        			var participantIcon = document.createElement("img");
        			//var src = GroupsPainter.getParticipantIconSrc(ParticipantContainer.getParticipant(participantsDelete[i]));
        			var src = GroupsPainter.getParticipantIconSrc(participantsDelete[i]);
        			participantIcon.setAttribute("src", src);
        			participantIcon.setAttribute("width", "12");
        			participantIcon.setAttribute("height", "20");
        			td.setAttribute("style", "padding-right:0px;padding-left:0px;");
        			td.appendChild(participantIcon);
        			tr.appendChild(td);
        			
                    var td = tr.appendChild(document.createElement("td"));
                    td.setAttribute("class", "participant");
                    td.innerHTML = participantsDelete[i].getName();
            }
        }
        var per=document.getElementById("numberDeletedParticipants");
        per.innerHTML = i18n.get("participantsToDelete") + " : " + participantsDelete.length;
        per.style.fontWeight = 'bold';
    },
    
    /**
     * Muestra en una tabla el listado de participantes que permanecen
     */
    showParticipantsRemain: function(participantsRemain){
    	ParticipantManagementDialog.participantsRemain = participantsRemain;
        var table = document.getElementById("remainParticipantsTable");
        //Se elimina el contenido anterior de la tabla
        if (table.hasChildNodes()){
            while (table.childNodes.length>=1){
                table.removeChild(table.firstChild);
            }
        }
        if (participantsRemain.length>0){
            table.className="tableAllParticipants";
            //Se construye la tabla con la nueva informaci�n
            for (var i = 0; i < participantsRemain.length; i++){
                var tr = table.appendChild(document.createElement("tr"));
                
    			var td = document.createElement("td");
    			var participantIcon = document.createElement("img");
    			var src = GroupsPainter.getParticipantIconSrc(participantsRemain[i]);
    			participantIcon.setAttribute("src", src);
    			participantIcon.setAttribute("width", "12");
    			participantIcon.setAttribute("height", "20");
    			td.setAttribute("style", "padding-right:0px;padding-left:0px;");
    			td.appendChild(participantIcon);
    			tr.appendChild(td);
            	
                var td = tr.appendChild(document.createElement("td"));
                td.setAttribute("class", "participant");
                td.innerHTML = participantsRemain[i].getName();
            }
        }
        var per=document.getElementById("numberRemainParticipants");
        per.innerHTML = i18n.get("participantsRemain") + " : " + participantsRemain.length;
        per.style.fontWeight = 'bold';
    },
    
       
    updateParticipantList: function(){	
    	var updated = false;
    	ParticipantManagementDialog.hideParticipantManagement();	
        for (var i = 0; i < ParticipantManagementDialog.participantsDelete.length; i++){
    		ParticipantManager.deleteParticipant(ParticipantManagementDialog.participantsDelete[i]);
    	}
    	for (var i = 0; i < ParticipantManagementDialog.participantsAdd.length; i++){
    		ParticipantContainer.addParticipant(ParticipantManagementDialog.participantsAdd[i]);
    	}
    	for (var i = 0; i < ParticipantManagementDialog.participantsRemain.length; i++){
    		var participant = ParticipantContainer.getParticipant(ParticipantManagementDialog.participantsRemain[i].getId());
    		if (ParticipantManagementDialog.participantsRemain[i].getIsStaff()!=participant.getIsStaff()){
    			participant.setIsStaff(ParticipantManagementDialog.participantsRemain[i].getIsStaff());
    			updated = true;
    		}
    	}
    	if (ParticipantManagementDialog.participantsAdd.length > 0 || ParticipantManagementDialog.participantsDelete.length > 0 || updated==true){
    		InformativeDialogs.showAlertDialog(i18n.get("info"), i18n.get("SuccessUpdatingParticipants"));
    		GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
    		JsonDB.notifyChanges();
    	}
		ParticipantManagementDialog.reset();
    },
    
    internationalize: function(){
    	dijit.byId("addedParticipantsTP").setTitle(i18n.get("participantsToAdd"));
    	dijit.byId("deletedParticipantsTP").setTitle(i18n.get("participantsToDelete"));
    	dijit.byId("remainParticipantsTP").setTitle(i18n.get("participantsRemain"));
    	dojo.byId("botonAceptarCambiosAlumnos").innerHTML = i18n.get("okButton");
    	dojo.byId("botonCancelarCambiosAlumnos").innerHTML = i18n.get("cancelButton");
    	dijit.byId("comparacionAlumnos").setAttribute("title", i18n.get("participantChanges"));
    }
	
	
}