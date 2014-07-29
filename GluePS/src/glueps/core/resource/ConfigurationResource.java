package glueps.core.resource;

import glueps.adaptors.vle.moodle.MoodleAdaptor;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.LearningEnvironment;
import glueps.core.persistence.JpaManager;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
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
		//ler.getCompleteLEObject();
		ler.getToolsLEObject(); //we do not need to the courses info, only the tools info
   		
   		
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
