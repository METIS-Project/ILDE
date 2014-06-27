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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.coppercore.common.MessageList;
import org.coppercore.common.XMLTag;
import org.coppercore.component.MonitorPropertyDef;
import org.coppercore.exceptions.CopperCoreException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design monitor element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.9 $, $Date: 2008/04/28 08:00:33 $
 */
public class IMSLDMonitorNode
    extends ItemModelNode {
  private MonitorPropertyDef monitorDef = null;
  private String roleToSupport = null;
  private Object role = null;

  /**
   * Constructs a IMSLDMonitorNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDMonitorNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("role-ref")) {
      //we cannot solve reference directly, so use reference for now
      role = getNamedAttribute(childNode,"ref");
   }
  }

  protected void resolveReferences() throws Exception {

    if (role != null) {
      role = lookupReferent((String) role);
    }

    super.resolveReferences();
  }

  protected void persist(int uolId) throws CopperCoreException {
    monitorDef.persist();

    //make sure all children are persisted as well
    super.persist(uolId);
  }


  protected void buildItemList(final String componentId, HashMap usedItems, HashMap usedIn, final HashSet referencedItems, String dataType) {
    //recursively pick up on items belonging to this component
    super.buildItemList(getIdentifier(), usedItems, usedIn, referencedItems, MonitorPropertyDef.DATATYPE);
  }


  protected boolean buildComponentModel(int uolId, MessageList messages) {
    //check if there was a role to support, otherwise role SELF is assumed
    if (role != null) {
      roleToSupport = ((IMSLDRoleNode) role).getIdentifier();
    }

    monitorDef = new MonitorPropertyDef(uolId,
                                        parent.getIdentifier(),
                                        parent.getIsVisible(),
                                        getXMLContent(),
                                        getItemsForComponent(getIdentifier()),
                                        roleToSupport);
    return super.buildComponentModel(uolId, messages);
  }

  protected String getComponentDataType() {
    return MonitorPropertyDef.DATATYPE;
  }


  protected void writeXMLEnvironmentTree(PrintWriter output) {
    XMLTag tag = new XMLTag("monitor");

    tag.addAttribute("identifier", parent.getIdentifier());
    tag.addAttribute("isvisible", parent.getIsVisible());
    tag.addAttribute("class", parent.getClassAttribute());
    tag.addAttribute("parameters",parent.getParameters());
    tag.writeOpenTag(output);

    Iterator myIterator = this.children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();
      child.writeXMLEnvironmentTree(output);
    }

    tag.writeCloseTag(output);
  }

  protected void writeXMLContent(PrintWriter output) {
    XMLTag tag = new XMLTag("monitor");

    //used the contextId as identifier
    tag.addAttribute("identifier", parent.getIdentifier());
    tag.addAttribute("isvisible", parent.getIsVisible(), "true");
    tag.addAttribute("parameters",parent.getParameters());
    tag.addAttribute("class", parent.getClassAttribute());

    tag.writeOpenTag(output);

    //write the info about who to monitor (a role, or self)
    if (role != null) {
      XMLTag subTag = new XMLTag("role-to-monitor");

      subTag.addAttribute("identifier", ((IMSLDRoleNode) role).getIdentifier());
      subTag.writeEmptyTag(output);
    }
    else {
      XMLTag subTag = new XMLTag("self");
      subTag.writeEmptyTag(output);
    }

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();
      childNode.writeXMLContent(output);
    }
    tag.writeCloseTag(output);
  }
}
