package glueps.adaptors.vle.moodle.model.moodle2;

/**
 * A rol in Moodle
 * @author Javier Enrique Hoyos Torio
 *
 */
public class Role {

	private Integer id;
    private String name;
    private String shortname;
    private String description;
    private Integer sortorder;
    
    public Role(Integer id, String name, String shortname, String description, Integer sortorder){
    	this.id = id;
    	this.name = name;
    	this.shortname = shortname;
    	this.description = description;
    	this.sortorder = sortorder;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSortorder() {
		return sortorder;
	}

	public void setSortorder(Integer sortorder) {
		this.sortorder = sortorder;
	}

}
