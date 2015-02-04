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
