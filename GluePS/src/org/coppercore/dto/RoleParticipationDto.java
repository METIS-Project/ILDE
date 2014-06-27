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
 * This class encapsulates all data of a RoleParticipation bean.
 *
 * <p> Using a Data Transfer Object improves the performance because it enables the code to set all
 * properties of a bean at once instead of setting them member by member.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.4 $, $Date: 2005/01/12 14:34:31 $
 */
public class RoleParticipationDto implements Serializable {
  private static final long serialVersionUID = 42L;

  private String userId;
  private int roleInstanceId;
  private String roleId;
  private int runId;

  /**
   * Constructs a new RoleParticiaptionDto instance and sets its members to the specified values.
   *
   * @param userId String the id of the user assigned to the role
   * @param roleInstanceId int the unique id of the role the user is assigned to
   * @param roleId String the learning design id of the role the user os assigned to
   * @param runId int the id of the run in which this user is assigned to the role
   */
  public RoleParticipationDto(String userId, int roleInstanceId, String roleId, int runId) {
  this.userId = userId;
    this.roleInstanceId = roleInstanceId;
    this.roleId = roleId;
    this.runId = runId;
  }

  /**
   * Constructs a new RoleParticiaptionDto instance and sets its members to the specified values.<p>
   * Only partially fills the members of the dto. roleId is set to null and the runid is set to 0.
   *
   * @param userId String the id of the user assigned to the role
   * @param roleInstanceId int the unique id of the role the user is assigned to
   */
  public RoleParticipationDto(String userId, int roleInstanceId) {
    this(userId, roleInstanceId, null, 0);
  }

  /**
   * Returns the id of the user assigned to the role.
   * @return String the id of the user assigned to the role
   * @see #setUserId
   */
  public String getUserId() {
  return userId;
  }

  /**
   * Returns the unique id of the role instance the user is assigned to.
   * @return int the unique id of the role instance the user is assigned to
   * @see #setRoleInstanceId
   */
  public int getRoleInstanceId() {
  return roleInstanceId;
  }

  /**
   * Returns the learning design identifier of the role the user is assigned to.
   * @return String the learning design identifier of the role the user is assigned to.
   * @see #setRoleId
   */
  public String getRoleId() {
  return roleId;
  }

  /**
   * Returns the id of the run for which this user is assigned to the role.
   * @return int the id of the run for which this user is assigned to the role
   * @see #setRunId
   */
  public int getRunId() {
  return runId;
  }

  /**
   * Sets the userid of the user who is assigned to this role to the specified value.
   * @param userId String the new value for the userid
   * @see #getUserId
   */
  public void setUserId(String userId) {
  this.userId = userId;
  }

  /**
   * Sets the unique id of the role the user is assigned to.
   *
   * @param roleInstanceId int the unique id of the role the user is assigned to
   * @see #getRoleInstanceId
   */
  public void setRoleInstanceId(int roleInstanceId) {
  this.roleInstanceId = roleInstanceId;
  }

  /**
   * Sets the learning design id of the role the user is assigned to.
   *
   * @param roleId String the learning design id of the role the user is assigned to
   * @see #getRoleId
   */
  public void setRoleId(String roleId) {
  this.roleId = roleId;
  }

  /**
   * Sets the id of the run for which the user is assigned to the role.
   * @param runId int the id of the run for which the user is assigned to the role
   * @see #getRunId
   */
  public void setRunId(int runId) {
  this.runId = runId;
  }

  /**
   * Returns a String representation for this role participation.
   *
   * @return String the representation for this role participation
   */
  public String toString() {
  return "RoleParticipationEntity[\""+userId+"\", "+roleInstanceId+"]";
  }
}
