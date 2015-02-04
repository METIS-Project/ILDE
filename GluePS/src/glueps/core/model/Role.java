package glueps.core.model;

public class Role {

	private String id;
	
	private String name;
	
	private String description;
	
	private boolean isTeacher;

	public Role() {
		super();
	}

	public Role(String id, String name, String description, boolean isTeacher) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.isTeacher = isTeacher;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", description="
				+ description + ", isTeacher=" + isTeacher + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public void setTeacher(boolean isTeacher) {
		this.isTeacher = isTeacher;
	}

	public boolean isTeacher() {
		return isTeacher;
	}

	
}
