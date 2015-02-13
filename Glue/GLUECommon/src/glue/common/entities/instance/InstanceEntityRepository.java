/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of GlueletCommon.
 * 
 * GlueletCommon is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * GlueletCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

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
