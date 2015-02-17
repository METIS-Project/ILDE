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
 * @author Javier Hoyos Torio
 *
 */
public class CourseSection {
	
	private Integer id;
	private Integer course;
	private Integer section;
	private String name;
	private String summary;
	private Integer summaryformat;
	private String sequence;
	private Integer visible;
	
	public CourseSection(Integer id, Integer course, Integer section){
		this.id = id;
		this.course = course;
		this.section = section;
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

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Integer getSummaryformat() {
		return summaryformat;
	}

	public void setSummaryformat(Integer summaryformat) {
		this.summaryformat = summaryformat;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}

}
