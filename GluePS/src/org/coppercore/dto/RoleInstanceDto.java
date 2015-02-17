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

package org.coppercore.dto;

import java.io.Serializable;

/**
 * This class encapsulates all data of a RoleInstance bean.
 *
 * <p> Using a Data Transfer Object improves the performance because it enables the code to set all
 * properties of a bean at once instead of setting them member by member.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.4 $, $Date: 2005/01/12 14:34:31 $
 */
public class RoleInstanceDto implements Serializable {
  private static final long serialVersionUID = 42L;

  private int roleInstanceId;
  private String roleId;
  private int runId;

  /**
   * Constructs a new RoleInstanceDto instance and sets its members to the specified values.
   *
   * @param roleInstanceId int the unique id of the role instance
   * @param roleId String the learning design id of the role instance
   * @param runId int the id of the run the role instance belongs to
   */
  public RoleInstanceDto(int roleInstanceId, String roleId, int runId) {
  this.roleInstanceId = roleInstanceId;
    this.roleId = roleId;
    this.runId = runId;
  }

  /**
   * Constructs a new RoleInstanceDto instance and sets its members to the specified values.<p>
   * The roleInstanceId member is initialized to -1.
   *
   * @param roleId String the learning design id of the role instance
   * @param runId int the id of the run the role instance belongs to
   */
  public RoleInstanceDto(String roleId, int runId) {
    this(-1, roleId, runId);
  }

  /**
   * Returns the role instance id of this RoleInstance.
   *
   * @return int the id of this role instance
   * @see #setRoleInstanceId
   */
  public int getRoleInstanceId() {
  return roleInstanceId;
  }

  /**
   * Returns the learning design role id of this role instance.
   *
   * @return String the learning design role id
   */
  public String getRoleId() {
  return roleId;
  }

  /**
   * Returns the id of the run this role instance belongs to.
   *
   * @return int the id of the run this role instance belongs to
   */
  public int getRunId() {
  return runId;
  }

  /**
   * Sets the unique id of this role instance to the specified value.
   *
   * @param roleInstanceId int the new value of the role instance id
   * @see #getRoleInstanceId
   */
  public void setRoleInstanceId(int roleInstanceId) {
  this.roleInstanceId = roleInstanceId;
  }

}
