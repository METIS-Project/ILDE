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

import org.coppercore.common.Util;

/**
 * This class encapsulates all data of a RunParticipation bean.
 *
 * <p> Using a Data Transfer Object improves the performance because it enables
 * the code to set all properties of a bean at once instead of setting them
 * member by member.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.4 $, $Date: 2005/01/12 14:34:31 $
 */
public class RunParticipationDTO implements Serializable {
  private static final long serialVersionUID = 42L;

  private String userId = null;
  private int runId;
  private int activeRole = -1; // id of the actual instance of the active LD role
                               // or -1 if there is no active role yet
  private String roleId = null;  // active LD role

  /**
   * Constructs an unitialized instance.<p>This constructor is required for the
   * Axis SOAP toolkit.
   */
  public RunParticipationDTO() {
  // do nothing
  }


  /**
   * Constructs a partially initialized RunParticipationDTO.
   * @param userId String the userid of this instance
   * @param runId int the runid of this instance
   */
  public RunParticipationDTO(String userId, int runId) {
  setUserId(userId);
    setRunId(runId);
  }

  /**
   * Constructs a partially initialized RunParticipationDTO.
   * @param userId String the userid of this instance
   * @param runId int the runid of this instance
   * @param activeRole int the active role id of this instance
   */
  public RunParticipationDTO(String userId, int runId, int activeRole) {
  this(userId, runId);
    this.activeRole = activeRole;
  }

  /**
   * Constructs a full initialized instance of a RunParticipationDTO.
   * @param userId String the userid of this instance
   * @param runId int the runid of this instance
   * @param activeRole int the active role id of this instance
   * @param roleId String the Learning Design role id of this instance
   */
  public RunParticipationDTO(String userId, int runId, int activeRole, String roleId) {
  this(userId, runId, activeRole);
    this.roleId = roleId;
  }

  /**
   * Returns the id of this instance.
   * @return int the id of this instance
   * @see #setRunId
   */
  public int getRunId() {
  return runId;
  }

  /**
   * Returns the userid of this instance.
   * @return String the userid of this instance
   * @see #setUserId
   */
  public String getUserId() {
  return userId;
  }

  /**
   * Returns the active role of this instance.
   * @return int  the active role of this instance
   * @see #setActiveRole
   */
  public int getActiveRole() {
  return activeRole;
  }


  /**
   * Returns the Learning Design role-id of this instance.
   * @return String the Learning Design role-id of this instance
   * @see #setRoleId
   */
  public String getRoleId() {
  return roleId;
  }

  /**
   * Sets the runid of this instance to the specified value.
   * @param runId int the new value of the runid
   * @see #getRunId
   */
  public void setRunId(int runId) {
  this.runId = runId;
  }

  /**
   * Sets the userid of this instance to the specified value.
   * @param userId String the new id of the userid
   * @see #getUserId
   */
  public void setUserId(String userId) {
  this.userId = userId;
  }

  /**
   * Sets the active role of this instance to the specified value.
   * @param activeRole int the new active role value
   * @see #getActiveRole
   */
  public void setActiveRole(int activeRole) {
  this.activeRole = activeRole;
  }

  /**
   * Sets the Learning Design active role id of this instance to the specified value.
   * @param roleId String the new value of the Learning Design role id
   * @see #getRoleId
   */
  public void setRoleId(String roleId) {
  this.roleId = roleId;
  }

  /**
   * Returns a String representation of this RunParticipation for presentation
   * purposes.
   * @return String the string representation of the RunParticipation
   */
  public String toString() {
  return "RunParticipation[userId=" + Util.quotedStr(userId) + ",runId=" + runId + ",activeRole=" +
        (activeRole == -1 ? "<not assigned>" : Integer.toString(activeRole)) + "]";
  }
}
