/*
Copyright 2008 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

// Code for a simple quake-style camera.
//
// Notes: This is a very simple camera and intended to be so. The 
// camera's altitude is always 0, relative to the surface of the
// earth.
//

//----------------------------------------------------------------------------
// Global Variables
//----------------------------------------------------------------------------

turnLeft = false;
turnRight = false;
tiltUp = false;
tiltDown = false;

moveForward = false;
moveBackward = false;
strafeLeft = false;
strafeRight = false;
altitudeUp = false;
altitudeDown = false;

enabledFirstPersonCamera = false;  //boolean used to check if the firstPersonCam mode is enabled

INITIAL_CAMERA_ALTITUDE = 1.7; // Roughly 6 feet tall
cameraAltitude = INITIAL_CAMERA_ALTITUDE;
//----------------------------------------------------------------------------
// Utility Functions
//----------------------------------------------------------------------------

// Keep an angle in [-180,180]
function fixAngle(a) {
  while (a < -180) {
    a += 360;
  }
  while (a > 180) {
    a -= 360;
  }
  return a;
}

function resetGlobalVariables(){
	turnLeft = false;
	turnRight = false;
	tiltUp = false;
	tiltDown = false;

	moveForward = false;
	moveBackward = false;
	strafeLeft = false;
	strafeRight = false;
	altitudeUp = false;
	altitudeDown = false;
}

//----------------------------------------------------------------------------
// Input Handlers
//----------------------------------------------------------------------------

function keyDown(event) {
  //The key events are only take into account when the first person camera view is enabled
  if (enabledFirstPersonCamera==false){
	  return true;
  }
  //Internet Explorer considers the event object as a property of the window object
  if (!event) {
    event = window.event;
  }
  else if (event.keyCode == 87) {  // Altitude Up
    altitudeUp = true;
  } else if (event.keyCode == 83) {  // Altitude Down
    altitudeDown = true;
  } else if (event.keyCode == 37) {  // Turn Left.
    turnLeft = true;
  } else if (event.keyCode == 39) {  // Turn Right.
    turnRight = true;
  } else if (event.keyCode == 38) {  // Move Forward.
	moveForward = true;
  } else if (event.keyCode == 40) {  // Move Backward.
	moveBackward = true;
  } else if (event.keyCode == 65) {  // Strafe Left.
    strafeLeft = true;
  } else if (event.keyCode == 68) {  // Strafe Right.
    strafeRight = true;
  }
  event.preventDefault(); //Other browsers
  event.stopPropagation();
  event.returnValue = false; //IExplorer browsers
  return false;
}

function keyUp(event) {
  if (enabledFirstPersonCamera==false){
		  return true;
  }
  if (!event) {
    event = window.event;
  } 
  if (event.keyCode == 87) {  // Altitude Up
    altitudeUp = false;
  } else if (event.keyCode == 83) {  // Altitude Down
    altitudeDown = false;
  } else if (event.keyCode == 37) {  // Turn Left.
    turnLeft = false;
  } else if (event.keyCode == 39) {  // Turn Right.
    turnRight = false;
  } else if (event.keyCode == 38) {  // Move Forward.
	moveForward = false;
  } else if (event.keyCode == 40) {  // Move Backward.
	moveBackward = false;
  } else if (event.keyCode == 65) {  // Strafe Left.
    strafeLeft = false;
  } else if (event.keyCode == 68) {  // Strafe Right.
    strafeRight = false;
  }
  event.preventDefault();
  event.stopPropagation();
  event.returnValue = false;
  return false;
}



//----------------------------------------------------------------------------
// JSObject - FirstPersonCamera
//----------------------------------------------------------------------------

function FirstPersonCam(lat, lon, altitude) {
  var me = this;
  enabledFirstPersonCamera = false;

  // Updates should be called on frameend to help keep objects in sync.
  // GE does not propogate changes caused by KML objects until an
  // end of frame.
  google.earth.addEventListener(ge, "frameend",function() {
	  if (enabledFirstPersonCamera==true){
		  me.update();
	  }
  });
}

FirstPersonCam.prototype.updateOrientation = function(dt) {
  var me = this;

  // Based on dt and input press, update turn angle.
  if (turnLeft || turnRight) {  
    var turnSpeed = 30;//60.0; // radians/sec
    if (turnLeft)
      turnSpeed *= -1.0;
    me.headingAngle += turnSpeed * dt * Math.PI / 180.0;
  }
  if (tiltUp || tiltDown) {
    var tiltSpeed = 60.0; // radians/sec
    if (tiltDown)
      tiltSpeed *= -1.0;
    me.tiltAngle = me.tiltAngle + tiltSpeed * dt * Math.PI / 180.0;
    // Clamp
    var tiltMax = 50.0 * Math.PI / 180.0;
    var tiltMin = -90.0 * Math.PI / 180.0;
    if (me.tiltAngle > tiltMax)
      me.tiltAngle = tiltMax;
    if (me.tiltAngle < tiltMin)
      me.tiltAngle = tiltMin;
  } 
}

FirstPersonCam.prototype.updatePosition = function(dt) {
  var me = this;
  
  // Convert local lat/lon to a global matrix. The up vector is 
  // vector = position - center of earth. And the right vector is a vector
  // pointing eastwards and the facing vector is pointing towards north.
  var localToGlobalFrame = M33.makeLocalToGlobalFrame(me.localAnchorLla); 
  
  // Move in heading direction by rotating the facing vector around
  // the up vector, in the angle specified by the heading angle.
  // Strafing is similar, except it's aligned towards the right vec.
  var headingVec = V3.rotate(localToGlobalFrame[1], localToGlobalFrame[2],
                             -me.headingAngle);                             
  var rightVec = V3.rotate(localToGlobalFrame[0], localToGlobalFrame[2],
                             -me.headingAngle);
  // Calculate strafe/forwards                              
  var strafe = 0;                             
  if (strafeLeft || strafeRight) {
    var strafeVelocity = 30;
    if (strafeLeft)
      strafeVelocity *= -1;      
    strafe = strafeVelocity * dt;
  }  
  var forward = 0;                             
  if (moveForward || moveBackward) {
    var forwardVelocity = 50;//100;
    if (moveBackward)
      forwardVelocity *= -1;      
    forward = forwardVelocity * dt;
  }  
  if (altitudeUp) {
    cameraAltitude += 1.0;
  } else if (altitudeDown) {
    cameraAltitude -= 1.0;
  }
  cameraAltitude = Math.max(0, cameraAltitude);
  
  me.distanceTraveled += forward;

  // Add the change in position due to forward velocity and strafe velocity 
  me.localAnchorCartesian = V3.add(me.localAnchorCartesian, 
                                   V3.scale(rightVec, strafe));
  me.localAnchorCartesian = V3.add(me.localAnchorCartesian, 
                                   V3.scale(headingVec, forward));
                                                                        
  // Convert cartesian to Lat Lon Altitude for camera setup later on.
  me.localAnchorLla = V3.cartesianToLatLonAlt(me.localAnchorCartesian);
}

FirstPersonCam.prototype.updateCamera = function() {
  var me = this;
           
  var lla = me.localAnchorLla;
  lla[2] = ge.getGlobe().getGroundAltitude(lla[0], lla[1]); 
  
  // Will put in a bit of a stride if the camera is at or below 1.7 meters
  var bounce = 0;  
  if (cameraAltitude <= INITIAL_CAMERA_ALTITUDE /* 1.7 */) {
    bounce = 1.5 * Math.abs(Math.sin(4 * me.distanceTraveled *
                                     Math.PI / 180)); 
  }
    
  // Update camera position. Note that tilt at 0 is facing directly downwards.
  //  We add 90 such that 90 degrees is facing forwards.
  var la = ge.createLookAt('');
  la.set(me.localAnchorLla[0], me.localAnchorLla[1],
         cameraAltitude + bounce,
         ge.ALTITUDE_RELATIVE_TO_SEA_FLOOR,
         fixAngle(me.headingAngle * 180 / Math.PI), /* heading */         
         me.tiltAngle * 180 / Math.PI + 90, /* tilt */         
         0 /* altitude is constant */         
         );  
  ge.getView().setAbstractView(la);         
};

FirstPersonCam.prototype.update = function() {
  var me = this;
  
  ge.getWindow().blur();
  
  // Update delta time (dt in seconds)
  var now = (new Date()).getTime();  
  var dt = (now - me.lastMillis) / 1000.0;
  if (dt > 0.25) {
    dt = 0.25;
  }  
  me.lastMillis = now;    
    
  // Update orientation and then position  of camera based
  // on user input   
  me.updateOrientation(dt);
  me.updatePosition(dt);
           
  // Update camera
  me.updateCamera();
};


/**
 * Enable first person view
 */
FirstPersonCam.prototype.enableCamera = function(lat, lon) {
	var me = this;
	ge.getNavigationControl().setVisibility(ge.VISIBILITY_HIDE);
	// prevent mouse navigation in the plugin
	ge.getOptions().setMouseNavigationEnabled(false);
	resetGlobalVariables();
	cameraAltitude = INITIAL_CAMERA_ALTITUDE; //reset the altitude value for the camera
	// The anchor point is where the camera is situated at. We store
	// the current position in lat, lon, altitude and in cartesian 
	// coordinates.
	if (typeof me.localAnchorLla != "undefined"){
		me.localAnchorLla.splice(0,3);
	}
	me.localAnchorLla = [lat,lon,0];
	me.localAnchorCartesian = V3.latLonAltToCartesian(me.localAnchorLla);

	// Heading, tilt angle is relative to local frame
	me.headingAngle = 0;
	me.tiltAngle = 0;
	//me.tiltAngle = 90;

	// Initialize the time
	me.lastMillis = (new Date()).getTime();  
	  
	// Used for bounce.
	me.distanceTraveled = 0;
	enabledFirstPersonCamera = true;
}

FirstPersonCam.prototype.disableCamera = function() {
	var me = this;
	ge.getNavigationControl().setVisibility(ge.VISIBILITY_AUTO);
	ge.getOptions().setMouseNavigationEnabled(true);
	// Get the current view.
	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);
	camera.setAltitude(200.0);//75.0
	//camera.setHeading(0.0); 
	camera.setTilt(0.0);//45.0
	//camera.setRoll(0.0);
	ge.getView().setAbstractView(camera);
	enabledFirstPersonCamera = false;
}

FirstPersonCam.prototype.getEnabledCamera = function() {
	return enabledFirstPersonCamera;
}

