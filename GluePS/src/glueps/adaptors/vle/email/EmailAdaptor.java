package glueps.adaptors.vle.email;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
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

	private GLUEPSManagerApplication app=null;
	private Properties properties= null;

	public EmailAdaptor() {
		super();
	}
	
	public EmailAdaptor(GLUEPSManagerApplication app, Properties properties){
		
		super();
		this.app=app;
		this.properties = properties;
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
		EmailerFactory factory = new EmailerFactory(app, properties);
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

    
	
	
}
