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
 * 
 */
var LdShakeManager = {
		
		ldShakeMode: false, 
		
		sectoken: null,
		
		ldshakeFrameOrigin: null,
		
		init: function(){
			LdShakeManager.ldShakeMode = false;
			LdShakeManager.sectoken = null;
			LdShakeManager.ldshakeFrameOrigin = null;
		},
		
		/**
		 * Set the value for ldshakeFrameOrigin from the deploy when the deploy has been loaded from the DB
		 */
		setFrameOrigin: function(){
			if (typeof JsonDB.deploy.ldshakeFrameOrigin != 'undefined'){
				LdShakeManager.ldshakeFrameOrigin = JsonDB.deploy.ldshakeFrameOrigin;
		    }
		    else{
		        //LdShakeManager.ldshakeFrameOrigin = 'http://localhost';
		    	LdShakeManager.ldshakeFrameOrigin = 'http://193.145.50.135';
		    }
		},
		
		buildLdshakeUrl: function(url){
			if (url.indexOf("/GLUEPSManager/ldshake/")==-1){
				url = url.replace("/GLUEPSManager/", "/GLUEPSManager/ldshake/");
			}
			if (url.indexOf("?")==-1){
				url = url + "?sectoken=" + LdShakeManager.sectoken;
			}
			else{
				url = url + "&sectoken=" + LdShakeManager.sectoken;
			}
			return url;
		},
		
        /*
         * Receives a message from the LdShake which contains this Glue!PS inside a iframe in order to change the title of the deploy
         */
		setTitle: function(event) {
		       if(event.origin !== LdShakeManager.ldshakeFrameOrigin){
		        return;
		       }else{
		            var data = event.data;
		            //Check the event type
		            if (data.type == "ldshake_name"){
		                var title = data.data;
		                JsonDB.deploy.name = title;
		                JsonDB.notifyChanges();
		            }
		            else{
		                return;
		            }
		       }
		 },
		 
         /*
          * Post a message to the LdShake which contains this Glue!PS inside a iframe in order to notify that Glue!PS has completed the deployment process successfully
          */
         postMessageDeploymentCompleted: function(){
             var ready = {type: "glueps_deployment", 
                          data: true};
             parent.postMessage(ready, LdShakeManager.ldshakeFrameOrigin);
         }
}