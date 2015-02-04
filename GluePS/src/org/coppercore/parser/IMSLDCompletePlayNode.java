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
import org.coppercore.component.CompletionComponentDef;
import org.coppercore.condition.CompleteForUser;
import org.coppercore.condition.Condition;
import org.coppercore.condition.ThenActionList;
import org.coppercore.exceptions.ValidationException;
import org.coppercore.expression.Complete;
import org.coppercore.expression.IfExpression;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class implements an IMS learning design complete-play element. 
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.12 $, $Date: 2005/01/21 14:01:10 $
 */
public class IMSLDCompletePlayNode extends OnCompletion {
  private boolean completeWhenLastActCompleted = false;


  /**
   * Constructs a IMSLDCompletePlayNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDCompletePlayNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    //time-limit and when property-value-is-set are inherited


    if (nodeName.equals("when-last-act-completed")) {
      completeWhenLastActCompleted = true;
    }
   }

  /**
   * Returns wether the play is completed when the last act is completed or not.<p> 
   * @return if the play is completed when the last act is completed
   */
  public boolean completedWhenLastActCompleted() {
    return completeWhenLastActCompleted;
  }

  CompletionComponentDef getComponentDef() {
    return ((IMSLDPlayNode) parent).getComponentDef();
  }


  protected boolean buildComponentModel(int uolId, MessageList messages) {
    boolean result = super.buildComponentModel(uolId, messages);

    //get the PlayPropertyDef now
    CompletionComponentDef componentDef = getComponentDef();

    //build the then action
    ThenActionList actionList = new ThenActionList();

    //change 2004-11-23: modified the scope of the play into local
    actionList.addThenAction(new CompleteForUser(componentDef));

    //build the ifExpression according to the completion situation
    if (completeWhenLastActCompleted == true) {
      condition = new Condition(buildLastActCompleteCondition(), actionList);
    }
    else if (whenPropertySet != null) {
      try {
        condition = new Condition(buildWhenPropertySetCondition(), actionList);
      }
      catch (ValidationException ex) {
        result = false;
        messages.logError(ex.getMessage());
      }
    }
    else if (timeLimit != null) {
      condition = new Condition(buildTimeLimitCondition(), actionList);
    }
    return result;
  }

  private IfExpression buildLastActCompleteCondition() {
    //build the condition

    IMSLDActNode lastAct = ((IMSLDPlayNode) parent).getLastAct();
    String lastActId = lastAct.getIdentifier();
    String lastActName = lastAct.getNameForCompleteExpression();

    IfExpression ifExpression = new IfExpression();
    ifExpression.addOperand(new Complete(lastActId,lastActName));

    return ifExpression;
  }
}
