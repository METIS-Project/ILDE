package glueps.adaptors.vle.moodle.model.moodle2;

/**
 * A resource of type Page in Moodle
 * @author Javier Enrique Hoyos Torio
 *
 */
public class CoursePage {
	
	private Integer id;
    private Integer course;
    private String name;
    private String intro;
    private Integer introformat;
    private String content;
    private Integer contentformat;
    private Integer display;
    private String displayoptions;
    private Integer timemodified;
    
    public CoursePage(Integer id, Integer course, String name, String intro){
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getContentformat() {
		return contentformat;
	}
	public void setContentformat(Integer contentformat) {
		this.contentformat = contentformat;
	}
	public Integer getDisplay() {
		return display;
	}
	public void setDisplay(Integer display) {
		this.display = display;
	}
	public String getDisplayoptions() {
		return displayoptions;
	}
	public void setDisplayoptions(String displayoptions) {
		this.displayoptions = displayoptions;
	}
	public Integer getTimemodified() {
		return timemodified;
	}
	public void setTimemodified(Integer timemodified) {
		this.timemodified = timemodified;
	}

	

}
