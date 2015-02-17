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
 * A enrol type in Moodle
 * @author Javier Enrique Hoyos Torio
 *
 */
public class CourseEnrol {

	private Integer id;
    private String enrol;
    private Integer status;
    private Integer courseid;
    private Integer sortorder;
    private Integer timecreated;
    private Integer timemodified;
    
    public CourseEnrol(Integer id, String enrol, Integer status, Integer courseid){
    	this.id = id;
    	this.enrol = enrol;
    	this.status = status;
    	this.courseid = courseid;
    }
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEnrol() {
		return enrol;
	}
	public void setEnrol(String enrol) {
		this.enrol = enrol;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCourseid() {
		return courseid;
	}
	public void setCourseid(Integer courseid) {
		this.courseid = courseid;
	}
	public Integer getSortorder() {
		return sortorder;
	}
	public void setSortorder(Integer sortorder) {
		this.sortorder = sortorder;
	}
	public Integer getTimecreated() {
		return timecreated;
	}
	public void setTimecreated(Integer timecreated) {
		this.timecreated = timecreated;
	}
	public Integer getTimemodified() {
		return timemodified;
	}
	public void setTimemodified(Integer timemodified) {
		this.timemodified = timemodified;
	}
	

}
