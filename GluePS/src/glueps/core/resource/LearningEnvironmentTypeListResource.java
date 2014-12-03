package glueps.core.resource;

import glueps.core.model.LearningEnvironmentType;
import glueps.core.model.LearningEnvironmentTypes;
import glueps.core.persistence.JpaManager;

import java.util.List;
import java.util.logging.Logger;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;

import com.google.gson.Gson;

/**
 * Resource 'list of learning environment installations'
 * 
 * List of all the learning environment installations
 * 
 * @author	 	Javier Enrique Hoyos Torio
 * @version 	2014310701
 * @package 	glueps.core.resources
 */
public class LearningEnvironmentTypeListResource extends GLUEPSResource{

	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
	
	 @Get("xml|html")
	public Representation getLearningEnvironmentTypeList(){
		 logger.info("** GET learning Environment type list received");
		 Representation answer = null;
		 JpaManager dbmanager = JpaManager.getInstance();
		 List<LearningEnvironmentType> lets = dbmanager.listLETypeObjects();
		 URLifyLearningEnvironmentType(lets, doGluepsUriSubstitution(getReference().getParentRef().getIdentifier()));
		 LearningEnvironmentTypes leTypesObj = new LearningEnvironmentTypes(lets);
		 
	   	 //We generate the xml on-the-fly and respond with it
	   	 String xmlfile = generateXML(leTypesObj, glueps.core.model.LearningEnvironmentTypes.class);
	   		
	   	 if (xmlfile != null){
	   		 answer = new StringRepresentation((CharSequence)xmlfile, MediaType.TEXT_XML);
	   		 answer.setCharacterSet(CharacterSet.UTF_8);
	   	 }
	   		
	   	 logger.info("** GET Learning Environment type list answer: \n" + (answer != null ? xmlfile : "NULL"));
	     return answer; 
	}
	
	@Get("json")
	public Representation getJsonLearningEnvironmentTypeList(){
		 logger.info("** GET Json learning Environment type list received");
		 Representation answer = null;
		 JpaManager dbmanager = JpaManager.getInstance();
		 List<LearningEnvironmentType> lets = dbmanager.listLETypeObjects();
		 URLifyLearningEnvironmentType(lets, doGluepsUriSubstitution(getReference().getParentRef().getIdentifier()));
		 
		 // convert java object to JSON format,
		 // and return it as a JSON formatted string
		 Gson gson = new Gson();
		 String json = gson.toJson(lets);
		 
	   	if (json != null){
	   		answer = new StringRepresentation((CharSequence)json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
	   	}
	   	
   		logger.info("** GET json learning environment type answer: \n" + (answer != null ? json : "NULL"));
   		return answer;
		 
	 }
}
