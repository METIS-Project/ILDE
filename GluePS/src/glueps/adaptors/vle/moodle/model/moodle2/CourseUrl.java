package glueps.adaptors.vle.moodle.model.moodle2;

/**
 * A resource of type URL in Moodle
 * @author Javier Enrique Hoyos Torio
 *
 */
public class CourseUrl {

	private Integer id;
	private Integer course;
	private String name;
	private String intro;
	private Integer introformat;
	private String externalurl;
	private Integer display;
	private String displayoptions;
	private String parameters;
	private Integer timemodified;
	
	public CourseUrl(Integer id, Integer course, String name, String intro, Integer introformat, String externalurl){
		this.id = id;
		this.course = course;
		this.name = name;
		this.intro = intro;
		this.introformat = introformat;
		this.externalurl = externalurl;
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
	public String getExternalurl() {
		return externalurl;
	}
	public void setExternalurl(String externalurl) {
		this.externalurl = externalurl;
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
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public Integer getTimemodified() {
		return timemodified;
	}
	public void setTimemodified(Integer timemodified) {
		this.timemodified = timemodified;
	}
}
