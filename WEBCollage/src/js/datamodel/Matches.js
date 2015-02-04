/**
 * @author Eloy
 * Parejas de elementos relacionados
 */

/**
 * @class Creación de los emparejamientos
 */
var Matches = function() {
    //Contiene las parejas de elementos
    this.data = new Array();
};

/**
 * Asocia los emparejamientos
 * @param data Emparejamientos entre los elementos
 */
Matches.prototype.setData = function(data) {
    this.data = data;
};

/**
 * Define emparejamentos entre un elemento y un conjunto de ellos
 * @param id Identificador del elemento a emparejar
 * @param ids Elementos con los que se empareja
 */
Matches.prototype.setMatchesFor = function(id, ids) {
    this.removeMatchesFor(id);
    for ( var i = 0; i < ids.length; i++) {
        this.data.push( [ id, ids[i] ]);
    }
};

/**
 * Añade un nuevo emparejamiento entre dos elementos
 * @param ida Identificador del elemento a
 * @param idb Identificador del elemento b
 * @param data Datos asociados al emparejamiento
 */
Matches.prototype.addMatch = function(ida, idb, data) {
    if (!this.match(ida, idb)) {
        if (typeof data == "undefined") {
            this.data.push( [ ida, idb ]);
        } else {
            this.data.push( [ ida, idb, data ]);
        }
    }
};

/**
 * Indica si existe un emparejamiento entre dos elementos
 * @param ida Identificador del elemento a
 * @param idb Identificador del elemento b
 * @return Booleano que indica si existe tal emparejamiento
 */
Matches.prototype.match = function(ida, idb) {
    for ( var i = 0; i < this.data.length; i++) {
        if ((this.data[i][0] == ida && this.data[i][1] == idb)
            || (this.data[i][0] == idb && this.data[i][1] == ida)) {
            return true;
        }
    }
    return false;
};

Matches.prototype.getData = function(ida, idb) {
    for ( var i = 0; i < this.data.length; i++) {
        if ((this.data[i][0] == ida && this.data[i][1] == idb)
            || (this.data[i][0] == idb && this.data[i][1] == ida)) {
            return true;
        }
    }
    return null;
};

/**
 * Elimina todos los emparejamientos existentes entre dos elementos
 * @param ida Identificador del elemento a
 * @param idb Identificador del elemento b
 */
Matches.prototype.removeMatch = function(ida, idb) {
    var matches = new Array();

    for ( var i = 0; i < this.data.length; i++) {
        if ((this.data[i][0] == ida && this.data[i][1] == idb)
            || (this.data[i][0] == idb && this.data[i][1] == ida)) {
            matches.push(i);
        }
    }

    for (i = matches.length - 1; i >= 0; i--) {
        this.data.splice(matches[i], 1);
    }
};

/**
 * Elimina todos los emparejamientos en los que participa el elemento
 * @param id Identificador del elemento
 */
Matches.prototype.removeMatchesFor = function(id) {
    var matches = new Array();

    for (var i = 0; i < this.data.length; i++) {
        if (this.data[i][0] == id || this.data[0][1] == id) {
            matches.push(i);
        }
    }

    for (i = matches.length - 1; i >= 0; i--) {
        this.data.splice(matches[i], 1);
    }
};

/**
 * Obtiene todos los elementos emparejados con un elemento dado
 * @param id Identificador del elemento
 * @return Elementos emparejados con él
 */
Matches.prototype.getMatchesFor = function(id) {
    var matches = new Array();
    var bad = new Array();

    for ( var i = 0; i < this.data.length; i++) {
        var obj = null;
        var match = false;
        if (this.data[i][0] == id) {
            match = true;
            obj = this.data[i][1];
        } else if (this.data[i][1] == id) {
            match = true;
            obj = this.data[i][0];
        }

        if (match) {
            if (IDPool.getObject(obj)) {
                matches.push(obj);
            } else {
                bad.push(i);
            }
        }
    }

    for (i = bad.length - 1; i >= 0; i--) {
        this.data.splice(bad[i], 1);
    }

    return matches;
};

Matches.prototype.getMatchingObjectsFor = function(id) {
    var matches = new Array();
    var bad = new Array();

    for ( var i = 0; i < this.data.length; i++) {
        var obj = null;
        var match = false;
        if (this.data[i][0] == id) {
            match = true;
            obj = this.data[i][1];
        } else if (this.data[i][1] == id) {
            match = true;
            obj = this.data[i][0];
        }

        if (match) {
            obj = IDPool.getObject(obj);
            if (obj) {
                matches.push(obj);
            } else {
                bad.push(i);
            }
        }
    }

    for (i = bad.length - 1; i >= 0; i--) {
        this.data.splice(bad[i], 1);
    }

    return matches;
};
