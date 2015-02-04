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
@Table(name = "glueps_designs")
@NamedQueries({@NamedQuery(name = "DesignEntity.findById", query = "SELECT u FROM DesignEntity " +
        "u WHERE u.id = :id"),
        @NamedQuery(name = "DesignEntity.listByUser", query = "SELECT u FROM DesignEntity " +
                "u WHERE u.userid = :userid"),
        @NamedQuery(name = "DesignEntity.listAll", query = "SELECT u FROM DesignEntity u"),
        @NamedQuery(name = "DesignEntity.deleteById", query = "DELETE FROM DesignEntity u WHERE u.id=:id")})
public class DesignEntity implements Serializable {
    
    
 
	
	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = 8070237713793608670L;

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
    
	@Basic(optional = false)
    @Column(name = "name")
    private String name;
    
	@Basic(optional = false)
    @Column(name = "type")
    private String type;
    
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length=1048576) 
	private byte[] xmlfile;
    
    public DesignEntity() {
    }

    public DesignEntity(String id) {
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
    
    public String getType() {
    	return type;
    }
    
    public void setType(String type){
    	this.type = type;
    }
    
	public byte[] getXmlfile() {
		return xmlfile;
	}

	public void setXmlfile(byte[] xmlfile) {
		this.xmlfile = xmlfile;
	}

 

}

