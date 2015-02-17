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

public class Participant {

	
	//Separator for the fields inside the LearningEnvironmentData tag
	public final static String USER_PARAMETER_SEPARATOR = ";";

	
	private String id;
	
	private String name;
	
	private String deployId;
	
	//In Moodle, this is <userid>;<username>;<email>;<firstaccess>;
	private String learningEnvironmentData;
	
	private boolean isStaff;

	public Participant() {
		super();
	}

	public Participant(String id, String name, String deployId,
			String learningEnvironmentData, boolean isStaff) {
		super();
		this.id = id;
		this.name = name;
		this.deployId = deployId;
		this.learningEnvironmentData = learningEnvironmentData;
		this.isStaff = isStaff;
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


	public String getLearningEnvironmentData() {
		return learningEnvironmentData;
	}

	public void setLearningEnvironmentData(String learningEnvironmentData) {
		this.learningEnvironmentData = learningEnvironmentData;
	}

	@Override
	public String toString() {
		return "Participant [id=" + id + ", name=" + name + ", deployId=" + deployId
				+ ", learningEnvironmentData=" + learningEnvironmentData + "+, isStaff="+isStaff+"]";
	}

	public void setStaff(boolean isStaff) {
		this.isStaff = isStaff;
	}

	public boolean isStaff() {
		return isStaff;
	}

	
}
