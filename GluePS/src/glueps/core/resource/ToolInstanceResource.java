package glueps.core.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import glue.common.format.FormattedEntry;
import glue.common.resources.GLUEResource;
import glueps.adaptors.ARbrowsers.adaptors.qrcode.QrCodeAdaptor;
import glueps.core.controllers.exist.Exist;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Deploy;
import glueps.core.model.Resource;
import glueps.core.model.ToolInstance;
import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.SectokenEntity;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.apache.ws.commons.util.Base64;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.data.CharacterSet;
import org.restlet.data.ClientInfo;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.FileRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;



public class ToolInstanceResource extends GLUEPSResource{

	private static String UPLOAD_FILE_FIELD = "nameFile";
	
	private static Logger logger = Logger.getLogger("org.restlet");
 
	
	/** Local id. */
    protected String deployId;//should be just the numbers XXXXXX-XXXXXXX
    
    protected String instanceId;//should be just the code XXXX;XX
    
    protected Deploy deploy;
    
    protected ToolInstance instance;
    
    protected String login;
    
    @Override
    protected void doInit() throws ResourceException {
    	
   		// get the "toolId" attribute value taken from the URI template /deploys/{deployId}/toolInstances/{toolInstanceId}
   		this.deployId = trimId((String) getRequest().getAttributes().get("deployId"));
   		
   		// get the "toolId" attribute value taken from the URI template /deploys/{deployId}/toolInstances/{toolInstanceId}
   		this.instanceId = trimId((String) getRequest().getAttributes().get("toolInstanceId"));

		logger.info("Initializing instance resource "+this.instanceId+" in deploy "+this.deployId);

  		JpaManager dbmanager = JpaManager.getInstance();
   		deploy = dbmanager.findDeployObjectById(this.deployId);

		this.instance = deploy.getToolInstanceById(this.instanceId);
		
   		// does it exist?
		setExisting(this.instance != null);	// setting 'false' implies that REST methods won't start; server will respond with 404
    }

    
    /**
     * GET ToolInstanceResource
     * 
     * Returns the representation of a single tool instance in a deploy in GLUEPS
     * (requesting and forwarding from the Gluelet Manager)
     * 
     * @return The HTML representation of the instance (GM response), which includes the
     * 			access location of the instance
     */
    @Get
    public Representation getToolInstance(Representation entity){
    	
    	//We do not perform usual authorization... the instances can be seen in light of the callerUser sent in the 
    	
   		logger.info("** GET INSTANCE received");
    	
   		//TODO We should make it more flexible to get either the HTML or XML representation of the instance from GM
   		
		URL location = null;
		//if((location=instance.getLocation())==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "The instance has not been created yet");
		if((location=instance.getLocationWithRedirects(deploy))==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "The instance has not been created yet");
		
		String targetURI = location.toString();
		
		//get the caller user, to pass it on to GM
		String caller = (String) getRequest().getResourceRef().getQueryAsForm().getFirstValue("callerUser");
		//if(caller==null || caller.length()==0) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing callerUser parameter");
		//if they pass no user, we put the glueps login as caller
		if(caller==null || caller.length()==0) caller = login;
		
		String instanceText = getRemoteResource(targetURI+"?callerUser="+caller);
    	
   		Representation answer = null;
    	
   		if (instanceText != null){
			answer = new StringRepresentation((CharSequence) instanceText, MediaType.TEXT_HTML);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
   		
   		logger.info("** GET INSTANCE answer: \n" + (answer != null ? instanceText : "NULL"));
   		return answer;  		

		
    }

    
    public void setToolInstance(String deployId, String toolInstId)  {
    	
   		// get just the deployId without extension, if it had any
    	this.deployId = trimId(deployId);

    	this.instanceId = trimId(toolInstId);
    	
		// Maybe this part is too inefficient?? but we need it to get the design id from the deploy (other means could be thought of)
   		JpaManager dbmanager = JpaManager.getInstance();
   		this.deploy = dbmanager.findDeployObjectById(this.deployId);
        
		this.instance = deploy.getToolInstanceById(this.instanceId);

   		//Since we are creating the resource internally, we set authentication to false
   		this.doAuthentication = false;
   		
   		// does it exist?
		setExisting(this.deploy != null);	// setting 'false' implies that REST methods won't start; server will respond with 404

    }    

    
	private String getRemoteResource(String url) {
		
		String response;
		try {
			response = doGetFromURL(url);
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}


    
	/**
	 * DELETE ToolInstanceResource
	 * 
	 * Deletion of a single tool instance in a deploy in Glueps (and gluelet manager).
	 * Just forwards the deletion to the GM
	 * 
	 * 
	 * @return Atom/XML with the GM response, including the updated location of the
	 *         created tool instance.
	 */
	@Delete
	public Representation deleteToolInstance() {
   		
	    GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		if(doAuthentication){
	   	    if (app.getLdShakeMode()==false){
					//If we are coming through an unguarded call, we forbid the deletion
					if(this.getRequest().getChallengeResponse()==null || this.getRequest().getChallengeResponse().getIdentifier()==null)
						 throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not allowed to do that!");
					
			    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
					login = this.getRequest().getChallengeResponse().getIdentifier();
					if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
	   	    }
	   	    else{
	    		if (this.getRequest().getChallengeResponse()!=null){
	    	    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
	    			login = this.getRequest().getChallengeResponse().getIdentifier();
	    			if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
	    		}
	    		else{
	    			//Check if a sectoken has been provided
	    			Reference ref = getReference();
	    			Form query = ref.getQueryAsForm();
	    			String sectoken = query.getFirstValue("sectoken");
	    			if (sectoken!=null){
	    				JpaManager dbmanager = JpaManager.getInstance();
	    				SectokenEntity ste = dbmanager.findSectokenById(deployId);
	    				if (ste==null || !ste.getSectoken().equals(sectoken)){
	    					throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken is not valid for this deploy");
	    				}
	    			}else{
	    				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken parameter is missing");
	    			}
	    		}
	   	    }
		}

		logger.info("** DELETE INSTANCE received");

		//GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();

   		if(this.isExisting()) {
   			URL location = null;
   			if((location=instance.getLocation())==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "The instance was not created, cannot be deleted");
        	
 			String targetURI = location.toString();
			
   			Status st = deleteGMResource(targetURI);
			
   			// set status result
   			getResponse().setStatus(st);
			
   			// change the instance location, change the deploy resource and store it in DB??
   			if (st.isSuccess() || st.getCode()==404) {

   				this.instance.setLocation(null);
   				
   				this.deploy.getToolInstanceById(this.instanceId).setLocation(null);
   			
   				//We insert the updated deploy in DB
   				JpaManager dbmanager = JpaManager.getInstance();
   				try {
					dbmanager.insertDeploy(deploy);
				} catch (Exception e) {
					throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to update the deploy in DB", e);
				}
   				
				//The location has been set to null and the instance doesn't exist, so we don't need to delete it
				if (st.getCode() == 404){
					getResponse().setStatus(st.SUCCESS_OK);
				}
				

				logger.info("** DELETE INSTANCE complete");

   			}
    			
   		} else {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		}		
   		return null;
	}
	
	
	/**
	 * Delete the actual tool GLUElet.
	 * 
	 * @param	targetUri 	String		URL to delete the the remote resource
	 * @return	HTTP status for the subsequence response.
	 */
	protected Status deleteGMResource(String targetURI) throws ResourceException {
		int response=500;
		GLUEPSManagerApplication app = (GLUEPSManagerApplication)this.getApplication();
		try {
			//Avoid using external URI for Gluelet Manager, if it is in the local machine
			targetURI = targetURI.replace(app.getGmurl(), app.getGmurlinternal());
			response = doDeleteFromURL(targetURI);
			return new Status(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Status.SERVER_ERROR_INTERNAL;
		}
	}
	
	
	/**
	 * POST hexconv
	 * 
	 * Convenience resource to transform a file to hex encoding, so that file
	 * attachments can be sent to create a toolInstance by the client (e.g.
	 * in GoogleDocs)
	 * 
	 * @param a multipart form with the file
	 * @return the hex encoding of the file
	 */	
	@Post("multipart")
	public Representation convertFileToHexcode(Representation entity){
		
		try {
			
			GLUEPSManagerApplication app = (GLUEPSManagerApplication)this.getApplication();
			if(doAuthentication){
				if (app.getLdShakeMode()==false){
					//If we are coming through an unguarded call, we forbid the call
					if(this.getRequest().getChallengeResponse()==null || this.getRequest().getChallengeResponse().getIdentifier()==null)
						 throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not allowed to do that!");
					if(!this.getReference().getIdentifier().endsWith("hexconv") && !this.getReference().getIdentifier().endsWith("64conv"))
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Unknown resource call");
				}
				else{
						if(!this.getReference().getIdentifier().contains("hexconv") && !this.getReference().getIdentifier().contains("64conv"))
							throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Unknown resource call");
						if(this.getRequest().getChallengeResponse()==null || this.getRequest().getChallengeResponse().getIdentifier()==null){
			    			//Check if a sectoken has been provided
			    			Reference ref = getReference();
			    			Form query = ref.getQueryAsForm();
			    			String sectoken = query.getFirstValue("sectoken");
			    			if (sectoken!=null){
			    				JpaManager dbmanager = JpaManager.getInstance();
			    				SectokenEntity ste = dbmanager.findSectokenById(deployId);
			    				if (ste==null || !ste.getSectoken().equals(sectoken)){
			    					throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken is not valid for this deploy");
			    				}
			    			}else{
			    				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken parameter is missing");
			    			}
						}
		    	}
			}
			
			
			//If we are coming through an unguarded call, we forbid the call
			/*if(this.getRequest().getChallengeResponse()==null || this.getRequest().getChallengeResponse().getIdentifier()==null)
				 throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not allowed to do that!");
			
			if(!this.getReference().getIdentifier().endsWith("hexconv") && !this.getReference().getIdentifier().endsWith("64conv"))
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Unknown resource call");*/

			logger.info("** POST HEXCONV received: \n" + entity.toString());

			if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(),
			        true)) {

				//Set the upload and schemas paths
				//GLUEPSManagerApplication app = (GLUEPSManagerApplication)this.getApplication();
				String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
				String uploadDir = UPLOAD_DIRECTORY+"temp/";

			    // The Apache FileUpload project parses HTTP requests which
			    // conform to RFC 1867, "Form-based File Upload in HTML". That
			    // is, if an HTTP request is submitted using the POST method,
			    // and with a content type of "multipart/form-data", then
			    // FileUpload can parse that request, and get all uploaded files
			    // as FileItem.

				boolean found = false;
				File file = null;

				try {
					// 1/ Create a factory for disk-based file items
					DiskFileItemFactory factory = new DiskFileItemFactory();
					factory.setSizeThreshold(1000240);

					// 2/ Create a new file upload handler based on the Restlet
					// FileUpload extension that will parse Restlet requests and
					// generates FileItems.
					RestletFileUpload upload = new RestletFileUpload(factory);
					List<FileItem> items;

					// 3/ Request is parsed by the handler which generates a
					// list of FileItems
					items = upload.parseRequest(getRequest());

					
					// Process only the uploaded item called "fileToUpload" and
					// save it on disk
					for (final Iterator<FileItem> it = items.iterator(); it
							.hasNext();) {
						FileItem fi = it.next();
						if (fi.getFieldName().equals(UPLOAD_FILE_FIELD)) {
							found = true;

							// We create the newly uploaded file with <deployId>.<instId>.<dateTime>
							
							String filename = deployId+"."+instanceId+"."+(new Date()).getTime();
							//File with no extension
							file = new File(uploadDir, filename);
							fi.write(file);
						}
						
						
					}
				} catch (FileUploadException e1) {

					e1.printStackTrace();
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Request could not be parsed", e1);  

				} catch (Exception e) {
					
					e.printStackTrace();
					throw new ResourceException(Status.SERVER_ERROR_INSUFFICIENT_STORAGE, "Could not write the uploaded files", e);  

				}

				if(found){
					//We read the binary file, and convert it to hex
					byte[] fileBytes;
					try {
						fileBytes = FileUtils.readFileToByteArray(file);
					} catch (IOException e) {
						e.printStackTrace();
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unknown error while trying to read the uploaded file");
					}
					
					String code = null;
					if (app.getLdShakeMode()==false){
						if(this.getReference().getIdentifier().endsWith("hexconv")){
							code = toHexString(fileBytes);
						}else if(this.getReference().getIdentifier().endsWith("64conv")){
							code = Base64.encode(fileBytes);
						}else throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect resource request");
					}else{
						if(this.getReference().getIdentifier().contains("hexconv")){
							code = toHexString(fileBytes);
						}else if(this.getReference().getIdentifier().contains("64conv")){
							code = Base64.encode(fileBytes);
						}else throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect resource request");						
					}

					//We build the response as HTML wrapping the data (json form)
					code = "<html><body><textarea>"+code+"</textarea></body></html>";
					
					Representation answer = new StringRepresentation(code, MediaType.TEXT_HTML);
					return answer;

				}else {
			        // Some problem occurred, the request probably was not valid.
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "No file was uploaded");  
			    }
				
				
				
			} else {
			    // POST request with no entity.
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect request");  
			}
		} catch (ResourceException e) {
			throw e;
		}finally{
            discardRepresentation(entity);        // fix to mimick GLUElet Manager corrections for connection stability
		}
		
	}
	
	
	public static String toHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			// look up high nibble char
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);

			// look up low nibble char
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	// table to convert a nibble to a hex char.
	static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f' };
	
	
	/**
	 * PUT ToolInstanceListResource
	 * 
	 * Creation of a deploy's tool instances in Glueps (and gluelet manager).
	 * Variant that receives an xml with the configuration, and just forwards
	 * the creation to the GM
	 * 
	 * Dispatch the configuration data to the Gluelet Manager in order to
	 * trigger the actual creation.
	 * 
	 * 
	 * @return Atom/XML with the GM response, including the updated location of the
	 *         created tool instance.
	 */
	@Put("xml")
	public Representation createToolInstance(Representation entity) {

	
		try {
			GLUEPSManagerApplication app = (GLUEPSManagerApplication)this.getApplication();
			if(doAuthentication){
				if (app.getLdShakeMode()==false){
					//If we are coming through an unguarded call, we forbid the deletion
					if(this.getRequest().getChallengeResponse()==null || this.getRequest().getChallengeResponse().getIdentifier()==null)
						 throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not allowed to do that!");
			    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
					login = this.getRequest().getChallengeResponse().getIdentifier();
					if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
				}
				else{
		    		if (this.getRequest().getChallengeResponse()!=null){
		    	    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
		    			login = this.getRequest().getChallengeResponse().getIdentifier();
		    			if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
		    		}
		    		else{
		    			login = deploy.getAuthor();
		    			//Check if a sectoken has been provided
		    			Reference ref = getReference();
		    			Form query = ref.getQueryAsForm();
		    			String sectoken = query.getFirstValue("sectoken");
		    			if (sectoken!=null){
		    				JpaManager dbmanager = JpaManager.getInstance();
		    				SectokenEntity ste = dbmanager.findSectokenById(deployId);
		    				if (ste==null || !ste.getSectoken().equals(sectoken)){
		    					throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken is not valid for this deploy");
		    				}
		    			}else{
		    				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken parameter is missing");
		    			}
		    		}
				}
			}

			//Set the upload and schemas paths
			//GLUEPSManagerApplication app = (GLUEPSManagerApplication)this.getApplication();
			String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
			String SCHEMA_LOCATION = app.getAppPath()+"/schemas/";
			String uploadDir = UPLOAD_DIRECTORY+"temp/";

			// load Atom+GLUE formatted entity
			FormattedEntry inputEntry = null;
			String entityText = null;
			try {
				entityText = entity.getText();
				//inputEntry = new FormattedEntry(entityText);
				logger.info("** PUT INSTANCE received: \n" + entityText);
				
			} catch (IOException e) {
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Request couldn't be parsed", e);  
			}
			
			
			FormattedEntry responseEntry = createInstanceGM(entityText);
			
			//we do not update the deploy with the new URL (the gui does it) 
			// it can make the deploy to be slightly outdated, but we avoid concurrency problems in batches, I guess...
			
			return responseEntry.getRepresentation();
		} catch (ResourceException e) {
			throw e;
		} finally {
            discardRepresentation(entity);        // fix to mimick GLUElet Manager corrections for connection stability
		}
		

	}


	private FormattedEntry createInstanceGM(String configData) {

		String toolKind = deploy.getInstanceToolKind(instance.getId());
		// In case we get the whole URL as the tool type, we get only the last
		// part (toolTypeID)
		String toolTypeURL = deploy.getInstanceToolType(instance.getId());
		String toolType = toolTypeURL
				.substring(toolTypeURL.lastIndexOf("/") + 1);

		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this
				.getApplication();

		if (toolKind.equals(Resource.TOOL_KIND_EXTERNAL)) {
			// if it is external, we look through all the instanced activities
			// that reference it and add its users to the list of users for that
			// instance
			String[] participants = deploy
					.getParticipantUsernamesForToolInstance(instance.getId());
			FormattedEntry answerEntry = null;

			//Workaround to modify page title in the GSIC MEdiaWiki tools, to avoid errors when creating multiple wiki pages with the same title
			if(toolType.equals(app.getGSICMWGMType())) configData = fixMWTitleInConfig(configData);
			
			try {
				// We already have the tool configuration as the input

				// We do the creation in the GM, with retries in case it timed
				// out
				int counter = 0;
				String url = null;
				do {
					if (counter > 0)
						System.out.println("Retrying for the " + counter
								+ " time");
					answerEntry = this.createToolInstanceGM(toolType,
							configData, deploy.getStaffUsernames(),
							participants);

					String createdInstanceId = answerEntry.getId();
					System.out.println("Created the instance "
							+ createdInstanceId);
					url = answerEntry.getLinks().get(0);
					System.out.println("The end-user URL is " + url);

					counter++;
				} while (url == null && counter <= app.getGmConnRetries());

				// If it was successful, get the URL and update it in the tool
				// instance
				// We take into account that the Moodle may see the GM at a
				// different address than we did
				
				if(url==null) throw new ResourceException(Status.SERVER_ERROR_SERVICE_UNAVAILABLE, "Could not create the instance. Please try again later");
				
				if(url.startsWith(app.getGmurl())){
					System.out.println("Successfully created instance. Updating location to "+url); 
					instance.setLocation(new URL(url));
	   				deploy.getToolInstanceById(this.instance.getId()).setLocation(new URL(url));
					QrCodeAdaptor.setQrCodePosition(deploy.getToolInstanceById(this.instance.getId()));
					
				}else{
					String prefix = url.substring(0, url.lastIndexOf("GLUEletManager/")+15);
					String newURL = url.replace((CharSequence) prefix, (CharSequence) app.getGmurl());
					System.out.println("Successfully created instance. Updating (transformed) location to "+newURL); 
					instance.setLocation(new URL(newURL));
	   				deploy.getToolInstanceById(this.instance.getId()).setLocation(new URL(newURL));
					QrCodeAdaptor.setQrCodePosition(deploy.getToolInstanceById(this.instance.getId()));
	   				//String newEntry = answerEntry.getTextContent().replace((CharSequence) prefix, (CharSequence) app.getGmurl());
					//String newEntry = answerEntry.getXhtmlContentAsText().replace((CharSequence) prefix, (CharSequence) app.getGmurl());
					String newEntry = answerEntry.getRepresentation().getText().replace((CharSequence) prefix, (CharSequence) app.getGmurl());
					answerEntry = new FormattedEntry(newEntry);
				}
				
				
				
				
			} catch (Exception e) {
				// There was some kind of error in the instance creation, we
				// leave the location blank
				System.out
						.println("There was an error in the creation of instance "
								+ instance.getId() + ": " + e.getMessage());
				
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Could not create the instance due to an unknown server error");
			}

			return answerEntry;
		}// if it is internal, we do nothing - for now
			// TODO: depending on the internal tool, we might want to create the
			// instance also
		return null;
	}

	
	private String fixMWTitleInConfig(String configData) {

		//<title xsi:type="xs:string">Title</title>
		String search = "</title>";
		int index = -1;
		if((index=configData.indexOf(search))!=-1){
			
			String prefix = configData.substring(0, configData.lastIndexOf(">", index)+1);
			System.out.println("prefix: "+prefix);
			String suffix = configData.substring(configData.lastIndexOf(">", index)+1);
			System.out.println("suffix: "+suffix);
			String newConfigData = prefix+(new Date()).getTime()+":"+suffix;
			System.out.println("Configuration after workaround: "+newConfigData);
			return newConfigData;
		}else return configData;
		
	}


	
	private FormattedEntry createToolInstanceGM(String toolType, String configData,
			String[] teachers, String[] participants) throws Exception {

		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this
				.getApplication();

		
		System.out.println("trying to create the instance of type "+toolType+" at URL "+app.getGmurlinternal() + "instance");
		try {
			String response = doPostToUrl(app.getGmurlinternal() + "instance", buildStringRepresentation(toolType, configData, teachers, participants), "application/atom+xml");
			FormattedEntry answerEntry = new FormattedEntry(response);
			return answerEntry;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		
	}

	
	private String buildStringRepresentation(String toolType, String configData,
		String[] teachers, String[] participants) throws IOException {
	
		try {
			return (buildRepresentation(toolType, configData, teachers, participants)).getText();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
	}

	
	
	private Representation buildRepresentation(String toolId, String configurationData, String[] teachersNames, String[] usersNames) {
		String creduser = deploy.getLearningEnvironment().getCreduser();
		FormattedEntry entry = new FormattedEntry();
		entry.addExtendedTextChild("tool", toolId);
		if (configurationData != null && configurationData.length() > 0)
			entry.addExtendedStructuredElement("configuration", configurationData);
		else
			entry.addExtendedTextChild("configuration", "");

		//We set the users - the glueps user login is always in the list of users, and it is the caller user of this request (?)
		
		String[] newUsers = null;
		if(teachersNames!=null && teachersNames.length>0){
			//teacher = teachersNames[0];
			//If there are teachers, we just add all of them to the users list
			
			if(usersNames!=null && usersNames.length>0){//if there are students in the group
				newUsers = new String[usersNames.length+teachersNames.length+1];
				for(int i = 0; i<usersNames.length; i++) newUsers[i] = usersNames[i];
				for(int i = 0; i<teachersNames.length; i++) newUsers[i+usersNames.length]=teachersNames[i];
				newUsers[usersNames.length+teachersNames.length]=creduser;
			}else{//if there are no students, the teachers are the only users
				newUsers = new String[teachersNames.length+1];
				for(int i = 0; i<teachersNames.length; i++) newUsers[i]=teachersNames[i];
				newUsers[teachersNames.length] = creduser;
			}
		}else{//No teachers, we just put the logged in user as callerUser
			//teacher = login;
			if(usersNames!=null && usersNames.length>0){
				newUsers = new String[usersNames.length+1];
				for(int i = 0; i<usersNames.length; i++) newUsers[i] = usersNames[i];
				newUsers[usersNames.length] = creduser;
			}else{
				newUsers = new String[1];
				newUsers[0] = creduser;
			}
		}
		
		entry.addExtentedStructuredList("users", "user", newUsers);
		//entry.addExtendedTextChild("callerUser", teacher);			
		//entry.addExtendedTextChild("callerUser", login);
		entry.addExtendedTextChild("callerUser", creduser);
		
		return entry.getRepresentation();
	}

	

		
}
	
	
	
	

