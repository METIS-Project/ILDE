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
package glueps.core.resource;

import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.VLEAdaptorFactory;
import glueps.adaptors.vle.blogger.BloggerAdaptor;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Course;
import glueps.core.model.Deploy;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.Participant;
import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.OauthTokenEntity;
import glueps.core.persistence.entities.SectokenEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class OauthResource extends GLUEPSResource {

	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
	
	private String oauthClientId;
	private String oauthClientSecret;

    @Override
    protected void doInit() throws ResourceException {
    	GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
    	oauthClientId = app.getOauthGoogleClientid();
    	oauthClientSecret = app.getOauthGoogleClientsecret();
    }
    

    @Get()
    public Representation getOauth() {    	
    	GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
    	if (app.getLdShakeMode()==true && this.getRequest().getChallengeResponse()==null){
    		//Check the sectoken
    		Reference ref = getReference();
    		Form query = ref.getQueryAsForm();
    		String sectoken = query.getFirstValue("sectoken");
    		String deployId = query.getFirstValue("deployId");
    		if (sectoken == null){
    			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken parameter is missing");
    		}else if (deployId == null){
    			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The deployId parameter is missing");
    		}
    		JpaManager dbmanager = JpaManager.getInstance();
    		SectokenEntity ste = dbmanager.findSectokenById(deployId);
    		if (ste==null || !ste.getSectoken().equals(sectoken)){
    			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken is not valid for this deploy");
			}
		}
    	
    	logger.info("** GET OAUTH received");
   		Representation answer = null;
   		
		/// Search for parameters in the URL
		Reference ref = getReference();
		Form query = ref.getQueryAsForm();
		
		String redirectUri = query.getFirstValue("redirectUri");
		String checktoken = query.getFirstValue("checktoken");
		String parameters = query.getFirstValue("parameters");
		String code = query.getFirstValue("code");
		
		  	    	
    	if (checktoken!=null && checktoken.length()>0){
			String leId = trimId(checktoken);
			JpaManager dbmanager = JpaManager.getInstance();
			OauthTokenEntity oauthToken = dbmanager.findOauthTokenByLeid(leId);
			if (oauthToken == null){
				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "Oauth authentication required: an access token is necessary");
			}else{
				long expirationTime = oauthToken.getExpiration();
				long currentTime = new Date().getTime();
				if (currentTime >= expirationTime){
					dbmanager.deleteOauthToken(String.valueOf(oauthToken.getId()));
					throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "Oauth authentication required: the access token has expired");
				}else{
					String accessToken = oauthToken.getAccessToken();
					//Check the access token is valid
					String userId = BloggerAdaptor.retrieveBloggerUserId(accessToken);
					if (userId == null){
						dbmanager.deleteOauthToken(String.valueOf(oauthToken.getId()));
						throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "Oauth authentication required: the current access token is not valid");
					}
				}
			}
			answer = new StringRepresentation((CharSequence)"The current access token is OK", MediaType.TEXT_ALL);
			return answer;
    	}	//If the code is not provided, we have to get it. So we return the oauth url
    	else if (redirectUri!=null && redirectUri.length()>0 && (code == null || code.length() == 0)){
    		String missingParameters = "";
    		if (parameters == null || parameters.length() == 0){
    			missingParameters += "parameters, ";
    		}
    		if (missingParameters.length() > 0) {
    			missingParameters = missingParameters.substring(0, missingParameters.length() - 2);			
    			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: " + missingParameters);
    		}
	        String formOauthUrl = formOauthUrl("https://accounts.google.com/o/oauth2/auth", "https://www.googleapis.com/auth/blogger.readonly https://www.googleapis.com/auth/blogger", redirectUri, parameters);
			answer = new StringRepresentation((CharSequence) formOauthUrl, MediaType.TEXT_ALL);
			answer.setCharacterSet(CharacterSet.UTF_8);	
			return answer;
    	}
    	
		/// Checks parameter values
		String missingParameters = "";
		if (redirectUri == null || redirectUri.length() == 0){
			missingParameters += "redirectUri, ";
		}
		if (parameters == null || parameters.length() == 0){
			missingParameters += "parameters, ";
		}
		if (code == null || code.length() == 0){
			missingParameters += "code, ";
		}
		if (missingParameters.length() > 0) {
			missingParameters = missingParameters.substring(0, missingParameters.length() - 2);			
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: " + missingParameters);
		}
		try {
			Map<String, String> param = OauthResource.decodeParams(parameters);
			String callerMethod = param.get("callerMethod");
			if (callerMethod!=null && (callerMethod.equals("impdep") || callerMethod.equals("newdep") || callerMethod.equals("postdep"))){				
				String result = doPostOauth("https://accounts.google.com/o/oauth2/token", code, redirectUri);
				Gson gson = new Gson();
				JsonElement element = gson.fromJson(result, JsonElement.class);
				JsonObject jobj = element.getAsJsonObject();
				String accessToken = jobj.get("access_token").getAsString();
				String expiresIn = jobj.get("expires_in").getAsString();//the lifetime of the token in seconds (not in milliseconds!)
				String tokenType = jobj.get("token_type").getAsString();
				
				String leId = trimId(param.get("leId"));
				OauthTokenEntity oauthToken = new OauthTokenEntity(Long.parseLong(leId), "Bearer " + accessToken);
				oauthToken.setExpiration(oauthToken.getCreated() + Long.parseLong(expiresIn) * 1000);
				oauthToken.setTokenType(tokenType);
				JpaManager dbmanager = JpaManager.getInstance();
				dbmanager.insertOauthToken(oauthToken);
				return answer;
			}else if(callerMethod!=null && callerMethod.equals("newle")){				
				String result = doPostOauth("https://accounts.google.com/o/oauth2/token", code, redirectUri);
				Gson gson = new Gson();
				JsonElement element = gson.fromJson(result, JsonElement.class);
				JsonObject jobj = element.getAsJsonObject();
				String accessToken = jobj.get("access_token").getAsString();
				String expiresIn = jobj.get("expires_in").getAsString();//the lifetime of the token in seconds (not in milliseconds!)
				String tokenType = jobj.get("token_type").getAsString();
				
				String leId = trimId(param.get("leId"));
				OauthTokenEntity oauthToken = new OauthTokenEntity(Long.parseLong(leId), "Bearer " + accessToken);
				oauthToken.setExpiration(oauthToken.getCreated() + Long.parseLong(expiresIn) * 1000);
				oauthToken.setTokenType(tokenType);
				JpaManager dbmanager = JpaManager.getInstance();
				dbmanager.insertOauthToken(oauthToken);
				
				String userId = BloggerAdaptor.retrieveBloggerUserId("Bearer " + accessToken);
				JsonObject userObj = new JsonObject();
				userObj.addProperty("userid", userId);
				answer = new StringRepresentation((CharSequence)userObj.toString(), MediaType.APPLICATION_JSON);
				answer.setCharacterSet(CharacterSet.UTF_8);	
				return answer;
			}
		} catch (ResourceException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Oauth error");
		}catch (Exception e){
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while parsing the Oauth response");
		}
		return answer;
	}
    
	/**
	 * Decodes a string with adapter-specific parameters
	 * 
	 * @return	Map<String, String>		Set of name-value pairs with the decoded parameters. 
	 */
	public static Map<String, String> decodeParams(String parameters){
		HashMap<String, String> result = new HashMap<String, String>();
		if (parameters != null) {
			StringTokenizer tokenizer = new StringTokenizer(parameters, "&");
			while (tokenizer.hasMoreElements()) {
				String token = tokenizer.nextToken();
				int pos = token.indexOf("=");
				if (pos > 0) {
					result.put(token.substring(0, pos), Reference.decode(token.substring(pos+1)));
				}
			}
		}
		return result;
	}
    
    
    private String formOauthUrl(String url, String scope, String redirectUri, String parameters){
    	String responseType = "code"; //For installed applications, use a value of code, indicating that the Google OAuth 2.0 endpoint should return an authorization code.
    	Form form = new Form();
    	form.add("scope", scope);
    	form.add("redirect_uri", redirectUri);
    	form.add("response_type",responseType);
    	form.add("client_id", oauthClientId);
    	form.add("state", parameters);
    	String oauthUrl = "";
		try {
			oauthUrl = url + "?" + form.getWebRepresentation().getText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return oauthUrl;
    }
    
    
	private String doPostOauth(String url, String code, String redirectUri) throws Exception {
		Status status = null;	
		Representation response = null;
		
		ClientResource remoteResource = new ClientResource(url);
		
		/* Create a Client with the socketTimout parameter for HttpClient and "attach"
		   it to the ClientResource. Of course we could just use the Client
		   on its own. */
		Context context = new Context();
		//context.getParameters().add("socketTimeout", "1000");
		Client c = new Client(context,Protocol.HTTPS);
		remoteResource.setNext(c);
		// Set the client to not retry on error. Default is true with 2 attempts.
		remoteResource.setRetryOnError(false);	
		
		Form form = new Form();
		form.add("code", code);
		form.add("client_id", oauthClientId);
		form.add("client_secret",oauthClientSecret);
		form.add("redirect_uri",redirectUri);
		form.add("grant_type", "authorization_code");
		Representation rep = form.getWebRepresentation();
		
		try{
			remoteResource.post(rep, MediaType.APPLICATION_WWW_FORM);
			status = remoteResource.getStatus();
			response = remoteResource.getResponseEntity();			
			if (status.equals(Status.SUCCESS_OK)){
				return response.getText();
			}else if (status.equals(Status.CLIENT_ERROR_NOT_FOUND)){
				return null;
			}
		} catch (ResourceException e) {
			if (remoteResource.getStatus().equals(Status.CLIENT_ERROR_NOT_FOUND) ){
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Post to: " + url);
			}else{
				if (remoteResource.getStatus().equals(Status.CLIENT_ERROR_BAD_REQUEST) ){
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Oauth error");
				}
			}
			return null;
		} catch (IOException e) {
			return "";
		}
		finally{
			remoteResource.release();
			try {
				//We must stop the client. Otherwise, the thread stands open
				c.stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		discardRepresentation(response);
		return null;
	}
 

}
