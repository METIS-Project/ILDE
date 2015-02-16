/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Dabbleboard Adapter.
 * 
 * Dabbleboard Adapter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Dabbleboard Adapter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.adapters.implementation.dabbleboard.resources;

import java.util.List;
import java.util.Map;

/**
 * Resource adapter instance.
 * 
 * @author	 		David A. Velasco
 * @contributor 	Javier Enrique Hoyos Torio
 * @contributor 	Carlos Alario
 * @version 		2012092501
 * @package 		glue.adapters.implementation.dabbleboard.resources
 */

public class InstanceResource extends glue.common.resources.InstanceResource {
	
	/// methods ///
	
	@Override
	protected String checkMissingParametersInDelete(Map<String, String> specificParams) {
		String result = "";
		if (!specificParams.containsKey("user"))
			result += "user, ";
		if (!specificParams.containsKey("pass"))
			result += "pass, ";
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 2);
			return result;
		}
		return null;
	}


	/*
	@Override
	protected String checkMissingParametersInGet(Map<String, String> specificParams) {
		String result = "";
		if (!specificParams.containsKey("user"))
			result += "user, ";
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 2);
			return result;
		}
		return null;
	}
	*/


	@Override
	protected String checkMissingParametersInPost(String callerUser, List<String> users, Map<String, String> specificParams) {
		String result = "";
		if (callerUser == null)
			result += "callerUser, ";
		/*if (users == null || users.size() <= 0)
			result += "users, ";*/
		if (!specificParams.containsKey("user"))
			result += "user (specific parameter), ";
		if (!specificParams.containsKey("pass"))
			result += "pass (specific parameter), ";
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 2);
			return result;
		}
		return null;
	}
    
}
