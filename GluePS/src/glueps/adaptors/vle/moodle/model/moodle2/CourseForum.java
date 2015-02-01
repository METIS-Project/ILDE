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
