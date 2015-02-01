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

import java.util.ArrayList;
import java.util.Iterator;

import org.coppercore.exceptions.ValidationException;
import org.coppercore.expression.Expression;
import org.coppercore.expression.Operand;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class acts as a base class for IMS learning design operand elements.
 * <p>
 * These elements include:
 * <ul>
 * <li>divide</li>
 * <li>greater-than</li>
 * <li>is</li>
 * <li>is-not</li>
 * <li>less-than</li>
 * <li>multiply</li>
 * <li>property-value</li>
 * <li>subtract</li>
 * <li>sum</li>
 * </ul>
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2005/01/21 14:01:31 $
 */
public class OperandEntity
    extends ExpressionEntity {
  ArrayList properties = new ArrayList();
  
  /**
   * Constructs a OperandEntity instance from the passed xml dom element.
   * 
   * @param aNode
   *          the xml dom node to parse
   * @param aParent
   *          the parsed IMS learning design element that is the parent element of this object
   */
  public OperandEntity(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("property-ref")) {
      properties.add(childNode.getAttribute("ref"));
    }
    else if (nodeName.equals("property-value")) {
      addElement(new IMSLDPropertyValueNode(childNode, this));
    }
  }

  protected void resolveReferences() throws Exception {
    super.resolveReferences();

    lookupAll(properties);
  }

  protected Operand getExpression(Expression expression) throws ValidationException {

    Iterator iter = properties.iterator();
    while (iter.hasNext()) {
      PropertyNode property = (PropertyNode) iter.next();
      expression.addOperand(property.getExpression());
    }

    //default behaviour suffices for all other situations
    return super.getExpression(expression);
  }
}
