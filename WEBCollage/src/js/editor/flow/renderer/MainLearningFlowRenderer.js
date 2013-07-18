
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
