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

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Defines the factory interface for creating and finding PropertyEntity beans.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2006/08/17 15:14:53 $
 */
public interface PropertyEntityHome extends javax.ejb.EJBLocalHome {

  /**
   * Finds the property with the given primary key in the database.
   *
   * <p>If no property is found, the method throws a FinderException.
   *
   * @param pk PropertyEntityPK the primary key of the property to look for
   * @throws FinderException when the property with the given primary key could
   *   not be located
   * @return PropertyEntity the found property
   */
  public PropertyEntity findByPrimaryKey(PropertyEntityPK pk) throws
      FinderException;

  /**
   * Locates the property with the passed parameters in the database. If the
   * property is found the method returns the found property,
   * otherwise the method throws a FinderException
   *
   * @param uolId int the id of the unit of learning this property belongs to
   * @param propId String the learning design of the property to find
   * @param userId String the id of the owner to find the property for
   * @param runId int the id of the run the property belongs to
   * @throws FinderException when the property could not be found
   * @return PropertyEntity the found property
   */
  public PropertyEntity findByLookUp(int uolId, String propId, String userId, int runId) throws FinderException;

  /**
   * Finds all properties for the specified user in the specified run.
   *
   * <p>The method returns all found properties or an empty collection if no
   * property matches the search criteria.
   *
   * @param userId String the id of the owner of the properties to search for
   * @param runId int the id of the run the properties belong to
   * @throws FinderException when there is an error finding the properties
   * @return Collection of PropertyEntity properties that match the search
   *   criteria, or an empty Collection if no properties are found
   */
  public Collection /* PropertyEntity */ findByUserRun(String userId, int runId) throws
      FinderException;

  /**
   * Find all properties that belong to the specified run.
   *
   * <p>The method returns all found properties or an empty collection if no
   * property matches the search criteria.
   *
   * @param runId int the id of the run to find all properties for.
   * @throws FinderException when there is an error finding the properties
   * @return Collection of PropertyEntity properties that match the search
   *   criteria, or an empty Collection if no properties are found
   */
  public Collection /* PropertyEntity */ findByRun(int runId) throws FinderException;

  /**
   * Finds all properties belonging to the specified unit of learning.
   *
   * <p>The method returns all found properties or an empty collection if no
   * property matches the search criteria.
   *
   * @param uolId int the id of the unit of learning to find all properties for.
   * @throws FinderException when there is an error finding the properties
   * @return Collection of all PropertyEntity properties that match the search
   *   criteria, or an empty Collection if no properties are found
   */
  public Collection /* PropertyEntity */ findByUol(int uolId) throws FinderException;

  /**
   * Finds all properties for the given user.
   *
   * <p>The method returns all found properties or an empty
   * collection if no property matches the search criteria.
   *
   * @param userId int the id of the user to find all properties
   *   for.
   * @throws FinderException when there is an error finding the properties
   * @return Collection of all PropertyEntity properties that
   *   match the search criteria, or an empty Collection if no properties are
   *   found
   */
  public Collection /* PropertyEntity */ findByUser(String userId) throws FinderException;

  /**
   * Creates a new property entity in the database.
   *
   * @param propValue String the value of the property
   * @param dataType String the datatype of the property
   * @param runId int the id of the run the property belongs to, or null for
   *   global properties
   * @param userId String the id of the owner of the property, or null for
   *   non-personal, non-role properties
   * @param propDefPK int the id of the property definition defining the
   *   behaviour of this property
   * @param scope int the scope of this property
   * @param propId String the learning design id of the property
   * @throws CreateException when there is an error creating the property
   * @return PropertyEntity the new created property
   */
  public PropertyEntity create(String propValue, String dataType, int runId, String userId, int propDefPK, int scope,
                               String propId) throws
      CreateException;
}
