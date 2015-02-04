/**
 This file is part of DoodleAdapter.

 DoodleAdapter is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2012 GSIC (UVA).

 DoodleAdapter is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use DoodleAdapter for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when DoodleAdapter is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute DoodleAdapter and/or modify it under the
   terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>
 ]
*/
package glue.adapters.implementation.doodle.entities;

import glue.common.entities.instance.InstanceEntity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;



/**
 * Entity representing a Doodle instance.
 * 
 * @author  	Carlos Alario
 * @contributor Javier Enrique Hoyos Torio
 * @contributor	David A. Velasco Villanueva
 * @version 	2012092501
 * @package 	glue.adapters.implementation.doodle.entities
 */
public class Doodle implements InstanceEntity {

	/// attributes ///
	
	/** Title */
	protected String title = null;
	
	/** Description */
	protected String description = null;
	
	/** Options */
	protected ArrayList<String> options = null;
	
	/** Caller User */
	protected String callerUser = null;
	
	/** URL to the Doodle instance */
	protected String entryURL = null;
	
	/** Doodle instance content-location */
	protected String contentLocation = null;
	
	/** Doodle instance key */
	protected String xDoodleKey = null;
	
	/** Local identifier */
	protected int index = -1;
	
	/** Date of the instance creation */ 
	protected Date updated;
	
	/** URL to the feed with the Doodle Server */
	protected String feedURL = null;	
	
	/// methods ///
	
	/**
	 * Constructor for a Doodle instance.
	 * 
	 * @param 	title		String		Title of the Doodle.
	 * @param	description	String		Description of the Doodle.
	 * @param	options		ArrayList	Options of the Doodle.		
	 * @param 	feed		string  URL with the feed to send the instance creation request 
	 * TODO Change this number for the name of the users userName[]. 
	 * 	 */
	
	public Doodle(String title, String description, ArrayList<String> options, String callerUser, String feed) {
		this.feedURL = feed;
		this.updated = new Date();
		this.options = new ArrayList<String>();
		try {
			this.title = new String (URLEncoder.encode(title,"UTF-8").replace("+"," "));
			this.description = new String(URLEncoder.encode(description, "UTF-8").replace("+"," "));
			for (int i = 0; i < options.size(); i++){
				this.options.add(URLEncoder.encode(options.get(i), "UTF-8").replace("+"," "));
			}
			this.callerUser = new String (URLEncoder.encode(callerUser, "UTF-8").replace("+"," "));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.err.println("It was not possible to encode the content of the Doodle");
		}
	}
	
	
	/**
	 * Constructor for a Doodle instance previously saved into an external file.
	 * 
	 * 
	 * @param	title			String
	 * 
	 */
	public Doodle(int index, String title, Date updated) {
		this.index = index;
		this.title = title;
		this.updated = updated;
	}

	/**
	 * Creation of a Doodle.
	 * 
	 * @param	specificParams 
	 */
	@Override
	public void create(String callerUser, Map<String, String> specificParams) throws ResourceException {
		String data = null;
		
		// Create the Doodle instance
		String postURL = new String(feedURL + "api1WithoutAccessControl/polls/");
		try {
			data = parseXMLRequest();
			postToDoodle(postURL, data);
			if (contentLocation == null || xDoodleKey == null){
				//System.err.println("Doodle server has not provided the suitable parameters to manage this instance");
				throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Doodle server has not provided the suitable parameters to manage this instance");
			}
			entryURL = feedURL + contentLocation;
			
		} catch (IOException io) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected error while creating a new Doodle instance", io);
		}
		
	}
			

	
	/**
	 * Deletion of a Doodle page.
	 * 
	 * @param	specificParams 
	 * @return					String			Always null
	 */	
	@Override
	public String delete(Map<String, String> specificParams) throws ResourceException {
		// Delete the Doodle instance
		String deleteURL = new String(feedURL + "api1WithoutAccessControl/polls/" + contentLocation);
		try {
			deleteToDoodle(deleteURL);	
		} catch (IOException io) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected error while deleting a Doodle instance", io);
		}

		return null; 
	}
	

	/** 
	 * Getter for title 
	 * 
	 * @return title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Getter for last update date
	 * 
	 * @return Last update date
	 */
	public Date getUpdated() {
		return updated;
	}
	
	/**
	 * Getter for browser-friendly URL 

	 * @param 	callerUser	String					Name of the user asking for the URL
	 * @param	params		Map<String, String>		List of specific parameters
	 * @return 										Browser-friendly URL 
	 */
	@Override
	public String getHtmlURL(String callerUser, Map<String, String> specificParams) {
		return entryURL;
	}

	/**
	 * Getter for local identifier
	 * 
	 * @return	int		Local identifier of the element
	 */
	public int getIndex() {
		return index;
	}
	
	
	/**
	 * 
	 */
	@Override
	public void loadSpecificState(BufferedReader in) throws IOException {
		feedURL = in.readLine(); 	// It is necessary to store the feed URL
		entryURL = in.readLine();
		contentLocation = in.readLine();
		xDoodleKey = in.readLine();
		
	}
	
	@Override
	public void saveSpecificState(PrintStream out) {
		out.println(feedURL);
		out.println(entryURL);
		out.println(contentLocation);
		out.println(xDoodleKey);
	}

	
	protected void postToDoodle(String urlStr, String data) throws IOException {
		
	    try {
	        URL url;
	        URLConnection urlConnection;
	        DataOutputStream outStream;
	 
	        // Create connection
	        url = new URL(urlStr);
	        urlConnection = url.openConnection();
	        ((HttpURLConnection)urlConnection).setRequestMethod("POST");
	        urlConnection.setDoInput(true);
	        urlConnection.setDoOutput(true);
	        urlConnection.setUseCaches(false);
	        urlConnection.setRequestProperty("Content-Type", "application/xml");
	        urlConnection.setRequestProperty("Content-Length", ""+ data.length());
	        
	 
	        outStream = new DataOutputStream(urlConnection.getOutputStream());
	        outStream.writeBytes(data);
	        outStream.flush();
	        outStream.close();
	 
			// Read Response
			contentLocation = urlConnection.getHeaderField("Content-Location");
			xDoodleKey = urlConnection.getHeaderField("X-DoodleKey"); 
	    }
	    catch(Exception ex) {
	        System.out.println("Exception in :\n"+ ex.toString());
	    }
		return;

	}
	
	protected void deleteToDoodle(String urlStr) throws IOException {
		
	    try {
	        URL url;
	        HttpURLConnection urlConnection;
	 
	        // Create connection
	        url = new URL(urlStr);
	        urlConnection = (HttpURLConnection) url.openConnection();
	        ((HttpURLConnection)urlConnection).setRequestMethod("DELETE");
	        urlConnection.setDoOutput(true);
	        urlConnection.setRequestProperty("Content-Type", "application/xml");
	        urlConnection.addRequestProperty("X-DoodleKey", xDoodleKey);
	        urlConnection.connect();
	        int responseCode = urlConnection.getResponseCode();	    
	        if (responseCode == 204){
	        	return;
	        }
	    }
	    catch(IOException ex) {
	    	ex.printStackTrace();
	        //System.out.println("Exception in :\n"+ ex.toString());
	    }
		return;

	}
		
	
	// Hardcoded with the RESTful Doodle API
	protected String parseXMLRequest(){
		StringBuffer data = new StringBuffer();
		
		data.append("<poll xmlns=\"http://doodle.com/xsd1\">");
		data.append("<type>TEXT</type>");
		data.append("<hidden>false</hidden>");
		data.append("<levels>2</levels>");
		
		data.append("<title>"+title+"</title>");
		data.append("<description>"+description+"</description>");
		
		data.append("<initiator>");
		data.append("<name>"+callerUser+"</name>");
		data.append("</initiator>");
		
		data.append("<options>");
		for (int i = 0; i < options.size(); i++){
			data.append("<option>"+options.get(i)+"</option>");
		}
		data.append("</options>");

		data.append("</poll>");
		
		return data.toString();
	}
	


	@Override
	public String getAccessParams(String callerUser, Map<String, String> specificParams) {
		return null;
	}


	@Override
	public void setIndex(int index) {
		this.index = index;
	}


	@Override
	public void setUsers(List<String> users, String callerUser, Map<String, String> specificParams) {
		// TODO something to do here?
	}
	
	
}
