/**
 This file is part of GDataAdapterApi3.

 GDataAdapterApi3 is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GDataAdapterApi3 is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GDataAdapterApi3 for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GDataAdapterApi3 is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GDataAdapterApi3 and/or modify it under the
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
package glue.adapters.implementation.gdata3.entities;

import glue.adapters.implementation.gdata3.manager.GDataAdapterServerMain;

import com.google.gdata.client.docs.DocsService;
import com.google.gdata.client.media.MediaService;
import com.google.gdata.util.AuthenticationException;


/**
 * Manager for getting access to Google Docs services.
 * 
 * @author  David A. Velasco
 * @contributor Javier Enrique Hoyos Torio
 * @version 2012092501
 * @package glue.adapters.implementation.gdata3.entities
 */

public class GDataServiceManager {

	
	/// attributes ///
	
	/** Interface with Google Documents List service */
	protected static DocsService docsService = null;
	
	/** Constant to identify documents */
	public final static String DOC_TYPE = "document";
	
	/** Constant to identify spreadsheets */
	public final static String SPR_TYPE = "spreadsheet";
	
	/** Constant to identify presentations */
	public final static String PRE_TYPE = "presentation";
	
	/** Constant to identify drawing */
	public final static String DRA_TYPE = "drawing";

	
	
	/// methods ///

	/** 
	 * Getter for the interface object with Google Documents List Service.
	 * 
	 * Initializes the interface if it hasn't been yet.
	 * 
	 *  @param	user	String		Google docs username
	 *  @param	pass	String 		Google docs password 
	 */
	public static DocsService getDocsService(String user, String pass) {
		if (docsService == null) {
			initDocsService();
		}
		
		/// authentication
		try {
			docsService.setUserCredentials(user, pass);
		} catch (AuthenticationException ae) {
			System.err.println("*** Google Authentication FAIL !!!***");	// TODO ordered logging
			ae.printStackTrace();
			docsService = null;
		}
		
		//System.out.println("** right authentication with Google **");		// TODO ordered logging

		// simple test to check the actual version //
		/// again, should be kept as a reference
		/*
		try {
			URL feedUri = new URL("http://docs.google.com/feeds/documents/private/full");		// URL 2.0 - our target 
			//URL feedUri = new URL("https://docs.google.com/feeds/default/private/full/");		// URL 3.0
			DocumentListFeed feed = docsService.getFeed(feedUri, DocumentListFeed.class);
			
		} catch (MalformedURLException ue) {
			System.err.println("You must be idiot... ");
			return;

		} catch (ServiceException se) {
			System.err.println("getFeed didn't work, error retrieving feed ");
			se.printStackTrace();
			
		} catch (IOException se) {
			System.err.println("getFeed didn't work, error sending request or reading feed");
			
		}
		System.out.println("*** GET was right ***");
		*/

		return docsService;	// if initDocsService fails, this will be null
	}

	
	/**
	 * Initialize Documents List service.
	 * 
	 * Protocol version 2.0 is enforced (default in Java library is 3.0). 2.0 is the last graduated (stable) version, 3.0 is in labs (in progress).
	 * 
	 * For more info about versions compare:
	 *		http://code.google.com/intl/es-ES/apis/documents/docs/2.0/developers_guide_protocol.html#Versioning
	 *  	http://code.google.com/intl/es-ES/apis/documents/docs/3.0/developers_guide_protocol.html#Versioning
	 */
	protected static void initDocsService() {
		/// version setting
		/// next commented code about version setting should be kept as a reference
		
		// test 1 - doesn't work
		/* docsService = new DocsService(GDataAdapterServerMain.appName);
		 * docsService.getRequestFactory().setHeader("GData-Version", "2.0");
		 */
			
		/// test 2 - THIS IS THE RIGHT WAY ; easy, isn't it?
		docsService = new DocsService(GDataAdapterServerMain.APP_NAME);
		docsService.setProtocolVersion(DocsService.Versions.V3);
		docsService.setChunkedMediaUpload(MediaService.NO_CHUNKED_MEDIA_REQUEST);		// TODO make it optional with an init option
			
		// test 3 - it looks like the best option; BUT IT DOESN'T WORK
		/*GoogleGDataRequest.Factory requestFactory = new GoogleGDataRequest.Factory();
		requestFactory.setHeader("GData-Version", "2.0");
		
		docsService = new DocsService(GDataAdapterServerMain.appName, requestFactory, null);
	    GoogleAuthTokenFactory authTokenFactory = new GoogleAuthTokenFactory(	DocsService.DOCS_SERVICE,
	    																		GDataAdapterServerMain.appName,
	    																		"https",
	    																		"www.google.com",
	    																		docsService );
		docsService.setAuthTokenFactory(authTokenFactory);*/
		
		/*
		System.out.println("** service protocol version is " + docsService.getProtocolVersion());	// THIS VALUE IS THE KEY FOR ADJUSTING Documents List VERSION
		System.out.println("** service version is " + docsService.getServiceVersion() + "(depens upon library version)"); 
		System.out.println("** current version of Gdata core protocol is " + DocsService.getVersion() + " (static)");
		*/
		
	}

	/**
	 * Gets the value of an attribute in the query part of an URL.  
	 * @param 	url			URL to search in.
	 * @param 	attName		Name of the target attribute in the query part of URL. 
	 * @return				Value of the target attribute 
	 */
	protected static String getURLAttribute(String url, String attName) { 			
		int pos = url.indexOf("?" + attName);
		if (pos < 0)
			pos = url.indexOf("&" + attName);
		if (pos < 0)
			return null;
		int pos2 = url.indexOf("&", pos + 1);
		if (pos2 < 0)
			pos2 = url.length();
		return url.substring(pos + attName.length() + 2, pos2);	// + 2 because of "?" before and "&" after attName
	}

}
