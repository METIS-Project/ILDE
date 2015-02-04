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

import java.io.Serializable;

/**
 * This class encapsulates all data of a Event bean.
 *
 * <p> Using a Data Transfer Object improves the performance because it enables
 * the code to set all properties of a bean at once instead of setting them
 * member by member.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.4 $
 */
public class EventDto implements Serializable {
  private static final long serialVersionUID = 42L;
  private int pk;
  private int uolId;
  private String triggerId;
  private String type;
  private String className;
  private String componentId;

  /**
   * Creates a new instance of an EventDto and sets its data to the passed
   * values.
   *
   * @param pk int the primary key for the event
   * @param uolId int the unit of learning this event belongs to
   * @param triggerId String the id of the component that triggers this event
   * @param type String the type of event
   * @param className String the name of the class handling the event
   * @param componentId String @todo fill in comment
   */
  public EventDto(int pk, int uolId, String triggerId, String type,
                  String className,
                  String componentId) {
    this.pk = pk;
    this.uolId = uolId;
    this.triggerId = triggerId;
    this.type = type;
    this.className = className;
    this.componentId = componentId;
  }

  /**
   * Creates a new instance of an EventDto and sets its data to the passed
   * values, but leaves the primary key empty.
   *
   * @param uolId int the unit of learning this event belongs to
   * @param triggerId String the id of the component that triggers this event
   * @param type String the type of event
   * @param className String the name of the class handling the event
   * @param componentId String @todo fill in comment
   */
  public EventDto(int uolId, String triggerId, String type, String className,
                  String componentId) {
    this.uolId = uolId;
    this.triggerId = triggerId;
    this.type = type;
    this.className = className;
    this.componentId = componentId;
  }

  /**
   * Returns the primary key of this event.
   * @return int the primary key of this event
   */
  public int getId() {
    return pk;
  }

  /**
   * Returns the unit of learning id this event belongs to.
   * @return int the unit of learning id this event belongs to
   * @see #setUolId
   */
  public int getUolId() {
    return uolId;
  }

  /**
   * Returns the id of the component that triggers this event.
   * @return String the id of the component that triggers this event
   * @see #setTriggerId
   */
  public String getTriggerId() {
    return triggerId;
  }

  /**
   * Returns the type of this event.
   * @return String the type of this event
   * @see #setType
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the name of the class that handles this event.
   * @return String  the name of the class that handles this event
   * @see #setClassName
   */
  public String getClassName() {
    return className;
  }

  /**
   * Returns the component id.
   * @return String the component id
   * @see #setComponentId
   */
  public String getComponentId() {
    return componentId;
  }

  /**
   * Sets the unit of learning id this event belongs to to the specified value.
   * @param uolId int the id of the unit of learning this event belongs to
   * @see #getUolId
   */
  protected void setUolId(int uolId) {
    this.uolId = uolId;
  }

  /**
   * Sets the trigger id to the passed id of the component that triggers this event.
   * @param triggerId String the id of the triggering component
   * @see #getTriggerId
   */
  protected void setTriggerId(String triggerId) {
    this.triggerId = triggerId;
  }

  /**
   * Sets the type of this event to the specified value.
   * @param type String the type of this event
   * @see #getType
   */
  protected void setType(String type) {
    this.type = type;
  }

  /**
   * Sets the className member to the passed name of the class handling this event.
   * @param className String the name of the class handling this event
   * @see #getClassName
   */
  protected void setClassName(String className) {
    this.className = className;
  }

  /**
   * Sets the component id to the specified value.
   * @param componentId String the new value of the component id
   * @see #getComponentId
   */
  protected void setComponentId(String componentId) {
    this.componentId = componentId;
  }

  /**
   * Compares this EventDto instance to the specified object.<p>Both objects are considered
   * equal if they are both the same type and their respective members are equal.
   * @param obj Object the object to compare this instance with
   * @return boolean if both objects are equal, else returns false
   */
  public boolean equals(Object obj) {
    if (obj != null) {
      if (this.getClass().equals(obj.getClass())) {
        EventDto that = (EventDto) obj;
        return ( ( (this.getUolId() == that.getUolId()) &&
                  ( ( (this.getTriggerId() == null) && (that.getTriggerId() == null)) ||
                   (this.getTriggerId() != null &&
                    this.getTriggerId().equals(that.getTriggerId()))) &&
                  ( ( (this.getType() == null) && (that.getType() == null)) ||
                   (this.getType() != null &&
                    this.getType().equals(that.getType()))) &&
                  ( ( (this.getClassName() == null) && (that.getClassName() == null)) ||
                   (this.getClassName() != null &&
                    this.getClassName().equals(that.getClassName()))) &&
                  ( ( (this.getComponentId() == null) && (that.getComponentId() == null)) ||
                   (this.getComponentId() != null &&
                    this.getComponentId().equals(that.getComponentId())))));
      }
    }
    return false;
  }

  /**
   * Returns a String representation of this event.
   * @return String the representation of this event
   */
  public String toString() {
    return "Event[uolId=" + getUolId() + ",triggerId=" + getTriggerId() +
        ",type=" + getType() + ",className=" +
        getClassName() + ",componentId=" + getComponentId() + "]";
  }
}
