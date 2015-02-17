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
import java.util.Iterator;


public class ToolInstance {
	
	private String id;
	
	private String name;
	
	private String deployId;
	
	private String resourceId;
	
	private URL location;
	
	//Added by Juan for initial support to geo-located resources
	private String position;
	private String maxdistance;
	private String positionType;
	private String scale;
	private String orientation;
	private String description;


	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	//Convenience field to store internal references (reuse) in non-URL form
	private String internalReference;
	
	public ToolInstance() {
		super();
	}
	
	public ToolInstance(String id, String name, String deployId,
			String resourceId, URL location) {
		super();
		this.id = id;
		this.name = name;
		this.deployId = deployId;
		this.resourceId = resourceId;
		this.location = location;
	}


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

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public URL getLocation() {
		return location;
	}

	public void setLocation(URL location) {
		this.location = location;
	}


	public String getInternalReference() {
		return internalReference;
	}

	public void setInternalReference(String internalReference) {
		this.internalReference = internalReference;
	}
	
	@Override
	public String toString() {
		return "ToolInstance [id=" + id + ", name=" + name + ", deployId="
				+ deployId + ", resourceId=" + resourceId + ", location="
				+ location + ", internalReference="+internalReference+"]";
	}
	
	
	//This returns the toolInstanceId, not the URL but just the 
	public String getTrimmedId(){
		if(!this.id.startsWith("http://") || this.id.indexOf("/toolInstances/")==-1){
			//This is not an expected URL!
			return this.id;
		}else{
			//This is an expected URL
			return this.id.substring(this.id.indexOf("/toolInstances/")+15);
		}
		
	}

	public URL getLocationWithRedirects(Deploy deploy) {
		//We first check the internal references (non-urlified deploy
		if(internalReference!=null){
			ToolInstance redirected = null;
			if((redirected=deploy.getToolInstanceById(internalReference))==null) return this.location;//we found no toolInstance with such an id, it must be the real location, not a redirection
			else return redirected.getLocationWithRedirects(deploy);//we get recursively the location in the redirected toolInstance
		}else{//there is no internal reference, or it is a urlified deploy
			if(this.location == null) return null;
			//we look for a toolInstance in this deploy, with the id in our location field
			ToolInstance redirected=null;
			if((redirected=deploy.getToolInstanceById(this.location.toString()))==null){
				//we found no toolInstance with such an id, it must be the real location, not a redirection
				return this.location;
			}else{
				//we get recursively the location in the redirected toolInstance
				return redirected.getLocationWithRedirects(deploy);
			}
		}
	}

	public boolean hasCircularReferences(Deploy deploy, ArrayList<String> arrayList) {
		//if we have already traversed this toolInstance while following redirects, it is circular!
		if(arrayList.contains(this.getId())) return true;
		
		//if not, we add this instance to the list of traversed instances, and then follow the redirects
		arrayList.add(this.getId());
		
		if(this.internalReference!=null){//redirected in non-urlified deploy
			ToolInstance redirected=null;
			if((redirected=deploy.getToolInstanceById(this.internalReference))==null){//we found no toolInstance with such an id, it must be the real location, not a redirection
				return false;
			}else{
				//it is a real redirection

				//we do the same recursively
				return redirected.hasCircularReferences(deploy, arrayList);
				
			}
		}else{//not redirected, or urlified deploy
			if(this.location == null) return false;
			//we look for a toolInstance in this deploy, with the id in our location field
			ToolInstance redirected=null;
			if((redirected=deploy.getToolInstanceById(this.location.toString()))==null){
				//we found no toolInstance with such an id, it must be the real location, not a redirection
				return false;
			}else{
				//it is a redirection

				//we do the same recursively
				return redirected.hasCircularReferences(deploy, arrayList);
			}
		}
	}
	
	public boolean isRedirection(Deploy deploy){
		//We first check the internal references (non-urlified deploy
		if(internalReference!=null){
			return true;
		}else{
			if(this.getLocation()==null) return false;
			else if(this.getLocationWithRedirects(deploy)==null) return true;//it is a redirection leading to a null (not created) tool instance
			if(this.getLocation().toString().equals(this.getLocationWithRedirects(deploy).toString())) return false;//the location is the same either we follow the redirections or not
			else return true;
		}
	}


	public ToolInstance getInstanceWithRedirects(Deploy deploy) {
		if(this.location == null) return this;
		//we look for a toolInstance in this deploy, with the id in our location field
		ToolInstance redirected=null;
		if((redirected=deploy.getToolInstanceById(this.location.toString()))==null){
			//we found no toolInstance with such an id, it must be the real location, not a redirection
			return this;
		}else{
			//we get recursively the location in the redirected toolInstance
			return redirected.getInstanceWithRedirects(deploy);
		}
	}

	//Added by Juan for initial support to geo-located resources
	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition() {
		return position;
	}

	public String getMaxdistance() {
		return maxdistance;
	}
	public String getOrientation() {
		return orientation;
	}

	public void setMaxdistance(String maxdistance) {
		this.maxdistance = maxdistance;
	}
	
	/**
	 * The toolInstance has any of these position types
	 * @param posType The different position types
	 * @return Whether the toolInstance has any of these position types
	 */
	public boolean hasPositionType(ArrayList<String> posType){
		for(Iterator<String> it = posType.iterator(); it.hasNext();){
			String pt = it.next();
			if (this.positionType!=null && this.positionType.equals(pt)){
				return true;
			}
		}
		return false;
	}
}
