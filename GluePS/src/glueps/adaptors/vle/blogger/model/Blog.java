package glueps.adaptors.vle.blogger.model;

/**
 * A blog in blogger
 * @author javierht
 *
 */
public class Blog {
	
	private String kind;
	private String id;
	private String name;
	private String description;
	private String url;
	
	public Blog(String id, String name, String description, String url){
		this.id = id;
		this.name = name;
		this.description = description;
		this.url = url;
		this.kind = "blogger#blogList";
	}
	
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
