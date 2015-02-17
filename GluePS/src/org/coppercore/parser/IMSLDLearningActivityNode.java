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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.coppercore.common.MessageList;
import org.coppercore.common.XMLTag;
import org.coppercore.component.CompletionComponent;
import org.coppercore.component.CompletionComponentDef;
import org.coppercore.component.EnvironmentTreePropertyDef;
import org.coppercore.component.LearningActivityPropertyDef;
import org.coppercore.exceptions.CopperCoreException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design learning-activity element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.22 $, $Date: 2005/06/16 22:09:56 $
 */
public class IMSLDLearningActivityNode
    extends IMSLDNode {
  private Vector environments = new Vector();
  
  protected Vector getEnvironments() {
	return environments;
}

private CompletionComponentDef componentDef = null;
  private EnvironmentTreePropertyDef environmentTree = null;
  /** Contains the complete-activity element for this activity element. */
  protected IMSLDCompleteActivityNode completeActivityNode = null;

  /**
   * Constructs a IMSLDLearningActivityNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDLearningActivityNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("learning-objectives")) {
      addElement(new IMSLDLearningObjectivesNode(childNode, this));
    }
    else if (nodeName.equals("prerequisites")) {
      addElement(new IMSLDPrerequisitesNode(childNode, this));
    }
    else if (nodeName.equals("environment-ref")) {
      //we cannot solve reference directly, so use reference for now
      environments.add(getNamedAttribute(childNode,"ref"));
    }
    else if (nodeName.equals("activity-description")) {
      addElement(new IMSLDActivityDescriptionNode(childNode, this));
    }
    else if (nodeName.equals("complete-activity")) {
      completeActivityNode = (IMSLDCompleteActivityNode) addElement(new
          IMSLDCompleteActivityNode(childNode, this));

    }
    else if (nodeName.equals("on-completion")) {
      addElement(new IMSLDOnCompletionNode(childNode, this));
    }
  }

  protected void persist(int uolId) throws CopperCoreException {
    componentDef.persist();
    environmentTree.persist();

    //make sure all children are persisted as well
    super.persist(uolId);
  }

  protected void resolveReferences() throws Exception {
    lookupAll(environments);
    super.resolveReferences();
  }

  protected String getNameForCompleteExpression() {
    return "learning-activity";
  }

  /**
   * Returns "true" if this learning-activity is completed via a user-choice.
   * @return "true" if this learning-activity is completed via a user-choice, otherwise "false"
   */
  protected String isUserChoice() {
    String result = "false";

    if (completeActivityNode != null) {
      result = ( (completeActivityNode.isUserChoice()) ? "true" : "false");
    }
    return result;
  }

  /**
   * Returns a String representing if this activity is completed via a time-limit.
   * <p>
   * Valid values are "true" and "false".
   * @return a String representing if this activity is completed via a time-limit
   */
  protected String isTimeLimit() {
    String result = "false";

    if (completeActivityNode != null) {
      result = (hasTimeLimit() ? "true" : "false");
    }
    return result;
  }

  /**
   * Returns whether this activity is completed via a time-limit.
   * @return  whether this activity is completed via a time-limit
   */
  protected boolean hasTimeLimit() {
    boolean result = false;

    if (completeActivityNode != null) {
      result = completeActivityNode.hasTimeLimit();
    }
    return result;
  }

  /**
   * Writes xml containing the roles that are supported by the support activity to 
   * the specified output. 
   * @param output the stream to write the xml describing the support roles to
   */
  protected void writeXMLRolesToSupport(PrintWriter output) {
    //do nothing because there are no roles to support
  }

  /**
   * Returns the definition component for this element. 
   * @return the definition component for this element
   */
  CompletionComponentDef getComponentDef() {
    return componentDef;
  }

  CompletionComponentDef buildComponent(int uolId) {
    return new LearningActivityPropertyDef(uolId, getIdentifier(),
                                           (completeActivityNode == null) ?
                                           CompletionComponent.UNLIMITED :
                                           CompletionComponent.NOTCOMPLETED,
                                           getIsVisible(),
                                           getXMLContent(),
                                           getItemsForComponent(getIdentifier()));
  }

  protected void buildItemList(final String componentId, HashMap usedItems, HashMap usedIn, final HashSet referencedItems, String dataType) {
    //recursively pick up on items belonging to this component
    super.buildItemList(getIdentifier(), usedItems, usedIn, referencedItems, LearningActivityPropertyDef.DATATYPE);
  }



  protected boolean buildComponentModel(int uolId, MessageList messages) {

    //create the ActivityPropertyDef now
    componentDef = buildComponent(uolId);

    //build the environment tree component
    environmentTree = new EnvironmentTreePropertyDef(uolId, getIdentifier(), getXMLEnvironmentTree());

    return super.buildComponentModel(uolId, messages);
  } 
  
  protected void writeXMLContent(PrintWriter output) {
    XMLTag tag = new XMLTag(node.getLocalName());

    //used the activity contextId as identifier
    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("isvisible", getIsVisible(), "true");
    tag.addAttribute("parameters",getParameters());
    tag.writeOpenTag(output);

    writeXMLRolesToSupport(output);

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();
      childNode.writeXMLContent(output);
    }

    tag.writeCloseTag(output);
  }

  protected void writeXMLPlay(PrintWriter output, IMSLDNode aRole,
                              ArrayList envIds) {

    //if there are any environments we should add the environments id reference
    //to the relevant list of environments for all the children
    if (!environments.isEmpty()) {
      envIds.add(0,getIdentifier());
    }

    XMLTag tag = new XMLTag(node.getLocalName());

    //used the activity contextId as identifier
    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("isvisible", getIsVisible(), "true");
    tag.addAttribute("user-choice", isUserChoice());
    tag.addAttribute("time-limit", isTimeLimit());
    tag.addAttribute("parameters", getParameters());    

    if (completeActivityNode != null) {
      tag.addAttribute("completed", CompletionComponent.NOTCOMPLETED);
    }
    else {
      tag.addAttribute("completed", CompletionComponent.UNLIMITED);
    }
    tag.addAttribute("environment", envIds);

    tag.writeOpenTag(output);
    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();
      child.writeXMLPlay(output, aRole);
    }
    tag.writeCloseTag(output);

    //if there are any environments we now must remove this environment again
    if (!environments.isEmpty()) {
      envIds.remove(0);
    }
  }

  protected void writeXMLEnvironmentTree(PrintWriter output) {

    //loop over all children of type IMSLDEnviromentNode
    Iterator myIterator = environments.iterator();
    while (myIterator.hasNext()) {
      IMSLDEnvironmentNode child = (IMSLDEnvironmentNode) myIterator.next();
      child.writeXMLEnvironmentTree(output);
    }
  }
}
