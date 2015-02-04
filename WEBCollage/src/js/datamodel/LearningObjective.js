/**
* @class Un objetivo de aprendizaje
* @param title Nombre del objetivo
* @param description Descripción del objetivo
*/
var LearningObjective = function(title, description) {
    /**
     * Indicador de tipo
     */
    this.type = "lo"
    /**
     * Nombre del objetivo
     */
    this.title = title;
    /**
     * Descripción del objetivo
     */
    this.description = description;
    IDPool.registerNewObject(this);
};

/**
* Obtiene el nombre del objetivo
* @return Nombre del objetivo
*/
LearningObjective.prototype.getTitle = function() {
    return this.title;
};

