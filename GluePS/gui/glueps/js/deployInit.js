dojo.registerModulePath("glueps.dojoi18n", "../../../dojoi18n");
//dojo.requireLocalization("glueps.dojoi18n", "glueps");


function init(){
    
	LdShakeManager.init();              
	var sectoken = gup("sectoken");
	if (sectoken!=""){
		LdShakeManager.ldShakeMode = true;
		LdShakeManager.sectoken = sectoken;
	}
	initDeploy();
}

function initDeploy(){ 
	
	/*dojo.connect(dojo.byId("saveButton"), "onclick", function(){
			var url = JsonDB.getDeploymentId() + "/save";
			if (LdShakeManager.ldShakeMode){
				var url = LdShakeManager.buildLdshakeUrl(url);
			}
			var bindArgs = {
				url : url,
				method: "PUT",
				handleAs : "json", // Manejamos la respuesta del get como un json
				headers : { // Indicar en la cabecera que es json
					"Content-Type" : "application/json",
					"Accept" : "application/json"
				},         
				load : function(data) {
					alert("saved");
				},
				error : function(error, ioargs) {
					var message = "";
					message = ErrorCodes.errores(ioargs.xhr.status);
					InformativeDialogs.showAlertDialog(i18n.get("warning"), message);
				}
			}
			dojo.xhrPut(bindArgs);
	});
	
	dojo.connect(dojo.byId("cancelButton"), "onclick", function(){
		var url = JsonDB.getDeploymentId() + "/cancel";
		if (LdShakeManager.ldShakeMode){
			var url = LdShakeManager.buildLdshakeUrl(url);
		}
		var bindArgs = {
			url : url,
			method: "PUT",
			handleAs : "json", // Manejamos la respuesta del get como un json
			headers : { // Indicar en la cabecera que es json
				"Content-Type" : "application/json",
				"Accept" : "application/json"
			},         
			load : function(data) {
            	alert("cancelled");
			},
			error : function(error, ioargs) {
				var message = "";
				message = ErrorCodes.errores(ioargs.xhr.status);
				InformativeDialogs.showAlertDialog(i18n.get("warning"), message);
			}
		}
		dojo.xhrPut(bindArgs);
	});
	
	dojo.connect(dojo.byId("saveAndExitButton"), "onclick", function(){
		var url = JsonDB.getDeploymentId() + "/saveAndExit";
		if (LdShakeManager.ldShakeMode){
			var url = LdShakeManager.buildLdshakeUrl(url);
		}
		var bindArgs = {
			url : url,
			method: "PUT",
			handleAs : "json", // Manejamos la respuesta del get como un json
			headers : { // Indicar en la cabecera que es json
				"Content-Type" : "application/json",
				"Accept" : "application/json"
			},         
			load : function(data) {
            	alert("saved and exit");
			},
			error : function(error, ioargs) {
				var message = "";
				message = ErrorCodes.errores(ioargs.xhr.status);
				InformativeDialogs.showAlertDialog(i18n.get("warning"), message);
			}
		}
		dojo.xhrPut(bindArgs);
	});*/
	
	dojo.connect(dojo.byId("homeImg"), "onclick", function() {
		window.location = "index.html";
	});
	
	dojo.connect(dojo.byId("exitImg"), "onclick", function() {
		var baseUrl = window.location.href.split("/GLUEPSManager")[0];
		var bindArgs = {
				url : baseUrl + "/GLUEPSManager/logout",
			    headers: {
			        "Content-Type": "text/plain",
			        "Authorization": "Basic bG9nb3V0OmxvZ291dA==" //logout:logout codificado directamente en base64.
			      },
				load: function(data)
				{
					InformativeDialogs.showLogoutDialog(i18n.get("logout"), i18n.get("logoutError"));
				},
				error : function(error, ioargs) {
					var baseUrl = window.location.href.split("/GLUEPSManager")[0];
					var bindArgs = {
							url : baseUrl + "/GLUEPSManager/logout",
						    user: "logout",
						    password: "logout",
							load: function(data)
							{
								InformativeDialogs.showLogoutDialog(i18n.get("logout"), i18n.get("logoutError"));
							},
							error : function(error, ioargs) {
								InformativeDialogs.showLogoutDialog(i18n.get("logout"), i18n.get("logoutOk"));	
							}
					}
					var xhr = dojo.xhrGet(bindArgs);					
				}
		}
		var xhr = dojo.xhrGet(bindArgs);
			
	});
	
	var deployId = gup('deployId');
	var urlDeploy = buildDeployUrl(window.location.href, deployId.split(".")[0]);
	//If we are in Ldshakemode, we append the sectoken to the url
	if (LdShakeManager.ldShakeMode){
		urlDeploy = LdShakeManager.buildLdshakeUrl(urlDeploy);
		//Hide the home and logout buttons
		dojo.style(dojo.byId("homeImg"), {
			display : "none"
		});
		dojo.style(dojo.byId("exitImg"), {
			display : "none"
		});
		dojo.style(dojo.byId("savingGif"), {
			display : "none"
		});
	                  
	    //Listen to the message event from LdShake to change the title of the design 
	    window.addEventListener('message', LdShakeManager.setTitle, false);   
	    window.addEventListener('message', LdShakeManager.saveDeploy, false); 
	}

	GFXTooltip.init();
	UndoManager.init();
	MenuManager.init();
	DeploymentPainter.init();
	DeployButtomPainter.init();
	GroupsPainter.init();
	InformativeDialogs.init();
	i18n.init();
	JsonDB.init(urlDeploy),
	StateManager.init(),
	ToolInstanceConfiguration.init();
	DeploymentController.init();
	
	GroupDialog.init();
	EditGroupDialog.init();
	ResourceDialog.init();
	EditResourceDialog.init();
	EditActivityDialog.init();
	CreateActivityDialog.init();
	PeerReviewActivityDialog.init();
	DeleteParticipantDialog.init();
	ParticipantManagementDialog.init();
	RedeploymentDialog.init();
	ActivityMenu.init();
	ActivityGroupMenu.init();
	ActivityResourceMenu.init();
	GroupMenu.init();
	ResourceMenu.init();
	ToolMenu.init();
	ActivityToolInstanceMenu.init();
	MessageManager.init();
}

function gup (name) {
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var regexS = "[\\?&]" + name + "=([^&#]*)";
	var regex = new RegExp(regexS);
	var results = regex.exec(window.location.href);
	if (results == null)
		return "";
	else
		return results[1];
}

function gupUrl (url, name) {
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var regexS = "[\\?&]" + name + "=([^&#]*)";
	var regex = new RegExp(regexS);
	var results = regex.exec(url);
	if (results == null)
		return "";
	else
		return results[1];
}

function buildDeployUrl(baseUrl, id) {
	var prefix = baseUrl.substring(0, baseUrl.lastIndexOf("/GLUEPSManager/")+15);
	return prefix+"deploys/"+id;
}

dojo.addOnLoad(init);
