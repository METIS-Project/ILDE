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
import java.net.URL;
import java.util.ArrayList;


public class InstancedActivity {

	private String id;
	
	private String deployId;
	
	private String activityId;
	
	private String groupId;
	
	private ArrayList<String> resourceIds;
	
	private ArrayList<String> instancedToolIds;

	//This is the location of the instancedActivity in the VLE, once it is deployed (only for live deployments)
	private URL location;
	

	public InstancedActivity() {
		super();
	}

	public InstancedActivity(String id, String deployId, String activityId,
			String groupId, ArrayList<String> resourceIds, ArrayList<String> instances) {
		super();
		this.id = id;
		this.deployId = deployId;
		this.activityId = activityId;
		this.groupId = groupId;
		this.resourceIds = resourceIds;
		this.instancedToolIds = instances;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public ArrayList<String> getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(ArrayList<String> resourceIds) {
		this.resourceIds = resourceIds;
	}

	@Override
	public String toString() {
		return "InstancedActivity [id=" + id + ", deployId=" + deployId
				+ ", activityId=" + activityId + ", groupId=" + groupId
				+ ", resourceIds=" + resourceIds + ", instancedToolIds=" + instancedToolIds + "]";
	}

	public void setInstancedToolIds(ArrayList<String> instancedToolIds) {
		this.instancedToolIds = instancedToolIds;
	}

	public ArrayList<String> getInstancedToolIds() {
		return instancedToolIds;
	}

	public void setLocation(URL location) {
		this.location = location;
	}

	public URL getLocation() {
		return location;
	}
	
	
}
