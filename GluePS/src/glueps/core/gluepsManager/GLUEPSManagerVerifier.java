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
