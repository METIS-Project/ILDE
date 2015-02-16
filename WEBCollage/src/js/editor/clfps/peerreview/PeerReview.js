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
 * @class Factoría del patrón PeerReview
 */
var PeerReviewFactory = {
    /**
     * Crea una instancia del patrón peer review
    */
    create : function(callback, staff) {
        ChangeManager.startGroup();
        callback(new PeerReview(staff));
        ChangeManager.endGroup();
    }
};

/**
 * @class Patrón Peer Review
 */
var PeerReview = function(staff) {
    /**
     * Indicador de tipo
    */
    this.type = "clfp";
    /**
     * Nombre del patrón
    */
    this.title = i18n.get("clfps.peerreview.title");
    /**
     * Identificador del patrón
    */
    this.patternid = "peerreview";
    this.ownRoles = new Array();

    IDPool.registerNewObject(this);

    this.flow = new Array();

    if (staff && staff.length > 0) {
        var staffRole = staff[0];
    }else {
        staffRole = new Role(i18n.get("roles.common.teacher"), "staff");
        this.ownRoles.push(staffRole);
    }

    var reviewer = new Role(i18n.get("clfps.peerreview.reviewers"), "learner");
    this.ownRoles.push(reviewer);
    var group = new Role(i18n.get("clfps.peerreview.discussion.group"),
        "learner");
    this.ownRoles.push(group);

    var act = new Act(i18n.get("clfps.peerreview.review"));
    this.flow.push(act);
    var reviewActivity = new Activity(i18n
        .get("clfps.peerreview.review.activity"), "learning");
    act.includeRole(reviewer).push(reviewActivity);
    act.includeRole(staffRole)
    .push(
        new Activity(i18n.get("clfps.peerreview.review.support"),
            "support"));

    act = new Act(i18n.get("clfps.peerreview.feedback"));
    this.flow.push(act);
    var analysisActivity = new Activity(i18n
        .get("clfps.peerreview.feedback.activity"), "learning");
    act.includeRole(reviewer).push(analysisActivity);
    act.includeRole(staffRole).push(
        new Activity(i18n.get("clfps.peerreview.feedback.support"),
            "support"));

    act = new Act(i18n.get("clfps.peerreview.discussion"));
    this.flow.push(act);
    act.includeRole(group).push(
        new Activity(i18n.get("clfps.peerreview.discussion.activity"),
            "learning"));
    act.includeRole(staffRole).push(
        new Activity(i18n.get("clfps.peerreview.discussion.support"),
            "support"));

    act = new Act(i18n.get("clfps.peerreview.improve"));
    this.flow.push(act);
    act.includeRole(reviewer).push(
        new Activity(i18n.get("clfps.peerreview.improve.activity"),
            "learning"));
    act.includeRole(staffRole).push(
        new Activity(i18n.get("clfps.peerreview.improve.support"),
            "support"));

    ChangeManager.startGroup();
    for (var i = 0; i < this.flow.length; i++) {
        act = this.flow[i];
        for ( var j = 0; j < act.roleparts.length; j++) {
            var activities = act.roleparts[j].activities;
            for ( var k = 0; k < activities.length; k++) {
                ChangeManager.activityAdded(activities[k]);
            }
        }
    }

    var assessment = new AssessmentFlow();
    LearningDesign.data.assessments.push(assessment);
    ChangeManager.assessmentAdded(assessment);

    assessment.setAssessmentActivity(reviewActivity);
    ChangeManager.assessmentActivitySet(assessment, reviewActivity);

    var reportAssessment = AssessmentPatternManager
    .createByName("reportassessment");
    assessment.getPatterns().push(reportAssessment);
    ChangeManager.patternAdded(reportAssessment, assessment);

    var assessmentFunction = new AssessmentFunction("formative");
    assessment.getFunctions().push(assessmentFunction);
    ChangeManager.assessmentFunctionAdded(assessmentFunction, assessment);

    assessmentFunction.link = analysisActivity.id;
    ChangeManager
    .assessmentFunctionEdited(assessmentFunction, analysisActivity);

    ChangeManager.endGroup();
};

/**
 * Obtiene el título del clfp
 * @return Título del clfp
 */
PeerReview.prototype.getTitle = function() {
    return this.title;
};

/**
 * Obtiene el flujo del clfp
 * @return Flujo del clfp
 */
PeerReview.prototype.getFlow = function() {
    return this.flow;
};

/**
 * Obtiene el renderer del clfp
 * @return Renderer del clfp
 */
PeerReview.prototype.getRenderer = function() {
    return PeerReviewRenderer;
};

/**
 * Obtiene el nombre del patrón
 * @return Nombre del patrón
 */
PeerReview.prototype.getPatternName = function() {
    return i18n.get("clfps.peerreview.title");
};

/**
 * Obtiene el menú de edición del patrón
 * @return Menú de edición del patrón
 */
PeerReview.prototype.getEditionMenu = function(link) {
    return null;
};

/**
 * Función que calcula los participantes disponibles para ser asignados a una instancia
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
PeerReview.prototype.obtainAvailableStudents = function(instanceid){
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
PeerReview.prototype.obtainAvailableStudentsMain = function(instanceid){
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
PeerReview.prototype.obtainAvailableStudentsAnidado = function(instanceid){
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
 * Clonar un par de instancias
 * @param idInstancia Identificador de una de las instancias que constituyen el par a clonar
 * @param level Nivel en el que se encuentra el par de instancias a clonar
 */
PeerReview.prototype.clonar = function(idInstancia, level){
    var instancia = IDPool.objects[idInstancia];
    var par = this.obtenerPar(instancia);
    var ni=DesignInstance.copiarEstructuraContinuacion(par.leftReview, par.leftReview.idParent, par.leftReview.id, par.rightReview.id);
    DesignInstance.copiarEstructuraContinuacion(par.rightReview, par.rightReview.idParent, par.rightReview.id, ni.id);
    DesignInstance.copiarEstructura(par.discussion, par.discussion.idParent, par.discussion.id);
};

/**
 * Borrar un par de instancias
 * @param instancia Identificador de una de las instancias que constituyen el par a borrar
 */
PeerReview.prototype.borrarInstancia = function(instancia){
    var par = this.obtenerPar(instancia);
    DesignInstance.borrarEstructura(par.leftReview);
    DesignInstance.borrarEstructura(par.rightReview);
    DesignInstance.borrarEstructura(par.discussion);
};

/** Función que se encarga de asignar un participante a la instancia
 * @param instancia Instancia a la que se desea asignar el participante
 * @param participante Participante a asignar a la instancia
 */
PeerReview.prototype.asignar = function(instancia, participante){
    var par = this.obtenerPar(instancia);
    //Se asigna el participante a la instancia de review
    DesignInstance.asignacion(instancia, participante);
    //Se asigna el participante a la instancia de discussion
    DesignInstance.asignacion(par.discussion, participante);
};

/**
 * Función que borra de una instancia un participante
 * @param instancia Instancia de la que se desea borrar el participante
 * @param participante Participante a borrar
 */
PeerReview.prototype.borrar = function(instancia, participante){
    var p = DesignInstance.getParticipant(participante);
    if (p.type=="teacher"){
        DesignInstance.borradoAscendente(instancia, participante);
    }
    else{
        var par = this.obtenerPar(instancia);
        //Se borra el participante de la instancia de review
        DesignInstance.borradoAscendente(instancia, participante);
        //Se borra el participante de la instancia de discussion
        DesignInstance.borradoAscendente(par.discussion, participante);
    }
};

/**
 * Dada una instancia de review obtiene el par de instancias de review y la instancia de discussion
 * @param instancia Instancia de review de la que se quiere obtener el par y la instancia de discussion
 * @return El par de instancias de review y la instancia de discussion
 */
PeerReview.prototype.obtenerPar = function(instancia){
    var grupo=DesignInstance.grupoInstancia(instancia);
    //Obtenemos la posición de la instancia de review en el conjunto
    if (ClfpsCommon.isInMain(this)){
        var ig = grupo.instances;
    }
    else{
        ig=DesignInstance.instanciasGrupoProcedenInstancia(grupo.roleid, DesignInstance.instanciaProcede(instancia).id);
    }
    var pos;
    for (var i=0;i<ig.length;i++){
        if (ig[i].id==instancia.id){
            pos=i;
        }
    }
    if (pos%2==0){
        var leftReview = instancia;
        var rightReview = ig[pos+1];
    }
    else{
        leftReview = ig[pos-1];
        rightReview = instancia;
    }
    
    var dg = DesignInstance.getGrupo(this.flow[2].learners[0]);   
    if (ClfpsCommon.isInMain(this)){
        var dgi = dg.instances;
    }
    else{
        dgi=DesignInstance.instanciasGrupoProcedenInstancia(dg.roleid, DesignInstance.instanciaProcede(instancia).id);
    }
    //Cada par de instancias consecutivas en el grupo de revisores está asociado con una instancia del grupo de discusión
    var posDiscussion = parseInt(pos/2);
    var discussion = dgi[posDiscussion];
    return{
        leftReview: leftReview,
        rightReview: rightReview,
        discussion: discussion
    };
};

/**
 * Dada una instancia de discussion obtiene el par de instancias de review y la instancia de discussion
 * @param instanciaDiscussion Instancia de discussion de la que se quiere obtener el par de review y la instancia de discussion
 * @return El par de instancias de review y la instancia de discussion
 */
PeerReview.prototype.obtenerParDiscussion = function(instanciaDiscussion){
    var grupo=DesignInstance.grupoInstancia(instanciaDiscussion);
    //Obtenemos la posición de la instancia de discussion en el conjunto
    if (ClfpsCommon.isInMain(this)){
        var ig = grupo.instances;
    }
    else{
        ig=DesignInstance.instanciasGrupoProcedenInstancia(grupo.roleid, DesignInstance.instanciaProcede(instanciaDiscussion).id);
    }
    var pos;
    for (var i=0;i<ig.length;i++){
        if (ig[i].id==instanciaDiscussion.id){
            pos=i;
        }
    }
    var rg = DesignInstance.getGrupo(this.flow[0].learners[0]);   
    if (ClfpsCommon.isInMain(this)){
        var rgi = rg.instances;
    }
    else{
        rgi=DesignInstance.instanciasGrupoProcedenInstancia(rg.roleid, DesignInstance.instanciaProcede(instanciaDiscussion).id);
    }
    //Cada par de instancias consecutivas en el grupo de revisores está asociado con una instancia del grupo de discusión
    return{
        leftReview: rgi[2*pos],
        rightReview: rgi[2*pos+1],
        discussion: instanciaDiscussion
    };
};

PeerReview.prototype.createInitialInstances = function(){
    var instancias = new Array();
    if (ClfpsCommon.isInMain(this)==false)
    {
        
        var actUpper = this.getFlow()[2];
        var roleUpper = IDPool.getObject(actUpper.learners[0]);
        //Obtenemos el número de instancias del rol del que depende en otro patrón
        var parentGroup=LearningDesign.findRoleParentOfRole(roleUpper);
        instancias = DesignInstance.instanciasGrupo(parentGroup.id);
    }
    //teacher
    var teacherRole = IDPool.getObject(this.getFlow()[2].staff[0]);
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
    
    var act = this.getFlow()[3];
    var role = IDPool.getObject(act.learners[0]);
    g = new Group(role.id);
    DesignInstance.addGroup(g);
    //Añadir dos instancias por cada grupo
    for (var i=0;i<2;i++)
    {
        gi = new GroupInstance(role.title);
        g.addInstance(gi);
    }
    //Se añaden las restantes instancias
    for (var n=1; n<instancias.length;n++)
    {
        for (var j=0;j<2;j++)
        {
            gi = new GroupInstance(role.title);
            g.addInstance(gi);
        }
    }
    for (n=0; n<instancias.length;n++)
    {
        g.instances[2*n].idParent = instancias[n].id;
        g.instances[2*n+1].idParent = instancias[n].id;
    }
    
    act = this.getFlow()[2];
    role = IDPool.getObject(act.learners[0]);
    g = new Group(role.id);
    DesignInstance.addGroup(g);
    //Añadir una instancia por cada grupo/rol
    gi = new GroupInstance(role.title);
    g.addInstance(gi);
    //Se añaden las restantes instancias
    for (n=1; n<instancias.length;n++)
    {
        gi = new GroupInstance(role.title);
        g.addInstance(gi);
    }
    for (n=0; n<instancias.length;n++)
    {
        g.instances[n].idParent = instancias[n].id;
    }
};

PeerReview.prototype.getAvailableGroupPatterns = function(type, actid){   
    
    var availables = new Array();
    
    //Los patrones de asignación de participantes y de grupos sólo están disponibles en la fase de review
    if (this.getFlow()[0].id == actid){
        availables["gn"] = new Array("fixednumbergroups", "fixedsizegroups");
        availables["pa"] = new Array("groupparticipantsdistributepattern");
    }else{
        availables["gn"] = new Array();
        availables["pa"] = new Array();
    }        
      
    var factories = GroupPatternManager.patternFactories[type];
    var patterns = new Array();
    for (var i = 0; i < factories.length; i++){
        if (availables[type].indexOf(factories[i].getId())!=-1){
            patterns.push(factories[i]);
        }

    }
    return patterns;        
};

PeerReview.prototype.canDeleteInstance = function(roleid, instanceId){
    var instance = IDPool.getObject(instanceId);
    if(DesignInstance.instanciasGrupoMismoPadre(roleid, instance.idParent).length > 1) {
        var letDelete = true;
    } else {
        letDelete = false;
    }
    return letDelete;
};


Factory.registerFactory("peerreview", PeerReview, PeerReviewFactory);
