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
import glueps.core.gluepsManager.GLUEPSManagerServerMain;
import glueps.core.model.AsynchronousOperation;
import glueps.core.model.Course;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.LearningEnvironmentInstallation;
import glueps.core.model.Participant;
import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.SectokenEntity;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
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
    public Representation getRequestCourseList(){
    	GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
    	if (app.isResponseAsAService()){
    		return getCourseListService();
    	}else{
    		return getCourseList();
    	}   
	}
	
	/**
	 * GET CourseListResource
	 * 
	 * Returns a list with all the courses in a LearningEnvironment. If a courseId is provided, it returns the course instead
	 * 
	 * @return	'Atomized' LearningEnvironment containing its courses or a course itself
	 */
    private Representation getCourseList() {
    	
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
		
		boolean showAR = Boolean.valueOf(query.getFirstValue("showAR")).booleanValue();
		boolean showVG = Boolean.valueOf(query.getFirstValue("showVG")).booleanValue();
		
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
				le = new LearningEnvironment(null, null, type, new URL(accessLocation), null, creduser, credsecret,null,showAR,showVG);
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
			else throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "The course does not exist or you do not have the necessary permissions to get its participants.");
			
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
				le = new LearningEnvironment(null, null, type, new URL(accessLocation), null, creduser, credsecret,null,showAR,showVG);
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
	 * @return	'Atomized' LearningEnvironment containing its courses or a course itself
	 */
    private Representation getCourseListService() {
    	
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
		
		boolean showAR = Boolean.valueOf(query.getFirstValue("showAR")).booleanValue();
		boolean showVG = Boolean.valueOf(query.getFirstValue("showVG")).booleanValue();
		
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
		AsynchronousOperation asynOper;
		//If a course identifier is provided, we have to get the participants in that course too
		if (courseId!=null){
			asynOper = new AsynchronousOperation(null, AsynchronousOperation.generateOperationId(), AsynchronousOperation.STATUS_INPROGRESS, "The course information is being obtained", null, new Date(), null);
			long asynOperId;
			try{
				JpaManager dbmanager = JpaManager.getInstance();
				asynOperId = dbmanager.insertAsynchronousOperation(asynOper);
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the asynchronous operation into the DB", e);
			}
			Future<Course> process = GLUEPSManagerServerMain.pool.submit(new BackgroundGetCourse(type, accessLocation, creduser, credsecret, showAR, showVG, version, wstoken, courseId, app, asynOperId, asynOper.getOperation(), true));			
		}else{
			asynOper = new AsynchronousOperation(null, AsynchronousOperation.generateOperationId(), AsynchronousOperation.STATUS_INPROGRESS, "The course list information is being obtained", null, new Date(), null);
			long asynOperId;
			try{
				JpaManager dbmanager = JpaManager.getInstance();
				asynOperId = dbmanager.insertAsynchronousOperation(asynOper);
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the asynchronous operation into the DB", e);
			}
			Future<LearningEnvironment> process = GLUEPSManagerServerMain.pool.submit(new BackgroundGetCourseList(type, accessLocation, creduser, credsecret, showAR, showVG, version, wstoken, app, asynOperId, asynOper.getOperation(), true));		
		}
		URLifyAsynchronousOperation(asynOper, doGluepsUriSubstitution(getReference().getParentRef().getIdentifier()));
   		String xmlfile = generateXML(asynOper, glueps.core.model.AsynchronousOperation.class);	
   		if (xmlfile != null){
   			answer = new StringRepresentation((CharSequence)xmlfile, MediaType.TEXT_XML);
   			answer.setCharacterSet(CharacterSet.UTF_8);
   		}				
		getResponse().setLocationRef(asynOper.getOperation());
		answer.setCharacterSet(CharacterSet.UTF_8);
		setStatus(Status.SUCCESS_ACCEPTED);
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
    public Representation getRequestJsonCourseList() {
    	GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
    	if (app.isResponseAsAService()){
    		return getJsonCourseListService();
    	}else{
    		return getJsonCourseList();
    	}
    }


	/**
	 * GET CourseListResource
	 * 
	 * Returns a list with all the courses in a LearningEnvironment. If a courseId is provided, it returns the course instead
	 * 
	 * @return	JSON LearningEnvironment containing its courses or a course itself
	 */
    private Representation getJsonCourseList() {
    	
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
		
		boolean showAR = Boolean.valueOf(query.getFirstValue("showAR")).booleanValue();
		boolean showVG = Boolean.valueOf(query.getFirstValue("showVG")).booleanValue();
		
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
				le = new LearningEnvironment(null, null, type, new URL(accessLocation), null, creduser, credsecret,null,showAR,showVG);
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
			else throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "The course does not exist or you do not have the necessary permissions to get its participants.");
			
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
				le = new LearningEnvironment(null, null, type, new URL(accessLocation), null, creduser, credsecret,null,showAR, showVG);
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
    
	/**
	 * GET CourseListResource
	 * 
	 * Returns a list with all the courses in a LearningEnvironment. If a courseId is provided, it returns the course instead
	 * 
	 * @return	JSON LearningEnvironment containing its courses or a course itself
	 */
    private Representation getJsonCourseListService() {
    	
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
		
		boolean showAR = Boolean.valueOf(query.getFirstValue("showAR")).booleanValue();
		boolean showVG = Boolean.valueOf(query.getFirstValue("showVG")).booleanValue();
		
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
		
		AsynchronousOperation asynOper;		
		//If a course identifier is provided, we have to get the participants in that course too
		if (courseId!=null){		
			asynOper = new AsynchronousOperation(null, AsynchronousOperation.generateOperationId(), AsynchronousOperation.STATUS_INPROGRESS, "The course information as a JSON file is being obtained", null, new Date(), null);
			long asynOperId;
			try{
				JpaManager dbmanager = JpaManager.getInstance();
				asynOperId = dbmanager.insertAsynchronousOperation(asynOper);
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the asynchronous operation into the DB", e);
			}
			Future<Course> process = GLUEPSManagerServerMain.pool.submit(new BackgroundGetCourse(type, accessLocation, creduser, credsecret, showAR, showVG, version, wstoken, courseId, app, asynOperId, asynOper.getOperation(), false));		   		
		}else{
			asynOper = new AsynchronousOperation(null, AsynchronousOperation.generateOperationId(), AsynchronousOperation.STATUS_INPROGRESS, "The course list information is being obtained", null, new Date(), null);
			long asynOperId;
			try{
				JpaManager dbmanager = JpaManager.getInstance();
				asynOperId = dbmanager.insertAsynchronousOperation(asynOper);
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the asynchronous operation into the DB", e);
			}
			Future<LearningEnvironment> process = GLUEPSManagerServerMain.pool.submit(new BackgroundGetCourseList(type, accessLocation, creduser, credsecret, showAR, showVG, version, wstoken, app, asynOperId, asynOper.getOperation(), false));
		}
		
		URLifyAsynchronousOperation(asynOper, doGluepsUriSubstitution(getReference().getParentRef().getIdentifier()));		
		Gson gson = new Gson();	
		String json = gson.toJson(asynOper);		
   		if (json!=null){
   			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
		getResponse().setLocationRef(asynOper.getOperation());
		answer.setCharacterSet(CharacterSet.UTF_8);
		setStatus(Status.SUCCESS_ACCEPTED);
   		return answer; 	
		
	}
    
	
	/**
	 * Does a background get of a course as an XML file or a JSON file
	 * @author Javier Enrique Hoyos Torio
	 *
	 */
	private class BackgroundGetCourse implements Callable<Course> {
		
		private String type;
		private String accessLocation;
		private String creduser;
		private String credsecret;
		private boolean showAR;
		private boolean showVG;
		private String version;
		private String wstoken;
		private String courseId;
		private GLUEPSManagerApplication app;
		private long asynOperId;
		private String operation;
		private boolean xmlFormat;
		
		public BackgroundGetCourse(String type, String accessLocation, String creduser, String credsecret, boolean showAR, boolean showVG, String version, String wstoken, String courseId, GLUEPSManagerApplication app, long asynOperId, String operation, boolean xmlFormat){
			this.type = type;
			this.accessLocation = accessLocation;
			this.creduser = creduser;
			this.credsecret = credsecret;
			this.showAR = showAR;
			this.showVG = showVG;
			this.version = version;
			this.wstoken = wstoken;
			this.courseId = courseId;
			this.app = app;
			this.asynOperId = asynOperId;
			this.operation = operation;
			this.xmlFormat = xmlFormat;
		}

		@Override
		public Course call() throws Exception {
			// TODO Auto-generated method stub
			LearningEnvironment le = null;
			try {
				le = new LearningEnvironment(null, null, type, new URL(accessLocation), null, creduser, credsecret, null, showAR, showVG);
				if (version!=null && !version.isEmpty() && wstoken!=null && !wstoken.isEmpty()){					
					le.setParameters("version=" + Reference.encode(version) + "&wstoken=" + wstoken);
				}
				LearningEnvironmentResource leResource = new LearningEnvironmentResource();
				leResource.setLearningEnvironment(le);
				leResource.setApplicationResource(app);
				leResource.getCompleteLEObject();
			} catch (MalformedURLException e) {
				defineErrorAsynchronousOperation(asynOperId, "The accessLocation provided is not a URL");
				return null;
			}
						
			HashMap<String,String> courses = le.getCourses();						
			String coursename = courses.get(courseId);
			Course course;
			//TODO For now, we just get course id and name... we could think of getting all the data at some point		
			if(coursename != null){
				course = new Course(courseId, coursename, null, null, null);
			}
			else {
				defineErrorAsynchronousOperation(asynOperId, "The course does not exist or you do not have the necessary permissions to get its participants.");
				return null;
			}
			
			VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory((GLUEPSManagerApplication) app);
			IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(le);
			//Now, we get the users for that course
			HashMap<String,Participant> users = adaptor.getCourseUsers(accessLocation,courseId);
			course.setParticipants(users);
			if (xmlFormat == true){
				String xmlfile = generateXML(course, glueps.core.model.Course.class);
				defineSuccessAsynchronousOperation(asynOperId, "The course information is already available", app.getAppExternalUri() + "asynchronousOperations/" + operation, xmlfile);
			}else{
				Gson gson = new Gson();	
				String json = gson.toJson(course);				
		   		if (json!=null){
		   			defineSuccessAsynchronousOperation(asynOperId, "The course information is already available", app.getAppExternalUri() + "asynchronousOperations/" + operation, json);
		   		}else{
					defineErrorAsynchronousOperation(asynOperId, "Error while parsing the course information as a JSON file");
					return null;
		   		}
			}
			return course;
		}
	
	}
	
	/**
	 * Does a background get of a learning environment containing the course list as an XML file
	 * @author Javier Enrique Hoyos Torio
	 *
	 */
	private class BackgroundGetCourseList implements Callable<LearningEnvironment> {
		
		private String type;
		private String accessLocation;
		private String creduser;
		private String credsecret;
		private boolean showAR;
		private boolean showVG;
		private String version;
		private String wstoken;
		private GLUEPSManagerApplication app;
		private long asynOperId;
		private String operation;
		private boolean xmlFormat;
		
		public BackgroundGetCourseList(String type, String accessLocation, String creduser, String credsecret, boolean showAR, boolean showVG, String version, String wstoken, GLUEPSManagerApplication app, long asynOperId, String operation, boolean xmlFormat){		
			this.type = type;
			this.accessLocation = accessLocation;
			this.creduser = creduser;
			this.credsecret = credsecret;
			this.showAR = showAR;
			this.showVG = showVG;
			this.version = version;
			this.wstoken = wstoken;
			this.app = app;
			this.asynOperId = asynOperId;
			this.operation = operation;
			this.xmlFormat = xmlFormat;
		}

		@Override
		public LearningEnvironment call() throws Exception {
			LearningEnvironment le = null;
			try {
				le = new LearningEnvironment(null, null, type, new URL(accessLocation), null, creduser, credsecret, null, showAR, showVG);
				if (version!=null && !version.isEmpty() && wstoken!=null && !wstoken.isEmpty()){					
					le.setParameters("version=" + Reference.encode(version) + "&wstoken=" + wstoken);
				}
				LearningEnvironmentResource leResource = new LearningEnvironmentResource();
				leResource.setLearningEnvironment(le);
				leResource.setApplicationResource(app);
				leResource.getCompleteLEObject();
			} catch (MalformedURLException e) {
				defineErrorAsynchronousOperation(asynOperId, "The accessLocation provided is not a URL");
				return null;
			}
			
			if (xmlFormat == true){
				//We generate the xml on-the-fly and respond with it
				String xmlfile = generateXML(le, glueps.core.model.LearningEnvironment.class);
				defineSuccessAsynchronousOperation(asynOperId, "The course list information is already available", app.getAppExternalUri() + "asynchronousOperations/" + operation, xmlfile);
			}else{				
				// convert the java object to JSON format,
				// and return it as a JSON formatted string
				Gson gson = new Gson();
				String json = gson.toJson(le);
		   		if (json != null){
		   			defineSuccessAsynchronousOperation(asynOperId, "The course list information is already available", app.getAppExternalUri() + "asynchronousOperations/" + operation, json);
		   		}
		   		else{
		   			defineErrorAsynchronousOperation(asynOperId, "Error while parsing the course list information as a JSON file");
		   			return null;
		   		}
			}
			return le;
		}
		
	}
	
	
	private void defineSuccessAsynchronousOperation(long asynOperId, String description, String resource, String xmlfile){
		JpaManager dbmanager = JpaManager.getInstance();
		AsynchronousOperation asynOper = dbmanager.findAsynchOperObjectById(String.valueOf(asynOperId));
		asynOper.setDescription(description);
		asynOper.setStatus(AsynchronousOperation.STATUS_OK);
		asynOper.setResource(resource);
		asynOper.setFinished(new Date());
		try {
			dbmanager.insertAsynchronousOperation(asynOper, xmlfile);
		} catch (Exception e) {
			System.err.println("Error while updating the asynchronous operation");
		}
	}
	
	private void defineErrorAsynchronousOperation(long asynOperId, String description){
		JpaManager dbmanager = JpaManager.getInstance();
		AsynchronousOperation asynOper = dbmanager.findAsynchOperObjectById(String.valueOf(asynOperId));
		asynOper.setDescription(description);
		asynOper.setStatus(AsynchronousOperation.STATUS_ERROR);
		asynOper.setResource(null);
		try {
			dbmanager.insertAsynchronousOperation(asynOper);
		} catch (Exception e) {
			System.err.println("Error while updating the asynchronous operation");
		}
	}
    

}
