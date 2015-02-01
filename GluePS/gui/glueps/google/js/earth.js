/**
 * Functions related to the Google Earth view which is showed in the UI
 */

/**
 * This function is called when loading the google earth plugin
 */
function initCallback(pluginInstance) {
	ge = pluginInstance;
	ge.getWindow().setVisibility(true);

	// add a navigation control
	ge.getNavigationControl().setVisibility(ge.VISIBILITY_AUTO);

	// add some layers
	ge.getLayerRoot().enableLayerById(ge.LAYER_BORDERS, true);
	ge.getLayerRoot().enableLayerById(ge.LAYER_TERRAIN , true);
	ge.getLayerRoot().enableLayerById(ge.LAYER_BUILDINGS  , true);
	ge.getLayerRoot().enableLayerById(ge.LAYER_TREES  , true);

	//disable the ground level view
	ge.getOptions().setAutoGroundLevelViewEnabled(false);
	//disable the street views
	ge.getNavigationControl().setStreetViewEnabled(false);

	google.earth.addEventListener(ge.getView(), 'viewchangeend', function() {
		//var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);
		//console.log("latitude: " + camera.getLatitude() + ", longitude: " + camera.getLongitude() + ", altitude: " + camera.getAltitude() + ", heading: " + camera.getHeading() + ", tilt: " + camera.getTilt() + ", roll: " + camera.getRoll());
		refreshPois();
	});
	resizeMapDiv();
	// create the buttons
	createCustomizedButtons();
	// show some buttons
	showNativeHTMLButton("firstPersonViewButton");
	showNativeHTMLButton("mapViewButton");
	
	$("#selectBucket").change(function() {
		if (groundView == true){//Make sure that we are in the ground level
			 var value = $(this).val();
			 if (value != ''){
				 for (var j = 0; j < poisArray.length; j++){
					 if (value == poisArray[j].id){
							var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);
							var latitude = camera.getLatitude();
							var longitude = camera.getLongitude();
							var poiHref = poisArray[j].webpage + "&p=" + latitude + "," + longitude;
							var mapHeight = ($("#mapContainer").outerHeight(false) + $("#firstPersonCamControls").outerHeight(false) - 150);
							var iframeWidth = ($("#mapContainer").outerWidth(false) - 40);
							var mainWidth = ($("#mainContainer").outerWidth(false));
							$("#iframe").html('<iframe src="' + decodeURI(poiHref) + '" width="' + iframeWidth + '" height="' + mapHeight + '" scrolling="auto" frameborder="1"></iframe>');
							$("#bucketTitle").html($.i18n.prop('select.bucket.title') + " " + $(this).find(":selected").text());
							$("#close-dialog").html($.i18n.prop('select.bucket.close'));
							$("#myModal").css("width",mainWidth);
							$('#myModal').modal({
								  backdrop: 'static',
								  keyboard: false,
								  show: true
								});
							$("#mapContainer").css("display", "none");
							$('#myModal').on('hide.bs.modal', function () {
								$("#mapContainer").css("display", "block");
							});
							$("#selectBucket option[value='']").attr("selected", true);
					 }
				 }
			 }
		}
	 });
	
	 $("#selectPoi").change(function(e) {
		 if (mapsMode==false){
			 var value = $(this).val();
			 if (value != ''){
				 for (var j = 0; j < poisArray.length; j++){
					 if (value == poisArray[j].id){
						 var range = Math.pow(2, 27 - ZOOM_TO_POI);
						 moveToPois(poisArray[j], range,true);
					 }
				 }
			 }
		 }	 
	 });
	 
	 $("#selectPerson").change(function(e) {
		 if (mapsMode==false){
			 var value = $(this).val();
			 if (value != ''){
				 for (var j = 0; j < poisArray.length; j++){
					 if (value == poisArray[j].id){
						 var range = Math.pow(2, 27 - ZOOM_TO_POI);
						 moveToPois(poisArray[j], range,true);
					 }
				 }
			 }
		 }
	 });
	 
	 $("#searchTargetEarth").click(function(e){
		 var geocoder = new google.maps.Geocoder();
		 var address = $("#targetEarth").val();
		 if (address == ""){
			 alert($.i18n.prop('go.empty'));
		 }else{
		    geocoder.geocode( { 'address': address}, function(results, status) {
		      if (status == google.maps.GeocoderStatus.OK) {
				var center = results[0].geometry.location;
				var latitude = center.lat();
				var longitude = center.lng();
				var lookAt = ge.getView().copyAsLookAt(ge.ALTITUDE_RELATIVE_TO_GROUND);
				var range = lookAt.getRange();
				moveToPointExact(latitude, longitude, range);
				//Refresh the pois of the earth view    
				refreshPois();
		      } else {
		    	  alert($.i18n.prop('go.notFound'));
		      }
		    });
		    $("#targetEarth").val("");
		 }
	 });
  	
	//notifyEarthReady();
	 getPois(false);
}

function failureCallback(errorCode) {
	console.log("se ha producido un error" + errorCode);
}

/**
 * Get the pois using as the current position the geolocation value
 * @param position
 */
function getPoisByGeolocation(position){
	var latitude = position.coords.latitude;
	var longitude = position.coords.longitude;
	moveToPointExact(latitude,longitude,4000);
}

/*
 * This function is called when there is an error getting the geolocation
 */
function geolocationError(error){
	switch(error.code) 
	{
	    case error.PERMISSION_DENIED:
	    	console.log("User denied the request for Geolocation.");
	    	break;
	    case error.POSITION_UNAVAILABLE:
	    	console.log("Location information is unavailable.");
	    	break;
	    case error.TIMEOUT:
	    	console.log("The request to get user location timed out.");
	    	break;
	    case error.UNKNOWN_ERROR:
	    	console.log("An unknown error occurred trying to get the Geolocation.");
	    	break;
	}
}

/**
 * Move to the geolocation provided by the browser
 */
function moveToGeolocation(){
	if (navigator.geolocation){
	      // timeout at 6000 milliseconds (6 seconds)
	    var options = {timeout:6000};
		navigator.geolocation.getCurrentPosition(getPoisByGeolocation,geolocationError, options);
	}else{
		console.log("Geolocation is not supported by this browser.");
	}
}

function getPois(update){
	if (firstTimeEarth){
		var latitude = 40.4167754;
		var longitude = -3.7037901999999576;
	}else{
		var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);
		var latitude = camera.getLatitude();
		var longitude = camera.getLongitude();
	}
		$.getJSON(
			"/GLUEPSManager/arbrowser/earth/pois/search/"+deployId+"/"+username,
			{latitude: latitude, longitude: longitude},
			function(data) {
				//console.log(data);
				//Update the pois array
				poisArray = data.results;
				for (var i = 0; i < poisArray.length; i++){
					poisArray[i].lat = parseFloat(poisArray[i].lat);
					poisArray[i].lng = parseFloat(poisArray[i].lng);
					if (poisArray[i].orientation!=null){
						poisArray[i].orientation = parseFloat(poisArray[i].orientation);
					}
				}
				//Remove the old ones
				removePois();
				//Place the new pois
				placePois(poisArray);
				//Add the option to the select poi
				addOptionSelectPoi();
				//Add the option to the select person
				addOptionSelectPerson();
				//Add the option to the select bucket
				addOptionSelectBucket();
				if (!update){
					$("#selectDeploy").removeAttr("disabled");
				}
				if (firstTimeEarth){
					firstTimeEarth = false;
					notifyEarthReady();
				}
				if (!update && poisArray.length > 0) {
					//Move towards the first one
					moveToPois(poisArray[0], 4000);
				}else if (!update && poisArray.length == 0){
					//Move to the current geolocation of the navigator
					moveToGeolocation();
				}
				/*if (!update){
					moveToGeolocation();
				}*/
			});
}


function moveToPois(pois, range,tilt){
	//Create a new lookAt object and set it as the view
	/*var lookAt = ge.createLookAt('');
	lookAt.setLatitude(pois.lat);
	lookAt.setLongitude(pois.lng);
	lookAt.setRange(range);
	ge.getView().setAbstractView(lookAt);*/
	moveToPointExact(pois.lat,pois.lng,range,tilt);
}

function moveToPoint(latitude, longitude, range){
	//Create a new lookAt object and set it as the view
	var lookAt = ge.createLookAt('');
	lookAt.setLatitude(latitude);
	lookAt.setLongitude(longitude);
	lookAt.setRange(range);
	ge.getView().setAbstractView(lookAt);
}

function moveToPointExact(latitude, longitude, range, tilt){
	var lookAt = ge.createLookAt('');
	lookAt.setLatitude(latitude);
	lookAt.setLongitude(longitude);
	lookAt.setRange(range);
	lookAt.setHeading(0);
	lookAt.setAltitude(0);
	if (tilt) {
	    // Teleport to the pre-tilt view immediately.
	    ge.getOptions().setFlyToSpeed(5);
	    ge.getView().setAbstractView(lookAt);
	    //lookAt.setTilt(15);
	    lookAt.setTilt(0);
	    // Fly to the tilt at regular speed in 200ms
	    ge.getOptions().setFlyToSpeed(0.75);
	    window.setTimeout(function() {
	      ge.getView().setAbstractView(lookAt);
	    }, 200);
	    // Set the flyto speed back to default after the animation starts.
	    window.setTimeout(function() {
	      ge.getOptions().setFlyToSpeed(1);
	    }, 250);
	} else {
	    // Fly to the approximate map view at regular speed.
	    ge.getView().setAbstractView(lookAt);
	}
}

function refreshPois(){
	//Remove the old ones
	removePois();
	//Place the new pois
	placePois(poisArray);
	//Add the option to the select poi
	addOptionSelectPoi();
	//Add the option to the select person
	addOptionSelectPerson();
}

function removePois(){
	var toDelete = [];
	for(var id in placemarks){
		var exists = false;
		for (var j = 0; j < poisArray.length; j++){
			if (id == poisArray[j].id || id == (poisArray[j].id + "_3d")){
				var latitude = placemarks[id].lat;
				var longitude = placemarks[id].lng;
				//check that the position for the point has not changed
				if (latitude == poisArray[j].lat && longitude==poisArray[j].lng){
					exists = true;
				}
				//break;
			}
		}
		if (!exists){
			toDelete.push(id);

		}
	}
	for (var i=0; i < toDelete.length; i++){
		var id = toDelete[i];
		if (typeof placemarks[id]!="undefined"){
			ge.getFeatures().removeChild(placemarks[id].obj);
			delete placemarks[id];
		}
	}
}

function placePois(pois) {
	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);
	var latitude = camera.getLatitude();
	var longitude = camera.getLongitude();
	var altitude = camera.getAltitude();
	var heading = camera.getHeading();
	
	$.each(pois, function() {
			
		var newPoi;
		if (typeof placemarks[this.id]=="undefined"){
			newPoi = true;
		}else{
			newPoi = false;
		}
		
		if (!newPoi && ((distFrom(latitude,longitude, this.lat, this.lng) < DISTANCE_TO_PERSON) && altitude < ALTITUDE_TO_PERSON) && this.poitype == 'user')
		{
			var placemark = placemarks[this.id].obj;
			placemark.getStyleSelector().getIconStyle().getIcon().setHref(window.location.href.substring(0, window.location.href.lastIndexOf("/google/")) + '/google/images/point.png');
			placemark.setName(this.title);
			
			//Create 3d if necessary when we are near the poi
			if (typeof placemarks[this.id + "_3d"] == "undefined"){
				var placemark2 = ge.createPlacemark('');
				placemark2.setName(this.title);
		
				// Placemark/Model (geometry)
				var model = ge.createModel('');
				var scale = ge.createScale('');
				scale.set(0.9,0.9,0.9);
				model.setScale(scale);
				var orientation = ge.createOrientation('');
				//orientation.set(camera.getHeading(), camera.getTilt(), camera.getRoll());
				if (this.orientation == null){
					orientation.setHeading((heading + 90) % 360); //By default the user is looking at the camera
				}else{
					orientation.setHeading((this.orientation + 270) % 360);
				}
				model.setOrientation(orientation);
				placemark2.setGeometry(model);
		
				// Placemark/Model/Link
				var link = ge.createLink('');
				link.setHref('https://dl.dropboxusercontent.com/s/43gklynm1ol4j8a/persona1.dae');
				model.setLink(link);
				
				// Placemark/Model/Location
				var loc = ge.createLocation('');
				loc.setLatitude(this.lat);
				loc.setLongitude(this.lng);
				model.setLocation(loc);
		
				// add the model placemark to Earth
				ge.getFeatures().appendChild(placemark2);
				
				placemarks[this.id + "_3d"] = {
						obj: placemark2,
						lat: this.lat,
						lng: this.lng
				};	
			}
		}
		else if (!newPoi && ((distFrom(latitude,longitude, this.lat, this.lng) >= DISTANCE_TO_PERSON ) || altitude >= ALTITUDE_TO_PERSON) && this.poitype == 'user')
		{
			//change the icon for the poi
			var placemark = placemarks[this.id].obj;
			placemark.getStyleSelector().getIconStyle().getIcon().setHref(window.location.href.substring(0, window.location.href.lastIndexOf("/google/")) + '/google/images/manBig.png');
			/*if (groundView == false){
				placemark.getStyleSelector().getIconStyle().setScale(1.0);
			}else{
				placemark.getStyleSelector().getIconStyle().setScale(3.0);
			}*/
			//destroy the 3D model
			if (typeof placemarks[this.id + "_3d"] != "undefined"){
				ge.getFeatures().removeChild(placemarks[this.id + "_3d"].obj);
				delete placemarks[this.id + "_3d"];
			}
			
		}
		
		
		if (newPoi && distFrom(latitude,longitude, this.lat, this.lng) < this.maxdistance){
		
			if (this.poitype != '3dmodel' && this.poitype != 'user'){
			//if (this.poitype != '3dmodel'){	
				//Create the placemark
				var placemark = ge.createPlacemark('');
				placemark.setName(this.title);
				
				// Define a custom icon.
				var icon = ge.createIcon('');
				icon.setHref(window.location.href.substring(0, window.location.href.lastIndexOf("/google/")) + '/google/images/glue.png');
				var style = ge.createStyle(''); //create a new style
				style.getIconStyle().setIcon(icon); //apply the icon to the style
				placemark.setStyleSelector(style); //apply the style to the placemark
				
				//Set the placemark's location
				var point = ge.createPoint('');
				point.setLatitude(this.lat);
				point.setLongitude(this.lng);
				//console.log(this.lat)
				placemark.setGeometry(point);
				
				//Add the placemark to earth
				ge.getFeatures().appendChild(placemark);
				
				placemarks[this.id] = {
						obj: placemark,
						lat: this.lat,
						lng: this.lng
				};
				var item = this;
				google.earth.addEventListener(placemark, 'click', function(event) {
					var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);
					var latitude = camera.getLatitude();
					var longitude = camera.getLongitude();
					var heading = camera.getHeading();
					if (item.poitype == "bucket"){
						var poiHref = item.webpage + "&p=" + latitude + "," + longitude;
					}else{
						var poiHref = item.webpage;
					}					
					
					// Prevent the default balloon from appearing.
					event.preventDefault();
					var balloon = ge.createHtmlStringBalloon('');
					//set the resource the balloon is associated to
					balloon.setFeature(placemark);
					//Set the content of the balloon
					balloon.setMaxWidth(400);
					balloon.setMaxHeight(400);
					balloon.setContentString('<div style="text-align:right"><input id="closeBalloon" type="image" src="images/close.png" onclick="ge.setBalloon(null)"></div><div style="text-align:center"><a style="margin:20px" data-poi-id="'+item.id +
							'" class="poiResource btn btn-large" onclick="postInteraction(' + latitude + ',' + longitude +',' + heading + ",'" + item.id + "'" + ')" target="_blank" href="' + 
							decodeURI(poiHref)+ '">' + item.title + '</a></div><div style="padding:10px">'+
							item.description+'</div>');
					ge.setBalloon(balloon);
				});
		
			}else if (this.poitype == 'user'){
				//Create the placemark
				var placemark = ge.createPlacemark('');
				placemark.setName(this.title);
				
				// Define a custom icon.
				var icon = ge.createIcon('');
				if ((distFrom(latitude,longitude, this.lat, this.lng) < DISTANCE_TO_PERSON) && altitude < ALTITUDE_TO_PERSON){
					icon.setHref(window.location.href.substring(0, window.location.href.lastIndexOf("/google/")) + '/google/images/point.png');
				}
				else{
					icon.setHref(window.location.href.substring(0, window.location.href.lastIndexOf("/google/")) + '/google/images/manBig.png');
					/*if (groundView == false){
						placemark.getStyleSelector().getIconStyle().setScale(1.0);
					}else{
						placemark.getStyleSelector().getIconStyle().setScale(3.0);
					}*/
				}
				var style = ge.createStyle(''); //create a new style
				style.getIconStyle().setIcon(icon); //apply the icon to the style
				placemark.setStyleSelector(style); //apply the style to the placemark
				
				//Set the placemark's location
				var point = ge.createPoint('');
				point.setLatitude(this.lat);
				point.setLongitude(this.lng);
				//console.log(this.lat)
				placemark.setGeometry(point);
				
				//Add the placemark to earth
				ge.getFeatures().appendChild(placemark);
				
				placemarks[this.id] = {
						obj: placemark,
						lat: this.lat,
						lng: this.lng
				};
				
				if ((distFrom(latitude,longitude, this.lat, this.lng) < DISTANCE_TO_PERSON) && altitude < ALTITUDE_TO_PERSON){
					var placemark2 = ge.createPlacemark('');
					placemark2.setName(this.title);
			
					// Placemark/Model (geometry)
					var model = ge.createModel('');
					var scale = ge.createScale('');
					scale.set(0.9,0.9,0.9);
					model.setScale(scale);
					var orientation = ge.createOrientation('');
					//orientation.set(camera.getHeading(), camera.getTilt(), camera.getRoll());
					
					if (this.orientation == null){
						orientation.setHeading((heading + 90) % 360); //By default the user is looking at the camera
					}else{
						orientation.setHeading((this.orientation + 270) % 360);
					}
					model.setOrientation(orientation);
					placemark2.setGeometry(model);
			
					// Placemark/Model/Link
					var link = ge.createLink('');
					link.setHref('https://dl.dropboxusercontent.com/s/43gklynm1ol4j8a/persona1.dae');;
					model.setLink(link);
					
					// Placemark/Model/Location
					var loc = ge.createLocation('');
					loc.setLatitude(this.lat);
					loc.setLongitude(this.lng);
					model.setLocation(loc);
			
					// add the model placemark to Earth
					ge.getFeatures().appendChild(placemark2);
					
					placemarks[this.id + "_3d"] = {
							obj: placemark2,
							lat: this.lat,
							lng: this.lng
					};
				}
				
				
			}else if (this.poitype == '3dmodel'){
				// Placemark
				var placemark2 = ge.createPlacemark('');
				placemark2.setName('model');
		
				// Placemark/Model (geometry)
				var model = ge.createModel('');
				placemark2.setGeometry(model);
		
				// Placemark/Model/Link
				var link = ge.createLink('');
				link.setHref(this.webpage);
				model.setLink(link);
		
		
				// Placemark/Model/Location
				var loc = ge.createLocation('');
				loc.setLatitude(this.lat);
				loc.setLongitude(this.lng);
				model.setLocation(loc);
		
				// add the model placemark to Earth
				ge.getFeatures().appendChild(placemark2);
				
				placemarks[this.id] = {
						obj: placemark2,
						lat: this.lat,
						lng: this.lng
				};
				
				// zoom into the model
				/*lookAt.setRange(300);
				lookAt.setTilt(80);
				ge.getView().setAbstractView(lookAt);*/
			}
		}else if (!newPoi && distFrom(latitude,longitude, this.lat, this.lng) >= this.maxdistance){
			ge.getFeatures().removeChild(placemarks[this.id].obj);
			delete placemarks[this.id];			
			if (typeof placemarks[this.id + "_3d"] != "undefined"){
				ge.getFeatures().removeChild(placemarks[this.id + "_3d"].obj);
				delete placemarks[this.id + "_3d"];
			}
		}
	});
	//onclick event for the poiResource
	/*$('.poiResource').click(function(){
		console.log("clic_poi: " + $(this).attr('data-poi-id'));
		$.post("/GLUEPSManager/arbrowser/earth/pois/event/",
				{ username: username ,message: "interaction", deployid: deployId, uid: uid, l: latitude + "," + longitude, orientation: heading, id: $(this).attr('data-poi-id')});
	});*/

}

function addOptionSelectPoi(){
	var options = 0;
	$("#selectPoi").html("");
	$("<option>", {value: '', text: $.i18n.prop('select.poi')}).appendTo("#selectPoi");
	
	for(var id in placemarks) {
		for (var j = 0; j < poisArray.length; j++){
			if (id == poisArray[j].id && poisArray[j].poitype != 'user'){
			    $("<option>", {value:  poisArray[j].id, text:  poisArray[j].title}).appendTo("#selectPoi");
			    options++;
			    break;
			}
		}
	}
	if (options == 0){
		$("#selectPoi").attr("disabled","disabled");
	}else{
		$("#selectPoi").removeAttr("disabled");
	}
}

function addOptionSelectPerson(){
	var options = 0;
	$("#selectPerson").html("");
	$("<option>", {value: '', text: $.i18n.prop('select.person')}).appendTo("#selectPerson");
	
	for(var id in placemarks) {
		for (var j = 0; j < poisArray.length; j++){
			if (id == poisArray[j].id && poisArray[j].poitype == 'user'){
			    $("<option>", {value:  poisArray[j].id, text:  poisArray[j].title}).appendTo("#selectPerson");
			    options++;
			    break;
			}
		}
	}
	if (options == 0){
		$("#selectPerson").attr("disabled","disabled");
	}else{
		$("#selectPerson").removeAttr("disabled");
	}
}

function addOptionSelectBucket(){
	var options = 0;
	$("#selectBucket").html("");
	$("<option>", {value: '', text: $.i18n.prop('select.bucket')}).appendTo("#selectBucket");
	
	for(var id in placemarks) {
		for (var j = 0; j < poisArray.length; j++){
			if (id == poisArray[j].id && poisArray[j].poitype == 'bucket'){
			    $("<option>", {value:  poisArray[j].id, text:  poisArray[j].title}).appendTo("#selectBucket");
			    options++;
			    break;
			}
		}
	}
	if (options == 0){
		$("#selectBucket").attr("disabled","disabled");
	}else{
		$("#selectBucket").removeAttr("disabled");
	}
}

