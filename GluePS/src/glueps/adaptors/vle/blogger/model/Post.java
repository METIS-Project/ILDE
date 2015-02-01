package glueps.adaptors.vle.blogger.model;

public class Post {
	
	private String kind;
	private String id;
	private String published;
	private String updated;
	private String url;
	private String selfLink;
	private String title;
	private String content;

	public Post(String id, String url, String selfLink, String title, String content){
		this.id = id;
		this.url = url;
		this.selfLink = selfLink;
		this.title = title;
		this.content = content;
		this.kind = "blogger#post";
	}

	public String getSelfLink() {
		return selfLink;
	}

	public void setSelfLink(String selfLink) {
		this.selfLink = selfLink;
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

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
