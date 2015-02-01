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

package org.coppercore.component;

import java.util.ArrayList;
import java.util.Collection;

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.Parser;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class represents the environment tree. This is a static component,
 * meaning that it is the same for all the users. Instances of this component
 * contain no personalized data itself.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.15 $, $Date: 2005/01/20 13:06:28 $
 */
public class EnvironmentTreeProperty extends StaticProperty {
  private static final long serialVersionUID = 42L;

  /**
   * The default constructor for this component.
   *
   * @param uol Uol the Uol defining this component
   * @param propId String id representing this view on the activity tree.
   */
  public EnvironmentTreeProperty(Uol uol, String propId) {
    super(uol, EnvironmentTreePropertyDef.IDPREFIX+propId);
  }

  /**
   * Returns the corresponding PropertyDefinition belonging to this component.
   *
   * @throws PropertyException when this operation fails
   * @return PropertyDef the PropertyDefinition for this component
   */
  protected PropertyDef findPropertyDef() throws
      PropertyException {
    return new EnvironmentTreePropertyDef(uolId, propId);
  }


  /**
   * Return the personalized xml representation of this component.
   * Personaliztion occurs on the visibility of the memebers of the tree.
   * 
   * @param run
   *          Run the Run to which this component belongs
   * @param user
   *          User the User to which this component belongs
   * @return the xml representation of the personalized environment tree.
   * @throws PropertyException whenever this operation fails.
   */
  public String getTreeXML(Run run, User user) throws PropertyException {
    String result = "";


    // recursivly fill in all actual completed values, starting at the documentroot.
    Element root = ( (EnvironmentTreePropertyDef) getPropertyDef()).getTree();

    Node node = root.getFirstChild();
    while (node != null) {

      //we have to check the root node (visibility)
      LocalPersonalContent component = getComponent(run, user, (Element) node);

      if (component != null && component.isVisible()) {
        personalizeXml(run, user, (Element) node);
        result += Parser.documentToString(node);
      }
      node = node.getNextSibling();
    }


    //transform dom document to string containing XML
    return result;
  }

  /**
   * Recursively traverse this node to seek any completed attributes. If found,
   * replace its value with the value in the dossier for the current user.
   * @param aNode
   */
  private void personalizeXml(Run run, User user,
                                              Element aNode) throws PropertyException {

    Node child = aNode.getFirstChild();

    //loop over all children
    while (child != null) {
      Node nextNode = child.getNextSibling();

      if (child.getNodeType() == Node.ELEMENT_NODE) {
        LocalPersonalContent component = getComponent(run, user, (Element) child);

        //check if the node is visible
        if (component != null && !component.isVisible()) {
          //if not, remove this child
          aNode.removeChild(child);
        }
        else {
          //go recursively into the depth
          personalizeXml(run,user,(Element) child);
        }
      }
      //restore the next child
      child = nextNode;
    }
  }


  private LocalPersonalContent getComponent(Run run, User user, Element node) throws
      PropertyException {
    String identifier = node.getAttribute("identifier");
    String nodeName = node.getNodeName();
    ComponentFactory factory = ComponentFactory.getPropertyFactory();
    LocalPersonalContent component = null;

    if ("environment".equals(nodeName)) {
      component = factory.getEnvironment(getUol(), run, user, identifier);
    }
    else if ("learning-object".equals(nodeName)) {
      component = factory.getLearningObject(getUol(), run, user, identifier);
    }
    else if ("send-mail".equals(nodeName)) {
      component = factory.getSendMail(getUol(), run, user, identifier);
    }
    else if ("monitor".equals(nodeName)) {
      component = factory.getMonitorObject(getUol(), run, user, identifier);
    }

    /** @todo add simular constructs for index-search-object and conference */

    return component;
  }

  /**
   * Returns a collection of users that are in the scope with this Property. In
   * effect this method will return all known users if the scope is global or
   * all users in the run if the scope is local or all users in role if the
   * scope is role or an empty collection if the scope is personal.
   *
   * @throws PropertyException whenever the method fails to return the
   *   requested users.
   * @return Collection of users that are in the same scope
   */
  public Collection /* User */ getUsersInScope() throws PropertyException {
    //this is a global properties that never should be included in the event chain
    return new ArrayList();
  }
}
