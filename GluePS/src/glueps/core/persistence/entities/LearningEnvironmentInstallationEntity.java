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
    @Column(name = "type")
    private String type;
    
	@Basic(optional = false)
    @Column(name = "accessLocation")
    private String accessLocation;
	
    @Basic(optional = true)
    @Column(name = "parameters")
    private String parameters;
    
    @Basic(optional = false)
    @Column(name = "sectype")
    private long sectype;
    
    public LearningEnvironmentInstallationEntity() {
    }

    public LearningEnvironmentInstallationEntity(long id) {
        this.id = id;
    }
    
    public LearningEnvironmentInstallationEntity(long id, String name, String type, String accessLocation) {
        this.id = id;
        this.name = name;
        this.type = type;
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

    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
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
   

}

