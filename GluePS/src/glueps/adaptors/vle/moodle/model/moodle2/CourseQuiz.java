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

public class CourseQuiz {

		private Integer id;
		private Integer course;
		private String name;
		private String intro;
		private Integer introformat;
		private Integer timeopen;
		private Integer timeclose;
		private String preferredbehaviour;
		private Integer attempts;
		private Integer timemodified;
		
		public CourseQuiz(Integer id, Integer course, String name, String intro){
			this.id = id;
			this.course = course;
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
		public Integer getTimeopen() {
			return timeopen;
		}
		public void setTimeopen(Integer timeopen) {
			this.timeopen = timeopen;
		}
		public Integer getTimeclose() {
			return timeclose;
		}
		public void setTimeclose(Integer timeclose) {
			this.timeclose = timeclose;
		}
		public String getPreferredbehaviour() {
			return preferredbehaviour;
		}
		public void setPreferredbehaviour(String preferredbehaviour) {
			this.preferredbehaviour = preferredbehaviour;
		}
		public Integer getAttempts() {
			return attempts;
		}
		public void setAttempts(Integer attempts) {
			this.attempts = attempts;
		}
		public Integer getTimemodified() {
			return timemodified;
		}
		public void setTimemodified(Integer timemodified) {
			this.timemodified = timemodified;
		}
		
		
}
