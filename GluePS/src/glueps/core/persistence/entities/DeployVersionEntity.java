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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@IdClass(DeployVersionEntityPK.class)
@Entity
@Table(name = "glueps_deploy_versions")
@NamedQueries({@NamedQuery(name = "DeployVersionEntity.findById", query = "SELECT u FROM DeployVersionEntity " +
        "u WHERE u.deployid = :deployid AND u.version = :version"),
        @NamedQuery(name = "DeployVersionEntity.findByDeployid", query = "SELECT u FROM DeployVersionEntity " +
        "u WHERE u.deployid = :deployid"),
        @NamedQuery(name = "DeployVersionEntity.findByDeployidNext", query = "SELECT u FROM DeployVersionEntity " +
        "u WHERE u.deployid = :deployid AND u.next = :next"),
        @NamedQuery(name = "DeployVersionEntity.findLastVersionDeploy", query = "SELECT max(u.version) FROM DeployVersionEntity " +
                "u WHERE u.deployid = :deployid"),
        @NamedQuery(name = "DeployVersionEntity.findLastValidVersionDeploy", query = "SELECT max(u.version) FROM DeployVersionEntity " +
                "u WHERE u.deployid = :deployid AND u.valid=1"),
        @NamedQuery(name = "DeployVersionEntity.findUndoVersionDeploy", query = "SELECT max(u.version) FROM DeployVersionEntity " +
                "u WHERE u.deployid = :deployid AND u.valid=1" + " AND u.version < (SELECT max(u2.version) FROM DeployVersionEntity u2 WHERE u2.deployid = :deployid AND u2.valid=1)"),
        @NamedQuery(name = "DeployVersionEntity.updateValidDeploy", query = "UPDATE DeployVersionEntity u SET u.valid = :valid " +
                "WHERE u.deployid = :deployid AND u.version = :version"),
        @NamedQuery(name = "DeployVersionEntity.updateNextDeploy", query = "UPDATE DeployVersionEntity u SET u.next = :next " +
                "WHERE u.deployid = :deployid AND u.version = :version"),
        @NamedQuery(name = "DeployVersionEntity.deleteById", query = "DELETE FROM DeployVersionEntity u " +
        		"WHERE u.deployid = :deployid AND u.version = :version"),
        @NamedQuery(name = "DeployVersionEntity.deleteByDeployid", query = "DELETE FROM DeployVersionEntity u " +
        		"WHERE u.deployid = :deployid")       		
                })
        
public class DeployVersionEntity implements Serializable {
    
    
    public DeployVersionEntity() {
    	
    }

	/**
	 * Auto-generated UID
	 */
	private static final long serialVersionUID = 8853362187448691965L;

	@Id
    @Basic(optional = false)
    @Column(name = "DEPLOYID")
    private String deployid;
    
	@Id
	@Basic(optional = false)
    @Column(name = "VERSION")
    private long version;

    @Column(name = "VALID")
    private boolean valid;
    
    @Column(name = "UNDOALERT")
    private boolean undoalert;
    
    @Column(name= "NEXT")
    private long next;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(length=1048576) 
	private byte[] xmlfile;

    public DeployVersionEntity(String deployid, long version ) {
    	this.deployid = deployid;
    	this.version = version;
    	this.valid = true;
    	this.undoalert = false;
    	this.next = 0;
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
    
    public boolean getValid() {
    	return this.valid;
    }
    
    public void setValid(boolean valid) {
    	this.valid = valid;
    }
    
    public boolean getUndoalert() {
    	return this.undoalert;
    }
    
    public void setUndoalert(boolean undoalert) {
    	this.undoalert= undoalert;
    }
    
    public long getNext() {
    	return this.next;
    }
    
    public void setNext(long next) {
    	this.next = next;
    }
    
	public byte[] getXmlfile() {
		return xmlfile;
	}

	public void setXmlfile(byte[] xmlfile) {
		this.xmlfile = xmlfile;
	}

 

}

