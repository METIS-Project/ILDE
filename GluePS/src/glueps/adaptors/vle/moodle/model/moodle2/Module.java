package glueps.adaptors.vle.moodle.model.moodle2;

/**
 * A module type in Moodle
 * @author Javier Enrique Hoyos Torio
 *
 */
public class Module {

	private Integer id;
	private String name;
	private Integer version;
	private Integer cron;
	private Integer lastcron;
	private String search;
	private Integer visible;
	
	public Module(Integer id, String name, Integer version, Integer cron){
		this.id = id;
		this.name = name;
		this.version = version;
		this.cron = cron;
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
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getCron() {
		return cron;
	}
	public void setCron(Integer cron) {
		this.cron = cron;
	}
	public Integer getLastcron() {
		return lastcron;
	}
	public void setLastcron(Integer lastcron) {
		this.lastcron = lastcron;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public Integer getVisible() {
		return visible;
	}
	public void setVisible(Integer visible) {
		this.visible = visible;
	}
	
}
