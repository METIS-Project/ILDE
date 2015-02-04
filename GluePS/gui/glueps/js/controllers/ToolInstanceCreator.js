/**
 * 
 */
var ToolInstanceCreator = {	
		
		reset: function(){	
			ToolInstanceCreator.toolInstancesCreate = 0;
			ToolInstanceCreator.toolInstancesCreateOk = 0;
			ToolInstanceCreator.toolInstancesCreateError = 0;
		},
		
		
		setTimeout: function(){
	    	//Establecer el valor del timeout
			ToolInstanceCreator.timeout = 60000;
	    	if (ToolInstanceCreator.toolInstancesCreate * 10000 > 300000)
	    	{
	    		ToolInstanceCreator.timeout = 300000;
	    	}
	    	else{
	    		if (ToolInstanceCreator.toolInstancesCreate * 10000 > 60000)
	    		{
	    			ToolInstanceCreator.timeout = ToolInstanceCreator.toolInstancesCreate * 10000;
	    		}
	    	}
		},
		
		startToolInstanceCreation: function(toolInstancesCreate)
		{
				
			this.reset();
				
			ToolInstanceCreator.toolInstancesCreate = toolInstancesCreate.length;
				
			this.setTimeout();
				
				
			if (ToolInstanceCreator.toolInstancesCreate == 1){
				InformativeDialogs.showLoadingDialog(i18n.get("creatingToolInstance"));
			}else{
			    var info = i18n.get("deployingCreateInfo") + "<div style='padding:15px'>" + i18n.get("toolInstancesCreate") + ToolInstanceCreator.toolInstancesCreate;
			    info = info + "<br />" + i18n.get("toolInstancesCreatedOk") + ToolInstanceCreator.toolInstancesCreateOk + "<br />" + i18n.get("toolInstancesCreatedError") + ToolInstanceCreator.toolInstancesCreateError + "</div>";
			    InformativeDialogs.showLoadingDialog(info);
			}
		    	
				
			//Here we have to add the code to create multiple instances of the same tool
		    for ( var t = 0; t < toolInstancesCreate.length; t++) {
		        ToolInstanceCreator.createToolInstance(toolInstancesCreate[t]);
		    }
			
		},
		
		/**
		 * Creaci�n de una instancia de herramienta
		 * 
		 * @param toolInstanceObj Objeto con informaci�n de la instancia de herramienta a crear
		 */
		createToolInstance : function(toolInstanceObj) {
			var toolInstance = toolInstanceObj.toolInstance;
			// Guardo el contenido del formulario.
			var configuration = ToolInstanceConfiguration.getFormContent();

			// Guardar el nombre elegido para la instancia de herramienta
			if (toolInstanceObj.isCopyConf==false){
				toolInstance.setName(configuration["titleVle"]);
			}
			//Se ha asignado previamente el nombre a la herramienta, si es necesario cambiarlo
			
			// Juan: Guardar la posición AR de la instancia de herramienta
			if (LearningEnvironment.getShowAr()) {
				if (toolInstanceObj.isCopyConf == false){
					if(dijit.byId("ptNone").attr('value') !== false){
						toolInstance.deletePosition();
						toolInstance.deleteMaxdistance();
						toolInstance.deletePositionType();
						toolInstance.deleteScale();
				//		toolInstance.deleteOrientation();
					}
					if(dijit.byId("ptGeo").attr('value') !== false){
						toolInstance.setPosition(configuration["geoposition"]);
						toolInstance.setMaxdistance(configuration["maxdistance"]);
						toolInstance.setPositionType(configuration["positionType"]);
						toolInstance.setScale(configuration["geoScale"]);
				//		toolInstance.setOrientation(configuration["geoOrientation"]);
						
	
					}
					if(dijit.byId("ptQr").attr('value') !== false){
						toolInstance.deletePosition();
						toolInstance.deleteMaxdistance();
						toolInstance.setPositionType(configuration["positionType"]);
						toolInstance.deleteScale();
				//		toolInstance.deleteOrientation();
					}
					if(dijit.byId("ptMarker").attr('value') !== false) {
						var select = dojo.byId("selectMarker");
						if (select && select.options[select.selectedIndex].value != "0") {
							var newMarkerId = select.options[select.selectedIndex].value;
							toolInstance.setPosition(newMarkerId);				
						}
						toolInstance.deleteMaxdistance();
						toolInstance.setPositionType(configuration["positionType"]);
						toolInstance.setScale(configuration["junaiomarkerScale"]);
				//		toolInstance.setOrientation(configuration["junaiomarkerOrientation"]);
					}
				}else{
					if(dijit.byId("ptNone").attr('value') !== false){
						toolInstance.deletePosition();
						toolInstance.deleteMaxdistance();
						toolInstance.deletePositionType();
						toolInstance.deleteScale();
						//toolInstance.deleteOrientation();
					}
					//Si se est� aplicando la configuraci�n de otra instancia s�lo se almacena si es geoposici�n
					if(dijit.byId("ptGeo").attr('value') !== false){
						toolInstance.setPosition(configuration["geoposition"]);
						toolInstance.setMaxdistance(configuration["maxdistance"]);
						toolInstance.setPositionType(configuration["positionType"]);
						toolInstance.setScale(configuration["geoScale"]);
						//toolInstance.setOrientation(configuration["geoOrientation"]);
					}
					if(dijit.byId("ptQr").attr('value') !== false){
						toolInstance.deletePosition();
						toolInstance.deleteMaxdistance();
						toolInstance.setPositionType(configuration["positionType"]);
						toolInstance.deleteScale();
				//		toolInstance.deleteOrientation();
					}
			
			
					if(dijit.byId("ptMarker").attr('value') !== false && toolInstanceObj.markerCopiable) {
						var select = dojo.byId("selectMarker");
						if (select && select.options[select.selectedIndex].value != "0") {
							var newMarkerId = select.options[select.selectedIndex].value;
							toolInstance.setPosition(newMarkerId);				
						}
						toolInstance.deleteMaxdistance();
						toolInstance.setPositionType(configuration["positionType"]);
						toolInstance.setScale(configuration["junaiomarkerScale"]);
						//toolInstance.setOrientation(configuration["junaiomarkerOrientation"]);
					}
				}
			}


			
			// Creo el xforms que voy a mandar al servicio REST de Glueps
			var xformTool = parserXforms.buildXFormsInstance();

			// Comprobar si existe campo de subir fichero
			if (configuration["nameFile"]) {
				// Convertir formulario para poder subir ficheros
				var formConf = dojo.byId("formToolConfiguration");
				formConf.setAttribute("enctype", "multipart/form-data");
				formConf.setAttribute("method", "POST");
				var root = dojox.xml.parser.parse(xformTool);
				ToolInstanceCreator.setFilename(configuration["nameFile"], root);
				ToolInstanceCreator.setMediatype(configuration["nameFile"], root);
				ToolInstanceCreator.convertFile(toolInstance, root);
			} else {
				ToolInstanceCreator.putCreateToolInstance(toolInstance, xformTool);
			}
			
			
		},
		

		/**
		 * Realiza el PUT para la creaci�n de la instancia de la herramienta
		 * enviando el xform con la informaci�n proporcionada
		 * 
		 * @param id
		 *            Identificador del icono de la herramienta
		 * @param xformTool
		 *            xform cuyo contenido se desea enviar
		 */
		putCreateToolInstance : function(toolInstance, xformTool) {
			var url = toolInstance.getId();
			if (LdShakeManager.ldShakeMode){
				var url = LdShakeManager.buildLdshakeUrl(url);
			}
			// Pongo en marcha gif animado mientras se procesa el put
			var xhrArgs = {
				url : url,
				method : "PUT",
				putData : xformTool,
		//		timeout : 10000,  // Juan: Se ampl�a para que le d� tiempo al 3d model converter a finalizar la conversi�n
				timeout : 30000,
				handleAs : "text",
				headers : {
					"Content-Type" : "text/xml",
					"Content-Encoding" : "UTF-8"
				},
				load : function(data) {
					
	            	ToolInstanceCreator.toolInstancesCreateOk++;
	            	
	            	if (ToolInstanceCreator.toolInstancesCreate == 1){
	    				InformativeDialogs.hideLoadingDialog();
	    				InformativeDialogs.showAlertDialog(i18n.get("info"), i18n.get("createdToolInstance"));
	    				var location = ToolInstanceReuse.getLocation(data);
	    				toolInstance.setLocation(location);
	    				//JUAN: a�adido para poner el position si positionType es "qrcode"
	    				if (toolInstance.getPositionType() == "qrcode"){
		    				var position = "http://qrickit.com/api/qr?qrsize=200&d=" + location + "?callerUser=";
		    				toolInstance.setPosition(position);
	    				}
	    				JsonDB.notifyChanges();
	            	}
	            	
	            	else{					
		    	    	var info = i18n.get("deployingCreateInfo") + "<div style='padding:15px'>" + i18n.get("toolInstancesCreate") + ToolInstanceCreator.toolInstancesCreate;
		    	    	info = info + "<br />" + i18n.get("toolInstancesCreatedOk") + ToolInstanceCreator.toolInstancesCreateOk + "<br />" + i18n.get("toolInstancesCreatedError") + ToolInstanceCreator.toolInstancesCreateError + "</div>";
		    	    	InformativeDialogs.setInfoLoadingDialog(info);
		    	    	
		                var location = ToolInstanceReuse.getLocation(data);
		                toolInstance.setLocation(location);
	    				//JUAN: a�adido para poner el position si positionType es "qrcode"
	    				if (toolInstance.getPositionType() == "qrcode"){
		    				var position = "http://qrickit.com/api/qr?qrsize=200&d=" + location + "?callerUser=";
		    				toolInstance.setPosition(position);
	    				}
		                
		                if (ToolInstanceCreator.toolInstancesCreateOk + ToolInstanceCreator.toolInstancesCreateError == ToolInstanceCreator.toolInstancesCreate){
		                		InformativeDialogs.hideLoadingDialog();
		                		if (ToolInstanceCreator.toolInstancesCreateOk == ToolInstanceCreator.toolInstancesCreate){
		            				InformativeDialogs.showAlertDialog(i18n.get("info"), i18n.get("createdAllToolInstance"));
		                		}
		                		else{
		                			InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("notCreatedAllToolInstance"));
		                		}
		                		JsonDB.notifyChanges();
		                }
	            	}
				},
				error : function() {
					
					ToolInstanceCreator.toolInstancesCreateError++;

					if (ToolInstanceCreator.toolInstancesCreate == 1){
						InformativeDialogs.hideLoadingDialog();
						InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("notCreatedToolInstance"));
					}
					
					else{
		    	    	var info = i18n.get("deployingCreateInfo") + "<div style='padding:15px'>" + i18n.get("toolInstancesCreate") + ToolInstanceCreator.toolInstancesCreate;
		    	    	info = info + "<br />" + i18n.get("toolInstancesCreatedOk") + ToolInstanceCreator.toolInstancesCreateOk + "<br />" + i18n.get("toolInstancesCreatedError") + ToolInstanceCreator.toolInstancesCreateError + "</div>";	    	    	
		            	InformativeDialogs.setInfoLoadingDialog(info);
		            	
		                if (ToolInstanceCreator.toolInstancesCreateOk + ToolInstanceCreator.toolInstancesCreateError == ToolInstanceCreator.toolInstancesCreate){
		    				InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("notCreatedAllToolInstance"));
		                	InformativeDialogs.hideLoadingDialog();
		                	JsonDB.notifyChanges();
		                }
					}	                
				}
			}
			var deferred = dojo.xhrPut(xhrArgs);
		},		
		
		
		setFilename: function(name, root)
		{
			var name = name.substring(name.lastIndexOf('\\')+1);
			var cadena = "ins:file";
			if (root.getElementsByTagName("ins:file").length == 0) {
				cadena = "file";
			}
			var nodoFile = root.getElementsByTagName(cadena)[0];
			nodoFile.setAttribute("ins:filename", name);
		},
		
		setMediatype: function(name, root)
		{
			var ext = name.substring(name.lastIndexOf('.')+1);
			var cadena = "ins:file";
			if (root.getElementsByTagName("ins:file").length == 0) {
				cadena = "file";
			}
			var nodoFile = root.getElementsByTagName(cadena)[0];
			var mediatype;
			switch(ext)
			{
			case('txt'): {
				mediatype = "text/plain";
				break;
				}
			case('rtf'):{
				mediatype = "application/rtf";
				break;
				}
			case('doc'):{
				mediatype = "application/msword";
				break;
			}
			case('docx'):{
				mediatype = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
				break;
			}
			case('xls'):{
				mediatype = "application/vnd.ms-excel";
				break;
			}
			case('xlsx'):{
				mediatype = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
				break;
			}
			case('ppt'):{
				mediatype = "application/vnd.ms-powerpoint";
				break;
			}
			case('pptx'):{
				mediatype = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
				break;
			}
			case('png'):{
				mediatype = "image/png";
				break;
			}
			case('jpg'):{
				mediatype = "image/jpg";
				break;
			}
			default:{
				mediatype = "";
			}
			}
			nodoFile.setAttribute("ins:mediatype", mediatype);
		},

		convertFile : function(toolInstance, root) {
			var cadena = "ins:file";
			if (root.getElementsByTagName("ins:file").length == 0) {
				cadena = "file";
			}
			var nodoFile = root.getElementsByTagName(cadena)[0];
			var type = nodoFile.getAttribute("xsi:type");
			switch (type) {
			case ("xs:base64Binary"): {
				ToolInstanceCreator.convertHexFile(toolInstance, root);
				break;
			}
			case ("xs:hexBinary"): {
				ToolInstanceCreator.convertHexFile(toolInstance, root);
				break;
			}
			default:{
				ToolInstanceCreator.convertHexFile(toolInstance, root);
				}
			}

		},

		/**
		 * Env�a el formulario con el fichero al servidor para que realice su conversi�n a binario codificado en hexadecimal
		 */
		convertHexFile : function(toolInstance, root) {
			var url = toolInstance.getId() + "/hexconv";
			if (LdShakeManager.ldShakeMode){
				var url = LdShakeManager.buildLdshakeUrl(url);
			}
			dojo.io.iframe.send({
				//url : toolInstance.getId() + "/hexconv",
				url: url,
				method : "POST",
				form : "formToolConfiguration",
				handleAs : "html",
				load : function(response, ioArgs) {
					var cadena = "ins:file";
					if (root.getElementsByTagName("ins:file").length == 0) {
						cadena = "file";
					}
					var nodoFile = root.getElementsByTagName(cadena)[0];
					// Asignar el resultado devuelto por el servidor como el
					// contenido del campo
					nodoFile.textContent = response.getElementsByTagName("textarea")[0].innerHTML;
					// Parsear para tener el nuevo contenido a�adido
					xformTool = dojox.xml.parser.innerXML(root);
					ToolInstanceCreator.putCreateToolInstance(toolInstance, xformTool);
				},
				error : function(response, ioArgs) {
					// Error al obtener la conversi�n del fichero realizada por el servidor
					
					ToolInstanceCreator.toolInstancesCreateError++;
					
					if (ToolInstanceCreator.toolInstancesCreate == 1){
						
						InformativeDialogs.hideLoadingDialog();
						InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("notCreatedToolInstance"));
					}
					else{

		    	    	var info = i18n.get("deployingCreateInfo") + "<div style='padding:15px'>" + i18n.get("toolInstancesCreate") + ToolInstanceCreator.toolInstancesCreate;
		    	    	info = info + "<br />" + i18n.get("toolInstancesCreatedOk") + ToolInstanceCreator.toolInstancesCreateOk + "<br />" + i18n.get("toolInstancesCreatedError") + ToolInstanceCreator.toolInstancesCreateError + "</div>";	    	    	
		            	InformativeDialogs.setInfoLoadingDialog(info);
		            	
		                if (ToolInstanceCreator.toolInstancesCreateOk + ToolInstanceCreator.toolInstancesCreateError == ToolInstanceCreator.toolInstancesCreate){
		    				InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n.get("notCreatedAllToolInstance"));
		                	InformativeDialogs.hideLoadingDialog();
		                	JsonDB.notifyChanges();
		                }
					}
				}
			});
		}
   
}
