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
 * A resource of type Forum in Moodle
 * @author Javier Enrique Hoyos Torio
 *
 */

public class CourseForum {
	
	private Integer id;
	private Integer course;
	private String type;
	private String name;
	private String intro;
	private Integer introformat;
	private Integer assessed;
	private Integer assesstimestart;
	private Integer assesstimefinish;
	private Integer maxattachments;
	private Integer timemodified;
	
	public CourseForum(Integer id, Integer course, String type, String name, String intro){
		this.id = id;
		this.course = course;
		this.type = type; 
		this.name = name;
		this.intro = intro;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCourse() {
		return course;
	}
	public void setCourse(Integer course) {
		this.course = course;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public Integer getIntroformat() {
		return introformat;
	}
	public void setIntroformat(Integer introformat) {
		this.introformat = introformat;
	}
	public Integer getAssessed() {
		return assessed;
	}
	public void setAssessed(Integer assessed) {
		this.assessed = assessed;
	}
	public Integer getAssesstimestart() {
		return assesstimestart;
	}
	public void setAssesstimestart(Integer assesstimestart) {
		this.assesstimestart = assesstimestart;
	}
	public Integer getAssesstimefinish() {
		return assesstimefinish;
	}
	public void setAssesstimefinish(Integer assesstimefinish) {
		this.assesstimefinish = assesstimefinish;
	}
	public Integer getMaxattachments() {
		return maxattachments;
	}
	public void setMaxattachments(Integer maxattachments) {
		this.maxattachments = maxattachments;
	}
	public Integer getTimemodified() {
		return timemodified;
	}
	public void setTimemodified(Integer timemodified) {
		this.timemodified = timemodified;
	}
	

}
