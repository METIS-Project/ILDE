/**
 This file is part of GlueletManager.

 GlueletManager is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GlueletManager is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GlueletManager for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GlueletManager is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GlueletManager and/or modify it under the
   terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>
 ]
*/
package glue.core.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * JPA entity class for accessing GLUElet instances data in Gluelets/Instances Repository.
 * 
 * @author  David A. Velasco <davivel@gsic.uva.es>
 * @original juaase
 * @version 2012092501
 * @package glue.core.entities
 */

@Entity
@Table(name = "Gluelets")		// binding to table in database !!!
@NamedQueries({
    @NamedQuery(name = "Gluelet.findAll", query = "SELECT g FROM Gluelet g"),
    @NamedQuery(name = "Gluelet.findByGlueletId", query = "SELECT g FROM Gluelet g WHERE g.id = :id"),
    @NamedQuery(name = "Gluelet.findByGlueToolId", query = "SELECT g FROM Gluelet g WHERE g.url = :url"),
    @NamedQuery(name = "Gluelet.findByGlueletParameters", query = "SELECT g FROM Gluelet g WHERE g.toolImplementationId = :toolImplementationId"),
    @NamedQuery(name = "Gluelet.findByGlueletAccess", query = "SELECT g FROM Gluelet g WHERE g.vle = :vle"),
    @NamedQuery(name = "Gluelet.findByUpdated", query = "SELECT g FROM Gluelet g WHERE g.updated = :updated"),
    @NamedQuery(name = "Gluelet.findMaxId", query = "SELECT MAX(g.id) FROM Gluelet g")})
public class Gluelet implements Serializable {
	
	/// attributes - fields in gluelets table ///
	
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    public Integer id;
    
    @Basic(optional = false)
    @Column(name = "url")
    private String url;
    
    @Basic(optional = false)
    @Column(name = "toolImplementationId")
    private Integer toolImplementationId;
    
    @Basic(optional = false)
    @Column(name = "vle")
    private String vle;
    
    @Basic(optional = false)
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Basic(optional = true)
    @Column(name = "parameters")
    private String parameters;


    /// constructors ///
    
    public Gluelet() {
    }

    public Gluelet(Integer id) {
        this.id = id;
    }

    public Gluelet(Integer id, String url, Integer toolImplementationId, String vle, Date updated, String parameters) {
        this.id = id;
        this.url = url;
        this.toolImplementationId = toolImplementationId;
        this.vle = vle;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getToolImplementationId() {
        return toolImplementationId;
    }

    public void setToolImplementationId(int toolImplementationId) {
        this.toolImplementationId = toolImplementationId;
    }

    public String getVle() {
        return vle;
    }

    public void setVle(String vle) {
        this.vle = vle;
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
        if (!(object instanceof Gluelet)) {
            return false;
        }
        Gluelet other = (Gluelet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "glue.core.entities.Gluelet[id=" + id + "]";
    }

}
