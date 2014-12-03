package glueps.core.resource;

import glueps.core.model.LearningEnvironmentType;
import glueps.core.persistence.JpaManager;

import java.util.logging.Logger;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import com.google.gson.Gson;

/**
 * Resource Learning Environment Type
 * 
 * Information about a Learning Environment Type (its properties)
 * 
 * @author	 	Javier Enrique Hoyos Torio
 * @version 	2014073001
 * @package 	glueps.core.resources
 */
public class LearningEnvironmentTypeResource extends GLUEPSResource{
	
	/** 
	 * Logger 
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
	
	/** Local id. Integer used as identifier in table of glueps_learning_environments_types */
    protected String leTypeId;
    
    /**
     * The Learning environment type requested
     */
    protected LearningEnvironmentType leType;
    
    @Override
    protected void doInit() throws ResourceException{
    	// get the "LETypeId" attribute value taken from the URI template /learningEnvironmentTypes/{LETypeId}
    	leTypeId = trimId((String)this.getRequest().getAttributes().get("LETypeId"));
    	
    	logger.info("Initializing resource " + this.leTypeId);
    	
    	JpaManager dbmanager = JpaManager.getInstance();
    	leType = dbmanager.findLETypeObjectById(leTypeId);
    	
   		// does it exist?
		setExisting(this.leType != null);	// setting 'false' implies that REST methods won't start; server will respond with 404
    }
    
    @Get("xml|html")
    public Representation getLearningEnvironmentType(){
    	
   		logger.info("** GET learning environment type received");
   		Representation answer = null;
   		
   		LearningEnvironmentType urlifiedLEType = URLifyLearningEnvironmentType(leType, doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getIdentifier()));
   	    //We generate the xml on-the-fly and respond with it
   		String xmlfile = generateXML(urlifiedLEType, glueps.core.model.LearningEnvironmentType.class);
   		
   		if (xmlfile != null){
   			answer = new StringRepresentation((CharSequence)xmlfile, MediaType.TEXT_XML);
   			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
   		
   		logger.info("** GET Learning Environment type answer: \n" + (answer != null ? xmlfile : "NULL"));
   		return answer; 
    }
    
    @Get("json")
    public Representation getJsonLearningEnvironmentType(){
   		logger.info("** GET json learning environment type received");
   		Representation answer = null;
   		
   		LearningEnvironmentType urlifiedLEType = URLifyLearningEnvironmentType(leType, doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getIdentifier()));
		// convert java object to JSON format,
		// and return it as a JSON formatted string
   		Gson gson = new Gson();
   		String json = gson.toJson(urlifiedLEType);
   		
   		if (json != null){
   			answer = new StringRepresentation((CharSequence)json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
    	
   		logger.info("** GET json learning environment type answer: \n" + (answer != null ? json : "NULL"));
   		return answer;
    }
    
}
