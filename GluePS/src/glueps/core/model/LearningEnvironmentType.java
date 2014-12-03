package glueps.core.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LearningEnvironmentType {
	
	private String id;
	private String name;
	private String description;
	private boolean getCourses;
	private boolean getParticipants;
	private boolean staticDeploy;
	private boolean dynamicDeploy;
	private boolean addTopic;
	private boolean multiplePosts;
	
	public LearningEnvironmentType(){
	}
	
	public LearningEnvironmentType(String id, String name, String description){
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public LearningEnvironmentType(String id, String name, String description, boolean getCourses, boolean getParticipants, boolean staticDeploy, boolean dynamicDeploy, boolean addTopic){
		this.id = id;
		this.name = name;
		this.description = description;
		this.getCourses = getCourses;
		this.getParticipants = getParticipants;
		this.staticDeploy = staticDeploy;
		this.dynamicDeploy = dynamicDeploy;
		this.addTopic = addTopic;
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
	public boolean isGetCourses() {
		return getCourses;
	}
	public void setGetCourses(boolean getCourses) {
		this.getCourses = getCourses;
	}
	public boolean isGetParticipants() {
		return getParticipants;
	}
	public void setGetParticipants(boolean getParticipants) {
		this.getParticipants = getParticipants;
	}
	public boolean isStaticDeploy() {
		return staticDeploy;
	}
	public void setStaticDeploy(boolean staticDeploy) {
		this.staticDeploy = staticDeploy;
	}
	public boolean isDynamicDeploy() {
		return dynamicDeploy;
	}
	public void setDynamicDeploy(boolean dynamicDeploy) {
		this.dynamicDeploy = dynamicDeploy;
	}
	public boolean isAddTopic() {
		return addTopic;
	}
	public void setAddTopic(boolean addTopic) {
		this.addTopic = addTopic;
	}

	public void setMultiplePosts(boolean multiplePosts) {
		this.multiplePosts = multiplePosts;
	}

	public boolean isMultiplePosts() {
		return multiplePosts;
	}

}
