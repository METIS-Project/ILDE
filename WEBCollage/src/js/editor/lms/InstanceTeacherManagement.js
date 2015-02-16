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
 * Gestión de los profesores asignados a una instancia de grupo con rol profesor
 */
var InstanceTeacherManagement = {
       
    /**
     * Array donde se guardan temporalmente los profesores pertenecientes a la instancia de grupo
     */
    assignedTeachers: new Array(),
    
    /**
     * Array donde se guardan temporalmente los profesores disponibles para ser asignados a la instancia de grupo
     */
    availableTeachers: new Array(),

    /**
     * Identificador de la instancia de grupo de rol profesor con la que se está trabajando
     */
    idTeacherInstance: null,

    /**
     * Muestra en un tooltip los profesores asignados a la instancia de grupo
     * @param instanceid Identificador de la instancia
     */
    showTooltipAssignedTeachers: function(instanceid){
        var tooltipTeachers = new Array();
        var instance = IDPool.getObject(instanceid);
        //Se van incluyendo los profesores que existen con un salto de línea en cada profesor introducido
        for (var i=0; i<instance.participants.length; i++){
            tooltipTeachers.push("<br>" + DesignInstance.getParticipant(instance.participants[i]).name);
        }      
        //Se añade al principio del array la cantidad de profesores que existen para poder mostrarlo en el campo help del menú
        tooltipTeachers.unshift(i18n.get("participants.pertenecienteProfesor") + " " + tooltipTeachers.length);
        return tooltipTeachers;
    },	
    
    /**
     * Oculta la ventana de la asignación de profesores
     */
    hideTeachers: function(){
        dijit.byId("divProfesoresAsignados").hide();
    },
    
    /**
     * Muestra la ventana de asignación de profesores si hay participantes en la instalación
     * @param instanceid Identificador de la instancia
     * @param clfp Identificador del clfp
     */
    showTeachers: function(instanceid, clfp){
        //Si no se ha elegido anteriormente ninguna lista de participantes, se indica que se debe elegir primero
        if (DesignInstance.data.participants.length==0){
            ParticipantManagement.showDialogAlert(i18n.get("actualizar.aviso"),i18n.get("participants.alumnoselegidos"));
        }else{
            dijit.byId("divProfesoresAsignados").show();
            InstanceTeacherManagement.startShowTeachers(instanceid, clfp);
            
        }
    },    
    
    /**
     * Función que inicializa todo el proceso y muestra los profesores asignados a la instancia y disponibles
     * @param instanceid Identificador de la instancia del grupo
     * @param clfp Identificador del clfp 
     */
    startShowTeachers: function(instanceid, clfp){
        InstanceTeacherManagement.idTeacherInstance = instanceid;
        
        InstanceTeacherManagement.assignedTeachers = ClfpsCommon.obtainAssignedTeachers(instanceid);
        InstanceTeacherManagement.showAssignedTeachers();
        InstanceTeacherManagement.availableTeachers = ClfpsCommon.obtainAvailableTeachers(clfp,instanceid);
        InstanceTeacherManagement.showAvailableTeachers();
    },
    
    /**
     * Función que saca por pantalla los profesores asignados al grupo
     */
    showAssignedTeachers: function(){
        var tablaasignadospro = document.getElementById("tablaProfesoresAsignados");
        if (tablaasignadospro.hasChildNodes()){
            while (tablaasignadospro.childNodes.length>=1){
                tablaasignadospro.removeChild(tablaasignadospro.firstChild);
            }
        }
        var th = tablaasignadospro.appendChild(document.createElement("th"));
        th.innerHTML = i18n.get("participantes.nombre");
        tablaasignadospro.className="participantsTable";
        tablaasignadospro.style.backgroundColor="#81F7F3";
        DesignInstance.participantSort(InstanceTeacherManagement.assignedTeachers);
        for (var i=0; i<InstanceTeacherManagement.assignedTeachers.length; i++){
            var tr = tablaasignadospro.appendChild(document.createElement("tr"));
            tr.className="clickable";
            var td = tr.appendChild(document.createElement("td"));
            td.innerHTML = DesignInstance.getParticipant(InstanceTeacherManagement.assignedTeachers[i]).name;
            MenuManager.registerThing(td, {
                getItems: function(data) {
                    return InstanceTeacherManagement.assignedTeachersOptions(data.participantId);
                },
                data: {
                    participantId: InstanceTeacherManagement.assignedTeachers[i]
                },
                onClick: function(data){
                    InstanceTeacherManagement.rejectTeacher(data.participantId);
                }
            });
        }
        var pa=document.getElementById("numProfesoresAsignados");
        pa.innerHTML = i18n.get("participants.profesoresasignadosgrupo")+ ": "+ InstanceTeacherManagement.assignedTeachers.length;
        pa.style.fontWeight = 'bold';
    },
        
    /**
     * Muestra por pantalla los profesores disponibles para poder ser añadidos a un grupo
     */
    showAvailableTeachers: function(){
        var tabladisponiblespro = document.getElementById("tablaProfesoresDisponibles");
        if (tabladisponiblespro.hasChildNodes()){
            while (tabladisponiblespro.childNodes.length>=1){
                tabladisponiblespro.removeChild(tabladisponiblespro.firstChild);
            }
        }
        var th = tabladisponiblespro.appendChild(document.createElement("th"));
        th.innerHTML = i18n.get("participantes.nombre");
        tabladisponiblespro.className="participantsTable";
        tabladisponiblespro.style.backgroundColor="#A9D0F5";
        DesignInstance.participantSort(InstanceTeacherManagement.availableTeachers);
        //Se tiene que recorrer exclusivamente el array de participantes de un grupo y de una instancia que vienen ya dados
        for (var i=0; i<InstanceTeacherManagement.availableTeachers.length; i++){
            var tr = tabladisponiblespro.appendChild(document.createElement("tr"));
            tr.className="clickable";
            var td = tr.appendChild(document.createElement("td"));
            td.innerHTML = DesignInstance.getParticipant(InstanceTeacherManagement.availableTeachers[i]).name;
            MenuManager.registerThing(td, {
                getItems: function(data) {
                    return InstanceTeacherManagement.availableTeachersOptions(data.participantId);
                },
                data: {
                    participantId: InstanceTeacherManagement.availableTeachers[i]
                },
                onClick: function(data){
                    InstanceTeacherManagement.assignTeacher(data.participantId);
                }
            });
        }
        var pd=document.getElementById("numProfesoresDisponibles");
        pd.innerHTML = i18n.get("participants.profesoresdisponibles")+ ": "+ InstanceTeacherManagement.availableTeachers.length;
        pd.style.fontWeight = 'bold';
    },
    
    /**
     * Opciones correspondientes a los profesores que pertenecen a la instancia de un grupo
     * @param participantId Identificador del profesor 
     */
    assignedTeachersOptions: function(participantId){
        var options = new Array();
        options.push({
            label: i18n.get("participants.desasignarprofesores"),
            icon: "rejectTeacher",
            onClick: function(data){
                InstanceTeacherManagement.rejectTeacher(data.instanceId);
            },
            data: {
                instanceId: participantId
            },
            help: i18n.get("participants.desasignarprofesores.help")
        });
        options.push( {
            isSeparator : true
        });
        options.push({
            label: i18n.get("participants.desasignartodosprofesores"),
            icon: "rejectAllTeachers",
            onClick: function(data){
                InstanceTeacherManagement.rejectAllTeachers();
            },
            data: {},
            help: i18n.get("participants.desasignartodosprofesores.help")
        });
        return options;
    },
    
        
    /**
     * Opciones disponibles para los profesores
     * @param participantId Identificador del profesor 
     */
    availableTeachersOptions: function(participantId){
        var options = new Array();
        options.push({
            label: i18n.get("participants.asignarprofesores"),
            icon: "addTeacher",
            onClick: function(data){
                InstanceTeacherManagement.assignTeacher(data.participantId);
            },
            data: {
                participantId: participantId
            },
            help: i18n.get("participants.asignarprofesores.help")
        });
        options.push( {
            isSeparator : true
        });
        options.push({
            label: i18n.get("participants.asignartodosprofesores"),
            icon: "addAllTeachers",
            onClick: function(data){
                InstanceTeacherManagement.assignAllTeachers();
            },
            data: {},
            help: i18n.get("participants.asignartodosprofesores.help")
        });
        return options;
    },
    
    /**
     * Función que desasigna un profesor de un grupo volviéndolo a pasar a disponibles
     * @param participantId Identificador del profesor 
     */
    rejectTeacher: function(participantId){
        for (var i=0; i<InstanceTeacherManagement.assignedTeachers.length;i++)
        {
            if (InstanceTeacherManagement.assignedTeachers[i]==participantId)
            {
                InstanceTeacherManagement.assignedTeachers.splice(i,1);
                break;
            }
        }
        InstanceTeacherManagement.availableTeachers.push(participantId);
        
        InstanceTeacherManagement.showAssignedTeachers();
        InstanceTeacherManagement.showAvailableTeachers();
    },
    
    /**
     * Función que desasigna todos los profesores de los ya pertenecientes a un grupo
     */
    rejectAllTeachers: function(){
        for (var i=0; i<InstanceTeacherManagement.assignedTeachers.length; i++){
            InstanceTeacherManagement.availableTeachers.push(InstanceTeacherManagement.assignedTeachers[i]);
        }
        InstanceTeacherManagement.assignedTeachers.splice(0, InstanceTeacherManagement.assignedTeachers.length);
        
        InstanceTeacherManagement.showAssignedTeachers();
        InstanceTeacherManagement.showAvailableTeachers();
    },

    
    /**
     * Función que asigna un profesor a un grupo de forma temporal hasta que se de al OK
     * @param participantId Identificador del profesor 
     */
    assignTeacher: function(participantId){
        InstanceTeacherManagement.assignedTeachers.push(participantId);
        for (var i=0; i<InstanceTeacherManagement.availableTeachers.length;i++)
        {
            if (InstanceTeacherManagement.availableTeachers[i]==participantId)
            {
                InstanceTeacherManagement.availableTeachers.splice(i,1);
                break;
            }
        }       
                
        InstanceTeacherManagement.showAssignedTeachers();
        InstanceTeacherManagement.showAvailableTeachers();
    },
    
    /**
     * Función que asigna todos los profesores disponibles a la instancia de grupo
     */
    assignAllTeachers: function(){
        for (var i=0; i<InstanceTeacherManagement.availableTeachers.length; i++){
            InstanceTeacherManagement.assignedTeachers.push(InstanceTeacherManagement.availableTeachers[i]);
        }
        InstanceTeacherManagement.availableTeachers.splice(0, InstanceTeacherManagement.availableTeachers.length);
        
        InstanceTeacherManagement.showAssignedTeachers();
        InstanceTeacherManagement.showAvailableTeachers();
    },
    
    /**
     * Guarda los cambios producidos en la asignación de profesores
     */
    confirmTeacherChanges: function(){
        var currentClfpId=LearningFlow.getCurrentClfpId();
        //Borrar de forma ascendente los participantes que desaparecen
        var indicestemp = new Array();
        var instance = IDPool.getObject(InstanceTeacherManagement.idTeacherInstance);
        for (var i=0; i<instance.participants.length; i++){
            var desaparece = true;
            for (var j=0; j<InstanceTeacherManagement.assignedTeachers.length; j++){
                if (instance.participants[i]==InstanceTeacherManagement.assignedTeachers[j]){
                    desaparece = false;
                }
            }
            //Se guardan los índices que se deben borrar del array
            if (desaparece){
                indicestemp.push(i);                
            }
        }
        //Se accede a las posiciones correspondientes a los índices del array y se van borrando
        for (var a=0; a<indicestemp.length; a++){
            indicestemp[a]=indicestemp[a]-a;
            IDPool.getObject(currentClfpId).borrar(instance, instance.participants[indicestemp[a]]);
        }
        instance.participants.splice(0,instance.participants.length);
        for (var b=0; b<InstanceTeacherManagement.assignedTeachers.length; b++){
            DesignInstance.asignacionAscendente(instance, InstanceTeacherManagement.assignedTeachers[b]);
        }
        Loader.save("");
        dijit.byId("divProfesoresAsignados").hide();
    }
};

