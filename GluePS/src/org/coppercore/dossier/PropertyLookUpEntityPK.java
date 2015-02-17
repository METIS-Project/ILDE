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

package org.coppercore.dossier;

import java.io.Serializable;

/**
 * Wraps a primary key for a property lookup record.
 *
 * <p>The primary key consists of two fields, the unit of learning id and the
 * learning design property id
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.7 $, $Date: 2005/01/12 14:34:26 $
 */
public class PropertyLookUpEntityPK implements Serializable {
  private static final long serialVersionUID = 42L;

  /** unit of learning id, first part of the primary key  of a lookup record. */
  public int uolId;

  /** learning design property id, second part of the primary key of a lookup record. */
  public String propId;

  /**
   * Creates a new uninitialized PropertyLookUpEntityPK instance.
   */
  public PropertyLookUpEntityPK() {
    //default constructor    
  }

  /**
   * Creates a new initialized PropertyLookUpEntityPK instance ans sets its primary key
   * to the specified value.
   *
   * @param uolId int the unit of learning id, first part of the primary key
   * @param propId String the learning design property id, second part of the primary key
   */
  public PropertyLookUpEntityPK(int uolId, String propId) {
    this.uolId = uolId;
    this.propId = propId;
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
    if (! (obj instanceof PropertyLookUpEntityPK)) {
      return false;
    }
    PropertyLookUpEntityPK that = (PropertyLookUpEntityPK) obj;
    if (that.uolId != this.uolId) {
      return false;
    }
    if (! (that.propId == null ? this.propId == null :
           that.propId.equals(this.propId))) {
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
    result = 37 * result + this.uolId;
    result = 37 * result + this.propId.hashCode();
    return result;
  }

}
