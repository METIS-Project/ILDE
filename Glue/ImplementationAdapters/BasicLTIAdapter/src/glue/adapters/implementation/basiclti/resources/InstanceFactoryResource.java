/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Basic LTI Adapter.
 * 
 * Basic LTI Adapter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Basic LTI Adapter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.adapters.implementation.basiclti.resources;

import java.util.List;
import java.util.Map;

import org.restlet.data.Reference;

/**
 * Resource instance factory, responsible for the creation of Gluelet instances corresponding to a BasicLTI.
 * 
 * @author  	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.adapters.implementation.basiclti.resources
 */
public class InstanceFactoryResource extends glue.common.resources.InstanceFactoryResource{

	@Override
	protected String checkMissingParameters(String toolName,List<String> users, String callerUser, Map<String, String> specificParams) {
		String result = "";
		if (toolName == null)
			result += "toolName, ";
		if (callerUser == null)
			result += "callerUser, ";
		/*if (users == null || users.size() <= 0)
			result += "users, ";*/
		if (!specificParams.containsKey("key"))
			result += "key, ";
		if (!specificParams.containsKey("pass"))
			result += "pass, ";
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 2);
			return result;
		}
		
		//We need to provide the reference URL to the create method
		Reference ref = this.getReference();
		String refSt = ref.getIdentifier();
		specificParams.put("refURL", refSt.substring(0, refSt.indexOf(ref.getLastSegment())));
		
		return null;
	}

}
