/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Doodle Adapter.
 * 
 * Doodle Adapter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Doodle Adapter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.adapters.implementation.doodle.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.List;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Element;

import glue.common.entities.instance.InstanceEntity;
import glue.common.entities.instance.InstanceEntityFactory;

/**
 * Factory for Doodle object.
 * 
 * @author  	Carlos Alario
 * @contributor Javier Enrique Hoyos Torio
 * @contributor	David A. Velasco Villanueva
 * @version 	2012092501
 * @package 	glue.adapters.implementation.doodle.entities
 */
public class DoodleFactory implements InstanceEntityFactory {

	@Override
	public InstanceEntity createLoadedInstanceEntity(int index, String title, Date updated) {
		return new Doodle(index, title, updated);
	}

	@Override
	public InstanceEntity createNewInstanceEntity(String toolName, List<String> users, String callerUser, Map<String, String> specificParams, Element configuration) {
		String title, description;
		ArrayList<String> options = new ArrayList<String>();
		try {
			title = configuration.getElementsByTagNameNS("*", "title").item(0).getTextContent();		// must be synchronized with DoodleConfiguration.html ; a little too hardcoded
			description = configuration.getElementsByTagNameNS("*", "description").item(0).getTextContent();	// must be synchronized with DoodleConfiguration.html ; a little too hardcoded
		
			for (int i = 0; i < 10; i++){
				String option = new String (configuration.getElementsByTagNameNS("*", "options-"+i).item(0).getTextContent()); // must be synchronized with DoodleConfiguration.html ; a little too hardcoded
				if (! (option.isEmpty() && option.equals("")) ){
					options.add(option);
				}
			}

		} catch (RuntimeException re) {
			throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Unexpected fail while reading entity body", re);
		}
		return new Doodle(title, description, options, callerUser, specificParams.get("feedURL"));
	}

}
