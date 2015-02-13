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
 * JPA entity class for accessing tools data in the Internal Registry.
 * 
 * @author  David A. Velasco <davivel@gsic.uva.es>
 * @original juaase
 * @version 2012092501
 * @package glue.core.entities
 */

@Entity
@Table(name = "Tools")
@NamedQueries({
    @NamedQuery(name = "Tool.findAll", query = "SELECT g FROM Tool g"),
    @NamedQuery(name = "Tool.findById", query = "SELECT g FROM Tool g WHERE g.id = :id"),
    @NamedQuery(name = "Tool.findByName", query = "SELECT g FROM Tool g WHERE g.name = :name"),
    @NamedQuery(name = "Tool.findByDescription", query = "SELECT g FROM Tool g WHERE g.description = :description"),
    @NamedQuery(name = "Tool.findByDataFormat", query = "SELECT g FROM Tool g WHERE g.dataFormat = :dataFormat"),
    @NamedQuery(name = "Tool.findByUpdated", query = "SELECT g FROM Tool g WHERE g.updated = :updated")})
public class Tool implements Serializable {
	
	/// attributes - fields in glueTools table ///
	
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
    @Column(name = "description")
    private String description;
    
    @Basic(optional = false)
    @Column(name = "dataFormat")
    private String dataFormat;
    
    @Basic(optional = false)
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    
    /// constructors ///
    
    public Tool() {
    }

    public Tool(Integer id) {
        this.id = id;
    }

    public Tool(Integer id, String name, String description, String dataFormat, Date updated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dataFormat = dataFormat;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
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
        if (!(object instanceof Tool)) {
            return false;
        }
        Tool other = (Tool) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "glue.core.entities.Tool[id=" + id + "]";
    }

}
