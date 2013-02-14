/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var LearningFlowMenus = {
    
    getClfpSelectMenu : function(obj, data) {
        var items = new Array();
        items.push({
            label : i18n.getReplaced1("flow.select.clfp", obj.getTitle()),
            icon : "select",
            onClick : function(data) {
                LearningFlow.activate(data);
            },
            data : data,
            help : i18n.get("help.flow.select.clfp")
        });
        return items;  
    },
    getAssessmentFlowMenu: function(obj, data) {
        var items = new Array();

        items.push({
            label : i18n.get("flow.assessment.edit"),
            icon : "editassessment",
            onClick : function(data) {
                LearningFlow.activate(data);
            },
            data : data,
            help : i18n.get("help.flow.assessment.edit")
        });

        items.push({
            isSeparator : true
        });

        items.push({
            label : i18n.get("assessment.delete"),
            icon : "delete",
            onClick : function(data) {
                LearningFlow.openRemoveAssessmentDialog(data);
            },
            data : data,
            help : i18n.get("help.assessment.delete")
        });

        return items;
    },
    getActivityMenu : function(obj, data) {
        return ActivitiesPainter.createMenu(data);
    },
    getClfpMenu : function(obj, data) {
        /* there is a CLFP in the view and we are over an object! */
        var items = new Array();

        /* menu for CLFP */
        var clfp = data.clfp;
        var clfpMenu = clfp.getEditionMenu(data);
        if(clfpMenu) {
            items.push({
                label : dojo.string.substitute(i18n.get("flow.clfp.configure"), [clfp.title]),
                icon : "edit"
            });

            for(var i in clfpMenu) {
                clfpMenu[i].isSubMenu = true;
                items.push(clfpMenu[i]);
            }

            items.push({
                isSeparator : true
            });
        }

        if(obj.type == "role") {
            items.push({
                label : dojo.string.substitute(i18n.get("roles.edit.title"), [obj.getTitle()]),
                icon : "edit",
                onClick : function(data) {
                    LearningFlow.activate(data);
                },
                data : data,
                help : i18n.get("help.roles.edit.title")
            });

            items.push({
                isSeparator : true
            });
            //Sólo se muestra la opción de clonar en aquellos que esté permitido
            if(data.letClone != false) {
                items.push({
                    label : i18n.get("learningflow.clonar"),
                    icon : "copy",
                    onClick : function(data) {
                        data.clfp.clonar(data.idInstancia, data.level);
                        Loader.save("");
                    },
                    data : data,
                    help : i18n.get("learningflow.clonar.help")
                });
            }
            if(data.letDelete == true) {
                items.push({
                    label : i18n.get("learningflow.borrar"),
                    icon : "delete",
                    onClick : function(data) {
                        ClfpsCommon.deleteInstance(data.idInstancia, data.clfp);
                        Loader.save("");
                    },
                    data : data,
                    help : i18n.get("learningflow.borrar.help")
                });
            }
            if (data.letAsignUsers==true){
                items.push({
                    label: i18n.get("learningflow.asignarparticipantes"),
                    icon: "edit",
                    onClick: function(data) {
                        InstanceStudentManagement.showStudents(data.idInstancia, data.clfp);
                    },
                    data: data,
                    help: i18n.get("learningflow.asignarparticipantes.help")
                });
            }
            items.push({
                label: i18n.get("learningflow.verparticipantes"),
                icon: "student",
                data: data,
                help: InstanceStudentManagement.showTooltipAssignedStudents(data.idInstancia)
            });
            if (data.letShowClfp==true){
                items.push( {
                    isSeparator : true
                });
                var clfpDepende = DesignInstance.clfpDependeInstanciaGrupo(IDPool.getObject(data.idInstancia));
                items.push({
                    label: i18n.get("learningflowGroups.mostrarclfp"),
                    icon: clfpDepende.patternid,
                    onClick: function(data) {                            
                        LearningFlow.activateInstance(data.idInstancia);
                    },
                    data: data,
                    help: i18n.get("learningflowGroups.mostrarclfp.help")
                });
            }
        }
            
        //Se muestra este menú si la asignación de profesores se realiza en el círculo
        if (data.allowAssignTeacher==1) {
            items.push({
                label: i18n.get("learningflow.asignarprofesores"),
                icon: "edit",
                onClick: function(data) {
                    InstanceTeacherManagement.showTeachers(DesignInstance.instanciasGrupo(data.role)[0].id, data.clfp);
                },
                data: data,
                help: i18n.get("learningflow.asignarprofesores.help")
            });
            items.push({
                label: i18n.get("learningflow.verprofesores"),
                icon: "teacher",
                data: data,
                help: InstanceTeacherManagement.showTooltipAssignedTeachers(DesignInstance.instanciasGrupo(data.role)[0].id)
            });
        }
        else{

            if(obj.type == "act") {
                items.push({
                    label : i18n.getReplaced1("flow.clfp.substitute", obj.getTitle()),
                    icon : "replace",
                    onClick : function(data) {
                        LearningFlow.replaceActWithClfp(data);
                    },
                    data : data,
                    help : i18n.get("help.flow.clfp.substitute")
                });
                items.push({
                    isSeparator : true
                });
            }

            if(obj.type == "act" || obj.type == "clfpact") {
                items.push({
                    label : i18n.get("flow.clfp.add"),
                    icon : "add"
                });
                items.push({
                    label : i18n.getReplaced1("flow.clfp.add.before", clfp.getTitle()),
                    onClick : function(data) {
                        LearningFlow.includePattern(0, data);
                    /* before */
                    },
                    isSubMenu : true,
                    data : data,
                    help : i18n.get("help.flow.clfp.add.before")
                });
                items.push({
                    label : i18n.getReplaced1("flow.clfp.add.after", clfp.getTitle()),
                    onClick : function(data) {
                        LearningFlow.includePattern(1, data);
                    /* after */
                    },
                    isSubMenu : true,
                    data : data,
                    help : i18n.get("help.flow.clfp.add.after")
                });

                items.push({
                    isSeparator : true
                });

                items.push({
                    label : dojo.string.substitute(i18n.get("flow.clfp.remove"), [data.clfp.title]),
                    icon : "delete",
                    onClick : function(data) {
                        LearningFlow.openRemoveClfpDialog(data);
                    },
                    data : data,
                    help : i18n.get("help.flow.clfp.remove")
                });
            }
        }

        items.push({
            isSeparator : true
        });

        items.push({
            label : dojo.string.substitute(i18n.get("common.pattern.doc"), [data.clfp.title]),
            icon : "help",
            onClick : function(data) {
                PatternSelector.showHelp(data.clfp.patternid);
            },
            data : data,
            help : i18n.get("help.common.pattern.doc")
        });

        return items;
    },
    getMenu: function(data) {
        
        var obj = IDPool.getObject(data.id);

        if(obj.type == "clfp") {
            return this.getClfpSelectMenu(obj, data);
        } else if (LearningFlowSelectMode.isSelecting()) {
            return LearningFlowSelectMode.getMenu(obj, data);
        } else if (obj.type == "assessmentflow") {
            return this.getAssessmentFlowMenu(obj, data);
        } else if (obj.type == "activity") {
            return this.getActivityMenu(obj, data);
        } else if(data.clfp) {
            return this.getClfpMenu(obj, data);
        }else {
            return null;
        }
    }
};

