package glueps.core.persistence.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * JPA Entity for the glueps_learning_environemnts_types table that 
 * stores the information about the different types of learning environments with which Glue!-PS can work
 * @author Javier Enrique Hoyos Torio
 *
 */
@Entity
@Table(name = "glueps_learning_environments_types")
@NamedQueries({@NamedQuery(name = LearningEnvironmentTypeEntity.QUERY_FIND_BY_ID, query = "SELECT te FROM LearningEnvironmentTypeEntity te " + "where te.id = :" + LearningEnvironmentTypeEntity.PARAM_ID),
			   @NamedQuery(name = LearningEnvironmentTypeEntity.QUERY_FIND_BY_NAME, query = "SELECT te FROM LearningEnvironmentTypeEntity te " + "where te.name = :" + LearningEnvironmentTypeEntity.PARAM_NAME),
			   @NamedQuery(name = LearningEnvironmentTypeEntity.QUERY_LIST_ALL, query = "SELECT te FROM LearningEnvironmentTypeEntity te")
			 })
public class LearningEnvironmentTypeEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9189536550370273209L;
	
	public static final String QUERY_FIND_BY_ID = "LearningEnvironmentTypeEntity.findById";
	public static final String QUERY_FIND_BY_NAME = "LearningEnvironmentTypeEntity.findByName";
	public static final String QUERY_LIST_ALL = "LearningEnvironmentTypeEntity.listAll";
	
	public static final String PARAM_ID = "id";
	public static final String PARAM_NAME = "name";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Basic(optional = false)
	@Column(name = "id")
	private long id;
	
	@Basic(optional = false)
	@Column(name = "name", unique=true)
	private String name;
	
	@Basic(optional = true)
	@Column(name = "description")
	private String description;
	
	@Basic(optional = false)
	@Column(name = "getCourses")
	private boolean getCourses;
	
	@Basic(optional = false)
	@Column(name = "getParticipants")
	private boolean getParticipants;
	
	@Basic(optional = false)
	@Column(name = "staticDeploy")
	private boolean staticDeploy;
	
	@Basic(optional = false)
	@Column(name = "dynamicDeploy")
	private boolean dynamicDeploy;
	
	@Basic(optional = false)
	@Column(name = "addTopic")
	private boolean addTopic;
	
	@Basic(optional = false)
	@Column(name = "multiplePosts")
	private boolean multiplePosts;

	public LearningEnvironmentTypeEntity(){
		
	}
	
	public LearningEnvironmentTypeEntity(long id){
		this.id = id;
	}
	
	public LearningEnvironmentTypeEntity(long id, String name, String description){
		this.id = id;
		this.name = name;
		this.description = description;
		this.getCourses = true;
		this.getParticipants = true;
		this.staticDeploy = true;
		this.dynamicDeploy = true;
		this.addTopic = false;
		this.multiplePosts = false;
	}
	
	public LearningEnvironmentTypeEntity(long id, String name, String description, boolean getCourses, boolean getParticipants, boolean staticDeploy, boolean dynamicDeploy, boolean addTopic, boolean multiplePosts){
		this.id = id;
		this.name = name;
		this.description = description;
		this.getCourses = getCourses;
		this.getParticipants = getParticipants;
		this.staticDeploy = staticDeploy;
		this.dynamicDeploy = dynamicDeploy;
		this.addTopic = addTopic;
		this.multiplePosts = multiplePosts;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
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
	
	public boolean isMultiplePosts() {
		return multiplePosts;
	}

	public void setMultiplePosts(boolean multiplePosts) {
		this.multiplePosts = multiplePosts;
	}
}
