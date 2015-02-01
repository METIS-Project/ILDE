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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.coppercore.common.XMLTag;
import org.coppercore.component.CompletionComponent;
import org.coppercore.component.CompletionComponentDef;
import org.coppercore.component.SupportActivityPropertyDef;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design support-activity element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.15 $, $Date: 2005/01/21 14:01:11 $
 */
public class IMSLDSupportActivityNode
    extends IMSLDLearningActivityNode {
  private ArrayList rolesToSupport = new ArrayList();

  /**
   * Constructs a IMSLDSupportActivityNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDSupportActivityNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("role-ref")) {
      rolesToSupport.add(getNamedAttribute(childNode,"ref"));
    }
  }

  protected void resolveReferences() throws Exception {

    lookupAll(rolesToSupport);

    super.resolveReferences();
  }

  protected String getNameForCompleteExpression() {
    return "support-activity";
  }

  /**
   * Returns a list of all roles this support-activity will support.
   * @return a list of all roles this support-activity will support
   */
  ArrayList getRoleIdsToSupport() {
    ArrayList result = new ArrayList();

    Iterator myIterator = rolesToSupport.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode role = (IMSLDNode) myIterator.next();
      result.add(role.getIdentifier());
    }
    return result;
  }

  CompletionComponentDef buildComponent(int uolId) {
    return new SupportActivityPropertyDef(uolId, getIdentifier(),
                                          (completeActivityNode == null) ?
                                          CompletionComponent.UNLIMITED :
                                          CompletionComponent.NOTCOMPLETED,
                                          getIsVisible(),
                                          getXMLContent(),
                                          getItemsForComponent(getIdentifier()),
                                          getRoleIdsToSupport());
  }

  /**
   * Writes a XML tag roleToSupport to the output stream for each item on the
   * rolesToSupport collection.
   * @param output PrintWriter to which output is written
   */
  protected void writeXMLRolesToSupport(PrintWriter output) {

    Iterator myIterator = rolesToSupport.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode role = (IMSLDNode) myIterator.next();
      XMLTag tag = new XMLTag("role-to-support");

      tag.addAttribute("identifier", role.getIdentifier());
      tag.writeEmptyTag(output);
    }
  }
}
