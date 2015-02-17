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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Design {
	
	private String id;
	
	private String name;
	
	private String description;
	
	//TODO: Change to some kind of Enum?
	private String originalDesignType;
	
	private String author;
	
	//TODO: Change to some other kind of Timestamp?
	private Date timestamp;
	
	private ArrayList<String> objectives;
	
	private Activity rootActivity;
	
	private String originalDesignData;

	private ArrayList<Role> roles;
	
	private ArrayList<Resource> resources;
	
	public Design() {
		super();
	}

	
	
	public Design(String id, String name, String description,
			String originalDesignType, String author, Date timestamp,
			ArrayList<String> objectives, Activity rootActivity,
			String originalDesignData, ArrayList<Role> roles,
			ArrayList<Resource> resources) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.originalDesignType = originalDesignType;
		this.author = author;
		this.timestamp = timestamp;
		this.objectives = objectives;
		this.rootActivity = rootActivity;
		this.originalDesignData = originalDesignData;
		this.roles = roles;
		this.resources = resources;
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

	public String getOriginalDesignType() {
		return originalDesignType;
	}

	public void setOriginalDesignType(String originalDesignType) {
		this.originalDesignType = originalDesignType;
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

	@XmlElementWrapper
	@XmlElement(name="objective")
	public ArrayList<String> getObjectives() {
		return objectives;
	}

	public void setObjectives(ArrayList<String> objectives) {
		this.objectives = objectives;
	}

	
	@XmlElement(name="activity")
	public Activity getRootActivity() {
		return rootActivity;
	}

	public void setRootActivity(Activity rootActivity) {
		this.rootActivity = rootActivity;
	}

	public String getOriginalDesignData() {
		return originalDesignData;
	}

	public void setOriginalDesignData(String originalDesignData) {
		this.originalDesignData = originalDesignData;
	}

	public void setRoles(ArrayList<Role> roles) {
		this.roles = roles;
	}

	@XmlElementWrapper
	@XmlElement(name="role")
	public ArrayList<Role> getRoles() {
		return roles;
	}

	public void setResources(ArrayList<Resource> resources) {
		this.resources = resources;
	}

	@XmlElementWrapper
	@XmlElement(name="resource")
	public ArrayList<Resource> getResources() {
		return resources;
	}



	@Override
	public String toString() {
		return "Design [id=" + id + ", name=" + name + ", description="
				+ description + ", originalDesignType=" + originalDesignType
				+ ", author=" + author + ", timestamp=" + timestamp
				+ ", objectives=" + objectives + ", rootActivity="
				+ rootActivity + ", originalDesignData=" + originalDesignData
				+ ", roles=" + roles + ", resources=" + resources + "]";
	}



	public Resource findResourceById(String resourceId) {
		if(resources != null){
			for(Iterator<Resource> it = resources.iterator();it.hasNext();){
				Resource res = (Resource) it.next();
				if(res.getId().equals(resourceId)) return res;
			}
		}
		return null;
	}



	public Activity findActivityById(String activityId) {
		if(activityId == null || this.rootActivity==null) return null;
		
		if(this.rootActivity.getId().equals(activityId)) return rootActivity;
		else return this.rootActivity.findChildrenActivityById(activityId);
		
	}



	public Resource getResourceById(String id) {
		
		if(this.resources!=null){
			for(Iterator<Resource> it = resources.iterator();it.hasNext();){
				Resource res = it.next();
				if(res.getId().equals(id)) return res;
			}
			return null;
		}else return null;
		
	}



	public ArrayList<Role> getStaffRoles() {

		ArrayList<Role> staffRoles = null;
		
		if(roles!=null){
			for(Iterator<Role> it = roles.iterator();it.hasNext();){
				Role r = it.next();
				if(r.isTeacher()){
					if(staffRoles==null) staffRoles = new ArrayList<Role>();
					staffRoles.add(r);
				}
				
			}			
			return staffRoles;
		}else return null;
	}



	public ArrayList<Activity> getActivitiesForResource(String id) {
		ArrayList<Activity> activities = null;
				
		if(id == null || this.rootActivity==null) return null;
		
		if(this.rootActivity.usesResource(id)){
			if(activities==null) activities = new ArrayList<Activity>();
			activities.add(rootActivity);
		}
		
		activities = rootActivity.getChildrenActivitiesUsingResource(id, activities);
		
		return activities;
	}



	public Role findRoleById(String id) {
		if(roles != null){
			for(Iterator<Role> it = roles.iterator();it.hasNext();){
				Role rol = (Role) it.next();
				if(rol.getId().equals(id)) return rol;
			}
		}
		return null;
	}



	public String getTrimmedId() {
		if(!this.id.startsWith("http://") && this.id.indexOf("/designs/")==-1){
			//This is not an expected URL!
			return this.id;
		}else{
			//This is an expected URL
			return this.id.substring(this.id.indexOf("/designs/")+9);
		}
	}



	
}
