/**
 * @class Renderer del patrón simulación
 */
var SimulationRenderer = {

    /**
	 * Hueco para la altura
	 */
    gap : 40,

    /**
	 * Posición X central en el diseño
	 */
    centerX : 200,

    /**
	 * Separación entre fases
	 */
    halfDistanceBetweenPhases : 20,
    /**
	 * Distancia entre roles en el diseño
	 */
    distanceBetweenPlayRoles : 80,
    /**
	 * Distancia entre grupos de simulación en el diseño
	 */
    distanceBetweenSimGroups : 80,

    /**
	 * Distancia que separada dos instancias de un rol
	 */
    distanceBetweenPlayRolesInstances : 50,
    /**
	 * Separación entre dos roles diferentes
	 */
    separationBetweenPlayRolesInstances : 30,
    /**
	 * Distancia que separada dos instancias de un grupo de simulación
	 */
    distanceBetweenSimGroupsInstances : 40,
    /**
	 * Separación entre dos grupos de simulación diferentes
	 */
    separationBetweenSimGroupsInstances : 28,
    /**
	 * Número de estudiantes a representar en la fase individual
	 */
    numberOfIndividualStudents : 10,
    /**
	 * Distancia que separada dos instancias de estudiante
	 */
    distanceBetweenIndividualStudents : 25,

    /**
	 * Imagen de un grupo de simulación
	 */
    simImage : {
        src : "images/clfps/simulation/sim.png",
        emphasis : "images/clfps/simulation/sim_emph.png",
        width : 128,
        height : 82
    },

    /**
	 * Imagen de un rol
	 */
    roleImage : {
        width : 46,
        height : 33
    },

    /**
	 * Separación entre cuadrados
	 */
    simMiniGap : 3,

    /**
	 * Distancia al profesor
	 */
    teacherDistance : 35,
    /**
	 * Radio del círculo del profesor
	 */
    teacherRadius : 12,

    /**
	 * Longitud de la mitad de la línea que separa fases
	 */
    halfLineLength : 140,

    /**
	 * Tamaño de un estudiante
	 */
    studentSize : 15, /* (this.abcImage.width - this.miniGap) / 2 */
    /**
	 * Propiedades gráficas de un estudiante
	 */
    studentPaint : {
        fill : [178, 0, 0, 1.0],
        emphasisFill : [204, 0, 0, 1.0],
        stroke : {
            color : "black",
            width : 1,
            cap : "butt",
            join : 1
        }
    },
    /**
	 * Propiedades gráficas de un estudiante seleccionado
	 */
    studentPaintSelected : {
        fill : [178, 0, 0, 1.0],
        emphasisFill : [204, 0, 0, 1.0],
        stroke : {
            color : "black",
            width : 2.5,
            cap : "butt",
            join : 1
        }
    },

    text : {
        font : {
            family : "Arial",
            size : "9pt",
            weight : "bold"
        },
        fill : "black",
        emphasisFill : [100, 100, 100, 1.0]
    },

    shareText : {
        font : {
            family : "Arial",
            size : "11pt",
            weight : "bold"
        },
        fill : "black",
        emphasisFill : [100, 100, 100, 1.0]
    },

    roleNameText : {
        font : {
            family : "Arial",
            size : "8pt",
            weight : "bold"
        },
        fill : [50, 50, 50, 1.0],
        emphasisFill : [150, 150, 150, 1.0]
    },

    lineStroke : {
        color : "black",
        width : 2,
        cap : "butt",
        join : 2
    },

    /**
	 * Propiedades gráficas de un profesor
	 */
    teacherPaint : {
        fill : [170, 170, 170, 1.0],
        emphasisFill : [200, 200, 200, 1.0],
        stroke : {
            color : "black",
            width : 1,
            cap : "butt",
            join : 1
        }
    },

    /**
	 * Obtiene las imágenes de un rol en función del índice
	 * @param index Índice del rol cuyas imágenes se desea obtener
	 * @return Imágenes del rol para el índice especificado
	 */
    getRoleImages : function(index) {
        return {
            src : "images/clfps/simulation/r_" + index + ".png",
            emphasis : "images/clfps/simulation/r_" + index + "_emph.png",
            selected : "images/clfps/simulation/r_" + index + "_s.png",
            emphasisSelected : "images/clfps/simulation/r_" + index + "_emph_s.png"
        };
    },
    /**
	 * Obtiene el tamaño de la instancia del clfp
	 * @param clfp Clfp del que se va a obtener su tamaño
	 * @param uiListener
	 * @return Ancho y alto del clfp
	 */
    getSize : function(clfp, uiListener) {
        
        
        var width = 2 * this.centerX;
        //Obtener el tamaño ocupado por los roles
        var act = clfp.flow[1];
        var sizeRoles = 0;
        for(var i = 0; i < act.learners.length; i++) {
            var instancias = ClfpsCommon.getInstances(clfp, act.learners[i], uiListener);
            sizeRoles = sizeRoles + this.distanceBetweenPlayRolesInstances * instancias.length;
        }
        sizeRoles = sizeRoles + (act.learners.length - 1) * this.separationBetweenPlayRolesInstances;
        //Comprobar si supera al necesario para el individual
        if(sizeRoles > this.centerX) {
            width = sizeRoles + 140;
        }
        
        //Obtener el tamaño ocupado por los grupos de simulación
        act = clfp.flow[2];
        var sizeSimGroups = 0;
        for( i = 0; i < act.learners.length; i++) {
            instancias = ClfpsCommon.getInstances(clfp, act.learners[i], uiListener);
            sizeSimGroups = sizeSimGroups + this.distanceBetweenSimGroupsInstances * instancias.length;
        }
        sizeSimGroups = sizeSimGroups + (act.learners.length - 1) * this.separationBetweenSimGroupsInstances;
        //Comprobar si supera al necesario para el individual o para los roles
        if(sizeSimGroups > this.centerX && sizeSimGroups + 140 > width) {
            width = sizeSimGroups + 140;
        }
        
        var size = {
            width : width,
            height: 3*this.gap + 3*this.studentSize + this.roleImage.height + this.simMiniGap + this.simImage.height + 10*this.halfDistanceBetweenPhases,
            acts : {}
        };
        
       
        var phaseHeight = new Array(); //Altura de cada fase
        var accumulatedPhaseHeight = new Array(); //Altura acumulada de las fases
        
        phaseHeight[0]= this.studentSize + this.halfDistanceBetweenPhases;
        phaseHeight[1]= this.roleImage.height + this.halfDistanceBetweenPhases + this.halfDistanceBetweenPhases;
        phaseHeight[2]= 2 * this.studentSize + this.simMiniGap + this.halfDistanceBetweenPhases + this.halfDistanceBetweenPhases;
        phaseHeight[3]= this.simImage.height + this.halfDistanceBetweenPhases + this.halfDistanceBetweenPhases;
        
        accumulatedPhaseHeight[0]=0;
        for (var i=1;i<phaseHeight.length;i++)
        {
            accumulatedPhaseHeight[i]=accumulatedPhaseHeight[i-1]+phaseHeight[i-1]+this.halfDistanceBetweenPhases;
        }
        
        var openactheight = 0;
        
        for(var i = 0; i < clfp.getFlow().length; i++) {
            var actid = clfp.getFlow()[i].id;
            var actsize = ClfpsRenderingCommon.getOpenActSize(actid, uiListener);
            var y = openactheight + accumulatedPhaseHeight[i] + this.gap;

            size.acts[actid] = {
                pre : {
                    x : 0,
                    y : y,
                    width : size.width,
                    height : phaseHeight[i]
                },
                post : {
                    x : 0,
                    y : y + phaseHeight[i] + actsize.height
                }
            };
            openactheight += actsize.height;
        }

        size.height += openactheight;
        return size;

    },
    /**
	 * Dibuja la linea de separación entre fases en la instanciación
	 * @param surface Superficie donde se dibujará la línea
	 * @param y Altura a la que se dibujará la línea
	 * @param clfp
	 * @param uiListener
	 */
    paintLineInstance : function(surface, y, clfp, uiListener) {
        var x2 = this.centerX + this.halfLineLength;
        if(x2 < this.getSize(clfp, uiListener).width - 25) {
            x2 = this.getSize(clfp, uiListener).width - 25;
        }
        surface.createLine({
            x1 : this.centerX - this.halfLineLength,
            y1 : y,
            x2 : x2,
            y2 : y
        }).setStroke(this.lineStroke);
    },
    /**
	 * Obtiene la posición central de la instancia
	 * @param clfp
	 * @param uiListener
	 */
    getCenterX : function(clfp, uiListener) {
        return this.getSize(clfp, uiListener).width / 2;
    },
    /**
	 * Dibuja la instancia del patrón en la instanciación
	 */
    paint : function(surface, clfp, transform, detailedLinks, uiListener) {
        var size = this.getSize(clfp, uiListener);
        var group = surface.createGroup().setTransform(transform);
        var paintdata = {
            group : group,
            size: size,
            acts : {}
        };
        
        if(!detailedLinks) {
            uiListener.registerActiveElement(group, {
                id : clfp.id
            });
        }
        /* individual */
        var act = clfp.flow[0];
        paintdata.acts[act.id] = ClfpsRenderingCommon.createActPaintData(group, size.acts[act.id], act.id, uiListener);
        var trailinggroup = paintdata.acts[act.id].trailinggroup;
        var actgroup = paintdata.acts[act.id].group;
        
        var inst = DesignInstance.instanciasGrupo(act.learners[0]);
        var delta = (1 - this.numberOfIndividualStudents) / 2;
        var link = detailedLinks ? {
            id : act.id,
            role : act.learners[0],
            idInstancia : inst[0].id //Sólo se permite una instancia del grupo
        } : null;
        for( i = 0; i < this.numberOfIndividualStudents; i++) {
            var x = this.getCenterX(clfp, uiListener) + this.distanceBetweenIndividualStudents * (i + delta) - this.studentSize / 2;
            this.createStudentShape(paintdata.acts[act.id], act.learners[0], inst[0].id, x, 0, link, uiListener, false);
        }
        if(detailedLinks) {
            var text = trailinggroup.createText({
                x : this.centerX - this.halfLineLength,//this.centerX + this.distanceBetweenIndividualStudents * delta - this.studentSize / 2 - 2 * this.teacherRadius,
                y : 14,
                text : act.title,
                align : "left"
            }).setFont(this.text.font).setFill(this.text.fill);
            new FillListener(text, this.text.fill, this.text.emphasisFill);
        }
        var button = ClfpsRenderingCommon.addActButton(act.id, paintdata.acts[act.id].group, size.acts[act.id], uiListener);
            uiListener.registerActiveElement(button, {
                clfp : clfp,
                id : act.id
            });
            
        this.paintLineInstance(trailinggroup, this.halfDistanceBetweenPhases, clfp, uiListener);

        /* roles */
        act = clfp.flow[1];
        paintdata.acts[act.id] = ClfpsRenderingCommon.createActPaintData(group, size.acts[act.id], act.id, uiListener);
        trailinggroup = paintdata.acts[act.id].trailinggroup;
        actgroup = paintdata.acts[act.id].group;

        //Obtener el valor de delta
        if(ClfpsCommon.isInMain(clfp) == true) {
            delta = (1 - clfp.numberOfInstances(1)) / 2;
        } else {
            if(uiListener.instanceid[clfp.id] == null) {
                uiListener.instanceid[clfp.id] = DesignInstance.instanciaProcede(DesignInstance.instanciasGrupo(act.learners[0])[0]).id;
            }
            delta = (1 - clfp.numberOfInstancesProcedenInstancia(1, uiListener.instanceid[clfp.id])) / 2;
        }

        var y2 = this.halfDistanceBetweenPhases + this.roleImage.height + 10;
        var numOfInstanceRole = 0;
        //Indica el número de instancia del total a representar
        for(var i = 0; i < act.learners.length; i++) {
            var roleId = act.learners[i];
            var instancias = ClfpsCommon.getInstances(clfp, act.learners[i], uiListener);
            for(var j = 0; j < instancias.length; j++) {
                x = this.getCenterX(clfp, uiListener) - this.roleImage.width / 2 - (act.learners.length - 1) * this.separationBetweenPlayRolesInstances / 2 + (this.distanceBetweenPlayRolesInstances) * (numOfInstanceRole + delta) + i * this.separationBetweenPlayRolesInstances;
                if(j == 0) {
                    var xinicialRole = x;
                }
                if(j == instancias.length - 1) {
                    var xfinalRole = x + this.roleImage.width;
                }
                numOfInstanceRole++;
                var images = this.getRoleImages(i % 4);

                //Mostrar de forma diferente si la instancia se encuentra seleccionada o no
                var selected = ClfpsCommon.selectedGroupInstance(instancias[j], uiListener);
                if(selected == true) {
                    var image = actgroup.createImage({
                        x : x,
                        y : this.halfDistanceBetweenPhases,
                        width : this.roleImage.width,
                        height : this.roleImage.height,
                        src : images.selected
                    });
                    new ImageSourceListener(image, images.selected, images.emphasisSelected);
                } else {
                    image = actgroup.createImage({
                        x : x,
                        y : this.halfDistanceBetweenPhases,
                        width : this.roleImage.width,
                        height : this.roleImage.height,
                        src : images.src
                    });
                    new ImageSourceListener(image, images.src, images.emphasis);
                }

                var clfpDepende = DesignInstance.clfpDependeInstanciaGrupo(IDPool.getObject(instancias[j].id));
                var g = DesignInstance.grupoInstancia(instancias[j]);
                //Sólo se permite borrar las instancias que tienen al menos un hermano
                if(DesignInstance.instanciasGrupoMismoPadre(g.roleid, instancias[j].idParent).length > 1) {
                    var letDelete = true;
                } else {
                    letDelete = false;
                }
                uiListener.registerActiveElement(image, {
                    clfp : clfp,
                    id : roleId,
                    idInstancia : instancias[j].id,
                    letAsignUsers : true,
                    letDelete : letDelete,
                    letShowClfp : clfpDepende != null
                });
                ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[i], instancias[j].id, image);

            }
            if (instancias.length>0){
                text = actgroup.createText({
                    x : (xinicialRole + xfinalRole) / 2,
                    y : y2,
                    text : IDPool.getObject(roleId).title,
                    align : "middle"
                }).setFont(this.roleNameText.font).setFill(this.roleNameText.fill);
                new FillListener(text, this.roleNameText.fill, this.roleNameText.emphasisFill);
            }
        }
        if(detailedLinks) {
            text = trailinggroup.createText({
                x : this.centerX - this.halfLineLength, //xinicio - 2 * this.teacherRadius,
                y : 14,
                text : act.title,
                align : "left"
            }).setFont(this.text.font).setFill(this.text.fill);
            new FillListener(text, this.text.fill, this.text.emphasisFill);
        }
        button = ClfpsRenderingCommon.addActButton(act.id, paintdata.acts[act.id].group, size.acts[act.id], uiListener);
            uiListener.registerActiveElement(button, {
                clfp : clfp,
                id : act.id
            });
        this.paintLineInstance(trailinggroup, this.halfDistanceBetweenPhases, clfp, uiListener);

        /* sim groups */
        act = clfp.flow[2];
        paintdata.acts[act.id] = ClfpsRenderingCommon.createActPaintData(group, size.acts[act.id], act.id, uiListener);
        trailinggroup = paintdata.acts[act.id].trailinggroup;
        actgroup = paintdata.acts[act.id].group;

        //Obtener el valor de delta
        if(ClfpsCommon.isInMain(clfp) == true) {
            delta = (1 - clfp.numberOfInstances(2)) / 2;
        } else {
            if(uiListener.instanceid[clfp.id] == null) {
                uiListener.instanceid[clfp.id] = DesignInstance.instanciaProcede(DesignInstance.instanciasGrupo(act.learners[0])[0]).id;
            }
            delta = (1 - clfp.numberOfInstancesProcedenInstancia(2, uiListener.instanceid[clfp.id])) / 2;
        }

        var numOfInstanceSim = 0;
        y2 = this.halfDistanceBetweenPhases + this.simMiniGap + this.studentSize;
        var y3 = y2 + this.studentSize + 10;
        for( i = 0; i < act.learners.length; i++) {
            instancias = ClfpsCommon.getInstances(clfp, act.learners[i], uiListener);
            for( j = 0; j < instancias.length; j++) {
                clfpDepende = DesignInstance.clfpDependeInstanciaGrupo(IDPool.getObject(instancias[j].id));
                g = DesignInstance.grupoInstancia(instancias[j]);
                //Sólo se permite borrar las instancias que tienen al menos un hermano
                if(DesignInstance.instanciasGrupoMismoPadre(g.roleid, instancias[j].idParent).length > 1) {
                    letDelete = true;
                } else {
                    letDelete = false;
                }
                roleId = act.learners[i];
                link = detailedLinks ? {
                    clfp : clfp,
                    id : roleId,
                    idInstancia : instancias[j].id,
                    letAsignUsers : true,
                    letDelete : letDelete,
                    letShowClfp : clfpDepende != null,
                    role : roleId
                } : null;
                x = this.getCenterX(clfp, uiListener) - (this.simMiniGap + 2 * this.studentSize) / 2 - (act.learners.length - 1) * this.separationBetweenSimGroupsInstances / 2 + (this.distanceBetweenSimGroupsInstances) * (numOfInstanceSim + delta) + i * this.separationBetweenSimGroupsInstances;

                if(j == 0) {
                    var xinicialSim = x;
                }
                if(j == instancias.length - 1) {
                    var xfinalSim = x + this.simMiniGap + 2 * this.studentSize;
                }
                numOfInstanceSim++;
                //Mostrar de forma diferente si la instancia se encuentra seleccionada o no
                selected = ClfpsCommon.selectedGroupInstance(instancias[j], uiListener);

                this.createStudentShape(paintdata.acts[act.id], act.learners[i], instancias[j].id, x, this.halfDistanceBetweenPhases, link, uiListener, selected);
                this.createStudentShape(paintdata.acts[act.id], act.learners[i], instancias[j].id, x + this.simMiniGap + this.studentSize, this.halfDistanceBetweenPhases, link, uiListener, selected);
                this.createStudentShape(paintdata.acts[act.id], act.learners[i], instancias[j].id, x + this.simMiniGap + this.studentSize, y2, link, uiListener, selected);
                this.createStudentShape(paintdata.acts[act.id], act.learners[i], instancias[j].id, x, y2, link, uiListener, selected);
            }
            
            text = actgroup.createText({
                x : (xinicialSim + xfinalSim) / 2,
                y : y3,
                text : IDPool.getObject(roleId).title,
                align : "middle"
            }).setFont(this.roleNameText.font).setFill(this.roleNameText.fill);
            new FillListener(text, this.roleNameText.fill, this.roleNameText.emphasisFill);
        }
        if(detailedLinks) {
            text = trailinggroup.createText({
                x : this.centerX - this.halfLineLength, //xinicio - 2 * this.teacherRadius,
                y : 14,
                text : act.title,
                align : "left"
            }).setFont(this.text.font).setFill(this.text.fill);
            new FillListener(text, this.text.fill, this.text.emphasisFill);
        }
        button = ClfpsRenderingCommon.addActButton(act.id, paintdata.acts[act.id].group, size.acts[act.id], uiListener);
            uiListener.registerActiveElement(button, {
                clfp : clfp,
                id : act.id
            });
        this.paintLineInstance(trailinggroup, this.halfDistanceBetweenPhases, clfp, uiListener);

        /* simulation */
        act = clfp.flow[3];
        paintdata.acts[act.id] = ClfpsRenderingCommon.createActPaintData(group, size.acts[act.id], act.id, uiListener);
        trailinggroup = paintdata.acts[act.id].trailinggroup;
        actgroup = paintdata.acts[act.id].group;
        
        image = actgroup.createImage({
            x : this.getCenterX(clfp, uiListener) - this.simImage.width / 2,
            y : this.halfDistanceBetweenPhases,
            width : this.simImage.width,
            height : this.simImage.height,
            src : this.simImage.src
        });
        //Obtenemos la única instancia de la simulación
        inst = DesignInstance.instanciasGrupo(act.learners[0]);
        new ImageSourceListener(image, this.simImage.src, this.simImage.emphasis);
        if(detailedLinks) {
            uiListener.registerActiveElement(image, {
                clfp : clfp,
                idInstancia : inst[0].id,
                id : act.learners[0],
                role : act.learners[0],
                letClone : false
            });
            ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[0], inst[0].id, image);
            text = trailinggroup.createText({
                x : this.centerX - this.halfLineLength,
                y : 14,
                text : act.title,
                align : "left"
            }).setFont(this.text.font).setFill(this.text.fill);
            new FillListener(text, this.text.fill, this.text.emphasisFill);
        }
        button = ClfpsRenderingCommon.addActButton(act.id, paintdata.acts[act.id].group, size.acts[act.id], uiListener);
            uiListener.registerActiveElement(button, {
                clfp : clfp,
                id : act.id
            });
        
        this.paintLineInstance(trailinggroup, this.halfDistanceBetweenPhases, clfp, uiListener);

        /* teacher */
        for( i = 0; i < 4; i++) {
            act = clfp.flow[i];
            actgroup = paintdata.acts[act.id].group;
            var posX = this.getTeacherXInstance(clfp, uiListener, i, xfinalRole, xfinalSim);
            var circle = actgroup.createCircle({
                cx : posX,
                cy : size.acts[act.id].pre.height - this.teacherRadius,
                r : this.teacherRadius
            }).setFill(this.teacherPaint.fill).setStroke(this.teacherPaint.stroke);
            new FillListener(circle, this.teacherPaint.fill, this.teacherPaint.emphasisFill);
            if(detailedLinks) {
                var allowAssignTeacher = 1;
                uiListener.registerActiveElement(circle, {
                    clfp : clfp,
                    id : clfp.flow[i].id,
                    role : clfp.flow[i].staff[0],
                    allowAssignTeacher : allowAssignTeacher
                });
            }
            ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.staff[0], DesignInstance.instanciasGrupo(act.staff[0])[0].id, circle);
        }

        return paintdata;
    },

    createStudentShape: function(actpaintdata, roleid, instanceid, x, y, link, uiListener, isSelected) {
        var style = isSelected ? this.studentPaintSelected : this.studentPaint;
        var rect = actpaintdata.group.createRect({
            x : x,
            y : y,
            width : this.studentSize,
            height : this.studentSize
        }).setFill(style.fill).setStroke(style.stroke);
        new FillListener(rect, style.fill, style.emphasisFill);
        if(link) {
            uiListener.registerActiveElement(rect, link);
        }
        
        ClfpsRenderingCommon.addRoleInstanceData(actpaintdata, roleid, instanceid, rect);

        return rect;
    },

    /**
	 * Obtiene la posición X en la que se representará el profesor en la instanciación
	 * @param clfp Patrón en el que se desea representar el profesor
	 * @param uiListener
	 * @param index Índice de la fase
	 * @param xfinalRole Posición X final para las instancias de roles
	 * @param xfinalSim Posición X final para las instancias de simulación
	 * @return Posición X en la que se representará el profesor
	 */
    getTeacherXInstance : function(clfp, uiListener, index, xfinalRole, xfinalSim) {
        var x = this.getCenterX(clfp, uiListener) + this.teacherDistance + this.teacherRadius;
        if(index > 2) {
            return x + this.simImage.width / 2;
        } else {
            if(index == 2) {
                return xfinalSim + this.teacherDistance + this.teacherRadius;
            } else if(index == 1) {
                return xfinalRole + this.teacherDistance + this.teacherRadius;
            } else {
                var delta = this.distanceBetweenIndividualStudents;
                var halfWidth = this.studentSize / 2;
                var n = this.numberOfIndividualStudents;
            }
            return x + delta * (n - 1) / 2 + halfWidth;
        }
    },

    /**
	 * Obtiene la posición para el patrón dependiente de la fase en la instanciación
	 * @param clfp Patrón
	 * @param index Índice de la fase
	 * @param uiListener
	 * @return Posición para el patrón dependiente
	 */
    getDesiredPositionForSubplay : function(clfp, index, uiListener) {
        var size = this.getSize(clfp, uiListener);
        var actid = clfp.flow[index].id;
        var y = size.acts[actid].pre.y + size.acts[actid].pre.height/2;
        /* roles */
        //Obtener el valor de delta para los roles
        var act = clfp.flow[1];
        if(ClfpsCommon.isInMain(clfp) == true) {
            var delta = (1 - clfp.numberOfInstances(1)) / 2;
        } else {
            if(uiListener.instanceid[clfp.id] == null) {
                uiListener.instanceid[clfp.id] = DesignInstance.instanciaProcede(DesignInstance.instanciasGrupo(act.learners[0])[0]).id;
            }
            delta = (1 - clfp.numberOfInstancesProcedenInstancia(1, uiListener.instanceid[clfp.id])) / 2;
        }

        var numOfInstancesRole = 0;
        for(var i = 0; i < act.learners.length; i++) {
            numOfInstancesRole = numOfInstancesRole + ClfpsCommon.getInstances(clfp, act.learners[i], uiListener).length;
        }
        var xfinalRole = this.getCenterX(clfp, uiListener) - this.roleImage.width / 2 - (act.learners.length - 1) * this.separationBetweenPlayRolesInstances / 2 + (this.distanceBetweenPlayRolesInstances) * (numOfInstancesRole - 1 + delta) + (act.learners.length - 1) * this.separationBetweenPlayRolesInstances;
        xfinalRole = xfinalRole + this.roleImage.width;
        /* simulacion */
        act = clfp.flow[2];
        //Obtener el valor de delta para los grupos de simulación
        if(ClfpsCommon.isInMain(clfp) == true) {
            delta = (1 - clfp.numberOfInstances(2)) / 2;
        } else {
            if(uiListener.instanceid[clfp.id] == null) {
                uiListener.instanceid[clfp.id] = DesignInstance.instanciaProcede(DesignInstance.instanciasGrupo(act.learners[0])[0]).id;
            }
            delta = (1 - clfp.numberOfInstancesProcedenInstancia(2, uiListener.instanceid[clfp.id])) / 2;
        }

        var numOfInstancesSim = 0;
        for( i = 0; i < act.learners.length; i++) {
            numOfInstancesSim = numOfInstancesSim + ClfpsCommon.getInstances(clfp, act.learners[i], uiListener).length;

        }
        var xfinalSim = this.getCenterX(clfp, uiListener) - (this.simMiniGap + 2 * this.studentSize) / 2 - (act.learners.length - 1) * this.separationBetweenSimGroupsInstances / 2 + (this.distanceBetweenSimGroupsInstances) * (numOfInstancesSim - 1 + delta) + (act.learners.length - 1) * this.separationBetweenSimGroupsInstances;
        xfinalSim = xfinalSim + this.simMiniGap + 2 * this.studentSize;
        var posX = this.getTeacherXInstance(clfp, uiListener, index, xfinalRole, xfinalSim);

        return {
            x : posX + this.teacherDistance + this.teacherRadius,
            y : y
        };
    },
    /**
	 * Obtiene la posición X a la izquierda del patrón
	 * @param clfp Patrón
	 * @param index Índice de la fase
	 * @return Posición X a la izquierda del patrón
	 */
    getLeftDesiredPosition : function(clfp, index, uiListener) {
        var size = this.getSize(clfp, uiListener);
        var point = this.getDesiredPositionForSubplay(clfp, index, uiListener);
        point.x = size.width - point.x;
        return point;
    }
};