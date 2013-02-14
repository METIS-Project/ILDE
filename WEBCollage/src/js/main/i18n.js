/**
 * Internacionalización en Web Instance Collage
 */

/*global dojo */

var i18n = {
    /**
     * Obtiene la cadena de caracteres que se corresponde con el key suministrado
     * @param key Identificador asociado a la cadena de caracteres
     */
    get : function(key) {
        return this.messages[key] ? this.messages[key] : ('!' + key + '!');
    },
    getReplaced : function(key, values) {
        return dojo.string.substitute(this.get(key), values);
    },
    getReplaced1 : function(key, value) {
        return this.getReplaced(key, [value]);
    },
    getReplaced2 : function(key, value1, value2) {
        return this.getReplaced(key, [value1, value2]);
    }
};
