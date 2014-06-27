package glueps.adaptors.vle.moodle.model.moodle2;

/**
 * A resource of type Assignment in Moodle
 * @author Javier Enrique Hoyos Torio
 *
 */

public class CourseAssignment {
	
    private Integer id;
    private Integer course;
    private String name;
    private String intro;
    private Integer introformat;
    private String assignmenttype;
    private Integer resubmit;
    private Integer preventlate;
    private Integer emailteachers;
    private Integer grade;
    private Integer maxbytes;
    private Integer timemodified;
    
    public CourseAssignment(Integer id, Integer course, String name, String intro){
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
	public String getAssignmenttype() {
		return assignmenttype;
	}
	public void setAssignmenttype(String assignmenttype) {
		this.assignmenttype = assignmenttype;
	}
	public Integer getResubmit() {
		return resubmit;
	}
	public void setResubmit(Integer resubmit) {
		this.resubmit = resubmit;
	}
	public Integer getPreventlate() {
		return preventlate;
	}
	public void setPreventlate(Integer preventlate) {
		this.preventlate = preventlate;
	}
	public Integer getEmailteachers() {
		return emailteachers;
	}
	public void setEmailteachers(Integer emailteachers) {
		this.emailteachers = emailteachers;
	}
	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public Integer getMaxbytes() {
		return maxbytes;
	}
	public void setMaxbytes(Integer maxbytes) {
		this.maxbytes = maxbytes;
	}
	public Integer getTimemodified() {
		return timemodified;
	}
	public void setTimemodified(Integer timemodified) {
		this.timemodified = timemodified;
	}

}
