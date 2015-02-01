/**
 * @class Proporciona funcionalidad para gestión de un diseño de una unidad de aprendizaje
 *
 */

/*global Matches, Act, ChangeManager, IDPool, Activity, LearningFlow */

var LearningDesign = {

    /**
     * Contiene la información relativa al diseño
     */
    data : null,

    /**
     * Parejas de actividades y objetivos de aprendizaje
     */

    activitiesAndLOsMatches : new Matches(),
    /**
     * Parejas de actividades y recursos asociados
     */
    activitiesAndResourcesMatches : new Matches(),

    /**
     * Función de inicialización
     */
    init : function() {
        this.activitiesAndLOsMatches.setData(this.data.activitiesAndLOs);
        this.activitiesAndResourcesMatches.setData(this.data.activitiesAndResources);

        if(this.data.groupPatternList === undefined) {
            this.data.groupPatternList = [];
        }
        GroupPatternManager.init();
    },
    /**
     * Borra los campos que contienen la información relativa al diseño
     */
    clear : function() {
        this.data = {
            title : "", //Nombre del diseño
            prerrequisites : "", //Requisitos previos del diseño
            learningObjectives : [], //Objetivos del diseño
            resources : [], //Recursos del diseño
            flow : new Act("main", true), //Flujo del diseño

            activitiesAndLOs : [], //Actividades y objetivos
            activitiesAndResources : [], //Actividades y recursos

            assessments : [], //Evaluaciones,
            groupPatternList : [],
            groupPatterns : {}
        };
        this.init();
    },
    /**
     * Carga la información relativa al diseño
     * @param data Información relativa al diseño a cargar
     */
    load : function(data) {
        this.data = data;
        this.init();
    },
    /* edition */

    /**
     * Asigna el título al diseño
     * @param title Titulo a asignar al diseño
     */
    setTitle : function(title) {
        this.data.title = title;
        ChangeManager.titleSet();
    },
    /**
     * Asigna los requisitos previos al diseño
     * @param prerrequisites Requisitos previos del diseño
     */
    setPrerrequisites : function(prerrequisites) {
        this.data.prerrequisites = prerrequisites;
        ChangeManager.prerrequisitesSet();
    },
    /**
     * Añade un objetivo de aprendizaje al diseño
     * @param lo Objetivo de aprendizaje a añadir
     */
    addLO : function(lo) {
        this.data.learningObjectives.push(lo);
        ChangeManager.loAdded(lo);
    },
    /**
     * ELimina un objetivo de aprendizaje del diseño
     * @param lo Objetivo de aprendizaje a eliminar
     */
    removeLO : function(lo) {
        this.data.learningObjectives.splice(this.data.learningObjectives.indexOf(lo), 1);
        ChangeManager.loDeleted(lo);
    },
    /**
     * Añade un recurso al diseño
     * @param resource Recurso a añadir
     */
    addResource : function(resource) {
        this.data.resources.push(resource);
        ChangeManager.resourceAdded(resource);
    },
    /**
     * Elimina un recurso del diseño
     * @param resource Recurso a eliminar
     */
    removeResource : function(resource) {
        this.data.resources.splice(this.data.resources.indexOf(resource), 1);
        ChangeManager.resourceDeleted(resource);
    },
    /**
     * Elimina una actividad del diseño
     * @param activity Actividad a eliminar
     */
    removeActivity : function(activity) {
        var position = this.findActParentOfActivity(activity.id);
        var activities = position.act.getActivitiesForRoleId(position.roleId);
        activities.splice(activities.indexOf(activity), 1);

        ChangeManager.activityDeleted(activity);
    },
    /**
     * Elimina un rol hijo de un padre (ambos roles pertenecen al mismo CLFP)
     * @param role Role a eliminar
     */
    removeRoleFromParent : function(role, parent) {
        parent.removeChild(role);
        ChangeManager.roleDeleted(role);
    },
    removeRoleFromAct : function(role, act) {
        var i = act.learners.indexOf(role.id);
        if(i >= 0) {
            act.learners.splice(i, 1);
        }
        i = act.staff.indexOf(role.id);
        if(i >= 0) {
            act.staff.splice(i, 1);
        }

        for( i = 0; i < act.roleparts.length; i++) {
            if(act.roleparts[i].roleId == role.id) {
                var activities = act.roleparts[i].activities;
                while(activities.length > 0) {
                    LearningDesign.removeActivity(activities[0]);
                }

                act.roleparts.splice(i, 1);
                break;
            }
        }
    },

    addRoleTo : function(role, parent) {
        parent.addChild(role);
        ChangeManager.roleAdded(role);
    },
    /**
     * Crea una nueva actividad
     * @param actid Identificador de la actividad
     * @param roleid Identificador del rol asociado
     * @param position Posición de la actividad en el conjunto de actividades
     * @param title Nombre de la actividad
     */
    createActivity : function(actid, roleid, position, title) {
        var activities = IDPool.getObject(actid).getActivitiesForRoleId(roleid);
        var index = position < 0 ? activities.length : position;

        for(var i = activities.length; i > index; i--) {
            activities[i] = activities[i - 1];
        }

        var type = IDPool.getObject(roleid).subtype == "staff" ? "support" : "learning";
        activities[index] = new Activity(title, type);

        ChangeManager.activityAdded(activities[index]);
        return activities[index];
    },
    /**
     * Incluye un nuevo clfp en el diseño
     * @param clfp Clfp a añadir al diseño
     * @param position Posición del clfp en el conjunto de patrones
     * @param referenceClfpId Identificador del clfp utilizado como referencia
     */
    includeClfp : function(clfp, position, referenceClfpId) {
        if(position === undefined || position < 0) {
            var where = {
                act : this.data.flow,
                index : 0
            };
        } else {
            where = this.findParentActFor(referenceClfpId);
            where.index += position;
            /* 0 is before, 1 if after :) */
        }
        for(var i = where.act.clfps.length; i > where.index; i--) {
            where.act.clfps[i] = where.act.clfps[i - 1];
        }
        where.act.clfps[where.index] = clfp;

        clfp.createInitialInstances();
        if(LearningFlow.instanceid[referenceClfpId]) {
            LearningFlow.activateInstance(LearningFlow.instanceid[referenceClfpId]);
        }
        ChangeManager.clfpAdded(clfp);
    },
    /**
     * Sustituye las actividades de una fase por un clfp
     * @param clfp Clfp por el que se sustituye
     * @param act Fase a reemplazar
     */
    replaceActWithClfp : function(clfp, act) {
        if(act.type == "act") {
            ChangeManager.startGroup();
            act.makeIntoClfp(clfp);
            ChangeManager.clfpAdded(clfp);

            clfp.createInitialInstances();
            LearningFlow.automaticInstanceActivation(act.learners[0]);

            ChangeManager.endGroup();
        }
    },
    /**
     * Elimina el clfp cuyo id se proporciona
     * @param clfpId Identificador del clfp a eliminar
     */
    removeClfp : function(clfpId) {
        var clfp = IDPool.getObject(clfpId);

        GroupPatternManager.deleteClfpPatterns(clfp);
        DesignInstance.deleteClfpGroups(clfp);
        LearningFlow.removeOldActivatedInstances(clfp);

        var position = this.findParentActFor(clfpId);
        var clfp2 = position.act.removeClfpAt(position.index);
        ChangeManager.clfpRemoved(clfp2);
    },

    /**
     * Mueve un clfp a otra posición
     * @param clfp Clfp a mover
     * @param position Posición a la que se mueve el clfp
     */
    moveClfp : function(clfp, position) {
        ChangeManager.startGroup();

        var oldposition = this.findParentActFor(clfp.id);

        if(position.isReplace) {
            oldposition.act.removeClfpAt(oldposition.index);
            position.act.makeIntoClfp(clfp);
        } else {
            if(position.index > oldposition.index) {
                position.act.clfps.splice(position.index, 0, clfp);
                oldposition.act.removeClfpAt(oldposition.index);
            } else {
                oldposition.act.removeClfpAt(oldposition.index);
                position.act.clfps.splice(position.index, 0, clfp);
            }
        }

        ChangeManager.clfpMoved(clfp);
        ChangeManager.endGroup();
    },
    /**
     *
     * @param {Object} clfpId what you're looking for.
     * @param {Object} clfpAct don't use this! It's only for recursivity.
     * @return An array of CLFPs: the content of a single act whose flow has
     *            been turned into a CLFP sequence.
     */
    findParentActFor : function(clfpId, clfpAct) {
        if( typeof clfpAct == "undefined") {
            clfpAct = LearningDesign.data.flow;
        }

        for(var i in clfpAct.clfps) {
            var clfp = clfpAct.clfps[i];
            if(clfp.id == clfpId) {
                return {
                    act : clfpAct,
                    index : parseInt(i, 10)
                };
            } else {
                var flow = clfp.getFlow();
                /* list of acts */
                for(var j in flow) {
                    if(flow[j].type == "clfpact") {
                        var result = this.findParentActFor(clfpId, flow[j]);
                        if(result !== null) {
                            return result;
                        }
                    }
                }
            }
        }
        return null;
    },
    /**
     *
     * @param {Object} id what we are looking for.
     */
    findClfpParentOf : function(id) {
        for(var i in this.data.flow.clfps) {
            if(this.data.flow.clfps[i].id == id) {
                return {
                    clfp : null,
                    index : parseInt(i, 10)
                };
            } else {
                var result = this.helpFindingParentOf(id, this.data.flow.clfps[i]);
                if(result !== null) {
                    return result;
                }
            }
        }

        return {
            clfp : null,
            index : -1
        };
    },
    helpFindingParentOf : function(id, clfp) {
        var flow = clfp.getFlow();
        for(var i = 0; i < flow.length; i++) {
            if(flow[i].id == id) {
                return {
                    clfp : clfp,
                    index : i
                };
            } else if(flow[i].type == "clfpact") {
                for(var j in flow[i].clfps) {
                    if(flow[i].clfps[j].id == id) {
                        return {
                            clfp : clfp,
                            index : i
                        };
                    } else {
                        var result = this.helpFindingParentOf(id, flow[i].clfps[j]);
                        if(result !== null) {
                            return result;
                        }
                    }
                }
            }
        }

        return null;
    },
    findAssessmentParentOfFunction : function(assessmentFunctionId) {
        for(var i = 0; i < this.data.assessments.length; i++) {
            var assessment = this.data.assessments[i];
            for(var j = 0; j < assessment.functions.length; j++) {
                if(assessment.functions[j].id == assessmentFunctionId) {
                    return assessment;
                }
            }
        }

        return null;
    },
    findAssessmentParentOfPattern : function(patternId) {
        for(var i = 0; i < this.data.assessments.length; i++) {
            var assessment = this.data.assessments[i];
            for(var j = 0; j < assessment.patterns.length; j++) {
                if(assessment.patterns[j].id == patternId) {
                    return assessment;
                }
            }
        }

        return null;
    },
    /**
     *
     * @param {Object} activityId
     */
    findActParentOfActivity : function(activityId) {
        return this.helpFindingActParentOfActivity(activityId, this.data.flow, null);
    },
    /**
     * Don't use this function!
     *
     * @param {Object}
     *            activityId
     * @param {Object}
     *            act
     * @param {Object}
     *            clfp
     * @param {int}
     *            index
     */
    helpFindingActParentOfActivity : function(activityId, act, clfp, index) {
        if(act.type == "act") {
            for(var i in act.roleparts) {
                for(var j in act.roleparts[i].activities) {
                    if(act.roleparts[i].activities[j].id == activityId) {
                        return {
                            roleId : act.roleparts[i].roleId,
                            act : act,
                            clfp : clfp,
                            actIndex : index
                        };
                    }
                }
            }

            return null;
        } else {
            /* is a clfpact */

            for(i in act.clfps) {
                for( j = 0; j < act.clfps[i].flow.length; j++) {
                    var result = this.helpFindingActParentOfActivity(activityId, act.clfps[i].flow[j], act.clfps[i], j);
                    if(result !== null) {
                        return result;
                    }
                }
            }

        }

        return null;
    },
    /**
     * Obtiene el rol padre de un rol
     * @param child Rol hijo cuyo padre se desea obtener
     */
    findRoleParentOfRole : function(child) {
        return this.helpFindingRoleParentInClfpact(child, this.data.flow, null);
    },
    helpFindingRoleParentInClfpact : function(child, act, potentialparent) {
        for(var i in act.clfps) {
            var clfp = act.clfps[i];
            for(var j = 0; j < clfp.ownRoles.length; j++) {
                var result = this.helpFindingRoleParentInRole(child, act.clfps[i].ownRoles[j], potentialparent);
                if(result != null) {
                    return result;
                }
            }

            for( j = 0; j < clfp.flow.length; j++) {
                var subact = clfp.flow[j];
                if(subact.type == "clfpact") {
                    potentialparent = child.subtype == "learner" ? IDPool.objects[subact.learners[0]] : IDPool.objects[subact.staff[0]];
                    result = this.helpFindingRoleParentInClfpact(child, subact, potentialparent);
                    if(result != null) {
                        return result;
                    }
                }
            }
        }

        return null;
    },
    helpFindingRoleParentInRole : function(child, role, potentialparent) {
        if(role.id == child.id) {
            return potentialparent;
        } else {
            for(var i = 0; i < role.children.length; i++) {
                var result = this.helpFindingRoleParentInRole(child, role.children[i], role);
                if(result != null) {
                    return result;
                }
            }
            return null;
        }
    },
    /**
     * Prueba de la búsqueda de roles
     * @return Obtiene la lista de roles con el padre de cada uno de ellos
     */
    testFindingRoles : function() {
        var list = new Array();
        for(var i in IDPool.objects) {
            var obj = IDPool.objects[i];
            if(obj.type == "role") {
                list[obj.id] = {
                    role : obj,
                    parent : LearningDesign.findRoleParentOfRole(obj)
                };
            }
        }
        return list;
    },
    /**
     * Obtiene los roles que aparecen en el diseño
     * @return Obtiene la lista de roles con el padre de cada uno de ellos
     */
    getRoles : function() {
        var list = new Array();
        for(var i in IDPool.objects) {
            var obj = IDPool.objects[i];
            if(obj.type == "role") {
                list[obj.id] = {
                    role : obj,
                    parent : LearningDesign.findRoleParentOfRole(obj)
                };
            }
        }
        return list;
    }
};
