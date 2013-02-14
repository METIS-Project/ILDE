/**
 * Renderer del patrón TPS
 */
var TPSRenderer = {

    gap : 40,

    /**
	 * Posición X central en el diseño
	 */
    centerX : 200,

    /**
	 * Separación entre fases
	 */
    halfDistanceBetweenPhases : 15,
    /**
	 * Distancia entre las imágenes abc
	 */
    distanceBetweenAbcImages : 30,
    /**
	 * Separación entre las imágenes abc y los cuadrados
	 */
    abcGap : 10,
    /**
	 * Separación entre los cuadrados
	 */
    miniGap : 6,
    /**
	 * Distancia al profesor
	 */
    teacherDistance : 35,
    /**
	 * Radio del círculo del profesor
	 */
    teacherRadius : 15,

    extraLineGap : 10,

    /**
	 * Directorio de las imágenes
	 */
    imageFolder : "images/clfps/tps/",
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

    /**
	 * Propiedades de la imagen share
	 */
    shareImage : {
        src : "share",
        width : 73,
        height : 101
    },

    /**
	 * Propiedades de la imagen abc
	 */
    abcImage : {
        src : "abc",
        width : 48,
        height : 65
    },

    /**
	 * Tamaño de los cuadrados
	 */
    rectSize : 21,
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
	 * Propiedades gráficas de un estudiante en blanco
	 */
    studentPaintBlank : {
        fill : [255, 255, 255, 1.0],
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
	 * Número máximo de instancias para las que se muestran los cuadrados en la fase de pares
	 */
    maxInstancesRect : 48,

    /**
	 * Obtiene el tamaño de la instancia del clfp
	 * @param clfp Clfp del que se va a obtener su tamaño
	 * @param uiListener
	 * @return Ancho y alto del clfps y de los actos que lo constituyen
	 */
    getSize : function(clfp, uiListener) {
        var size = {
            width : 2 * this.gap + 3 * this.distanceBetweenAbcImages + 4 * this.abcImage.width + this.teacherDistance + 2 * this.teacherRadius,
            height: 2 * this.gap + 2*this.rectSize + this.abcImage.height + this.abcGap + this.shareImage.height + 7*this.halfDistanceBetweenPhases,
            acts : {}
        };
 
        var prevActHeight = new Array(); //Altura de los actos previos
        var cumPrevActHeight = new Array(); //Altura acumulada de los actos previos (incluyendo separaciones entre actos)
        
        prevActHeight[0]= this.rectSize + this.halfDistanceBetweenPhases;
        prevActHeight[1]= this.abcImage.height + this.abcGap + this.rectSize + this.halfDistanceBetweenPhases;
        prevActHeight[2]= this.shareImage.height + this.halfDistanceBetweenPhases;
        
        cumPrevActHeight[0]=0;
        for (var i=1;i<prevActHeight.length;i++)
        {
            cumPrevActHeight[i]=cumPrevActHeight[i-1]+prevActHeight[i-1]+2*this.halfDistanceBetweenPhases;
        }
        
        var openactheight = 0;

        for(var i = 0; i < clfp.flow.length; i++) {
            var actid = clfp.getFlow()[i].id;
            var actsize = ClfpsRenderingCommon.getOpenActSize(actid, uiListener);
            var y = openactheight + this.gap + cumPrevActHeight[i];

            size.acts[actid] = {
                pre : {
                    x : 0,
                    y : y,
                    width : size.width,
                    height : prevActHeight[i]
                },
                post : {
                    x : 0,
                    y : y + prevActHeight[i] + actsize.height
                }
            };
            openactheight += actsize.height;
        }

        size.height += openactheight;

        return size;
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

        var lineExtremeDistanceToCenter = 1.5 * this.distanceBetweenAbcImages + 2 * this.abcImage.width + this.extraLineGap;

        /* think */
        var act = clfp.flow[0];
        paintdata.acts[act.id] = ClfpsRenderingCommon.createActPaintData(group, size.acts[act.id], act.id, uiListener);
        var trailinggroup = paintdata.acts[act.id].trailinggroup;
        var actgroup = paintdata.acts[act.id].group;
       
        var instancias = ClfpsCommon.getInstances(clfp, act.learners[0], uiListener);
        var numPart = instancias[0].participants.length;

        var tv = this.getThinkValues(numPart);
        for(var i = 0; i < tv.rows; i++) {
            var k = 0;
            var posY = i * (tv.rectSizeInstance + (TPSRenderer.rectSize - tv.rectSizeInstance * tv.rows));
            for(var j = i * tv.columns; j < (i + 1) * (tv.columns) && j < numPart; j++) {
                var x = (this.centerX - lineExtremeDistanceToCenter) + tv.studentGapInstance / 2 + k * (tv.rectSizeInstance + tv.studentGapInstance);
                var rect = actgroup.createRect({
                    x : x,
                    y : posY,
                    width : tv.rectSizeInstance,
                    height : tv.rectSizeInstance
                }).setFill(this.studentPaint.fill).setStroke(this.studentPaint.stroke);
                new FillListener(rect, this.studentPaint.fill, this.studentPaint.emphasisFill);
                if(detailedLinks) {
                    uiListener.registerActiveElement(rect, {
                        clfp : clfp,
                        idInstancia : instancias[0].id,
                        id : act.learners[0],
                        role : act.learners[0],
                        letClone : false
                    });
                }
                ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[0], instancias[0].id, rect);
                k++;
            }
        }
        if (numPart==0)
        {
            var rect = actgroup.createRect({
                x : this.centerX - tv.rectSizeInstance/2,
                y : posY,
                width : tv.rectSizeInstance,
                height : tv.rectSizeInstance
            }).setFill(this.studentPaintBlank.fill).setStroke(this.studentPaintBlank.stroke);
            ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[0], instancias[0].id, rect);
        }

        if(detailedLinks) {
            text = trailinggroup.createText({
                x : this.centerX - lineExtremeDistanceToCenter - this.extraLineGap,
                y : 0,
                text : clfp.flow[0].title,
                align : "end"
            }).setFont(this.text.font).setFill(this.text.fill);
            new FillListener(text, this.text.fill, this.text.emphasisFill);
            var button = ClfpsRenderingCommon.addActButton(act.id, paintdata.acts[act.id].group, size.acts[act.id], uiListener);
            uiListener.registerActiveElement(button, {
                clfp : clfp,
                id : act.id
            });
        }

        trailinggroup.createLine({
            x1 : this.centerX - lineExtremeDistanceToCenter,
            y1 : this.halfDistanceBetweenPhases,
            x2 : this.centerX + lineExtremeDistanceToCenter,
            y2 : this.halfDistanceBetweenPhases
        }).setStroke(this.lineStroke);

        /* pair */

        act = clfp.flow[1];
        paintdata.acts[act.id] = ClfpsRenderingCommon.createActPaintData(group, size.acts[act.id], act.id, uiListener);
        trailinggroup = paintdata.acts[act.id].trailinggroup;
        actgroup = paintdata.acts[act.id].group;
        
        instancias = ClfpsCommon.getInstances(clfp, act.learners[0], uiListener);

        var j = 0;
        numPart = instancias.length;
        var pv = this.getPairValues(numPart);

        for(var r = 0; r < pv.rows; r++) {
            var k = 0;
            for(var c = r * pv.columns; c < (r + 1) * (pv.columns) && c < numPart; c++) {
                x = (this.centerX - lineExtremeDistanceToCenter) + pv.distanceBetweenAbcImages / 2 + k * (pv.abcImageWidth + pv.distanceBetweenAbcImages);
                //Mostrar de forma diferente si la instancia se encuentra seleccionada o no
                var selected = ClfpsCommon.selectedGroupInstance(instancias[j], uiListener);
                if(selected == true) {
                    var imageSrc = this.imageFolder + this.abcImage.src + this.selectedExtension;
                } else {
                    var imageSrc = this.imageFolder + this.abcImage.src + this.fileExtension;
                }
                image = actgroup.createImage({
                    x : x,
                    y : r * (pv.abcImageHeight + this.abcGap + (pv.abcImageWidth - this.miniGap) / 2) + this.miniGap,
                    width : pv.abcImageWidth,
                    height : pv.abcImageHeight,
                    src : imageSrc
                });
                ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[0], instancias[j].id, image);

                if(selected == true) {
                    new ImageSourceListener(image, this.imageFolder + this.abcImage.src + this.selectedExtension, this.imageFolder + this.abcImage.src + this.emphasisSelectedExtension);
                } else {
                    new ImageSourceListener(image, this.imageFolder + this.abcImage.src + this.fileExtension, this.imageFolder + this.abcImage.src + this.emphasisExtension);
                }

                if(instancias.length <= this.maxInstancesRect) {
                    if(selected == true) {
                        var sp = this.studentPaintSelected;
                    } else {
                        var sp = this.studentPaint;
                    }
                    var rectA = actgroup.createRect({
                        x : x,
                        y : r * (pv.abcImageHeight + this.abcGap + (pv.abcImageWidth - this.miniGap) / 2) + this.miniGap + pv.abcImageHeight + this.abcGap / 2,
                        width : (pv.abcImageWidth - this.miniGap) / 2,
                        height : (pv.abcImageWidth - this.miniGap) / 2
                    }).setFill(sp.fill).setStroke(sp.stroke);
                    var rectB = actgroup.createRect({
                        x : x + rectA.shape.width + this.miniGap,
                        y : r * (pv.abcImageHeight + this.abcGap + (pv.abcImageWidth - this.miniGap) / 2) + this.miniGap + pv.abcImageHeight + this.abcGap / 2,
                        width : (pv.abcImageWidth - this.miniGap) / 2,
                        height : (pv.abcImageWidth - this.miniGap) / 2
                    }).setFill(sp.fill).setStroke(sp.stroke);
                    
                    ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[0], instancias[j].id, rectA);
                    ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[0], instancias[j].id, rectB);
                    
                    new FillListener(rectA, this.studentPaint.fill, this.studentPaint.emphasisFill);
                    new FillListener(rectB, this.studentPaint.fill, this.studentPaint.emphasisFill);
                }

                //Mostrar las opciones de menú correspondientes
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
                        id : act.learners[0],
                        idInstancia : instancias[j].id,
                        letAsignUsers : true,
                        letDelete : letDelete,
                        letShowClfp : clfpDepende != null,
                        role : act.learners[0]
                    });
                    if(instancias.length <= this.maxInstancesRect) {
                        uiListener.registerActiveElement(rectA, {
                            clfp : clfp,
                            id : act.learners[0],
                            idInstancia : instancias[j].id,
                            letAsignUsers : true,
                            letDelete : letDelete,
                            letShowClfp : clfpDepende != null,
                            role : act.learners[0]
                        });
                        uiListener.registerActiveElement(rectB, {
                            clfp : clfp,
                            id : act.learners[0],
                            idInstancia : instancias[j].id,
                            letAsignUsers : true,
                            letDelete : letDelete,
                            letShowClfp : clfpDepende != null,
                            role : act.learners[0]
                        });
                    }
                }
                k++;
                j++;
            }
        }

        if(detailedLinks) {
            text = trailinggroup.createText({
                x : this.centerX - lineExtremeDistanceToCenter - this.extraLineGap,
                y : 0,
                text : clfp.flow[1].title,
                align : "end"
            }).setFont(this.text.font).setFill(this.text.fill);
            new FillListener(text, this.text.fill, this.text.emphasisFill);
            var button = ClfpsRenderingCommon.addActButton(act.id, paintdata.acts[act.id].group, size.acts[act.id], uiListener);
            uiListener.registerActiveElement(button, {
                clfp : clfp,
                id : act.id
            });
        }       

        trailinggroup.createLine({
            x1 : this.centerX - lineExtremeDistanceToCenter,
            y1 : this.halfDistanceBetweenPhases,
            x2 : this.centerX + lineExtremeDistanceToCenter,
            y2 : this.halfDistanceBetweenPhases
        }).setStroke(this.lineStroke);

        /* share */

        act = clfp.flow[2];
        paintdata.acts[act.id] = ClfpsRenderingCommon.createActPaintData(group, size.acts[act.id], act.id, uiListener);
        trailinggroup = paintdata.acts[act.id].trailinggroup;
        actgroup = paintdata.acts[act.id].group;
        instancias = ClfpsCommon.getInstances(clfp, act.learners[0], uiListener);

        var image = actgroup.createImage({
            x : this.centerX - this.shareImage.width / 2,
            y : 0,
            width : this.shareImage.width,
            height : this.shareImage.height,
            src : this.imageFolder + this.shareImage.src + this.fileExtension
        });
        ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[0], instancias[0].id, image);
        
        new ImageSourceListener(image, this.imageFolder + this.shareImage.src + this.fileExtension, this.imageFolder + this.shareImage.src + this.emphasisExtension);
        if(detailedLinks) {
            uiListener.registerActiveElement(image, {
                clfp : clfp,
                idInstancia : instancias[0].id,
                id : act.learners[0],
                role : act.learners[0],
                letClone : false
            });
        }

        if(detailedLinks) {
            var text = trailinggroup.createText({
                x : this.centerX - lineExtremeDistanceToCenter - this.extraLineGap,
                y : 0,
                text : clfp.flow[2].title,
                align : "end"
            }).setFont(this.text.font).setFill(this.text.fill);
            new FillListener(text, this.text.fill, this.text.emphasisFill);
            var button = ClfpsRenderingCommon.addActButton(act.id, paintdata.acts[act.id].group, size.acts[act.id], uiListener);
            uiListener.registerActiveElement(button, {
                clfp : clfp,
                id : act.id
            });
        }

        //Dibujar los círculos de los profesores
        for( i = 0; i < 3; i++) {
            act = clfp.flow[i];
            actgroup = paintdata.acts[act.id].group;
            var posX = this.getPositionForTeacherX(i);
            var circle = actgroup.createCircle({
                cx : posX,
                cy : size.acts[act.id].pre.height/2,
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

    /**
	 * Obtiene la posición en la que se representará el profesor
	 * @param index Índice de la fase
	 * @return Posición en la que se representará el profesor
	 */
    getPositionForTeacherX : function(index) {
        return this.centerX + 1.5 * this.distanceBetweenAbcImages + 2 * this.abcImage.width + this.teacherDistance + this.teacherRadius;
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
        var posX = this.getPositionForTeacherX(index);
        return {
            x: posX + this.teacherDistance + this.teacherRadius,
            y: y
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
    },
    /**
	 * Obtiene los parámetros de representación de los cuadrados de la fase Think
	 * @param number Número de cuadrados que se desea representar
	 */
    getThinkValues : function(number) {
        var lineExtremeDistanceToCenter = 1.5 * TPSRenderer.distanceBetweenAbcImages + 2 * TPSRenderer.abcImage.width + TPSRenderer.extraLineGap;
        var rectSizeInstance, studentGapInstance, rows, columns;
        if(number <= 8) {
            rows = 1;
            columns = 8;
            rectSizeInstance = 21;
            studentGapInstance = ((lineExtremeDistanceToCenter * 2) - number * rectSizeInstance) / (number);
        } else if(number > 8 && number <= 16) {
            rows = 1;
            columns = 16;
            rectSizeInstance = 14;
            studentGapInstance = ((lineExtremeDistanceToCenter * 2) - number * rectSizeInstance) / (number);
        } else if(number > 16 && number <= 20) {
            rows = 1;
            columns = 20;
            rectSizeInstance = 10;
            studentGapInstance = ((lineExtremeDistanceToCenter * 2) - number * rectSizeInstance) / (number);
        } else if(number > 20 && number <= 50) {
            rows = 2;
            columns = 25;
            rectSizeInstance = 9.25;
            if(number > 20 && number <= 25) {
                studentGapInstance = ((lineExtremeDistanceToCenter * 2) - number * rectSizeInstance) / (number);
            } else {
                studentGapInstance = ((lineExtremeDistanceToCenter * 2) - columns * rectSizeInstance) / (columns);
            }
        } else if(number > 50 && number <= 99) {
            rows = 3;
            columns = 33;
            rectSizeInstance = 6;
            studentGapInstance = ((lineExtremeDistanceToCenter * 2) - columns * rectSizeInstance) / (columns);
        } else {
            rows = 3;
            //Se divide el total entre tres filas
            columns = Math.round(number / rows + 0.49);
            //redondeo siempre hacia arriba
            //Proporcionamos un valor fijo para la separación entre cuadrados
            studentGapInstance = 1.5;
            rectSizeInstance = (lineExtremeDistanceToCenter * 2 - columns * studentGapInstance) / (columns);
            //Si supera el tamaño disponible en vertical se ajusta el tamaño del cuadrado
            if(rectSizeInstance > (TPSRenderer.rectSize - studentGapInstance * (rows - 1)) / rows) {
                rectSizeInstance = (TPSRenderer.rectSize - studentGapInstance * (rows - 1)) / rows;
                studentGapInstance = ((lineExtremeDistanceToCenter * 2) - columns * rectSizeInstance) / (columns);
            }
        }
        return {
            rectSizeInstance : rectSizeInstance,
            studentGapInstance : studentGapInstance,
            rows : rows,
            columns : columns
        };
    },
    /** Obtiene los parámetros de representación de las imágenes de la fase Pair
	 * @param number Número de imágenes que se desea representar
	 */
    getPairValues : function(number) {
        var lineExtremeDistanceToCenter = 1.5 * TPSRenderer.distanceBetweenAbcImages + 2 * TPSRenderer.abcImage.width + TPSRenderer.extraLineGap;
        var abcImageWidth, abcImageHeight, distanceBetweenAbcImages, rows, columns;
        if(number <= 4) {
            abcImageWidth = this.abcImage.width;
            abcImageHeight = this.abcImage.height;
            rows = 1;
            columns = 4;
            distanceBetweenAbcImages = ((lineExtremeDistanceToCenter * 2) - number * abcImageWidth) / (number);
        } else if(number > 4 && number <= 6) {
            abcImageWidth = this.abcImage.width / 1.15;
            abcImageHeight = this.abcImage.height / 1.15;
            rows = 1;
            columns = 6;
            distanceBetweenAbcImages = ((lineExtremeDistanceToCenter * 2) - number * abcImageWidth) / (number);
        } else if(number > 6 && number <= 8) {
            abcImageWidth = this.abcImage.width / 1.6;
            abcImageHeight = this.abcImage.height / 1.6;
            rows = 1;
            columns = 8;
            distanceBetweenAbcImages = ((lineExtremeDistanceToCenter * 2) - number * abcImageWidth) / (number);
        } else if(number > 8 && number <= 10) {
            abcImageWidth = this.abcImage.width / 2;
            abcImageHeight = this.abcImage.height / 2;
            rows = 1;
            columns = 10;
            distanceBetweenAbcImages = ((lineExtremeDistanceToCenter * 2) - number * abcImageWidth) / (number);

        } else if(number > 10 && number <= 20) {
            //No se representan más de 20 instancias
            abcImageWidth = this.abcImage.width / 2.2;
            abcImageHeight = this.abcImage.height / 2.2;
            rows = 2;
            columns = 10;
            distanceBetweenAbcImages = ((lineExtremeDistanceToCenter * 2) - columns * abcImageWidth) / (columns);

        } else if(number > 20 && number <= 48) {
            abcImageWidth = this.abcImage.width / 3.6;
            abcImageHeight = this.abcImage.height / 3.6;
            rows = 3;
            columns = 16;
            distanceBetweenAbcImages = ((lineExtremeDistanceToCenter * 2) - columns * abcImageWidth) / (columns);
        } else {
            rows = 3;
            //Se divide el total entre tres filas
            columns = Math.round(number / rows + 0.49);
            //redondeo siempre hacia arriba
            //Proporcionamos un valor fijo para la separación entre imágenes
            distanceBetweenAbcImages = 1.5;
            abcImageWidth = (lineExtremeDistanceToCenter * 2 - columns * distanceBetweenAbcImages) / (columns);
            abcImageHeight = this.abcImage.height / (this.abcImage.width / abcImageWidth);
        }

        return {
            abcImageWidth : abcImageWidth,
            abcImageHeight : abcImageHeight,
            distanceBetweenAbcImages : distanceBetweenAbcImages,
            rows : rows,
            columns : columns
        };
    }
};