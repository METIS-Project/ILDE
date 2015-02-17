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

package org.coppercore.dto;

import java.io.Serializable;

/**
 * This class represents the primary key of the RunParticipation.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.4 $, $Date: 2005/01/12 14:34:31 $
 */
public class RunParticipationEntityPK
    implements Serializable {
  private static final long serialVersionUID = 42L;

  /**
   * The id of the run this RunParticipation belongs to.
   */
  public int runId;

  /**
   * The id of the user this RunParticipation belongs to.
   */
  public String userId;

  /**
   * Constructs an uninitialized instance.
   */
  public RunParticipationEntityPK() {
    //default constructor    
  }

  /**
   * Constructs a new RunParticipationEntityPK instance where the primary key is
   * set to the specified values.
   *
   * @param runId int the id of the run
   * @param userId String the is of the user
   */
  public RunParticipationEntityPK(int runId, String userId) {
    this.runId = runId;
    this.userId = userId;
  }

  /**
   * Checks if the specified object equals this RunParticipationEntityPK
   * instance.
   *
   * @param obj Object the object to compare this instance to
   * @return boolean true if the two objects are equal, otherwise false
   */
  public boolean equals(Object obj) {
    if (obj != null) {
      if (this.getClass().equals(obj.getClass())) {
        RunParticipationEntityPK that = (RunParticipationEntityPK) obj;
        return ( ( (this.userId == null) && (that.userId == null)) ||
                (this.userId != null && this.userId.equals(that.userId))) &&
            this.runId == that.runId;
      }
    }
    return false;
  }

  /**
   * Calculates and returns a hashode for this instance.
   *
   * @return int the hashcode of this instance
   */
  public int hashCode() {
    return (userId + "" + runId).hashCode();
  }

  /**
   * Returns a string representation of this RunParticipationEntityPK.
   *
   * @return String the representation of this instance
   */
  public String toString() {
    return "RunParticipationEntity[\"" + userId + "\", \"" + runId + "\"]";
  }

}
