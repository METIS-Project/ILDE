/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package glueps.core.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeSet;

import javax.annotation.Resources;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Deploy {

	private String id;
	
	private Design design;
	
	private String name;
	
	private LearningEnvironment learningEnvironment;
	
	private String author;
	
	//TODO: Change this to another Timestamp class?
	private Date timestamp;
	
	private String deployData;
	public final static String DEPLOY_DATA_FIELD_SEPARATOR = ";";
	public final static String DEPLOY_DATA_VALUE_SEPARATOR = "=";

	public static final String ID_SEPARATOR = "-";

	public static final String AUTHOR_SEPARATOR = ";";
	

	private ArrayList<InstancedActivity> instancedActivities;
	
	private ArrayList<ToolInstance> toolInstances;
	
	private ArrayList<Participant> participants;
	
	private ArrayList<Group> groups;
	
	//This is the URL where the deploy is available as a static file (e.g. moodle backup file) 
	private URL staticDeployURL = null;
	
	//This is the URL where the deploy has been deployed (e.g. wiki course page)
	private URL liveDeployURL = null;
	
	//This field indicates that the deploy is being deployed right now (so better not touch it or change it since it can lead to inconsistent states)
	private boolean inProcess = false;
	
	//This is the domain of the server cointaining Glue!PS in a iframe
	private String ldshakeFrameOrigin;
	
	private Course course = null;
	
	
	public boolean isInProcess() {
		return inProcess;
	}

	public void setInProcess(boolean inProcess) {
		this.inProcess = inProcess;
	}
	
	public String getLdshakeFrameOrigin() {
		return ldshakeFrameOrigin;
	}

	public void setLdshakeFrameOrigin(String ldshakeFrameOrigin) {
		this.ldshakeFrameOrigin = ldshakeFrameOrigin;
	}



	public Deploy() {
		super();
	}

	

	public Deploy(String id, Design design, String name,
			LearningEnvironment learningEnvironment, String author,
			Date timestamp, String deployData,
			ArrayList<InstancedActivity> instancedActivities,
			ArrayList<ToolInstance> toolInstances,
			ArrayList<Participant> participants, ArrayList<Group> groups) {
		super();
		this.id = id;
		this.design = design;
		this.name = name;
		this.learningEnvironment = learningEnvironment;
		this.author = author;
		this.timestamp = timestamp;
		this.deployData = deployData;
		this.instancedActivities = instancedActivities;
		this.toolInstances = toolInstances;
		this.participants = participants;
		this.groups = groups;
	}


	//This method extracts data from the deployData field, assuming the format is
	//field1=value1;field2=value2;...
	public String getFieldFromDeployData(String field){
		String value = null;
		
		if(field==null || deployData==null || deployData.length()==0) return null;
		
		String[] pairs = deployData.split(DEPLOY_DATA_FIELD_SEPARATOR);
		
		for (int i=0;i<pairs.length;i++){
			if (pairs[i].startsWith(field+DEPLOY_DATA_VALUE_SEPARATOR)) value = pairs[i].substring(field.length()+1);
		}
		
		return value;
	}

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Design getDesign() {
		return design;
	}

	public void setDesign(Design design) {
		this.design = design;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LearningEnvironment getLearningEnvironment() {
		return learningEnvironment;
	}

	public void setLearningEnvironment(LearningEnvironment learningEnvironment) {
		this.learningEnvironment = learningEnvironment;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getDeployData() {
		return deployData;
	}

	public void setDeployData(String deployData) {
		this.deployData = deployData;
	}

	public void setInstancedActivities(ArrayList<InstancedActivity> instancedActivities) {
		this.instancedActivities = instancedActivities;
	}

	@XmlElementWrapper
	@XmlElement(name="instancedActivity")
	public ArrayList<InstancedActivity> getInstancedActivities() {
		return instancedActivities;
	}

	public void setToolInstances(ArrayList<ToolInstance> toolInstances) {
		this.toolInstances = toolInstances;
	}

	@XmlElementWrapper
	@XmlElement(name="toolInstance")
	public ArrayList<ToolInstance> getToolInstances() {
		return toolInstances;
	}


	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}


	@XmlElementWrapper
	@XmlElement(name="group")
	public ArrayList<Group> getGroups() {
		return groups;
	}


	public void setParticipants(ArrayList<Participant> participants) {
		this.participants = participants;
	}

	@XmlElementWrapper
	@XmlElement(name="participant")
	public ArrayList<Participant> getParticipants() {
		return participants;
	}



	@Override
	public String toString() {
		return "Deploy [id=" + id + ", design=" + design + ", name=" + name
				+ ", learningEnvironment=" + learningEnvironment + ", author="
				+ author + ", timestamp=" + timestamp + ", deployData="
				+ deployData + ", instancedActivities=" + instancedActivities
				+ ", toolInstances=" + toolInstances + ", participants="
				+ participants + ", groups=" + groups + "]";
	}



	public String[] getParticipantIdsForToolInstance(String instanceId) {
		
		String[] users = null;
		
		ArrayList<String> userList = new ArrayList<String>();
		
		//We try to find the instancedActivity of the instances, to find out the relevant groups
		for(Iterator<InstancedActivity> it = getInstancedActivities().iterator(); it.hasNext();){
			InstancedActivity instAct = it.next();
			
			if(instAct.getInstancedToolIds()!=null && instAct.getInstancedToolIds().contains(instanceId)){
				//This is a relevant instancedActivity, add the components of the group to the list
				ArrayList<String> participants = this.findGroupById(instAct.getGroupId()).getParticipantIds();
				if(participants!=null){
					for (Iterator<String> it2 = participants.iterator();it2.hasNext();){
						String partId = it2.next();
						//We try to avoid duplicate users in the user list
						if(!userList.contains(partId)) userList.add(partId);
					}
				}
				
			}
			
		}
		//We add the author of the deploy (supposedly the teacher) to the user list - THIS PROVED TO BE BUGGY!
		//userList.add(this.getAuthor());
		
		
		//Convert the list to an array (for some reason, just using .toArray does not work!)
		if(userList.size()>0){
			users = new String[userList.size()];
			for(int i=0;i<userList.size();i++){
				users[i] = (String) userList.get(i);
				
			}
		}
		
		return users;
	}



	public Group findGroupById(String groupId) {
		
		for(Iterator<Group> it = this.getGroups().iterator(); it.hasNext();){
			Group g = it.next();
			if(g.getId().equals(groupId)) return g;
		}
		return null;
		
	}



	public Participant getParticipantById(String partId) {
		
		Participant part = null;
		
		if(this.participants==null || this.participants.size()==0) return null;
		for(Iterator<Participant> it = this.participants.iterator();it.hasNext();){
			part = it.next();
			if (part.getId().equals(partId)) return part;
			
		}
		
		return null;
	}



	public String[] getParticipantUsernamesForToolInstance(String toolInstId) {
		
		String[] userids = getParticipantIdsForToolInstance(toolInstId);
		
		if (userids == null || userids.length ==0) return null;
		
		//TODO This procedure is VLE-dependent! by now, we only implement the Moodle one
		String[] usernames = new String[userids.length];
			
		for (int i=0;i<userids.length;i++){
			Participant p = this.getParticipantById(userids[i]);
			
			//TODO If we don't find the username, we just use the provided ids. This is potentially buggy
			if(p==null||p.getLearningEnvironmentData()==null) usernames[i] = userids[i];
			else{
				// The format of the learningEnvironmentData is: userid;username;name;firstAccess
				String[] partFields = p.getLearningEnvironmentData().split(";");
				//TODO If we don't find the username, we just use the provided ids. This is potentially buggy
				if(partFields.length<2) usernames[i] = userids[i];
				else usernames[i] = partFields[1];
			}
		}
		
		return usernames;
	}

	
	public HashMap<String, InstancedActivity> getInstancedActivitiesForToolInstance(String toolInstId){
		
		HashMap<String, InstancedActivity> instActivities = null;
		if(this.instancedActivities!=null){
			for(Iterator<InstancedActivity> it = this.instancedActivities.iterator();it.hasNext();){
				InstancedActivity inst = it.next();
				if(inst.getInstancedToolIds()!=null){
					for(Iterator<String> it2 = inst.getInstancedToolIds().iterator();it2.hasNext();){
						if(it2.next().equals(toolInstId)){
							if(instActivities==null) instActivities = new HashMap<String, InstancedActivity>();
							instActivities.put(inst.getId(), inst);
						}
					}
				}
			}
		}		
		return instActivities;
	}


	public String getInstanceToolKind(String instanceId) {
		ToolInstance instance = this.getToolInstanceById(instanceId);
		if (instance!=null){
			if(instance.getResourceId()!=null){
				Resource res = this.getDesign().getResourceById(instance.getResourceId());
				if(res!=null){
					return res.getToolKind();
				}else return null;
			} else return null;
		}else return null;
		//If any of the elements is missing, we return null
	}



	public ToolInstance getToolInstanceById(String instanceId) {
		if(this.toolInstances!=null){
			for(Iterator<ToolInstance> it = toolInstances.iterator();it.hasNext();){
				ToolInstance inst = it.next();
				if(inst.getId().equals(instanceId)) return inst;
			}
			return null;
		}else return null;
		
	}



	public String getInstanceToolType(String instanceId) {
		ToolInstance instance = this.getToolInstanceById(instanceId);
		if (instance!=null){
			if(instance.getResourceId()!=null){
				Resource res = this.getDesign().getResourceById(instance.getResourceId());
				if(res!=null){
					return res.getToolType();
				}else return null;
			} else return null;
		}else return null;
		//If any of the elements is missing, we return null
	}



	public String[] getStaffUsernames() {
		
		ArrayList<String> staff=null;
		
		if (participants!=null){
			for(Iterator<Participant> it = participants.iterator();it.hasNext();){
				Participant p = it.next();
				
				if(p.isStaff()){
					if(staff==null) staff = new ArrayList<String>();
					
					//TODO If we don't find the username, we just use the provided ids. This is potentially buggy
					if(p.getLearningEnvironmentData()==null) staff.add(p.getId());
					else{
						// The format of the learningEnvironmentData is: userid;username;name;firstAccess
						String[] partFields = p.getLearningEnvironmentData().split(";");
						//TODO If we don't find the username, we just use the provided ids. This is potentially buggy
						if(partFields.length<2) staff.add(p.getId());
						else staff.add(partFields[1]);
					}
				
				}
				
			}
			if(staff==null) return null;//if there are no teachers, we return null
			else{
				String[] staffArray = (String[]) staff.toArray(new String[staff.size()]);
				return staffArray;
			}
			
		}else return null;
	}


	/*
	 * This is a convenience method to check that the deploy is completely defined 
	 * (sp. the instances) and thus, deployable in a VLE
	 * 
	 */
	public boolean isComplete(){
		
		if(this.staticDeployURL!=null || this.liveDeployURL!=null) return true;//If we have a deployURL, automatically we know its complete
		
		boolean complete = true;
	
		if(design.getResources()!=null){
			//First, we check that the resources and tools have been defined
			for(Iterator<Resource> it = design.getResources().iterator() ; it.hasNext(); ){
				Resource res = it.next();
				if(res.isInstantiable()){
					if(res.getToolKind()==null||res.getToolKind().length()==0||res.getToolKind().equals("unknown")||res.getToolType()==null||res.getToolType().length()==0||res.getToolType().equals("unknown")){//the resource is undefined... is it used?
						if(design.getActivitiesForResource(res.getId())!=null) return (complete=false);//if some activity uses the resource, it should be defined... the deploy is not complete!
						//TODO: should we eliminate the resource from the design?
					}
				}
			}
		}
		
		
		if(toolInstances!=null){
			//Second, we check that the external instances have been created and defined
			for(Iterator<ToolInstance> it = toolInstances.iterator(); it.hasNext(); ){
				ToolInstance instance = it.next();
				
				if((getInstanceToolKind(instance.getId())!=null && getInstanceToolKind(instance.getId()).equals("external")) && instance.getInternalReference()==null && (instance.getLocation()==null || instance.getLocation().toString().length()==0)){//the toolInstance is undefined... is it used?
					if(getInstancedActivitiesForToolInstance(instance.getId())!=null) return (complete=false);//if some instancedActivity uses the toolinstance, it should be defined... the deploy is not complete!
					//TODO: should we eliminate the toolInstance from the deploy?
	
				}
				
			}
		}
		//If none of the above happened, everything is defined, the design is complete
		//If it is complete but no zip has been generated, it will be generated/deployed when the deployed zip or link is GET
		return complete;
	}





	public ArrayList<Group> getGroupsForParticipant(String partId) {

		ArrayList<Group> groupList = null;
		
		//JUAN: Avoid a null pointer exception if deploy has not groups
		if (groups != null){
			for(Iterator<Group> it = groups.iterator(); it.hasNext(); ){
				Group g = it.next();
				
				//JUAN: modification to correct a bug (if group has not participants, there would be a null pointer exception
				if (g.getParticipantIds() != null) {
					if(g.getParticipantIds().contains(partId)){
						if(groupList==null) groupList = new ArrayList<Group>();
						groupList.add(g);
					}
				}
			}

		}
		
		return groupList;
	}



	public void setStaticDeployURL(URL staticDeployURL) {
		this.staticDeployURL = staticDeployURL;
	}



	public URL getStaticDeployURL() {
		return staticDeployURL;
	}



	public void setLiveDeployURL(URL liveDeployURL) {
		this.liveDeployURL = liveDeployURL;
	}



	public URL getLiveDeployURL() {
		return liveDeployURL;
	}

	/**
	 * Convenience method to get the numeric id (not the URL one)
	 */
	public String getShortId(){
		if(!this.id.startsWith("http://") || this.id.indexOf("/deploys/")==-1) return this.id;
		
		else return this.id.substring(this.id.indexOf("/deploys/")+9);
		
	}



	public String getGroupParticipantNamesAsString(Group g) {
		
		if(g.getParticipantIds()==null || g.getParticipantIds().size()==0) return "[grupo sin participantes]";
		String participants = "[ ";
		Iterator<String> it = g.getParticipantIds().iterator();
		participants += this.getParticipantById(it.next()).getName();
		for(;it.hasNext();){
			String partId = it.next();
			participants += " , "+this.getParticipantById(partId).getName();
		}
		participants += " ]";
		return participants;
	}



	//This returns the toolInstanceId, not the URL but just the 
	public String getTrimmedId(){
		if(!this.id.startsWith("http://") && this.id.indexOf("/deploys/")==-1){
			//This is not an expected URL!
			return this.id;
		}else{
			//This is an expected URL
			return this.id.substring(this.id.indexOf("/deploys/")+9);
		}
		
	}



	public HashMap<String, InstancedActivity> getInstancedActivitiesForActivity(
			String actId) {
		
		HashMap<String, InstancedActivity> instActivities = null;
		if(this.instancedActivities!=null){
			for(Iterator<InstancedActivity> it = this.instancedActivities.iterator();it.hasNext();){
				InstancedActivity inst = it.next();
				if(inst.getActivityId()!=null){
					if(inst.getActivityId().equals(actId)){
							if(instActivities==null) instActivities = new HashMap<String, InstancedActivity>();
							instActivities.put(inst.getId(), inst);
					}
				}
			}
		}		
		return instActivities;

	}

	
	//  Juan: Obtener Instanced  Activities de un username
//	public HashMap<String, InstancedActivity> getInstancedActivitiesForUsername(
	public ArrayList<InstancedActivity> getInstancedActivitiesForUsername(
			String username) {
		
		int numMatches = 0;
		String partId = null;
		
		if (participants!=null){
			for(Iterator<Participant> it = participants.iterator();it.hasNext();){
				Participant p = it.next();
				
				if (p.getName().equals(username)){
					numMatches++;
					partId = p.getId();
				}
			}
//			HashMap<String, InstancedActivity> instActivities = null;
			ArrayList<InstancedActivity> instActivities = null;
			ArrayList<Group> partGroups = null;
			if (numMatches == 1){
				partGroups = this.getGroupsForParticipant(partId);
				if (partGroups!=null){
					for (Iterator<Group> it2 = partGroups.iterator();it2.hasNext();){
						Group groupIt =it2.next();
						if(this.instancedActivities!=null){
							for(Iterator<InstancedActivity> it3 = this.instancedActivities.iterator();it3.hasNext();){
								InstancedActivity inst = it3.next();
								if (inst.getGroupId()!=null){
									if(inst.getGroupId().equals(groupIt.getId())){
//											if(instActivities==null) instActivities = new HashMap<String, InstancedActivity>();
//											instActivities.put(inst.getId(), inst);
										if(instActivities==null) instActivities = new ArrayList<InstancedActivity>();
										instActivities.add(inst);
									}
								}
							}
						} else {
							return null;
						}

						
					}
					return instActivities;
				} else {
					return null;
				} 
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	//Juan: Hasta aqu� mi modificaci�n
	
	/**
	 * Get the instanced activities for a user that contain tool instances with a specific position type
	 * @param username the name of the user of the instanced activities
	 * @param posTypes the possible values for the position type 
	 * @return the list of instanced activities
	 */
	public ArrayList<InstancedActivity> getInstancedActivitiesForUsernamePositionType(String username, ArrayList<String> posTypes) {
		List<InstancedActivity> listIA = getInstancedActivitiesForUsername(username);
		ArrayList<InstancedActivity> iaPosType = new ArrayList<InstancedActivity>();
		if (listIA !=null){
			for (InstancedActivity ia: listIA){
				List<String> instToolId = ia.getInstancedToolIds();
				if (instToolId !=null)
				{
					for(String instanceId: instToolId){
						ToolInstance ti = getToolInstanceById(instanceId);
						if (ti.hasPositionType(posTypes)){
							iaPosType.add(ia);
						}
					}
				}
			}
		}
		return iaPosType;
	}
	
	

	//Convenience method to clear all internal tool instance locations (set to null)
	public void clearInternalToolInstanceLocations() {
		if(this.toolInstances==null || this.toolInstances.size()==0) return;
		else{
			for(ToolInstance instance : toolInstances){
				//we clear them... if they are internal, and NOT a redirection!
				if(this.getDesign().findResourceById(instance.getResourceId()).getToolKind().equals(Resource.TOOL_KIND_INTERNAL) 
						&& instance.getLocation()!=null 
						&& instance.getLocationWithRedirects(this)!=null 
						&& instance.getLocation().toString().equals(instance.getLocationWithRedirects(this).toString())) 
					instance.setLocation(null);
			}
		}
	}



	//Convenience method to clear all instanced activity locations (set to null)
	public void clearInstancedActivityLocations() {
		if(this.instancedActivities==null || this.instancedActivities.size()==0) return;
		else{
			for(InstancedActivity instance : instancedActivities){
				if(instance.getLocation()!=null) instance.setLocation(null);
			}
		}
	}



	//Convenience method to clear all activity locations (set to null), including recursively the children
	public void clearActivityLocations(Activity act) {
		
		if(act!=null){
			if(act.getLocation()!=null) act.setLocation(null);
			if(act.getChildrenActivities()!=null && act.getChildrenActivities().size()>0){
				for(Activity child : act.getChildrenActivities()){
					clearActivityLocations(child);
				}
			}
		}
		
	}



	public ToolInstance getToolInstanceByTrimmedId(String trimmedInstanceId) {
		if(this.toolInstances!=null){
			for(Iterator<ToolInstance> it = toolInstances.iterator();it.hasNext();){
				ToolInstance inst = it.next();
				if(inst.getTrimmedId().equals(trimmedInstanceId)) return inst;
			}
			return null;
		}else return null;
	}



	public boolean isValid() {
		
		//check that the locations of all (non-instantiable) resources are not null and are valid URLs
		ArrayList<Resource> resources = this.getDesign().getResources();
		if(resources!=null){
			for(Iterator<Resource> it = resources.iterator();it.hasNext();){
				Resource res = it.next();
				if(!res.isInstantiable()){
					if (res.getLocation()==null) return false;
					//We do not check that resources are URLs... they can be internal files!
//					else{
//						try {
//							URL url = new URL(res.getLocation());
//						} catch (MalformedURLException e) {
//							return false;
//						}
//						
//					}
				}
			}
		}
		//check that the references to the resources and toolInstances all exist
		ArrayList<InstancedActivity> instActs = this.getInstancedActivities();
		if(instActs!=null){
			for(Iterator<InstancedActivity> it = instActs.iterator();it.hasNext();){
				InstancedActivity act = it.next();
				//check the resources
				ArrayList<String> actResources = act.getResourceIds();
				if(actResources!=null){
					for(Iterator<String> it2 = actResources.iterator();it2.hasNext();){
						String resId = it2.next();
						if(this.getDesign().findResourceById(resId)==null) return false;
					}
					
				}
				//check the toolInstances
				ArrayList<String> instances = act.getInstancedToolIds();
				if(instances!=null){
					for(Iterator<String> it2 = instances.iterator();it2.hasNext();){
						String instId = it2.next();
						ToolInstance instance = null;
						if((instance=this.getToolInstanceById(instId))==null) return false;
						//check that the locations of the toolInstances all exist (if internal), and are not circular
						if(instance.getLocation()!=null){
							if(instance.getLocation().toString().indexOf("/GLUEPSManager/")!=-1){//it is an internal reference
								if(this.getToolInstanceById(instance.getLocation().toString())==null) return false; //the reference should exist
								if(instance.hasCircularReferences(this, new ArrayList<String>())) return false;
							}
							
						}else{//if the location is null, we check the internal references (de-urlified document)
							if(instance.getInternalReference()!=null){
								if(this.getToolInstanceById(instance.getInternalReference())==null) return false;//the referenced tool instance should exist
								if(instance.hasCircularReferences(this, new ArrayList<String>())) return false;
							}
						}
						
						
					}
					
					
				}
			}
		}
		
		
		
		
		
		return true;
	}



	public void setCourse(Course course) {
		this.course = course;
	}



	public Course getCourse() {
		return course;
	}


}
