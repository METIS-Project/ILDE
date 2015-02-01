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

/**
 * Defines the factory interface for creating and finding UserEntity beans.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.4 $, $Date: 2006/12/05 16:26:06 $
 */
public interface UserEntityHome extends javax.ejb.EJBLocalHome {

  /**
   * Creates a new <code>UserEntity</code> record in the database.
   *
   * @param userId the id of user
   * @return the newly created UserEntity
   * @throws CreateException if the new record cannot be created in the database
   */
  public UserEntity create(String userId) throws CreateException;

  /**
   * Returns the UserEntity identified by the primary key, which is the userid.
   * @param userId String the primary key of the UserEntity to find
   * @throws FinderException when the UserEntity with the given id cannot be found
   * @return UserEntity the user with id userId
   */
  public UserEntity findByPrimaryKey(String userId) throws FinderException;

  /**
   * Returns all users of the system in a Collection of UserEntity.
   *
   * <p> If there are no users in the system, the method returns an empty
   * Collection.
   *
   * @throws FinderException defined by the spec, is never thrown
   * @return Collection of all users in the system as UserEntity's. If no users
   *   are found return an empty Collection
   */
  public Collection findAllUsers() throws FinderException;

  /**
   * Finds all users in the system assigned to the specified LD roles in the
   * specified run and returns them in a Collection of UserEntity's.
   *
   * <p>The roleids passed is a space seperated list of LD role ids to match.
   *
   * <p> If no users are found the method returns an empty Collection.
   *
   * @param runId int the id of the run the users belong to
   * @param roleId String a space seperated list of LD ids of the roles the users
   *   are assigned to
   * @throws FinderException defined by the spec, is never thrown
   * @return Collection of users assigned to the specified LD role(s) in the
   *   specified run as UserEntity's
   */
  public Collection findByRoleId(int runId, String roleId) throws
      FinderException;
  
  
  /**
   * Finds all users in the system assigned to the specified role instances in the
   * specified run and returns them in a Collection of UserEntity's.
   *
   * <p>The roleids passed is a space seperated list of role isntance ids to match.
   *
   * <p> If no users are found the method returns an empty Collection.
   *
   * @param runId int the id of the run the users belong to
   * @param roleInstanceIds String a space seperated list of instance ids of the roles the users
   *   are assigned to
   * @throws FinderException defined by the spec, is never thrown
   * @return Collection of users assigned to the specified role instances in the
   *   specified run as UserEntity's
   */
  public Collection findByRoleInstanceId(int runId, String roleInstanceIds) throws FinderException;

}
