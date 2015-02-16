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
 * @class Renderer del patrón pirámide
 */

/*global ClfpsRenderingCommon, DesignInstance, FillListener, ClfpsCommon*/

var PyramidRenderer = {

    /**
	 * Hueco para la altura
	 */
    gapY : -15, //gapY:30,
    buttonGap: 5,
    /**
	 * Almacena la información relativa a los cuadrados de la instancia
	 */
    cuadrados : [],

    height : 35,
    distance : 15,
    tan : Math.tan(Math.PI / 3),
    /**
	 * Distancia al profesor
	 */
    teacherDistance : 20,
    /**
	 * Radio del círculo del profesor
	 */
    teacherRadius : 10,
    /**
	 * Tamaño del cuadrado
	 */
    roleSize : 10,

    /**
	 * Propiedades gráficas de un nivel
	 */
    levelPaint : {
        fill : [230, 230, 50, 1.0],
        emphasisFill : [260, 260, 60, 1.0],
        stroke : {
            color : "black",
            width : 2,
            cap : "butt",
            join : 2
        }
    },

    /**
	 * Propiedades gráficas de un rol
	 */
    rolePaint : {
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
	 * Propiedades gráficas de un rol seleccionado
	 */
    roleSelectedPaint : {
        fill : [178, 0, 0, 1.0],
        emphasisFill : [204, 0, 0, 1.0],
        stroke : {
            color : "black",
            width : 4,
            cap : "butt",
            join : 1
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

    lineStroke : {
        color : "gray",
        width : 1.5,
        cap : "round",
        join : 2
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

    /**
	 * Obtiene el tamaño de la instancia del clfp
	 * @param clfp Clfp del que se va a obtener su tamaño
	 * @param uiListener
	 * @return Ancho y alto del clfp y tamaño y posiciones de los actos
	 */
    getSize : function(clfp, uiListener) {
        var height0 = clfp.levels * (this.height + this.distance + this.buttonGap);
        var size = {
            width : height0 * this.tan,
            height : height0 + this.height, 
            acts : {}
        };

        var openactheight = 0;

        for(var i = clfp.levels - 1; i >= 0; i--) {
            var actid = clfp.getFlow()[clfp.levels - 1 - i].id;
            var actsize = ClfpsRenderingCommon.getOpenActSize(actid, uiListener);

            var topY2 = (clfp.levels - i) * (this.height + this.distance + this.buttonGap);

            var y = openactheight + this.gapY + topY2;

            size.acts[actid] = {
                pre : {
                    x : 0,
                    y : y,
                    width : size.width,
                    height : this.height + this.buttonGap
                },
                post : {
                    x : 0,
                    y : y + this.height + actsize.height
                }
            };
            openactheight += actsize.height;
        }

        size.height += openactheight;

        return size;

    },
    /**
	 * Dibuja la instancia del patrón en el diseño
	 */
    paint : function(surface, clfp, transform, detailedLinks, uiListener, animator) {
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

//        var centerX = 0.5 * clfp.levels * (this.height + this.distance) * this.tan;
        var centerX = 0.5 * size.width;

        for(var i = 0; i < clfp.levels; i++) {
            this.paintLevel(paintdata, size, clfp, i, centerX, detailedLinks, uiListener, animator);
        }
		
        this.paintLines(paintdata, size, clfp, uiListener, animator);

        for(var j = 0; j < clfp.getFlow().length; j++) {
            var act = clfp.getFlow()[j];
            var actid = act.id;
            var button = ClfpsRenderingCommon.addActButton(actid, paintdata.acts[actid].group, size.acts[actid], uiListener);
            uiListener.registerActiveElement(button, {
                clfp : clfp,
                id : actid
            });
        }

        return paintdata;
    },
        
    paintLines : function(paintdata, size, clfp, uiListener, animator) {
        for(var a = 1; a < clfp.getFlow().length; a++) {
            var childrenact = clfp.getFlow()[a - 1];
            var act = clfp.getFlow()[a];
            var instances = ClfpsCommon.getInstances(clfp, act.learners[0], uiListener);
            var linesGroup = paintdata.acts[childrenact.id].group.createGroup();
            animator.addNoAnimStuff(linesGroup);

            var dy = size.acts[act.id].pre.y - size.acts[childrenact.id].pre.y;

            for(var i = 0; i < instances.length; i++) {
                var parentRect = PyramidRenderer.cuadrados[instances[i].id];
                var instHijas = DesignInstance.obtenerInstanciasHijasClfp(instances[i]);

                for(var j = 0; j < instHijas.length; j++) {
                    var childRect = PyramidRenderer.cuadrados[instHijas[j].id];
                    var linedata = {
                        x1 : childRect.shape.x + childRect.shape.width / 2,
                        y1 : childRect.shape.y + childRect.shape.height / 2,
                        x2 : parentRect.shape.x + parentRect.shape.width / 2,
                        y2 : parentRect.shape.y + parentRect.shape.width / 2 + dy
                    };
					
                    var x_y = (linedata.x1 - linedata.x2) / (linedata.y1 - linedata.y2);
					
                    if(Math.abs(x_y) > 1) {
                        var sgn = x_y > 0 ? 1 : -1;
                        x_y = Math.abs(x_y);
                        linedata.y1 += (1 + childRect.shape.height) / 2 / x_y;
                        linedata.x1 += (1 + childRect.shape.height) / 2 * sgn;
                        linedata.y2 -= (1 + parentRect.shape.height) / 2 / x_y;
                        linedata.x2 -= (1 + parentRect.shape.height) / 2 * sgn;
                    } else {
                        linedata.y1 += (1 + childRect.shape.height) / 2;
                        linedata.x1 += (1 + childRect.shape.height) / 2 * x_y;
                        linedata.y2 -= (1 + parentRect.shape.height) / 2;
                        linedata.x2 -= (1 + parentRect.shape.height) / 2 * x_y;
                    }
					
                    linesGroup.createLine(linedata).setStroke(this.lineStroke);
                }
            }
        }
    },
    /**
	 * Dibuja un nivel del patrón en la instanciación
	 */
    paintLevel : function(paintdata, size, clfp, level, centerX, detailedLinks, uiListener, animator) {

        var act = clfp.getFlow()[clfp.levels - 1 - level];

        var topY = level * (this.height + this.distance);
        var halfTopWidth = 0.5 * topY * this.tan;

        var bottomY = topY + this.height;
        var halfBottomWidth = 0.5 * bottomY * this.tan;

        // act group
        paintdata.acts[act.id] = ClfpsRenderingCommon.createActPaintData(paintdata.group, size.acts[act.id], act.id, uiListener);

        var actgroup = paintdata.acts[act.id].group;

        var points = [{
            x : centerX - halfBottomWidth,
            y : 0
        }, {
            x : centerX + halfBottomWidth,
            y : 0
        }, {
            x : centerX + halfTopWidth,
            y : this.height
        }, {
            x : centerX - halfTopWidth,
            y : this.height
        }, {
            x : centerX - halfBottomWidth,
            y : 0
        }];

        var polygon = actgroup.createPolyline(points).setFill(this.levelPaint.fill).setStroke(this.levelPaint.stroke);

        new FillListener(polygon, this.levelPaint.fill, this.levelPaint.emphasisFill);

        //Obtenemos el total de instancias del grupo
        var instancias = ClfpsCommon.getInstances(clfp, act.learners[0], uiListener);

        var num = instancias.length;
        var halfWidthForRoles = 0.5 * (halfTopWidth + halfBottomWidth);
        var halfSlot = halfWidthForRoles / (num);
        var slot = 2 * halfSlot;
        var currentX = centerX - halfWidthForRoles + halfSlot - .5 * this.roleSize;
        var currentY = 0.5 * this.height - .5 * this.roleSize;

        for(var i = 0; i < instancias.length; i++) {
            var selected = ClfpsCommon.selectedGroupInstance(instancias[i], uiListener);
            if(selected == false) {
                var rp = this.rolePaint;
            } else {
                var rp = this.roleSelectedPaint;
            } 
            var rect = actgroup.createRect({
                x : currentX,
                y : currentY,
                width : this.roleSize,
                height : this.roleSize
            }).setFill(rp.fill).setStroke(rp.stroke);
            new FillListener(rect, rp.fill, rp.emphasisFill);
            PyramidRenderer.cuadrados[instancias[i].id] = rect;
            var clfpDepende = DesignInstance.clfpDependeInstanciaGrupo(IDPool.getObject(instancias[i].id));
            var g = DesignInstance.grupoInstancia(instancias[i]);
            //Sólo se permite borrar las instancias que tienen al menos un hermano
            if(DesignInstance.instanciasGrupoMismoPadre(g.roleid, instancias[i].idParent).length > 1) {
                var letDelete = true;
            } else {
                letDelete = false;
            }
            if(detailedLinks) {
                uiListener.registerActiveElement(rect, {
                    clfp : clfp,
                    id : act.learners[0],
                    idInstancia : instancias[i].id,
                    level : level,
                    letAsignUsers : level == (clfp.levels - 1), //Pueden asignarse usuarios sólo al nivel inferior
                    letDelete : letDelete,
                    letShowClfp : clfpDepende != null
                });
            }
            ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[0], instancias[i].id, rect);
            currentX += slot;
        }

        var cy = .5 * this.height;
        var cx = centerX + halfBottomWidth + this.teacherDistance;
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

        var trailinggroup = paintdata.acts[act.id].trailinggroup;
        if(detailedLinks) {
            var text = trailinggroup.createText({
                x : centerX - halfBottomWidth - this.teacherDistance / 2,
                y : 0,
                text : act.title,
                align : "end"
            }).setFont(this.text.font).setFill(this.text.fill);
            new FillListener(text, this.text.fill, this.text.emphasisFill);
            RenameElementDialog.registerElement(text, act);
        }
    },
    /**
	 * Obtiene la posición para el patrón dependiente de la fase
	 * @param clfp Patrón
	 * @param index Índice de la fase
         * @param uiListener uiListener
	 * @return Posición para el patrón dependiente
	 */
    getDesiredPositionForSubplay : function(clfp, index, uiListener) {
        var size = this.getSize(clfp, uiListener);
        var actid = clfp.flow[index].id;
        var centerX = .5 * clfp.levels * (this.height + this.distance) * this.tan;
        var x = centerX + .5 * ((clfp.levels - index - 1) * (this.height + this.distance) + this.height) * this.tan + this.teacherDistance + 2 * this.teacherRadius + 20;
        var y = size.acts[actid].pre.y + size.acts[actid].pre.height/2;
        return {
            x : x,
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
