/**
 * Oauth Manager responsible for making the requests that require authentication using Oauth 
 */
var OauthManager = {
		
	    oauthRequired:function(tokenId) {
	    	var required = false;
	    	var baseUrl = window.location.href.split("/GLUEPSManager")[0];
			if (typeof (LdShakeManager)!= "undefined" && LdShakeManager.ldShakeMode){
				var baseUrl = LdShakeManager.buildLdshakeUrl(baseUrl);
			}
	        var xhrArgs = {
	            url: baseUrl + "/GLUEPSManager/oauth",
	            handleAs: "text",//Tipo de dato de la respuesta del Get
	            content: {
	            	checktoken: tokenId
	            },
	            sync: true,
	            load: function(data) {    
	            	required = false;
	            },
	            error: function(error, ioargs) {  
	            	required = true;
	            }
	        }
	        //Call the asynchronous xhrGet
	        var deferred = dojo.xhrGet(xhrArgs);
	        return required;
	    },
		
		startOauth: function(parameters){
		 	OauthManager.getOauthUrl(parameters);
	 	},
	    
	    /**
	     * Get the URL to use when authenticating the user
	     */
	    getOauthUrl: function(parameters){
	    	var baseUrl = window.location.href.split("/GLUEPSManager")[0];
			if (typeof (LdShakeManager)!= "undefined" && LdShakeManager.ldShakeMode){
				var baseUrl = LdShakeManager.buildLdshakeUrl(baseUrl);
			}
	    	var redirectUri = baseUrl + "/GLUEPSManager/gui/glueps/oauth.html";
	        var xhrArgs = {
	            url: baseUrl + "/GLUEPSManager/oauth",
	            handleAs: "text",//Tipo de dato de la respuesta del Get
	            content: {
	            	redirectUri: redirectUri,
	            	parameters: parameters
	            },
	            load: function(data) {
	            	OauthManager.openOauthWindow(data);
	            },
	            error: function(error, ioargs) {  
	            	alert("An error has occurred while getting the oauth URL");
	            }
	        }
	        //Call the asynchronous xhrGet
	        var deferred = dojo.xhrGet(xhrArgs);
	    },
	    
	    /**
	     * Open the URL used to handle the authentication and consent of the user using Oauth 2.0. As a result of the process,
	     * an authorization code is returned in the title bar of the browser
	     */
	    openOauthWindow: function(oAuthURL){
	    	var win = window.open(oAuthURL, 'User\'s authentication and consent', 'width=600,height=500,modal=yes,alwaysRaised=yes');
	    },
	    
	    /**
	     * Provides the authorization code in order to get the access token, which is returned as part of the LearningEnvironment info
	     * @param code the authorization code for Oauth
	     * @param redirectUri The redirect URI obtained from the Developers Console
	     */
	    getOauthLogin:function(code,redirectUri, parameters) {
	    	var baseUrl = window.location.href.split("/GLUEPSManager")[0];
			if (LdShakeManager.ldShakeMode){
				var baseUrl = LdShakeManager.buildLdshakeUrl(baseUrl);
			}
	        var xhrArgs = {
	            url: baseUrl + "/GLUEPSManager/oauth",
	            handleAs: "json",//Tipo de dato de la respuesta del Get
	            content: {
	            	code: code,
	            	redirectUri: redirectUri,
	            	parameters: parameters
	            },
	            load: function(data, ioargs) {    
	            	//Go on with the deployment process
	            	if (gupUrl(ioargs.args.content.parameters,"isNewDeploy")=="true"){
	            		var isNewDeploy = true;
	            	}else{
	            		var isNewDeploy = false;
	            	}
	            	if (gupUrl(ioargs.args.content.parameters,"isRedeploy")=="true"){
	            		var isRedeploy = true; 
	            	}else{
	            		var isRedeploy = false;
	            	}
	            	DeploymentController.completeDeployment(isNewDeploy, isRedeploy);
	            },
	            error: function(error, ioargs) {  
	            	alert("An error has occurred in the authentication process. Please, retry it again");
	            }
	        }
	        //Call the asynchronous xhrGet
	        var deferred = dojo.xhrGet(xhrArgs);
	    },
	    
	    /**
	     * Provides the authorization code in order to get the access token, which is returned as part of the LearningEnvironment info
	     * @param code the authorization code for Oauth
	     * @param redirectUri The redirect URI obtained from the Developers Console
	     */
	    getOauthCredentials:function(code,redirectUri,parameters) {
	    	var baseUrl = window.location.href.split("/GLUEPSManager")[0];
	        var xhrArgs = {
	            url: baseUrl + "/GLUEPSManager/oauth",
	            handleAs: "json",//Tipo de dato de la respuesta del Get
	            content: {
	            	code: code,
	            	redirectUri: redirectUri,
	            	parameters: parameters
	            },
	            load: function(data) {    
	            	//set the input values of the new le dialog
	        		dijit.byId("dialogNewLeUser").setValue(data.userid);
	        		dijit.byId("dialogNewLePassword").setValue(data.token_type + " " + data.access_token);
	            },
	            error: function(error, ioargs) {  
	            	alert("An error has occurred in the authentication process.");
	            }
	        }
	        //Call the asynchronous xhrGet
	        var deferred = dojo.xhrGet(xhrArgs);
	    },
	    
	    /**
	     * Provides the authorization code in order to get the access token, which is returned as part of the LearningEnvironment info
	     * @param code the authorization code for Oauth
	     * @param redirectUri The redirect URI obtained from the Developers Console
	     */
	    getVleCourses:function(code,redirectUri, parameters) {
	    	var baseUrl = window.location.href.split("/GLUEPSManager")[0];
	        var xhrArgs = {
	            url: baseUrl + "/GLUEPSManager/oauth",
	            handleAs: "json",//Tipo de dato de la respuesta del Get
	            content: {
	            	code: code,
	            	redirectUri: redirectUri,
	            	parameters: parameters
	            },
	            load: function(data) {
	            	//Get the courses for the vle once the access token is available 
    	            ImportDeploy.resetSelectClassVle();
    	            ImportDeploy.checkGetClassesVle();
	            },
	            error: function(error, ioargs) {  
	            	alert("An error has occurred in the authentication process.");
	            }
	        }
	        //Call the asynchronous xhrGet
	        var deferred = dojo.xhrGet(xhrArgs);
	    },
	    
	    getNewDeployCourses:function(code,redirectUri,parameters) {
	    	var baseUrl = window.location.href.split("/GLUEPSManager")[0];
	        var xhrArgs = {
	            url: baseUrl + "/GLUEPSManager/oauth",
	            handleAs: "json",//Tipo de dato de la respuesta del Get
	            content: {
	            	code: code,
	            	redirectUri: redirectUri,
	            	parameters: parameters
	            },
	            load: function(data) {    
	            	//Get the courses for the vle once the access token is available when creating a new deploy
	    	    	Deploy.resetSelectClassLms();
	    	    	Deploy.checkGetClassesLms();
	            },
	            error: function(error, ioargs) {  
	            	alert("An error has occurred in the authentication process.");
	            }
	        }
	        //Call the asynchronous xhrGet
	        var deferred = dojo.xhrGet(xhrArgs);
	    },
	    
}