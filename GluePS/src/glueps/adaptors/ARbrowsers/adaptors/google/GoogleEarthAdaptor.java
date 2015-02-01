package glueps.adaptors.ARbrowsers.adaptors.google;


import glueps.adaptors.ARbrowsers.adaptors.Constants;
import glueps.adaptors.ARbrowsers.adaptors.Utils;
import glueps.adaptors.ARbrowsers.model.ARbrowserResponse;
import glueps.adaptors.ARbrowsers.model.ActiveUser;
import glueps.adaptors.ARbrowsers.model.Poi;
import glueps.adaptors.ARbrowsers.service.LogService;
import glueps.adaptors.ARbrowsers.service.PoiService;
import glueps.adaptors.ARbrowsers.service.SortPoi;
import glueps.adaptors.ARbrowsers.service.AuthService;
import glueps.core.model.Deploy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.util.Series;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class GoogleEarthAdaptor
{
	
	public static ARbrowserResponse login(String appType, String uri, Form options, Series<Parameter> headers, String remoteIpAddress)
	{
		String username = options.getFirstValue("username");
		ArrayList<String> positionTypes = new ArrayList<String>();
		//The position type of the toolInstances of the user must be geoposition
		positionTypes.add("geoposition");
		//String password = options.getFirstValue( "pwd" );
		List<Deploy> deploys = AuthService.getDeployListByUsernamePositionType(username, positionTypes);
	    String action = "login";
	    String logfile = Constants.ACCESS_LOGFILE;
	    LogService.writelog(appType, options, remoteIpAddress, username, action, username, logfile);
//		List<Deploy> deploys =  new ArrayList<Deploy>();
		JsonObject answer = new JsonObject();
		for (Deploy deploy : deploys){
			answer.addProperty(deploy.getId(), deploy.getName());
		}
		return new ARbrowserResponse(answer.toString(), Status.SUCCESS_OK, MediaType.APPLICATION_JSON);
	}

	public static ARbrowserResponse getPois(String appType, String uri, Form options, Series<Parameter> headers, String remoteIpAddress)
    {
        double latitude = Utils.getDouble(options, "latitude", 41.6623469);
        double longitude = Utils.getDouble(options, "longitude", -4.7061101);
        double radius = Utils.getDouble(options, "radius", 20);
        int max = Utils.getInt(options, "max", 50);
        

	//	System.out.println("El getrequri contiene esto: " + getrequri);

        //Format of callback URL: .../mixare/<deployId>/<username>
		String[] geturlparts;				 
		String delimiter = "/";
		String deployId = null;
		String username = null;
		try {
			String path = (new URL(uri)).getPath();
			geturlparts = path.split(delimiter);
			int urlsize = geturlparts.length;
			
			deployId = geturlparts[urlsize-2];
			username = URLDecoder.decode(geturlparts[urlsize-1], "UTF-8");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String positionType = Constants.POS_TYPE_GEOPOSITION;  //mixare only supports geoposition



        //mixare format: http://code.google.com/p/mixare/wiki/DisplayYourOwnData

        JsonObject data = new JsonObject();
        JsonArray results = new JsonArray();
        int count = 0;
        //List<Poi> listPoi = PoiService.getNearestPoisForPositionType(deployId, username, positionType, latitude, longitude, max, true);
        List<Poi> listPoi = PoiService.getPoisForPositionType(deployId, username, positionType);

        for (Poi poi : listPoi)
        {
            //Codificación de caracteres para que aparezcan tildes y demás caracteres especiales
        //	String name = Utils.toISO(poi.getName());  //En las pruebas con restlet no hace falta. Con servlets sí que hacía falta 8-O
            
            double distance = SortPoi.distFrom(poi.getLat(), poi.getLon(), latitude, longitude);
            
            
            count++;
            JsonObject poiJson = new JsonObject();
            poiJson.addProperty("id", "" + poi.getPoiId());
            poiJson.addProperty("lat", "" + poi.getLat());
            poiJson.addProperty("lng", "" + poi.getLon());
            poiJson.addProperty("elevation", "0");
            poiJson.addProperty("title", poi.getName());
            poiJson.addProperty("poitype", poi.getPoitype());
            poiJson.addProperty("distance", "" + distance);
            poiJson.addProperty("has_detail_page", "1");
            poiJson.addProperty("description", poi.getDescription());
            poiJson.addProperty("maxdistance", poi.getMaxdistance());
            String webpageUrl = poi.getLocation().toString();
            //try {
				//poiJson.addProperty("webpage", URLEncoder.encode( webpageUrl , "UTF-8"));
            	poiJson.addProperty("webpage", webpageUrl);
			//} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}

            results.add(poiJson);
        }
        
        //Adding active users as POIs
        
        List<ActiveUser> activeUsers = AuthService.getActiveUsersFromDeployId(deployId);
        if (activeUsers != null){
            for (ActiveUser user : activeUsers)
            {
                double distance = SortPoi.distFrom(user.getLat(), user.getLon(), latitude, longitude);
                
                JsonObject poiJson = new JsonObject();
                poiJson.addProperty("id", "" + user.getUid());
                poiJson.addProperty("lat", "" + user.getLat());
                poiJson.addProperty("lng", "" + user.getLon());
                poiJson.addProperty("elevation", "0");
                poiJson.addProperty("title", user.getUsername());
                poiJson.addProperty("poitype", "user");
                poiJson.addProperty("distance", "" + distance);
                poiJson.addProperty("has_detail_page", "1");
                poiJson.addProperty("description", "");
                poiJson.addProperty("maxdistance", "50000");
                poiJson.addProperty("orientation", user.getOrientation());
                String webpageUrl = "";
                //try {
    				//poiJson.addProperty("webpage", URLEncoder.encode( webpageUrl , "UTF-8"));
                	poiJson.addProperty("webpage", webpageUrl);
    			//} catch (UnsupportedEncodingException e) {
    				// TODO Auto-generated catch block
    			//	e.printStackTrace();
    			//}

                if (!user.getUsername().equals(username)){
                	results.add(poiJson);
                }
                
            }
        }
       
        	
        //Ending JSON
        data.addProperty("status", "OK");
        data.addProperty("num_results", count);
        data.add("results", results);

        ARbrowserResponse response = new ARbrowserResponse(data.toString(), Status.SUCCESS_OK, MediaType.APPLICATION_JSON);	 
        //debug
   	//	System.out.println("JSON response: " + data.toString());
        
   		return response;
        
    }
	
    public static void createLog(String appType, String uri, Form options, Series<Parameter> headers, String remoteIpAddress)
    {
    	//debug
    	System.out.println("POST received in Google Event Controller to URI " + uri);

		String username;
		try {
			username = URLDecoder.decode(Utils.getDecodeAndEncode(options, "username", null), "UTF-8");
			String message = Utils.getDecodeAndEncode(options, "message", null);
			String deployid = Utils.getDecodeAndEncode(options, "deployid", null);
			String uid = Utils.getDecodeAndEncode(options, "uid", null);
			String lparameter = options.getFirstValue("l");
	    	//Search for author if channel is a filter		  
			String author = (String) Utils.getAuthor(uri).get("author");
			Double orientation = Double.parseDouble(options.getFirstValue("orientation"));
			
			if (message.equals("trace")){
				
		        if (lparameter != null)
		        {
		            StringTokenizer st = new StringTokenizer(lparameter, ",");
		            Double latitude = Double.parseDouble(st.nextToken());
		            Double longitude = Double.parseDouble(st.nextToken());
					ActiveUser activeuser= new ActiveUser(appType, latitude, longitude, orientation, uid,
							username, deployid, Constants.POS_TYPE_GEOPOSITION);
					AuthService.saveActiveUser(activeuser);
		        }

			}

			

			 //Log
			 String logfile = Constants.GOOGLE_LOGFILE;
			 String action = message;
			 LogService.writelog(appType, options, remoteIpAddress, username, action, author, logfile);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
    }
}
