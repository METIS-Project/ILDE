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

package org.coppercore.parser;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.coppercore.common.MessageList;
import org.coppercore.condition.Condition;
import org.coppercore.condition.Conditions;
import org.coppercore.condition.ThenActionList;
import org.coppercore.expression.Complete;
import org.coppercore.expression.IfExpression;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design on-completion element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2005/06/16 22:09:57 $
 */
public class IMSLDOnCompletionNode
    extends IMSLDNode {
  private ArrayList changePropertyValues = new ArrayList();
  private ArrayList notifications = new ArrayList();
  private Condition onCompletionCondition = null;

  /**
   * Constructs a IMSLDOnCompletionNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDOnCompletionNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("feedback-description")) {
      addElement(new IMSLDFeedbackDescriptionNode(childNode, this));
    }
    else if (nodeName.equals("change-property-value")) {
      changePropertyValues.add(addElement(new IMSLDChangePropertyValueNode(
          childNode, this)));
    }
    else if (nodeName.equals("notification")) {
      notifications.add(addElement(new IMSLDNotificationNode(childNode,this)));
    }
  }


  protected boolean buildComponentModel(int uolId, MessageList messages) {
    boolean result = super.buildComponentModel(uolId, messages);

    //check if the were actions to be performed
    if (changePropertyValues.size() > 0 || notifications.size() > 0) {
      IfExpression ifExpression = new IfExpression();
      ifExpression.addOperand(new Complete(parent.getIdentifier(),parent.getNameForCompleteExpression()));

      ThenActionList thenActions = new ThenActionList();

      Iterator iter = changePropertyValues.iterator();
      while (iter.hasNext()) {
        IMSLDChangePropertyValueNode changeProp = (IMSLDChangePropertyValueNode)iter.next();
        thenActions.addThenActions(changeProp.getThenActions());
      }

      iter = notifications.iterator();
      while (iter.hasNext()) {
        IMSLDNotificationNode notification = (IMSLDNotificationNode) iter.next();
        thenActions.addThenAction(notification.getThenAction());
      }
      onCompletionCondition = new Condition(ifExpression, thenActions);
    }
    return result;
  }

  protected void getSystemConditions(Conditions conditions) {
    super.getSystemConditions(conditions);

    //add the complete conditions to the conditions container
    if (onCompletionCondition != null) {
      conditions.addCondition(onCompletionCondition);
    }
  }


  protected void writeXMLContent(PrintWriter output) {

    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();

      if (child instanceof IMSLDFeedbackDescriptionNode) {
        child.writeXMLContent(output);
      }
    }
  }

}
