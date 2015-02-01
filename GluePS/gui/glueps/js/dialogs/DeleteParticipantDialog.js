/**
 * Diálogo de borrado de un participante
 */

var DeleteParticipantDialog = {
		
	init: function(){
		dojo.connect(dojo.byId("dialogConfirmDeleteParticipantOk"), "onclick", function() {
			var participantId = dijit.byId("deleteParticipantId").getValue();
			DeleteParticipantDialog.deleteParticipant(participantId);
		});
		
		dojo.connect(dojo.byId("dialogConfirmDeleteParticipantCancel"), "onclick", function() {
			DeleteParticipantDialog.hideDeleteParticipant();
		});
	},
	
	/**
	 * Muestra la ventana de confirmación del borrado de un participante
	 * @param group Grupo a borrar
	 */
	showDeleteParticipant: function(participant)
	{
        var dlg = dijit.byId("dialogConfirmDeleteParticipant");
        dlg.titleNode.innerHTML = i18n.get("confirmDelete");
        dojo.byId("dialogConfirmDeleteParticipantInfo").innerHTML= i18n.get("confirmDeleteParticipant");
        dlg.show();
		dijit.byId("deleteParticipantId").setValue(participant.getId());
	},
	
	/**
	 * OCulta la ventana de confirmación del borrado de un participante
	 */
	hideDeleteParticipant: function()
	{
		dijit.byId("dialogConfirmDeleteParticipant").hide();
	},
	
	
	deleteParticipant: function(participantId)
	{
		//Guardar el estado actual previo al borrado
		GroupsPainter.currentState = GroupsPainter.updateCurrentStateInfo();
		//Borrar participante
		var participant = ParticipantContainer.getParticipant(participantId);
		ParticipantManager.deleteParticipant(participant);
		DeleteParticipantDialog.hideDeleteParticipant();
		//Notificar cambios
		JsonDB.notifyChanges();
	}
}