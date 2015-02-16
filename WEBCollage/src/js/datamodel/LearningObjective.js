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

