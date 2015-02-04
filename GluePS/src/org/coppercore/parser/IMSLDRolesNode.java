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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import org.coppercore.common.MessageList;
import org.coppercore.common.XMLTag;
import org.coppercore.component.RolesTreePropertyDef;
import org.coppercore.exceptions.CopperCoreException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design roles element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/01/21 14:01:11 $
 */
public class IMSLDRolesNode extends IMSLDNode {
  private RolesTreePropertyDef rolesTree = null;

  /**
   * Constructs a IMSLDRolesNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDRolesNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("learner") || nodeName.equals("staff")) {
      addElement(new IMSLDRoleNode(childNode, this));
    }

  }

  /* HAM Users are never assigned to the 'Roles' role, so there is no need to create
   a PlayTree for this role.  IMS page 24. ... The attribute 'identifier' on roles
   can be used to _refer_ to the whole group of all roles...
   */

  protected boolean hasParent(IMSLDNode aRole) {
    return this.equals(aRole);
  }

  protected boolean isRelevantFor(IMSLDNode aRole) {
    return hasParent(aRole);
  }

  protected IMSLDRolesNode findRolesNode() {
    return this;
  }

  protected void persist(int uolId) throws CopperCoreException {
    //persist the roles
    rolesTree.persist();

    //make sure all children are persisted as well
    super.persist(uolId);
  }

  protected boolean buildComponentModel(int uolId, MessageList messages) {
    //create the RolesTreePropertyDef now
    rolesTree = new RolesTreePropertyDef(uolId, RolesTreePropertyDef.ALL_ROLE_ID, getXMLRoles());

    return super.buildComponentModel(uolId, messages);
  }

  /**
   * Returns the XML representation of the content for the current node including
   * all child nodes.
   * @return XML representation of the content
   */
  public String getXMLRoles() {
    StringWriter outputStream = new StringWriter();
    try {
      PrintWriter output = new PrintWriter(outputStream);

      try {
        writeXMLRolesTree(output);
      }
      catch (Exception ex) {
        System.out.println(ex);
      }
      finally {
        output.close();
      }
      return outputStream.toString();
    }
    finally {
      try {
        outputStream.close();
      }
      catch (IOException ex) {
        System.out.println(ex);
      }
    }
  }

  protected void writeXMLRolesTree(PrintWriter output) {
    XMLTag tag = new XMLTag("roles");

    //used the roles contextId as identifier
    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("org-identifier", getIdentifier());
    tag.writeOpenTag(output);

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();

      childNode.writeXMLRolesTree(output);
    }
    tag.writeCloseTag(output);
  }

  private boolean equals(IMSLDNode that) {
    boolean result = false;
    if (that instanceof IMSLDRoleNode) {
      result = getIdentifier().equals(that.getIdentifier());
    }
    else if (that instanceof IMSLDRolesNode) {
      result = (this == that);
    }
    return result;
  }
}
