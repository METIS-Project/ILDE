/**
 * Functions related to the Google map which is showed in the UI
 */

function initialize() {

  var initialPlace = new google.maps.LatLng(40.4167754, -3.7037901999999576);
  // Set up the map
  
  var mapOptions = {
    center: initialPlace,
    zoom: 18,
    mapTypeId: google.maps.MapTypeId.SATELLITE,
    mapTypeControl: true,
    mapTypeControlOptions: {
        style: google.maps.MapTypeControlStyle.HORIZONTAL_BAR,
        position: google.maps.ControlPosition.TOP_RIGHT
    },
    panControl: true,
    panControlOptions: {
        position: google.maps.ControlPosition.RIGHT_TOP
    },
    zoomControl: true,
    zoomControlOptions: {
        style: google.maps.ZoomControlStyle.LARGE,
        position: google.maps.ControlPosition.RIGHT_CENTER
    },
    streetViewControl: true
  };
  map = new google.maps.Map(document.getElementById('map_canvas'),
      mapOptions);
  
  var panoramaOptions = {
	  addressControlOptions:{
		    position: google.maps.ControlPosition.TOP_CENTER
	  },
	  panControl: true,
	  panControlOptions: {
	        position: google.maps.ControlPosition.RIGHT_TOP
	  },
	  zoomControl: true,
	  zoomControlOptions: {
		  style: google.maps.ZoomControlStyle.LARGE,
		  position: google.maps.ControlPosition.RIGHT_CENTER
	 }
  };
  
  // We get the map's default panorama and set up some defaults.
  // Note that we don't yet set it visible.
  var panorama = map.getStreetView();
  panorama.setOptions(panoramaOptions);
  panorama.setPosition(initialPlace);
  panorama.setPov(/** @type {google.maps.StreetViewPov} */({
    heading: 265,
    pitch: 0
  }));
  
  google.maps.event.addListener(panorama, 'visible_changed', function() {
	  if (mapsMode == true){
		  var panorama = map.getStreetView();
			//Check if we are in the street view and show the select in that case
			if (panorama.getVisible()==true){
				changeViewToStreetViewMode();
				for(var id in markers) {
					var icon = markers[id].obj.icon;
					if (icon.url =="images/manBig.png"){
						icon.size.width=128;
						icon.size.height=128;
						icon.url='images/manExtraBig.png';
					}
				}
			}else{
				for(var id in markers) {
					var icon = markers[id].obj.icon;
					if (icon.url =="images/manExtraBig.png"){
						icon.size.width=64;
						icon.size.height=64;
						icon.url='images/manBig.png';
					}
				}
				//recover the normal view
				$("#toggleEarthMaps").css("display", "inline");
				$("#selectPoi").css("display", "inline");
				$("#selectPerson").css("display", "inline");
				$("#selectBucket").css("display", "none");
				$("#target").css("display", "inline");
				
				var mobileDevice = navigatorIsMobileDevice();
				if (mobileDevice==false){
					showNativeHTMLButton("earthViewButton");
				}
				
				var latitude = panorama.getPosition().lat();
				var longitude = panorama.getPosition().lng();
				moveToPointExactMap(latitude, longitude, map.getZoom());
			}
	  }
  });
  
  var input = document.getElementById('target');

  //var searchBox = new google.maps.places.SearchBox(input);
  var autocomplete = new google.maps.places.Autocomplete(input);
  
  google.maps.event.addListener(autocomplete, 'place_changed', function() {
	  	var place = autocomplete.getPlace();	  	
	    if (!place.geometry) {
	        // the place was not found and return.
	        return;
	      }
	    	if (mapsMode == true){	
	    		// If the place has a geometry, then present it on a map.
	    		if (place.geometry.viewport) {
	    			map.fitBounds(place.geometry.viewport);
	    		} else {
	    			map.setCenter(place.geometry.location);
	    			map.setZoom(17);  // Why 17? Because it looks good.
	    		}
	    	}else{
	    		if (place.geometry.viewport) {
	    			map.fitBounds(place.geometry.viewport);
	    			var center = map.getCenter();
	    			moveToPoint(center.lat(), center.lng(), 4000);
	    		} else {
	    			map.setCenter(place.geometry.location);
	    			var center = map.getCenter();
	    			moveToPoint(center.lat(), center.lng(), 4000);
	    		}    		
	    	}
  	});
  
  
  google.maps.event.addListener(map, 'center_changed', function(event) {
	  if (vcMapTimeout !== null)
	  {
	    window.clearTimeout(vcMapTimeout);
	  }
	  // hide the 'view changed' message after 250 ms of inactivity
	  vcMapTimeout = window.setTimeout(function() {
		  vcMapTimeout = null;
		  
		  if (mapsMode == true){			  
			   refreshPoisMap();
		  }
		    
		  }, 250);
	  }); 
  
  /*google.maps.event.addListener(panorama, 'position_changed', function() {
		var center = map.getCenter();
		var latitude = center.lat();
		var longitude = center.lng();
		
		var latitude2 = panorama.getPosition().lat();
		var longitude2 = panorama.getPosition().lng();
  });*/
  
	
	$("#selectBucket").change(function() {
		var panorama = map.getStreetView();
		//Check if we are in street view mode
		if (panorama.getVisible()==true){
			 var value = $(this).val();
			 if (value != ''){
				 for (var j = 0; j < poisArray.length; j++){
					 if (value == poisArray[j].id){
							var latitude = panorama.getPosition().lat();
							var longitude = panorama.getPosition().lng();
							var poiHref = poisArray[j].webpage + "&p=" + latitude + "," + longitude;
							var mapHeight = ($("#mapContainer").outerHeight(false) - 150);
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
		 if (mapsMode==true){
			 var value = $(this).val();
			 if (value != ''){
				 for (var j = 0; j < poisArray.length; j++){
					 if (value == poisArray[j].id){
						 moveToPoisMap(poisArray[j], ZOOM_TO_POI);
					 }
				 }
			 }
		}
	 });
	 
	 $("#selectPerson").change(function(e) {
		 if (mapsMode==true){
			 var value = $(this).val();
			 if (value != ''){
				 for (var j = 0; j < poisArray.length; j++){
					 if (value == poisArray[j].id){
						 moveToPoisMap(poisArray[j], ZOOM_TO_POI);
					 }
				 }
			 }
		}
	 });
	
  	//notifyMapsReady();
	 getPoisMap(false);
}

/**
 * Get the pois using as the current position the geolocation value
 * @param position
 */
function getPoisByGeolocationMap(position){
	var latitude = position.coords.latitude;
	var longitude = position.coords.longitude;
	moveToPointExactMap(latitude,longitude,4000);
}

/*
 * This function is called when there is an error getting the geolocation
 */
function geolocationErrorMap(error){
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
function moveToGeolocationMap(){
	if (navigator.geolocation){
	      // timeout at 6000 milliseconds (6 seconds)
	    var options = {timeout:6000};
		navigator.geolocation.getCurrentPosition(getPoisByGeolocationMap,geolocationErrorMap, options);
	}else{
		console.log("Geolocation is not supported by this browser.");
	}
}

function getPoisMap(update){
	var panorama = map.getStreetView();
	//Check if we are in street view mode
	if (panorama.getVisible()==false)
	{
		var center = map.getCenter();
		var latitude = center.lat();
		var longitude = center.lng();
	}else{
		var latitude = panorama.getPosition().lat();
		var longitude = panorama.getPosition().lng();	
	}
	$.getJSON(
		"/GLUEPSManager/arbrowser/earth/pois/search/"+deployId+"/"+username,
		{latitude: latitude, longitude: longitude},
		function(data) {
			//Update the pois array
			poisArray = data.results;
			for (var i = 0; i < poisArray.length; i++){
				poisArray[i].lat = parseFloat(poisArray[i].lat);
				poisArray[i].lng = parseFloat(poisArray[i].lng);
			}
			//Remove the old ones
			removePoisMap();
			//Place the new pois
			placePoisMap(poisArray);
			//Add the option to the select poi
			addOptionSelectPoiMap();
			//Add the option to the select person
			addOptionSelectPersonMap();
			//Add the option to the select bucket
			addOptionSelectBucketMap();
			if (!update){
				$("#selectDeploy").removeAttr("disabled");
			}
			if (firstTimeMaps){
				firstTimeMaps = false;
				notifyMapsReady();
			}
			//Move towards the first one
			if (!update && poisArray.length > 0) {
				moveToPoisMap(poisArray[0],18);
			}else if (!update && poisArray.length == 0){
				//Move to the current geolocation of the navigator
				moveToGeolocationMap();
			}
			/*if (!update){
				moveToGeolocationMap();
			}*/
		});
}

function moveToPoisMap(pois, zoom){
	
	var center = new google.maps.LatLng(pois.lat, pois.lng);
	map.setZoom(zoom);
	map.setCenter(center);
	
	var panorama = map.getStreetView();
	panorama.setPosition(center);
	panorama.setPov(/** @type {google.maps.StreetViewPov} */({
	    heading: 265,
	    pitch: 0
	  }));
}

function moveToPointExactMap(latitude, longitude, zoom){
	
	var center = new google.maps.LatLng(latitude, longitude);
	map.setZoom(zoom);
	map.setCenter(center);
	map.panTo(center);
	
	var panorama = map.getStreetView();
	panorama.setPosition(center);
	panorama.setPov(/** @type {google.maps.StreetViewPov} */({
	    heading: 265,
	    pitch: 0
	  }));
}

function moveToPointMap(latitude, longitude, zoom){
	
	var center = new google.maps.LatLng(latitude, longitude);
	map.setZoom(zoom);
	map.setCenter(center);
	
	var panorama = map.getStreetView();
	panorama.setPosition(center);
	panorama.setPov(/** @type {google.maps.StreetViewPov} */({
	    heading: 265,
	    pitch: 0
	  }));
}

function refreshPoisMap(){
	//Remove the old ones
	removePoisMap();
	//Place the new pois
	placePoisMap(poisArray);
	//Add the option to the select poi
	addOptionSelectPoiMap();
	//Add the option to the select person
	addOptionSelectPersonMap();
}

function removePoisMap(){
	var toDelete = [];
	for(var id in markers){
		var exists = false;
		for (var j = 0; j < poisArray.length; j++){
			if (id == poisArray[j].id){
				var latitude = markers[id].lat;
				var longitude = markers[id].lng;
				//check that the position for the point has not changed
				if (latitude == poisArray[j].lat && longitude==poisArray[j].lng){
					exists = true;
				}
				break;
			}
		}
		if (!exists){
			toDelete.push(id);

		}
	}
	for (var i=0; i < toDelete.length; i++){
		var id = toDelete[i];
		if (typeof markers[id]!="undefined"){
			markers[id].obj.setMap(null);
			delete markers[id];
		}
	}
}


function placePoisMap(pois) {
	
	var panorama = map.getStreetView();
	//Check if we are in street view mode
	if (panorama.getVisible()==false)
	{
		var center = map.getCenter();
		var latitude = center.lat();
		var longitude = center.lng();
	}else{
		var latitude = panorama.getPosition().lat();
		var longitude = panorama.getPosition().lng();	
	}
	
	var infowindow = new google.maps.InfoWindow({
		maxWidth: 400
	});
	
	$.each(pois, function() {
			
		var newPoi;
		if (typeof markers[this.id]=="undefined"){
			newPoi = true;
		}else{
			newPoi = false;
		}
		
		if (newPoi && distFrom(latitude,longitude, this.lat, this.lng) < this.maxdistance){
		
			if (this.poitype != '3dmodel' && this.poitype != 'user'){
				
				var position = new google.maps.LatLng(this.lat, this.lng);
				
				var image = {
					url: 'images/glue.png',
					// This marker is 32 pixels wide by 62 pixels tall.
					size: new google.maps.Size(32, 62),
					// The origin for this image is 0,0.
					origin: new google.maps.Point(0,0)//,
					// The anchor for this image is the base of the flagpole at 0,32.
					//anchor: new google.maps.Point(0, 32)
				};
				  
				// Setup the markers on the map
				var marker = new google.maps.Marker({
				      position: position,
				      map: map,
				      //icon: 'http://chart.apis.google.com/chart?chst=d_map_pin_icon&chld=cafe|FFFF00',
				      //icon: 'http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png',
				      icon: image,
				      title: this.title
				});				 
				
				
				var item = this;
				google.maps.event.addListener(marker, 'click', function() {
							
					var center = map.getCenter();
					var latitude = center.lat();
					var longitude = center.lng();
					var heading = lastHeading;
					if (item.poitype == "bucket"){
						var poiHref = item.webpage + "&p=" + latitude + "," + longitude;
					}else{
						var poiHref = item.webpage;
					}	
					var contentString = '<div style="text-align:center"><a style="margin:20px" data-poi-id="'+item.id +
					'" class="poiResource btn btn-large" onclick="postInteraction(' + latitude + ',' + longitude +',' + heading + ",'" + item.id + "'" + ')" target="_blank" href="' + 
					decodeURI(poiHref)+ '">' + item.title + '</a></div><div style="padding:10px">'+
					item.description+'</div>';
					
					infowindow.setContent(contentString);
					infowindow.setPosition(marker.getPosition());
					infowindow.open(map);
					//infowindow.open(map,marker);
					
					var panorama = map.getStreetView();
					//Check if we are in street view mode
					if (panorama.getVisible()==true)
					{
						if (item.poitype == "bucket"){
							var panorama = map.getStreetView();
							var latitude = panorama.getPosition().lat();
							var longitude = panorama.getPosition().lng();							
							var poiHref = item.webpage + "&p=" + latitude + "," + longitude;
						}else{
							var poiHref = item.webpage;
						}	
						
						postInteraction(latitude, longitude, heading, item.id);
						window.open (decodeURI(poiHref),item.title);
					}
					
				});
				  
				markers[this.id] = {
							obj: marker,
							lat: this.lat,
							lng: this.lng
				  };
			}else if (this.poitype == 'user'){
				
				var position = new google.maps.LatLng(this.lat, this.lng);
				
				var panorama = map.getStreetView();
				if (panorama.getVisible()==false){
					var image = {
						url: 'images/manBig.png',
						// This marker is 64 pixels wide by 64 pixels tall.
						size: new google.maps.Size(64, 64),
						// The origin for this image is 0,0.
						origin: new google.maps.Point(0,0)//,
						// The anchor for this image is the base of the flagpole at 0,32.
						//anchor: new google.maps.Point(0, 32)
					};
				}else{
					var image = {
							url: 'images/manExtraBig.png',
							size: new google.maps.Size(128, 128),
							origin: new google.maps.Point(0,0)
					};
				}
				  
				// Setup the markers on the map
				var marker = new google.maps.Marker({
				      position: position,
				      map: map,
				      //icon: 'http://chart.apis.google.com/chart?chst=d_map_pin_icon&chld=cafe|FFFF00',
				      //icon: 'http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png',
				      icon: image,
				      title: this.title
				});
				
				markers[this.id] = {
						obj: marker,
						lat: this.lat,
						lng: this.lng
				};
				
			}
			
		}else if (!newPoi && distFrom(latitude,longitude, this.lat, this.lng) >= this.maxdistance){
			markers[this.id].obj.setMap(null);
			delete markers[this.id];
		}
	});
}

function addOptionSelectPoiMap(){
	var options = 0;
	$("#selectPoi").html("");
	$("<option>", {value: '', text: $.i18n.prop('select.poi')}).appendTo("#selectPoi");
	for(var id in markers) {
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

function addOptionSelectPersonMap(){
	var options = 0;
	$("#selectPerson").html("");
	$("<option>", {value: '', text: $.i18n.prop('select.person')}).appendTo("#selectPerson");
	for(var id in markers) {
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

function addOptionSelectBucketMap(){
	var options = 0;
	$("#selectBucket").html("");
	$("<option>", {value: '', text: $.i18n.prop('select.bucket')}).appendTo("#selectBucket");
	
	for(var id in markers) {
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