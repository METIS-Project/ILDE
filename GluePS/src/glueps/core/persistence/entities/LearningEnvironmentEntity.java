package glueps.core.persistence.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "glueps_learning_environments")
        @NamedQueries({@NamedQuery(name = "LearningEnvironmentEntity.findById", query = "SELECT u FROM LearningEnvironmentEntity " +
        "u WHERE u.id = :id"),
        @NamedQuery(name = "LearningEnvironmentEntity.listAll", query = "SELECT u FROM LearningEnvironmentEntity u"),
        @NamedQuery(name = "LearningEnvironmentEntity.deleteById", query = "DELETE FROM LearningEnvironmentEntity u WHERE u.id=:id")})
public class LearningEnvironmentEntity implements Serializable {
    
    
 
	/**
	 * Auto-generated uid
	 */
	private static final long serialVersionUID = 3647320785534166908L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private long id;
	
	@Basic(optional = false)
    @Column(name = "name")
	private String name;
	
	@Basic(optional = false)
    @Column(name = "type")
	private String type;
	
	@Basic(optional = false)
    @Column(name = "accessLocation")
	private String accessLocation;
	
	@Basic(optional = false)
	@Column(name = "userid")
	private String userid;
	
	@Basic(optional = false)
	@Column(name = "creduser")
	private String creduser;
	
	@Basic(optional = false)
	@Column(name = "credsecret")
	private String credsecret;
	
	@Basic(optional = false)
	@Column(name = "installation")
	private long installation;
	
	
    /*@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length=1048576) 
	private byte[] xmlfile;*/
	
    
    public LearningEnvironmentEntity() {
    	
    }

    public LearningEnvironmentEntity(long id) {
        this.id = id;
    }
    
    public LearningEnvironmentEntity(long id, String name, String type, String accessLocation, String userid, String creduser, String credsecret, long installation) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.accessLocation = accessLocation;
        this.userid = userid;
        this.creduser = creduser;
        this.credsecret = credsecret;
        this.installation = installation;
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
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getAccessLocation() {
        return accessLocation;
    }

    public void setAccessLocation(String accessLocation) {
        this.accessLocation = accessLocation;
    }
    
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    public String getCreduser() {
        return creduser;
    }

    public void setCreduser(String creduser) {
        this.creduser  = userid;
    }
    
    public String getCredsecret() {
        return credsecret;
    }

    public void setCredsecret(String credsecret) {
        this.credsecret  = credsecret;
    }
    
    public long getInstallation() {
        return installation;
    }

    public void setInstallation(long installation) {
        this.installation  = installation;
    }

 

}

