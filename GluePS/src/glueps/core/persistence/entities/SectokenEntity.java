package glueps.core.persistence.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "glueps_sectokens")
        @NamedQueries({@NamedQuery(name = "SectokenEntity.findById", query = "SELECT u FROM SectokenEntity " +
        "u WHERE u.id = :id"),
        @NamedQuery(name = "SectokenEntity.findBySectoken", query = "SELECT u FROM SectokenEntity " +
        "u WHERE u.sectoken = :sectoken"), 
        @NamedQuery(name = "SectokenEntity.listAll", query = "SELECT u FROM SectokenEntity u "),
        @NamedQuery(name = "SectokenEntity.deleteById", query = "DELETE FROM SectokenEntity u WHERE u.id=:id")
        })
public class SectokenEntity implements Serializable {
    
    
    /**
	 * Auto-generated uid
	 */
	private static final long serialVersionUID = -9204808581482158829L;

	@Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    
	@Basic(optional = false)
    @Column(name = "sectoken")
    private String sectoken;
    
    public SectokenEntity() {
    }

    public SectokenEntity(String id) {
        this.id = id;
    }
    
    public SectokenEntity(String id, String sectoken) {
        this.id = id;
        this.sectoken = sectoken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSectoken() {
        return sectoken;
    }

    public void setSectoken(String sectoken) {
        this.sectoken = sectoken;
    }
   

}

