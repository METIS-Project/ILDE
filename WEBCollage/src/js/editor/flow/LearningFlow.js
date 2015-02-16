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
 * Flujo de aprendizaje en el diseño
 */
var LearningFlow = {

    displayInfo : {
        activeClfpId : null,
        openActIds : []
    },

    /**
	 * Identificador del enlace activo
	 */
    currentActiveLink : null, /* just where the mouse pointer is */
    currentEditingClfp : null,
    currentEditingLink : null,

    listenToUI : true,

    tmpAssessment: null,
    /**
	 * Array que indica la instancia de la que depende cada uno de los clfps que se está mostrando
	 */
    instanceid : new Object(),
    
    setInstanceId: function(instanceid) {
        this.instanceid=instanceid;
    },

    init : function() {
        FlowDialogs.prepareDialogs();
        this.createTree();
        MainLearningFlowRenderer.init();
        MenuManager.registerThing(dojo.byId("FlowEditMenuWhenEmptyContent"), {
            getItems : function() {
                return LearningFlow.createMenuForEmpty();
            }
        });

        dojo.connect(dojo.byId("FlowEditMenuWhenEmpty"), "onclick", function() {
            LearningFlow.includePattern(-1);
        });
        dojo.connect(dojo.byId("removeClfpDialogOk"), "onclick", function(){
            LearningFlow.closeRemoveClfpDialog();
            LearningFlow.removeClfp();
        });        
        dojo.connect(dojo.byId("removeClfpDialogCancel"), "onclick", function(){
            LearningFlow.closeRemoveClfpDialog();
        });
        
        dojo.connect(dojo.byId("removeAssessmentDialogOk"), "onclick", function(){
            LearningFlow.closeRemoveAssessmentDialog();
            LearningFlow.removeAssessment(LearningFlow.tmpAssessment);
        });        
        dojo.connect(dojo.byId("removeAssessmentDialogCancel"), "onclick", function(){
            LearningFlow.closeRemoveAssessmentDialog();
        });
        
    },
    restart : function() {
        if(this.displayInfo.activeClfpId && !IDPool.getObject(this.displayInfo.activeClfpId)) {
            this.displayInfo.activeClfpId = null;
        }
        this.currentEditingClfp = null;
        this.currentEditingLink = null;
    },
    setActIsOpen : function(actid, isOpen) {
        if (this.listenToUI) {
            this.displayInfo.openActIds[actid] = isOpen;
            MainLearningFlowRenderer.update(true);
        }
    },
    createTree : function() {
        var callback = function(id) {
            var obj = IDPool.getObject(id);
            if(obj) {
                if(obj.type == "clfp") {
                    LearningFlow.displayInfo.activeClfpId = id;
                    MainLearningFlowRenderer.update(true);
                } else if(obj.type == "act") {
                    this.setActIsOpen(obj.id, true);
                }
            }
        };
        TreeManager.createTree("LDFlowStructureTree", callback, i18n.get("flow.structure"), "LDFlowStructureTreeRoot", "structureIcon");
    },
    createMenuForEmpty : function() {
        if(this.displayInfo.activeClfpId == null) {
            return [{
                label : i18n.get("flow.clfp.add"),
                icon : "add",
                onClick : function() {
                    /* '-1' means no specific position  */
                    LearningFlow.includePattern(-1);
                },
                help : i18n.get("help.clfp.add.first")
            }];
        } else {
            return null;
        }
    },
    /**
	 * Crea los menús de opciones para cada uno de los elementos en modo edición del diseño
	 */
    createMenuWithClfps : function(data) {
        if(!this.listenToUI) {
            return null;
        } else {
            return LearningFlowMenus.getMenu(data);
        }
    },
    
    
    openRemoveClfpDialog: function(data){
        this.currentEditingClfp = data.clfp;
        dijit.byId("removeClfpDialog").titleNode.innerHTML = dojo.string.substitute(i18n.get("flow.clfp.remove"), [data.clfp.title]);
        dojo.byId("removeClfpDialogInfo").innerHTML = dojo.string.substitute(i18n.get("flow.clfp.remove.confirm"), [this.currentEditingClfp.title]);
        dijit.byId("removeClfpDialog").show();
    },
           
    closeRemoveClfpDialog: function(){
        dijit.byId("removeClfpDialog").hide();
    },
    
    /** ** Flow editing things ****** */
    removeClfp : function() {
        var parent = LearningDesign.findClfpParentOf(this.currentEditingClfp.id).clfp;
        var parentId = parent != null ? parent.id : null;
        this.displayInfo.activeClfpId = parentId;

        LearningDesign.removeClfp(this.currentEditingClfp.id);
    },
    
    openRemoveAssessmentDialog: function(data){
        this.tmpAssessment = data;
        dijit.byId("removeAssessmentDialog").show();
    },
                 
    closeRemoveAssessmentDialog: function(){
        dijit.byId("removeAssessmentDialog").hide();
    },
    
    removeAssessment : function(data) {
        AssessmentManager.deleteAssessment(IDPool.getObject(data.id));
        this.tmpAssessment = null;
    //}
    },
    includePattern : function(position, data) {
        this.positionForNewClfp = position;
        this.clfpForNewClfpId = data ? data.clfp.id : null;

        PatternSelector.open(function(patternDef) {
            LearningFlow.actuallyIncludePattern(patternDef);
        }, "clfp");
    },
    actuallyIncludePattern : function(patternDef) {
        callback = function(clfp) {
            LearningFlow.displayInfo.activeClfpId = clfp.id;
            LearningDesign.includeClfp(clfp, LearningFlow.positionForNewClfp, LearningFlow.clfpForNewClfpId);
        };
        Factory.create(patternDef.id, callback);
    },
    replaceActWithClfp : function(data) {
        this.currentEditingLink = data;
        var obj = IDPool.getObject(this.currentEditingLink.id);
        if(obj.type == "act" && obj.learners.length == 1) {
            PatternSelector.open(function(patternDef) {
                LearningFlow.actuallyReplaceActWithClfp(patternDef);
            }, "clfp");
        }
    },
    actuallyReplaceActWithClfp : function(patternDef) {
        var obj = IDPool.getObject(this.currentEditingLink.id);
        if(obj.type == "act" && obj.learners.length == 1) {
            callback = function(clfp) {
                var obj = IDPool.getObject(LearningFlow.currentEditingLink.id);
                //LearningFlow.displayInfo.activeClfpId = clfp.id;
                LearningDesign.replaceActWithClfp(clfp, obj);
            };
            Factory.create(patternDef.id, callback);
        }
    },
    /*    moveClfp : function(data) {
        var callback = function(reference, selection) {
            LearningFlow.actuallyMoveClfp(reference, selection);
        };

        ActivityPickerDialog.open({
            clfpPosition : true
        }, callback, data);
    },
    actuallyMoveClfp : function(reference, position) {
        LearningDesign.moveClfp(reference.clfp, position);
    },*/
    /** ** display ***** */

    activate : function(link) {
        if(!this.listenToUI) {
            return;
        }
        
        MenuManager.hideFloatMenu();

        var obj = IDPool.getObject(link.id);
        
        if(obj.type == "act") {
            this.setActIsOpen(link.id, !this.displayInfo.openActIds[link.id]);
        } else if(obj.type == "clfp") {
            this.activateCLFP(link.id);
        } else if (LearningFlowSelectMode.isSelecting()) {
            LearningFlowSelectMode.activate(obj);
        } else if(obj.type == "role") {
            EditRoleDialog.open(obj);
        } else if(obj.type == "assessmentflow") {
            EditAssessmentDialog.open(obj);
        } else if (obj.type == "activity") {
            EditActivityDialog.open(obj);
        }
    },
    activateCLFP : function(id) {
        if(!this.listenToUI) {
            return;
        }
        this.displayInfo.activeClfpId = id;
        MainLearningFlowRenderer.update(true);
        this.expandTreeToShowCurrentClfp();
    },
    
    /**
     * Activa la primera de las instancias de una fase de una instancia de clfp
     * @param roleId role id
    */
    automaticInstanceActivation: function(roleId){
        var clfp = DesignInstance.clfpRole(IDPool.getObject(roleId));
        var instancias = ClfpsCommon.getInstances(clfp, roleId, this);
        this.activateInstance(instancias[0].id);
    },
       
    removeOldActivatedInstances: function(clfpRemoved){
        for (var i=0; i<clfpRemoved.flow.length;i++)
        {
            if (clfpRemoved.flow[i].type=="clfpact")
            {
                for (var j=0; j<clfpRemoved.flow[i].clfps.length;j++)
                {
                    this.removeOldActivatedInstances(clfpRemoved.flow[i].clfps[j]);
                }
            } 
        }
        if (this.instanceid[clfpRemoved.id])
        {
            delete this.instanceid[clfpRemoved.id];  
        }           
    },
    
    /**
     * Activa una instancia dada del clfp y actualiza los clfps que dependen de esta
     * @param instanceid Identificador de la instancia a activar
    */
    activateInstance: function(instanceid){
        var nuevaInstanciaActiva = IDPool.getObject(instanceid);
        //Proporcionamos a la función la nueva instancia que se activa para que se actualicen las dependencias
        this.updateDependencias(nuevaInstanciaActiva);
        var clfps = DesignInstance.clfpDependenInstanciaGrupo(nuevaInstanciaActiva);
        //Activamos el primero de todos los clfps que dependen de la instancia
        LearningFlow.activateCLFP(clfps[0].id);
        //Si es una instancia de review se activa la correspondiente de discussion
        var rol = IDPool.getObject(DesignInstance.grupoInstancia(nuevaInstanciaActiva).roleid);
        if (rol.title=="Reviewers"){
            var clfpInstancia = DesignInstance.clfpInstanciaGrupo(nuevaInstanciaActiva);
            var par = clfpInstancia.obtenerPar(nuevaInstanciaActiva);
            this.updateDependencias(par.discussion); 
        }
        else{
            if (rol.title=="Discussion group"){
                clfpInstancia = DesignInstance.clfpInstanciaGrupo(nuevaInstanciaActiva);
                par = clfpInstancia.obtenerParDiscussion(nuevaInstanciaActiva);
                this.updateDependencias(par.leftReview); 
            }
        }
    },
    
    /**
	 * Activa la siguiente instancia del clfp y actualiza los clfps que dependen de esta
	 @param clfpid Identificador del clfp que depende de la instancia
	 */
    activateNextInstance : function(clfpid) {
        var instanciaDepende = IDPool.getObject(this.instanceid[clfpid]);
        var g = DesignInstance.grupoInstancia(instanciaDepende);
        //Obtenemos las instancias que proceden de la misma
        var ip = DesignInstance.instanciaProcede(instanciaDepende);
        if(ip != null) {
            var igmp = DesignInstance.instanciasGrupoProcedenInstancia(g.roleid, ip.id);
        } else {
            igmp = g.instances;
        }

        var pos = -1;

        for(var i = 0; i < igmp.length; i++) {
            if(igmp[i].id == instanciaDepende.id) {
                pos = i;
            }
        }
        var nuevaPos = (pos + 1) % igmp.length;
        //Proprorcionamos a la función la nueva instancia que se activa para que se actualicen las dependencias
        this.updateDependencias(igmp[nuevaPos]);
        LearningFlow.activateCLFP(clfpid);
        //Si es una instancia de review se activa la correspondiente de discussion
        var rol = IDPool.getObject(DesignInstance.grupoInstancia(igmp[nuevaPos]).roleid);
        if(rol.title == "Reviewers") {
            var clfpInstancia = DesignInstance.clfpInstanciaGrupo(igmp[nuevaPos]);
            var par = clfpInstancia.obtenerPar(igmp[nuevaPos]);
            this.updateDependencias(par.discussion);
        } else {
            if(rol.title == "Discussion group") {
                clfpInstancia = DesignInstance.clfpInstanciaGrupo(igmp[nuevaPos]);
                par = clfpInstancia.obtenerParDiscussion(igmp[nuevaPos]);
                this.updateDependencias(par.leftReview);
            }
        }
    },
    /**
	 * Activa la instancia previa del clfp y actualiza los clfps que dependen de esta
	 * @param clfpid Identificador del clfp que depende de la instancia
	 */
    activatePreviousInstance : function(clfpid) {
        var instanciaDepende = IDPool.getObject(this.instanceid[clfpid]);
        var g = DesignInstance.grupoInstancia(instanciaDepende);
        //Obtenemos las instancias que proceden de la misma
        var ip = DesignInstance.instanciaProcede(instanciaDepende);
        if(ip != null) {
            var igmp = DesignInstance.instanciasGrupoProcedenInstancia(g.roleid, ip.id);
        } else {
            igmp = g.instances;
        }

        var pos = -1;

        for(var i = 0; i < igmp.length; i++) {
            if(igmp[i].id == instanciaDepende.id) {
                pos = i;
            }
        }
        if(pos - 1 >= 0) {
            var nuevaPos = pos - 1;
        } else {
            nuevaPos = igmp.length - 1;
        }

        //Proprorcionamos a la función la nueva instancia que se activa para que se actualicen las dependencias
        this.updateDependencias(igmp[nuevaPos]);
        LearningFlow.activateCLFP(clfpid);
        //Si es una instancia de review se activa la correspondiente de discussion
        var rol = IDPool.getObject(DesignInstance.grupoInstancia(igmp[nuevaPos]).roleid);
        if(rol.title == "Reviewers") {
            var clfpInstancia = DesignInstance.clfpInstanciaGrupo(igmp[nuevaPos]);
            var par = clfpInstancia.obtenerPar(igmp[nuevaPos]);
            this.updateDependencias(par.discussion);
        } else {
            if(rol.title == "Discussion group") {
                clfpInstancia = DesignInstance.clfpInstanciaGrupo(igmp[nuevaPos]);
                par = clfpInstancia.obtenerParDiscussion(igmp[nuevaPos]);
                this.updateDependencias(par.leftReview);
            }
        }
    },
    /**
	 * Dada una nueva instancia activa actualiza convenientemente las dependencias en los clfps afectados
	 * @param nuevaInstanciaActiva Nueva instancia a activar
	 * */
    updateDependencias : function(nuevaInstanciaActiva) {
        var clfps = DesignInstance.clfpDependenInstanciaGrupo(nuevaInstanciaActiva);
        for(var j = 0; j < clfps.length; j++) {
            //Actualizamos el identificador de la instancia de la que depende
            this.instanceid[clfps[j].id] = nuevaInstanciaActiva.id;
            //Actualizar también los identificadores de los clfps que dependen de este
            for(var id in LearningFlow.instanceid) {
                if(LearningFlow.instanceid[id]!=null && DesignInstance.clfpInstanciaGrupo(IDPool.getObject(LearningFlow.instanceid[id])).id == clfps[j].id) {
                    var g = DesignInstance.grupoInstancia(IDPool.getObject(LearningFlow.instanceid[id]));
                    //Obtenemos las instancias dependientes de la seleccionada en el primer patrón
                    var ig = DesignInstance.instanciasGrupoProcedenInstancia(g.roleid, nuevaInstanciaActiva.id);
                    //Se considera activa la primera de ellas
                    var inst = ig[0];
                    //Actualizar dependencias
                    this.updateDependencias(inst);
                }
            }
        }
    },
    expandTreeToShowCurrentClfp : function() {
        TreeManager.expand("LDFlowStructureTree", this.displayInfo.activeClfpId);
    },
    getCurrentClfpId : function() {
        if(this.displayInfo.activeClfpId == null && LearningDesign.data.flow.clfps.length > 0) {
            this.displayInfo.activeClfpId = LearningDesign.data.flow.clfps[0].id;
        }
        return this.displayInfo.activeClfpId;
    },
    updateDisplay : function() {
        this.buildStructureTree();
        var emptyMenuIsVisible = LearningDesign.data.flow.clfps.length == 0;
        dojo.query("#FlowEditMenuWhenEmpty").style({
            visibility : ( emptyMenuIsVisible ? "visible" : "hidden")
        });
        MainLearningFlowRenderer.update(false);
    },
    buildStructureTree : function() {
        var how = {
            paintActivities : "none"
        };
        FlowTree.buildStructureTree("LDFlowStructureTree", "LDFlowStructureTreeRoot", how);
    },
    registerActiveElement : function(object, link, followCursor) {
        new FlowActiveObjectListener(object, link);
        var linkedobj = IDPool.getObject(link.id);
        switch(linkedobj.type) {
            case "activity":
                var menustyle = "activity";
                var linkedObjTitle = linkedobj.getTitle();
                break;
            case "role":
                menustyle = "role";
                linkedObjTitle = linkedobj.getTitle();
                break;
            case "act":
            case "clfpact":
                menustyle = link.role ? "role" : "act";
                linkedObjTitle = link.role ? IDPool.getObject(link.role).getTitle() : linkedobj.getTitle();
                break;
            case "clfp":
                menustyle = "clfp";
                linkedObjTitle = linkedobj.getTitle();
                break;
            default:
                menustyle = "default";
                linkedObjTitle = linkedobj.getTitle();
                break;
        }
        MenuManager.registerThing(object, {
            getItems : function(data) {
                return LearningFlow.createMenuWithClfps(data);
            },
            data : link,
            followCursor : followCursor,            
            menuStyle: menustyle,
            linkedObjTitle : linkedObjTitle
        });
    },

    stopUI : function() {
        this.listenToUI = false;
    },
    resumeUI : function() {
        this.listenToUI = true;
    }
};


var FlowActiveObjectListener = function(object, link) {
    this.link = link;
    object.connect("onmouseover", this, "active");
    object.connect("onmouseout", this, "unactive");
    object.connect("onclick", this, "clicked");
};

FlowActiveObjectListener.prototype.active = function() {
    LearningFlow.currentActiveLink = this.link;
};

FlowActiveObjectListener.prototype.unactive = function() {
    if(LearningFlow.currentActiveLink == this.link) {
        LearningFlow.currentActiveLink = null;
    }
};

FlowActiveObjectListener.prototype.clicked = function() {
    LearningFlow.activate(this.link);
};


