/**
 * @class Renderer del patrón Tapps
 */
var TappsRenderer = {

    verticalGap : 40,
    /**
	 * Posición X central en el diseño
	 */
    middleX : 100,
    /**
	 * Ancho de la mitad de la línea de separación
	 */
    lineHalfWidth : 80,

    /**
	 * Imagen del rol estudiante
	 */
    learner : {
        src : "images/clfps/tapps/learner.png",
        emphasis : "images/clfps/tapps/learner_emph.png",
        width : 50,
        height : 63
    },

    /**
	 * Imagenes de las actividades
	 */
    activities : {
        listen : "images/clfps/tapps/listen.png",
        listenEmphasis : "images/clfps/tapps/listen_emph.png",
        solve : "images/clfps/tapps/solve.png",
        solveEmphasis : "images/clfps/tapps/solve_emph.png",
        emphasis : "images/clfps/tapps/learner_emph.png",
        width : 50,
        height : 48
    },

    /**
	 * Distancia entre fases
	 */
    distanceBetweenPhases : 35,

    /**
	 * Distancia de las imágenes al centro
	 */
    imagesDistanceToCenter : 35,

    /**
	 * Distancia al profesor
	 */
    teacherDistance : 35,
    /**
	 * Radio del círculo del profesor
	 */
    teacherRadius : 15,
    /**
	 * Separación entre pares de instancias
	 */
    distanceBetweenPairs : 60,
    /**
	 * Separación entre instancias individuales
	 */
    distanceBetweenInstances : 15,

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
	 * Obtiene el tamaño de la instancia del clfp
	 * @param clfp Clfp del que se va a obtener su tamaño
	 * @param uiListener
	 * @return Ancho y alto del clfp
	 */
    getSize : function(clfp, uiListener) {
        var separacion = 2 * this.activities.width + this.imagesDistanceToCenter + this.distanceBetweenPairs;
        var num = ClfpsCommon.getInstances(clfp, clfp.roleAid, uiListener).length;
        var xini = this.middleX - this.activities.width / 2 - this.imagesDistanceToCenter;
        var xfin = this.middleX - this.activities.width / 2 + (num - 1) * separacion + this.imagesDistanceToCenter + this.activities.width;
        var width = (xfin - xini) + this.middleX;
        
        var size = {
            width : width,
            height : 1.5 * this.verticalGap + this.learner.height + this.distanceBetweenPhases + clfp.getFlow().length * (this.activities.height + 1.5*this.distanceBetweenPhases),
            acts : {}
        };
        
        var openactheight = 0;

        for(var i = 0; i < clfp.flow.length; i++) {
            var actid = clfp.getFlow()[i].id;
            var actsize = ClfpsRenderingCommon.getOpenActSize(actid, uiListener);
            var y = openactheight + this.verticalGap + this.learner.height + this.distanceBetweenPhases + i*(this.activities.height + 1.5*this.distanceBetweenPhases);

            size.acts[actid] = {
                pre : {
                    x : 0,
                    y : y,
                    width : size.width,
                    height : this.activities.height + this.distanceBetweenPhases/2
                },
                post : {
                    x : 0,
                    y : y + this.activities.height + this.distanceBetweenPhases/2 + actsize.height
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

        var y = this.verticalGap;
        /* paint roles */
        var x = this.middleX - this.imagesDistanceToCenter - this.learner.width / 2;
        this.paintRole(x, y, clfp, group, detailedLinks, uiListener);
        y += this.learner.height;
        this.paintLine(y, group, clfp, uiListener);
        y += this.distanceBetweenPhases;

        var flow = clfp.getFlow();

        for(var i = 0; i < flow.length; i++) {
            var act = flow[i];
            paintdata.acts[act.id] = ClfpsRenderingCommon.createActPaintData(group, size.acts[act.id], act.id, uiListener);
            var trailinggroup = paintdata.acts[act.id].trailinggroup;
            this.paintAct(0, act, i, clfp, paintdata, detailedLinks, uiListener);
            y += this.activities.height;

            if(i < flow.length - 1) {
                this.paintLine(0, trailinggroup, clfp, uiListener);
            }
            y += this.distanceBetweenPhases;
            var button = ClfpsRenderingCommon.addActButton(act.id, paintdata.acts[act.id].group, size.acts[act.id], uiListener);
            uiListener.registerActiveElement(button, {
                clfp : clfp,
                id : act.id
            });
        }

        return paintdata;
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
        return {
            x : 2 * this.middleX + this.teacherDistance + 2 * this.teacherRadius,
            y : y
        };
    },
    /**
	 * Obtiene la posición X a la izquierda del patrón
	 * @param clfp Patrón
	 * @param index Índice de la fase
	 * @return Posición X a la izquierda del patrón
	 */
    getLeftDesiredPosition : function(clfp, index,uiListener) {
        var size = this.getSize(clfp, uiListener);
        var point = this.getDesiredPositionForSubplay(clfp, index, uiListener);
        point.x = size.width - point.x;
        return point;
    },
    /**
	 * Dibuja los roles en la instanciación
	 */
    paintRole: function(x, y, clfp, group, detailedLinks, uiListener) {
        var roleAId = clfp.roleAid;
        var roleBId = clfp.roleBid;
        var instanciasA = ClfpsCommon.getInstances(clfp, roleAId, uiListener);
        var instanciasB = ClfpsCommon.getInstances(clfp, roleBId, uiListener);
        for(var i = 0; i < instanciasA.length; i++) {
            //if (detailedLinks) {
            //Instancia de A
            var imageA = group.createImage({
                x : x,
                y : y,
                width : this.learner.width,
                height : this.learner.height,
                src : this.learner.src
            });
            new ImageSourceListener(imageA, this.learner.src, this.learner.emphasis);
            uiListener.registerActiveElement(imageA, {
                clfp : clfp,
                id : roleAId,
                role : roleAId,
                idInstancia : instanciasA[i].id,
                letAsignUsers : true,
                letShowClfp : false,
                letClone : true,
                letDelete : instanciasA.length > 1 //Sólo se permite borrar si hay más de una instancia del grupo
            });
            //Instancia de B
            x = x + this.learner.width + this.distanceBetweenInstances;
            var imageB = group.createImage({
                x : x,
                y : y,
                width : this.learner.width,
                height : this.learner.height,
                src : this.learner.src
            });
            new ImageSourceListener(imageB, this.learner.src, this.learner.emphasis);
            uiListener.registerActiveElement(imageB, {
                clfp : clfp,
                id : roleBId,
                role : roleBId,
                idInstancia : instanciasB[i].id,
                letAsignUsers : true,
                letShowClfp : false,
                letClone : true,
                letDelete : instanciasA.length > 1 //Sólo se permite borrar si hay más de una instancia del grupo

            });
            x = x + this.learner.width + this.distanceBetweenPairs;
        //}
        }
    },
    /**
	 * Dibuja una actividad en la instanciación
	 */
    paintAct : function(y, act, index, clfp, paintdata, detailedLinks, uiListener) {
        var actgroup = paintdata.acts[act.id].group;
        var trailinggroup = paintdata.acts[act.id].trailinggroup;
        var roleAId = clfp.roleAid;
        var roleBId = clfp.roleBid;
        var instanciasA = ClfpsCommon.getInstances(clfp, roleAId, uiListener);
        var instanciasB = ClfpsCommon.getInstances(clfp, roleBId, uiListener);
        var separacion = 2 * this.activities.width + this.distanceBetweenInstances + this.distanceBetweenPairs;
        var even = index % 2 == 0;
        for(var i = 0; i < instanciasA.length; i++) {
            /* solve */
            var x = this.middleX - this.activities.width / 2 + i * separacion;
            x += even ? -this.imagesDistanceToCenter : this.imagesDistanceToCenter;
            var roleId = even ? clfp.roleAid : clfp.roleBid;

            var image = actgroup.createImage({
                x : x,
                y : y,
                width : this.activities.width,
                height : this.activities.height,
                src : this.activities.solve
            });

            new ImageSourceListener(image, this.activities.solve, this.activities.solveEmphasis);
            if(detailedLinks) {
                uiListener.registerActiveElement(image, {
                    clfp : clfp,
                    id : act.id,
                    role : roleId
                });
            }
            ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[0], instanciasA[i].id, image);

            /* listen */
            x = this.middleX - this.activities.width / 2 + i * separacion;
            x += even ? this.imagesDistanceToCenter : -this.imagesDistanceToCenter;
            roleId = even ? clfp.roleBid : clfp.roleAid;
            image = actgroup.createImage({
                x : x,
                y : y,
                width : this.activities.width,
                height : this.activities.height,
                src : this.activities.listen
            });

            new ImageSourceListener(image, this.activities.listen, this.activities.listenEmphasis);
            if(detailedLinks) {
                uiListener.registerActiveElement(image, {
                    clfp : clfp,
                    id : act.id,
                    role : roleId
                });
            }
            ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[1], instanciasB[i].id, image);
            
        }

        var cy = y + this.activities.height / 2;
        var cx = this.middleX - this.activities.width / 2 + (instanciasA.length - 1) * separacion + this.activities.width + this.imagesDistanceToCenter + this.teacherDistance;
        var circle = actgroup.createCircle({
            cx : cx,
            cy : cy,
            r : this.teacherRadius
        }).setFill(this.teacherPaint.fill).setStroke(this.teacherPaint.stroke);
        new FillListener(circle, this.teacherPaint.fill, this.teacherPaint.emphasisFill);
        var allowAssignTeacher = 1;
        if(detailedLinks) {
            uiListener.registerActiveElement(circle, {
                clfp : clfp,
                id : act.id,
                role : act.staff[0],
                allowAssignTeacher : allowAssignTeacher
            });
        }
        ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.staff[0], DesignInstance.instanciasGrupo(act.staff[0])[0].id, circle);

        if(detailedLinks) {
            var text = trailinggroup.createText({
                x : this.middleX - this.imagesDistanceToCenter - this.activities.width / 2 - this.teacherDistance / 2,
                y : 0,
                text : act.title,
                align : "end"
            }).setFont(this.text.font).setFill(this.text.fill);
            new FillListener(text, this.text.fill, this.text.emphasisFill);
        }

    },
    /**
	 * Dibuja una línea entre fases en la instanciación
	 * @param y Posición Y de referencia para dibujar la línea
	 * @param group Superficie en la que se dibujará la línea
	 * @param clfp Clfp en el que se dibuja la línea
	 * @param uiListener
	 */
    paintLine : function(y, group, clfp, uiListener) {
        var ly = y + this.distanceBetweenPhases / 2;
        var separacion = 2 * this.activities.width + this.imagesDistanceToCenter + this.distanceBetweenPairs;
        var num = ClfpsCommon.getInstances(clfp, clfp.roleAid, uiListener).length;
        var xini = this.middleX - this.activities.width / 2 - this.imagesDistanceToCenter;
        var xfin = this.middleX - this.activities.width / 2 + (num - 1) * separacion + this.imagesDistanceToCenter + this.activities.width;
        var dif = xini - (this.middleX - this.lineHalfWidth);
        group.createLine({
            x1 : xini - dif,
            y1 : ly,
            x2 : xfin + dif,
            y2 : ly
        }).setStroke(this.lineStroke);
    }
};