/**
 * @class Renderer del patrón Jigsaw
 */
var JigsawRenderer = {

    /**
     * Tamaño de las imágenes
     */
    imageSize : {
        width : 54,
        height : 57
    },

    /**
     * Distancia entre fases
     */
    distanceBetweenPhases : 40,
    /**
     * Distancia entre imágenes
     */
    distanceBetweenImages : 25,
    gap : 30,

    /**
     * Distancia al profesor
     */
    teacherDistance : 15,
    /**
     * Radio del círculo del profesor
     */
    teacherRadius : 15,

    extraLineGap : 10,

    /**
     * Directorio de las imágenes
     */
    imageFolder : "images/clfps/jigsaw/",
    /**
     * Extensión de las imágenes
     */
    fileExtension : ".png",
    /**
     * Extensión empleada para la imagen que representa que el ratón está sobre la instancia
     */
    emphasisExtension : "_e.png",
    /**
     * Extensión empleada para la imagen que representa que la instancia está seleccionada para que se muestre su clfp
     */
    selectedExtension : "_s.png",
    /**
     * Extensión empleada para la imagen que representa que el ratón está sobre la instancia y esta seleccionada para que se muestre su clfp
     */
    emphasisSelectedExtension : "_e_s.png",

    lineStrokeInstances : {
        color : "gray",
        width : 1.3,
        cap : "butt",
        join : 2,
        style : "Dash"
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
     * Obtiene el tamaño de la instancia del clfp
     * @param clfp Clfp del que se va a obtener su tamaño
     * @param uiListener
     * @return Ancho y alto del clfp
     */
    getSize : function(clfp, uiListener) {
        var max = Math.max(ClfpsCommon.getInstances(clfp, clfp.getFlow()[0].learners[0], uiListener).length, ClfpsCommon.getInstances(clfp, clfp.getFlow()[1].learners[0], uiListener).length);

        var size = {
            //            width : 2 * this.gap + max * this.imageSize.width + (max - 1) * this.distanceBetweenImages + this.teacherDistance + 2 * this.teacherRadius,
            width : 2 * this.gap + max * this.imageSize.width + (max - 1) * this.distanceBetweenImages,
            height : 2 * this.gap + 3 * this.imageSize.height + 2 * this.distanceBetweenPhases,
            acts : {}
        };

        var openactheight = 0;

        for(var i = 0; i < 3; i++) {
            var actid = clfp.getFlow()[i].id;
            var actsize = ClfpsRenderingCommon.getOpenActSize(actid, uiListener);
            var y = openactheight + this.gap + i * (this.imageSize.height + this.distanceBetweenPhases);

            size.acts[actid] = {
                pre : {
                    x : 0,
                    y : y,
                    width : size.width,
                    height : this.imageSize.height
                },
                post : {
                    x : 0,
                    y : y + this.imageSize.height + actsize.height
                }
            };
            openactheight += actsize.height;
        }

        size.height += openactheight;

        return size;
    },
    /**
     * Obtiene las imágenes asociadas a la fase en la instanciación del diseño
     * @param phase Fase de la que se van a obtener las imágenes
     * @param num Número de imágenes a obtener
     * @return Imágenes de la fase
     */
    getImages : function(phase, num) {
        var arrayReturn = new Array();
        switch (phase) {
            case 0: {
                var array = ["igreen", "iyellow", "iblue", "ired"];
                break;
            }
            case 1: {
                array = ["egreen", "eyellow", "eblue", "ered"];
                break;
            }
            case 2:
                array = ["jgroup"];
        }
        var n = array.length;
        for(var i = 0; i < num; i++) {
            arrayReturn.push(array[i % n]);
        }
        return arrayReturn;
    },
    /**
     * Dibuja la instancia del patrón en la instanciación
     */
    paint : function(surface, clfp, transform, detailedLinks, uiListener, animator) {

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

        var piezas = new Array();
        //Obtención del numero máximo de instancias que hay en una fase
        var max = Math.max(ClfpsCommon.getInstances(clfp, clfp.getFlow()[0].learners[0], uiListener).length, ClfpsCommon.getInstances(clfp, clfp.getFlow()[1].learners[0], uiListener).length);

        for(var i = 0; i < 3; i++) {
            var act = clfp.getFlow()[i];
            paintdata.acts[act.id] = ClfpsRenderingCommon.createActPaintData(group, size.acts[act.id], act.id, uiListener);

            // text and lines

            var trailinggroup = paintdata.acts[act.id].trailinggroup;
            var tglx1 = this.gap - this.extraLineGap;
            var tglx2 = this.gap + max * this.imageSize.width + (max - 1) * this.distanceBetweenImages + this.teacherDistance + 2 * this.teacherRadius + this.extraLineGap;
            var tgly = this.distanceBetweenPhases / 2;

            ClfpsRenderingCommon.createTrailingGroupArt(trailinggroup, tglx1, tglx2, tgly, act, detailedLinks);

            // act group

            var actgroup = paintdata.acts[act.id].group;

            var instancias = ClfpsCommon.getInstances(clfp, act.learners[0], uiListener);
            var images = this.getImages(i, instancias.length);
            var x = this.gap;
            var instancediff = max - instancias.length;
            if(instancediff > 0) {
                x += 0.5 * (instancediff * this.imageSize.width + (instancediff - 0) * this.distanceBetweenImages);
            }

            for(var j = 0; j < images.length; j++) {
                var selected = ClfpsCommon.selectedGroupInstance(instancias[j], uiListener);
                var normal = this.imageFolder + images[j] + ( selected ? this.selectedExtension : this.fileExtension);
                var emphasis = this.imageFolder + images[j] + ( selected ? this.emphasisSelectedExtension : this.emphasisExtension);
                var image = actgroup.createImage({
                    x : x,
                    y : 0,
                    width : this.imageSize.width,
                    height : this.imageSize.height,
                    src : normal
                });
                new ImageSourceListener(image, normal, emphasis);

                //Almacenamos la información relativa a imagen de la pieza de la fase individual o de expertos
                if(i != 2) {
                    piezas[instancias[j].id] = image;
                }
                var clfpDepende = DesignInstance.clfpDependeInstanciaGrupo(IDPool.getObject(instancias[j].id));
                var g = DesignInstance.grupoInstancia(instancias[j]);

                if(detailedLinks) {
                    //Sólo se permite borrar las instancias que tienen al menos un hermano
                    var letDelete = DesignInstance.instanciasGrupoMismoPadre(g.roleid, instancias[j].idParent).length > 1;
                    uiListener.registerActiveElement(image, {
                        clfp : clfp,
                        id : act.learners[0],
                        idInstancia : instancias[j].id,
                        letAsignUsers : true,
                        level : i,
                        letDelete : letDelete,
                        letShowClfp : clfpDepende != null
                    });
                }
                ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[0], instancias[j].id, image);
                x += this.imageSize.width + this.distanceBetweenImages;
            }

            var cy = this.imageSize.height / 2;
            var cx = x + this.teacherDistance + this.teacherRadius - this.distanceBetweenImages;
            var circle = actgroup.createCircle({
                cx : cx,
                cy : cy,
                r : this.teacherRadius
            }).setFill(this.teacherPaint.fill).setStroke(this.teacherPaint.stroke);
            new FillListener(circle, this.teacherPaint.fill, this.teacherPaint.emphasisFill);
            if(detailedLinks) {
                var allowAssignTeacher = 1;
                uiListener.registerActiveElement(circle, {
                    clfp : clfp,
                    id : act.id,
                    role : act.staff[0],
                    allowAssignTeacher : allowAssignTeacher
                });
            }
            ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.staff[0], DesignInstance.instanciasGrupo(act.staff[0])[0].id, circle);
            var button = ClfpsRenderingCommon.addActButton(act.id, paintdata.acts[act.id].group, size.acts[act.id], uiListener);
            uiListener.registerActiveElement(button, {
                clfp : clfp,
                id : act.id
            });
        }
        /*        this.drawLines(paintdata, size, clfp, uiListener, piezas, animator);*/
        return paintdata;
    },
    /**
     * Dibuja una linea entre instancias de la fase individual y de expertos con algún participante en común
     * @param group Superficie donde se dibujarán las líneas
     * @param clfp Patrón
     * @param uiListener
     * @param piezas Información de las posiciones de las piezas
     */
    /*  drawLines : function(paintdata, size, clfp, uiListener, piezas, animator) {
    var actIndividual = clfp.getFlow()[0];
    var linesGroup = paintdata.acts[actIndividual.id].group.createGroup();
    animator.addNoAnimStuff(linesGroup);
    var instanciasIndividual = ClfpsCommon.getInstances(clfp, actIndividual.learners[0], uiListener);
    var actExpertos = clfp.getFlow()[1];
    var instanciasExpertos = ClfpsCommon.getInstances(clfp, actExpertos.learners[0], uiListener);

    var dy = size.acts[actExpertos.id].pre.y - size.acts[actIndividual.id].pre.y;

    for(var i = 0; i < instanciasIndividual.length; i++) {
    //Obtenemos la pieza que se corresponde con la instancia de la fase individual
    var piezaIndividual = piezas[instanciasIndividual[i].id];
    for(var j = 0; j < instanciasExpertos.length; j++) {
    //Obtenemos la pieza que se corresponde con la instancia de la fase de expertos
    var piezaExpertos = piezas[instanciasExpertos[j].id];

    var pertenecen = false;
    //Comprobar si alguno de los participantes en la instancia de la fase individual está incluido en la instancia
    //de la fase de expertos
    for(var p = 0; p < instanciasIndividual[i].participants.length; p++) {
    if(DesignInstance.perteneceInstancia(instanciasExpertos[j], instanciasIndividual[i].participants[p])) {
    pertenecen = true;
    break;
    }
    }
    //Dibujar la línea si hay participantes en común
    if(pertenecen == true) {
    var linedata = {
    x1 : piezaIndividual.shape.x + piezaIndividual.shape.width / 2,
    y1 : piezaIndividual.shape.y + piezaIndividual.shape.height/2,//piezaIndividual.shape.height - 10,
    x2 : piezaExpertos.shape.x + piezaExpertos.shape.width / 2,
    y2 : dy + piezaExpertos.shape.height / 2
    };

    /*var dx1 = .5 * piezaIndividual.shape.width * (linedata.x2 - linedata.x1) / size.width;
    var dx2 = .5 * piezaExpertos.shape.width * (linedata.x2 - linedata.x1) / size.width;
    linedata.x1 += dx1;
    linedata.x2 -= dx2;*/
    /*
    linesGroup.createLine(linedata).setStroke(this.lineStrokeInstances);
    }
    }
    }
    },*/

    /**
     * Obtiene la posición para el patrón dependiente de la fase en la instanciación
     * @param clfp Patrón
     * @param index Índice de la fase
     * @param uiListener
     * @return Posición para el patrón dependiente
     */
    getDesiredPositionForSubplay : function(clfp, index, uiListener) {
        var actIndividual = clfp.getFlow()[0];
        var instanciasIndividual = ClfpsCommon.getInstances(clfp, actIndividual.learners[0], uiListener);
        var actExpertos = clfp.getFlow()[1];
        var instanciasExpertos = ClfpsCommon.getInstances(clfp, actExpertos.learners[0], uiListener);
        var max = Math.max(instanciasIndividual.length, instanciasExpertos.length);
        var size = this.getSize(clfp, uiListener);
        var actid = clfp.flow[index].id;
        var y = size.acts[actid].pre.y + size.acts[actid].pre.height / 2;

        return {
            x : this.gap + max * this.imageSize.width + (max - 1) * this.distanceBetweenImages + this.teacherDistance + 2 * this.teacherRadius + this.extraLineGap,
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
