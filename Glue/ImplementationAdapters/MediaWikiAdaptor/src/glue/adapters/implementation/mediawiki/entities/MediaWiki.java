/**
 This file is part of MediaWikiAdapter.

 MediaWikiAdapter is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 MediaWikiAdapter is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use MediaWikiAdapter for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when MediaWikiAdapter is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute MediaWikiAdapter and/or modify it under the
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
package glue.adapters.implementation.mediawiki.entities;

import glue.common.entities.instance.InstanceEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Entity representing a MediaWiki instance.
 * 
 * @author  Carlos Alario
 * @version 2012092501
 * @package glue.adapters.implementation.mediawiki.entities
 */
public class MediaWiki implements InstanceEntity {

	/// attributes ///
	
	/** Title */
	protected String title = null;
	
	/** Content */
	protected String content = null;
	
	/** URL to the MediaWiki instance */
	protected String entryURL = null;
		
	/** Local identifier */
	protected int index = -1;
	
	/** Date of the instance creation */ 
	protected Date updated;
	
	/** URL to the feed with the MediaWiki Server */
	protected String feedURL = null;	
	
	/** Cookie shared with MediaWiki */
	protected String cookie = "";
	
	/// methods ///
	
	/**
	 * Constructor for a MediaWiki instance.
	 * 
	 * @param 	title		String		Title of the MediaWiki page.
	 * @param 	feed		string  URL with the feed to send the instance creation request 
	 * TODO Change this number for the name of the users userName[]. These names are used in the user creation in WookieWidget instance
	 */
	
	public MediaWiki(String pageTitle, String pageContent, String feed) {
		feedURL = feed;
		title = pageTitle.replace(" ", "_"); //Necessary to create a new page. 
		updated = new Date();
		try {
			content = new String(URLEncoder.encode(pageContent, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.err.println("It was not possible to encode the content of the MediaWiki page");
		}
	}
	
	
	/**
	 * Constructor for a MediaWiki instance previously saved into a external file.
	 * 
	 * 
	 * @param	title			String
	 * 
	 */
	public MediaWiki(int index, String title, Date updated) {
		this.index = index;
		this.title = title;
		this.updated = updated;
	}


	/**
	 * Creation of a MediaWiki.
	 * 
	 * @param	specificParams 
	 */
	@Override
	public void create(String callerUser, Map<String, String> specificParams) throws ResourceException {
		// find parameters
		String devUser = specificParams.get("user");
		String devPass = specificParams.get("pass");
		
		// Authentication with MediaWiki
		String loginURL = new String(feedURL + "/api.php?action=login&lgname=" + devUser + "&lgpassword=" + devPass + "&format=xml");
		try {
			String loginResponse = postToWiki(loginURL, cookie);
			// Parse XML response to retrieve the lgtoken (login token); node name is "login", Attribute name is "lgtoken"
			String loginResult = parseXMLResponse(loginResponse, "login", "result");
			if (loginResult!=null && loginResult.equals("NeedToken")) {
				// confirm login - step added in MediaWiki 1.15.3
				String confirmationToken = parseXMLResponse(loginResponse, "login", "token");
				String loginConfirmationURL = new String(feedURL + "/api.php?action=login&lgname=" + devUser + "&lgpassword=" + devPass + "&format=xml&lgtoken=" + confirmationToken);
				loginResponse = postToWiki(loginConfirmationURL, cookie);
				
			} else if (loginResult == null || !loginResult.equals("Success")) {
				throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Authentication with MediaWiki failed (result='" + loginResult + "'). Check credentials");	// TODO Should responde with CLIENT_ERROR_UNAUTHORIZED?
			}
			
			String loginToken = parseXMLResponse(loginResponse, "login", "lgtoken");
			if (loginToken == null || loginToken.length() <= 0)
				throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Authentication with MediaWiki failed (lgToken never received). Check credentials");	// TODO Should responde with CLIENT_ERROR_UNAUTHORIZED?
			
		} catch (IOException io) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Authentication with MediaWiki failed. Check credentials", io);	// TODO Should responde with CLIENT_ERROR_UNAUTHORIZED?
			
		} catch (SAXException sax) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while parsing the authentication response. Check credentials", sax);
			
		} catch (ParserConfigurationException pce) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unexpected error while trying to parse the authentication response", pce);
		}
		
		// Create the page in MediaWiki
		// According to http://www.mediawiki.org/wiki/API:Edit_-_Create%26Edit_pages/es
        // it is necessary to get an "Edit Token"
		String editToken = null;
		String editTokenURL = new String(feedURL + "/api.php?action=query&prop=info|revisions&intoken=edit&titles=" + title + "&format=xml");
		String editTokenResponse;
		try {
			editTokenResponse = postToWiki(editTokenURL, cookie);
			// Parse XML response to retrieve the edittoken (edit token); node name is "page", attribute name is "edittoken"
			editToken = parseXMLResponse (editTokenResponse, "page", "edittoken");
			
		} catch (IOException io) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while obtaining an edit token from MediaWiki", io);
			
		} catch (SAXException sax) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while parsing the edit token from MediaWiki", sax);
			
		} catch (ParserConfigurationException pce) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unexpected error while trying to parse an edit token from MediaWiki", pce);
			
		}
			
		
		String editTokenEncoded;
		try {
			editTokenEncoded = URLEncoder.encode(editToken,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unexpected error while encoding edit token", e);
		}
				
		//Check that the page has not been previously created
		//If the page has been previously created send the user to an error page
		//The error page must be created the first time.
		String pageExists;
		try {
			pageExists = parseXMLResponse(editTokenResponse, "page", "pageid");
			if (pageExists != "")
				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "Page already exists");
			
		} catch (IOException io) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while checking the existance of the new page in MediaWiki", io);
			
		} catch (SAXException sax) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while checking the existance of the new page in MediaWiki", sax);
			
		} catch (ParserConfigurationException pce) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unexpected error while checking the existance of the new page in MediaWiki", pce);
		}
		
		// Really create the page in MediaWiki
		String createURL = new String(feedURL + "/api.php?action=edit&title=" + title + "&section=new&text=" + content + "&token="+editTokenEncoded+"&format=php");
		try {
			/*String createResponse = */postToWiki(createURL, cookie);
		} catch (IOException io) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected error while creating new page in MediaWiki", io);
		}
		// URL with the new page
		entryURL = new String(feedURL + "/index.php/" + title);
	}
	
	
	/**
	 * Deletion of a MediaWiki page.
	 * 
	 * @param	specificParams 
	 * @return					String			Always null
	 */	
	@Override
	public String delete(Map<String, String> specificParams) throws ResourceException {
		// Create the page in MediaWiki
		// According to http://www.mediawiki.org/wiki/API:Edit_-_Create%26Edit_pages/es
        // it is necessary to get an "Edit Token"
		String deleteToken = null;
		String deleteTokenURL = new String(feedURL + "/api.php?action=query&prop=info&intoken=delete&titles=" + title + "&format=xml");
		try {
			String deleteTokenResponse = postToWiki(deleteTokenURL, cookie);
			// Parse XML response to retrieve the deletetoken (delete token); node name is "page", attribute name is "deletetoken"
			deleteToken = parseXMLResponse(deleteTokenResponse, "page", "deletetoken");
			
		} catch (IOException io) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while obtaining a delete token from MediaWiki", io);
			
		} catch (SAXException sax) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while parsing a delete token from MediaWiki", sax);
			
		} catch (ParserConfigurationException pce) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected error while trying to parse a delete token from MediaWiki", pce);
		}
		
		String deleteTokenEncoded;
		try {
			deleteTokenEncoded = URLEncoder.encode(deleteToken,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unexpected error while encoding the delete token", e);
		}

		// Delete a page in MediwWiki
		try {
			String  deleteURL = new String(feedURL + "/api.php?action=delete&title=" + title + "&token="+deleteTokenEncoded+"&format=php"); 
			postToWiki(deleteURL, cookie);
		} catch (IOException io) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected error while deletig page in MediaWiki", io);
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
		cookie = in.readLine();
		feedURL = in.readLine(); 	// It is necessary to store the feed URL
		entryURL = in.readLine();
		
	}
	
	@Override
	public void saveSpecificState(PrintStream out) {
		out.println(cookie);
		out.println(feedURL);
		out.println(entryURL);
	}

	
	protected String postToWiki(String urlStr, String requestCookie) throws IOException {
		URL url;
		url = new URL(urlStr);
		URLConnection connection;
		connection = url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Cookie", requestCookie);
		connection.connect();
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		out.close();
			
		// Get first cookie
		String headerName=null;
		for (int i=1; (headerName = connection.getHeaderFieldKey(i))!=null; i++) {
		 	if (headerName.equals("Set-Cookie")) {                  
		 		String responseCookie = connection.getHeaderField(i);     
		 		responseCookie = responseCookie.substring(0, responseCookie.indexOf(";"));
		        String cookieName = responseCookie.substring(0, responseCookie.indexOf("="));
		        String cookieValue = responseCookie.substring(responseCookie.indexOf("=") + 1, responseCookie.length());
		        if (cookieName.contains("session")){
		        	cookie = cookieName + "=" + cookieValue;
		        	break;
		        }
		 	}
		}
			
		// Read Response
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String response = in.readLine();
		return response;
	}
	
	
	protected String parseXMLResponse (String response, String node, String attribute) throws ParserConfigurationException, SAXException, IOException {
		String output = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(response));
		Document doc = db.parse(is);
		NodeList nodeLogin = doc.getElementsByTagName(node);
		Element elementLogin = (Element) nodeLogin.item(0);
		output = elementLogin.getAttribute(attribute);
		return output;
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
