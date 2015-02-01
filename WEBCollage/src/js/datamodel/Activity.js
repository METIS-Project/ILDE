/**
 * @class Una actividad 
 * @param title Nombre de la actividad
 * @param type Tipo de actividad
 */
var Activity = function(title, type) {
    /**
    * Indicador de tipo actividad
    */
    this.type="activity";
    /**
    * Tipo de actividad
    */
    this.subtype = type;
    /**
    * Nombre de la actividad
    */
    this.title = title;
    /**
    * Descripción de la actividad
    */
    this.description= "";
    IDPool.registerNewObject(this);
};

/**
 * Obtiene el título de la actividad
 * @return título de la actividad
 */
Activity.prototype.getTitle = function() {
    return this.title;
};

Activity.prototype.setTitle = function(title) {
    this.title = title;
};
