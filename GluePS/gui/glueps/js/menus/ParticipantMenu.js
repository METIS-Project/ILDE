/**
 *  Men� de un participante
 */

var ParticipantMenu = {

		
		/**
		 * Construye el men� de opciones de un participante
		 * 
		 * @param data Informaci�n del participante
		 * @return Items del men�
		 */
		getParticipantMenu : function(data) {
			var items = new Array();
			items.push({
				label : i18n.get("addParticipant"),
				icon: "edit",
				onClick : function(data) {
					StateManager.setStateAddParticipant(data.participant);
				},
				data : data,
				help : i18n.get("addParticipantHelp")
			});
			return items;
		}
}