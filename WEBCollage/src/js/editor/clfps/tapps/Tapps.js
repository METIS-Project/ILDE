/**
 * @class Factoría del patrón Tapps
 */
var TappsFactory = {
    /**
     * Crea una instancia del patrón Tapps
    */
    create : function(callback, staff) {
        ChangeManager.startGroup();
        callback(new Tapps(staff));
        ChangeManager.endGroup();
    },

    /**
     * Crea un problema
    */
    createProblem : function(data) {
        data.clfp.createProblem();
    }
};

/**
 * Patrón Tapps
 */
var Tapps = function(staff) {
    /**
     * Indicador de tipo
    */
    this.type = "clfp";
    /**
     * Nombre del patrón
    */
    this.title = i18n.get("clfps.tapps.title");
    /**
     * Identificador del patrón
    */
    this.patternid = "tapps";
    this.ownRoles = new Array();

    IDPool.registerNewObject(this);

    this.flow = new Array();

    if (staff && staff.length > 0) {
        var staffRole = staff[0];
    } else {
        staffRole = new Role(i18n.get("roles.common.teacher"), "staff");
        this.ownRoles.push(staffRole);
    }

    var roles = [ new Role(i18n.get("clfps.tapps.pair"), "learner"),
    new Role(i18n.get("clfps.tapps.studentA"), "learner"),
    new Role(i18n.get("clfps.tapps.studentB"), "learner") ];

    this.roleAid = roles[1].id;
    this.roleBid = roles[2].id;
    this.staffRoleId = staffRole.id;

    roles[0].addChild(roles[1]);
    roles[0].addChild(roles[2]);

    this.ownRoles.push(roles[0]);

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
    this.createProblem();
    this.createProblem();
    ChangeManager.endGroup();
};

/**
 * Crea un problema
 */
Tapps.prototype.createProblem = function() {
    var index = this.flow.length;
    var even = index % 2 == 0;
    var activities = [ new Activity(i18n.get("clfps.tapps.solve"), "learning"),
    new Activity(i18n.get("clfps.tapps.listen"), "learning") ];

    var problemName = dojo.string.substitute(i18n.get("clfps.tapps.problem"),
        [ (index + 1) ]);
    var act = new Act(problemName);
    this.flow.push(act);
    var activityA = even ? activities[0] : activities[1];
    var activityB = even ? activities[1] : activities[0];

    var supportActivity = new Activity(i18n.get("clfps.tapps.support"),
        "support");

    ChangeManager.startGroup();
    act.includeRole(IDPool.getObject(this.roleAid)).push(activityA);
    ChangeManager.activityAdded(activityA);
    act.includeRole(IDPool.getObject(this.roleBid)).push(activityB);
    ChangeManager.activityAdded(activityB);
    act.includeRole(IDPool.getObject(this.staffRoleId)).push(supportActivity);
    ChangeManager.activityAdded(supportActivity);
    ChangeManager.endGroup();
};

/**
 * Elimina un problema
 */
Tapps.prototype.removeProblem = function(index) {
    if (typeof index == "undefined") {
        index = this.flow.length - 1;
    }

    if (index >= 0 && index < this.flow.length) {
        this.flow.splice(index, 1);
        /* warning!! Need to delete things! */
        var idA = this.roleAid;
        var idB = this.roleBid;
        for ( var i = index; i < this.flow.length; i++) {
            var act = this.flow[i];
            /* next line supposedly switches the activities */
            act.setActivitiesForRoleId(idA, act.setActivitiesForRoleId(idB, act.getActivitiesForRoleId(idA)));
        }
    }
};

/**
 * Obtiene el título del clfp
 * @return Título del clfp
 */
Tapps.prototype.getTitle = function() {
    return this.title;
};

/**
 * Obtiene el flujo del clfp
 * @return Flujo del clfp
 */
Tapps.prototype.getFlow = function() {
    return this.flow;
};

/**
 * Obtiene el renderer del clfp
 * @return Renderer del clfp
 */
Tapps.prototype.getRenderer = function() {
    return TappsRenderer;
};

/**
 * Obtiene el nombre del patrón
 * @return Nombre del patrón
 */
Tapps.prototype.getPatternName = function() {
    return i18n.get("clfps.tapps.title");
};

/**
 * Obtiene el menú de edición del patrón
 * @return Menú de edición del patrón
 */
Tapps.prototype.getEditionMenu = function(link) {
    var obj = IDPool.getObject(link.id);
    if (obj && obj.type == "act") {
        return [ {
            label : i18n.get("clfps.tapps.edit.add"),
            onClick : function(data) {
                TappsFactory.createProblem(data);
            },
            data : {
                clfp : this
            },
            help: i18n.get("help.clfps.tapps.edit.add")
        } ];
    }

    return null;
};

/**
 * Función que se encarga de asignar un participante a la instancia
 * @param instancia Instancia a la que se desea asignar el participante
 * @param participante Participante a asignar a la instancia
 */
Tapps.prototype.asignar = function(instancia, participante){
    DesignInstance.asignacionAscendente(instancia, participante);
};

/**
 * Función que borra de una instancia un participante
 * @param instancia Instancia de la que se desea borrar el participante
 * @param participante Participante a borrar
 */
Tapps.prototype.borrar = function(instancia, participante){
    DesignInstance.borradoAscendente(instancia, participante);
};

/**
 * Función que calcula los participantes disponibles para ser asignados a una instancia
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
Tapps.prototype.obtainAvailableStudents = function(instanceid){
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
Tapps.prototype.obtainAvailableStudentsMain = function(instanceid){
    var disponibles = new Array();
    var grupoA = DesignInstance.getGrupo(this.roleAid);
    var grupoB = DesignInstance.getGrupo(this.roleBid);
    for (var i=0;i<DesignInstance.data.participants.length;i++){
        //Para que el participante esté disponible debe de ser de tipo student y no estar asignado a ninguna instancia de los dos tipos de grupos existentes
        if (DesignInstance.data.participants[i].participantType=="student" && !DesignInstance.perteneceGrupo(grupoA, DesignInstance.data.participants[i].participantId) && !DesignInstance.perteneceGrupo(grupoB, DesignInstance.data.participants[i].participantId)){
            disponibles.push(DesignInstance.data.participants[i].participantId);
        }
    }
    return disponibles;
};

/**
 * Función obtiene los participantes disponibles cuando el clfp está contenido en otro
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
Tapps.prototype.obtainAvailableStudentsAnidado = function(instanceid){
    var disponibles = new Array();
    var instancia = IDPool.getObject(instanceid);
    var instProcede = DesignInstance.instanciaProcede(instancia);
    var grupoA = DesignInstance.getGrupo(this.roleAid);
    var grupoB = DesignInstance.getGrupo(this.roleBid);
    for (var i=0; i<instProcede.participants.length; i++){
        //Para que el participante esté disponible no puede estar asignado a ninguna instancia de los dos tipos de grupos existentes
        if (!DesignInstance.perteneceGrupo(grupoA, instProcede.participants[i]) && !DesignInstance.perteneceGrupo(grupoB, instProcede.participants[i])){
            disponibles.push(instProcede.participants[i]);
        }
    }
    return disponibles;
};

/**
 * Clona un par de instancias asociadas, una de cada grupo
 * @param idInstancia Identificador de una de las dos instancias a clonar
 * @param level Nivel en el que se encuentra la instancia a clonar
 */
Tapps.prototype.clonar = function(idInstancia, level){
    var instancia = IDPool.objects[idInstancia];
    var grupoA = DesignInstance.getGrupo(this.roleAid);
    var grupoB = DesignInstance.getGrupo(this.roleBid);
    var posicion = DesignInstance.posicionGrupo(grupoA, instancia);   
    if (posicion!=-1){
        //Pertenece al grupo A
        DesignInstance.copiarEstructura(instancia, instancia.idParent, instancia.id);
        //La instancia del grupo B está en la misma posición de su grupo
        var instanciaB = grupoB.instances[posicion];
        DesignInstance.copiarEstructura(instanciaB, instanciaB.idParent, instanciaB.id);
    }
    else{
        //Debe pertenecer al grupo B
        DesignInstance.copiarEstructura(instancia, instancia.idParent, instancia.id);
        posicion = DesignInstance.posicionGrupo(grupoB, instancia);
        if (posicion!=-1){
            //La instancia del grupo A está en la misma posición de su grupo
            var instanciaA = grupoA.instances[posicion];
            DesignInstance.copiarEstructura(instanciaA, instanciaA.idParent, instanciaA.id);
        }
    }
};

/**
 * Borra un par de instancias asociadas, una de cada grupo
 * @param instancia Una de las dos instancias a borrar
 */
Tapps.prototype.borrarInstancia = function(instancia){
    var grupoA = DesignInstance.getGrupo(this.roleAid);
    var grupoB = DesignInstance.getGrupo(this.roleBid);
    var posicion = DesignInstance.posicionGrupo(grupoA, instancia);   
    if (posicion!=-1){
        //Pertenece al grupo A
        DesignInstance.borrarEstructura(instancia);
        //La instancia del grupo B está en la misma posición de su grupo
        var instanciaB = grupoB.instances[posicion];
        DesignInstance.borrarEstructura(instanciaB);
    }
    else{
        posicion = DesignInstance.posicionGrupo(grupoB, instancia);
        if (posicion!=-1){
            //Debe pertenecer al grupo B
            DesignInstance.borrarEstructura(instancia);
            //La instancia del grupo A está en la misma posición de su grupo
            var instanciaA = grupoA.instances[posicion];
            DesignInstance.borrarEstructura(instanciaA);
        }
    }
};

Tapps.prototype.createInitialInstances = function(){
    var instancias = new Array();
    var roleUpper = IDPool.getObject(this.ownRoles[1].id);
    //Obtenemos el número de instancias del rol del que depende en otro patrón (si depende de alguno)
    var parentGroup=LearningDesign.findRoleParentOfRole(roleUpper);
    if (parentGroup!=null && ClfpsCommon.isInMain(this)==false)
    {
        instancias = DesignInstance.instanciasGrupo(parentGroup.id);
    }   
    g = new Group(roleUpper.id);
    DesignInstance.addGroup(g);
    //Añadir una instancia por cada grupo/rol
    gi = new GroupInstance(roleUpper.title);
    g.addInstance(gi);
    //Se añaden las restantes instancias
    for (var n=1; n<instancias.length;n++)
    {
        gi = new GroupInstance(roleUpper.title);
        g.addInstance(gi);
    }
    
    //teacher
    var teacherRole = IDPool.getObject(this.ownRoles[0].id);
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
    var act = this.getFlow()[0];
    for (var i=0; i<=1; i++) {
        var role = IDPool.getObject(act.learners[i]);
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
    for (var i=0; i<=1; i++) {
        var actChild = this.getFlow()[0];
        var roleChild = IDPool.getObject(actChild.learners[i]);
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

Tapps.prototype.getAvailableGroupPatterns = function(type, actid){ 
    var availables = new Array();
    
    availables["gn"] = new Array("fixednumbergroups", "fixedsizegroups");
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

Tapps.prototype.canDeleteInstance = function(roleid, instanceId){
    var instance = IDPool.getObject(instanceId);
    if(DesignInstance.instanciasGrupoMismoPadre(roleid, instance.idParent).length > 1) {
        var letDelete = true;
    } else {
        letDelete = false;
    }
    return letDelete;;
};


Factory.registerFactory("tapps", Tapps, TappsFactory);
