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
package glueps.adaptors.vle.email;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import glueps.core.model.Activity;
import glueps.core.model.Deploy;
import glueps.core.model.Group;
import glueps.core.model.InstancedActivity;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.Participant;
import glueps.core.model.Role;

public class SingleEmailer implements IEmailer {

	private String templateUrl=null;
	private String appUrl=null;
	
	public SingleEmailer(String template, String appUrl){
		
		this.templateUrl=template;
		this.appUrl=appUrl;
	}
	
	
	@Override
	public Deploy batchEmail(Deploy lfdeploy) throws Exception {
		//Extract server parameters from the deploy's LE
		String mailserver = extractMailServer(lfdeploy);
		String username = extractUsername(lfdeploy);
		String password = extractPassword(lfdeploy);
		int port = extractPort(lfdeploy);
		
		String fromEmail = "glueps@gsic.uva.es";
		//We suppose that the credential has the form of an email (might not be so!!!)
		if(EmailValidator.getInstance().isValid(username)) fromEmail = username;
		
		String subject = "Learning Activities for \""+lfdeploy.getName()+"\"";
		

		//Basically, we get the recipient info from the list of participants (we assume the username is the email address!)
		List<String> toEmail = new ArrayList<String>();
		toEmail.add(fromEmail);
		for (Participant p : lfdeploy.getParticipants()) if(EmailValidator.getInstance().isValid(p.getName())){
			toEmail.add(p.getName());
		}
		
		String template = loadTemplate(templateUrl);
		
		try {
			//Send out the email to each participant
			for(String addressee : toEmail){
				//Generate one email content (html) equivalent to the whole deploy
				String htmlBody = template;
				htmlBody = constructGeneralHtmlEmail(htmlBody,lfdeploy,addressee);

				send(mailserver, port, username, password, fromEmail, addressee, subject, htmlBody);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return lfdeploy;
		}
		
		lfdeploy.setLiveDeployURL(lfdeploy.getLearningEnvironment().getAccessLocation());
		
		return lfdeploy;
	}

	
	/**
	 * This method gets an html template and creates an structure that resembles the
	 *  LF deploy. It does not hide activities based on participant, but it does 
	 *  hide activities marked as NOT to be deployed.
	 *  The template has the following placeholders:
	 *  <!--$TITLE--> = The title of the email 
	 *  <!--$DEPLOYDATA--> = General deploy data, normally in the header of the page. Optional
	 *  <!--$ACTIVITIES--> = Activity structure, including activities, instanced activities and instances/resources
	 *  <!--$FOOTER--> = Ending notes, such as the link to the deploy in GLUEPS that originated this email
	 * @param htmlBody the HTML template to be filled in
	 * @param lfdeploy the LF deploy used to create the structure
	 * @return the completed template
	 */
	private String constructGeneralHtmlEmail(String htmlBody, Deploy lfdeploy, String addressee) {
		
		String title = "Learning Activities for \""+lfdeploy.getName()+"\"";
		htmlBody = htmlBody.replace("<!--$TITLE-->", title);
		
		String deploydata = generateDeployDataHtml(lfdeploy);
		htmlBody = htmlBody.replace("<!--$DEPLOYDATA-->", deploydata);
		
		//construct the activities part with the deploy data
		String activities = generateActivitiesHtml(lfdeploy, addressee);
		htmlBody = htmlBody.replace("<!--$ACTIVITIES-->", activities);

		String deployUrl = appUrl+"gui/glueps/deploy.html?deployId="+lfdeploy.getId();
		String footer = "This is an automated email generated with <a href=\"http://gsic.uva.es/glueps\">GLUEPS-AR</a> e-mail learning environment. You can access the deployment interface <a href=\""+deployUrl+"\">here</a>.";
		htmlBody = htmlBody.replace("<!--$FOOTER-->", footer);
		
		return htmlBody;
	}


	private String generateDeployDataHtml(Deploy lfdeploy) {
		
		String content = "";
		if(lfdeploy.getDesign().getObjectives()!=null && lfdeploy.getDesign().getObjectives().size()>0){
			content += "<b>Learning objectives</b><br/>\n<ul>\n";
			for(Iterator<String> it = lfdeploy.getDesign().getObjectives().iterator();it.hasNext();){
				content += "<li>"+it.next()+"</li>\n";
			}
			content += "</ul>\n\n";
		}
		return content;
	}


	private String generateActivitiesHtml(Deploy lfdeploy, String addressee) {
		String content = "";
		
		//TODO All this content should be internationalized!!! For now, we do it in English
		
		if(lfdeploy.getDesign()!=null && lfdeploy.getDesign().getRootActivity()!=null && lfdeploy.getDesign().getRootActivity().getChildrenActivities()!=null){
			
			//We represent the activity tree
			content += "<ul>\n";
			for(Iterator<Activity> it = lfdeploy.getDesign().getRootActivity().getChildrenActivities().iterator(); it.hasNext();){
				Activity activity = it.next();
				
				content += writeActivityNode(activity, lfdeploy, addressee);
				
			}
			content += "</ul>\n";

		}
		return content;
	}


	private String writeActivityNode(Activity activity, Deploy deploy, String addressee) {
		
		String content = "";
		
		if(activity.isToDeploy()){
			//We create a list item for this activity
			content += "<li>";
			content += "<b>"+activity.getName()+"</b>";
			if(activity.getDescription()!=null && activity.getDescription().length()>0) content += ": "+activity.getDescription();
			content += "\n";
				
				if(activity.getChildrenActivities()==null || activity.getChildrenActivities().size()==0){//if it is a leaf node
					
					//add the instanced activities
					HashMap<String,InstancedActivity> instancedActs = deploy.getInstancedActivitiesForActivity(activity.getId());
					if(instancedActs!=null && instancedActs.size()>0){
						content += "<br/>Groups\n";
						content += "<ul>\n";
						
						for(String i : instancedActs.keySet()){
							content += writeInstancedActivityNode(instancedActs.get(i), deploy, addressee);
						}
						
						content += "</ul>\n";
					}
					content += "\n";
				}else{//it is not a leaf node
					content += "<ul>\n";
					//Add children activities
					for(Iterator<Activity> it = activity.getChildrenActivities().iterator(); it.hasNext();){
						
						content += writeActivityNode(it.next(), deploy, addressee);
						
					}
					content += "</ul>\n";
				}			
	
			content += "</li>\n";
		}
		return content;
		
	}


	
	
	private String writeInstancedActivityNode(
			InstancedActivity instancedActivity, Deploy deploy, String addressee) {

		String groupid = instancedActivity.getGroupId();
		Group group = deploy.findGroupById(groupid);
		String content = "";
		
		if(group!=null){
			content += "<li>"+group.getName();
			if(group.getParticipantIds()!=null && group.getParticipantIds().size()>0){
				content += " "+deploy.getGroupParticipantNamesAsString(group);
			}
			
			int totalItems = 0;
			if(instancedActivity.getResourceIds()!=null) totalItems += instancedActivity.getResourceIds().size();
			if(instancedActivity.getInstancedToolIds()!=null) totalItems += instancedActivity.getInstancedToolIds().size();
			
			if(totalItems>0){
				content += "<ul>\n";
				//We add the resources of this iAct
				if(instancedActivity.getResourceIds()!=null && instancedActivity.getResourceIds().size()>0){
					for(String resId : instancedActivity.getResourceIds()) content += "<li><a href=\""+deploy.getDesign().getResourceById(resId).getLocation()+"\" target=\"_blank\">"+deploy.getDesign().getResourceById(resId).getName()+"</a></li>\n";
				}
				
				//We add the tool instances of this iAct
				if(instancedActivity.getInstancedToolIds()!=null && instancedActivity.getInstancedToolIds().size()>0){
					//TODO We should use the addressee for the callerUser parameter!! by now, we put the deploy's username (from the LE credentials)
					for(String instId : instancedActivity.getInstancedToolIds()) content += "<li><a href=\""+deploy.getToolInstanceById(instId).getLocationWithRedirects(deploy)+"?callerUser="+addressee+"\" target=\"_blank\">"+deploy.getToolInstanceById(instId).getName()+"</a></li>\n";
				}
				
				content += "</ul>\n";
			}
			content += "</li>\n";
			
		}
		
		
		return content;
	}


	private String loadTemplate(String url) throws Exception{
		//Copied from GLUEPSResource.doGetFromURL
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = null;
		HttpEntity entity = null;
		try {
			System.out.println((new Date()).toString()+" - Trying to GET "+url);
			
			response = httpclient.execute(httpget);
			
			int rc = response.getStatusLine().getStatusCode();

			entity = response.getEntity();
     
			if (rc != 200) {
				throw new Exception("GET unsuccessful. Returned code "+rc);
			}
				
			if (entity != null) {

				String content = EntityUtils.toString(entity, "UTF-8");
				System.out.println((new Date()).toString()+" - Got response from server: "+content);

				return content;
			} else throw new Exception("GET unsuccessful. Null entity!");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(entity!=null){
				try {
					EntityUtils.consume(entity);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(httpget!=null) httpget.abort();
			if(httpclient!=null) httpclient.getConnectionManager().shutdown();
		}
		
	}


	private int extractPort(Deploy lfdeploy) {
		LearningEnvironment le = lfdeploy.getLearningEnvironment();
		URL server = le.getAccessLocation();
		return server.getPort();
	}

	private String extractPassword(Deploy lfdeploy) {
		LearningEnvironment le = lfdeploy.getLearningEnvironment();
		return le.getCredsecret();
	}

	private String extractUsername(Deploy lfdeploy) {
		LearningEnvironment le = lfdeploy.getLearningEnvironment();
		return le.getCreduser();
	}

	private String extractMailServer(Deploy lfdeploy) {
		LearningEnvironment le = lfdeploy.getLearningEnvironment();
		URL server = le.getAccessLocation();
		return server.getHost();
	}

	private void send(String mailserver, int port, String username, String password, String fromEmail, String addressee, String subject, String htmlBody) throws EmailException{
			
		try {
			HtmlEmail email = new HtmlEmail();
			email.setCharset(org.apache.commons.mail.EmailConstants.UTF_8);
			email.setHostName(mailserver);
			
			//For gsic email, we do not need the @gsic.uva.es part
			username = username.substring(0,username.lastIndexOf("@"));
			//TODO: address this more generally!
			
			email.setAuthentication(username, password);
			email.setSmtpPort(port);
			email.setFrom(fromEmail);
			
			email.addTo(addressee);
			
			email.setSubject(subject);

			//email.setTextMsg(null);
			email.setHtmlMsg(htmlBody);

			//Possibly needed
			//email.setStartTLSEnabled(true);
			email.setSSL(true);
			
			email.setDebug(true);

			String mimeid = email.send();
			System.out.println("Successfully sent mail: "+mimeid);
		} catch (EmailException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e2){
			e2.printStackTrace();
			throw new EmailException(e2.getMessage());
		}
		
		
	}
	
	
	
}
