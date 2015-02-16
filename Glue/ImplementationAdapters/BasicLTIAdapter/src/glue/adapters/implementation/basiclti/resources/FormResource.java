/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Basic LTI Adapter.
 * 
 * Basic LTI Adapter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Basic LTI Adapter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.adapters.implementation.basiclti.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthUtil;

import glue.adapters.implementation.basiclti.entities.Base64Coder;
import glue.common.resources.GLUEResource;

/**
 * Form resource
 * 
 * @author  	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.adapters.implementation.basiclti.resources
 */
public class FormResource extends GLUEResource{
	
	/**
	 * Directory where the instance files are going to be saved
	 */
	protected static final String DIRNAME = "instances";
	
	/**
	 * Saves to a file the information needed to create the post form
	 * @param instanceid Identifier of the instance whose information need to be saved
	 * @param userId Identifier of the user who creates the instance
	 * @param resourceKey Key Oauth consumer key
	 * @param resourcePass Secret Oauth consumer secret
	 * @param postUrl URL where the POST has to be done
	 * @return
	 */
	public static boolean saveFormInfo(String instanceid, String userId, String resourceKey, String resourcePass, String postUrl, String title, String description) {
		boolean saved = true;
        FileOutputStream file = null;
		//Create the dir if necessary
		File dir = new File(DIRNAME);
		if (!dir.exists())
		{
			dir.mkdir();
		}
        try
        {
            file = new FileOutputStream(DIRNAME + "/" + instanceid + ".txt");
            PrintStream pe = new PrintStream(file);
            pe.println(userId);
            pe.println(resourceKey);
            pe.println(resourcePass);
            pe.println(postUrl);
            pe.println(title);
            pe.println(description);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
        	   if (null != file)
        		   file.close();
        	   else
        		   saved = false;
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
        return saved;
	}
	
	/**
	 * Deletes the file
	 * @param instanceid Identifier of the instance to delete
	 *
	 */
	public static boolean deleteFormInfo(String instanceid) {
		File f = new File(DIRNAME + "/" + instanceid + ".txt");
		return f.delete();
	}
	
	/**
	 * Creates the post form and returns it
	 * 
	 */
	@Get()
	public Representation createForm() {
		//Get the instanceid value
		Reference ref = getReference();
		Form query = ref.getQueryAsForm();
		String instanceid = query.getFirstValue("instanceid");
		
		/// Checks parameter values
		String missingParameters = "";
		if (instanceid == null || instanceid.length() == 0)
			missingParameters += "instanceid, ";
		if (missingParameters.length() > 0) {
			missingParameters = missingParameters.substring(0, missingParameters.length() - 2);			
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: " + missingParameters);
		}
		
		String userId = null;
		String resourceKey = null;
		String resourcePass = null;
		String postUrl = null;
		String title = null;
		String description = null;
		
		BufferedReader in = null;		
		try {
			in = new BufferedReader(new FileReader(DIRNAME + "/" + instanceid + ".txt"));
			userId = in.readLine();
			resourceKey = in.readLine();
			resourcePass = in.readLine();
			postUrl = in.readLine();
			title = in.readLine();
			description = in.readLine();
			
			
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing instance");
		}
		finally{
			//Cerramos el fichero
			try{                    
				if( in !=null ){   
					in.close();     
				}                  
			}catch (Exception e2){ 
				e2.printStackTrace();
			}
		}
			
			//POST parameters
		Map<String, String> data = new HashMap<String, String>();			
		data.put("oauth_version", "1.0");
		data.put("oauth_nonce", OAuthUtil.getNonce());
		data.put("oauth_timestamp", OAuthUtil.getTimestamp());
		data.put("oauth_consumer_key", resourceKey);
		data.put("resource_link_id", instanceid);
		data.put("resource_link_title", title);
		data.put("resource_link_description", description);
	
		data.put("user_id", userId);
		data.put("roles", "Learner");
		data.put("context_id", "1");
		data.put("context_label", "Default course");
		data.put("context_title", "Default course");
		data.put("launch_presentation_locale", "es_es");
		data.put("launch_presentation_document_target", "iframe");
		data.put("oauth_callback", "about:blank");
		data.put("lti_version", "LTI-1p0");
		data.put("lti_message_type", "basic-lti-launch-request");
		data.put("oauth_signature_method", "HMAC-SHA1");
		data.put("ext_submit", "Press to launch this activity");
			
		String signature_base_string = null;
		try{
			//Generation of the signature base string
			signature_base_string = OAuthUtil.getSignatureBaseString(postUrl, "POST", data);
			}
		catch (OAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * Sign the request
		 */
		try {
			Mac m = Mac.getInstance("HmacSHA1");
			String signkey = resourcePass + "&";
			//System.out.println("signkey: " + signkey);
			m.init(new SecretKeySpec(signkey.getBytes(), "HmacSHA1"));
			m.update(signature_base_string.getBytes());
			byte[] res = m.doFinal();
			String sig = String.valueOf(Base64Coder.encode(res));
			//The signature is added to the post parameters
			data.put("oauth_signature", sig);
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String form = formString(postUrl, data);
	        
	    StringRepresentation rep = new StringRepresentation((CharSequence) form, MediaType.TEXT_HTML);
		return rep;
	}
	
	/**
	 * Creates the javascript code needed to post the form
	 * @param postUrl URL of the action field
	 * @param data Input fields
	 * @return the form
	 */
	private String formString(String postUrl, Map<String, String> data)
	{
	    String form = "<form action=\""+ postUrl +"\" name=\"ltiLaunchForm\" id=\"ltiLaunchForm\" method=\"post\" encType=\"application/x-www-form-urlencoded\">\n";

	    Set keys = data.keySet();
	    Iterator keyIter = keys.iterator();
		for (int i = 0; keyIter.hasNext(); i++) {
			Object key = keyIter.next();
	        if ( key.equals("ext_submit" )) {
	        	form += "<input type=\"submit\" name=\"";
	        } else {
	        	form += "<input type=\"hidden\" name=\"";
	        }
	        form += key;
	        form += "\" value=\"";
	        form += data.get(key);
	        form += "\"/>\n";
	    }
		form += "</form>\n";

		form += " <script type=\"text/javascript\"> \n" +
	    //"  //<![CDATA[ \n" +
	    "    document.getElementById(\"ltiLaunchForm\").style.display = \"none\";\n" +
	    "    nei = document.createElement('input');\n" +
	    "    nei.setAttribute('type', 'hidden');\n" +
	    "    nei.setAttribute('name', '"+ "ext_submit" + "');\n" +
	    "    nei.setAttribute('value', '" + "Press to launch this activity" + "');\n" +
	    "    document.getElementById(\"ltiLaunchForm\").appendChild(nei);\n" +
	    "    document.ltiLaunchForm.submit(); \n" +
	    //"  //]]> \n" +
	    " </script> \n";
		
		//System.out.println(form);
		return form;
	}
	
}
