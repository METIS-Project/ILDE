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
 * JPA entity class for accessing tool implementations data in the Internal Registry.
 * 
 * @author  David A. Velasco <davivel@gsic.uva.es>
 * @original juaase
 * @version 2012092501
 * @package glue.core.entities
 */

@Entity
@Table(name = "ToolImplementations")
@NamedQueries({
    @NamedQuery(name = "ToolImplementation.findAll", query = "SELECT g FROM ToolImplementation g"),
    @NamedQuery(name = "ToolImplementation.findById", query = "SELECT g FROM ToolImplementation g WHERE g.id = :id"),
    @NamedQuery(name = "ToolImplementation.findByToolId", query = "SELECT g FROM ToolImplementation g WHERE g.toolId = :toolId"),
    @NamedQuery(name = "ToolImplementation.findByModelId", query = "SELECT g FROM ToolImplementation g WHERE g.modelId = :modelId"),
    @NamedQuery(name = "ToolImplementation.findByToolServiceId", query = "SELECT g FROM ToolImplementation g WHERE g.toolServiceId = :toolServiceId"),
    @NamedQuery(name = "ToolImplementation.findByUpdated", query = "SELECT g FROM ToolImplementation g WHERE g.updated = :updated")})
public class ToolImplementation implements Serializable {
	
	/// attributes - fields in glueTools table ///
	
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "toolId")
    private Integer toolId;
    
    @Basic(optional = false)
    @Column(name = "modelId")
    private Integer modelId;
    
    @Basic(optional = false)
    @Column(name = "toolServiceId")
    private Integer toolServiceId;
    
    @Basic(optional = false)
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    
    @Basic(optional = true)
    @Column(name = "parameters")
    private String parameters;
    
    /// constructors ///
    
    public ToolImplementation() {
    }

    public ToolImplementation(Integer id) {
        this.id = id;
    }

    public ToolImplementation(Integer id, Integer toolId, Integer modelId, Integer toolServiceId, Date updated, String parameters) {
        this.id = id;
        this.toolId = toolId;
        this.modelId = modelId;
        this.toolServiceId = toolServiceId;
        this.updated = updated;
        this.parameters = parameters;
    }


    /// Getters and setters for every attribute / field ///

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getToolId() {
        return toolId;
    }

    public void setToolId(Integer toolId) {
        this.toolId = toolId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public int getToolServiceId() {
        return toolServiceId;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
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
        if (!(object instanceof ToolImplementation)) {
            return false;
        }
        ToolImplementation other = (ToolImplementation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "glue.core.entities.ToolImplementation[id=" + id + "]";
    }
    
}
