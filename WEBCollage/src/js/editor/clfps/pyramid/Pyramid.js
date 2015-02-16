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
 * @class Factoría del patrón pirámide
 */
var PyramidFactory = {
    /**
     * Crea una instancia del patrón pirámide
    */
    create : function(callback, staff) {
        this.callback = callback;
        this.staff = staff;
        this.openDialog();
    },

    /**
     * Muestra el diálogo de creación de una pirámide
    */
    openDialog : function() {
        var dlg = new dijit.Dialog( {
            id : "PyramidNumberOfLevelsDialog",
            title : i18n.get("clfps.pyramid.dialog.title")
        });

        var content = '<p>${0}</p>';
        content += '<div id="PyramidNumberOfLevelsSlider" dojoType="dijit.form.HorizontalSlider" value="3" minimum="2" maximum="10" discreteValues="9" intermediateChanges="true" style="width:300px;">';
        content += '<ol dojoType="dijit.form.HorizontalRuleLabels" container="topDecoration" style="height:20px;">';
        content += '<li>2</li><li>3</li><li>4</li><li>5</li><li>6</li><li>7</li><li>8</li><li>9</li><li>10</li>';
        content += '</ol></div>';
        content += '<div dojoType="dijit.layout.ContentPane" style="text-align: center;">';
        content += '<div dojoType="dijit.form.Button" onclick="PyramidFactory.okDialog();">${1}</div><div dojoType="dijit.form.Button" onclick="PyramidFactory.closeDialog();">${2}</div>';
        content += "</div>";
        content += "</div>";

        content = dojo.string.substitute(content, [
            i18n.get("clfps.pyramid.dialog.intro"), i18n.get("common.ok"),
            i18n.get("common.cancel") ]);

        dlg.setContent(content);
        dojo.body().appendChild(dlg.domNode);
        dlg.show();
        dojo.connect(dlg, "hide", PyramidFactory, "closeDialog");
        dlg.resize();

    },

    /**
     * Funcionalidad asociada a la creación de la pirámide
    */
    okDialog : function() {
        var levels = dijit.byId("PyramidNumberOfLevelsSlider").value;
        this.closeDialog();
        ChangeManager.startGroup();
        this.callback(new Pyramid(levels, this.staff));
        ChangeManager.endGroup();
    },

    /**
     * Funcionalidad asociada al cierre de la ventana de creación
    */
    closeDialog : function() {
        dijit.byId("PyramidNumberOfLevelsDialog").destroyRecursive();
    }
};

/**
 * @class Patrón pirámide
 */
var Pyramid = function(levels, staff) {
    /**
     * Indicador de tipo
    */
    this.type = "clfp";
    /**
     * Nombre del patrón
    */
    this.title = i18n.get("clfps.pyramid.title");
    /**
     * Identificador del patrón
    */
    this.patternid = "pyramid";
    this.ownRoles = new Array();

    IDPool.registerNewObject(this);

    this.levels = levels;
    this.flow = new Array();

    var roles = new Array();

    /* create roles */

    if (staff && staff.length > 0) {
        var staffRole = staff[0];
    } else {
        staffRole = new Role(i18n.get("roles.common.teacher"), "staff");
        this.ownRoles.push(staffRole);
    }

    for ( var i = levels; i > 0; i--) {
        if (i == levels) {
            roles[i - 1] = new Role(i18n.get("clfps.pyramid.class"), "learner");
        } else if (i == 1) {
            roles[i - 1] = new Role(i18n.get("clfps.pyramid.student"),
                "learner");
            roles[i].addChild(roles[i - 1]);
        } else {
            var rolename = dojo.string.substitute(i18n
                .get("clfps.pyramid.group"), [ i ]);
            roles[i - 1] = new Role(rolename, "learner");
            roles[i].addChild(roles[i - 1]);
        }
    }
    this.ownRoles.push(roles[this.levels - 1]);

    for (i = 0; i < levels; i++) {
        var levelName = (i + 1);
        var actname = dojo.string.substitute(i18n.get("clfps.pyramid.level"),
            [ levelName ]);
        var supportname = dojo.string.substitute(i18n
            .get("clfps.pyramid.support"), [ levelName ]);

        var act = new Act(actname);
        this.flow.push(act);

        var activities = act.includeLearner(roles[i]);
        activities.push(new Activity(actname, "learning"));
        act.includeStaff(staffRole).push(new Activity(supportname, "support"));
    }

    ChangeManager.startGroup();
    for (i = 0; i<this.flow.length; i++) {
        act = this.flow[i];
        for (var j=0; j<act.roleparts.length; j++) {
            activities = act.roleparts[j].activities;
            for (var k=0; k<activities.length; k++) {
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
Pyramid.prototype.getTitle = function() {
    return this.title;
};

/**
 * Obtiene el flujo del clfp
 * @return Flujo del clfp
 */
Pyramid.prototype.getFlow = function() {
    return this.flow;
};

/**
 * Obtiene el renderer del clfp
 * @return Renderer del clfp
 */
Pyramid.prototype.getRenderer = function() {
    return PyramidRenderer;
};

/**
 * Obtiene el nombre del patrón
 * @return Nombre del patrón
 */
Pyramid.prototype.getPatternName = function() {
    return i18n.get("clfps.pyramid.title");
};

/**
 * Obtiene el menú de edición del patrón
 * @return Menú de edición del patrón
 */
Pyramid.prototype.getEditionMenu = function(link) {
    return null;
};

/**
 * Clonar una instancia
 * @param idInstancia Identificador de la instancia a clonar
 * @param level Nivel en el que se encuentra la instancia a clonar
 */
Pyramid.prototype.clonar = function(idInstancia, level){
        var levelZero = false;
        var instancia = IDPool.getObject(idInstancia);
        var grupoInstancia = DesignInstance.grupoInstancia(instancia);
        var roleid = grupoInstancia.roleid;       
        //Buscamos el rol en el nivel 0, que se corresponde con flow.length - 1
        var act = this.getFlow()[this.flow.length - 1];
        for (var i = 0; i < act.roleparts.length; i++){
            if (act.roleparts[i].roleId == roleid){
                levelZero = true;
            }
        }
        if (levelZero){
            DesignInstance.copiarEstructuraTop(instancia);
        }else{
            DesignInstance.copiarEstructura(instancia, instancia.idParent, instancia.id);
        }
        
    /*var instancia = IDPool.objects[idInstancia];
    if (level>0){
        DesignInstance.copiarEstructura(instancia, instancia.idParent, instancia.id);
    }else{
        DesignInstance.copiarEstructuraTop(instancia);
    }*/
};

/**
 * Función que calcula los participantes disponibles para ser asignados a una instancia
 * @param instanceid Identificador de la instancia de la que se desea conocer los participantes disponibles
 */
Pyramid.prototype.obtainAvailableStudents = function(instanceid){
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
Pyramid.prototype.obtainAvailableStudentsMain = function(instanceid){
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
Pyramid.prototype.obtainAvailableStudentsAnidado = function(instanceid){
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
 * Función que borra de una instancia un participante
 * @param instancia Instancia de la que se desea borrar el participante
 * @param participante Participante a borrar
 */
Pyramid.prototype.borrar = function(instancia, participante){
    DesignInstance.borradoAscendente(instancia, participante);
};

/**
 * Función que se encarga de asignar un participante a la instancia
 * @param instancia Instancia a la que se desea asignar el participante
 * @param participante Participante a asignar a la instancia
 */
Pyramid.prototype.asignar = function(instancia, participante){
    DesignInstance.asignacionAscendente(instancia, participante);
};

/**
 * Función que crea las instancias iniciales de los grupos
 */
Pyramid.prototype.createInitialInstances = function(){
    var instancias = new Array();
    var actUpper = this.getFlow()[this.levels - 1];
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
    for(var i = this.levels - 1; i >= 0; i--) {
        var act = this.getFlow()[this.levels - 1 - i];
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
    for (i = this.levels -1; i>=1; i--){
        var actChild = this.getFlow()[this.levels - 1 - i];
        var roleChild = IDPool.getObject(actChild.learners[0]);
        var actParent = this.getFlow()[this.levels - 1 - (i-1)];
        roleParent = IDPool.getObject(actParent.learners[0]);
        var groupChild = DesignInstance.getGrupo(roleChild.id);
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

Pyramid.prototype.getAvailableGroupPatterns = function(type, actid){
    var availables = new Array();
    
    //En el nivel cero de la pirámide no están disponibles los patrones de asignación de participantes ni de grupos
    if (this.getFlow()[this.flow.length - 1].id == actid){
        availables["gn"] = [];
        availables["pa"] = [];
    //En el nivel superior están disponibles todos los patrones
    }else if (this.getFlow()[0].id == actid){
        availables["gn"] = new Array("fixednumbergroups", "fixedsizegroups");
        availables["pa"] = new Array("groupparticipantsdistributepattern");
    }else{
        //En los niveles intermedios sólo están disponibles los patrones de grupos
        availables["gn"] = new Array("fixednumbergroups", "fixedsizegroups");
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

    
Pyramid.prototype.canDeleteInstance = function(roleid, instanceId){
    var instance = IDPool.getObject(instanceId);
    if(DesignInstance.instanciasGrupoMismoPadre(roleid, instance.idParent).length > 1) {
        var letDelete = true;
    } else {
        letDelete = false;
    }
    return letDelete;
};

Factory.registerFactory("pyramid", Pyramid, PyramidFactory);
