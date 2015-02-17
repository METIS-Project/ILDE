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
package glueps.adaptors.vle.moodle.model.moodle2;

/**
 * A grouping in a Moodle's course
 * @author Javier Hoyos Torio
 *
 */
public class CourseGrouping {
	
    private Integer id;
    private Integer courseid;
    private String name;
    private String description;
    private Integer descriptionformat;
    
    public CourseGrouping(Integer id, Integer courseid, String name, String description){
    	this.id = id;
    	this.courseid = courseid;
    	this.name = name;
    	this.description = description;
    }
    
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCourseid() {
		return courseid;
	}
	public void setCourseid(Integer courseid) {
		this.courseid = courseid;
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
	public Integer getDescriptionformat() {
		return descriptionformat;
	}
	public void setDescriptionformat(Integer descriptionformat) {
		this.descriptionformat = descriptionformat;
	}
	
}
