/**
 This file is part of NoteFlightAdapter.

 NoteFlightAdapter is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 NoteFlightAdapter is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use NoteFlightAdapter for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when NoteFlightAdapter is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute NoteFlightAdapter and/or modify it under the
   terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>
 ]
*/
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
