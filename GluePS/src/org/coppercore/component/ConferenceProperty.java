/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * CopperCore, an IMS-LD level C engine
 * Copyright (C) 2003 Harrie Martens and Hubert Vogten
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program (/license.txt); if not,
 * write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 *     Contact information:
 *     Open University of the Netherlands
 *     Valkenburgerweg 177 Heerlen
 *     PO Box 2960 6401 DL Heerlen
 *     e-mail: hubert.vogten@ou.nl or
 *             harrie.martens@ou.nl
 *
 *
 * Open Universiteit Nederland, hereby disclaims all copyright interest
 * in the program CopperCore written by
 * Harrie Martens and Hubert Vogten
 *
 * prof.dr. Rob Koper,
 * director of learning technologies research and development
 *
 */

package org.coppercore.component;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.exceptions.NotFoundException;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;

/**
 * The class represent the IMS LD Conference. A Conference inherits from
 * LocalPersonalContentProperty.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.1 $, $Date: 2008/04/28 08:00:36 $
 */
public class ConferenceProperty extends LocalPersonalContent {
	private static final long serialVersionUID = 42L;

	/**
	 * Constructor for creating this component.
	 * 
	 * @param uol
	 *            Uol the Uol in which this component was declared
	 * @param run
	 *            Run the Run to which this component belongs
	 * @param user
	 *            User the User to which this component belongs
	 * @param propId
	 *            String the identifier of this component as defined in IMS LD
	 * @throws PropertyException
	 *             when the constructor fails
	 */
	public ConferenceProperty(Uol uol, Run run, User user, String propId) throws PropertyException {
		super(uol, run, user, propId);
	}

	/**
	 * Returns the corresponding PropertyDefinition belonging to this component.
	 * 
	 * @throws PropertyException
	 *             when this operation fails
	 * @return PropertyDef the PropertyDefinition for this component
	 */
	protected PropertyDef findPropertyDef() throws PropertyException {
		return new ConferencePropertyDef(uolId, propId);
	}

	/**
	 * Recursively traverse this passed DOM node to seek any completed
	 * attributes. If found, replace its value with the value in the dossier for
	 * the current user. Nodes will be ordered according to the IMS LD rules and
	 * invisible nodes and incomplete acts are filtered out.
	 * 
	 * @param anUol
	 *            Uol the Uol which defined this activity tree
	 * @param aRun
	 *            Run the Run for which this activity tree is retrieved
	 * @param aUser
	 *            User for which this activity is personalized
	 * @param element
	 *            Element the node of the activity tree to be evaluated
	 * @param webroot
	 *            String the offset into URL where the content was stored
	 * @return LocalPersonalContent representing the personalized version of
	 *         this node
	 */
	protected boolean personalizeElement(Uol anUol, Run aRun, User aUser, Element element, String webroot) {
		boolean result = super.personalizeElement(anUol, aRun, aUser, element, webroot);
		String nodeName = element.getNodeName();

		if (result
				&& ("participant".equals(nodeName) || "observer".equals(nodeName)
						|| "conference-manager".equals(nodeName) || "moderator".equals(nodeName))) {
			Collection users = User.findByRoleId(runId, element.getAttribute("identifier"));

			Iterator myIterator = users.iterator();
			while (myIterator.hasNext()) {
				String userId = ((User) myIterator.next()).getId();
				Element newUserNode = element.getOwnerDocument().createElement("user");
				newUserNode.setAttribute("user-id", userId);
				element.appendChild(newUserNode);
			}
		}

		return result;
	}
}
