/*
Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
Valladolid, Spain. https://www.gsic.uva.es/

This file is part of Glue!PS.

Glue!PS is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or 
(at your option) any later version.

Glue!PS is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
 * Manager for the event of type message
 */

var MessageManager = {
		
	init: function(){
		window.addEventListener('message', MessageManager.getEventMessage, false);
	},

	getEventMessage: function(event){
		//Check that the message is coming from this Glue!PS
		//if (event.origin != window.location.toString().split("/GLUEPSManager/")[0]){
		if (event.origin != (window.location.protocol + "//" + window.location.hostname) && event.origin != (window.location.protocol + "//" + window.location.hostname + ":" + window.location.port)){
			return;
		}else{
			var data = event.data;
			//Check the event type
			if (data.type == "oauth_redirect_uri") {
				var redirectUrl = data.data;
				var code = gupUrl(redirectUrl, "code");
				var state = gupUrl(redirectUrl, "state");
				state = decodeURIComponent(state);
				var callerMethod = gupUrl("http://localhost?"+state, "callerMethod");
				
		    	var baseUrl = encodeURI(redirectUrl.split("?")[0]);
				
		    	if (callerMethod!=""){
					if (callerMethod=="impdep"){
						//the user is importing a deploy
						OauthManager.getVleCourses(code, baseUrl, state);
					}else if (callerMethod=="postdep"){
						//the user is posting a deploy
						OauthManager.getOauthLogin(code, baseUrl, state);
					}else if (callerMethod=="newdep"){
						//the user is creating a new deploy
						OauthManager.getNewDeployCourses(code, baseUrl, state);
					}else if (callerMethod=="newle"){
						//the user is creating a new learning environment
						OauthManager.getOauthCredentials(code, baseUrl, state);
					}
				}
				/*else{
					var error = gupUrl(redirectUrl, "error");
					if (error == "access_denied"){
						//something to do
					}
				}*/
			} else {
				return;
			}
		}
	}
	
}