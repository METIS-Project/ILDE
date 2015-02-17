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
import org.coppercore.component.ActivityStructurePropertyDef;
import org.coppercore.component.CompletionComponent;
import org.coppercore.component.EnvironmentTreePropertyDef;
import org.coppercore.condition.CompleteForUser;
import org.coppercore.condition.Condition;
import org.coppercore.condition.Conditions;
import org.coppercore.condition.ThenActionList;
import org.coppercore.exceptions.CopperCoreException;
import org.coppercore.expression.Complete;
import org.coppercore.expression.CountExpression;
import org.coppercore.expression.GreaterThanExpression;
import org.coppercore.expression.IfExpression;
import org.coppercore.expression.PropertyConstant;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import glueps.core.model.Activity;

/**
 * This class represents an IMS Learning Design activity-structure element.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.23 $, $Date: 2005/01/11 13:15:08 $
 */
public class IMSLDActivityStructureNode extends IMSLDNode {
  private ActivityStructurePropertyDef activityStructure = null;

  private Condition activityStructureCondition = null;

  private EnvironmentTreePropertyDef environmentTree = null;

  private Vector environments = new Vector();

  private Vector activities = new Vector();

  public Vector getActivities() {
	return activities;
}

private int numberToSelect = -1;

  private String sort;

  private String structureType;

  /**
   * Constructs an IMSLDActivitiesNode instance for the passed xml dom element.
   * 
   * @param aNode
   *          Node the xml dom node to parse
   * @param aParent
   *          IMSLDNode the parsed IMS learing design element that is the parent
   *          element of this object
   */
  public IMSLDActivityStructureNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);

    //get all the specific attribute for this element
    String attr = getNamedAttribute("number-to-select");
    if (attr != null) {
      this.numberToSelect = Integer.parseInt(attr);
    }
    sort = getNamedAttribute("sort");
    structureType = getNamedAttribute("structure-type");
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("environment-ref")) {
      //we cannot solve reference directly, so use reference for now
      environments.add(getNamedAttribute(childNode, "ref"));
    }
    else if (nodeName.equals("information")) {
      addElement(new IMSLDInformationNode(childNode, this));
    }
    else if (nodeName.equals("learning-activity-ref") || nodeName.equals("support-activity-ref")
        || nodeName.equals("activity-structure-ref")) {
      //we cannot solve reference directly, so use reference for now
      activities.add(getNamedAttribute(childNode, "ref"));
    }
  }

  protected void resolveReferences() throws Exception {

    lookupAll(environments);
    lookupAll(activities);

    super.resolveReferences();
  }

  protected String getNameForCompleteExpression() {
    return "activity-structure";
  }

  /**
   * Checks if the activity-structure contains the passed node.
   * 
   * <p>
   * The method also returns true if the passed node equals the current
   * instance. The search is recursive, if a child of this activity-structure is
   * an activity-structure itself the search continues in that child.
   * 
   * @param aNode
   *          IMSLDNode the node to search for
   * @return boolean true if this activity-structure contains the specified node
   */
  protected boolean containsNode(IMSLDNode aNode) {
    boolean result = false;

    if (aNode == this) {
      result = true;
    }
    else {
      Iterator myIterator = activities.iterator();

      while (!result && myIterator.hasNext()) {
        IMSLDNode activity = (IMSLDNode) myIterator.next();

        result = result || (activity == aNode);

        if (!result && activity instanceof IMSLDActivityStructureNode) {
          result = result || ((IMSLDActivityStructureNode) activity).containsNode(aNode);
        }
      }
    }

    return result;
  }

  protected void persist(int uolId) throws CopperCoreException {
    activityStructure.persist();
    environmentTree.persist();

    //make sure all children are persisted as well
    super.persist(uolId);
  }

  /**
   * Returns the number-to-select attribute of the activity-structure.
   * 
   * <p>
   * If the attribute was not defined in the learning design manifest for this
   * activity-structure, the method returns the count of activities for this
   * structure.
   * 
   * @return int the number-to-select attribute of the activity-structure
   */
  protected int getNumberToSelect() {
    int result;
    if (numberToSelect > 0) {
      result = numberToSelect;
    }
    else {
      result = activities.size();
    }
    return result;
  }

  /**
   * Returns the type of this activity-structure.
   * <p>
   * The structure type is either 'sequence' or 'selection'.
   * 
   * @return String the type of this activity-structure
   */
  protected String getStructureType() {
    return structureType;
  }

  /**
   * Returns the sort order of this activity-structure.
   * <p>
   * Allowed values are 'as-is' or 'visibilty-order'.
   * 
   * @return String the sort order of the activity-structure
   */
  protected String getSort() {
    return sort;
  }

  protected void buildItemList(final String componentId, HashMap usedItems, HashMap usedIn,
      final HashSet referencedItems, String dataType) {
    //recursively pick up on items belonging to this component
    super.buildItemList(getIdentifier(), usedItems, usedIn, referencedItems, ActivityStructurePropertyDef.DATATYPE);
  }

  protected boolean buildComponentModel(int uolId, MessageList messages) {
    super.buildComponentModel(uolId, messages);

    //build the activity structure component
    activityStructure = new ActivityStructurePropertyDef(uolId, getIdentifier(), CompletionComponent.NOTCOMPLETED,
        getXMLContent(), getItemsForComponent(getIdentifier()));

    //build the environment tree component
    environmentTree = new EnvironmentTreePropertyDef(uolId, getIdentifier(), getXMLEnvironmentTree());

    //build the condition
    IfExpression ifExpression = new IfExpression();
    GreaterThanExpression greaterThan = new GreaterThanExpression();
    CountExpression count = new CountExpression();

    Iterator iter = activities.iterator();
    while (iter.hasNext()) {
      IMSLDNode activity = (IMSLDNode) iter.next();
      count.addOperand(new Complete(activity.getIdentifier(), activity.getNameForCompleteExpression()));

    }
    greaterThan.addOperand(count);
    greaterThan.addOperand(new PropertyConstant((getNumberToSelect() - 1)));
    ifExpression.addOperand(greaterThan);

    //build the then action
    ThenActionList actionList = new ThenActionList();
    actionList.addThenAction(new CompleteForUser(activityStructure));

    //now finally we can build the complete rolepart condition
    activityStructureCondition = new Condition(ifExpression, actionList);

    return true;
  }

  protected void getSystemConditions(Conditions conditions) {
    super.getSystemConditions(conditions);

    //add the complete conditions to the conditions container
    conditions.addCondition(activityStructureCondition);
  }

  protected void writeXMLPlay(PrintWriter output, IMSLDNode aRole, ArrayList envIds) {

    //if there are any environments we should add the environments id reference
    //to the relevant list of environments for all the children
    if (!environments.isEmpty()) {
      envIds.add(0, getIdentifier());
    }

    XMLTag tag = new XMLTag("activity-structure");
    tag.addAttribute("identifier", getIdentifier());
    if (getNumberToSelect() > 0) {
      tag.addAttribute("number-to-select", String.valueOf(getNumberToSelect()));
    }
    tag.addAttribute("sort", getSort());
    tag.addAttribute("structure-type", getStructureType());
    tag.addAttribute("completed", CompletionComponent.NOTCOMPLETED);
    tag.addAttribute("environment", envIds);
    tag.writeOpenTag(output);

    //treat the title seperately
    if (title != null) {
      title.writeXMLPlay(output, aRole, envIds);
    }

    Iterator myIterator = activities.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();
      child.writeXMLPlay(output, aRole, envIds);
    }
    tag.writeCloseTag(output);

    //if there are any environments we now must remove this environment again
    if (!environments.isEmpty()) {
      envIds.remove(0);
    }
  }

  protected void writeXMLContent(PrintWriter output) {
    XMLTag tag = new XMLTag("activity-structure");

    //used the activity contextId as identifier
    tag.addAttribute("identifier", getIdentifier());
    tag.addAttribute("structure-type", getStructureType());
    tag.writeOpenTag(output);

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();
      childNode.writeXMLContent(output);
    }
    tag.writeCloseTag(output);
  }

  protected void writeXMLEnvironmentTree(PrintWriter output) {

    //loop over all children of type IMSLDEnviromentNode
    Iterator myIterator = environments.iterator();
    while (myIterator.hasNext()) {
      IMSLDEnvironmentNode child = (IMSLDEnvironmentNode) myIterator.next();
      child.writeXMLEnvironmentTree(output);
    }
  }

	public Activity getActivityStructureAsTreeNode(IMSCPManifestNode imscpManifestNode, String unpackedPath) {
		Activity activityStructureNode = new Activity();
		// look recursively for the children activities and activity structures

		ArrayList<Activity> childrenActivities = new ArrayList<Activity>();
		Iterator it = this.getActivities().iterator();

		while (it.hasNext()) {
			IMSLDNode child = (IMSLDNode) it.next();
			if (child.node.getLocalName().equals("learning-activity")
					|| child.node.getLocalName().equals("support-activity")) {
				Activity activityNode = new Activity();

				//old, bad algorithm
				//activityNode = new Activity(child.getIdentifier(),child.getTitle(), "", null, null, null, null, null);
				
				activityNode = getActivityAsTreeNode(child.getIdentifier(), imscpManifestNode, unpackedPath);
				
				
				childrenActivities.add(activityNode);
			} else if (child.node.getLocalName().equals("activity-structure")) {
				IMSLDActivityStructureNode childStructure = (IMSLDActivityStructureNode) child;
				Activity activityNode = childStructure
						.getActivityStructureAsTreeNode(imscpManifestNode, unpackedPath);
				childrenActivities.add(activityNode);
			}

		}

		// TODO Extracting the parent is best done later, once the activity tree
		// is complete
		// TODO The mode is quite difficult to extract from IMS-LD, leave it
		// empty for now
		activityStructureNode = new Activity(this.getIdentifier(),
				this.getTitle(), null, null, null, null, null,
				null);

		if (!childrenActivities.isEmpty())
			activityStructureNode.setChildrenActivities(childrenActivities);

		// TODO Correct the next activity fields using .getStructureType? by now, the order is defined by the position in the arraylist
		return activityStructureNode;
	}
}
