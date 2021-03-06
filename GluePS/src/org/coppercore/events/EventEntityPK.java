/*
 * CopperCore , an IMS-LD level C engine
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

package org.coppercore.events;

import java.io.Serializable;

/**
 * This class represents the primary key of the EventEntity bean.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.4 $, $Date: 2005/01/12 14:34:27 $
 */
public class EventEntityPK implements Serializable {
  private static final long serialVersionUID = 42L;

  /** The primary key of the EventEtntity instance. */
  public int id;

  /**
   * Constructs an unitialized EventEntityPK object.
   */
  public EventEntityPK() {
    //default constructor
  }

  /**
   * Creates a new EventEntityPK and sets it primary key to the specified value.
   * @param id int the primary key of th instance
   */
  public EventEntityPK(int id) {
    this.id = id;
  }

  /**
   * Checks if the specified object equals this EventEntityPK instance.
   *
   * @param obj Object the object to compare this instance to
   * @return boolean true if the two objects are equal, otherwise false
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (! (obj instanceof EventEntityPK)) {
      return false;
    }
    EventEntityPK that = (EventEntityPK) obj;
    if (that.id != this.id) {
      return false;
    }
    return true;
  }

  /**
   * Calculates and returns a hashode for this instance.
   *
   * @return int the hashcode of this instance
   */
  public int hashCode() {
    int result = 17;
    result = 37 * result + this.id;
    return result;
  }
}
