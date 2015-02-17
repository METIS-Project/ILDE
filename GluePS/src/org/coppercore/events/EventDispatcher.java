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

package org.coppercore.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.coppercore.business.Event;
import org.coppercore.business.Run;
import org.coppercore.business.RunParticipation;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.component.ComponentFactory;
import org.coppercore.component.ExpressionPropertyDef;
import org.coppercore.component.PropertyDef;
import org.coppercore.component.StaticProperty;
import org.coppercore.exceptions.NotFoundException;
import org.coppercore.exceptions.PropertyException;

/**
 * The EventDispatecher is responsible for forwarding all events to the correct event handlers.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.5 $, $Date: 2005/07/12 20:28:58 $
 */
public class EventDispatcher {
  //these are the event types that can occur
  /** Event fired when a learning design item is completed. */
  public final static String COMPLETION_EVENT = "completion";
  /** Event fired when a timer elaspses. */
  public final static String TIMER_EVENT = "timer";
  /** Event fired when ??. */
  public final static String START_EVENT = "start";
  /** Event fired when a property is changed. */
  public final static String PROPERTY_EVENT = "property";
  /** Event fired when a user switches from role. */
  public final static String ROLE_EVENT = "role";

  private static EventDispatcher dispatcher = null;

  /**
   * Returns a singleton EventDispatcher.<p>Only one eventdispatcher is created.
   * @return EventDispatcher the single event dispatcher for this class
   */
  public static EventDispatcher getEventDispatcher() {
  if (dispatcher == null) {
      dispatcher = new EventDispatcher();
    }
    return dispatcher;
  }

  public EventDispatcher() {
    //default constructor
  }

  /**
   * This method receives a new event and fetches all conditions to be fired.
   *
   * <p> The stack of all conditions fired is reset.
   *
   * @param uol Uol the unit of learning this event is fired from
   * @param run Run the run this event is fired from
   * @param user User the user this event is fired from
   * @param sender StaticProperty the originating item the event stems from
   * @param type String the type of event that is sent
   * @throws PropertyException when there is an error dealing with properties
   */
  public void postMessage(Uol uol, Run run, User user,
                          StaticProperty sender, String type) throws
      PropertyException {

    postMessage(uol, run, user, sender, type, new ArrayList());
  }

  /**
   * This method receives a new event and fetches all conditions to be fired.
   *
   * @param uol Uol the unit of learning this event is fired from
   * @param run Run the run this event is fired from
   * @param user User the user this event is fired from
   * @param sender StaticProperty the originating item the event stems from
   * @param type String the type of event that is sent
   * @param conditionsFired Collection a list of al conditions that fired to
   *   prevent performing the same condition
   * @throws PropertyException when there is an error dealing with properties
   */
  public void postMessage(Uol uol, Run run, User user,
  StaticProperty sender, String type,
                          Collection conditionsFired) throws PropertyException {

    try {

      //processing of events will differ depending on the type of event. So we
      //have to check with which type of event we are dealing
      if (PROPERTY_EVENT.equals(type) ||
          COMPLETION_EVENT.equals(type)) {

        boolean globalSender = (PropertyDef.ISLOCAL & sender.getScope()) == 0;

        //get all relevant events from the global eventlist.
        //the uol  is not part of the search criterium because there is only
        //one owner uol defining a global property. So the propDefPK is
        //the same although local naming may differ !!!
        Collection events = Event.findByPropDefPk(sender.getPropDefPK(), type);
    
        Iterator iter = events.iterator();
        while (iter.hasNext()) {
          Event event = (Event) iter.next();

          //check the scope of this property. Eventhandling is different for global and local scopes
          if (globalSender) {
            //the send was a global property

            //now fetch all the runs for which this user is assigned in this uol
            Collection rps = RunParticipation.findByUser(user, event.getUol());
            Iterator it = rps.iterator();
            while (it.hasNext()) {
              //we need the runparticipation for the retreiving the run
              RunParticipation rp = (RunParticipation) it.next();

              //notifiy the receiver
              sendEvent(event.getUol(), rp.getRun(), user, sender, event,
                        conditionsFired);
            }
          }
          else {
            //this is a local property, so simply inform the receiver that an event was raised

            //notify the receiver
            sendEvent(uol, run, user, sender, event, conditionsFired);
          }
        }
      }

      else if (TIMER_EVENT.equals(type)) {
        //process the timer event which differs from the others because there is no sender context.

        //fetch all time dependent events per uol
        Collection events = Event.findByType(uol, type);

        //next fetch all runs for this uol
        Collection runs = Run.findByUol(uol);

        //loop over all the events
        Iterator iter1 = events.iterator();
        while (iter1.hasNext()) {
          Event event = (Event) iter1.next();

          Iterator iter2 = runs.iterator();
          while (iter2.hasNext()) {
            Run nextRun = (Run) iter2.next();

            //now we must fetch all the users per run
            Collection runParticipations = RunParticipation.findByRun(nextRun);

            //now we must notify the receiver with this context
            Iterator iter3 = runParticipations.iterator();
            while (iter3.hasNext()) {
              RunParticipation rp = (RunParticipation) iter3.next();

              //notify the receiver
              sendEvent(uol, nextRun, rp.getUser(), sender, event,
                        conditionsFired);
            }
          }
        }
      }

      else if (ROLE_EVENT.equals(type)  ||
               START_EVENT.equals(type)) {

        //fetch all role dependent events per uol
        Collection events = Event.findByType(uol, type);

        Iterator iter = events.iterator();
        while (iter.hasNext()) {
          Event event = (Event)iter.next();

          //notify the receiver
          sendEvent(uol, run, user, sender, event,
                    conditionsFired);
        }
      }
    }
    catch (NotFoundException ex) {
      throw new PropertyException(ex);
    }
  }

  private void sendEvent(Uol uol, Run run, User user,
                         StaticProperty sender, Event event,
                         Collection conditionsFired) throws PropertyException {

    //for now we are only able to deal with expressions
    if (ExpressionPropertyDef.DATATYPE.equals(event.getClassName())) {
      StaticProperty target = ComponentFactory.getPropertyFactory().
          getExpression(uol, event.getComponentId());

      target.processEvent(uol, run, user, event, sender,
                          conditionsFired);
    }
    else {
      Logger logger = Logger.getLogger(this.getClass());
      logger.fatal("postMessage can not process " + event);
      throw new EJBException("postMessage can not process " + event);
    }
  }
}
