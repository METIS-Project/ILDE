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
	
	@Basic(optional = false)
	@Column(name = "showAR")
	private boolean showAR;
	
	@Basic(optional = false)
	@Column(name = "showVG")
	private boolean showVG;
	
	
    /*@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length=1048576) 
	private byte[] xmlfile;*/
	

	public LearningEnvironmentEntity() {
    	
    }

    public LearningEnvironmentEntity(long id) {
        this.id = id;
    }
    
    public LearningEnvironmentEntity(long id, String name, String userid, String creduser, String credsecret, long installation, boolean showAR, boolean showVG) {
        this.id = id;
        this.name = name;
        this.userid = userid;
        this.creduser = creduser;
        this.credsecret = credsecret;
        this.installation = installation;
        this.showAR = showAR;
        this.showVG = showVG;
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
    
    public boolean isShowAR() {
		return showAR;
	}

	public void setShowAR(boolean showAR) {
		this.showAR = showAR;
	}

	public boolean isShowVG() {
		return showVG;
	}

	public void setShowVG(boolean showVG) {
		this.showVG = showVG;
	}

 

}

