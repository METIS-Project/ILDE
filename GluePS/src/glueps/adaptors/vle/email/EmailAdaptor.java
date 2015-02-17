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
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import glueps.adaptors.vle.IDynamicVLEDeployer;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Course;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.Group;
import glueps.core.model.Participant;

public class EmailAdaptor implements IDynamicVLEDeployer {

	private Properties properties= null;
	protected Map<String, String> parameters;

	public EmailAdaptor() {
		super();
	}
	
	public EmailAdaptor(Properties properties, Map<String, String> parameters){
		
		super();
		this.properties = properties;
		this.parameters = parameters;
	}
	
	@Override
	public HashMap<String, Participant> getCourseUsers(String baseUri,
			String courseId) {
		return getUsers(baseUri);
	}

	@Override
	public HashMap<String, Group> getCourseGroups(String baseUri,
			String courseId) {
		return null;
	}

	
	@Override
	public HashMap<String, String> getCourses(String baseUri) {
		HashMap<String, String> courses = new HashMap<String, String>();
		
		courses.put("0", "List of users for emailing");
		
		return courses;
	}
	
	@Override
	public HashMap<String, String> getCourses(String baseUri, String username) {
		HashMap<String, String> courses = new HashMap<String, String>();
		
		courses.put("0", "List of users for emailing");
		
		return courses;
	}

	@Override
	public HashMap<String, String> getInternalTools() {
		return null;
	}

	@Override
	public HashMap<String, Participant> getUsers(String baseUri) {
		
		//Copied from GLUEPSResource.doGetFromURL
		HttpClient httpclient = new DefaultHttpClient();
		String url = getEmailParticipants();
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

				Course course = (Course) generateObject(content, glueps.core.model.Course.class);

				
				return course.getParticipants();
			} else throw new Exception("GET unsuccessful. Null entity!");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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

	@Override
	public String getToolConfiguration(String toolId) {
		return null;
	}

	@Override
	public Deploy deploy(String baseUri, Deploy lfdeploy) {
		EmailerFactory factory = new EmailerFactory(properties, parameters);
		IEmailer emailer = factory.getEmailer(getEmailMode());
		Deploy newDeploy = null;
		try {
			newDeploy = emailer.batchEmail(lfdeploy);
			return newDeploy;
		} catch (Exception e) {
			e.printStackTrace();
			return lfdeploy;
		}

	}

	@Override
	public Deploy undeploy(String baseUri, Deploy lfdeploy) {
		return null;
	}

	@Override
	public Deploy redeploy(String baseUri, Deploy newDeploy) {
		return (deploy(baseUri, newDeploy));
	}

	
	   
    protected Object generateObject(String xmlfile,
			Class classname) {
		
    	Object o = null;
		//We create the learning environment with the provided XML
		try {
	        JAXBContext jc = JAXBContext.newInstance( classname );
	        Unmarshaller u = jc.createUnmarshaller();
	        o = u.unmarshal(new StringReader(xmlfile));
	        System.out.println( "\nThe unmarshalled objects are:\n" + o.toString());
			return o;
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getEmailParticipants(){
		return properties.getProperty("participants");
    }
	
	private String getEmailMode(){
		return properties.getProperty("mode", "single");
    }

	@Override
	public boolean canBeDeployed(String baseUri, Deploy lfdeploy) {
		return true;
	}

    
	
	
}
