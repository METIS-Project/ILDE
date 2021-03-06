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

package org.coppercore.dossier;

import java.io.Serializable;

/**
 * This class wraps the primary key of the property definitions.
*
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.7 $, $Date: 2005/01/12 14:34:26 $
 */
public class PropertyDefEntityPK implements Serializable {
  private static final long serialVersionUID = 42L;

  /** the primary key of the property definition. */
  public int propDefPK;

  /**
   * Creates an uninitialized instance of PropertyDefEntityPK.
   */
  public PropertyDefEntityPK() {
    //default constructor    
  }

  /**
   * Creates an initialized instance of PropertyDefEntityPK and sets its
   * primary key to the specified value.
   *
   * @param propDefPK int the new value for the primary key
   */
  public PropertyDefEntityPK(int propDefPK) {
  this.propDefPK = propDefPK;
  }

  /**
   * Returns a String representation of this instance.
   * @return String the representation of this instance
   */
  public String toString() {
  return ("PropertyDefEntity[" + propDefPK + "]");
  }

  /**
   * Compares this instance to the specified object.
   *
   * <p>The two objects are considered equal when they both are of the same type
   * and their respective data members are equal.
   *
   * @param obj Object the object to compare this instance with
   * @return boolean true if this instance is equal to specified object,
   *   otherwise false.
   */
  public boolean equals(Object obj) {
  if (this == obj) {
      return true;
    }
    if (! (obj instanceof PropertyDefEntityPK)) {
      return false;
    }
    PropertyDefEntityPK that = (PropertyDefEntityPK) obj;
    if (that.propDefPK != this.propDefPK) {
      return false;
    }
    return true;
  }

  /**
   * Computes and returns a hash value for this instance.
   *
   * @return int the computed hashvalue for this instance
   */
  public int hashCode() {
  int result = 17;
    result = 37 * result + this.propDefPK;
    return result;
  }
}
