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

package org.coppercore.parser;

import org.coppercore.common.MessageList;
import org.coppercore.component.ComponentFactory;
import org.coppercore.exceptions.CopperCoreException;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design existing element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.9 $, $Date: 2005/01/21 14:01:31 $
 * 
 * @todo This class should be extended from a new subclass of property node.
 * see buildComponentModel() for further documentation
 */
public class IMSLDExistingNode extends PropertyNode {

  private String href = null;
  
  /**
   * Constructs a IMSLDCalculateNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDExistingNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);

    href = getNamedAttribute("href");
  }

  protected String getHref() {
    return href;
  }

  protected boolean isValid(MessageList messages) {
    //we may assume that an existing property is valid
    return true;
  }

  protected void persist(int uolId) throws CopperCoreException {
    getPropertyDef().persist();

    //make sure all children are persisted as well
    super.persist(uolId);
  }


  protected int getScope() {
    return getPropertyDef().getScope();
  }

  protected String getIdentifier() {
    return parent.getIdentifier();
  }



  protected boolean buildComponentModel(int uolId, MessageList messages) {


    // we don't call the super because it will try to construct a new PropertyDef on the
    // basis of the LD content. Instead we will try to retrieve an existing PropertyDef from
    // the database. We extend the PropertyNode class because this property may be used in
    // an expression. Because this node has no children it is not necessary to loop.
    //boolean result = super.buildComponentModel(uolId, messages);

    boolean result = true;

    try {
      //create explicit property components so they may be validated and persisted
      setPropertyDef(ComponentFactory.getPropertyFactory().
                     getExplicitGlobalPropertyDef(uolId, getIdentifier(), getHref()));

      int scope;
      if (parent instanceof IMSLDGlobPersPropertyNode) {
        scope = ((IMSLDGlobPersPropertyNode) parent).getScope();
      }
      else {
        scope = ((IMSLDGlobPropertyNode) parent).getScope();
      }

      if (scope != getPropertyDef().getScope()) {
        /** @todo extend this message with better info */
        messages.logError("Scope mismatch of existing property");
      }
    }
    catch (PropertyException ex) {
      messages.logError(ex.toString());
      result = false;
    }

    return result;
  }

}
