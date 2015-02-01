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
