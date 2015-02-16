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

package glue.adapters.implementation.wookiewidgets.entities;

import java.util.Date;
import java.util.Map;
import java.util.List;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Element;

import glue.common.entities.instance.InstanceEntity;
import glue.common.entities.instance.InstanceEntityFactory;

public class WookieWidgetsFactory implements InstanceEntityFactory {

	@Override
	public InstanceEntity createLoadedInstanceEntity(int index, String title, Date updated) {
		return new WookieWidgets(index, title, updated);
	}

	@Override
	public InstanceEntity createNewInstanceEntity(String toolName, List<String> users, String callerUser, Map<String, String> specificParams, Element configuration) {
		// select the tool to use
		String guidURL = null;
		if (toolName.equals("Chat Widget"))
			guidURL = WookieWidgets.CHAT_GUID;
		else if (toolName.equals("SharedDraw Widget"))
			guidURL = WookieWidgets.SHAREDDRAW_GUID;
		else if (toolName.equals("Sudoku Widget"))
			guidURL = WookieWidgets.SUDOKU_GUID;
		else if (toolName.equals("Forum Widget"))
			guidURL = WookieWidgets.FORUM_GUID;
		else if (toolName.equals("Natter Chat Widget"))
			guidURL = WookieWidgets.NATTER_GUID;
		else if (toolName.equals("Bubble Game Widget"))
			guidURL = WookieWidgets.BUBBLE_GUID;
		else if (toolName.equals("Butterfly Paint Widget"))
			guidURL = WookieWidgets.BUTTERFLY_GUID;
		// Specific configuration for YouDecideWidget
		else if (toolName.equals("You Decide Widget")){
			String question = "";
			String[] answers = {"","","","",""};
			guidURL = WookieWidgets.YOUDECIDE_GUID;
			try {
				question = configuration.getElementsByTagNameNS("*", "question-input").item(0).getTextContent();		// must be synchronized with YouDecideWidgetConfiguration.html ; a little too hardcoded
				answers[0] = configuration.getElementsByTagNameNS("*", "answer-1").item(0).getTextContent();	// must be synchronized with YouDecideWidgetConfiguration.html  ; a little too hardcoded
				answers[1] = configuration.getElementsByTagNameNS("*", "answer-2").item(0).getTextContent();	// must be synchronized with YouDecideWidgetConfiguration.html  ; a little too hardcoded
				answers[2] = configuration.getElementsByTagNameNS("*", "answer-3").item(0).getTextContent();	// must be synchronized with YouDecideWidgetConfiguration.html  ; a little too hardcoded
				answers[3] = configuration.getElementsByTagNameNS("*", "answer-4").item(0).getTextContent();	// must be synchronized with YouDecideWidgetConfiguration.html  ; a little too hardcoded
				answers[4] = configuration.getElementsByTagNameNS("*", "answer-5").item(0).getTextContent();	// must be synchronized with YouDecideWidgetConfiguration.html  ; a little too hardcoded
			} catch (RuntimeException re) {
				throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Unexpected fail while reading entity body", re);
			}
			return new WookieWidgets(callerUser, users, specificParams.get("feedURL"), guidURL, question, answers);
		}
		else {
			throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Unkown tool parameter");
		}
		
		return new WookieWidgets(callerUser, users, specificParams.get("feedURL"), guidURL);
	}

}
