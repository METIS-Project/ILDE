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

package org.coppercore.condition;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.coppercore.business.Event;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.Parser;
import org.coppercore.common.XMLTag;
import org.coppercore.component.ExpressionElement;
import org.coppercore.component.StaticProperty;
import org.coppercore.events.EventDispatcher;
import org.coppercore.exceptions.ComponentDataException;
import org.coppercore.exceptions.ExpressionException;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;

public class AllConditions extends ConditionRoot {
  private static final long serialVersionUID = 1L;
  public final static String CONDITIONS_ID = "_conditions";

  private ArrayList conditions = new ArrayList();

  private HashMap propertyTriggerMap = null;

  private HashMap timerTriggerMap = null;

  private HashMap completionTriggerMap = null;

  private HashMap startTriggerMap = null;

  private HashMap roleTriggerMap = null;

  public AllConditions() {
    //default constructor    
  }

  public ExpressionElement addElement(Element node, int uolId) throws PropertyException {
    if ("conditions".equals(node.getNodeName())) {
      Conditions cond = new Conditions();
      addConditions(cond);
      return cond;
    }

    //a fall trough indicates that an element was not recognized, so throw exception
    throw new ComponentDataException("Invalid expression format encounted: " + Parser.documentToString(node));
  }

  public void addConditions(Conditions theConditions) {
    if (theConditions != null) {
      this.conditions.add(theConditions);
    }
  }

  public void toXml(PrintWriter out) {
    XMLTag root = new XMLTag("all-conditions");

    root.writeOpenTag(out);

    Iterator iter = conditions.iterator();
    while (iter.hasNext()) {
      Conditions cond = (Conditions) iter.next();
      cond.toXml(out);
    }

    root.writeCloseTag(out);
  }

  public void buildTriggers() {
    //reset the hashMap containing the triggers
    propertyTriggerMap = new HashMap(100);
    timerTriggerMap = new HashMap(100);
    completionTriggerMap = new HashMap(100);
    startTriggerMap = new HashMap(100);
    roleTriggerMap = new HashMap(100);

    Iterator iter = conditions.iterator();
    while (iter.hasNext()) {
      Conditions cond = (Conditions) iter.next();
      cond.getTriggers(propertyTriggerMap, timerTriggerMap, completionTriggerMap, startTriggerMap, roleTriggerMap);
    }
  }

  public HashMap getTriggers() {
    HashMap result = new HashMap(4);

    if (propertyTriggerMap == null || timerTriggerMap == null || completionTriggerMap == null
        || startTriggerMap == null) {
      buildTriggers();
    }

    //add the triggers to the hash map of triggers, per type of trigger (key)
    result.put(EventDispatcher.PROPERTY_EVENT, propertyTriggerMap.keySet());
    result.put(EventDispatcher.TIMER_EVENT, timerTriggerMap.keySet());
    result.put(EventDispatcher.COMPLETION_EVENT, completionTriggerMap.keySet());
    result.put(EventDispatcher.START_EVENT, startTriggerMap.keySet());
    result.put(EventDispatcher.ROLE_EVENT, roleTriggerMap.keySet());

    return result;
  }

  /**
   * Returns this Expression as an XML representation.
   *
   * @return String
   */
  public String toXml() {
    StringWriter outputStream = new StringWriter();
    try {
      PrintWriter output = new PrintWriter(outputStream);
      try {
        toXml(output);
        return outputStream.toString();
      }
      catch (Exception e) {
        throw new EJBException(e);
      }
      finally {
        output.close();
      }
    }
    finally {
      try {
        outputStream.close();
      }
      catch (IOException ex) {
        throw new EJBException(ex);
      }
    }
  }

  public void evaluateAll(Uol uol, Run run, User user) throws PropertyException {
    Iterator iter = conditions.iterator();
    while (iter.hasNext()) {
      Conditions conds = (Conditions) iter.next();
      conds.evaluateAll(uol, run, user);
    }
  }

  public void processEvent(Uol uol, Run run, User user, Event event, StaticProperty trigger, Collection firedActions)
      throws PropertyException {
    {
      Collection conditionsToEvaluate = null;
      //determine which conditions should be evaluated
      if (EventDispatcher.COMPLETION_EVENT.equals(event.getType())) {
        conditionsToEvaluate = (Collection) completionTriggerMap.get(trigger.getIdentifier());
      }
      else if (EventDispatcher.TIMER_EVENT.equals(event.getType())) {
        /** @todo check if we really do need the trigger property now we are passing
         *  the event itself. Furthermore is this code correct in case of a global
         *  property triggering an event for a different uol. This same global
         * property will have a different triggerid here. So should we use this
         * triggerid instead??? **/
        conditionsToEvaluate = (Collection) timerTriggerMap.get(event.getTriggerId());
      }
      else if (EventDispatcher.START_EVENT.equals(event.getType())) {
        /** todo Changed trigger.getIdentifier() to event.getTriggerId().
         *  Needs to be verified. See remark above */
        conditionsToEvaluate = (Collection) startTriggerMap.get(event.getTriggerId());
      }
      else if (EventDispatcher.PROPERTY_EVENT.equals(event.getType())) {
        /** todo Changed trigger.getIdentifier() to event.getTriggerId().
         *  Needs to be verified. See remark above */
        conditionsToEvaluate = (Collection) propertyTriggerMap.get(event.getTriggerId());
      }
      else if (EventDispatcher.ROLE_EVENT.equals(event.getType())) {
        /** todo Changed trigger.getIdentifier() to event.getTriggerId().
         *  Needs to be verified. See remark above */
        conditionsToEvaluate = (Collection) roleTriggerMap.get(event.getTriggerId());
      }
      if (conditionsToEvaluate != null) {

        Iterator iter = conditionsToEvaluate.iterator();
        while (iter.hasNext()) {

          Condition condition = (Condition) iter.next();
          try {
            //check if there was a trigger. If a trigger property was causing
            //the evaluation of these conditions, we should take the scope of
            //the trigger into account when propagating the event
            if (trigger != null) {
              //get all the users for whom this conditions needs evaluation
              //based on the trigger
              Collection users = trigger.getUsersInScope();
              Iterator it = users.iterator();
              while (it.hasNext()) {
                User userInScope = (User) it.next();
                condition.evaluate(uol, run, userInScope, firedActions);
              }
            }
            else {
              //there wasn't a trigger. So we assume that the triggering mechanism
              //takes care of the correct propagation of events to all the involved
              //users
              condition.evaluate(uol, run, user, firedActions);
            }
          }
          catch (ExpressionException ex) {
            //Expression exception can be serious but are not fatal.
            //So we continue with the evaluation
            Logger logger = Logger.getLogger(this.getClass());
            logger.error(ex);
          }
        }
      }
      else {
        //this should not occur although it is not fatal
        Logger logger = Logger.getLogger(this.getClass());
        logger.error("Event raised for condition element, but no trigger found!");
      }
    }
  }
}
