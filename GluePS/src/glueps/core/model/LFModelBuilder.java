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
import java.util.Arrays;
import java.util.Date;

//This simple class constructs an instance of a design and deploy in lingua franca

public class LFModelBuilder {

	protected static final String GROUP_MODE = "group";

	protected static final String INTERNAL_TOOL_KIND = "internal";

	protected static final String TOOL_TYPE_FORUM = "forum";

	protected static final String TOOL_TYPE_CHAT = "chat";

	protected static final String TOOL_TYPE_QUESTIONNAIRE = "questionnaire";

	protected static final String TOOL_TYPE_DOCUMENT = "document";

	protected static final String INDIVIDUAL_MODE = "individual";

	protected static final String MOODLE_LE = "moodle";

	//These are the ones that will be made available
	protected ArrayList<Activity> activities = new ArrayList<Activity>();
	protected ArrayList<Resource> resources = new ArrayList<Resource>();
	protected ArrayList<Role> roles = new ArrayList<Role>();
	protected Design design = null;
	
	protected ArrayList<Participant> participants = new ArrayList<Participant>();
	protected ArrayList<Group> groups = new ArrayList<Group>();
	protected ArrayList<ToolInstance> toolInstances = new ArrayList<ToolInstance>();
	protected ArrayList<InstancedActivity> instancedActivities = new ArrayList<InstancedActivity>();
	protected Deploy deploy = null;
	protected LearningEnvironment learningEnvironment = null;
	
	public LFModelBuilder() throws MalformedURLException{
		
		
	}

	public ArrayList<Activity> getActivities() {
		return activities;
	}



	public void setActivities(ArrayList<Activity> activities) {
		this.activities = activities;
	}



	public ArrayList<Resource> getResources() {
		return resources;
	}



	public void setResources(ArrayList<Resource> resources) {
		this.resources = resources;
	}



	public ArrayList<Role> getRoles() {
		return roles;
	}



	public void setRoles(ArrayList<Role> roles) {
		this.roles = roles;
	}



	public Design getDesign() {
		return design;
	}



	public void setDesign(Design design) {
		this.design = design;
	}



	public Deploy getDeploy() {
		return deploy;
	}



	public void setDeploy(Deploy deploy) {
		this.deploy = deploy;
	}



	public void setParticipants(ArrayList<Participant> participants) {
		this.participants = participants;
	}



	public ArrayList<Participant> getParticipants() {
		return participants;
	}



	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}



	public ArrayList<Group> getGroups() {
		return groups;
	}



	public void setToolInstances(ArrayList<ToolInstance> toolInstances) {
		this.toolInstances = toolInstances;
	}



	public ArrayList<ToolInstance> getToolInstances() {
		return toolInstances;
	}



	public void setInstancedActivities(ArrayList<InstancedActivity> instancedActivities) {
		this.instancedActivities = instancedActivities;
	}



	public ArrayList<InstancedActivity> getInstancedActivities() {
		return instancedActivities;
	}



	public LearningEnvironment getLearningEnvironment() {
		return learningEnvironment;
	}



	public void setLearningEnvironment(LearningEnvironment learningEnvironment) {
		this.learningEnvironment = learningEnvironment;
	}
	
	
	
}
