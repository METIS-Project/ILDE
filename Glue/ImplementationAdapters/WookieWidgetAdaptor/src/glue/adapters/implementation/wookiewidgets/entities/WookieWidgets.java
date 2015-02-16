/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Wookie Widgets Adapter.
 * 
 * Wookie Widgets Adapter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Wookie Widgets Adapter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.adapters.implementation.wookiewidgets.entities;

import glue.common.entities.instance.InstanceEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.wookie.connector.framework.User;
import org.apache.wookie.connector.framework.WookieConnectorException;
import org.apache.wookie.connector.framework.WookieConnectorService;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;


/**
 * Entity representing a WookieWidget instance.
 * 
 * @author  Carlos Alario
 * @contributor Javier Enrique Hoyos Torio
 * @version 2012092501
 * @package glue.adapters.implementation.wookiewidget.entities
 */
public class WookieWidgets implements InstanceEntity {
	
	/// attributes ///
	
	/** Title */
	protected String title = null;
	
	/** Local identifier */
	protected int index = -1;
	
	/** Date of the instance creation */ 
	protected Date updated;
	
	/** List of users */
	protected Vector<String> userNames = null;
	
	/** URLs to the WookieWidget instance */
	protected Vector<String> userURLs = null;
	
	/** Shared Data Key */
	protected String sharedDataKey = null;
	
	/** GUID */
	protected String guidURL = null;
	
	/** URL to the feed with the WookieWidget Server */
	protected String feedURL = null;	
	
	
	/** GUID depending on the tool*/
	public final static String CHAT_GUID = "http://www.getwookie.org/widgets/chat";
	public final static String SHAREDDRAW_GUID = "http://wookie.apache.org/widgets/sharedraw";
	public final static String SUDOKU_GUID = "http://www.getwookie.org/widgets/sudoku";
	public final static String FORUM_GUID = "http://www.getwookie.org/widgets/forum";
	public final static String NATTER_GUID = "http://www.getwookie.org/widgets/natter";
	public final static String BUBBLE_GUID = "http://www.opera.com/widgets/bubbles";
	public final static String BUTTERFLY_GUID = "http://wookie.apache.org/widgets/butterfly";
	public final static String YOUDECIDE_GUID = "http://www.getwookie.org/widgets/youdecide";

	/** Additional parameters depending on the tool */
	/** YouDecide parameters */
	protected String question = null;
	protected String[] answers = null;
	
	
	/// methods ///
	
	/**
	 * Constructor for a WookieWidget instance.
	 * 
	 * @param	callerUser	String			Current user name (widget creator)
	 * @param	users		List<String>	List of users names
	 * @param 	feed		String  		URL with the feed to send the instance creation request
	 * @param 	guid		String			URL identifying each specific Widget
	 */
	public WookieWidgets(String callerUser, List<String> users, String feed, String guid) {
		int numberOfUsers = (users == null ? 0 : users.size());
		userNames = new Vector<String>(numberOfUsers);	
		userURLs = new Vector<String>(numberOfUsers);	// this will be filled in create(...)
		feedURL = feed;
		guidURL = guid;
		
		//The users list could be null. The users are copied directly
		for (int i = 0; i < numberOfUsers; i++){
			userNames.add(users.get(i));
		}
		
		//If the callerUser is not included, we include it
		if (users == null || users.indexOf(callerUser) < 0)
		{
			userNames.add(callerUser);
		}
		
		sharedDataKey = "key" + "-" + System.currentTimeMillis();
		title = "No title for this instance";
		updated = new Date();
	}
	

	/**
	 * Constructor for a YouDecide Widget instance.
	 * 
	 * @param	callerUser	String			Current user name (widget creator)
	 * @param	users		List<String>	List of users names
	 * @param 	feed		String  		URL with the feed to send the instance creation request
	 * @param 	guid		String			URL identifying each specific Widget
	 * @param	question	String			Configured question in the vote tool
	 * @param	answers		String[]		Configured answers in the vote tool
	 */
	public WookieWidgets(String callerUser, List<String> users, String feed, String guid, String question, String[] answers) {
		
		int numberOfUsers = (users == null ? 0 : users.size());
		userNames = new Vector<String>(numberOfUsers);
		userURLs = new Vector<String>(numberOfUsers);	// this will be filled in create(...)
		feedURL = feed;
		guidURL = guid;
		
		//The users list could be null. The users are copied directly
		for (int i = 0; i < numberOfUsers; i++){
			userNames.add(users.get(i));
		}
		
		//If the callerUser is not included, we include it
		if (users == null || users.indexOf(callerUser) < 0)
		{
			userNames.add(callerUser);
		}
		
		sharedDataKey = "key" + "-" + System.currentTimeMillis();
		title = "No title for this instance";
		updated = new Date();
		this.question = question;
		this.answers = answers;
	}	
	
	
	/**
	 * Constructor for a WookieWidget instance previously saved into a external file.
	 * 
	 * @param	index		int		
	 * @param	title		String
	 * @param 	updated		Date
	 */
	public WookieWidgets(int index, String title, Date updated) {
		this.index = index;
		this.title = title;
		this.updated = updated;
	}
	
	
	/**
	 * Effective creation of a WookieWidget.
	 * 
	 * @param	specificParams			
	 */
	@Override
	public void create(String callerUser, Map<String, String> specificParams) throws ResourceException {
		
		String devKey = specificParams.get("devKey");	// developer key
		
		WookieConnectorService connectorService;
		try {
			connectorService = new WookieConnectorService(feedURL, devKey, this.sharedDataKey);
			
			// Creation of URL for the students
			String userI; User wookieUser;
			for (int i = 0; i < userNames.size(); i++) {
				userI = userNames.get(i);
				wookieUser = new User(userI, userI);
				connectorService.setCurrentUser(wookieUser);
				System.out.println("### URL: " + guidURL);
				userURLs.add(connectorService.getOrCreateInstance(guidURL).getUrl());	
			}
			
			/*	Specific configuration for W3C widgets */
			// Specific configuration for YouDecide Widget
			if (guidURL.equals(WookieWidgets.YOUDECIDE_GUID)){
				connectorService.setPropertyForInstance(connectorService.getOrCreateInstance(guidURL), "setpublicproperty", "moderator", "true");
				connectorService.setPropertyForInstance(connectorService.getOrCreateInstance(guidURL), "setpublicproperty", "question", question);
				connectorService.setPropertyForInstance(connectorService.getOrCreateInstance(guidURL), "setpublicproperty", "answers", answers[0]+"|"+answers[1]+"|"+answers[2]+"|"+answers[3]+"|"+answers[4]);
			}
			
		} catch (WookieConnectorException e) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while creating new instance in Wookie service", e);
			
		} catch (IOException e) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Communication error while creating new instance in Wookie service", e);
		}
		
	}
	
	
	/**
	 * 'Effective' deletion of a Dabbleboard.
	 * 
	 * @return		String		Warning message.
	 */	
	@Override
	public String delete(Map<String, String> specificParams) throws ResourceException {
		return "Widget instance was not actually deleted because of incomplete provider documentation";
	}
	

	@Override
	public String getAccessParams(String callerUser, Map<String, String> specificParams) {
		return null;
	}
	
	@Override
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Getter for browser-friendly URL 

	 * @param 	callerUser	String				Name of the user asking for the URL
	 * @param	params	Map<String, String>		List of specific parameters
	 * @return 							Browser-friendly URL 
	 */
	@Override
	public String getHtmlURL(String callerUser, Map<String, String> specificParams) {
		int pos = userNames.indexOf(callerUser);
		if (pos >= 0)
			return userURLs.get(pos);
		else
			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "User " + callerUser + " is not allowed to get this instance");
	}
	


	@Override
	public void loadSpecificState(BufferedReader in) throws IOException {
		sharedDataKey = in.readLine();
		feedURL = in.readLine();
		guidURL = in.readLine();
		int numberOfUsers = Integer.parseInt(in.readLine());
		userNames = new Vector<String>(numberOfUsers);
		userURLs = new Vector<String>(numberOfUsers);
		for (int i = 0; i < numberOfUsers; i ++) {
			userNames.add(in.readLine());
			userURLs.add(in.readLine());
		}
	}


	@Override
	public void saveSpecificState(PrintStream out) {
		out.println(sharedDataKey);
		out.println(feedURL);
		out.println(guidURL);
		out.println(userNames.size());
		for (int i=0; i<userNames.size(); i++) {
			out.println(userNames.get(i));
			out.println(userURLs.get(i));
		}
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
	 * Getter for local identifier
	 * 
	 * @return	int		Local identifier of the element
	 */
	@Override
	public int getIndex() {
		return index;
	}


	@Override
	public void setUsers(List<String> users, String callerUser, Map<String, String> specificParams) throws ResourceException {
		
		String devKey = specificParams.get("devKey");	// developer key
		
		WookieConnectorService connectorService;
		try {
			// get access to Wookie service
			connectorService = new WookieConnectorService(feedURL, devKey, sharedDataKey);
			
			// temporal save of old data
			Vector<String> oldUserNames = userNames;
			Vector<String> oldUserURLs = userURLs;
			
			// modification of users list
			int numberOfUsers = (users == null ? 0 : users.size());
			userNames = new Vector<String>(numberOfUsers);
			userURLs = new Vector<String>(numberOfUsers);
			
			//The users list could be null. The users are copied directly
			for (int i = 0; i < numberOfUsers; i++){
				userNames.add(users.get(i));
			}
			
			//If the callerUser is not included, we include it
			if (users == null || users.indexOf(callerUser) < 0)
			{
				userNames.add(callerUser);
				numberOfUsers++;
			}
			
			// creation of URLs for the NEW students
			User wookieUser; String userI; String URLI; int pos;
			for (int i = 0; i < numberOfUsers; i++) {
				userI = userNames.get(i);
				wookieUser = new User(userI, userI);
				connectorService.setCurrentUser(wookieUser);
				URLI = connectorService.getOrCreateInstance(guidURL).getUrl();
				userURLs.add(URLI);
				// check if old students keep old urls	// TODO ERASE WHEN CHECKED
				pos = oldUserNames.indexOf(userI);
				if (pos >= 0) {
					if (URLI.equals(oldUserURLs.get(pos)))
						System.out.println("** En efecto; el usuario " + userI + " conserva la entryURL " + URLI);
					else
						System.out.println("--- Pues no; el usuario " + userI + " pasa de " + oldUserURLs.get(pos) + " a " + URLI);
				} else {
					System.out.println("** Usuario nuevo " + userI);
				}
			}
			
			updated = new Date();
			
		} catch (WookieConnectorException e) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while creating new instance in Wookie service", e);
			
		} catch (IOException e) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Communication error while creating new instance in Wookie service", e);
		}

	}
	
}
