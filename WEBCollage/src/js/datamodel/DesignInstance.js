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
 * @class Proporciona funcionalidad para gestión de grupos, instancias de grupos y participantes en una instancia de diseño
 *
 */
var DesignInstance = {

    /**
     * Contiene la información relativa a la instancia del diseño
     */
    data : null,

    /**
     * Función de inicialización
     */
    init : function() {
    },

    /**
     * Borra los campos que contienen la información relativa a la instancia del diseño
     */
    clear : function() {
        this.data = {
            instObj : new InstObj("", ""), //Instalación seleccionada
            lmsType : {},
            lmsObj : new LmsObj("", ""), //LMS Elegido
            classObj : new ClassObj("", ""), //Clase seleccionada
            participants : [], //Participantes
            groups : [], //Grupos
            instanceid : {},
            vleTools : [] //Herramientas del VLE
        };
        this.init();
    },

    /**
     * Carga la información relativa a la instancia del diseño
     * @param data Información relativa a la instancia de diseño a cargar
     */
    load : function(data) {
        this.data = data;
        this.init();
    },

    /**
     * Asigna una instalación a la instancia del diseño
     * @param instObj Instalación a asignar
     */
    setInst : function(instObj) {
        this.data.instObj = instObj;
    },

    /**
     * Asigna un tipo de LMS a la instancia del diseño
     * @param lmsType Tipo de LMS a asignar
     */
    setLmsType : function(lmsType) {
        this.data.lmsType = lmsType;
    },

    /**
     * Asigna un LMS a la instancia del diseño
     * @param lmsObj LMS a asignar
     */
    setLms : function(lmsObj) {
        this.data.lmsObj = lmsObj;
    },

    /**
     * Asigna la clase elegida
     * @param classObj Clase a asignar
     */
    setClass : function(classObj) {
        this.data.classObj = classObj;
    },

    /**
     * Añade un participante a la lista
     * @param participant Participante a añadir
     */
    addParticipant : function(participant) {
        this.data.participants.push(participant);
    },

    /**
     * Obtiene un participante de la lista
     * @param idParticipant Identificador del participante que se desea obtener
     * @return El participante o null si no se encuentra
     */
    getParticipant : function(idParticipant) {
        for(var i = 0; i < this.data.participants.length; i++) {
            if(this.data.participants[i].participantId == idParticipant) {
                return this.data.participants[i];
            }
        }
        return null;
    },

    /**
     * Indica si el participante pertenece a la lista
     * @param participant Participante del que se desea conocer si pertenece a la lista
     * @return booleano que indica si existe o no el participante
     */
    existsParticipant : function(participant) {
        for(var i = 0; i < this.data.participants.length; i++) {
            if(participant.participantId == this.data.participants[i].participantId) {
                return true;
            }
        }
        return false;
    },

    /**
     * Indica si el participante está asignado a alguna instancia
     */
    asignedAnyInstance : function(participant) {
        for(var i = 0; i < this.data.groups.length; i++) {

            if(DesignInstance.perteneceGrupo(this.data.groups[i], participant)) {
                return true;
            }
        }
        return false;
    },

    /**
     * Borra el participante de todas las instancias en las que esté asignado
     */
    deleteFromAllInstances : function(participant) {
        for(var i = 0; i < this.data.groups.length; i++) {
            for(var j = 0; j < this.data.groups[i].instances.length; j++) {
                DesignInstance.borrarParticipante(this.data.groups[i].instances[j], participant);
            }
        }
    },

    changeType : function(id) {
        for(var i = 0; i < this.data.participants.length; i++) {
            if(id == this.data.participants[i].participantId) {
                if(this.data.participants[i].participantType == "student") {
                    this.data.participants[i].participantType = "teacher";
                } else {
                    this.data.participants[i].participantType = "student";
                }
                return true;
            }
        }
        return false;
    },

    /**
     * Borra la lista de participantes de la clase
     */
    deleteParticipants : function() {
        this.data.participants = new Array();
    },

    /**
     * Añade un nuevo grupo
     * @param group Grupo que se desea añadir
     */
    addGroup : function(group) {
        this.data.groups.push(group);
    },

    /**
     * Obtiene las instancias de un grupo/rol que son hijas de una instancia de un grupo/rol dada
     * @param instance Instancia de la que se desean obtener sus hijas
     * @return Array con las hijas
     */
    obtenerInstanciasHijas : function(instance) {
        var lista = new Array();
        for(var i in DesignInstance.data.groups) {
            for(var j in DesignInstance.data.groups[i].instances) {
                if(DesignInstance.data.groups[i].instances[j].idParent == instance.id) {
                    lista.push(DesignInstance.data.groups[i].instances[j]);
                }
            }
        }
        return lista;
    },

    /**
     * Obtiene las instancias de un grupo/rol que son hijas de una instancia de un grupo/rol dada y están en el mismo clfp que este
     * @param instance Instancia de la que se desean obtener sus hijas en el mismo clfp
     * @return Array con las hijas en el mismo clfp
     */
    obtenerInstanciasHijasClfp : function(instance) {
        var idClfpInstancia = DesignInstance.clfpInstanciaGrupo(instance).id;
        var lista = [];
        for(var i in DesignInstance.data.groups) {
            for(var j in DesignInstance.data.groups[i].instances) {
                //Comprobar que es su padre y que está en el mismo clfp
                if(DesignInstance.data.groups[i].instances[j].idParent == instance.id && DesignInstance.clfpInstanciaGrupo(DesignInstance.data.groups[i].instances[j]).id == idClfpInstancia) {
                    lista.push(DesignInstance.data.groups[i].instances[j]);
                }
            }
        }
        return lista;
    },

    /**
     * Crea una copia de una instancia de un grupo y le asigna el padre especificado
     * @param instance Instancia de la que se desea crear una copia
     * @param idpadre Identificador del padre de la instancia de la que se creará la copia
     * @param idInicial Identificador de la instancia desde la que comienza la copia
     * @return Instancia creada
     */
    crearCopiaInstancia : function(instance, idpadre, idInicial) {
        var gi = new GroupInstance(instance.name);
        if(idpadre != null) {
            gi.idParent = idpadre;
        }
        var g = DesignInstance.grupoInstancia(instance);
        var encontrado = false;
        var i = 0;
        //Obtenemos la posición siguiente a aquella en la que se encuentra la instancia a copiar
        while(i < g.instances.length && !encontrado) {
            if(g.instances[i].id == instance.id) {
                encontrado = true;
            }
            i++;
        }
        //Si no es la instancia inicial se copia al final de todas las que descienden de esta
        if(instance.id != idInicial) {
            while(i < g.instances.length && DesignInstance.instanciaDesciendeInstancia(g.instances[i], idInicial)) {
                i++;
            }
            g.instances.splice(i, 0, gi);
        } else {
            g.instances.splice(i, 0, gi);
        }
        return gi;
    },

    /**
     * Función recursiva que copia la jerarquía de instancias desde la instancia indicada
     * @param instance Instancia desde la que se copia
     * @param idpadre Identificador del padre de la instancia desde la que se copia
     * @param idInicial Identificador de la instancia desde la que comienza el proceso de copia
     */
    copiarEstructura : function(instance, idpadre, idInicial) {
        var copia = DesignInstance.crearCopiaInstancia(instance, idpadre, idInicial);
        var hijas = DesignInstance.obtenerInstanciasHijas(instance);
        for(var i = 0; i < hijas.length; i++) {
            DesignInstance.copiarEstructura(hijas[i], copia.id, idInicial);
        }

    },

    /**
     * Función recursiva que copia la jerarquía de instancias desde la instancia indicada y a continuación
     * de una dada
     * @param instance Instancia desde la que se copia
     * @param idpadre Identificador del padre de la instancia desde la que se copia
     * @param idInicial Identificador de la instancia desde la que comienza el proceso de copia
     * @param idAnterior Identificador de la instancia que queremos que la preceda
     * @return Instancia creada
     */
    copiarEstructuraContinuacion : function(instance, idpadre, idInicial, idAnterior) {

        var gi = new GroupInstance(instance.name);
        if(idpadre != null) {
            gi.idParent = idpadre;
        }
        var g = DesignInstance.grupoInstancia(instance);
        var encontrado = false;
        var i = 0;
        //Obtenemos la posición siguiente a aquella en la que se encuentra la instancia que queremos sea la anterior
        while(i < g.instances.length && !encontrado) {
            if(g.instances[i].id == idAnterior) {
                encontrado = true;
            }
            i++;
        }
        g.instances.splice(i, 0, gi);

        var hijas = DesignInstance.obtenerInstanciasHijas(instance);
        for( i = 0; i < hijas.length; i++) {
            DesignInstance.copiarEstructura(hijas[i], gi.id, idInicial);
        }
        return gi;
    },

    /**
     * Función recursiva que copia la jerarquía de instancias desde la instancia indicada.
     * No se crea una copia en el mismo nivel
     * @param instance Instancia desde la que se copia
     */
    copiarEstructuraTop : function(instance) {
        var hijas = DesignInstance.obtenerInstanciasHijasClfp(instance);
        for(var i = 0; i < hijas.length; i++) {
            DesignInstance.copiarEstructura(hijas[i], instance.id, instance.id);
        }
    },

    /**
     * Obtiene el grupo al que pertenece una instancia
     * @param instance Instancia de la que se desea obtener su grupo
     * @return Grupo de la instancia o null si no existe
     */
    grupoInstancia : function(instance) {
        var i = 0;
        while(i < DesignInstance.data.groups.length) {
            var j = 0;
            while(j < DesignInstance.data.groups[i].instances.length) {
                if(DesignInstance.data.groups[i].instances[j].id == instance.id) {
                    return DesignInstance.data.groups[i];
                }
                j++;
            }
            i++;
        }
        return null;
    },

    /**
     * Obtiene el clfp al que pertenece una instancia
     * @param instance Instancia de la que se quiere conocer el clfp al que pertenece
     * @return Clfp de la instancia o null si no existe
     */
    clfpInstanciaGrupo : function(instance) {
        var clfps = new Array();
        for(var i in IDPool.objects) {
            var obj = IDPool.objects[i];
            if(obj.type == "clfp") {
                clfps.push(obj);
            }
        }
        var grupoInstancia = this.grupoInstancia(instance);
        for(var j = 0; j < clfps.length; j++) {
            for(var k = 0; k < clfps[j].ownRoles.length; k++) {
                var result = this.buscarInstancia(clfps[j].ownRoles[k], grupoInstancia);
                if(result != null) {
                    return clfps[j];
                }
            }
        }
        return null;
    },

    clfpRole : function(role) {
        var clfps = new Array();
        for(var i in IDPool.objects) {
            var obj = IDPool.objects[i];
            if(obj.type == "clfp") {
                clfps.push(obj);
            }
        }
        for(var j = 0; j < clfps.length; j++) {
            var roles = this.getRolesClfp(clfps[j]);
            for(var k = 0; k < roles.length; k++) {
                if(roles[k].id == role.id) {
                    return clfps[j];
                }
            }
        }
        return null;
    },

    getRolesClfp : function(clfp) {
        var roles = new Array();
        for(var r = 0; r < clfp.ownRoles.length; r++) {
            roles.push(clfp.ownRoles[r]);
            this.getRolesChildren(clfp.ownRoles[r].children, roles);
        }
        return roles;
    },

    getRolesChildren : function(children, roles) {
        for(var c = 0; c < children.length; c++) {
            roles.push(children[c]);
            this.getRolesChildren(children[c].children, roles);
        }
    },

    /**
     * Obtiene el primero de los clfps que dependen de una instancia dada
     * @param instance Instancia de la que se desea conocer el primero de los clfps que dependen de ella
     * @return Primer clfp encontrado que depende de la instancia o null si no hay ninguno
     */
    clfpDependeInstanciaGrupo : function(instance) {
        var clfpInstance = DesignInstance.clfpInstanciaGrupo(instance);
        var instHijas = DesignInstance.obtenerInstanciasHijas(instance);
        for(var i = 0; i < instHijas.length; i++) {
            var clfpHija = DesignInstance.clfpInstanciaGrupo(instHijas[i]);
            if(clfpInstance.id != clfpHija.id) {
                return clfpHija;
            }
        }
        return null;
    },

    /**
     * Obtiene todos los clfps que dependen de una instancia dada
     * @param instance Instancia de la que se desea conocer los clfps que dependen de ella
     * @return Clfp que dependen de la instancia o null si no hay ninguno
     */
    clfpDependenInstanciaGrupo : function(instance) {
        var clfps = new Array();
        var clfpInstance = DesignInstance.clfpInstanciaGrupo(instance);
        var instHijas = DesignInstance.obtenerInstanciasHijas(instance);
        for(var i = 0; i < instHijas.length; i++) {
            var clfpHija = DesignInstance.clfpInstanciaGrupo(instHijas[i]);
            if(clfpInstance.id != clfpHija.id) {
                var encontrado = false;
                for(var j = 0; j < clfps.length; j++) {
                    if(clfpHija.id == clfps[j].id) {
                        encontrado = true;
                    }
                }
                if(!encontrado) {
                    clfps.push(clfpHija);
                }
            }
        }
        return clfps;
    },

    /**
     * Busca si una instancia de un grupo es de un rol dado o de alguno de sus hijos
     * @param role Tipo de rol del que se quiere saber si lo es la instancia de grupo
     * @param grupoInstancia Instancia de grupo
     * @return true si lo es o null en caso contrario
     */
    buscarInstancia : function(role, grupoInstancia) {
        if(role.id == grupoInstancia.roleid) {
            return true;
        } else {
            for(var i = 0; i < role.children.length; i++) {
                var result = this.buscarInstancia(role.children[i], grupoInstancia);
                if(result != null) {
                    return result;
                }
            }
            return null;
        }
    },

    /**
     * Obtiene todas las instancias un grupo
     * @param idGrupo Identificador del grupo del que se desean obtener las instancias
     * @return instancias del grupo o null si no se ha encontrado el grupo
     */
    instanciasGrupo : function(idGrupo) {
        for(var i = 0; i < DesignInstance.data.groups.length; i++) {
            if(DesignInstance.data.groups[i].roleid == idGrupo) {
                return DesignInstance.data.groups[i].instances;
            }
        }
        return null;
    },

    /**
     * Obtiene todas las instancias un grupo que proceden de una dada
     * @param idGrupo Identificador del grupo del que se desean obtener las instancias
     * @param idInstancia Identificador de la instancia de la que deben proceder
     * @return instancias del grupo que proceden de una dada
     */
    instanciasGrupoProcedenInstancia : function(idGrupo, idInstancia) {
        var inst = new Array();
        for(var i = 0; i < DesignInstance.data.groups.length; i++) {
            if(DesignInstance.data.groups[i].roleid == idGrupo) {
                for(var j = 0; j < DesignInstance.data.groups[i].instances.length; j++) {
                    var instance = DesignInstance.instanciaProcede(DesignInstance.data.groups[i].instances[j]);
                    if(instance && instance.id == idInstancia) {
                        inst.push(DesignInstance.data.groups[i].instances[j]);
                    }
                }
            }
        }
        return inst;
    },

    /* Obtiene las instancias de un grupo que tienen como padre el especificado
    * Argumentos:
    *      idGrupo: identificador del grupo del que se quieren obtener las instancias
    *      idPadre: identificador del padre de las instancias
    * Retorno:
    *      instancias del grupo que tienen el mismo padre
    */

    /**
     * Obtiene todas las instancias un grupo que tienen como padre el especificado
     * @param idGrupo Identificador del grupo del que se desean obtener las instancias
     * @param idPadre Identificador del padre de las instancias
     * @return Instancias del grupo con el mismo padre
     */
    instanciasGrupoMismoPadre : function(idGrupo, idPadre) {
        var inst = new Array();
        for(var i = 0; i < DesignInstance.data.groups.length; i++) {
            if(DesignInstance.data.groups[i].roleid == idGrupo) {
                for(var j = 0; j < DesignInstance.data.groups[i].instances.length; j++) {
                    if(DesignInstance.data.groups[i].instances[j].idParent == idPadre) {
                        inst.push(DesignInstance.data.groups[i].instances[j]);
                    }
                }
            }
        }
        return inst;
    },

    /**
     * Borra la instancia del grupo al que pertenece
     * @param instance Instancia que se desea borrar
     * @return booleano que indica si la instancia ha sido encontrada y borrada
     */
    borrarInstancia : function(instance) {
        for(var i = 0; i < DesignInstance.data.groups.length; i++) {
            for(var j = 0; j < DesignInstance.data.groups[i].instances.length; j++)
                if(DesignInstance.data.groups[i].instances[j].id == instance.id) {
                    DesignInstance.data.groups[i].instances.splice(j, 1);
                    return true;
                }
        }
        return false;
    },

    /**
     * Borra una instancia y todas las que descienden de ella
     * @param instance Instancia desde la que se desea borrar
     */
    borrarEstructura : function(instance) {
        var hijas = DesignInstance.obtenerInstanciasHijas(instance);
        for(var i = 0; i < hijas.length; i++) {
            DesignInstance.borrarEstructura(hijas[i]);
        }
        DesignInstance.borrarInstancia(instance);
    },

    /**
     * Obtiene el número de instancias del grupo del nivel superior de un clfp
     * @param clfp Clfp del que se desea obtener el número de instancias
     * @return Número de instancias del grupo del nivel superior del clfp
     */
    numeroInstancias : function(clfp) {
        var act = clfp.getFlow()[clfp.flow.length - 1];
        var inst = DesignInstance.instanciasGrupo(act.learners[0]);
        return inst.length;
    },

    /**
     * Obtiene el número de instancias del grupo de un nivel dado de un clfp
     * @param clfp Clfp del que se desea obtener el número de instancias
     * @param level Nivel del que se desea obtener el número de instancias
     * @return Número de instancias del grupo del nivel indicado del clfp
     */
    numeroInstanciasNivel : function(clfp, level) {
        var act = clfp.getFlow()[clfp.levels - 1 - level];
        var inst = DesignInstance.instanciasGrupo(act.learners[0]);
        return inst.length;
    },

    /**
     * Asigna de forma ascendente un participante a las instancias
     * @param instancia Instancia a partir de la que se realiza la asignación
     * @param participante Participante a asignar
     */
    asignacionAscendente : function(instancia, participante) {
        if(!DesignInstance.perteneceInstancia(instancia, participante)) {
            instancia.participants.push(participante);
        }
        if(instancia.idParent != null) {
            var instanciaPadre = IDPool.getObject(instancia.idParent);
            this.asignacionAscendente(instanciaPadre, participante);
        }
    },

    /**
     * Asigna un participante a una instancia
     * @param instancia Instancia a la que se realiza la asignación
     * @param participante Participante a asignar
     * @return Booleano que indica si se ha asignado o no el participante (ya existía)
     */
    asignacion : function(instancia, participante) {
        if(!DesignInstance.perteneceInstancia(instancia, participante)) {
            instancia.participants.push(participante);
            return true;
        }
        return false;
    },

    /**
     * Borra un participante de la instancia
     * @param instancia Instancia de la que se desea borrar el participante
     * @param participante Participante a borrar
     * @return Booleano que indica si se ha borrado o no el participante (no se encontró)
     */
    borrarParticipante : function(instancia, participante) {
        for(var i = 0; i < instancia.participants.length; i++) {
            if(instancia.participants[i] == participante) {
                instancia.participants.splice(i, 1);
                return true;
            }
        }
        return false;
    },

    /**
     * Borra de forma ascendente participantes asignados a las instancias de los grupos dentro del mismo clfp
     * @param instancia Instancia a partir de la que se realiza el borrado
     * @param participante Participante a borrar
     */
    borradoAscendente : function(instancia, participante) {
        var encontrado = false;
        for(var i = 0; i < instancia.participants.length; i++) {
            if(instancia.participants[i] == participante) {
                instancia.participants.splice(i, 1);
                encontrado = true;
            }
        }
        if(encontrado == true) {
            var hijas = DesignInstance.obtenerInstanciasHijas(instancia);
            for(var j = 0; j < hijas.length; j++) {
                DesignInstance.borradoAscendente(hijas[j], participante);
            }
            if(instancia.idParent != null) {
                var instanciaPadre = IDPool.getObject(instancia.idParent);
                if(DesignInstance.clfpInstanciaGrupo(instancia).id == DesignInstance.clfpInstanciaGrupo(instanciaPadre).id) {
                    DesignInstance.borradoAscendente(instanciaPadre, participante);
                }
            }
        }
    },

    /**
     * Indica si el participante pertenece a la instancia
     * @param instancia Instancia
     * @param participante Participante del que se desea conocer si pertenece a la instancia
     * @return Booleano que indica si el participante pertenece o no a la instancia especificada
     */
    perteneceInstancia : function(instancia, participante) {
        for(var i = 0; i < instancia.participants.length; i++) {
            if(instancia.participants[i] == participante) {
                return true;
            }
        }
        return false;
    },

    /**
     * Indica si el participante pertenece a alguna de las instancias del grupo
     * @param grupo Grupo del que se desea saber su pertenencia
     * @param participante Participante del que se desea conocer si pertenece a alguna de las instancias del grupo
     * @return Booleano que indica si el participante pertenece o no al grupo
     */
    perteneceGrupo : function(grupo, participante) {
        for(var i = 0; i < grupo.instances.length; i++) {
            if(DesignInstance.perteneceInstancia(grupo.instances[i], participante)) {
                return true;
            }
        }
        return false;
    },

    /**
     * Obtiene el grupo con el identificador proporcionado
     * @param idGrupo Identificador del grupo que se desea obtener
     * @return El grupo con el identificador proporcionado o false si no existe
     */
    getGrupo : function(idGrupo) {
        for(var i = 0; i < DesignInstance.data.groups.length; i++) {
            if(DesignInstance.data.groups[i].roleid == idGrupo) {
                return DesignInstance.data.groups[i];
            }
        }
        return false;
    },

    /**
     * Obtiene la instancia del otro patrón de la que procede una instancia de un patrón anidado
     * @param instancia Instancia de la que se quiere obtener la instancia de la que procede
     * @return Instancia del otro patrón de la que procede o null si es una instancia que no está en un patrón anidado
     */
    instanciaProcede : function(instancia) {
        while(instancia.idParent) {
            var instPadre = IDPool.getObject(instancia.idParent);
            if(DesignInstance.clfpInstanciaGrupo(instPadre).id != DesignInstance.clfpInstanciaGrupo(instancia).id) {
                return instPadre;
            }
            instancia = instPadre;
        }
        return null;
    },
    /**
     *
     */
    parentInstances : function(clfpId) {
        var roleId = IDPool.getObject(clfpId).getFlow()[0].learners[0];
        var role = IDPool.getObject(roleId);
        var parentRole = this.grupoProcedeGrupo(role);
        return parentRole ? DesignInstance.instanciasGrupo(parentRole.id) : null;
    },
    /**
     * Obtiene el grupo del otro patrón del que procede un grupo de un patrón anidado
     * @param grupo Grupo del que se desea obtener el grupo del que procedo
     * @return Grupo del otro patrón de la que procede o null si es un grupo que no está en un patrón anidado
     */
    grupoProcedeGrupo : function(grupo) {

        while(LearningDesign.findRoleParentOfRole(grupo) != null) {
            var groupParent = IDPool.getObject(LearningDesign.findRoleParentOfRole(grupo).id);
            if(DesignInstance.clfpRole(groupParent).id != DesignInstance.clfpRole(grupo).id) {
                return groupParent;
            }
            grupo = groupParent;
        }
        return null;
    },

    deleteClfpGroups : function(clfp) {
        for(var i = 0; i < clfp.flow.length; i++) {
            if(clfp.flow[i].type == "clfpact") {
                for(var j = 0; j < clfp.flow[i].clfps.length; j++) {
                    this.deleteClfpGroups(clfp.flow[i].clfps[j]);
                }
            }
        }
        var roles = this.getRolesClfp(clfp);
        for(var j = 0; j < roles.length; j++) {
            this.deleteGroup(roles[j].id);
        }
    },

    deleteGroup : function(groupId) {
        for(var i = 0; i < DesignInstance.data.groups.length; i++) {
            if(DesignInstance.data.groups[i].roleid == groupId) {
                DesignInstance.data.groups.splice(i, 1);
                return true;
            }
        }
        return false;
    },

    /* Indica si una instancia desciende de otra instancia dentro del mismo patrón
    * Argumentos:
    *              instancia: instancia de la que se desea saber si desciende de una dada
    *              idAscendente: identificador de la instancia de la que debe descender
    * Retorno:
    *              booleano que indica si desciende o no
    */

    /**
     * Indica si una instancia desciende de otra instancia dentro del mismo patrón
     * @param instancia Instancia de la que se desea saber si desciende de una dada
     * @param idAscendente Identificador de la instancia de la que debe descender
     * @return booleano que indica si la instancia desciende de la otra en el mismo patrón
     */
    instanciaDesciendeInstancia : function(instancia, idAscendente) {
        while(instancia.idParent) {
            var instPadre = IDPool.getObject(instancia.idParent);
            if(instPadre.id == idAscendente) {
                return true;
            }
            instancia = instPadre;
        }
        return false;
    },

    /**
     * Indica la posición que ocupa una instancia en un grupo
     * @param grupo Grupo de la instancia
     * @param instancia Instancia de la que se desea conocer su posición en el grupo
     * @return Posición en el grupo o -1 si no pertenece a este
     */
    posicionGrupo : function(grupo, instancia) {
        for(var i = 0; i < grupo.instances.length; i++) {
            if(grupo.instances[i].id == instancia.id) {
                return i;
            }
        }
        return -1;
    },

    /**
     * Función que ordena los participantes de un grupo
     * Cada participante viene dado por su id.
     * La ordenación se realiza sobre el propio array
     * @param participantes Array de participantes a ordenar
     */
    participantSort : function(participantes) {
        var participantesOrdenados = new Array();
        for(var i = 0; i < participantes.length; i++) {
            participantesOrdenados.push(DesignInstance.getParticipant(participantes[i]));
        }
        participantesOrdenados.sort(natcompare);
        participantes.splice(0, participantes.length);
        for( i = 0; i < participantesOrdenados.length; i++) {
            participantes.push(participantesOrdenados[i].participantId);
        }
    }
};

/**
 * @class Instalación de LMS con la que trabaja la instancia de diseño
 */
var InstObj = function(id, name) {
    /**
     * Indentificador de la instalacón
     */
    this.id = id;
    /**
     * Nombre de la instalación
     */
    this.name = name;
};

/**
 * @class LMS de la instalación con el que trabaja la instancia de diseño
 */
var LmsObj = function(id, name) {
    /**
     * Indentificador del LMS
     */
    this.id = id;
    /**
     * Nombre del LMS
     */
    this.name = name;
};

/**
 * @class Clase con la que trabaja la instancia de diseño
 */
var ClassObj = function(id, name) {
    /**
     * Identificador de la clase
     */
    this.id = id;
    /**
     * Nombre de la clase
     */
    this.name = name;
};

