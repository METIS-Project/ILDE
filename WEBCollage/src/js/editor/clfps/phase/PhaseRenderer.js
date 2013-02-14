/**
 * @class Renderer del patrón simulación
 */
var PhaseRenderer = {

    /**
     * Hueco para la altura
     */
    gap : 40,

    /**
     * Separación entre fases
     */
    halfDistanceBetweenPhases : 20,

    /**
     * Distancia que separada dos instancias de un rol
     */
    distanceBetweenPlayRolesInstances : 44,
    /**
     * Separación entre dos roles diferentes
     */
    separationBetweenPlayRolesInstances : 10,

    /**
     * Imagen de un rol
     */
    roleImage : {
        width : 46,
        height : 33
    },

    /**
     * Distancia al profesor
     */
    teacherDistance : 35,
    /**
     * Radio del círculo del profesor
     */
    teacherRadius : 12,

    roleNameText : {
        font : {
            family : "Arial",
            size : "8pt",
            weight : "bold"
        },
        fill : [50, 50, 50, 1.0],
        emphasisFill : [150, 150, 150, 1.0]
    },

    phasePaint : {
        fill : [245, 220, 220, 1.0],
        emphasisFill : [255, 230, 230, 1.0],
        stroke : {
            color : [80, 80, 80, 1.0],
            width : 2,
            cap : "butt",
            join : 2
        }
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
            src : "images/clfps/phase/rc_" + index + ".png",
            emphasis : "images/clfps/phase/rc_" + index + "_emph.png",
            selected : "images/clfps/phase/rc_" + index + "_s.png",
            emphasisSelected : "images/clfps/phase/rc_" + index + "_emph_s.png"
        };
    },
    /**
     * Obtiene el tamaño de la instancia del clfp
     * @param clfp Clfp del que se va a obtener su tamaño
     * @param uiListener
     * @return Ancho y alto del clfp
     */
    getSize : function(clfp, uiListener) {

        //Obtener el tamaño ocupado por los roles
        var act = clfp.flow[0];
        var rolesWidth = 0;
        for(var i = 0; i < act.learners.length; i++) {
            var instancias = ClfpsCommon.getInstances(clfp, act.learners[i], uiListener);
            rolesWidth += this.distanceBetweenPlayRolesInstances * instancias.length;
        }
        rolesWidth += (act.learners.length - 1) * this.separationBetweenPlayRolesInstances + 50;

        var phaseHeight = this.roleImage.height + 10 + this.halfDistanceBetweenPhases;

        var size = {
            width : rolesWidth ,
            height : 2 * this.gap + phaseHeight,
            acts : {}
        };

        var openactheight = 0;

        var actid = clfp.getFlow()[0].id;
        var actsize = ClfpsRenderingCommon.getOpenActSize(actid, uiListener);
        var y = openactheight + this.gap;

        size.acts[actid] = {
            pre : {
                x : 0,
                y : y,
                width : size.width,
                height : phaseHeight
            },
            post : {
                x : 0,
                y : y + phaseHeight + actsize.height
            }
        };
        openactheight += actsize.height;

        size.height += openactheight;
        return size;
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
            size : size,
            acts : {}
        };

        if(!detailedLinks) {
            uiListener.registerActiveElement(group, {
                id : clfp.id
            });
        }

        /* roles */
        var act = clfp.flow[0];
        paintdata.acts[act.id] = ClfpsRenderingCommon.createActPaintData(group, size.acts[act.id], act.id, uiListener);
        var actgroup = paintdata.acts[act.id].group;

        //Obtener el valor de delta
        if(ClfpsCommon.isInMain(clfp) == true) {
            var delta = (1 - clfp.numberOfInstances(0)) / 2;
        } else {
            if(uiListener.instanceid[clfp.id] == null) {
                uiListener.instanceid[clfp.id] = DesignInstance.instanciaProcede(DesignInstance.instanciasGrupo(act.learners[0])[0]).id;
            }
            delta = (1 - clfp.numberOfInstancesProcedenInstancia(0, uiListener.instanceid[clfp.id])) / 2;
        }

        var y2 = this.roleImage.height + 20;
        var numOfInstanceRole = 0;
        //Indica el número de instancia del total a representar

        var cx = .5 * size.width;
        var w = size.width;
        var h = y2 + 10;

        var points = LinePainter.transformLine([{
            x : cx,
            y : 0
        }, {
            x : 0,
            y : 0

        }, {
            x : 0,
            y : h
        }, {
            x : w,
            y : h
        }, {
            x : w,
            y : 0
        }, {
            x : cx,
            y : 0
        }], 10, 1);

        var polygon = actgroup.createPolyline(points).setStroke(this.phasePaint.stroke).setFill(this.phasePaint.fill);

        new FillListener(polygon, this.phasePaint.fill, this.phasePaint.emphasisFill);

        for(var i = 0; i < act.learners.length; i++) {
            var roleId = act.learners[i];
            var instancias = ClfpsCommon.getInstances(clfp, act.learners[i], uiListener);
            for(var j = 0; j < instancias.length; j++) {
                var x = this.getCenterX(clfp, uiListener) - this.roleImage.width / 2 - (act.learners.length - 1) * this.separationBetweenPlayRolesInstances / 2 + (this.distanceBetweenPlayRolesInstances) * (numOfInstanceRole + delta) + i * this.separationBetweenPlayRolesInstances;
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

                var actgroup = paintdata.acts[act.id].group;

                if(selected == true) {
                    var image = actgroup.createImage({
                        x : x,
                        y : 10,
                        width : this.roleImage.width,
                        height : this.roleImage.height,
                        src : images.selected
                    });
                    new ImageSourceListener(image, images.selected, images.emphasisSelected);
                } else {
                    image = actgroup.createImage({
                        x : x,
                        y : 10,
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
                if(detailedLinks) {
                    uiListener.registerActiveElement(image, {
                        clfp : clfp,
                        id : roleId,
                        idInstancia : instancias[j].id,
                        letAsignUsers : true,
                        letDelete : letDelete,
                        letShowClfp : clfpDepende != null
                    });
                }
                ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[i], instancias[j].id, image);

            }

            if(instancias.length > 0) {
                var role = IDPool.getObject(roleId);
                var text = actgroup.createText({
                    x : (xinicialRole + xfinalRole) / 2,
                    y : y2,
                    text : role.title,
                    align : "middle"
                }).setFont(this.roleNameText.font).setFill(this.roleNameText.fill);
                new FillListener(text, this.roleNameText.fill, this.roleNameText.emphasisFill);
                if(detailedLinks) {
                    RenameElementDialog.registerElement(text, role);
                }
            }
        }
        var button = ClfpsRenderingCommon.addActButton(act.id, paintdata.acts[act.id].group, size.acts[act.id], uiListener);
        uiListener.registerActiveElement(button, {
            clfp : clfp,
            id : act.id
        });

        /* teacher */
        actgroup = paintdata.acts[act.id].group;
        var pos = this.getPositionForTeacher(clfp, uiListener, xfinalRole);
        var circle = actgroup.createCircle({
            cx : pos.x,
            cy : pos.y,
            r : this.teacherRadius
        }).setFill(this.teacherPaint.fill).setStroke(this.teacherPaint.stroke);
        new FillListener(circle, this.teacherPaint.fill, this.teacherPaint.emphasisFill);

        if(detailedLinks) {
            var allowAssignTeacher = 1;
            uiListener.registerActiveElement(circle, {
                clfp : clfp,
                id : clfp.flow[0].id,
                role : clfp.flow[0].staff[0],
                allowAssignTeacher : allowAssignTeacher
            });
        }
        ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.staff[0], DesignInstance.instanciasGrupo(act.staff[0])[0].id, circle);

        // text and lines
        var trailinggroup = paintdata.acts[act.id].trailinggroup;
        var tglx1 = 0;
        var tglx2 = size.width;
        var tgly = this.halfDistanceBetweenPhases;
        ClfpsRenderingCommon.createTrailingGroupArt(trailinggroup, tglx1, tglx2, tgly, act, detailedLinks);

        return paintdata;
    },
    /**
     * Obtiene la posición en la que se representará el profesor en la instanciación
     * @param clfp Patrón en el que se desea representar el profesor
     * @param uiListener
     * @param xfinalRole Posición X final para las instancias de roles
     * @return Posición en la que se representará el profesor
     */
    getPositionForTeacher : function(clfp, uiListener, xfinalRole) {
        return {
            x : xfinalRole + this.teacherDistance + this.teacherRadius,
            y : this.roleImage.height / 2 + 10
        };
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
        var y = size.acts[actid].pre.y + size.acts[actid].pre.height / 2;
        /* roles */
        //Obtener el valor de delta para los roles
        var act = clfp.flow[0];
        if(ClfpsCommon.isInMain(clfp) == true) {
            var delta = (1 - clfp.numberOfInstances(0)) / 2;
        } else {
            if(uiListener.instanceid[clfp.id] == null) {
                uiListener.instanceid[clfp.id] = DesignInstance.instanciaProcede(DesignInstance.instanciasGrupo(act.learners[0])[0]).id;
            }
            delta = (1 - clfp.numberOfInstancesProcedenInstancia(0, uiListener.instanceid[clfp.id])) / 2;
        }

        var numOfInstancesRole = 0;
        for(var i = 0; i < act.learners.length; i++) {
            numOfInstancesRole = numOfInstancesRole + ClfpsCommon.getInstances(clfp, act.learners[i], uiListener).length;
        }
        var xfinalRole = this.getCenterX(clfp, uiListener) - this.roleImage.width / 2 - (act.learners.length - 1) * this.separationBetweenPlayRolesInstances / 2 + (this.distanceBetweenPlayRolesInstances) * (numOfInstancesRole - 1 + delta) + (act.learners.length - 1) * this.separationBetweenPlayRolesInstances;
        xfinalRole = xfinalRole + this.roleImage.width;
        var pos = this.getPositionForTeacher(clfp, uiListener, xfinalRole);

        return {
            x : pos.x + this.teacherDistance + this.teacherRadius,
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
