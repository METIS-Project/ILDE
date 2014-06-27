package glueps.core.gluepsManager;

import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.UserEntity;

import org.restlet.security.LocalVerifier;

public class GLUEPSManagerVerifier extends LocalVerifier {

	private static JpaManager manager = null;
	
	
	public GLUEPSManagerVerifier(){
		
		manager = JpaManager.getInstance();
		
	}
	
	
	@Override
    public char[] getLocalSecret(String identifier) {
		
		// TODO Lookup in the database
		UserEntity user = manager.findUserByUsername(identifier);
		
		if(user!=null) return user.getPassword().toCharArray();
		
        return null;
    }
	
}
