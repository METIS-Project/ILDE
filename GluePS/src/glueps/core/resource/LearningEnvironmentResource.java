package glueps.core.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import glue.common.format.FormatStatic;
import glue.common.format.FormattedEntry;
import glue.common.format.FormattedFeed;
import glueps.adaptors.ARbrowsers.adaptors.Constants;
import glueps.adaptors.vle.IDynamicVLEDeployer;
import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.VLEAdaptorFactory;
import glueps.core.controllers.exist.Exist;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.LearningEnvironmentInstallation;
import glueps.core.model.Resource;
import glueps.core.model.ToolInstance;
import glueps.core.persistence.JpaManager;


import org.apache.commons.io.FileUtils;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Status;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import com.google.gson.Gson;


/**
 * Resource Learning Environment.
 * 
 * Information about a registered tool implementation, available to create Gluelets. 
 * 
 * @author	 	Juan Carlos A. <jcalvarezgsic@gsic.uva.es>
 * @version 	2010042000
 * @package 	glueps.core.resources
 */

public class LearningEnvironmentResource extends GLUEPSResource {
	
	private static final String EDIT_LE_NAME_FIELD = "leName";
	private static final String EDIT_LE_INSTALLATION_FIELD = "leInstallation";
	private static final String EDIT_LE_USER_FIELD = "leUser";
	private static final String EDIT_LE_PASSWORD_FIELD = "lePassword";
	
/// attributes ///
	
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
 
	
	/** Local id. Integer used as identifier in table of tool learningEnvironments */
    protected String leId;
    
    protected LearningEnvironment le;
    
    protected String feedReference;
    
    public LearningEnvironment getLEObject(){
    	return this.le;
    }
    
    public LearningEnvironment getCompleteLEObject(){
    	GLUEPSManagerApplication app = (GLUEPSManagerApplication)this.getApplication();

		//We ask GLUEletManager for the external tools, and add it to the LE data
		le.setExternalTools(getExternalTools());
		
		//TODO Order the course entries by course name??
		if (app.getOnlyUserCourses()){
			le.setCourses(getCourses(le.getAccessLocation(), le.getCreduser()));
		}
		else{
			le.setCourses(getCourses(le.getAccessLocation()));
		}

		//We convert the internal tool IDs to accessible URLs (to get the configuration)
		le.setInternalTools(getInternalTools());
		
		//JUAN: introduced to may hide AR controls in GUI
		boolean showAr = Constants.GUI_SHOWAR;
		le.setShowAr(showAr);

		return le;
    }
    
    
    @Override
    protected void doInit() throws ResourceException {
    	
   		// get the "LEId" attribute value taken from the URI template /learningEnvironment/{LEId}
   		this.leId = trimId((String) getRequest().getAttributes().get("LEId"));
   		

   		logger.info("Initializing resource "+this.leId);

   		JpaManager dbmanager = JpaManager.getInstance();
   		le = dbmanager.findLEObjectById(this.leId);
   		
   		// does it exist?
		setExisting(this.le != null);	// setting 'false' implies that REST methods won't start; server will respond with 404


    }
    

 
    @Get("xml|html")
	public Representation getLearningEnvironment()  {
    		
   		logger.info("** GET LEARNING ENVIRONMENT received");
   		Representation answer = null;
		
		getCompleteLEObject();

		LearningEnvironment urlifiedLE = URLifyLearningEnvironment(this.le, doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getIdentifier()));
		
		//We generate the xml on-the-fly and respond with it
		String xmlfile = generateXML(urlifiedLE, glueps.core.model.LearningEnvironment.class);

		if (xmlfile != null){	   		
				answer = new StringRepresentation((CharSequence) xmlfile, MediaType.TEXT_XML);
				answer.setCharacterSet(CharacterSet.UTF_8);
   		}
    	
   		logger.info("** GET Learning Environment answer: \n" + (answer != null ? xmlfile : "NULL"));
   		return answer;   	
    }
    
    @Get("json")
	public Representation getJsonLearningEnvironment()  {
    		
   		logger.info("** GET JSON LEARNING ENVIRONMENT received");
   		Representation answer = null;

   		getCompleteLEObject();

		LearningEnvironment urlifiedLE = URLifyLearningEnvironment(this.le, doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getIdentifier()));
		
    	Gson gson = new Gson();
   	 
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(urlifiedLE);
   		
   		
   		if (json != null){
			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
   		
   		logger.info("** GET JSON Learning Environment answer: \n" + (answer != null ? json : "NULL"));
   		return answer;   	
    }

	@Delete
	public Representation deleteLearningEnvironment() {
					
		if(doAuthentication){
	    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
			String login = this.getRequest().getChallengeResponse().getIdentifier();
	
			if(!le.getUserid().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the creator of the Learning Environment");
		}
	
		logger.info("** DELETE LEARNING ENVIRONMENT received");
		
		JpaManager dbmanager = JpaManager.getInstance();
		dbmanager.deleteLearningEnvironment(leId);
		
		logger.info("** DELETE LEARNING ENVIRONMENT completed");
		return null;
	}
	
    @Put
	public Representation putLearningEnvironment(Representation entity)  {
    		
   		logger.info("** PUT LEARNING ENVIRONMENT received");
   		
		Form form = new Form(entity);
		String leName 	= form.getFirstValue(EDIT_LE_NAME_FIELD);
		String leInstallation 	= form.getFirstValue(EDIT_LE_INSTALLATION_FIELD);
		String leUser 	= form.getFirstValue(EDIT_LE_USER_FIELD);
		String lePassword 	= form.getFirstValue(EDIT_LE_PASSWORD_FIELD);
		Representation answer = null;
		
		/// Checks parameter values
		String missingParameters = "";
		if (leName == null || leName.length() == 0)
			missingParameters += EDIT_LE_NAME_FIELD + ", ";
		if (leInstallation == null || leInstallation.length() == 0) 
			missingParameters += EDIT_LE_INSTALLATION_FIELD + ", ";
		if (leUser == null || leUser.length() == 0) 
			missingParameters += EDIT_LE_USER_FIELD + ", ";
		if (lePassword == null || lePassword.length() == 0) 
			missingParameters += EDIT_LE_PASSWORD_FIELD + ", ";
		
		if (missingParameters.length() > 0) {
			missingParameters = missingParameters.substring(0, missingParameters.length() - 2);			
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: " + missingParameters);
		}
		
		String userid = this.getRequest().getChallengeResponse().getIdentifier();
		if(userid==null) throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to get the learning environment caller user");
   		JpaManager dbmanager = JpaManager.getInstance();
   		LearningEnvironmentInstallation leInst = dbmanager.findLEInstObjectById(leInstallation);
   		if (leInst==null) throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to get the LE Installation from the DB");
		String leType = leInst.getType();
		URL accessLocation = leInst.getAccessLocation();
		
		LearningEnvironment le;
		try {
			le = dbmanager.findLEObjectById(leId);
			if (!le.getUserid().equals(userid)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the Learning Environment");
			le.setName(leName);
			le.setType(leType);
			le.setAccessLocation(accessLocation);
			le.setUserid(userid);
			le.setCreduser(leUser);
			le.setCredsecret(lePassword);
			//Hacemos el update
			dbmanager.insertLearningEnvironment(le);
		}
		catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to update the learning environment into the DB", e);
		}
		return answer;
    	
   		//logger.info("** GET Learning Environment answer: \n" + (answer != null ? xmlfile : "NULL")); 	
    }

private HashMap<String, String> fixInternalToolURLs(HashMap<String, String> internalTools) {

	if(internalTools == null) return null;
	if(internalTools.size()==0) return internalTools;
	
	GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
	String gluepsUrl = app.getAppExternalUri();

	HashMap<String, String> newInternalTools = new HashMap<String, String>();
	
	for(Iterator<String> it = internalTools.keySet().iterator(); it.hasNext();){
		
		String key = it.next();
		
		String value = internalTools.get(key);
		
		//If the key is not a valid GLUEPS URL, we convert its name to one
		if(!key.startsWith(gluepsUrl)) key = gluepsUrl + "learningEnvironments/" + leId + "/tools/" + key;		
		
		newInternalTools.put(key, value);
	}
	
	return newInternalTools;
}




//	private HashMap<String, Course> getCourses(URL baseurl) {
//		
//		MoodleAdaptor adaptor = new MoodleAdaptor();
//   		logger.info("Trying to get courses from the URL: "+baseurl.toString());
//		//TODO This would be dependent on the VLE type selected, for now only Moodle
//   		HashMap<String,Course> courses = adaptor.getMoodleCourses(baseurl.toString());
//   		
//   		//We process the courses so that the id is the full course URL
//   		HashMap<String,Course> newcourses = new HashMap<String, Course>();
//   		for(Iterator<String> it = courses.keySet().iterator(); it.hasNext();){
//   			String key = it.next();
//   			Course course = courses.get(key);
//   			String newkey = this.getReference().getParentRef().getIdentifier()+this.le.getId()+"/courses/"+key+".xml";
//   			//This setting of the ID might be optional?
//   			course.setId(newkey);
//   			newcourses.put(newkey, course);
//   		}
//   		
//		return newcourses;
//	}

protected HashMap<String, String> getCourses(URL baseurl) {
	
	VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
	IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(this.le);
	
		logger.info("Trying to get courses from the URL: "+baseurl.toString());
		HashMap<String,String> courses = adaptor.getCourses(baseurl.toString());
		
		//We process the courses so that the id is the full course URL
		//Now this is done at the end, by urlifying the whole LE object
//		HashMap<String,String> newcourses = new HashMap<String, String>();
//		for(Iterator<String> it = courses.keySet().iterator(); it.hasNext();){
//			String key = it.next();
//			String name = courses.get(key);
//			String newkey = this.leCompleteId+"/courses/"+key;
//			
//			newkey = doGluepsUriSubstitution(newkey);
//			
//			newcourses.put(newkey, name);
//		}
		
	return courses;
}

protected HashMap<String, String> getCourses(URL baseurl, String username) {
	
	VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
	IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(this.le);
		logger.info("Trying to get courses from the URL: "+baseurl.toString());
		HashMap<String,String> courses = adaptor.getCourses(baseurl.toString(), username);
		
		//We process the courses so that the id is the full course URL
		//Now this is done at the end, by urlifying the whole LE object
//		HashMap<String,String> newcourses = new HashMap<String, String>();
//		for(Iterator<String> it = courses.keySet().iterator(); it.hasNext();){
//			String key = it.next();
//			String name = courses.get(key);
//			String newkey = this.leCompleteId+"/courses/"+key;
//			
//			newkey = doGluepsUriSubstitution(newkey);
//			
//			newcourses.put(newkey, name);
//		}
		
	return courses;
}

protected HashMap<String, String> getInternalTools() {
	
	VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
	IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(this.le);
	
	HashMap<String,String> tools = adaptor.getInternalTools();		
	return tools;
}

//	private String generateXML(LearningEnvironment le){
//		
//		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
//		String TMP_DIRECTORY = app.getAppPath()+"/uploaded/temp/";
//		
//		//We create the LE XML
//		String xml = null;
//		File fichero;
//		JAXBContext context;
//		try {
//			fichero = new File(TMP_DIRECTORY+le.getId()+".xml");
//			context = JAXBContext.newInstance(LearningEnvironment.class);
//			  Marshaller m = context.createMarshaller();	
//			  //Esta Propiedad formatea el código del XML
//	          m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
//	          m.marshal(le, fichero);
//		      xml = FileUtils.readFileToString(fichero, "UTF-8");
//		      return xml;
//		} catch (JAXBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}

    
    private HashMap<String, String> getExternalTools() {
		HashMap<String,String> tools = null;
    	
    	//Get the GlueletManager URL from app properties
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		String gmUrl = app.getGmurlinternal();
	
		//We get the external GLUEPS URI, either from the properties or from our own reference
		//Not needed anymore, we urlify the tool ids just before responding to the client
//		String gluepsUrl = app.getAppExternalUri();
//		if(gluepsUrl==null){
//			gluepsUrl = this.leCompleteId.substring(0, this.leCompleteId.lastIndexOf("learningEnvironments/"));
//			//gluepsUrl = getReference().getParentRef().getParentRef().getIdentifier();
//		}

		
		System.out.println("Client connection to " + gmUrl+"tools");
		
		FormattedFeed toolsFeed;
		String response;
		try {
			response = doGetFromURL(gmUrl+"tools");
			toolsFeed = new FormattedFeed(response);
			
			System.out.println("Received feed " + toolsFeed.toString());
			for (Iterator<FormattedEntry> it = toolsFeed.getEntries()
					.iterator(); it.hasNext();) {
				FormattedEntry entry = it.next();

				if (entry.getId() != null && entry.getTitle() != null) {
					if (tools == null)
						tools = new HashMap<String, String>();
					
					//Old code before merge
					//tools.put(entry.getId(), entry.getTitle());
					
					//New code
					String newToolId = trimId(entry.getId());
					//If the tool URL from the GM is not from GLUEPS (which is sure, we substitute it to make the GLUEPS the intermediary)
					//not needed anymore, since we urlify the tool ids just before responding
					//if(!newToolId.startsWith(gluepsUrl)) newToolId = newToolId.replace(newToolId.substring(0, newToolId.indexOf("tools/")), gluepsUrl+"learningEnvironments/"+this.le.getId()+"/");
					tools.put(newToolId, entry.getTitle());

				}
			}
			
			return tools;
			
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}

		
//		//Get the list of tools from the GlueletManager
//    	Component component = new Component();
//    	Client client = new Client(Protocol.HTTP);
//    	//Timeout 0 means that timeout is infinite 
//    	client.setConnectTimeout(0);
//    	component.getClients().add(client);
//    	try {
//			component.start();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		//ClientResource toolsResource = new ClientResource(component.getContext().createChildContext(), gmUrl+"tools");  
		//New code equivalent mimicking GLUEletManager changes for connection stability



		
		
//		ClientResource toolsResource = new ClientResource(gmUrl+"tools");
//		Representation rep = null;
//		try {
//
//			rep = toolsResource.get();
//			
//			Representation toolsRep = null;
//			if (toolsResource.getStatus().isSuccess()
//					&& toolsResource.getResponseEntity().isAvailable())
//				toolsRep = toolsResource.getResponseEntity();
//
//			// Extract the title and id of each entry from the Atom
//
//			FormattedFeed toolsFeed;
//			try {
//				toolsFeed = new FormattedFeed(toolsRep.getText());
//			} catch (IOException e) {
//				// If we cannot format the returned response, we just return
//				// null (no external tools)
//				e.printStackTrace();
//				return null;
//			}
//
//			System.out.println("Received feed " + toolsFeed.toString());
//			for (Iterator<FormattedEntry> it = toolsFeed.getEntries()
//					.iterator(); it.hasNext();) {
//				FormattedEntry entry = it.next();
//
//				if (entry.getId() != null && entry.getTitle() != null) {
//					if (tools == null)
//						tools = new HashMap<String, String>();
//					
//					//Old code before merge
//					//tools.put(entry.getId(), entry.getTitle());
//					
//					//New code
//					String newToolId = entry.getId();
//					//If the tool URL from the GM is not from GLUEPS (which is sure, we substitute it to make the GLUEPS the intermediary)
//					if(!newToolId.startsWith(gluepsUrl)) newToolId = newToolId.replace(newToolId.substring(0, newToolId.indexOf("tools/")), gluepsUrl+"learningEnvironments/"+this.le.getId()+"/");
//					tools.put(newToolId, entry.getTitle());
//
//				}
//			}
//
//			return tools;
//			
//		} catch (ResourceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} finally {
////			Representation entity = toolsResource.getResponseEntity();
////			if (entity != null)
////				entity.release();
////			toolsResource.release();
//			
//			//New code equivalent mimicking GLUEletManager changes for connection stability
//			discardRepresentation(toolsResource.getResponseEntity());    /// **** LA CLAVE: "VOLVER A" EXTRAER LA ENTIDAD RESPUESTA DEL 
//																		///CLIENTRESOURCE; si se llega aqu� es desde la ejecuci�n de .get(), 
//																		///por lo que la variable 'response' NO SE HA ASIGNADO
//		}
//		

	}



	public void setLearningEnvironment(String LEId, String feedReference)  {
		
		//We do not know whether lEId is an URL or just the identifier!
        //this.lELocalId = lEId;
		this.leId = trimId(LEId);
        
        this.feedReference=feedReference;
		
   		JpaManager dbmanager = JpaManager.getInstance();
   		le = dbmanager.findLEObjectById(this.leId);
   		
   		// does it exist?
		setExisting(this.le != null);	// setting 'false' implies that REST methods won't start; server will respond with 404

    } 
	
	public void setLearningEnvironment(LearningEnvironment le)  {		
		this.leId = null;       
        this.feedReference = null;
        this.le = le;
    }  
    
    public FormattedEntry getLearningEnvironmentEntry() {
    	FormattedEntry entry = null;
    	java.util.Date fecha = new Date();
        if(this.isExisting()) {
            entry = new FormattedEntry();

            /// build URI
            String uri = null;
            Reference ref = getReference();
            if(ref!=null)
            	uri = ref.getIdentifier();
            else
            	uri =feedReference+"/" + this.leId;

            uri = doGluepsUriSubstitution(uri);
            
//Apparently, we do not need the URLification??            
//    		String gluepsUrl = ((GLUEPSManagerApplication) this.getApplication()).getAppExternalUri();
//    		if(gluepsUrl==null){
//    			gluepsUrl = uri.substring(0, uri.lastIndexOf("learningEnvironments"));
//    		}
//    		
//    		LearningEnvironment urlifiedLE = URLifyLearningEnvironment(this.le, gluepsUrl);
            
            /// Atom standard elements
            entry.setId(uri);
            entry.setTitle(this.le.getName());
            entry.setUpdated(fecha);
            entry.setAlternateLink("Description of learning environment "+ this.leId, uri);
           	entry.addAuthor(FormatStatic.GSIC_NAME, null, null);
            entry.setRelatedLink("Provider", uri);
           	
            //Glueps specific elements
           	entry.addExtendedTextChild("id",this.leId);
           	entry.addExtendedTextChild("type",this.le.getType());
           	entry.addExtendedTextChild("vlelocation", this.le.getAccessLocation().toString());
           	entry.addExtendedTextChild("creduser", this.le.getCreduser());
           	
        } else {
        	setStatus(Status.CLIENT_ERROR_NOT_FOUND);
        }
        
        return entry;
    } 
    
}
