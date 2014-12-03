package glueps.core.resource;

import glue.common.format.FormatStatic;
import glue.common.format.FormattedEntry;
import glue.common.format.FormattedFeed;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;

import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import org.restlet.resource.Get;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import glueps.adaptors.ld.ILDAdaptor;
import glueps.adaptors.ld.LDAdaptorFactory;
import glueps.adaptors.ld.imsld.IMSLDAdaptor;
import glueps.adaptors.vle.IDynamicVLEDeployer;
import glueps.adaptors.vle.IStaticVLEDeployer;
import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.VLEAdaptorFactory;
import glueps.core.controllers.exist.Exist;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.gluepsManager.GLUEPSManagerServerMain;
import glueps.core.model.AsynchronousOperation;
import glueps.core.model.Course;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.Group;
import glueps.core.model.InstancedActivity;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.Participant;
import glueps.core.model.ToolInstance;
import glueps.core.model.Resource;
import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.DeployEntity;
import glueps.core.persistence.entities.DeployVersionEntity;
import glueps.core.persistence.entities.DesignEntity;
import glueps.core.persistence.entities.SectokenEntity;
import glueps.core.persistence.entities.UserEntity;
import glueps.core.service.inprocess.InProcessInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Resource 'list of deploy' (tool implementations, indeed).
 * 
 * List of all the registered tool implementations available to create Gluelets. 
 * 
 * @author	 	Juan Carlos A. <jcalvarez@gsic.uva.es>
 * @version 	
 * @package 	glueps.core.resources
 */

public class DeployListResource extends GLUEPSResource {
	
	private static final String IMPORT_DEPLOY_INSTANCES_FIELD = "checkImportToolsConf";
	
	private static final String NEW_DEPLOY_TITLE_FIELD = "NewDeployTitleName";
	private static final String NEW_DEPLOY_FILE_FIELD = "archiveWic";
	private static final String NEW_DEPLOY_VLE_FIELD = "vleSelect";
	private static final String NEW_DEPLOY_AUTHOR_FIELD = "newDeployTextBoxTeacher";
	private static final String NEW_DEPLOY_TYPE_FIELD = "instType";
	private static final String NEW_DEPLOY_INCR_CHECK = "checkDeploy";
	private static final String NEW_DEPLOY_INCR_LESSON = "temaSelect";
	
	private static final String NEW_DEPLOY_INCR_CHECK_VALUE = "agreed";

	private static final String NEW_DEPLOY_COURSE_FIELD = "courseSelect";
	
	private static final String NEW_DEPLOY_SEC_TOKEN = "sectoken";
	private static final String NEW_DEPLOY_VLEDATA = "vleData";
	private static final String NEW_DEPLOY_FRAME_ORIGIN = "ldshake_frame_origin";
	
	
	//private static final Object ICOLLAGE_DEPLOY_TYPE = "WIC";
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");


	/**
	 * GET DeployListResource
	 * 
	 * Returns a list with all registered deploy.
	 * 
	 * 
	 * @return	'Atomized' list of tool implementations known by GLUEletManager
	 */
	//@Get("atom")
	@Get("xml|html")
    public Representation getDeploys() {
    	
   		logger.info("** GET DEPLOYS received");
    	
   		FormattedFeed feed = new FormattedFeed();
		String uri = getReference().getIdentifier();
		
		uri = doGluepsUriSubstitution(uri);
		
		// Atom standard elements
		feed.setId(uri);  
		feed.setTitle("List of deploys (deploy)");
		feed.addAuthor(FormatStatic.GSIC_NAME, null, null);
		feed.setSelfLink(null, uri);
		
		
		String login = this.getRequest().getChallengeResponse().getIdentifier();
		JpaManager dbmanager = JpaManager.getInstance();
		try {
			List<Deploy> deploys = dbmanager.listDeployObjectsByUser(login);
			for(Deploy deploy : deploys){
				DeployResource deployItem = new DeployResource();
				deployItem.setDeploy(deploy.getId(), getReference().toString());
				feed.getEntries().add(deployItem.getDeployEntry());
			}
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Internal error while trying to retrieve deploys from DB", e);
		}
		
		
		Representation answer = feed.getRepresentation();
		
		try {
			logger.info("** GET DEPLOYS answer: \n" + (answer != null ? answer.getText() : "NULL"));
		} catch (IOException io) {
			throw new RuntimeException("Extremely weird failure while trying to log", io);
		}
		return answer;
		
	}
    
	/**
	 * GET DeployListResource
	 * 
	 * Returns a list with all registered deploy as a JSON
	 * 
	 * 
	 * @return	JSON list of tool implementations known by GLUEletManager
	 */
    @Get("json")
    public Representation getJsonDeploys() {
    	
   		logger.info("** GET JSON DEPLOYS received");
   		Representation answer = null;
		
		String login = this.getRequest().getChallengeResponse().getIdentifier();
		JpaManager dbmanager = JpaManager.getInstance();
		ArrayList<HashMap<String, String>> deployList = new ArrayList<HashMap<String, String>>();
		UserEntity user = dbmanager.findUserByUsername(login);
		List<DeployEntity> deployEntities = dbmanager.listDeploysByUser(user.getId());
		for (DeployEntity deployEntity : deployEntities) {
			HashMap<String, String> prop = getPropertiesJsonDeploy(getReference().toString(), deployEntity);
			deployList.add(prop);
		}
				
		// convert java object to JSON format,
		// and returned as JSON formatted string
		Gson gson = new Gson();
		String json = gson.toJson(deployList);
		
   		if (json != null){
   			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
		
		logger.info("** GET JSON DEPLOYS answer: \n" + (answer != null ? json : "NULL"));
		return answer;
		
	}
    
    /**
     * Get the information of the deploy which is going to be shown in the list of deploys in JSON format
     * @param feedReference The reference to the DeployListResource
     * @param deployEntity The deploy whose information is going to be gotten
     * @return The information of the deploy as a HashMap<String, String> of pairs property,value
     */
	private HashMap<String, String> getPropertiesJsonDeploy(String feedReference, DeployEntity deployEntity) {
		HashMap<String, String> prop = new HashMap<String, String>();
		// / build URI
		String uri = null;
		uri = feedReference + "/" + deployEntity.getId();
		uri = doGluepsUriSubstitution(uri);
		prop.put("id", uri);
		prop.put("name", deployEntity.getName());
		prop.put("complete", String.valueOf(deployEntity.getComplete()));
		if (deployEntity.getComplete()){
			if (deployEntity.getStaticDeploy()!=null) prop.put("staticDeploy", deployEntity.getStaticDeploy());
			if (deployEntity.getLiveDeploy()!=null) prop.put("liveDeploy", deployEntity.getLiveDeploy());
		}
		String designUri = (new Reference(feedReference)).getParentRef().getIdentifier()+"designs/"+trimId(deployEntity.getDesignid());
       	designUri = doGluepsUriSubstitution(designUri);
       	prop.put("design", designUri);
		return prop;
	}
	
    /**
     * Update the database fields of every DeployEntity with the information contained in its Deploy xmlfile
     * @throws UnsupportedEncodingException 
     */
	@Put()
    public Representation updateDeployEntities(Representation entity){
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		if (app.getLetPutUpdateTables()){
			String feedReference = getReference().toString();
			Representation answer = null;
	    	logger.info("** Starting updating glueps_deploys table");
	    	JpaManager dbmanager = JpaManager.getInstance();
	    	List<DeployEntity> deployEntities = dbmanager.listDeploys();
	    	if(deployEntities!=null){
	    		for(DeployEntity de : deployEntities){
	    			Deploy deploy = dbmanager.findDeployObjectById(de.getId());
	    			if (deploy.getName()==null || deploy.getName().isEmpty()){
	    				de.setName("default name");
	    			}else{
	    				de.setName(deploy.getName());
	    			}
	    			de.setComplete(deploy.isComplete());
	    			if (de.getComplete()){
	               		//we get the learningEnvironment of the deploy, and check whether the deployment is static, dynamic or both, by looking at its adaptor
	               		VLEAdaptorFactory fact = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
	               		IVLEAdaptor adaptor = fact.getVLEAdaptor(deploy.getLearningEnvironment());
	                	String uri =feedReference+"/" + deploy.getId();
	                    uri = doGluepsUriSubstitution(uri);
	               		if(deploy.getStaticDeployURL()!=null && adaptor instanceof IStaticVLEDeployer){
	               			de.setStaticDeploy(uri+"/static");
	               		}
	               		if(deploy.getLiveDeployURL()!=null && adaptor instanceof IDynamicVLEDeployer){
	               			de.setLiveDeploy(uri+"/live");
	               		}
	    			}
	    			//Make the changes permanent
	    			dbmanager.insertDeploy(de);
	    		}
	    	}
	    	logger.info("** Finished updating glueps_deploys table");
	    	return answer;
	    }else{
	    	throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "updating glueps_deploys table is not allowed");
	    }
    }
    
    
	@Post("multipart")
	public Representation postDeployForm(Representation entity) {
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		//Get the multipart request arguments as a FiletItem
		List<FileItem> items = parseRequestAsFileItems();
		//If a designId is provided, we create a deploy for an existing design
		if (this.getRequest().getAttributes().get("designId")!=null)
		{
			if (app.isResponseAsAService()){
				return createDeployFormService(entity, items);
			}else{
				return createDeployForm(entity, items);
			}
		}
		for (final Iterator<FileItem> it = items.iterator(); it.hasNext();) {
			FileItem fi = it.next();
			if(fi.getFieldName().equals(NEW_DEPLOY_TYPE_FIELD)){
				String deployType = fi.getString();
				//If it is a glueps type deploy, we are importing a deploy in the Glueps format
				//The glueps type deploy already contains a design inside
				if (deployType.equals(LDAdaptorFactory.GLUEPS_TYPE)){
					if (app.isResponseAsAService()){
						return importDeployFormService(entity, items);						
					}else{
						return importDeployForm(entity, items);
					}
				}
			}
		}
		//Otherwise, we create both the deploy and its design. 		
		if (app.isResponseAsAService()){
			return postDesignDeployFormService(entity, items);
		}else{
			return postDesignDeployForm(entity, items);
		}
	}
	
	/**
	 * 
	 * Creation Deploy in Glueps. Variant that receives a multipart form
	 * containing a deployment file
	 * 
	 * @return URI to new Deploy resource, or null.
	 */
	private Representation postDesignDeployForm(Representation entity, List<FileItem> items) {

		String login = this.getRequest().getChallengeResponse().getIdentifier();

		String designUploadDir = null;
		String designId = null;
		Design design = null;
		String designUrl = null;

		String deployUploadDir = null;
		String deployId = null;
		Deploy deploy = null;
		String deployUrl = null;

		String deployTitle = null;
		String deployType = null;
		Boolean importToolConf = false;
		String sectoken = null;
		String vleId = null;
		String courseId = null;
		String ldshakeFrameOrigin = null;

		HashMap<String, Participant> vleUsers = null;
		HashMap<String, Group> vleGroups = null;
		HashMap<String, String> courses = null;

		// Set the upload and schemas paths
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		String UPLOAD_DIRECTORY = app.getAppPath() + "/uploaded/";

		// This will contain the LF deploy xml
		String deployData = null;

		boolean found = false;
		boolean foundVleDataFile = false;
		File file = null;
		File vledataFile = null;

		try {

			// Process only the uploaded item called "fileToUpload" and
			// save it on disk
			for (final Iterator<FileItem> it = items.iterator(); it.hasNext();) {
				FileItem fi = it.next();
				if (fi.getFieldName().equals(NEW_DEPLOY_FILE_FIELD)) {
					found = true;

					// Create a new designId for the design
					do {
						designId = "" + (new Random()).hashCode();
						designUrl = getReference().getParentRef().getIdentifier() + "designs/" + designId;
						designUploadDir = UPLOAD_DIRECTORY + designId + File.separator;
					} while ((new File(designUploadDir)).exists());

					// Create the newly uploaded deployment folder where
					// the deployment will be stored
					do {
						deployId = designId + Deploy.ID_SEPARATOR + (new Random()).hashCode();
						deployUrl = getReference().getIdentifier() + "/" + deployId;
						deployUploadDir = UPLOAD_DIRECTORY + deployId + File.separator;
					} while ((new File(deployUploadDir)).exists());
					
					(new File(deployUploadDir)).mkdirs();

					// Store the design file
					(new File(designUploadDir)).mkdirs();
					file = new File(designUploadDir, trimPath(fi.getName()));
					fi.write(file);

				} else if (fi.getFieldName().equals(NEW_DEPLOY_TITLE_FIELD)) {
					deployTitle = fi.getString("UTF-8");
				} else if (fi.getFieldName().equals(NEW_DEPLOY_TYPE_FIELD)) {
					deployType = fi.getString("UTF-8");
				} else if(fi.getFieldName().equals(IMPORT_DEPLOY_INSTANCES_FIELD)){
					importToolConf=true;
				} else if(fi.getFieldName().equals(NEW_DEPLOY_VLE_FIELD)){
					vleId=fi.getString("UTF-8");
				} else if(fi.getFieldName().equals(NEW_DEPLOY_COURSE_FIELD)){
					courseId=fi.getString("UTF-8");
			
				//Parameters from an LdShake POST request
				} else if (fi.getFieldName().equals(NEW_DEPLOY_SEC_TOKEN)) {
					sectoken = fi.getString("UTF-8");
				} else if (fi.getFieldName().equals(NEW_DEPLOY_VLEDATA)) {
					foundVleDataFile = true;
					vledataFile = new File(UPLOAD_DIRECTORY, (new Random()).hashCode() + "_" + trimPath(fi.getName()));
					fi.write(vledataFile);
				} else if (fi.getFieldName().equals(NEW_DEPLOY_FRAME_ORIGIN)) {
					ldshakeFrameOrigin = fi.getString("UTF-8");
				}

			}
		} catch (FileUploadException e1) {

			e1.printStackTrace();
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Request could not be parsed", e1);

		} catch (Exception e) {

			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INSUFFICIENT_STORAGE,"Could not write the uploaded files", e);

		}

		// Once handled, the content of the uploaded file is sent
		// back to the client.
		if (!found) {
			// Some problem occurred, the request probably was not valid.
			// rep = new StringRepresentation("no file uploaded",
			// MediaType.TEXT_PLAIN);
			// setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"No file was uploaded");
		}
		if (deployTitle==null || deployTitle.equals("")){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, NEW_DEPLOY_TITLE_FIELD + " parameter with the deploy name is missing");
		}
		if (deployType==null || deployType.equals("")){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, NEW_DEPLOY_TYPE_FIELD + " parameter with the deploy type is missing");
		}
		if (foundVleDataFile && sectoken==null){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"sectoken parameter with the Sectoken is missing");
		}
			
		LDAdaptorFactory factory = new LDAdaptorFactory(app);

		ILDAdaptor ldAdaptor = factory.getLDAdaptor(deployType, designId);

		design = ldAdaptor.fromLDToLF(file.toString());
		if (design == null)
			throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY,"Failed to parse the provided design");

		// set the title of the design with the name received
		// from the request, not the one in IMSLD, and also set the id
		design.setName(deployTitle);
		design.setAuthor(login);
		design.setId(designUrl);

		// We store the deurlified design in DB
		JpaManager dbmanager = JpaManager.getInstance();
		try {
			dbmanager.insertDesign(deURLifyDesign(design));
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,"Error while trying to insert the design in DB", e);
		}
		
		LearningEnvironment leObj = null;
        //If a vleData is provided, we get the learning environment and course identifiers from the vleData        
        if (vleId == null && foundVleDataFile){
    		String jsonString = getJsonVledata(vledataFile);
    		JsonElement rootElement = new JsonParser().parse(jsonString);
    		JsonElement leElement = rootElement.getAsJsonObject().get("learningEnvironment");
    		Gson gson = new Gson();
    		
    		leObj = gson.fromJson(leElement,LearningEnvironment.class);
    		//Get the identifier of this learning environment in Glueps
    		vleId = getGluepsLeId(leObj, login);

    		JsonElement courseElement = rootElement.getAsJsonObject().get("course");
    		Course courseObj = gson.fromJson(courseElement, Course.class);
    		courseId = courseObj.getId();        	
        }       
		if(vleId!=null){
						
			LearningEnvironmentResource ler = new LearningEnvironmentResource();
			ler.setLearningEnvironment(vleId, vleId);
				
			leObj = ler.getCompleteLEObject();
		}
		else{
			//The le doesn't exist in Glueps
			if (foundVleDataFile){
				LearningEnvironmentResource ler = new LearningEnvironmentResource();
				leObj.setId(null);
				ler.setLearningEnvironment(leObj);
					
				leObj = ler.getCompleteLEObject();
			}
			//neither vleSelect nor vledataFile has been provided
			else{
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect request. No VLE specified");  
			}
		}

			// The learning environment already contains the external tools,
			// internal tools and courses information
			// Should we check if this content is updated?
			// ler.getCompleteLEObject();

			System.out.println("Trying to contact VLE: " + leObj.getAccessLocation().toString());

			VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
			IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(leObj);

			// If we got a course ID, we get the course users, and if not, all the VLE users
			if (courseId == null)
				vleUsers = adaptor.getUsers(leObj.getAccessLocation().toString());
			else {
				vleUsers = adaptor.getCourseUsers(leObj.getAccessLocation().toString(), trimId(courseId));
				vleGroups = adaptor.getCourseGroups(leObj.getAccessLocation().toString(), trimId(courseId));
			}

			if (app.getOnlyUserCourses()){
				courses = adaptor.getCourses(leObj.getAccessLocation().toString(), leObj.getCreduser());
			}
			else{
				courses = adaptor.getCourses(leObj.getAccessLocation().toString());
			}

			if (vleUsers != null) {

				// We set the deployId for all participants (the Moodle adaptor
				// does not know it!)
				for (Iterator<String> it = vleUsers.keySet().iterator(); it.hasNext();) {
					String key = (String) it.next();
					Participant p = vleUsers.get(key);
					p.setDeployId(deployId);
					vleUsers.put(key, p);
				}
			}

			if (vleGroups != null) {
				// We set the deployId for all groups (the Moodle adaptor does
				// not know it!)
				for (Iterator<String> it = vleGroups.keySet().iterator(); it.hasNext();) {
					String key = (String) it.next();
					Group g = vleGroups.get(key);
					g.setDeployId(deployId);
					vleGroups.put(key, g);
				}

			}
	        
		/*}else{
			// Some problem occurred, we could not get the list of users
			//This is the ldshake case
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect request. No VLE specified");  
		}*/


		//Set the path to the deploy file
		String filePath = "";
		if (deployType.equals(LDAdaptorFactory.IMSLD_TYPE)) {
			// path to the icmanifest, if it exists
			filePath = designUploadDir + "icmanifest.xml";
		} else if (deployType.equals(LDAdaptorFactory.PPC_TYPE)) {
			// There is not a deploy file
			filePath = "";
		} else if (deployType.equals(LDAdaptorFactory.T2_TYPE)) {
			// The design and deploy files are the same
			filePath = designUploadDir + file.getName();
		}

		if (new File(filePath).exists()) {

			// TODO: What happens if vleUsers==null??
			deploy = ldAdaptor.processInstantiation(filePath, design, vleGroups, vleUsers);
			if (deploy == null)
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Error while processing instantiation file. Probably bad format");

		} else {// We found no xml instantiation file, we have to create an empty deploy: no groups unless present in VLE, no instancedactivities, no toolinstances, list of participants from the VLE

			// Since right now we cannot create users from scratch (they
			// have to come from the LD tool or from the VLE), if the
			// vleUsers == null, we through an error
			if (vleUsers == null)
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"Error while processing instantiation file. No participants provided, and no participants are enrolled in the course!");
			else {				
				deploy = ldAdaptor.processInstantiation(null, design, vleGroups, vleUsers);
				if (deploy == null){			   
					ArrayList<Participant> parts = new ArrayList<Participant>(vleUsers.values());
					ArrayList<Group> groups = null;
					if(vleGroups!=null){
						groups = new ArrayList<Group>(vleGroups.values());
					}
					deploy = new Deploy(null, design, null, null, null, new Date(), null, null, null, parts, groups);
				}
			}
		}

		// set the title of the deploy with the name received
		// from the request
		deploy.setName(deployTitle);
		deploy.setId(deployId);

		// We now ignore the author from the form, and use the login to
		// GLUEPS
		// deploy.setAuthor(author);
		deploy.setAuthor(login);

		deploy.setLearningEnvironment(leObj);

		deploy.setCourse(new Course(courseId, courses.get(trimId(courseId)),null, null, null));
		
		deploy.setLdshakeFrameOrigin(ldshakeFrameOrigin);

		// We fix the toolInstance ids so that they are GLUEPS Urls
		// deploy = fixToolInstanceIds(deploy);
		// Supposedly, not needed... the toolinstances should not be URLs in
		// DB

		// We fix the resource tool kinds, which were probably lost in the
		// import process (now that we know the LE)
		//deploy = fixToolKinds(deploy);

		// Guardo Xml en base de datos
		Deploy deurlifiedDeploy;
		try {
			deurlifiedDeploy = deURLifyDeploy(deploy);
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,"Error while processing the generated deploy", e);
		}
		
		deurlifiedDeploy = fixToolKinds(deurlifiedDeploy);	
		
		dbmanager = JpaManager.getInstance();
		try {
			dbmanager.insertDeploy(deurlifiedDeploy);
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,"Error while trying to insert the generated deploy into the DB",e);
		}
		
		if (sectoken!=null){
			//Save the sectoken in the database
			SectokenEntity sectEnt = new SectokenEntity(deployId, sectoken);
			dbmanager.insertSectoken(sectEnt);
		}

		// We urlify the deploy to be returned
		Deploy urlifiedDeploy;
		try {
			urlifiedDeploy = URLifyDeploy(deurlifiedDeploy, getReference().getParentRef().getIdentifier());
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,"Error while processing the generated deploy to return", e);
		}
		deployData = generateXML(urlifiedDeploy, glueps.core.model.Deploy.class);

		Representation answer = new StringRepresentation((CharSequence) deployData, MediaType.TEXT_XML);
		answer.setCharacterSet(CharacterSet.UTF_8);
		return answer;

	}
	
	/**
	 * 
	 * Creation Deploy in Glueps. Variant that receives a multipart form
	 * containing a deployment file
	 * 
	 * @return URI to new Deploy resource, or null.
	 */
	private Representation postDesignDeployFormService(Representation entity, List<FileItem> items) {

		String login = this.getRequest().getChallengeResponse().getIdentifier();

		String designUploadDir = null;
		String designId = null;
		Design design = null;
		String designUrl = null;

		String deployUploadDir = null;
		String deployId = null;
		Deploy deploy = null;
		String deployUrl = null;
		Representation answer = null;

		String deployTitle = null;
		String deployType = null;
		Boolean importToolConf = false;
		String sectoken = null;
		String vleId = null;
		String courseId = null;
		String ldshakeFrameOrigin = null;

		// Set the upload and schemas paths
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		String UPLOAD_DIRECTORY = app.getAppPath() + "/uploaded/";

		// This will contain the LF deploy xml
		String deployData = null;

		boolean found = false;
		boolean foundVleDataFile = false;
		File file = null;
		File vledataFile = null;

		try {

			// Process only the uploaded item called "fileToUpload" and
			// save it on disk
			for (final Iterator<FileItem> it = items.iterator(); it.hasNext();) {
				FileItem fi = it.next();
				if (fi.getFieldName().equals(NEW_DEPLOY_FILE_FIELD)) {
					found = true;

					// Create a new designId for the design
					do {
						designId = "" + (new Random()).hashCode();
						designUrl = getReference().getParentRef().getIdentifier() + "designs/" + designId;
						designUploadDir = UPLOAD_DIRECTORY + designId + File.separator;
					} while ((new File(designUploadDir)).exists());

					// Create the newly uploaded deployment folder where
					// the deployment will be stored
					do {
						deployId = designId + Deploy.ID_SEPARATOR + (new Random()).hashCode();
						deployUrl = getReference().getIdentifier() + "/" + deployId;
						deployUploadDir = UPLOAD_DIRECTORY + deployId + File.separator;
					} while ((new File(deployUploadDir)).exists());
					
					(new File(deployUploadDir)).mkdirs();

					// Store the design file
					(new File(designUploadDir)).mkdirs();
					file = new File(designUploadDir, trimPath(fi.getName()));
					fi.write(file);

				} else if (fi.getFieldName().equals(NEW_DEPLOY_TITLE_FIELD)) {
					deployTitle = fi.getString("UTF-8");
				} else if (fi.getFieldName().equals(NEW_DEPLOY_TYPE_FIELD)) {
					deployType = fi.getString("UTF-8");
				} else if(fi.getFieldName().equals(IMPORT_DEPLOY_INSTANCES_FIELD)){
					importToolConf=true;
				} else if(fi.getFieldName().equals(NEW_DEPLOY_VLE_FIELD)){
					vleId=fi.getString("UTF-8");
				} else if(fi.getFieldName().equals(NEW_DEPLOY_COURSE_FIELD)){
					courseId=fi.getString("UTF-8");
			
				//Parameters from an LdShake POST request
				} else if (fi.getFieldName().equals(NEW_DEPLOY_SEC_TOKEN)) {
					sectoken = fi.getString("UTF-8");
				} else if (fi.getFieldName().equals(NEW_DEPLOY_VLEDATA)) {
					foundVleDataFile = true;
					vledataFile = new File(UPLOAD_DIRECTORY, (new Random()).hashCode() + "_" + trimPath(fi.getName()));
					fi.write(vledataFile);
				} else if (fi.getFieldName().equals(NEW_DEPLOY_FRAME_ORIGIN)) {
					ldshakeFrameOrigin = fi.getString("UTF-8");
				}

			}
		} catch (FileUploadException e1) {

			e1.printStackTrace();
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Request could not be parsed", e1);

		} catch (Exception e) {

			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INSUFFICIENT_STORAGE,"Could not write the uploaded files", e);

		}

		// Once handled, the content of the uploaded file is sent
		// back to the client.
		if (!found) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"No file was uploaded");
		}
		if (deployTitle==null || deployTitle.equals("")){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, NEW_DEPLOY_TITLE_FIELD + " parameter with the deploy name is missing");
		}
		if (deployType==null || deployType.equals("")){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, NEW_DEPLOY_TYPE_FIELD + " parameter with the deploy type is missing");
		}
		if (foundVleDataFile && sectoken==null){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"sectoken parameter with the Sectoken is missing");
		}
			
		LDAdaptorFactory factory = new LDAdaptorFactory(app);

		ILDAdaptor ldAdaptor = factory.getLDAdaptor(deployType, designId);

		design = ldAdaptor.fromLDToLF(file.toString());
		if (design == null)
			throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY,"Failed to parse the provided design");

		// set the title of the design with the name received
		// from the request, not the one in IMSLD, and also set the id
		design.setName(deployTitle);
		design.setAuthor(login);
		design.setId(designUrl);

		JpaManager dbmanager = JpaManager.getInstance();		
		AsynchronousOperation asynOper = new AsynchronousOperation(null, AsynchronousOperation.generateOperationId(), AsynchronousOperation.STATUS_INPROGRESS, "The deployment is being created", null, new Date(), null);
		long asynOperId;
		try{
			asynOperId = dbmanager.insertAsynchronousOperation(asynOper);
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the asynchronous operation into the DB", e);
		}
		
		Future<Deploy> process = GLUEPSManagerServerMain.pool.submit(new BackgroundLivePostDesignDeploy(vleId, vledataFile, courseId, login, designId, deployId, deployType, designUploadDir, file, design, deployTitle, ldshakeFrameOrigin, app, asynOperId));
		
		URLifyAsynchronousOperation(asynOper, doGluepsUriSubstitution(getReference().getParentRef().getIdentifier()));
   		String xmlfile = generateXML(asynOper, glueps.core.model.AsynchronousOperation.class);	
   		if (xmlfile != null){
   			answer = new StringRepresentation((CharSequence)xmlfile, MediaType.TEXT_XML);
   			answer.setCharacterSet(CharacterSet.UTF_8);
   		}				
		getResponse().setLocationRef(asynOper.getOperation());
		
		if (sectoken!=null){
			//Save the sectoken in the database
			SectokenEntity sectEnt = new SectokenEntity(deployId, sectoken);
			dbmanager.insertSectoken(sectEnt);
		}

		answer.setCharacterSet(CharacterSet.UTF_8);
		setStatus(Status.SUCCESS_ACCEPTED);
		return answer;

	}
	
	
	   /**
	 * 
	 * Creation Deploy in Glueps. Variant that receives a multipart form containing a deployment file
	 * 
	 * @return	URI to new Deploy resource, or null.
	 */
	private Representation importDeployForm(Representation entity, List<FileItem> items) {
		
		String login = this.getRequest().getChallengeResponse().getIdentifier();
		
		String designUploadDir = null;
		String designId = null;
		String designUrl = null;
		
		String deployUploadDir = null;
		String deployId = null;
		String deployUrl = null;
		
		String deployTitle = null;
		String deployType = null;
		Boolean importToolConf = false;
		String vleId = null;
		String courseId = null;
		String sectoken = null;
		String ldshakeFrameOrigin = null;
		
		HashMap<String, Participant> vleUsers = null;
		HashMap<String, Group> vleGroups = null;
		HashMap<String,String> courses = null;
		
		//Set the upload and schemas paths
		GLUEPSManagerApplication app = (GLUEPSManagerApplication)this.getApplication();
		String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
		String GM_URL = app.getGmurl();
		
		//This will contain the LF deploy xml
		String deployData = null;

		boolean found = false;
		boolean foundVleDataFile = false;
		File file = null;
		File vledataFile = null;
	
		try {
			// Process only the uploaded item called "fileToUpload" and
			// save it on disk
			for (final Iterator<FileItem> it = items.iterator(); it.hasNext();) {
				FileItem fi = it.next();
				if (fi.getFieldName().equals(NEW_DEPLOY_FILE_FIELD)) {
					found = true;
					
					//We create a new designId for the design
					do {
						designId = "" + (new Random()).hashCode();						
						designUrl = getReference().getParentRef().getIdentifier()+"designs/"+designId;
						designUploadDir = UPLOAD_DIRECTORY + designId + File.separator;						
					} while ((new File(designUploadDir)).exists());
					
					// We create the newly uploaded deployment folder where
					// the deployment will be stored
					do {
						deployId = designId+Deploy.ID_SEPARATOR+(new Random()).hashCode();					
						deployUploadDir = UPLOAD_DIRECTORY + deployId + File.separator;						
					} while ((new File(deployUploadDir)).exists());
					
					//We store the deployment file
					(new File(deployUploadDir)).mkdirs();					
					file = new File(deployUploadDir, deployId + "_original.xml");
					fi.write(file);

				}else if(fi.getFieldName().equals(NEW_DEPLOY_TITLE_FIELD)){
					deployTitle=fi.getString("UTF-8");
				}else if(fi.getFieldName().equals(NEW_DEPLOY_TYPE_FIELD)){
					deployType=fi.getString("UTF-8");
				}else if(fi.getFieldName().equals(IMPORT_DEPLOY_INSTANCES_FIELD)){
					importToolConf=true;
				}else if(fi.getFieldName().equals(NEW_DEPLOY_VLE_FIELD)){
					vleId=fi.getString("UTF-8");
				}else if(fi.getFieldName().equals(NEW_DEPLOY_COURSE_FIELD)){
					courseId=fi.getString("UTF-8");
				} 
				
				//Parameters from an LdShake POST request
				else if (fi.getFieldName().equals(NEW_DEPLOY_SEC_TOKEN)) {
					sectoken = fi.getString("UTF-8");
				} else if (fi.getFieldName().equals(NEW_DEPLOY_VLEDATA)) {
					foundVleDataFile = true;
					vledataFile = new File(UPLOAD_DIRECTORY, (new Random()).hashCode() + "_" + trimPath(fi.getName()));
					fi.write(vledataFile);
				} else if (fi.getFieldName().equals(NEW_DEPLOY_FRAME_ORIGIN)) {
					ldshakeFrameOrigin = fi.getString("UTF-8");
				}	
								
			}
		} catch (FileUploadException e1) {
	
			e1.printStackTrace();
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Request could not be parsed", e1);  
	
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INSUFFICIENT_STORAGE, "Could not write the uploaded files", e);  
	
		}
		if (deployTitle==null || deployTitle.equals("")){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, NEW_DEPLOY_TITLE_FIELD + " parameter with the deploy name is missing");
		}
		if (deployType==null || deployType.equals("")){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, NEW_DEPLOY_TYPE_FIELD + " parameter with the deploy type is missing");
		}
		if (foundVleDataFile && sectoken==null){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"sectoken parameter with the Sectoken is missing");
		}
		if (foundVleDataFile){
			//If the request comes from LdShake we always try to import the tool configuration
			importToolConf=true;
		}
	
	    // Once handled, the content of the uploaded file is sent
	    // back to the client.
		if (found) {

			Deploy deploy = null;
			try {
				String xmlContent = FileUtils.readFileToString(file, "UTF-8");
		        JAXBContext jc = JAXBContext.newInstance(glueps.core.model.Deploy.class);
		        Unmarshaller u = jc.createUnmarshaller();
		        deploy = (Deploy)u.unmarshal(new StringReader(xmlContent));
		        
		        LearningEnvironment leObj = null;
	    		String version = null, wstoken = null;
		        //If a vleData is provided, we get the learning environment and course identifiers from the vleData        
		        if (vleId == null && foundVleDataFile){
		    		String jsonString = getJsonVledata(vledataFile);
		    		JsonElement rootElement = new JsonParser().parse(jsonString);
		    		JsonElement leElement = rootElement.getAsJsonObject().get("learningEnvironment");
		    		Gson gson = new Gson();
		    		
		    		leObj = gson.fromJson(leElement,LearningEnvironment.class);
		    		
		    		if (leElement.getAsJsonObject().get("version")!=null){
		    			version = leElement.getAsJsonObject().get("version").getAsString();
		    		}
		    		if (leElement.getAsJsonObject().get("wstoken")!=null){
		    			wstoken = leElement.getAsJsonObject().get("wstoken").getAsString();
		    		}
		    		
		    		//Get the identifier of this learning environment in Glueps
		    		vleId = getGluepsLeId(leObj, login);

		    		JsonElement courseElement = rootElement.getAsJsonObject().get("course");
		    		Course courseObj = gson.fromJson(courseElement, Course.class);
		    		courseId = courseObj.getId();        	
		        }
		        
		        LearningEnvironment leDeploy = deploy.getLearningEnvironment();
				String gluepsLeId = getGluepsLeId(leDeploy, login);
				if (gluepsLeId != null && (leDeploy.getId()==null || !leDeploy.getId().equals(gluepsLeId))){
					//Update the LE id from the deploy according to the id used by Glue!PS for that LE
					leDeploy.setId(gluepsLeId);
				}
				
				String leDeployId = trimId(leDeploy.getId()); //The VLE id in the XML containing the deploy info
				String leSelectedId = trimId(vleId); //The VLE id in the LE selected/provided
				String leCourseId = null;
				if (deploy.getCourse()!=null){
					leCourseId = trimId(deploy.getCourse().getId()); //The Course id in the XML containing the deploy info
				}
				String leSelectedCourseId = trimId(courseId); //The Course id in the LE selected/provided
				
				if(vleId!=null){							
					LearningEnvironmentResource ler = new LearningEnvironmentResource();
					ler.setLearningEnvironment(vleId, vleId);
					//If some parameters have been provided for the LearningEnvironment, we replace the existing ones with these ones
			    	if (version!=null && !version.isEmpty() && wstoken!=null && !wstoken.isEmpty()){
			    		ler.getLEObject().setParameters("version=" + Reference.encode(version) + "&wstoken=" + Reference.encode(wstoken));
			    	}
					leObj = ler.getCompleteLEObject();
				}
				else{
					//The le doesn't exist in Glueps
					if (foundVleDataFile){
						LearningEnvironmentResource ler = new LearningEnvironmentResource();
						leObj.setId(null);
			    		if (version!=null && !version.isEmpty() && wstoken!=null && !wstoken.isEmpty()){
			    			leObj.setParameters("version=" + Reference.encode(version) + "&wstoken=" + Reference.encode(wstoken));
			    		}
						ler.setLearningEnvironment(leObj);									
						leObj = ler.getCompleteLEObject();
					}
					//neither vleSelect nor vledataFile has been provided
					else{
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect request. No VLE specified");  
					}
				}
				
			    deploy.setLearningEnvironment(leObj);

				VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
				IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(leObj);
				if (app.getOnlyUserCourses()){
					courses = adaptor.getCourses(leObj.getAccessLocation().toString(), leObj.getCreduser());
				}
				else{
					courses = adaptor.getCourses(leObj.getAccessLocation().toString());
				}
			    deploy.setCourse(new Course(courseId, courses.get(trimId(courseId)), null, null, null));	
			    
				//If the original LE does not exist in glue!ps or it is selected a different LE or course, the original participants have to be deleted
				//if (gluepsLeId==null || !leDeployId.equals(leSelectedId) || leCourseId == null || !leCourseId.equals(leSelectedCourseId))
			    if (!leObj.getType().equals("Blogger") && (leObj.getAccessLocation().equals(leDeploy.getAccessLocation())==false || (leCourseId==null && leSelectedCourseId!=null) || (leCourseId!=null && leCourseId.equals(leSelectedCourseId)==false) 
			    		|| leObj.getCreduser().equals(leDeploy.getCreduser())==false))
				{	
											
					System.out.println("Trying to contact VLE: "+leObj.getAccessLocation().toString());
						
					//If we got a course ID, we get the course users, and if not, all the VLE users
					if(courseId==null) vleUsers = adaptor.getUsers(leObj.getAccessLocation().toString());
					else{
						vleUsers = adaptor.getCourseUsers(leObj.getAccessLocation().toString(), trimId(courseId));
						vleGroups = adaptor.getCourseGroups(leObj.getAccessLocation().toString(), trimId(courseId));
					}
						
					if (vleUsers!=null) {
				       	//We set the deployId for all participants (the Moodle adaptor does not know it!)
				       	for(Iterator<String> it = vleUsers.keySet().iterator(); it.hasNext();){
				       		String key = (String) it.next();
				       		Participant p = vleUsers.get(key);
				       		p.setDeployId(deployId);
				       		vleUsers.put(key, p);
				       	}
					}
						
				    if(vleGroups!=null){
					    //We set the deployId for all groups (the Moodle adaptor does not know it!)
					    for(Iterator<String> it = vleGroups.keySet().iterator(); it.hasNext();){
					       	String key = (String) it.next();
					       	Group g = vleGroups.get(key);
					       	g.setDeployId(deployId);
					       	vleGroups.put(key, g);
					    }
				    }
						
				    if(vleUsers==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Error while processing instantiation file. No participants provided, and no participants are enrolled in the course!");
				    else{
				    	ArrayList<Participant> parts = new ArrayList<Participant>(vleUsers.values());
							
				    	//We delete the previous participants and include the new ones from the vle
				    	deploy.setParticipants(parts);
				    }
					//We empty the groups of the deploy
				    for (Group g: deploy.getGroups())
				    {
				        g.setParticipantIds(new ArrayList<String>());
				    }
				    //deploy.setLearningEnvironment(leObj);
				        
					ArrayList<Group> groups = null;
					if(vleGroups!=null){
						groups = new ArrayList<Group>(vleGroups.values());
						deploy.getGroups().addAll(groups);
					}
				}	
			    //Check if we need to delete the tool instances information
			    if (!login.equals(deploy.getAuthor()) || importToolConf==false || 
			    		leObj.getAccessLocation().equals(leDeploy.getAccessLocation())==false || (leCourseId==null && leSelectedCourseId!=null) || (leCourseId!=null && leCourseId.equals(leSelectedCourseId)==false) 
			    		|| leObj.getCreduser().equals(leDeploy.getCreduser())==false)
			    {			      
			    	if (deploy.getToolInstances()!=null){
			        	Design d = deploy.getDesign();
			        	LearningEnvironment selectedLe = deploy.getLearningEnvironment();
				        for (ToolInstance ti : deploy.getToolInstances())
				        {
				        	String resourceId = ti.getResourceId();
				        	String location;
				        	if (ti.getLocation()!=null){
				        		location = ti.getLocation().toString();
				        		String urlGlueOriginal = "";
				        		//The url of the instances in glueps contain the substring /instance
				        		if (location.indexOf("instance") != -1){
				        			//Get the GLUE url
				        			urlGlueOriginal = location.substring(0, location.indexOf("instance"));
				        		}
					       		Resource r = d.getResourceById(resourceId);
						       	String valueOriginal = findToolIdInExternalTools(leDeploy,trimId(r.getToolType()));
						       	String valueCurrent = findToolIdInExternalTools(selectedLe,trimId(r.getToolType()));
					       		if (!valueOriginal.equals(valueCurrent) || importToolConf == false || (!urlGlueOriginal.equals("") && (!urlGlueOriginal.equals(GM_URL)))){
					       			ti.setLocation(null);
					       		}
				        	}
				        }
			        }
			        deploy.setStaticDeployURL(null);
			        deploy.setLiveDeployURL(null);
			    }
			    //reset the inprocess value because it is a different deploy
		        deploy.setInProcess(false);
		        //Set the current timestamp
		        deploy.setTimestamp(new Date());
			} catch (IOException e) {
				throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Failed to parse the provided deploy"); 
			} catch (JAXBException e) {
				throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Failed to parse the provided deploy"); 
			}
			
			Design design = deploy.getDesign();	
			// set the title of the design with the name received
			// from the request, not the one in IMSLD, and also set the id
			design.setName(deployTitle);
			design.setAuthor(login);
			design.setId(designUrl);
			design.setTimestamp(new Date());
				
			Design deurlifiedDesign = deURLifyDesign(design);
				
			//We store the deurlified design in DB
			JpaManager dbmanager = JpaManager.getInstance();
			try {
				dbmanager.insertDesign(deurlifiedDesign);
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the design in DB", e);
			}
				
								
			// set the title of the deploy with the name received
			// from the request
			deploy.setName(deployTitle);
			deploy.setId(deployId);

			//We now ignore the author from the form, and use the login to GLUEPS
			//deploy.setAuthor(author);
			deploy.setAuthor(login);
			
			deploy.setLdshakeFrameOrigin(ldshakeFrameOrigin);				
				
			//We fix the toolInstance ids so that they are GLUEPS Urls
			//deploy = fixToolInstanceIds(deploy);
			//Supposedly, not needed... the toolinstances should not be URLs in DB

			//We fix the resource tool kinds, which were probably lost in the import process (now that we know the LE)
			//deploy = fixToolKinds(deploy);

			// Guardo Xml en base de datos 
			Deploy deurlifiedDeploy;
			try {
				deurlifiedDeploy = deURLifyDeploy(deploy);
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while processing the generated deploy", e);
			}
			
			deurlifiedDeploy = fixToolKinds(deurlifiedDeploy);
			
			try {
				dbmanager.insertDeploy(deurlifiedDeploy);
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the generated deploy into the DB", e);
			}
			
			//Save the sectoken in the database
			if (sectoken!=null){
				SectokenEntity sectEnt = new SectokenEntity(deployId, sectoken);
				dbmanager.insertSectoken(sectEnt);
			}
				
			//We urlify the deploy to be returned
			Deploy urlifiedDeploy;
			try {
				urlifiedDeploy = URLifyDeploy(deurlifiedDeploy, getReference().getParentRef().getIdentifier());
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while processing the generated deploy to return", e);
			}
			deployData = generateXML(urlifiedDeploy, glueps.core.model.Deploy.class);
				
			Representation answer = new StringRepresentation((CharSequence) deployData, MediaType.TEXT_XML);
			answer.setCharacterSet(CharacterSet.UTF_8);
			return answer;		
	
		} else {
	        // Some problem occurred, the request probably was not valid.
	        //rep = new StringRepresentation("no file uploaded",
	        //        MediaType.TEXT_PLAIN);
	        //setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "No file was uploaded");  	
	    }
	}
	
	   /**
	 * 
	 * Creation of a deploy in Glueps by importing one. Variant that receives a multipart form containing a deployment file
	 * @return	URI to new Deploy resource that is being created
	 */
	private Representation importDeployFormService(Representation entity, List<FileItem> items) {
		
		String login = this.getRequest().getChallengeResponse().getIdentifier();
		
		String designUploadDir = null;
		String designId = null;
		String designUrl = null;
		
		String deployUploadDir = null;
		String deployId = null;
		
		String deployTitle = null;
		String deployType = null;
		Boolean importToolConf = false;
		String vleId = null;
		String courseId = null;
		String sectoken = null;
		String ldshakeFrameOrigin = null;
		Representation answer = null;
		
		//Set the upload and schemas paths
		GLUEPSManagerApplication app = (GLUEPSManagerApplication)this.getApplication();
		String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
		
		//This will contain the LF deploy xml
		String deployData = null;

		boolean found = false;
		boolean foundVleDataFile = false;
		File file = null;
		File vledataFile = null;
		LearningEnvironmentResource ler;
	
		try {
			// Process only the uploaded item called "fileToUpload" and
			// save it on disk
			for (final Iterator<FileItem> it = items.iterator(); it.hasNext();) {
				FileItem fi = it.next();
				if (fi.getFieldName().equals(NEW_DEPLOY_FILE_FIELD)) {
					found = true;
					
					//We create a new designId for the design
					do {
						designId = "" + (new Random()).hashCode();						
						designUrl = getReference().getParentRef().getIdentifier()+"designs/"+designId;
						designUploadDir = UPLOAD_DIRECTORY + designId + File.separator;						
					} while ((new File(designUploadDir)).exists());
					
					// We create the newly uploaded deployment folder where
					// the deployment will be stored
					do {
						deployId = designId+Deploy.ID_SEPARATOR+(new Random()).hashCode();					
						deployUploadDir = UPLOAD_DIRECTORY + deployId + File.separator;						
					} while ((new File(deployUploadDir)).exists());
					
					//We store the deployment file
					(new File(deployUploadDir)).mkdirs();					
					file = new File(deployUploadDir, deployId + "_original.xml");
					fi.write(file);

				}else if(fi.getFieldName().equals(NEW_DEPLOY_TITLE_FIELD)){
					deployTitle=fi.getString("UTF-8");
				}else if(fi.getFieldName().equals(NEW_DEPLOY_TYPE_FIELD)){
					deployType=fi.getString("UTF-8");
				}else if(fi.getFieldName().equals(IMPORT_DEPLOY_INSTANCES_FIELD)){
					importToolConf=true;
				}else if(fi.getFieldName().equals(NEW_DEPLOY_VLE_FIELD)){
					vleId=fi.getString("UTF-8");
				}else if(fi.getFieldName().equals(NEW_DEPLOY_COURSE_FIELD)){
					courseId=fi.getString("UTF-8");
				} 
				
				//Parameters from an LdShake POST request
				else if (fi.getFieldName().equals(NEW_DEPLOY_SEC_TOKEN)) {
					sectoken = fi.getString("UTF-8");
				} else if (fi.getFieldName().equals(NEW_DEPLOY_VLEDATA)) {
					foundVleDataFile = true;
					vledataFile = new File(UPLOAD_DIRECTORY, (new Random()).hashCode() + "_" + trimPath(fi.getName()));
					fi.write(vledataFile);
				} else if (fi.getFieldName().equals(NEW_DEPLOY_FRAME_ORIGIN)) {
					ldshakeFrameOrigin = fi.getString("UTF-8");
				}	
								
			}
		} catch (FileUploadException e1) {
	
			e1.printStackTrace();
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Request could not be parsed", e1);  
	
		} catch (Exception e) {			
			e.printStackTrace();
			throw new ResourceException(Status.SERVER_ERROR_INSUFFICIENT_STORAGE, "Could not write the uploaded files", e);  
	
		}
		if (deployTitle==null || deployTitle.equals("")){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, NEW_DEPLOY_TITLE_FIELD + " parameter with the deploy name is missing");
		}
		if (deployType==null || deployType.equals("")){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, NEW_DEPLOY_TYPE_FIELD + " parameter with the deploy type is missing");
		}
		if (foundVleDataFile && sectoken==null){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,"sectoken parameter with the Sectoken is missing");
		}
		if (foundVleDataFile){
			//If the request comes from LdShake we always try to import the tool configuration
			importToolConf=true;
		}
	
	    // Once handled, the content of the uploaded file is sent
	    // back to the client.
		if (found) {

			Deploy deploy = null;
			try {
				String xmlContent = FileUtils.readFileToString(file, "UTF-8");
		        JAXBContext jc = JAXBContext.newInstance(glueps.core.model.Deploy.class);
		        Unmarshaller u = jc.createUnmarshaller();
		        deploy = (Deploy)u.unmarshal(new StringReader(xmlContent));
		        
		        LearningEnvironment leObj = null;
	    		String version = null, wstoken = null;
		        //If a vleData is provided, we get the learning environment and course identifiers from the vleData        
		        if (vleId == null && foundVleDataFile){
		    		String jsonString = getJsonVledata(vledataFile);
		    		JsonElement rootElement = new JsonParser().parse(jsonString);
		    		JsonElement leElement = rootElement.getAsJsonObject().get("learningEnvironment");
		    		Gson gson = new Gson();
		    		
		    		leObj = gson.fromJson(leElement,LearningEnvironment.class);
		    		
		    		if (leElement.getAsJsonObject().get("version")!=null){
		    			version = leElement.getAsJsonObject().get("version").getAsString();
		    		}
		    		if (leElement.getAsJsonObject().get("wstoken")!=null){
		    			wstoken = leElement.getAsJsonObject().get("wstoken").getAsString();
		    		}
		    		
		    		//Get the identifier of this learning environment in Glueps
		    		vleId = getGluepsLeId(leObj, login);

		    		JsonElement courseElement = rootElement.getAsJsonObject().get("course");
		    		Course courseObj = gson.fromJson(courseElement, Course.class);
		    		courseId = courseObj.getId();        	
		        }
		        
		        LearningEnvironment leDeploy = deploy.getLearningEnvironment();
				String gluepsLeId = getGluepsLeId(leDeploy, login);
				if (gluepsLeId != null && (leDeploy.getId()==null || !leDeploy.getId().equals(gluepsLeId))){
					//Update the LE id from the deploy according to the id used by Glue!PS for that LE
					leDeploy.setId(gluepsLeId);
				}
				
				if(vleId!=null){							
					ler = new LearningEnvironmentResource();
					ler.setLearningEnvironment(vleId, vleId);
					//If some parameters have been provided for the LearningEnvironment, we replace the existing ones with these ones
			    	if (version!=null && !version.isEmpty() && wstoken!=null && !wstoken.isEmpty()){
			    		ler.getLEObject().setParameters("version=" + Reference.encode(version) + "&wstoken=" + Reference.encode(wstoken));
			    	}
				}
				else{
					//The le doesn't exist in Glueps
					if (foundVleDataFile){
						ler = new LearningEnvironmentResource();
						leObj.setId(null);
			    		if (version!=null && !version.isEmpty() && wstoken!=null && !wstoken.isEmpty()){
			    			leObj.setParameters("version=" + Reference.encode(version) + "&wstoken=" + Reference.encode(wstoken));
			    		}
						ler.setLearningEnvironment(leObj);									
					}
					//neither vleSelect nor vledataFile has been provided
					else{
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect request. No VLE specified");  
					}
				}

			    //set the inprocess value to indicate that the import deploy process has started (but it has not finished yet)
		        deploy.setInProcess(true);
		        //Set the current timestamp
		        deploy.setTimestamp(new Date());
			} catch (IOException e) {
				throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Failed to parse the provided deploy"); 
			} catch (JAXBException e) {
				throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Failed to parse the provided deploy"); 
			}
			
			Design design = deploy.getDesign();	
			// set the title of the design with the name received
			// from the request, not the one in IMSLD, and also set the id
			design.setName(deployTitle);
			design.setAuthor(login);
			design.setId(designUrl);
			design.setTimestamp(new Date());
								
			// set the title of the deploy with the name received
			// from the request
			deploy.setName(deployTitle);
			deploy.setId(deployId);

			//We now ignore the author from the form, and use the login to GLUEPS
			String originalAuthor = deploy.getAuthor();
			deploy.setAuthor(login);			
			deploy.setLdshakeFrameOrigin(ldshakeFrameOrigin);				
			
			JpaManager dbmanager = JpaManager.getInstance();		
			AsynchronousOperation asynOper = new AsynchronousOperation(null, AsynchronousOperation.generateOperationId(), AsynchronousOperation.STATUS_INPROGRESS, "The deployment is being created", null, new Date(), null);
			long asynOperId;
			try{
				asynOperId = dbmanager.insertAsynchronousOperation(asynOper);
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the asynchronous operation into the DB", e);
			}
			//complete the process on the background by getting the information from the vle
			Future<Deploy> process = GLUEPSManagerServerMain.pool.submit(new BackgroundLiveImportDeploy(deploy, ler, courseId, originalAuthor, importToolConf, app, asynOperId));
			
			//Save the sectoken in the database
			if (sectoken!=null){
				SectokenEntity sectEnt = new SectokenEntity(deployId, sectoken);
				dbmanager.insertSectoken(sectEnt);
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
	
		} else {
	        // Some problem occurred, the request probably was not valid.
	        //rep = new StringRepresentation("no file uploaded",MediaType.TEXT_PLAIN);
	        //setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "No file was uploaded");  	
	    }
	}
	
	
    /**
	 * POST DeployListResource
	 * 
	 * Creation Deploy in Glueps. Variant that receives a multipart form
	 * 
	 * Dispatch the configuration data to the proper LD adaptor in order to trigger the actual creation.
	 *
	 * 
	 * @return	URI to new Deploy resource, or null.
	 */
	private Representation createDeployForm(Representation entity, List<FileItem> items) {
		
		String login = this.getRequest().getChallengeResponse().getIdentifier();
		 
		String uploadDir = null;
		String deployId = null;
		String deployUrl = null;

		//Form fields to be received
		String designId = null;
		String deployTitle = null;
		String deployType = null;
		String vleId = null;
		String author = null;
		String startLesson = null;
		boolean incremental = false;
		
		String courseId = null;
		
		Design design = null;
		Deploy deploy = null;
		LearningEnvironment le = null;
		
		HashMap<String,String> courses = null;
		
		String deployData = null;
		
		HashMap<String, Participant> vleUsers = null;
		
		HashMap<String, Group> vleGroups = null;
		
		if (entity != null) {

			try {
				logger.info("** POST DEPLOY received: \n" + entity.toString());

				//Set the upload and schemas paths
				GLUEPSManagerApplication app = (GLUEPSManagerApplication)this.getApplication();
				String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
				String SCHEMA_LOCATION = app.getAppPath()+"/schemas/";

				//extract the design id from the request, coming in the url: /designs/{designId}/deploys
				designId = trimId((String) this.getRequest().getAttributes().get("designId"));
				//extract the design object once we know the design id
				// TODO: test this!
				DesignResource dres = new DesignResource();
				//To the resource we should pass the name of the xml file, not just the number
				dres.setDesign(designId, getReference().getParentRef().getParentRef().getIdentifier());
				design = dres.getDesignObject();
				
				//We create the newly uploaded deploy folder where the xml will be stored
				do{
					deployId = designId+Deploy.ID_SEPARATOR+(new Random()).hashCode();
					deployUrl = getReference().getParentRef().getParentRef().getParentRef().getIdentifier()+"deploys/"+deployId;
					uploadDir = UPLOAD_DIRECTORY + deployId + File.separator;
				}while((new File(uploadDir)).exists());
				(new File(uploadDir)).mkdirs();

				if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(),true)) {

					boolean found = false;

					try {
						// Process only the uploaded item called "fileToUpload" and
						// save it on disk
						for (final Iterator<FileItem> it = items.iterator(); it
								.hasNext();) {
							FileItem fi = it.next();
							if (fi.getFieldName().equals(NEW_DEPLOY_FILE_FIELD)) {
								
								if(fi.getName()!=null && fi.getName().length()>0){
									found = true;

									File file = new File(uploadDir, deployId + "_original.xml");
									fi.write(file);
								}
							}else if(fi.getFieldName().equals(NEW_DEPLOY_TITLE_FIELD)){
								deployTitle=fi.getString("UTF-8");
							}else if(fi.getFieldName().equals(NEW_DEPLOY_TYPE_FIELD)){
								deployType=fi.getString("UTF-8");
							}else if(fi.getFieldName().equals(NEW_DEPLOY_AUTHOR_FIELD)){
								author=fi.getString("UTF-8");
							}else if(fi.getFieldName().equals(NEW_DEPLOY_VLE_FIELD)){
								vleId=fi.getString("UTF-8");
							}else if(fi.getFieldName().equals(NEW_DEPLOY_INCR_CHECK)){
								if(fi.getString("UTF-8").equals(NEW_DEPLOY_INCR_CHECK_VALUE)) incremental=true;
							}else if(fi.getFieldName().equals(NEW_DEPLOY_INCR_LESSON)){
								startLesson=fi.getString("UTF-8");
							}else if(fi.getFieldName().equals(NEW_DEPLOY_COURSE_FIELD)){
								courseId = fi.getString("UTF-8");
							}
							
						}
					} catch (FileUploadException e1) {

						e1.printStackTrace();
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Request could not be parsed", e1);  

					} catch (Exception e) {
						
						e.printStackTrace();
						throw new ResourceException(Status.SERVER_ERROR_INSUFFICIENT_STORAGE, "Could not write the uploaded files", e);  

					}
					
					if (deployTitle==null || deployTitle.equals("")){
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, NEW_DEPLOY_TITLE_FIELD + " parameter with the deploy name is missing");
					}
					if (deployType==null || deployType.equals("")){
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, NEW_DEPLOY_TYPE_FIELD + " parameter with the deploy type is missing");
					}

					// We get the users data from the vle
					// we check the VLE type and load the adaptor dynamically
					
					if(vleId!=null){
						

						LearningEnvironmentResource ler = new LearningEnvironmentResource();
						ler.setLearningEnvironment(vleId, vleId);
						
						le = ler.getCompleteLEObject();
						System.out.println("Trying to contact VLE: "+le.getAccessLocation().toString());

						VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
						IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(le);
						
						//If we got a course ID, we get the course users, and if not, all the VLE users
						if(courseId==null) vleUsers = adaptor.getUsers(le.getAccessLocation().toString());
						else{
							vleUsers = adaptor.getCourseUsers(le.getAccessLocation().toString(), trimId(courseId));
							vleGroups = adaptor.getCourseGroups(le.getAccessLocation().toString(), trimId(courseId));
						}
						
						
						if (app.getOnlyUserCourses()){
							courses = adaptor.getCourses(le.getAccessLocation().toString(), le.getCreduser());
						}
						else{
							courses = adaptor.getCourses(le.getAccessLocation().toString());
						}
						
						if (vleUsers!=null) {

				       		//We set the deployId for all participants (the Moodle adaptor does not know it!)
				       		for(Iterator<String> it = vleUsers.keySet().iterator(); it.hasNext();){
				       			String key = (String) it.next();
				       			Participant p = vleUsers.get(key);
				       			p.setDeployId(deployId);
				       			vleUsers.put(key, p);
				       		}
						}
						
				       	if(vleGroups!=null){
					    	//We set the deployId for all groups (the Moodle adaptor does not know it!)
					       	for(Iterator<String> it = vleGroups.keySet().iterator(); it.hasNext();){
					       		String key = (String) it.next();
					       		Group g = vleGroups.get(key);
					       		g.setDeployId(deployId);
					       		vleGroups.put(key, g);
					       	}

				       	}
				        
					}else{
						// Some problem occurred, we could not get the list of users
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect request. No VLE specified");  
					}
					
					//We fix the tool types that may have come from the design tool (WIC2)
					//design = fixDesignToolTypes(design, le);
					//The design has come from the DB, it should already be de-urlified
					
					//Note: vleUsers and vleGroups can be null!
					
					if (found) {

						// Received xml file is now on disk. We use the LD
						// adaptor to get the Deploy
						LDAdaptorFactory factory = new LDAdaptorFactory(app);
						ILDAdaptor adaptor = factory.getLDAdaptor(deployType, designId);

						//TODO: What happens if vleUsers==null??
						deploy = adaptor.processInstantiation(uploadDir + deployId + "_original.xml", design, vleGroups, vleUsers);
						if(deploy==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Error while processing instantiation file. Probably bad format");

					} else {//We found no xml instantiation file, we have to create an empty deploy: no groups unless present in VLE, no instancedactivities, no toolinstances, list of participants from the VLE
				        
						//Since right now we cannot create users from scratch (they have to come from the LD tool or from the VLE), if the vleUsers == null, we through an error 
						if(vleUsers==null) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Error while processing instantiation file. No participants provided, and no participants are enrolled in the course!");
						else{
							
							LDAdaptorFactory factory = new LDAdaptorFactory(app);
							ILDAdaptor ldAdaptor = factory.getLDAdaptor(deployType, designId);
							deploy = ldAdaptor.processInstantiation(null, design, vleGroups, vleUsers);
							if (deploy == null){			   
								ArrayList<Participant> parts = new ArrayList<Participant>(vleUsers.values());
								ArrayList<Group> groups = null;
								if(vleGroups!=null){
									groups = new ArrayList<Group>(vleGroups.values());
								}
								deploy = new Deploy(null, design, null, null, null, new Date(), null, null, null, parts, groups);
							}
						}
					}
						
						
					// set the title of the deploy with the name received
					// from the request
					deploy.setName(deployTitle);
					deploy.setId(deployId);

					//We now ignore the author from the form, and use the login to GLUEPS
					//deploy.setAuthor(author);
					deploy.setAuthor(login);
					
					deploy.setLearningEnvironment(le);
					
					deploy.setCourse(new Course(courseId, courses.get(trimId(courseId)), null, null, null));
					
					// set the incremental deploy parameters
					if(incremental && startLesson!=null){
						String incrementalParameter = GLUEPSManagerApplication.STARTING_SECTION_FIELD+Deploy.DEPLOY_DATA_VALUE_SEPARATOR+startLesson+Deploy.DEPLOY_DATA_FIELD_SEPARATOR;
						if(deploy.getDeployData()!=null) deploy.setDeployData(deploy.getDeployData()+incrementalParameter);
						else deploy.setDeployData(incrementalParameter);
					}
					
					//We fix the toolInstance ids so that they are GLUEPS Urls
					//deploy = fixToolInstanceIds(deploy);
					//Supposedly, not needed... the toolinstances should not be URLs in DB

					//We fix the resource tool kinds, which were probably lost in the import process (now that we know the LE)
					//deploy = fixToolKinds(deploy);

					// Guardo Xml en base de datos 
					Deploy deurlifiedDeploy;
					try {
						deurlifiedDeploy = deURLifyDeploy(deploy);
					} catch (Exception e) {
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while processing the generated deploy", e);
					}
					
					deurlifiedDeploy = fixToolKinds(deurlifiedDeploy);
					
					JpaManager dbmanager = JpaManager.getInstance();
					try {
						dbmanager.insertDeploy(deurlifiedDeploy);
					} catch (Exception e) {
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the generated deploy into the DB", e);
					}
					
					//We urlify the deploy to be returned
					Deploy urlifiedDeploy;
					try {
						urlifiedDeploy = URLifyDeploy(deurlifiedDeploy, getReference().getParentRef().getParentRef().getParentRef().getIdentifier());
					} catch (Exception e) {
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while processing the generated deploy to return", e);
					}
					deployData = generateXML(urlifiedDeploy, glueps.core.model.Deploy.class);
					
					Representation result = null;
					Representation answer = new StringRepresentation((CharSequence) deployData, MediaType.TEXT_XML);
					answer.setCharacterSet(CharacterSet.UTF_8);
					return answer;		


				}else{
				    // POST request with a different content type (this should not happen).
				    //setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect content type. Should be multipart/form-data");  

				}
			} catch (ResourceException e) {
				throw e;
			} finally {
	            discardRepresentation(entity);        // fix to mimick GLUElet Manager corrections for connection stability
			}
			
			
			
        } else {
            // POST request with no entity.
            //setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect request");  

        }

		
		
	}//end of CreateDeployForm

    /**
	 * POST DeployListResource
	 * 
	 * Creation Deploy in Glueps. Variant that receives a multipart form
	 * 
	 * Dispatch the configuration data to the proper LD adaptor in order to trigger the actual creation.
	 *
	 * 
	 * @return	URI to new Deploy resource, or null.
	 */
	private Representation createDeployFormService(Representation entity, List<FileItem> items) {
		
		String login = this.getRequest().getChallengeResponse().getIdentifier();
		 
		String uploadDir = null;
		String deployId = null;
		String deployUrl = null;
		Representation answer = null;

		//Form fields to be received
		String designId = null;
		String deployTitle = null;
		String deployType = null;
		String vleId = null;
		String author = null;
		String startLesson = null;
		boolean incremental = false;
		
		String courseId = null;
		
		Design design = null;
		Deploy deploy = null;
		
		String deployData = null;
		
		if (entity != null) {

			try {
				logger.info("** POST DEPLOY received: \n" + entity.toString());

				//Set the upload and schemas paths
				GLUEPSManagerApplication app = (GLUEPSManagerApplication)this.getApplication();
				String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";

				//extract the design id from the request, coming in the url: /designs/{designId}/deploys
				designId = trimId((String) getRequest().getAttributes().get("designId"));
				//extract the design object once we know the design id
				// TODO: test this!
				DesignResource dres = new DesignResource();
				//To the resource we should pass the name of the xml file, not just the number
				dres.setDesign(designId, getReference().getParentRef().getParentRef().getIdentifier());
				design = dres.getDesignObject();
				
				//We create the newly uploaded deploy folder where the xml will be stored
				do{
					deployId = designId+Deploy.ID_SEPARATOR+(new Random()).hashCode();
					deployUrl = getReference().getParentRef().getParentRef().getParentRef().getIdentifier()+"deploys/"+deployId;
					uploadDir = UPLOAD_DIRECTORY + deployId + File.separator;
				}while((new File(uploadDir)).exists());
				(new File(uploadDir)).mkdirs();

				if (MediaType.MULTIPART_FORM_DATA.equals(entity.getMediaType(),true)) {

					boolean found = false;

					try {
						// Process only the uploaded item called "fileToUpload" and
						// save it on disk
						for (final Iterator<FileItem> it = items.iterator(); it
								.hasNext();) {
							FileItem fi = it.next();
							if (fi.getFieldName().equals(NEW_DEPLOY_FILE_FIELD)) {
								
								if(fi.getName()!=null && fi.getName().length()>0){
									found = true;

									File file = new File(uploadDir, deployId + "_original.xml");
									fi.write(file);
								}
							}else if(fi.getFieldName().equals(NEW_DEPLOY_TITLE_FIELD)){
								deployTitle=fi.getString("UTF-8");
							}else if(fi.getFieldName().equals(NEW_DEPLOY_TYPE_FIELD)){
								deployType=fi.getString("UTF-8");
							}else if(fi.getFieldName().equals(NEW_DEPLOY_AUTHOR_FIELD)){
								author=fi.getString("UTF-8");
							}else if(fi.getFieldName().equals(NEW_DEPLOY_VLE_FIELD)){
								vleId=fi.getString("UTF-8");
							}else if(fi.getFieldName().equals(NEW_DEPLOY_INCR_CHECK)){
								if(fi.getString("UTF-8").equals(NEW_DEPLOY_INCR_CHECK_VALUE)) incremental=true;
							}else if(fi.getFieldName().equals(NEW_DEPLOY_INCR_LESSON)){
								startLesson=fi.getString("UTF-8");
							}else if(fi.getFieldName().equals(NEW_DEPLOY_COURSE_FIELD)){
								courseId = fi.getString("UTF-8");
							}
							
						}
					} catch (FileUploadException e1) {

						e1.printStackTrace();
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Request could not be parsed", e1);  

					} catch (Exception e) {
						
						e.printStackTrace();
						throw new ResourceException(Status.SERVER_ERROR_INSUFFICIENT_STORAGE, "Could not write the uploaded files", e);  

					}
					
					if (deployTitle==null || deployTitle.equals("")){
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, NEW_DEPLOY_TITLE_FIELD + " parameter with the deploy name is missing");
					}
					if (deployType==null || deployType.equals("")){
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, NEW_DEPLOY_TYPE_FIELD + " parameter with the deploy type is missing");
					}

					// We get the users data from the vle
					// we check the VLE type and load the adaptor dynamically
					
					if(vleId==null){
						// Some problem occurred, we could not get the list of users
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect request. No VLE specified");  
					}

					JpaManager dbmanager = JpaManager.getInstance();		
					AsynchronousOperation asynOper = new AsynchronousOperation(null, AsynchronousOperation.generateOperationId(), AsynchronousOperation.STATUS_INPROGRESS, "The deployment is being created", null, new Date(), null);
					long asynOperId;
					try{
						asynOperId = dbmanager.insertAsynchronousOperation(asynOper);
					} catch (Exception e) {
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the asynchronous operation into the DB", e);
					}
					Future<Deploy> process = GLUEPSManagerServerMain.pool.submit(new BackgroundLiveCreateDeploy(vleId, courseId, deployId, found, deployType, design, uploadDir, deployTitle, login, incremental, startLesson, app, asynOperId));
										
					URLifyAsynchronousOperation(asynOper, doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getParentRef().getIdentifier()));
			   		String xmlfile = generateXML(asynOper, glueps.core.model.AsynchronousOperation.class);	
			   		if (xmlfile != null){
			   			answer = new StringRepresentation((CharSequence)xmlfile, MediaType.TEXT_XML);
			   			answer.setCharacterSet(CharacterSet.UTF_8);
			   		}				
					getResponse().setLocationRef(asynOper.getOperation());
					answer.setCharacterSet(CharacterSet.UTF_8);
					setStatus(Status.SUCCESS_ACCEPTED);
					return answer;		
				}else{
				    // POST request with a different content type (this should not happen).
				    //setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect content type. Should be multipart/form-data");  

				}
			} catch (ResourceException e) {
				throw e;
			} finally {
	            discardRepresentation(entity);        // fix to mimick GLUElet Manager corrections for connection stability
			}		
        } else {
            // POST request with no entity.
            //setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Incorrect request");  

        }
	}//end of CreateDeployForm

	private Deploy fixToolKinds(Deploy deploy) {
		if(deploy!=null && deploy.getDesign()!=null && deploy.getDesign().getResources()!=null && deploy.getLearningEnvironment()!=null){
			HashMap<String,String> internal = deploy.getLearningEnvironment().getInternalTools();
			HashMap<String,String> external = deploy.getLearningEnvironment().getExternalTools();
			
			for(Resource res : deploy.getDesign().getResources()){
				
				if(internal!=null && res.getToolType()!=null && internal.get(trimId(res.getToolType()))!=null) 
					res.setToolKind(Resource.TOOL_KIND_INTERNAL);
				else if(external!=null && res.getToolType()!=null && external.get(trimId(res.getToolType()))!=null) 
					res.setToolKind(Resource.TOOL_KIND_EXTERNAL);
				
			}
			return deploy;
		}else return deploy;
	}
	
	private String findToolIdInExternalTools(LearningEnvironment le, String toolId) {
		HashMap<String, String> externalTools = le.getExternalTools();
		if(toolId == null || externalTools == null || externalTools.size()==0) return "";
		
		for(Iterator<String> it = externalTools.keySet().iterator(); it.hasNext();){
			String gmKey = it.next();
			if (gmKey.equals(toolId)) return externalTools.get(gmKey);
			else if(gmKey.substring(gmKey.lastIndexOf("/")+1).equals(toolId)) return externalTools.get(gmKey);
		}
		
		
		return "";
	}
	
	private String findToolIdInInternalTools(LearningEnvironment le, String toolId) {
		HashMap<String, String> internalTools = le.getInternalTools();
		if(toolId == null || internalTools == null || internalTools.size()==0) return "";
		
		for(Iterator<String> it = internalTools.keySet().iterator(); it.hasNext();){
			String gmKey = it.next();
			//if(gmKey.lastIndexOf("/")==-1) continue;
			if(gmKey.equals(toolId)) return internalTools.get(gmKey);
			else if(gmKey.substring(gmKey.lastIndexOf("/")+1).equals(toolId)) return internalTools.get(gmKey);
		}
		return "";
	}

//These should not be used anymore, we should manage them with URLify-deURLify	
//	private Design fixDesignToolTypes(Design design, LearningEnvironment le) {
//	
//		//If there are no resources, we do nothing
//		if(design.getResources()==null) return design;
//		
//
//		//We go through the resources and compare their tool types with the ones in the le
//		for(Iterator<Resource> it = design.getResources().iterator(); it.hasNext();){
//			Resource res = it.next();
//
//			if(res.getToolType()!=null){
//				for(Iterator<String> it2 = le.getInternalTools().keySet().iterator(); it2.hasNext();){
//					String intTool = it2.next();
//					if(res.getToolType().startsWith(intTool)){
//						res.setToolType(intTool);
//						res.setToolKind(Resource.TOOL_KIND_INTERNAL);
//						break;
//					}
//				}
//				
//				for(Iterator<String> it2 = le.getExternalTools().keySet().iterator(); it2.hasNext();){
//					String extTool = it2.next();
//					if(res.getToolType().startsWith(extTool)){
//						res.setToolType(extTool);
//						res.setToolKind(Resource.TOOL_KIND_EXTERNAL);
//						break;
//					}
//				}
//				
//				if(!res.getToolKind().equals(Resource.TOOL_KIND_EXTERNAL) && !res.getToolKind().equals(Resource.TOOL_KIND_INTERNAL)){//if it is not internal or external, we leave it at unknown, and delete the tooltype
//					res.setToolKind("unknown");
//				}
//			}
//		}
//		return design;
//	}
//
//	//Makes the tool instance Ids to be URLs, by constructing them from the deploy Id, which itself is a URL
//private Deploy fixToolInstanceIds(Deploy deploy) {
//	
//	Deploy newDeploy = deploy;
//	
//	String baseUrl = deploy.getId()+"/toolInstances/";
//	
//	if(deploy.getToolInstances()!=null){
//		ArrayList<ToolInstance> newInstances = new ArrayList<ToolInstance>();
//		
//		//We update the IDs in the toolInstance array
//		for(Iterator<ToolInstance> it = deploy.getToolInstances().iterator();it.hasNext();){
//			ToolInstance instance = it.next();
//			if(!instance.getId().startsWith(baseUrl)){
//				instance.setId(baseUrl+instance.getId());
//			}
//			//We also fix the instance's deployId, just in case
//			if(instance.getDeployId()==null || !instance.getDeployId().startsWith(deploy.getId())){
//				instance.setDeployId(deploy.getId());
//			}
//			
//			newInstances.add(instance);
//		}
//		
//		newDeploy.setToolInstances(newInstances);
//	}
//	
//	if(deploy.getInstancedActivities()!=null){
//		ArrayList<InstancedActivity> newInstances = new ArrayList<InstancedActivity>();
//		
//		//We update the toolInstance IDs in the references from the instancedActivities
//		for(Iterator<InstancedActivity> it = deploy.getInstancedActivities().iterator();it.hasNext();){
//			InstancedActivity instance = it.next();
//			
//			//We go through the toolInstances for this instancedActivity
//			if(instance.getInstancedToolIds()!=null){
//				ArrayList<String> newRefs = new ArrayList<String>();
//				
//				for(Iterator<String> it2 = instance.getInstancedToolIds().iterator();it2.hasNext();){
//					String toolInstanceRef = it2.next();
//					if(!toolInstanceRef.startsWith(baseUrl)){
//						toolInstanceRef = baseUrl+toolInstanceRef;
//					}
//					
//					newRefs.add(toolInstanceRef);
//				}
//				
//				instance.setInstancedToolIds(newRefs);
//			}
//			
//			//We also fix the instance's deployId, just in case
//			if(instance.getDeployId()==null || !instance.getDeployId().startsWith(deploy.getId())){
//				instance.setDeployId(deploy.getId());
//			}
//			
//			newInstances.add(instance);
//			
//		}
//		
//		newDeploy.setInstancedActivities(newInstances);
//	}
//	
//	
//	return newDeploy;
//}
	
	/**
	 * Parse the request as as list of FileItem objects
	 * @return The list of FileItem provided in the request
	 */
	private List<FileItem> parseRequestAsFileItems() {

		// The Apache FileUpload project parses HTTP requests which
		// conform to RFC 1867, "Form-based File Upload in HTML". That
		// is, if an HTTP request is submitted using the POST method,
		// and with a content type of "multipart/form-data", then
		// FileUpload can parse that request, and get all uploaded files
		// as FileItem.

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
		try {
			items = upload.parseRequest(getRequest());
		} catch (FileUploadException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
					"Request could not be parsed", e);
		}
		return items;
	}
	
	
	/**
	 * Get the identifier of this learning environment in Glueps. 
	 * If the learning environment comes from another Glueps or le management tool, the identifier could be different or even the le might not exist in this glueps
	 * @param le The learning environment from which we want to get identifier
	 * @return the identifier of this learning environment in Glueps
	 */
	private String getGluepsLeId(LearningEnvironment le, String userid){
		try {
			JpaManager dbmanager = JpaManager.getInstance();
			URL accessLocation = le.getAccessLocation();
			LearningEnvironment leGlueps = dbmanager.findLEObjectById(le.getId());
			//Check if the LE id from the deploy to import is the same as the LE id in our Glue!PS
			//The accessLocation value should be the same
			if (leGlueps!=null && leGlueps.getAccessLocation().toString().equals(accessLocation.toString()) && leGlueps.getUserid().equals(userid) && leGlueps.getCreduser().equals(le.getCreduser()) && leGlueps.getCredsecret().equals(le.getCredsecret()))
			{
				return leGlueps.getId();
			}
			else{
				//Check if the LE exists in our Glue!ps with another id
				List<LearningEnvironment> les = dbmanager.listLEObjects();
				for (LearningEnvironment leItem: les)
				{
					if (leItem.getAccessLocation().toString().equals(accessLocation.toString())&& leItem.getUserid().equals(userid) && leItem.getCreduser().equals(le.getCreduser()) && leItem.getCredsecret().equals(le.getCredsecret()))
					{
						return leItem.getId();
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Internal error while trying to retrieve Learning Environments from DB", e);
		}
		return null;
	}
	
	
	/**
	 * Get a JSON string that contains the vle, course and participants information
	 * @param vledataFile The file from which the JSON string is read
	 * @return the JSON string 
	 */
	private String getJsonVledata(File vledataFile) {
		FileReader fr = null;
		BufferedReader br = null;
		StringBuffer jsonString;
		try {
			fr = new FileReader(vledataFile);
			br = new BufferedReader(fr);
			// Lectura del fichero
			jsonString = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
					"Error while reading the vle information", e);
		} finally {
			try {
				if (fr != null) {
					fr.close();
					// We do not need the file anymore, so we delete it
					vledataFile.delete();
				}
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
						"Error while closing the file", e);
			}
		}
		return jsonString.toString();
	}
	
	/**
	 * Does a background creation of a design and a deploy from a POST request
	 * @author Javier Hoyos Torio
	 *
	 */
	private class BackgroundLivePostDesignDeploy implements Callable<Deploy>{
		
		private String vleId;
		private File vledataFile;
		private String courseId;
		private String login;
		private String designId;
		private String deployId;
		private String deployType;
		private String designUploadDir;
		private File file;
		private Design design;
		private String deployTitle;
		private String ldshakeFrameOrigin;
		private GLUEPSManagerApplication app;
		private long asynOperId;
		
		public BackgroundLivePostDesignDeploy(String vleId, File vledataFile, String courseId, String login, String designId, String deployId, String deployType, String designUploadDir, File file, Design design, String deployTitle, String ldshakeFrameOrigin, GLUEPSManagerApplication app, long asynOperId){
			this.vleId = vleId;
			this.vledataFile = vledataFile;
			this.courseId = courseId;
			this.login = login;
			this.designId = designId;
			this.deployId = deployId;
			this.deployType = deployType;
			this.designUploadDir = designUploadDir;
			this.file = file;
			this.design = design;
			this.deployTitle = deployTitle;
			this.ldshakeFrameOrigin = ldshakeFrameOrigin;
			this.app = app;
			this.asynOperId = asynOperId;
		}
		
		@Override
		public Deploy call(){
			JpaManager dbmanager = JpaManager.getInstance();
			HashMap<String, Participant> vleUsers = null;
			HashMap<String, Group> vleGroups = null;
			HashMap<String, String> courses = null;
			LearningEnvironment leObj = null;
			Deploy deploy = null;
			LDAdaptorFactory factory = new LDAdaptorFactory(app);
			ILDAdaptor ldAdaptor = factory.getLDAdaptor(deployType, designId);
	        //If a vleData is provided, we get the learning environment and course identifiers from the vleData        
	        if (vleId == null && vledataFile!=null){
	    		String jsonString = getJsonVledata(vledataFile);
	    		JsonElement rootElement = new JsonParser().parse(jsonString);
	    		JsonElement leElement = rootElement.getAsJsonObject().get("learningEnvironment");
	    		Gson gson = new Gson();
	    		
	    		leObj = gson.fromJson(leElement,LearningEnvironment.class);
	    		//Get the identifier of this learning environment in Glueps
	    		vleId = getGluepsLeId(leObj, login);

	    		JsonElement courseElement = rootElement.getAsJsonObject().get("course");
	    		Course courseObj = gson.fromJson(courseElement, Course.class);
	    		courseId = courseObj.getId();        	
	        }       
			if(vleId!=null){
							
				LearningEnvironmentResource ler = new LearningEnvironmentResource();
				ler.setLearningEnvironment(vleId, vleId);
				ler.setApplicationResource(app)	;
				leObj = ler.getCompleteLEObject();
			}
			else{
				//The le doesn't exist in Glueps
				if (vledataFile != null){
					LearningEnvironmentResource ler = new LearningEnvironmentResource();
					leObj.setId(null);
					ler.setLearningEnvironment(leObj);
					ler.setApplicationResource(app)	;	
					leObj = ler.getCompleteLEObject();
				}
				//neither vleSelect nor vledataFile has been provided
				else{
					defineErrorAsynchronousOperation(asynOperId, "Incorrect request. No VLE specified");
					return null;
				}
			}
			
			System.out.println("Trying to contact VLE: " + leObj.getAccessLocation().toString());

			VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory(app);
			IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(leObj);

			// If we got a course ID, we get the course users, and if not, all the VLE users
			if (courseId == null)
				vleUsers = adaptor.getUsers(leObj.getAccessLocation().toString());
			else {
				vleUsers = adaptor.getCourseUsers(leObj.getAccessLocation().toString(), trimId(courseId));
				vleGroups = adaptor.getCourseGroups(leObj.getAccessLocation().toString(), trimId(courseId));
			}

			if (app.getOnlyUserCourses()){
				courses = adaptor.getCourses(leObj.getAccessLocation().toString(), leObj.getCreduser());
			}
			else{
				courses = adaptor.getCourses(leObj.getAccessLocation().toString());
			}

			if (vleUsers != null) {

				// We set the deployId for all participants (the Moodle adaptor
				// does not know it!)
				for (Iterator<String> it = vleUsers.keySet().iterator(); it.hasNext();) {
					String key = (String) it.next();
					Participant p = vleUsers.get(key);
					p.setDeployId(deployId);
					vleUsers.put(key, p);
				}
			}

			if (vleGroups != null) {
				// We set the deployId for all groups (the Moodle adaptor does
				// not know it!)
				for (Iterator<String> it = vleGroups.keySet().iterator(); it.hasNext();) {
					String key = (String) it.next();
					Group g = vleGroups.get(key);
					g.setDeployId(deployId);
					vleGroups.put(key, g);
				}

			}
			
			//Set the path to the deploy file
			String filePath = "";
			if (deployType.equals(LDAdaptorFactory.IMSLD_TYPE)) {
				// path to the icmanifest, if it exists
				filePath = designUploadDir + "icmanifest.xml";
			} else if (deployType.equals(LDAdaptorFactory.PPC_TYPE)) {
				// There is not a deploy file
				filePath = "";
			} else if (deployType.equals(LDAdaptorFactory.T2_TYPE)) {
				// The design and deploy files are the same
				filePath = designUploadDir + file.getName();
			}

			if (new File(filePath).exists()) {

				// TODO: What happens if vleUsers==null??
				deploy = ldAdaptor.processInstantiation(filePath, design, vleGroups, vleUsers);
				if (deploy == null){
					defineErrorAsynchronousOperation(asynOperId, "Error while processing instantiation file. Probably bad format");
					return null;
				}					

			} else {// We found no xml instantiation file, we have to create an empty deploy: no groups unless present in VLE, no instancedactivities, no toolinstances, list of participants from the VLE

				// Since right now we cannot create users from scratch (they
				// have to come from the LD tool or from the VLE), if the
				// vleUsers == null, we through an error
				if (vleUsers == null){
					defineErrorAsynchronousOperation(asynOperId, "Error while processing instantiation file. No participants provided, and no participants are enrolled in the course!");
					return null;
				}
				else {					
					deploy = ldAdaptor.processInstantiation(null, design, vleGroups, vleUsers);
					if (deploy == null){			   
						ArrayList<Participant> parts = new ArrayList<Participant>(vleUsers.values());
						ArrayList<Group> groups = null;
						if(vleGroups!=null){
							groups = new ArrayList<Group>(vleGroups.values());
						}
						deploy = new Deploy(null, design, null, null, null, new Date(), null, null, null, parts, groups);
					}
				}
			}
			// set the title of the deploy with the name received
			// from the request
			deploy.setName(deployTitle);
			deploy.setId(deployId);

			// We now ignore the author from the form, and use the login to
			// GLUEPS
			// deploy.setAuthor(author);
			deploy.setAuthor(login);

			deploy.setLearningEnvironment(leObj);

			deploy.setCourse(new Course(courseId, courses.get(trimId(courseId)),null, null, null));
			
			deploy.setLdshakeFrameOrigin(ldshakeFrameOrigin);
			
		    //the post design deploy process has finished
		    deploy.setInProcess(false);
		    
			// We store the deurlified design in DB
			try {
				dbmanager.insertDesign(deURLifyDesign(design));
			} catch (Exception e) {
				defineErrorAsynchronousOperation(asynOperId, "Error while trying to insert the design in DB");
				return null;
			}
		    
		    
			// Guardo Xml en base de datos
			Deploy deurlifiedDeploy;
			try {
				deurlifiedDeploy = deURLifyDeploy(deploy);
			} catch (Exception e) {		
				deleteDraftDesign(deploy);
				defineErrorAsynchronousOperation(asynOperId, "Error while processing the generated deploy");
				return null;
			}
			
			deurlifiedDeploy = fixToolKinds(deurlifiedDeploy);	
			try {
				dbmanager.insertDeploy(deurlifiedDeploy);
			} catch (Exception e) {
				deleteDraftDesign(deploy);
				defineErrorAsynchronousOperation(asynOperId, "Error while trying to insert the generated deploy into the DB");
				return null;
			}
			defineSuccessAsynchronousOperation(asynOperId, "The design and deploy have been created", app.getAppExternalUri() + "deploys/" + deploy.getId());
			return deploy;
		}
	}
	
	/**
	 * Does a background creation of a deploy from a POST request
	 * @author Javier Hoyos Torio
	 *
	 */
	private class BackgroundLiveCreateDeploy implements Callable<Deploy> {
		
		private String vleId;
		private String courseId;
		private String deployId;
		private boolean found; 
		private String deployType;
		private Design design;
		private String uploadDir;
		private String deployTitle;
		private String login;
		private boolean incremental;
		private String startLesson;
		private GLUEPSManagerApplication app;
		private long asynOperId;
		
		public BackgroundLiveCreateDeploy(String vleId, String courseId, String deployId, boolean found, String deployType, Design design, String uploadDir, String deployTitle, String login, boolean incremental, String startLesson, GLUEPSManagerApplication app, long asynOperId) {
			this.vleId = vleId;
			this.courseId = courseId;
			this.deployId = deployId;
			this.found = found;
			this.deployType = deployType;
			this.design = design;
			this.uploadDir = uploadDir;
			this.deployTitle = deployTitle;
			this.login = login;
			this.incremental = incremental;
			this.startLesson = startLesson;
			this.app = app;
			this.asynOperId = asynOperId;
		}
		
		public Deploy call() {
			JpaManager dbmanager = JpaManager.getInstance();
			HashMap<String, Participant> vleUsers = null;			
			HashMap<String, Group> vleGroups = null;
			HashMap<String,String> courses = null;
			Deploy deploy;
			String designId = design.getId();
			
			LearningEnvironmentResource ler = new LearningEnvironmentResource();
			ler.setLearningEnvironment(vleId, vleId);
			ler.setApplicationResource(app);
			LearningEnvironment le = ler.getCompleteLEObject();
			System.out.println("Trying to contact VLE: "+le.getAccessLocation().toString());

			VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory(app);
			IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(le);
			
			//If we got a course ID, we get the course users, and if not, all the VLE users
			if(courseId==null) vleUsers = adaptor.getUsers(le.getAccessLocation().toString());
			else{
				vleUsers = adaptor.getCourseUsers(le.getAccessLocation().toString(), trimId(courseId));
				vleGroups = adaptor.getCourseGroups(le.getAccessLocation().toString(), trimId(courseId));
			}
			
			
			if (app.getOnlyUserCourses()){
				courses = adaptor.getCourses(le.getAccessLocation().toString(), le.getCreduser());
			}
			else{
				courses = adaptor.getCourses(le.getAccessLocation().toString());
			}
			
			if (vleUsers!=null) {

	       		//We set the deployId for all participants (the Moodle adaptor does not know it!)
	       		for(Iterator<String> it = vleUsers.keySet().iterator(); it.hasNext();){
	       			String key = (String) it.next();
	       			Participant p = vleUsers.get(key);
	       			p.setDeployId(deployId);
	       			vleUsers.put(key, p);
	       		}
			}
			
	       	if(vleGroups!=null){
		    	//We set the deployId for all groups (the Moodle adaptor does not know it!)
		       	for(Iterator<String> it = vleGroups.keySet().iterator(); it.hasNext();){
		       		String key = (String) it.next();
		       		Group g = vleGroups.get(key);
		       		g.setDeployId(deployId);
		       		vleGroups.put(key, g);
		       	}

	       	}
	       	
			if (found) {

				// Received xml file is now on disk. We use the LD
				// adaptor to get the Deploy
				LDAdaptorFactory factory = new LDAdaptorFactory(app);
				ILDAdaptor ldAdaptor = factory.getLDAdaptor(deployType, designId);

				//TODO: What happens if vleUsers==null??
				deploy = ldAdaptor.processInstantiation(uploadDir + deployId + "_original.xml", design, vleGroups, vleUsers);
				if(deploy==null) {					
					defineErrorAsynchronousOperation(asynOperId, "Error while processing instantiation file. Probably bad format");
					return null;
				}

			} else {//We found no xml instantiation file, we have to create an empty deploy: no groups unless present in VLE, no instancedactivities, no toolinstances, list of participants from the VLE
		        
				//Since right now we cannot create users from scratch (they have to come from the LD tool or from the VLE), if the vleUsers == null, we through an error 
				if(vleUsers==null){
					defineErrorAsynchronousOperation(asynOperId, "Error while processing instantiation file. No participants provided, and no participants are enrolled in the course!");
					return null;
				}
				else{
				   
					LDAdaptorFactory factory = new LDAdaptorFactory(app);
					ILDAdaptor ldAdaptor = factory.getLDAdaptor(deployType, designId);
					deploy = ldAdaptor.processInstantiation(null, design, vleGroups, vleUsers);
					if (deploy == null){			   
						ArrayList<Participant> parts = new ArrayList<Participant>(vleUsers.values());
						ArrayList<Group> groups = null;
						if(vleGroups!=null){
							groups = new ArrayList<Group>(vleGroups.values());
						}
						deploy = new Deploy(null, design, null, null, null, new Date(), null, null, null, parts, groups);
					}
				}
			}
			
			// set the title of the deploy with the name received
			// from the request
			deploy.setName(deployTitle);
			deploy.setId(deployId);

			//We now ignore the author from the form, and use the login to GLUEPS
			//deploy.setAuthor(author);
			deploy.setAuthor(login);
			
			deploy.setLearningEnvironment(le);
			
			deploy.setCourse(new Course(courseId, courses.get(trimId(courseId)), null, null, null));
			
			// set the incremental deploy parameters
			if(incremental && startLesson!=null){
				String incrementalParameter = GLUEPSManagerApplication.STARTING_SECTION_FIELD+Deploy.DEPLOY_DATA_VALUE_SEPARATOR+startLesson+Deploy.DEPLOY_DATA_FIELD_SEPARATOR;
				if(deploy.getDeployData()!=null) deploy.setDeployData(deploy.getDeployData()+incrementalParameter);
				else deploy.setDeployData(incrementalParameter);
			}
			
		    //the import deploy process has finished
		    deploy.setInProcess(false);
			Deploy deurlifiedDeploy;
			try {
				deurlifiedDeploy = deURLifyDeploy(deploy);
			} catch (Exception e) {
				defineErrorAsynchronousOperation(asynOperId, "Error while processing the generated deploy");
				return null;
				
			}
			
			deurlifiedDeploy = fixToolKinds(deurlifiedDeploy);
			try {
				dbmanager.insertDeploy(deurlifiedDeploy);
			} catch (Exception e) {
				//deleteDraftDeploy(deploy);
				defineErrorAsynchronousOperation(asynOperId, "Error while trying to insert the generated deploy into the DB");
				return null;
			}
			defineSuccessAsynchronousOperation(asynOperId, "The deployment for that design has been created", app.getAppExternalUri() + "deploys/" + deploy.getId());
			return deploy;
		}
	}
	
	
	/**
	 * Does a background importation of a deploy from a POST request
	 * @author Javier Hoyos Torio
	 *
	 */
	private class BackgroundLiveImportDeploy implements Callable<Deploy> {
		
		private Deploy deploy;
		private LearningEnvironmentResource ler;
		private String courseId;
		private String originalAuthor;
		private boolean importToolConf;
		private GLUEPSManagerApplication app;
		private long asynOperId;
		
		public BackgroundLiveImportDeploy(Deploy deploy, LearningEnvironmentResource ler, String courseId, String originalAuthor, boolean importToolConf, GLUEPSManagerApplication app, long asynOperId) {
			this.deploy = deploy;
			this.ler = ler;
			this.courseId = courseId;
			this.originalAuthor = originalAuthor;
			this.importToolConf = importToolConf;
			this.app = app;
			this.asynOperId = asynOperId;
		}
		
		public Deploy call() {
			JpaManager dbmanager = JpaManager.getInstance();
			HashMap<String, Participant> vleUsers = null;
			HashMap<String, Group> vleGroups = null;
			HashMap<String,String> courses = null;
			LearningEnvironment leDeploy = deploy.getLearningEnvironment();
			String deployId = deploy.getId();
			String leCourseId = null;
			if (deploy.getCourse()!=null){
				leCourseId = trimId(deploy.getCourse().getId()); //The Course id in the XML containing the deploy info
			}
			String leSelectedCourseId = trimId(courseId); //The Course id in the LE selected/provided
			
			ler.setApplicationResource(app);
			LearningEnvironment leObj = ler.getCompleteLEObject();
		    deploy.setLearningEnvironment(leObj);

			VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory(app);
			IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(leObj);
			if (app.getOnlyUserCourses()){
				courses = adaptor.getCourses(leObj.getAccessLocation().toString(), leObj.getCreduser());
			}
			else{
				courses = adaptor.getCourses(leObj.getAccessLocation().toString());
			}
		    deploy.setCourse(new Course(courseId, courses.get(trimId(courseId)), null, null, null));	
		    
			//If the original LE does not exist in glue!ps or it is selected a different LE or course, the original participants have to be deleted
		    if (!leObj.getType().equals("Blogger") && (leObj.getAccessLocation().equals(leDeploy.getAccessLocation())==false || (leCourseId==null && leSelectedCourseId!=null) || (leCourseId!=null && leCourseId.equals(leSelectedCourseId)==false) 
		    		|| leObj.getCreduser().equals(leDeploy.getCreduser())==false))
			{	
										
				System.out.println("Trying to contact VLE: "+leObj.getAccessLocation().toString());
					
				//If we got a course ID, we get the course users, and if not, all the VLE users
				if(courseId==null) vleUsers = adaptor.getUsers(leObj.getAccessLocation().toString());
				else{
					vleUsers = adaptor.getCourseUsers(leObj.getAccessLocation().toString(), trimId(courseId));
					vleGroups = adaptor.getCourseGroups(leObj.getAccessLocation().toString(), trimId(courseId));
				}
					
				if (vleUsers!=null) {
			       	//We set the deployId for all participants (the Moodle adaptor does not know it!)
			       	for(Iterator<String> it = vleUsers.keySet().iterator(); it.hasNext();){
			       		String key = (String) it.next();
			       		Participant p = vleUsers.get(key);
			       		p.setDeployId(deployId);
			       		vleUsers.put(key, p);
			       	}
				}
					
			    if(vleGroups!=null){
				    //We set the deployId for all groups (the Moodle adaptor does not know it!)
				    for(Iterator<String> it = vleGroups.keySet().iterator(); it.hasNext();){
				       	String key = (String) it.next();
				       	Group g = vleGroups.get(key);
				       	g.setDeployId(deployId);
				       	vleGroups.put(key, g);
				    }
			    }
					
			    if(vleUsers==null){
					defineErrorAsynchronousOperation(asynOperId, "Error while processing instantiation file. No participants provided, and no participants are enrolled in the course!");
					return null;
			    }
			    else{
			    	ArrayList<Participant> parts = new ArrayList<Participant>(vleUsers.values());
						
			    	//We delete the previous participants and include the new ones from the vle
			    	deploy.setParticipants(parts);
			    }
				//We empty the groups of the deploy
			    for (Group g: deploy.getGroups())
			    {
			        g.setParticipantIds(new ArrayList<String>());
			    }
			        
				ArrayList<Group> groups = null;
				if(vleGroups!=null){
					groups = new ArrayList<Group>(vleGroups.values());
					deploy.getGroups().addAll(groups);
				}
			}	
		    //Check if we need to delete the tool instances information
		    if (!originalAuthor.equals(deploy.getAuthor()) || importToolConf==false || 
		    		leObj.getAccessLocation().equals(leDeploy.getAccessLocation())==false || (leCourseId==null && leSelectedCourseId!=null) || (leCourseId!=null && leCourseId.equals(leSelectedCourseId)==false) 
		    		|| leObj.getCreduser().equals(leDeploy.getCreduser())==false)
		    {			      
		    	if (deploy.getToolInstances()!=null){
		        	Design d = deploy.getDesign();
		        	LearningEnvironment selectedLe = deploy.getLearningEnvironment();
			        for (ToolInstance ti : deploy.getToolInstances())
			        {
			        	String resourceId = ti.getResourceId();
			        	String location;
			        	if (ti.getLocation()!=null){
			        		location = ti.getLocation().toString();
			        		String urlGlueOriginal = "";
			        		//The url of the instances in glueps contain the substring /instance
			        		if (location.indexOf("instance") != -1){
			        			//Get the GLUE url
			        			urlGlueOriginal = location.substring(0, location.indexOf("instance"));
			        		}
				       		Resource r = d.getResourceById(resourceId);
					       	String valueOriginal = findToolIdInExternalTools(leDeploy,trimId(r.getToolType()));
					       	String valueCurrent = findToolIdInExternalTools(selectedLe,trimId(r.getToolType()));
				       		if (!valueOriginal.equals(valueCurrent) || importToolConf == false || (!urlGlueOriginal.equals("") && (!urlGlueOriginal.equals(app.getGmurl())))){
				       			ti.setLocation(null);
				       		}
			        	}
			        }
		        }
		        deploy.setStaticDeployURL(null);
		        deploy.setLiveDeployURL(null);
		    }
		    //the import deploy process has finished
		    deploy.setInProcess(false);
		    			
			Design design = deploy.getDesign();
			Design deurlifiedDesign = deURLifyDesign(design);
			//We store the deurlified design in DB
			try {
				dbmanager.insertDesign(deurlifiedDesign);
			} catch (Exception e) {
				defineErrorAsynchronousOperation(asynOperId, "Error while trying to insert the design in DB");
			}
			
			Deploy deurlifiedDeploy;
			try {
				deurlifiedDeploy = deURLifyDeploy(deploy);
			} catch (Exception e) {
				deleteDraftDesign(deploy);
				defineErrorAsynchronousOperation(asynOperId, "Error while processing the generated deploy");
				return null;
			}
			deurlifiedDeploy = fixToolKinds(deurlifiedDeploy);
			try {
				dbmanager.insertDeploy(deurlifiedDeploy);
			} catch (Exception e) {
				deleteDraftDesign(deploy);
				deleteDraftDeploy(deploy);
				defineErrorAsynchronousOperation(asynOperId, "Error while trying to insert the generated deploy into the DB");
				return null;
			}
			
			defineSuccessAsynchronousOperation(asynOperId, "The deployment has been created", app.getAppExternalUri() + "deploys/" + deploy.getId());
			return deploy;
		}
	}
	
	
	private void deleteDraftDesign(Deploy deploy){
		JpaManager dbmanager = JpaManager.getInstance();
		dbmanager.deleteDesign(deploy.getDesign().getId());
	}
	
	private void deleteDraftDeploy(Deploy deploy){
		JpaManager dbmanager = JpaManager.getInstance();
		//delete the deploy
		dbmanager.deleteDeploy(deploy.getId());
		//delete all the versions of this deploy
		dbmanager.deleteDeployVersions(deploy.getId());
	}
	
	private void defineSuccessAsynchronousOperation(long asynOperId, String description, String resource){
		JpaManager dbmanager = JpaManager.getInstance();
		AsynchronousOperation asynOper = dbmanager.findAsynchOperObjectById(String.valueOf(asynOperId));
		asynOper.setDescription(description);
		asynOper.setStatus(AsynchronousOperation.STATUS_OK);
		asynOper.setResource(resource);
		asynOper.setFinished(new Date());
		try {
			dbmanager.insertAsynchronousOperation(asynOper);
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
