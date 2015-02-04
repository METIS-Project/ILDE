/**
 * @class Mantiene un registro de los objetos 
 */
var IDPool = {

    /**
    * Siguiente identificador entero que se proporcionará
    */
    next : 1,
    /**
    * Objetos registrados
    */
    objects : {},

    /**
    * Registra un nuevo objeto
    * @param obj Nuevo objeto a registrar
    */
    registerNewObject : function(obj) {
        obj.id = this.next;
        this.objects[this.next] = obj;
        this.next++;
    },

    /**
    * Registra un objeto ya existente
    * @param obj Objeto ya existente a registrar
    */
    registerExistingObject : function(obj) {
        this.objects[obj.id] = obj;
        this.next = Math.max(this.next, parseInt(obj.id) + 1);
        return;
    },

    /**
    * Obtiene el objeto cuyo id se proporciona
    * @param id Identificador del objeto a obtener
    * @return Objeto con el identificador indicado
    */
    getObject : function(id) {
        return this.objects[id];
    },

    /**
    * Elimina del registro el objeto cuyo id se proporciona
    * @param objId Identificador del objeto a eliminar del registro
    */
    removeObject : function(objId) {
        delete this.objects[objId];
    },

    /**
    * Borra toda la información contenida en el registro
    */
    clear : function() {
        this.next = 1;
        this.objects = {};
    }
};
