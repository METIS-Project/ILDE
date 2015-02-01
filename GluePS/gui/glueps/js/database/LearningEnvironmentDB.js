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
	
	