package glueps.adaptors.vle.moodle.model.moodle2;

/**
 * A resource of type Chat in Moodle
 * @author Javier Enrique Hoyos Torio
 *
 */

public class CourseChat {
    
	private Integer id;
    private Integer course;
    private String name;
    private String intro;
    private Integer introformat;
    private Integer keepdays;
    private Integer studentlogs;
    private Integer chattime;
    private Integer schedule;
    private Integer timemodified;
	
	public CourseChat(Integer id, Integer course, String name, String intro){
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
	public Integer getKeepdays() {
		return keepdays;
	}
	public void setKeepdays(Integer keepdays) {
		this.keepdays = keepdays;
	}
	public Integer getStudentlogs() {
		return studentlogs;
	}
	public void setStudentlogs(Integer studentlogs) {
		this.studentlogs = studentlogs;
	}
	public Integer getChattime() {
		return chattime;
	}
	public void setChattime(Integer chattime) {
		this.chattime = chattime;
	}
	public Integer getSchedule() {
		return schedule;
	}
	public void setSchedule(Integer schedule) {
		this.schedule = schedule;
	}
	public Integer getTimemodified() {
		return timemodified;
	}
	public void setTimemodified(Integer timemodified) {
		this.timemodified = timemodified;
	}
	

}
