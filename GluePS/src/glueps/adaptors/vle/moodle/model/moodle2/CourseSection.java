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
