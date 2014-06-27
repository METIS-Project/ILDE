/**
 * Start point for the application
 */

//Google earth object
var ge;
//Google maps object
var map;
//Camera object
var cam = null;


//The identifier generated for the user logged in
var uid = null;
//The name of the user logged in
var username;
//The id of the deploy selected by the user
var deployId;
//The view is Google Earth (mapsMode=false) or Google Maps (mapsMode=true)
var mapsMode = false;
//The view is Google Earth at ground level
var groundView = false;
//The Google earth instance has been created yet or not
var earthLoaded = false; 
//The Google map instance has been loaded yet or not
var mapsLoaded = false;
//The last heading value
var lastHeading = 0;
//We have to move the by the first time
var firstTimeEarth = true;
//We have to move the by the first time
var firstTimeMaps = true;


//The array where the pois are stored
var poisArray = [];
//Timeout for the Google earth view refresh when the camera position changes
var vcTimeout = null;
//Timeout for the Google Maps view refresh when the camera position changes
var vcMapTimeout = null;
//Array where the placemarks (Google earth) are stored. We need this to delete the old ones before adding the new ones.
var placemarks = [];
//Array where the markers (Google maps) are stored. We need this to delete the old ones before adding the new ones.
var markers = [];

//The time in milliseconds between two calls to the post trace messages function
const POST_TRACE_INTERVAL = 5000; //300000;
//The time in milliseconds between two calls to the getPois function
const GET_POIS_INTERVAL = 10000;
//The zoom value when a poi is selected
const ZOOM_TO_POI = 20;
//the maximum distance to see a person
const DISTANCE_TO_PERSON = 65;
//the maximum altitude to see a person
const ALTITUDE_TO_PERSON = 40;

//Load the google earth module in the namespace google.earth, version 1
google.load("earth", "1");

function init() {
	$("#loginForm").on("click", "#entrar", function(e) {//A real login should be done!!
		e.preventDefault();
		username = $("#username").val();
		uid = generateRandomUid();
		//Check if the username has been provided. If not, show an error message
		if 	(username==""){
			document.getElementById("message").innerHTML = $.i18n.prop('error.usernameNotProvided');
			$("#message").attr("class", "error");
			$("#message").css("display", "");
			//After 5000 milliseconds we hide the message
			$("#message").delay(5000).fadeOut("slow");
		}else{
			$("#message").css("display", "none");
			login();
		}
	});	
	if (gup(window.location.href, "username")!="" && gup(window.location.href, "deployid")!=""){
		username = gup(window.location.href, "username");
		deployId = gup(window.location.href, "deployid");
		uid = generateRandomUid();
		$("#message").css("display", "none");
		loginAuto();
	}else if (gup(window.location.href, "errorcode")!="" && gup(window.location.href, "errorcode")=="deploy_not_found"){
			document.getElementById("message").innerHTML = $.i18n.prop('error.deployNotFound');
			$("#message").attr("class", "error");
			$("#message").css("display", "");
			$("#message").delay(5000).fadeOut("slow");
	}else if (gup(window.location.href, "errorcode")!="" && gup(window.location.href, "errorcode")=="username_no_courses"){
		document.getElementById("message").innerHTML = $.i18n.prop('info.noCourses');
		$("#message").attr("class", "info");
		$("#message").css("display", "");
		$("#message").delay(5000).fadeOut("slow");
	}
}

/**
 * Login the user and loads their deploys
 */
function login(){
	var bienvenidaHTML = "<span class=\"centering navbar-text\"><span id=\"welcomeSpan\">" + $.i18n.prop('welcome') + "</span><strong> "+username+"</strong>. ";
	var loadingDeploys = "<div id=\"loadingDeploys\"><img src=\"images/\loading.gif\"></div>"; 
	var desconectarHTML = '<button id="desconectar" class="btn" disabled=\"disabled\">' + $.i18n.prop('disconnect') + '</button>';
	var select = $(bienvenidaHTML + "<span id=\"courseSpan\">" + $.i18n.prop('course') + "</span></span><select id=\"selectDeploy\" name=\"selectDeploy\" disabled=\"disabled\"></select>" + loadingDeploys + desconectarHTML);
	$("#loginForm").css("display", "none");
	$("#logoutDiv").html(select);
	$("<option>", {value: '', text: $.i18n.prop('select.searchingUserCourses')}).appendTo("#selectDeploy");
	$.get("/GLUEPSManager/arbrowser/earth/pois/login/",
			{ username: username, uid: uid }, 
			function(deploys) {
				 $("#desconectar").removeAttr("disabled");
				 $("#desconectar").click(logout);
				 
				 $("#selectDeploy option[value='']").html($.i18n.prop('select.course'));
				 //Add the options to the select
				 for(var val in deploys) {
				     $("<option>", {value: val, text: deploys[val]}).appendTo("#selectDeploy");
				 }
				 if ($("#selectDeploy option").length == 1){
					 var pos = window.location.href.indexOf("?")
					 if (pos!=-1){
						 var newLocation = window.location.href.substring(0, pos) + "?errorcode=username_no_courses";
					 }else{
						 var newLocation = window.location.href + "?errorcode=username_no_courses";
					 }
					 window.location.search="?errorcode=username_no_courses";
					 window.location.href = newLocation;
				 }else{
					 //hide the loading gift
					 $("#loadingDeploys").css("display","none");
					 //enable the select element
					 $("#selectDeploy").removeAttr("disabled");
					 
					 $("#selectDeploy").change(function(e) {
						 if ($(this).val() != ''){
							 deployId = $(this).val();
							 var mobileDevice = navigatorIsMobileDevice();
							 if (!earthLoaded || !mapsLoaded){
								 if (!earthLoaded){
									 if (!mobileDevice){
										 $("#selectDeploy").attr("disabled", "disabled");
										 //Create a new instance of the plugin(div where the instance is added, callback function whether the result is ok, callback function if there is an error)
										 google.earth.createInstance('map3d', initCallback, failureCallback);
									 }
								 }
								 if (!mapsLoaded){
									 initialize();
								 }
							 }else{
								 if (mapsMode==false){
									 $("#selectDeploy").attr("disabled", "disabled");
									 getPois(false);
								 }else{
									 $("#selectDeploy").attr("disabled", "disabled");
									 getPoisMap(false);
								 }
							 }
						 }
					 });
				 }
				 
			}, 
			'json').fail(function(jqXHR, textStatus, errorThrown) {
				//alert(textStatus);
			});
}

/**
 * Login the user and loads the deploy whose id has been provided as an argument in the url
 */
function loginAuto(){
	var bienvenidaHTML = "<span class=\"centering navbar-text\"><span id=\"welcomeSpan\">" + $.i18n.prop('welcome') + "</span><strong> "+username+"</strong>. ";
	var loadingDeploys = "<div id=\"loadingDeploys\"><img src=\"images/\loading.gif\"></div>"; 
	var desconectarHTML = '<button id="desconectar" class="btn" disabled=\"disabled\">' + $.i18n.prop('disconnect') + '</button>';
	var select = $(bienvenidaHTML + "<span id=\"courseSpan\">" + $.i18n.prop('course') + "</span></span><select id=\"selectDeploy\" name=\"selectDeploy\" disabled=\"disabled\"></select>" + loadingDeploys + desconectarHTML);
	$("#loginForm").css("display", "none");
	$("#logoutDiv").html(select);
	$("<option>", {value: '', text: $.i18n.prop('select.searchingCourse')}).appendTo("#selectDeploy");
	$.get("/GLUEPSManager/arbrowser/earth/pois/login/",
			{ username: username, uid: uid }, 
			function(deploys) {
				 $("#desconectar").removeAttr("disabled");
				 $("#desconectar").click(logout);
				 var foundDeploy = false;
				 //Add the options to the select
				 for(var val in deploys) {
					 if (val==deployId){
					     $("<option>", {value: val, text: deploys[val]}).appendTo("#selectDeploy");
					     $("#selectDeploy").val(val);
					     $("#selectDeploy option[value='']").remove();
					     foundDeploy = true;
					 }
				 }
				 //hide the loading gift
				 $("#loadingDeploys").css("display","none");			 				 
				 
				 if (foundDeploy == true){
					 var mobileDevice = navigatorIsMobileDevice();
					 if (!earthLoaded || !mapsLoaded){
						 if (!earthLoaded){
							 if (!mobileDevice){
								 //Create a new instance of the plugin(div where the instance is added, callback function whether the result is ok, callback function if there is an error)
								 google.earth.createInstance('map3d', initCallback, failureCallback);
							 }
						 }
						 if (!mapsLoaded){
							 initialize();
						 }
					 }else{
						 if (mapsMode==false){
							 getPois(false);
						 }else{
							 getPoisMap(false);
						 }
					 }
				 }else{
					 var pos = window.location.href.indexOf("?")
					 if (pos!=-1){
						 var newLocation = window.location.href.substring(0, pos) + "?errorcode=deploy_not_found";
					 }else{
						 var newLocation = window.location.href + "?errorcode=deploy_not_found";
					 }
					 window.location.search="?errorcode=deploy_not_found";
					 window.location.href = newLocation;
				 }
				 
			}, 
			'json').fail(function(jqXHR, textStatus, errorThrown) {
				//alert(textStatus);
			});
}

/**
 * Logout the user and reload the main page
 */
function logout(e){
	 e.preventDefault();
	 //A real logout should be done!!
	 var pos = window.location.href.indexOf("?")
	 if (pos!=-1){
		 var newLocation = window.location.href.substring(0, pos);
	 }else{
		 var newLocation = window.location.href;
	 }
	 window.location.search="";
	 pos = newLocation.indexOf("#");
	 if (pos !=-1){
		 newLocation = newLocation.substring(0,pos);
	 }
	 window.location.href = newLocation;
}


/**
 * Init the application when the page is loaded
 */
$(document).ready(function() {
	//document.getElementById("map3d").style.display="block";
	//document.getElementById("map_canvas").style.display="none";
	$("#map3d").css("display","block");
	$("#map_canvas").css("display","none");
	
	/**
	 * Resize the map div when the window is resized
	 */
	$(window).resize(function () {
		resizeMapDiv();
		if (earthLoaded == true && mapsLoaded === true){
			if (mapsMode==false){
				updateCustomizedButtonsEarth();
			}else{
				updateCustomizedButtonsMaps();
			}
		}
	});
	
	/*$('#collapsable').on('shown.bs.collapse', function () {
		resizeMapDiv();
		updateCustomizedButtons();
	});
	$('#collapsable').on('hidden.bs.collapse', function () {
		resizeMapDiv();
		updateCustomizedButtons();
	});*/
	
	i18n.init();
	init();
	resizeMapDiv();
});

/**
 * Set an interval for the trace message
 */
setInterval(function(){
	//Make sure the google earth plugin is loaded. Otherwise the latitude and longitude are not available yet 
	if (earthLoaded == true && mapsLoaded === true){
		postTrace();
	}
}, POST_TRACE_INTERVAL);

/**
 * Set an interval to get the pois list from the pois server
 */
setInterval(function(){
	if (earthLoaded == true && mapsLoaded == true){
		if (mapsMode==false){
			getPois(true);
		}else{
			getPoisMap(true);
		}
	}
}, GET_POIS_INTERVAL);

/**
 * This function is called when the google earth plugin is ready
 */
function notifyEarthReady(){
	earthLoaded = true;
	if (earthLoaded && mapsLoaded){
		showPanel();
		if (mapsMode==false){
			changeViewToEarthMode();
			updateCustomizedButtonsEarth();
		}else{
			updateCustomizedButtonsMaps();
		}
	}
}

/**
 * This function is called when the google map is ready
 */
function notifyMapsReady(){
	mapsLoaded = true;
	var mobileDevice = navigatorIsMobileDevice();
	if (mobileDevice){
	 	createCustomizedButtons();
	 	earthLoaded = true;//It is not necessary to try to load the earth plugin
		mapsMode = true;
		changeViewToOnlyMapsMode();
	}
	if (earthLoaded && mapsLoaded){
		showPanel();
		if (mapsMode==true){
			updateCustomizedButtonsMaps();
		}else{
			updateCustomizedButtonsEarth();
		}
	}
}


/**
 * Post a trace message with the current position of the user 
 */
function postTrace(){
	
	if (mapsMode == false){
		// Post the position in the earth/ground level view
		var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);
		var latitude = camera.getLatitude();
		var longitude = camera.getLongitude();
		var heading = camera.getHeading();
	}else{
		var panorama = map.getStreetView();
		if (panorama.getVisible()==false){
			//post the position in the map view
			var center = map.getCenter();
			var latitude = center.lat();
			var longitude = center.lng();
		}else{
			//post the position in the street view
			var latitude = panorama.getPosition().lat();
			var longitude = panorama.getPosition().lng();
		}		
		//maps.getHeading() does not return a value
		var heading = lastHeading;
	}
	$.post("/GLUEPSManager/arbrowser/earth/pois/event/",
			{ username: username ,message: "trace", deployid: deployId, uid: uid, l: latitude + "," + longitude, orientation: heading});
}

function postInteraction(latitude, longitude, heading, id){
	$.post("/GLUEPSManager/arbrowser/earth/pois/event/",
				{ username: username ,message: "interaction", deployid: deployId, uid: uid, l: latitude + "," + longitude, orientation: heading, id: id}
	);
}

