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

package org.coppercore.entity;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import org.coppercore.dto.RoleParticipationDto;
import org.coppercore.dto.RoleParticipationEntityPK;

/**
 * Defines the factory interface for creating and finding RoleParticipationEntity beans.
 *
 * <p>A RoleParticipationEntity couples a user to an instance of a learning design role in the
 * context of a specific run. These role instance are either the ld
   * role itself or a new created instance of the learning design role. New instances can be created for roles that
   * have the create-new attribute set to allowed

 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.3 $, $Date: 2005/01/11 13:15:11 $
 */
public interface RoleParticipationEntityHome extends javax.ejb.EJBLocalHome {

  /**
   * Creates a new RoleParticipation entity in the database, based on the specified parameters.
   *
   * <p> A RoleParticipation couples a user to a role instance. The role instance is either the ld
   * role itself or a new created instance of this role. New instances can be created for roles that
   * have the create-new attribute set to allowed
   *
   * @param dto RoleParticipationDto a data transfer object defining all creation parameters
   * @throws CreateException when the entity could not be created
   * @return RoleParticipationEntityPK the primary key for the new created entity
   */
  public RoleParticipationEntity create(RoleParticipationDto dto) throws
      CreateException;

  /**
   * Returns a Collection of all RoleParticipationEntitys of the specified user in the specified
   * run.
   *
   * <p>This list defines all roles the user is assigned to in the run. If the user is not assigned
   * to any run, this methos returns an empty Collection.
   *
   * @param anUserId String the id of the user to look the RoleParticipation up for
   * @param aRunId int the id of the run where to look.
   * @throws FinderException is nver thrown
   * @return Collection of RoleParticipationEntity for all role participations this user is
   *   assigned to, or an empty Collection if the user is not assigned to any role in this run.
   */
  public Collection findByUserId(String anUserId, int aRunId) throws
      FinderException;

  /**
   * Returns an RoleParticipationEntity with the given key from the database. If the entity is not
   * found, the method throws a FinderException.
   *
   * @param pk RoleParticipationEntityPK the primary key of the RoleParticipationEntity to look for.
   * @throws FinderException when no entity with the given key is found
   * @return RoleParticipationEntity the found entity.
   */
  public RoleParticipationEntity findByPrimaryKey(RoleParticipationEntityPK pk) throws
      FinderException;

  /**
   * Finds the RoleParticipationEntity where the user is assigned to an instance of the specified
   * learning design role in the given run.
   *
   * <p>If no participation is found a FinderException is thrown.
   *
   * <p>A user might be assigned to more than one instance of an ld role but only one is returned.
   *
   * @param runId int the id of the run this role participatiomn is bound to
   * @param userId String the id of the user to look the participation for
   * @param roleId String the learning design role id of the instance to look for
   * @throws FinderException when no participation can be found for the given user, role in the
   *   specified run
   * @return RoleParticipationEntity the entity that meets the search criteria
   */
  public RoleParticipationEntity findByUserRole(int runId, String userId, String roleId) throws
      FinderException;
}
