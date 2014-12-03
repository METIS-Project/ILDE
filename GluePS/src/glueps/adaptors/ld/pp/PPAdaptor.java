package glueps.adaptors.ld.pp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;

import glueps.adaptors.ld.ILDAdaptor;
import glueps.adaptors.ld.LDAdaptor;
import glueps.adaptors.ld.pp.model.Pedplan;
import glueps.adaptors.ld.pp.model.PlanActivity;
import glueps.adaptors.ld.pp.model.PlanActivityAttribute;
import glueps.adaptors.ld.pp.model.PlanActivityField;
import glueps.adaptors.ld.pp.model.PlanActivityResource;
import glueps.adaptors.ld.pp.model.PlanBlock;
import glueps.adaptors.ld.pp.model.PlanTag;
import glueps.adaptors.ld.pp.model.PlanTagAttribute;
import glueps.adaptors.ld.ppc.model.PatternType;
import glueps.core.model.Activity;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.Group;
import glueps.core.model.InstancedActivity;
import glueps.core.model.Participant;
import glueps.core.model.Resource;
import glueps.core.model.Role;

public class PPAdaptor implements ILDAdaptor, LDAdaptor{

	private String designId = null;
	private static final String PP_XML_FILENAME = "pp.xml";
	private static final String DESIGN_TYPE = "PP";
	
	public PPAdaptor(String designId){
		this.designId = designId;
	}
	
	@Override
	public Design fromLDToLF(String filepath) {
		String ppXmlDesign = null;
		Pedplan pedplanner = null;
		try {
			//Uncompress the zip file and search for the pp.xml file containing the pedagogical plan
			FileInputStream fis = new FileInputStream(filepath);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry;
			while ( (entry = zis.getNextEntry())!=null && ppXmlDesign==null){
				if (!entry.isDirectory()){
					if (entry.getName().equalsIgnoreCase(PP_XML_FILENAME)){
						byte[] xmlBinding = unzipFile(zis, entry);
						ppXmlDesign = new String(xmlBinding, "utf-8");
						ppXmlDesign = StringEscapeUtils.unescapeHtml4(ppXmlDesign);
						ppXmlDesign = ppXmlDesign.replace("&nbsp;", " ");
					}
				}
				
			}
			zis.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			JAXBContext jc = JAXBContext.newInstance(glueps.adaptors.ld.pp.model.Pedplan.class);
			Unmarshaller u = jc.createUnmarshaller();
			//Unmarshal the xml file to get the Pedplan java object instead of working directly with the xml tags
			pedplanner = (Pedplan)u.unmarshal(new StringReader(ppXmlDesign));
		} catch (JAXBException e) {
			return null;
		}
		if(pedplanner==null) return null;
		Design design = convertPPtoLF(pedplanner);
		
		File file = new File(filepath);
		file.setWritable(true);
		file.delete();
		
		
		return design;
	}
	
	private static byte[] unzipFile(ZipInputStream zis, ZipEntry entry) throws IOException{
		final int BUFFER = 2048;
		int count;
		byte[] data = new byte[BUFFER];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		while ( (count = zis.read(data, 0, BUFFER))!=-1){
			out.write(data, 0, count);
		}
		out.close();
		return out.toByteArray();
	}
	
	private Design convertPPtoLF(Pedplan pedplanner) {
		String name = pedplanner.getPlanTitle();
		String description = pedplanner.getPlanDescription();
		String author = pedplanner.getPlanAuthor();	
		ArrayList<String> objectives = getObjectives(pedplanner);	
		ArrayList<Activity> childrenActivities = getChildrenActivities(pedplanner);
		Activity generalInfAct = getGeneralInformationAsAnActivity(pedplanner);
		ArrayList<Resource> resources = getResources(pedplanner);
		ArrayList<Role> roles = getRoles();
		assignActivityResourceIds(pedplanner, childrenActivities, resources);
		assignActivityRoleIds(childrenActivities, roles);
		
		//We construct the activity tree, adding resources when needed
		Activity rootActivity = new Activity("0", "Root Activity", "The whole design activity", Activity.CLASS, null, null, null, null);
		rootActivity.setChildrenSequenceMode(Activity.SEQUENCE);
		childrenActivities.add(0, generalInfAct);//The general information is added as an activity
		rootActivity.setChildrenActivities(childrenActivities);
		
		Design design = new Design(this.designId, name, description, DESIGN_TYPE, author, new Date(), objectives, rootActivity, null, roles, resources);
		
		return design;
	}
	
	/**
	 * Get the list of objectives from the pedagogical planner
	 * @param pedplanner The pedagogical planner from which the objectives are obtained
	 * @return the objectives of the design from the pedagocial planner
	 */
	private ArrayList<String> getObjectives(Pedplan pedplanner){
		ArrayList<String> objectives = null;	
		List<PlanTag> planTags = pedplanner.getPlanTags().getPlanTag();
		for (int i = 0; i < planTags.size(); i++){
			//the order value of the planTag which contains the properties is 5
			if (planTags.get(i).getOrder().intValue()==5){
				List<PlanTagAttribute> ptAttributes = planTags.get(i).getPlanTagAttributes().getPlanTagAttribute();
				for (int j = 0; j < ptAttributes.size(); j++){
					if (ptAttributes.get(j).getOrder().equals("L1")){
						if (objectives == null){
							objectives = new ArrayList<String>();
						}
						objectives.add(ptAttributes.get(j).getPlanTagAttributeValues().getPlanTagAttributeValue());
						return objectives;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Get the resources that the pedagogical planner design contains
	 * @param pedplanner The pedagogical planner object containing the xml info as a Java object
	 * @return The list of resources in the design
	 */
	private ArrayList<Resource> getResources(Pedplan pedplanner){
		int resourceCount = 1;
		ArrayList<Resource> resources = new ArrayList<Resource>();
		List<PlanBlock> planBlocks = pedplanner.getPlanBlocks().getPlanBlock();
		for (int i = 0; i < planBlocks.size(); i++){
			List<PlanActivity> planActivities = planBlocks.get(i).getPlanActivities().getPlanActivity();
			for (int j = 0; j < planActivities.size(); j++){
				List<PlanActivityAttribute> attributes = planActivities.get(j).getPlanActivityAttributes().getPlanActivityAttribute();
				for (int k = 0; k < attributes.size(); k++){
					if (attributes.get(k).getPlanActivityAttributeName().getId().equals("L3")){//Get the resources
						List<PlanActivityField> fields = attributes.get(k).getPlanActivityFields().getPlanActivityField();
						for (int l = 0; l < fields.size(); l++){
							if (fields.get(l).getFid().intValue()==1 && fields.get(l).getType().equals("resources")){
								if (fields.get(l).getPlanActivityResources()!=null){
									List<PlanActivityResource> actResources = fields.get(l).getPlanActivityResources().getPlanActivityResource();
									for (int m = 0; m < actResources.size(); m++){
										String resourceLink = actResources.get(m).getPlanActivityResourceLink();
										try {
											URL url = new URL(resourceLink);
											String resourceText = actResources.get(m).getPlanActivityResourceText();
											Resource resource = new Resource("Res" + resourceCount,resourceText,false,resourceLink,"external",null,null);
											resources.add(resource);
											resourceCount++;
										} catch (MalformedURLException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return resources;
	}
	
	
	private void assignActivityResourceIds(Pedplan pedplanner, ArrayList<Activity> activities, ArrayList<Resource> resources){
		int activityCount = 0;
		int resourceCount = 0;
		List<PlanBlock> planBlocks = pedplanner.getPlanBlocks().getPlanBlock();
		for (int i = 0; i < planBlocks.size(); i++){
			List<PlanActivity> planActivities = planBlocks.get(i).getPlanActivities().getPlanActivity();
			for (int j = 0; j < planActivities.size(); j++){
				List<PlanActivityAttribute> attributes = planActivities.get(j).getPlanActivityAttributes().getPlanActivityAttribute();
				Activity activity = activities.get(activityCount++);
				for (int k = 0; k < attributes.size(); k++){
					if (attributes.get(k).getPlanActivityAttributeName().getId().equals("L3")){
						List<PlanActivityField> fields = attributes.get(k).getPlanActivityFields().getPlanActivityField();
						for (int l = 0; l < fields.size(); l++){
							if (fields.get(l).getFid().intValue()==1 && fields.get(l).getType().equals("resources")){
								if (fields.get(l).getPlanActivityResources()!=null){
									List<PlanActivityResource> actResources = fields.get(l).getPlanActivityResources().getPlanActivityResource();
									for (int m = 0; m < actResources.size(); m++){
										Resource resource = resources.get(resourceCount++);
										if (activity.getResourceIds() == null){
											activity.setResourceIds(new ArrayList<String>());
										}
										activity.getResourceIds().add(resource.getId());
									}
								}
							}
						}
					}
				}
				
			}			
		}
	}
	
	private void assignActivityRoleIds(ArrayList<Activity> activities, ArrayList<Role> roles){
		for (int i = 0; i < activities.size(); i++){
			for (int j = 0; j < roles.size(); j++){
				if (activities.get(i).getRoleIds() == null){
					activities.get(i).setRoleIds(new ArrayList<String>());
				}
				activities.get(i).getRoleIds().add(roles.get(j).getId());
			}
		}

	}
	
	/**
	 * Get the activities that are children of the root activity
	 * @param pedplanner The pedagogical planner object containing the xml info as a Java object
	 * @return The list of activities that are children of the root activity
	 */
	private ArrayList<Activity> getChildrenActivities(Pedplan pedplanner){
		ArrayList<Activity> activities = new ArrayList<Activity>();
		List<PlanBlock> planBlocks = pedplanner.getPlanBlocks().getPlanBlock();
		for (int i = 0; i < planBlocks.size(); i++){
			List<PlanActivity> planActivities = planBlocks.get(i).getPlanActivities().getPlanActivity();
			for (int j = 0; j < planActivities.size(); j++){
				String name = planActivities.get(j).getPlanActivityTitle();
				name = new HtmlToPlainText().getPlainText(Jsoup.parse(name));
				String description = "";
				List<PlanActivityAttribute> attributes = planActivities.get(j).getPlanActivityAttributes().getPlanActivityAttribute();
				for (int k = 0; k < attributes.size(); k++){
					if (attributes.get(k).getPlanActivityAttributeName().getId().equals("L1") || attributes.get(k).getPlanActivityAttributeName().getId().equals("L2")
							|| attributes.get(k).getPlanActivityAttributeName().getId().equals("L4") || attributes.get(k).getPlanActivityAttributeName().getId().equals("L5")){
						//Get the objectives, orchestration, sensorimotor learning and evaluation criteria of the activity
						description += "<h3>" + attributes.get(k).getPlanActivityAttributeName().getContent() + "</h3>";
						List<PlanActivityField> fields = attributes.get(k).getPlanActivityFields().getPlanActivityField();
						for (int l = 0; l < fields.size(); l++){
							String fieldName = fields.get(l).getPlanActivityFieldName();
							String fieldValue = fields.get(l).getPlanActivityFieldValue().getContent();
							description += "<h4>" + fieldName + "</h4>" + fieldValue;
						}
					}
				}
				Activity activity = new Activity("Act" + (activities.size()+1),name,description,Activity.GROUP_SEPARATE,null,"0",null,null);
				activities.add(activity);
			}			
		}
		return activities;
	}
	
	/**
	 * Get the general information about the plan ("Population", "Context" and "Content Domain" sections in the PP)
	 * @param pedplanner The pedagogical planner object containing the xml info as a Java object
	 * @return A created activity to store the general information about the plan
	 */
	private Activity getGeneralInformationAsAnActivity(Pedplan pedplanner){
		List<PlanTag> planTags = pedplanner.getPlanTags().getPlanTag();
		String description = "";
		description += "<h2>" + pedplanner.getPlanTitle() + "</h2>";
		description += "<h3>Description</h3>" + pedplanner.getPlanDescription();
		for (int i = 0; i < planTags.size(); i++){
			PlanTag pt = planTags.get(i);
			int order = pt.getOrder().intValue();
			if (order >=1 && order <=3){//population, context or content domain
				description += "<h3>" + pt.getPlanTagValue() + "</h3>";
				List<PlanTagAttribute> attributes = pt.getPlanTagAttributes().getPlanTagAttribute();
				for (int j = 0; j < attributes.size(); j++){
					PlanTagAttribute attr = attributes.get(j);
					description += "<h4>" + attr.getPlanTagAttributeTitle() + "</h4>";
					description += attr.getPlanTagAttributeValues().getPlanTagAttributeValue();
					String attrCode = attr.getPlanTagAttributeValues().getPlanTagAttributeCode();
					if (attrCode!=null){
						attrCode = attrCode.replace("<script async ", "<script async=\"async\" "); //Ugly way of avoiding the parsing error because of the attribute async with no value
						description += attrCode;
					}
				}
			}			
		}
		Activity activity = new Activity("ActGenInf", "General information about the plan", description, Activity.GROUP_SEPARATE,null,"0",null,null);
		return activity;
	}
	
	private ArrayList<Role> getRoles(){
		Role classRole = new Role("Role1","PP-Class","A class role created for the Pedagocial Planner" , false);
		Role teacherRole = new Role("Role2","PP-Teacher","A teacher role created for the Pedagocial Planner" , false);
		ArrayList<Role> roles = new ArrayList<Role>(2);
		roles.add(classRole);
		roles.add(teacherRole);
		return roles;
	}
	
	private ArrayList<Group> getGroups(Design design, HashMap<String, Participant> vleUsers, HashMap<String, Group> vleGroups){
		ArrayList<String> classParticipantIds = new ArrayList<String>(vleUsers.size());
		ArrayList<String> teacherIds = new ArrayList<String>();
		Iterator<Entry<String,Participant>> iterator = vleUsers.entrySet().iterator();
		while (iterator.hasNext()){
			Entry<String, Participant> entry = iterator.next();
			//classParticipantIds.add(entry.getValue().getId());
			if (entry.getValue().isStaff()){
				teacherIds.add(entry.getValue().getId());
			}else{
				classParticipantIds.add(entry.getValue().getId());
			}
		}
		//The first group contains all of the participants in the class (teacher or staff)
		Group classGroup = new Group("Group1", "PP - Class", null, classParticipantIds);
		Group teacherGroup = new Group("Group2", "PP - Teachers", null, teacherIds);
		ArrayList<Group> groups = new ArrayList<Group>(2);
		groups.add(classGroup);
		groups.add(teacherGroup);
		return groups;
	}
	
	private ArrayList<InstancedActivity> getInstancedActivities(Design design, ArrayList<Group> groups){
		int instActCount = 1;
		ArrayList<InstancedActivity> instActivities = new ArrayList<InstancedActivity>();
		ArrayList<Activity> activities = design.getRootActivity().getChildrenActivities();
		//we create an instanced activity for each deploy group and activity
		for (int i = 0; i < activities.size(); i++){
			if (!activities.get(i).getId().equals("ActGenInf")){
				for (int j = 0; j < groups.size(); j++){
					InstancedActivity instAct = new InstancedActivity("InstAct" + instActCount, null, activities.get(i).getId(), groups.get(j).getId(), activities.get(i).getResourceIds(), null);
					instActivities.add(instAct);
					instActCount++;
				}
			}
		}
		return instActivities;
	}

	@Override
	public Deploy processInstantiation(String filepath, Design design, HashMap<String, Group> vleGroups, HashMap<String, Participant> vleUsers) {
		ArrayList<Participant> participants = new ArrayList<Participant>(vleUsers.values());
		ArrayList<Group> groups = getGroups(design, vleUsers, vleGroups);
		ArrayList<InstancedActivity> instActivities = getInstancedActivities(design, groups);
		if (vleGroups!=null){
			groups.addAll(vleGroups.values());
		}
		Deploy deploy = new Deploy(null, design, design.getName(), null, design.getAuthor(), new Date(), null, instActivities, null, participants, groups);
		return deploy;
	}

	@Override
	public ILDAdaptor getLDAdaptor(Map<String, String> parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
