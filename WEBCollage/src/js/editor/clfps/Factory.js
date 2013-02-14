/**
 * @class Factoría donde se registran los patrones y sus factorías
 */
var Factory = {
    /**
    * Factorías de los patrones registrados por la factoría
    */
    factories : [],
    /**
    * Registra una factoría de un patrón
    * @param id Identificador para la factoría
    * @param definition Patrón a registrar
    * @param factory Factoría del patrón a registrar
    */
    registerFactory : function(id, definition, factory) {
        this.factories[id] = factory;
        Inheritance.registerPattern(id, definition);
    },

    /**
    * Crea la factoría de un patrón
    * @param clfpId Identificador del patrón
    * @param callback
    */
    create : function(clfpId, callback) {
        var factory = Factory.factories[clfpId];
        factory.create(callback);
    }
};
