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

import java.util.Iterator;

import org.coppercore.exceptions.ValidationException;
import org.coppercore.expression.Expression;
import org.coppercore.expression.Operand;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class represents an abstract IMS Learning Design element that is part of
 * an expression.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.9 $, $Date: 2005/01/11 13:15:08 $
 */
public abstract class ExpressionEntity
    extends IMSLDNode {

  /** Holds the parsed operator of this expression entity. */
  protected IMSLDNode operator = null;

  /**
   * Constructs an ExpressionEntity object.
   *
   * @param aNode Node the xml dom node to parse
   * @param aParent IMSLDNode the parsed IMS learing design element that is the
   * parent element of this object
   */
  public ExpressionEntity(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  /**
   * Parses the passed xml dom element node with the given name.<p>If the method
   * recognizes a name as an element it can handle, but first it lets the super class
   * handle the element.
   *
   * @param childNode Element the xml dom element node to be parsed
   * @param nodeName String the name of the node to parse
   * @throws Exception when an error occurs during parsing
   */
  protected void parseLDElement(Element childNode, String nodeName) throws
      Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("is-member-of-role")) {
      operator = addElement(new IMSLDIsMemberOfRoleNode(childNode, this));
    }
    else if (nodeName.equals("is")) {
      operator = addElement(new IMSLDIsNode(childNode, this));
    }
    else if (nodeName.equals("is-not")) {
      operator = addElement(new IMSLDIsNotNode(childNode, this));
    }
    else if (nodeName.equals("and")) {
      operator = addElement(new IMSLDAndNode(childNode, this));
    }
    else if (nodeName.equals("or")) {
      operator = addElement(new IMSLDOrNode(childNode, this));
    }
    else if (nodeName.equals("sum")) {
      operator = addElement(new IMSLDSumNode(childNode, this));
    }
    else if (nodeName.equals("subtract")) {
      operator = addElement(new IMSLDSubtractNode(childNode, this));
    }
    else if (nodeName.equals("multiply")) {
      operator = addElement(new IMSLDMultiplyNode(childNode, this));
    }
    else if (nodeName.equals("divide")) {
      operator = addElement(new IMSLDDivideNode(childNode, this));
    }
    else if (nodeName.equals("greater-than")) {
      operator = addElement(new IMSLDGreaterThanNode(childNode, this));
    }
    else if (nodeName.equals("less-than")) {
      operator = addElement(new IMSLDLessThanNode(childNode, this));
    }
    else if (nodeName.equals("users-in-role")) {
      operator = addElement(new IMSLDUsersInRoleNode(childNode, this));
    }
    else if (nodeName.equals("no-value")) {
      operator = addElement(new IMSLDNoValueNode(childNode, this));
    }
    else if (nodeName.equals("time-unit-of-learning-started")) {
      operator = addElement(new IMSLDTimeUnitOfLearningStartedNode(childNode, this));
    }
    else if (nodeName.equals("datetime-activity-started")) {
      operator = addElement(new IMSLDDateTimeActivityStartedNode(childNode, this));
    }
    else if (nodeName.equals("current-datetime")) {
      operator = addElement(new IMSLDCurrentDateTimeNode(childNode, this));
    }
    else if (nodeName.equals("complete")) {
      operator = addElement(new IMSLDCompleteNode(childNode, this));
    }
    else if (nodeName.equals("not")) {
      operator = addElement(new IMSLDNotNode(childNode, this));
    }
  }

  /**
   * Returns the expression value.
   *
   * @param expression Expression
   * @throws ValidationException
   * @return Operand
   */
  protected Operand getExpression(Expression expression) throws
      ValidationException {
    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();
      expression.addOperand(child.getExpression());
    }
    return expression;
  }
}
