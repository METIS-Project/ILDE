package glueps.core.gluepsManager;

import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.UserEntity;

import org.restlet.security.LocalVerifier;

public class GLUEPSManagerDummyVerifier extends LocalVerifier {

	public GLUEPSManagerDummyVerifier(){
		
		//manager = JpaManager.getInstance();
		
	}
	
	
	@Override
	/**
	 * Returns the local secret associated to a given identifier.
	 */
    public char[] getLocalSecret(String identifier) {	
        return null;
    }


	@Override
	/**
	 * Verifies that the identifier/secret couple is valid
	 */
	public boolean verify(String identifier, char[] secret) {
		if (identifier.equals("logout") && String.valueOf(secret).equals("logout")){
			return true;
		}else{
			return false;
		}
	}
	
	
}
