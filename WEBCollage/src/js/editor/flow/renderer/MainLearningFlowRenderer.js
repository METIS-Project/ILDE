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

var MainLearningFlowRenderer = {

    renderer: null,

    init: function(){
        var node = dojo.byId("FlowPaintSurface");  //Obtiene el nodo que representa la posición donde se creará la superficie de dibujo para el flujo
        var surface = dojox.gfx.createSurface(node, 400, 400); //Crea la superficie de dibujo (SGV) indicando nodo padre, ancho y alto de la superficie
        
        this.renderer = new LearningFlowRenderer(surface, LearningFlow); //Crea el objeto encargado del renderizado del flujo de aprendizaje

        dijit.byId("FlowOptionsShowAssessment").onChange = function() {
            MainLearningFlowRenderer.update(false);
        };
        
        dijit.byId("ShowParticipantFlow").onChange = function() {
            MainLearningFlowRenderer.update(false);
        };
        
        new ZoomManager("FlowOptionsZoom", "FlowOptionsZoomValue", "FlowPaintContainer", this);

    },

    setZoom: function(zoom) {
        this.renderer.setZoom(zoom);
    },

    update: function(animate) {
                      
        if (Loader.ldShakeMode==true && (DesignInstance.data.classObj.id =="" || DesignInstance.data.lmsObj.id=="")){
            dojo.byId("divShowParticipantFlow").style.display="none";
        }
        
        var how = {
            paintAssessment: dijit.byId("FlowOptionsShowAssessment").checked, //Indica si debe pintarse el flujo de evaluación
            //paintAlerts: true, //Indica si deben pintarse las alertas,
            paintAlerts: false, //We do not want to show the alerts by now
            paintParticipantFlow: dijit.byId("ShowParticipantFlow").checked, //Indica si debe pintarse el flujo de participantes
            currentDisplayInfo: LearningFlow.currentDisplayInfo
        };

        this.renderer.repaint(animate, how);
    }
    
};
