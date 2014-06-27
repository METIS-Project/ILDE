package glueps.core.resource;

import glue.common.format.FormatStatic;
import glue.common.format.FormattedEntry;
import glue.common.format.FormattedFeed;
import glue.common.resources.GLUEResource;
import glue.common.server.Server;
import glueps.adaptors.vle.moodle.MoodleAdaptor;
import glueps.core.controllers.exist.Exist;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.LearningEnvironment;
import glueps.core.persistence.JpaManager;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.restlet.Client;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

public class ConfigurationResource extends GLUEPSResource {

	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");

	/** Local id. Integer used as identifier in table of tool learningEnvironments */
    protected String leId;
    
    protected LearningEnvironment le;

    protected String leXmlFile=null;
    
    protected String localToolId;
    
    protected String localConfiguration;
    
    @Override
    protected void doInit() throws ResourceException {
    	
   		// get the "LEId" attribute value taken from the URI template /learningEnvironment/{LEId}/tools/{toolId}/configuration
   		leId = trimId((String) getRequest().getAttributes().get("LEId"));
   		
        JpaManager dbmanager = JpaManager.getInstance();
        le = dbmanager.findLEObjectById(leId);
        
		LearningEnvironmentResource ler = new LearningEnvironmentResource();
		ler.setLearningEnvironment(le);
		ler.getCompleteLEObject();
   		
   		
   		this.localToolId = trimId((String) getRequest().getAttributes().get("toolId"));
		
		//We ask GLUEletManager for the external tools, and add it to the LE data
		//le.setExternalTools(getExternalTools());

		
   		// does it exist?
		boolean exists = findToolIdInExternalTools(this.localToolId) || findToolIdInInternalTools(this.localToolId);
		setExisting(exists);	// setting 'false' implies that REST methods won't start; server will respond with 404

    }
    

	private boolean findToolIdInExternalTools(String toolId) {
		HashMap<String, String> externalTools = le.getExternalTools();
		if(toolId == null || externalTools == null || externalTools.size()==0) return false;
		
		for(Iterator<String> it = externalTools.keySet().iterator(); it.hasNext();){
			String gmKey = it.next();
			//if(gmKey.lastIndexOf("/")==-1) continue;
			if (gmKey.equals(toolId)) return true;
			else if(gmKey.substring(gmKey.lastIndexOf("/")+1).equals(toolId)) return true;
		}
		
		
		return false;
	}
	
	private boolean findToolIdInInternalTools(String toolId) {
		HashMap<String, String> internalTools = this.le.getInternalTools();
		if(toolId == null || internalTools == null || internalTools.size()==0) return false;
		
		for(Iterator<String> it = internalTools.keySet().iterator(); it.hasNext();){
			String gmKey = it.next();
			//if(gmKey.lastIndexOf("/")==-1) continue;
			if(gmKey.equals(toolId)) return true;
			else if(gmKey.substring(gmKey.lastIndexOf("/")+1).equals(toolId)) return true;
		}
		
		
		return false;
	}


	@Get
	public Representation getConfiguration(){

		
   		logger.info("** GET CONFIGURATION received");
   		Representation answer = null;
   		
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();

   		if(findToolIdInExternalTools(this.localToolId)){//It is an external tool
   			
   	   		logger.info(this.localToolId+" is an external tool. We ask GLUE");
   			this.localConfiguration = getToolInstanceConfiguration(this.localToolId);
   			
   		}else if(findToolIdInInternalTools(this.localToolId)){//It is an internal tool
   			
   	   		logger.info(this.localToolId+" is an internal tool");
   			//TODO select the tool type. For now, only Moodle
   			MoodleAdaptor adaptor = new MoodleAdaptor();
   			adaptor.setTemplateDir(app.getAppPath()+File.separator+"templates");
   			this.localConfiguration = adaptor.getToolConfiguration(this.localToolId);
   			
   		}

   		if(localConfiguration!=null) answer = new StringRepresentation(localConfiguration);

   		return answer;
		
	}
	
	
    private HashMap<String, String> getExternalTools() {
		HashMap<String,String> tools = null;
    	
    	//Get the GlueletManager URL from app properties
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		String gmUrl = app.getGmurlinternal();

		//We get the external GLUEPS URI, either from the properties or from our own reference
		String gluepsUrl = app.getAppExternalUri();
		if(gluepsUrl==null){
			gluepsUrl = this.getReference().getIdentifier().substring(0, this.getReference().getIdentifier().lastIndexOf("learningEnvironments/"));
			//gluepsUrl = getReference().getParentRef().getParentRef().getIdentifier();
		}

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
					String newToolId = entry.getId();
					//If the tool URL from the GM is not from GLUEPS (which is sure, we substitute it to make the GLUEPS the intermediary)
					if(!newToolId.startsWith(gluepsUrl)) newToolId = newToolId.replace(newToolId.substring(0, newToolId.indexOf("tools/")), gluepsUrl+"learningEnvironments/"+this.le.getId()+"/");
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
//
//		//ClientResource toolsResource = new ClientResource(component.getContext().createChildContext(), gmUrl+"tools");  
//		//New code equivalent mimicking GLUEletManager changes for connection stability
//		ClientResource toolsResource = new ClientResource(gmUrl+"tools");
//		
//		try {
//
//			toolsResource.get();
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
//					tools.put(entry.getId(), entry.getTitle());
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
//																		///CLIENTRESOURCE; si se llega aquí es desde la ejecución de .get(), 
//																		///por lo que la variable 'response' NO SE HA ASIGNADO
//
//		}
		

	}



	
	
	private String getToolInstanceConfiguration(String toolType) {
		
     	//Get the GlueletManager URL from app properties
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		String gmUrl = app.getGmurlinternal();
		
		String response;
		try {
			response = doGetFromURL(gmUrl+"tools/"+toolType+"/configuration");
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	
		
//		//Get the list of tool types from the GlueletManager
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
//
//		//ClientResource configResource = new ClientResource(component.getContext().createChildContext(), gmUrl+"tools/"+toolType+"/configuration");  
//		//New code equivalent mimicking GLUEletManager changes for connection stability
//		ClientResource configResource = new ClientResource(gmUrl+"tools/"+toolType+"/configuration");
//		
//		try{
//			configResource.get();
//			Representation config = null;
//			if (configResource.getStatus().isSuccess() && configResource.getResponseEntity().isAvailable()) config = configResource.getResponseEntity();
//			
//			//config.write(System.out);
//			
//			//Extract the ins:data tag from the Atom
//			String configData = null;
//			FormattedEntry configEntry = new FormattedEntry(config.getText());
//			
//			System.out.println("Received from GM:\n"+configEntry.getXhtmlContentAsText());
//			
//			//configData = findConfigData(configEntry.getXhtmlContent());
//			configData = configEntry.getXhtmlContentAsText();
//			
//			return configData;
//		}catch(Exception e){
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} finally {
////			Representation entity = configResource.getResponseEntity();
////			if (entity != null)
////				entity.release();
////			configResource.release();
//			
//			//New code equivalent mimicking GLUEletManager changes for connection stability
//			discardRepresentation(configResource.getResponseEntity());    /// **** LA CLAVE: "VOLVER A" EXTRAER LA ENTIDAD RESPUESTA DEL 
//																		///CLIENTRESOURCE; si se llega aqu� es desde la ejecuci�n de .get(), 
//																		///por lo que la variable 'response' NO SE HA ASIGNADO
//
//		}
		

	}


	
}
