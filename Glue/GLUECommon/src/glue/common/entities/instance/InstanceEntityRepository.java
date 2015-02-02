/**
 This file is part of GLUECommon.

 GLUECommon is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GLUECommon is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GLUECommon for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GLUECommon is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GLUECommon and/or modify it under the
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
package glue.common.entities.instance;

import java.util.List;
import java.util.Map;


public interface InstanceEntityRepository {

	/**
	 * Getter for existing GLUElet instances.
	 * 
	 * @param 	index	int 									Local (implementation adapter) identifier corresponding to the desired document. 
	 * @return			InstanceEntity		Immediately accesible data from the instance, or null if index is not valid.
	 */
	public InstanceEntity getInstanceEntity(int index);
	
	
	public int addInstanceEntity(InstanceEntity instanceEntity);
	
	public void updateInstanceUsers(int index, List<String> users, String callerUser, Map<String, String> specificParams);
	//public int updateInstanceUsers(InstanceEntity instanceEntity, List<String> users, String callerUser, Map<String, String> specificParams);
	
	public void deleteInstanceEntity(int index);
	
	
	public boolean saveEntities();

	public InstanceEntityFactory getInstanceEntityFactory();
	
}
