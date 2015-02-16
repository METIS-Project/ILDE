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
