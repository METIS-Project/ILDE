/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package glueps.core.persistence.entities;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "glueps_oauth_tokens")
        @NamedQueries({@NamedQuery(name = "OauthTokenEntity.findById", query = "SELECT u FROM OauthTokenEntity " +
        "u WHERE u.id = :id"), 
        @NamedQuery(name = "OauthTokenEntity.findByLeid", query = "SELECT u FROM OauthTokenEntity " +
        "u WHERE u.leid = :leid"), 
        @NamedQuery(name = "OauthTokenEntity.listAll", query = "SELECT u FROM OauthTokenEntity u "),
        @NamedQuery(name = "OauthTokenEntity.deleteById", query = "DELETE FROM OauthTokenEntity u WHERE u.id=:id")
        })
public class OauthTokenEntity implements Serializable {
    
    
    /**
	 * Auto-generated uid
	 */
	private static final long serialVersionUID = -9104808581482158829L;

	@Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private long id;
    
	@Basic(optional = false)
    @Column(name = "leid")
    private long leid;
    
	@Basic(optional = false)
	@Column(name = "accessToken")
	private String accessToken;
	
	@Column(name = "tokenType")
	private String tokenType;
	
	@Basic(optional = false)
	@Column(name = "created")
	private long created;
	
	@Column(name = "expiration")
	private long expiration;
	
    public OauthTokenEntity() {
    }
    
    public OauthTokenEntity(long leid, String accessToken) {
    	this.id = 0;
        this.leid = leid;
        this.accessToken = accessToken;
        this.created = new Date().getTime();
    }
    
    public OauthTokenEntity(long id, long leid, String accessToken) {
        this.id = id;
        this.leid = leid;
        this.accessToken = accessToken;
        this.created = new Date().getTime();
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLeid() {
		return leid;
	}

	public void setLeid(long leid) {
		this.leid = leid;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

   

}

