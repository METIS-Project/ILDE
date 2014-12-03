package glueps.adaptors.ld.edit2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import glueps.adaptors.ld.ILDAdaptor;
import glueps.adaptors.ld.LDAdaptor;
import glueps.core.model.Activity;
import glueps.core.model.Group;
import glueps.core.model.InstancedActivity;
import glueps.core.model.Participant;
import glueps.core.model.Resource;
import glueps.core.model.Role;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.ToolInstance;

public class EDIT2Adaptor implements ILDAdaptor, LDAdaptor {
	
	private String designId = null;
	private ArrayList<Activity> activities = new ArrayList<Activity>();
	private ArrayList<Group> groups = new ArrayList<Group>();
	private ArrayList<Participant> participants = new ArrayList<Participant>();
	private ArrayList<Resource> resources = new ArrayList<Resource>(); 
	private ArrayList<Role> roles = new ArrayList<Role>();
	private ArrayList<InstancedActivity> instancedActivities = new ArrayList<InstancedActivity>();
	private ArrayList<ToolInstance> toolInstances = new ArrayList<ToolInstance>();
	
	public EDIT2Adaptor(String designId) {
		this.designId = designId;
	}

	@Override
	public Design fromLDToLF(String filepath) {
		
		String t2Content = readFile(filepath);
		
		Design design = convertEDIT2toLFDesign(t2Content);
		return design;
	}

	private String readFile(String filepath) {
		String t2Content = null;
		File t2File = new File(filepath);
		
		if(!t2File.exists()) return null;
		
		//TODO: we might want to encode in UTF-8, but EDIT2 seems to support only ANSI
		try {
			t2Content = FileUtils.readFileToString(t2File, "UTF-8");
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		
		if(t2Content == null) return null;
		return t2Content;
	}

	private Design convertEDIT2toLFDesign(String t2Content) {
		
		Design design = new Design(this.designId,"Design from ediT2",null,"ediT2","plsobreira",new Date(),null,null,null,null,null);
		//Design(String id,String name,String description,String originalDesignType,String author,Date timestamp,
		//       ArrayList<String> objectives,Activity rootActivity,String originalDesignData,ArrayList<Role> roles,ArrayList<Resource> resources)
		
		
		//Creating the Root Activity (the "Whole-Design" Activity)
		Activity rootActivity = new Activity("0","Root Activity","The whole-design Activity","class",null,null,null,null);
		//Activity(String id,String name,String description,String mode,
		//         ArrayList<Activity> childrenActivities,String parentActivityId,ArrayList<String> resourceIds,ArrayList<String> roleIds)
		this.activities.add(rootActivity);
		
		this.createNotionsObjects(t2Content);               //Activity,Participant,Resource and Role objects
		
		//Updating rootActivity (childrenActivities,resourceIds,roleIds)
		int i;
		ArrayList<Activity> childrenActivities = new ArrayList<Activity>();
		ArrayList<String> resourceIds = new ArrayList<String>();
		ArrayList<String> roleIds = new ArrayList<String>();
		for (i = 1; i < this.activities.size(); i++) {
			childrenActivities.add(this.activities.get(i));
		}
		for (i = 0; i < this.resources.size(); i++) {
			resourceIds.add(this.resources.get(i).getId());
		}
		for (i = 0; i < this.roles.size(); i++) {
			roleIds.add(this.roles.get(i).getId());			
		}
		rootActivity.setChildrenActivities(childrenActivities);
		rootActivity.setResourceIds(resourceIds);
		rootActivity.setRoleIds(roleIds);
		
		//Updating design
		design.setRootActivity(rootActivity);
		design.setRoles(this.roles);
		design.setResources(this.resources);		
	
		
		return design;		
	}
		
	private Deploy convertEDIT2toLFDeploy(String t2Content) {
		
		Design design = convertEDIT2toLFDesign(t2Content);
		
		
		Deploy deploy = new Deploy(null,design,null,null,"plsobreira",new Date(),null,null,null,null,null);
		//Deploy(String id,Design design,String name,LearningEnvironment learningEnvironment,String author,Date timestamp,
		//       String deployData,ArrayList<InstancedActivity> instancedActivities,ArrayList<ToolInstance> toolInstances,ArrayList<Participant> participants, ArrayList<Group> groups)
		
		//We reset the participants and resources array, so that we do not get repeated ones from the previous call to createNotionsObjects
		this.participants = new ArrayList<Participant>();
		this.resources = new ArrayList<Resource>(); 
		this.createNotionsObjects(t2Content);               //Activity,Participant,Resource and Role objects
		this.createGroupsAndInstancedActivities(t2Content); //Group and InstancedActivity objects
		

		//Updating deploy
		deploy.setInstancedActivities(instancedActivities);
		deploy.setParticipants(this.participants);
		deploy.setGroups(this.groups);
		deploy.setToolInstances(this.toolInstances);
		
		return deploy;		
	}
	
	private void createNotionsObjects(String script) {
		Activity activity;
		Participant participant;
		Resource resource;
		Role role;
		int notionCount;
		int colonCount;
		String notion,componentList;
		List<String> notions = this.getNotions(script);
		String leData = "";
		String staffStr = "";
		boolean staff = false;
		
		for (int i = 0; i < notions.size(); i++) {
			notionCount = 0;
			String name = "";
			String information = "";
			notion = notions.get(i);
			
			if (!notion.equals("Group")) {
				if (notion.equals("Activity")) {
					componentList = "notion1ObjectsList";
				} else if (notion.equals("Participant")) {
					componentList = "gluepsParticipantsList";
				} else if (notion.equals("Resource")) {
					componentList = "notion4ObjectsList";
				} else { //notion.equals("Role")
					componentList = "notion5ObjectsList";
				}
				
				int j = script.indexOf(componentList) + componentList.length() + 6;
				char ch = script.charAt(j);
				Boolean endLoop = false;
				
				while (!endLoop) {
					

					
					//Finding the object's 'name'
					while (ch != '\"') {
						name += ch;
						ch = script.charAt(++j);
					}
					
					//Finding the object's 'information' - apparently we ignore it??
					if (notion.equals("Activity") || notion.equals("Resource") || notion.equals("Role")) {
						j = j + 3;
					} else { //notion.equals("Participant") 
								
						leData = "";
						staff = false;
						staffStr = "";						
						colonCount = 0;
						
						while (colonCount <= 3) {
							ch = script.charAt(++j);
							if (ch == ',') {
								++colonCount;
							}
							if (ch == '\"'){
								//we have a new string to grab - depending on the colonCount, we assign it to a variable
								if(colonCount==1){//it is the numeric id - in principle we ignore it
									ch = script.charAt(++j);
									while (ch != '\"') ch = script.charAt(++j);
								}else if(colonCount==2){//it is the leData - we store it
									ch = script.charAt(++j);
									while (ch != '\"') {
										leData += ch;
										ch = script.charAt(++j);
									}
								}else if(colonCount==3){//it is the staff - we store it and set the variable
									ch = script.charAt(++j);
									while (ch != '\"') {
										staffStr += ch;
										ch = script.charAt(++j);
									}
									staff = Boolean.parseBoolean(staffStr);
									break;
								}
								
							}
						}
						//j = j + 2;
					}					
					ch = script.charAt(j);
					while (ch != '\"') {
						information += ch;
						ch = script.charAt(++j);
					}
					
					//Registering the object
					if (notion.equals("Activity")) {
						activity = new Activity(String.valueOf(notionCount + 1),name,information,"group",null,"0",null,null);
						//Activity(String id,String name,String description,String mode,
						//         ArrayList<Activity> childrenActivities,String parentActivityId,ArrayList<String> resourceIds,ArrayList<String> roleIds)
						this.activities.add(activity);
						++notionCount;
					} else if (notion.equals("Participant")) {
						participant = new Participant(String.valueOf(notionCount),name,null,leData,staff);
						//Participant(String id,String name,String deployId,String learningEnvironmentData,boolean isStaff)
			
						this.participants.add(participant);
						++notionCount;
					} else if (notion.equals("Resource")) {
						Boolean instantiable;
						if (information.indexOf("http://") > -1) {
							instantiable = false;
						} else {
							instantiable = true;
							information = null;
						}						
						resource = new Resource(String.valueOf(notionCount),name,instantiable,information,"external",null,null);
						//Resource(String id,String name,boolean instantiable,String location,String toolKind,String toolType,String toolData)
						this.resources.add(resource);
						++notionCount;
					} else { //notion.equals("Role")
						role = new Role(String.valueOf(notionCount),name,information,false);
						//Role(String id, String name, String description, boolean isTeacher)
						this.roles.add(role);
						++notionCount;
					}
					
					name = "";
					information = "";
					
					//Verifying if there are more objects to parser in each notionObjectsList
					if (notion.equals("Activity") || notion.equals("Role")) {
						ch = script.charAt(j+2);
						if (ch == ']') {
							endLoop = true;
						} else {
							j = j + 5;
							ch = script.charAt(j);
						}	
					} else { // notion.equals("Participant") || notion.equals("Resource")
						while (ch != ')') {
							ch = script.charAt(++j);
						}
						ch = script.charAt(j+1);
						if (ch == ']') {
							endLoop = true;
						} else {
							j = j + 4;
							ch = script.charAt(j);
						}
					}
				}
			}
		}
		this.updateActivities(script); //Updating 'resourceIds' and 'roleIds' in each activity
	}
	
	private List<String> getNotions(String script) {
		int i = 2;
		char ch = script.charAt(i);
		String notion = "";
		List<String> notions = new ArrayList<String>();
		
		while (ch != '\n') {
			while (ch != '\"') {
				notion += ch;
				ch = script.charAt(++i);
			}
			notions.add(notion);
			notion = "";
			i = i + 3;
			ch = script.charAt(i);
		}		
		return notions;		
	}
	
	private void updateActivities(String script) {
		String[] scriptComponents = this.getInitialScriptComponents(script);
		
		int i,j,k;
		Boolean stopFor;
		ArrayList<String> resourceIds;
		ArrayList<String> resourceNames;

		for (i = 0; i < scriptComponents.length; i++) {
			resourceIds = new ArrayList<String>();
			resourceNames = this.getComponentNames("Resource",scriptComponents[i]);

			for (j = 0; j < resourceNames.size(); j++) {
				stopFor = false;
				for (k = 0; !stopFor && (k < this.resources.size()); k++) {
					if (this.resources.get(k).getName().equals(resourceNames.get(j))) {
						stopFor = true;
						resourceIds.add(this.resources.get(k).getId());
					}
				}
			}
			//Updating an Activity (/resourceIds)
			if (resourceIds.size() > 0) {
				this.activities.get(i + 1).setResourceIds(resourceIds);				
			}
		}			

		if (this.getNotions(script).contains("Role")) {
			ArrayList<String> roleIds;
			ArrayList<String> roleNames;
			
			for (i = 0; i < scriptComponents.length; i++) {
				roleIds = new ArrayList<String>();
				roleNames = this.getComponentNames("Role",scriptComponents[i]);
				
				for (j = 0; j < roleNames.size(); j++) {
					stopFor = false;
					for (k = 0; !stopFor && (k < this.roles.size()); k++) {
						if (this.roles.get(k).getName().equals(roleNames.get(j))) {
							stopFor = true;
							roleIds.add(this.roles.get(k).getId());
						}
					}
				}
				//Updating an Activity (/roleIds)
				if (roleIds.size() > 0) {
					this.activities.get(i + 1).setRoleIds(roleIds);					
				}
			}
		}
	}
	
	private String[] getInitialScriptComponents(String script) {
		int initialIndex = StringUtils.indexOf(script,"\tNode (\"Activity\"");
		int finalIndex   = StringUtils.indexOf(script,"notion1ObjectsList") - 8;
		
		String[] instancedActivities = StringUtils.splitByWholeSeparator(StringUtils.substring(script,initialIndex,finalIndex),",\r\n\tNode (\"Activity\"");
		String[] scriptComponents    = new String[instancedActivities.length];
		
		for (int i = 0; i < instancedActivities.length; i++) {
			if (i == 0) {
				scriptComponents[i] = instancedActivities[i];	
			} else {
				scriptComponents[i] = "\tNode (\"Activity\"" + instancedActivities[i];	
			}            
        }
		return scriptComponents;
	}	
	
	private ArrayList<String> getComponentNames (String notion,String scriptComponent) {		
		ArrayList<String> componentNames = new ArrayList<String>();
		
		int i;
		int index = 0;
		Boolean endWhile;
		String componentName;
		String sequence = "Node (\"" + notion + "\"";
		
		for (i = 0; i < StringUtils.countMatches(scriptComponent,sequence); i++) {
			endWhile = false;
			index = StringUtils.indexOf(scriptComponent,sequence,index);
			char ch = scriptComponent.charAt(index);
			while (ch != '[') {
				ch = scriptComponent.charAt(++index);
			}
			ch = scriptComponent.charAt(++index);
			if (ch != ']') {
				ch = scriptComponent.charAt(++index);
				while (!endWhile) {
					componentName = "";
					while (ch != '\"') {
						componentName += ch;
						ch = scriptComponent.charAt(++index);
					}
					if (!componentNames.contains(componentName)) {
						componentNames.add(componentName);
					}
					ch = scriptComponent.charAt(++index);
					if (ch == ',') {
						index = index + 2;
						ch = scriptComponent.charAt(index);
					} else { //ch == ']'
						endWhile = true;
					}
				}
			}
		}
		return componentNames;
	}
	
	private void createGroupsAndInstancedActivities(String script) {
		int i,j,k,l,m,z;
		Group group;
		String groupName = "";
		String instancedGroupName;
		int instancedActivityGroupId = 0;
		InstancedActivity instancedActivity;
		Boolean stopFor;
		ArrayList<String> participantIds;
		ArrayList<String> participantNames;
		ArrayList<String> resourceIds;
		ArrayList<String> resourceNames;
		ArrayList<String> toolInstanceIds;
		String[] initialScriptComponents = this.getInitialScriptComponents(script);
		
		z=0;//counter for the toolInstanceIds
		
		for (i = 0; i < initialScriptComponents.length; i++) {
			String[] instancedGroups = this.getInstancedGroupsParticipants("Group",initialScriptComponents[i]);
			for (j = 0; j < instancedGroups.length; j++) {
				if (this.getComponentNames("Group",instancedGroups[j]).size() > 0) {
					groupName = this.getComponentNames("Group",instancedGroups[j]).get(0);
				} else {
					groupName = "";
				}
				String[] instancedParticipants = this.getInstancedGroupsParticipants("Participant",instancedGroups[j]);
				for (k = 0; k < instancedParticipants.length; k++) {
					if (instancedParticipants.length == 1) {
						instancedGroupName = groupName;
					} else {
						instancedGroupName = groupName + "_" + (k+1);
					}
					
					group = new Group(String.valueOf(instancedActivityGroupId),instancedGroupName,null,null);
					//Group(String id,String name,String deployId,ArrayList<String> participantIds)
					
					participantNames = this.getComponentNames("Participant",instancedParticipants[k]);
					participantIds = new ArrayList<String>();
					for (l = 0; l < participantNames.size(); l++) {
						stopFor = false;
						for (m = 0; !stopFor && (m < this.participants.size()); m++) {
							if (this.participants.get(m).getName().equals(participantNames.get(l))) {
								stopFor = true;
								participantIds.add(this.participants.get(m).getId());
							}
						}
					}
					if (participantIds.size() > 0) {
						group.setParticipantIds(participantIds);
					}
					this.groups.add(group);
					
					instancedActivity = new InstancedActivity(String.valueOf(instancedActivityGroupId),null,String.valueOf(i+1),String.valueOf(instancedActivityGroupId),null,null);
					//InstancedActivity(String id,String deployId,String activityId,String groupId,ArrayList<String> resourceIds,ArrayList<String> instances)
					resourceNames = this.getComponentNames("Resource",instancedParticipants[k]);
					resourceIds = new ArrayList<String>();
					toolInstanceIds = new ArrayList<String>();
					for (l = 0; l < resourceNames.size(); l++) {
						stopFor = false;
						for (m = 0; !stopFor && (m < this.resources.size()); m++) {
							if (this.resources.get(m).getName().equals(resourceNames.get(l))) {
								stopFor = true;
								
								//TODO: bug! only should add it to the resource Ids if the resource is NON-instantiable. If it is instantiable, a toolInstance should be created and 
								//Quick fix: only add the resource if it is non-instantiable (if instantiable, we ignore it)
								if(!this.resources.get(m).isInstantiable()){
									resourceIds.add(this.resources.get(m).getId());
								}else{//The resource is instantiable, we create a new toolInstance, associate it to this resource and instancedActivity, and add it to the toolInstances array
									ToolInstance ti = new ToolInstance(String.valueOf(z), this.resources.get(m).getName()+" ("+group.getName()+")", null, this.resources.get(m).getId(), null); 
									toolInstanceIds.add(String.valueOf(z));
									z++;
									this.toolInstances.add(ti);
								}
							}
						}
					}
					if (resourceIds.size() > 0) {
						instancedActivity.setResourceIds(resourceIds);
					}
					if (toolInstanceIds.size() > 0) {
						instancedActivity.setInstancedToolIds(toolInstanceIds);
					}
					this.instancedActivities.add(instancedActivity);
					
					++instancedActivityGroupId;
				}
			}
		}
	}
	
	private String[] getInstancedGroupsParticipants(String notion,String scriptComponent) {
		String separator1,separator2; 
		int initialIndex = StringUtils.indexOf(scriptComponent,"[\r\n");
		initialIndex = initialIndex + 3;
		int finalIndex = StringUtils.indexOf(scriptComponent,scriptComponent.length() - 1);
		String scriptComponentWithoutFatherNotion = StringUtils.substring(scriptComponent,initialIndex,finalIndex);
		if (notion == "Group") {
			separator1 = ",\r\n\t\tNode (\"Group\"";
			separator2 = "\t\tNode (\"Group\"";
		} else { // notion == "Participant"
			separator1 = ",\r\n\t\t\tNode (\"Participant\"";
			separator2 = "\t\t\tNode (\"Participant\"";			
		}
		String[] initialInstancedGroupsParticipants = StringUtils.splitByWholeSeparator(scriptComponentWithoutFatherNotion,separator1);
		String[] finalInstancedGroupsParticipants = new String[initialInstancedGroupsParticipants.length];
		
		for(int i = 0; i < initialInstancedGroupsParticipants.length; i++) {
			if (i == 0) {
				finalInstancedGroupsParticipants[i] = initialInstancedGroupsParticipants[i];	
			} else {
				finalInstancedGroupsParticipants[i] = separator2 + initialInstancedGroupsParticipants[i];	
			}            
        }		
		return finalInstancedGroupsParticipants;		
	}
	
	@Override
	public Deploy processInstantiation(String filepath, Design design, HashMap<String, Group> vleGroups,
			HashMap<String, Participant> vleUsers) {
		String t2Content = readFile(filepath);
		
		Deploy deploy = convertEDIT2toLFDeploy(t2Content);
		return deploy;
	}

	@Override
	public ILDAdaptor getLDAdaptor(Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}
	
}