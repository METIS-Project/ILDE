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
 * Gestión de las instalaciones de LMS
 */
var LmsManagement = {

    /**
     * Función de inicialización
     */
    init: function() {
        dojo.connect(dijit.byId("gestionLMS"), "onClick", function(){
            LmsManagement.openLmsInstallationManagementDialog();
            LmsManagement.displayLmsInstallations();
        });
        
        dojo.connect(dijit.byId("aceptaradmingestion"), "onClick", function(){
            LmsManagement.closeLmsInstallationManagementDialog();
        });
        
        dojo.connect(dijit.byId("rechazaradmingestion"), "onClick", function(){
            LmsManagement.closeLmsInstallationManagementDialog();
        });
        
        
        //Click en el botón de creación una instalación de LMS        
        dojo.connect(dijit.byId("crearLMS"), "onClick", function(){
            LmsManagement.resetNewInstallation();
            LmsManagement.openNewLmsInstallationDialog();
            LmsManagement.getLmsTypesNew();
        });
        
        //Click en el botón aceptar de la ventana de creación de una instalación de LMS
        dojo.connect(dijit.byId("aceptarCrearLMS"), "onClick", function(){
            LmsManagement.checkNewInstallation();
        });
        
        //Click en el botón cancelar de la ventana de creación de una instalación de LMS
        dojo.connect(dijit.byId("cancelarCrearLMS"), "onClick", function(){
            LmsManagement.closeNewLmsInstallationDialog();
        });
        
        dojo.connect(dijit.byId("selectLMS"), "onChange", function(){
            LmsManagement.resetNewInstallation();
            LmsManagement.managementNewInstallationDialog();
        });
        
        dojo.connect(dijit.byId("dialogoEditarSelectLMS"), "onChange", function(){
            LmsManagement.managementEditInstallationDialog();
        });
    },
           
    /**
    * Apertura de la ventana de gestión de instalaciones de LMS
    */
    openLmsInstallationManagementDialog: function(){
        dijit.byId("adminGestionLMS").show();
        //dojo.style(dijit.byId("adminGestionLMS"), "opacity", 100);
    },
    
    /**
    * Ocultar la ventana de gestión de LMS
    */
    closeLmsInstallationManagementDialog: function(){
        dijit.byId("adminGestionLMS").hide();
    },
    
    /**
    * Actualización de la lista de Instalaciones LMS
    */
    displayLmsInstallations: function(){
        var bindArgs = {
            url: "manager/manageLms.php",
            handleAs: "json",
            content: {
                task: "obtener_installations_user"
            },
            load: function(data){
                LmsManagement.showLmsInstallations(data);
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
    * Mostrar la lista de instalaciones de LMS en la tabla
    */
    showLmsInstallations: function(data){
        var tabla = document.getElementById("tablalms");
        var nodoTabla = tabla.getElementsByTagName("table");
        //Se eliminan los elementos de la tabla si existen
        if (nodoTabla.length!=0){
            tabla.removeChild(nodoTabla[0]);
        }
        var table1 = document.createElement("table");
        table1.className="participantsTable";
        var th = table1.appendChild(document.createElement("th"));
        th.innerHTML = i18n.get("participantes.nombre");
        th = table1.appendChild(document.createElement("th"));
        th.innerHTML = i18n.get("participantes.user");
        //Rellenamos la tabla con las Instalaciones que existen
        for (var i in data.installations){
            var tr = table1.appendChild(document.createElement("tr"));
            var td = tr.appendChild(document.createElement("td"));
            var td2 = tr.appendChild(document.createElement("td"));
            var instanc = data.installations[i];
            td.setAttribute("width", "150");
            td.innerHTML = instanc.title;
            td2.innerHTML = instanc.user;
            MenuManager.registerThing(td, {
                getItems: function(data) {
                    return LmsManagement.getLmsInstallationOptions(data);
                },
                data: {
                    res: data.installations[i]
                }
            });
        }
        tabla.appendChild(table1);
    },
            
    /**
    * Menú Desplegable para editar y eliminar instalaciones de LMS
    */
    getLmsInstallationOptions: function(data){
        var options = new Array();
        options.push({
            label: i18n.get("participantes.optioneditlms"),
            icon: "editlo",
            onClick: function(data){
                //No se permite editar la instalación seleccionada
                if (DesignInstance.data.instObj.id==data.res.idlms_installation){
                    ParticipantManagement.showDialogAlert(i18n.get("participantes.aviso"),i18n.get("participantes.imposibleactualizar"));
                }else{
                    LmsManagement.openEditLmsInstallationDialog(data.res.idlms_installation);
                    dijit.byId("dialogoEditarLMS").set("title", i18n.get("participantes.editarInstalacionLMS") + ": " + '"' + data.res.title + '"');
                }
            },
            data: data,
            help: i18n.get("participantes.lmsintallation.edit")
        });
        options.push({
            isSeparator: true
        });
        options.push({
            label: i18n.get("participantes.optiondeletelms"),
            icon: "delete",
            onClick: function(data){
                LmsManagement.deleteInstallation(data.res.idlms_installation);
                dijit.byId("ConfirmacionEliminar").set("title", i18n.get("participantes.optiondeletelms") + ": " + '"' + data.res.title + '"' + " | " +  '"' + data.res.user + '"');
            },
            data: data,
            help: i18n.get("participantes.lmsintallation.delete")
        });
        return options;
    },
    
       
    /**
     * EDICIÓN DE INSTALACIONES
     * 
     */
    
    /**
    * Apertura Dialogo Edición LMS
    */
    openEditLmsInstallationDialog: function(data){
        dojo.byId("dialogoEditarLMSErrorReport").innerHTML = "";
        dijit.byId("dialogoEditarLMS").show();
        //dojo.style(dijit.byId("dialogoEditarLMS"),"opacity",100);
        LmsManagement.getLmsTypes();
        LmsManagement.getInstallationInfo(data);
    },
            
    /**
    * Cierre Dialogo Edición LMS
    */
    closeEditLmsInstallationDialog: function(){
        dijit.byId("dialogoEditarLMS").hide();
    },
    
    /**
    * Actualiza el listado de tipos de LMS disponibles en la edición de una instalación de LMS
    */
    getLmsTypes: function(){
        var bindArgs = {
            url: "manager/manageLms.php",
            handleAs: "json",
            content: {
                task: "obtener_lms"
            },
            load: function(data){
                LmsManagement.updateSelectLmsEditDialog(data);
            }
        };
        dojo.xhrPost(bindArgs);
    },
         
    /**
    * Actualiza el campo seleccionar LMS en el diálogo editar instalación
    * @param data Información sobre los lms
    */
    updateSelectLmsEditDialog: function(data){
        var lms_select = dijit.byId("dialogoEditarSelectLMS");
        while (lms_select.getOptions(0)!=null){
            lms_select.removeOption(lms_select.getOptions(0));
        }
        lms_select.addOption({
            label: i18n.get("participantes.tipoLMS.select"),
            value: "0"
        });
        for (var i in data.lms) {
            var lms = data.lms[i];
            lms_select.addOption({
                label: lms.name,
                value: lms.idlms
            });
        }
    },
            
    /**
    * Obtiene la configuración de la Edición de una Instalación
    */
    getInstallationInfo: function(idlms_installation){
        var bindArgs = {
            url: "manager/manageLms.php",
            handleAs: "json",
            content: {
                task: "obtener_installation",
                idlms_installation: idlms_installation
            },
            load: function(data){
                LmsManagement.showInstallationInfo(data);
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    
    /**
    * Muestra los Datos obtenidos en la Edición de una Instalación
    * @param data Información sobre la instalación
    */
    showInstallationInfo: function(data){
        if (data.ok==true){
            dijit.byId("dialogoEditarLMSidlms_inst").setValue(data.installation.idlms_installation);
            dijit.byId('dialogoEditarSelectLMS').attr("value", data.installation.idlms);
            dijit.byId("dialogoEditarLMStitulo").setValue(data.installation.title);
            dijit.byId("dialogoEditarLMSuri").setValue(data.installation.instance_identifier);
            dijit.byId("dialogoEditarLMSusuario").setValue(data.installation.user);
            dijit.byId("dialogoEditarLMSpass").setValue(data.installation.pass);
            if (dijit.byId("dialogoEditarSelectLMS").get("value") == "3")
            {
                dijit.byId("dialogoEditarLMStoolId").set("value", data.installation.others.toolid); 
            }
        }else{
            dijit.byId("dialogoEditarLMSidlms_inst").set("value", "");
            dijit.byId('dialogoEditarSelectLMS').attr("value", 0);
            dijit.byId("dialogoEditarLMStitulo").setValue("");
            dijit.byId("dialogoEditarLMStitulo").setValue("");
            dijit.byId("dialogoEditarLMSuri").setValue("");
            dijit.byId("dialogoEditarLMSusuario").setValue("");
            dijit.byId("dialogoEditarLMSpass").setValue("");
            ParticipantManagement.showDialogAlert(i18n.get("actualizar.aviso"),i18n.get("participantes.editarinstalacion.error"));
        }
    },

    /**
    * Comprobar la Edición de las Instalaciones
    */
    checkEditInstallation: function(){
        var idlms_installation = dijit.byId('dialogoEditarLMSidlms_inst').attr('value');
        var idlms = dijit.byId('dialogoEditarSelectLMS').attr('value');
        var title = dijit.byId("dialogoEditarLMStitulo");
        var uri = dijit.byId("dialogoEditarLMSuri");
        var user = dijit.byId("dialogoEditarLMSusuario");
        var pass = dijit.byId("dialogoEditarLMSpass");
        var toolid = dijit.byId("dialogoEditarLMStoolId");
        var others = {};
        var error;
        
        switch(dijit.byId("dialogoEditarSelectLMS").get("value")){
            case "1":
            {
                if (idlms == 0){
                    error = i18n.get("participantes.crearinstalacion.idlms");
                }
                else{
                    if (title.value.length == 0){
                        error = i18n.get("participantes.crearinstalacion.title");
                    }
                    else{
                        if (dojo.byId("archivoEditar").value.length == 0)
                        {
                            error = i18n.get("participantes.crearinstalacion.file");
                        }
                    }
                }
                break;
            }
            case "3":
            {
                if (idlms == 0){
                    error = i18n.get("participantes.crearinstalacion.idlms");
                }
                else{
                    if (title.value.length == 0){
                        error = i18n.get("participantes.crearinstalacion.title");
                    }
                    else{
                        if (uri.value.length == 0){
                            error = i18n.get("participantes.crearinstalacion.uri");
                        }
                        else{
                            if (user.value.length ==0){
                                error = i18n.get("participantes.crearinstalacion.user");
                            }
                            else{
                                if (pass.value.length == 0){
                                    error = i18n.get("participantes.crearinstalacion.passwd");
                                }
                                else{
                                    if (toolid.value.length == 0){
                                        error = i18n.get("participantes.crearinstalacion.toolid");
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            }
            default:
            {
                if (idlms == 0){
                    error = i18n.get("participantes.crearinstalacion.idlms");
                }
                else{
                    if (title.value.length == 0){
                        error = i18n.get("participantes.crearinstalacion.title");
                    }
                    else{
                        if (uri.value.length == 0){
                            error = i18n.get("participantes.crearinstalacion.uri");
                        }
                        else{
                            if (user.value.length ==0){
                                error = i18n.get("participantes.crearinstalacion.user");
                            }
                            else{
                                if (pass.value.length == 0){
                                    error = i18n.get("participantes.crearinstalacion.passwd");
                                }
                            }
                        }
                    }
                }
            }
        }

        if (error){
            dojo.byId("dialogoEditarLMSErrorReport").innerHTML = error;
        }
        else {
            dojo.byId("dialogoEditarLMSErrorReport").innerHTML = "";
            if (dijit.byId("dialogoEditarSelectLMS").get("value")=="1"){
                LmsManagement.changeFile(idlms_installation); 
            }
            else{
                if (dijit.byId("dialogoEditarSelectLMS").get("value")=="3"){
                    others.toolid=dijit.byId("dialogoEditarLMStoolId").get('value');
                }
                //Si se han introducido los campos se actualiza la instalación 
                this.callUpdateInstallation(idlms_installation, idlms, uri.value, title.value, user.value, pass.value, others);
            }
        }
    },
    
    /**
    * Comprobar la Actualización de las Instalaciones
    * @param idlms Identificador del lms 
    * @param instId Identificador de la instalación
    * @param title Nombre de la instalación
    * @param user Nombre del usuario
    * @param pass Contraseña del usuario
    * @param others Otros parámetros adicionales
    */
    callUpdateInstallation: function (idlms_installation, idlms, instId, title, user, pass, others){
        var oth = JSON.encode(others);
        var bindArgs = {
            url: "manager/manageLms.php",
            handleAs: "json",
            content: {
                task: "actualiza_lms_installation",
                idlms_installation: idlms_installation,
                idlms: idlms,
                inst_id: instId,
                title: title,
                user: user,
                pass: pass,
                others: oth
            },
            load: function(data){
                LmsManagement.closeEditLmsInstallationDialog();
                LmsManagement.displayLmsInstallations();
                ParticipantSelection.callUpdateSelectLmsInstallation();
            },
            error: function(){
            
            }
        };
        dojo.xhrPost(bindArgs);
    },
            
    /**
    * Gestiona los campos que se muestran en la ventana editar instalación dependiendo del tipo de LMS
    */
    managementEditInstallationDialog: function(){
        switch(dijit.byId("dialogoEditarSelectLMS").get("value")){
            case "1": {
                dojo.style("instance_idEditar","display","none");
                dojo.style("usernameEditar","display","none");
                dojo.style("passwordEditar","display","none");
                dojo.style("toolidEditar","display","none");
                //Mostrar campo para subir fichero
                dojo.style("uploadFileEditar","display","");                
                break;
            }
            case "3": {
                dojo.style("instance_idEditar","display","");
                dojo.style("usernameEditar","display","");
                dojo.style("passwordEditar","display","");
                dojo.style("toolidEditar","display","");
                dojo.style("uploadFileEditar","display","none");
                break;
            }
            default: {
                dojo.style("instance_idEditar","display","");
                dojo.style("usernameEditar","display","");
                dojo.style("passwordEditar","display","");
                dojo.style("toolidEditar","display","none");
                dojo.style("uploadFileEditar","display","none");
            }
        }
    },
           
    /** 
    * Resetea los campos de la Ventana 'Editar Instalación de LMS'
    */
    resetEditInstallation: function(){
        dijit.byId("dialogoEditarLMStitulo").setValue("");
        dijit.byId("dialogoEditarLMSuri").setValue("");
        dijit.byId("dialogoEditarLMSusuario").setValue("");
        dijit.byId("dialogoEditarLMSpass").setValue("");
        dojo.byId("dialogoEditarLMSErrorReport").innerHTML = "";
        //Resetear valor de toolid
        dijit.byId("dialogoEditarLMStoolId").set('value', "");
        //Resetear valor fichero seleccionado
        dojo.byId("uploadFileEditar").innerHTML='<td>'+i18n.get("participantes.fichero")+'</td><td> <input id="archivoEditar" name="archivoEditar" size="28"  type="file" onchange="LmsManagement.getNameEditLmsFile(this.value);"/></td><td><input type="hidden" name="MAX_FILE_SIZE" value="100000" /></td>';
    },
        
    /** 
    * Sustituye un fichero del servidor por otro
    */
    changeFile: function(idlms_installation)
    {
        dojo.io.iframe.send({          
            form:"formUploadFileEditar",
            url: "manager/ficheros.php",
            handleAs:"json",
            content: {
                task: "changeFile",
                idlms_installation: idlms_installation
            },
            handle:function(ioResponse,args){       
                if(ioResponse instanceof Error){
                    console.log("Error: "+ioResponse);
                }
                else{
                    //Si se ha sustituido con éxito el fichero se actualiza la instalación
                    if (ioResponse.ok==true && ioResponse.updated==true)
                    {   
                        LmsManagement.callUpdateInstallation(idlms_installation, dijit.byId('dialogoEditarSelectLMS').get('value'), ioResponse.path, dijit.byId("dialogoEditarLMStitulo").value, "", "", {});
                    }
                    else{
                        // En caso contrario se muestra información del error
                        if (ioResponse.notDeleted==true)
                        {
                            dojo.byId("dialogoEditarLMSErrorReport").innerHTML = "Error al borrar el fichero anterior";
                        }
                        else{
                            dojo.byId("dialogoEditarLMSErrorReport").innerHTML = "Error al cambiar el fichero"; 
                        }
                    }
                }
            }  
        });
    },
           
    /** 
    * Obtiene el nombre del fichero dada la ruta completa y le pone como valor del campo en la edición de instalación
    * @param fichero Ruta del fichero desde la que se obtiene el nombre
    */
    getNameEditLmsFile: function(fichero){
        fichero = fichero.split('\\');
        dijit.byId("dialogoEditarLMStitulo").setValue(fichero[fichero.length-1]);
        dojo.byId("dialogoEditarLMStitulo").select();
    },
    
    
    /**
     * ELIMINACIÓN DE INSTALACIONES
     */
            
    /**
    * Abrir Ventana Confirmación de Eliminación
    */
    openDeleteConfirmDialog: function(){
        dijit.byId("ConfirmacionEliminar").show();
    },
    /**
    * Cerrar Ventana Confirmación de Eliminación
    */
    closeDeleteConfirmDialog: function(){
        dijit.byId("ConfirmacionEliminar").hide();
    },
  
    /**
    * Se realiza la llamada para Eliminar la Instalación
    * @param idlms_installation Identificador de la instalación a eliminar
    */
    deleteInstallation: function(idlms_installation){
        if (DesignInstance.data.instObj.id==idlms_installation){
            ParticipantManagement.showDialogAlert(i18n.get("participantes.aviso"),i18n.get("participantes.imposibilidadborrar"));
        }else{
            LmsManagement.openDeleteConfirmDialog();
            dojo.connect(dijit.byId("confirmareliminacion"), "onClick", function(){
                LmsManagement.callDeleteInstallation(idlms_installation);
            });
        }
    },
    
    /**
    * Elimina la Instalación Seleccionada
    * @param idlms_installation Identificador de la instalación a eliminar
    */
    callDeleteInstallation: function(idlms_installation){
        var bindArgs = {
            url: "manager/manageLms.php",
            handleAs: "json",
            content: {
                task: "eliminar_installation",
                idlms_installation: idlms_installation
            },
            load: function(data){
                LmsManagement.displayLmsInstallations();
                ParticipantSelection.callUpdateSelectLmsInstallation();
            }
        };
        LmsManagement.closeDeleteConfirmDialog();
        dojo.xhrPost(bindArgs);
    },
    
    
    /**
     * CREACIÓN DE INSTALACIONES
     */
    
    /**
     * Apertura de la ventana de creación de LMS
     */
    openNewLmsInstallationDialog: function(){
        dijit.byId("dialogoCrearLMS").show();
        //dojo.style(dijit.byId("dialogoCrearLMS"), "opacity", 100);
    },
    
    /**
     * Cierra la ventana de creación de una instalación de lms
    */
    closeNewLmsInstallationDialog: function(){
        dijit.byId("dialogoCrearLMS").hide();
    },
    
    /**
    * Actualiza el listado de tipos de LMS disponibles en la creación de una instalación de LMS
    */
    getLmsTypesNew: function(){
        var bindArgs = {
            url: "manager/manageLms.php",
            handleAs: "json",
            content: {
                task: "obtener_lms"
            },
            load: function(data){
                LmsManagement.updateSelectLmsNewDialog(data);
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
    * Actualiza el campo seleccionar LMS en el diálogo crear instalación
    * @param data Información sobre los lms
    */
    updateSelectLmsNewDialog: function(data){
        var lms_select = dijit.byId("selectLMS");
        while (lms_select.getOptions(0)!=null){
            lms_select.removeOption(lms_select.getOptions(0));
        }
        lms_select.addOption({
            label: i18n.get("participantes.tipoLMS.select"),
            value: "0"
        });
        for (var i in data.lms) {
            var lms = data.lms[i];
            lms_select.addOption({
                label: lms.name,
                value: lms.idlms
            });
        }
    },
        
    /**
     * Gestiona los campos que se muestran en la ventana crear instalación dependiendo del tipo de LMS
     */
    managementNewInstallationDialog: function(){
        switch(dijit.byId("selectLMS").get("value")){
            //fichero
            case "1":
            {                    
                dojo.style("instance_id","display","none");
                dojo.style("username","display","none");
                dojo.style("password","display","none");
                dojo.style("toolid","display","none");
                //Mostrar campo para subir fichero
                dojo.style("uploadFile","display","");
                break;
            }
            //PA
            case "3":
            {
                dojo.style("instance_id","display","");
                dojo.style("username","display","");
                dojo.style("password","display","");
                dojo.style("toolid","display","");
                dojo.style("uploadFile","display","none");
                break;
            }
            default:
            {
                dojo.style("instance_id","display","");
                dojo.style("username","display","");
                dojo.style("password","display","");
                dojo.style("toolid","display","none");
                dojo.style("uploadFile","display","none");
            }
        }
    },
        
    /**
 * Función que comprueba si se han completado los campos necesarios para crear una instalación de LMS
 */
    checkNewInstallation: function(){
        var idlms = dijit.byId('selectLMS').attr('value');
        var title = dijit.byId("dialogoCrearLMStitulo");
        var uri = dijit.byId("dialogoCrearLMSuri");
        var user = dijit.byId("dialogoCrearLMSusuario");
        var pass = dijit.byId("dialogoCrearLMSpass");
        var toolid = dijit.byId("dialogoCrearLMStoolId");
        var others = {};
        var error;
        switch(dijit.byId("selectLMS").get("value")){
            //fichero
            case "1":
            {      
                if (idlms == 0){
                    error = i18n.get("participantes.crearinstalacion.idlms");
                }
                else{
                    if (title.value.length == 0){
                        error = i18n.get("participantes.crearinstalacion.title");
                    }
                    else{
                        if (dojo.byId("archivo").value.length == 0)
                        {
                            error = i18n.get("participantes.crearinstalacion.file");
                        }
                    }
                }
                break;
            }
            //glue
            case "3":
            {
                if (idlms == 0){
                    error = i18n.get("participantes.crearinstalacion.idlms");
                }
                else{
                    if (title.value.length == 0){
                        error = i18n.get("participantes.crearinstalacion.title");
                    }
                    else{
                        if (uri.value.length == 0){
                            error = i18n.get("participantes.crearinstalacion.uri");
                        }
                        else{
                            if (user.value.length ==0){
                                error = i18n.get("participantes.crearinstalacion.user");
                            }
                            else{
                                if (pass.value.length == 0){
                                    error = i18n.get("participantes.crearinstalacion.passwd");
                                }
                                else{
                                    if (toolid.value.length == 0){
                                        error = i18n.get("participantes.crearinstalacion.toolid");
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            }
            default:
            {
                if (idlms == 0){
                    error = i18n.get("participantes.crearinstalacion.idlms");
                }
                else{
                    if (title.value.length == 0){
                        error = i18n.get("participantes.crearinstalacion.title");
                    }
                    else{
                        if (uri.value.length == 0){
                            error = i18n.get("participantes.crearinstalacion.uri");
                        }
                        else{
                            if (user.value.length ==0){
                                error = i18n.get("participantes.crearinstalacion.user");
                            }
                            else{
                                if (pass.value.length == 0){
                                    error = i18n.get("participantes.crearinstalacion.passwd");
                                }
                            }
                        }
                    }
                }
            }
        }
        if (error) {
            dojo.byId("dialogoCrearLMSErrorReport").innerHTML = error;
        }else {
            dojo.byId("dialogoCrearLMSErrorReport").innerHTML = "";
            if (dijit.byId("selectLMS").get("value")=="1"){
                LmsManagement.uploadFile();
            }
            else{
                if (dijit.byId("selectLMS").get("value")=="3"){
                    others.toolid=dijit.byId("dialogoCrearLMStoolId").get('value');
                }
                //Si se han introducido los campos se crea la instalación
                this.callCreateInstallation(idlms, uri.value, title.value, user.value, pass.value, others);
            }
        }
    },

    /**
    * Crea una nueva instalación con los parámetros proporcionados
    * @param idlms Identificador del lms 
    * @param instId Identificador de la instalación
    * @param title Nombre de la instalación
    * @param user Nombre del usuario
    * @param pass Contraseña del usuario
    * @param others Otros parámetros adicionales
    */
    callCreateInstallation: function (idlms, instId, title, user, pass, others){
        var oth = JSON.encode(others);
        var bindArgs = {
            url: "manager/manageLms.php",
            handleAs: "json",
            content: {
                task: "crear_lms_installation",
                idlms: idlms,
                inst_id: instId,
                title: title,
                user: user,
                pass: pass,
                others: oth
            },
            load: function(data){
                //Cerrar ventana
                LmsManagement.closeNewLmsInstallationDialog();
                //Resetear campos
                LmsManagement.resetNewInstallation();
                //Mostrar listado actualizado de instalaciones
                LmsManagement.displayLmsInstallations();
                ParticipantSelection.callUpdateSelectLmsInstallation();
            },
            error: function(){

            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /** 
    * Resetea los campos de la Ventana 'Añadir Instalación de LMS'
    */
    resetNewInstallation: function(){
        dijit.byId("dialogoCrearLMStitulo").setValue("");
        dijit.byId("dialogoCrearLMSuri").setValue("");
        dijit.byId("dialogoCrearLMSusuario").setValue("");
        dijit.byId("dialogoCrearLMSpass").setValue("");
        dojo.byId("dialogoCrearLMSErrorReport").innerHTML = "";
        //Resetear valor de toolid
        dijit.byId("dialogoCrearLMStoolId").set('value', "");
        //Resetear valor fichero seleccionado
        dojo.byId("uploadFile").innerHTML='<td>'+i18n.get("participantes.fichero")+'</td><td> <input id="archivo" name="archivo" size="28"  type="file" onchange="LmsManagement.getNameNewLmsFile(this.value);"/></td><td><input type="hidden" name="MAX_FILE_SIZE" value="100000" /></td>';
    },
    
    /** 
    * Sube un fichero al servidor
    */
    uploadFile: function()
    {
        dojo.io.iframe.send({          
            form:"formUploadFile",
            url: "manager/ficheros.php",
            handleAs:"json",
            content: {
                task: "uploadFile"
            },
            handle:function(ioResponse,args){       
                if(ioResponse instanceof Error){
                    console.log("Error: "+ioResponse);
                }
                else{
                    //Si se ha subido con éxito el fichero se crea la instalación
                    if (ioResponse.ok==true && ioResponse.uploaded==true)
                    {
                        LmsManagement.callCreateInstallation(dijit.byId('selectLMS').get('value'), ioResponse.path, dijit.byId("dialogoCrearLMStitulo").value, "", "", {});
                    }
                    else{
                        // En caso contrario se muestra información del error
                        if (ioResponse.notSupportedFormat==true)
                        {
                            dojo.byId("dialogoCrearLMSErrorReport").innerHTML = i18n.get("fichero.notSupportedFormat");
                        }
                        else{
                            dojo.byId("dialogoCrearLMSErrorReport").innerHTML = i18n.get("fichero.error");
                        }
                    }
                }
            }  
        });
    },

    /** 
    * Obtiene el nombre del fichero dada la ruta completa y le pone como valor del campo en la creación de instalación
    * @param fichero Ruta del fichero desde la que se obtiene el nombre
    */
    getNameNewLmsFile: function(fichero){
        fichero = fichero.split('\\');
        dijit.byId("dialogoCrearLMStitulo").setValue(fichero[fichero.length-1]);
        dojo.byId("dialogoCrearLMStitulo").select();
    }
}


