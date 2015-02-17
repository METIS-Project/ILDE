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
 * Copyright (C) 2003, 2004 Harrie Martens and Hubert Vogten
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

package org.coppercore.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.coppercore.common.HomeFactory;
import org.coppercore.common.ServiceLocator;
import org.coppercore.events.EventDto;
import org.coppercore.events.EventEntity;
import org.coppercore.events.EventEntityHome;
import org.coppercore.events.EventEntityPK;
import org.coppercore.exceptions.NotFoundException;
import org.coppercore.exceptions.ServiceLocatorException;

/**
 * The <code>Event</code> business object represents the persisted events of CopperCore.
 *
 * <p> Instances of this class can only be created by calling either the create or finder class
 * methods.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2005/01/12 14:34:31 $
 */
public class Event implements Serializable {
  private static final long serialVersionUID = 42L;
  private EventDto dto;
  private static EventEntityHome eventHome = null;

  /**
   * The contructor for a new Uol instance.
   *
   * <p>The constructor is private to enforce the usage of the class factory methods to create a new
   * run. Use Uol.create to create a new rnu in the database, use the Uol.find* methods to lookup
   * existing runs from the database.
   *
   * @param dto a UolDto containing the unit of learning properties.
   */
  private Event(EventDto dto) {
    this.dto = dto;
  }

  /**
   * Returns the dto of this unit of learning.
   *
   * @return a UolDto containing all properties of this unit of learning.
   */
  public EventDto getDto() {
    return dto;
  }

  /**
   * Returns the uol of the event.
   *
   * @throws NotFoundException  when the Uol could not be found
   * @return a Uol for which this event was defined.
   */
  public Uol getUol() throws NotFoundException {
    return Uol.findByPrimaryKey(dto.getUolId());
  }

  /**
   * Returns the trigger id of the event which represents the local identifier
   * of the trigger. of the component. For some events this field is null (e.g.
   * timed events).
   *
   * @return a String containing the trigger id.
   */
  public String getTriggerId() {
    return dto.getTriggerId();
  }

  /**
   * Returns the type of the event. Defined types are: COMPLETION_EVENT
   * ,TIMER_EVENT, START_EVENT, PROPERTY_EVENT, ROLE_EVENT.
   *
   * @return a String representing the type of this event.
   */
  public String getType() {
    return dto.getType();
  }

  /**
   * Returns the id of the event.
   *
   * @return an int containing the id of the event.
   */
  public int getId() {
    return dto.getId();
  }

  /**
   * Returns the class of the receiver of this event. Currently only the class "expression" in supported.
   *
   * @return a String representing the class of the receiver of this event.
   */
  public String getClassName() {
    return dto.getClassName();
  }

  /**
   * Returns the component id of the component that will be notified of this
   * event when it was triggered.
   *
   * @return a String representing the class of the receiver of this event.
   */
  public String getComponentId() {
    return dto.getComponentId();
  }
/**
  private void persist() {
    EventEntityPK pk = new EventEntityPK(dto.getId());
    try {
      EventEntity event = getEventHome().findByPrimaryKey(pk);
      event.setDto(dto);
    }
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }
*/
  /**
   * Creates a new Event business object.
   *
   * <p>The event is created according to the parameters that are specified. The
   *  new event is persisted in the database.
   *
   * @param uolId int specifying the id of the unit of learning for which this event is defined.
   * @param triggerId String specifying the local name of the trigger for this event. This parameter may be ommitted e.g. in case of timed events where there is no clear trigger.
   * @param type String specifying the type of this event.
   * @param className String specifying the class of component that will be triggered by this event. Currently only expressions are supported.
   * @param componentId String specifying the id of the component that will be triggered by this event.
   * @return the newly created event.
   */
  public static Event create(int uolId, String triggerId, String type,
                             String className, String componentId) {
    try {
      EventDto dto = new EventDto(uolId, triggerId, type, className,
                                  componentId);
      EventEntity eventEntity = getEventHome().create(dto);
      return new Event(eventEntity.getDto());
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Returns a <code>Event</code> object representing the event with id eventId.
   *
   * <p>If the specified event cannot be located a NotFoundException is thrown.
   *
   * @param eventId an int specifying the event to find.
   * @throws NotFoundException when the specified uevent cannot be located.
   * @return a Event object representing the event.
   */
  public static Event findByPrimaryKey(int eventId) throws NotFoundException {
    EventEntityPK pk = new EventEntityPK(eventId);
    try {
      EventEntity eventEntity = getEventHome().findByPrimaryKey(pk);
      return new Event(eventEntity.getDto());
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (ServiceLocatorException slex) {
      throw new EJBException(slex);
    }
    catch (FinderException fex) {
      throw new NotFoundException(fex);
    }
  }

  /**
   * Returns a <code>Collection</code> of events defined for the passed unit of
   * learning.
   *
   * @param uol an Uol specifying the unit of learning for which this event was
   *   defined.
   * @return a Collection containing the found events. This collection may be
   *   empty if no events were found.
   */
  public static Collection findByUol(Uol uol) {
    ArrayList result = new ArrayList();
    try {
      Collection allEvents = getEventHome().findByUol(uol.getId());
      Iterator i = allEvents.iterator();
      while (i.hasNext()) {
        EventEntity item = (EventEntity) i.next();
        result.add(new Event(item.getDto()));
      }
      return result;
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Returns a <code>Collection</code> of events defined for the passed foreign
   * key of the property definition table and the type of event.
   *
   * @param propDefPk an int specifying primary key of the property definition
   *   table entry containing the definition of the component triggering this
   *   event.
   * @param type a String representing the type of event to be found.
   * @return a Collection containing the found events. This collection may be
   *   empty if no events were found.
   */
  public static Collection findByPropDefPk(int propDefPk, String type) {
    ArrayList result = new ArrayList();
    try {
      Collection allEvents = getEventHome().findByPropDefPK(propDefPk, type);
      Iterator i = allEvents.iterator();
      while (i.hasNext()) {
        EventEntity item = (EventEntity) i.next();
        result.add(new Event(item.getDto()));
      }
      return result;
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Returns a <code>Collection</code> of events defined for the passed unit of
   * learning and type.
   *
   * @param uol an int specifying primary key of the property definition table
   *   entry containing the definition of the component triggering this event.
   * @param type a String representing the type of event to be found.
   * @return a Collection containing the found events. This collection may be
   *   empty if no events were found.
   */
  public static Collection findByType(Uol uol, String type) {
    ArrayList result = new ArrayList();
    try {
      Collection allEvents = getEventHome().findByType(uol.getId(), type);
      Iterator i = allEvents.iterator();
      while (i.hasNext()) {
        EventEntity item = (EventEntity) i.next();
        result.add(new Event(item.getDto()));
      }
      return result;
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Removes all events defined for the passed unit of learning.
   *
   * @param uol an int specifying the primary key of the unit of learning.
   */
  public static void remove(Uol uol) {
    Collection allEvents = null;
    try {
      allEvents = getEventHome().findByUol(uol.getId());
      Iterator i = allEvents.iterator();
      while (i.hasNext()) {
        EventEntity item = (EventEntity) i.next();
        item.remove();
      }
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  private static EventEntityHome getEventHome() throws
      ServiceLocatorException {
    // Because looking up a home interface is an expensive operation, fetch it only once.
    if (eventHome == null) {
      // First time, fetch a new home interface
      ServiceLocator locator = ServiceLocator.getInstance();
      eventHome = (EventEntityHome) locator.getEjbLocalHome(HomeFactory.
          EVENTENTITY);
    }
    return eventHome;
  }

}
