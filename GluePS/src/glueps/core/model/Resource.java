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

public class Resource {
	
	private String id;
	
	private String name;
	
	private boolean instantiable;
	
	private String location;
	
	//TODO: Change this to an Enum?
	private String toolKind;
	public static final String TOOL_KIND_INTERNAL="internal";
	public static final String TOOL_KIND_EXTERNAL="external";
	
	private String toolType;
	protected static final String TOOL_TYPE_FORUM = "forum";
	protected static final String TOOL_TYPE_CHAT = "chat";
	protected static final String TOOL_TYPE_QUESTIONNAIRE = "questionnaire";
	protected static final String TOOL_TYPE_DOCUMENT = "document";
	
	private String toolData;

	public Resource() {
		super();
	}

	public Resource(String id, String name, boolean instantiable, String location,
			String toolKind, String toolType, String toolData) {
		super();
		this.id = id;
		this.name = name;
		this.instantiable = instantiable;
		this.location = location;
		this.toolKind = toolKind;
		this.toolType = toolType;
		this.toolData = toolData;
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

	public boolean isInstantiable() {
		return instantiable;
	}

	public void setInstantiable(boolean instantiable) {
		this.instantiable = instantiable;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getToolKind() {
		return toolKind;
	}

	public void setToolKind(String toolKind) {
		this.toolKind = toolKind;
	}

	public String getToolType() {
		return toolType;
	}
	
	/*
	 * This one is just a convenience method, since tooltype (for external tools) normally takes the form
	 * http://GM_ip:port/GLUEletManager/tools/id
	 */
	public String getToolTypeNumber() {
		String number=null;
		
		try {
			number = this.toolType.substring(this.toolType.lastIndexOf("/")+1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (number==null) return this.toolType;
		else return number;
		
	}

	public void setToolType(String toolType) {
		this.toolType = toolType;
	}

	public String getToolData() {
		return toolData;
	}

	public void setToolData(String toolData) {
		this.toolData = toolData;
	}

	@Override
	public String toString() {
		return "Resource [id=" + id + ", name=" + name + ", instantiable="
				+ instantiable + ", location=" + location + ", toolKind="
				+ toolKind + ", toolType=" + toolType + ", toolData="
				+ toolData + "]";
	}

}
