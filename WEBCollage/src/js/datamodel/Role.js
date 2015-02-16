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
* @class Un rol
* @param title Nombre del rol
* @param type Tipo de rol
*/
var Role = function(title, type) {
    this.type = "role";
    this.subtype = type;
    this.title = title;
    this.description = "";
    this.persons = {
        min : "",
        max : ""
    };
    IDPool.registerNewObject(this);
    this.children = new Array();
};

/**
* Añade un rol hijo al rol
* @param role Rol hijo a añadir
*/
Role.prototype.addChild = function(role) {
    this.children.push(role);
};

/**
* Elimina un rol hijo del rol
* @param role Rol hijo a eliminar
*/
Role.prototype.removeChild = function(role) {
    this.children.splice(this.children.indexOf(role), 1);
};

/**
* Obtiene el nombre del rol
* @return Nombre del rol
*/
Role.prototype.getTitle = function() {
    return this.title;
};

Role.prototype.setTitle = function(title) {
    this.title = title;
};


