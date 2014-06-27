package glueps.adaptors.vle.moodle.model.moodle2;

/**
 * A grouping in a Moodle's course
 * @author Javier Hoyos Torio
 *
 */
public class CourseGrouping {
	
    private Integer id;
    private Integer courseid;
    private String name;
    private String description;
    private Integer descriptionformat;
    
    public CourseGrouping(Integer id, Integer courseid, String name, String description){
    	this.id = id;
    	this.courseid = courseid;
    	this.name = name;
    	this.description = description;
    }
    
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCourseid() {
		return courseid;
	}
	public void setCourseid(Integer courseid) {
		this.courseid = courseid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getDescriptionformat() {
		return descriptionformat;
	}
	public void setDescriptionformat(Integer descriptionformat) {
		this.descriptionformat = descriptionformat;
	}
	
}
