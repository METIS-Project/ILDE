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

