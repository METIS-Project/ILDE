/**
 * Gestión de la pestaña Participant management
 */
var ParticipantManagement = {
    
    /**
     * Array auxiliar donde se guardan los identificadores de los participantes que se desasignan de una instancia de un grupo
     */
    deletedParticipants: new Array(),
    
    /**
     * Función de inicialización
     */
    init: function() {
        dojo.style("numeroParticipantesElegidos","display","none");
        dojo.style("createGluePSDesign","display","none");
        dojo.style("tablaInfo","display","none");
        
        //Ventana Cambios Alumnos
        dojo.connect(dojo.byId("botonCancelarCambiosAlumnos"), "onclick", function(){
            ParticipantManagement.hideParticipantComparation();
        });
        dojo.connect(dojo.byId("botonAceptarCambiosAlumnos"), "onclick", function(){
            ParticipantManagement.acceptParticipantChanges();
        });
        //Botón de actualización de participantes
        dojo.connect(dojo.byId("actualizarParticipantes"), "onclick", function(){
            ParticipantManagement.showParticipantUpdate();
        });
        
        //Ventana de cambiar tipo participante
        dojo.connect(dojo.byId("changeTypeDialogOk"), "onclick", function(){
            ParticipantManagement.changeTypeConfirm();
        });
        
        //Ventana de cambiar tipo participante
        dojo.connect(dojo.byId("changeTypeDialogCancel"), "onclick", function(){
            ParticipantManagement.hideChangeTypeConfirm();
        });
    },  
    
    /**
     * Muestra los cambios que se producirán al actualizar la lista de participantes para la instalación y clase actuales
     */
    showParticipantUpdate: function(){
        //Para comprobar que se ha seleccionado la clase y la instalación
        if (DesignInstance.data && DesignInstance.data.instObj.id!=""){
            
            if (DesignInstance.data.lmsObj.id==""){
                ParticipantManagement.getChosenParticipants(DesignInstance.data.instObj.id, DesignInstance.data.classObj.id);
            }
            else{
                //Obtiene los participantes de glue
                ParticipantManagement.getChosenParticipantsGlue(DesignInstance.data.instObj.id, DesignInstance.data.lmsObj.id, DesignInstance.data.classObj.id);
            }
        }
        else{
            this.showDialogAlert(i18n.get("actualizar.aviso"),i18n.get("actualizar.aviso.info"));
        }
    },
        
    /**
     * Muestran las modificaciones que se producen en el listado de participantes al elegir una instalación y clase dados
     * @param idlms_installation Instalación de la que se obtienen los participantes
     * @param idclase Clase de la que se obtienen los participantes
     */
    getChosenParticipants: function(idlms_installation, idclase){
        var bindArgs = {
            url: "manager/lms/controladorLms.php",
            handleAs: "json",
            content: {
                task: "obtener_participantes",
                idlms_installation: idlms_installation,
                idclase: idclase
            },
            load: function(data){
                ParticipantManagement.addInfo(idlms_installation, idclase);
                ParticipantManagement.showParticipantsAdd(data);
                ParticipantManagement.showParticipantsDelete(data);
                ParticipantManagement.showParticipantsRemain(data);
                dijit.byId("comparacionAlumnos").show();
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
     * Muestran las modificaciones que se producen en el listado de participantes al elegir una instalación de Glue
     * @param idlms_installation Instalación de la que se obtienen los participantes
     * @param idlmsSystem LMS del que se obtienen los participantes
     * @param idlmsCourse Clase de la que se obtienen los participantes
     */
    getChosenParticipantsGlue: function(idlms_installation, idlmsSystem, idlmsCourse){
        var bindArgs = {
            url: "manager/lms/controladorLms.php",
            handleAs: "json",
            content: {
                task: "obtener_participantes_lms",
                idlms_installation: idlms_installation,
                idlmsSystem: idlmsSystem,
                idlmsCourse: idlmsCourse
            },
            load: function(data){
                ParticipantManagement.addInfoGlue(idlms_installation, idlmsSystem, idlmsCourse);
                ParticipantManagement.showParticipantsAdd(data);
                ParticipantManagement.showParticipantsDelete(data);
                ParticipantManagement.showParticipantsRemain(data);
                dijit.byId("comparacionAlumnos").show();
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    
    /** 
     * Almacena información de la instalación seleccionada
     * @param idlms_installation Identificador de la instalación seleccionada
     * @param idclase Identificador de la clase seleccionada
     */
    addInfo: function(idlms_installation, idclase){
        dijit.byId("comparacionAlumnosIdInst").setValue(idlms_installation);
        dijit.byId("comparacionAlumnosIdClase").setValue(idclase);
        //No se ha elegido un lms de glue
        dijit.byId("comparacionAlumnosIdLmsGlue").setValue("0");
    },
    
    /** 
     * Almacena información de la instalación de Glue seleccionada
     * @param idlms_installation Identificador de la instalación seleccionada
     * @param idlmsSystem Identificador del LSM seleccionado
     * @param idlmsCourse Identificador de la clase seleccionada
     */
    addInfoGlue: function(idlms_installation, idlmsSystem, idlmsCourse){
        dijit.byId("comparacionAlumnosIdInst").setValue(idlms_installation);
        dijit.byId("comparacionAlumnosIdClase").setValue(idlmsCourse);
        dijit.byId("comparacionAlumnosIdLmsGlue").setValue(idlmsSystem);
    },
    
    /**
     * Muestra en una tabla el listado de participantes que se van a añadir
     */
    showParticipantsAdd: function(data){
        var table = document.getElementById("tablaAlumnosAnyadidos");
        //Se elimina el contenido anterior de la tabla
        if (table.hasChildNodes()){
            while (table.childNodes.length>=1){
                table.removeChild(table.firstChild);
            }
        }
        var numero=0;
        if (data.participantes.length>0){
            var th = table.appendChild(document.createElement("th"));
            th.innerHTML = i18n.get("participantes.nombre");
            table.className="participantsTable";
            var participantes = new Array();
            for (var i=0; i<data.participantes.length;i++)
            {
                participantes.push(data.participantes[i]);
            }
            participantes.sort(natcompare);
            //Se construye la tabla con la nueva información
            for (var j in participantes){
                if (DesignInstance.existsParticipant(participantes[j])==false){
                    var tr = table.appendChild(document.createElement("tr"));
                    var td = tr.appendChild(document.createElement("td"));
                    td.innerHTML = participantes[j].name;
                    numero++;
                }
            }
        }
        var per=document.getElementById("numAlumnosAnadidos");
        per.innerHTML = i18n.get("participantes.alumnosAnadidos")+ ": "+ numero;
        per.style.fontWeight = 'bold';
    },
    
    /**
     * Muestra en una tabla el listado de participantes que se van a eliminar
     */
    showParticipantsDelete: function(data){
        this.deletedParticipants.splice(0,this.deletedParticipants.length);
        var table = document.getElementById("tablaAlumnosEliminados");
        //Se elimina el contenido anterior de la tabla
        if (table.hasChildNodes()){
            while (table.childNodes.length>=1){
                table.removeChild(table.firstChild);
            }
        }   
        var numero=0;
        if (DesignInstance.data.participants.length>0){
            var th = table.appendChild(document.createElement("th"));
            th.innerHTML = i18n.get("participantes.nombre");
            table.className="participantsTable";
            //Se construye la tabla con la nueva información
            var participantes = new Array();
            for (var i=0; i<DesignInstance.data.participants.length;i++)
            {
                participantes.push(DesignInstance.data.participants[i]);
            }
            participantes.sort(natcompare);
            for (var i=0; i<participantes.length; i++){
                var encontrado=false;
                var j=0;
                //Se busca si está en el nuevo listado de participantes
                while (encontrado==false && j<data.participantes.length){
                    if (participantes[i].participantId==data.participantes[j].participantId){
                        encontrado=true;
                    }
                    j++;
                }
                //Si no se ha encontrado el participante se elimina
                if (encontrado==false){
                    this.deletedParticipants.push(participantes[i].participantId);
                    var tr = table.appendChild(document.createElement("tr"));
                    var td = tr.appendChild(document.createElement("td"));
                    td.innerHTML = participantes[i].name;
                    numero++;
                }
            }
        }
        var per=document.getElementById("numAlumnosEliminados");
        per.innerHTML = i18n.get("participantes.alumnosEliminados") + ": "+ numero;
        per.style.fontWeight = 'bold';
    },

    /**
     * Muestra en una tabla el listado de participantes que permanecen
     */
    showParticipantsRemain: function(data){
        var table = document.getElementById("tablaAlumnosPermanecen");
        //Se elimina el contenido anterior de la tabla
        if (table.hasChildNodes()){
            while (table.childNodes.length>=1){
                table.removeChild(table.firstChild);
            }
        }
        var numero=0;
        if (DesignInstance.data.participants.length>0){
            var th = table.appendChild(document.createElement("th"));
            th.innerHTML = i18n.get("participantes.nombre");
            table.className="participantsTable";
            var participantes = new Array();
            for (var i=0; i<DesignInstance.data.participants.length;i++)
            {
                participantes.push(DesignInstance.data.participants[i]);
            }
            participantes.sort(natcompare);
            //Se construye la tabla con la nueva información
            for (var i=0; i<participantes.length; i++){
                var encontrado=false;
                for (var j = 0; j < data.participantes.length; j++) {
                    //Se busca si está en el nuevo listado de participantes
                    if (participantes[i].participantId==data.participantes[j].participantId){
                        encontrado=true;
                        break;
                    }
                }
                //Si se ha encontrado el participante se mantiene en la lista
                if (encontrado==true){
                    var tr = table.appendChild(document.createElement("tr"));
                    var td = tr.appendChild(document.createElement("td"));
                    td.innerHTML = participantes[i].name;
                    numero++;
                }
            }
        }
        var per=document.getElementById("numAlumnosPermanecen");
        per.innerHTML = i18n.get("participantes.alumnosPermanecen")+ ": "+ numero;
        per.style.fontWeight = 'bold';
    },
        
    /**
     * Ocultar la ventana de comparación de alumnos
     */
    hideParticipantComparation: function(){
        dijit.byId("comparacionAlumnos").hide();
    },
       
    /**
     * Guarda los cambios realizados en los participantes de la instancia
     */
    acceptParticipantChanges: function(){
        //Se borran las apariciones en las instancias de los participantes que desaparecen
        for (var p=0;p<this.deletedParticipants.length; p++){
            for (var g=0; g<DesignInstance.data.groups.length;g++){
                for (var i=0; i<DesignInstance.data.groups[g].instances.length;i++){
                    var encontrado = false;
                    var pos=0;
                    while (pos<DesignInstance.data.groups[g].instances[i].participants.length && encontrado==false){
                        if (this.deletedParticipants[p]==DesignInstance.data.groups[g].instances[i].participants[pos]){
                            DesignInstance.data.groups[g].instances[i].participants.splice(pos,1);
                            encontrado=true;
                        }
                        pos++;
                    }
                }
            }
        }
        
        if (dijit.byId("comparacionAlumnosIdLmsGlue").get("value")==0){
            ParticipantManagement.getParticipantsSave(dijit.byId("comparacionAlumnosIdInst").get("value"),
                dijit.byId("comparacionAlumnosIdClase").get("value"));
        }
        else
        {
            ParticipantManagement.getParticipantsSaveGlue(dijit.byId("comparacionAlumnosIdInst").get("value"),
                dijit.byId("comparacionAlumnosIdLmsGlue").get("value"),dijit.byId("comparacionAlumnosIdClase").get("value"));
        }
    },
    
        
    /**
     * Obtiene los participantes que se guardarán como consecuencia de la instalación y clase seleccionadas
     * @param idlms_installation Instalación de la que se obtienen los participantes
     * @param idclase Clase de la que se obtienen los participantes
     */
    getParticipantsSave: function(idlms_installation, idclase){
        var bindArgs = {
            url: "manager/lms/controladorLms.php",
            handleAs: "json",
            content: {
                task: "obtener_participantes",
                idlms_installation: idlms_installation,
                idclase: idclase
            },
            load: function(data){
                var lmsObj = new LmsObj("", "");
                DesignInstance.setLms(lmsObj);
                
                if (idclase!=DesignInstance.data.classObj.id){
                    var classObj = new ClassObj(idclase, dijit.byId("select_clases").get("displayedValue"));
                    DesignInstance.setClass(classObj);
                }
            
                DesignInstance.deleteParticipants();
                var p;
                for (var j in data.participantes){
                    p = new Participant(data.participantes[j].participantId, data.participantes[j].name, data.participantes[j].type);
                    DesignInstance.addParticipant(p);
                }
                
                ParticipantManagement.addInstallationInfo(idlms_installation);
                dijit.byId("comparacionAlumnos").hide();
                ParticipantSelection.hideParticipantSelectionDialog();
                
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
     * Obtiene la configuración de la Edición de una Instalación
     */
    addInstallationInfo: function(idlms_installation){
        var bindArgs = {
            url: "manager/manageLms.php",
            handleAs: "json",
            content: {
                task: "obtener_installation",
                idlms_installation: idlms_installation
            },
            load: function(data){
                if (data.ok){
                    var instObj = new InstObj(data.installation.idlms_installation, data.installation.title);
                    DesignInstance.setInst(instObj);
                    ParticipantManagement.addLmsInfo(data.installation.idlms);
                }
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
     * Obtiene la configuración de la Edición de una Instalación
     */
    addLmsInfo: function(idlms){
        var bindArgs = {
            url: "manager/manageLms.php",
            handleAs: "json",
            content: {
                task: "obtener_un_lms",
                idlms: idlms
            },
            load: function(data){
                if (data.ok){
                    DesignInstance.setLmsType(data.lms);
                    EditToolVLEDialog.availableVLE();
                }
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
     * Obtiene los participantes que se guardarán como consecuencia de la instalación de Glue y clase seleccionadas
     * @param idlms_installation Identificador de la instalación seleccionada
     * @param idlmsSystem Identificador del LSM seleccionado
     * @param idlmsCourse Identificador de la clase seleccionada
     */
    getParticipantsSaveGlue: function(idlms_installation, idlmsSystem, idlmsCourse){
        var bindArgs = {
            url: "manager/lms/controladorLms.php",
            handleAs: "json",
            content: {
                task: "obtener_participantes_lms",
                idlms_installation: idlms_installation,
                idlmsSystem: idlmsSystem,
                idlmsCourse: idlmsCourse
            },
            load: function(data){

                if (idlmsSystem!=DesignInstance.data.lmsObj.id){
                    var lmsObj = new LmsObj(idlmsSystem, dijit.byId("selectLMS_installation_glue").get("displayedValue"));
                    DesignInstance.setLms(lmsObj);
                }
                if (idlmsCourse!=DesignInstance.data.classObj.id){
                    var classObj = new ClassObj(idlmsCourse, dijit.byId("select_clases").get("displayedValue"));
                    DesignInstance.setClass(classObj);
                }
                DesignInstance.deleteParticipants();
                var p;
                for (var j in data.participantes){
                    p = new Participant(data.participantes[j].participantId, data.participantes[j].name, data.participantes[j].type);
                    DesignInstance.addParticipant(p);
                }
                
                ParticipantManagement.addInstallationInfo(idlms_installation);
                dijit.byId("comparacionAlumnos").hide();
                ParticipantSelection.hideParticipantSelectionDialog();
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    
    /**
     * Muestra un diálogo de alerta
     * @param txtTitle Título del diálogo de alerta
     * @param txtContent Contenido del diálogo de alerta
     */
    showDialogAlert: function(txtTitle, txtContent){
        var dlg = dijit.byId("dialogAlert");
        dlg.titleNode.innerHTML = txtTitle;
        dojo.byId("dialogAlertInfo").innerHTML = txtContent;
        dlg.show();
    },
    
    /**
     * Oculta el diálogo de alerta
     */
    hideDialogAlert: function(){
        var dlg = dijit.byId("dialogAlert");
        dlg.hide();
    },
        
    /**
     * Actualiza la pestaña de gestión de participantes
     */    
    updateDisplay: function(){
        
        if (DesignInstance.data!= null && DesignInstance.data.instObj.id!=""){
            dojo.style("tablaInfo","display","");
            dojo.byId("instancia_elegida").innerHTML = "<b>" + DesignInstance.data.instObj.name + "</b>";
            dojo.byId("clase_elegida").innerHTML = "<b>" + DesignInstance.data.classObj.name + "</b>";
            ParticipantManagement.showDeployButton(DesignInstance.data.instObj.id);
        }else{
            dojo.style("tablaInfo","display","none");
            dojo.byId("instancia_elegida").innerHTML =  "";
            dojo.byId("clase_elegida").innerHTML = "";
            dojo.style("createGluePSDesign","display","none");
        }
        ParticipantManagement.checkInstallation();
        ParticipantManagement.showSelectedLmsType();
        ParticipantManagement.showChosenParticipants();
    },
             
    /**
     * Comprueba si existe una Instalación LMS ya cargada en la Instanciación
     */
    checkInstallation: function(){
        if ((DesignInstance.data!=null && DesignInstance.data.instObj.name!=0)){
            dijit.byId("elegirInstancia").set("label", i18n.get("participantes.elegirinstancia"));
        }
    },
    
    /**
     * Muestra, si es necesario, el botón de despliegue del diseño
     */
    /*showDeployButton: function(){
        if (DesignInstance.data.lmsType.idlms && DesignInstance.data.lmsType.idlms=="4"){
            dojo.style("createGluePSDesign","display","");
        }
        else{
            dojo.style("createGluePSDesign","display","none");
        }
    },*/
    
    showDeployButton: function(idlmsInstallation){
        var bindArgs = {
            url: "manager/manageLms.php",
            handleAs: "json",
            content: {
                task: "obtener_installation_lms",
                idlms_installation: idlmsInstallation
            },
            load: function(data){
                //El tipo de lms 4 se corresponde GluePS
                if (data.ok && data.installation.idlms=="4"){
                    dojo.style("createGluePSDesign","display","");
                }
                else{
                    dojo.style("createGluePSDesign","display","none");
                }
            }
        };
        dojo.xhrPost(bindArgs);
    },    
    
    /**
     *Actualiza el tipo de lms seleccionado
     */
    showSelectedLmsType: function(){
        if (DesignInstance.data!= null){
            var bindArgs = {
                url: "manager/manageLms.php",
                handleAs: "json",
                content: {
                    task: "obtener_installation_lms",
                    idlms_installation: DesignInstance.data.instObj.id
                },
                load: function(data){
                    dojo.byId("lms_elegido").innerHTML = "";
                    if (data.ok)
                    {
                        dojo.byId("lms_elegido").innerHTML = dojo.byId("lms_elegido").innerHTML + "<b>" + data.installation.name + "</b>";
                    }
                    if (DesignInstance.data.lmsObj.name!=""){
                        dojo.byId("lms_elegido").innerHTML = dojo.byId("lms_elegido").innerHTML + " / <b>" + DesignInstance.data.lmsObj.name + "</b>";
                    }
                }
            };
            dojo.xhrPost(bindArgs);
        }
    },
    
    /**
     * Muestra en una tabla el listado de participantes elegido con los que trabaja la instancia
     */
    showChosenParticipants: function(){
        //Ajustar la altura máxima a la mitad de la resolución de pantalla
        dojo.byId("prueba").style.maxHeight = screen.height/2 + "px";
        
        var table = document.getElementById("participantesElegidos");
        table.className="participantsTable";
        //Se elimina el contenido anterior de la tabla
        if (table.hasChildNodes()){
            while (table.childNodes.length>=1){
                table.removeChild(table.firstChild);
            }
        }
        //Se reconstruye la tabla con el nuevo listado de participantes
        if (DesignInstance.data && DesignInstance.data.participants && DesignInstance.data.instObj.id!=""){
            var th = table.appendChild(document.createElement("th"));
            th.innerHTML = i18n.get("participantes.nombre");
            th = table.appendChild(document.createElement("th"));
            th.innerHTML = i18n.get("participantes.tipo");
        
            var participantes = new Array();
            for (var i=0; i<DesignInstance.data.participants.length;i++)
            {
                participantes.push(DesignInstance.data.participants[i]);
            }
            participantes.sort(natcompare);
        
            for (var i=0; i<participantes.length; i++) {
                var tr = table.appendChild(document.createElement("tr"));
                var td = tr.appendChild(document.createElement("td"));
                td.innerHTML = participantes[i].name;
                td = tr.appendChild(document.createElement("td"));
                td.className="clickable";
                if (participantes[i].participantType!="teacher"){
                    td.innerHTML = participantes[i].participantType;
                }
                else{
                    td.innerHTML = "<b>"+participantes[i].participantType + "</b>";
                   
                }
                //Menú para cambiar el tipo
                MenuManager.registerThing(td, {
                    getItems : function(data) {
                        return ParticipantManagement.getParticipantTypeMenu(data);
                    },
                    data : {
                        participant : participantes[i]
                    }
                });
            }
            
            //Mostrar el número de participantes elegidos
            dojo.style("numeroParticipantesElegidos","display","");
            var pe=document.getElementById("numeroParticipantesElegidos");
            pe.innerHTML = i18n.get("participantes.numberParticipants")+ participantes.length;
            pe.style.fontWeight = 'bold';
        }
        
    },

    /**
     * Función que se encarga de comprobar cambios en los participantes
     */
    checkChanges: function(){
        if (DesignInstance.data && DesignInstance.data.instObj.id!="" && DesignInstance.data.classObj.id!=""){
            if (DesignInstance.data.lmsObj.id==""){
                ParticipantManagement.getParticipantChanges(DesignInstance.data.instObj.id, DesignInstance.data.classObj.id);
            }
            else{
                ParticipantManagement.getParticipantChangesGlue(DesignInstance.data.instObj.id, DesignInstance.data.lmsObj.id, DesignInstance.data.classObj.id);
            }
        }
    },
    
    
    /**
     * Obtiene los cambios que se producen en los participantes para una instalación y clase dadas
     * @param idlms_installation Identificador de la instalación de la que se obtienen los participantes
     * @param idclase Identificador de la clase de la que se obtienen los participantes
     */
    getParticipantChanges: function(idlms_installation, idclase){
        var bindArgs = {
            url: "manager/lms/controladorLms.php",
            handleAs: "json",
            content: {
                task: "obtener_participantes",
                idlms_installation: idlms_installation,
                idclase: idclase
            },
            load: function(data){
                if (ParticipantManagement.thereIsChanges(data)){
                    ParticipantManagement.showDialogAlert(i18n.get("actualizar.aviso"),i18n.get("participantes.obtenerparticipantes.cambios"));
                    ParticipantManagement.getChosenParticipants(DesignInstance.data.instObj.id, DesignInstance.data.classObj.id);
                }
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
     * Obtiene los cambios que se producen en los participantes para una instalación y clase de glue dadas
     * @param idlms_installation Identificador de la instalación de la que se obtienen los participantes
     * @param idlmsSystem Identificador del lms de glue del que se obtienen los participantes
     * @param idlmsCourse Identificador de la clase de la que se obtienen los participantes
     */
    getParticipantChangesGlue: function(idlms_installation, idlmsSystem, idlmsCourse){
        var bindArgs = {
            url: "manager/lms/controladorLms.php",
            handleAs: "json",
            content: {
                task: "obtener_participantes_lms",
                idlms_installation: idlms_installation,
                idlmsSystem: idlmsSystem,
                idlmsCourse: idlmsCourse
            },
            load: function(data){
                if (ParticipantManagement.thereIsChanges(data)){
                    ParticipantManagement.showDialogAlert(i18n.get("actualizar.aviso"),i18n.get("participantes.obtenerparticipantes.cambios"));
                    ParticipantManagement.getChosenParticipantsGlue(idlms_installation, idlmsSystem, idlmsCourse);
                }
            }
        };
        dojo.xhrPost(bindArgs);
    },
           
    /**
     * Función que indica si hay cambios en los participantes
     */
    thereIsChanges: function(data){
        //Se construye la tabla con la nueva información
        var permanecen=0;
        for (var i=0; i<DesignInstance.data.participants.length; i++ ){
            var encontrado=false;
            var j=0;
            //Se busca si está en el nuevo listado de participantes
            while (encontrado ==false && j<data.participantes.length){
                if (DesignInstance.data.participants[i].participantId==data.participantes[j].participantId){
                    encontrado=true;
                    permanecen++;
                }
                j++;
            }
        }
        return (DesignInstance.data.participants.length!=data.participantes.length || permanecen!=DesignInstance.data.participants.length);
    },
    
    
    getParticipantTypeMenu : function(data) {
        var items = new Array();
        if (data.participant.participantType=="student")
        {
            items.push({
                icon: "teacher",
                label : i18n.get("changeTypeToTeacher"),
                help : i18n.get("changeTypeToTeacher.help"),
                onClick : function(data) {
                    if (!DesignInstance.asignedAnyInstance(data.participant.participantId))
                    {
                        DesignInstance.changeType(data.participant.participantId);
                        DesignInstance.deleteFromAllInstances(data.participant.participantId);
                        Loader.save("");
                    }
                    else{
                        ParticipantManagement.showChangeTypeConfirm(data.participant.participantId);
                    }
                },
                data: data
            });
        }
        else{
            items.push({
                icon: "student",
                label : i18n.get("changeTypeToStudent"),
                help : i18n.get("changeTypeToStudent.help"),
                onClick : function(data) {
                    if (!DesignInstance.asignedAnyInstance(data.participant.participantId))
                    {
                        DesignInstance.changeType(data.participant.participantId);
                        DesignInstance.deleteFromAllInstances(data.participant.participantId);
                        Loader.save("");
                    }
                    else{
                        ParticipantManagement.showChangeTypeConfirm(data.participant.participantId);
                    }
                },
                data: data
            });
        }
        return items;
    },
    
    changeTypeConfirm: function(){
        var participantId = dijit.byId("participantId").getValue();
        DesignInstance.changeType(participantId);
        DesignInstance.deleteFromAllInstances(participantId);
        ParticipantManagement.hideChangeTypeConfirm();
        Loader.save("");
    },
    
    showChangeTypeConfirm: function(participantId){
        dijit.byId("changeTypeDialog").show();
        dijit.byId("participantId").setValue(participantId);
    },
    
    hideChangeTypeConfirm: function(){
        dijit.byId("changeTypeDialog").hide();
    }

};