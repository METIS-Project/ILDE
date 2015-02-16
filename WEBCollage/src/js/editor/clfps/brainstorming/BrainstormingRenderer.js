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
 * @class Renderer del patrón Brainstorming
 */

/*global ClfpsRenderingCommon, ClfpsCommon, FilListener */

var BrainstormingRenderer = {
    /**
     * Radio de la circunferencia
     */
    radius : 60,
    /**
     * Distancia entre los cuadrados y la circunferencia
     */
    distanceToTable : 20,
    /**
     * Tamaño de los cuadrados
     */
    size : 10,
    /**
     * Número de cuadrados a representar
     */
    gap : 50,
    /**
     * Propiedades gráficas de la circunferencia
     */
    tablePaint : {
        fill : [50, 230, 30, 1.0],
        emphasisFill : [60, 260, 40, 1.0],
        stroke : {
            color : "black",
            width : 2,
            cap : "butt",
            join : "butt"
        }
    },
    /**
     * Propiedades gráficas del profesor
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
     * Propiedades gráficas del estudiante
     */
    studentPaint : {
        fill : [200, 150, 150, 1.0],
        emphasisFill : [250, 170, 170, 1.0],
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
        var actid = clfp.getFlow()[0].id;

        var size = 2 * (this.radius + this.gap);

        var sizes = {
            width : size,
            height : size,
            acts : {}
        };

        sizes.acts[actid] = {
            pre : {
                x : 0,
                y : 0,
                width : sizes.width,
                height : sizes.height
            },
            post : {
                x : 0,
                y : 0
            }
        };

        var actsize = ClfpsRenderingCommon.getOpenActSize(actid, uiListener);
        sizes.height += actsize.height;

        return sizes;
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

        var act = clfp.getFlow()[0];
        var instancias = ClfpsCommon.getInstances(clfp, act.learners[0], uiListener);
        var circle = {
            cx : this.radius + this.gap,
            cy : this.radius + this.gap,
            r : this.radius
        };

        paintdata.acts[act.id] = ClfpsRenderingCommon.createActPaintData(group, size.acts[act.id], act.id, uiListener);
        var actgroup = paintdata.acts[act.id].group;

        var table = actgroup.createCircle(circle).setFill(this.tablePaint.fill).setStroke(this.tablePaint.stroke);
        new FillListener(table, this.tablePaint.fill, this.tablePaint.emphasisFill);
        if(detailedLinks) {
            uiListener.registerActiveElement(table, {
                clfp : clfp,
                id : act.learners[0],
                role : act.learners[0],
                idInstancia : instancias[0].id,
                letAsignUsers : true,
                letShowClfp : clfpDepende != null,
                letClone : false
            });
        }
        ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.learners[0], instancias[0].id, table);

        var halfSize = this.size / 2;
        var cx = this.radius + this.gap;
        var cy = this.radius + this.gap;
        var peopleRadius = this.radius + this.distanceToTable;
        var nPeople = 1 + instancias[0].participants.length;
        var angleStep = 2 * Math.PI / nPeople;

        for(var i = 0; i < nPeople; i++) {
            var angle = i * angleStep;
            var x = cx + Math.cos(angle) * peopleRadius;
            var y = cy + Math.sin(angle) * peopleRadius;

            var paint = i == 0 ? this.teacherPaint : this.studentPaint;
            var shape = i == 0 ? actgroup.createCircle({
                cx : x,
                cy : y,
                r : this.size
            }) : actgroup.createRect({
                x : x - halfSize,
                y : y - halfSize,
                width : this.size,
                height : this.size
            });

            shape.setFill(paint.fill).setStroke(paint.stroke);

            new FillListener(shape, paint.fill, paint.emphasisFill);
            var allowAssignTeacher = 1;
            var clfpDepende = DesignInstance.clfpDependeInstanciaGrupo(IDPool.getObject(instancias[0].id));
            if(detailedLinks && i === 0) {
                uiListener.registerActiveElement(shape, {
                    clfp : clfp,
                    id : act.id,
                    role : act.staff[0],
                    allowAssignTeacher : allowAssignTeacher
                });
            }

            if(i == 0) {
                ClfpsRenderingCommon.addRoleInstanceData(paintdata.acts[act.id], act.staff[0], DesignInstance.instanciasGrupo(act.staff[0])[0].id, shape);
            }
        }

        var button = ClfpsRenderingCommon.addActButton(act.id, paintdata.acts[act.id].group, size.acts[act.id], uiListener);
        uiListener.registerActiveElement(button, {
            clfp : clfp,
            id : act.id
        });

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
        return {
            x : 2 * this.radius + 3 * this.gap,
            y : this.radius + this.gap
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
