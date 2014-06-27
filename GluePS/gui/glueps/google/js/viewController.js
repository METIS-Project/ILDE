/**
 * Functions to control the different views
 */

/**
 * Show the panel
 */
function showPanel(){
	$("#panel").css("display","block");
}

/**
 * hide the panel
 */
function hidePanel(){
	$("#panel").css("display","none");
}

/**
 * Show the google earth view
 */
function changeViewToEarthMode(){
	$("#map3d").css("display","block");
	$("#map_canvas").css("display","none");
	$("#firstPersonCamControls").css("display", "none");
	
	$("#toggleEarthMaps").css("display", "inline");
	$("#toogleGroundView").css("display", "inline");
	$("#selectPoi").css("display", "inline");
	$("#selectPerson").css("display", "inline");
	$("#selectBucket").css("display", "none");
	$("#targetEarth").css("display", "inline");
	$("#searchTargetEarth").css("display", "inline");
	$("#target").css("display", "none");
	$("#selectDeploy").removeAttr("disabled");
	
	$("#toggleEarthMaps").html($.i18n.prop('change.map'));
	
	hideNativeHTMLButton("closeGroundLevel");
	
	hideNativeHTMLButton("turnLeftArrow");
	hideNativeHTMLButton("moveBackwardArrow");
	hideNativeHTMLButton("turnRightArrow");
	hideNativeHTMLButton("moveForwardArrow");
	
	hideNativeHTMLButton("strafeLeftArrow");
	hideNativeHTMLButton("altitudeDownArrow");
	hideNativeHTMLButton("strafeRightArrow");
	hideNativeHTMLButton("altitudeUpArrow");
	
	showNativeHTMLButton("firstPersonViewButton");
	showNativeHTMLButton("mapViewButton");
	hideNativeHTMLButton("earthViewButton");
	resizeMapDiv();
	updateCustomizedButtonsEarth();
}


/**
 * Show the google maps view
 */
function changeViewToMapsMode(){
	$("#map3d").css("display","none");
	$("#map_canvas").css("display","block");
	$("#firstPersonCamControls").css("display", "none");
	
	$("#toggleEarthMaps").css("display", "inline");
	$("#toogleGroundView").css("display", "none");//don't show the ground level view here
	$("#selectPoi").css("display", "inline");
	$("#selectPerson").css("display", "inline");
	$("#selectBucket").css("display", "none");
	$("#targetEarth").css("display", "none");
	$("#searchTargetEarth").css("display", "none");
	$("#target").css("display", "inline");
	
	$("#toggleEarthMaps").html($.i18n.prop('change.earth'));
	google.maps.event.trigger(map, 'resize');
	
	hideNativeHTMLButton("closeGroundLevel");
	
	hideNativeHTMLButton("turnLeftArrow");
	hideNativeHTMLButton("moveBackwardArrow");
	hideNativeHTMLButton("turnRightArrow");
	hideNativeHTMLButton("moveForwardArrow");
	
	hideNativeHTMLButton("strafeLeftArrow");
	hideNativeHTMLButton("altitudeDownArrow");
	hideNativeHTMLButton("strafeRightArrow");
	hideNativeHTMLButton("altitudeUpArrow");
	
	hideNativeHTMLButton("firstPersonViewButton");
	hideNativeHTMLButton("mapViewButton");
	showNativeHTMLButton("earthViewButton");
	resizeMapDiv();
	updateCustomizedButtonsMaps();
}

/**
 * Show the google maps view
 */
function changeViewToOnlyMapsMode(){
	$("#map3d").css("display","none");
	$("#map_canvas").css("display","block");
	$("#firstPersonCamControls").css("display", "none");
	
	$("#toggleEarthMaps").css("display", "none");
	$("#toogleGroundView").css("display", "none");//don't show the ground level view here
	$("#selectPoi").css("display", "inline");
	$("#selectPerson").css("display", "inline");
	$("#selectBucket").css("display", "none");
	$("#targetEarth").css("display", "none");
	$("#searchTargetEarth").css("display", "none");
	$("#target").css("display", "inline");
	
	$("#toggleEarthMaps").html($.i18n.prop('change.earth'));
	google.maps.event.trigger(map, 'resize');
	
	hideNativeHTMLButton("closeGroundLevel");
	
	hideNativeHTMLButton("turnLeftArrow");
	hideNativeHTMLButton("moveBackwardArrow");
	hideNativeHTMLButton("turnRightArrow");
	hideNativeHTMLButton("moveForwardArrow");
	
	hideNativeHTMLButton("strafeLeftArrow");
	hideNativeHTMLButton("altitudeDownArrow");
	hideNativeHTMLButton("strafeRightArrow");
	hideNativeHTMLButton("altitudeUpArrow");
	
	hideNativeHTMLButton("firstPersonViewButton");
	hideNativeHTMLButton("mapViewButton");
	hideNativeHTMLButton("earthViewButton");
	resizeMapDiv();
}

/**
 * Show the street view
 */
function changeViewToStreetViewMode(){
	$("#toggleEarthMaps").css("display", "none");
	$("#selectPoi").css("display", "none");
	$("#selectPerson").css("display", "none");
	$("#selectBucket").css("display", "inline");
	$("#target").css("display", "none");
	
	hideNativeHTMLButton("earthViewButton");
}

/**
 * Show the ground level view
 */
function changeViewToGroundLevel(){
	$("#map3d").css("display","block");
	$("#map_canvas").css("display","none");
	$("#firstPersonCamControls").css("display", "block");
	$("#toggleEarthMaps").css("display", "none");
	$("#toogleGroundView").css("display", "none");
	$("#selectPoi").css("display", "none");
	$("#selectPerson").css("display", "none");
	$("#selectBucket").css("display", "inline");
	$("#targetEarth").css("display", "none");
	$("#searchTargetEarth").css("display", "none");
	
	showNativeHTMLButton("closeGroundLevel");
	
	showNativeHTMLButton("turnLeftArrow");
	showNativeHTMLButton("moveBackwardArrow");
	showNativeHTMLButton("turnRightArrow");
	showNativeHTMLButton("moveForwardArrow");
	
	//we don't want to show this button by now
	hideNativeHTMLButton("strafeLeftArrow");
	showNativeHTMLButton("altitudeDownArrow");
	//we don't want to show this button by now
	hideNativeHTMLButton("strafeRightArrow");
	showNativeHTMLButton("altitudeUpArrow");
	
	hideNativeHTMLButton("firstPersonViewButton");
	hideNativeHTMLButton("mapViewButton");
	hideNativeHTMLButton("earthViewButton");
	resizeMapDiv();
}

/*
 * Resize the DIV used by the map view in order to show it properly
 */
function resizeMapDiv(){
	var containerHeight = $("#headerContainer").outerHeight(false);
	$("#mainContainer").css("top", containerHeight + "px");
	
	var mainHeight = $("#mainContainer").outerHeight(false);
	if ($("#panel").css("display")=="none")
	{
		var panelHeight = 0;
	}else{
		var panelHeight = $("#panel").outerHeight(false);
	}
	if ($("#firstPersonCamControls").css("display")=="none"){
		var cameraControlsHeight = 0;
	}else{
		var cameraControlsHeight = $("#firstPersonCamControls").outerHeight(false);
	}
	var mapHeight = mainHeight - (panelHeight+cameraControlsHeight) - 50;
	$("#map3d").css("height", mapHeight + "px");
	$("#map_canvas").css("height", mapHeight + "px");
}

/**
 * Toogle between the google earth and google maps views
 */
function toggleMaps() 
{
	if (mapsMode == false){//Change from google earth view to google maps view
		
		var lookAt = ge.getView().copyAsLookAt(ge.ALTITUDE_RELATIVE_TO_GROUND);
		var range = lookAt.getRange();
		var zoom = Math.round(27 - (Math.log(range) / Math.log(2)));
		if (!map.getZoom() == zoom) {
		    zoom = zoom - 1;
		} else {
		   zoom = zoom;
		}
		var latitudeLookAt = lookAt.getLatitude();
		var longitudeLookAt = lookAt.getLongitude();
		var headingLookAt = lookAt.getHeading();
		lastHeading = headingLookAt;
		
		//refresh the pois of the maps view	  
		refreshPoisMap();
		mapsMode = true;
		changeViewToMapsMode();
		moveToPointExactMap(latitudeLookAt, longitudeLookAt, zoom);
			
	}else{
		var panorama = map.getStreetView();
		if (panorama.getVisible()==false){//Change from google maps view to google earth view
			var center = map.getCenter();
			var latitude = center.lat();
			var longitude = center.lng();
		}else{//Change from street view to google earth view
			var latitude = panorama.getPosition().lat();
			var longitude = panorama.getPosition().lng();
		}
		var range = Math.pow(2, 27 - map.getZoom());
		
		//Refresh the pois of the earth view    
		refreshPois();
		mapsMode = false;
		changeViewToEarthMode();
		moveToPointExact(latitude, longitude, range, true);
	}
}

/**
 * Toggle between earth view and first person view
 */
function toggleGroundView(){
	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);
	if (cam == null){
		cam = new FirstPersonCam();
	}
	if (cam.getEnabledCamera()==false){
		ge.getOptions().setFlyToSpeed(100); //do not delete this line!
		cam.enableCamera(camera.getLatitude(), camera.getLongitude());
		cam.updateCamera();
		changeViewToGroundLevel();
		groundView = true;
	}else{
		cam.disableCamera();
		changeViewToEarthMode();
		groundView = false;
	}	
}

function createCustomizedButtons(){
	var pluginRect = getElementRect(document.getElementById('map3d'));
	createNativeHTMLButton(pluginRect.width - 32, 10, 32, 32, "images/close-icon.png", "closeGroundLevel", "Close first person view", "map3d"); // x, y, width, height
	$("#closeGroundLevel").click(function(){
		toggleGroundView();
	});	
	
	var marginRight = 10;
	createNativeHTMLButton(pluginRect.width - 48*3 - marginRight, 150, 48, 48, "images/arrow-left.png", "turnLeftArrow", "Turn left (key left)", "map3d");
	$("#turnLeftArrow").mousedown(function(){
		turnLeft=true;
		this.style.backgroundImage = 'url(' + "images/arrow-left-focus.png" +')';
	});
	$("#turnLeftArrow").mouseup(function(){
		turnLeft=false;
		this.style.backgroundImage = 'url(' + "images/arrow-left.png" +')';
	});
	createNativeHTMLButton(pluginRect.width - 48*2 - marginRight, 150, 48, 48, "images/arrow-down.png", "moveBackwardArrow", "Move backward (key down)", "map3d");
	$("#moveBackwardArrow").mousedown(function(){
		moveBackward=true;
		this.style.backgroundImage = 'url(' + "images/arrow-down-focus.png" +')';
	});
	$("#moveBackwardArrow").mouseup(function(){
		moveBackward=false;
		this.style.backgroundImage = 'url(' + "images/arrow-down.png" +')';
	});
	createNativeHTMLButton(pluginRect.width - 48 - marginRight, 150, 48, 48, "images/arrow-right.png", "turnRightArrow", "Turn right (key right)", "map3d");
	$("#turnRightArrow").mousedown(function(){
		turnRight=true;
		this.style.backgroundImage = 'url(' + "images/arrow-right-focus.png" +')';
	});
	$("#turnRightArrow").mouseup(function(){
		turnRight=false;
		this.style.backgroundImage = 'url(' + "images/arrow-right.png" +')';
	});
	createNativeHTMLButton(pluginRect.width - 48*2 - marginRight, 150-48, 48, 48, "images/arrow-up.png", "moveForwardArrow", "Move forward (key up)", "map3d");
	$("#moveForwardArrow").mousedown(function(){
		moveForward=true;
		this.style.backgroundImage = 'url(' + "images/arrow-up-focus.png" +')';
	});
	$("#moveForwardArrow").mouseup(function(){
		moveForward=false;
		this.style.backgroundImage = 'url(' + "images/arrow-up.png" +')';
	});
	
	
	createNativeHTMLButton(pluginRect.width - 48*3 - marginRight, 350, 48, 48, "images/rounded-arrow-left.png", "strafeLeftArrow", "Strafe left (key a)", "map3d");
	$("#strafeLeftArrow").mousedown(function(){
		strafeLeft=true;
		this.style.backgroundImage = 'url(' + "images/rounded-arrow-left-focus.png" +')';
	});
	$("#strafeLeftArrow").mouseup(function(){
		strafeLeft=false;
		this.style.backgroundImage = 'url(' + "images/rounded-arrow-left.png" +')';
	});
	createNativeHTMLButton(pluginRect.width - 48*2 - marginRight, 350, 48, 48, "images/rounded-arrow-down.png", "altitudeDownArrow", "Altitude down (key s)", "map3d");
	$("#altitudeDownArrow").mousedown(function(){
		altitudeDown=true;
		this.style.backgroundImage = 'url(' + "images/rounded-arrow-down-focus.png" +')';
	});
	$("#altitudeDownArrow").mouseup(function(){
		altitudeDown=false;
		this.style.backgroundImage = 'url(' + "images/rounded-arrow-down.png" +')';
	});
	createNativeHTMLButton(pluginRect.width - 48 - marginRight, 350, 48, 48, "images/rounded-arrow-right.png", "strafeRightArrow", "Strafe right (key d)", "map3d");
	$("#strafeRightArrow").mousedown(function(){
		strafeRight=true;
		this.style.backgroundImage = 'url(' + "images/rounded-arrow-right-focus.png" +')';
	});
	$("#strafeRightArrow").mouseup(function(){
		strafeRight=false;
		this.style.backgroundImage = 'url(' + "images/rounded-arrow-right.png" +')';
	});
	createNativeHTMLButton(pluginRect.width - 48*2 - marginRight, 350-48, 48, 48, "images/rounded-arrow-up.png", "altitudeUpArrow", "Altitude up (key w)", "map3d");
	$("#altitudeUpArrow").mousedown(function(){
		altitudeUp=true;
		this.style.backgroundImage = 'url(' + "images/rounded-arrow-up-focus.png" +')';
	});
	$("#altitudeUpArrow").mouseup(function(){
		altitudeUp=false;
		this.style.backgroundImage = 'url(' + "images/rounded-arrow-up-focus.png" +')';
	});
	
	
	createNativeHTMLButton(10, 10, 144, 31, "images/first-person-view.png", "firstPersonViewButton", "Show the first person view", "map3d");
	$("#firstPersonViewButton").click(function(){
		toggleGroundView();
	});
	$("#firstPersonViewButton").mouseover(function(){
		this.style.backgroundImage = 'url(' + "images/first-person-view-over.png" +')';
	});
	$("#firstPersonViewButton").mouseout(function(){
		this.style.backgroundImage = 'url(' + "images/first-person-view.png" +')';
	});
	createNativeHTMLButton(10 + 144, 10, 93, 31, "images/map-view.png", "mapViewButton", "Show the map view", "map3d");
	$("#mapViewButton").click(function(){
		toggleMaps();
	});
	$("#mapViewButton").mouseover(function(){
		this.style.backgroundImage = 'url(' + "images/map-view-over.png" +')';
	});
	$("#mapViewButton").mouseout(function(){
		this.style.backgroundImage = 'url(' + "images/map-view.png" +')';
	});
	createNativeHTMLButton(10, 10, 98, 31, "images/earth-view.png", "earthViewButton", "Show the earth view", "map3d");
	$("#earthViewButton").click(function(){
		toggleMaps();
	});
	$("#earthViewButton").mouseover(function(){
		this.style.backgroundImage = 'url(' + "images/earth-view-over.png" +')';
	});
	$("#earthViewButton").mouseout(function(){
		this.style.backgroundImage = 'url(' + "images/earth-view.png" +')';
	});
}

function updateCustomizedButtonsEarth(){
	var pluginRect = getElementRect(document.getElementById('map3d'));
	moveNativeHTMLButton(pluginRect.width - 32, 10, "closeGroundLevel", "map3d"); // x, y, id, rectId
	
	var marginRight = 10;
	moveNativeHTMLButton(pluginRect.width - 48*3 - marginRight, 150, "turnLeftArrow", "map3d");
	moveNativeHTMLButton(pluginRect.width - 48*2 - marginRight, 150, "moveBackwardArrow", "map3d");
	moveNativeHTMLButton(pluginRect.width - 48 - marginRight, 150, "turnRightArrow", "map3d");
	moveNativeHTMLButton(pluginRect.width - 48*2 - marginRight, 150-48, "moveForwardArrow", "map3d");
	
	moveNativeHTMLButton(pluginRect.width - 48*3 - marginRight, 350, "strafeLeftArrow", "map3d");
	moveNativeHTMLButton(pluginRect.width - 48*2 - marginRight, 350, "altitudeDownArrow", "map3d");
	moveNativeHTMLButton(pluginRect.width - 48 - marginRight, 350, "strafeRightArrow", "map3d");
	moveNativeHTMLButton(pluginRect.width - 48*2 - marginRight, 350-48, "altitudeUpArrow", "map3d");
	
	moveNativeHTMLButton(10, 10, "firstPersonViewButton", "map3d");
	moveNativeHTMLButton(10 + 144, 10, "mapViewButton", "map3d");
	//moveNativeHTMLButton(10, 10, "earthViewButton", "map3d");
}

function updateCustomizedButtonsMaps(){
	moveNativeHTMLButton(10, 10, "earthViewButton", "map_canvas");
}