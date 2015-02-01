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

import org.coppercore.dto.UnitOfLearningPK;
import org.coppercore.dto.UolDto;

/**
 * Defines the factory interface for creating and finding UnitOfLearning beans.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.3 $, $Date: 2005/01/11 13:15:12 $
 */
public interface UnitOfLearningHome
    extends javax.ejb.EJBLocalHome {

  /**
   * Creates a new UnitOfLearning using the specified parameters.
   *
   * @param uolDto UolDto a data transfer object specifiying all parameters for
   *   the new unit of learning
   * @throws CreateException when there is an error creating the new unit of
   *   learning in the database
   * @return UnitOfLearning the new created UnitOfLearning
   */
  public UnitOfLearning create(UolDto uolDto) throws CreateException;

  /**
   * Finds the unit of learning matching the specified uri.
   *
   * <p>The uri is the unique identifier assigned to the unit of learning in the
   * learning design manifest. A uri should be world-wide unique. Only one unit
   * of learning in the system can have this id because all units of learning
   * with the same id are considered the same and will overwrite each other
   * during publication.
   *
   * @param uri String the uri to search for
   * @throws FinderException if the unit of learning with the specified uri
   *   could not be locatde in the database
   * @return UnitOfLearning the unit of learning found
   */
  public UnitOfLearning findByURI(String uri) throws FinderException;

  /**
   * Returns a Collection containing all the UnitOfLearnings in the database.
   *
   * <p> The method returns an empty collection if the are no UnitOfLearnings
   * in the database.
   *
   * @return a Collection containing all UnitOfLearnings in the
   *   database or an empty Collection if no unit of learning is found
   * @throws FinderException is never thrown, but required by the spec
   */
  public Collection findAllUnitOfLearnings() throws FinderException;

  /**
   * Return a collection of all UnitOfLearning found for the specified user.
   *
   * <p>If no unit of learning is found for the user the method returns an empty
   * collection.
   *
   * @param userId is the id of user who's uols will be retrieved
   * @return a collection of all UnitOfLearning found for the specified
   *   user.
   * @throws FinderException is never thrown, but required by the spec
   */
  public Collection findByUser(String userId) throws FinderException;

  /**
   * Finds the unit of learning matching the specified primary key.
   *
   * @param pk UnitOfLearningPK the primary key to search for
   * @throws FinderException if the unit of learning with the specified primary
   *   key could not be located in the database
   * @return UnitOfLearning the unit of learning found
   */
  public UnitOfLearning findByPrimaryKey(UnitOfLearningPK pk) throws
      FinderException;
}
