/**
 * @class Factoría del patrón Jigsaw
 */
var JigsawFactory = {
    /**
     * Crea una instancia del patrón jigsaw
     */
    create : function(callback, staff) {
        ChangeManager.startGroup();
        callback(new Jigsaw(staff));
        ChangeManager.endGroup();
    }
};

/**
 * @class Patrón Jigsaw
 */
var Jigsaw = function(staff) {
    /**
     * Indicador de tipo
     */
    this.type = "clfp";
    /**
     * Nombre del patrón
     */
    this.title = i18n.get("clfps.jigsaw.title");
    /**
     * Identificador del patrón
     */
    this.patternid = "jigsaw";
    IDPool.registerNewObject(this);

    this.flow = new Array();

    this.ownRoles = new Array();

    var jigsawGroup = new Role(i18n.get("clfps.jigsaw.jigsaw.group"), "learner");
    this.ownRoles.push(jigsawGroup);

    var expertGroup = new Role(i18n.get("clfps.jigsaw.expert.group"), "learner");
    this.ownRoles.push(expertGroup);

    if(staff && staff.length > 0) {
        var staffRole = staff[0];
    } else {
        staffRole = new Role(i18n.get("roles.common.teacher"), "staff");
        this.ownRoles.push(staffRole);
    }

    this.flow = [new Act(i18n.get("clfps.jigsaw.individual.phase")), new Act(i18n.get("clfps.jigsaw.expert.phase")), new Act(i18n.get("clfps.jigsaw.jigsaw.phase"))];

    /* individual */

    var las = this.flow[0].includeLearner(jigsawGroup);
    las.push(new Activity(i18n.get("clfps.jigsaw.individual.activity"), "learning"));
    var sas = this.flow[0].includeStaff(staffRole);
    sas.push(new Activity(i18n.get("clfps.jigsaw.individual.support"), "support"));

    /* expert */

    las = this.flow[1].includeLearner(expertGroup);
    las.push(new Activity(i18n.get("clfps.jigsaw.expert.activity"), "learning"));
    sas = this.flow[1].includeStaff(staffRole);
    sas.push(new Activity(i18n.get("clfps.jigsaw.expert.support"), "support"));

    /* jigsaw */

    las = this.flow[2].includeLearner(jigsawGroup);
    las.push(new Activity(i18n.get("clfps.jigsaw.jigsaw.activity1"), "learning"));
    las.push(new Activity(i18n.get("clfps.jigsaw.jigsaw.activity2"), "learning"));
    sas = this.flow[2].includeStaff(staffRole);
    sas.push(new Activity(i18n.get("clfps.jigsaw.jigsaw.support"), "support"));

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
    ChangeManager.endGroup();
};
/**
 * Obtiene el título del clfp
 * @return Título del clfp
 */
Jigsaw.prototype.getTitle = function() {
    return this.title;
};
/**
 * Obtiene el flujo del clfp
 * @return Flujo del clfp
 */
Jigsaw.prototype.getFlow = function() {
    return this.flow;
};
/**
 * Obtiene el renderer del clfp
 * @return Renderer del clfp
 */
Jigsaw.prototype.getRenderer = function() {
    return JigsawRenderer;
};
/**
 * Obtiene el nombre del patrón
 * @return Nombre del patrón
 */
Jigsaw.prototype.getPatternName = function() {
    return i18n.get("clfps.jigsaw.title");
};
/**
 * Obtiene el menú de edición del patrón
 * @return Menú de edición del patrón
 */
Jigsaw.prototype.getEditionMenu = function(link) {
    return null;
};
/**
 * Clonar una instancia
 * @param idInstancia Identificador de la instancia a clonar
 * @param level Nivel en el que se encuentra la instancia a clonar
 */
Jigsaw.prototype.clonar = function(idInstancia, level) {
    var instancia = IDPool.objects[idInstancia];
    DesignInstance.copiarEstructura(instancia, instancia.idParent, instancia.id);
};
/**
 * Función que calcula los participantes disponibles para ser asignados a una instancia
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
Jigsaw.prototype.obtainAvailableStudents = function(instanceid) {
    var inMain = ClfpsCommon.isInMain(this);
    if(inMain) {
        if(DesignInstance.grupoInstancia(IDPool.getObject(instanceid)).roleid == this.flow[0].learners[0]) {
            var disponibles = this.obtainAvailableStudentsIndividualMain(instanceid);
        } else {
            var disponibles = this.obtainAvailableStudentsExpertos(instanceid);
        }
    } else {
        if(DesignInstance.grupoInstancia(IDPool.getObject(instanceid)).roleid == this.flow[0].learners[0]) {
            var disponibles = this.obtainAvailableStudentsIndividualAnidado(instanceid);
        } else {
            var disponibles = this.obtainAvailableStudentsExpertosAnidado(instanceid);
        }
    }
    return disponibles;
};
/**
 * Función obtiene los participantes disponibles cuando el clfp no está contenido en otro
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
Jigsaw.prototype.obtainAvailableStudentsIndividualMain = function(instanceid) {
    var disponibles = new Array();
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
 * Función que obtiene los alumnos disponibles para la fase individual de un jigsaw anidado
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles para la fase individual
 */
Jigsaw.prototype.obtainAvailableStudentsIndividualAnidado = function(instanceid) {
    var asignadosOtras = ClfpsCommon.obtainAssignedStudentsOthers(instanceid);
    var disponibles = new Array();
    var instancia = IDPool.getObject(instanceid);
    if(instancia.idParent) {
        var instParent = IDPool.getObject(instancia.idParent);
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
    return disponibles;
};
/**
 * Función que obtiene los alumnos disponibles para la fase de expertos de un jigsaw
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles para la fase de expertos
 */
Jigsaw.prototype.obtainAvailableStudentsExpertos = function(instanceid) {
    var asignadosOtras = ClfpsCommon.obtainAssignedStudentsOthers(instanceid);
    var disponibles = new Array();
    var instancia = IDPool.getObject(instanceid);
    var ig = DesignInstance.instanciasGrupo(this.flow[0].learners[0]);
    for(var i = 0; i < ig.length; i++) {
        for(var j = 0; j < ig[i].participants.length; j++) {
            if(!DesignInstance.perteneceInstancia(instancia, ig[i].participants[j])) {
                var encontrado = false;
                for(var k = 0; k < asignadosOtras.length; k++) {
                    if(ig[i].participants[j] == asignadosOtras[k]) {
                        encontrado = true;
                    }
                }
                if(!encontrado) {
                    disponibles.push(ig[i].participants[j]);
                }
            }
        }
    }
    return disponibles;
};
/**
 * Función que obtiene los alumnos disponibles para la fase de expertos de un jigsaw anidado
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles para la fase de expertos
 */
Jigsaw.prototype.obtainAvailableStudentsExpertosAnidado = function(instanceid) {
    var asignadosOtras = ClfpsCommon.obtainAssignedStudentsOthers(instanceid);
    var disponibles = new Array();
    var instancia = IDPool.getObject(instanceid);
    var ig = DesignInstance.instanciasGrupo(this.flow[0].learners[0]);
    for(var i = 0; i < ig.length; i++) {
        //Sólo se consideran las que proceden de la misma instancia en el otro patrón
        if(DesignInstance.instanciaProcede(ig[i]) == DesignInstance.instanciaProcede(instancia)) {
            for(var j = 0; j < ig[i].participants.length; j++) {
                if(!DesignInstance.perteneceInstancia(instancia, ig[i].participants[j])) {
                    var encontrado = false;
                    for(var k = 0; k < asignadosOtras.length; k++) {
                        if(ig[i].participants[j] == asignadosOtras[k]) {
                            encontrado = true;
                        }
                    }
                    if(!encontrado) {
                        disponibles.push(ig[i].participants[j]);
                    }
                }
            }
        }
    }
    return disponibles;
};
/**
 * Función que borra de una instancia un participante
 * @param instancia Instancia de la que se desea borrar el participante
 * @param participante Participante a borrar
 */
Jigsaw.prototype.borrar = function(instancia, participante) {
    //Es una instancia de un grupo de la fase de expertos
    if(DesignInstance.grupoInstancia(instancia).roleid == this.flow[1].learners[0]) {
        DesignInstance.borradoAscendente(instancia, participante);
    }
    //Es una instancia de un grupo de la fase individual
    else {
        this.borrarParticipanteIndividual(instancia, participante);
    }
};
/**
 * Función encargada de borrar participantes cuando el participante está en una instancia de la fase individual
 * @param instancia Instancia de la que se desea borrar el participante
 * @param participante Participante a borrar
 */
Jigsaw.prototype.borrarParticipanteIndividual = function(instancia, participante) {
    DesignInstance.borradoAscendente(instancia, participante);
    var instExpertos = DesignInstance.instanciasGrupo(this.flow[1].learners[0]);
    for(var j = 0; j < instExpertos.length; j++) {
        DesignInstance.borradoAscendente(instExpertos[j], participante);
    }
};
/**
 * Función que se encarga de asignar un participante a la instancia
 * @param instancia Instancia a la que se desea asignar el participante
 * @param participante Participante a asignar a la instancia
 */
Jigsaw.prototype.asignar = function(instancia, participante) {
    DesignInstance.asignacionAscendente(instancia, participante);
};

Jigsaw.prototype.setPatterns = function() {
/*    var act = this.getFlow()[0];
    var actExpert = this.getFlow()[1];
    var jgp = new JigsawGroupPattern(act.id, this.id, actExpert.id);
    GroupPattern.addPattern(jgp);
    var egp = new ExpertGroupPattern(actExpert.id, this.id, act.id);
    GroupPattern.addPattern(egp);
    */
};

Jigsaw.prototype.createInitialInstances = function() {
    var instancias = new Array();
    var actUpper = this.getFlow()[1];
    var roleUpper = IDPool.getObject(actUpper.learners[0]);
    //Obtenemos el número de instancias del rol del que depende en otro patrón (si depende de alguno)
    var parentGroup = LearningDesign.findRoleParentOfRole(roleUpper);
    if(parentGroup != null && ClfpsCommon.isInMain(this) == false) {
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
    for(var i = 0; i <= 1; i++) {
        var act = this.getFlow()[i];
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
    }
    for(var i = 0; i < 1; i++) {
        var actChild = this.getFlow()[i];
        var roleChild = IDPool.getObject(actChild.learners[0]);
        var groupChild = DesignInstance.getGrupo(roleChild.id);
        roleParent = LearningDesign.findRoleParentOfRole(roleChild);
        if(roleParent != null) {
            var groupParent = DesignInstance.getGrupo(roleParent.id);
            //Se especifica el padre de la instancia
            groupChild.instances[0].idParent = groupParent.instances[0].id;
            //Si se han creado instancias adicionales se especifica su padre
            for( n = 1; n < instancias.length; n++) {
                groupChild.instances[n].idParent = groupParent.instances[n].id;
            }
        }
    }
    var groupUpper = DesignInstance.getGrupo(roleUpper.id);
    for( n = 0; n < instancias.length; n++) {
        groupUpper.instances[n].idParent = instancias[n].id;
    }

    this.setPatterns();

};

Jigsaw.prototype.getAvailableGroupPatterns = function(type, aactid){
    var availables = new Array();
    availables["gn"] = new Array("fixednumbergroups", "fixedsizegroups", "expertgrouppattern", "jigsawgrouppattern");
    availables["pa"] = new Array("groupparticipantsdistributepattern", "expertgroupparticipantspattern");
    
    var factories = GroupPatternManager.patternFactories[type];
    var patterns = new Array();
    for (var i = 0; i < factories.length; i++){
        if (availables[type].indexOf(factories[i].getId())!=-1){
            patterns.push(factories[i]);
        }

    }
    return patterns;        
};                    
                    
Jigsaw.prototype.canDeleteInstance = function(roleid, instanceId){   
    var instance = IDPool.getObject(instanceId);
    if(DesignInstance.instanciasGrupoMismoPadre(roleid, instance.idParent).length > 1) {
        var letDelete = true;
    } else {
        letDelete = false;
    }
    return letDelete;
};

Factory.registerFactory("jigsaw", Jigsaw, JigsawFactory);
