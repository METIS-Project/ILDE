/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Note Flight Adapter.
 * 
 * Note Flight Adapter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Note Flight Adapter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.adapters.implementation.noteflight.resources;

import java.util.Map;
import java.util.List;

/**
 * Resource instance factory, responsible for the creation of Gluelet instances corresponding to a Dabbleboard.
 * 
 * @author	 		David A. Velasco
 * @contributor 	Carlos Alario
 * @version 		2011020700
 * @package 		glue.adapters.implementation.dabbleboard.resources
 */

public class InstanceFactoryResource extends glue.common.resources.InstanceFactoryResource {

	/**
	 * 
	 */
	@Override
	protected String checkMissingParameters(String toolName, List<String> users, String callerUser, Map<String, String> specificParams) {
		String result = "";
		if (toolName == null)
			result += "toolName, ";
		if (callerUser == null)
			result += "callerUser, ";
		if (users == null || users.size() <= 0)
			result += "users, ";
		if (!specificParams.containsKey("feedURL"))
			result += "feedURL (specific parameter), ";
		if (!specificParams.containsKey("key"))
			result += "key (specific parameter), ";
		if (!specificParams.containsKey("secret"))
			result += "secret (specific parameter), ";
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 2);
			return result;
		}
		return null;
	}

}
