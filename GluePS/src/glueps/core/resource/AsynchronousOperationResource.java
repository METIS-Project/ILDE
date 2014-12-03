package glueps.core.resource;

import glueps.core.model.AsynchronousOperation;
import glueps.core.model.LearningEnvironmentType;
import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.AsynchronousOperationEntity;

import java.sql.Timestamp;
import java.util.logging.Logger;

import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import com.google.gson.Gson;

/**
 * Resource AsynchronousOperation
 * 
 * Information about the current status of an asynchronous operation that started as a result of 
 * a REST request and it takes a long time to generate the response
 * 
 * @author	 	Javier Enrique Hoyos Torio
 * @version 	2014092901
 * @package 	glueps.core.resources
 */
public class AsynchronousOperationResource extends GLUEPSResource{

	/** 
	 * Logger 
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
	
	/** Local id. Integer used as identifier in table of glueps_asynchronous_operations */
    protected String asynOperId;
    
    /**
     * The operation id used to identify the resource
     */
    protected String operationId;
    
    /**
     * The AsynchronousOpertion requested
     */
    protected AsynchronousOperation operation;
    
    @Override
    protected void doInit() throws ResourceException{
    	// get the "operation" attribute value taken from the URI template /asynchronousOperations/{operationId}
    	operationId = trimId((String)this.getRequest().getAttributes().get("operationId"));
    	
    	logger.info("Initializing resource " + this.operationId);
    	
    	JpaManager dbmanager = JpaManager.getInstance();
    	operation = dbmanager.findAsynchOperObjectByOperation(operationId);
    	
   		// does it exist?
		setExisting(this.operation != null);	// setting 'false' implies that REST methods won't start; server will respond with 404
    }
    
    @Get("xml|html")
    public Representation getAsynchronousOperation(){
    	logger.info("** GET asynchronous operation received");
    	Representation answer = null;
    	URLifyAsynchronousOperation(operation, doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getIdentifier()));
   	    //We generate the xml on-the-fly and respond with it
   		String xmlfile = generateXML(operation, glueps.core.model.AsynchronousOperation.class);
   		
   		if (xmlfile != null){
   			answer = new StringRepresentation((CharSequence)xmlfile, MediaType.TEXT_XML);
   			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
   		
		if (operation.getStatus().equals(AsynchronousOperation.STATUS_OK)){
			if (this.getReference().toString().equals(operation.getResource())){
				getResponse().setLocationRef(operation.getResource()); //we include the location to the new resource
				JpaManager dbmanager = JpaManager.getInstance();
				AsynchronousOperationEntity operationEnt = dbmanager.findAsynchOperById(Long.valueOf(operation.getId()));
				operationEnt.setStatus(AsynchronousOperation.STATUS_NOT_RETURNED); //we have to return the resource located at this same URL
			}else{
			    getResponse().setLocationRef(operation.getResource()); //we include the location to the new resource
				//setStatus(Status.REDIRECTION_SEE_OTHER); //if this status is set, the browser will automatically redirect to that location. We do not want that by now
			    JpaManager dbmanager = JpaManager.getInstance();
			    //Once the response with the resource has been provided, we can delete this table entry
			    dbmanager.deleteAsynchronousOperation(operation.getId());
			}
		}else if (operation.getStatus().equals(AsynchronousOperation.STATUS_NOT_RETURNED)){
			JpaManager dbmanager = JpaManager.getInstance();
			AsynchronousOperationEntity operationEnt = dbmanager.findAsynchOperById(Long.valueOf(operation.getId()));
			String file = new String(operationEnt.getFile());
	   		answer = new StringRepresentation((CharSequence)file, MediaType.TEXT_XML);
	   		answer.setCharacterSet(CharacterSet.UTF_8);
		    dbmanager.deleteAsynchronousOperation(operation.getId());
		}
		setStatus(Status.SUCCESS_OK);
   		
   		logger.info("** GET asynchronous operation answer: \n" + (answer != null ? xmlfile : "NULL"));
   		return answer; 
    }
    
    @Get("json")
    public Representation getJsonAsynchronousOperation(){
   		logger.info("** GET json asynchronous operation received");
   		Representation answer = null;
   		URLifyAsynchronousOperation(operation, doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getIdentifier()));
		// convert java object to JSON format,
		// and return it as a JSON formatted string
   		Gson gson = new Gson();
   		String json = gson.toJson(operation);
   		
   		if (json != null){
   			answer = new StringRepresentation((CharSequence)json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
   		
		if (operation.getStatus().equals(AsynchronousOperation.STATUS_OK)){
			if (this.getReference().toString().equals(operation.getResource())){
				getResponse().setLocationRef(operation.getResource()); //we include the location to the new resource
				JpaManager dbmanager = JpaManager.getInstance();
				AsynchronousOperationEntity operationEnt = dbmanager.findAsynchOperById(Long.valueOf(operation.getId()));
				operationEnt.setStatus(AsynchronousOperation.STATUS_NOT_RETURNED); //we have to return the resource located at this same URL
			}else{
			    getResponse().setLocationRef(operation.getResource()); //we include the location to the new resource
				//setStatus(Status.REDIRECTION_SEE_OTHER); //if this status is set, the browser will automatically redirect to that location. We do not want that by now
			    JpaManager dbmanager = JpaManager.getInstance();
			    //Once the response with the resource has been provided, we can delete this table entry
			    dbmanager.deleteAsynchronousOperation(operation.getId());
			}
		}else if (operation.getStatus().equals(AsynchronousOperation.STATUS_NOT_RETURNED)){
			JpaManager dbmanager = JpaManager.getInstance();
			AsynchronousOperationEntity operationEnt = dbmanager.findAsynchOperById(Long.valueOf(operation.getId()));
			String file = new String(operationEnt.getFile());
	   		answer = new StringRepresentation((CharSequence)file, MediaType.APPLICATION_JSON);
	   		answer.setCharacterSet(CharacterSet.UTF_8);
		    dbmanager.deleteAsynchronousOperation(operation.getId());
		}
		setStatus(Status.SUCCESS_OK);
   		
   		logger.info("** GET json asynchronous operation answer: \n" + (answer != null ? json : "NULL"));
   		return answer;
    }
}
