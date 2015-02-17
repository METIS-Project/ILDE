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