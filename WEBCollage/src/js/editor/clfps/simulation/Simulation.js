/**
 * @class Factoría del patrón simulación
 */
var SimulationFactory = {
    /**
     * Crea una instancia del patrón simulación
    */
    create : function(callback, staff) {
        ChangeManager.startGroup();
        callback(new Simulation(staff));
        ChangeManager.endGroup();
    },
    /**
     * Crea un grupo de simulación
    */
    createSimGroup : function(data) {
        data.clfp.createSimGroup();
    },
    /**
     * Crea un rol
    */
    createRole : function(data) {
        data.clfp.createRole();
    }
};

/**
 * @class Patrón simulación
 */
var Simulation = function(staff) {
    /**
     * Indicador de tipo
    */
    this.type = "clfp";
    /**
     * Nombre del patrón
    */
    this.title = i18n.get("clfps.simulation.title");
    /**
     * Identificador del patrón
    */
    this.patternid = "simulation";
    this.ownRoles = new Array();
    IDPool.registerNewObject(this);

    var students = new Role(i18n.get("clfps.sumulation.students"), "learner");
    this.studentsId = students.id;
    this.ownRoles.push(students);
    this.playRoleIds = [];
    this.playGroupIds = [];

    if (staff && staff.length > 0) {
        var staffRole = staff[0];
    }
    else {
        staffRole = new Role(i18n.get("roles.common.teacher"), "staff");
        this.ownRoles.push(staffRole);
    }
    this.flow = [ new Act(i18n.get("clfps.sumulation.information")),
    new Act(i18n.get("clfps.sumulation.defining")),
    new Act(i18n.get("clfps.sumulation.groups")),
    new Act(i18n.get("clfps.sumulation.simulation"))];

    /* information */
    this.flow[0].includeLearner(students).push(
        new Activity(i18n.get("clfps.sumulation.information.deliver"),
            "learning"));
    this.flow[0].includeStaff(staffRole).push(
        new Activity(i18n.get("clfps.sumulation.information.support"),
            "support"));

    /* role definition */
    this.flow[1].includeStaff(staffRole).push(
        new Activity(i18n.get("clfps.sumulation.defining.support"),
            "support"));

    /* sim groups */
    this.flow[2].includeStaff(staffRole)
    .push(
        new Activity(i18n.get("clfps.sumulation.groups.support"),
            "support"));

    /* simulation */
    this.flow[3].includeLearner(students).push(
        new Activity(i18n.get("clfps.sumulation.simulation.activity"),
            "learning"));
    this.flow[3].includeStaff(staffRole).push(
        new Activity(i18n.get("clfps.sumulation.simulation.support"),
            "support"));

    ChangeManager.startGroup();
    for ( var i = 0; i < this.flow.length; i++) {
        var act = this.flow[i];
        for ( var j = 0; j < act.roleparts.length; j++) {
            var activities = act.roleparts[j].activities;
            for ( var k = 0; k < activities.length; k++) {
                ChangeManager.activityAdded(activities[k]);
            }
        }
    }

    this.createRole(true);
    this.createSimGroup(true);

    ChangeManager.endGroup();
};


/**
 * Crea un rol
 */
Simulation.prototype.createRole = function(initial) {
    var parent = IDPool.getObject(this.studentsId);
    var newRole = new Role(i18n.get("clfps.sumulation.defining.role"),
        "learner");
    parent.addChild(newRole);
    this.playRoleIds.push(newRole.id);
    //this.ownRoles.push(newRole);
    var activity = new Activity(i18n.get("clfps.sumulation.defining.activity"),
        "learning");
    this.flow[1].includeLearner(newRole).push(activity);

    if (!initial){
        var act = this.getFlow()[1];
        var role = IDPool.getObject(act.learners[act.learners.length-1]);
        var g = new Group(role.id);
        DesignInstance.addGroup(g);
               
        var roleParent=LearningDesign.findRoleParentOfRole(role);
        if (roleParent!=null)
        {
            var instancias = DesignInstance.instanciasGrupo(roleParent.id); 
            for (var i=0; i< instancias.length;i++){
                //Añadir una instancia por cada grupo/rol
                var gi = new GroupInstance(role.title);
                g.addInstance(gi);
                //Se especifica el padre de la instancia
                g.instances[i].idParent = instancias[i].id;
            }
        }   
        
    }

    ChangeManager.activityAdded(activity);
};

/**
 * Crea un grupo de simulación
 */
Simulation.prototype.createSimGroup = function(initial) {
    var parent = IDPool.getObject(this.studentsId);
    var newRole = new Role(i18n.get("clfps.sumulation.groupos.role"), "learner");
    parent.addChild(newRole);
    //this.ownRoles.push(newRole);
    this.playGroupIds.push(newRole.id);
    var activity = new Activity(i18n.get("clfps.sumulation.groups.activity"),
        "learning");
    this.flow[2].includeLearner(newRole).push(activity);

    if (!initial){
        var act = this.getFlow()[2];
        var role = IDPool.getObject(act.learners[act.learners.length-1]);
        var g = new Group(role.id);
        DesignInstance.addGroup(g);
        
        var roleParent=LearningDesign.findRoleParentOfRole(role);
        if (roleParent!=null)
        {
            var instancias = DesignInstance.instanciasGrupo(roleParent.id); 
            for (var i=0; i< instancias.length;i++){
                //Añadir una instancia por cada grupo/rol
                var gi = new GroupInstance(role.title);
                g.addInstance(gi);
                //Se especifica el padre de la instancia
                g.instances[i].idParent = instancias[i].id;
            }
        }        
    }

    ChangeManager.activityAdded(activity);
};

/**
 * Obtiene el título del clfp
 * @return Título del clfp
 */
Simulation.prototype.getTitle = function() {
    return this.title;
};

/**
 * Obtiene el flujo del clfp
 * @return Flujo del clfp
 */
Simulation.prototype.getFlow = function() {
    return this.flow;
};

/**
 * Obtiene el renderer del clfp
 * @return Renderer del clfp
 */
Simulation.prototype.getRenderer = function() {
    return SimulationRenderer;
};

/**
 * Obtiene el nombre del patrón
 * @return Nombre del patrón
 */
Simulation.prototype.getPatternName = function() {
    return i18n.get("clfps.simulation.title");
};

/**
 * Obtiene el menú de edición del patrón
 * @return Menú de edición del patrón
 */
Simulation.prototype.getEditionMenu = function(link) {
    if (link.role) {
        var obj = IDPool.getObject(link.role);
    } else {
        obj = IDPool.getObject(link.id);
    }

    if (obj && obj.type == "role") {
        if (this.playRoleIds.indexOf(obj.id) >= 0 && this.getFlow()[1].type == "act") {
            return [ {
                label : i18n.get("clfps.simulation.edit.addrole"),
                icon: "add",
                onClick : function(data) {
                    SimulationFactory.createRole(data);
                },
                data : {
                    clfp : this
                },
                help: i18n.get("help.clfps.simulation.edit.addrole")
            } ];
        } else if (this.playGroupIds.indexOf(obj.id) >= 0 && this.getFlow()[2].type == "act") {
            return [ {
                label : i18n.get("clfps.simulation.edit.addgroup"),
                icon: "add",
                onClick : function(data) {
                    SimulationFactory.createSimGroup(data);
                },
                data : {
                    clfp : this
                },
                help: i18n.get("help.clfps.simulation.edit.addgroup")
            } ];
        }
    }
    return null;
};

/**
 * Clonar una instancia
 * @param idInstancia Identificador de la instancia a clonar
 * @param level Nivel en el que se encuentra la instancia a clonar
 */
Simulation.prototype.clonar = function(idInstancia, level){
    var instancia = IDPool.objects[idInstancia];
    DesignInstance.copiarEstructura(instancia, instancia.idParent, instancia.id);
};

/**
 * Función que indica si una instancia pertenece a algún grupo de la fase de definición de roles
 * @param instanceid Identificador de la instancia
 * @return booleano que indica si la instancia pertenece a algún grupo de la fase de definición de roles
 */
Simulation.prototype.isInRoleDefinition = function(instanceid){
    for (var i=0; i<this.flow[1].learners.length; i++){
        if (DesignInstance.grupoInstancia(IDPool.getObject(instanceid)).roleid==this.flow[1].learners[i]){
            return true;
        }
    }
    return false;
};

/**
 * Función que calcula los participantes disponibles para ser asignados a una instancia
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
Simulation.prototype.obtainAvailableStudents = function(instanceid){
    var inMain = ClfpsCommon.isInMain(this);
    if (inMain){
        if (this.isInRoleDefinition(instanceid)){
            var disponibles = this.obtainAvailableStudentsRoleDefinitionMain(instanceid);
        }else{
            var disponibles = this.obtainAvailableStudentsSimulationGroups(instanceid);
        }   
    }else{
        if (this.isInRoleDefinition(instanceid)){
            var disponibles = this.obtainAvailableStudentsRoleDefinitionAnidado(instanceid);
        }else{
            var disponibles = this.obtainAvailableStudentsSimulationGroups(instanceid);
        }  
    }
    return disponibles;
};

/**
 * Función que obtiene los participantes disponibles cuando el clfp no está contenido en otro
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
Simulation.prototype.obtainAvailableStudentsRoleDefinitionMain = function(instanceid){
    var disponibles = new Array();
    var instance = IDPool.getObject(instanceid);
    var grupoInstancia = DesignInstance.grupoInstancia(instance);
    for (var i=0;i<DesignInstance.data.participants.length;i++){
        if (DesignInstance.data.participants[i].participantType=="student" && !DesignInstance.perteneceGrupo(grupoInstancia, DesignInstance.data.participants[i].participantId)){
            disponibles.push(DesignInstance.data.participants[i].participantId);
        }
    }
    return disponibles;
};

/**
 * Función que obtiene los alumnos disponibles para la fase de definición de roles del patrón simulation anidado
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
Simulation.prototype.obtainAvailableStudentsRoleDefinitionAnidado = function(instanceid){
    var asignadosOtras = ClfpsCommon.obtainAssignedStudentsOthers(instanceid);
    var disponibles = new Array();
    var instancia = IDPool.getObject(instanceid);
    if (instancia.idParent){
        var instParent = IDPool.getObject(instancia.idParent);
        if (instParent.idParent){
            instParent = IDPool.getObject(instParent.idParent);
            for (var q=0; q<instParent.participants.length; q++){
                if (!DesignInstance.perteneceInstancia(instancia, instParent.participants[q])){
                    var encontrado = false;
                    for (var k=0; k<asignadosOtras.length; k++){
                        if (instParent.participants[q]== asignadosOtras[k]){
                            encontrado = true;
                        }
                    }
                    if (!encontrado){
                        disponibles.push(instParent.participants[q]);
                    }
                }
            }
        }
    }
    return disponibles;
};

/**
 * Función que obtiene los alumnos disponibles para la fase de grupos de simulación
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
Simulation.prototype.obtainAvailableStudentsSimulationGroups = function(instanceid){
    var asignadosOtras = ClfpsCommon.obtainAssignedStudentsOthers(instanceid);
    var disponibles = new Array();
    var instancia = IDPool.getObject(instanceid);
    for (var g=0; g<this.flow[1].learners.length; g++){
        var ig = DesignInstance.instanciasGrupo(this.flow[1].learners[g]);
        for (var i=0; i<ig.length; i++){
            //Sólo se consideran las que proceden de la misma instancia en el otro patrón
            if (DesignInstance.instanciaProcede(ig[i])==DesignInstance.instanciaProcede(instancia)){
                for (var p=0; p<ig[i].participants.length; p++){
                    //Comprobamos si no pertenece ya a la instancia
                    if (!DesignInstance.perteneceInstancia(instancia, ig[i].participants[p])){
                        //Comprobamos que no pertenece a otra instancia
                        var encontrado = false;
                        for (var k=0; k<asignadosOtras.length; k++){
                            if (ig[i].participants[p]== asignadosOtras[k]){
                                encontrado = true;
                            }
                        }
                        if (!encontrado){
                            var d=0;
                            while (!encontrado && d<disponibles.length){
                                if (disponibles[d]==ig[i].participants[p]){
                                    encontrado=true;
                                }
                                d++;
                            }
                            if (!encontrado){
                                disponibles.push(ig[i].participants[p]);
                            }
                        }
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
Simulation.prototype.perteneceAlgunGrupoSimulacionDiferente = function(participante, gi){
    for (var g=0; g<this.flow[2].learners.length; g++){
        var grupo = DesignInstance.getGrupo(this.flow[2].learners[g]);
        if (grupo.roleid!=gi.roleid && DesignInstance.perteneceGrupo(grupo, participante)==true)
        {
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
Simulation.prototype.borrar = function(instancia, participante){
    var p = DesignInstance.getParticipant(participante);
    if (p.participantType=="teacher" || this.isInRoleDefinition(instancia.id)){
        DesignInstance.borradoAscendente(instancia, participante);
    }else{
        var gi = DesignInstance.grupoInstancia(instancia);
        if (this.perteneceAlgunGrupoSimulacionDiferente(participante, gi)==false){
            DesignInstance.borradoAscendente(instancia, participante);
        }
        else{
            DesignInstance.borrarParticipante(instancia, participante);
        }
    }
   
};

/**
 * Función que devuelve el número total de instancias que hay en un determinado nivel del patrón
 * @param level Nivel del patrón del que se desea conocer el número de instancias
 */
Simulation.prototype.numberOfInstances = function(level){
    var totalInstancias = 0;
    for (var i=0; i<this.flow[level].learners.length; i++) {
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
Simulation.prototype.numberOfInstancesProcedenInstancia = function(level, idInstancia){
    var totalInstancias = 0;
    for (var i=0; i<this.flow[level].learners.length; i++) {
        totalInstancias = totalInstancias + DesignInstance.instanciasGrupoProcedenInstancia(this.flow[level].learners[i], idInstancia).length;
    }
    return totalInstancias;
};

/**
 * Función que se encarga de asignar un participante a la instancia
 * @param instancia Instancia a la que se desea asignar el participante
 * @param participante Participante a asignar a la instancia
 */
Simulation.prototype.asignar = function(instancia, participante){
    if (this.isInRoleDefinition(instancia.id)){
        DesignInstance.asignacion(instancia, participante);
    }else{
        DesignInstance.asignacionAscendente(instancia, participante);
    }
};

Simulation.prototype.createInitialInstances = function(){
    var instancias = new Array();
    var actUpper = this.getFlow()[0];
    var roleUpper = IDPool.getObject(actUpper.learners[0]);
    //Obtenemos el número de instancias del rol del que depende en otro patrón (si depende de alguno)
    var parentGroup=LearningDesign.findRoleParentOfRole(roleUpper);
    if (parentGroup!=null && ClfpsCommon.isInMain(this)==false)
    {
        instancias = DesignInstance.instanciasGrupo(parentGroup.id);
    }
    //teacher
    var teacherRole = IDPool.getObject(actUpper.staff[0]);
    var g = new Group(teacherRole.id);
    DesignInstance.addGroup(g);
    //Añadir una única instancia para el grupo del profesor
    var gi = new GroupInstance(teacherRole.title);
    g.addInstance(gi);
    var roleParent=LearningDesign.findRoleParentOfRole(teacherRole);
    if (roleParent!=null)
    {
        parentGroup=DesignInstance.getGrupo(roleParent.id);
        gi.idParent = parentGroup.instances[0].id;
    }
    for (var i=0; i<=2; i++) {
        var act = this.getFlow()[i];
        var role = IDPool.getObject(act.learners[0]);
        g = new Group(role.id);
        DesignInstance.addGroup(g);
        //Añadir una instancia por cada grupo/rol
        gi = new GroupInstance(role.title);
        g.addInstance(gi);
        //Se añaden las restantes instancias
        for (var n=1; n<instancias.length;n++)
        {
            gi = new GroupInstance(role.title);
            g.addInstance(gi);
        }
    }
    for (var i=1; i<=2; i++) {
        var actChild = this.getFlow()[i];
        var roleChild = IDPool.getObject(actChild.learners[0]);
        var groupChild = DesignInstance.getGrupo(roleChild.id);
        roleParent=LearningDesign.findRoleParentOfRole(roleChild);
        if (roleParent!=null)
        {
            var groupParent = DesignInstance.getGrupo(roleParent.id);
            //Se especifica el padre de la instancia
            groupChild.instances[0].idParent = groupParent.instances[0].id;
            //Si se han creado instancias adicionales se especifica su padre
            for (n=1; n<instancias.length;n++)
            {
                groupChild.instances[n].idParent = groupParent.instances[n].id;
            }
        }
    }
    var groupUpper = DesignInstance.getGrupo(roleUpper.id);
    for (n=0; n<instancias.length;n++)
    {
        groupUpper.instances[n].idParent = instancias[n].id;
    }
};

Factory.registerFactory("simulation", Simulation, SimulationFactory);
