package glueps.adaptors.vle.moodle.model.moodle2;

/**
 * A module in a Moodle's course
 * @author Javier Hoyos Torio
 */
public class CourseModule {

	private Integer id;
	private Integer course;
	private Integer module;
	private Integer instance;
	private Integer section;
	private Integer visible;
	private Integer groupmode;
	private Integer groupingid;
	private Integer groupmembersonly;
	
	public CourseModule(Integer id, Integer course, Integer module, Integer instance, Integer section){
		this.id = id;
		this.course = course;
		this.module = module;
		this.instance = instance;
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

	public Integer getModule() {
		return module;
	}

	public void setModule(Integer module) {
		this.module = module;
	}

	public Integer getInstance() {
		return instance;
	}

	public void setInstance(Integer instance) {
		this.instance = instance;
	}

	public Integer getSection() {
		return section;
	}

	public void setSection(Integer section) {
		this.section = section;
	}

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}

	public Integer getGroupmode() {
		return groupmode;
	}

	public void setGroupmode(Integer groupmode) {
		this.groupmode = groupmode;
	}

	public Integer getGroupingid() {
		return groupingid;
	}

	public void setGroupingid(Integer groupingid) {
		this.groupingid = groupingid;
	}

	public Integer getGroupmembersonly() {
		return groupmembersonly;
	}

	public void setGroupmembersonly(Integer groupmembersonly) {
		this.groupmembersonly = groupmembersonly;
	}
}
