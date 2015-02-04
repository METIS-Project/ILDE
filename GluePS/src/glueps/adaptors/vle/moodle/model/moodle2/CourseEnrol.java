package glueps.adaptors.vle.moodle.model.moodle2;

/**
 * A enrol type in Moodle
 * @author Javier Enrique Hoyos Torio
 *
 */
public class CourseEnrol {

	private Integer id;
    private String enrol;
    private Integer status;
    private Integer courseid;
    private Integer sortorder;
    private Integer timecreated;
    private Integer timemodified;
    
    public CourseEnrol(Integer id, String enrol, Integer status, Integer courseid){
    	this.id = id;
    	this.enrol = enrol;
    	this.status = status;
    	this.courseid = courseid;
    }
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEnrol() {
		return enrol;
	}
	public void setEnrol(String enrol) {
		this.enrol = enrol;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getCourseid() {
		return courseid;
	}
	public void setCourseid(Integer courseid) {
		this.courseid = courseid;
	}
	public Integer getSortorder() {
		return sortorder;
	}
	public void setSortorder(Integer sortorder) {
		this.sortorder = sortorder;
	}
	public Integer getTimecreated() {
		return timecreated;
	}
	public void setTimecreated(Integer timecreated) {
		this.timecreated = timecreated;
	}
	public Integer getTimemodified() {
		return timemodified;
	}
	public void setTimemodified(Integer timemodified) {
		this.timemodified = timemodified;
	}
	

}
