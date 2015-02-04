/**
 * @class Funcionalidad común a los patrones
 */
var ClfpsCommon = {

    /**
     * Obtiene las instancias de un grupo que se corresponden con una instancia de un patrón
     * @param clfp Patrón al que pertenecen las instancias a obtener
     * @param idGrupo Identificador del grupo al que pertenecen las instancias
     * @param uiListener 
     * @return Instancias de un grupo que se corresponden con una instancia de un patrón
     */
    getInstances : function(clfp, idGrupo, uiListener) {
        var inst = DesignInstance.instanciasGrupo(idGrupo);
        var instancias = new Array();
        //Si no es una instancia anidada está constituido por todas las instancias del grupo
        if (this.isInMain(clfp)==true){
            instancias = DesignInstance.instanciasGrupo(idGrupo);
        }else{
            if (uiListener.instanceid[clfp.id]==null){
                uiListener.instanceid[clfp.id] = DesignInstance.instanciaProcede(inst[0]).id;
            }
            var idpadre = uiListener.instanceid[clfp.id];
            //Si es una instancia anidada está constituido por todas las instancias de ese grupo que dependen de una dada
            instancias = DesignInstance.instanciasGrupoProcedenInstancia(idGrupo, idpadre);
        } 
        return instancias;
    },

    selectedGroupInstance: function(instance, uiListener){
        for(var z in uiListener.instanceid) {
            if(uiListener.instanceid[z] == instance.id) {
                return true;
            }
        }
        return false;
    },

    /**
     * Indica si es un patrón principal o dependiente de otro
     * @param clfp Patrón del que se desea conocer si es un patrón principal o dependiente de otro
     * @return Booleano que indica si es un patrón principal o no
     */
    isInMain : function(clfp) {
        for (var i=0; i<LearningDesign.data.flow.clfps.length; i++){
            if (LearningDesign.data.flow.clfps[i].id == clfp.id){
                return true;
            }
        }
        return false;
    },
    
    /**
     * Función que calcula los participantes  asignados a otras instancias del mismo grupo
     * @param instanceid Identificador de la instancia
     */
    obtainAssignedStudentsOthers: function(instanceid){
        var inMain = ClfpsCommon.isInMain(this);
        if (inMain){
            var asignadosOtras = this.obtainAssignedStudentsOthersMain(instanceid);
        }else{
            var asignadosOtras = this.obtainAssignedStudentsOthersAnidado(instanceid);
        }
        return asignadosOtras;
    },

    /**
     * Función obtiene los participantes asignados a otras instancias del mismo nivel
     * @param instanceid Identificador de la instancia de la que se desea conocer los participantes asignados a otras instancias
     */
    obtainAssignedStudentsOthersMain: function(instanceid){
        var asignadosOtras = new Array();
        var instance = IDPool.getObject(instanceid);
        var grupoInstancia = DesignInstance.grupoInstancia(instance);
        for (var i=0; i<grupoInstancia.instances.length; i++){
            if (instanceid!=grupoInstancia.instances[i].id){
                for (var j=0; j<grupoInstancia.instances[i].participants.length; j++){
                    asignadosOtras.push(grupoInstancia.instances[i].participants[j]);
                }
            }
        }
        return asignadosOtras;
    },

    /**
     * Función obtiene los participantes asignados a otras instancias del mismo nivel cuando el patrón está anidado
     * @param instanceid Identificador de la instancia de la que se desea conocer los participantes asignados a otras instancias
     */
    obtainAssignedStudentsOthersAnidado: function(instanceid){
        var asignadosOtras = new Array();
        var instance = IDPool.getObject(instanceid);
        var grupoInstancia = DesignInstance.grupoInstancia(instance);
        for (var i=0; i<grupoInstancia.instances.length; i++){
            //Sólo se consideran las que proceden de la misma instancia en el otro patrón
            if (DesignInstance.instanciaProcede(grupoInstancia.instances[i])==DesignInstance.instanciaProcede(instance)){
                if (instanceid!=grupoInstancia.instances[i].id){
                    for (var j=0; j<grupoInstancia.instances[i].participants.length; j++){
                        asignadosOtras.push(grupoInstancia.instances[i].participants[j]);
                    }
                }
            }
        }
        return asignadosOtras;
    },
    
    /**
     * Obtiene los participantes (estudiantes y profesores) disponibles para ser asignados a instancias de grupos del patrón
     * @param clfp Patrón para el que se desea conocer los participantes disponibles
     */
    obtainAvailableParticipantsClfp: function(clfp){
        var inMain = ClfpsCommon.isInMain(clfp);
        if (inMain){
            var disponibles = this.obtainAvailableParticipantsClfpMain(clfp);
        }else{
            var disponibles = this.obtainAvailableParticipantsClfpAnidado(clfp);
        }
        return disponibles;
    },
    
     /**
     * Obtiene los estudiantes disponibles para ser asignados a instancias de grupos del patrón
     * @param clfp Patrón para el que se desea conocer los participantes disponibles
     */
    obtainAvailableStudentsClfp: function(clfp){
        var inMain = ClfpsCommon.isInMain(clfp);
        if (inMain){
            var disponibles = this.obtainAvailableStudentsClfpMain(clfp);
        }else{
            var disponibles = this.obtainAvailableParticipantsClfpAnidado(clfp);
        }
        return disponibles;
    },
    
    
         /**
     * Función que obtiene los estudiantes disponibles cuando el clfp no está contenido en otro (es de primer nivel)
     * @param instanceid Instancia para la que se desea conocer los estudiantes disponibles
     */
    obtainAvailableStudentsClfpMain: function(clfp){
        var disponibles = new Array();
        for (var i=0;i<DesignInstance.data.participants.length;i++){
            if (DesignInstance.data.participants[i].participantType=="student"){
                disponibles.push(DesignInstance.data.participants[i].participantId);
            }
        }
        return disponibles;
    },
    /**
     * Obtiene los participantes (estudiantes y profesores) disponibles para ser asignados a instancias de grupos del patrón de primer nivel
     * @param clfp Patrón para el que se desea conocer los participantes disponibles para su instancia actual
     */
    obtainAvailableParticipantsClfpMain: function(clfp){
        var disponibles = new Array();
        for (var i=0;i<DesignInstance.data.participants.length;i++){
            disponibles.push(DesignInstance.data.participants[i].participantId);
        }
        return disponibles;
    },
    
    /**
     * Función Obtiene los participantes (estudiantes y profesores) disponibles para ser asignados a instancias de grupos del clfp cuando está contenido en otro (no es de primer nivel)
     * @param clfp Patrón para el que se desea conocer los participantes disponibles para su instancia actual
     */
    obtainAvailableParticipantsClfpAnidado: function(clfp){
        var disponibles = new Array();
        var instProcede = IDPool.getObject(LearningFlow.instanceid[clfp.id]);
        for (var i=0; i<instProcede.participants.length; i++){
                disponibles.push(instProcede.participants[i]);
        }
        return disponibles;
    },
    

    /**
     * Obtiene los profesores disponibles para ser asignados a una instancia
     * @param clfp Patrón para el que se desea conocer los profesores disponibles
     * @param instanceid Instancia para la que se desea conocer los profesores disponibles
     */
    obtainAvailableTeachers: function(clfp, instanceid){
        var inMain = ClfpsCommon.isInMain(clfp);
        if (inMain){
            var disponibles = this.obtainAvailableTeachersMain(instanceid);
        }else{
            var disponibles = this.obtainAvailableTeachersAnidado(instanceid);
        }
        return disponibles;
    },

    /**
     * Función que obtiene los profesores asignados a una instancia de grupo
     * @param instanceid Identificador de la instancia para la que se desea conocer los profesores asignados
     * @return Profesores asignados a la instancia
     */
    obtainAssignedTeachers: function(instanceid){
        var asignados = new Array();
        var instancia=IDPool.getObject(instanceid);
        for (var i=0; i<instancia.participants.length; i++){
            asignados.push(instancia.participants[i]);
        }
        return asignados;
    },

    /**
     * Función que obtiene los profesores disponibles cuando el clfp no está contenido en otro (es de primer nivel)
     * @param instanceid Instancia para la que se desea conocer los profesores disponibles
     */
    obtainAvailableTeachersMain: function(instanceid){
        var disponibles = new Array();
        var instancia = IDPool.getObject(instanceid);
        for (var i=0;i<DesignInstance.data.participants.length;i++){
            if (DesignInstance.data.participants[i].participantType=="teacher" && !DesignInstance.perteneceInstancia(instancia, DesignInstance.data.participants[i].participantId)){
                disponibles.push(DesignInstance.data.participants[i].participantId);
            }
        }
        return disponibles;
    },
    
     /**
     * Función que obtiene los estudiantes disponibles cuando el clfp no está contenido en otro (es de primer nivel)
     * @param instanceid Instancia para la que se desea conocer los estudiantes disponibles
     */
    obtainAvailableStudentsMain: function(instanceid){
        var disponibles = new Array();
        var instancia = IDPool.getObject(instanceid);
        for (var i=0;i<DesignInstance.data.participants.length;i++){
            if (DesignInstance.data.participants[i].participantType=="student" && !DesignInstance.perteneceInstancia(instancia, DesignInstance.data.participants[i].participantId)){
                disponibles.push(DesignInstance.data.participants[i].participantId);
            }
        }
        return disponibles;
    },

    /**
     * Función que obtiene los profesores disponibles cuando el clfp está contenido en otro (no es de primer nivel)
     * @param instanceid Instancia para la que se desea conocer los profesores disponibles
     */
    obtainAvailableTeachersAnidado: function(instanceid){
        var disponibles = new Array();
        var instancia = IDPool.getObject(instanceid);
        var instProcede = DesignInstance.instanciaProcede(instancia);
        for (var i=0; i<instProcede.participants.length; i++){
            if (!DesignInstance.perteneceInstancia(instancia, instProcede.participants[i])){
                disponibles.push(instProcede.participants[i]);
            }
        }
        return disponibles;
    },
    
        
        
        
    /**
     * Borra una instancia de un grupo del clfp indicado
     * @param idInstancia Identificador de la instancia a borrar
     * @param clfp Patrón al que pertenece la instancia a borrar
     */
    deleteInstance: function(idInstancia, clfp){
        var instancia = IDPool.objects[idInstancia];
        var rol = IDPool.getObject(DesignInstance.grupoInstancia(instancia).roleid);
                            
        //Array donde se almacenan el identificador del grupo al que pertenece cada instancia a deseleccionar
        var idGrupoBorradas = new Array();
        var clfps = DesignInstance.clfpDependenInstanciaGrupo(instancia);
                           
        for (var i=0;i<clfps.length;i++){
            var id=clfps[i].id;
            //Comprobamos si la instancia a borrar se corresponde con la seleccionada
            if (LearningFlow.instanceid[id]==idInstancia){
                //Se almacena el id de su grupo
                idGrupoBorradas.push(DesignInstance.grupoInstancia(instancia).roleid);
                //Se deselecciona la instancia
                LearningFlow.instanceid[id]=null;
            } 
                                
            if (rol.title=="Reviewers"){
                //Si la otra instancia del par está seleccionada se deselecciona
                var par = clfp.obtenerPar(instancia);          
                if (idInstancia!=par.leftReview.id && LearningFlow.instanceid[id]==par.leftReview.id){
                    //Se almacena el id de su grupo
                    idGrupoBorradas.push(DesignInstance.grupoInstancia(instancia).roleid);
                    //Se deselecciona la instancia
                    LearningFlow.instanceid[id]=null;
                } 
                else{
                    if (idInstancia!=par.rightReview.id && LearningFlow.instanceid[id]==par.rightReview.id){
                        //Se almacena el id de su grupo
                        idGrupoBorradas.push(DesignInstance.grupoInstancia(instancia).roleid);
                        //Se deselecciona la instancia
                        LearningFlow.instanceid[id]=null;
                    } 
                }
                //Si la instancia del grupo de discussion a borrar está seleccionada se selecciona
                for (var j in LearningFlow.instanceid)
                {
                    if (LearningFlow.instanceid[j]==par.discussion.id){
                        //Se almacena el id de su grupo
                        idGrupoBorradas.push(DesignInstance.grupoInstancia(IDPool.getObject(LearningFlow.instanceid[j])).roleid);
                        //Se deselecciona la instancia
                        LearningFlow.instanceid[j]=null;
                    } 
                }
                                    
            }
        }
        //Se deseleccionan las descendientes de esta que están seleccionadas
        for (var j in LearningFlow.instanceid)
        {
            if (LearningFlow.instanceid[j]!=null && DesignInstance.instanciaDesciendeInstancia(IDPool.getObject(LearningFlow.instanceid[j]), instancia.id)){
                idGrupoBorradas.push(DesignInstance.grupoInstancia(IDPool.getObject(LearningFlow.instanceid[j])).roleid); 
                LearningFlow.instanceid[j]=null;
            }
                                
            if (rol.title=="Reviewers"){
                par = clfp.obtenerPar(instancia); 
                if (idInstancia!=par.leftReview.id && LearningFlow.instanceid[j]!=null && DesignInstance.instanciaDesciendeInstancia(IDPool.getObject(LearningFlow.instanceid[j]), par.leftReview.id)){
                    idGrupoBorradas.push(DesignInstance.grupoInstancia(IDPool.getObject(LearningFlow.instanceid[j])).roleid); 
                    LearningFlow.instanceid[j]=null;
                } 
                else{
                    if (idInstancia!=par.rightReview.id && LearningFlow.instanceid[j]!=null && DesignInstance.instanciaDesciendeInstancia(IDPool.getObject(LearningFlow.instanceid[j]), par.rightReview.id)){
                        idGrupoBorradas.push(DesignInstance.grupoInstancia(IDPool.getObject(LearningFlow.instanceid[j])).roleid); 
                        LearningFlow.instanceid[j]=null;
                    } 
                }
                if (LearningFlow.instanceid[j]!=null && DesignInstance.instanciaDesciendeInstancia(IDPool.getObject(LearningFlow.instanceid[j]), par.discussion.id)){
                    //Se almacena el id de su grupo
                    idGrupoBorradas.push(DesignInstance.grupoInstancia(IDPool.getObject(LearningFlow.instanceid[j])).roleid);
                    //Se deselecciona la instancia
                    LearningFlow.instanceid[j]=null;
                } 
                                    
            }                              
        }
        //Borramos los participantes asignados a la instancia
        while (instancia.participants && instancia.participants.length>0){
            clfp.borrar(instancia,instancia.participants[0]);
        }
        //Borramos la jerarquía 
        if (clfp.patternid!="peerreview" && clfp.patternid!="tapps"){
            DesignInstance.borrarEstructura(instancia);
        }
        else{
            clfp.borrarInstancia(instancia);
        }
                            
        for (j=0; j<idGrupoBorradas.length;j++)
        {
            var nuevaSeleccionada = DesignInstance.instanciasGrupo(idGrupoBorradas[j])[0];
            clfps = DesignInstance.clfpDependenInstanciaGrupo(nuevaSeleccionada);
            for (var k=0;k<clfps.length;k++){
                LearningFlow.instanceid[clfps[k].id] = nuevaSeleccionada.id;
            }
                                
        }
    }
    
    
    
    
    
};
