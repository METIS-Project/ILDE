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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.Parser;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * This class represents the activity tree. An activity tree corresponds with
 * the plays and its children defined in IMS LD filtered on a particular role.
 * This is a static component, meaning that it is the same for all the users.
 * Instances of this component contain no personalized data itself.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.26 $, $Date: 2005/12/13 12:50:15 $
 */
public class ActivityTreeProperty extends StaticProperty {
  private static final long serialVersionUID = 1L;

  /**
   * The default constructor for this component.
   *
   * @param uol Uol the Uol defining this component
   * @param propId String id representing this view on the activity tree.
   */
  public ActivityTreeProperty(Uol uol, String propId) {
    super(uol, ActivityTreePropertyDef.IDPREFIX + propId);
  }

  /**
   * Returns the corresponding PropertyDefinition belonging to this component.
   *
   * @throws PropertyException when this operation fails
   * @return PropertyDef the PropertyDefinition for this component
   */
  protected PropertyDef findPropertyDef() throws
      PropertyException {
    return new ActivityTreePropertyDef(uolId, propId);
  }

  /**
   * Gets the activity tree for the current user in the current run. The
   * activity is expressed in XML. The tree is personalized, meaning that all
   * properties are filled in with their actual values for that user.
   *
   * @param run Run the Run for which this activity tree is retrieved
   * @param user User for which this activity is personalized
   * @throws PropertyException when the operation fails
   * @return String the XML representation for the personalized activity tree.
   */
  public String getTreeXML(Run run, User user) throws
      PropertyException {
    // recursivly fill in all actual completed values, starting at the documentroot.
    Element root = ( (ActivityTreePropertyDef) getPropertyDef()).getTree();
    personalizeXml(run, user, root);

    //transform dom document to string containing XML
    return Parser.documentToString(root);
  }

  /**
   * Recursively traverse this passed DOM node to seek any completed attributes.
   * If found, replace its value with the value in the dossier for the current
   * user. Nodes will be ordered according to the IMS LD rules and invisible
   * nodes and incomplete acts are filtered out.
   *
   * @param run Run the Run for which this activity tree is retrieved
   * @param user User for which this activity is personalized
   * @param aNode Element the node of the activity tree to be evaluated
   * @throws PropertyException when the operation fails
   * @return LocalPersonalContent representing the personalized version of this
   *   node
   */
  private LocalPersonalContent personalizeXml(Run run, User user,
                                              Element aNode) throws
      PropertyException {
    boolean isSequence = false;

    //fetch the component belonging to this node if we have to do some personalization
    LocalPersonalContent component = getComponent(run, user, aNode);

    if (component != null) {
      if (component instanceof CompletionComponent) {
        //set the completed attribute
        aNode.setAttribute("completed",
                           ( (CompletionComponent) component).getCompleted());

        //deal with the sort order of activity-structure (selections)
        if (component instanceof ActivityStructureProperty) {
          //check if we dealing with a selection
          if ("selection".equals(aNode.getAttribute("structure-type"))) {
            if ("visibility-order".equals(aNode.getAttribute("sort"))) {
              //sort the children based on their first access.
              sortChildren(run, user, aNode);
            }
          }
          else { // structure-type == "sequence" || structure-type == null
            //ok we are dealing with a sequence !
            isSequence = true;
          }
        }
      }
    }

    //Traverse the tree by recursively calling this method for all the children
    //for which the previous parent act was true
    Node childNode = aNode.getFirstChild();
    boolean remove = false;

    while (childNode != null) {
      //determine the next node now, because childNode may be removed in the
      //process
      Node nextNode = childNode.getNextSibling();
      if (childNode.getNodeType() == Node.ELEMENT_NODE) {
        if (remove) {
          aNode.removeChild(childNode);
        }
        else {
          LocalPersonalContent childComponent = personalizeXml(run, user,
              (Element) childNode);
          if (childComponent instanceof CompletionComponent) {
            CompletionComponent child = (CompletionComponent) childComponent;

            //check if we have to remove the following children
            if (isSequence) {

              //make the component visible as it is part of a sequence. This is an
              //LD requirement as all children of an sequence should be made visible
              child.setVisibility(true);

              //remove should become false when act wasn't completed!!
              remove = !child.isCompleted();

            }
            else if (child instanceof ActProperty) {
              //fetch the previous act, which may be null is the current act is the first
              ActProperty previousAct = ( (ActProperty) child).getPreviousAct();

              //check if the previous act was completed
              if (previousAct != null) {
                if (!previousAct.isCompleted()) {
                  //the previous act was not completed yet, so we can't continue
                  remove = true;
                  //remove this act from the activity tree
                  aNode.removeChild(childNode);
                }
              }
            }

            // check if we have to remove this component from the list.
            if (!child.isVisible()) {
              aNode.removeChild(childNode);
            }
          }
        }
      }
      //move to the next node
      childNode = nextNode;
    }

    return component;
  }

  /**
   * Returns a LocalPersonalContent component corresponding with the passed node.
   *
   * @param run Run the Run for which the component should be returned
   * @param user User the User for which the component should be returned
   * @param node Element the node representing this component in the activity
   *   tree
   * @throws PropertyException when the operation fails
   * @return LocalPersonalContent the component matching the parameters
   */
  private LocalPersonalContent getComponent(Run run, User user, Element node) throws
      PropertyException {
    String identifier = node.getAttribute("identifier");
    String nodeName = node.getNodeName();
    ComponentFactory factory = ComponentFactory.getPropertyFactory();
    LocalPersonalContent component = null;

    if (nodeName.equals("learning-activity")) {
      component = factory.getLearningActivity(getUol(), run, user, identifier);
    }
    else if (nodeName.equals("support-activity")) {
      component = factory.getSupportActivity(getUol(), run, user, identifier);
    }
    else if (nodeName.equals("environment-activity")) {
      component = factory.getEnvironment(getUol(), run, user, identifier);
    }
    else if (nodeName.equals("activity-structure")) {
      component = factory.getActivityStructure(getUol(), run, user, identifier);
    }
    else if (nodeName.equals("role-part")) {
      component = factory.getRolePart(uol, run, user, identifier);
    }
    else if (nodeName.equals("act")) {
      component = factory.getAct(uol, run, user, identifier);
    }
    else if (nodeName.equals("play")) {
      component = factory.getPlay(uol, run, user, identifier);
    }
    else if (nodeName.equals("learning-design")) {
      component = factory.getUnitOfLearning(uol, run, user, identifier);
    }
    return component;
  }

  /**
   * Sorts the children of a root node based on the visibleSince date of the
   * corresponding completion componenent.
   *
   * @param run Run the Run of the component that should be sorted
   * @param user User the User of the component that should be sorted
   * @param root Element the root node containing the children that should be
   *   sorted
   * @throws PropertyException when the operation fails
   */
  private void sortChildren(Run run, User user, Element root) throws
      PropertyException {
    HashMap result = new HashMap();
    Vector components = new Vector();

    Node child = root.getFirstChild();

    //first build a TreeMap containing the child nodes and remove all child nodes
    while (child != null) {

      Node nextChild = child.getNextSibling();

      //only element nodes are relevant
      if (child.getNodeType() == Node.ELEMENT_NODE) {

        /**
         * Fixed 2004-11-18: Titles were treated the same as all other sortable items. This resulted in an exception.
         * Titles are now not part of the sorting process anymore.
         */
        if (!"title".equals(child.getNodeName())) {

          //add the child node with the corresponding completion components as key
          LocalPersonalContent comp = getComponent(run, user, (Element) child);
          result.put(comp, child);

          //determine where the new component has to be put
          int insertPos = -1;

          for (int i = 0; i < components.size(); i++) {
            CompletionComponent current = (CompletionComponent) components.get(
                i);

            //determine were we must insert the component
            if (comp.getVisibilitySince().before(current.getVisibilitySince())) {
              insertPos = i;
              break;
            }
          }

          //insert the new component at the correct spot
          if (insertPos == -1) {
            //add this element at the last position
            components.add(comp);
          }
          else {
            //insert the element at the specified position
            components.insertElementAt(comp, insertPos);
          }

          //remove the child node from the tree
          root.removeChild(child);
        }
      }
      child = nextChild;
    }

    //second reconstruct all child nodes in a sorted fashion
    Iterator iter = components.iterator();
    while (iter.hasNext()) {
      //fetch the key sorted by first access date
      CompletionComponent item = (CompletionComponent) iter.next();

      //rebuild the tree with children
      root.appendChild( (Node) result.get(item));
    }
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
