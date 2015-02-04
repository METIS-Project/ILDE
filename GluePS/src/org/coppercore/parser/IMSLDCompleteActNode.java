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

import org.coppercore.common.MessageList;
import org.coppercore.component.CompletionComponentDef;
import org.coppercore.condition.CompleteForUser;
import org.coppercore.condition.Condition;
import org.coppercore.condition.ThenActionList;
import org.coppercore.exceptions.ValidationException;
import org.coppercore.expression.AndExpression;
import org.coppercore.expression.Complete;
import org.coppercore.expression.IfExpression;
import org.coppercore.expression.UsersInRole;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design complete-act element. 
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.16 $, $Date: 2005/01/21 14:01:10 $
 */
public class IMSLDCompleteActNode
    extends OnCompletion {

  private ArrayList rolePartsToComplete = null;
  private IMSLDWhenConditionTrueNode whenConditionTrue = null;

  /**
   * Constructs a IMSLDCompleteActNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDCompleteActNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    //time-limit and when property-value-is-set are inherited

    if (nodeName.equals("when-role-part-completed")) {
      //we cannot solve reference directly, so use reference for now
      addRolePartReference(getNamedAttribute(childNode,"ref"));
    }
    else if (nodeName.equals("when-condition-true")) {
      whenConditionTrue = (IMSLDWhenConditionTrueNode) addElement(new
          IMSLDWhenConditionTrueNode(childNode, this));
    }
  }

  protected void resolveReferences() throws Exception {
    if (rolePartsToComplete != null) {
      lookupAll(rolePartsToComplete);
    }

    super.resolveReferences();
  }

  private void addRolePartReference(String ref) {
    if (rolePartsToComplete == null) {
      rolePartsToComplete = new ArrayList();
    }
    rolePartsToComplete.add(ref);
  }

  /**
   * Returns true if rolePart is part of a complete-act condition.
   * @param rolePart the role-part to check if it's part of a complete-act condition
   * @return true if this node is part of a complete-act condition
   */
  protected boolean isPartOfCompletion(IMSLDRolePartNode rolePart) {
    return (rolePartsToComplete != null ? rolePartsToComplete.contains(rolePart) : false);
  }

  CompletionComponentDef getComponentDef() {
    return ((IMSLDActNode) parent).getComponentDef();
  }

  protected boolean buildComponentModel(int uolId, MessageList messages) {
    boolean result = super.buildComponentModel(uolId, messages);

    //get the ActPropertyDef now
    CompletionComponentDef componentDef = getComponentDef();

/*  removed 2004-11-20: all users wil now be triggered, not only the members
    of this Act. There was some controversey so the code is left in, if decission
    is reversed in the future.

    //build a space separated list of role id for which this act has to be
    //completed
    StringBuffer roleId = new StringBuffer();
    Iterator iter = ( (IMSLDActNode) parent).collectRolesCompletingAct().
        iterator();
    while (iter.hasNext()) {
      String role = (String) iter.next();
      roleId.append(role);
      //use the split method of the String class the recover all separate role ids
      //last obsolete space should not be a problem and will be discarded
      roleId.append(" ");
    }




    //build the then action
    ThenActionList actionList = new ThenActionList();
    actionList.addThenAction(new CompleteForRole(componentDef, roleId.toString()));
*/
    //build the then action
    ThenActionList actionList = new ThenActionList();
    actionList.addThenAction(new CompleteForUser(componentDef));

    try {

      //build the ifExpression according to the completion situation
      if (rolePartsToComplete != null) {
        condition = new Condition(buildRolePartCondition(), actionList);
      }
      else if (whenConditionTrue != null) {
        condition = new Condition(buildWhenExpressionCondition(), actionList);
      }
      else if (whenPropertySet != null) {
        condition = new Condition(buildWhenPropertySetCondition(), actionList);
      }
      else if (timeLimit != null) {
        condition = new Condition(buildTimeLimitCondition(), actionList);
      }
    }
    catch (ValidationException ex) {
      result = false;
      messages.logError(ex.getMessage());
    }
    return result;
  }

  private IfExpression buildRolePartCondition() {
    //build the condition
    IfExpression ifExpression = new IfExpression();
    Complete completeExpression = null;
    AndExpression andExpression = new AndExpression();

    //build up an and expression, and save the last complete expression
    Iterator iter = rolePartsToComplete.iterator();
    while (iter.hasNext()) {
      IMSLDRolePartNode rolePart = (IMSLDRolePartNode) iter.next();
      completeExpression = new Complete(rolePart.getIdentifier(),
                                        rolePart.
                                        getNameForCompleteExpression());

      andExpression.addOperand(completeExpression);
    }

    if (rolePartsToComplete.size() >= 2) {
      //we require the and because all the roleparts need to be completed
      ifExpression.addOperand(andExpression);
    }
    else {
      //we don't need the and, so leave it out. Be aware that the and operator
      //requires at least two operands.
      ifExpression.addOperand(completeExpression);
    }
    //now finnally we can build the complete rolepart condition
    return ifExpression;
  }

  private IfExpression buildWhenExpressionCondition() throws ValidationException {
    //build the condition
    IfExpression ifExpression = new IfExpression();
    UsersInRole usersInRole = new UsersInRole(whenConditionTrue.getRoleNode().
                                              getIdentifier());
    usersInRole.addOperand(whenConditionTrue.getExpressionNode().getExpression());
    ifExpression.addOperand(usersInRole);

    return ifExpression;
  }
}
