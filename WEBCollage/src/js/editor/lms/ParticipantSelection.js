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
 * Gestión de los participantes en la instancia de diseño
 */
var ParticipantSelection = {
    
    /**
     * Función de inicialización
     */
    init: function() {
        dojo.style("lmsGlue","display","none");
        dojo.style("lmsGLueErrorReport","display","none");
                       
        //Para enlazar botón de elegir con su ventana y al evento onclick
        dojo.connect(dojo.byId("elegirInstancia"), "onclick", function(){
            ParticipantSelection.callUpdateSelectLmsInstallation();
            dojo.style("lmsGlue","display","none");
            ParticipantSelection.resetParticipants();
            ParticipantSelection.showParticipantSelectionDialog();
        });
        
        dojo.connect(dijit.byId("selectLMS_installation"), "onChange", function(){
            ParticipantSelection.resetSelectClass();
            ParticipantSelection.resetSelectLmsGlue();
            ParticipantSelection.checkGetClasses();
            ParticipantSelection.getLmsGlue();
        });
        //Obtener las clases de un lms de glue
        dojo.connect(dijit.byId("selectLMS_installation_glue"), "onChange", function(){
            ParticipantSelection.resetSelectClassGlue();
            ParticipantSelection.checkGetClassesGlue();
        });
        dojo.connect(dijit.byId("select_clases"), "onChange", function(){
            dojo.byId("getParticipantsErrorReport").innerHTML = "";
            dojo.byId("numberOfClassParticipants").innerHTML = "";
            ParticipantSelection.resetParticipantTable();
            
            //ParticipantSelection.updateParticipantButton();
            ParticipantSelection.checkGetParticipants();
            
        });
        
        dojo.byId("lmsGLueErrorReport").innerHTML = "";
    },
    
        
    /**
     * Llamada para obtener el listado de instalaciones de LMS y mostrarlas en el select
     */
    callUpdateSelectLmsInstallation: function(){
        var bindArgs = {
            url: "manager/manageLms.php",
            handleAs: "json",
            content: {
                task: "obtener_installations"
            },
            load: function(data){
                var selectedValue = dijit.byId("selectLMS_installation").get("value");
                ParticipantSelection.updateSelectLmsInstallation(data, selectedValue);
            }
        };
        LoaderIcon.show("loadingSelectLMSInstallation");
        dojo.xhrPost(bindArgs);
    },
    
    /**
    * Actualizar la selección de las Instalaciones LMS
    * @param data Información sobre las instalaciones de lms
    */
    updateSelectLmsInstallation: function(data, selectedValue){
        var lms_select = dijit.byId("selectLMS_installation");
        while (lms_select.getOptions(0)!=null){
            lms_select.removeOption(lms_select.getOptions(0));
        }
        lms_select.addOption({
            label: i18n.get("participantes.lms.select"),
            value: "0"
        });
        for (var i in data.installations) {
            var installation = data.installations[i];
            lms_select.addOption({
                label: installation.title,
                value: installation.idlms_installation
            });
            if (installation.idlms_installation==selectedValue){
                dijit.byId("selectLMS_installation").set("value", selectedValue);
            }
        }
        dojo.byId("lmsGLueErrorReport").innerHTML = "";
        LoaderIcon.hide("loadingSelectLMSInstallation");
    },
    
    /**
 * Activa o desactiva el botón de obtención de participantes 
 */
    updateParticipantButton: function(){
        if (dijit.byId("select_clases").get("value") == 0){
            dijit.byId("boton_obtener_participantes").set('disabled', true);
        } else{
            dijit.byId("boton_obtener_participantes").set('disabled', false);
        }
    },
    
    /**
 * Si la instalación es de tipo GLUE obtiene los LMS de la misma
 */
    getLmsGlue: function(){
        if (dijit.byId("selectLMS_installation").get("value")!=0){
            var bindArgs = {
                url: "manager/manageLms.php",
                handleAs: "json",
                content: {
                    task: "obtener_installation_lms",
                    idlms_installation: dijit.byId("selectLMS_installation").get("value")
                },
                load: function(data){
                    //El tipo de lms 3 se corresponde con PA y el 4 con GluePS
                    if (data.ok && (data.installation.idlms=="3" || data.installation.idlms=="4")){
                        dijit.byId("selectLMS_installation_glue").set('disabled', true);
                        dojo.style("lmsGlue","display","");
                        dojo.style("lmsGLueErrorReport","display","");
                        LoaderIcon.show("loadingSelectLMSGlue");
                        ParticipantSelection.getLms(dijit.byId("selectLMS_installation").get("value"));
                    }
                    else{
                        dojo.style("lmsGlue","display","none");
                        dojo.style("lmsGLueErrorReport","display","none");
                        dojo.byId("lmsGLueErrorReport").innerHTML = "";
                    }
                }
            };
            dojo.xhrPost(bindArgs);
        }
        else{
            dojo.style("lmsGlue","display","none");
        }
    },   
    
    /**
 * Muestra la ventana de selección de participantes
 */
    showParticipantSelectionDialog: function(){
        dijit.byId("pantalla_obtener_alumnos").show();
    },
     
    /**
    * Ocultar la ventana de selección de participantes
    */
    hideParticipantSelectionDialog: function(){
        dijit.byId("pantalla_obtener_alumnos").hide();
    },
    
    /**
 * Muestra los cambios que se producirán al actualizar la lista de participantes para la instalación y clase seleccionados
 */
    showParticipantComparation: function(){
        if ((dijit.byId("selectLMS_installation").get("value") != 0) && (dijit.byId("select_clases").get("value") != 0)){
            if (dijit.byId("selectLMS_installation_glue").get("value") == 0){
                ParticipantManagement.getChosenParticipants(dijit.byId("selectLMS_installation").get("value"),
                    dijit.byId("select_clases").get("value") );
            }
            else{
                ParticipantManagement.getChosenParticipantsGlue(dijit.byId("selectLMS_installation").get("value"),dijit.byId("selectLMS_installation_glue").get("value"), dijit.byId("select_clases").get("value"));
            }
        }
        else{
            ParticipantSelection.hideParticipantSelectionDialog();
        }
    },   
    
    /**
    * Resetea la tabla donde se muestran los participantes
    */
    resetParticipants: function(){
        dijit.byId("selectLMS_installation").set("value", 0);
        dijit.byId("select_clases").set("value", 0);
        if(DesignInstance.data.instObj.name!=0){
            var divtable = document.getElementById("tabla_alumnos");
            //Eliminamos todos los elementos de la tabla
            var nodoTabla = divtable.getElementsByTagName("table");
            if (nodoTabla.length!=0){
                divtable.removeChild(nodoTabla[0]) ;
            }
        }
        dojo.byId("getParticipantsErrorReport").innerHTML = "";
        dojo.byId("numberOfClassParticipants").innerHTML = "";
    },
    
    /**
 * Resetea la tabla de participantes
 */
    resetParticipantTable: function(){
        var divtable = document.getElementById("tabla_alumnos");
        //Eliminamos todos los elementos de la tabla
        var nodoTabla = divtable.getElementsByTagName("table");
        if (nodoTabla.length!=0){
            divtable.removeChild(nodoTabla[0]) ;
        }
        var tabla=document.createElement("table");
        tabla.className="participantsTable";
        //Rellenamos la table
        var th = tabla.appendChild(document.createElement("th"));
        th.innerHTML = i18n.get("participantes.nombre");
        th = tabla.appendChild(document.createElement("th"));
        th.innerHTML = i18n.get("participantes.tipo");
    },
    
    /**
    * Resetea el select para la elección de clase
    */
    resetSelectClass: function(){
        dojo.byId("getParticipantsErrorReport").innerHTML = "";
        dojo.byId("numberOfClassParticipants").innerHTML = "";
        dojo.byId("getClasesErrorReport").innerHTML = "";
        dojo.byId("lmsGLueErrorReport").innerHTML = "";
        dijit.byId("select_clases").set("disabled", true);
        var class_select = dijit.byId("select_clases");
        while (class_select.getOptions(0)!=null){
            class_select.removeOption(class_select.getOptions(0));
        }
        class_select.addOption({
            label: i18n.get("participantes.clase.select"),
            value: "0"
        });
    },
    
    /**
* Resetea el select para la elección de clase de una instalación de glue
*/
    resetSelectClassGlue: function(){
        dojo.byId("getParticipantsErrorReport").innerHTML = "";
        dojo.byId("numberOfClassParticipants").innerHTML = "";
        dojo.byId("getClasesErrorReport").innerHTML = "";
        dijit.byId("select_clases").set("disabled", true);
        var class_select = dijit.byId("select_clases");
        while (class_select.getOptions(0)!=null){
            class_select.removeOption(class_select.getOptions(0));
        }
        class_select.addOption({
            label: i18n.get("participantes.clase.select"),
            value: "0"
        });
    },
    
    /**
* Resetea el select para la elección de lms en una instalación de glue
*/
    resetSelectLmsGlue: function(){
        dojo.byId("lmsGLueErrorReport").innerHTML = "";
        var lms_select = dijit.byId("selectLMS_installation_glue");
        while (lms_select.getOptions(0)!=null){
            lms_select.removeOption(lms_select.getOptions(0));
        }
        lms_select.addOption({
            label: i18n.get("participantes.lmsglue.select"),
            value: "0"
        });
    },
    
    /**
    * Obtiene las clases de una instalación de lms
    */
    checkGetClasses: function(){
        if (dijit.byId("selectLMS_installation").get("value")!=0){
            var bindArgs = {
                url: "manager/manageLms.php",
                handleAs: "json",
                content: {
                    task: "obtener_installation_lms",
                    idlms_installation: dijit.byId("selectLMS_installation").get("value")
                },
                load: function(data){
                    //Si no es necesario especificar un lms en concreto se pasa a obtener las clases
                    if (data.ok && data.installation.idlms!=3 && data.installation.idlms!=4){
                        LoaderIcon.show("loadingSelectClases");
                        ParticipantSelection.getClasses(dijit.byId("selectLMS_installation").get("value"));
                    }
                }
            };
            dojo.xhrPost(bindArgs);
        }
    },

    /**
* Obtiene las clases de una instalación de lms de Glue
*/
    checkGetClassesGlue: function(){
        if (dijit.byId("selectLMS_installation_glue").get("value")!=0){
            LoaderIcon.show("loadingSelectClases");
            ParticipantSelection.getClassesGlue(dijit.byId("selectLMS_installation").get("value"), dijit.byId("selectLMS_installation_glue").get("value"));
        }
    },
    
    /**
    * Obtiene los lms de una instalación de glue para mostrarles en el select
    * @param idlms_installation Identificador de la instalación de la que se quiere obtener los lms
    */
    getLms: function(idlms_installation){
        var bindArgs = {
            url: "manager/lms/controladorLms.php",
            handleAs: "json",
            content: {
                task: "obtener_lms",
                idlms_installation: idlms_installation
            },
            load: function(data){
                if (data.ok){
                    ParticipantSelection.showLms(data);
                }
                else{
                    if (dijit.byId("selectLMS_installation").get("value")==idlms_installation){                        
                        ParticipantSelection.resetSelectLmsGlue();
                        var lms_select = dijit.byId("selectLMS_installation_glue");
                        while (lms_select.getOptions(0)!=null){
                            lms_select.removeOption(lms_select.getOptions(0));
                        }
                        lms_select.addOption({
                            label: i18n.get("participantes.lmsglue.select"),
                            value: "0"
                        });
                        dojo.byId("lmsGLueErrorReport").innerHTML = i18n.get("participantes.errorGetLms");
                    }
                }
                LoaderIcon.hide("loadingSelectLMSGlue");
            }
        };
        dojo.xhrPost(bindArgs);
    },

    /**
    * Muestra en el select las clases de una instalación de lms
    * @param idlms_installation Instalación de lms de la que se muestran las clases
    */
    getClasses: function(idlms_installation){
        var bindArgs = {
            url: "manager/lms/controladorLms.php",
            handleAs: "json",
            content: {
                task: "obtener_clases",
                idlms_installation: idlms_installation
            },
            load: function(data){
                if (data.ok){
                    ParticipantSelection.showSelectClasses(idlms_installation, data);
                }
                else{
                    if (dijit.byId("selectLMS_installation").get("value")==idlms_installation){
                        dojo.byId("getClasesErrorReport").innerHTML = i18n.get("participantes.errorGetClasses");
                        var class_select = dijit.byId("select_clases");
                        while (class_select.getOptions(0)!=null){
                            class_select.removeOption(class_select.getOptions(0));
                        }
                        class_select.addOption({
                            label: i18n.get("participantes.clase.select"),
                            value: "0"
                        });
                    }
                }
                LoaderIcon.hide("loadingSelectClases");
            }

        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
* Muestra en el select las clases de una instalación de lms de Glue
* @param idlms_installation Instalación de lms de la que se muestran las clases
* @param idlmsSystem LMs de la instalación de la que se muestran las clases
*/
    getClassesGlue: function(idlms_installation, idlmsSystem){
        var bindArgs = {
            url: "manager/lms/controladorLms.php",
            handleAs: "json",
            content: {
                task: "obtener_clases_lms",
                idlms_installation: idlms_installation,
                idlmsSystem: idlmsSystem
            },
            load: function(data){
                if (data.ok){
                    ParticipantSelection.showSelectClasses(idlms_installation, data);
                }
                else{
                    if (dijit.byId("selectLMS_installation").get("value")==idlms_installation){
                        dojo.byId("getClasesErrorReport").innerHTML = i18n.get("participantes.errorGetClasses");
                        var class_select = dijit.byId("select_clases");
                        while (class_select.getOptions(0)!=null){
                            class_select.removeOption(class_select.getOptions(0));
                        }
                        class_select.addOption({
                            label: i18n.get("participantes.clase.select"),
                            value: "0"
                        });
                    }
                }
                LoaderIcon.hide("loadingSelectClases");
            }
        };
        dojo.xhrPost(bindArgs);
    },

    /**
    * Función que se encarga de comprobar la obtención de participantes
    */
    checkGetParticipants: function(){
        if ((dijit.byId("selectLMS_installation").get("value")!=0) && (dijit.byId("select_clases").get("value")!=0)){
            var bindArgs = {
                url: "manager/manageLms.php",
                handleAs: "json",
                content: {
                    task: "obtener_installation_lms",
                    idlms_installation: dijit.byId("selectLMS_installation").get("value")
                },
                load: function(data){
                    if (data.ok && data.installation.idlms!=3 && data.installation.idlms!=4){
                        ParticipantSelection.getParticipants(dijit.byId("selectLMS_installation").get("value"), dijit.byId("select_clases").get("value") );
                    }
                    else{
                        ParticipantSelection.getGlueParticipants(dijit.byId("selectLMS_installation").get("value"), dijit.byId("selectLMS_installation_glue").get("value"),dijit.byId("select_clases").get("value")); 
                    }
                }
            };
            ParticipantSelection.deleteParticipantTable();
            LoaderIcon.show("loadingBotonObtenerParticipantes");
            dojo.xhrPost(bindArgs);
        }
    },

    /**
* Obtiene los participantes de una instalación y clase dados
* @param idlms_installation Instalación de la que se obtienen los participantes
* @param idclase Clase de la que se obtienen los participantes
*/
    getParticipants: function(idlms_installation, idclase){
        var bindArgs = {
            url: "manager/lms/controladorLms.php",
            handleAs: "json",
            content: {
                task: "obtener_participantes",
                idlms_installation: idlms_installation,
                idclase: idclase
            },
            load: function(data){
                if (data.ok){
                    ParticipantSelection.showParticipants(data);
                }
                else{
                    dojo.byId("getParticipantsErrorReport").innerHTML = i18n.get("participantes.errorGetParticipants");
                }
                LoaderIcon.hide("loadingBotonObtenerParticipantes");
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
* Obtiene los participantes de una instalación de glue
* @param idlms_installation Instalación de la que se obtienen los participantes
* @param idlmsSystem LMS del que se obtienen los participantes
* @param idlmsCourse Clase de la que se obtienen los participantes
*/
    getGlueParticipants: function(idlms_installation, idlmsSystem, idlmsCourse){
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
                if (data.ok){
                    ParticipantSelection.showParticipants(data);
                }
                else{
                    dojo.byId("getParticipantsErrorReport").innerHTML = i18n.get("participantes.errorGetParticipants");
                }
                LoaderIcon.hide("loadingBotonObtenerParticipantes");
            }
        };
        dojo.xhrPost(bindArgs);
    },
    
    /**
    * Muestra en un select los lms de una instalación de glue
    */
    showLms: function(data){
        dijit.byId("selectLMS_installation_glue").setAttribute('disabled', false);
        ParticipantSelection.resetSelectLmsGlue();
        var lms_select = dijit.byId("selectLMS_installation_glue");
        for (var i in data.lms) {
            var lms = data.lms[i];
            lms_select.addOption({
                label: lms.name,
                value: lms.id
            });
        }
    },

    /**
* Muestra en select las clases 
*/
    showSelectClasses: function(idlms_installation, data){
        ParticipantSelection.resetSelectClass();
        var class_select = dijit.byId("select_clases");
        for (var i in data.clases) {
            var clase = data.clases[i];
            class_select.addOption({
                label: clase.name,
                value: clase.id
            });
        }
        dijit.byId("select_clases").set("disabled", false);
    },
    
    deleteParticipantTable: function(){
        var divtable = document.getElementById("tabla_alumnos");
        //Eliminamos todos los elementos de la tabla
        var nodoTabla = divtable.getElementsByTagName("table");
        if (nodoTabla.length!=0){
            divtable.removeChild(nodoTabla[0]) ;
        }
    },
    /**
    * Muestra en una tabla el listado de participantes de una clase seleccionada
    */
    showParticipants: function(data){
        var divtable = document.getElementById("tabla_alumnos");
        //Eliminamos todos los elementos de la tabla
        var nodoTabla = divtable.getElementsByTagName("table");
        if (nodoTabla.length!=0){
            divtable.removeChild(nodoTabla[0]) ;
        }
        var tabla=document.createElement("table");
        tabla.className="participantsTable";
        //Rellenamos la table
        var th = tabla.appendChild(document.createElement("th"));
        th.innerHTML = i18n.get("participantes.nombre");
        th = tabla.appendChild(document.createElement("th"));
        th.innerHTML = i18n.get("participantes.tipo");
        var participantes = new Array();
        for (var i=0; i<data.participantes.length;i++)
        {
            participantes.push(data.participantes[i]);
        }
        participantes.sort(natcompare);
        for (var j in participantes) {
            var tr = tabla.appendChild(document.createElement("tr"));
            var td = tr.appendChild(document.createElement("td"));
            td.innerHTML = participantes[j].name;
            td = tr.appendChild(document.createElement("td"));
            if (participantes[j].type!="teacher"){
                td.innerHTML = participantes[j].type;
            }
            else{
                td.innerHTML = "<b>" + participantes[j].type + "</b>";
            }
        }
        divtable.appendChild(tabla);
        
        var cp=document.getElementById("numberOfClassParticipants");
        cp.innerHTML = i18n.get("participantes.numberParticipants")+ participantes.length;
        cp.style.fontWeight = 'bold';
    }


};