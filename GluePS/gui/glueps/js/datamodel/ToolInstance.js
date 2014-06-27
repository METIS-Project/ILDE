/**
 * Funcionalidad asociada a una instancia de herramienta
 */

var ToolInstance = function(data)
{
	
	var data = data;
		
	this.getId =  function(){
		return data.id;
	}
	
	this.getName =  function(){
		return data.name;
	}
	
	this.getTool = function(){
		return ToolContainer.getTool(data.resourceId);
	}
	
	this.getLocation = function(){
		if (data.location)
		{
			return data.location;
		}
		else{
			return false;
		}
	}
	
	//Added by Juan to allow geo-locate a tool instance
	this.getPosition =  function(){
		return data.position;
	}
	this.getMaxdistance =  function(){
		return data.maxdistance;
	}
	
	this.getPositionType =  function(){
		return data.positionType;
	}
	
	this.getScale =  function(){
		return data.scale;
	}
	this.getOrientation =  function(){
		return data.orientation;
	}
	
	this.deletePosition = function(){
		delete data.position;
	}
	this.deleteMaxdistance = function(){
		delete data.maxdistance;
	}
	this.deletePositionType = function(){
		delete data.positionType;
	}
	this.deleteScale = function(){
		delete data.scale;
	}
	this.deleteOrientation = function(){
		delete data.orientation;
	}
	
	
	
	
	this.setName = function(name){
		data.name = name;
	}
	
	this.setLocation = function(location){
		data.location = location;
	}
	
	this.deleteLocation = function(){
		delete data.location;
	}
	
	this.setTool = function(tool){
		data.resourceId = tool.getId();
	}
	
	//Added by Juan to allow geo-locate a tool instance
	this.setPosition = function(position){
		data.position = position;
	}
	this.setMaxdistance = function(maxdistance){
		data.maxdistance = maxdistance;
	}
	this.setPositionType = function(positionType){
		data.positionType = positionType;
	}
	this.setScale = function(scale){
		data.scale = scale;
	}
	this.setOrientation = function(orientation){
		data.orientation = orientation;
	}
	

	this.getValidGeoposition = function() {
		var hasGeoposition = null;
		var toolOrigen = ToolContainer.getTool(data.resourceId);
		var toolInstance = ToolInstanceContainer.getToolInstance(data.id);
		var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(toolInstance);
		var position = data.position;
		var positionType =  data.positionType;
		var validPosition = false;
		//Juan: junaiomarker
		//Sólo se muestra el icono si hay un marcador válido en el toolinstance o en el origen de reutilización
		if (positionType == "geoposition" && position) {
				var coordenadas = position;
				var coordenadasSplit = coordenadas.split(",");

				if (coordenadasSplit.lenght = 2) {
					var latitud = coordenadasSplit[0];
					var longitud = coordenadasSplit[1];
					  
					var validLatitud = ActivityToolInstanceMenu.isNumber(latitud);
					var validLongitud = ActivityToolInstanceMenu.isNumber(longitud);
					if (validLatitud && validLatitud) {
						validPosition = true;
					}					  
				}
				if (toolOrigen.getToolKind()=="external" && toolInstanceOrigen.getLocation() && !ToolInstanceReuse.reusesToolInstance(toolInstanceOrigen) && validPosition)
				{
					hasGeoposition = coordenadas;
				}
		}	
		return hasGeoposition;
	}
	
	this.getValidJunaioMarkerPosition = function(){
		var hasGeoposition = null;
		var toolOrigen = ToolContainer.getTool(data.resourceId);
		var toolInstance = ToolInstanceContainer.getToolInstance(data.id);
		var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(toolInstance);
		var position = data.position;
		var positionType =  data.positionType;
		//Juan: junaiomarker
		//Sólo se muestra el icono si hay un marcador válido en el toolinstance o en el origen de reutilización
		if (positionType == "junaiomarker" && position) {
			//Localizo ID de marker
			var myRegexp = /MetaioMarker(.*?).png/;
			var match = myRegexp.exec(position);
			if (match){
				var id = match[1];
				if (toolOrigen.getToolKind()=="external" && toolInstanceOrigen.getLocation() && !ToolInstanceReuse.reusesToolInstance(toolInstanceOrigen))
				{
					hasGeoposition = match[1];				}
			}
		}
		return hasGeoposition;
	}
	
	this.hasValidQrCodePosition = function(){
		
//		var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(data);
//		var toolOrigen = toolInstanceOrigen.getTool();
//		if (toolOrigen.getToolKind()=="external" && toolInstanceOrigen.getLocation() && !ToolInstanceReuse.reusesToolInstance(toolInstanceOrigen))
//		{
//			 var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(data);
//			 var location = toolInstanceOrigen.getLocation();
//			 var teachers = ParticipantContainer.getTeachers();
//			 var callerUserName = "";
//			 if (teachers.length>0)
//			 {
//			 callerUserName = teachers[0].getName();
//			 }
//		
//			 var url = "http://qrickit.com/api/qr?qrsize=200&d=" + location + "?callerUser=" + callerUserName;
//
//		
		
		var hasQrCodeposition = false;
		var toolOrigen = ToolContainer.getTool(data.resourceId);
		var toolInstance = ToolInstanceContainer.getToolInstance(data.id);
		var toolInstanceOrigen = ToolInstanceReuse.getOriginalToolInstance(toolInstance);
		var location = data.location;
		var position = data.position;
		var positionType =  data.positionType;
		if (positionType == "qrcode" && position && location) {
			if (position.indexOf("qrickit.com") !== -1){
				hasQrCodeposition = true;
			}

			
		}
		return hasQrCodeposition;
	}
	
}