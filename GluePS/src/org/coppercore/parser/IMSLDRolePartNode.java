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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.coppercore.common.MessageList;
import org.coppercore.common.XMLTag;
import org.coppercore.component.CompletionComponent;
import org.coppercore.component.RolePartPropertyDef;
import org.coppercore.condition.CompleteForRole;
import org.coppercore.condition.Condition;
import org.coppercore.condition.Conditions;
import org.coppercore.condition.ThenActionList;
import org.coppercore.exceptions.CopperCoreException;
import org.coppercore.expression.Complete;
import org.coppercore.expression.IfExpression;
import org.coppercore.expression.UsersInRole;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design role-part element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.20 $, $Date: 2005/01/21 14:01:31 $
 */
public class IMSLDRolePartNode extends IMSLDNode {
  private String roleRef = null;
  private IMSLDNode role = null;
  
  private Object activity = null;
  private RolePartPropertyDef rolePart = null;
  private Condition rolePartCondition = null;

  /**
   * Constructs a IMSLDRolePartNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDRolePartNode(Node aNode, IMSLDNode aParent) {
    super (aNode,aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("role-ref")) {
      //we cannot solve reference directly, so use reference for now
      roleRef = getNamedAttribute(childNode,"ref");
   }
    else if (nodeName.equals("learning-activity-ref") ||
             nodeName.equals("support-activity-ref") ||
             nodeName.equals("activity-structure-ref") ||
             nodeName.equals("environment-ref")) {

      //we cannot solve reference directly, so use reference for now
      activity = getNamedAttribute(childNode,"ref");
    }
    else if (nodeName.equals("unit-of-learning-href")) {
      throw new UnsupportedOperationException("unit-of-learning-href not yet supported");
    }
  }


  protected void resolveReferences() throws Exception {

    role = lookupReferent(roleRef);
    activity = lookupReferent((String) activity);

    //notify an environment that is part of an role part. It should create
    //an environment tree
    if (activity instanceof IMSLDEnvironmentNode) {
      ((IMSLDEnvironmentNode)activity).isPartOfRolePart();
    }

    super.resolveReferences();
  }

  /*
   * Collects all the container with a completed property that are needed when
   * processing a particular role tree.
   * @param properties
   * @param uolId
   * @param aRole
     protected void collectCompletedProperties(Vector properties, IMSLDNode aRole) {
    
    if (isRelevantFor(aRole)) {
      //if this rolepart is part of the complete condition for the parent act, a
      //complete role-part property should be generated
      if (parent.isPartOfCompletion(this)) {
        properties.add(rolePart);
      }

      ((IMSLDNode) activity).collectCompletedProperties(properties, aRole);
    }
    
  }
*/

  protected void persist(int uolId) throws CopperCoreException {

    rolePart.persist();

    //make sure all children are persisted as well
    super.persist(uolId);
  }


  /**
   * Returns the activity to be performed in this role-part.
   * @return the activity to be performed in this role-part
   */
  public IMSLDNode getActivity() {
    return (IMSLDNode) activity;
  }

  /**
   * Returns the role that participates in this role-part.
   * <p>
   * Only one role can be part of a role-part.
   * @return the role that participates in this role-part
   */
  public IMSLDNode getRole() {
    return role;
  }

  public String getRoleRef() {
	return roleRef;
}

protected boolean isRelevantFor(IMSLDNode aRole) {
    return (aRole.isRelevantFor(role));
  }

/*  protected void getReferingRoleIds(IMSLDNode aNode, Set roles) {
    if (aNode == activity) {
      roles.add(role.getIdentifier());
    }
    else if (activity instanceof IMSLDActivityStructureNode) {
      if (((IMSLDActivityStructureNode) activity).containsNode(aNode)) {
        roles.add(role.getIdentifier());
      }
    }
  }
*/
  protected void buildItemList(final String componentId, HashMap usedItems, HashMap usedIn, final HashSet referencedItems, String dataType) {
    //recursively pick up on items belonging to this component
    super.buildItemList(getIdentifier(), usedItems, usedIn, referencedItems, RolePartPropertyDef.DATATYPE);
  }


  protected boolean buildComponentModel(int uolId, MessageList messages) {
    super.buildComponentModel(uolId, messages);

    String roleId = role.getIdentifier();
    String activityId = ((IMSLDNode) activity).getIdentifier();
    //create the RolePartPropertyDef now
    rolePart = new RolePartPropertyDef(uolId,
                                       getIdentifier(),
                                       getXMLContent(),
                                       getItemsForComponent(getIdentifier()),
                                       roleId);

    //build the condition
    IfExpression ifExpression = new IfExpression();
    UsersInRole usersInRole = new UsersInRole(roleId);
    usersInRole.addOperand(new Complete(activityId, ((IMSLDNode) activity).getNameForCompleteExpression()));
    ifExpression.addOperand(usersInRole);

    //build the then action
    ThenActionList actionList = new ThenActionList();
    actionList.addThenAction(new CompleteForRole(rolePart,roleId));

    //now finally we can build the complete rolepart condition
    rolePartCondition = new Condition(ifExpression,actionList);

    return true;
  }

  protected void getSystemConditions(Conditions conditions) {
   super.getSystemConditions(conditions);

   //check if the role-part is relevant for the completion of its act.
   if (parent.isPartOfCompletion(this)) {
     //add the complete conditions to the conditions container
     conditions.addCondition(rolePartCondition);
   }
  }

  protected String getNameForCompleteExpression() {
    return "role-part";
  }

  protected void writeXMLPlay(PrintWriter output, IMSLDNode aRole) {

    //check if the role of this IMSLDrolePart is an ancestor of aRole
    if (isRelevantFor(aRole)) {
      //if so, this rolePart is part of play
      XMLTag tag = new XMLTag("role-part");
      tag.addAttribute("identifier", getIdentifier());

      if (parent.isPartOfCompletion(this)) {
        tag.addAttribute("completed", CompletionComponent.NOTCOMPLETED);
      }

      tag.writeOpenTag(output);

      //this array will contain the collection of environments ids that are
      //relevant for the leaf nodes.
      ArrayList envIds = new ArrayList();

      ((IMSLDNode) activity).writeXMLPlay(output, aRole, envIds);
      tag.writeCloseTag(output);
    }
  }

  protected void writeXMLContent(PrintWriter output) {
    XMLTag tag = new XMLTag("role-part");

    //used the activity contextId as identifier
    tag.addAttribute("identifier", getIdentifier());
    tag.writeOpenTag(output);

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();
        if((childNode instanceof IMSLDTitleNode) || (childNode instanceof IMSLDMetadataNode)) {
        childNode.writeXMLContent(output);
        }
    }

    tag.writeCloseTag(output);
  }
}
