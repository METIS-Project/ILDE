/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glueps.adaptors.ARbrowsers.adaptors.junaio;



import glueps.adaptors.ARbrowsers.adaptors.Constants;
import glueps.adaptors.ARbrowsers.adaptors.Utils;
import glueps.adaptors.ARbrowsers.model.ARbrowserResponse;
import glueps.adaptors.ARbrowsers.model.LoggedUser;
import glueps.adaptors.ARbrowsers.model.Poi;
import glueps.adaptors.ARbrowsers.service.AuthService;
import glueps.adaptors.ARbrowsers.service.LogService;
import glueps.adaptors.ARbrowsers.service.PoiService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.util.Series;


public class JunaioAdaptor
{


	
    public static ARbrowserResponse getPois(String appType, String uri, Form options, Series<Parameter> headers, String remoteIpAddress)
    {

		ARbrowserResponse response = null;

    	String adminKey = options.getFirstValue( "adminKey" );
    	String uid = Utils.getDecodeAndEncode(options, "uid", null);   
		
		//debug
		System.out.println("Received GET in the junaio servlet: " + uri);
		
		//We get the author (if the channel is a filter), and if the logout control will be shown
		HashMap getauthor = new HashMap();
		getauthor = Utils.getAuthor(uri);
		String author = (String) getauthor.get("author");
		boolean showLogout = (Boolean) getauthor.get("showLogout");
	
		//Do authentication and answer unauthorized if it fails. If the URL contains the adminKey, don't check authentication (for tests)
        if (!JunaioUtils.isAuthorized(headers) || !JunaioUtils.isValidSignature(uri, headers , Constants.JUNAIO_KEY))
        {
            if( adminKey == null || ! adminKey.equals( Constants.JUNAIO_ADMIN_KEY ))
            {
                // Returns Unauthorized 401
            	response = new ARbrowserResponse("Unauthorized", Status.CLIENT_ERROR_UNAUTHORIZED, null); 
                return response;
            }
        }

        double latitude = 41.661289;
        double longitude = -4.7075631;

        String lparameter = options.getFirstValue("l");

        if (lparameter != null)
        {
            StringTokenizer st = new StringTokenizer(lparameter, ",");
            latitude = Double.parseDouble(st.nextToken());
            longitude = Double.parseDouble(st.nextToken());
        }
        int max = Utils.getInt(options, "m", 100);
        
        
        
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

			 
			 //Recognizing the junaio channel, setting the position type, and the channel to switch
			 String switchchannel = Constants.JUNAIO_CHANNEL_ID;
			 if (uri.contains("/junaio/")){
				 loggedUsers.get(uid).setPositionType(Constants.POS_TYPE_GEOPOSITION);
				 if (author != null){
					 switchchannel = Constants.configuration().getProperty("junaio.filter." + author + ".channelid.glue", Constants.JUNAIO_GLUE_CHANNEL_ID);
				 } else {					 
					 switchchannel = Constants.JUNAIO_GLUE_CHANNEL_ID;
				 }
			 } else if (uri.contains("/junaioglue/")){
				 loggedUsers.get(uid).setPositionType(Constants.POS_TYPE_JUNAIOMARKER);
				 if (author != null){
					 switchchannel = Constants.configuration().getProperty("junaio.filter." + author + ".channelid.geo", Constants.JUNAIO_CHANNEL_ID);
				 } else {					 
					 switchchannel = Constants.JUNAIO_CHANNEL_ID;
				 }
			 }
			 
			 
			 String username = loggedUsers.get(uid).getUsername();
			 String deployId = loggedUsers.get(uid).getDeployId();
			 String positionType = loggedUsers.get(uid).getPositionType();
			 

		 
			 // ### START TO CONSTRUCT THE ANSWER ###
			 StringBuilder stringBuilder = new StringBuilder();
		     
			// stringBuilder.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
			 stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			 
	 
			 String channeltype = "both";
			 String glueicon = "http://www.gsic.uva.es/glue/images/logoGlue3.PNG";
			 String glueicon3D = Constants.JUNAIO_ICON_GLUE;
			 String logouticon = Constants.JUNAIO_ICON_LOGOUT;
			 String urlLogoutEncoded = Constants.SERVER_URL + "arbrowser/" + appType + Constants.URL_LOGOUT + "?uid=" + uid;
			
			 //If the channel is a filter, search for icons in the properties file
			 if (author != null) {
				 channeltype = Constants.configuration().getProperty("junaio.filter." + author + ".channeltype", "both");
				 glueicon = Constants.configuration().getProperty("junaio.filter." + author + ".thumbnail.glue", "http://www.gsic.uva.es/glue/images/logoGlue3.PNG");
				 glueicon3D = Constants.configuration().getProperty("junaio.filter." + author + ".icon.glue", Constants.configuration().getProperty("junaio.icon.glue", "http://localhost:8287/GLUEPSManager/gui/glueps/arbrowsers/glue.zip"));
				 logouticon = Constants.configuration().getProperty("junaio.filter." + author + ".icon.logout", Constants.configuration().getProperty("junaio.icon.logout", "http://localhost:8287/GLUEPSManager/gui/glueps/arbrowsers/logout.zip"));
			 }
			 
			 
			 //Tracking file for images recognition. It is configurable via app.properties in filter channels
			 String trackingfile = Constants.SERVER_URL + "gui/glueps/arbrowsers/markers/ID_Marker1-21/TrackingConfiguration_smoother.xml_enc";
			 if (author != null) {
				 trackingfile = Constants.configuration().getProperty("junaio.filter." + author + ".trackingfile", trackingfile);
			 }
			 
			 
			 if (positionType.equals(Constants.POS_TYPE_JUNAIOMARKER) && (!channeltype.equals("glue"))){

				 // ### SCREEN CONTROLS ###
				 stringBuilder.append("<results trackingurl=\"" + trackingfile + "\">");
				 //Geo button	 
				 stringBuilder.append("<poi id=\"geo\" interactionfeedback=\"none\">");
				 stringBuilder.append("<name><![CDATA[geo]]></name>");
				 stringBuilder.append("<o>1.5708,0,0</o>");
				 stringBuilder.append("<mime-type>model/obj</mime-type>");
				 stringBuilder.append("<s>2.8</s>");
				 stringBuilder.append("<mainresource>" + Constants.JUNAIO_ICON_GEO + "</mainresource>"); 
				 stringBuilder.append("<relativetoscreen>1,0.02</relativetoscreen>"); 
				 stringBuilder.append("<customization>");
				 stringBuilder.append("<name>Cust_for_marker</name>");
				 stringBuilder.append("<type>url</type>");
				 stringBuilder.append("<node_id>click</node_id>");
				 stringBuilder.append("<value>junaio://channel/switchChannel/?id=" + switchchannel + "</value>");
				 stringBuilder.append("</customization>"); 
	
				 stringBuilder.append("</poi>");
			 } else if (positionType.equals(Constants.POS_TYPE_GEOPOSITION) && (!channeltype.equals("geo"))) {
				 stringBuilder.append("<results>");
				 //Marker button 
				 stringBuilder.append("<poi id=\"marker\" interactionfeedback=\"none\">");
				 stringBuilder.append("<name><![CDATA[marker]]></name>");
				 stringBuilder.append("<o>1.5708,0,0</o>");
				 stringBuilder.append("<mime-type>model/obj</mime-type>");
				 stringBuilder.append("<s>5</s>");
				 stringBuilder.append("<mainresource>" + Constants.JUNAIO_ICON_MARKER + "</mainresource>"); 
				 stringBuilder.append("<relativetoscreen>1,0.02</relativetoscreen>"); 
				 stringBuilder.append("<customization>");
				 stringBuilder.append("<name>Cust_for_marker</name>");
				 stringBuilder.append("<type>url</type>");
				 stringBuilder.append("<node_id>click</node_id>");
				 stringBuilder.append("<value>junaio://channel/switchChannel/?id=" + switchchannel + "</value>");
				 stringBuilder.append("</customization>"); 
	
				 stringBuilder.append("</poi>");
			 } else  if (positionType.equals(Constants.POS_TYPE_JUNAIOMARKER)){
				 stringBuilder.append("<results trackingurl=\"" + trackingfile + "\">");
			 } else {
				 stringBuilder.append("<results>");				 
			 }
			 //Logout button
			 
			 if (showLogout) {
				 stringBuilder.append("<poi id=\"Logout_button\" interactionfeedback=\"none\">");
				 stringBuilder.append("<name><![CDATA[Logout]]></name>");
				 stringBuilder.append("<o>1.5708,0,0</o>");
				 stringBuilder.append("<mime-type>model/obj</mime-type>");
				 stringBuilder.append("<s>5</s>");
				 stringBuilder.append("<mainresource>" + logouticon + "</mainresource>"); 
				 stringBuilder.append("<relativetoscreen>0,0.02</relativetoscreen>"); 
				 stringBuilder.append("<customization>");
				 stringBuilder.append("<name>Cust_for_logout</name>");
				 stringBuilder.append("<type>url</type>");
				 stringBuilder.append("<node_id>click</node_id>");
				 stringBuilder.append("<value>" + urlLogoutEncoded + "</value>");
				 stringBuilder.append("</customization>"); 
				 stringBuilder.append("</poi>");
			 }
			// ### END OF SCREEN CONTROLS ###

			 // ### LIST OF POIs ###
			 
			 //Selection of geopositioned or positioned via marker POIs
			List<Poi> listPoi = PoiService.getNearestPoisForPositionType(deployId, username, positionType, latitude, longitude, max, false);

	        for (Poi poi : listPoi)
	        {
	            //Avoid strange characters in texts	            <-- Al pasar de servlets a RESTLET ya no hace falta 8-O
//	            String name = Utils.toISO(poi.getName());
//	            String desc = Utils.toISO(poi.getDescription());
	            
	            String name = poi.getName();
	            String desc = poi.getDescription();

	        	//TODO include orientation attribute
	      /*
	        	//Si se usa el campo "orientation"
	        	String orientation;
	        	if (tag.getOrientation() != null){
					  String[] temp;
					//   delimiter 
					  String delimiter = ",";
					//   given string will be split by the argument delimiter provided. 
					  temp = tag.getOrientation().split(delimiter);
					  Double x = Double.parseDouble(temp[0])*3.1416/180;
					  Double y = Double.parseDouble(temp[1])*3.1416/180;
					  Double z = Double.parseDouble(temp[2])*3.1416/180;
					  orientation = x.toString() + "," + y.toString() + "," + z.toString();
	        	} else {
	        		orientation = null;
	        	}
	        */	
	            
	            // ### Include coordinates in geoposition or COSID with markers ###
	            if (positionType.equals(Constants.POS_TYPE_JUNAIOMARKER)) {

	            	String cosid = poi.getCosid();
	            	stringBuilder.append("<poi id=\"" + poi.getPoiId() + "\" cosid=\"" + cosid + "\" interactionfeedback=\"none\">");

	            } else {
	            	stringBuilder.append("<poi id=\"" + poi.getPoiId() + "\" interactionfeedback=\"none\">");
		            stringBuilder.append("<l>" + poi.getLat() + "," + poi.getLon() + ",0</l>");
		            stringBuilder.append("<maxdistance>" + poi.getMaxdistance() + "</maxdistance>"); 
		            stringBuilder.append("<route>true</route>"); 
	            }
	            
	            // ### Include name, description and date	            
	            stringBuilder.append("<name><![CDATA[" + name + "]]></name>");	            
	            if (desc != null){
	            	stringBuilder.append("<description><![CDATA[" + desc + "]]></description>");
	            } else {
	            	stringBuilder.append("<description></description>");
	            }
	            String localeString = options.getFirstValue("Accept-Language");
	            Locale locale = Locale.getDefault();
	            if (localeString != null){
	            	locale = new Locale(localeString);
	            }
	            stringBuilder.append("<date><![CDATA[" + poi.getFormatedDate(locale) + "]]></date>");


	            // ### CASE 3dmodels ###
	            if (poi.getPoitype()== "3dmodel") {
	            	
	            	stringBuilder.append("<mime-type>model/obj</mime-type>");
	            	stringBuilder.append("<force3d>true</force3d>");
		            stringBuilder.append("<thumbnail>" + glueicon + "</thumbnail>");
		            stringBuilder.append("<icon>" + glueicon + "</icon>");
		            stringBuilder.append("<mainresource>" + poi.getLocation() + "</mainresource>"); 
		            stringBuilder.append("<translation>0,0,0</translation>");
	            	if (poi.getScale() != null) {
			            //"Scale" is scaled to start from the default value, and don't mislead the user
		            	Double scaleDouble = Double.parseDouble(poi.getScale())*Double.parseDouble(Constants.JUNAIO_3DMODEL_DEFAULT_SCALE);
	            		stringBuilder.append("<s>" + scaleDouble + "</s>");
	            	} else {
	            		stringBuilder.append("<s>" + Constants.JUNAIO_3DMODEL_DEFAULT_SCALE + "</s>");
	            	}
	            	stringBuilder.append("<o>" + Constants.JUNAIO_3DMODEL_DEFAULT_ORIENTATION + "</o>");
	            	
	            	//TODO orientation attribute is not currently used
//	            	if (orientation != null) {
//	            		stringBuilder.append("<o>" + orientation + "</o>");
//	            	} else {
//	            		stringBuilder.append("<o>" + Constants.JUNAIO_3DMODEL_DEFAULT_ORIENTATION + "</o>");
//	            	}
	            	
	            	
	            // ### CASE ARimages ###
	            } else if (poi.getPoitype()== "ARimage"){
	            	stringBuilder.append("<mime-type>model/obj</mime-type>");
	            	//Positioning with markers
	            	if (positionType.equals(Constants.POS_TYPE_JUNAIOMARKER)) {
	            		stringBuilder.append("<o>" + Constants.JUNAIO_ARIMAGE_JUNMARKER_DEFAULT_ORIENTATION + "</o>");
	            		if (poi.getScale() != null) {
			            	Double translationDouble = Double.parseDouble(poi.getScale())*(-190);
		            		stringBuilder.append("<translation>0," + translationDouble.toString() + ",0</translation>");
			            	stringBuilder.append("<s>" + poi.getScale() + "</s>");
		            		} else {
		            			
		            			//TODO positioning POIs inside an image (e.g., a picture in a museum)
//		            			//(1)
//		            		   	if (orientation != null) {
//				            		stringBuilder.append("<translation>" + tag.getOrientation() + "</translation>");
//				            		stringBuilder.append("<s>0.05</s>");
//				            	} else {
//				    	            stringBuilder.append("<translation>0,-190,0</translation>");
//				    		        stringBuilder.append("<s>" + Constants.JUNAIO_ARIMAGE_DEFAULT_SCALE + "</s>");
//				            	}
//		            			//END (1)
		            		   	
		            			//To remove if (1)
		            			stringBuilder.append("<translation>0,-190,0</translation>");
			            		stringBuilder.append("<s>" + Constants.JUNAIO_ARIMAGE_DEFAULT_SCALE + "</s>");
			            		//END to remove if (1)
			            	}
		            		
		            		//TODO orientation attribute is not currently used
//			            	if (orientation != null) {
//			            		stringBuilder.append("<o>" + orientation + "</o>");
//			            	} else {
//			            		stringBuilder.append("<o>0,1.5708,1.5708</o>");
//			            		stringBuilder.append("<o>" + Constants.JUNAIO_ARIMAGE_JUNMARKER_DEFAULT_ORIENTATION + "</o>");
//			            	}
	            	//Geopositioning
	            	} else {
	            		stringBuilder.append("<o>" + Constants.JUNAIO_ARIMAGE_GEO_DEFAULT_ORIENTATION + "</o>");
	            		stringBuilder.append("<translation>0,0,0</translation>");
	            		//TODO orientation attribute is not currently used
//		            	if (orientation != null) {
//		            		stringBuilder.append("<o>" + orientation + "</o>");
//		            	} else {
//		            		stringBuilder.append("<o>" + Constants.JUNAIO_ARIMAGE_GEO_DEFAULT_ORIENTATION + "</o>");
//		            	}
		     
	            	}	           		
	            	stringBuilder.append("<force3d>true</force3d>");
		            stringBuilder.append("<thumbnail>" + glueicon + "</thumbnail>");
		            stringBuilder.append("<icon>" + glueicon + "</icon>");
		            stringBuilder.append("<resource>" + poi.getLocation() + "</resource>"); 
		            stringBuilder.append("<mainresource>" + Constants.SERVER_URL + "gui/glueps/arbrowsers/plane2.md2</mainresource>"); 
	            	
	            	
	            // ### CASE normal gluelets ###
	            } else {		            
		            stringBuilder.append("<thumbnail>" + glueicon + "</thumbnail>");
		            stringBuilder.append("<icon>" + glueicon + "</icon>");
		            stringBuilder.append("<homepage>" + poi.getLocation() + "</homepage>"); 
		            stringBuilder.append("<translation>0,0,0</translation>");
		            
		            //Positioning with markers
		            if (positionType.equals(Constants.POS_TYPE_JUNAIOMARKER)) {
		            	
			            	//Configuration of interaction type (onTargetDetect, Click, show description window)
			            	
			            	//Retrieving interaction type (default, an particular if the channel is a filter)
			            	String glueletTriggerType = Constants.configuration().getProperty("junaio.default.glueletTriggerType", "none");
			            	if (author != null){
			            		glueletTriggerType = Constants.configuration().getProperty("junaio.filter." + author + ".glueletTriggerType", "none");
			            	}	            		
		            		//Interaction type onTargetDetect
		            		if (glueletTriggerType.equals("onTargetDetect")){
		            			//Modo de mostrar el gluelet: sólo al detectar el marcador
		            	
	    			   			 stringBuilder.append("<customization>");
	    						 stringBuilder.append("<name>Cust_for_"+ poi.getPoiId() + "</name>");
	    						 stringBuilder.append("<type>url</type>");
	    						 stringBuilder.append("<node_id>onTargetDetect</node_id>");
	    						 stringBuilder.append("<value>" + poi.getLocation() + "</value>");
	    						 stringBuilder.append("</customization>"); 
	    						 
	    			   			 stringBuilder.append("<customization>");
	    						 stringBuilder.append("<name>Cust_event_for_"+ poi.getPoiId() + "</name>");
	    						 stringBuilder.append("<type>triggerEvent</type>");
	    						 stringBuilder.append("<node_id>onTargetDetect</node_id>");
	    						 stringBuilder.append("</customization>"); 
	    					//Interaction type click
		            		} else if (glueletTriggerType.equals("click")) {
		            			//Modo de mostrar el gluelet: al tocar el logo
	    		            	stringBuilder.append("<mime-type>model/obj</mime-type>");
	    		            	stringBuilder.append("<mainresource>" + glueicon3D + "</mainresource>"); 
	    		            	stringBuilder.append("<force3d>false</force3d>");
	    		            	stringBuilder.append("<s>25</s>");
	    		            	stringBuilder.append("<o>1.5708,0,0</o>");
	    		            	
	    			   			stringBuilder.append("<customization>");
	    			   			stringBuilder.append("<name>Cust_for_"+ poi.getPoiId() + "</name>");
	    						stringBuilder.append("<type>url</type>");
	    						stringBuilder.append("<node_id>click</node_id>");
	    						stringBuilder.append("<value>" + poi.getLocation() + "</value>");
	    						stringBuilder.append("</customization>"); 		            			
		            		//Interaction type normal: 
	    					//a logo is shown, clicking in the logo a window with description appears, with access to gluelet	
		            		} else {	            			
	    		            	stringBuilder.append("<mime-type>model/obj</mime-type>");
	    		            	stringBuilder.append("<mainresource>" + glueicon3D + "</mainresource>"); 
	    		            	stringBuilder.append("<force3d>false</force3d>");
	    		            	stringBuilder.append("<s>25</s>");
	    		            	stringBuilder.append("<o>1.5708,0,0</o>");            			
		            		}
	            	//Geoposition
		            } else {
			            stringBuilder.append("<o>0,0,0</o>");
		            	stringBuilder.append("<mime-type>text/plain</mime-type>");
		            	stringBuilder.append("<mainresource>" + poi.getLocation() + "</mainresource>"); 
		            }
	            }
	            stringBuilder.append("</poi>");
	        }

	        stringBuilder.append("</results>");
	        
		    String answer = stringBuilder.toString();
		    response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.APPLICATION_XML);
	        
	     //No user is logged in the device	 
		 } else {			 
			 if (uri.contains("/filter/")){  
			 //If the channel is a filter: autologin and doGet is called again to load POIs. (Check this function if creating a new adaptor)
				 if (AuthService.autologin(appType, uri, options, remoteIpAddress, author)){
					 getPois(appType, uri, options, headers, remoteIpAddress);
				 }			 	 
			 } else	{
				//Generic channel (not a filter): The channel requires login			 
				 
				 //GUI POI to access login page
				 StringBuilder stringBuilder = new StringBuilder();
				 stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				 stringBuilder.append("<results>");
				 stringBuilder.append("<poi id=\"Login_button\" interactionfeedback=\"none\">");
				 stringBuilder.append("<name><![CDATA[Logout]]></name>");
				 stringBuilder.append("<description>Logout</description>");
				 stringBuilder.append("<o>1.5708,0,0</o>");
				 stringBuilder.append("<mime-type>model/obj</mime-type>");
				 stringBuilder.append("<s>20</s>");
				 stringBuilder.append("<mainresource>" + Constants.JUNAIO_ICON_LOGIN + "</mainresource>"); 
				 stringBuilder.append("<relativetoscreen>0.5,0.7</relativetoscreen>"); 
				 stringBuilder.append("<customization>");
				 stringBuilder.append("<name>Cust_for_logout</name>");
				 stringBuilder.append("<type>url</type>");
				 stringBuilder.append("<node_id>click</node_id>");
				 stringBuilder.append("<value>" + Constants.SERVER_URL + "arbrowser/" + appType + Constants.URL_LOGIN + "?uid=" + uid + "</value>");
				 stringBuilder.append("</customization>"); 
				 stringBuilder.append("</poi>");
				 stringBuilder.append("</results>");
				 
				 String answer = stringBuilder.toString();
				 response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.APPLICATION_XML);			 
			 }

		 }
        return response;
    }
    
    // Event handler
    public static void createLog(String appType, String uri, Form options, Series<Parameter> headers, String remoteIpAddress)
    {
    	//debug
    	System.out.println("POST received in Junaio Event Controller to URI " + uri);
    	
        String uid;

		uid = Utils.getDecodeAndEncode(options, "uid", null);
		
    	//Search for author if channel is a filter		  
		String author = (String) Utils.getAuthor(uri).get("author");

		 //Log
		 String logfile = Constants.JUNAIO_LOGFILE;
		 String action = "interaction";
		 LogService.writelog(appType, options, remoteIpAddress, uid, action, author, logfile);
	 
    }
    


}


