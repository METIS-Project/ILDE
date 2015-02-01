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
import java.util.Set;

import org.coppercore.common.MessageList;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ActPropertyDef;
import org.coppercore.component.CompletionComponent;
import org.coppercore.component.CompletionComponentDef;
import org.coppercore.exceptions.CopperCoreException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class represents an IMS Learning Design act element.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.22 $, $Date: 2005/01/21 14:01:30 $
 */
public class IMSLDActNode extends IMSLDNode {
  private IMSLDCompleteActNode completeAct = null;

  private ActPropertyDef componentDef = null;

  private ArrayList roleParts = new ArrayList();

  private String previousActId = null;

  /**
   * Constructs an IMSLDActivitiesNode instance for the passed xml dom element.
   * 
   * @param aNode
   *          Node the xml dom node to parse
   * @param aParent
   *          IMSLDNode the parsed IMS learing design element that is the parent element of this
   *          object
   * @param previousActId
   *          String the learing design id of the previous act in the play
   */
  public IMSLDActNode(Node aNode, IMSLDNode aParent, String previousActId) {
    super(aNode, aParent);
    this.previousActId = previousActId;
  }

  public void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("complete-act")) {
      completeAct = (IMSLDCompleteActNode) addElement(new IMSLDCompleteActNode(childNode, this));
    }
    else if (nodeName.equals("role-part")) {
      roleParts.add(addElement(new IMSLDRolePartNode(childNode, this)));
    }
    else if (nodeName.equals("on-completion")) {
      addElement(new IMSLDOnCompletionNode(childNode, this));
    }
  }

  /**
   * Returns the property definition object for this Act.
   * 
   * @return CompletionComponentDef the property definition object for this Act
   */
  CompletionComponentDef getComponentDef() {
    return componentDef;
  }

  protected String getNameForCompleteExpression() {
    return "act";
  }

/*  protected void getReferingRoleIds(IMSLDNode aNode, Set roles) {
    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();

      if (child instanceof IMSLDRolePartNode) {
        child.getReferingRoleIds(aNode, roles);
      }
    }
  }
*/
  /**
   * Return a collection with role-ids for whom this act must be completed.
   * 
   * @return a collection with role-ids
   */
  protected ArrayList collectRolesCompletingAct() {
    HashMap allRoles = new HashMap();

    // fetch all roles referenced in this act via role-parts
    Iterator myIterator = roleParts.iterator();
    IMSLDNode role = null;
    while (myIterator.hasNext()) {
      IMSLDRolePartNode rolePart = (IMSLDRolePartNode) myIterator.next();

      role = rolePart.getRole();

      // using a hashmap filters out roles that are referenced multiple times
      // in the same act
      allRoles.put(role.getIdentifier(), role);
    }

    ArrayList parents = new ArrayList();
    ArrayList result = new ArrayList();

    // filter out roles which are children of roles already present in the list
    // (optimalization, only relevant roles remain avoiding duplicate processing
    // in event handler)
    myIterator = allRoles.keySet().iterator();
    while (myIterator.hasNext()) {
      String roleId = (String) myIterator.next();
      role = (IMSLDRoleNode) allRoles.get(roleId);

      // reset parents for re-use
      parents.clear();
      ((IMSLDRoleNode) role).getParentIds(parents);

      // determine insersection of parents and temp. If not empty there is an
      // intersection meaning parent role is part of the act so this child doesn't
      // need to be
      parents.retainAll(allRoles.keySet());
      if (parents.isEmpty()) {
        result.add(roleId);
      }
    }
    return result;
  }

  /**
   * Collects all roles that are part of all roleparts making up this act.
   * 
   * @return a Set containing all roles that are part of the act via the roleparts
   */
  protected Set getAllRolesForAct() {
    HashSet roles = new HashSet();

    // get all roles for relevant for this act
    Iterator myIterator = roleParts.iterator();
    while (myIterator.hasNext()) {
      IMSLDRolePartNode rolePart = (IMSLDRolePartNode) myIterator.next();
      roles.add(rolePart.getRole().getIdentifier());
    }
    return roles;
  }

  private String isTimeLimit() {
    String result = "false";

    if (completeAct != null) {
      result = (hasTimeLimit() ? "true" : "false");
    }
    return result;
  }

  protected boolean hasTimeLimit() {
    boolean result = false;

    if (completeAct != null) {
      result = completeAct.hasTimeLimit();
    }
    return result;
  }

  protected void persist(int uolId) throws CopperCoreException {
    componentDef.persist();

    // make sure all children are persisted as well
    super.persist(uolId);

  }

  protected boolean isRelevantFor(IMSLDNode aRole) {

    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();

      if (child instanceof IMSLDRolePartNode && child.isRelevantFor(aRole)) {

        // appearently this rolePart is relevant for this role, so the
        // act is as well
        return true;
      }
    }
    // no relevant roleParts found!
    return false;
  }

  /**
   * Returns true if <code>IMSLDRolePart</code> rolePart is part of a complete-act condition.
   * 
   * @param rolePart
   *          the rolepart to check
   * @return true if this node is part of a complete-act condition
   */
  protected boolean isPartOfCompletion(IMSLDRolePartNode rolePart) {
    boolean result = false;

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();

      // check the children to see if this rolePart is part of the completion of
      // this act.
      result |= child.isPartOfCompletion(rolePart);
    }

    return result;
  }

  protected void buildItemList(final String componentId, HashMap usedItems, HashMap usedIn,
      final HashSet referencedItems, String dataType) {
    // recursively pick up on items belonging to this component
    super.buildItemList(getIdentifier(), usedItems, usedIn, referencedItems, ActPropertyDef.DATATYPE);
  }

  protected boolean buildComponentModel(int uolId, MessageList messages) {

    // create the RolePartPropertyDef now
    componentDef = new ActPropertyDef(uolId, getIdentifier(), (completeAct == null) ? CompletionComponent.UNLIMITED
        : CompletionComponent.NOTCOMPLETED, previousActId, getXMLContent(), getItemsForComponent(getIdentifier()));

    return super.buildComponentModel(uolId, messages);
  }

  protected void writeXMLPlay(PrintWriter output, IMSLDNode aRole) {
    if (isRelevantFor(aRole)) {
      XMLTag tag = new XMLTag("act");
      tag.addAttribute("time-limit", isTimeLimit());
      if (completeAct != null) {
        tag.addAttribute("completed", CompletionComponent.NOTCOMPLETED);
      }
      else {
        tag.addAttribute("completed", CompletionComponent.UNLIMITED);
      }
      tag.addAttribute("identifier", getIdentifier());
      tag.writeOpenTag(output);

      Iterator myIterator = children.iterator();

      while (myIterator.hasNext()) {
        IMSLDNode child = (IMSLDNode) myIterator.next();
        child.writeXMLPlay(output, aRole);
      }
      tag.writeCloseTag(output);
    }
  }

  protected void writeXMLContent(PrintWriter output) {
    // check if activity has been properly initialized
    XMLTag tag = new XMLTag("act");

    // used the activity contextId as identifier
    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("isvisible", getIsVisible(), "true");
    tag.writeOpenTag(output);

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();
      if (!(childNode instanceof IMSLDRolePartNode) && !(childNode instanceof IMSLDCompleteActNode)) {
        childNode.writeXMLContent(output);
      }
    }
    tag.writeCloseTag(output);
  }

}
