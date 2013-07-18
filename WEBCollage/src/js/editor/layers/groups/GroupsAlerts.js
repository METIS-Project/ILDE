/*global MenuManager, i18n, DesignInstance  */

var GroupsAlerts = {

    circleRadius : 8,

    circlePaint : {
        fill : [250, 250, 250, 1.0],
        emphasisFill : [250, 250, 250, 1.0],
        stroke : {
            color : "black",
            width : 1,
            cap : "butt",
            join : 1
        }
    },

    paint : function(flowRenderer, participantFlow) {
        this.paintGroupPatternsAlerts(flowRenderer.renderData.mainBlock, flowRenderer, participantFlow);
    },
    paintGroupPatternsAlerts : function(block, flowRenderer, participantFlow) {
        for (var i = 0; i < block.renderClfps.length; i++) {
            if (i==0) {
                //Obtener y pintar alertas a la izquierda
                var alerts = GroupsAlerts.showSideAlert(block.renderClfps, flowRenderer, "left");
                if (alerts){
                    //Pintar alertas del clfp
                    var clfp = block.renderClfps[i].clfp;
                    var imageLeftGroup = flowRenderer.bigGroup.createGroup();

                    var imageLeft = imageLeftGroup.createImage({
                        x : flowRenderer.graphicElements.clfps[clfp.id].x + flowRenderer.graphicElements.clfps[clfp.id].width / 2 - 62,
                        y : flowRenderer.graphicElements.clfps[clfp.id].y - 32,
                        width : 18,
                        height : 18,
                        src : "images/icons/person.png"
                    });
                    flowRenderer.animator.addNoAnimStuff(imageLeft);
                    MenuManager.registerThing(imageLeft, {
                        getItems : function(data) {
                            return GroupsAlerts.getGroupPatternAlertsMenu(data);
                        },
                        data : {
                            alerts : [{
                                text : i18n.get("groupPatternAlerts.leftAlert"),
                                help : ""
                            }]
                        },
                        menuStyle : "groupPatternAlerts"
                    });
                }
            }

            if (i==0) {
                //Obtener y pintar alertas a la derecha
                alerts = GroupsAlerts.showSideAlert(block.renderClfps, flowRenderer, "right");
                if (alerts){
                    //Pintar alertas del clfp
                    var clfp = block.renderClfps[i].clfp;
                    var width = clfp.getRenderer().getSize(clfp, flowRenderer.uiListener).width * this.scale;
                    var imageRightGroup = flowRenderer.bigGroup.createGroup();

                    var imageRight = imageRightGroup.createImage({
                        x : flowRenderer.graphicElements.clfps[clfp.id].x + flowRenderer.graphicElements.clfps[clfp.id].width / 2 + 62 - 18,
                        y : flowRenderer.graphicElements.clfps[clfp.id].y - 32,
                        width : 18,
                        height : 18,
                        src : "images/icons/person.png"
                    });
                    flowRenderer.animator.addNoAnimStuff(imageRight);
                    MenuManager.registerThing(imageRight, {
                        getItems : function(data) {
                            return GroupsAlerts.getGroupPatternAlertsMenu(data);
                        },
                        data : {
                            alerts : [{
                                text : i18n.get("groupPatternAlerts.rightAlert"),
                                help : ""
                            }]
                        },
                        menuStyle : "groupPatternAlerts"
                    });
                }
            }

            var clfpid = block.renderClfps[i].clfp.id;
            var instanceid = flowRenderer.uiListener.instanceid[clfpid];
            if (!instanceid) {
                instanceid = 0;
            }
            //Obtener y pintar alertas para cada un de las fases
            for (var j = 0; j < block.renderClfps[i].clfp.flow.length; j++) {
                var actid = block.renderClfps[i].clfp.flow[j].id;
                //var roleStaff = block.renderClfps[i].clfp.flow[j].staff[0];
                //var instancesStaff = DesignInstance.instanciasGrupo(roleStaff);
                //var idinstStaff = instancesStaff[instancesStaff.length - 1].id;
                var posx = participantFlow.acts[actid].x;
                var posy = participantFlow.acts[actid].y;

                //var posx = flowRenderer.graphicElements.clfps[clfpid].x + flowRenderer.graphicElements.clfps[clfpid].width;
                //var posy = flowRenderer.graphicElements.acts[actid].instances[idinstStaff].y + flowRenderer.graphicElements.acts[actid].instances[idinstStaff].height / 2;
                var size = 16;
                var halfsize = size / 2;
                var distance = 40;
                
                //Pintar cuadrado para las opciones del patrón de grupo
                var circleGroup = flowRenderer.bigGroup.createGroup();
                var circle = circleGroup.createRect({
                    x : posx + (distance * block.renderClfps[i].scale) - halfsize,
                    y : posy - halfsize,
                    width : size,
                    height : size
                }).setFill(this.circlePaint.fill).setStroke(this.circlePaint.stroke);
                flowRenderer.animator.addNoAnimStuff(circle);
                //Añadir menú de opciones del patrón de grupo
                MenuManager.registerThing(circle, {
                    getItems : function(data) {
                        return GroupsAlerts.getGroupMenu(data);
                    },
                    data : {
                        actid : actid,
                        instanceid : instanceid,
                        clfpid : clfpid,
                        uiListener : flowRenderer.uiListener
                    },
                    menuStyle : "groupPatternOptions"
                });

                //Pintar alertas de la fase
                alerts = GroupsAlerts.getActAlerts(block.renderClfps[i].clfp, block.renderClfps[i].clfp.flow[j], flowRenderer, instanceid);
                if (alerts.length > 0) {
                    var alertdistance = 5;
                    var imageActGroup = flowRenderer.bigGroup.createGroup();
                    var imageAct = imageActGroup.createImage({
                        x : posx + halfsize + alertdistance + (distance * block.renderClfps[i].scale),
                        y : posy - halfsize,
                        width : 18,
                        height : 18,
                        src : "images/icons/person.png"
                    });
                    flowRenderer.animator.addNoAnimStuff(imageAct);

                    MenuManager.registerThing(imageAct, {
                        getItems : function(data) {
                            return GroupsAlerts.getGroupPatternAlertsMenu(data);
                        },
                        data : {
                            alerts : alerts
                        },
                        menuStyle : "groupPatternAlerts"
                    });
                }
            }

            for (var s = 0; s < block.renderClfps[i].subBlocks.length; s++) {
                this.paintGroupPatternsAlerts(block.renderClfps[i].subBlocks[s], flowRenderer, participantFlow);
            }
        }
    },
    actHasAlerts : function(clfp, act, flowRenderer, instanceId) {
        return this.getActAlerts(clfp, act, flowRenderer, instanceId).length > 0;
    },
    getActAlerts : function(clfp, act, flowRenderer, instanceId) {
        var alerts = new Array();
        alerts.addAlerts = function(list) {
            for (var i = 0; i < list.length; i++) {
                this.push(list[i]);
            }
        }
        var actstate = GroupPatternManager.getActState(instanceId, act.id);
        if (actstate.gn) {
            alerts = alerts.concat(actstate.gn.alerts);
        }
        if (actstate.pa) {
            alerts = alerts.concat(actstate.pa.alerts);
        }
        //Comentar esta línea para mostrar las otras posibles alertas
        //return alerts;

        if (Context.isClassSelected()) {
            //Obtener alertas de grupos vacíos
            if (instanceId) {
                var instances = DesignInstance.instanciasGrupoProcedenInstancia(act.learners[0], instanceId);
                var disponibles = IDPool.getObject(instanceId).participants;

            } else {
                var instances = DesignInstance.instanciasGrupo(act.learners[0]);
                var disponibles = ClfpsCommon.obtainAvailableStudentsClfpMain(clfp);
            }
            
            if (disponibles.length == 0){
                for (var i = 0; i < instances.length; i++) {
                    if (instances[i].participants.length == 0) {
                        alerts.push({
                            text : i18n.get("groupPattern.noAvailableParticipants"),
                            help : i18n.get("groupPattern.noAvailableParticipants.help"), 
                            icon : "gpaRed"
                        });
                        break;
                    }
                }
            }
            else{
                for (var i = 0; i < instances.length; i++) {
                    if (instances[i].participants.length == 0) {
                        alerts.push({
                            text : i18n.get("groupPattern.emptyGroup"),
                            help : i18n.get("groupPattern.emptyGroup.help"),
                            icon : "gpaRed"
                        });
                        break;
                    }
                }
            }

            //Obtener alertas de alumnos no asignados
            var grupo = DesignInstance.getGrupo(act.learners[0]);
            for (var i = 0; i < disponibles.length; i++) {
                if (!DesignInstance.perteneceGrupo(grupo, disponibles[i])) {
                    alerts.push({
                        text : i18n.get("groupPattern.notAsigned"),
                        help : i18n.get("groupPattern.notAsigned.help"),
                        icon : "gpa"
                    });
                    break;
                }
            }

            //Obtener alertas de profesores no asignados
            grupo = DesignInstance.getGrupo(act.staff[0]);
            var available = ClfpsCommon.obtainAvailableTeachers(clfp, grupo.instances[0].id);
            //Si no hay profesores disponibles para ser asignados y está vacío se muestra alerta
            if (available.length == 0 && grupo.instances[0].participants.length == 0){
                alerts.push({
                    text: i18n.get("groupPattern.noAvailableTeachers"),
                    help: i18n.get("groupPattern.noAvailableTeachers.help"),
                    icon : "gpaRed"
                });
                
            }else{
                //Mostrar alerta de que no hay ningún profesor asignado
                if (grupo.instances[0].participants.length == 0) {
                    alerts.push({
                        text : i18n.get("groupPattern.notAsignedTeacher"),
                        help : i18n.get("groupPattern.notAsignedTeacher.help"),
                        icon : "gpaRed"
                    });
                }else{
                    if (available.length > 0){
                        alerts.push({
                            text : i18n.get("groupPattern.notAsignedSomeTeachers"),
                            help : i18n.get("groupPattern.notAsignedSomeTeachers.help"),
                            icon : "gpa"
                        });
                    }                   
                }
            }
        }

        return alerts;
    },
    showSideAlert : function(renderClfps, flowRenderer, side) {
        var clfpTop = renderClfps[0].clfp;
        //Obtenemos la instancia de la que depende actualmente el patrón (si es que es un patrón dependiente)

        if (flowRenderer.uiListener.instanceid[clfpTop.id]) {
            var instanciaDepende = IDPool.getObject(flowRenderer.uiListener.instanceid[clfpTop.id]);
            var g = DesignInstance.grupoInstancia(instanciaDepende);
            var clfpGrupo = DesignInstance.clfpRole(IDPool.getObject(g.roleid));
            //Obtemos las posibles instancias de las que puede depender el patrón
            var instanciasDependePatron = ClfpsCommon.getInstances(clfpGrupo, g.roleid, flowRenderer.uiListener);
            //Obtenemos la posición que ocupa entre ellas la instancia de la que depende actualmente
            var pos = -1;
            for (var i = 0; i < instanciasDependePatron.length; i++) {
                if (instanciasDependePatron[i].id == instanciaDepende.id) {
                    pos = i;
                }
            }

            if (side == "left") {
                var startIndex = 0;
                var endIndex = pos;
            } else {
                startIndex = pos + 1;
                endIndex = instanciasDependePatron.length;
            }
                
            //Para cada patrón que están uno a continuación de otro se comprueba si tiene alertas. Si alguno de ellos tiene alertas hay que mostrarlas
            for (var rc=0; rc < renderClfps.length; rc++){
                var clfp = renderClfps[rc].clfp;
                //Para cada posible instancia de la que puede depender el patrón
                for (var i = startIndex; i < endIndex; i++) {
                    for (var j = 0; j < clfp.flow.length; j++) {
                        var act = clfp.flow[j];
                        if (GroupsAlerts.actHasAlerts(clfp, act, flowRenderer, instanciasDependePatron[i].id)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    },
    /**
     * @param data
     */
    getGroupMenu : function(data) {
        var items = new Array();
        items.push({
            label : i18n.get("groupPattern.manage"),
            icon : "edit",
            onClick : function(data) {
                GroupPatternsDialog.open(data.actid, data.clfpid, data.instanceid);
            },
            data : data,
            help : i18n.get("help.groupPattern.manage")
        });

        items.push({
            isSeparator : true
        });
        this.getGroupMenuAddUpdateItem(items, data, "gn");
        this.getGroupMenuAddUpdateItem(items, data, "pa");

        var patterns = GroupPatternManager.getPatternsForAct(data.actid);
        this.getGroupMenuAddConfig(items, data, patterns.gn);
        this.getGroupMenuAddConfig(items, data, patterns.pa);

        return items;
    },
    getGroupMenuAddConfig : function(items, data, patterns) {
        for (var i = 0; i < patterns.length; i++) {
            //Sólo mostrar los elementos correspondientes a instancia activa del cflp
            if (patterns[i].instanceid == data.instanceid){
                items.push({
                    isSeparator : true
                });
                items.push({
                    icon : "edit",
                    label : patterns[i].getTitle() + "..."
                });
                var pattern = patterns[i];
                if (pattern.getMenuItems) {
                    var subitems = pattern.getMenuItems();
                    for (var j = 0; j < subitems.length; j++) {
                        items.push({
                            label : subitems[j].label,
                            help : subitems[j].label,
                            isSubMenu : true,
                            onClick : function(data) {
                                data.pattern.menuItemClicked(data.index);
                            },
                            data : {
                                pattern : pattern,
                                index : j
                            }
                        });
                    }
                }
            }

        }
    },
    getGroupMenuAddUpdateItem : function(items, data, type) {
        var state = GroupPatternManager.getActTypeState(data.instanceid, data.actid, type);
        var gn = type == "gn";
        var icon, label, enabled;

        if (state && state.ok) {
            icon = gn ? "gpok" : "gpok";
            enabled = false;
            label = i18n.get( gn ? "groupPattern.updateGroupNumberOK" : "groupPattern.updateGroupCompositionOK");
        } else if (state && state.fixable) {
            icon = gn ? "gpg" : "gpp";
            enabled = true;
            label = i18n.get( gn ? "groupPattern.updateGroupNumber" : "groupPattern.updateGroupComposition");
        } else {
            icon = "gpd";
            enabled = false;
            label = i18n.get( gn ? "groupPattern.disabledGroupNumber" : "groupPattern.disabledGroupComposition");
        }

        items.push({
            label : label,
            icon : icon,
            onClick : enabled ? function(data) {
                GroupPatternManager.updatePattern(data);
            } : null,
            data : state ? state.data.completekey : null,
            help : state ? state.why : i18n.get("groupPattern.nopatterns")
        });
    },
    /* to remove */
    /*getGroupPatternMenu : function(data) {
        var items = new Array();
        return items;

        var capabilities = data.gp.getCapabilities();

        if (capabilities.groupConfig) {
            var cfgn = data.gp.canFixGroupNumber(data.uiListener.instanceid[IDPool.getObject(data.gp.clfpId).id]);

            if (cfgn.canFix == "yes") {
                var icon = "gpg";
                var enabled = true;
                var label = i18n.get("groupPattern.updateGroupNumber");
            } else if (cfgn.canFix == "no") {
                icon = "gpd";
                enabled = false;
                label = i18n.get("groupPattern.disabledGroupNumber");
            } else {// "noneed"
                icon = "gpok";
                enabled = false;
                label = i18n.get("groupPattern.updateGroupNumberOK");
            }

            items.push({
                label : label,
                icon : icon,
                onClick : enabled ? function(data) {
                    var clfp = IDPool.getObject(data.gp.clfpId);
                    var instanceId = data.uiListener.instanceid[clfp.id];
                    var proposed = data.gp.getProposedGroupNumber(instanceId);

                    var idGrupo = Context.getStudentRoles(data.gp.actId)[0].id;
                    var instances = ClfpsCommon.getInstances(clfp, idGrupo, LearningFlow);
                    var numberInstances = instances.length;

                    //Clono la  última instancia el número de veces necesario
                    var instanceCloneId = instances[instances.length - 1].id;
                    //Guardo los identificadores de las instancias a borrar
                    var instanceDeleteIds = new Array();
                    for (var i = instances.length - 1; i >= proposed; i--) {
                        instanceDeleteIds.push(instances[i].id);
                    }
                    for (var i = numberInstances; i < proposed; i++) {
                        clfp.clonar(instanceCloneId);
                    }
                    for (var i = 0; i < instanceDeleteIds.length; i++) {
                        ClfpsCommon.deleteInstance(instanceDeleteIds[i], clfp);
                    }
                    Loader.save("");
                } : null,
                data : data,
                help : cfgn.help
            });
        }

        if (capabilities.participantConfig) {
            var cfgc = data.gp.canFixGroupComposition(data.uiListener.instanceid[IDPool.getObject(data.gp.clfpId).id]);

            if (cfgc.canFix == "yes") {
                var icon = "gpp";
                var enabled = true;
                var label = i18n.get("groupPattern.updateGroupComposition");
            } else if (cfgc.canFix == "no") {
                icon = "gpd";
                enabled = false;
                label = i18n.get("groupPattern.disabledGroupComposition");
            } else {// "noneed"
                icon = "gpok";
                enabled = false;
                label = i18n.get("groupPattern.updateGroupCompositionOK");
            }
            items.push({
                label : label,
                icon : icon,
                onClick : enabled ? function(data) {
                    var clfp = IDPool.getObject(data.gp.clfpId);
                    var instanceId = data.uiListener.instanceid[clfp.id];
                    var proposed = data.gp.getProposedGroupsComposition(instanceId);

                    var idGrupo = Context.getStudentRoles(data.gp.actId)[0].id;
                    var instances = ClfpsCommon.getInstances(clfp, idGrupo, LearningFlow);
                    //Clono la  última instancia el número de veces necesario
                    var instanceCloneId = instances[instances.length - 1].id;
                    //Debo borrar todas las que había
                    var instanceDeleteIds = new Array();
                    for (var i = 0; i < instances.length; i++) {
                        instanceDeleteIds.push(instances[i].id);
                    }
                    for (var i = 0; i < proposed.length; i++) {
                        clfp.clonar(instanceCloneId);
                    }
                    for (var i = 0; i < instanceDeleteIds.length; i++) {
                        ClfpsCommon.deleteInstance(instanceDeleteIds[i], clfp);
                    }
                    instances = ClfpsCommon.getInstances(clfp, idGrupo, LearningFlow);
                    //Asignación de participantes a los grupos creados
                    for (var i = 0; i < instances.length; i++) {
                        for (var j = 0; j < proposed[i].length; j++) {
                            clfp.asignar(instances[i], proposed[i][j]);
                        }
                    }
                    Loader.save("");

                } : null,
                data : data,
                help : cfgc.help
            });
        }

        if (capabilities.setupMenuItems) {
            items.push({
                isSeparator : true
            });

            var menuItems = data.gp.getMenuItems();
            for (var i in menuItems) {
                items.push({
                    label : menuItems[i].label,
                    icon : "edit",
                    onClick : function(data) {
                        data.maindata.gp.menuItemClicked(data.itemindex);
                    },
                    data : {
                        maindata : data,
                        itemindex : i
                    },
                    help : menuItems[i].help
                });
            }
        }

        items.push({
            isSeparator : true
        });
        items.push({
            label : i18n.get("groupPattern.delete"),
            icon : "delete",
            onClick : function(data) {
                GroupPattern.deletePattern(data.gp.id);
                Loader.save("");
            },
            data : data,
            help : i18n.get("help.groupPattern.delete")
        });
        items.push({
            isSeparator : true
        });
        items.push({
            label : i18n.get("groupPattern.documentation"),
            icon : "help",
            onClick : function(data) {
                alert("not implemented yet");
            },
            data : data,
            help : i18n.get("help.groupPattern.documentation")
        });

        return items;
    },*/
    getDefaultMenu : function(data) {
        var items = new Array();
        items.push({
            label : i18n.get("groupPattern.add"),
            icon : "add",
            onClick : function(data) {
                alert("not implemented yet");
            },
            data : data,
            help : i18n.get("help.groupPattern.add")
        });
        return items;
    },
    /**
     * @param data
     */
    getGroupPatternAlertsMenu : function(data) {
        var items = new Array();
        
        for (var i = 0; i < data.alerts.length; i++) {
            if (typeof data.alerts[i].icon == 'undefined'){
                var icon = "gpa";
            }else{
                icon = data.alerts[i].icon;
            }
            items.push({
                icon: icon,
                label : data.alerts[i].text,
                help : data.alerts[i].help
            });
        }
        return items;
    }
};
