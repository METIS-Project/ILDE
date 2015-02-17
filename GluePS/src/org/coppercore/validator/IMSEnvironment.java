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
 * Copyright (C) 2003, 2004 Harrie Martens and Hubert Vogten
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

package org.coppercore.validator;

import java.util.ArrayList;

import org.coppercore.common.Parser;
import org.coppercore.exceptions.SemanticException;
import org.w3c.dom.Node;

/**
 * @author Hubert Vogten
 * 
 * @version $Revision: 1.1 $, $Date: 2008/11/18 16:52:39 $, $Author: hvo $
 */
public class IMSEnvironment extends IMSObject {

	/**
	 * Instantiates a new iMS environment.
	 */
	public IMSEnvironment() {
		// TODO Auto-generated method stub
	}
	
	 protected void initialize(Node anItemNode, IMSLDManifest aManifest) {
		    super.initialize(anItemNode, aManifest);
		    setKey("identifier");
	 }	    

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.coppercore.validator.IMSObject#isValid()
	 */
	protected void isValid() throws SemanticException {
	    if (isCircular(new ArrayList())) {
	        throw new SemanticException("Circular reference for " + toString());
	      }	}

	private boolean isCircular(ArrayList items) {
		boolean result = false;
		Node child = node.getFirstChild();

		while (!result && child != null) {

			if ((child.getNodeType() == Node.ELEMENT_NODE) && child.getLocalName().equals("environment-ref")
					&& child.getNamespaceURI().equals(org.coppercore.common.Parser.IMSLDNS)) {
				String identifierref = Parser.getNamedAttribute(child,"ref");
				IMSObject referencedObject = manifest.findIMSObject(identifierref);
				if (referencedObject instanceof IMSEnvironment) {
					if (!items.contains(referencedObject)) {
						items.add(referencedObject);
						result = ((IMSEnvironment) referencedObject).isCircular(items);
					} else {
						result = true;
					}
				}
			}
			child = child.getNextSibling();
		}
		return result;
	}

}
