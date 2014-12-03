/**
 * 
 */
var LearningEnvironmentTypesDB = {

	getLearningEnvironmentType : function(id) {
		var result = null;
		var baseUrl = window.location.href.split("/GLUEPSManager")[0];
		var xhrArgs = {
			url : baseUrl + "/GLUEPSManager/learningEnvironmentTypes/" + id,
			handleAs : "json",
			sync: true,
			headers : {
		        "Content-Type": "application/json",
		        "Accept" : "application/json"		
			},
			load : function(data) {
				result = data;
			},
			error : function(error, ioargs) {
				result = null;
			}
		}
		//Call the asynchronous xhrGet
		var deferred = dojo.xhrGet(xhrArgs);
		return result;
	}
}