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
package glueps.adaptors.vle.mediawiki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Abstract class that implements the most common MediaWiki API operations
 * @author lprisan
 *
 */

public abstract class MWOperator {

	//This is the wiki base URL
	protected URL wikiURL = null;
	protected String wikiCookie = "";
	//The maximum number of retries to create a page
	protected static final int MAX_RETRIES = 25;



	protected boolean mediaWikiAuth(String user, String pass){
		String lgtoken = null;
		String loginURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=login&lgname=" + user + "&lgpassword=" + pass + "&format=xml");
		String loginResponseToken = postToMediaWikiApi(loginURL, this.wikiCookie);
		if (loginResponseToken == null){
			return false;
		}
		System.out.println(loginResponseToken);
		
		// Parse XML response to retrieve the result
		String result = parseMediaWikiXMLResponse(loginResponseToken, "login", "result");

		if(result.equals("NeedToken")){//This is MW 1.15.4 or later
			// We need to confirm the token
			
			//We get the token from the initial login, to be used in the confirmation
			String token = parseMediaWikiXMLResponse(loginResponseToken, "login", "token");
			if(token==null || token.length()==0) return false;
			
			loginURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=login&lgname=" + user + "&lgpassword=" + pass + "&lgtoken="+token+"&format=xml");
			loginResponseToken = postToMediaWikiApi(loginURL, this.wikiCookie);
			if (loginResponseToken == null){
				return false;
			}
			//System.out.println(loginResponseToken);
			result = parseMediaWikiXMLResponse(loginResponseToken, "login", "result");
			/*login = parseMediaWikiXMLResponse(loginResponseToken, "login", "lgtoken");
			if (login == null || login.isEmpty()){
				System.err.println("Error parsing response when authenticating. Check credentials");
				return false;
			}
			return true;*/
			
		}
		//else if(result.equals("Success")){ //In MW 1.15.3 and before, it is simpler
			// Parse XML response to retrieve the lgtoken (login token)
			// node name is "login". Attribute name is "lgtoken"
		if(result.equals("Success")){
			lgtoken = parseMediaWikiXMLResponse(loginResponseToken, "login", "lgtoken");
			if (lgtoken == null || lgtoken.isEmpty()){
				System.err.println("Error parsing response when authenticating. Check credentials");
				return false;
			}
			return true;
		}else return false;//Something wrong happened!
	}

	
	protected boolean addContentIntoWikiPage(String titlepage, String newcontent, String cookie, int section){
		
		try {
			String editToken = null;
			String editTokenURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=query&prop=info|revision&intoken=edit&titles=" + titlepage + "&format=xml");
			String editTokenResponse = postToMediaWikiApi(editTokenURL, cookie);
			if (editTokenResponse == null){
				return false;
			}
			// Parse XML response to retrieve the edittoken (edit token)
			// node name is "page". Attribute name is "edittoken"
			editToken = parseMediaWikiXMLResponse(editTokenResponse, "page", "edittoken");
			if (editToken == null){
				System.err.println("Error parsing response when requesting a token for the edition.");
				return false;
			}

			String editTokenEncoded = URLEncoder.encode(editToken,"UTF-8");

			//Check that the page has not been previously created
			//If the page has been previously created send the user to an error page
			//The error page must be created the first time.
			String pageExists = parseMediaWikiXMLResponse(editTokenResponse, "page", "pageid");
			if (pageExists == null){
				System.err.println("Error obtaining the edit token in MediaWiki");
				return false;
			}

			if (pageExists == ""){
				System.out.println("la pagina no existe - se crear� de todas formas y se a�adir el contenido");
			}
			
			
			
			String  createURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=edit&title=" + titlepage  +
					"&section=new&text="+ newcontent +"&token="+editTokenEncoded+"&format=php");

			String createResponseToken = postToMediaWikiApi(createURL, cookie);

			if (createResponseToken == null){
				return false;
			}
		
			System.out.println("Añadido el texto a: " + new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "index.php/" + titlepage));
			return true;	
			
			

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		
		
	}

	
	protected boolean updateContentIntoWikiPage(String titlepage, String newcontent, String cookie, int section){
		
		try {
			String editToken = null;
			String editTokenURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=query&prop=info|revision&intoken=edit&titles=" + titlepage + "&format=xml");
			String editTokenResponse = postToMediaWikiApi(editTokenURL, cookie);
			if (editTokenResponse == null){
				return false;
			}
			// Parse XML response to retrieve the edittoken (edit token)
			// node name is "page". Attribute name is "edittoken"
			editToken = parseMediaWikiXMLResponse(editTokenResponse, "page", "edittoken");
			if (editToken == null){
				System.err.println("Error parsing response when requesting a token for the edition.");
				return false;
			}

			String editTokenEncoded = URLEncoder.encode(editToken,"UTF-8");

			//Check that the page has not been previously created
			//If the page has been previously created send the user to an error page
			//The error page must be created the first time.
			String pageExists = parseMediaWikiXMLResponse(editTokenResponse, "page", "pageid");
			if (pageExists == null){
				System.err.println("Error obtaining the edit token in MediaWiki");
				return false;
			}

			if (pageExists == ""){
				System.out.println("la pagina no existe - se creará de todas formas y se añadirá el contenido");
			}
			
			
			//We omit the section parameter, to replace the entire page
			//TODO: consider doing this always, normally we do not want to add content!!
			//String  createURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=edit&title=" + titlepage  +
			//		"&section=new&text="+ newcontent +"&token="+editTokenEncoded+"&format=php");
			String  createURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=edit&title=" + titlepage  +
					"&text="+ newcontent +"&token="+editTokenEncoded+"&format=php");

			String createResponseToken = postToMediaWikiApi(createURL, cookie);

			if (createResponseToken == null){
				return false;
			}
		
			System.out.println("A�adido el texto a: " + new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "index.php/" + titlepage));
			return true;	
			
			

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		
		
	}

	

	protected boolean deleteWikiPage(String titlepage, String cookie){
		
		try {
			String deleteToken = null;
			String deleteTokenURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=query&prop=info&intoken=delete&titles=" + titlepage + "&format=xml");
			String deleteTokenResponse = postToMediaWikiApi(deleteTokenURL, cookie);
			if (deleteTokenResponse == null){
				return false;
			}
			// Parse XML response to retrieve the edittoken (edit token)
			// node name is "page". Attribute name is "deletetoken"
			deleteToken = parseMediaWikiXMLResponse(deleteTokenResponse, "page", "deletetoken");
			if (deleteToken == null || deleteToken.length()==0){
				System.err.println("Error parsing response when requesting a token for the deletion.");
				return false;
			}

			String deleteTokenEncoded = URLEncoder.encode(deleteToken,"UTF-8");

			
			String  deleteURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=delete&title=" + titlepage  +
					"&token="+deleteTokenEncoded+"&format=php");

			String deleteResponseToken = postToMediaWikiApi(deleteURL, cookie);

			if (deleteResponseToken == null){
				return false;
			}
		
			System.out.println("Borrada la p�gina: " + new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "index.php/" + titlepage));
			return true;	
			
			

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		
		
	}

	
	protected boolean protectWikiPage(String titlepage, String cookie){
		
		try {
			String protectToken = null;
			String protectTokenURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=query&prop=info&intoken=protect&titles=" + titlepage + "&format=xml");
			String protectTokenResponse = postToMediaWikiApi(protectTokenURL, cookie);
			if (protectTokenResponse == null){
				return false;
			}
			// Parse XML response to retrieve the edittoken (edit token)
			// node name is "page". Attribute name is "deletetoken"
			protectToken = parseMediaWikiXMLResponse(protectTokenResponse, "page", "protecttoken");
			if (protectToken == null || protectToken.length()==0){
				System.err.println("Error parsing response when requesting a token for the protection.");
				return false;
			}

			String protectTokenEncoded = URLEncoder.encode(protectToken,"UTF-8");

			
			String  protectURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=protect&title=" + titlepage  +
					"&protections=edit=sysop|move=sysop&token="+protectTokenEncoded+"&format=php");

			String protectResponseToken = postToMediaWikiApi(protectURL, cookie);

			if (protectResponseToken == null){
				return false;
			}
		
			System.out.println("Protegida la p�gina: " + new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "index.php/" + titlepage));
			return true;	
			
			

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}
		
		
	}

	
	
	
//	protected String postToMediaWikiApi(String urlStr, String RequestCookie) {
//	
//		//Get the list of tools from the GlueletManager
//    	Component component = new Component();
//    	Client client = new Client(Protocol.HTTP);
//    	//Timeout 0 means that timeout is infinite 
//    	client.setConnectTimeout(0);
//    	component.getClients().add(client);
//    	try {
//			component.start();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		ClientResource res = new ClientResource(component.getContext().createChildContext(), urlStr);
//		
//		res.getCookies().add("Cookie", RequestCookie);
//		
//
//			try {
//				Representation response = res.post(new EmptyRepresentation());
//
//				if (res.getStatus().isSuccess()) {
//					
//					Map<String, Object> attrs = res.getResponse().getAttributes();
//					
//					String respString = response.getText();
//					
//					return respString;
//					
//				} else return null;
//			} catch (ResourceException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return null;
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return null;
//			}
//			
//			
//	}
	
	
	protected String postToMediaWikiApi(String urlStr, String RequestCookie) {
		URL url;
		try {
			url = new URL(urlStr);
			
			HttpURLConnection connection;
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Cookie", RequestCookie);
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			out.close();
			
			// Get first cookie
			String headerName=null;
			for (int i=1; (headerName = connection.getHeaderFieldKey(i))!=null; i++) {
			 	if (headerName.equals("Set-Cookie")) {                  
			 		String ResponseCookie = connection.getHeaderField(i);     
			 		ResponseCookie = ResponseCookie.substring(0, ResponseCookie.indexOf(";"));
			        String cookieName = ResponseCookie.substring(0, ResponseCookie.indexOf("="));
			        String cookieValue = ResponseCookie.substring(ResponseCookie.indexOf("=") + 1, ResponseCookie.length());
			        if (cookieName.contains("session")){
			        	this.wikiCookie = cookieName + "=" + cookieValue;
			        	break;
			        }
			 	}
			}
			
			
			// Read Response
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			
			 String thisLine;
			 String response = null;
			 while ((thisLine = in.readLine()) != null ){ // while loop begins here
				 //To avoid some answers wherein first line is an empty line
				 if (!thisLine.equals("")){
					 response = thisLine;
					 break;
				 }
				 thisLine = in.readLine();

			 } // end while
			 
			
			return response;
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	protected String createANewWikiPage(String title, String cookie) throws UnsupportedEncodingException{
		String originalTitle=title;
		String editToken = null;
		title.replace(" ","_");
		String editTokenURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=query&prop=info|revision&intoken=edit&titles=" + title + "&format=xml");
		String editTokenResponse = postToMediaWikiApi(editTokenURL, cookie);
		if (editTokenResponse == null){
			return null;
		}
		// Parse XML response to retrieve the edittoken (edit token)
		// node name is "page". Attribute name is "edittoken"
		editToken = parseMediaWikiXMLResponse(editTokenResponse, "page", "edittoken");
		if (editToken == null){
			System.err.println("Error parsing response when requesting a token for the edition.");
			return null;
		}
		String editTokenEncoded = URLEncoder.encode(editToken,"UTF-8");
		//Check that the page has not been previously created
		//If the page has been previously created send the user to an error page
		//The error page must be created the first time.
		String pageExists = parseMediaWikiXMLResponse(editTokenResponse, "page", "pageid");
		if (pageExists == null){
			System.err.println("Error obtaining the edit token in MediaWiki");
			return null;
		}
		
		
		if (pageExists == ""){
				// Si es menor del limite creamos la p�gina en una sola llamada
				System.err.println("no exsite la p�gina--- la creamos");
				
				//Old version, puts the page title as the first section
				//String  createURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=edit&title=" + title  +
				//		"&section=new&text=%3D"+URLEncoder.encode(originalTitle,"UTF-8")+"%3D&token="+editTokenEncoded+"&format=php");
				//New version, no section or content
				String  createURL = new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "api.php?action=edit&title=" + title  +
					"&section=new&text=&token="+editTokenEncoded+"&format=php");

				String createResponseToken = postToMediaWikiApi(createURL, cookie);

				if (createResponseToken == null){
					return null;
				}
			
			return new String(this.wikiURL + (this.wikiURL.toString().endsWith("/")?"":"/") + "index.php/" + URLEncoder.encode(title,"UTF-8"));	
			
		} else{
			System.err.println("ya existe la p�gina");
			return null;
		}
		
	}

	
	

	
	protected String parseMediaWikiXMLResponse (String response, String node, String attribute){
		String output = null;
		try{
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        InputSource is = new InputSource();
	        is.setCharacterStream(new StringReader(response));
	        Document doc = db.parse(is);
	        //NameList nodeLogin = doc.getElementsByTagName(node);
	        NodeList nodeLogin = doc.getElementsByTagName(node);
	        Element elementLogin = (Element) nodeLogin.item(0);
	        output = elementLogin.getAttribute(attribute);
		}catch(Exception e){
			System.err.println("Error parsing response.");
			return null;
		}
		return output;
	}
	

	
}
