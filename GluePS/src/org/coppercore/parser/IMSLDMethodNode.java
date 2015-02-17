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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.coppercore.business.Event;
import org.coppercore.common.MessageList;
import org.coppercore.component.ExpressionPropertyDef;
import org.coppercore.condition.AllConditions;
import org.coppercore.condition.Conditions;
import org.coppercore.exceptions.CopperCoreException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * This class represents an IMS learning design method element.  
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.20 $, $Date: 2005/01/21 14:01:11 $
 */
public class IMSLDMethodNode extends IMSLDNode {
  private IMSLDCompleteUnitOfLearningNode completeUnitOfLearning = null;
  private ArrayList conditions = new ArrayList();
  private AllConditions allConditions = new AllConditions();

  /**
   * Constructs a IMSLDMethodNode instance from the passed xml dom element.
   * 
   * @param aNode the xml dom node to parse
   * @param aParent the parsed IMS learning design element that is the
   * parent element of this object
   */
  public IMSLDMethodNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);
  }

  public void parse() throws Exception {
    super.parse();
  }

  protected void parseLDElement(Element childNode, String nodeName) throws Exception {
    super.parseLDElement(childNode, nodeName);

    if (nodeName.equals("play")) {
      addElement(new IMSLDPlayNode(childNode, this));
    }
    else if (nodeName.equals("complete-unit-of-learning")) {
      completeUnitOfLearning = (IMSLDCompleteUnitOfLearningNode) addElement(new
          IMSLDCompleteUnitOfLearningNode(childNode, this));
    }
    else if (nodeName.equals("on-completion")) {
      addElement(new IMSLDOnCompletionNode(childNode, this));
    }
    else if (nodeName.equals("conditions")) {
      conditions.add(addElement(new IMSLDConditionsNode(childNode, this)));
    }

  }

  protected void persist(int uolId) throws CopperCoreException {
    //persist the expression property def containing all conditions!
    ExpressionPropertyDef propertyDef = new ExpressionPropertyDef(uolId,
        AllConditions.CONDITIONS_ID, allConditions);
    propertyDef.persist();

    HashMap triggers = allConditions.getTriggers();
    Iterator iter = triggers.keySet().iterator();
    while (iter.hasNext()) {
      //store the key as we need it to type the event we are listening for
      String eventType = (String) iter.next();

      //these are the triggers for this event type
      Collection triggerSet = (Collection) triggers.get(eventType);

      //create the index of all triggers of this type for the EventDispatcher
      Iterator iter2 = triggerSet.iterator();
      while (iter2.hasNext()) {
        //get the triggerId to be put on the eventDispatcher event list
        String triggerId = (String) iter2.next();

        //persist a new event handler dealing causing a re-evaluation of a
        //condition.
        Event.create(uolId, triggerId,
            eventType, ExpressionPropertyDef.DATATYPE,
            AllConditions.CONDITIONS_ID);
      }
    }

    //make sure all children are persisted as well
    super.persist(uolId);
  }

/*  protected void getReferingRoleIds(IMSLDNode aNode, Set roles) {
    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();

      if (child instanceof IMSLDPlayNode) {
        child.getReferingRoleIds(aNode, roles);
      }
    }
  }
*/
  /**
   * Return the IMSLDCompleteUnitOfLearningNode if present, null otherwise.
   * @return the IMSLDCompleteUnitOfLearningNode if present, null otherwise.
   */
  protected IMSLDCompleteUnitOfLearningNode getCompleteUnitOfLearning() {
    return completeUnitOfLearning;
  }

  protected boolean buildComponentModel(int uolId, MessageList messages) {
    //build up all conditions
    boolean result = super.buildComponentModel(uolId, messages);

    //create a conditions wrapper for system defined conditions
    Conditions systemConditions = new Conditions("Container for CopperCore system conditions", null);

    //now collect all system defined conditions
    parent.getSystemConditions(systemConditions);

    //add the system conditions to the collection of conditions
    allConditions.addConditions(systemConditions);

    //collect all user defined conditions
    Iterator iter = conditions.iterator();
    while (iter.hasNext()) {
      IMSLDConditionsNode conds = (IMSLDConditionsNode) iter.next();
      allConditions.addConditions(conds.getConditions());
    }
    return result;
  }

  protected void writeXMLContent(PrintWriter output) {

    Iterator myIterator = children.iterator();
    while (myIterator.hasNext()) {
      IMSLDNode childNode = (IMSLDNode) myIterator.next();
      if (childNode instanceof IMSLDOnCompletionNode) {
        childNode.writeXMLContent(output);
      }
    }
  }

  protected void writeXMLPlay(PrintWriter output, IMSLDNode aRole) {
    Iterator myIterator = children.iterator();

    while (myIterator.hasNext()) {
      IMSLDNode child = (IMSLDNode) myIterator.next();

      if (child instanceof IMSLDPlayNode) {
        child.writeXMLPlay(output, aRole);
      }
    }
  }

}
