/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Dabbleboard Adapter.
 * 
 * Dabbleboard Adapter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Dabbleboard Adapter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.adapters.implementation.dabbleboard.entities;

import glue.adapters.implementation.dabbleboard.manager.DabbleboardAdapterServerMain;
import glue.common.entities.instance.InstanceEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;


/**
 * Entity representing a Dabbleboard.
 * 
 * @author  	Carlos Alario
 * @contibutor	Javier Enrique Hoyos Torio
 * @contibutor	David A. Velasco
 * @version 	2012092501
 * @package 	glue.adapters.implementation.dabbleboard.entities
 */
public class Dabbleboard implements InstanceEntity {
	
	/// constants ///
	protected String TO_CREATE_MARK = "[TO_CREATE]";
	
	/// attributes ///
	
	/** title */
	protected String title = null;
	
	/** URL to the Dabbleboard */
	protected Vector<String> userURLs = null;
	
	/** Local identifier */
	protected int index = -1;
	
	/** Date of the instance creation */ 
	protected Date updated;
	
	/** List of user names */
	protected Vector<String> userNames = null;
	
	/** List of user passwords */
	protected Vector<String> userPasswords = null; 
	
	/** Drawing */
	protected String drawing = null;
	
	/** Drawing password */
	protected String drawingPassword = null;
	
	/** Owner */
	protected String ownerName = null;	// DON'T REPLACE WITH 'ownerIndex' or the like; read setUsers() comments to see why 
	
	/** URL to the feed with the Dabbleboard */
	protected String feedURL = null;


	/** Authorized-to-creation developer username */
	protected String devUser;

	/**
	 * Authorized-to-creation developer password 
	 * 
	 * Needed here to provide lazy creation of Dabbleboard users without changing the GLUE contract, 
	 * since this parameter is being received only in POST and DELETE calls, but not in GETs, where
	 * it's required for the delayed creation of users.
	 * 
	 * Its presence as a member implies that it has to be stored in instances.txt; this is another point
	 * to be adressed when providing secutiry to the GLUE system.
	 * 
	 * The more security-friendly solution is modify the GLUE contract to let the adapters receive more
	 * parameters. In that way, the password must be protected in all the HTTP calls (that's needed anyway in
	 * POST and DELETE), but not in persistence.
	 * 
	 * Piece of comment!!
	 */
	protected String devPass;		
	
	
	/// methods ///
	
	/**
	 * Constructor for a Dabbleboard.
	 * 
	 * @param	callerUser	String			Username of the creating user; owner of the dabbleboard
	 * @param 	users		List<String> 	List of users names 
	 * @param 	feed		String  		URL with the feed to send the instance creation request
	 * @param	devUser		String			Developer username 
	 */
	
	public Dabbleboard(String callerUser, List<String> users, String feed, String devUser) {
		int numberOfUsers = (users == null ? 0 : users.size());
		
		userNames = new Vector<String>(numberOfUsers);
		userPasswords = new Vector<String>(numberOfUsers);
		userURLs = new Vector<String>(numberOfUsers);
		
		long timestamp = System.currentTimeMillis();
		
		// Checking the inclusion of callerUser in users list
		
		// Creation of users and passwords according to the Dabbleboard rules.
		String userI = null;
		for (int i = 0; i < numberOfUsers; i++){
			userI = users.get(i);
			userNames.add(userI + "-" + timestamp++);
			userPasswords.add(userI);
		}
		
		feedURL = feed;
		int pos = -1;
		if (users != null)
		{
			pos = users.indexOf(callerUser);
		}
		// Creation of an owner
		
		if (pos >= 0)
			ownerName = userNames.get(pos);
		else {	// exceptionally permissive adapter - the callerUser (teacher) MUST be in the users list; returning an exception would be fair
			ownerName = callerUser + "-" + timestamp++;
			userNames.add(ownerName);
			userPasswords.add(callerUser);
			//Increment the number of users to add the userURL of the callerUser
			numberOfUsers ++;
			
		}

		
		// Manual creation of the drawing
		drawing = "drawing" + "-" + timestamp++;
		drawingPassword = "drawing";
		
		// Manual creation of HTML URL - MARKED FOR DELAYED CREATION!!!
		for (int i = 0; i < numberOfUsers; i++){
			userURLs.add(TO_CREATE_MARK + 
						 feedURL + "iframe?"+ 
							"dev_id=" 			+ devUser + 
							"&user_id="  		+ userNames.get(i) + 
							"&user_key=" 		+ userPasswords.get(i) + 
							"&drawing_user_id=" + ownerName + 
							"&drawing_id=" 		+ drawing + 
							"&drawing_key=" 	+ drawingPassword);
		}
		
		title = "No title for this instance";
		updated = new Date();
	}
	
	
	/**
	 * Constructor for a Dabbleboard instance previously saved into a external file.
	 * 
	 * 
	 * @param	title			String
	 * 
	 */
	public Dabbleboard(int index, String title, Date updated) {
		this.index = index;
		this.title = title;
		this.updated = updated;
	}
		
		
	/**
	 * Creation of a Dabbleboard.
	 * 
	 * - Create users (including owner)
	 * - Create drawing
	 * - Creating HTML URL
	 * 
	 * @param	specificParams 
	 */
	@Override
	public void create(String callerUser, Map<String, String> specificParams) throws ResourceException {
		/// Get specific parameters
		devUser = specificParams.get("user");
		devPass = specificParams.get("pass");
		
		/// Creation of OWNER USER
		int ownerIndex = userNames.indexOf(ownerName);	// constructor 
		String userCreationURL = new String(feedURL + "create_user?" + 
											"dev_id=" 		+ devUser +
											"&dev_key=" 	+ devPass + 
											"&user_id=" 	+ ownerName + 
											"&user_key=" 	+ userPasswords.get(ownerIndex));
		
		Response response;
		try {
			response = DabbleboardAdapterServerMain.pool.submit(new Post(userCreationURL)).get(); 	// creations always in pool
			if (response.code < 200 || response.code >= 300) {
				throw new ResourceException(new Status(response.code), response.message + " in Dabbleboard while creating user '" + ownerName + "' (drawing owner)");	// TODO better exceptions policy; maybe inside post() method?
			}
			userURLs.set(ownerIndex, userURLs.get(ownerIndex).substring(TO_CREATE_MARK.length()));
		} catch (ExecutionException e) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error in Dabbleboard creating user '" + ownerName + "' (drawing owner)", e.getCause());
			
		} catch (InterruptedException e) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error in Dabbleboard creating user '" + ownerName + "' (drawing owner)", e.getCause());
		}
		//System.out.println("Owner: " + ownerName + " has been created in Dabbleboard");	// TODO use a logger
		
		
		/// Creation of the DRAWING 	
		String drawingCreationURL = new String(feedURL + "create_drawing?" + 
												"dev_id=" 		+ devUser + 
												"&dev_key=" 	+ devPass + 
												"&user_id=" 	+ ownerName + 
												"&drawing_id=" 	+ drawing + 
												"&drawing_key=" + drawingPassword);
		try {
			response = (DabbleboardAdapterServerMain.pool.submit(new Post(drawingCreationURL))).get();	// debugging hell!!!
			if (response.code < 200 || response.code >= 300) {
				throw new ResourceException(new Status(response.code), response.message + " in Dabbleboard while creating a new drawing");		// TODO better exceptions policy
			}
		} catch (ExecutionException e){
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error in Dabbleboard while creating a new drawing", e.getCause());
		} catch (InterruptedException e) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error in Dabbleboard while creating a new drawing", e.getCause());
		}
		//System.out.println("Drawing: " + drawing + " has been created in Dabbleboard"); // TODO use a logger
		
	}
	
	
	/**
	 * Deletion of a Dabbleboard.
	 * 
	 * @param	specificParams 
	 * @return					String			Always null
	 */	
	@Override
	public String delete(Map<String, String> specificParams) throws ResourceException {
		/// Get specific parameters
		devUser = specificParams.get("user");
		devPass = specificParams.get("pass");
		
		
		/// Delete DRAWING
		String drawingDeletionURL = new String(feedURL + "delete_drawing?" + 
												"dev_id=" 		+ devUser +
												"&dev_key="		+ devPass + 
												"&user_id="		+ ownerName + 
												"&drawing_id="	+ drawing);
		Response response;
		try {
			response = (DabbleboardAdapterServerMain.pool.submit(new Post(drawingDeletionURL))).get();	// debugging hell!!!
			if (response.code < 200 || response.code >= 300) {
				throw new ResourceException(new Status(response.code), response.message + " in Dabbleboard while deleting drawing");		// TODO better exceptions policy?
			}
		} catch (ExecutionException e){
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error in Dabbleboard while deleting drawing", e.getCause());
		} catch (InterruptedException e){
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error in Dabbleboard while deleting drawing", e.getCause());
		}	
		//System.out.println("Drawing: " + drawing + " has been deleted in Dabbleboard");	// TODO use a looger
		
		/// Delete users WITHOUT WAITING FOR RESPONSE
		for (int i = 0; i < userNames.size(); i++){
			if (!userURLs.get(i).startsWith(TO_CREATE_MARK)) {	// only yet created users :)
				String userDeletionURL = new String(feedURL  + "delete_user?" + 
													"dev_id=" 		+ devUser +
													"&dev_key=" 	+ devPass + 
													"&user_id=" 	+ userNames.get(i));
				DabbleboardAdapterServerMain.pool.submit(new Post(userDeletionURL));
			}
		}
		/// RESULTS ARE NOT IMPORTANT (if Dabbleboard fails de deletion, worse for them... ; just joking, TODO garbage collector for pending deletions /// 
		
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
		int pos = userPasswords.indexOf(callerUser);
		if (pos >= 0) {
			String userURL = userURLs.get(pos);
			if (userURL.startsWith(TO_CREATE_MARK)) {
				// delayed creation of Dabbleboard user 
				String userCreationURL = new String(feedURL + "create_user?" + 
													"dev_id=" 		+ devUser +
													"&dev_key=" 	+ devPass + 
													"&user_id=" 	+ userNames.get(pos) + 
													"&user_key=" 	+ userPasswords.get(pos));
				Response response;
				try {
					response = DabbleboardAdapterServerMain.pool.submit(new Post(userCreationURL)).get();
					if (response.code < 200 || response.code >= 300) {
						throw new ResourceException(new Status(response.code), response.message + " in Dabbleboard while creating user '" + userNames.get(pos) + "'");	// TODO better exceptions policy; maybe inside post() method?
					}
					userURLs.set(pos, userURL.substring(TO_CREATE_MARK.length()));	// unmark URL as pending of creation
				} catch (ExecutionException e) {
					throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error in Dabbleboard creating user '" + userNames.get(pos) + "'", e.getCause());
				} catch (InterruptedException e) {
					throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error in Dabbleboard creating user '" + userNames.get(pos) + "'", e.getCause());
				}
				//System.out.println("User: " + userNames.get(pos) + " has been created in Dabbleboard");	// TODO use a logger
			}
			return userURLs.get(pos);
		} else
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "User " + callerUser + " is not allowed to get this instance");
	}


	/**
	 * Getter for local identifier
	 * 
	 * @return	int		Local identifier of the element
	 */
	public int getIndex() {
		return index;
	}
	
	
	private class Response {
		public int code = -1;
		public String message = "";
	}
	
	/**
	 * Send an HTTP POST to the Dabbleboard server.
	 *
	 * @param 	string	URL for the POST.
	 */
	private class Post implements Callable<Response> {
		
		private String urlStr;
		
		public Post(String urlStr) {
			this.urlStr = urlStr;
		}
		
		public Response call() throws MalformedURLException, IOException {
			URL url = new URL(urlStr);
			URLConnection connection;
			connection = url.openConnection();
			connection.setDoOutput(true);
			
			//long delay = System.currentTimeMillis(); 
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.close();
			HttpURLConnection h = (HttpURLConnection) connection;
			Response response = new Response();
			response.code = h.getResponseCode();
			response.message = h.getResponseMessage();
			
			// trying to get more detail
			try {
				Object content = h.getContent();
				if (content != null && content instanceof String)
					response.message += " - " + (String)content;
				
			} catch (IOException e) {
				// it does'n matter - this was for extended information in case of an error response
			}
			
			//delay = System.currentTimeMillis() - delay;
			//System.out.println("POST a " + urlStr + " en " + delay + " ms para obtener " + code + " " + message);
			//System.out.println("** CODE: " + response.code + " ; MESSAGE: " + response.message + " ; URL: " + urlStr);
			return response;
		}
	}
	
	
	@Override
	public String getAccessParams(String callerUser, Map<String, String> specificParams) {
		String devUser = specificParams.get("user");
		String pass = specificParams.get("pass");
		return "user=" + Reference.encode(devUser) + "&pass=" + Reference.encode(pass);
	}


	/**
	 * 
	 */
	@Override
	public void loadSpecificState(BufferedReader in) throws IOException {
		feedURL = in.readLine();
		
		// dealing with backwards compatibility 
		String numberOrString = in.readLine();
		int numberOfUsers = -1;
		try {
			numberOfUsers = Integer.parseInt(numberOrString);
		} catch (NumberFormatException n) {
			// not integer -> new format; this is devUser (ugly, but Dabbleboard doesn't let numbers as user names)
			devUser = numberOrString;
			devPass = in.readLine();
			numberOfUsers = Integer.parseInt(in.readLine());
		}
		
		ownerName = in.readLine();	
		drawing = in.readLine();
		drawingPassword = in.readLine();
		userNames = new Vector<String>(numberOfUsers);
		userPasswords = new Vector<String>(numberOfUsers);
		userURLs = new Vector<String>(numberOfUsers);
		for (int i = 0; i < numberOfUsers; i ++){
			userNames.add(in.readLine());
			userPasswords.add(in.readLine());
			userURLs.add(in.readLine());
		}

	}


	@Override
	public void saveSpecificState(PrintStream out) {
		out.println(feedURL);

		// NEW fields needed to provide lazy creation of users in Dabbleboard (devUser & devPass)
		out.println(devUser);
		out.println(devPass);	// TODO provide security
		
		out.println(userNames.size());
		out.println(ownerName);
		out.println(drawing);
		out.println(drawingPassword);
		for (int i=0; i<userNames.size(); i++) {
			out.println(userNames.get(i));
			out.println(userPasswords.get(i));
			out.println(userURLs.get(i));
		}
	}


	@Override
	public void setIndex(int index) {
		this.index = index;
	}


	@Override
	public void setUsers(List<String> users, String callerUser, Map<String, String> specificParams) {
		
		// find parameters - we are assuming these are not changing; it will be true while this parameters keep coming from the Internal Registy in GLUEletManager
		devUser = specificParams.get("user");  
		devPass = specificParams.get("pass");
		
		// WATCH OUT THE TRICK!!
		// 'ownerName' won't be changed, no matter what is the value of 'callerUser' parameter, AND NO MATTER IF OWNER IS NOT IN THE NEW USERS LIST;
		// this way, although a teacher is pushed out of the course, Dabbleboard instances previously created won't get broken.
		//
		// BUT the owner user is not kept in the users list if he's not included in the new users list; that way he won't be able to access the instance when being 
		// puhsed out of the curse
		
		// temporal save of old data
		Vector<String> oldUserNames = userNames;
		Vector<String> oldUserPasswords = userPasswords;
		Vector<String> oldUserURLs = userURLs;
		
		// restarting the users "stuff"
		int numberOfUsers = (users == null ? 0 : users.size());
		userNames = new Vector<String>(numberOfUsers);
		userPasswords = new Vector<String>(numberOfUsers);
		userURLs = new Vector<String>(numberOfUsers);
		
		// creation of users and passwords according to the Dabbleboard rules; keeping old users and passwords when are in the new list of users too
		String userI; String userName; int pos;
		long timestamp = System.currentTimeMillis();
		Vector<Future<Response>> results = new Vector<Future<Response>>(numberOfUsers);
		for (int i = 0; i < numberOfUsers; i++){
			userI = users.get(i);
			pos = oldUserPasswords.indexOf(userI);
			if (pos >= 0) { // old user here again
				//System.out.println("** Loyal user: " + userI);	// TODO use a logger
				userNames.add(oldUserNames.get(pos));
				userPasswords.add(userI);
				userURLs.add(oldUserURLs.get(pos));
				
				results.add(null);	// RIGHT CODE: needed to make 'results' and 'users' of the same size (for exception message later)
				
			} else {
				/// new user
				//System.out.println("** New user " + userI);		// TODO use a logger
				userName = userI + "-" + timestamp++;
				userNames.add(userName);
				userPasswords.add(userI);
				userURLs.add(TO_CREATE_MARK +
							 feedURL + "iframe?" + 
								"dev_id=" 			+ devUser + 
								"&user_id=" 		+ userName + 
								"&user_key=" 		+ userI + 
								"&drawing_user_id=" + ownerName + 
								"&drawing_id=" 		+ drawing + 
								"&drawing_key=" 	+ drawingPassword);
				
				// creation of new Dabbleboard user - NO MORE, lazy creation of users, GET INSTANCE will do
			}
		}
		
		//If the callerUser is not included, it is added
		if (users == null || users.indexOf(callerUser) < 0)
		{
			userI = callerUser;
			pos = oldUserPasswords.indexOf(userI);
			if (pos >= 0) { // old user here again
				//System.out.println("** Loyal user: " + userI);	// TODO use a logger
				userNames.add(oldUserNames.get(pos));
				userPasswords.add(userI);
				userURLs.add(oldUserURLs.get(pos));
				
				results.add(null);	// RIGHT CODE: needed to make 'results' and 'users' of the same size (for exception message later)
				
			} else {
				/// new user
				//System.out.println("** New user " + userI);		// TODO use a logger
				userName = userI + "-" + timestamp++;
				userNames.add(userName);
				userPasswords.add(userI);
				userURLs.add(TO_CREATE_MARK +
							 feedURL + "iframe?" + 
								"dev_id=" 			+ devUser + 
								"&user_id=" 		+ userName + 
								"&user_key=" 		+ userI + 
								"&drawing_user_id=" + ownerName + 
								"&drawing_id=" 		+ drawing + 
								"&drawing_key=" 	+ drawingPassword);
				
				// creation of new Dabbleboard user - NO MORE, lazy creation of users, GET INSTANCE will do
			}
		}
		
		// TODO - remove 'lost' users - EXCEPT OWNER!!
		/*
		Set<String> oldUsers = oldUsersAndEntrys.keySet();
		Iterator<String> it = oldUsers.iterator();
		String oldUser;
		while (it.hasNext()) {
			oldUser = it.next();
			if ()
		}
		for (int i = 0; i < listOfUsers.length; i++){
			String userURL = new String(feedURL  + "delete_user?" + "dev_id=" + devUser +
					"&dev_key=" + devPass + "&user_id=" + listOfUsers[i]);
			try {
				post(userURL);
				System.out.println("User: " + listOfUsers[i] + " has been deleted in Dabbleboard");
			} catch (IOException io) {
				throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error in Dabbleboard while deleting user '" + listOfUsers[i] + "'", io);
			}
		}
		*/// TODO - erase lost users here (URLs are lost when leaving the method)
		
		updated = new Date();

	}


}
