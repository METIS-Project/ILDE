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

public class Role {

	private String id;
	
	private String name;
	
	private String description;
	
	private boolean isTeacher;

	public Role() {
		super();
	}

	public Role(String id, String name, String description, boolean isTeacher) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.isTeacher = isTeacher;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", description="
				+ description + ", isTeacher=" + isTeacher + "]";
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTeacher(boolean isTeacher) {
		this.isTeacher = isTeacher;
	}

	public boolean isTeacher() {
		return isTeacher;
	}

	
}
