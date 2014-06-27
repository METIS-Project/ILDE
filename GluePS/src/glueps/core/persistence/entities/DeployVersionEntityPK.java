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

