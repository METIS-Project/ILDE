/**
 * @class Factoría del patrón brainstorming
 */
var BrainstormingFactory = {
    /**
     * Crea una instancia del patrón brainstorming
    */
    create : function(callback, staff) {
        ChangeManager.startGroup();
        callback(new Brainstorming(staff));
        ChangeManager.endGroup();
    }
};

/**
 * @class Patrón brainstorming
 */
var Brainstorming = function(staff) {
    /**
     * Indicador de tipo
    */
    this.type = "clfp";
    /**
     * Nombre del patrón
    */
    this.title = i18n.get("clfps.brainstorming.title");
    /**
     * Identificador del patrón
    */
    this.patternid = "brainstorming";
    this.ownRoles = new Array();

    IDPool.registerNewObject(this);

    this.flow = new Array();

    var groups = new Role(i18n.get("clfps.brainstorming.group"), "learner");

    this.ownRoles.push(groups);

    if (staff && staff.length > 0) {
        var staffRole = staff[0];
    } else {
        staffRole = new Role(i18n.get("roles.common.teacher"), "staff");
        this.ownRoles.push(staffRole);
    }

    var act = new Act(i18n.get("clfps.brainstorming.title"));
    this.flow.push(act);

    var las = act.includeRole(groups);
    var activity = new Activity(i18n.get("clfps.brainstorming.title"),
        "learning");
    las.push(activity);

    var sas = act.includeStaff(staffRole);
    activity = new Activity(i18n.get("clfps.brainstorming.support"), "support");
    sas.push(activity);

    ChangeManager.startGroup();
    for ( var i = 0; i < this.flow.length; i++) {
        act = this.flow[i];
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
Brainstorming.prototype.getTitle = function() {
    return this.title;
};

/**
 * Obtiene el flujo del clfp
 * @return Flujo del clfp
 */
Brainstorming.prototype.getFlow = function() {
    return this.flow;
};

/**
 * Obtiene el renderer del clfp
 * @return Renderer del clfp
 */
Brainstorming.prototype.getRenderer = function() {
    return BrainstormingRenderer;
};

/**
 * Obtiene el nombre del patrón
 * @return Nombre del patrón
 */
Brainstorming.prototype.getPatternName = function() {
    return i18n.get("clfps.brainstorming.title");
};

/**
 * Obtiene el menú de edición del patrón
 * @return Menú de edición del patrón
 */
Brainstorming.prototype.getEditionMenu = function(link) {
    return null;
};

/**
 * Función que calcula los participantes disponibles para ser asignados a una instancia
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
Brainstorming.prototype.obtainAvailableStudents = function(instanceid){
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
Brainstorming.prototype.obtainAvailableStudentsMain = function(instanceid){
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
Brainstorming.prototype.obtainAvailableStudentsAnidado = function(instanceid){
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

/**
 * Función que se encarga de asignar un participante a la instancia
 * @param instancia Instancia a la que se desea asignar el participante
 * @param participante Participante a asignar a la instancia
 */
Brainstorming.prototype.asignar = function(instancia, participante){
    DesignInstance.asignacion(instancia, participante);
};

/**
 * Función que borra de una instancia un participante
 * @param instancia Instancia de la que se desea borrar el participante
 * @param participante Participante a borrar
 */
Brainstorming.prototype.borrar = function(instancia, participante){
    DesignInstance.borradoAscendente(instancia, participante);
};

Brainstorming.prototype.createInitialInstances = function(){
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
    g = new Group(roleUpper.id);
    DesignInstance.addGroup(g);
    //Añadir una instancia para el grupo/rol
    gi = new GroupInstance(roleUpper.title);
    g.addInstance(gi);
    //Se añaden las restantes instancias
    for (var n=1; n<instancias.length;n++)
    {
        gi = new GroupInstance(roleUpper.title);
        g.addInstance(gi);
    }
    for (n=0; n<instancias.length;n++)
    {
        g.instances[n].idParent = instancias[n].id;
    }
};

Brainstorming.prototype.getAvailableGroupPatterns = function(type, actid){   
    
    var availables = new Array();
    //Sólo hay un grupo. No pueden aplicarse patrones de número de grupos
    availables["gn"] = new Array();
    //Los participantes pueden distribuirse de forma aleatoria
    availables["pa"] = new Array("groupparticipantsdistributepattern");
    
    var factories = GroupPatternManager.patternFactories[type];
    var patterns = new Array();
    for (var i = 0; i < factories.length; i++){
        if (availables[type].indexOf(factories[i].getId())!=-1){
            patterns.push(factories[i]);
        }
    }   
    return patterns;      
};

Brainstorming.prototype.canDeleteInstance = function(roleid, instanceId){
    //Sólo hay una instancia en un único grupo y no puede ser borrada
    return false;
};

Factory.registerFactory("brainstorming", Brainstorming, BrainstormingFactory);
