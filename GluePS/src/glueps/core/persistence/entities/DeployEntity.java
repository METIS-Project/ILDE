package glueps.core.persistence.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "glueps_deploys")
@NamedQueries({@NamedQuery(name = "DeployEntity.findById", query = "SELECT u FROM DeployEntity " +
        "u WHERE u.id = :id"),
        @NamedQuery(name = "DeployEntity.listByUser", query = "SELECT u FROM DeployEntity " +
                "u WHERE u.userid = :userid"),
        @NamedQuery(name = "DeployEntity.listByDesign", query = "SELECT u FROM DeployEntity " +
                        "u WHERE u.designid = :design"),
        @NamedQuery(name = "DeployEntity.listAll", query = "SELECT u FROM DeployEntity u"),
        @NamedQuery(name = "DeployEntity.deleteById", query = "DELETE FROM DeployEntity u WHERE u.id=:id")})
public class DeployEntity implements Serializable {
    
    
	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = 8853362187448691965L;

	@Id
//    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    private String id;
    
    @Column(name = "USERID")
    private long userid;
// TODO For now, we do not implement the relationships with JPA, rather do it by hand    
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="USERID")
//	private UserEntity userid;	

    @Column(name = "DESIGNID")
    private String designid;
// TODO For now, we do not implement the relationships with JPA, rather do it by hand    
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="DESIGNID")
//	private DesignEntity userid;
    
	@Basic(optional = false)
    @Column(name = "name")
    private String name;
	
	@Basic(optional = false)
    @Column(name = "complete")
    private boolean complete;
	
	@Basic(optional = true)
    @Column(name = "static")
    private String staticDeploy;
	
	@Basic(optional = true)
    @Column(name = "live")
    private String liveDeploy;
    
    
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length=1048576) 
	private byte[] xmlfile;
    
    public DeployEntity() {
    }

    public DeployEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    
    public long getUserid() {
        return userid;
    }

    public void setUserid(long u) {
        this.userid = u;
    }
    
    public String getName() {
    	return name;
    }
    
    public void setName(String name){
    	this.name = name;
    }
    
    public boolean getComplete() {
    	return complete;
    }
    
    public void setComplete(boolean complete){
    	this.complete = complete;
    }
    
    public String getStaticDeploy() {
    	return staticDeploy;
    }
    
    public void setStaticDeploy(String staticDeploy){
    	this.staticDeploy = staticDeploy;
    }
    
    public String getLiveDeploy() {
    	return liveDeploy;
    }
    
    public void setLiveDeploy(String liveDeploy){
    	this.liveDeploy = liveDeploy;
    }
    
	public byte[] getXmlfile() {
		return xmlfile;
	}
	 
	public void setXmlfile(byte[] xmlfile) {
		this.xmlfile = xmlfile;
	}

	public String getDesignid() {
		return designid;
	}

	public void setDesignid(String designid) {
		this.designid = designid;
	}

 

}

