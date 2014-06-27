package glueps.core.resource;

import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Get;

import com.google.gson.Gson;

import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.VLEAdaptorFactory;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Course;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.LearningEnvironmentInstallation;
import glueps.core.model.Participant;
import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.SectokenEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Resource 'anonymous getting of courses'
 * 
 * List of all the courses in a Learning Environment
 * 
 * @author	 	Javier E. Hoyos Torio
 * @version 	2013200300
 * @package 	glueps.core.resources
 */

public class CourseListResource extends GLUEPSResource {
	
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
	
	/**
	 * GET CourseListResource
	 * 
	 * Returns a list with all the courses in a LearningEnvironment. If a courseId is provided, it returns the course instead
	 * 
	 * @return	'Atomized' LearningEnvironment containing its courses or a course itself
	 */
    @Get("xml|html")
    public Representation getCourseList() {
    	
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
    	
   		logger.info("** GET Course List received");
   		Representation answer = null;
   		
		/// Search for parameters in the URL
		Reference ref = getReference();
		Form query = ref.getQueryAsForm();

		String type = query.getFirstValue("type");
		String accessLocation = query.getFirstValue("accessLocation");  // additional URL decoding is not needed!!; getFirstValue(...) applies it
		String creduser = query.getFirstValue("creduser");
		String credsecret = query.getFirstValue("credsecret");
		//The course parameter is not mandatory
		String courseId = query.getFirstValue("course");
		
		//Optional parameters for Moodle
		String version = query.getFirstValue("version");
		String wstoken = query.getFirstValue("wstoken");
		
		/// Checks parameter values
		String missingParameters = "";
		if (type == null || type.length() == 0)
			missingParameters += "type, ";
		if (accessLocation == null || accessLocation.length() == 0) 
			missingParameters += "accessLocation, ";
		if (creduser == null || creduser.length() == 0) 
			missingParameters += "creduser, ";
		if (credsecret == null || credsecret.length() == 0) 
			missingParameters += "credsecret, ";

		if (missingParameters.length() > 0) {
			missingParameters = missingParameters.substring(0, missingParameters.length() - 2);			
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: " + missingParameters);
		}
		
		//Moodle 2.x versions require a wstoken parameter
		if (type.equals("Moodle") && version!=null && version.startsWith("2.") && wstoken==null){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: wstoken");
		}
		
		
		//If a course identifier is provided, we have to get the participants in that course too
		if (courseId!=null){
			
			LearningEnvironment le = null;
			try {
				le = new LearningEnvironment(null, null, type, new URL(accessLocation), null, creduser, credsecret,null);
				if (version!=null && !version.isEmpty() && wstoken!=null && !wstoken.isEmpty()){					
					le.setParameters("version=" + Reference.encode(version) + "&wstoken=" + wstoken);
				}
				LearningEnvironmentResource leResource = new LearningEnvironmentResource();
				leResource.setLearningEnvironment(le);
				leResource.getCompleteLEObject();
			} catch (MalformedURLException e) {
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "The accessLocation provided is not a URL");
			}
						
			HashMap<String,String> courses = le.getCourses();						
			String coursename = courses.get(courseId);
			Course course;
			//TODO For now, we just get course id and name... we could think of getting all the data at some point
			if(coursename != null){
				course = new Course(courseId, coursename, null, null, null);
			}
			else throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "The course does not exist");
			
			VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
			IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(le);
			//Now, we get the users for that course
			HashMap<String,Participant> users = adaptor.getCourseUsers(accessLocation,courseId);
			course.setParticipants(users);			
			
			//We generate the xml on-the-fly and respond with it
			String xmlfile = generateXML(course, glueps.core.model.Course.class);

			if (xmlfile != null){	   		
					answer = new StringRepresentation((CharSequence) xmlfile, MediaType.TEXT_XML);
					answer.setCharacterSet(CharacterSet.UTF_8);
	   		}
	    	
	   		logger.info("** GET Learning Environment course participants answer: \n" + (answer != null ? xmlfile : "NULL"));
	   		
		}else{
			//Otherwise, we return only the course list in that VLE
			LearningEnvironment le = null;
			try {
				le = new LearningEnvironment(null, null, type, new URL(accessLocation), null, creduser, credsecret,null);
				if (version!=null && !version.isEmpty() && wstoken!=null && !wstoken.isEmpty()){					
					le.setParameters("version=" + Reference.encode(version) + "&wstoken=" + wstoken);
				}
				LearningEnvironmentResource leResource = new LearningEnvironmentResource();
				leResource.setLearningEnvironment(le);
				leResource.getCompleteLEObject();
			} catch (MalformedURLException e) {
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "The accessLocation provided is not a URL");
			}
			
			//We generate the xml on-the-fly and respond with it
			String xmlfile = generateXML(le, glueps.core.model.LearningEnvironment.class);

			if (xmlfile != null){	   		
					answer = new StringRepresentation((CharSequence) xmlfile, MediaType.TEXT_XML);
					answer.setCharacterSet(CharacterSet.UTF_8);
	   		}
	    	
	   		logger.info("** GET learning environment courses answer: \n" + (answer != null ? xmlfile : "NULL"));
		}
   		return answer; 	
		
	}


	/**
	 * GET CourseListResource
	 * 
	 * Returns a list with all the courses in a LearningEnvironment. If a courseId is provided, it returns the course instead
	 * 
	 * @return	JSON LearningEnvironment containing its courses or a course itself
	 */
    @Get("json")
    public Representation getJsonCourseList() {
    	
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
    	
   		logger.info("** GET JSON Course List received");
   		Representation answer = null;
   		
		/// Searchs for parameters in the URL
		Reference ref = getReference();
		Form query = ref.getQueryAsForm();

		String type = query.getFirstValue("type");
		String accessLocation = query.getFirstValue("accessLocation");  // additional URL decoding is not needed!!; getFirstValue(...) applies it
		String creduser = query.getFirstValue("creduser");
		String credsecret = query.getFirstValue("credsecret");
		//The course parameter is not mandatory
		String courseId = query.getFirstValue("course");
		
		//Optional parameters for Moodle
		String version = query.getFirstValue("version");
		String wstoken = query.getFirstValue("wstoken");
		
		/// Checks parameter values
		String missingParameters = "";
		if (type == null || type.length() == 0)
			missingParameters += "type, ";
		if (accessLocation == null || accessLocation.length() == 0) 
			missingParameters += "accessLocation, ";
		if (creduser == null || creduser.length() == 0) 
			missingParameters += "creduser, ";
		if (credsecret == null || credsecret.length() == 0) 
			missingParameters += "credsecret, ";

		if (missingParameters.length() > 0) {
			missingParameters = missingParameters.substring(0, missingParameters.length() - 2);			
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: " + missingParameters);
		}
				
		//Moodle 2.x versions require a wstoken parameter
		if (type.equals("Moodle") && version!=null && version.startsWith("2.") && wstoken==null){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: wstoken");
		}
		
		
		//If a course identifier is provided, we have to get the participants in that course too
		if (courseId!=null){
			
			LearningEnvironment le = null;
			try {
				le = new LearningEnvironment(null, null, type, new URL(accessLocation), null, creduser, credsecret,null);
				if (version!=null && !version.isEmpty() && wstoken!=null && !wstoken.isEmpty()){					
					le.setParameters("version=" + Reference.encode(version) + "&wstoken=" + wstoken);
				}
				LearningEnvironmentResource leResource = new LearningEnvironmentResource();
				leResource.setLearningEnvironment(le);
				leResource.getCompleteLEObject();
			} catch (MalformedURLException e) {
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "The accessLocation provided is not a URL");
			}
						
			HashMap<String,String> courses = le.getCourses();						
			String coursename = courses.get(courseId);
			Course course;
			//TODO For now, we just get course id and name... we could think of getting all the data at some point
			if(coursename != null){
				course = new Course(courseId, coursename, null, null, null);
			}
			else throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "The course does not exist");
			
			VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
			IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(le);
			//Now, we get the users for that course
			HashMap<String,Participant> users = adaptor.getCourseUsers(accessLocation,courseId);
			course.setParticipants(users);			
				
			/*HashMap<String,Object> info = new HashMap<String, Object>();
			info.put("learningEnvironment", le);
			info.put("course", course);*/
			
			// convert the java object to JSON format,
			// and return it as a JSON formatted string	
			Gson gson = new Gson();	
			//String json = gson.toJson(info);
			String json = gson.toJson(course);
			
	   		if (json!=null){
	   			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
				answer.setCharacterSet(CharacterSet.UTF_8);
	   		}
	   		
	   		logger.info("** GET JSON LEARNING ENVIRONMENT COURSE PARTICIPANTS answer: \n" + (answer != null ? json : "NULL"));
	   		
		}else{
			//Otherwise, we return only the course list in that VLE
			LearningEnvironment le = null;
			try {
				le = new LearningEnvironment(null, null, type, new URL(accessLocation), null, creduser, credsecret,null);
				if (version!=null && !version.isEmpty() && wstoken!=null && !wstoken.isEmpty()){					
					le.setParameters("version=" + Reference.encode(version) + "&wstoken=" + wstoken);
				}
				LearningEnvironmentResource leResource = new LearningEnvironmentResource();
				leResource.setLearningEnvironment(le);
				leResource.getCompleteLEObject();
			} catch (MalformedURLException e) {
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "The accessLocation provided is not a URL");
			}
			
			// convert the java object to JSON format,
			// and return it as a JSON formatted string
			Gson gson = new Gson();
			//String json = gson.toJson(info);
			String json = gson.toJson(le);
	   		if (json != null){
	   			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
				answer.setCharacterSet(CharacterSet.UTF_8);
	   		}
	   		logger.info("** GET JSON learning environment courses answer: \n" + (answer != null ? json : "NULL"));
		}
   		return answer; 	
		
	}
    

}
