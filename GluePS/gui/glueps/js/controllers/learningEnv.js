/*
Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Glue!PS.

Glue!PS is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Glue!PS is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

dojo.registerModulePath("glueps.dojoi18n", "../../../dojoi18n");
var LE = {

    init: function(){
    	 dojo.connect(dijit.byId("checkDeploy"),"onClick",LE,"showSelectTemas"); 
    },
    
    leJson: "",
    
     /**
      * Obtiene los diferentes VLE
      */
     getLEnvironments:function() {
    	 var baseUrl = window.location.href.split("/GLUEPSManager")[0];
        //The parameters to pass to xhrGet, the url, how to handle it, and the callbacks.
        var xhrArgs = {
            url: baseUrl + "/GLUEPSManager/learningEnvironments",
			handleAs : "json",// Tipo de dato de la respuesta del Get,
			headers : { // Indicar en la cabecera que es json
				"Content-Type" : "application/json",
				"Accept" : "application/json"
			}, 
            load: function(data) {
            	LE.leJson = data;
            	//Tratamiento de la respuesta del get            		           	
            	LE.rellenarSelectVle(data);
            },
            error: function(error, ioargs) {  	   
         	   var message = "";
         	   LE.leJson = "";
         	   message=ErrorCodes.errores(ioargs.xhr.status);
         	   Glueps.showAlertDialog(i18n.get("warning"), message);
            }
        };

        //Call the asynchronous xhrGet
        var deferred = dojo.xhrGet(xhrArgs);
    },
    
    /**
     * Añade las opciones al select del lms
     * @param data la información de los LMS
     */
    rellenarSelectVle: function(data){
    	var entryList=data;
    	var vle_Select=dojo.byId("vleSelect");    	
    	//Elimino options del select
       	while (vle_Select.options.length != 0)
    	{
    		vle_Select.removeChild(vle_Select.options[0]);    	
       	}
       	//Añadir opción por defecto
		var option =document.createElement("option");
		option.innerHTML=i18n.get("NewDeployLabelSelectVleDefault");
		option.value="0";
		option.setAttribute("selected","true");
		vle_Select.appendChild(option); 
    	//Añado restantes opciones
    	for(var i=0; i<entryList.length;i++){
    		var title=entryList[i].name;
    		var creduser = entryList[i].creduser;
    		var option =document.createElement("option");
    		option.innerHTML= title +  " (" + creduser + ")";
    		option.value=entryList[i].id;
    		vle_Select.appendChild(option); 
    	}
    	//Ocultar la selección de curso
    	dojo.style("divCourseSelect","display","none");
    	
    },
    
    /**
     * Muestra o oculta, según corresponda, el select del lms
     */
    showSelectTemas:function(){
    	var nodoCheck=dijit.byId("checkDeploy");
    	if (nodoCheck.checked){
	    	dojo.style(dojo.byId("temaSelect"), {
	            display:""
	        });
    	}
    	else{
    		dojo.style(dojo.byId("temaSelect"), {
	            display:"none"
	        });
    	}
   },
   
   getLearningEnvironmentType: function(leId){
   	var leType ="";
     	if (LE.leJson!=""){
   		var leType = "";
   		for (var i = 0; i < LE.leJson.length; i++){
   			if (LE.leJson[i].id==leId){
   				leType = LE.leJson[i].type;
   				return leType;
   			}
   		}       		
   	}
     	return leType;
     }
};
