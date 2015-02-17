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


public class Group {

	private String id;
	
	private String name;
	
	private String deployId;
	
	private ArrayList<String> participantIds;
	

	public Group() {
		super();
	}

	public Group(String id, String name, String deployId,
			ArrayList<String> participantIds) {
		super();
		this.id = id;
		this.name = name;
		this.deployId = deployId;
		this.participantIds = participantIds;
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

	public ArrayList<String> getParticipantIds() {
		return participantIds;
	}

	public void setParticipantIds(ArrayList<String> participantIds) {
		this.participantIds = participantIds;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", deployId=" + deployId
				+ ", participantIds=" + participantIds + "]";
	}

	
	
}
