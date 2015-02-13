/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of GlueletManager.
 * 
 * GlueletManager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * GlueletManager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.core.entities;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * JPA entity class for accessing a implementation models data in the Internal Registry.
 * 
 * @author  David A. Velasco <davivel@gsic.uva.es>
 * @original juaase
 * @version 2012092501
 * @package glue.core.entities
 */

@Entity
@Table(name = "ImplementationModels")		// binding to table in database !!!
@NamedQueries({
    @NamedQuery(name = "ImplementationModel.findAll", query = "SELECT g FROM ImplementationModel g"),
    @NamedQuery(name = "ImplementationModel.findById", query = "SELECT g FROM ImplementationModel g WHERE g.id = :id"),
    @NamedQuery(name = "ImplementationModel.findByName", query = "SELECT g FROM ImplementationModel g WHERE g.name = :name"),
    @NamedQuery(name = "ImplementationModel.findByAdapterId", query = "SELECT g FROM ImplementationModel g WHERE g.adapterId = :adapterId"),
    @NamedQuery(name = "ImplementationModel.findByUpdated", query = "SELECT g FROM ImplementationModel g WHERE g.updated = :updated")})
public class ImplementationModel implements Serializable {
	
	/// attributes - fields in glueToolModels table ///
	
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    
    @Basic(optional = false)
    @Column(name = "adapterId")
    private Integer adapterId;

    @Basic(optional = false)
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    
    /// constructors ///
    
    public ImplementationModel() {
    }

    public ImplementationModel(Integer id) {
        this.id = id;
    }

    public ImplementationModel(Integer id, String name, Integer adapterId, Date updated) {
        this.id = id;
        this.name = name;
        this.adapterId = adapterId;
        this.updated = updated;
    }
    

    /// Getters and setters for every attribute / field ///

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAdapterId() {
        return adapterId;
    }

    public void setAdapterId(Integer adapterId) {
        this.adapterId = adapterId;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    
    /// other methods ///
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ImplementationModel)) {
            return false;
        }
        ImplementationModel other = (ImplementationModel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "glue.core.entities.ImplementationModel[id=" + id + "]";
    }

}
