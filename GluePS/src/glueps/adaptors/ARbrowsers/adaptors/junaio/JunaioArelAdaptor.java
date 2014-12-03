package glueps.adaptors.ARbrowsers.adaptors.junaio;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import glueps.adaptors.ARbrowsers.adaptors.Constants;
import glueps.adaptors.ARbrowsers.adaptors.Utils;
import glueps.adaptors.ARbrowsers.model.ARbrowserResponse;
import glueps.adaptors.ARbrowsers.model.ActiveUser;
import glueps.adaptors.ARbrowsers.model.LoggedUser;
import glueps.adaptors.ARbrowsers.model.Poi;
import glueps.adaptors.ARbrowsers.service.AuthService;
import glueps.adaptors.ARbrowsers.service.LogService;
import glueps.adaptors.ARbrowsers.service.PoiService;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.util.Series;

public class JunaioArelAdaptor {
	
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
        
        //Treatment of logs in AREL API
        String action = options.getFirstValue("filter_action");

        if (action == null){
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
   			 if (uri.contains("/junaioarel/")){
   				 loggedUsers.get(uid).setPositionType(Constants.POS_TYPE_GEOPOSITION);
   				 if (author != null){
   					 switchchannel = Constants.configuration().getProperty("junaio.filter." + author + ".channelid.glue", Constants.JUNAIO_GLUE_CHANNEL_ID);
   				 } else {					 
   					 switchchannel = Constants.JUNAIO_GLUE_CHANNEL_ID;
   				 }
   			 } else if (uri.contains("/junaioarelglue/")){
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
     		         stringBuilder.append("<arel><![CDATA[" + Constants.SERVER_URL + "gui/glueps/arbrowsers/junaio/arel.html]]></arel>");

   				 //Geo button	 
   				 stringBuilder.append("<object id=\"geo\">");
   				 stringBuilder.append("<title><![CDATA[geo]]></title>");
   				 stringBuilder.append("<parameters>");
   				 stringBuilder.append("<parameter key=\"channelid\"><![CDATA[" + switchchannel + "]]></parameter>");
   				 stringBuilder.append("</parameters>");
   				 stringBuilder.append("<assets3d>");
   				 stringBuilder.append("<model>" + Constants.JUNAIO_ICON_GEO + "</model>");
   				 stringBuilder.append("<transform>");
   				 stringBuilder.append("<rotation type=\"eulerrad\">");
   				 stringBuilder.append("<x>1.5708</x>");
   				 stringBuilder.append("<y>0</y>");
   				 stringBuilder.append("<z>0</z>");
   				 stringBuilder.append("</rotation>");
   				 stringBuilder.append("<scale>");
   				 stringBuilder.append("<x>2.8</x>");
   				 stringBuilder.append("<y>2.8</y>");
   				 stringBuilder.append("<z>2.8</z>");
   				 stringBuilder.append("</scale>");
   				 stringBuilder.append("</transform>");
//   				 stringBuilder.append("<properties>");
//   				 stringBuilder.append("<screencoordinates>");
//   				 stringBuilder.append("<x>1</x>");
//   				 stringBuilder.append("<y>0.02</y>");
//   				 stringBuilder.append("</screencoordinates>");
//   				 stringBuilder.append("</properties>");
   				 stringBuilder.append("</assets3d>");
   				 stringBuilder.append("</object>");
   			 } else if (positionType.equals(Constants.POS_TYPE_GEOPOSITION) && (!channeltype.equals("geo"))) {
   				 stringBuilder.append("<results>");
   			     stringBuilder.append("<arel><![CDATA[" + Constants.SERVER_URL + "gui/glueps/arbrowsers/junaio/arel.html]]></arel>");

   				 //Marker button 
   				 stringBuilder.append("<object id=\"marker\">");
   				 stringBuilder.append("<title><![CDATA[marker]]></title>");
   				 stringBuilder.append("<parameters>");
   				 stringBuilder.append("<parameter key=\"channelid\"><![CDATA[" + switchchannel + "]]></parameter>");
   				 stringBuilder.append("</parameters>");
   				 stringBuilder.append("<assets3d>");
   				 stringBuilder.append("<model>" + Constants.JUNAIO_ICON_MARKER + "</model>");
   				 stringBuilder.append("<transform>");
   				 stringBuilder.append("<rotation type=\"eulerrad\">");
   				 stringBuilder.append("<x>1.5708</x>");
   				 stringBuilder.append("<y>0</y>");
   				 stringBuilder.append("<z>0</z>");
   				 stringBuilder.append("</rotation>");
   				 stringBuilder.append("<scale>");
   				 stringBuilder.append("<x>5</x>");
   				 stringBuilder.append("<y>5</y>");
   				 stringBuilder.append("<z>5</z>");
   				 stringBuilder.append("</scale>");
   				 stringBuilder.append("</transform>");
//   				 stringBuilder.append("<properties>");
//   				 stringBuilder.append("<screencoordinates>");
//   				 stringBuilder.append("<x>1</x>");
//   				 stringBuilder.append("<y>0.02</y>");
//   				 stringBuilder.append("</screencoordinates>");
//   				 stringBuilder.append("</properties>");
   				 stringBuilder.append("</assets3d>");
   				 stringBuilder.append("</object>");

   			 } else  if (positionType.equals(Constants.POS_TYPE_JUNAIOMARKER)){
   				 stringBuilder.append("<results trackingurl=\"" + trackingfile + "\">");
   			     stringBuilder.append("<arel><![CDATA[" + Constants.SERVER_URL + "gui/glueps/arbrowsers/junaio/arel.html]]></arel>");

   			 } else {
   				 stringBuilder.append("<results>");		
   			     stringBuilder.append("<arel><![CDATA[" + Constants.SERVER_URL + "gui/glueps/arbrowsers/junaio/arel.html]]></arel>");

   			 }
   			 //Logout button
   			 
   			 if (showLogout) {
   				 
   				 stringBuilder.append("<object id=\"LogoutButton\">");
   				 stringBuilder.append("<title><![CDATA[Logout]]></title>");
   				 stringBuilder.append("<parameters>");
   				 stringBuilder.append("<parameter key=\"urlaccess\"><![CDATA[" + urlLogoutEncoded + "]]></parameter>");
   				 stringBuilder.append("</parameters>");
   				 stringBuilder.append("<assets3d>");
   				 stringBuilder.append("<model>" + logouticon + "</model>");
   				 stringBuilder.append("<transform>");
   				 stringBuilder.append("<rotation type=\"eulerrad\">");
   				 stringBuilder.append("<x>1.5708</x>");
   				 stringBuilder.append("<y>0</y>");
   				 stringBuilder.append("<z>0</z>");
   				 stringBuilder.append("</rotation>");
   				 stringBuilder.append("<scale>");
   				 stringBuilder.append("<x>5</x>");
   				 stringBuilder.append("<y>5</y>");
   				 stringBuilder.append("<z>5</z>");
   				 stringBuilder.append("</scale>");
   				 stringBuilder.append("</transform>");
//   				 stringBuilder.append("<properties>");
//   				 stringBuilder.append("<screencoordinates>");
//   				 stringBuilder.append("<x>0</x>");
//   				 stringBuilder.append("<y>0.02</y>");
//   				 stringBuilder.append("</screencoordinates>");
//   				 stringBuilder.append("</properties>");
   				 stringBuilder.append("</assets3d>");
   				 stringBuilder.append("</object>");
   				 
   			 }
   			// ### END OF SCREEN CONTROLS ###

   			 // ### LIST OF POIs ###
   			 
   			 //Selection of geopositioned or positioned via marker POIs
   			List<Poi> listPoi = PoiService.getNearestPoisForPositionType(deployId, username, positionType, latitude, longitude, max, false);

   	        for (Poi poi : listPoi)
   	        {
   	            //Avoid strange characters in texts	            <-- Al pasar de servlets a RESTLET ya no hace falta 8-O
//   	            String name = Utils.toISO(poi.getName());
//   	            String desc = Utils.toISO(poi.getDescription());
   	            
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
   	            boolean asserts3d = false;
   	            // ### Include coordinates in geoposition or COSID with markers ###
   	            if (positionType.equals(Constants.POS_TYPE_JUNAIOMARKER)) {

   	            	String cosid = poi.getCosid();
   	            	stringBuilder.append("<object id=\"" + poi.getPoiId() + "\">");
       
   	   	            stringBuilder.append("<title><![CDATA[" + name + "]]></title>");	 

   	            	stringBuilder.append("<assets3d>");
   	            	asserts3d = true; //To control if <assert3D> is already written
   	            	stringBuilder.append("<properties>");
   	            	stringBuilder.append("<coordinatesystemid>" + cosid + "</coordinatesystemid>");
   	            	
//   	            	stringBuilder.append("<occluding>false</occluding>");
//   	            	stringBuilder.append("<transparency>0</transparency>");
//   	            	stringBuilder.append("<pickingenabled>true</pickingenabled>");
//   	            	stringBuilder.append("<renderorder>true</renderorder>");
   	            	
   	            	stringBuilder.append("</properties>");
   	            } else {
   	            	stringBuilder.append("<object id=\"" + poi.getPoiId() + "\">");
   	            	
   	   	            stringBuilder.append("<title><![CDATA[" + name + "]]></title>");	 
   	   	            
   	                stringBuilder.append("<location>");
   	                stringBuilder.append("<lat>" + poi.getLat() + "</lat>");
   	                stringBuilder.append("<lon>" + poi.getLon() + "</lon>");
   	                stringBuilder.append("<alt>0</alt>");
   	                stringBuilder.append("</location>");
   	                
   	                stringBuilder.append("<viewparameters>");
   	                stringBuilder.append("<maxdistance>" + poi.getMaxdistance() + "</maxdistance>");
   	                stringBuilder.append("</viewparameters>");
   	            }
   	            


   	            // ### CASE 3dmodels ###
   	            if (poi.getPoitype()== "3dmodel") {
   	            	if (positionType.equals(Constants.POS_TYPE_GEOPOSITION)) {
   	   		            stringBuilder.append("<thumbnail>" + glueicon + "</thumbnail>");
   	   		            stringBuilder.append("<icon>" + glueicon + "</icon>");
   	            	}

   	            	if (!asserts3d) {
   		            	stringBuilder.append("<assets3d>");
   		            	asserts3d = true;
   	            	}
   		            stringBuilder.append("<model>" + poi.getLocation() + "</model>"); 
   		            stringBuilder.append("<transform>");
   		            stringBuilder.append("<translation>");
   		            stringBuilder.append("<x>0</x>");
   		            stringBuilder.append("<y>0</y>");
   		            stringBuilder.append("<z>0</z>");
   		            stringBuilder.append("</translation>");

   	            	if (poi.getScale() != null) {
   			            //"Scale" is scaled to start from the default value, and don't mislead the user
   		            	Double scaleDouble = Double.parseDouble(poi.getScale())*Double.parseDouble(Constants.JUNAIO_3DMODEL_DEFAULT_SCALE);
           		
   	            		stringBuilder.append("<scale>");
   	            		stringBuilder.append("<x>" + scaleDouble + "</x>");
   	            		stringBuilder.append("<y>" + scaleDouble + "</y>");
   	            		stringBuilder.append("<z>" + scaleDouble + "</z>");
   	            		stringBuilder.append("</scale>");	            		
   	            	} else {
   	            		
   	            		stringBuilder.append("<scale>");
   	            		stringBuilder.append("<x>" + Constants.JUNAIO_3DMODEL_DEFAULT_SCALE + "</x>");
   	            		stringBuilder.append("<y>" + Constants.JUNAIO_3DMODEL_DEFAULT_SCALE + "</y>");
   	            		stringBuilder.append("<z>" + Constants.JUNAIO_3DMODEL_DEFAULT_SCALE + "</z>");
   	            		stringBuilder.append("</scale>");	 
   	            	}
   	     //       	stringBuilder.append("<o>" + Constants.JUNAIO_3DMODEL_DEFAULT_ORIENTATION + "</o>");
   	            	stringBuilder.append("<rotation type=\"eulerrad\">");
   	            	stringBuilder.append("<x>1.5708</x>");
   	            	stringBuilder.append("<y>0</y>");
   	            	stringBuilder.append("<z>0</z>");
   	            	stringBuilder.append("</rotation>");
   		            stringBuilder.append("</transform>");
   		            stringBuilder.append("</assets3d>");
   	            	
   	            	//TODO orientation attribute is not currently used
//   	            	if (orientation != null) {
//   	            		stringBuilder.append("<o>" + orientation + "</o>");
//   	            	} else {
//   	            		stringBuilder.append("<o>" + Constants.JUNAIO_3DMODEL_DEFAULT_ORIENTATION + "</o>");
//   	            	}
   	            	
   	            	
   	            // ### CASE ARimages ###
   	            } else if (poi.getPoitype()== "ARimage"){
   	            	if (!asserts3d) {
   		            	stringBuilder.append("<assets3d>");
   		            	asserts3d = true;
   	            	}
   	            	//Positioning with markers
   		            stringBuilder.append("<transform>");
   	            	if (positionType.equals(Constants.POS_TYPE_JUNAIOMARKER)) {
   	            	//	stringBuilder.append("<o>" + Constants.JUNAIO_ARIMAGE_JUNMARKER_DEFAULT_ORIENTATION + "</o>");
   	            		
   		            	stringBuilder.append("<rotation type=\"eulerrad\">");
   		            	stringBuilder.append("<x>0</x>");
   		            	stringBuilder.append("<y>1.5708</y>");
   		            	stringBuilder.append("<z>1.5708</z>");
   		            	stringBuilder.append("</rotation>");
   	            		
   	            		if (poi.getScale() != null) {
   			            	Double translationDouble = Double.parseDouble(poi.getScale())*(-190);
   		            		
   				            stringBuilder.append("<translation>");
   				            stringBuilder.append("<x>0</x>");
   				            stringBuilder.append("<y>" + translationDouble.toString() + "</y>");
   				            stringBuilder.append("<z>0</z>");
   				            stringBuilder.append("</translation>");
   		            		stringBuilder.append("<scale>");
   		            		stringBuilder.append("<x>" + poi.getScale() + "</x>");
   		            		stringBuilder.append("<y>" + poi.getScale() + "</y>");
   		            		stringBuilder.append("<z>" + poi.getScale() + "</z>");
   		            		stringBuilder.append("</scale>");	 

   	            		} else {
   		            			
   		            			//TODO positioning POIs inside an image (e.g., a picture in a museum)
//   		            			//(1)
//   		            		   	if (orientation != null) {
//   				            		stringBuilder.append("<translation>" + tag.getOrientation() + "</translation>");
//   				            		stringBuilder.append("<s>0.05</s>");
//   				            	} else {
//   				    	            stringBuilder.append("<translation>0,-190,0</translation>");
//   				    		        stringBuilder.append("<s>" + Constants.JUNAIO_ARIMAGE_DEFAULT_SCALE + "</s>");
//   				            	}
//   		            			//END (1)
   		            		   	
   		            			//To remove if (1)
   					            stringBuilder.append("<translation>");
   					            stringBuilder.append("<x>0</x>");
   					            stringBuilder.append("<y>-190</y>");
   					            stringBuilder.append("<z>0</z>");
   					            stringBuilder.append("</translation>");
   			            		stringBuilder.append("<scale>");
   			            		stringBuilder.append("<x>" + Constants.JUNAIO_ARIMAGE_DEFAULT_SCALE + "</x>");
   			            		stringBuilder.append("<y>" + Constants.JUNAIO_ARIMAGE_DEFAULT_SCALE + "</y>");
   			            		stringBuilder.append("<z>" + Constants.JUNAIO_ARIMAGE_DEFAULT_SCALE + "</z>");
   			            		stringBuilder.append("</scale>");			            		
   			            		//END to remove if (1)
   			            	}
   		            		
   		            		//TODO orientation attribute is not currently used
//   			            	if (orientation != null) {
//   			            		stringBuilder.append("<o>" + orientation + "</o>");
//   			            	} else {
//   			            		stringBuilder.append("<o>0,1.5708,1.5708</o>");
//   			            		stringBuilder.append("<o>" + Constants.JUNAIO_ARIMAGE_JUNMARKER_DEFAULT_ORIENTATION + "</o>");
//   			            	}
   	            	//Geopositioning
   	            	} else {
//   	            		stringBuilder.append("<o>" + Constants.JUNAIO_ARIMAGE_GEO_DEFAULT_ORIENTATION + "</o>");
   	            		
   		            	stringBuilder.append("<rotation type=\"eulerrad\">");
   		            	stringBuilder.append("<x>0</x>");
   		            	stringBuilder.append("<y>0</y>");
   		            	stringBuilder.append("<z>1.5708</z>");
   		            	stringBuilder.append("</rotation>");
   			            stringBuilder.append("<translation>");
   			            stringBuilder.append("<x>0</x>");
   			            stringBuilder.append("<y>0</y>");
   			            stringBuilder.append("<z>0</z>");
   			            stringBuilder.append("</translation>");
   	            		
   	            		
   	            		//TODO orientation attribute is not currently used
//   		            	if (orientation != null) {
//   		            		stringBuilder.append("<o>" + orientation + "</o>");
//   		            	} else {
//   		            		stringBuilder.append("<o>" + Constants.JUNAIO_ARIMAGE_GEO_DEFAULT_ORIENTATION + "</o>");
//   		            	}
   		     
   	            	}	  
   		            stringBuilder.append("</transform>");
   			        stringBuilder.append("<model>" + Constants.SERVER_URL + "gui/glueps/arbrowsers/plane3.md2</model>"); 
   		            stringBuilder.append("<texture>" + poi.getLocation() + "</texture>"); 
   			        stringBuilder.append("</assets3d>");
//   		            stringBuilder.append("<thumbnail>" + glueicon + "</thumbnail>");
//   		            stringBuilder.append("<icon>" + glueicon + "</icon>");
   	            	
   	            	
   	            // ### CASE normal gluelets ###
   	            } else {		            
   		            
   		            //Positioning with markers
   		            if (positionType.equals(Constants.POS_TYPE_JUNAIOMARKER)) {
                			
   		            	if (!asserts3d) {
   			            	stringBuilder.append("<assets3d>");
   			            	asserts3d = true;
   		            	}
   		            	stringBuilder.append("<model>" + glueicon3D + "</model>"); 
   			            stringBuilder.append("<transform>");       	
   			            stringBuilder.append("<translation>");
   			            stringBuilder.append("<x>0</x>");
   			            stringBuilder.append("<y>0</y>");
   			            stringBuilder.append("<z>0</z>");
   			            stringBuilder.append("</translation>");
   	            		stringBuilder.append("<scale>");
   	            		stringBuilder.append("<x>25</x>");
   	            		stringBuilder.append("<y>25</y>");
   	            		stringBuilder.append("<z>25</z>");
   	            		stringBuilder.append("</scale>");	
   		            	stringBuilder.append("<rotation type=\"eulerrad\">");
   		            	stringBuilder.append("<x>1.5708</x>");
   		            	stringBuilder.append("<y>0</y>");
   		            	stringBuilder.append("<z>0</z>");
   		            	stringBuilder.append("</rotation>");			            
   			            stringBuilder.append("</transform>");
   			            stringBuilder.append("</assets3d>");

   	            	//Geoposition
   		            } else {

   		     //       	stringBuilder.append("<mainresource>" + poi.getLocation() + "</mainresource>"); 
   		            }
   		        
   		            stringBuilder.append("<thumbnail>" + glueicon + "</thumbnail>");
   		            stringBuilder.append("<icon>" + glueicon + "</icon>");
   		            
   	//	            stringBuilder.append("<homepage>" + poi.getLocation() + "</homepage>"); 
   		            String locEncoded = Utils.encodeURIComponent(poi.getLocation().toString());
   		            
   		            stringBuilder.append("<popup>");
   	   	            if (desc != null){
   	   	            	if (!desc.equals("null")){
   	   	   	            	stringBuilder.append("<description><![CDATA[" + desc + "]]></description>");
   	   	            	} else {
   	   	            		stringBuilder.append("<description><![CDATA[ ]]></description>");
   	   	            	}
   	   	            } else {
   	   	            	stringBuilder.append("<description><![CDATA[ ]]></description>");
   	   	            }
   		            stringBuilder.append("<buttons>");
   		            
   		            //Old version, failing in junaio due to a junaio bug
//   		        stringBuilder.append("<button id=\"openjun\" name=\"Abrir en Junaio\"><![CDATA[javascript:openurlinternal(\"" + locEncoded + "\")]]></button>");
//		            stringBuilder.append("<button id=\"openbrow\" name=\"Abrir en Navegador de Internet\"><![CDATA[javascript:openUrlExternal(\"" + locEncoded + "\")]]></button>");

   		            //Workaround
   		            stringBuilder.append("<button id=\"openjun\" name=\"Abrir en Junaio\"><![CDATA[javascript:arel.Media.openWebsite(decodeURIComponent(\"" + locEncoded + "\"));]]></button>");
   		            stringBuilder.append("<button id=\"openbrow\" name=\"Abrir en Navegador de Internet\"><![CDATA[javascript:arel.Media.openWebsite(decodeURIComponent(\"" + locEncoded + "\"), \"external\");]]></button>");


   		            
   		            stringBuilder.append("</buttons>");
   		    		stringBuilder.append("</popup>");

   	            }
   	            stringBuilder.append("</object>");
   	        }

   	        //Padding to create space at the bottom for screen controls (temporary)
    	    stringBuilder.append("<SceneOptions><SceneOption key=\"PaddingToAnnotations\">80</SceneOption></SceneOptions>");
   	        stringBuilder.append("</results>");
   	        
   		    String answer = stringBuilder.toString();
   		    response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.APPLICATION_XML);
   		    return response;
   	        
   	     //No user is logged in the device	 
   		 } else {			 
   			 if (uri.contains("/filter/")){  
   			 //If the channel is a filter: autologin and doGet is called again to load POIs. (Check this function if creating a new adaptor)
   				 if (AuthService.autologin(appType, uri, options, remoteIpAddress, author)){
   					 response = getPois(appType, uri, options, headers, remoteIpAddress);
   				 }			 	 
   			 } else	{
   				//Generic channel (not a filter): The channel requires login			 
   				 
   				 //GUI POI to access login page
   				 StringBuilder stringBuilder = new StringBuilder();
   				 stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
   				 stringBuilder.append("<results>");
   			     stringBuilder.append("<arel><![CDATA[" + Constants.SERVER_URL + "gui/glueps/arbrowsers/junaio/arel.html]]></arel>");

   			     
   				 
   				 stringBuilder.append("<object id=\"LoginButton\">");
   				 stringBuilder.append("<title><![CDATA[Logout]]></title>");
   				 stringBuilder.append("<parameters>");
  // 				 stringBuilder.append("<parameter key=\"urlAccess\">\"hola\"</parameter>");
   				 stringBuilder.append("<parameter key=\"urlaccess\"><![CDATA[" + Constants.SERVER_URL + "arbrowser/" + appType + Constants.URL_LOGIN + "?uid=" + uid + "]]></parameter>");
   				 stringBuilder.append("</parameters>");
   				 stringBuilder.append("<assets3d>");
   				 stringBuilder.append("<model>" + Constants.JUNAIO_ICON_LOGIN + "</model>");
   				 stringBuilder.append("<transform>");
   				 stringBuilder.append("<rotation type=\"eulerrad\">");
   				 stringBuilder.append("<x>1.5708</x>");
   				 stringBuilder.append("<y>0</y>");
   				 stringBuilder.append("<z>0</z>");
   				 stringBuilder.append("</rotation>");
   				 stringBuilder.append("<scale>");
   				 stringBuilder.append("<x>20</x>");
   				 stringBuilder.append("<y>20</y>");
   				 stringBuilder.append("<z>20</z>");
   				 stringBuilder.append("</scale>");
   				 stringBuilder.append("</transform>");
//   				 stringBuilder.append("<properties>");
//   				 stringBuilder.append("<screencoordinates>");
//   				 stringBuilder.append("<x>0.5</x>");
//   				 stringBuilder.append("<y>0.7</y>");
//   				 stringBuilder.append("</screencoordinates>");
//   				 stringBuilder.append("</properties>");
   				 stringBuilder.append("</assets3d>");
   				 stringBuilder.append("</object>");

   				 stringBuilder.append("</results>");
   				 
   				 String answer = stringBuilder.toString();
   				 response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.APPLICATION_XML);
   				 return response;
   			 }

   		 }
           return response;
        //If action != null: event handler
        } else {
           	//debug
        	System.out.println("GET received in Junaio Event Controller to URI " + uri);
        	String username = null;
        	String deployId = null;
        	//Saving active user
			 HashMap<String,LoggedUser> loggedUsers = AuthService.readLoggedUsers();
			 if (loggedUsers != null && loggedUsers.get(uid) != null && loggedUsers.get(uid) != null){
	 			 username = loggedUsers.get(uid).getUsername();
	  			 deployId = loggedUsers.get(uid).getDeployId();
			 }

  			 boolean isStaff = false;
    		if ((action != null) && action.equals("trace")){
    	        if (lparameter != null && deployId != null && username != null)
    	        {

    				ActiveUser activeuser= new ActiveUser(appType, latitude, longitude, uid,
    						username, deployId, Constants.POS_TYPE_GEOPOSITION);
    				AuthService.saveActiveUser(activeuser);
    				isStaff = activeuser.isStaff();
    				 System.out.println("user isStaff: " + isStaff);
    	        }

    		}
        	
    		 //Log
    		String logfile = Constants.JUNAIO_LOGFILE;
    		if (action != null){
    			LogService.writelog(appType, options, remoteIpAddress, uid, action, author, logfile);
    		}    		 
   	 
    		 
    		// ### CREATING THE XML RESPONSE ###
  			 StringBuilder stringBuilder = new StringBuilder();  
			 //Tracking file for images recognition. It is configurable via app.properties in filter channels
  			 String trackingfile = Constants.SERVER_URL + "gui/glueps/arbrowsers/markers/ID_Marker1-21/TrackingConfiguration_smoother.xml_enc";
  			 if (author != null) {
  				 trackingfile = Constants.configuration().getProperty("junaio.filter." + author + ".trackingfile", trackingfile);
  			 }			 
		     
  			 stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
  			 
  			 //Response is different if the log is a trace
     		 if ((action != null) && (action.equals("trace") && uri.contains("/junaioarel/")) && (Constants.ARBROWSERS_SHOWUSERS || isStaff)){
     			// ### RETURN THE LIST OF USERS AS POIS XML FILE ###
     			 stringBuilder.append("<results>");
     			 
     		       List<ActiveUser> activeUsers = AuthService.getActiveUsersFromDeployId(deployId);
     		        if (activeUsers != null){
     		            for (ActiveUser user : activeUsers)
     		            {
     		                if (!user.getUsername().equals(username)){
        		            	String icon = "http://files.softicons.com/download/internet-cons/emotions-2-icons-by-kirozeng/png/256/smile.png";
        		            	String title;
	       		   				if (user.getAppType().equals("earth")){
	    		   					 title = user.getUsername() + " (from Google Earth)";
	    		   				} else {
	    		   					title = user.getUsername();
	    		   				}
        		   				 stringBuilder.append("<object id=\"" + user.getUid() +  "\">");

        		   				 stringBuilder.append("<title><![CDATA[" + title + "]]></title>");
        		   				 stringBuilder.append("<thumbnail>" + icon + "</thumbnail>");
        		   				 stringBuilder.append("<icon>" + icon + "</icon>");
        		   			 	 stringBuilder.append("<popup>");
        		   			 	 stringBuilder.append("<description><![CDATA[" + title + "]]></description>");
        		   			 	 stringBuilder.append("</popup>");
        	   	                 stringBuilder.append("<location>");
        	   	                 stringBuilder.append("<lat>" + user.getLat() + "</lat>");
        	   	                 stringBuilder.append("<lon>" + user.getLon() + "</lon>");
        	   	                 stringBuilder.append("<alt>0</alt>");
        	   	                 stringBuilder.append("</location>");     	   	                
        	   	                 stringBuilder.append("<viewparameters>");
        	   	                 stringBuilder.append("<maxdistance>50000</maxdistance>");
        	   	                 stringBuilder.append("</viewparameters>");
        		   				 stringBuilder.append("<parameters>");
        		   				 stringBuilder.append("<parameter key=\"poitype\"><![CDATA[user]]></parameter>");
        		   				 stringBuilder.append("</parameters>");
//        		   				 stringBuilder.append("<assets3d>");
//        		   				 stringBuilder.append("<model>" + Constants.JUNAIO_ICON_GEO + "</model>");
//        		   				 stringBuilder.append("<transform>");
//        		   				 stringBuilder.append("<rotation type=\"eulerrad\">");
//        		   				 stringBuilder.append("<x>0</x>");
//        		   				 stringBuilder.append("<y>0</y>");
//        		   				 stringBuilder.append("<z>0</z>");
//        		   				 stringBuilder.append("</rotation>");
//        		   				 stringBuilder.append("<scale>");
//        		   				 stringBuilder.append("<x>2.8</x>");
//        		   				 stringBuilder.append("<y>2.8</y>");
//        		   				 stringBuilder.append("<z>2.8</z>");
//        		   				 stringBuilder.append("</scale>");
//        		   				 stringBuilder.append("</transform>");
////        		   				 stringBuilder.append("<properties>");
////        		   				 stringBuilder.append("<screencoordinates>");
////        		   				 stringBuilder.append("<x>1</x>");
////        		   				 stringBuilder.append("<y>0.02</y>");
////        		   				 stringBuilder.append("</screencoordinates>");
////        		   				 stringBuilder.append("</properties>");
//        		   				 stringBuilder.append("</assets3d>");
        		   				 stringBuilder.append("</object>");
        		   				 
//        		   				 if (user.getAppType().equals("earth")){
//        		   					System.out.println("Añado POI earth -avatar- para usuario " + user.getUsername());
//
//           		   				 stringBuilder.append("<object id=\"" + user.getUid() +  "avatar\">");
//           		   				 stringBuilder.append("<title><![CDATA[" + user.getUsername() + "]]></title>");
//           		   				 stringBuilder.append("<thumbnail>" + icon + "</thumbnail>");
//           		   				 stringBuilder.append("<icon>" + icon + "</icon>");
////           		   			 	 stringBuilder.append("<popup>");
////           		   			 	 stringBuilder.append("<description><![CDATA[" + user.getUsername() + "]]></description>");
////           		   			 	 stringBuilder.append("</popup>");
//           	   	                 stringBuilder.append("<location>");
//           	   	                 stringBuilder.append("<lat>" + user.getLat() + "</lat>");
//           	   	                 stringBuilder.append("<lon>" + user.getLon() + "</lon>");
//           	   	                 stringBuilder.append("<alt>0</alt>");
//           	   	                 stringBuilder.append("</location>");     	   	                
//           	   	                 stringBuilder.append("<viewparameters>");
//           	   	                 stringBuilder.append("<maxdistance>50000</maxdistance>");
//           	   	                 stringBuilder.append("</viewparameters>");
//           		   				 stringBuilder.append("<parameters>");
//           		   				 stringBuilder.append("<parameter key=\"poitype\"><![CDATA[user]]></parameter>");
//           		   				 stringBuilder.append("</parameters>");
//           		   				 stringBuilder.append("<assets3d>");
//           		   				 stringBuilder.append("<model>" + "http://www.gsic.uva.es/juanmunoz/GLUEPSManager/gui/glueps/arbrowsers/personaPeloVerde.zip" + "</model>");
//           		   				 stringBuilder.append("<transform>");
//           		   				 stringBuilder.append("<rotation type=\"eulerrad\">");
//           		   				 stringBuilder.append("<x>-45</x>");
//           		   				 stringBuilder.append("<y>-45</y>");
//           		   				 stringBuilder.append("<z>-45</z>");
//           		   				 stringBuilder.append("</rotation>");
//           		   				 stringBuilder.append("<scale>");
//           		   				 stringBuilder.append("<x>10</x>");
//           		   				 stringBuilder.append("<y>10</y>");
//           		   				 stringBuilder.append("<z>10</z>");
//           		   				 stringBuilder.append("</scale>");
//           		   				 stringBuilder.append("</transform>");
////           		   				 stringBuilder.append("<properties>");
////           		   				 stringBuilder.append("<screencoordinates>");
////           		   				 stringBuilder.append("<x>1</x>");
////           		   				 stringBuilder.append("<y>0.02</y>");
////           		   				 stringBuilder.append("</screencoordinates>");
////           		   				 stringBuilder.append("</properties>");
//           		   				 stringBuilder.append("</assets3d>");
//           		   				 stringBuilder.append("</object>");
//        		   				 }
     		                }
      		            }
     		        }
	   				stringBuilder.append("</results>");
      			
     		 } else {
     			 // ### RETURN A EMPTY XML FILE ###


      			 
      			if (uri.contains("/junaioarel/")){      				 
      				 stringBuilder.append("<results>");	
       				 stringBuilder.append("<object id=\"dummy\">");
       				 stringBuilder.append("</object>");
      				 stringBuilder.append("</results>");

      			 } else if (uri.contains("/junaioarelglue/")){
      				 stringBuilder.append("<results trackingurl=\"" + trackingfile + "\">");
       				 stringBuilder.append("<object id=\"dummy\">");
       			 	 stringBuilder.append("<assets3d>");
       				 stringBuilder.append("<model>" + Constants.JUNAIO_ICON_GEO + "</model>");
   	            	 stringBuilder.append("<properties>");
   	                 stringBuilder.append("<coordinatesystemid>21</coordinatesystemid>");  
   	            	 stringBuilder.append("<transparency>1</transparency>"); 
   	            	 stringBuilder.append("</properties>");
   	            	 stringBuilder.append("<transform>");
   	            	 stringBuilder.append("<scale>");
   	            	 stringBuilder.append("<x>0.000000001</x>");
   	            	 stringBuilder.append("<y>0.000000001</y>");
   	            	 stringBuilder.append("<z>0.000000001</z>");
   	            	 stringBuilder.append("</scale>");	            		
   	            	 stringBuilder.append("</transform>");
   	            	 stringBuilder.append("</assets3d>");
       				 stringBuilder.append("</object>");
       				 stringBuilder.append("</results>");

      			 }
      			 
      			 
     		 }
    		 


			 String answer = stringBuilder.toString();
   			 response = new ARbrowserResponse(answer, Status.SUCCESS_OK, MediaType.APPLICATION_XML);			
   		     return response;


        }
        
  
    }
    
//    // Event handler
//    public static void createLog(String appType, String uri, Form options, Series<Parameter> headers, String remoteIpAddress)
//    {
//    	//debug
//    	System.out.println("POST received in Junaio Event Controller to URI " + uri);
//    	
//        String uid;
//
//		uid = Utils.getDecodeAndEncode(options, "uid", null);
//		
//    	//Search for author if channel is a filter		  
//		String author = (String) Utils.getAuthor(uri).get("author");
//
//		 //Log
//		 String logfile = Constants.JUNAIO_LOGFILE;
//		 String action = "interaction";
//		 LogService.writelog(appType, options, remoteIpAddress, uid, action, author, logfile);
//	 
//    }
    
	

}
