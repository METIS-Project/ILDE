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
import org.coppercore.component.CompletionComponentDef;
import org.coppercore.component.PlayPropertyDef;
import org.coppercore.condition.Conditions;
import org.coppercore.exceptions.CopperCoreException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design play element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.18 $, $Date: 2005/01/21 14:01:11 $
 */
public class IMSLDPlayNode extends IMSLDNode {
  private IMSLDActNode lastAct = null;

  private ArrayList acts = new ArrayList();

  private IMSLDCompletePlayNode completePlay = null;

  private PlayPropertyDef componentDef = null;

  /**
   * Constructs a IMSLDPlayNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDPlayNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("act")) {
      String previousActId = (lastAct == null) ? null : lastAct.getIdentifier();
      lastAct = (IMSLDActNode) addElement(new IMSLDActNode(childNode, this, previousActId));
      acts.add(lastAct);
    }
    else if (nodeName.equals("complete-play")) {
      completePlay = (IMSLDCompletePlayNode) addElement(new IMSLDCompletePlayNode(childNode, this));
    }
    else if (nodeName.equals("on-completion")) {
      addElement(new IMSLDOnCompletionNode(childNode, this));
    }
  }

  protected String getNameForCompleteExpression() {
    return "play";
  }

  protected void getSystemConditions(Conditions conditions) {
    super.getSystemConditions(conditions);

    //add the complete conditions to the conditions container
    //conditions.addCondition(rolePartCondition);
  }

  /*  protected void getReferingRoleIds(IMSLDNode aNode, Set roles) {
   Iterator myIterator = children.iterator();

   while (myIterator.hasNext()) {
   IMSLDNode child = (IMSLDNode) myIterator.next();

   if (child instanceof IMSLDActNode) {
   child.getReferingRoleIds(aNode, roles);
   }
   }
   }
   */
  String isTimeLimit() {
    String result = "false";

    if (completePlay != null) {
      result = (hasTimeLimit() ? "true" : "false");
    }
    return result;
  }

  protected boolean hasTimeLimit() {
    boolean result = false;

    if (completePlay != null) {
      result = completePlay.hasTimeLimit();
    }
    return result;
  }

  String getAllRolesForPlay() {
    HashSet roles = new HashSet();
    StringBuffer result = new StringBuffer();

    //collect all unique roles for all acts of this play
    Iterator myIterator = acts.iterator();
    while (myIterator.hasNext()) {
      IMSLDActNode act = (IMSLDActNode) myIterator.next();
      roles.addAll(act.getAllRolesForAct());
    }

    //build a space seperated list of identifiers
    myIterator = roles.iterator();
    while (myIterator.hasNext()) {
      String role = (String) myIterator.next();
      result.append(role);
      result.append(" ");
    }
    return result.toString();
  }

  IMSLDActNode getLastAct() {
    return lastAct;
  }

  CompletionComponentDef getComponentDef() {
    return componentDef;
  }

  protected void persist(int uolId) throws CopperCoreException {
    componentDef.persist();

    //make sure all children are persisted as well
    super.persist(uolId);

  }

  protected boolean isRelevantFor(IMSLDNode aRole) {

    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();

      if (child instanceof IMSLDActNode) {
        if (child.isRelevantFor(aRole)) {
          //appearently this act is relevant for this role, so the play is as well
          return true;
        }
      }
    }
    //no relevant acts found!
    return false;
  }

  protected void buildItemList(final String componentId, HashMap usedItems, HashMap usedIn,
      final HashSet referencedItems, String dataType) {
    //recursively pick up on items belonging to this component
    super.buildItemList(getIdentifier(), usedItems, usedIn, referencedItems, PlayPropertyDef.DATATYPE);
  }

  protected boolean buildComponentModel(int uolId, MessageList messages) {

    //create the RolePartPropertyDef now
    componentDef = new PlayPropertyDef(uolId, getIdentifier(), (completePlay == null) ? CompletionComponent.UNLIMITED
        : CompletionComponent.NOTCOMPLETED, getIsVisible(), getXMLContent(), getItemsForComponent(getIdentifier()));

    return super.buildComponentModel(uolId, messages);
  }

  protected void writeXMLPlay(PrintWriter output, IMSLDNode aRole) {

    //first check if this play is relevant for aRole
    if (isRelevantFor(aRole)) {
      XMLTag tag = new XMLTag("play");
      tag.addAttribute("time-limit", isTimeLimit());
      tag.addAttribute("identifier", getIdentifier());
      tag.addAttribute("isvisible", getIsVisible());
      if (completePlay != null) {
        tag.addAttribute("completed", CompletionComponent.NOTCOMPLETED);
      }
      else {
        tag.addAttribute("completed", CompletionComponent.UNLIMITED);
      }
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
    XMLTag tag = new XMLTag("play");

    //used the activity contextId as identifier
    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("isvisible", getIsVisible(), "true");
    tag.writeOpenTag(output);

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();

      if (!(childNode instanceof IMSLDActNode)) {
        childNode.writeXMLContent(output);
      }
    }
    tag.writeCloseTag(output);
  }

}
