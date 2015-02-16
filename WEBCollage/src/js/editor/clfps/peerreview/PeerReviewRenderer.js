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
 * @class Renderer del patrón Peer Review
 */
var PeerReviewRenderer = {

    verticalGap : 40,
    /**
	 * Posición X central en el diseño
	 */
    middleX : 100,
    
    lineHalfWidth : 80,

    /**
	 * Imagen izquierda del par de instancias de review
	 */
    reviewInstanceA : {
        src : "images/clfps/peerreview/review_a.png",
        selected : "images/clfps/peerreview/review_a_s.png",
        emphasis : "images/clfps/peerreview/review_emph_a.png",
        emphasis_selected : "images/clfps/peerreview/review_emph_a_s.png",
        width : 54,
        height : 61
    },

    /**
	 * Imagen derecha del par de instancias de review
	 */
    reviewInstanceB : {
        src : "images/clfps/peerreview/review_b.png",
        selected : "images/clfps/peerreview/review_b_s.png",
        emphasis : "images/clfps/peerreview/review_emph_b.png",
        emphasis_selected : "images/clfps/peerreview/review_emph_b_s.png",
        width : 54,
        height : 61
    },

    /**
	 * Imagen izquierda del par de instancias de feedback
	 */
    feedbackInstanceA : {
        src : "images/clfps/peerreview/feedbackeval_a.png",
        selected : "images/clfps/peerreview/feedbackeval_a_s.png",
        emphasis : "images/clfps/peerreview/feedbackeval_emph_a.png",
        emphasis_selected : "images/clfps/peerreview/feedbackeval_emph_a_s.png",
        width : 54,
        height : 65
    },

    /**
	 * Imagen izquierda del par de instancias de feedback
	 */
    feedbackInstanceB : {
        src : "images/clfps/peerreview/feedbackeval_b.png",
        selected : "images/clfps/peerreview/feedbackeval_b_s.png",
        emphasis : "images/clfps/peerreview/feedbackeval_emph_b.png",
        emphasis_selected : "images/clfps/peerreview/feedbackeval_emph_b_s.png",
        width : 54,
        height : 65
    },

    /**
	 * Imagen de discussion
	 */
    discussionInstance : {
        src : "images/clfps/peerreview/discussion.png",
        selected : "images/clfps/peerreview/discussion_s.png",
        emphasis : "images/clfps/peerreview/discussion_emph.png",
        emphasis_selected : "images/clfps/peerreview/discussion_emph_s.png",
        width : 57,
        height : 60
    },

    /**
	 * Imagen izquierda del par de instancias de refine and improve
	 */
    improvementInstanceA : {
        src : "images/clfps/peerreview/improvement_a.png",
        selected : "images/clfps/peerreview/improvement_a_s.png",
        emphasis : "images/clfps/peerreview/improvement_emph_a.png",
        emphasis_selected : "images/clfps/peerreview/improvement_emph_a_s.png",
        width : 54,
        height : 33
    },

    /**
	 * Imagen derecha del par de instancias de refine and improve
	 */
    improvementInstanceB : {
        src : "images/clfps/peerreview/improvement_b.png",
        selected : "images/clfps/peerreview/improvement_b_s.png",
        emphasis : "images/clfps/peerreview/improvement_emph_b.png",
        emphasis_selected : "images/clfps/peerreview/improvement_emph_b_s.png",
        width : 54,
        height : 33
    },

    /**
	 * Imagenes del diseño
	 */
    images : [{
        src : "images/clfps/peerreview/review.png",
        emphasis : "images/clfps/peerreview/review_emph.png",
        width : 109,
        height : 61
    }, {
        src : "images/clfps/peerreview/feedbackeval.png",
        emphasis : "images/clfps/peerreview/feedbackeval_emph.png",
        width : 109,
        height : 65
    }, {
        src : "images/clfps/peerreview/discussion.png",
        emphasis : "images/clfps/peerreview/discussion_emph.png",
        width : 57,
        height : 60
    }, {
        src : "images/clfps/peerreview/improvement.png",
        emphasis : "images/clfps/peerreview/improvement_emph.png",
        width : 109,
        height : 33
    }],

    /**
	 * Distancia entre fases
	 */
    distanceBetweenPhases : 25,

    /**
	 * Distancia al profesor
	 */
    teacherDistance : 80,
    /**
	 * Radio del círculo del profesor
	 */
    teacherRadius : 15,

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
	 * Separación entre pares de instancias
	 */
    distanceBetweenPairs : 17,
    /**
	 * Redución de la separación entre las dos intancias que constituyen el par
	 */
    decreaseDistance : -8,

    /**
	 * Obtiene el tamaño de la instancia del clfp
	 * @param clfp Clfp del que se va a obtener su tamaño
	 * @param uiListener
	 * @return Ancho y alto del clfp
	 */
    getSize : function(clfp, uiListener) {
        var height = 2 * this.verticalGap + 7 * this.distanceBetweenPhases;
        for(var i in this.images) {
            height += this.images[i].height;
        }
        var instancias = ClfpsCommon.getInstances(clfp, clfp.flow[0].learners[0], uiListener);
        var size = {
            width :  this.middleX + instancias.length * this.reviewInstanceA.width + (instancias.length / 2 - 1) * this.distanceBetweenPairs,
            height : height,
            acts : {}
        };
               
        var phaseHeight = new Array(); //Altura de cada fase
        var accumulatedPhaseHeight = new Array(); //Altura acumulada de las fases                   
        
        phaseHeight[0]= this.reviewInstanceA.height + this.distanceBetweenPhases;
        phaseHeight[1]= this.feedbackInstanceB.height + this.distanceBetweenPhases;
        phaseHeight[2]= this.discussionInstance.height + this.distanceBetweenPhases;
        phaseHeight[3]= this.improvementInstanceA.height + this.distanceBetweenPhases;
        
        accumulatedPhaseHeight[0]=0;
        for (var i=1;i<phaseHeight.length;i++)
        {
            accumulatedPhaseHeight[i]=accumulatedPhaseHeight[i-1] + phaseHeight[i-1] + this.distanceBetweenPhases;
        }
        
        var openactheight = 0;
        
        for(var i = 0; i < 4; i++) {
            var actid = clfp.getFlow()[i].id;
            var actsize = ClfpsRenderingCommon.getOpenActSize(actid, uiListener);
            var y = openactheight + accumulatedPhaseHeight[i] + this.verticalGap;

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

        //Obtenemos el total de instancias del grupo review a mostrar para proporcionarselo a la función que pinta las líneas
        var instancias = ClfpsCommon.getInstances(clfp, clfp.flow[0].learners[0], uiListener);
        var y = 0;

        for(var i = 0; i < clfp.flow.length; i++) {
            var act = clfp.flow[i];
            paintdata.acts[act.id] = ClfpsRenderingCommon.createActPaintData(group, size.acts[act.id], act.id, uiListener);
            var trailinggroup = paintdata.acts[act.id].trailinggroup;
            var imageData = new Array();
            switch(i) {
                case 0: {
                    imageData[0] = this.reviewInstanceA;
                    imageData[1] = this.reviewInstanceB;
                    this.paintReviewInstance(y, clfp, act, imageData, paintdata, detailedLinks, uiListener, i);
                    break;
                }
                case 1: {
                    imageData[0] = this.feedbackInstanceA;
                    imageData[1] = this.feedbackInstanceB;
                    this.paintReviewInstance(y, clfp, act, imageData, paintdata, detailedLinks, uiListener, i);
                    break;
                }
                case 2: {
                    imageData[0] = this.discussionInstance;
                    this.paintDiscussionInstance(y, clfp, act, imageData[0], paintdata, detailedLinks, uiListener);
                    break;
                }
                case 3: {
                    imageData[0] = this.improvementInstanceA;
                    imageData[1] = this.improvementInstanceB;
                    this.paintReviewInstance(y, clfp, act, imageData, paintdata, detailedLinks, uiListener, i);
                    break;
                }
            }
            if(i < clfp.flow.length - 1) {
                this.paintLineInstance(y, trailinggroup, instancias.length);
            }
            var button = ClfpsRenderingCommon.addActButton(act.id, paintdata.acts[act.id].group, size.acts[act.id], uiListener);
            uiListener.registerActiveElement(button, {
                clfp : clfp,
                id : act.id
            });
        }

        return paintdata;
    },

    /**
	 * Dibuja las instancias en la fase review
	 */
    paintReviewInstance : function(y, clfp, act, imageData, paintdata, detailedLinks, uiListener, index) {
        var actgroup = paintdata.acts[act.id].group;
        var trailinggroup = paintdata.acts[act.id].trailinggroup;
        var x = this.middleX - (imageData[0].width + imageData[1].width) / 2;
        var roleId = act.learners[0];

        var instancias = ClfpsCommon.getInstances(clfp, act.learners[0], uiListener);
        for(var i = 0; i < instancias.length; i++) {
            var selected = ClfpsCommon.selectedGroupInstance(instancias[i], uiListener);
            var clfpDepende = DesignInstance.clfpDependeInstanciaGrupo(IDPool.getObject(instancias[i].id));
            if(i % 2 == 0) {
                var imageIndex=0;
                var currentX = x + i / 2 * (imageData[0].width + imageData[1].width + this.distanceBetweenPairs);
            } else {
                var imageIndex=1;
                var currentX = x + i / 2 * (imageData[0].width + imageData[1].width + this.distanceBetweenPairs) + this.decreaseDistance;
            }
            if(selected) {
                var image = actgroup.createImage({
                    x : currentX,
                    y : y,
                    width : imageData[imageIndex].width,
                    height : imageData[imageIndex].height,
                    src : imageData[imageIndex].selected
                });
                new ImageSourceListener(image, imageData[imageIndex].selected, imageData[imageIndex].emphasis_selected);
            } else {
                image = actgroup.createImage({
                    x : currentX,
                    y : y,
                    width : imageData[imageIndex].width,
                    height : imageData[imageIndex].height,
                    src : imageData[imageIndex].src
                });
                new ImageSourceListener(image, imageData[imageIndex].src, imageData[imageIndex].emphasis);
            }
            if(detailedLinks) {
                uiListener.registerActiveElement(image, {
                    clfp : clfp,
                    //id: act.id,
                    idInstancia : instancias[i].id,
                    id : act.learners[0],
                    role : roleId,
                    letClone : index==0,
                    letAsignUsers : index==0,
                    letDelete : instancias.length > 2 && index==0,
                    letShowClfp : clfpDepende != null
                });
            }
            ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[0], instancias[i].id, image);
        }

        /* teacher */
        var cy = y + imageData[0].height / 2;
        var cx = this.middleX + this.teacherDistance + (instancias.length / 2 - 1) * (imageData[0].width + imageData[1].width + this.distanceBetweenPairs);
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

        if(detailedLinks) {
            var text = trailinggroup.createText({
                x : this.middleX - this.teacherDistance + this.teacherRadius,
                y : 0,
                text : act.title,
                align : "end"
            }).setFont(this.text.font).setFill(this.text.fill);
            new FillListener(text, this.text.fill, this.text.emphasisFill);
        }
    },

    /**
	 * Dibuja las instancias en la fase discussion
	 */
    paintDiscussionInstance : function(y, clfp, act, imageData, paintdata, detailedLinks, uiListener) {
        var actgroup = paintdata.acts[act.id].group;
        var trailinggroup = paintdata.acts[act.id].trailinggroup;
        var x = this.middleX - imageData.width;
        var roleId = act.learners[0];

        var instancias = ClfpsCommon.getInstances(clfp, act.learners[0], uiListener);
        for(var i = 0; i < instancias.length; i++) {
            //Mostrar de forma diferente si la instancia se encuentra seleccionada o no
            var selected = ClfpsCommon.selectedGroupInstance(instancias[i], uiListener);

            var posX = ((x + i * (2 * this.feedbackInstanceA.width + this.distanceBetweenPairs)) + (x + (i + 1) * (2 * this.feedbackInstanceA.width + this.distanceBetweenPairs) + this.decreaseDistance)) / 2 - imageData.width / 2;
            if(selected) {
                var image = actgroup.createImage({
                    x : posX,
                    y : y,
                    width : imageData.width,
                    height : imageData.height,
                    src : imageData.selected
                });

                new ImageSourceListener(image, imageData.selected, imageData.emphasis_selected);
            } else {
                image = actgroup.createImage({
                    x : posX,
                    y : y,
                    width : imageData.width,
                    height : imageData.height,
                    src : imageData.src
                });

                new ImageSourceListener(image, imageData.src, imageData.emphasis);
            }
            var clfpDepende = DesignInstance.clfpDependeInstanciaGrupo(IDPool.getObject(instancias[i].id));
            if(detailedLinks) {
                uiListener.registerActiveElement(image, {
                    clfp : clfp,
                    idInstancia : instancias[i].id,
                    id : act.learners[0],
                    role : roleId,
                    letClone : false,
                    letShowClfp : clfpDepende != null
                });
            }
            ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[0], instancias[i].id, image);
        }

        /* teacher */
        var cy = y + imageData.height / 2;
        var cx = this.middleX + this.teacherDistance + (instancias.length - 1) * (2 * this.feedbackInstanceA.width + this.distanceBetweenPairs);

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
                x : this.middleX - this.teacherDistance + this.teacherRadius,
                y : 0,
                text : act.title,
                align : "end"
            }).setFont(this.text.font).setFill(this.text.fill);
            new FillListener(text, this.text.fill, this.text.emphasisFill);
        }
    },
    /**
	 * Dibuja las líneas en la instanciación
	 */
    paintLineInstance : function(y, group, numberOfInstances) {

        var ly = y + this.distanceBetweenPhases / 2;
        group.createLine({
            x1 : this.middleX - this.lineHalfWidth,
            y1 : ly,
            x2 : this.middleX + this.teacherDistance + (numberOfInstances / 2 - 1) * (this.reviewInstanceA.width + this.reviewInstanceA.width + this.distanceBetweenPairs),
            y2 : ly
        }).setStroke(this.lineStroke);
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
        var instancias = ClfpsCommon.getInstances(clfp, clfp.flow[0].learners[0], uiListener);
        return {
            x : this.middleX - (this.reviewInstanceA.width + this.reviewInstanceB.width) / 2 + this.teacherDistance + 3 * this.teacherRadius + (instancias.length - 1) * (this.reviewInstanceA.width) + (instancias.length / 2 - 1) * this.distanceBetweenPairs,
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