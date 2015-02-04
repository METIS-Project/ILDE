package glueps.core.persistence.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "glueps_users")
        @NamedQueries({@NamedQuery(name = "UserEntity.findById", query = "SELECT u FROM UserEntity " +
        "u WHERE u.id = :id"), 
        @NamedQuery(name = "UserEntity.findByLogin", query = "SELECT u FROM UserEntity u " +
        "WHERE u.login = :login"), 
        @NamedQuery(name = "UserEntity.findByPassword", query = "SELECT u FROM UserEntity u " +
        "WHERE u.password = :password")})
public class UserEntity implements Serializable {
    
    
    /**
	 * Auto-generated uid
	 */
	private static final long serialVersionUID = -9204808581482158829L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "ID", nullable = false)
    private long id;
    
    @Column(name = "LOGIN")
    private String login;
    
    @Column(name = "PASSWORD")
    private String password;
    
    public UserEntity() {
    }

    public UserEntity(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   

}

