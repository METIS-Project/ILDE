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

import org.coppercore.exceptions.ValidationException;
import org.coppercore.expression.Complete;
import org.coppercore.expression.Operand;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class represents an IMS learning design complete element. 
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.10 $, $Date: 2005/01/20 16:12:45 $
 */
public class IMSLDCompleteNode
    extends IMSLDNode {

  private Object referedNode = null;

  /**
   * Constructs a IMSLDCompleteNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDCompleteNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("role-part-ref")) {
      //we cannot solve reference directly, so use reference for now
      referedNode = getNamedAttribute(childNode,"ref");
   }
   else if (nodeName.equals("learning-activity-ref")) {
     //we cannot solve reference directly, so use reference for now
    referedNode = getNamedAttribute(childNode,"ref");
   }
   else if (nodeName.equals("support-activity-ref")) {
     //we cannot solve reference directly, so use reference for now
    referedNode = getNamedAttribute(childNode,"ref");
   }
   else if (nodeName.equals("activity-structure-ref")) {
     //we cannot solve reference directly, so use reference for now
    referedNode = getNamedAttribute(childNode,"ref");
   }
   else if (nodeName.equals("act-ref")) {
     //we cannot solve reference directly, so use reference for now
    referedNode = getNamedAttribute(childNode,"ref");
   }
   else if (nodeName.equals("play-ref")) {
     //we cannot solve reference directly, so use reference for now
    referedNode = getNamedAttribute(childNode,"ref");
   }
   else if (nodeName.equals("unit-of-learning-ref")) {
     //we cannot solve reference directly, so use reference for now
     //not implemented yet
     //referedNode = Parser.getNamedAttribute(childNode,"ref");
   }
  }


  protected void resolveReferences() throws Exception {
    referedNode = lookupReferent((String) referedNode);

    super.resolveReferences();
  }

  protected Operand getExpression() throws ValidationException {
    String className = ((IMSLDNode) referedNode).node.getLocalName();
    Complete completeExpression = new Complete(((IMSLDNode) referedNode).getIdentifier(),className);
    return completeExpression;
  }
}
