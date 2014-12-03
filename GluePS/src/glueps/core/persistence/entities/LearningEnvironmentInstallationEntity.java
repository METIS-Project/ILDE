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


@Entity
@Table(name = "glueps_learning_environments_installations")
        @NamedQueries({@NamedQuery(name = "LearningEnvironmentInstallationEntity.findById", query = "SELECT u FROM LearningEnvironmentInstallationEntity " +
        "u WHERE u.id = :id"),
        @NamedQuery(name = "LearningEnvironmentInstallationEntity.findByAccessLocation", query = "SELECT u FROM LearningEnvironmentInstallationEntity " +
        "u WHERE u.accessLocation = :accessLocation"), 
        @NamedQuery(name = "LearningEnvironmentInstallationEntity.listAll", query = "SELECT u FROM LearningEnvironmentInstallationEntity u ")
        })
public class LearningEnvironmentInstallationEntity implements Serializable {
    
    
    /**
	 * Auto-generated uid
	 */
	private static final long serialVersionUID = -9204808581482158829L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private long id;
    
	@Basic(optional = false)
    @Column(name = "name")
    private String name;
    
	@Basic(optional = false)
    @Column(name = "accessLocation")
    private String accessLocation;
	
    @Basic(optional = true)
    @Column(name = "parameters")
    private String parameters;
    
    @Basic(optional = false)
    @Column(name = "sectype")
    private long sectype;
    
    @Basic(optional = false)
    @Column(name = "leType")
    private long leType;
    
    public LearningEnvironmentInstallationEntity() {
    }

    public LearningEnvironmentInstallationEntity(long id) {
        this.id = id;
    }
    
    public LearningEnvironmentInstallationEntity(long id, String name, long leType, String accessLocation) {
        this.id = id;
        this.name = name;
        this.leType = leType;
        this.accessLocation = accessLocation;
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
    
    public String getAccessLocation() {
        return this.accessLocation;
    }

    public void setAccessLocation(String accessLocation) {
        this.accessLocation = accessLocation;
    }
    
    public String getParameters(){
    	return this.parameters;
    }
    
    public void setParameters(String parameters){
    	this.parameters = parameters;
    }
    
	public long getSectype() {
		return sectype;
	}

	public void setSectype(long sectype) {
		this.sectype = sectype;
	}
	
	public long getLeType(){
		return leType;
	}
	
	public void setLeType(long leType){
		this.leType = leType;
	}
   

}

