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

package org.coppercore.dto;

import java.io.Serializable;

/**
 * Wraps the primary key of the RoleParticipation in a class.
 *
 * <p>The primary key of a role participation consists of its role
 * instance id.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.4 $, $Date: 2005/01/12 14:34:31 $
 */
public class RoleInstanceEntityPK
    implements Serializable {
  private static final long serialVersionUID = 42L;

  /**
   * The primary key.
   */
  public int roleInstanceId;

  /**
   * Constructs an uninitialized RoleInstanceEntityPK.
   */
  public RoleInstanceEntityPK() {
    //default constructor    
  }

  /**
   * Constructs an initialized RoleInstanceEntityPK and sets its key to the specified value.
   *
   * @param roleInstanceId int
   */
  public RoleInstanceEntityPK(int roleInstanceId) {
  this.roleInstanceId = roleInstanceId;
  }

  /**
   * Returns a string representation of the RoleInstanceEntity.
   *
   * @return String the representation of the RoleInstanceEntity
   */
  public String toString() {
    return "RoleInstanceEntity[" + roleInstanceId + "]";
  }

  /**
   * Checks if the passed object and this key are equal.<p>
   * The keys are considered equal if they both are of the same type and their members are equal.
   * @param obj Object the object to compare this key to
   * @return boolean true if the keys are equal otherwise  false
   */
  public boolean equals(Object obj) {
  if (this == obj) {
      return true;
    }
    if (! (obj instanceof RoleInstanceEntityPK)) {
      return false;
    }
    RoleInstanceEntityPK that = (RoleInstanceEntityPK) obj;
    if (that.roleInstanceId != this.roleInstanceId) {
      return false;
    }
    return true;
  }

  /**
   * Calculates and returns the hashcode for this object.
   *
   * @return int the hashcode of this object
   */
  public int hashCode() {
  int result = 17;
    result = 37 * result + this.roleInstanceId;
    return result;
  }

}
