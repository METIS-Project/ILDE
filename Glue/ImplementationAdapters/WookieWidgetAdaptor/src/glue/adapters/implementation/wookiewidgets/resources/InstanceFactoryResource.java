/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Wookie Widgets Adapter.
 * 
 * Wookie Widgets Adapter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Wookie Widgets Adapter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.adapters.implementation.wookiewidgets.resources;

import java.util.Map;
import java.util.List;

/**
 * Resource instance factory, responsible for the creation of Gluelet instances corresponding to a WookieWidget.
 * 
 * @author	 		David A. Velasco
 * @contributor 	Carlos Alario
 * @contributor		Javier Enrique Hoyos Torio
 * @version 		2012092501
 * @package 		glue.adapters.implementation.wookiewidgets.resources
 */

public class InstanceFactoryResource extends glue.common.resources.InstanceFactoryResource {

	/// methods ///

	@Override
	protected String checkMissingParameters(String toolName, List<String> users, String callerUser, Map<String, String> specificParams) {
		String result = "";
		if (toolName == null)
			result += "toolName, ";
		/*if (users == null)
			result += "users, ";*/
		if (callerUser == null)
			result += "callerUser, ";
		if (!specificParams.containsKey("feedURL"))
			result += "feedURL, ";
		if (!specificParams.containsKey("devKey"))
			result += "devKey, ";
		if (result.length() > 0) {
			result = result.substring(0, result.length() - 2);
			return result;
		}
		return null;
	}
	
}
