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

public class DeployVersionEntityPK implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -4435734520041701475L;

	private String deployid;
	
	private long version;
    
    public DeployVersionEntityPK() {
    	
    }

    public String getDeployid() {
        return deployid;
    }

    public void setDeployid(String deployid) {
        this.deployid = deployid;
    }
    
    public long getVersion() {
        return this.version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
    
    public int hashCode() {
        return (int)deployid.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof DeployVersionEntityPK)) return false;
        DeployVersionEntityPK pk = (DeployVersionEntityPK) obj;
        return pk.deployid.equals(this.deployid) && pk.version==this.version;
    }

}

