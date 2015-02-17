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

/**
 * 
 */
var LearningEnvironmentDB = {
		   	   
	learningEnvironment: null,
		   
	init: function(id){
		//this.getLearningEnvironment(urlDeploy);
	},
	
    getLearningEnvironment:function(id) {
    	var baseUrl = window.location.href.split("/GLUEPSManager")[0];
        var xhrArgs = {
            url: baseUrl + "/GLUEPSManager/learningEnvironments/" + id,
            handleAs: "text",//Tipo de dato de la respuesta del Get
            load: function(data) {    
            	LearningEnvironmentDB.learningEnvironment = data;
            },
            error: function(error, ioargs) {  
               LearningEnvironmentDB.learningEnvironment = null;
         	   var message = "";
         	   message=ErrorCodes.errores(ioargs.xhr.status);
         	  Glueps.showAlertDialog(i18n.get("warning"), message);
            }
        }
        //Call the asynchronous xhrGet
        var deferred = dojo.xhrGet(xhrArgs);
    }
}
	
	