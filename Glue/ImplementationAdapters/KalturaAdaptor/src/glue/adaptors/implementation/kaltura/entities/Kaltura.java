/**
  This file is part of KalturaToolAdapter.

 KalturaToolAdapter is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011-2012 GSIC (UVA).

 KalturaToolAdapter is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use KalturaToolAdapter for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when KalturaToolAdapter is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute KalturaToolAdapter and/or modify it under the
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
package glue.adaptors.implementation.kaltura.entities;


import glue.common.entities.instance.InstanceEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import com.kaltura.client.KalturaApiException;
import com.kaltura.client.KalturaClient;
import com.kaltura.client.KalturaConfiguration;
import com.kaltura.client.enums.KalturaEntryType;
import com.kaltura.client.enums.KalturaSessionType;
import com.kaltura.client.types.KalturaBaseEntry;



/**
 * Entity representing a document to be created with one of the Kaltura tool.
 * 
 * Keeps a video file to post to kaltura service.
 * 
 * @author  David A. Velasco <davivel@gsic.uva.es>
 * @version 2012092501
 * @package glue.adaptors.implementation.gdata3.entities
 */
public class Kaltura implements InstanceEntity {

	/// class attributes ///
	/** Helper for date conversion from Google 'DateFormat' */
	protected static final SimpleDateFormat dateTimeFormat822 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
	
	
	/// attributes ///
	private Date updated = new Date();
	private String title = "";
	private File file = null; 
	private String kalturaEntryId = null;
	/** Local identifier */
	protected int index = -1;
	/** Browser-friendly URL to the Google Doc */
	protected String htmlURL = null;
	

	/// methods ///
	
	/** 
	 * Constructor for creation of an empty kaltura video.
	 * 
	 * @param 	title	String	Title of the document.
	 * @param	doctype	String	GData category string identifying the type of document.
	 */
	public Kaltura() {

	}
	

	
	/**
	 * Constructor for an already existing Kaltura viudeo.
	 * 
	 * @param 	index		int		
	 * @param	title		String
	 * @param	updated		Date
	 */
	public Kaltura(int index, String title, Date updated) {
		this.index = index;
		this.title = title;
		this.updated = updated;
	}
	
	/**
	 * Constructor for upload of an existing document as a new Kaltura video.
	 * 
	 * @param 	title		String		Title of the video.
	 * @param 	file 		File 		Accesor to the existing document in the file system.
	 */
	public Kaltura(String title, File file) {
		this.title = title;
		this.file = file;
	}
	
	
	/**
	 * Effective creation of a Kaltura intance.
	 */
	@Override
	public void create(String callerUser, Map<String, String> specificParams) throws ResourceException {
		try {
			
			int partnerID = Integer.parseInt(specificParams.get("partnerId"));
			String kalturaSecret = specificParams.get("secret");
			String userId = specificParams.get("userId");
			
			
			KalturaClient client = getKalturaClient(partnerID, userId, kalturaSecret, true);
			
			String entryId = addEntry(client,this.file.getPath(),this.title);

			
			this.kalturaEntryId = entryId;
	        this.htmlURL = "http://www.kaltura.com/kwidget/wid/_37182/uiconf_id/1466342/entry_id/" + entryId;
	        
			
		} catch (KalturaApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private KalturaClient getKalturaClient(int partnerId, String userId, String secret, boolean isAdmin) throws KalturaApiException
	{       
		KalturaConfiguration config = new KalturaConfiguration();
		config.setPartnerId(partnerId);
		config.setEndpoint("http://www.kaltura.com/");
		KalturaClient client = new KalturaClient(config);

		String ks = client.getSessionService().start(secret, userId, (isAdmin ? KalturaSessionType.ADMIN : KalturaSessionType.USER));
		client.setSessionId(ks);
		return client;
	}
	
	
	private String addEntry(KalturaClient client, String fileName, String entryName) throws KalturaApiException
	{
	        // create a new USER-session client                       
	        // upload the new file and recieve the token that identifies it on the kaltura server
	        File up = new File(fileName);
	        String token = client.getBaseEntryService().upload(up);
	        // create a new entry object with the required meta-data
	        KalturaBaseEntry entry = new KalturaBaseEntry();
	        entry.name = entryName;
	        entry.type = KalturaEntryType.MEDIA_CLIP;
	        // add the entry you created to the kaltura server, by attaching it with the uploaded file
	        KalturaBaseEntry newEntry = client.getBaseEntryService().addFromUploadedFile(entry, token);
	        //KalturaBaseEntry newEntry = client.getBaseEntryService().add(entry);
	        // newEntry now contains the information of the new entry that was just created on the server
	        return newEntry.id;
	}
	

	
	/**
	 * Effective deletion of a Klatura intance.
	 * 
	 * @param	specificParams
	 * @return  String	Always null 
	 */
	@Override
	public String delete(Map<String, String> specificParams) throws ResourceException {
		
		try {
			
			int partnerID = Integer.parseInt(specificParams.get("partnerId"));
			String kalturaSecret = specificParams.get("secret");
			String userId = specificParams.get("userId");
			
			
			KalturaClient client = getKalturaClient(partnerID, userId, kalturaSecret, true);
			deleteEntry(client, this.kalturaEntryId);
			
		} catch (KalturaApiException e) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while deleting remote resource in Kaltura", e);
		}
		return null;
	}
	
	private String deleteEntry(KalturaClient client, String id) throws KalturaApiException
	{
		if (id!=null && !id.equals("")){
			try {
				client.getBaseEntryService().delete(id);
				return null;
			} catch (KalturaApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Delete request failed because worng kaltura id");
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
	 * Getter for browser-friendly URL to the document.
	 * 
	 * @param 	callerUser	String					Name of the user asking for the URL
	 * @param	params		Map<String, String>		List of specific parameters
	 * @return 										Browser-friendly URL 
	 */
	@Override
	public String getHtmlURL(String callerUser, Map<String, String> specificParams) {
		return htmlURL;
	}


	/**
	 * Getter for local identifier
	 * 
	 * @return	int		Local identifier of instance
	 */
	public int getIndex() {
		return index;
	}
	


	
	
	/**
	 * Getter for browser-friendly URL to the instance.
	 * 
	 * @return Browser-friendly URL to the instance.
	 */
	public String getHtmlURL() {
		return htmlURL;
	}


	@Override
	public void saveSpecificState(PrintStream out) {
		out.println(htmlURL);;
		out.println(kalturaEntryId);
	}


	@Override
	public void loadSpecificState(BufferedReader in) throws IOException {
		htmlURL = in.readLine();
		kalturaEntryId = in.readLine();
	}

	
	@Override
	public void setIndex(int index) {
		this.index = index; 
	}


	@Override
	public void setUsers(List<String> users, String callerUser, Map<String, String> specificParams) {
		// nothing to do
	}


	@Override
	public String getTitle() {
		return null;
	}


	@Override
	public String getAccessParams(String callerUser, Map<String, String> specificParams) {
		String partnerId = specificParams.get("partnerId");
		String kalturaSecret = specificParams.get("secret");
		String userId = specificParams.get("userId");
		return "partnerId=" + Reference.encode(partnerId) + "&secret=" + Reference.encode(kalturaSecret) + "&userId=" + Reference.encode(userId);
	}
	
}
