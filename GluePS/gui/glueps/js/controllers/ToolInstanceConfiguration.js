/**
 * 
 */
var ToolInstanceConfiguration = {

	init : function() {

		dojo.connect(dojo.byId("dialogConfirmDeleteToolInstanceOk"), "onclick",
				function() {
					var idToolInstance = dijit.byId("deleteToolInstanceId")
							.getValue();
					ToolInstanceConfiguration.deleteInstance(idToolInstance);
				});

		dojo.connect(dojo.byId("dialogConfirmDeleteToolInstanceCancel"),
				"onclick", function() {
					ToolInstanceConfiguration.hideDeleteToolInstance();
				});
	},

	internationalizeDialog : function() {
		dijit.byId("toolConfiguration").titleNode.innerHTML = i18n
				.get("toolConfigurationTitle");
	},

	toolInstanceConfiguration : function(toolInstance, position, activity, instancedActivity) {
		// Muestro gif animado puesto en marcha al pedir el formulario xforms a Glue
		InformativeDialogs.showLoadingDialog(i18n.get("loadingConfiguration"));
		ToolInstanceConfiguration.internationalizeDialog();
		var tool = toolInstance.getTool();
		var positionType = toolInstance.getPositionType();
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
		
		//Se elimina este control, al a�adir en ActivityPainter.js que al reutilizar se copia el position
		/*
		var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(toolInstance);
		if (toolInstance.getPosition() && toolInstance.getPosition() != "" && toolInstance.getPosition() != "undefined") {
			geoposition = toolInstance.getPosition();
		} else if (toolInstanceOrigen.getPosition() && toolInstanceOrigen.getPosition() != "" && toolInstanceOrigen.getPosition() != "undefined") {
			geoposition = toolInstanceOrigen.getPosition();
		} else {
			geoposition = toolInstance.getPosition();
		}	
		*/
		//Juan: hasta aqu? modificaci?n
		
		var xhrArgs = {
			url : url,
			timeout : 10000, // Tiempo m�ximo de espera
			handleAs : "text",// Tipo de dato de la respuesta del Get
			load : function(data) {
				parserXforms.data = data;
				parserXforms.printformfields();
				dojo.byId("toolConfigurationError").innerHTML="";
				var divToolConfiguration = document.getElementById("divToolConfiguration");
				// Se borra todo lo que tenga el div (el formulario)
				divToolConfiguration.removeChild(document.getElementById("formToolConfiguration"));
				// Se crea el formulario
				var formConfiguration = document.createElement("form");
				formConfiguration.setAttribute("id", "formToolConfiguration");
				divToolConfiguration.appendChild(formConfiguration);

				// A�ado campo para el t�tulo
				var pTitleVle = document.createElement("p");
				pTitleVle.innerHTML = i18n.get("ToolReuseTitleInVle");
				var inputTitleVle = document.createElement("input");
				inputTitleVle.setAttribute("type", "text");
				inputTitleVle.setAttribute("id", "titleVle");
				inputTitleVle.setAttribute("name", "titleVle");
				inputTitleVle.setAttribute("size", 40);
				inputTitleVle.setAttribute("value", toolInstance.getName());
				pTitleVle.appendChild(inputTitleVle);
				formConfiguration.appendChild(pTitleVle);
				
				//Juan
				
				if (LearningEnvironment.getEnableAR() && LearningEnvironment.getShowAR()) {					
					//Si la herramienta es interna no aparecen campos de geoposici�n
					if (!tool.isInternal()) {
						ToolInstanceConfiguration.addRadioButtonARPosition(formConfiguration, positionType);
						// Se a�ade campo para la geoposici�n
						ToolInstanceConfiguration.addARConfFields(formConfiguration, toolInstance, instancedActivity);
						//Se muestran los par�metros de config de AR que correspondan
						ToolInstanceConfiguration.showARConfFields(positionType);
						formConfiguration.appendChild(document.createElement("br"));
					}	
				}

				//Juan: Hasta aqu� mi �apa
				
				
				var reuses = ToolInstanceReuse.reusesToolInstance(toolInstance);
				// A�ado el radio button para la selecci�n de crear instancia de herramienta o reutilizar
				if (tool.getToolKind() != "internal") {
					ToolInstanceConfiguration.addRadioButtonExternalTool(formConfiguration, reuses);
					var divCI = formConfiguration.appendChild(document.createElement("div"));
					divCI.setAttribute("id", "divCI");
					formConfiguration.appendChild(divCI);
					// A�ade cada uno de los campos del formulario
					for ( var i = 0; i < parserXforms.camposFormulario.length; i++) {
						divCI.appendChild(parserXforms.camposFormulario[i]);
					}
					if (reuses)
					{
						dojo.style("divCI", "display", "none");
					}
				} else {
					var toolVle = tool.getToolVle();
					if (toolVle.value.search(/(Moodle)/i) != -1) {
						var moodleType = true;
					} else {
						moodleType = false;
					}
					ToolInstanceConfiguration.addRadioButtonInternalTool(formConfiguration, moodleType, reuses);
				}
				
				ToolInstanceConfiguration.addSelectReuseTool(formConfiguration, toolInstance, position, activity, instancedActivity);
				ToolInstanceConfiguration.addApplyAll(formConfiguration);
				if (tool.isInternal()){
					dojo.style("divCheckbox", "display", "none");
				}

				// Elimino botones que he creado antes
				while (document.getElementById("buttonsConfiguration").childNodes.length > 0) {
					document
							.getElementById("buttonsConfiguration")
							.removeChild(
									document
											.getElementById("buttonsConfiguration").childNodes[0]);
				}
				while (document.getElementById("buttonsCreate").childNodes.length > 0) {
					document
							.getElementById("buttonsCreate")
							.removeChild(
									document.getElementById("buttonsCreate").childNodes[0]);
				}

				// A�adir botones
				var buttonSave = document
						.getElementById("buttonsConfiguration").appendChild(
								document.createElement("button"));
				var button = new dijit.form.Button({
					label : i18n.get("okButton"),
					// label : i18n.get("ToolReuseButtonConfigurationSave"),
					name : "toolReuseButtonConfigurationSave",
					onClick : function() {
						ToolInstanceConfiguration.checkReuseConfiguration(toolInstance, position, activity, instancedActivity);
					}
				}, buttonSave);
				button.setAttribute("id", "buttonSave");
				var buttonCancel = document.getElementById(
						"buttonsConfiguration").appendChild(
						document.createElement("button"));
				var button = new dijit.form.Button({
					label : i18n.get("cancelButton"),
					name : "toolReuseButtonConfigurationSaveCancel",
					onClick : function() {
						dijit.byId("toolConfiguration").hide();
					}
				}, buttonCancel);
				dojo.style("buttonsConfiguration", "display", "");

				// En las herramientas internas no aparece el bot�n de crear
				if (tool.getToolKind() != "internal") {
					var buttonCreate = document.getElementById("buttonsCreate")
							.appendChild(document.createElement("button"));
					var button = new dijit.form.Button({
						label : i18n.get("okButton"),
						name : "toolReuseButtonConfigurationCreate",
						onClick : function() {
							ToolInstanceConfiguration.checkCreateToolInstance(toolInstance, activity);
						}
					}, buttonCreate);
					button.setAttribute("id", "buttonCreate");
					var buttonCancel = document.getElementById("buttonsCreate")
							.appendChild(document.createElement("button"));
					var button = new dijit.form.Button({
						label : i18n.get("cancelButton"),
						name : "toolReuseButtonConfigurationCreateCancel",
						onClick : function() {
							dijit.byId("toolConfiguration").hide();
						}
					}, buttonCancel);
					var reuses = ToolInstanceReuse.reusesToolInstance(toolInstance);
					if (reuses)
					{
						dojo.style("buttonsConfiguration", "display", "");
						dojo.style("buttonsCreate", "display", "none");
					}
					else
					{
						dojo.style("buttonsConfiguration", "display", "none");
						dojo.style("buttonsCreate", "display", "");
					}
				}

				dijit.byId("loading").hide();
				dijit.byId("toolConfiguration").show();
			},

			error : function(error, ioargs) {
				dijit.byId("loading").hide();
				var message = "";
				if (error.dojoType == "timeout") {
					message = error.message;
				} else {
					//message = ErrorCodes.errores(ioargs.xhr.status);
					message = i18n.get("toolConfigurationError");
				}
				InformativeDialogs.showAlertDialog(i18n.get("warning"), message);
			}
		}
		// Call the asynchronous xhrGet
		var deferred = dojo.xhrGet(xhrArgs);
	},
	
	
	//Juan: Funci�n para guardar configuraci�n sin crear instancia, cuando la instancia ya existe
	toolInstanceUpdateConfig : function(toolInstance, position, activity, instancedActivity) {
		// Muestro gif animado puesto en marcha al pedir el formulario xforms a Glue
		InformativeDialogs.showLoadingDialog(i18n.get("loadingConfiguration"));
		ToolInstanceConfiguration.internationalizeDialog();
		var tool = toolInstance.getTool();
		var positionType = toolInstance.getPositionType();
		
		//Se elimina este control, al a�adir en ActivityPainter.js que al reutilizar se copia el position
		/*
		var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(toolInstance);
		if (toolInstance.getPosition() && toolInstance.getPosition() != "" && toolInstance.getPosition() != "undefined") {
			geoposition = toolInstance.getPosition();
		} else if (toolInstanceOrigen.getPosition() && toolInstanceOrigen.getPosition() != "" && toolInstanceOrigen.getPosition() != "undefined") {
			geoposition = toolInstanceOrigen.getPosition();
		} else {
			geoposition = toolInstance.getPosition();
		}	
		*/
		//Juan: hasta aqu� modificaci�n
		
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
			timeout : 10000, // Tiempo m?ximo de espera
			handleAs : "text",// Tipo de dato de la respuesta del Get
			load : function(data) {
				parserXforms.data = data;
				parserXforms.printformfields();
				dojo.byId("toolConfigurationError").innerHTML="";
				var divToolConfiguration = document.getElementById("divToolConfiguration");
				// Se borra todo lo que tenga el div (el formulario)
				divToolConfiguration.removeChild(document.getElementById("formToolConfiguration"));
				// Se crea el formulario
				var formConfiguration = document.createElement("form");
				formConfiguration.setAttribute("id", "formToolConfiguration");
				divToolConfiguration.appendChild(formConfiguration);

				// A�ado campo para el t�tulo
				var pTitleVle = document.createElement("p");
				pTitleVle.innerHTML = i18n.get("ToolReuseTitleInVle");
				var inputTitleVle = document.createElement("input");
				inputTitleVle.setAttribute("type", "text");
				inputTitleVle.setAttribute("id", "titleVle");
				inputTitleVle.setAttribute("name", "titleVle");
				inputTitleVle.setAttribute("size", 40);
				inputTitleVle.setAttribute("value", toolInstance.getName());
				pTitleVle.appendChild(inputTitleVle);
				formConfiguration.appendChild(pTitleVle);
				
				//Juan

				if (LearningEnvironment.getEnableAR() && LearningEnvironment.getShowAR()) {
					ToolInstanceConfiguration.addRadioButtonARPosition(formConfiguration, positionType);
					
					//Si la herramienta es interna no aparcen campos de geoposici�n
					if (tool.getToolKind() != "internal") {	
						// Se a�aden campos AR
						ToolInstanceConfiguration.addARConfFields(formConfiguration, toolInstance, instancedActivity);
						formConfiguration.appendChild(document.createElement("br"));
						//Se muestran los par�metros de config de AR que correspondan
						ToolInstanceConfiguration.showARConfFields(positionType);

					}	
				}
								
				//A�ado campo aplicar a todos
				ToolInstanceConfiguration.addApplyAllConf(formConfiguration);

				//Juan: Hasta aqu� mi �apa
				
				// Elimino botones que he creado antes
				while (document.getElementById("buttonsConfiguration").childNodes.length > 0) {
					document
							.getElementById("buttonsConfiguration")
							.removeChild(
									document
											.getElementById("buttonsConfiguration").childNodes[0]);
				}
				while (document.getElementById("buttonsCreate").childNodes.length > 0) {
					document
							.getElementById("buttonsCreate")
							.removeChild(
									document.getElementById("buttonsCreate").childNodes[0]);
				}

				// A?adir botones
				var buttonSave = document
						.getElementById("buttonsConfiguration").appendChild(
								document.createElement("button"));
				var button = new dijit.form.Button({
					label : i18n.get("okButton"),
					// label : i18n.get("ToolReuseButtonConfigurationSave"),
					name : "toolReuseButtonConfigurationSave",
					onClick : function() {
						ToolInstanceConfiguration.checkUpdateToolInstance(toolInstance, activity);
					}
				}, buttonSave);
				button.setAttribute("id", "buttonSave");
				var buttonCancel = document.getElementById(
						"buttonsConfiguration").appendChild(
						document.createElement("button"));
				var button = new dijit.form.Button({
					label : i18n.get("cancelButton"),
					name : "toolReuseButtonConfigurationSaveCancel",
					onClick : function() {
						dijit.byId("toolConfiguration").hide();
					}
				}, buttonCancel);
				dojo.style("buttonsConfiguration", "display", "");

		

				dijit.byId("loading").hide();
				dijit.byId("toolConfiguration").show();
			},

			error : function(error, ioargs) {
				dijit.byId("loading").hide();
				var message = "";
				if (error.dojoType == "timeout") {
					message = error.message;
				} else {
					message = ErrorCodes.errores(ioargs.xhr.status);
				}
				InformativeDialogs
						.showAlertDialog(i18n.get("warning"), message);
			}
		}
		// Call the asynchronous xhrGet
		var deferred = dojo.xhrGet(xhrArgs);
	},
	
	
	
	addSelectReuseTool : function(parentNode, toolInstance, position,activity, instancedActivity) {
		// A�ado el select para la reutilizaci�n de herramientas
		var divSelect = parentNode.appendChild(document.createElement("div"));
		divSelect.setAttribute("id", "divSelect");
		dojo.style("divSelect", "padding", "10px 0px");
		parentNode.appendChild(divSelect);
		// A�adir el select con las instancias de herramientas del mismo tipo
		var nodoSelect = document.createElement("select");
		nodoSelect.id = "selectToolInstance";
		nodoSelect.name = "selectToolInstance";

		// Obtenemos y a�adimos al select las instancias del mismo tipo que han
		// aparecido previamente
		var tiPreviously = ToolInstanceReuse.getToolInstanceTypePreviously(toolInstance, position,activity, instancedActivity);
		var option = new Option(i18n.get("selectToolInstanceDefault"),"0");
		option.selected = true;
		nodoSelect.options[0] = option;
		for ( var i = 0; i < tiPreviously.length; i++) 
		{
			var option = new Option(tiPreviously[i].getName(),tiPreviously[i].getId());
			nodoSelect.options[i + 1] = option;
		}
		if (ToolInstanceReuse.reusesToolInstance(toolInstance))
		{
		   var reusedId = toolInstance.getLocation();
		   for (i = 0; i < nodoSelect.options.length; i++) {
			      if (nodoSelect.options[i].value == reusedId) {
			    	  nodoSelect.options[i].selected = true;
			    	  break;
			      }   
		   }
		}
		else{
			dojo.style("divSelect", "display", "none"); // Inicialmente est� oculto
		}
		divSelect.appendChild(nodoSelect);
	},
	
	addApplyAll: function(parentNode){
		var divCheckbox = parentNode.appendChild(document.createElement("div"));
		divCheckbox.setAttribute("id", "divCheckbox");
		dojo.style("divCheckbox", "margin", "10px 0px");
		parentNode.appendChild(divCheckbox);
		//A�adir el checkbox
		var checkbox = document.createElement("input");
		checkbox.setAttribute("type","checkbox");
		checkbox.setAttribute("id", "checkboxApplyAll");
		checkbox.setAttribute("value", "applyAll");
		var label = document.createElement("label");
		label.setAttribute("id", "labelApplyAll");
		label.style.marginLeft = "5px";
		label.innerHTML=i18n.get("labelApplyConfAll");
		divCheckbox.appendChild(checkbox);	
		divCheckbox.appendChild(label);
		
		dojo.connect(label, "onclick",
				function() {
			var checkbox = dojo.byId("checkboxApplyAll");
			if (checkbox.checked){
				checkbox.checked = false;
			}
			else{
				checkbox.checked = true;
			}
		});
		
	},
	
	addApplyAllConf: function(parentNode){
		var divCheckbox = parentNode.appendChild(document.createElement("div"));
		divCheckbox.setAttribute("id", "divCheckboxConf");
		dojo.style("divCheckboxConf", "margin", "10px 0px");
		parentNode.appendChild(divCheckbox);
		//Añadir el checkbox
		var checkbox = document.createElement("input");
		checkbox.setAttribute("type","checkbox");
		checkbox.setAttribute("id", "checkboxApplyAllConf");
		checkbox.setAttribute("value", "applyAll");
		var label = document.createElement("label");
		label.setAttribute("id", "labelApplyAllConf");
		label.style.marginLeft = "5px";
		label.innerHTML=i18n.get("labelApplyConfAllConf");
		divCheckbox.appendChild(checkbox);	
		divCheckbox.appendChild(label);
		
		dojo.connect(label, "onclick",
				function() {
			var checkbox = dojo.byId("checkboxApplyAllConf");
			if (checkbox.checked){
				checkbox.checked = false;
			}
			else{
				checkbox.checked = true;
			}
		});
		
	},

	/**
	 * A�ade un radio button para la elecci�n del tipo de configuraci�n
	 * 
	 * @param parentNode
	 */
	addRadioButtonExternalTool : function(parentNode, reuseToolInstance) {
		var newElement = parentNode.appendChild(document.createElement("div"));
		var createTool = new dijit.form.RadioButton({
			value : "ci",
			name : "confMode",
			checked: reuseToolInstance==false,
			onChange : function() {
				ToolInstanceConfiguration.changeConfigurationModeExternal(createTool
						.getValue());
			}
		}, newElement);
		createTool.setAttribute("id", "rbCi");
		var labelElement = document.createElement("label");
		labelElement.setAttribute("for", createTool.id);
		labelElement.innerHTML = " " + i18n.get("createToolInstance");
		parentNode.appendChild(labelElement);
		parentNode.appendChild(document.createElement("br"));

		var newElement = parentNode.appendChild(document.createElement("div"));
		var reuse = new dijit.form.RadioButton({
			value : "reuse",
			name : "confMode",
			checked: reuseToolInstance
		}, newElement);
		reuse.setAttribute("id", "rbReuse");
		var labelElement = document.createElement("label");
		labelElement.setAttribute("for", reuse.id);
		labelElement.innerHTML = " " + i18n.get("reuseToolInstance");
		parentNode.appendChild(labelElement);
		parentNode.appendChild(document.createElement("br"));
	},

	addRadioButtonInternalTool : function(parentNode, moodleType, reuseToolInstance) {
		// Las herramientas de tipo moodle no pueden reutilizar. A�adir mensaje informativo
		if (moodleType) {
			var div = document.createElement("div");
			div.setAttribute("class", "divInfo");
			var infoIcon = document.createElement("img");
			infoIcon.setAttribute("id", "infoMoodle");
			infoIcon.setAttribute("class", "imgInfo");
			infoIcon.setAttribute("src", "images/icons/info.png");
			div.appendChild(infoIcon);
			var label = document.createElement("label");
			label.innerHTML = i18n.get("reusingNotPossible");
			div.appendChild(label);
			parentNode.appendChild(div);
		}

		var newElement = parentNode.appendChild(document.createElement("div"));
		var dontReuse = new dijit.form.RadioButton({
			value : "dontReuse",
			name : "confModeInternal",
			checked : reuseToolInstance==false,
			onChange : function() {
				ToolInstanceConfiguration.changeConfigurationModeInternal(dontReuse
						.getValue());
			}
		}, newElement);
		dontReuse.setAttribute("id", "rbDontReuse");
		var labelElement = document.createElement("label");
		labelElement.setAttribute("for", dontReuse.id);
		labelElement.innerHTML = " " + i18n.get("notReuseToolInstance");
		parentNode.appendChild(labelElement);
		parentNode.appendChild(document.createElement("br"));

		var newElement = parentNode.appendChild(document.createElement("div"));
		var reuse = new dijit.form.RadioButton({
			value : "reuse",
			name : "confModeInternal",
			checked: reuseToolInstance
		}, newElement);
		reuse.setAttribute("id", "rbReuse");
		var labelElement = document.createElement("label");
		labelElement.setAttribute("for", reuse.id);
		labelElement.innerHTML = " " + i18n.get("reuseToolInstance");
		parentNode.appendChild(labelElement);
		parentNode.appendChild(document.createElement("br"));

		if (moodleType) {
			dojo.byId("rbReuse").disabled = true;
		}
	},

	//Juan: A�ado bot�n radio para posicionar en AR
	addRadioButtonARPosition : function(parentNode,positionType) {
		var np;
		var geo;
		var qr;
		var marker;
		switch(positionType)
		{
			case(null): {
				np = true;
				geo = false;
				qr = false;
				marker = false;
				break;
				}
			case('geoposition'):{
				np = false;
				geo = true;
				qr = false;
				marker = false;
				break;
				}
			case('qrcode'):{
				np = false;
				geo = false;
				qr = true;
				marker = false;
				break;
				}
			case('junaiomarker'):{
				np = false;
				geo = false;
				qr = false;
				marker = true;
				break;
				}
			default:{
				np = true;
				geo = false;
				qr = false;
				marker = false;
				}
		}
		//Radio no Ar position
		var newElement = parentNode.appendChild(document.createElement("div"));
        var w1 = dijit.byId("ptNone");
        if(w1) { w1.destroyRecursive(); } 
		var ptNone = new dijit.form.RadioButton({
			value : null,
			name : "positionType",
			id: "ptNone",
			checked: np,
			onClick : function() {
				np = true;
				geo = false;
				marker = false;
				if (np) {
					ToolInstanceConfiguration.showARConfFields(ptNone.getValue());
				}
			}
			
		}, newElement);
		newElement.setAttribute("id", "divRadio");
		ptNone.setAttribute("id", "ptNone");
		var labelElement = document.createElement("label");
		labelElement.setAttribute("for", ptNone.id);
		labelElement.innerHTML = " " + i18n.get("positionNull");
		parentNode.appendChild(labelElement);
		parentNode.appendChild(document.createElement("br"));
		
		
		//Radio geoposition
		var newElement = parentNode.appendChild(document.createElement("div"));
        var w2 = dijit.byId("ptGeo");
        if(w2) { w2.destroyRecursive(); } 
		var ptGeo = new dijit.form.RadioButton({
			value : "geoposition",
			name : "positionType",
			id : "ptGeo",
			checked: geo,
			onClick : function() {
				np = false;
				geo = true;
				marker = false;
				if (geo) {
					ToolInstanceConfiguration.showARConfFields(ptGeo.getValue());
				}
			}
		}, newElement);
		ptGeo.setAttribute("id", "ptGeo");
		var labelElement = document.createElement("label");
		labelElement.setAttribute("for", ptGeo.id);
		labelElement.innerHTML = " " + i18n.get("positionGeo");
		parentNode.appendChild(labelElement);
		parentNode.appendChild(document.createElement("br"));
		
		//Radio qr code position
		var newElement = parentNode.appendChild(document.createElement("div"));
        var w1 = dijit.byId("ptQr");
        if(w1) { w1.destroyRecursive(); } 
		var ptQr = new dijit.form.RadioButton({
			value : "qrcode",
			name : "positionType",
			id: "ptQr",
			checked: qr,
			onClick : function() {
				np = false;
				geo = false;
				qr = true;
				marker = false;
				if (qr) {
					ToolInstanceConfiguration.showARConfFields(ptQr.getValue());
				}
			}
			
		}, newElement);
		newElement.setAttribute("id", "divRadio");
		ptQr.setAttribute("id", "ptQr");
		var labelElement = document.createElement("label");
		labelElement.setAttribute("for", ptQr.id);
		labelElement.innerHTML = " " + i18n.get("positionQr");
		parentNode.appendChild(labelElement);
		parentNode.appendChild(document.createElement("br"));
		
		//Radio marker position
		var newElement = parentNode.appendChild(document.createElement("div"));
        var w3 = dijit.byId("ptMarker");
        if(w3) { w3.destroyRecursive(); } 
		var ptMarker = new dijit.form.RadioButton({
			value : "junaiomarker",
			name : "positionType",
			id: "ptMarker",
			checked: marker,
			onClick : function() {
				np = false;
				geo = false;
				marker = true;
				if (marker) {
					ToolInstanceConfiguration.showARConfFields(ptMarker.getValue());
				}
			}
		}, newElement);
		ptMarker.setAttribute("id", "ptMarker");
		var labelElement = document.createElement("label");
		labelElement.setAttribute("for", ptMarker.id);
		labelElement.innerHTML = " " + i18n.get("positionMarker");
		parentNode.appendChild(labelElement);
		parentNode.appendChild(document.createElement("br"));
	//	parentNode.appendChild(document.createElement("br"));
		


	},
	
	//Juan: Se añaden campos de configuración de AR
	addARConfFields : function(parentNode, toolInstance, instancedActivity) {
		var arposition = toolInstance.getPosition();
		var maxdistance = toolInstance.getMaxdistance();
		var positionType = toolInstance.getPositionType();
		
		var geoposition = "";
		var maxdist = "";
		if (positionType == "geoposition") {
			geoposition = arposition;
			maxdist = maxdistance;
		}
		
		var divARgeo = parentNode.appendChild(document.createElement("div"));
		divARgeo.setAttribute("id", "divARgeo");
		parentNode.appendChild(divARgeo);
		var pGeoposition = document.createElement("p");
		pGeoposition.innerHTML = i18n.get("ToolReuseGeoposition");
		pGeoposition.innerHTML = "<span id=\"geopositionSpan\">" + i18n.get("ToolReuseGeoposition") + "</span>";
		var inputGeoposition = document.createElement("input");
		inputGeoposition.setAttribute("type", "text");
		inputGeoposition.setAttribute("id", "geoposition");
		inputGeoposition.setAttribute("name", "geoposition");
		inputGeoposition.setAttribute("size", 40);
		inputGeoposition.setAttribute("value", geoposition);
		pGeoposition.appendChild(inputGeoposition);
		divARgeo.appendChild(pGeoposition);
		//Se muestra tooltip con la ayuda
		ToolInstanceConfiguration.tooltipHelp("geopositionSpan", i18n.get("ToolReuseGeopositionHelp"));
			
		
		// Juan: Enlace a mapa para obtener geo-posici�n
		var linkToMap = document.createElement("a");
		linkToMap.href = "pages/geoposition.html";
		linkToMap.target = "_blank";

		var pMap = document.createElement("p");
		pMap.innerHTML = i18n.get("ToolReuseMap"); 
		linkToMap.appendChild(pMap);
		divARgeo.appendChild(linkToMap);
	
		
			
		// Juan: A�ado campo para la distancia m�xima para mostrarlo en AR browser
		var pMaxdistance = document.createElement("p");
		pMaxdistance.innerHTML = i18n.get("ToolReuseMaxdistance");
		var inputMaxdistance = document.createElement("input");
		inputMaxdistance.setAttribute("type", "text");
		inputMaxdistance.setAttribute("id", "maxdistance");
		inputMaxdistance.setAttribute("name", "maxdistance");
		inputMaxdistance.setAttribute("size", 10);
		inputMaxdistance.setAttribute("value", maxdist);
		pMaxdistance.appendChild(inputMaxdistance);
		divARgeo.appendChild(pMaxdistance);
		
		ToolInstanceConfiguration.showARscaleField(divARgeo,toolInstance, "geo");
//		ToolInstanceConfiguration.showARorienField(divARgeo, toolInstance, "geo");
	

		//Se a�aden campos para posicionamiento con marcadores
		// A�ado el select para la selecci�n de marcador
		var divSelect = parentNode.appendChild(document.createElement("div"));
		divSelect.setAttribute("id", "divSelectARjunaiomarker");
		dojo.style("divSelectARjunaiomarker", "padding", "10px 0px");
		parentNode.appendChild(divSelect);
		// A?adir el select
		var nodoSelect = document.createElement("select");
		nodoSelect.id = "selectMarker";
		nodoSelect.name = "selectMarker";

		//Ubicacion actual de los 21 marcadores de prueba: http://157.88.130.207/GLUEPS_AR/markers/ID_Marker1-21/MetaioMarkerX.png
		//TODO est� puesta a fuego la ubicaci�n de los marcadores. Habr�a que sacar parte de la URL autom�ticamente si es posible.
		if (window.location.host == "www.gsic.uva.es") {
			var urlmarker = "http://" + window.location.host + "/juanmunoz/GLUEPSManager/gui/glueps/arbrowsers/markers/ID_Marker1-21/MetaioMarker";
		} else if (window.location.host == "glue-test.cloud.gsic.tel.uva.es") {
			var urlmarker = "http://" + window.location.host + "/ActionServer/GLUEPSManager/gui/glueps/arbrowsers/markers/ID_Marker1-21/MetaioMarker";
		} else {
			var urlmarker = "http://" + window.location.host + "/GLUEPSManager/gui/glueps/arbrowsers/markers/ID_Marker1-21/MetaioMarker";
		}
	//	var urlmarker = "http://" + window.location.host + "/juanmunoz/GLUEPSManager/gui/glueps/arbrowsers/markers/ID_Marker1-21/MetaioMarker";
		var markers = new Array();
		for (i=0;i<21;i++) {
			var s = i+1;
			markers[i]=urlmarker + s + ".png";
			}
		
		// A�adimos al select los marcadores
		var option = new Option(i18n.get("selectMarkerDefault"),"0");
		option.selected = true;
		nodoSelect.options[0] = option;
		var index = 1;
		for ( var i = 0; i < markers.length; i++) 
		{
			var s = i+1;
			//Se comprueba que el marcador no est� ya usado en otra instanced activity (ni en la presente)
			var notused = true;
			var toolinstances = new Array();
			toolinstances = instancedActivity.getToolInstances();
			for ( var j = 0; j < toolinstances.length; j++) 
			{
				if (toolinstances[j].getPosition() == markers[i] && toolinstances[j].getLocation()) {
					if (toolinstances[j].getId() == toolInstance.getId()) {
						notused = true;
					} else {
						notused = false;
						break;
					}
				}					
			}
			
			if (notused)
			{
				var option = new Option(i18n.get("selectMarkername") + " " + s,markers[i]);
			//	nodoSelect.options[i + 1] = option;
				nodoSelect.options[index] = option;
				index++;
			}

		}
		if (positionType == "junaiomarker")
		{
			if (arposition) {
				   var markerId = arposition;
				   for (i = 0; i < nodoSelect.options.length; i++) {
					      if (nodoSelect.options[i].value == markerId) {
					    	  nodoSelect.options[i].selected = true;
					    	  break;
					      }   
				   }
			} 

		}
		else{
			dojo.style("divSelectARjunaiomarker", "display", "none"); // Inicialmente est� oculto
		}
		divSelect.appendChild(nodoSelect);
		ToolInstanceConfiguration.showARscaleField(divSelect,toolInstance, "junaiomarker");
	//	ToolInstanceConfiguration.showARorienField(divSelect, toolInstance, "junaiomarker");
		divSelect.appendChild(document.createElement("br"));
			
			
		ToolInstanceConfiguration.showARConfFields(positionType);
		
	},
	
	
	//Juan: Hasta aqu� modificaci�n
	
	changeConfigurationModeExternal : function(create) {
		dojo.byId("toolConfigurationError").innerHTML="";
		if (create) {
			dojo.style("divCI", "display", "");
			dojo.style("divSelect", "display", "none");
			//dojo.style("divCheckbox", "display", "");
			dojo.style("buttonsConfiguration", "display", "none");
			dojo.style("buttonsCreate", "display", "");
		} else {
			dojo.style("divCI", "display", "none");
			dojo.style("divSelect", "display", "");
			//dojo.style("divCheckbox", "display", "none");
			dojo.style("buttonsConfiguration", "display", "");
			dojo.style("buttonsCreate", "display", "none");
		}
	},

	changeConfigurationModeInternal : function(dontReuse) {
		if (dontReuse) {
			dojo.style("divSelect", "display", "none");
		} else {
			dojo.style("divSelect", "display", "");
		}
	},
	

	
	//Juan: Para controlar la aparici�n de los men�s de posicionamiento AR
	showARConfFields : function(postype) {
		dojo.byId("toolConfigurationError").innerHTML="";
		switch(postype)
		{
		case(null): {
			dojo.style("divSelectARjunaiomarker", "display", "none");
			dojo.style("divARgeo", "display", "none");
			break;
			}
		case('geoposition'):{
			dojo.style("divSelectARjunaiomarker", "display", "none");
			dojo.style("divARgeo", "display", "");
			break;
			}
		case('qrcode'): {
			dojo.style("divSelectARjunaiomarker", "display", "none");
			dojo.style("divARgeo", "display", "none");
			break;
			}
		case('junaiomarker'):{
			dojo.style("divSelectARjunaiomarker", "display", "");
			dojo.style("divARgeo", "display", "none");
			break;
			}
		default:{
			dojo.style("divSelectARjunaiomarker", "display", "none");
			dojo.style("divARgeo", "display", "none");
		}
		}

	},
	
	//Juan
	/**	
	 * Muestra el campo "escala" en herramientas externas de tipo 3Dmodel y AR image
	**/
	
	showARscaleField: function(parentNode, toolInstance, place) {
		var nameOrId = place + "Scale";
		var positionType = toolInstance.getPositionType();
		var tool = toolInstance.getTool();
		if (tool.getToolKind() != "internal") {	
			var toolType = tool.getToolType().substr(tool.getToolType().lastIndexOf('/') + 1);
			if (toolType == "18" || toolType == "19") {
				var scale = "";
				if (positionType == "geoposition" || positionType == "junaiomarker") {
					if (toolInstance.getScale() == "undefined" || toolInstance.getScale() == null) {
						scale = "";
					} else {
						scale = toolInstance.getScale();
					}
					
				}
				var pScale = document.createElement("p");
				pScale.innerHTML = "<span id=\"" + nameOrId + "Span\">" + i18n.get("ToolReuseScale") + "</span>";
				var inputScale = document.createElement("input");
				inputScale.setAttribute("type", "text");
				inputScale.setAttribute("id", nameOrId);
				inputScale.setAttribute("name", nameOrId);
				inputScale.setAttribute("size", 10);
				inputScale.setAttribute("value", scale);
				pScale.appendChild(inputScale);
				parentNode.appendChild(pScale);
				//Se muestra tooltip con la ayuda
				ToolInstanceConfiguration.tooltipHelp(nameOrId + "Span", i18n.get("ToolReuseScaleHelp"));
								
			}
			
			
		}
	},
	
	
	//Juan
	/**	
	 * Muestra el campo "orientaci�n" en herramientas externas
	**/
	/*
	showARorienField: function(parentNode, toolInstance, place){
		var nameOrId = place + "Orientation";
		var positionType = toolInstance.getPositionType();
		var tool = toolInstance.getTool();
		if (tool.getToolKind() != "internal") {	
			var toolType = tool.getToolType().substr(tool.getToolType().lastIndexOf('/') + 1);
			if (toolType == "18" || toolType == "19") {
				var orientation = "";
				if (positionType == "geoposition" || positionType == "junaiomarker") {
					if (toolInstance.getOrientation() == "undefined" || toolInstance.getOrientation() == null) {
						orientation = "";
					} else {
						orientation = toolInstance.getOrientation();
					}
					
				}
				var pOrientation = document.createElement("p");
				pOrientation.innerHTML = "<span id=\"" + nameOrId + "Span\">" + i18n.get("ToolReuseOrientation") + "</span>";
				var inputOrientation = document.createElement("input");
				inputOrientation.setAttribute("type", "text");
				inputOrientation.setAttribute("id", nameOrId);
				inputOrientation.setAttribute("name", nameOrId);
				inputOrientation.setAttribute("size", 10);
				inputOrientation.setAttribute("value", orientation);
				pOrientation.appendChild(inputOrientation);
				parentNode.appendChild(pOrientation);
				//Se muestra tooltip con la ayuda
				ToolInstanceConfiguration.tooltipHelp(nameOrId + "Span", i18n.get("ToolReuseOrientationHelp"));
								
			}
			
			
		}
	
	},
	
	*/

	//Juan
	/**	
	 * Genera un tooltip con el texto label en una etiqueta span (con id scaleSpan)
	**/
    tooltipHelp: function(scaleSpan,label){
        new dijit.Tooltip({
            connectId: [scaleSpan],
            label: label
        });
    },
	
	getFormContent : function() {
		var configurationTool = new Array();
		var nameInput = "";
		var valueInput = "";
		// Se obtienen y almacenan cada uno de los campos del formulario de
		// configuraci�n de herramientas
		for ( var j = 0; j < document.forms.formToolConfiguration.length; j++) {
			nameInput = document.forms.formToolConfiguration[j].name;
			valueInput = document.forms.formToolConfiguration[j].value;
			configurationTool[nameInput] = valueInput;
		}
		
		//Juan: Se a�ade el positionType seleccionado en el elemento radio

		if (LearningEnvironment.getEnableAR() && LearningEnvironment.getShowAR()) {
			var pos = null;
			if(dijit.byId("ptNone")!=null && dijit.byId("ptNone").attr('value') !== false){
				pos = dijit.byId("ptNone").attr('value');
			}
			if(dijit.byId("ptGeo")!=null && dijit.byId("ptGeo").attr('value') !== false){
				pos = dijit.byId("ptGeo").attr('value');
			}
			if(dijit.byId("ptQr")!=null && dijit.byId("ptQr").attr('value') !== false) {
				pos = dijit.byId("ptQr").attr('value');
			}
			if(dijit.byId("ptMarker")!=null && dijit.byId("ptMarker").attr('value') !== false) {
				pos = dijit.byId("ptMarker").attr('value');
			}
			configurationTool["positionType"] = pos;
		}
		
		//Juan: Hasta aqu�
		
		
		return configurationTool;

	},
	
	/**
	 *  Comprueba que el formulario de actualizaci�n de la configuraci�n est� completo y 
	 *  obtiene las instancias de la herramienta que se van a actualizar
	 */
	checkUpdateToolInstance: function(toolInstance, activity)
	{
		var configuration = ToolInstanceConfiguration.getFormContent();
		if (configuration["titleVle"].length == 0)
		{
			dojo.byId("toolConfigurationError").innerHTML = i18n.get("noVleTitle");
		}
		else{
			dijit.byId("toolConfiguration").hide();
			//Comprobar si el usuario quiere aplicar la misma configuraci�n a todas las instancias de la misma herramienta en la actividad						
			if (!dojo.byId("checkboxApplyAllConf").checked){
				var ti = {
						toolInstance: toolInstance,
						isCopyConf: false,
						markerCopiable: true
				};
				ToolInstanceConfiguration.updateconfigToolInstance(ti);			
			}
			else{
				if (LearningEnvironment.getEnableAR() && LearningEnvironment.getShowAR()){
					var select = dojo.byId("selectMarker");
					var newMarkerId = select.options[select.selectedIndex].value;
				}
				
				var ti = {
						toolInstance: toolInstance,
						isCopyConf: false,
						markerCopiable: true
				};
				ToolInstanceConfiguration.updateconfigToolInstance(ti);
				var toolSource = toolInstance.getTool();
					
				var instancedActivities = activity.getInstancedActivities();
				for (var i = 0; i < instancedActivities.length; i++)
				{
					var toolInstances = instancedActivities[i].getToolInstances();
					if (toolInstances) {
						var instActTool = false; //Indica si la actividad instanciada contiene a la instancia que se configura
						for (var j = 0; j < toolInstances.length; j++){
							if (toolInstance.getId()== toolInstances[j].getId()){
								instActTool = true;
								break;
							}
						}
						if (LearningEnvironment.getEnableAR() && LearningEnvironment.getShowAR()){
							var markerExists = false; //Indica si la actividad instanciada tiene asignado ya ese marcador en alguna instancia
							for (var j = 0; j < toolInstances.length; j++){
								if (toolInstances[j].getPosition()== newMarkerId){
									markerExists = true;
									break;
								}
							}
						}
						var firstCopyConf = true; //Indica si es la primera copia de la configuraci�n que se va a realizar en la actividad instanciada
						for (var j = 0; j < toolInstances.length; j++)
						{
							//Evitar actualizar la instancia inicial de nuevo
							if (toolInstance.getId()!= toolInstances[j].getId()){
						    	var tool = toolInstances[j].getTool();
						    	//Se debe de actualizar la configuraci�n para aquellas instancias de la misma herramienta que han sido configuradas
						    	if (tool.getId()== toolSource.getId() && tool.isExternal() && toolInstances[j].getLocation())
						    	{
						    		var ti = {
											toolInstance: toolInstances[j],
											isCopyConf: true,
											markerCopiable: (firstCopyConf && !markerExists && !instActTool)
						    		};
						    		ToolInstanceConfiguration.updateconfigToolInstance(ti);
						    		if (firstCopyConf){
						    			firstCopyConf = false;
						    		}
						    	}
							}
						}
					}
				}
			}
			JsonDB.notifyChanges();
		}
	},
	
	/**
	 *  Comprueba que el formulario de creaci�n de instancia est� completo y 
	 *  obtiene las instancias de la herramienta que se van a instanciar
	 */
	checkCreateToolInstance: function(toolInstance, activity)
	{
		var configuration = ToolInstanceConfiguration.getFormContent();
		if (configuration["titleVle"].length == 0)
		{
			dojo.byId("toolConfigurationError").innerHTML = i18n.get("noVleTitle");
		}
		else{
			dijit.byId("toolConfiguration").hide();
			//Comprobar si el usuario quiere aplicar la misma configuraci�n a todas las instancias de la misma herramienta en la actividad
			if (!dojo.byId("checkboxApplyAll").checked){
				var toolInstancesCreate = new Array();
				var ti = {
						toolInstance: toolInstance,
						isCopyConf: false,
						markerCopiable: true
				};
				toolInstancesCreate.push(ti);
				ToolInstanceCreator.startToolInstanceCreation(toolInstancesCreate);
			}
			else{
				if (LearningEnvironment.getEnableAR() && LearningEnvironment.getShowAR()){
					var select = dojo.byId("selectMarker");
					var newMarkerId = select.options[select.selectedIndex].value;				
				}
				var toolInstancesCreate = new Array();
				//Incluimos la instancia de herramienta que se configura
				var ti = {
						toolInstance: toolInstance,
						isCopyConf: false,
						markerCopiable: true
				};
				toolInstancesCreate.push(ti);
				var toolSource = toolInstance.getTool();
					
				var instancedActivities = activity.getInstancedActivities();
				for (var i = 0; i < instancedActivities.length; i++)
				{
					var toolInstances = instancedActivities[i].getToolInstances();
					if (toolInstances) {
						var instActTool = false; //Indica si la actividad instanciada contiene a la instancia que se configura
						for (var j = 0; j < toolInstances.length; j++){
							if (toolInstance.getId()== toolInstances[j].getId()){
								instActTool = true;
								break;
							}
						}
						var markerExists = false; //Indica si la actividad instanciada tiene asignado ya ese marcador en alguna instancia
						if (LearningEnvironment.getEnableAR() && LearningEnvironment.getShowAR()){
							for (var j = 0; j < toolInstances.length; j++){
								if (toolInstances[j].getPosition()== newMarkerId){
									markerExists = true;
									break;
								}
							}
						}
						var firstCopyConf = true; //Indica si es la primera copia de la configuraci�n que se va a realizar en la actividad instanciada
						for (var j = 0; j < toolInstances.length; j++)
						{
							//Evitar incluir la instancia inicial de nuevo
							if (toolInstance.getId()!= toolInstances[j].getId()){
						    	var tool = toolInstances[j].getTool();
						    	//Se debe crear la instancia para aquellas instancias de la misma herramienta que no han sido configuradas
						    	if (tool.getId()== toolSource.getId() && tool.isExternal() && !toolInstances[j].getLocation())
						    	{
						    		var ti = {
											toolInstance: toolInstances[j],
											isCopyConf: true,
											markerCopiable: (firstCopyConf && !markerExists && !instActTool)
						    		};
						    		toolInstancesCreate.push(ti);
						    		if (firstCopyConf){
						    			firstCopyConf = false;
						    		}
						    	}
							}
						}
					}
				}				
				ToolInstanceCreator.startToolInstanceCreation(toolInstancesCreate);
			}
		}		
	},

	
	/**
	 * Actualizar configuraci�n de una instancia de herramienta (AR)
	 * 
	 * @param toolInstanceObj Objeto con informaci�n de la instancia de herramienta a actualizar
	 */
	updateconfigToolInstance : function(toolInstanceObj) {
		var toolInstance = toolInstanceObj.toolInstance;
		// Guardo el contenido del formulario.
		var configuration = ToolInstanceConfiguration.getFormContent();

		// Guardar el nombre elegido para la instancia de herramienta.
		//if (toolInstanceObj.isCopyConf==false){
			toolInstance.setName(configuration["titleVle"]);
		//}

		// Juan: Guardar la posición AR de la instancia de herramienta
		if (LearningEnvironment.getEnableAR() && LearningEnvironment.getShowAR()) {
			if (toolInstanceObj.isCopyConf == false){
				if (dijit.byId("ptNone").attr('value') !== false) {
					toolInstance.deletePosition();
					toolInstance.deleteMaxdistance();
					toolInstance.deletePositionType();
					toolInstance.deleteScale();
					// toolInstance.deleteOrientation();
				}
				if (dijit.byId("ptGeo").attr('value') !== false) {
					toolInstance.setPosition(configuration["geoposition"]);
					toolInstance.setMaxdistance(configuration["maxdistance"]);
					toolInstance.setPositionType(configuration["positionType"]);
					toolInstance.setScale(configuration["geoScale"]);
					// toolInstance.setOrientation(configuration["geoOrientation"]);
				}
				if (dijit.byId("ptQr").attr('value') !== false) {
					toolInstance.deletePosition();
					toolInstance.deleteMaxdistance();
					toolInstance.setPositionType(configuration["positionType"]);
					toolInstance.deleteScale();
					if (toolInstance.getLocation() != null) {
						var toolInstanceOrigen = ToolInstanceReuse
								.getOriginalToolInstance(toolInstance);
						var loc = toolInstanceOrigen.getLocation();
						var position = "http://qrickit.com/api/qr?qrsize=200&d="
								+ loc + "?callerUser=";
						toolInstance.setPosition(position);
					}
	
					// toolInstance.deleteOrientation();
				}
				if (dijit.byId("ptMarker").attr('value') !== false) {
					var select = dojo.byId("selectMarker");
					if (select && select.options[select.selectedIndex].value != "0") {
						var newMarkerId = select.options[select.selectedIndex].value;
						toolInstance.setPosition(newMarkerId);
					}
					toolInstance.deleteMaxdistance();
					toolInstance.setPositionType(configuration["positionType"]);
					toolInstance.setScale(configuration["junaiomarkerScale"]);
					// toolInstance.setOrientation(configuration["junaiomarkerOrientation"]);
				}
			}else{
				if (dijit.byId("ptNone").attr('value') !== false) {
					toolInstance.deletePosition();
					toolInstance.deleteMaxdistance();
					toolInstance.deletePositionType();
					toolInstance.deleteScale();
					// toolInstance.deleteOrientation();
				}
				//Si se est� aplicando la configuraci�n de otra instancia s�lo se almacena si es geoposici�n
				if (dijit.byId("ptGeo").attr('value') !== false) {
					toolInstance.setPosition(configuration["geoposition"]);
					toolInstance.setMaxdistance(configuration["maxdistance"]);
					toolInstance.setPositionType(configuration["positionType"]);
					toolInstance.setScale(configuration["geoScale"]);
					// toolInstance.setOrientation(configuration["geoOrientation"]);
				}
				if (dijit.byId("ptQr").attr('value') !== false) {
					toolInstance.deletePosition();
					toolInstance.deleteMaxdistance();
					toolInstance.setPositionType(configuration["positionType"]);
					toolInstance.deleteScale();
					if (toolInstance.getLocation() != null) {
						var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(toolInstance);
						var loc = toolInstanceOrigen.getLocation();
						var position = "http://qrickit.com/api/qr?qrsize=200&d=" + loc + "?callerUser=";
						toolInstance.setPosition(position);
					}
	
					// toolInstance.deleteOrientation();
				}
				if (dijit.byId("ptMarker").attr('value') !== false && toolInstanceObj.markerCopiable) {
					var select = dojo.byId("selectMarker");
					if (select && select.options[select.selectedIndex].value != "0") {
						var newMarkerId = select.options[select.selectedIndex].value;
						toolInstance.setPosition(newMarkerId);
					}
					toolInstance.deleteMaxdistance();
					toolInstance.setPositionType(configuration["positionType"]);
					toolInstance.setScale(configuration["junaiomarkerScale"]);
					// toolInstance.setOrientation(configuration["junaiomarkerOrientation"]);
				}
			}
		}

	},
	
	checkReuseConfiguration: function(toolInstance, position, activity, instancedActivity){
		var configuration = ToolInstanceConfiguration.getFormContent();
		if (configuration["titleVle"].length == 0)
		{
			dojo.byId("toolConfigurationError").innerHTML = i18n.get("noVleTitle");
		}
		else{
			// Oculto pantalla de configuraci�n de la herramienta
			dijit.byId("toolConfiguration").hide();
			var createTool = dojo.byId("rbCi");
			var dontReuse = dojo.byId("rbDontReuse");
			// Comprobar si se ha elegido reutilizar herramienta
			if ((createTool && !createTool.checked) || (dontReuse && !dontReuse.checked)) {
				// Actualizaci�n de la instancia de herramienta elegida
				var select = dojo.byId("selectToolInstance");
				if (select && select.options[select.selectedIndex].value == "0") {
					dojo.byId("toolConfigurationError").innerHTML = i18n.get("noToolSelected");
				}
				else{

					//Comprobar si el usuario quiere aplicar la misma configuraci�n a todas las instancias de la misma herramienta en la actividad
					if (!dojo.byId("checkboxApplyAll").checked){
						ToolInstanceConfiguration.saveReuseConfiguration(toolInstance,activity, false);
					}
					else{
						ToolInstanceConfiguration.saveReuseConfiguration(toolInstance,activity, false);
						var toolSource = toolInstance.getTool();
							
						var instancedActivities = activity.getInstancedActivities();
						for (var i = 0; i < instancedActivities.length; i++)
						{
							var toolInstances = instancedActivities[i].getToolInstances();
							if (toolInstances) {
								for (var j = 0; j < toolInstances.length; j++)
								{
									//Evitar actualizar la instancia inicial de nuevo o copiar configuraci�n en la instancia que se va a reutilizar
									if (toolInstance.getId()!= toolInstances[j].getId() && select.options[select.selectedIndex].value!=toolInstances[j].getId()){
								    	var tool = toolInstances[j].getTool();
								    	//Se debe copiar la configuraci�n para aquellas instancias de la misma herramienta que no han sido configuradas
								    	if (tool.getId()== toolSource.getId() && (tool.isExternal() && !toolInstances[j].getLocation()))
								    	{
								    		ToolInstanceConfiguration.saveReuseConfiguration(toolInstances[j],activity, true);
								    	}
									}
								}
							}
						}
					}
					JsonDB.notifyChanges();
				}
			}
			else{
				var configuration = ToolInstanceConfiguration.getFormContent();
				// Asignar el nombre elegido para la instancia de herramienta
				toolInstance.setName(configuration["titleVle"]);
				ToolInstanceConfiguration.saveReuseConfiguration(toolInstance,activity);
				JsonDB.notifyChanges();
			}
		}
	},

	/**
	 * Guarda la configuraci�n de una herramienta almacenando el contenido del formulario
	 * 
	 * @param toolInstance Herramienta cuya configuraci�n se desea guardar
	 * @param activity Actividad de la herramienta
	 * @param isCopyConf Se est� aplicando la configuraci�n de otra instancia o no
	 */
	saveReuseConfiguration : function(toolInstance, activity, isCopyConf) {

		// Guardo el contenido del formulario.
		var configuration = ToolInstanceConfiguration.getFormContent();

		// Guardar el nombre elegido para la instancia de herramienta
		if (isCopyConf==false){
			toolInstance.setName(configuration["titleVle"]);
		}
		
		// Juan: Guardar la posici�n AR de la instancia de herramienta
		if (LearningEnvironment.getEnableAR() && LearningEnvironment.getShowAR()) {
			if (isCopyConf == false){
				if(dijit.byId("ptNone")!=null && dijit.byId("ptNone").attr('value') !== false){
					toolInstance.deletePosition();
					toolInstance.deleteMaxdistance();
					toolInstance.deletePositionType();
					toolInstance.deleteScale();
			//		toolInstance.deleteOrientation();
				}
				if(dijit.byId("ptGeo")!=null && dijit.byId("ptGeo").attr('value') !== false){
					toolInstance.setPosition(configuration["geoposition"]);
					toolInstance.setMaxdistance(configuration["maxdistance"]);
					toolInstance.setPositionType(configuration["positionType"]);
					toolInstance.setScale(configuration["geoScale"]);
			//		toolInstance.setOrientation(configuration["geoOrientation"]);
	
				}
				if(dijit.byId("ptQr")!=null && dijit.byId("ptQr").attr('value') !== false){
					toolInstance.deletePosition();
					toolInstance.deleteMaxdistance();
					toolInstance.setPositionType(configuration["positionType"]);
					toolInstance.deleteScale();
					if (toolInstance.getLocation() != null){
						var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(toolInstance);
						var loc = toolInstanceOrigen.getLocation();
	    				var position = "http://qrickit.com/api/qr?qrsize=200&d=" + loc + "?callerUser=";    				toolInstance.setPosition(position);	
					}
			//		toolInstance.deleteOrientation();
				}
				if(dijit.byId("ptMarker")!=null && dijit.byId("ptMarker").attr('value') !== false) {
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
			}
			else{
				if(dijit.byId("ptNone")!=null && dijit.byId("ptNone").attr('value') !== false){
					toolInstance.deletePosition();
					toolInstance.deleteMaxdistance();
					toolInstance.deletePositionType();
					toolInstance.deleteScale();
					//toolInstance.deleteOrientation();
				}
				if(dijit.byId("ptGeo")!=null && dijit.byId("ptGeo").attr('value') !== false){
					toolInstance.setPosition(configuration["geoposition"]);
					toolInstance.setMaxdistance(configuration["maxdistance"]);
					toolInstance.setPositionType(configuration["positionType"]);
					toolInstance.setScale(configuration["geoScale"]);
					//toolInstance.setOrientation(configuration["geoOrientation"]);
				}
			}
		}


		var createTool = dojo.byId("rbCi");
		var dontReuse = dojo.byId("rbDontReuse");
		// Comprobar si se ha elegido reutilizar herramienta
		if ((createTool && !createTool.checked) || (dontReuse && !dontReuse.checked)) {
			// Actualizaci�n de la instancia de herramienta elegida
			var select = dojo.byId("selectToolInstance");
			if (select && select.options[select.selectedIndex].value != "0") {
				var newInstancedToolId = select.options[select.selectedIndex].value;
				toolInstance.setLocation(newInstancedToolId);

				// Comprobar si cambia la herramienta para actualizar el
				// identificador del recurso
				var sourceTool = toolInstance.getTool();
				var destinyTool = ToolInstanceContainer.getToolInstance(
						newInstancedToolId).getTool();
				if (sourceTool.getId() != destinyTool.getId()) {
					toolInstance.setTool(destinyTool);
					// Si no se usa la herramienta en otra instancia de la
					// actividad se borra como herramienta de la actividad
					var instanced = activity.getInstancedActivities();
					var used = false;
					for ( var i = 0; i < instanced.length; i++) {
						if (instanced[i].usesTool(sourceTool)) {
							used = true;
						}
					}
					if (used == false) {
						activity.deleteTool(sourceTool);
					}
				}

			}
		}
		else{
			//Borrar la reutilizaci�n de una herramienta interna
			if (dontReuse && dontReuse.checked)
			{
				toolInstance.deleteLocation();
			}
		}
	},

	/**
	 * Borra la instancia de herramienta cuyo id se proporciona
	 * 
	 * @param idToolInstance
	 *            Identificador de la instancia de herramienta
	 */
	deleteInstance : function(toolInstanceId) {
		var url = toolInstanceId;
		if (LdShakeManager.ldShakeMode){
			var url = LdShakeManager.buildLdshakeUrl(url);
		}
		ToolInstanceConfiguration.hideDeleteToolInstance();
		InformativeDialogs.showLoadingDialog(i18n.get("deletingToolInstance"));
		var xhrArgs = {
			url : url,
			handleAs : "text",
			timeout : 10000, // tiempo m�ximo de expera de 10 segundos
			load : function(data) {
				// Borrar el location
				var toolInstance = ToolInstanceContainer
						.getToolInstance(toolInstanceId);
				toolInstance.deleteLocation();
				JsonDB.notifyChanges();
				InformativeDialogs.hideLoadingDialog();
			},
			error : function() {
				InformativeDialogs.hideLoadingDialog();
				InformativeDialogs.showAlertDialog(i18n.get("warning"), i18n
						.get("notDeletedToolInstance"));
			}
		}
		var deferred = dojo.xhrDelete(xhrArgs);
	},

	/**
	 * Muestra la ventana de confirmaci�n del borrado de una instancia de
	 * herramienta
	 * 
	 * @param toolInstance
	 *            Instancia de herramienta a borrar
	 */
	showDeleteToolInstance : function(toolInstance) {
		var dlg = dijit.byId("dialogConfirmDeleteToolInstance");
		dlg.titleNode.innerHTML = i18n.get("confirmDelete");
		dojo.byId("dialogConfirmDeleteToolInstanceInfo").innerHTML = i18n
				.get("confirmDeleteToolInstance");
		dlg.show();
		dijit.byId("deleteToolInstanceId").setValue(toolInstance.getId());
	},

	/**
	 * OCulta la ventana de confirmaci�n del borrado de una instancia de
	 * herramienta
	 */
	hideDeleteToolInstance : function() {
		dijit.byId("dialogConfirmDeleteToolInstance").hide();
	}

}
