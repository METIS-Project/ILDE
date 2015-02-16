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
 * @class Un grupo en la instanciación del diseño
 * @param roleid Identificador del grupo
 */
var Group = function(roleid) {
	this.type = "group";
	/**
	 * Indentificador del grupo
	 */
	this.roleid = roleid;
	/**
	 * Instancias del grupo
	 */
	this.instances = new Array();
	IDPool.registerNewObject(this);
};
/**
 * Añade una instancia al grupo
 * @param instance Identificador de la instancia a añadir al grupo
 */
Group.prototype.addInstance = function(instance) {
	this.instances.push(instance)
};
