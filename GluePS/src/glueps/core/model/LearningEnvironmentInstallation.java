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
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;

import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LearningEnvironmentInstallation {
	
	private String id;
	
	private String name;

	private String type;
	
	private URL accessLocation;
	
	private String parameters;
	
	private long sectype;
	
	public LearningEnvironmentInstallation() {
		super();
	}

	public LearningEnvironmentInstallation(String id, String name, String type, URL accessLocation, String parameters, long sectype) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.accessLocation = accessLocation;
		this.parameters = parameters;
		this.sectype = sectype;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public URL getAccessLocation() {
		return accessLocation;
	}

	public void setAccessLocation(URL accessLocation) {
		this.accessLocation = accessLocation;
	}

	@Override
	public String toString() {
		return "LearningEnvironmentInstallation [id=" + id + ", name=" + name + ", type="
				+ type + ", accessLocation=" + accessLocation + "]";
	}
	
	
	/**
	 * Decodes a string with adapter-specific parameters
	 * 
	 * @return	Map<String, String>		Set of name-value pairs with the decoded parameters. 
	 */
	public Map<String, String> decodeParams(){
		HashMap<String, String> result = new HashMap<String, String>();
		if (parameters != null) {
			StringTokenizer tokenizer = new StringTokenizer(parameters, "&");
			while (tokenizer.hasMoreElements()) {
				String token = tokenizer.nextToken();
				int pos = token.indexOf("=");
				if (pos > 0) {
					result.put(token.substring(0, pos), Reference.decode(token.substring(pos+1)));
				}
			}
		}
		return result;
	}
		
	public long getSectype() {
		return sectype;
	}

	public void setSectype(long sectype) {
		this.sectype = sectype;
	}

	
}
