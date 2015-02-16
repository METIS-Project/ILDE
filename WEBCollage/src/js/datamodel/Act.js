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
 * @class Actividades de una fase o clfp
 * @param title Nombre de las actividades
 * @param isMainAct Indica si es la principal o no
 */
var Act = function(title, isMainAct) {
    /**
     * Nombre de las actividades
     */
    this.title = title;

    if (isMainAct) {
        /**
         * Indicador de tipo
         */
        this.type = "clfpact";
        this.clfps = new Array();
        /**
         * Indica si es actividad principal o no
         */
        this.isMainAct = true;
    } else {

        this.type = "act";
        this.roleparts = new Array();
        this.learners = new Array();
        this.staff = new Array();
        this.isMainAct = false;
    }

    IDPool.registerNewObject(this);
};

/**
* Obtiene el nombre de las actividades
*/
Act.prototype.getTitle = function() {
    return this.title;
};

Act.prototype.setTitle = function(title) {
    this.title = title;
};

/**
* Incluye un rol de tipo estudiante
* @param role Rol de tipo estudiante a incluir
*/
Act.prototype.includeLearner = function(role) {
    return this.includeRole(role, "learner");
};

/**
* Incluye un rol de tipo profesor
* @param role Rol de tipo profesor a incluir
*/
Act.prototype.includeStaff = function(role) {
    return this.includeRole(role, "staff");
};

/**
* Incluye un rol del tipo especificado
* @param role Rol a incluir
* @param type Tipo de rol a incluir
*/
Act.prototype.includeRole = function(role, type) {
    if (typeof type == "undefined") {
        type = role.subtype;
    }

    if (type == "learner") {
        this.learners.push(role.id);
    } else {
        this.staff.push(role.id);
    }

    var activities = new Array();
    this.roleparts.push( {
        roleId : role.id,
        activities : activities
    });

    return activities;
};

/**
* Obtiene las actividades asociadas al rol
* @param role Rol del que se desean obtener sus actividades asociadas
*/
Act.prototype.getActivitiesForRole = function(role) {
    return this.getActivitiesForRoleId(role.id);
};

/**
* Obtiene las actividades asociadas al rol
* @param roleId Identificador del rol del que se desean obtener sus actividades asociadas
*/
Act.prototype.getActivitiesForRoleId = function(roleId) {
    for ( var i in this.roleparts) {
        if (this.roleparts[i].roleId == roleId) {
            return this.roleparts[i].activities;
        }
    }
    return null;
};

/**
 * Use this with caution!!! It gives back the old activity list, but does not
 * delete them!!!
 * Asigna las actividades a un rol
 * @param {String} roleId Identificador del rol
 * @param {Object} activities Actividades a asignar al rol
 */
Act.prototype.setActivitiesForRoleId = function(roleId, activities) {
    for ( var i in this.roleparts) {
        if (this.roleparts[i].roleId == roleId) {
            var temp = this.roleparts[i].activities;
            this.roleparts[i].activities = activities;
            return temp;
        }
    }
    return null;
};

/**
 * Transforma las actividades en un clfp
 * @param clfp Patrón en el que se desea convertir
 */
Act.prototype.makeIntoClfp = function(clfp) {
    ChangeManager.startGroup();

    for ( var i in this.roleparts) {
        var activities = this.roleparts[i].activities;
        for ( var j in activities) {
            ChangeManager.activityDeleted(activities[j]);
            delete activities[j].id;
            delete activities[j].type;

        //LearningDesign.removeActivity(activities[j]);
        }
    //        this.roleparts[i].activities = new Array();
    }

    this.type = "clfpact";
    this.clfps = [ clfp ];

    ChangeManager.endGroup();
};

/**
* Elimina uno de los CLFPs asociados
* @param index Posición del clfp a eliminar
*/
Act.prototype.removeClfpAt = function(index) {
    //ChangeManager.startGroup();

    var clfp = this.clfps[index];
    this.clfps.splice(index, 1);
    if (this.clfps.length == 0 && !this.isMainAct) {
        this.clfps = null;
        this.type = "act";

        for ( var i in this.roleparts) {
            var activities = this.roleparts[i].activities;
            for ( var j in activities) {
                IDPool.registerNewObject(activities[j]);
                activities[j].type = "activity";
                //ChangeManager.activityAdded(activities[j]);
            }
        }
    }

    //ChangeManager.endGroup();

    return clfp;
};
