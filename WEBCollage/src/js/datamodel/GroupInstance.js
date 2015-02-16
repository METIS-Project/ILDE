/**
Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Web Collage.

Web Collage is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Web Collage is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
 * @class Una instancia de un grupo en la instanciación del diseño
 * @param name Nombre de la instancia del grupo
 */
var GroupInstance = function(name) {
    /**
     * Nombre de la instancia de grupo
     */
    this.name = name;
    /**
     * Indicador de tipo
     */
    this.type = "groupInstance";
    //Las instancia de grupo son del tipo "groupInstance"
    /**
     * Participantes asignados a la instancia de grupo
     */
    this.participants = [];
    IDPool.registerNewObject(this);
};
/**
 * Añade un participante a la instancia
 * @param idparticipant Identificador del participante a añadir a la instancia
 */
GroupInstance.prototype.addParticipant = function(idparticipant) {
    this.participants.push(idparticipant);
}
/**
 * Remove all participants from the group
 */
GroupInstance.prototype.clearParticipants = function() {
    this.participants = [];
}
/**
 * @return participant list
 */
GroupInstance.prototype.getParticipants = function() {
    return this.participants;
}
