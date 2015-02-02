/**
 This file is part of NoteFlightAdapter.

 NoteFlightAdapter is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 NoteFlightAdapter is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use NoteFlightAdapter for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when NoteflightAdapter is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute NoteFlightAdapter and/or modify it under the
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
package glue.adapters.implementation.noteflight.entities;

import glue.common.entities.instance.InstanceEntity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;
import net.oauth.http.HttpClient;

import org.restlet.resource.ResourceException;


/**
 * Entity representing a Dabbleboard.
 * 
 * @author  Carlos Alario
 * @version 2011020800
 * @package glue.adapters.implementation.dabbleboard.entities
 */
public class Noteflight implements InstanceEntity {

	/// attributes ///

	/** title */
	protected String title = null;

	protected String htmlURl = null;

	/** Local identifier */
	protected int index = -1;

	/** Date of the instance creation */ 
	protected Date updated;



	/** URL to the feed with the Dabbleboard */
	protected String feedURL = null;

	private String userKey = null;
	private String userSecret = null;



	/// methods ///

	/**
	 * Constructor for a Dabbleboard.
	 * 
	 * @param	callerUser	String			Username of the creating user; owner of the dabbleboard
	 * @param 	users		List<String> 	List of users names 
	 * @param 	feed		String  		URL with the feed to send the instance creation request
	 * @param	devUser		String			Developer username 
	 */

	public Noteflight(String callerUser, List<String> users, String feed, String userKey, String userSecret) {


		this.userKey = userKey;
		this.userSecret = userSecret;
		feedURL = feed;

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
	public Noteflight(int index, String title, Date updated) {
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

		if (userKey!=null && !userKey.equals("") && 
				userSecret!=null && !userSecret.equals("") && 
				feedURL!=null && !feedURL.equals("")){


				String viewScoreURL = sendNoteflightCreateRequest(
						// consumer key
						userKey,
						// consumer secret
						userSecret,
						// url
						feedURL + "scores/create",
						null,
						// body data
						""
						);
				
				if (viewScoreURL!=null && !viewScoreURL.equals("")){
					this.htmlURl = viewScoreURL;
				}

		//	}

		}
	}




	


	public String sendNoteflightCreateRequest(String consumerKey, String consumerSecret, String url, Entry<String,String> noteglightSessionCookie, String data) {
		
		
		OAuthConsumer consumer = new OAuthConsumer(
				url,
				consumerKey,
				consumerSecret,
				new OAuthServiceProvider(null, null, null)		
				);

		OAuthAccessor accessor = new OAuthAccessor(consumer);

		try {

			OAuthMessage request = new OAuthMessage(
					HttpClient.POST, 
					url, 
					null 
					);
			/**
			 * resource_link_id=120988f929-274612&
			 * resource_link_title=GsicEmic+Noteflight+Adapter&
			 * resource_link_description=An+implementation+tool+adapter+for+Noteflight.com+online+music+edit+application&
			 */
	        String resourcelinkID = "120988f929-274612";
	        request.addParameter("resource_link_id", resourcelinkID);
	        String resourcelinkTitle = "GsicEmic Noteflight Adapter";
	        request.addParameter("resource_link_title", resourcelinkTitle);
	        String resourcelinkDescription= "An+implementation tool adapter for Noteflight.com online music edit application";
	        request.addParameter("resource_link_description", resourcelinkDescription);
	        
	        
	        /**
	         * user_id=292832126&
	         * roles=Instructor&
	         * lis_person_name_full=Intelligent+%26+Cooperative+Systems+Research+Group+%28GSIC%29.&
	         * lis_person_name_family=GSIC-EMIC&
	         * lis_person_name_given=GSIC_EMIC&
	         * lis_person_contact_email_primary=javieraragon%40gsic.uva.es&
	         * lis_person_sourcedid=gsic.uva.es%3Auser&
	         */
	        String userId= "292832126";
	        request.addParameter("user_id", userId);
	        
	        String roles= "Instructor";
	        request.addParameter("roles", roles);
	        
	        String personNameFull= "Intelligent & Cooperative Systems Research Group (GSIC).";
	        request.addParameter("lis_person_name_full", personNameFull);	
	        
	        String personNameFamily= "GSIC-EMIC";
	        request.addParameter("lis_person_name_family", personNameFamily);
	        
	        String personNamegiven= "GSIC_EMIC";
	        request.addParameter("lis_person_name_given", personNamegiven);
	        
	        String personContactEmail= "javieraragon@gsic.uva.es";
	        request.addParameter("lis_person_contact_email_primary", personContactEmail);
	        
	        String personSourceId= "gsic.uva.es:user";
	        request.addParameter("lis_person_sourcedid", personSourceId);
	        
	        

	        /**
	         * context_id=456434513&	
	         * context_title=Design+of+Personal+Environments&
	         * context_label=SI182&
	         * tool_consumer_instance_guid=lmsng.school.edu&
	         * tool_consumer_instance_description=University+of+School+%28LMSng%29&
	         */
	        String contextID= "456434513";
	        request.addParameter("context_id", contextID);	
	        
	        String contextTitle= "Design of Personal Environments";
	        request.addParameter("context_title", contextTitle);
	        
	        String contextLabel= "SI182";
	        request.addParameter("context_label", contextLabel);
	        
	        String toolConsumerGUID= "gsic.uva.es";
	        request.addParameter("tool_consumer_instance_guid", toolConsumerGUID);
	        
	        String toolConsumerDescription= "GSIC_EMIC, University of Valladolid, Spain";
	        request.addParameter("tool_consumer_instance_description", toolConsumerDescription);

	        /**
	        * oauth_callback=about%3Ablank&
	        * lis_outcome_service_url=http%3A%2F%2Fwww.imsglobal.org%2Fdevelopers%2FBLTI%2Ftool_consumer_outcome.php&
	        * lis_result_sourcedid=feb-123-456-2929%3A%3A28883&
	        * lti_version=LTI-1p0&
	        * lti_message_type=basic-lti-launch-request&
	        * ext_submit=Press+to+Launch&
	        * oauth_signature_method=HMAC-SHA1&
	        * oauth_signature=9EyjVWs3c%2BsQASEqSaop0osCaNc%3D";
	        */
	        // MANDATORY FIELDS
	        String oauthCallback =  "about:blank";
	        request.addParameter("oauth_callback",oauthCallback);
	        
	        String ltiVersion= "LTI-1p0";
	        request.addParameter("lti_version", ltiVersion);
	        
	        String submit ="Press to Launch";
	   		request.addParameter("ext_submit", submit);
	        
	        String ltiMessageType= "basic-lti-launch-request";
	        request.addParameter("lti_message_type", ltiMessageType);
	        // EOF MANDATORY FIELDS
	        
	        String lis_outcome_service= "http://www.imsglobal.org/developers/BLTI/tool_consumer_outcome.php";
	        request.addParameter("lis_outcome_service_url", lis_outcome_service);
	        
	        String lisResultSourceId= "feb-123-456-2929::28883";
	        request.addParameter("lis_result_sourcedid", lisResultSourceId);
			
			
			request.addRequiredParameters(accessor);
	          
			System.out.println(request.URL);
			request.sign(accessor);

			List<Entry<String,String>> parameterlist1 = request.getParameters();
			Iterator<Entry<String,String>> it1 = parameterlist1.iterator();
			String form1 = "";
			while(it1.hasNext()){
				Entry<String,String> item = (Entry<String, String>) it1.next();
				System.out.println(item.getKey() + "= " + item.getValue());
				form1 = form1 + item.getKey() + "=" + URLEncoder.encode(item.getValue(),"UTF-8") + "&";
			}

			System.out.println(form1);
			return excutePost(url, form1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static String excutePost(String targetURL, String urlParameters)
	{


		URL url;
		HttpURLConnection connection = null;  
		try {
			//Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded");

			
			//connection.setRequestProperty("Cookie","_noteflightSite_session=BAh7CDoMY3NyZl9pZCIlMzU0NGQ0YTA4MDc2MWM4OTA4ZTdmNDc5NjkwMDg0%0AZTE6CWhhc2h7BzoPY3JlYXRlZF9hdEl1OglUaW1lDSrsG4AVw%2Fi4BjofQG1h%0AcnNoYWxfd2l0aF91dGNfY29lcmNpb25GOhRsYXN0X3Zpc2l0ZWRfYXRJdTsI%0ADSrsG4DR4wO5BjsJRiIKZmxhc2hJQzonQWN0aW9uQ29udHJvbGxlcjo6Rmxh%0Ac2g6OkZsYXNoSGFzaHsABjoKQHVzZWR7AA%3D%3D--854b63c3b7a845a73f7b375ae2a5a0d7ba80df07; path=/");

			String timestamp = Long.toString(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
			System.out.println(timestamp);
			/**
			String bodyContent = 
					"oauth_version=1.0&" +
					"oauth_nonce=f590d75e63e2139a91783d429fd82ce4&" +
					"oauth_timestamp=1323079913&" +
					"oauth_consumer_key=alario&" +
					"resource_link_id=120988f929-274612&" +
					"resource_link_title=GsicEmic+Noteflight+Adapter&" +
					"resource_link_description=An+implementation+tool+adapter+for+Noteflight.com+online+music+edit+application&" +
					"user_id=292832126&" +
					"roles=Instructor&" +
					"lis_person_name_full=Intelligent+%26+Cooperative+Systems+Research+Group+%28GSIC%29.&" +
					"lis_person_name_family=GSIC-EMIC&" +
					"lis_person_name_given=GSIC_EMIC&" +
					"lis_person_contact_email_primary=javieraragon%40gsic.uva.es&" +
					"lis_person_sourcedid=gsic.uva.es%3Auser&" +
					"context_id=456434513&" +
					"context_title=Design of Personal Environments&" +
					"context_label=SI182" +
					"&tool_consumer_instance_guid=gsic.uva.es&" +
					"tool_consumer_instance_description=GSIC_EMIC%2C+University+of+Valladolid%2C+Spain" +
					"&oauth_callback=about:blank" +
					"&lis_outcome_service_url=http%3A%2F%2Fwww.imsglobal.org%2Fdevelopers%2FBLTI%2Ftool_consumer_outcome.php&" +
					"lis_result_sourcedid=feb-123-456-2929%3A%3A28883&" +
					"lti_version=LTI-1p0&" +
					"lti_message_type=basic-lti-launch-request&" +
					"ext_submit=Press to Launch&" +
					"oauth_signature_method=HMAC-SHA1&" +
					"oauth_signature=Gx4RUp%2Bf1g2NAZZ3IgpjIXRTNUM%3D";
					*/
			
			
			connection.setRequestProperty("Content-Length", "" + 
					Integer.toString(urlParameters.getBytes().length));

			connection.setDoOutput(true);

			connection.setUseCaches (false);
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);


			//Send request
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream ());
			wr.writeBytes (urlParameters);
			wr.flush ();
			wr.close ();



			//Get Response	
			//connection.connect();
			//InputStream is = null;

			System.out.println("Response code = " + connection.getResponseCode());
			String header = connection.getHeaderField("Location");
			//System.out.println(connection.getHeaderField("Cookie"));
			//System.out.println(connection.getHeaderField("Set-Cookie"));
			System.out.println("LOG: " + header);
			if (header != null)
				return header;



			return "";

		} catch (Exception e) {

			e.printStackTrace();
			return null;

		} finally {

			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}




	/**
	 * Deletion of a Dabbleboard.
	 * 
	 * @param	specificParams 
	 * @return					String			Always null
	 */	
	@Override
	public String delete(Map<String, String> specificParams) throws ResourceException {
		// find parameters
		
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
		
		System.out.println(this.feedURL);
		System.out.println(this.userKey);
		System.out.println(this.userSecret);
		return htmlURl;
	}


	/**
	 * Getter for local identifier
	 * 
	 * @return	int		Local identifier of the element
	 */
	public int getIndex() {
		return index;
	}



	@Override
	public String getAccessParams(String callerUser, Map<String, String> specificParams) {
		String devUser = specificParams.get("user");
		String pass = specificParams.get("pass");
		return "user=" + devUser + "&pass=" + pass;
	}


	/**
	 * 
	 */
	@Override
	public void loadSpecificState(BufferedReader in) throws IOException {
		feedURL = in.readLine();

	}


	@Override
	public void saveSpecificState(PrintStream out) {
		out.println(feedURL);
	}


	@Override
	public void setIndex(int index) {
		this.index = index;
	}


	@Override
	public void setUsers(List<String> users, String callerUser,
			Map<String, String> specificParams) {
		// nothing to do
	}




}
