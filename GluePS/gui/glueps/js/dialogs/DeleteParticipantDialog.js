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