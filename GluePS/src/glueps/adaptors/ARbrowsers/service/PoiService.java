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

package glueps.adaptors.ARbrowsers.service;

import glueps.adaptors.ARbrowsers.adaptors.Constants;
import glueps.adaptors.ARbrowsers.model.Poi;
import glueps.core.model.Deploy;
import glueps.core.model.InstancedActivity;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.ToolInstance;
import glueps.core.persistence.JpaManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;


import bucket.model.Artifact;
import bucket.model.ArtifactType;
import bucket.model.Bucket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;




/**
*
*  @author of the original source: Pierre Levy [ARTags project owners (see http://www.artags.org)]
*  This is a severe modification of the ARTags Original code, by Juan A. Muñoz, GSIC-EMIC research group (see www.gsic.uva.es)
*/

public class PoiService
{
	//Bucket-server caches
	private static HashMap<String, ArrayList<ArtifactType>> bucketserverArtifactTypeList = new HashMap<String, ArrayList<ArtifactType>>();
	private static HashMap<String, Pair<Long, Bucket>> bucketCache = new HashMap<String, Pair<Long, Bucket>>();
	private static HashMap<String, Pair<Long, URL>> finalUrlCache = new HashMap<String, Pair<Long, URL>>();
	private static Long bucketserverArtifactTypeListTimestamp;
	private static Long bucketCacheTimestamp;
	private static Long finalUrlTimestamp;
	

    public static List<Poi> getNearestPois(String deployId, String username, double latitude, double longitude, int max, boolean useMaxdistance)
    {
        List<Poi> list = filter(getAllPois(deployId, username), latitude, longitude);

        if (list.size() > max)
        {
            list = getNearestPois(list, latitude, longitude, max, useMaxdistance);
        }

        return list;
    }
 
//    El usemaxdistance es para devolver sólo los POIs más cercanos del atributo "maxdistance" de cada POI. En los browsers
//    donde la respuesta hacia el cliente incluye el maxdistance y el cliente puede procesarlo (como junaio), es mejor ponerlo a "false)
//    En layar, por ejemplo, se usa, pero se depende del refresco de canal para que se muestren los POIs según el usuario se acerca a ellos
    
    public static List<Poi> getNearestPoisForPositionType(String deployId, String username, String positionType, double latitude, double longitude, int max, boolean useMaxdistance)
    {
    	List<Poi> list = null;
    	if (positionType.equals(Constants.POS_TYPE_GEOPOSITION)) {
        	list = filter(getPoisForPositionType(deployId, username, positionType), latitude, longitude);

            if (list.size() > max)
            {
                list = getNearestPois(list, latitude, longitude, max, useMaxdistance);
            } else {
            	list = getNearestPois(list, latitude, longitude, 500, useMaxdistance);
            }            	
        } else if (positionType.equals(Constants.POS_TYPE_JUNAIOMARKER)){
        	list = getPoisForPositionType(deployId, username, positionType);
        }
    	


        return list;
    }
    
    public static List<Poi> getNearestPoisAllPositionType(String deployId, String username, double latitude, double longitude, int max, boolean useMaxdistance)
    {

    		List<Poi> list = filter(getPoisForPositionType(deployId, username, Constants.POS_TYPE_GEOPOSITION), latitude, longitude);

            if (list.size() > max)
            {
                list = getNearestPois(list, latitude, longitude, max, useMaxdistance);
            }
            
            
            List<Poi> listMarkers = getPoisForPositionType(deployId, username, Constants.POS_TYPE_JUNAIOMARKER);
        
            for (Poi poi : listMarkers)
            {
            	list.add(poi);
            }
    	


        return list;
    }

    
    private static List<Poi> filter(List<Poi> list, double latitude, double longitude)
    {
        List<Poi> listFiltered = new ArrayList<Poi>();
        long lat = get10e6(latitude);
        long lon = get10e6(longitude);
        long latmin = lat - 1000000;
        long latmax = lat + 1000000;
        long lonmin = lon - 1000000;
        long lonmax = lon + 1000000;
        for (Poi t : list)
        {
        	long poiLat = get10e6(t.getLat());
        	long poiLon = get10e6(t.getLon());
            if ((poiLat < latmin) || (poiLat > latmax)
                    || (poiLon < lonmin) || (poiLon > lonmax))
            {
                continue;
            }
            listFiltered.add(t);
        }
        return listFiltered;
    }

    static List<Poi> getNearestPois(List<Poi> list, double latitude, double longitude, int max, boolean useMaxdistance)
    {
        return getNearestPois(list, latitude, longitude, max, 10000L, useMaxdistance);
    }

    static List<Poi> getNearestPois(List<Poi> list, double latitude, double longitude, int max, long radius, boolean useMaxdistance)
    {
        long r = radius * radius / 10000;
        List<SortPoi> listSort = new ArrayList<SortPoi>();
        for (Poi poi : list)
        {
            SortPoi t = new SortPoi(poi, latitude, longitude);
            
            //Se tiene en cuenta el useMaxdistance para devolver todos los POIs o sólo los que están más cerca del correspondiente maxdistance
            if (useMaxdistance){
            	//debug
            	System.out.println("t.dist= " + t.dist + " ; r=" + r + " ; maxdistance= " + poi.getMaxdistance());
                if ((t.dist < r) && (t.dist < poi.getMaxdistance()))
                {
                    listSort.add(t);
                }
            } else {
                if (t.dist < r)
                {
                    listSort.add(t);
                }
            }

        }

        if (listSort.size() > max)
        {
            Collections.sort(listSort);
        }

        List<Poi> listNearest = new ArrayList<Poi>();

        int count = (listSort.size() > max) ? max : listSort.size();
        for (int i = 0; i < count; i++)
        {
            listNearest.add(listSort.get(i).poi);
        }
        return listNearest;

    }


    static class DateComparator implements Comparator
    {

        public int compare(Object t1, Object t2)
        {
            long date1 = ((Poi) t1).getDate();
            long date2 = ((Poi) t2).getDate();

            return (date2 < date1) ? -1 : 1;
        }
    }


    public static List<Poi> getAllPois(String deployId, String username)
    {    	
		List<Poi> allTags = new ArrayList<Poi>();
	    allTags = findPoisByDeployIdAndUsername(deployId, username);
        return allTags;
    	
    }
    
    public static List<Poi> getPoisForPositionType(String deployId, String username, String positionType)
    {   	
		List<Poi> allPois = new ArrayList<Poi>();
	    allPois = findPoisByDeployIdAndUsername(deployId, username);
	    
        List<Poi> listTag = new ArrayList<Poi>();

        if (allPois != null){
            for (Poi poi : allPois)
            {
            	if (poi.getPositionType().equals(positionType)) {
            		listTag.add(poi);
            	}
            }
        }

   
        return listTag;
    }
    

    
    /**
     * Method to get the list of POIs from glueps data base, belonging to a deployid and a username
     * @param deployId
     * @param username
     * @return List<Poi>
     */
    private static List<Poi> findPoisByDeployIdAndUsername(String deployId, String username)
    {
		List<Poi> poiList = new ArrayList<Poi>();
        try {
        	
        	JpaManager dbmanager = JpaManager.getInstance();
    		Deploy deploy = null;
       		deploy = dbmanager.findDeployObjectById(deployId);
        		ArrayList<InstancedActivity> instActList = new ArrayList<InstancedActivity>();
       		instActList = null;
       		if (deploy != null){
        		instActList = deploy.getInstancedActivitiesForUsername(username);
       		}
    		
       		
       		
    		if(instActList !=null){
    		  	for(Iterator<InstancedActivity> it = instActList.iterator();it.hasNext();){
    				InstancedActivity inst = it.next();
					
    				//Checking if corresponding activity has the toDeploy parameter to true
    				//If it is not, POI will not be shown
					String activityId = inst.getActivityId();
					Boolean todeploy = deploy.getDesign().findActivityById(activityId).isToDeploy();
					if (todeploy) {

						
						//Obtaining tool instances from instanced activity (resources currently don't have position field)
						ArrayList<String> toolInstanceIds = inst.getInstancedToolIds();
						if (toolInstanceIds !=null){
							for (Iterator<String> it2 = toolInstanceIds.iterator();it2.hasNext();){
								String toolInstId =it2.next();
								ToolInstance toolInst = deploy.getToolInstanceById(toolInstId);
								
								//Checking the position type parsing parameters of tools instance
								if ((toolInst.getPosition()!= null && toolInst.getPositionType() != null) || (isBucket(deploy, toolInst, false, null))) {
									
									HashMap<String, Object> positionAttributes = processPositionType(toolInst);
									String postype = (String) positionAttributes.get("postype");
								
									
									//End of checking the position type
									
									//Now, we generate the POI location attribute, processing the tool instance location depending on the tool type
									//In case of some tool type, we get also the poi orientation attribute
			//						if (postype != null && toolInst.getLocationWithRedirects(deploy) != null && !isBucket(deploy, toolInst)) {
									if (postype != null && toolInst.getLocationWithRedirects(deploy) != null) {
										  
										//Default location, for generic tool types
										  URI uriToolInstance = new URI(toolInst.getLocationWithRedirects(deploy).toString() + "?callerUser=" + URLEncoder.encode(username, "UTF-8"));
										  URL urlToolInstance = uriToolInstance.toURL();
										  String toolInsResID = toolInst.getResourceId();
										  String resKind = deploy.getDesign().findResourceById(toolInsResID).getToolKind();
										  String resType = deploy.getDesign().findResourceById(toolInsResID).getToolTypeNumber();
										  
										  //Creation of POI										  
										  poiList = addPoi(deploy, inst, activityId, toolInst, positionAttributes, poiList, username, resKind, resType, urlToolInstance);
										  
										  //End of Creation of the POI object and processing of repeated POIs
									}
					//				} else {
										  //If the tool instance is a bucket, positioned artifacts are extracted and added to POI list										  
										  if (isBucket(deploy, toolInst, false, null) && toolInst.getLocationWithRedirects(deploy) != null) {
											  addPoisFromBucket(deploy, inst, activityId, poiList, toolInst, username);

											  
										  }
							//		}
								}
							}

						}
					}

    			}

    		} else {
    			poiList = null;
    		}
    	
    	    } catch (Exception e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
		return poiList;

    }
    
    
    
    private static boolean  isNum(String s) {
    	try {
    	Double.parseDouble(s);
    	}
    	catch (NumberFormatException nfe) {
    	return false;
    	}
    	return true;
    	}
    
    private static boolean  isInteger(String s) {
    	try {
    	Integer.parseInt(s);
    	}
    	catch (NumberFormatException nfe) {
    	return false;
    	}
    	return true;
    	}
    
    private static String getOrientation (ToolInstance toolInst){
    	String orientation = null;
    	if (toolInst.getOrientation()!= null) {
			 /* String to split. */
			  String orientationfield = toolInst.getOrientation();
			  String[] temp;
			  /* delimiter */
			  String delimiter = ",";
			  /* given string will be split by the argument delimiter provided. */
			  temp = orientationfield.split(delimiter);
			  /* print substrings */
			  if (temp.length == 3){
				  boolean ok = true;
				  for (String coord: temp) {	
					  if (isInteger(coord)){
						  if (Integer.parseInt(coord) > 360 || Integer.parseInt(coord) < -360){
							  ok = false;
						  }
					  } else { 
						  ok = false;
						  }
				  }
				  if (ok) {
					  orientation = orientationfield;
				  }
	
			  }
    	}
    	return orientation;
    }
    
    
    
    private static long get10e6( double coord )
    {
    	double X10E6 = 1000000.0;
        return (long) ( coord * X10E6 );
    }
    
    
    private static String getScale(ToolInstance toolInst, String scale){
    	
        if (toolInst.getScale()!= null) {
	        if (isNum(toolInst.getScale())){
	        	if (Double.parseDouble(toolInst.getScale())>0 && Double.parseDouble(toolInst.getScale())<5000) {
	        		scale = toolInst.getScale();
	        	} else {												        		
	        	}												        	
	        } else {												        	
	        }
        }else {											        	
        }
        return scale;
    	
    }
    
    private static URL getFinalUrl (URL urlToolInstance){
	        
        //Final URL is taken from the cache if necessary
		//Using a cache to avoid massive queries to GLUE to resolve the instance and obtain the final url
        
		  URL finalUrl = null;
    	  if (finalUrlCache == null){
    		  finalUrlCache = new HashMap<String,Pair<Long, URL>>();
		  }		  

		  if (finalUrlCache.containsKey(urlToolInstance.toString())){
			  finalUrl = finalUrlCache.get(urlToolInstance.toString()).getValue();
			  finalUrlTimestamp = finalUrlCache.get(urlToolInstance.toString()).getKey();

			  //checking bucket timestamp of the cache and obtaining the remote url if necessary
	          long bucketExpirationTime = 10000; //final urls are obtained from cache during 10s
	          long cacheExpirationTime = 3600000; //cache is cleared after 1h without being used
	          long currentTime = new Date().getTime();
	          if (finalUrlTimestamp != null){
	        	  if (currentTime >(finalUrlTimestamp + bucketExpirationTime)){
	        		  	System.out.println("Final URL of toolinstance " + urlToolInstance.toString() + " expired in cache. Obtaining the remote URL");
	        		  	finalUrl = getFinalUrlFromRemote(urlToolInstance);	
	    			    if (finalUrl != null){
	    			    	finalUrlCache.put(urlToolInstance.toString(), Pair.of(new Date().getTime(), finalUrl));
	            		  	System.out.println("Final URL of toolinstance " + urlToolInstance.toString() + " renewed in cache");
	    			    }	
	        	  }
	        	  if (currentTime >(finalUrlTimestamp + cacheExpirationTime)){
	        		  finalUrlCache.clear();
	        		  System.out.println("Final URL cache cleared");
	        	  }
	          }

		  } else {
			  finalUrl = getFinalUrlFromRemote(urlToolInstance);	
			  if (finalUrl != null){
				  finalUrlCache.put(urlToolInstance.toString(), Pair.of(new Date().getTime(), finalUrl));
				  System.out.println("Final URL of toolinstance " + urlToolInstance.toString() + " included in cache");
			  }		  
		  }
		  finalUrlTimestamp = new Date().getTime();
		  return finalUrl;
    }
    
	private static URL getFinalUrlFromRemote(URL urlToolInstance){
        BufferedReader in;
		
		try {   
				System.out.println("Obtaining remote URL from toolinstance " + urlToolInstance.toString());
		        String urlToolInstanceString = urlToolInstance.toString();
				String uriGlueExternal= Constants.configuration().getProperty("gluelet.uri", "http://157.88.130.207/GLUEletManager/");
				String uriGlueInternal= Constants.configuration().getProperty("gluelet.uri.internal", "http://localhost:8185/GLUEletManager/");
				urlToolInstance = new URL(urlToolInstanceString.replace(uriGlueExternal, uriGlueInternal) + "&autoRequest=true");
		        
				in = new BufferedReader(
				new InputStreamReader(urlToolInstance.openStream(), "UTF-8"));
		        String inputLine;
		        Pattern p = Pattern.compile("<body onload=\"window.location.replace\\(\'(.*?)\'\\)\">");
		        while ((inputLine = in.readLine()) != null)  {
		   //         System.out.println(inputLine);
			    	Matcher m = p.matcher(inputLine);
			    	while (m.find() == true) {
			 //       System.out.println(m.group(1));
			    		
			    		String urlObtenida = m.group(1);
			    		
			    		
			    		//Personalization for dropbox download URL
			    		urlToolInstance = new URL(urlObtenida.replace("https://www.dropbox.com", "http://dl.dropboxusercontent.com"));
			    		
			    		//debug
					//	System.out.println("URL modificada. Nueva URL: " + urlToolInstance.toString());
			    	}
		        }
	  	}  catch (Exception  e1) {
	  		System.out.println("Error trying to access to URL");
			e1.printStackTrace();
			//TODO se podría controlar que esto ha ocurrido, y no mostrar el POI
		}
	  	return urlToolInstance;
		
		
	}
	  	
    private static HashMap<String, Object> processPositionType(ToolInstance toolInst){
		//Variable to identify the positionType after validating that geoposition or marker are correct
		String postype = null;
		//marker id
		String cosid = null;
		String orientation = null;		
		double lat = 0;
		double lon = 0;
		int maxdistance = 0;
		
		if (toolInst.getPositionType() != null){
			if (toolInst.getPositionType().equals(Constants.POS_TYPE_GEOPOSITION)){
				
				//Checking if geoposition. In that case, we extract latitude, longitude, and set the postype variable
				 /* String to split. */
				  String coordenadas = toolInst.getPosition();
				  String[] temp;
				  /* delimiter */
				  String delimiter = ",";
				  /* given string will be split by the argument delimiter provided. */
				  temp = coordenadas.split(delimiter);
				  /* print substrings */
				  if (temp.length == 2){
					  String latString = temp[0];
					  String lonString = temp[1];
					  if ((isNum(latString))&&(isNum(lonString))){
						  lat = Double.parseDouble(latString);
						  lon = Double.parseDouble(lonString);
						  
						  //the maximum distance to be visible in AR is converted to integer
						  String distance = toolInst.getMaxdistance();
						  maxdistance = 50000; //Default hardcoded maxdistance if the parsing of the default maxdistance fails
					      try{
					        	maxdistance = Integer.parseInt(distance);
					      } catch(NumberFormatException nfe) {
							    //Default max distance to be visible
					        	try {
					        		maxdistance = Integer.parseInt(Constants.ARBROWSERS_DEF_MAXDISTANCE);
					        	} catch(NumberFormatException nfe2) {
					        		maxdistance = 50000;
					        	}													    
					      }
						  
					      //if the coordenates are correct, it is considered as a geopositioned POI
						  postype = Constants.POS_TYPE_GEOPOSITION;

					  } else {
						  System.out.println("Latitude and longitude are not numbers");
			    		}									  
				  } else {
					  System.out.println("Bad format in Latitude and longitude");
		    		}
			
			} else if (toolInst.getPositionType().equals(Constants.POS_TYPE_JUNAIOMARKER)) {
				//Checking if positioned with a marker. In that case, we cosid and set the postype variable										

				if (toolInst.getPosition().contains(Constants.JUNAIOMARKER_URLBASE)){
						//If the marker URL is correct, it is considered a junaiomarker POI
					  							
						//Extracting the marker id
				        Pattern p = Pattern.compile(Constants.JUNAIOMARKER_URLBASE + "(.*?)\\.png");
					    Matcher m = p.matcher(toolInst.getPosition());
					    if (m.find() == true) {
			    		cosid = m.group(1);
			    		postype = Constants.POS_TYPE_JUNAIOMARKER;
					    }
				}
			}
		}
   	

		

		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("postype", postype);
		result.put("cosid", cosid);
		result.put("orientation", orientation);
		result.put("lat", lat);
		result.put("lon", lon);
		result.put("maxdistance", maxdistance);    	
    	
    	return result;
    }
    
  
    
    private static List<Poi> addPoi(Deploy deploy, InstancedActivity inst, String activityId, ToolInstance toolInst, HashMap<String, Object> positionAttributes, List<Poi> poiList, String username, String resKind, String resType, URL urlToolInstance){
    	
		String postype = (String) positionAttributes.get("postype");
		String cosid = (String) positionAttributes.get("cosid");									
		String orientation = (String) positionAttributes.get("orientation");
		double lat = (Double) positionAttributes.get("lat");
		double lon = (Double) positionAttributes.get("lon");
		int maxdistance = (Integer) positionAttributes.get("maxdistance");	
		LearningEnvironment le = deploy.getLearningEnvironment();
		String resTypeName = le.getToolTypeNameFromId(resType);
    	
		  String poitype = "url";
		  String scale = null;
//		  if ((resKind.equals("external")) && 
//		  		((resTypeName.equals("Google Documents")) || (resTypeName.equals("Google Spreadsheets")) || (resTypeName.equals("Google Presentations"))|| (resTypeName.equals("Google Forms (validation code)")) || (resTypeName.equals("Web Content")))) {
			  
		  if ((resKind.equals("external")) && 
			  		( (resTypeName.equals("Google Forms (validation code)")))) {

			  //ToolTypes GData, "GoogleForms with validation code" (20) and WebContent (6)
			  //WebContent added to correct the url in dropbox (it would not be necessary, but may help)
			  
			//debug (para quitar)
			  //	System.out.println("Voy a modificar la URL para toolInsResID: " + toolInsResID);
			  
			  	//Obtaining the final URL doing a HTTP GET to the url stored in the location field of the deploy
			  	//(the gluelet manager url)											  		
			  		urlToolInstance = getFinalUrl(urlToolInstance);
			  		String urlObtenida = urlToolInstance.toString();
			  		
			  		//In Google Forms with validation code, the validation code is included and
			  		//a short URL is generated to avoid junaio limitations with url options
		    		if ((resTypeName.equals("Google Forms (validation code)"))) {
		    			if (AuthService.getUidByUsername(username)!= null) {
		    			//	System.out.println("El uid para poner la validacion es " + AuthService.getUidByUsername(username));
		    				 Random generator = new Random();
		    				 int randomInt = generator.nextInt(10000);

							urlObtenida = urlObtenida.replace("#gid=0", "").concat("&entry_0=" + username + "-" + deploy.findGroupById(inst.getGroupId()).getName() + "-" + AuthService.getUidByUsername(username) + "-" + randomInt );
		    				
		    				try {
			    				//bit.ly API is used to obtain a short url, due to Junaio fails if the url contains more than one option
			    			    String obtenidaEncoded = URLEncoder.encode(urlObtenida, "UTF-8");
			    			    String shorturl = "http://api.bit.ly/v3/shorten?login=gsicjuan&apiKey=R_11a2dd0486b48d023214ff5563bffa42&format=txt&longUrl=" + obtenidaEncoded;
			    				URL URLshort = new URL(shorturl);

			    				BufferedReader in2;
								in2 = new BufferedReader(
								new InputStreamReader(URLshort.openStream(), "UTF-8"));
					        	urlObtenida = in2.readLine();
					        	in2.close();

						  	}  catch (Exception  e1) {
					  		System.out.println("Error trying to access to short URL");
			    			e1.printStackTrace();
			    			//TODO se podría controlar que esto ha ocurrido, y no mostrar el POI
			    		}

		    			}												    			
		    		}
		    		try {
						urlToolInstance = new URL(urlObtenida);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}

		    		//debug (para quitar)
				  //	System.out.println("URL modificada. Nueva URL: " + urlToolInstance.toString());

		        
		  } else if ((resKind.equals("external")) && 
			  		((resTypeName.equals("3D Model")))) {
			  //ToolType 3Dmodel
			  
			  poitype = "3dmodel";
			  
			  urlToolInstance = getFinalUrl(urlToolInstance);

			  //Verifying that "orientation" and "scale" exist and their contain is valid
		      scale = getScale(toolInst, scale);
		      orientation = getOrientation (toolInst);
		      //TODO en la orientación si el formato es incorrecto, lo dejo null y será en
		      //el adaptador de browser donde fije los valores por defecto
		      //Habría que hacer lo mismo en el resto de parámetros (eg scale)

		  } else if ((resKind.equals("external")) && 
			  		((resTypeName.equals("AR Image")))) {
			//ToolType ARImage
			  
			  poitype = "ARimage";
			  
			  urlToolInstance = getFinalUrl(urlToolInstance);											  

		      //Verifying that "orientation" and "scale" exist and their contain is valid
	          scale = getScale(toolInst, scale);
	          orientation = getOrientation (toolInst);

		  } else if ((resKind.equals("external")) && 
			  		((resTypeName.equals("Bucket")))) {
				//ToolType ARImage
				  
				  poitype = "bucket";
				  //The final URL is necessary to may use options in the URL (e.g., in Glue Earth), because GLUE does not pass them through 
				  urlToolInstance = getFinalUrl(urlToolInstance);
		  
		  }
		  //End of the generation of POI location attribute
		  
		  
		  //Creation of the POI object and Processing of repeated POIs (same tool instance in same position)
		  
//			  Tag tagIt = new Tag(toolInst.getName(),toolInst.getId(),toolInst.getLocationWithRedirects(deploy),lat,lon);
		  String activitydesc = deploy.getDesign().findActivityById(activityId).getDescription();
		  //Description is composed using the descriptions of the activity or of the toolinstance.
		  String poiDesc = getPoiDescription(activitydesc, toolInst);
		  if (postype.equals(Constants.POS_TYPE_GEOPOSITION)){
//					  System.out.println("Voy a enchufar un tag con el orientation a: " + orientation);
			  Poi poiIt = new Poi(toolInst.getName(), toolInst.getId(), poiDesc, urlToolInstance, lat, lon, maxdistance, poitype, toolInst.getPositionType(), scale, orientation);
				
			  boolean repeated =false;
			  //If other POI has same location in the same position, POI is repeated
			  if (!poiList.isEmpty()){
				  for (Poi poiInList : poiList) {
					  if (poiInList.getPositionType().equals(Constants.POS_TYPE_GEOPOSITION)) {
							if (poiIt.getLocation().equals(poiInList.getLocation()) && poiIt.getLat() == poiInList.getLat() &&poiIt.getLon() == poiInList.getLon()) {
								repeated = true;  
								System.out.println("Repeated POI found for coordinates " + poiIt.getLat());
							} 
					  }
					}  
			  }

			  //If POI is not repeated in the same position, add POI to the list
			  if (!repeated) {
				  //if maxdistance <= 0, poi is not considered
				  if (poiIt.getMaxdistance() > 0){
					  poiList.add(poiIt);
				  }

			  }
			  
			  

		  } else if (postype.equals(Constants.POS_TYPE_JUNAIOMARKER)) {
			  Poi poiIt = new Poi(toolInst.getName(), toolInst.getId(), poiDesc, urlToolInstance, cosid, poitype, toolInst.getPositionType(), scale, orientation);
			  boolean repeated =false;
			  //If other POI has same location in the same position, POI is repeated
			  if (!poiList.isEmpty()){
				  for (Poi poiInList : poiList) {
					  if (poiInList.getPositionType().equals(Constants.POS_TYPE_JUNAIOMARKER)) {
							if (poiIt.getLocation().equals(poiInList.getLocation()) && poiIt.getCosid().equals(poiInList.getCosid())) {
								repeated = true;  
								System.out.println("Repeated POI found for coordinates " + poiIt.getCosid());
							} 
					  }
					}  
			  }

			  //If POI is not repeated in the same position, add POI to the list
			  if (!repeated) {
				  poiList.add(poiIt);
			  }
		  }
	  	
    	return poiList;
    }
    
    private static boolean isBucket(Deploy deploy, ToolInstance toolInst, boolean bucketInBucket, String artifactTypeName){
    	boolean result = false;
    	
    	if (!bucketInBucket){
    		String toolInsResID = toolInst.getResourceId();
    		String resKind = deploy.getDesign().findResourceById(toolInsResID).getToolKind();
    		String resType = deploy.getDesign().findResourceById(toolInsResID).getToolTypeNumber();
    		LearningEnvironment le = deploy.getLearningEnvironment();
    		String resTypeName = le.getToolTypeNameFromId(resType);
    		
    		if ((resKind.equals("external")) && ((resTypeName.equals("Bucket")))) {
    			result = true;
    		}
    	} else {
    		if (artifactTypeName.equals("Bucket")) {
    			result = true;
    		}
    	}

		return result;
    }
    
    private static void addPoisFromBucket(Deploy deploy, InstancedActivity inst, String activityId, List<Poi> poiList, ToolInstance toolInst, String username){
    	
		  URL urlToolInstance = null;
		  URI uriToolInstance = null;
		  Bucket bucket = null;
		  Long bucketTimestamp = null;
		try {
			uriToolInstance = new URI(toolInst.getLocationWithRedirects(deploy).toString() + "?callerUser=" + URLEncoder.encode(username, "UTF-8"));
			urlToolInstance = uriToolInstance.toURL();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		  
		  System.out.println("bucket urlToolInstance: " + urlToolInstance);		  
	//	  String bucketGuiUrl = ( getFinalUrl(urlToolInstance) ).toString();	
		  
	//	  System.out.println("bucket : " + bucketGuiUrl);
     	
    	  if (bucketCache == null){
			  bucketCache = new HashMap<String,Pair<Long, Bucket>>();
		  }		  

		  if (bucketCache.containsKey(urlToolInstance.toString())){
			  bucket = bucketCache.get(urlToolInstance.toString()).getValue();
			  bucketTimestamp = bucketCache.get(urlToolInstance.toString()).getKey();

			  //checking bucket timestamp of the cache and obtaining the remote bucket if necessary
	          long bucketExpirationTime = 10000; //buckets are obtained from cache during 10s
	          long currentTime = new Date().getTime();
	          if (bucketTimestamp != null){
	        	  if (currentTime >(bucketTimestamp + bucketExpirationTime)){
	        		  	System.out.println("Bucket " + urlToolInstance.toString() + " expired in cache. Obtaining the remote bucket");
	    			    bucket = getRemoteBucket (urlToolInstance);
	    			    if (bucket != null){
	    				    bucketCache.put(urlToolInstance.toString(), Pair.of(new Date().getTime(), bucket));
	            		  	System.out.println("Bucket " + urlToolInstance.toString() + " renewed in cache");
	    			    }	

	        	  }
	          }

		  } else {
			  bucket = getRemoteBucket (urlToolInstance);
			  if (bucket != null){
				  bucketCache.put(urlToolInstance.toString(), Pair.of(new Date().getTime(), bucket));
			  }		  

		  }
		  bucketCacheTimestamp = new Date().getTime();

    	
		  String bucketPosType = bucket.getPositionType();
		  if (bucket != null && bucketPosType != null && bucketPosType != "none" && bucketPosType != ""){
			  ArrayList<Artifact> artifacts = bucket.getArtifacts();
			  if (artifacts != null){
				  for (Artifact artifact : artifacts){
					  ToolInstance artToolInst = getToolInstanceFromArtifact(artifact, deploy);
					  
					  
					  //TODO esto se podría meter en un método, ya que se repite
						//Checking the position type parsing parameters of tools instance
					    String artifactTypeName = getArtifactTypeNameFromId(bucket, artifact.getType());
						if ((artToolInst.getPosition()!= null && artToolInst.getPositionType() != null) || (artifactTypeName != "Bucket")) {
							
							HashMap<String, Object> positionAttributes = processPositionType(artToolInst);
							String postype = (String) positionAttributes.get("postype");								
							
							//End of checking the position type
							
							//Now, we generate the POI location attribute, processing the tool instance location depending on the tool type
							//In case of some tool type, we get also the poi orientation attribute
							if (postype != null && postype != "none" && artToolInst.getLocationWithRedirects(deploy) != null && artifactTypeName != "Bucket") {
								  
								//Default location, for generic tool types
								  URI uriArtToolInst = null;
								  URL urlArtToolInst = null;
								try {
									uriArtToolInst = new URI(artToolInst.getLocationWithRedirects(deploy).toString() + "?callerUser=" + URLEncoder.encode(username, "UTF-8"));
									urlArtToolInst = uriArtToolInst.toURL();
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (URISyntaxException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (MalformedURLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								  
								  String resKind = "external";
								  String resType = artifact.getType();
								  
								  //Creation of POI										  
								  poiList = addPoi(deploy, inst, activityId, artToolInst, positionAttributes, poiList, username, resKind, resType, urlArtToolInst);
								  
								  //End of Creation of the POI object and processing of repeated POIs
							} else {
								  //If the tool instance is a bucket, positioned artifacts are extracted and added to POI list										  
								  if (isBucket(deploy, artToolInst, true, artifactTypeName) && artToolInst.getLocationWithRedirects(deploy) != null) {
									  addPoisFromBucket(deploy, inst, activityId, poiList, artToolInst, username);

									  
								  }
							}
						}
						 //TODO HASTA AQU�? (esto se podría meter en un método, ya que se repite								  
					  
				  }
			  }
		  }
    	
    }

    
    private static Bucket getRemoteBucket(URL urlToolInstance){
		  //Extracting bucketId from location
		  String bucketId = null;
		  Bucket bucket = null;
		  
		  //It would be necessary if instance wouldn't be resolved before
		 // String bucketGuiUrl = ( getFinalUrl(urlToolInstance) ).toString();		  
		    String bucketGuiUrl;
	  
		  if (finalUrlCache != null && finalUrlCache.containsKey(urlToolInstance.toString())){
			  bucketGuiUrl = finalUrlCache.get(urlToolInstance.toString()).getValue().toString();
		  } else {
			  bucketGuiUrl = ( getFinalUrl(urlToolInstance) ).toString();	
		  }
		  
		  //checking buckets cache timestamp and empting it if necessary
		  long currentTime = new Date().getTime();
          long cacheExpirationTime = 3600000; //cache is cleared after 1h without being used
          if (bucketCacheTimestamp != null){
        	  if (currentTime >(bucketCacheTimestamp + cacheExpirationTime)){
        		  	bucketCache.clear();
        		  	System.out.println("Buckets cache cleared");
        	  }
          }
		  
		  try {

			  
			  Pattern p = Pattern.compile("bucketId=(.*?)(&.*)?$");
			  Matcher m = p.matcher(bucketGuiUrl);
			  if (m.find() == true) {
				  bucketId = m.group(1);
			  }
			  //debug
			  System.out.println("Processing bucket id " + bucketId);
			  
			  if (bucketId != null){

				  String bucketUrlBase = bucketGuiUrl.substring(0, bucketGuiUrl.lastIndexOf("bucketserver/")+13);
				  URL bucketUrl;
	
				  bucketUrl = new URL(bucketUrlBase + "bucket/" + bucketId);
			
				  //debug
				  System.out.println("Trying to access to bucket " + bucketUrl);

		   //JUAN: If bucket is in local, host will be replaced by localhost
				  URL uriGluepsExternal= new URL(Constants.configuration().getProperty("app.external.uri", "http://157.88.130.207/GLUEPSManager/"));
				  String hostGlueps = uriGluepsExternal.getHost();
				  String hostBucket = bucketUrl.getHost();
				  int protocolBucket = bucketUrl.getPort();
				  if (protocolBucket == -1){
					  protocolBucket = 80;
				  }
				  URL bucketUrlGet = bucketUrl;
				  if (hostGlueps.equals(hostBucket)){
					  bucketUrlGet = new URL(bucketUrl.toString().replace(hostBucket, "localhost:" + protocolBucket));
					  System.out.println("Bucket URL is local. Doing GET to " + bucketUrlGet);
					  bucketUrlBase = bucketUrlBase.replace(hostBucket, "localhost:" + protocolBucket);
				  }
				  
				  
				  
				  BufferedReader in2;
				  in2 = new BufferedReader(
//				  new InputStreamReader(bucketUrl.openStream(), "UTF-8"));
			//JUAN: If bucket is in local, host will be replaced by localhost (It is supposed that it will exist an apache redirection to the correct port
				  new InputStreamReader(bucketUrlGet.openStream(), "UTF-8"));
				  String bucketJson = in2.readLine();
				  in2.close();
	      	  
				  Gson gson = new Gson();
				  bucket = gson.fromJson(bucketJson, Bucket.class);
				  //debug
				  System.out.println("Recibido JSON de bucket " + bucketUrlGet);
				  
				  
			  }
			} catch (Exception e) {
				e.printStackTrace();
			}	  
			
			return bucket;
    }
    
    
    private static ToolInstance getToolInstanceFromArtifact(Artifact artifact, Deploy deploy){
    	ToolInstance toolInst = null;
    	if (artifact != null){
    		toolInst = new ToolInstance();
    		
    		toolInst.setId(artifact.getId());
    		toolInst.setName(artifact.getName());
    		toolInst.setDeployId(deploy.getId());
    		toolInst.setResourceId(null);
    		toolInst.setLocation(artifact.getLocation());
    		toolInst.setPosition(artifact.getPosition());
    		toolInst.setPositionType(artifact.getPositionType());
    		toolInst.setMaxdistance(artifact.getMaxdistance());
    		toolInst.setScale(artifact.getScale());
    		toolInst.setDescription(artifact.getDescription());
    		toolInst.setOrientation(null);    		
    	}
    	
    	return toolInst;
    }
    
    private static String getArtifactTypeNameFromId (Bucket bucket, String typeId){    	
//		ArrayList<ArtifactType> artifactTypeList = bucket.getArtifactTypes();
		  String typeName = null;
		  String bucketUrlBase = bucket.getLocation().toString().substring(0, bucket.getLocation().toString().lastIndexOf("bucketserver/")+13);
		  ArrayList<ArtifactType> artifactTypeList = null;
		  if (bucketserverArtifactTypeList == null){
			  bucketserverArtifactTypeList = new HashMap<String, ArrayList<ArtifactType>>();
		  }		  

		  if (bucketserverArtifactTypeList.containsKey(bucketUrlBase)){
			  artifactTypeList = bucketserverArtifactTypeList.get(bucketUrlBase);

		  } else {
			  artifactTypeList = getRemoteArtifactTypes (bucketUrlBase, bucket, typeId);
			  bucketserverArtifactTypeList.put(bucketUrlBase, artifactTypeList);
		  }
		  if (artifactTypeList != null){
				for (ArtifactType type : artifactTypeList){
					if (type.getId().equals(typeId)){
						typeName = type.getName();
						break; 
					}
				}
		  }
		  
		  //checking ArtifactTypeList cache timestamp and empting it if necessary
		  long currentTime = new Date().getTime();
          long cacheExpirationTime = 3600000; //cache is cleared after 1h without being used
          if (bucketserverArtifactTypeListTimestamp != null){
        	  if (currentTime >(bucketserverArtifactTypeListTimestamp + cacheExpirationTime)){
        		    bucketserverArtifactTypeList.clear();
        		  	System.out.println("ArtifactTypeList cache cleared");
        	  }
          }
          bucketserverArtifactTypeListTimestamp = new Date().getTime();
		  
		  return typeName; 
	  
    }
    
    
    private static ArrayList<ArtifactType> getRemoteArtifactTypes (String bucketUrlBase, Bucket bucket, String typeId){    	
//		ArrayList<ArtifactType> artifactTypeList = bucket.getArtifactTypes();
		  String typeName = null;
		  ArrayList<ArtifactType> artifactTypeList = null;
		  URL toolTypesUrl;
		  

	  
	  
		  try {
			  toolTypesUrl = new URL(bucketUrlBase + "artifactTypeList");
				
			  //debug
			  System.out.println("Trying to access to bucket artifact types list " + toolTypesUrl);
			  
			   //JUAN: If bucket is in local, host will be replaced by localhost
			  URL uriGluepsExternal= new URL(Constants.configuration().getProperty("app.external.uri", "http://157.88.130.207/GLUEPSManager/"));
			  String hostGlueps = uriGluepsExternal.getHost();
			  String hostBucket = toolTypesUrl.getHost();
			  int protocolBucket = toolTypesUrl.getPort();
			  if (protocolBucket == -1){
				  protocolBucket = 80;
			  }
			  URL toolTypesUrlGet = toolTypesUrl;
			  if (hostGlueps.equals(hostBucket)){
				  toolTypesUrlGet = new URL(toolTypesUrl.toString().replace(hostBucket, "localhost:" + protocolBucket));
				  System.out.println("Bucket URL is local. Doing GET to " + toolTypesUrlGet);
			  }
			  

			  
			  BufferedReader in2;

			  in2 = new BufferedReader(
			  new InputStreamReader(toolTypesUrlGet.openStream(), "UTF-8"));
			  String artifactTypeListJson = in2.readLine();
			  in2.close();
	  	  
			  Gson gson = new Gson();
			  Type typeOfT = new TypeToken<ArrayList<ArtifactType>>(){}.getType();
			  artifactTypeList = gson.fromJson(artifactTypeListJson, typeOfT);
			

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return artifactTypeList;    	
    }

    
    
    
    
    private static String getPoiDescription(String activitydesc, ToolInstance toolInst)  {
    	String description = "";
    	//If activity has not description or the POI is a bucket artifact, the description is that of the toolinstance
    	//(in case of bucket artifacts, the activity description is not used
    	if (activitydesc == null || activitydesc.equals("") || toolInst.getResourceId() == null){
    		if (toolInst.getDescription() == null){
    			description = "";
    		} else {
    			description = toolInst.getDescription();
    		}
    	} else {
    		if (toolInst.getDescription() == null || toolInst.getDescription().equals("")){
    			description = activitydesc;  			
    		} else {
    			//description = activitydesc + "\n\n" + toolInst.getDescription();
    			description = toolInst.getDescription();
    		}
    		
    	}
    	return description;
    }
    
    
}
