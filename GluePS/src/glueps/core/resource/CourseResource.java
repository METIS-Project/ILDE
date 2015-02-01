package glueps.core.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import glue.common.resources.GLUEResource;
import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.VLEAdaptorFactory;
import glueps.adaptors.vle.moodle.MoodleAdaptor;
import glueps.core.controllers.exist.Exist;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Course;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.Participant;
import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.SectokenEntity;

public class CourseResource extends GLUEPSResource {

	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");

	/** Local id. Integer used as identifier in table of tool learningEnvironments */
    protected String leId;
    
    protected LearningEnvironment le;

    protected String courseXmlFile=null;
    
    protected String leXmlFile=null;

    protected String courseLocalId;
	
    protected Course localCourse;
    
    @Override
    protected void doInit() throws ResourceException {
    	
   		// get the "LEId" attribute value taken from the URI template /learningEnvironment/{LEId}/courses/{courseId}
   		leId = trimId((String) getRequest().getAttributes().get("LEId"));
   		
        JpaManager dbmanager = JpaManager.getInstance();
        le = dbmanager.findLEObjectById(leId);

   		String courseId = trimId((String) getRequest().getAttributes().get("courseId"));
   		this.courseLocalId = courseId;
		
   		localCourse = getCourseFromVLE(le);
		
   		// does it exist?
		setExisting(this.localCourse != null);	// setting 'false' implies that REST methods won't start; server will respond with 404

    }

    
    @Get
	public Representation getCourse()  {
    		
   		logger.info("** GET COURSE received");
   		Representation answer = null;
   		//OutputStream salida;  	
   		
		//We generate the xml on-the-fly and respond with it
		//courseXmlFile = generateXML(localCourse);
   		courseXmlFile = generateXML(localCourse, glueps.core.model.Course.class);

		if (this.courseXmlFile != null){	   		
   		// Now, with the complete Learning environment, we respond	
   		//	try {
				//salida = new FileOutputStream(xmlFile);
				//answer.write(salida);
				answer = new StringRepresentation((CharSequence) courseXmlFile, MediaType.TEXT_XML);
				answer.setCharacterSet(CharacterSet.UTF_8);
		//	} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
		//	} catch (IOException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
		//	}
   		}
    	
   		logger.info("** GET COURSE answer: \n" + (answer != null ? this.courseXmlFile : "NULL"));
   		return answer;
    	
    }

    
    
    
	private Course getCourseFromVLE(LearningEnvironment vle) {
		
		//MoodleAdaptor adaptor = new MoodleAdaptor();
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		VLEAdaptorFactory fact = new VLEAdaptorFactory(app);
		IVLEAdaptor adaptor = fact.getVLEAdaptor(vle);
		
		logger.info("Trying to get course "+this.courseLocalId+" from the URL: "+vle.getAccessLocation().toString());
		//TODO This would be dependent on the VLE type selected, for now only Moodle
		HashMap<String,String> courses = null;
		if (app.getOnlyUserCourses()){
			courses = adaptor.getCourses(vle.getAccessLocation().toString(), vle.getCreduser());
		}
		else{
			courses = adaptor.getCourses(vle.getAccessLocation().toString());
		}
		
		String coursename;
		Course course;
		//TODO For now, we just get course id and name... we could think of getting all the data at some point
		if((coursename = courses.get(this.courseLocalId)) != null) course = new Course(this.courseLocalId, coursename, null, null, null);
		else throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "The course does not exist");
		
		//Now, we get the users for that course
		HashMap<String,Participant> users = adaptor.getCourseUsers(vle.getAccessLocation().toString(),this.courseLocalId);
		course.setParticipants(users);
		
		return course;


	}

	/*private String generateXML(Course course){
		
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		String TMP_DIRECTORY = app.getAppPath()+"/uploaded/temp/";
		
		//We create the LE XML
		String xml = null;
		File fichero;
		JAXBContext context;
		try {
			fichero = new File(TMP_DIRECTORY+course.getId()+".xml");
			context = JAXBContext.newInstance(Course.class);
			  Marshaller m = context.createMarshaller();	
			  //Esta Propiedad formatea el código del XML
	          m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
	          m.marshal(course, fichero);
		      xml = FileUtils.readFileToString(fichero, "UTF-8");
		      return xml;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}*/

	
	
}
