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
import org.coppercore.exceptions.ValidationException;
import org.w3c.dom.Node;

/**
 * Represents an item element from the IMS Content Package Specification.<p>The item element
* checks if it refers to the correct item type and checks if the referring chain does not contain
* circular references.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/02/25 14:15:09 $
 */
public class IMSItem
    extends IMSObject {

  private String identifierref;
  private IMSObject referencedObject;

  public IMSItem() {
    // default constructor
  }

  protected void initialize(Node anItemNode, IMSLDManifest aManifest) {
    super.initialize(anItemNode, aManifest);

    setKey("identifier");
    identifierref = getNamedAttribute("identifierref");
  }

  protected void isValid() throws SemanticException {

    if (isCircular(new ArrayList())) {
      throw new SemanticException("Circular reference for " + toString());
    }
  }

  public void resolveReferences() throws ValidationException {
    boolean ok = true;
    String errorMessage = null;

    if (identifierref != null) {
      referencedObject = manifest.findIMSObject(identifierref);

      if (referencedObject != null) {
        if (! (referencedObject instanceof IMSResource) &&
            ! (referencedObject instanceof IMSItem)) {
          ok = false;

          errorMessage = "Incorrect item referenced by " + toString() +
                               ". " + referencedObject.node.getNodeName() +
                               " found, item or resource was expected.";

          //add the error to the logger
          getLogger().logError(errorMessage);
        }
      }
      else {
        ok = false;

        errorMessage = "Unresolved reference identfierref = " +
                             identifierref + " found for " + toString();

        //add the error to the logger
        getLogger().logError(errorMessage);
      }
    }
    else {
      
      //changed 2005-02-25: due to problems with the Reload editor which is producing empty items
      //this code has been changed. Items are now allowed to have emty items although this is not
      //recommended.
      errorMessage = "Item does not contains a reference to a resource " + toString();
      getLogger().logWarning(errorMessage);
      
      /*
      ok = false;

      errorMessage = "Unresolved reference identfierref = " +
                           identifierref + " found for " + toString();

      //add the error to the logger
      getLogger().logError(errorMessage);
      */
      

    }


    if (!ok) {
      throw new SemanticException(errorMessage);
    }

  }

  private boolean isCircular(ArrayList items) {
    boolean result = false;
    items.add(this);

    if (referencedObject instanceof IMSItem) {
      if (!items.contains(referencedObject)) {
        result = ( (IMSItem) referencedObject).isCircular(items);
      }
      else {
        result = true;
      }
    }
    return result;
  }
}
