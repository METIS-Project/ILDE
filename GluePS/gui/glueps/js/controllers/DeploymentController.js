/**
 * Controller for the deployment process in the VLE by first creating the necessary tool instances
 */
var DeploymentController = {
		
		
		init: function(){
			dojo.connect(dojo.byId("deploymentButtonTop"), "onclick", function(){
				DeploymentController.deploy();
			});
			dojo.connect(dojo.byId("deploymentButtonBottom"), "onclick", function(){
				DeploymentController.deploy();
			});			
			
			dojo.connect(dojo.byId("deployVleOk"), "onclick", function(){
				DeploymentController.hideDeployVleDialog();
				//Mostrar también un mensaje informativo
				DeploymentPainter.showInformationCompleted();
			});
		},
		
		deploy: function(){
			/*var leValue = LearningEnvironment.getLearningEnvironmentId();
		    if (LearningEnvironment.getLearningEnvironmentType()=="Blogger" && OauthManager.oauthRequired(leValue)){
    			var callerMethod = "postdep";
    			var deployId = JsonDB.getDeploymentId();
    			//start the oauth process in order to have an updated access token which let us deploy the course
    			//we provide a name for the callerMethod and the id of the deploy
    			OauthManager.startOauth("callerMethod=" + callerMethod + "&leId=" + leValue);
	    	}else{
	    		DeploymentController.completeDeployment();
	    	}*/
		    if (LearningEnvironment.getLearningEnvironmentType()=="Blogger" && typeof JsonDB.deploy.liveDeployURL!="undefined"){
		    	RedeploymentDialog.showDialog();
	    	}else{
	    		DeploymentController.completeDeployment(false,false);
	    	}
		},		
		
		reset: function(){
			DeploymentController.finishedStatic = false;
			DeploymentController.finishedLive = false;
			DeploymentController.liveErrorMessage = "";
	    	DeploymentController.toolInstancesCreate = 0;
	    	DeploymentController.toolInstancesCreateOk = 0;
	    	DeploymentController.toolInstancesCreateError = 0;
	    	DeploymentController.isNewDeploy = false;
	    	DeploymentController.isRedeploy = false;
		},
		
		
		setTimeout: function(){
	    	//Establecer el valor del timeout
	    	DeploymentController.timeout = 60000;
	    	if (DeploymentController.toolInstancesCreate * 10000 > 300000)
	    	{
	    		DeploymentController.timeout = 300000;
	    	}
	    	else{
	    		if (DeploymentController.toolInstancesCreate * 10000 > 60000)
	    		{
	    			DeploymentController.timeout = DeploymentController.toolInstancesCreate * 10000;
	    		}
	    	}
		},
		
	    completeDeployment : function(isNewDeploy, isRedeploy) { 
	    	
	    	var toolInstancesCreate = new Array();
	    	var toolInstances = ToolInstanceContainer.getAllToolInstances();
	    	for ( var i=0; i < toolInstances.length; i++)
	    	{
	    		var tool = toolInstances[i].getTool();
	    		//Se debe de crear la instancia para aquellas instancias de herramientas externas que no han sido configuradas
	    		if (tool.getToolKind()=="external" && !toolInstances[i].getLocation())
	    		{
	    			toolInstancesCreate.push(toolInstances[i]);
	    		}
	    	}
	    	
	    	this.reset();
			if (typeof isNewDeploy!="undefined" && isNewDeploy==true){
				DeploymentController.isNewDeploy = true;
			}else if (typeof isRedeploy!="undefined" && isRedeploy==true){
				DeploymentController.isRedeploy = true;
			}
	    	
	    	DeploymentController.toolInstancesCreate = toolInstancesCreate.length;
	    	
	    	this.setTimeout();	    	
	    	
	    	var info = i18n.get("deployingWait") + "<br /><br />" + i18n.get("deployingCreateInfo") + "<div style='padding:15px'>" + i18n.get("toolInstancesCreate") + DeploymentController.toolInstancesCreate;
	    	if (DeploymentController.toolInstancesCreate > 0){
	    		info = info + "<br />" + i18n.get("toolInstancesCreatedOk") + DeploymentController.toolInstancesCreateOk + "<br />" + i18n.get("toolInstancesCreatedError") + DeploymentController.toolInstancesCreateError + "</div>";
	    		InformativeDialogs.showLoadingDialog(info, i18n.get("deploying"));
	    	}
	    	else{
	    		info = info + "</div>" + i18n.get("deployingCourseInfo");
	    		InformativeDialogs.showLoadingDialog(info, i18n.get("deploying"));
	    		InformativeDialogs.setInfoLoadingDialog(info, false);
	    	}
	        for ( var t = 0; t < toolInstancesCreate.length; t++) {
	        	DeploymentController.toolConfigurationDefault(toolInstancesCreate[t]);
	        }
	        //Si no es necesario crear instancias de herramientas se muestra directamente la última pantalla
			if (toolInstancesCreate.length == 0) {
				
				if (JsonDB.getDeployURL()==false)
				{
					//Añade la url del despliegue
					var idDeploy = JsonDB.getDeploymentId().split("/GLUEPSManager/deploys/")[1];
					var url = window.location.href.split("/GLUEPSManager")[0];
					var url = url + "/GLUEPSManager/deploys/" + idDeploy + ".zip";
					JsonDB.setDeployURL(url);
				}                
							                
				//We generate both static and dynamic courses, if possible
				//We try the PUT with both static and dynamic deploys (sets the deploy URL and stores updated fileDeploy if correct)				
				DeploymentController.putStaticDeploy();
				DeploymentController.putLiveDeploy();
			}
	    },
	    
	    /**
		 * Configuración de una herramienta con los campos por defecto
		 */
	    toolConfigurationDefault : function(toolInstance) {	
	    	var tool = toolInstance.getTool();
	    	var toolType = tool.getToolType();
	    	var toolKind = tool.getToolKind();
	    	var vleType = JsonDB.deploy.learningEnvironment.type;
			if (toolType.indexOf("/",0)==-1){
				var baseUrl = window.location.href.split("/GLUEPSManager")[0];
				var url = baseUrl + "/GLUEPSManager/tools?id=" + toolType + "&toolKind=" + toolKind + "&vleType=" + vleType;
			}
			else{
				var url = tool.getToolType() + "/configuration";
			}
			if (LdShakeManager.ldShakeMode){
				var url = LdShakeManager.buildLdshakeUrl(url);
			}
	        var xhrArgs = {
	            url : url,
	            timeout : DeploymentController.timeout, // Tiempo máximo de espera
	            handleAs : "text",// Tipo de dato de la respuesta del Get
	            load : function(data) {	
	                parserXforms.data = data;
	                parserXforms.printformfields();
	                var divToolConfiguration = document.getElementById("divToolConfiguration");
	                // Se borra todo lo que tenga el div (el formulario)
	                divToolConfiguration.removeChild(document.getElementById("formToolConfiguration"));
	                //Se crea el formulario
	                var formConfiguration = document.createElement("form");
	                formConfiguration.setAttribute("id", "formToolConfiguration");
	                divToolConfiguration.appendChild(formConfiguration);
	                
            		var divCI = formConfiguration.appendChild(document.createElement("div"));
            		divCI.setAttribute("id", "divCI");
            		formConfiguration.appendChild(divCI);
                    // Añade cada uno de los campos del formulario
                    for ( var i = 0; i < parserXforms.camposFormulario.length; i++) {
                    	divCI.appendChild(parserXforms.camposFormulario[i]);
                    }
                    
        	        // Creo el xforms que voy a mandar al servicio REST de Glueps
        	        var xformTool = parserXforms.buildXFormsInstance();
        	        DeploymentController.putCreateToolInstance(toolInstance, xformTool);
	            },

	            error : function(error, ioargs) {
	            	DeploymentController.toolInstancesCreateError++;
	            	
	    	    	var info = i18n.get("deployingWait") + "<br /><br />" + i18n.get("deployingCreateInfo") + "<div style='padding:15px'>" + i18n.get("toolInstancesCreate") + DeploymentController.toolInstancesCreate;
	    	    	info = info + "<br />" + i18n.get("toolInstancesCreatedOk") + DeploymentController.toolInstancesCreateOk + "<br />" + i18n.get("toolInstancesCreatedError") + DeploymentController.toolInstancesCreateError + "</div>";
	    	    	
	            	InformativeDialogs.setInfoLoadingDialog(info);
	                if (DeploymentController.toolInstancesCreateOk + DeploymentController.toolInstancesCreateError == DeploymentController.toolInstancesCreate){                	
	                	InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("deploymentError"));
	                	InformativeDialogs.hideLoadingDialog();
	                	JsonDB.notifyChanges();
	                }
	            }
	        }

	        // Call the asynchronous xhrGet
	        var deferred = dojo.xhrGet(xhrArgs);
	    },
	    
	    /**
		 *  Realiza el PUT para la creación de la instancia de la herramienta enviando el xform con la información proporcionada
		 */
	    putCreateToolInstance : function(toolInstance, xformTool) {
			var url = toolInstance.getId();
			if (LdShakeManager.ldShakeMode){
				var url = LdShakeManager.buildLdshakeUrl(url);
			}
	        var xhrArgs = {
	            url : url,
	            method : "PUT",
	            putData : xformTool,
	            timeout : DeploymentController.timeout, // Tiempo máximo de espera
	            handleAs : "text",
	            headers : {
	                "Content-Type" : "text/xml",
	                "Content-Encoding" : "UTF-8"
	            },
	            load : function(data) {
	            	DeploymentController.toolInstancesCreateOk++;
	            	
	    	    	var info = i18n.get("deployingWait") + "<br /><br />" + i18n.get("deployingCreateInfo") + "<div style='padding:15px'>" + i18n.get("toolInstancesCreate") + DeploymentController.toolInstancesCreate;
	    	    	info = info + "<br />" + i18n.get("toolInstancesCreatedOk") + DeploymentController.toolInstancesCreateOk + "<br />" + i18n.get("toolInstancesCreatedError") + DeploymentController.toolInstancesCreateError + "</div>";
	    	    	if (DeploymentController.toolInstancesCreate==DeploymentController.toolInstancesCreateOk){
	    	    		info = info + "</div>" + i18n.get("deployingCourseInfo");
		            	InformativeDialogs.setInfoLoadingDialog(info, false);
	    	    	}
	    	    	else{
	    	    		InformativeDialogs.setInfoLoadingDialog(info);
	    	    	}
	    	    	
	                var location = ToolInstanceReuse.getLocation(data);
	                toolInstance.setLocation(location);
	                if (DeploymentController.toolInstancesCreate==DeploymentController.toolInstancesCreateOk){
						//Añade la url del despliegue
						var idDeploy = JsonDB.getDeploymentId().split("/GLUEPSManager/deploys/")[1];
						var url = window.location.href.split("/GLUEPSManager")[0];
						var url = url + "/GLUEPSManager/deploys/" + idDeploy + ".zip";
						JsonDB.setDeployURL(url);                 
	            	   	//We generate both static and dynamic courses, if possible
	            	   	//We try the PUT with both static and dynamic deploys (sets the deploy URL and stores updated fileDeploy if correct)
						DeploymentController.putStaticDeploy();
						DeploymentController.putLiveDeploy();      	   	
	                }
	                else{
	                	if (DeploymentController.toolInstancesCreateOk + DeploymentController.toolInstancesCreateError == DeploymentController.toolInstancesCreate){
	                		InformativeDialogs.hideLoadingDialog();
	                		InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("deploymentError"));
	                		JsonDB.notifyChanges();
	                	}
	                }
	            },
	            error : function(error, ioargs) {
	            	DeploymentController.toolInstancesCreateError++;

	    	    	var info = i18n.get("deployingWait") + "<br /><br />" + i18n.get("deployingCreateInfo") + "<div style='padding:15px'>" + i18n.get("toolInstancesCreate") + DeploymentController.toolInstancesCreate;
	    	    	info = info + "<br />" + i18n.get("toolInstancesCreatedOk") + DeploymentController.toolInstancesCreateOk + "<br />" + i18n.get("toolInstancesCreatedError") + DeploymentController.toolInstancesCreateError + "</div>";
	    	    	
	            	InformativeDialogs.setInfoLoadingDialog(info);
	                if (DeploymentController.toolInstancesCreateOk + DeploymentController.toolInstancesCreateError == DeploymentController.toolInstancesCreate){
	                	InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("deploymentError"));
	                	InformativeDialogs.hideLoadingDialog();
	                	JsonDB.notifyChanges();
	                }
	            }
	        }
	        var deferred = dojo.xhrPut(xhrArgs);
	    },
	    
		putStaticDeploy : function() {
			var url = JsonDB.getDeploymentId() + "/static";
			if (LdShakeManager.ldShakeMode){
				var url = LdShakeManager.buildLdshakeUrl(url);
			}
			var xhrArgs = {
				url : url,
				method : "PUT",
				putData : dojo.toJson(JsonDB.deploy),
				handleAs : "json",
				//sync: true,
				headers : {
	                "Content-Type" : "application/json"
				},
				load : function(data) {
					DeploymentController.finishedStatic = true;
					DeploymentController.checkFinished();
				},
				error : function(error, ioargs) {
					DeploymentController.finishedStatic = true;
					DeploymentController.checkFinished();
				}
			}
			var deferred = dojo.xhrPut(xhrArgs);
		},
		
		putLiveDeploy : function() {
			var url = JsonDB.getDeploymentId() + "/live";
			if (DeploymentController.isNewDeploy==true){
				url = url + "/newdeploy";
			}else if (DeploymentController.isRedeploy==true){
				url = url + "/redeploy";
			}
			if (LdShakeManager.ldShakeMode){
				var url = LdShakeManager.buildLdshakeUrl(url);
			}
			var xhrArgs = {
				url : url,
				method : "PUT",
				putData : dojo.toJson(JsonDB.deploy),
				handleAs : "json",
				sync : true,
				headers : {
	                "Content-Type" : "application/json"
				},
				load : function(data, ioargs) {
					//Se ha iniciado el proceso
					DeploymentController.getInprocess();
				},
				error : function(error, ioargs) {
					//Method not allowed -> the live deployment is not available
					if (ioargs.xhr.status == 405){
						DeploymentController.finishedLive = true;
						DeploymentController.checkFinished();
					}
					//There is already a live deploy process running for that deploy
					else if (ioargs.xhr.status == 503){
						DeploymentController.liveErrorMessage = i18n.get("deploymentErrorCourseInProcess");
						DeploymentController.finishedLive = true;
						DeploymentController.checkFinished();
					}else{
						//Another error in the deploy process
						DeploymentController.finishedLive = true;
						DeploymentController.checkFinished();
					}
				}
			}
			var deferred = dojo.xhrPut(xhrArgs);
		},
				
		getInprocess: function(){
			var url = JsonDB.getDeploymentId() + "/live";
			if (LdShakeManager.ldShakeMode){
				var url = LdShakeManager.buildLdshakeUrl(url);
			}
	        var xhrArgs = {
	            url : url,
	            //timeout : DeploymentController.timeout, // Tiempo máximo de espera
	            handleAs : "text",// Tipo de dato de la respuesta del Get,
	            sync: true,
	            load : function(data, ioargs) {			
	            	if (ioargs.xhr.status == 200)
	            	{
		            	//Proceso terminado correctamente
		    			DeploymentController.finishedLive = true;
		    			DeploymentController.checkFinished();
	            	}else{
	            		//Otro codigo en el proceso de despliegue que no esperamos
	            		DeploymentController.finishedLive = true;
	            		DeploymentController.checkFinished();
	            	}
	            },

	            error : function(error, ioargs) { 
	            	if (ioargs.xhr.status == 503 || error.dojoType=='cancel')
	            	{
	            		//Está en proceso
	            		//Esperar un tiempo en milisegundos y volver a realizar el GET
	            		window.setTimeout(function(){DeploymentController.getInprocess();}, 5000);
	            		//Mostrar mensaje con el porcentaje completado ¿Vendrá en data?
	            		
		    	    	/*var info = i18n.get("deployingWait") + "<br /><br />" + i18n.get("deployingCreateInfo") + "<div style='padding:15px'>" + i18n.get("toolInstancesCreate") + DeploymentController.toolInstancesCreate;
		    	    	info = info + "<br />" + i18n.get("toolInstancesCreatedOk") + DeploymentController.toolInstancesCreateOk + "<br />" + i18n.get("toolInstancesCreatedError") + DeploymentController.toolInstancesCreateError + "</div>";
		    	    	if (DeploymentController.toolInstancesCreate==DeploymentController.toolInstancesCreateOk){
		    	    		info = info + "</div>" + i18n.get("deployingCourseInfo") + data + "%completed";
		    	    	}*/
	            	}
	            	else
	            	{
	            		// user/password error
	            		if (ioargs.xhr.status == 403){
	            			DeploymentController.finishedLive = true;
	            			DeploymentController.liveErrorMessage = i18n.get("deploymentErrorCredentials");
	            			DeploymentController.checkFinished();
	            		}
	            		else{
		            		//Otro error en el proceso de despliegue
		            		DeploymentController.finishedLive = true;
		            		DeploymentController.checkFinished();
	            		}
	            	}
	            }
	        }
	        // Call the asynchronous xhrGet
	        var deferred = dojo.xhrGet(xhrArgs);			
		},
		
		/** 
		 *  Detiene el proceso el número de milisegundos indicados
		 */
		sleep: function(milisegundos) {
			var inicio = new Date().getTime();
			while ((new Date().getTime() - inicio) < milisegundos){}
		},

		getDeployFinished : function(urlDeploy) {
			var url = urlDeploy;
			if (LdShakeManager.ldShakeMode){
				var url = LdShakeManager.buildLdshakeUrl(url);
			}
			var bindArgs = {
				url : url,
				handleAs : "json", // Manejamos la respuesta del get como un json
				headers : { // Indicar en la cabecera que es json
					"Content-Type" : "application/json",
					"Accept" : "application/json"
				},
				load : function(data) {
					if (DeploymentController.liveErrorMessage!=""){
						InformativeDialogs.showAlertDialog(i18n.get("warning"), DeploymentController.liveErrorMessage);
					}else if ( (data.staticDeployURL || data.liveDeployURL) && data.inProcess==false) {
						JsonDB.deploy = data;
						DeploymentController.showDeployVleDialog();
						
						//Si estamos en modo LdShake notificar a este que el despliegue se ha completado
						if (LdShakeManager.ldShakeMode){
							LdShakeManager.postMessageDeploymentCompleted();
						}
					}else{
						InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("deploymentErrorProcess"));		
					}
					UndoManager.newEditionAction();
					StateManager.reset();
					
					DeploymentPainter.paint();
				},
				error : function(error, ioargs) {
				}
				
			}
			dojo.xhrGet(bindArgs);
		},
		
		/** 
		 *  Comprueba si se han completado los PUT de ambos tipos de despliegue y en caso afirmativo termina
		 */
		checkFinished: function(){
			if (DeploymentController.finishedStatic==true && DeploymentController.finishedLive==true)
			{	
				DeploymentController.sleep(3000);
				window.setTimeout(function(){
					InformativeDialogs.hideLoadingDialog();
					DeploymentController.getDeployFinished(JsonDB.getDeploymentId());
					}, 3000);		
			}
		},
		
		showDeployVleDialog: function(){
			
			var dlg = dijit.byId("deployVle");
			dojo.byId("deployVleInfo").innerHTML=i18n.get("deploymentOk");       
    		dojo.byId("staticDeployVleTextoEnlace").innerHTML=i18n.get("StaticDeployVleTextoEnlace");
     	  	dojo.byId("liveDeployVleTextoEnlace").innerHTML=i18n.get("LiveDeployVleTextoEnlace");  		   	    
    	    			
			// Si no hay error en el despliegue static se muestra el enlace
			if (JsonDB.deploy.staticDeployURL) {
				dojo.style(dojo.byId("staticDeployVleTextoEnlace"), {
					display : ""
				});
				// Muestro enlace
				dojo.style(dojo.byId("staticDeployLink"), {
					display : ""
				});
				// Pongo dirección al enlace para el zip del deploy
				var nodoEnlace = dojo.byId("staticDeployLink");
				var url = JsonDB.deploy.staticDeployURL;
				if (LdShakeManager.ldShakeMode){
					var url = LdShakeManager.buildLdshakeUrl(url);
				}
				nodoEnlace.setAttribute("href", url);
				nodoEnlace.childNodes[0].setAttribute("title", i18n.get("getDeployToolTip"));
			}
			else{
				dojo.style(dojo.byId("staticDeployVleTextoEnlace"), {
					display : "none"
				});
				// Muestro enlace
				dojo.style(dojo.byId("staticDeployLink"), {
					display : "none"
				});
			}

			// Si no hay error en el despliegue live se muestra el enlace
			if (JsonDB.deploy.liveDeployURL) {
				dojo.style(dojo.byId("liveDeployVleTextoEnlace"), {
					display : ""
				});
				// Muestro enlace
				dojo.style(dojo.byId("liveDeployLink"), {
					display : ""
				});
				// Pongo dirección al enlace para el zip del deploy
				var url = JsonDB.deploy.liveDeployURL;
				if (LdShakeManager.ldShakeMode){
					var url = LdShakeManager.buildLdshakeUrl(url);
				}
				var nodoEnlace = dojo.byId("liveDeployLink");
				nodoEnlace.setAttribute("href", url);
			}
			else{
				dojo.style(dojo.byId("liveDeployVleTextoEnlace"), {
					display : "none"
				});
				// Muestro enlace
				dojo.style(dojo.byId("liveDeployLink"), {
					display : "none"
				});
			}
			dijit.byId("deployVleOk").focus();			
			dlg.show();
		},
				
		hideDeployVleDialog: function(){
			var dlg = dijit.byId("deployVle");
			dlg.hide();
		}	    
}
