package glueps.core.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Activity {

	//@XmlAttribute
	private String id;
	
	private String name;
	
	private String description;
	
	//TODO By now, the order is the one in the ArrayList of children, unless otherwise specified
	//private ArrayList<String> nextActivityIds;
	//Sequencing of the array of children activities, sequencing by default
	public static final int SEQUENCE = 0;
	public static final int PARALLEL = 1;
	public static final int CHOOSE_ONE = 2;
	private int childrenSequenceMode = SEQUENCE;
	
	//TODO: Change this to some kind of Enum?
	public static final String CLASS = "class";
	public static final String INDIVIDUAL = "individual";
	public static final String GROUP_SEPARATE = "group";
	public static final String GROUP_OPEN = "groupopen";
	private String mode; //ie. class, group (open or separate), individual
	
	private ArrayList<Activity> childrenActivities;
	
	private String parentActivityId;
	
	private ArrayList<String> resourceIds;
	
	private ArrayList<String> roleIds;
	
	//This is the location of the activity in the VLE, once it is deployed (only for live deployments)
	private URL location;
	
	private boolean toDeploy = true;
	
	
	public Activity() {
		super();
	}


	public Activity(String id, String name, String description, String mode,
			ArrayList<Activity> childrenActivities, String parentActivityId,
			ArrayList<String> resourceIds, ArrayList<String> roleIds) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.mode = mode;
		this.childrenActivities = childrenActivities;
		this.parentActivityId = parentActivityId;
		this.resourceIds = resourceIds;
		this.roleIds = roleIds;
	}


	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}


	public String getParentActivityId() {
		return parentActivityId;
	}

	public void setParentActivityId(String parentActivityId) {
		this.parentActivityId = parentActivityId;
	}

	@XmlElementWrapper
	@XmlElement(name="resourceId")
	public ArrayList<String> getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(ArrayList<String> resourceIds) {
		this.resourceIds = resourceIds;
	}

	@XmlElementWrapper
	@XmlElement(name="roleId")
	public ArrayList<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(ArrayList<String> roleIds) {
		this.roleIds = roleIds;
	}


	@XmlElementWrapper
	@XmlElement(name="activity")
	public void setChildrenActivities(ArrayList<Activity> childrenActivities) {
		this.childrenActivities = childrenActivities;
	}

	public ArrayList<Activity> getChildrenActivities() {
		return childrenActivities;
	}


	@Override
	public String toString() {
		return "Activity [id=" + id + ", name=" + name + ", description="
				+ description + ", childrenSequenceMode="
				+ childrenSequenceMode + ", mode=" + mode
				+ ", childrenActivities=" + childrenActivities
				+ ", parentActivityId=" + parentActivityId + ", resourceIds="
				+ resourceIds + ", roleIds=" + roleIds + "]";
	}


	public void addChild(Activity child){
		if(child==null) return;
		else{
			if(childrenActivities==null){
				childrenActivities=new ArrayList<Activity>();
			}
			childrenActivities.add(child);
		}
	}


	public void setChildrenSequenceMode(int childrenSequenceMode) {
		this.childrenSequenceMode = childrenSequenceMode;
	}


	public int getChildrenSequenceMode() {
		return childrenSequenceMode;
	}


	public void setChildrenRoleIds(ArrayList<String> arrayList) {
		//Recursively sets the roles of all children activities to the provided role list
		if(childrenActivities!=null){
			for (Iterator<Activity> it = childrenActivities.iterator();it.hasNext();){
				Activity child = (Activity) it.next();
				child.setRoleIds(arrayList);
				child.setChildrenRoleIds(arrayList);
			}
		}
		
	}


	public void fixParentActivities() {
		
		if(childrenActivities!=null){
			
			for(Iterator it = childrenActivities.iterator();it.hasNext();){
				Activity child = (Activity) it.next();
				child.parentActivityId = this.id;
				child.fixParentActivities();
			}
			
		}
		
		return;
	}


	public ArrayList<String> getResourceIdsByInstantiable(
			Design design, boolean instantiable) {
		
		ArrayList<String> relevantResourceIds = null;
		
		if(design==null || design.getResources()==null || this.resourceIds==null) return null;
		
		for(Iterator<String> it = this.resourceIds.iterator();it.hasNext();){
			Resource res = design.findResourceById(it.next());
			if(res.isInstantiable() == instantiable){
				//This is a relevant resource, add it to the array
				if(relevantResourceIds==null) relevantResourceIds = new ArrayList<String>();
				relevantResourceIds.add(res.getId());
			}

		}
		
		return relevantResourceIds;
	}


	public Activity findChildrenActivityById(String activityId) {
		if(childrenActivities==null || activityId==null) return null;

		for(Iterator<Activity> it = childrenActivities.iterator();it.hasNext();){
			Activity act = it.next();
			if(act.getId().equals(activityId)) return act;
			else{
				act = act.findChildrenActivityById(activityId);
				if(act!=null) return act;
			}
			
		}
		
		return null;
	}


	public boolean usesResource(String id) {
		
		if(this.resourceIds==null || this.resourceIds.size()==0) return false;
		
		for(Iterator<String> it = resourceIds.iterator(); it.hasNext();){
			
			if(it.next().equals(id)) return true;
			
		}
		
		return false;
	}


	//returns a list with the children activities that use the resource (not counting the current activity)
	public ArrayList<Activity> getChildrenActivitiesUsingResource(String id,
			ArrayList<Activity> activities) {
		
		ArrayList<Activity> newActivities = activities;
		
		if(this.getChildrenActivities()!=null && this.getChildrenActivities().size()>0){
			for(Iterator<Activity> it = this.getChildrenActivities().iterator();it.hasNext();){//we iterate recursively through the children activities
				Activity child = it.next();
				if(child.usesResource(id)){
					if(newActivities==null) newActivities = new ArrayList<Activity>();
					newActivities.add(child);
					newActivities = child.getChildrenActivitiesUsingResource(id, newActivities);
				}
				
			}
			
			
		}
		
		return newActivities;
	}


	public void setLocation(URL location) {
		this.location = location;
	}


	public URL getLocation() {
		return location;
	}


	public boolean isPerformedOnlyByStaff(Design design) {
		
		if(this.roleIds==null || this.roleIds.size()==0) return false;//if we do not have enough information, return false just in case
		
		boolean hasStaff = false;
		
		for(String rolId : roleIds){
			if(design.findRoleById(rolId).isTeacher()) hasStaff = true;
			else return false;//If there is any non-staff, then it is not performed only by staff!
			
		}
		
		return hasStaff;
	}


	public void setToDeploy(boolean toDeploy) {
		this.toDeploy = toDeploy;
	}


	public boolean isToDeploy() {
		return toDeploy;
	}
	
	
	
}
