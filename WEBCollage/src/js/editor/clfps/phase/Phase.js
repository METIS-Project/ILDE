/**
 * @class Factoría del patrón Phase
 */

/*global ChangeManager, i18n, Role, Act, Activity, Group, IDPool, DesignInstance, GroupInstance, PhaseRenderer, ClfpsCommon, LearningDesign */

/**
 * @class Patrón Phase
 */
var Phase = function(staff, allowNewGroups) {
    /**
     * Indicador de tipo
     */
    this.type = "clfp";
    /**
     * Nombre del patrón
     */
    this.title = i18n.get("clfps.phase.title");
    /**
     * Identificador del patrón
     */
    //this.patternid = allowNewGroups ? "blankphase2" : "blankphase";
    this.patternid = "blankphase";
    this.ownRoles = [];
    IDPool.registerNewObject(this);
    this.allowNewGroups = allowNewGroups;

    var students = new Role(i18n.get("roles.common.rol"), "learner");
    this.studentsId = students.id;
    this.ownRoles.push(students);
    this.playRoleIds = [];
    this.playGroupIds = [];

    if(staff && staff.length > 0) {
        var staffRole = staff[0];
    } else {
        staffRole = new Role(i18n.get("roles.common.teacher"), "staff");
        this.ownRoles.push(staffRole);
    }

    this.flow = [new Act(i18n.get("clfps.phase.title"))];

    /* information */
    this.flow[0].includeStaff(staffRole).push(new Activity(i18n.get("clfps.phase.support"), "support"));

    ChangeManager.startGroup();
    for(var i = 0; i < this.flow.length; i++) {
        var act = this.flow[i];
        for(var j = 0; j < act.roleparts.length; j++) {
            var activities = act.roleparts[j].activities;
            for(var k = 0; k < activities.length; k++) {
                ChangeManager.activityAdded(activities[k]);
            }
        }
    }

    this.createRole(true);
    /*if(this.allowNewGroups) {
     this.createRole(true);
     }*/

    ChangeManager.endGroup();
};
/**
 * Crea un rol
 */
Phase.prototype.createRole = function(initial) {
    ChangeManager.startGroup();

    var parent = IDPool.getObject(this.studentsId);
    var newRole = new Role(i18n.get("clfps.phase.newgrouptype"), "learner");
    LearningDesign.addRoleTo(newRole, parent);
    
    this.playRoleIds.push(newRole.id);
    //this.ownRoles.push(newRole);
    var activity = new Activity(i18n.get("clfps.phase.activity"), "learning");
    this.flow[0].includeLearner(newRole).push(activity);

    if(!initial) {
        var act = this.getFlow()[0];
        var role = IDPool.getObject(act.learners[act.learners.length - 1]);
        var g = new Group(role.id);
        DesignInstance.addGroup(g);

        var roleParent = LearningDesign.findRoleParentOfRole(role);
        if(roleParent !== null) {
            var instancias = DesignInstance.instanciasGrupo(roleParent.id);
            for(var i = 0; i < instancias.length; i++) {
                //Añadir una instancia por cada grupo/rol
                var gi = new GroupInstance(role.title);
                g.addInstance(gi);
                //Se especifica el padre de la instancia
                g.instances[i].idParent = instancias[i].id;
            }
        }

    }

    ChangeManager.activityAdded(activity);
    ChangeManager.endGroup();
};

Phase.prototype.removeRole = function(role) {
    ChangeManager.startGroup();
    
    DesignInstance.deleteGroup(role.id);
    LearningDesign.removeRoleFromAct(role, this.flow[0]);
    this.playRoleIds.splice(this.playRoleIds.indexOf(role.id), 1);
    
    var parent = IDPool.getObject(this.studentsId);
    LearningDesign.removeRoleFromParent(role, parent);

    ChangeManager.endGroup();
};
/**
 * Obtiene el título del clfp
 * @return Título del clfp
 */
Phase.prototype.getTitle = function() {
    return this.title;
};
/**
 * Obtiene el flujo del clfp
 * @return Flujo del clfp
 */
Phase.prototype.getFlow = function() {
    return this.flow;
};
/**
 * Obtiene el renderer del clfp
 * @return Renderer del clfp
 */
Phase.prototype.getRenderer = function() {
    return PhaseRenderer;
};
/**
 * Obtiene el nombre del patrón
 * @return Nombre del patrón
 */
Phase.prototype.getPatternName = function() {
    return i18n.get("clfps.phase.title");
};
/**
 * Obtiene el menú de edición del patrón
 * @return Menú de edición del patrón
 */
Phase.prototype.getEditionMenu = function(link) {
    //if(this.allowNewGroups) {
    var obj = link.role ? IDPool.getObject(link.role) : IDPool.getObject(link.id);

    if(obj && obj.type == "role" && this.getFlow()[0].type == "act") {
        if(this.playRoleIds.indexOf(obj.id) >= 0) {
            var items = [{
                label : i18n.get("clfps.phase.edit.addgroup"),
                icon : "add",
                onClick : function(data) {
                    data.clfp.createRole();
                },
                data : {
                    clfp : this
                },
                help : i18n.get("help.clfps.phase.edit.addrole")
            }];

            if(this.playRoleIds.length > 1) {
                items.push({
                    label : i18n.getReplaced1("clfps.phase.edit.removegroup", obj.getTitle()),
                    icon : "delete",
                    onClick : function(data) {
                        data.clfp.removeRole(data.role);
                    },
                    data : {
                        clfp : this,
                        role: obj
                    },
                    help : i18n.get("help.clfps.phase.edit.removegroup")
                });
            }
            return items;
        }
    }
    //}
    return null;
};
/**
 * Clonar una instancia
 * @param idInstancia Identificador de la instancia a clonar
 * @param level Nivel en el que se encuentra la instancia a clonar
 */
Phase.prototype.clonar = function(idInstancia, level) {
    var instancia = IDPool.objects[idInstancia];
    DesignInstance.copiarEstructura(instancia, instancia.idParent, instancia.id);
};
/**
 * Función que calcula los participantes disponibles para ser asignados a una instancia
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
Phase.prototype.obtainAvailableStudents = function(instanceid) {
    var inMain = ClfpsCommon.isInMain(this);
    var disponibles = inMain ? this.obtainAvailableStudentsMain(instanceid) : this.obtainAvailableStudentsAnidado(instanceid);
    return disponibles;
};
/**
 * Función que obtiene los participantes disponibles cuando el clfp no está contenido en otro
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
Phase.prototype.obtainAvailableStudentsMain = function(instanceid) {
    var disponibles = [];
    var instance = IDPool.getObject(instanceid);
    var grupoInstancia = DesignInstance.grupoInstancia(instance);
    for(var i = 0; i < DesignInstance.data.participants.length; i++) {
        if(DesignInstance.data.participants[i].participantType == "student" && !DesignInstance.perteneceGrupo(grupoInstancia, DesignInstance.data.participants[i].participantId)) {
            disponibles.push(DesignInstance.data.participants[i].participantId);
        }
    }
    return disponibles;
};
/**
 * Función que obtiene los alumnos disponibles para la fase de definición de roles del patrón simulation anidado
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
Phase.prototype.obtainAvailableStudentsAnidado = function(instanceid) {
    var asignadosOtras = ClfpsCommon.obtainAssignedStudentsOthers(instanceid);
    var disponibles = [];
    var instancia = IDPool.getObject(instanceid);
    if(instancia.idParent) {
        var instParent = IDPool.getObject(instancia.idParent);
        if(instParent.idParent) {
            instParent = IDPool.getObject(instParent.idParent);
            for(var q = 0; q < instParent.participants.length; q++) {
                if(!DesignInstance.perteneceInstancia(instancia, instParent.participants[q])) {
                    var encontrado = false;
                    for(var k = 0; k < asignadosOtras.length; k++) {
                        if(instParent.participants[q] == asignadosOtras[k]) {
                            encontrado = true;
                        }
                    }
                    if(!encontrado) {
                        disponibles.push(instParent.participants[q]);
                    }
                }
            }
        }
    }
    return disponibles;
};
/**
 * Función que indica si un participante pertenece a algún otro grupo de simulación diferente
 * @param participante Participante que se desea conocer si pertenece a algún otro grupo de simulación diferente
 * @param gi Grupo de la instancia a la que pertenece el participante
 * @return Booleano que indica si el participante pertenece a algún grupo de simulación diferente
 */
Phase.prototype.perteneceAlgunGrupoSimulacionDiferente = function(participante, gi) {
    for(var g = 0; g < this.flow[2].learners.length; g++) {
        var grupo = DesignInstance.getGrupo(this.flow[2].learners[g]);
        if(grupo.roleid != gi.roleid && DesignInstance.perteneceGrupo(g, participante) == true) {
            return true;
        }
    }
    return false;
};
/**
 * Función que borra de una instancia un participante
 * @param instancia Instancia de la que se desea borrar el participante
 * @param participante Participante a borrar
 */
Phase.prototype.borrar = function(instancia, participante) {
    var p = DesignInstance.getParticipant(participante);
    DesignInstance.borradoAscendente(instancia, participante);
};
/**
 * Función que devuelve el número total de instancias que hay en un determinado nivel del patrón
 * @param level Nivel del patrón del que se desea conocer el número de instancias
 */
Phase.prototype.numberOfInstances = function(level) {
    var totalInstancias = 0;
    for(var i = 0; i < this.flow[level].learners.length; i++) {
        totalInstancias = totalInstancias + DesignInstance.instanciasGrupo(this.flow[level].learners[i]).length;
    }
    return totalInstancias;
};
/**
 * Función que devuelve el número total de instancias que hay en un determinado nivel del patrón y que proceden de una instancia dada
 * @param level Nivel del patrón del que se desea conocer el número de instancias
 * @param idInstancia Identificador de la instancia de la que deben proceder
 * @return Instancias del nivel que proceden de una dada
 */
Phase.prototype.numberOfInstancesProcedenInstancia = function(level, idInstancia) {
    var totalInstancias = 0;
    for(var i = 0; i < this.flow[level].learners.length; i++) {
        totalInstancias = totalInstancias + DesignInstance.instanciasGrupoProcedenInstancia(this.flow[level].learners[i], idInstancia).length;
    }
    return totalInstancias;
};
/**
 * Función que se encarga de asignar un participante a la instancia
 * @param instancia Instancia a la que se desea asignar el participante
 * @param participante Participante a asignar a la instancia
 */
Phase.prototype.asignar = function(instancia, participante) {
    DesignInstance.asignacionAscendente(instancia, participante);
};

Phase.prototype.createInitialInstances = function() {
    var instancias = new Array();
    var actUpper = this.getFlow()[0];
    var roleUpper = IDPool.getObject(this.studentsId);
    //Obtenemos el número de instancias del rol del que depende en otro patrón (si depende de alguno)
    var parentGroup = LearningDesign.findRoleParentOfRole(roleUpper);
    if(parentGroup !== null && ClfpsCommon.isInMain(this) == false) {
        instancias = DesignInstance.instanciasGrupo(parentGroup.id);
    }
    //teacher
    var teacherRole = IDPool.getObject(actUpper.staff[0]);
    var g = new Group(teacherRole.id);
    DesignInstance.addGroup(g);
    //Añadir una única instancia para el grupo del profesor
    var gi = new GroupInstance(teacherRole.title);
    g.addInstance(gi);
    var roleParent = LearningDesign.findRoleParentOfRole(teacherRole);
    if(roleParent != null) {
        parentGroup = DesignInstance.getGrupo(roleParent.id);
        gi.idParent = parentGroup.instances[0].id;
    }
    g = new Group(roleUpper.id);
    DesignInstance.addGroup(g);
    //Añadir una instancia por cada grupo/rol
    gi = new GroupInstance(roleUpper.title);
    g.addInstance(gi);
    //Se añaden las restantes instancias
    for(var n = 1; n < instancias.length; n++) {
        gi = new GroupInstance(roleUpper.title);
        g.addInstance(gi);
    }

    var act = this.getFlow()[0];
    var role = IDPool.getObject(act.learners[0]);
    g = new Group(role.id);
    DesignInstance.addGroup(g);
    //Añadir una instancia por cada grupo/rol
    gi = new GroupInstance(role.title);
    g.addInstance(gi);
    //Se añaden las restantes instancias
    for(var n = 1; n < instancias.length; n++) {
        gi = new GroupInstance(role.title);
        g.addInstance(gi);
    }
    roleParent = LearningDesign.findRoleParentOfRole(role);
    if(roleParent !== null) {
        var groupParent = DesignInstance.getGrupo(roleParent.id);
        //Se especifica el padre de la instancia
        g.instances[0].idParent = groupParent.instances[0].id;
        //Si se han creado instancias adicionales se especifica su padre
        for( n = 1; n < instancias.length; n++) {
            g.instances[n].idParent = groupParent.instances[n].id;
        }
    }

    var groupUpper = DesignInstance.getGrupo(roleUpper.id);
    for( n = 0; n < instancias.length; n++) {
        groupUpper.instances[n].idParent = instancias[n].id;
    }
};
var PhaseFactory = {
    /**
     * Crea una instancia del patrón Phase
     */
    create : function(callback, staff) {
        ChangeManager.startGroup();
        callback(new Phase(staff, true));
        ChangeManager.endGroup();
    }
};

Factory.registerFactory("blankphase", Phase, PhaseFactory);
