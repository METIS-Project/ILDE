/**
 * Factoría del patrón TPS
 */
var TPSFactory = {
    /**
     * Crea una instancia del patrón TPS
    */
    create : function(callback, staff) {
        ChangeManager.startGroup();
        callback(new TPS(staff));
        ChangeManager.endGroup();
    }
};
/**
 * Patrón TPS
 */
var TPS = function(staff) {
    this.type = "clfp";
    this.title = i18n.get("clfps.tps.title");
    this.patternid = "tps";
    this.ownRoles = new Array();
    IDPool.registerNewObject(this);

    this.flow = new Array();
    var students = new Role(i18n.get("clfps.tps.students"), "learner");
    var pairs = new Role(i18n.get("clfps.tps.pair.role"), "learner");
    students.addChild(pairs);
    this.ownRoles.push(students);

    if (staff && staff.length > 0) {
        var staffRole = staff[0];
    } else {
        staffRole = new Role(i18n.get("roles.common.teacher"), "staff");
        this.ownRoles.push(staffRole);
    }

    this.flow = [ new Act(i18n.get("clfps.tps.think")),
    new Act(i18n.get("clfps.tps.pair")),
    new Act(i18n.get("clfps.tps.share")) ];

    /* think */

    var las = this.flow[0].includeLearner(students);
    las.push(new Activity(i18n.get("clfps.tps.think.activity"), "learning"));
    var sas = this.flow[0].includeStaff(staffRole);
    sas.push(new Activity(i18n.get("clfps.tps.think.support"), "support"));

    /* pair */

    las = this.flow[1].includeLearner(pairs);
    las.push(new Activity(i18n.get("clfps.tps.pair.activity"), "learning"));
    sas = this.flow[1].includeStaff(staffRole);
    sas.push(new Activity(i18n.get("clfps.tps.pair.support"), "support"));

    /* share */

    las = this.flow[2].includeLearner(students);
    las.push(new Activity(i18n.get("clfps.tps.share.activity"), "learning"));
    sas = this.flow[2].includeStaff(staffRole);
    sas.push(new Activity(i18n.get("clfps.tps.share.support"), "support"));

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
    ChangeManager.endGroup();
};

/**
 * Obtiene el título del clfp
 * @return Título del clfp
 */
TPS.prototype.getTitle = function() {
    return this.title;
};

/**
 * Obtiene el flujo del clfp
 * @return Flujo del clfp
 */
TPS.prototype.getFlow = function() {
    return this.flow;
};

/**
 * Obtiene el renderer del clfp
 * @return Renderer del clfp
 */
TPS.prototype.getRenderer = function() {
    return TPSRenderer;
};

/**
 * Obtiene el nombre del patrón
 * @return Nombre del patrón
 */
TPS.prototype.getPatternName = function() {
    return i18n.get("clfps.tps.title");
};

/**
 * Obtiene el menú de edición del patrón
 * @return Menú de edición del patrón
 */
TPS.prototype.getEditionMenu = function(link) {
    return null;
};

/** Clonar una instancia
 * @param idInstancia Identificador de la instancia a clonar
 * @param level Nivel en el que se encuentra la instancia a clonar
 */
TPS.prototype.clonar = function(idInstancia, level){
    var instancia = IDPool.objects[idInstancia];
    DesignInstance.copiarEstructura(instancia, instancia.idParent, instancia.id);
};

/**
 * Función que se encarga de asignar un participante a la instancia
 * @param instancia Instancia a la que se desea asignar el participante
 * @param participante Participante a asignar a la instancia
 */
TPS.prototype.asignar = function(instancia, participante){
    DesignInstance.asignacionAscendente(instancia, participante);
};

/**
 * Función que borra de una instancia un participante
 * @param instancia Instancia de la que se desea borrar el participante
 * @param participante Participante a borrar
 */
TPS.prototype.borrar = function(instancia, participante){
    DesignInstance.borradoAscendente(instancia, participante);
};

/**
 * Función que calcula los participantes disponibles para ser asignados a una instancia
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
TPS.prototype.obtainAvailableStudents = function(instanceid){
    var inMain = ClfpsCommon.isInMain(this);
    if (inMain){
        var disponibles = this.obtainAvailableStudentsMain(instanceid);
    }else{
        var disponibles = this.obtainAvailableStudentsAnidado(instanceid);
    }
    return disponibles;
};

/**
 * Función obtiene los participantes disponibles cuando el clfp no está contenido en otro
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
TPS.prototype.obtainAvailableStudentsMain = function(instanceid){
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
 * Función obtiene los participantes disponibles cuando el clfp está contenido en otro
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
TPS.prototype.obtainAvailableStudentsAnidado = function(instanceid){
    var disponibles = new Array();
    var instancia = IDPool.getObject(instanceid);
    var instProcede = DesignInstance.instanciaProcede(instancia);
    var grupoInstancia = DesignInstance.grupoInstancia(instancia);
    for (var i=0; i<instProcede.participants.length; i++){
        if (!DesignInstance.perteneceGrupo(grupoInstancia, instProcede.participants[i])){
            disponibles.push(instProcede.participants[i]);
        }
    }
    return disponibles;
};

TPS.prototype.createInitialInstances = function(){
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
    for (var i=0; i<=1; i++) {
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

    var actChild = this.getFlow()[1];
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

    var groupUpper = DesignInstance.getGrupo(roleUpper.id);
    for (n=0; n<instancias.length;n++)
    {
        groupUpper.instances[n].idParent = instancias[n].id;
    }
};

Factory.registerFactory("tps", TPS, TPSFactory);
