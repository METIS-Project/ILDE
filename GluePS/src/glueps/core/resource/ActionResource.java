package glueps.core.resource;

import glue.common.format.FormattedEntry;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Activity;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.Group;
import glueps.core.model.InstancedActivity;
import glueps.core.model.Resource;
import glueps.core.model.ToolInstance;
import glueps.core.persistence.JpaManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.net.URL;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class ActionResource extends GLUEPSResource {

	/** 
	 * Logger 
	 * 
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
 
	/** Local id., without extension */
    protected String deployId;
        
    protected Deploy deploy;
	
    private static String FIELD_SEPARATOR=";";
    
    private static String PARTICIPANT_ELEMENT="participant";
    private static String ACTIVITY_ELEMENT="activity";
    private static String GROUP_ELEMENT="group";
    private static String RESOURCE_ELEMENT="resource";
    private static String DEPLOY_ELEMENT="deployVLE";
    
    private static String COLOR_ERROR="#FF9999";
    private static String COLOR_DEPLOY="#9999FF";
    private static String COLOR_OK="#99FF99";
    private static String COLOR_NONE="#FFFFFF";

    private static String MARK_MESSAGE="<!--MESSAGE-->";
    private static String MARK_COLOR="<!--COLOR-->";
    
    //activity Id upon which the new ones are added, as children
    private static String ORIGIN_ACTIVITY_ID="0";
    
    private static String DEFAULT_TOOL_TYPE="21";
    
    //TODO: this is hardcoded, and should not be so!! but we are not identified in this resource
    private String login="lprisan";
    
    private String RESPONSE_TEMPLATE_FILE="transaction.template.html";
    
    @Override
    protected void doInit() throws ResourceException {
    	
   		// get the "toolId" attribute value taken from the URI template /deploy/{deployId}
   		this.deployId = trimId((String) getRequest().getAttributes().get("deployId"));
   		
   		logger.info("Initializing deploy resource "+this.deployId);
   		
   		JpaManager dbmanager = JpaManager.getInstance();
   		deploy = dbmanager.findDeployObjectById(this.deployId);

   		// does it exist?
		setExisting(this.deploy != null);	// setting 'false' implies that REST methods won't start; server will respond with 404
    }

	
    @Get()
    public Representation getAction() {
    	
    	Representation answer = null;
    	
    	List actionElements = null;
    	
    	String requestUrl = getReference().getIdentifier();
    	
    	System.out.println("Received ACTION for deploy "+deployId+": "+requestUrl);
    	
    	
    	try {
	    	//TODO: If available, open the deploy's "transaction file", and read it to an array of objects. 
	    	//If not, create it and open it
			GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
			String TRANSACTIONS_DIRECTORY = app.getAppPath()+"/transActions/";
			File transFile = new File(TRANSACTIONS_DIRECTORY+deployId+".txt");
			if(!transFile.exists()){
		    	logger.info("Creating transAction file... "+transFile.toString());
				transFile.createNewFile();
		    	logger.info("TransAction file created");
			}
			else{
		    	logger.info("Opening transAction file... "+transFile.toString());
				actionElements=Files.readLines(transFile, Charsets.UTF_8);
		    	logger.info("TransAction file opened");
			}
			
			
			logger.info("Processing action "+requestUrl);
			
	    	//if it is a "closing" action (OK, NOK, QuestionMark), resolve it
	    	if(requestUrl.contains("/actions/NOK")){//we just delete the entire transaction
	    		actionElements=null;
	    		if(transFile.exists()) transFile.delete();
	    		answer = getResponseRepresentation(COLOR_ERROR, "The transaction has been cancelled");
	    	}else if(requestUrl.contains("/actions/OK")){//we find out what the transaction is (depending on the first element of transaction), and execute it
	    		if(actionElements!=null && actionElements.size()>0){
	    			String message = executeAction(actionElements);
	    			if(message!=null) answer = getResponseRepresentation(COLOR_OK, message);
	    			else answer =  getResponseRepresentation(COLOR_ERROR, "There was an error executing the action. Try again the whole action");
	    		} else answer = getResponseRepresentation(COLOR_ERROR, "There was no transaction to accept");
	    		if(transFile.exists()) transFile.delete();
	    	}else if(requestUrl.contains("/actions/QuestionMark")){//we find out what is the question about, and return the adequate information
	    		if(actionElements!=null && actionElements.size()>0){
	    			String message = executeQuestion(actionElements);
	    			if(message!=null) answer = getResponseRepresentation(COLOR_NONE, message);
	    			else answer =  getResponseRepresentation(COLOR_ERROR, "There was an error when processing the question. Try again the whole question");
	    		} else answer = getResponseRepresentation(COLOR_ERROR, "There was nothing to ask about");
	    		if(transFile.exists()) transFile.delete();
	    	}
	
	    	//if it is not a closing action, add received element (as it is, without URL decoding! we need it for the IDs) to the action and store/close the transaction file
	    	else if(requestUrl.contains("/actions/participant")){
	    		String participantId = (String) getRequest().getAttributes().get("participantId");
	    		if(participantId==null) throw new Exception("Received a null participantId!");
	    		if(actionElements==null) actionElements=new ArrayList();
	    		actionElements.add(PARTICIPANT_ELEMENT+FIELD_SEPARATOR+participantId);
	    		writeActionFile(transFile, actionElements);
	    		answer = getResponseRepresentation(COLOR_NONE, "Added participant \""+URLDecoder.decode(participantId)+"\" to the action.");
	    	}else if(requestUrl.contains("/actions/group")){
	    		String groupId = (String) getRequest().getAttributes().get("groupId");
	    		if(groupId==null) throw new Exception("Received a null groupId!");
	    		if(actionElements==null) actionElements=new ArrayList();
	    		actionElements.add(GROUP_ELEMENT+FIELD_SEPARATOR+groupId);
	    		writeActionFile(transFile, actionElements);
	    		answer = getResponseRepresentation(COLOR_NONE, "Added group \""+URLDecoder.decode(groupId)+"\" to the action.");
	    	}else if(requestUrl.contains("/actions/activity")){
	    		String activityId = (String) getRequest().getAttributes().get("activityId");
	    		if(activityId==null) throw new Exception("Received a null activityId!");
	    		if(actionElements==null) actionElements=new ArrayList();
	    		actionElements.add(ACTIVITY_ELEMENT+FIELD_SEPARATOR+activityId);
	    		writeActionFile(transFile, actionElements);
	    		answer = getResponseRepresentation(COLOR_NONE, "Added activity \""+URLDecoder.decode(activityId)+"\" to the action.");
	    	}else if(requestUrl.contains("/actions/resource")){
	    		String toolId = (String) getRequest().getAttributes().get("toolId");
	    		if(toolId==null) throw new Exception("Received a null toolId!");
	    		if(actionElements==null) actionElements=new ArrayList();
	    		actionElements.add(RESOURCE_ELEMENT+FIELD_SEPARATOR+toolId);
	    		writeActionFile(transFile, actionElements);
	    		answer = getResponseRepresentation(COLOR_NONE, "Added a tool \""+URLDecoder.decode(toolId)+"\" to the action.");
	    	}else if(requestUrl.contains("/actions/deployVLE")){
	    		if(actionElements==null) actionElements=new ArrayList();
	    		actionElements.add(DEPLOY_ELEMENT);
	    		writeActionFile(transFile, actionElements);
	    		answer = getResponseRepresentation(COLOR_DEPLOY, "You asked to deploy the current activities.");
	    	}
    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			answer = getResponseRepresentation(COLOR_ERROR, "Some error occurred while processing the action.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			answer = getResponseRepresentation(COLOR_ERROR, "Some error occurred while processing the action.");
		}
    	//answer = getResponseRepresentation("You clicked me!");
    	
    	return answer;
    }

    
    private Representation getResponseRepresentation(String color, String message){
    	
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		String TRANSACTIONS_DIRECTORY = app.getAppPath()+"/transActions/";
		File template = new File(TRANSACTIONS_DIRECTORY+RESPONSE_TEMPLATE_FILE);
    	
		String response = null;
		try {
			//We read the template file
			response = Files.toString(template, Charsets.UTF_8);
		} catch (IOException e) {
			// If the template is not available, we just return the text message
			e.printStackTrace();
			return new StringRepresentation(message);
		}
		
		response = response.replace(MARK_COLOR, color);
		response = response.replace(MARK_MESSAGE, message);
		
		StringRepresentation rep = new StringRepresentation((CharSequence) response, MediaType.TEXT_HTML);
		rep.setCharacterSet(CharacterSet.UTF_8);
		return rep;
    	
    }

	private String executeAction(List actionElements) throws MalformedURLException {
		
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		
		//We get the first element as the determining factor of what we want to do
		String first = (String) actionElements.get(0);
		String[] firstElems = first.split(FIELD_SEPARATOR);
		String action = firstElems[0];
		String actionId=null;
		if(firstElems.length>1) actionId = firstElems[1];
		
		//In all cases, the action ID's are URLencoded!!
		
		//If it is to deploy a VLE, we deploy it
		if(action.equals(DEPLOY_ELEMENT)){
			logger.info("Trying to execute deployment of "+deployId);
			
			DeployResource depRes = new DeployResource();
			
			if(deploy.getLiveDeployURL()==null) depRes.doLiveDeploy(deploy, false);
			else depRes.doLiveDeploy(deploy, true);
			logger.info("Successfully started deployment of "+deployId);

			return "Deployment is in process. Please wait for a few seconds and refresh the activities page";
		}
		
		//If it is activity, we only allow to create a new activity, with that ID
		else if(action.equals(ACTIVITY_ELEMENT)){
			String activityId = actionId;
			Design newDesign = deploy.getDesign();
			logger.info("Trying to add activity "+activityId);
			
			Activity origin = newDesign.findActivityById(ORIGIN_ACTIVITY_ID);
			ArrayList<Activity> activities = origin.getChildrenActivities();
			activities.add(new Activity(activityId, URLDecoder.decode(activityId), "Add description here", Activity.GROUP_SEPARATE, null, ORIGIN_ACTIVITY_ID, null, null));
			origin.setChildrenActivities(activities);
			
			//newDesign.getRootActivity().getChildrenActivities().set(0, origin);
			//TODO: This only works if the origin activity is the root one!!! we should try to come up with a more general solution...
			newDesign.setRootActivity(origin);
			deploy.setDesign(newDesign);
			logger.info("Added activity "+activityId+" to deploy "+deployId+". Storing in DB...");

			storeDeploy(deploy);
			return "The new activity \""+URLDecoder.decode(activityId)+"\" has been added.";
		}
		
		//If it is a group, we can 1) assign a group (and participants?) to an activity; or 2) assign participants to a group
		else if(action.equals(GROUP_ELEMENT)){
			String groupId = actionId;
			logger.info("Trying to add group "+groupId);

			ArrayList<String> participantIds=null;
			String activityId=null;
			
			//We parse the action elements
			for(Iterator it=actionElements.iterator(); it.hasNext();){
				String[] element = ((String) it.next()).split(FIELD_SEPARATOR);
				if(element.length>1){
					if(element[0].equals(ACTIVITY_ELEMENT)) activityId=element[1];
					else if(element[0].equals(PARTICIPANT_ELEMENT)){
						if(participantIds==null) participantIds = new ArrayList<String>();
						participantIds.add(element[1]);
					}
				}	
			}
			
			
			//if there isn't a group with this id, we create it
			Group newGroup = deploy.findGroupById(groupId);
			if(newGroup==null){
				logger.info("The group does not exist. We create it");
				newGroup = new Group(groupId, URLDecoder.decode(groupId), deploy.getId(), null);
			}
			//If we had participants in the action, we update the group composition
			if(participantIds!=null) newGroup.setParticipantIds(participantIds);

			//We add the group to the deploy
			logger.info("Adding the group to the groups array");
			ArrayList<Group> groups = deploy.getGroups();
			ListIterator<Group> listGroups = groups.listIterator();
			boolean substituted=false;
			while(listGroups.hasNext()){
				if(listGroups.next().getId().equals(groupId)){
					listGroups.set(newGroup);
					substituted=true;
				}
			}
			if(!substituted){
				logger.info("The group was not substituted. We just add it to the groups array");
				groups.add(newGroup);
			}
			deploy.setGroups(groups);

			//If there is an activityId, we create an instancedActivity and add the group to the activity
			//we also create and add a bucket with default configuration to the instancedActivity
			if(activityId!=null){
				logger.info("We are trying to add the group to activity "+activityId);
				ArrayList<InstancedActivity> instActs = deploy.getInstancedActivities();
				InstancedActivity newIA = new InstancedActivity(activityId+"_"+groupId, deploy.getId(), activityId, groupId, null, null); 
				
				//We create a new resource in that activity
				String resourceId = DEFAULT_TOOL_TYPE+"_"+(new Date()).getTime();
				Design newDesign = deploy.getDesign();
				ArrayList<Resource> resources = newDesign.getResources();
				resources.add(new Resource(resourceId, "New bucket for activity "+URLDecoder.decode(activityId), true, null, Resource.TOOL_KIND_EXTERNAL, DEFAULT_TOOL_TYPE, null));
				newDesign.setResources(resources);
				logger.info("added resource to the design: "+resourceId);
				Activity act = newDesign.findActivityById(activityId);
				logger.info("Assigning resource "+resourceId+" to the activity "+act.toString());				
				ArrayList<String> res = act.getResourceIds();
				if(res==null) res = new ArrayList<String>();
				res.add(resourceId);
				act.setResourceIds(res);
				logger.info("added resource to the activity "+activityId+". Resulting resources: "+newDesign.findActivityById(activityId).getResourceIds().toString());
				deploy.setDesign(newDesign);
				
				//We create a toolInstance 
				ArrayList<ToolInstance> toolInsts = deploy.getToolInstances();
				String instId = "BUCKET_"+activityId+"_"+groupId+"_"+(new Date()).getTime();
				//We create the tool instance with the default configuration!!
				logger.info("trying to create instance "+instId);
				String configData = getToolInstanceConfiguration(DEFAULT_TOOL_TYPE);
				String url=null;
				logger.info("Retrieved configuration "+configData);
				ArrayList<String> partIds=newGroup.getParticipantIds();
				String[] participants = null;
				if(partIds!=null){
					participants = new String[partIds.size()];
					participants = partIds.toArray(participants);
				}		
				
				logger.info("Configuring instance for users "+participants);
				try {
					FormattedEntry answerEntry = createToolInstanceGM(DEFAULT_TOOL_TYPE, configData, deploy.getStaffUsernames(), participants);
					String createdInstanceId = answerEntry.getId();
					logger.info("Created the instance "+ createdInstanceId);
					url = answerEntry.getLinks().get(0);
					
					if(!url.startsWith(app.getGmurl())){//If the gluelet/bucket URL is not visible from outside (e.g. localhost), we update the URL
						String prefix = url.substring(0, url.lastIndexOf("GLUEletManager/")+15);
						String newURL = url.replace((CharSequence) prefix, (CharSequence) app.getGmurl());
						System.out.println("Updating (transformed) location to "+newURL); 
						url=newURL;
					}
					
					logger.info("The end-user URL is " + url);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
				
				toolInsts.add(new ToolInstance(instId, "Bucket for group \""+URLDecoder.decode(groupId)+"\" in activity \""+URLDecoder.decode(activityId)+"\"", deploy.getId(), resourceId, new URL(url)));
				deploy.setToolInstances(toolInsts);
				logger.info("Added tool instance to the deploy. Resulting array: "+deploy.getToolInstances().toString());

				//...and add it to that instancedactivity
				logger.info("Adding instance to the instanced activity...");
				ArrayList<String> iaToolInsts=newIA.getInstancedToolIds();
				if(iaToolInsts==null) iaToolInsts = new ArrayList<String>();
				iaToolInsts.add(instId);
				newIA.setInstancedToolIds(iaToolInsts);

				logger.info("Adding instanced activity to the deploy "+deployId);
				instActs.add(newIA);
				deploy.setInstancedActivities(instActs);
			}

			//We store deploy in DB
			logger.info("Added group "+groupId+" to deploy "+deployId+". Storing in DB...");
			storeDeploy(deploy);
			return "The group \""+URLDecoder.decode(groupId)+"\" has been updated and added to activity \""+URLDecoder.decode(activityId)+"\". Its participants are "+participantIds.toString();
		}
		
		//If it is a resource, we add one instance of the resource to that group in that activity
		//TODO: This will not be used, probably... we now create a bucket when adding the instancedactivity
		else if(action.equals(RESOURCE_ELEMENT)){
			String toolId = actionId;
			logger.info("Trying to create an instance of type "+toolId);

			//We parse the action elements
			String groupId=null;
			String activityId=null;
			
			//We parse the action elements
			logger.info("Parsing the action elements...");
			for(Iterator it=actionElements.iterator(); it.hasNext();){
				String[] element = ((String) it.next()).split(FIELD_SEPARATOR);
				if(element.length>1){
					if(element[0].equals(ACTIVITY_ELEMENT)) activityId=element[1];
					else if(element[0].equals(GROUP_ELEMENT)) groupId=element[1];
				}	
			}
			
			logger.info("Parsing finished. Looking up the tool "+toolId);

			//We try to find the tool kind by looking in the LE
			boolean external = false;
			boolean internal = false;
			if(deploy.getLearningEnvironment().getExternalTools().containsKey(toolId)) external=true;
			if(deploy.getLearningEnvironment().getInternalTools().containsKey(toolId)) internal=true;
			String toolKind = null;
			if((!internal && !external)||(internal && external)) return null;
			else if(!external && internal) toolKind = "internal";
			else if(!internal && external) toolKind = "external";
			logger.info("The tool "+toolId+" is "+toolKind);

			String resourceId = toolId+"_"+(new Date()).getTime();
			logger.info("trying to create resource "+resourceId);

			//We create a new resource in that activity
			if(activityId==null || groupId==null) return null;
			else{
				Design newDesign = deploy.getDesign();
				ArrayList<Resource> resources = newDesign.getResources();
				resources.add(new Resource(resourceId, "New resource "+URLDecoder.decode(toolId), true, null, toolKind, toolId, null));
				newDesign.setResources(resources);
				logger.info("added resource to the design: "+resourceId);

				Activity act = newDesign.findActivityById(activityId);
				logger.info("Assigning resource "+resourceId+" to the activity "+act.toString());
				
				ArrayList<String> res = act.getResourceIds();
				if(res==null) res = new ArrayList<String>();
				res.add(resourceId);
				act.setResourceIds(res);
				
				logger.info("added resource to the activity "+activityId+". Resulting resources: "+newDesign.findActivityById(activityId).getResourceIds().toString());
				
				
				deploy.setDesign(newDesign);
			}
			
			//We create a toolInstance 
			ArrayList<ToolInstance> toolInsts = deploy.getToolInstances();
			String instId = toolId+"_"+activityId+"_"+groupId+"_"+(new Date()).getTime();
			//We create the tool instance with the default configuration!!
			logger.info("trying to create instance "+instId);
			String configData = getToolInstanceConfiguration(toolId);
			String url=null;
			logger.info("Retrieved configuration "+configData);

			ArrayList<String> partIds=deploy.findGroupById(groupId).getParticipantIds();
			String[] participants = new String[partIds.size()];
			participants = partIds.toArray(participants);
			logger.info("Configuring instance for users "+participants);
			try {
				FormattedEntry answerEntry = createToolInstanceGM(toolId, configData, deploy.getStaffUsernames(), participants);
				String createdInstanceId = answerEntry.getId();
				logger.info("Created the instance "+ createdInstanceId);
				url = answerEntry.getLinks().get(0);
				logger.info("The end-user URL is " + url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			toolInsts.add(new ToolInstance(instId, "Instance of type \""+URLDecoder.decode(toolId)+"\" in group \""+URLDecoder.decode(groupId)+"\" for activity \""+URLDecoder.decode(activityId)+"\"", deploy.getId(), resourceId, new URL(url)));
			deploy.setToolInstances(toolInsts);
			logger.info("Added tool instance to the deploy. Resulting array: "+deploy.getToolInstances().toString());

			//...and add it to that instancedactivity
			logger.info("Adding instance to the instanced activity...");
			ArrayList<InstancedActivity> instActs = deploy.getInstancedActivities();
			InstancedActivity updatedInstAct = null;
			for(Iterator it=instActs.iterator(); it.hasNext();){
				InstancedActivity ia = (InstancedActivity) it.next();
				if(ia.getGroupId().equals(groupId) && ia.getActivityId().equals(activityId)) updatedInstAct=ia;
			}
			if(updatedInstAct==null) return null;
			else{
				logger.info("Adding to instanced activity "+updatedInstAct.toString());

				ArrayList<String> iaToolInsts=updatedInstAct.getInstancedToolIds();
				if(iaToolInsts==null) iaToolInsts = new ArrayList<String>();
				iaToolInsts.add(instId);
				updatedInstAct.setInstancedToolIds(iaToolInsts);
			}
			ListIterator<InstancedActivity> listInstActs = instActs.listIterator();
			while(listInstActs.hasNext()){
				InstancedActivity ia = listInstActs.next();
				if(ia.getGroupId().equals(groupId) && ia.getActivityId().equals(activityId)) listInstActs.set(updatedInstAct);
			}
			logger.info("Adding instanced activity to the deploy "+deployId);

			deploy.setInstancedActivities(instActs);
			logger.info("Instanced activity added. Resulting ia array: "+deploy.getInstancedActivities().toString());
			
			//We store it in the DB
			logger.info("Added tool type "+toolId+" to deploy "+deployId+". Storing in DB...");

			storeDeploy(deploy);
			return "A resource of type \""+URLDecoder.decode(toolId)+"\" has been added to group \""+URLDecoder.decode(groupId)+"\" in activity \""+URLDecoder.decode(activityId)+"\"";
			
		}
		
		//Participants can never be the first element of an action!
		else if(action.equals(PARTICIPANT_ELEMENT)) return null;
		
		return null;
	}


	private void storeDeploy(Deploy updatedDeploy) {
		//We store the deploy in DB
		//We convert the deploy into one where ids are NOT urls - to store in database
		JpaManager dbmanager = JpaManager.getInstance();
		try {
			dbmanager.insertDeploy(deURLifyDeploy(updatedDeploy));
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert deploy in the DB", e);
		}

	}


	private String executeQuestion(List actionElements) {
		
		if(actionElements==null && actionElements.size()==0) return null;
		
		//The question basically depends on just the first element
		String first = (String) actionElements.get(0);
		
		//TODO: for now, only support deployVLE?
		if(first.startsWith(DEPLOY_ELEMENT)){
			if(deploy.getLiveDeployURL()!=null) return deploy.getLiveDeployURL().toString();
			else return "Sorry, the deploy has not been completed.";
		}
		
		//TODO: also support resource + group + activity
		else if(first.startsWith(RESOURCE_ELEMENT)){
			String toolId=first.substring(first.lastIndexOf(FIELD_SEPARATOR)+1);
			String activityId=null;
			String groupId=null;
			//We get the first activity Id
			for(Iterator it=actionElements.iterator();it.hasNext();){
				String element = (String) it.next();
				if(element.startsWith(ACTIVITY_ELEMENT)) activityId = element.substring(element.indexOf(FIELD_SEPARATOR)+1);
			}
			
			
			//We get the first group Id
			for(Iterator it=actionElements.iterator();it.hasNext();){
				String element = (String) it.next();
				if(element.startsWith(GROUP_ELEMENT)) groupId = element.substring(element.indexOf(FIELD_SEPARATOR)+1);
			}

			//If we have all the elements of the question, try to find it out
			if(activityId!=null && groupId!=null){
				String instancedActivityId = null;
				InstancedActivity instancedAct = null;
				HashMap instActs = deploy.getInstancedActivitiesForActivity(activityId);
				if(instActs==null || instActs.size()==0) return "The activity "+activityId+" does not have resources"; 
				for(Iterator it=instActs.values().iterator();it.hasNext();){
					InstancedActivity iAct = (InstancedActivity) it.next();
					if(iAct.getGroupId().equals(groupId)) instancedAct=iAct;
				}
				if(instancedAct==null) return "The group "+groupId+" is not associated to the activity "+activityId;
				else{//we have located the right instancedActivity - let's try to find the resource of type toolId
					ArrayList<String> toolInstIds = instancedAct.getInstancedToolIds();
					
					ToolInstance toolInst = null;
					for(Iterator it=toolInstIds.iterator();it.hasNext();){
						ToolInstance tInst = deploy.getToolInstanceByTrimmedId((String)it.next());
						if(deploy.getDesign().getResourceById(tInst.getResourceId()).getToolType().endsWith(toolId)) return tInst.getLocationWithRedirects(deploy).toString();
					}
					return "The group "+groupId+" does not have such kind of tool ("+toolId+").";
				}
			}else return "Insufficient data to answer the question. Please scan the resource type, the activity and the group.";
		}
			
		else return null;
		
	}


	private void writeActionFile(File file, List actionElements) throws IOException {
		
		String content = new String();
		
		if(!file.exists()) file.createNewFile();
		
		if(actionElements!=null && actionElements.size()>0){
			
			for(Iterator<Object> it=actionElements.iterator(); it.hasNext();) content+=(((String) it.next())+"\n");
			
		}
		
		Files.write(content, file, Charsets.UTF_8);
		
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

	
	
	//TODO from here below it is copied from ToolInstanceREsource - we should refactor it!!!
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
					newUsers[usersNames.length+teachersNames.length]=login;
				}else{//if there are no students, the teachers are the only users
					newUsers = new String[teachersNames.length];
					for(int i = 0; i<teachersNames.length; i++) newUsers[i]=teachersNames[i];
				}
			}else{//No teachers, we just put the logged in user as callerUser
				//teacher = login;
				if(usersNames!=null && usersNames.length>0){
					newUsers = new String[usersNames.length+1];
					for(int i = 0; i<usersNames.length; i++) newUsers[i] = usersNames[i];
					newUsers[usersNames.length] = login;
				}else{
					newUsers = new String[1];
					newUsers[0] = login;
				}
			}
			
			entry.addExtentedStructuredList("users", "user", newUsers);
			//entry.addExtendedTextChild("callerUser", teacher);			
			entry.addExtendedTextChild("callerUser", login);
			
			return entry.getRepresentation();
		}

		

	
	
	
}
