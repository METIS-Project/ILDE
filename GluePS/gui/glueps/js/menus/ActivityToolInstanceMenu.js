/**
 * Menú de una instancia de una herramienta en una actividad
 */

var ActivityToolInstanceMenu = {
		
		init: function(){
			dojo.connect(dojo.byId("dialogConfirmDeleteToolInstanceActivityOk"), "onclick", function() {
				var idActivity = dijit.byId("deleteToolInstanceActivityId").getValue();
				var idInstancedActivity = dijit.byId("deleteToolInstanceInstancedActivityId").getValue();
				var idToolInstance = dijit.byId("deleteToolInstanceToolInstanceId").getValue();
				ActivityToolInstanceMenu.deleteToolInstanceActivity(idActivity, idInstancedActivity, idToolInstance);
			});
			
			dojo.connect(dojo.byId("dialogConfirmDeleteToolInstanceActivityCancel"), "onclick", function() {
				ActivityToolInstanceMenu.hideDeleteToolInstanceActivity();
			});
			
			
			dojo.connect(dojo.byId("dialogConfirmDeleteAllToolInstanceActivityOk"), "onclick", function() {
				ActivityToolInstanceMenu.hideDeleteAllToolInstanceActivity();
				var idActivity = dijit.byId("deleteAllToolInstanceActivityActId").getValue();				
				var idTool = dijit.byId("deleteAllToolInstanceActivityToolId").getValue();
				ToolInstanceManager.deleteToolInstancesActivity(idActivity, idTool);
			});

			dojo.connect(dojo.byId("dialogConfirmDeleteAllToolInstanceActivityCancel"), "onclick",  function() {
				ActivityToolInstanceMenu.hideDeleteAllToolInstanceActivity();
			});
		},
		
		/**
		 * Menú de opciones de un recurso
		 * 
		 * @param data
		 */
		getToolInstanceMenu : function(data) {
			var items = new Array();
			//if (!data.toolInstance.getLocation())

				items.push({
					label : i18n.get("ConfigureTool"),
					icon: "edit",
					onClick : function(data) {
						if (!data.toolInstance.getLocation() ||ToolInstanceReuse.reusesToolInstance(data.toolInstance))
						{
							ToolInstanceConfiguration.toolInstanceConfiguration(data.toolInstance, data.position, data.activity, data.instancedActivity);
						} else {
							ToolInstanceConfiguration.toolInstanceUpdateConfig(data.toolInstance, data.position, data.activity, data.instancedActivity);
							
						}
					},
					data : data,
					help : i18n.get("ConfigureToolInfo")
				});
			
			
			//Obtenemos la instancia de herramienta origen de las referencias
			var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(data.toolInstance);
			var toolOrigen = toolInstanceOrigen.getTool();
			if (toolOrigen.getToolKind()=="external" && toolInstanceOrigen.getLocation() && !ToolInstanceReuse.reusesToolInstance(toolInstanceOrigen))
			{
				items.push({
					label: i18n.get("activityResourceMenuOpen"),
					icon: "lupa",
					onClick: function(data) {
						var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(data.toolInstance);
						ActivityToolInstanceMenu.openLocation(toolInstanceOrigen);
					},
					data: data,
					help: i18n.get("activityResourceMenuOpenHelp")
				});
			}
			
			
			//Juan: Meter QR code en menú
			var hasQrPosition = data.toolInstance.hasValidQrCodePosition();
			if (hasQrPosition){
				
				var url = data.toolInstance.getPosition();
//				var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(data.toolInstance);
//				var toolOrigen = toolInstanceOrigen.getTool();
//				if (toolOrigen.getToolKind()=="external" && toolInstanceOrigen.getLocation() && !ToolInstanceReuse.reusesToolInstance(toolInstanceOrigen))
//				{
//					 var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(data.toolInstance);
//					 var location = toolInstanceOrigen.getLocation();
//					 var teachers = ParticipantContainer.getTeachers();
//					 var callerUserName = "";
//					 var url = "http://qrickit.com/api/qr?qrsize=200&d=" + location + "?callerUser=" + callerUserName;
//					 if (teachers.length>0)
//					 {
//					 callerUserName = teachers[0].getName();
//					 }
					items.push({
						label: i18n.get("activityResourceMenuQR"),
						icon: "qr.png",
						onClick: function(data) {						
							 window.open(url); 
						},
						data: data,
						help: i18n.get("activityResourceMenuQRHelp") +  "<br><img src=\"" + url + "\"></img>"
					});
//				}

			}
			//Juan: Hasta aquí modificación
			
			//Juan: Meter LLA marker de junaio en menú	
			
//			// Lo quito porque el funcionamiento actual de junaio no es bueno y despista.
//			var coordenadas = data.toolInstance.getValidGeoposition();
//			if (coordenadas != null){
//				items.push({
//					label: i18n.get("activityResourceMenuLLA"),
//					icon: "LLAmarkerJunaio.png",
//					onClick: function(data) {
//						// El HTTP POST para crear el LLA marker ya no es necesario, debido a que junaio ha implementado los LLA al vuelo, sin necesidad de crearlos		
//						 // HTTP POST para crear el LLA marker en caso de que no exista
//						 var coordenadasSplit = coordenadas.split(",");
//						 var latitud = coordenadasSplit[0];
//						 var longitud = coordenadasSplit[1];
//						 var url2 = "http://dev.junaio.com/tools/createmarkerfrompgm/user/jamcristobal/imagePath/LAT" + latitud + "_LON" + longitud + "_ALT0.0000000.pgm+";
//						 window.open(url2); 
//					},
//					data: data,
//					help: i18n.get("activityResourceMenuLLAHelp")
//				});
//			}
			
			
			
			// Icono de geoposition o de junaio marker en menú (sólo si el positionType es el correspondiente y el position es válido)
			//Sólo se muestra el icono si hay un campo de geoposición válido en el toolinstance o en el origen de reutilización

			var toolInstance = data.toolInstance;
			var markerId = toolInstance.getValidJunaioMarkerPosition();
			var position = toolInstance.getPosition();
			var coordenadas = toolInstance.getValidGeoposition();
			if (markerId != null){			
				items.push({
					label: i18n.get("activityResourceMenuJunaiomarker") + " (ID " + markerId + ")",
					icon: "MetaioMarker21.png",
					onClick: function(data) {
						 var url2 = position.replace(/MetaioMarker.*\.png$/, 'Marker.pdf');
						 window.open(url2); 
					},
					data: data,
					help: i18n.get("activityResourceMenuJunaiomarkerHelp")
				});
			} else if (coordenadas != null){
				items.push({
					label: i18n.get("activityResourceMenuGeoposition"),
					icon: "geoposition.png",
					onClick: function(data) {
						 var url2 = "https://maps.google.com/maps?q=" + coordenadas + "&ie=UTF8&z=15&iwloc=addr&t=m&output=embed";
					//	 var url2 = "http://maps.google.com/maps/api/staticmap?zoom=14&size=100x100&maptype=roadmap&markers=color:blue|label:|" + coordenadas + "&sensor=false";
						 window.open(url2); 
					},
					data: data,
					help: i18n.get("activityResourceMenuGeopositionHelp")
				});
			}
			
			

			
			var toolVle = data.toolInstance.getTool().getToolVle();
			//Las herramientas internas de tipo moodle no pueden configurarse
			if (LearningEnvironment.isInternal(toolVle.key)==false || toolVle.value.search(/(Moodle)/i)== -1)
			{
				items.push({
					label : i18n.get("activityResourceMenuReuse"),
					icon: "reset",
					onClick : function(data) {
						//Mostrar seleccionada la instancia de herramienta 
						var toolInstances = ActivityPainter.positions[data.activity.getId()].instAct[data.instancedActivity.getId()].toolInstances;
						for (var i=0; i < toolInstances.length; i++)
						{
							if (toolInstances[i].id == data.toolInstance.getId())
							{
								var text = toolInstances[i].text;
								text.setFill('#6600FF'); //Se cambia el color del texto
								break;
							}
						}
						
						StateManager.changeToolInstanceReuse(data.toolInstance, data.activity, data.instancedActivity);
					},
					data : data,
					help : i18n.get("activityResourceMenuReuseHelp")
				});
			}
			
			
			
			/*else{
				items.push({
					label : i18n.get("activityResourceMenuReuse"),
					icon: "reset",
					onClick : function(data) {
						StateManager.changeToolInstanceReuse(data.toolInstance, data.activity, data.instancedActivity);
					},
					data : data,
					help : i18n.get("activityResourceMenuReuseHelp"),
					isDisabled: true
				});
			}*/
			items.push({
				label : i18n.get("activityResourceMenuDelete"),
				icon: "delete",
				onClick : function(data) {					
					ActivityToolInstanceMenu.showDeleteToolInstanceActivity(data.activity, data.instancedActivity, data.toolInstance);
				},
				data : data,
				help : i18n.get("activityResourceMenuDeleteHelp")
			});
			if (data.toolInstance.getTool().getToolKind()=="external" && data.toolInstance.getLocation() && !ToolInstanceReuse.reusesToolInstance(data.toolInstance))
			{
				items.push({
					label: i18n.get("DeleteTool"),
					icon: "erase.png",
					onClick: function(data) {
						ToolInstanceConfiguration.showDeleteToolInstance(data.toolInstance);
					},
					data: data,
					help: i18n.get("DeleteToolInfo")
				});
				
				items.push({
					label: i18n.get("DeleteAllTool"),
					icon: "erase_all.png",
					onClick: function(data) {
						ActivityToolInstanceMenu.showDeleteAllToolInstanceActivity(data.activity, data.toolInstance.getTool());
					},
					data: data,
					help: i18n.get("DeleteAllToolInfo")
				});
				
			}
			if (ToolInstanceReuse.reusesToolInstance(data.toolInstance))
			{
				items.push({
					label: i18n.get("StopReusingTool"),
					icon: "delete_reset.png",
					onClick: function(data) {
						//Borro el location asociado a la herramienta
						data.toolInstance.deleteLocation();
						JsonDB.notifyChanges();
					},
					data: data,
					help: i18n.get("StopReusingToolInfo")
				});
			}
			return items;
		},
		
		
		openLocation: function(toolInstance){
			var location = toolInstance.getLocation();
			//Se proporciona como callerUser el usuario de moodle indicado en el LearningEnvironment
			var callerUserName = LearningEnvironment.getCreduser(); 
			var url = location + "?callerUser=" + callerUserName;
			window.open(url);
		},
		
		/**
		 * Muestra la ventana de confirmación de una instancia de herramienta de la actividad
		 */
		showDeleteToolInstanceActivity: function(activity, instancedActivity, toolIstance)
		{
	        var dlg = dijit.byId("dialogConfirmDeleteToolInstanceActivity");
	        dlg.titleNode.innerHTML = i18n.get("confirmDelete");
	        dojo.byId("dialogConfirmDeleteToolInstanceActivityInfo").innerHTML= i18n.get("confirmDeleteToolInstanceActivity");
	        dlg.show();
			dijit.byId("deleteToolInstanceActivityId").setValue(activity.getId());
			dijit.byId("deleteToolInstanceInstancedActivityId").setValue(instancedActivity.getId());
			dijit.byId("deleteToolInstanceToolInstanceId").setValue( toolIstance.getId());
		},
		
		/**
		 * OCulta la ventana de confirmación del borrado de un grupo
		 */
		hideDeleteToolInstanceActivity: function()
		{
			dijit.byId("dialogConfirmDeleteToolInstanceActivity").hide();
		},
		
		
		deleteToolInstanceActivity: function(activityId, instancedActivityId, toolInstanceId)
		{
			ActivityToolInstanceMenu.hideDeleteToolInstanceActivity();
			var activity = ActivityContainer.getFinalActivity(activityId);
			var instancedActivity = InstancedActivityContainer.getInstancedActivity(instancedActivityId);
			var toolInstance = ToolInstanceContainer.getToolInstance(toolInstanceId);
			var ok = ToolInstanceManager.deleteToolInstanceGroup(activity, instancedActivity, toolInstance);
			//Mostrar información si ha fallado el borrado
			if (!ok)
			{
				
			}
			JsonDB.notifyChanges();
		},
		
		//Juan: Funci�n para validar que una entrada es un n�mero
		isNumber: function(n) {
			  return !isNaN(parseFloat(n)) && isFinite(n);
		},
		
		
		/**
		 * Muestra la ventana de confirmación del borrado de todas las instancias creadas de una herramienta en la actividad
		 * 
		 * @param activity Actividad de la que se borrarán las instancias de herramienta
		 * @param tool Herramienta de la que se borrarán sus instancias creadas
		 */
		showDeleteAllToolInstanceActivity : function(activity, tool) {
			var dlg = dijit.byId("dialogConfirmDeleteAllToolInstanceActivity");
			dlg.titleNode.innerHTML = i18n.get("confirmDelete");
			dojo.byId("dialogConfirmDeleteAllToolInstanceActivityInfo").innerHTML = i18n.get("confirmDeleteAllToolInstanceActivity");
			dlg.show();
			dijit.byId("deleteAllToolInstanceActivityActId").setValue(activity.getId());
			dijit.byId("deleteAllToolInstanceActivityToolId").setValue(tool.getId());
		},

		/**
		 * Oculta la ventana de confirmación del borrado de todas las instancias creadas de una herramienta en la actividad
		 */
		hideDeleteAllToolInstanceActivity : function() {
			dijit.byId("dialogConfirmDeleteAllToolInstanceActivity").hide();
		}

		
		
		
}