/**
 This file is part of BasicLTIAdapter.

 BasicLTIAdapter is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011-2012 GSIC (UVA).

 BasicLTIAdapter is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use BasicLTIAdapter for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when BasicLTIAdapter is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute BasicLTIAdapter and/or modify it under the
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
package glue.adapters.implementation.basiclti.entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;

import glue.adapters.implementation.basiclti.resources.FormResource;
import glue.common.entities.instance.InstanceEntity;

/**
 * Entity representing a BasicLTI.
 * 
 * @author  	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.adapters.implementation.basiclti.entities
 */
public class BasicLTI implements InstanceEntity {

	// / attributes ///

	/** title */
	protected String title = null;

	/** Description */
	protected String description = null;

	/** Local identifier */
	protected int index = -1;

	/** Date of the instance creation */
	protected Date updated;
	
	protected String htmlURL = null;

	/** Caller User */
	protected String callerUser = null;

	/** URL to the feed with the BasicLTI Server */
	protected String feedURL = null;

	/** Basic LTI tool Resource Key */
	protected String resourceKey;

	/** Basic LTI tool Password */
	protected String resourcePass;

	public BasicLTI(String title, String description, String callerUser, String feed) {
		
		this.feedURL = feed;
		this.updated = new Date();
		try {
			/*this.title = new String(URLEncoder.encode(title, "UTF-8").replace(
					"+", " "));*/
			this.title = title;
			/*this.description = new String(URLEncoder.encode(description,
					"UTF-8").replace("+", " "));*/
			this.description = description;
			this.callerUser = new String(URLEncoder.encode(callerUser, "UTF-8")
					.replace("+", " "));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.err.println("It was not possible to encode the content of the BasicLTI");
		}

	}

	/**
	 * Constructor for a BasicLTI instance previously saved into a external
	 * file.
	 * 
	 * @param index
	 *            int
	 * @param title
	 *            String
	 * @param updated
	 *            Date
	 */
	public BasicLTI(int index, String title, Date updated) {
		this.index = index;
		this.title = title;
		this.updated = updated;
	}
	
	@Override
	public void create(String callerUser, Map<String, String> specificParams) throws ResourceException {
		try {
			//Generate really a unique identifier
			String instanceid = UUID.randomUUID().toString();
			
			//We need to save the instanceid in order to pass it to the getAccessParams method
			specificParams.put("instanceid", instanceid);
			
			resourceKey = specificParams.get("key");
			
			resourcePass = specificParams.get("pass");
			resourcePass = URLEncoder.encode(resourcePass, "UTF-8");
			
			String postURL = feedURL;
			
			String refURL = specificParams.get("refURL");
			
			try {
				URL url = null;
				if (refURL.endsWith("/"))
				{
					url = new URL(refURL + "form?instanceid="+ instanceid);
				}
				else
				{
					url = new URL(refURL + "/form?instanceid="+ instanceid);
				}
				this.htmlURL = url.toString();
				FormResource.saveFormInfo(instanceid, callerUser, resourceKey, resourcePass, postURL, title, description);
			} catch (MalformedURLException e) {
				System.err.println("Malformed URL at Server Application.");
				e.printStackTrace();
			}
			
			/*System.out.println("resourceKey: " + resourceKey);
			System.out.println("resourcePass: " + resourcePass);
			System.out.println("postURL: " + postURL);
			System.out.println("refURL: " + htmlURL);*/
			
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getAccessParams(String callerUser,
			Map<String, String> specificParams) {
		//Parameter needed by delete method
		String instanceid = specificParams.get("instanceid");
		return "instanceid=" + Reference.encode(instanceid);
	}

	@Override
	public String getHtmlURL(String callerUser,
			Map<String, String> specificParams) {
		return htmlURL;
	}

	@Override
	public String delete(Map<String, String> urlDecodedParams){
		//Deletion of the instance file
		String instanceid = urlDecodedParams.get("instanceid");
		FormResource.deleteFormInfo(instanceid);
		return null;
	}

	@Override
	public void setUsers(List<String> users, String callerUser,
			Map<String, String> specificParams) {
		// Nothing to do
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public Date getUpdated() {
		return updated;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setIndex(int index) {
		this.index = index;

	}

	@Override
	public void saveSpecificState(PrintStream out) {
		out.println(htmlURL);
	}

	@Override
	public void loadSpecificState(BufferedReader in) throws IOException {
		htmlURL = in.readLine();
	}

	/* No needed method. Post has been replaced by FormResource 
	 * protected void postToBasicLTI(String urlStr, String data)
			throws IOException {

		try {
			URL url;
			URLConnection urlConnection;
			//DataOutputStream outStream;
			OutputStreamWriter outStream;

			// Create connection
			url = new URL(urlStr);
			urlConnection = url.openConnection();
			((HttpURLConnection) urlConnection).setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			((HttpURLConnection) urlConnection).setInstanceFollowRedirects(false);
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//urlConnection.setRequestProperty("Content-Length", ""+ data.length());
			
			outStream = new OutputStreamWriter(urlConnection.getOutputStream());
			//outStream = new DataOutputStream(urlConnection.getOutputStream());
			
			//System.out.println(data);
			//outStream.writeBytes(data);
			outStream.write(data);
			outStream.flush();
			outStream.close();
			
			HttpURLConnection h = (HttpURLConnection) urlConnection;
			System.out.println(h.getResponseCode());
			System.out.println(h.getResponseMessage());

			// Read Response
			String location = urlConnection.getHeaderField("Location");
			this.htmlURL = location;
			for (int j = 1;; j++) {
				String header = urlConnection.getHeaderField(j);
				if (header == null)
					break;
				System.out.println(urlConnection.getHeaderFieldKey(j) + " "
						+ header);
			}
			
			// String content = (String)urlConnection.getContent();
			//System.out.println(location);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			String line = "";
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			in.close();
		}

		catch (Exception ex) {
			System.out.println("Exception in :\n" + ex.toString());
		}

	}*/

}
