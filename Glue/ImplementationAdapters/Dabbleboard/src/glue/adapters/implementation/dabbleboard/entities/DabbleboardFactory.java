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

package glue.adapters.implementation.dabbleboard.entities;

import java.util.Date;
import java.util.Map;
import java.util.List;

import org.w3c.dom.Element;

import glue.common.entities.instance.InstanceEntity;
import glue.common.entities.instance.InstanceEntityFactory;

/**
 * Entity representing a Dabbleboard Factory.
 * 
 * @author  	Carlos Alario
 * @contibutor	Javier Enrique Hoyos Torio
 * @contibutor	David A. Velasco
 * @version		2012092501
 * @package 	glue.adapters.implementation.dabbleboard.entities
 */
public class DabbleboardFactory implements InstanceEntityFactory {

	@Override
	public InstanceEntity createLoadedInstanceEntity(int index, String title, Date updated) {
		return new Dabbleboard(index, title, updated);
	}

	@Override
	public InstanceEntity createNewInstanceEntity(String toolName, List<String> users, String callerUser, Map<String, String> specificParams, Element configuration) {
		return new Dabbleboard(callerUser, users, specificParams.get("feedURL"), specificParams.get("user"));
	}

}
