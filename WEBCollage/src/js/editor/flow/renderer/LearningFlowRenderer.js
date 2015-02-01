/**
 * @author Eloy
 */

var LearningFlowRendererOptions = {
    gapBetweenClfps : 0,
    gapBetweenBlocks : 30,
    gapBetweenLevels : 100,

    topGap : 100,
    bottomGap : 100,

    subClfpLineStartHalfGap : 0,

    subClfpLineStroke : {
        style : "dash",
        color : "#606060",
        width : 0.5,
        cap : "round",
        join : "round",
        fill : "white"
    }
};

/**
 * Constructor del encargado de la renderización del flujo de aprendizaje
 * @param surface Superficie de dibujo
 * @param uiListener Flujo de aprendizaje
 */
function LearningFlowRenderer(surface, uiListener) {
    /**
     * Superficie de dibujo
     */
    this.surface = surface;
    /**
     * Flujo de aprendizaje asociado
     */
    this.uiListener = uiListener;
    /**
     * Unico grupo que abarca toda la superficie
     */
    this.bigGroup = surface.createGroup();
    /**
     * Animador del flujo de aprendizaje
     */
    this.animator = new LearningFlowAnim(this);
    this.renderData = null;

    this.graphicElements = {
        clfps : [],
        acts : [],
        activities : []
    };

    this.setZoom(1);
}

LearningFlowRenderer.prototype.setZoom = function(zoom) {
    this.zoom = zoom;
    if(this.renderData) {
        this.surface.setDimensions(zoom * this.renderData.mainBlock.easterMostPoint, zoom * this.renderData.mainBlock.furthestPoint);
    }

    this.bigGroup.setTransform([dojox.gfx.matrix.translate(0, 0), dojox.gfx.matrix.scale({
        x : zoom,
        y : zoom
    })]);
};
/**
 * how: {currentDisplayInfo, paintAssessment, paintAlerts
 * @param animate Indica si debe realizarse la animación a la hora de pintar
 * @param how Información necesaria a la hora de pintar
 */

LearningFlowRenderer.prototype.repaint = function(animate, how) {
    this.how = how;

    //El renderData actual pasa a ser el viejo renderData
    var old = this.renderData;
    this.renderData = this.calculatePositions();

    if(animate) {
        this.animator.animate(old.mainBlock, this.renderData.mainBlock);
    } else {
        this.paint();
    }
};
LearningFlowRenderer.prototype.paint = function() {

    this.bigGroup.clear();
    this.animator.clear();
    this.surface.setDimensions(this.zoom * this.renderData.mainBlock.easterMostPoint, this.zoom * (this.renderData.mainBlock.furthestPoint));
    CoolLineRenderer.paintMainLine(this.renderData.mainBlock, this.renderData.positions, this.bigGroup, this.animator);
    this.renderData.mainBlock.paint(this, this.how);

    //Obtener infomación de graphicsElements
    this.calculateGraphicElementPositions();
    GroupsLayer.beginObtainParticipantFlow(this);
    //Actualizar la información de las alertas de patrones de grupo
    GroupPatternManager.check();

    if(this.how.paintAssessment) {
        AssessmentFlowRenderer.paint(this, this.how.paintAlerts);
    }
    
    //Si está activado el checkbox correspondiente se muestra el flujo de participantes
    if(this.how.paintParticipantFlow) {
        GroupsLayer.renderer.paint(this, GroupsLayer.participantFlow);
        GroupsAlerts.paint(this, GroupsLayer.participantFlow);
    }

    if(this.how.highlightPhase) {
        this.highlightPhase(this.how.highlightPhase);
    }

    if(this.how.paintClfpPositions) {
        this.paintPotentialClfpPositions();
    }
};
/**
 * Obtiene las posiciones y dimensiones de los elementos representativos del diseño
 */
LearningFlowRenderer.prototype.calculateGraphicElementPositions = function() {
    this.graphicElements.clfps = [];
    this.graphicElements.acts = [];
    this.graphicElements.activities = [];
    this.calculateGraphicBlockPositions(this.renderData.mainBlock);
};
 
LearningFlowRenderer.prototype.calculateGraphicBlockPositions = function(block) {
    for(var i = 0; i < block.renderClfps.length; i++) {
        this.addGraphicElementsClfp(block.renderClfps[i]);
        this.addGraphicElementsActs(block.renderClfps[i], block.renderClfps[i].paintdata);
        this.addGraphicElementsActivities(block.renderClfps[i], block.renderClfps[i].paintdata);
        for(var j = 0; j < block.renderClfps[i].subBlocks.length; j++) {
            this.calculateGraphicBlockPositions(block.renderClfps[i].subBlocks[j]);
        }
    }
},
/**
 * Obtiene las posiciones y dimensiones de los elementos gráficos relativos a los CLFPs
 */
LearningFlowRenderer.prototype.addGraphicElementsClfp = function(renderClfp) {
    var idclfp = renderClfp.clfp.id;
    this.graphicElements.clfps[idclfp] = {
        x : renderClfp.position.x,
        y : renderClfp.position.y,
        width : renderClfp.size.width,
        height : renderClfp.size.height
    };

/*this.bigGroup.createRect({
	 x : this.graphicElements.clfps[idclfp].x,
	 y : this.graphicElements.clfps[idclfp].y,
	 width : this.graphicElements.clfps[idclfp].width,
	 height : this.graphicElements.clfps[idclfp].height
	 }).setFill([0, 0, 0, 0]);*/
};
/**
 * Obtiene las posiciones y dimensiones de los elementos gráficos relativos a los actos
 */
LearningFlowRenderer.prototype.addGraphicElementsActs = function(renderClfp, paintdata) {
    //Obtener posiciones y tamaños de los actos
    for(var id in paintdata.size.acts) {
        this.graphicElements.acts[id] = {
            rect : {
                x : renderClfp.position.x + paintdata.size.acts[id].pre.x * renderClfp.scale,
                y : renderClfp.position.y + paintdata.size.acts[id].pre.y * renderClfp.scale,
                width : paintdata.size.acts[id].pre.width * renderClfp.scale,
                height : paintdata.size.acts[id].pre.height * renderClfp.scale
            },
            roles : [],
            instances : []
        };

    /*this.bigGroup.createRect({
		 x : this.graphicElements.acts[id].rect.x,
		 y : this.graphicElements.acts[id].rect.y,
		 width : this.graphicElements.acts[id].rect.width,
		 height : this.graphicElements.acts[id].rect.height
		 }).setFill([0, 0, 0, 0]);*/
    }

    //Obtener posiciones y tamaños de los grupos e instancias de estos que aparecen en los actos
    for(var ida in paintdata.acts) {
        for(var idr in paintdata.acts[ida].roles) {
            //Obtener posiciones y tamaños del rol/grupo que aparece en el acto
            this.graphicElements.acts[ida].roles[idr] = {
                x : this.graphicElements.acts[ida].rect.x + paintdata.acts[ida].roles[idr].bbox.x * renderClfp.scale,
                y : this.graphicElements.acts[ida].rect.y + paintdata.acts[ida].roles[idr].bbox.y * renderClfp.scale,
                width : paintdata.acts[ida].roles[idr].bbox.width * renderClfp.scale,
                height : paintdata.acts[ida].roles[idr].bbox.height * renderClfp.scale
            };

            /*this.bigGroup.createRect({
			x : this.graphicElements.acts[ida].roles[idr].x,
			y : this.graphicElements.acts[ida].roles[idr].y,
			width : this.graphicElements.acts[ida].roles[idr].width,
			height : this.graphicElements.acts[ida].roles[idr].height
			}).setFill([0, 0, 0,0]);*/

            //Obtener posiciones y tamaños de las instancias del rol/grupo
            for(var idi in paintdata.acts[ida].roles[idr].instances) {
                this.graphicElements.acts[ida].instances[idi] = {
                    x : this.graphicElements.acts[ida].rect.x + paintdata.acts[ida].roles[idr].instances[idi].x * renderClfp.scale,
                    y : this.graphicElements.acts[ida].rect.y + paintdata.acts[ida].roles[idr].instances[idi].y * renderClfp.scale,
                    width : paintdata.acts[ida].roles[idr].instances[idi].width * renderClfp.scale,
                    height : paintdata.acts[ida].roles[idr].instances[idi].height * renderClfp.scale
                };

            /*this.bigGroup.createRect({
				 x : this.graphicElements.acts[ida].instances[idi].x,
				 y : this.graphicElements.acts[ida].instances[idi].y,
				 width : this.graphicElements.acts[ida].instances[idi].width,
				 height : this.graphicElements.acts[ida].instances[idi].height
				 }).setFill([0, 0, 0, 0]);*/
            }
        }
    }
},
/**
 * Obtiene las posiciones y dimensiones de los elementos gráficos relativos a las actividades
 */
LearningFlowRenderer.prototype.addGraphicElementsActivities = function(renderClfp, paintdata) {
    for(var ida in paintdata.acts) {
        if(paintdata.acts[ida].activitiessize) {
            for(var idactivity in paintdata.acts[ida].activitiessize) {
                this.graphicElements.activities[idactivity] = {
                    rect : {
                        x : this.graphicElements.acts[ida].rect.x + paintdata.acts[ida].activitiessize[idactivity].rect.x * renderClfp.scale,
                        y : this.graphicElements.acts[ida].rect.y + paintdata.size.acts[ida].pre.height * renderClfp.scale + paintdata.acts[ida].activitiessize[idactivity].rect.y * renderClfp.scale,
                        width : paintdata.acts[ida].activitiessize[idactivity].rect.width * renderClfp.scale,
                        height : paintdata.acts[ida].activitiessize[idactivity].rect.height * renderClfp.scale
                    },
                    instances : []
                };

                /*this.bigGroup.createRect({
				 x : this.graphicElements.activities[idactivity].rect.x,
				 y : this.graphicElements.activities[idactivity].rect.y,
				 width : this.graphicElements.activities[idactivity].rect.width,
				 height : this.graphicElements.activities[idactivity].rect.height
				 }).setFill([0, 0, 0, 0]);*/

                for(var idinstance in paintdata.acts[ida].activitiessize[idactivity].instances) {
                    this.graphicElements.activities[idactivity].instances[idinstance] = {
                        x : this.graphicElements.acts[ida].rect.x + paintdata.acts[ida].activitiessize[idactivity].instances[idinstance].x * renderClfp.scale,
                        y : this.graphicElements.acts[ida].rect.y + paintdata.size.acts[ida].pre.height * renderClfp.scale + paintdata.acts[ida].activitiessize[idactivity].instances[idinstance].y * renderClfp.scale,
                        width : paintdata.acts[ida].activitiessize[idactivity].instances[idinstance].width * renderClfp.scale,
                        height : paintdata.acts[ida].activitiessize[idactivity].instances[idinstance].height * renderClfp.scale
                    };

                /*this.bigGroup.createRect({
					 x : this.graphicElements.activities[idactivity].instances[idinstance].x,
					 y : this.graphicElements.activities[idactivity].instances[idinstance].y,
					 width : this.graphicElements.activities[idactivity].instances[idinstance].width,
					 height : this.graphicElements.activities[idactivity].instances[idinstance].height
					 }).setFill([0, 0, 0, 0]);*/
                }
            }
        }
    }

},


/**
 * Obtiene las posiciones en las que se debe pintar en función del identificador del CLFP que tiene el foco
 */
LearningFlowRenderer.prototype.calculatePositions = function() {
    var mainBlock = new LearningFlowRenderBlock(LearningDesign.data.flow, this.how.currentDisplayInfo, null, this.uiListener);
    mainBlock.position();

    var widths = [];
    mainBlock.reckonWidths(0, widths);

    var positions = this.getXPositions(widths);
    mainBlock.finalPosition(0, positions);
    mainBlock.easterMostPoint = widths.length > 0 ? (positions[positions.length - 1] + .5 * widths[widths.length - 1]) : 0;
    mainBlock.furthestPoint += LearningFlowRendererOptions.bottomGap;

    var separators = mainBlock.getSeparators();

    return {
        mainBlock : mainBlock,
        widths : widths,
        positions : positions,
        separators : separators
    };
};

LearningFlowRenderer.prototype.getXPositions = function(widths) {
    var positions = [];
    var total = LearningFlowRendererOptions.gapBetweenLevels;
    for(var i = 0; i < widths.length; i++) {
        positions[i] = total + .5 * widths[i];
        total += widths[i] + LearningFlowRendererOptions.gapBetweenLevels;
    }

    positions.push(total);

    return positions;
};

LearningFlowRenderer.prototype.paintLineToSub = function(parentRenderClfp, subBlock) {
    var points = [{
        x : parentRenderClfp.position.x + subBlock.desiredPosition.x,
        y : parentRenderClfp.position.y + subBlock.desiredPosition.y - LearningFlowRendererOptions.subClfpLineStartHalfGap * parentRenderClfp.scale
    }, {
        x : this.renderData.positions[subBlock.level] - .5 * this.renderData.widths[subBlock.level],
        y : subBlock.getY()
    }, {
        x : this.renderData.positions[subBlock.level] + .5 * this.renderData.widths[subBlock.level],
        y : subBlock.getY()
    }];

    var points2 = [{
        x : this.renderData.positions[subBlock.level] + .5 * this.renderData.widths[subBlock.level],
        y : subBlock.getBottom()
    }, {
        x : this.renderData.positions[subBlock.level] - .5 * this.renderData.widths[subBlock.level],
        y : subBlock.getBottom()
    }, {
        x : parentRenderClfp.position.x + subBlock.desiredPosition.x,
        y : parentRenderClfp.position.y + subBlock.desiredPosition.y + LearningFlowRendererOptions.subClfpLineStartHalfGap * parentRenderClfp.scale
    }];

    //        var points3 = [points[0], points[1], points[2], points2[0], points2[1], points2[2], ];

    //        LearningFlowAnim.addNoAnimStuff(this.surface.createPolyline(points3).setFill(this.subClfpLineStroke.fill).moveToBack());
    this.animator.addNoAnimStuff(this.bigGroup.createPolyline(LinePainter.transformLine(points)).setStroke(LearningFlowRendererOptions.subClfpLineStroke));
    this.animator.addNoAnimStuff(this.bigGroup.createPolyline(LinePainter.transformLine(points2)).setStroke(LearningFlowRendererOptions.subClfpLineStroke));
};

LearningFlowRenderer.prototype.addIcon = function(center, icon, scale) {

    var shapeData = {
        src : icon.src,
        x : center.x - .5 * icon.width * scale,
        y : center.y - .5 * icon.height * scale,
        width : icon.width * scale,
        height : icon.height * scale
    };

    center.width = shapeData.width;
    center.height = shapeData.height;

    var shape = this.bigGroup.createImage(shapeData);
    this.animator.addNoAnimStuff(shape);
    return shape;
};
