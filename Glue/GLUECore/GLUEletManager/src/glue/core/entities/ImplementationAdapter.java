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
 * JPA entity class for accessing implementation adapters data in the Internal Registry.
 * 
 * @author  David A. Velasco <davivel@gsic.uva.es>
 * @original juaase
 * @version 2012092501
 * @package glue.core.entities
 */

@Entity
@Table(name = "ImplementationAdapters")
@NamedQueries({
    @NamedQuery(name = "ImplementationAdapter.findAll", query = "SELECT g FROM ImplementationAdapter g"),
    @NamedQuery(name = "ImplementationAdapter.findById", query = "SELECT g FROM ImplementationAdapter g WHERE g.id = :id"),
    @NamedQuery(name = "ImplementationAdapter.findByName", query = "SELECT g FROM ImplementationAdapter g WHERE g.name = :name"),
    @NamedQuery(name = "ImplementationAdapter.findByUrl", query = "SELECT g FROM ImplementationAdapter g WHERE g.url = :url"),
    @NamedQuery(name = "ImplementationAdapter.findByUpdated", query = "SELECT g FROM ImplementationAdapter g WHERE g.updated = :updated")})

public class ImplementationAdapter implements Serializable {
	
	/// attributes - fields in ImplementationAdapters table ///
	
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
    @Column(name = "url")
    private String url;
    
    @Basic(optional = false)
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    
    /// constructors ///
    
    public ImplementationAdapter() {
    }

    public ImplementationAdapter(Integer id) {
        this.id = id;
    }

    public ImplementationAdapter(Integer id, String name, String url, Date updated) {
        this.id = id;
        this.name = name;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        if (!(object instanceof ImplementationAdapter)) {
            return false;
        }
        ImplementationAdapter other = (ImplementationAdapter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "glue.core.entities.ImplementationAdapter[id=" + id + "]";
    }

}
