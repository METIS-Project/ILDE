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
