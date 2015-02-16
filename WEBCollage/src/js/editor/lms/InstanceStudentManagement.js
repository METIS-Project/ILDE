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
 * Gestión de los estudiantes asignados a una instancia de grupo
 */
var InstanceStudentManagement = {
        
    /**
     * Array donde se guardan temporalmente los alumnos pertenecientes a la instancia de grupo
     */
    assignedStudents: new Array(),

    /**
     * Array donde se guardan temporalmente los alumnos disponibles para ser asignados a la instancia de un grupo
     */
    availableStudents: new Array(),
        
    /**
     * Identificador de la instancia de grupo con la que se está trabajando
     */
    idStudentInstance: null,
    
            
    /**
     * Muestra en un tooltip los alumnos asignados a la instancia de grupo
     * @param instanceid Identificador de la instancia
     */
    showTooltipAssignedStudents: function(instanceid){
        var tooltipStudents = new Array();
        var instance = IDPool.getObject(instanceid);
        //Se van incluyendo los alumnos que existen con un salto de línea en cada alumno introducido
        for (var i=0; i<instance.participants.length; i++){
            tooltipStudents.push("<br>" + DesignInstance.getParticipant(instance.participants[i]).name);
        }
        //Se añade al principio del array la cantidad de alumnos que existen para poder mostrarlo en el campo help del menú
        tooltipStudents.unshift(i18n.get("participants.perteneciente") + " " + tooltipStudents.length);
        return tooltipStudents;
    },
    
    /**
     * Oculta la ventana de la asignación de Alumnos
     */
    hideStudents: function(){
        dijit.byId("alumnosAsignadosClase").hide();
    },
        
    /**
     * Muestra la ventana de asignación de alumnos si hay participantes en la instalación
     * @param instanceid Identificador de la instancia del grupo
     * @param clfp Identificador del clfp 
     */
    showStudents: function(instanceid, clfp){
        //Si no se ha elegido anteriormente ninguna lista de participantes, se indica que se debe elegir primero
        if (DesignInstance.data.participants.length==0){
            ParticipantManagement.showDialogAlert(i18n.get("actualizar.aviso"),i18n.get("participants.alumnoselegidos"));
        }else{
            dijit.byId("alumnosAsignadosClase").show();
            InstanceStudentManagement.startShowStudents(instanceid, clfp);
        }
    },
    
    /**
     * Función que inicializa todo el proceso y muestra los participantes asignados a la instancia, disponible y asignados a otras instancias del grupo
     * @param instanceid Identificador de la instancia del grupo
     * @param clfp Identificador del clfp 
     */
    startShowStudents: function(instanceid, clfp){
        InstanceStudentManagement.idStudentInstance = instanceid;
        
        InstanceStudentManagement.assignedStudents = InstanceStudentManagement.obtainAssignedStudents(instanceid);
        InstanceStudentManagement.showAssignedStudents();
        InstanceStudentManagement.availableStudents = clfp.obtainAvailableStudents(instanceid);
        InstanceStudentManagement.showAvailableStudents();
        var assignedStudentsOthers = ClfpsCommon.obtainAssignedStudentsOthers(instanceid);  //Obtenemos los estudiantes asignados a otras instancias del grupo
        InstanceStudentManagement.showAssignedStudentsOthers(assignedStudentsOthers);
    },
    
    /**
     * Obtiene los alumnos asignados a la instancia de grupo
     * @param instanceid Identificador de la instancia del grupo
     * @return Array con los alumnos asignados a la instancia de grupo
     */
    obtainAssignedStudents: function(instanceid){        
        var instance = IDPool.getObject(instanceid);
        var asigned = new Array();
        for (var i=0; i<instance.participants.length; i++){
            asigned.push(instance.participants[i]);
        }
        return asigned;
    },
    
    /**
     * Se muestra en la tabla el listado de alumnos pertenecientes al grupo de la instancia elegida
     */
    showAssignedStudents: function(){
        var tablaasignados = document.getElementById("tablaAlumnosAsignados");
        if (tablaasignados.hasChildNodes()){
            while (tablaasignados.childNodes.length>=1){
                tablaasignados.removeChild(tablaasignados.firstChild);
            }
        }
        var th = tablaasignados.appendChild(document.createElement("th"));
        th.innerHTML = i18n.get("participantes.nombre");
        tablaasignados.className="participantsTable";
        tablaasignados.style.backgroundColor="#81F7F3";
        DesignInstance.participantSort(InstanceStudentManagement.assignedStudents);
        for (var i=0; i<InstanceStudentManagement.assignedStudents.length; i++){
            var tr = tablaasignados.appendChild(document.createElement("tr"));
            tr.className="clickable";
            var td = tr.appendChild(document.createElement("td"));
            td.innerHTML = DesignInstance.getParticipant(InstanceStudentManagement.assignedStudents[i]).name;
            MenuManager.registerThing(td, {
                getItems: function(data) {
                    return InstanceStudentManagement.assignedStudentsOptions(data.participantId);
                },
                data: {
                    participantId: InstanceStudentManagement.assignedStudents[i]
                },
                onClick: function(data){
                    InstanceStudentManagement.rejectStudent(data.participantId);
                }
            });
        }
        var pm=document.getElementById("num1");
        pm.innerHTML = i18n.get("participants.perteneciente")+ InstanceStudentManagement.assignedStudents.length;
        pm.style.fontWeight = 'bold';
    },
      
    /**
     * Se muestran por pantalla los alumnos disponibles
     */
    showAvailableStudents: function(){
        var tabladisponibles = document.getElementById("tablaAlumnosDisponibles");
        if (tabladisponibles.hasChildNodes()){
            while (tabladisponibles.childNodes.length>=1){
                tabladisponibles.removeChild(tabladisponibles.firstChild);
            }
        }
        var th = tabladisponibles.appendChild(document.createElement("th"));
        th.innerHTML = i18n.get("participantes.nombre");
        tabladisponibles.className="participantsTable";
        tabladisponibles.style.backgroundColor="#A9D0F5";
        //Ordenamos el alfanuméricamente el listado de participantes disponibles
        DesignInstance.participantSort(InstanceStudentManagement.availableStudents);
        //Se tiene que recorrer exclusivamente el array de participantes de un grupo y de una instancia que vienen ya dados
        for (var i=0; i<InstanceStudentManagement.availableStudents.length; i++){
            var tr = tabladisponibles.appendChild(document.createElement("tr"));
            var td = document.createElement("td");
            tr.className="clickable";
            tr.appendChild(td);
            td.innerHTML = DesignInstance.getParticipant(InstanceStudentManagement.availableStudents[i]).name;
            MenuManager.registerThing(td, {
                getItems: function(data) {
                    return InstanceStudentManagement.availableStudentsOptions(data.participantId);
                },
                data: {
                    participantId: InstanceStudentManagement.availableStudents[i]
                },
                onClick: function(data){
                    InstanceStudentManagement.assignStudent(data.participantId);
                }
            });
        }
        //Se saca por pantalla el número de usuarios disponibles
        var pm=document.getElementById("num2");
        pm.innerHTML = i18n.get("participants.numdisponibles")+ InstanceStudentManagement.availableStudents.length;
        pm.style.fontWeight = 'bold';
    },
            
    /**
     * Se muestran por pantalla los alumnos asignados a otras instancias del grupo
     */
    showAssignedStudentsOthers: function(assignedStudentsOthers){
        
        //Se sacan por pantalla los usuarios asignados
        var tablayaasignados = document.getElementById("tablaAlumnosAsignadosOtrasInstancias");
        if (tablayaasignados.hasChildNodes()){
            while (tablayaasignados.childNodes.length>=1){
                tablayaasignados.removeChild(tablayaasignados.firstChild);
            }
        }
        var th2 = tablayaasignados.appendChild(document.createElement("th"));
        th2.innerHTML = i18n.get("participantes.nombre");
        tablayaasignados.className="participantsTable";
        tablayaasignados.style.backgroundColor="#58ACFA";
        DesignInstance.participantSort(assignedStudentsOthers);
        //Se tiene que recorrer exclusivamente el array de participantes de un grupo y de una instancia que vienen ya dados
        for (var j=0; j<assignedStudentsOthers.length; j++){
            var tr2 = tablayaasignados.appendChild(document.createElement("tr"));
            var td2 = tr2.appendChild(document.createElement("td"));
            td2.innerHTML = DesignInstance.getParticipant(assignedStudentsOthers[j]).name;
        }
        //Se muestra por pantalla el número de usuarios asignados
        var e=document.getElementById("num3");
        e.innerHTML = i18n.get("participants.numasignados")+ assignedStudentsOthers.length;
        e.style.fontWeight = 'bold';
    },

    /**
     * Opciones correspondientes a los alumnos que pertenecen a la instancia de un grupo
     * @param participantId Identificador del alumno
     */
    assignedStudentsOptions: function(participantId){
        var options = new Array();
        options.push({
            label: i18n.get("participants.desasignaralumnos"),
            icon: "rejectStudent",
            onClick: function(data){
                InstanceStudentManagement.rejectStudent(data.participantId);
            },
            data: {
                participantId: participantId
            },
            help: i18n.get("participants.desasignaralumnos.help")
        });
        options.push( {
            isSeparator : true
        });
        options.push({
            label: i18n.get("participants.desasignartodosalumnos"),
            icon: "rejectAllStudents",
            onClick: function(data){
                InstanceStudentManagement.rejectAllStudents();
            },
            data: {
            },
            help: i18n.get("participants.desasignartodosalumnos.help")
        });
        return options;
    },
            
    /**
     * Se muestra por pantalla el menú de los alumnos disponibles
     * @param participantId Identificador del alumno
     */
    availableStudentsOptions: function(participantId){
        var options = new Array();
        options.push({
            label: i18n.get("participants.asignaralumnos"),
            icon: "addStudent",
            onClick: function(data){
                InstanceStudentManagement.assignStudent(data.participantId);
            },
            data: {
                participantId: participantId
            },
            help: i18n.get("participants.asignaralumnos.help")
        });
        options.push( {
            isSeparator : true
        });
        options.push({
            label: i18n.get("participants.asignartodosalumnos"),
            icon: "addAllStudents",
            onClick: function(data){
                InstanceStudentManagement.assignAllStudents();
            },
            data: {
            },
            help: i18n.get("participants.asignartodosalumnos.help")
        });
        return options;
    },
        
    /**
     * Función que desasigna un alumno de un grupo volviéndolo a pasar a disponibles
     * @param participantId Identificador del alumno
     */
    rejectStudent: function(participantId){
        for (var i=0; i<InstanceStudentManagement.assignedStudents.length; i++)
        {
            if (InstanceStudentManagement.assignedStudents[i]==participantId)
            {
                InstanceStudentManagement.assignedStudents.splice(i, 1);
                break;
            }
        }
        InstanceStudentManagement.availableStudents.push(participantId);
                
        InstanceStudentManagement.showAssignedStudents();
        InstanceStudentManagement.showAvailableStudents();
    },
    
    /**
     * Función que desasigna todos los alumnos de los ya pertenecientes a un grupo
     */
    rejectAllStudents: function(){
        for (var i=0; i<InstanceStudentManagement.assignedStudents.length; i++){
            InstanceStudentManagement.availableStudents.push(InstanceStudentManagement.assignedStudents[i]);
        }
        InstanceStudentManagement.assignedStudents.splice(0, InstanceStudentManagement.assignedStudents.length);
        
        InstanceStudentManagement.showAssignedStudents();
        InstanceStudentManagement.showAvailableStudents();
    },
    
    /**
     * Función que asigna un alumno a un grupo de forma temporal hasta que se de al OK
     * @param participantId Identificador del alumno
     */
    assignStudent: function(participantId){
        InstanceStudentManagement.assignedStudents.push(participantId);
        for (var i=0; i<InstanceStudentManagement.availableStudents.length;i++){
            if (InstanceStudentManagement.availableStudents[i]==participantId)
            {
                InstanceStudentManagement.availableStudents.splice(i,1);
                break;
            }
        }
        
        InstanceStudentManagement.showAssignedStudents();
        InstanceStudentManagement.showAvailableStudents();
    },
    
    /**
     * Función que asigna todos los alumnos disponibles a la instancia de grupo en la que estamos
     */
    assignAllStudents: function(){
        for (var a=0; a<InstanceStudentManagement.availableStudents.length; a++){
            InstanceStudentManagement.assignedStudents.push(InstanceStudentManagement.availableStudents[a]);
        }
        InstanceStudentManagement.availableStudents.splice(0, InstanceStudentManagement.availableStudents.length);
        
        InstanceStudentManagement.showAssignedStudents();
        InstanceStudentManagement.showAvailableStudents();
    },
        
    /**
     * Aceptar finalmente los cambios que se han realizado al pulsar el botón 'Aceptar'
     */
    confirmStudentChanges: function(){
        var currentClfpId=LearningFlow.getCurrentClfpId();
        var instance = IDPool.getObject(InstanceStudentManagement.idStudentInstance);
        var indicestempalumnos = new Array();
        //Borrar de forma ascendente los participantes que desaparecen
        for (var i=0; i<instance.participants.length; i++){
            var desaparece = true;
            for (var j=0; j<InstanceStudentManagement.assignedStudents.length; j++){
                if (instance.participants[i]==InstanceStudentManagement.assignedStudents[j]){
                    desaparece = false;
                }
            }
            //Se guardan los índices que luego se deben borrar
            if (desaparece){
                indicestempalumnos.push(i);
            }
        }
        //Se recorren los índices y se va borrando
        for (var a=0; a<indicestempalumnos.length; a++){
            indicestempalumnos[a]=indicestempalumnos[a]-a;
            IDPool.getObject(currentClfpId).borrar(instance, instance.participants[indicestempalumnos[a]]);
        }
        instance.participants.splice(0,instance.participants.length);
        for (var m=0; m<InstanceStudentManagement.assignedStudents.length; m++){
            IDPool.getObject(currentClfpId).asignar(instance, InstanceStudentManagement.assignedStudents[m]);
        }
        Loader.save("");
        dijit.byId("alumnosAsignadosClase").hide();
    }
};


