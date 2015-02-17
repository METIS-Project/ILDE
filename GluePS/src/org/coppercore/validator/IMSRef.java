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

package org.coppercore.validator;

import java.util.ArrayList;

import org.coppercore.exceptions.SemanticException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This abstract class represents a reference to any ims learning design element.<p>
 * The subclasses implement references to actual ims learning design elements.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2005/01/11 13:15:02 $
 */
public abstract class IMSRef
    extends IMSObject {

  /** Contains the id of the element this reference is pointing at. */
  protected String idref;

  /** List containing the types of all elements this reference is allowed to point at. */
  protected ArrayList refersTo = new ArrayList();

  /** is the ims learning design type of the refering element. */
  protected String referingNode;

  public IMSRef() {
    // default constructor
  }

  protected void initialize(Node aNode, IMSLDManifest aManifest) {
    super.initialize(aNode, aManifest);

    idref = getNamedAttribute("ref");
  }

  /**
   * Returns the list of all ims learning design element types this reference might be pointing at.
   * @return ArrayList the list of all ims learning design element types this reference might be pointing at
   */
  public ArrayList refersToObject() {
    return refersTo;
  }

  /**
   * Returns the ims learning design typename of this element.
   * @return String the ims learning design typename of this element
   */
  public String referingNode() {
    return referingNode;
  }

  protected void isValid() throws SemanticException {
    //try to locate the refered object
    Document domDocument = node.getOwnerDocument();

    Node referedNode = domDocument.getElementById(idref);

    if (referedNode != null) {
      //we have found a node, but is it the right type
      if (!refersTo.contains(referedNode.getLocalName())) {
        throw new SemanticException(referingNode() + "(" + idref +
                                    ") should refer to an " + refersToObject()
                                    + " but refers to " +
                                    referedNode.getLocalName());
      }
    }
    else {
      throw new SemanticException(referingNode() + "(" + idref +
                                  ") could find refered object.");
    }
  }
}
