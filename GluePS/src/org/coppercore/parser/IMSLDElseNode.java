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
import org.coppercore.condition.Condition;
import org.coppercore.exceptions.ValidationException;
import org.coppercore.expression.IfExpression;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design else element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2005/01/20 16:12:45 $
 */
public class IMSLDElseNode
    extends IMSLDThenNode {
  private Condition condition = null;
  private IMSLDIfNode ifNode = null;
  private IMSLDThenNode thenNode = null;
  private IMSLDElseNode elseNode = null;

  /**
   * Constructs a IMSLDElseNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDElseNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws
      Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("if")) {
      ifNode = (IMSLDIfNode) addElement(new IMSLDIfNode(childNode, this));
    }
    else if (nodeName.equals("then")) {
      thenNode = (IMSLDThenNode) addElement(new IMSLDThenNode(childNode, this));
    }
    else if (nodeName.equals("else")) {
      elseNode = (IMSLDElseNode) addElement(new IMSLDElseNode(childNode, this));
    }
  }

  protected boolean buildComponentModel(int uolId, MessageList messages) {
    boolean result = super.buildComponentModel(uolId, messages);

    try {
      if (ifNode != null) {
        if (elseNode == null) {
          condition = new Condition( (IfExpression) ifNode.getExpression(),
                                    thenNode.getThenActionList());
        }
        else {
          if (elseNode.getCondition() == null) {
            condition = new Condition( (IfExpression) ifNode.getExpression(),
                                      thenNode.getThenActionList(),
                                      elseNode.getThenActionList());
          }
          else {
            condition = new Condition( (IfExpression) ifNode.getExpression(),
                                      thenNode.getThenActionList(),
                                      elseNode.getCondition());
          }
        }

      }
    }
    catch (ValidationException ex) {

      result = false;
    }
    return result;
  }

  /**
   * Returns the else condition.
   * <p>
   * @return the else condition
   */
  protected Condition getCondition() {
    return condition;
  }

}
