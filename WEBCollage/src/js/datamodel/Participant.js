/**
 * @class Un participante en la instanciación del diseño
 * @param id Identificador del participante
 * @param name Nombre del participante
 * @param type Tipo de participante
 */
var Participant = function(id, name, type) {

    this.type = "participant";
    
    /**
    * Identificador del participante
    */
    this.participantId = id;
    /**
    * Nombre del participante
    */
    this.name = name;
    /**
    * Tipo de participante (estudiante o profesor)
    */
    this.participantType = type;
    
    IDPool.registerNewObject(this);
};

