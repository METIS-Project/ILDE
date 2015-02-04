/**
 *  Menú de un participante
 */

var ParticipantMenu = {

		
		/**
		 * Construye el menú de opciones de un participante
		 * 
		 * @param data Información del participante
		 * @return Items del menú
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