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
import java.util.Iterator;

import org.coppercore.common.Parser;
import org.coppercore.exceptions.SemanticException;
import org.coppercore.exceptions.ValidationException;
import org.w3c.dom.Node;

/**
 * This class represents an ims LD activity-structure.
*
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/04/23 18:39:35 $
 */
public class IMSActivityStructure
    extends IMSObject {
  private ArrayList activityStructures = new ArrayList();

  public IMSActivityStructure() {
    // default constructor
  }

  protected void initialize(Node anItemNode, IMSLDManifest aManifest) {
    super.initialize(anItemNode, aManifest);

    setKey("identifier");

    Node child = node.getFirstChild();

    while (child != null) {

      if ( (child.getNodeType() == Node.ELEMENT_NODE) &&
          child.getLocalName().equals("activity-structure-ref") &&
          child.getNamespaceURI().equals(org.coppercore.common.Parser.IMSLDNS)) {

        String identifierref = Parser.getNamedAttribute(child, "ref");
        activityStructures.add(identifierref);
      }
      child = child.getNextSibling();
    }
  }

  public void resolveReferences() throws ValidationException {

    boolean ok = true;

    int count = activityStructures.size();
    for (int i = 0; i < count; i++) {

      String idref = (String) activityStructures.get(i);
      IMSObject referencedObject = manifest.findIMSObject(idref);
      activityStructures.set(i, referencedObject);

      if (referencedObject != null) {
        if (! (referencedObject instanceof IMSActivityStructure)) {
          ok = false;
          getLogger().logError("activity-structure-ref (" + idref +
                               ") in " +
                               this +" should refer to an activity-structure" +
                               " but refers to " + referencedObject);
        }
      }
      else {
        ok = false;
        getLogger().logError("Unresolved reference identfierref = " +
                             idref + " found for " + this);
      }
    }

    if (!ok) {
      throw new SemanticException("Invalid reference encountered");
    }

  }

  protected void isValid() throws SemanticException {
    if (isCircular(new ArrayList())) {
      throw new SemanticException("Circular reference for " + toString());
    }
  }

  private boolean isCircular(ArrayList originators) {

    if (originators.contains(this)) {
      return true;
    }

    // fork the list by cloning it
    ArrayList myList = (ArrayList) originators.clone();

    myList.add(this);

    Iterator myIterator = activityStructures.iterator();

    while (myIterator.hasNext()) {
      Object debug = myIterator.next();
      IMSActivityStructure myActivityStructure = (IMSActivityStructure)
          debug;
      if (myActivityStructure != null) {

        if (myActivityStructure.isCircular(myList)) {
          return true;
        }
      }
    }
    return false;
  }
}
