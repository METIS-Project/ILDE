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
package glue.adapters.implementation.basiclti.entities;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Element;

import glue.common.entities.instance.InstanceEntity;
import glue.common.entities.instance.InstanceEntityFactory;

/**
 * Entity representing a BasicLTI Factory.
 * 
 * @author  	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.adapters.implementation.basiclti.entities
 */
public class BasicLTIFactory implements InstanceEntityFactory{

	@Override
	public InstanceEntity createNewInstanceEntity(String toolName,
			List<String> users, String callerUser,
			Map<String, String> specificParams, Element configuration) {
		// TODO Auto-generated method stub
		String title, description;
		try {
			title = configuration.getElementsByTagNameNS("*", "title").item(0).getTextContent();		// must be synchronized with BasicLTIConfiguration.html ; a little too hardcoded
			description = configuration.getElementsByTagNameNS("*", "description").item(0).getTextContent();	// must be synchronized with BasicLTIConfiguration.html ; a little too hardcoded
		} catch (RuntimeException re) {
			throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Unexpected fail while reading entity body", re);
		}

		//We need to make sure the content is not empty. Otherwise the signature checking by BASICLTI will fail.
		//A blank space is enough
		if (title.length() == 0)
		{
			title = " ";
		}
		if (description.length() == 0)
		{
			description = " ";
		}
		return new BasicLTI(title, description, callerUser, specificParams.get("feedURL"));
	}

	@Override
	public InstanceEntity createLoadedInstanceEntity(int index, String title,
			Date updated) {
		return new BasicLTI(index, title, updated);
	}

}
