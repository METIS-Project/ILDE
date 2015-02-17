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

package glueps.adaptors.ARbrowsers.adaptors.mixare;


import glueps.adaptors.ARbrowsers.adaptors.Constants;
import glueps.adaptors.ARbrowsers.adaptors.Utils;
import glueps.adaptors.ARbrowsers.model.ARbrowserResponse;
import glueps.adaptors.ARbrowsers.model.ActiveUser;
import glueps.adaptors.ARbrowsers.model.Poi;
import glueps.adaptors.ARbrowsers.service.AuthService;
import glueps.adaptors.ARbrowsers.service.LogService;
import glueps.adaptors.ARbrowsers.service.PoiService;
import glueps.adaptors.ARbrowsers.service.SortPoi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;


import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.util.Series;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class MixareAdaptor
{

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
			username = geturlparts[urlsize-1];
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String positionType = Constants.POS_TYPE_GEOPOSITION;  //mixare only supports geoposition



        //mixare format: http://code.google.com/p/mixare/wiki/DisplayYourOwnData

        JsonObject data = new JsonObject();
        JsonArray results = new JsonArray();
        int count = 0;
        List<Poi> listPoi = PoiService.getNearestPoisForPositionType(deployId, username, positionType, latitude, longitude, max, true);

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
            poiJson.addProperty("distance", "" + distance);
            poiJson.addProperty("has_detail_page", "1");
            String webpageUrl = poi.getLocation().toString();
            try {
				poiJson.addProperty("webpage", URLEncoder.encode( webpageUrl , "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            results.add(poiJson);
        }
        
        
        //Adding active users as POIs
		ActiveUser activeuser= new ActiveUser(appType, latitude, longitude, "unknown",
				username, deployId, Constants.POS_TYPE_GEOPOSITION);
		AuthService.saveActiveUser(activeuser);
		boolean isStaff = activeuser.isStaff();
		//debug
		System.out.println("user isStaff: " + isStaff);
        
        List<ActiveUser> activeUsers = AuthService.getActiveUsersFromDeployId(deployId);
        if ((activeUsers != null) && (Constants.ARBROWSERS_SHOWUSERS || isStaff)){
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
                poiJson.addProperty("has_detail_page", "0");

                if (!user.getUsername().equals(username)){
                	results.add(poiJson);
                }
                
            }
        }
 
         
        //ENDing Json
        data.addProperty("status", "OK");
        data.addProperty("num_results", count);
        data.add("results", results);

        ARbrowserResponse response = new ARbrowserResponse(data.toString(), Status.SUCCESS_OK, MediaType.APPLICATION_JSON);	 
        //debug
   		System.out.println("JSON response: " + data.toString());
   		
   		//Writting log
   		//TODO

        
   		return response;
        
    }
}
