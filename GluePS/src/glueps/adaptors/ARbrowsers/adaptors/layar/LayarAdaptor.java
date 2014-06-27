package glueps.adaptors.ARbrowsers.adaptors.layar;

import glueps.adaptors.ARbrowsers.adaptors.Constants;
import glueps.adaptors.ARbrowsers.adaptors.Utils;
import glueps.adaptors.ARbrowsers.model.ARbrowserResponse;
import glueps.adaptors.ARbrowsers.model.LoggedUser;
import glueps.adaptors.ARbrowsers.model.Poi;
import glueps.adaptors.ARbrowsers.service.AuthService;
import glueps.adaptors.ARbrowsers.service.PoiService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;


import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.util.Series;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class LayarAdaptor
{
	

	public static ARbrowserResponse getPois(String appType, String uri, Form options, Series<Parameter> headers, String remoteIpAddress)
    {
		ARbrowserResponse response = null;
        
        int defmaxpois  = Constants.LAYAR_DEF_MAX_POIS;

        String layerName = options.getFirstValue("layerName");

        double latitude = Utils.getDouble(options, "lat", 41.6623469);
        double longitude = Utils.getDouble(options, "lon", -4.7061101);
        int max = Utils.getInt(options, "max", defmaxpois); // not provided by Layar
        String radiusParam = options.getFirstValue("radius");
        long radius = (radiusParam != null) ? Long.parseLong(radiusParam) : 1000L;

         
         String uid = Utils.getDecodeAndEncode(options, "userId", null);  
      

 		//debug
 		System.out.println("Received GET in the layar servlet: " + uri);
 		
//    	  String[] geturlparts;				 
//    	  String delimiter = "/";
//    	  geturlparts = uri.split(delimiter);
    	  
  		//We get the author (if the channel is a filter), and if the logout control will be shown
  		HashMap getauthor = new HashMap();
  		getauthor = Utils.getAuthor(uri);
  		String author = (String) getauthor.get("author");
  		boolean showLogout = (Boolean) getauthor.get("showLogout");

  		//Checking if a user is logged in the device
		 HashMap<String,LoggedUser> loggedUsers = new HashMap<String,LoggedUser>();
		 loggedUsers = AuthService.readLoggedUsers();
		 LoggedUser loggeduser = null;
		 if (loggedUsers != null){
			 if (!loggedUsers.isEmpty()){
				 loggeduser = loggedUsers.get(uid);
			 }			 
		 }
		 if (loggeduser != null){
				//A user is logged in the device
			 
				 //If the channel is a filter, the user is auto-logged in (Check this function if creating a new adaptor)
				 AuthService.autologin(appType, uri, options, remoteIpAddress, author);
				 
				 //Recognizing the layar channel, setting the position type, and the channel to switch
				 String switchchannel = Constants.LAYAR_CHANNEL_ID;
				 if (uri.contains("/layar/")){
					 loggedUsers.get(uid).setPositionType(Constants.POS_TYPE_GEOPOSITION);
					 if (author != null){
						 switchchannel = Constants.configuration().getProperty("layar.filter." + author + ".channelid.vision", Constants.LAYAR_VISION_CHANNEL_ID);
					 } else {						 
						 switchchannel = Constants.LAYAR_VISION_CHANNEL_ID;
					 }
				 } else if (uri.contains("/layarvision/")){
					 loggedUsers.get(uid).setPositionType(Constants.POS_TYPE_JUNAIOMARKER);
					 if (author != null){
						 switchchannel = Constants.configuration().getProperty("layar.filter." + author + ".channelid.geo", Constants.LAYAR_CHANNEL_ID);
					 } else {						 
						 switchchannel = Constants.LAYAR_CHANNEL_ID;
					 }
				 }
				 
				 
				 String username = loggedUsers.get(uid).getUsername();
				 String deployId = loggedUsers.get(uid).getDeployId();
				 String positionType = loggedUsers.get(uid).getPositionType();
				 

				// ### START TO CONSTRUCT THE ANSWER ###
				 
				 //Start layer in JSON output
	             JsonObject layer = new JsonObject();
	             layer.addProperty("layer", layerName);
	             layer.addProperty("errorString", "OK");
	             layer.addProperty("errorCode", 0);
		         layer.addProperty("morePages", false);
		         layer.add("nextPageKey", null);		         
		         //     layer.addProperty("disableClueMenu", true);
		         JsonArray hotspots = new JsonArray();
	             JsonArray layerActions = new JsonArray();
	             

				 

				 
				 String channeltype = "both";
				 String glueicon = "http://www.gsic.uva.es/glue/images/logoGlue3.PNG";
				 String glueicon3D = Constants.LAYAR_ICON_GLUE;
				 String logouticon = Constants.LAYAR_ICON_LOGOUT;
				
				//If the channel is a filter, search for icons in the properties file
				 if (author != null) {
					 channeltype = Constants.configuration().getProperty("junaio.filter." + author + ".channeltype", "both");
					 glueicon = Constants.configuration().getProperty("junaio.filter." + author + ".thumbnail.glue", "http://www.gsic.uva.es/glue/images/logoGlue3.PNG");
					 glueicon3D = Constants.configuration().getProperty("junaio.filter." + author + ".icon.glue", Constants.configuration().getProperty("junaio.icon.glue", "http://localhost:8287/GLUEPSManager/gui/glueps/arbrowsers/glue.zip"));
					 logouticon = Constants.configuration().getProperty("junaio.filter." + author + ".icon.logout", Constants.configuration().getProperty("junaio.icon.logout", "http://localhost:8287/GLUEPSManager/gui/glueps/arbrowsers/logout.zip"));
				 }
				 
				 if (positionType.equals(Constants.POS_TYPE_JUNAIOMARKER) && (!channeltype.equals("glue"))){

					// ### SCREEN CONTROLS ###
					 
					 //Geo button	 --> Version 7 of client has action layer disabled and this doesn't work					 
		             JsonObject geoAction = new JsonObject();
		             geoAction.addProperty("contentType", "application/vnd.layar.internal");
		             geoAction.addProperty("uri", "layar://" + switchchannel + "/?action=refresh");
		             geoAction.addProperty("label", "Geo layer");
		             layerActions.add(geoAction);	
		             
		            //Adding POI to hotspots for allowing to swap to geo layer  --> This is the workaround for the geo button
		             String iconLogout = Constants.LAYAR_ICON_GEO;
		        	hotspots = addPOItoHotspots (hotspots, "geolayer", "geolayer", "geolayer", "ARimage", 
		        			Constants.POS_TYPE_JUNAIOMARKER, iconLogout, "geolayer", null, null, null, switchchannel, uid); 
		        	
		        	//debug
		        	// System.out.println("adding geo POI: " + hotspots.toString());

				 } else if (positionType.equals(Constants.POS_TYPE_GEOPOSITION) && (!channeltype.equals("geo"))) {

					 //Vision button	 
		             JsonObject visionAction = new JsonObject();
		             visionAction.addProperty("contentType", "application/vnd.layar.internal");
		             visionAction.addProperty("uri", "layar://" + switchchannel + "/?action=refresh");
		             visionAction.addProperty("label", "Marker layer");
		             layerActions.add(visionAction);	
		             
				 } 
				 
				 //Logout button				 
				 if (showLogout) {
			         //action for logout --> In client version 7 it doesn't work in vision layers

		             JsonObject logoutAction = new JsonObject();
		             logoutAction.addProperty("contentType", "text/html");
		             logoutAction.addProperty("uri", Constants.SERVER_URL + "arbrowser/" + appType + Constants.URL_LOGOUT + "?uid=" + uid);
		             logoutAction.addProperty("label", "Logout");
		             layerActions.add(logoutAction); 
				 }

				 //adding actions buttons to layer
	             layer.add("actions", layerActions);	

	             // ### END OF SCREEN CONTROLS ###
			 
				 // ### LIST OF POIs ###
				 
				 //Selection of geopositioned or positioned via marker POIs
				List<Poi> listTag = PoiService.getNearestPoisForPositionType(deployId, username, positionType, latitude, longitude, max, true);
				
		        for (Poi poi : listTag)
		        {
		            //Avoid strange characters in texts	            <-- Al pasar de servlets a RESTLET ya no hace falta 8-O
//		            String name = Utils.toISO(poi.getName());
//		            String desc = Utils.toISO(poi.getDescription());
		            
		            String name = poi.getName();
		            String desc = poi.getDescription();

		            //Adding POI to hotspots
		        	hotspots = addPOItoHotspots (hotspots, poi.getPoiId(), name, desc, poi.getPoitype(), 
	                          positionType, poi.getLocation().toString(), poi.getCosid(), poi.getLat(), poi.getLon(), poi.getScale(), null, uid);        	
		        }

		      	 layer.add("hotspots", hotspots);
		      	 response = new ARbrowserResponse(layer.toString(), Status.SUCCESS_OK, MediaType.APPLICATION_JSON);	 
		      	 //debug
		   		 System.out.println("JSON response: " + layer.toString());	        

			     
			     //debug
	        	// System.out.println("JSON response: " + layer.toString());
	        	 
	        	// ### END OF ANSWER ###		        
		 
	        //No user is logged in the device	  
			 } else {
				 if (uri.contains("/filter/")){
					 //If the channel is a filter: autologin and doGet is called again to load POIs. (Check this function if creating a new adaptor)
					 if (AuthService.autologin(appType, uri, options, remoteIpAddress, author)){
						 getPois(appType, uri, options, headers, remoteIpAddress);
					 }
				 } else	{
					//Generic channel (not a filter): The channel requires login	
					 
					//dialog to access login page
				 
		            JsonObject showDialog = new JsonObject();
		            showDialog.addProperty("title", "Login");
		            showDialog.addProperty("description", "Please, login in a course");
		            showDialog.addProperty("iconURL", Constants.LAYAR_ICON_LOGIN);
		            
		            //action
	                JsonArray actions = new JsonArray();
	                JsonObject action = new JsonObject();
	                action.addProperty("contentType", "text/html");
	                action.addProperty("uri", Constants.SERVER_URL + "arbrowser/" + appType + Constants.URL_LOGIN + "?uid=" + uid);
	                action.addProperty("label", "Login");
	                action.addProperty("activityType", "16");
	                actions.add(action);
	                showDialog.add("actions", actions);
					 
	                JsonObject layer = new JsonObject();
	                JsonArray hotspots = new JsonArray();
	                
	                layer.add("hotspots", hotspots);
	                
	                layer.add("actions", actions);
	        
			        layer.add("showDialog", showDialog);
			        layer.addProperty("layer", layerName);
		            layer.addProperty("errorString", "OK");
		            layer.addProperty("errorCode", 0);
			        layer.addProperty("morePages", false);
			        layer.add("nextPageKey", null);
			        
			        response = new ARbrowserResponse(layer.toString(), Status.SUCCESS_OK, MediaType.APPLICATION_JSON);	 
			        //debug
			   		System.out.println("JSON response: " + layer.toString());
			        
					 
				 }
			 }
   		 return response;
    }
    
    
    private static JsonArray addPOItoHotspots (JsonArray hotspots, String id, String title, String description, String poitype, String positionType, 
    		                          String location, String cosid, Double latitude, Double longitude, String scale, String switchchannel, String uid){
    	
    	// ### Generic attributes ###
        JsonObject poi = new JsonObject();
        poi.addProperty("id", id);
        
        JsonObject text = new JsonObject();
        text.addProperty("title", title);
        if (description != null){
        	text.addProperty("description", description);
        } else {
        	text.addProperty("description", "");
        }        

        poi.add("text", text);
        
        poi.addProperty("imageURL", "http://www.gsic.uva.es/glueps/glueps-logo.png");
                
        JsonObject anchor = new JsonObject();
        
     // ### Include coordinates in geoposition or marker name with markers ###
        if (positionType.equals(Constants.POS_TYPE_JUNAIOMARKER)) {      
        	anchor.addProperty("referenceImage", "MetaioMarker" + cosid);
        } else {
            JsonObject geolocation = new JsonObject();
            geolocation.addProperty("lat", latitude);
            geolocation.addProperty("lon", longitude);
            anchor.add("geolocation", geolocation);
        }
         poi.add("anchor", anchor);
        
/*        JsonObject icon = new JsonObject();
        icon.addProperty("url", "http://www.gsic.uva.es/glue/images/logoGlue3.PNG");
        poi.add("icon", icon);
 */       
        
        // ### CASE 3D models ###
        if (poitype== "3dmodel") {
        	
            JsonObject object = new JsonObject();
            object.addProperty("contentType", "model/vnd.layar.l3d");
            object.addProperty("url", location);
            object.addProperty("size", "0.3");
            poi.add("object", object);            

            // Transform values            
        	if (scale != null) {
        		//"Scale" is scaled to start from the default value, and don't mislead the user
            	Double scaleDouble = Double.parseDouble(scale)*Double.parseDouble(Constants.JUNAIO_3DMODEL_DEFAULT_SCALE);
        		scale = scaleDouble.toString();
        	} else {
        		scale = Constants.JUNAIO_3DMODEL_DEFAULT_SCALE;
        	}
        	
            JsonObject transform = new JsonObject();
     //       transform.addProperty("rel", false);
     //       transform.addProperty("angle", );
            transform.addProperty("scale", scale);
            poi.add("transform", transform);
            
         // ### CASE ARimages ###
        } else if (poitype == "ARimage"){
        	
            JsonObject object = new JsonObject();
            object.addProperty("contentType", "image/vnd.layar.generic");
            object.addProperty("url", location);
            object.addProperty("size", "0");
            poi.add("object", object);            

            // Transform values
        	if (scale != null) {
	            ///"Scale" is scaled to start from the default value, and don't mislead the user
            	Double scaleDouble = Double.parseDouble(scale)*Double.parseDouble(Constants.JUNAIO_ARIMAGE_DEFAULT_SCALE);
        		scale = scaleDouble.toString();
        	} else {
        		scale = Constants.JUNAIO_ARIMAGE_DEFAULT_SCALE;
        	}
        	
            JsonObject transform = new JsonObject();
            
            JsonObject rotate = new JsonObject();
            rotate.addProperty("angle", 0);
            rotate.addProperty("rel", true);
            
            JsonObject axis = new JsonObject();            
            axis.addProperty("x", 0);
            axis.addProperty("y", 0);
            axis.addProperty("z", 1);
            
            rotate.add("axis", axis);
            transform.addProperty("scale", scale);
            transform.add("rotate", rotate);           
            
            poi.add("transform", transform);
	           		
            //If POI is geolayer (to swap to geo layer), action for swapping is added
            if (id == "geolayer"){            	
                JsonArray poiActions = new JsonArray();
                JsonObject poiAction = new JsonObject();
	            poiAction.addProperty("contentType", "application/vnd.layar.internal");
	            poiAction.addProperty("uri", "layar://" + switchchannel + "/?action=refresh");
	            poiAction.addProperty("label", "Geo layer");
                poiActions.add(poiAction);
                poi.add("actions", poiActions);	
            }
 
        	
        // ### CASE normal gluelets ###
        } else {

        	//In vision layer, a image is shown. In geo layer, a icon is shown. 
            if (positionType.equals(Constants.POS_TYPE_JUNAIOMARKER)) {      
        		JsonObject object = new JsonObject();
                object.addProperty("contentType", "image/vnd.layar.generic");
                object.addProperty("url", Constants.LAYAR_ICON_GLUE);
                object.addProperty("size", 0 );
                poi.add("object", object);
            } else {
                JsonObject icon = new JsonObject();
          //      icon.addProperty("url", "http://www.gsic.uva.es/glue/images/logoGlue3.PNG");
          //      icon.addProperty("url", null);
                icon.addProperty("type", 1);
                poi.add("icon", icon);
            }

             
            JsonObject transform = new JsonObject();
            
            JsonObject rotate = new JsonObject();
            rotate.addProperty("angle", 0);
            rotate.addProperty("rel", true);
            
            JsonObject axis = new JsonObject();            
            axis.addProperty("x", 0);
            axis.addProperty("y", 0);
            axis.addProperty("z", 1);
            
            rotate.add("axis", axis);
//            if (positionType.equals(Constants.POS_TYPE_GEOPOSITION)) { 
//            	transform.addProperty("scale", 2);
//            }            
            transform.add("rotate", rotate);
            
            poi.add("transform", transform);
             
            JsonArray poiActions = new JsonArray();
            JsonObject poiAction = new JsonObject();
            poiAction.addProperty("contentType", "text/html");
            poiAction.addProperty("uri", location);
            poiAction.addProperty("label", "Open");
            poiAction.addProperty("activityType", "33");
            poiActions.add(poiAction);     
            poi.add("actions", poiActions);	 
        }
        
          hotspots.add(poi);
   	
    	return hotspots;
    }

}

