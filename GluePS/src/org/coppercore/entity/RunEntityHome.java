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

import org.coppercore.dto.RunDto;
import org.coppercore.dto.RunEntityPK;

/**
 * Defines the factory interface for creating and finding RunEntity beans.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.3 $, $Date: 2005/01/11 13:15:12 $
 */
public interface RunEntityHome
    extends javax.ejb.EJBLocalHome {

  /**
   * Creates a new <code>RunEntity</code> record in the database.
   *
   * @param runDto the id of the unit-of-learning for this run
   * @return the newly created RunEntity
   * @throws CreateException if the new record cannot be created in the database
   */
  public RunEntity create(RunDto runDto) throws CreateException;

  /**
   * Returns a Collection containing the RunEntity's of all runs for the
   * specified unit-of-learning.
   *
   * <p> The method returns an empty collection if there are no runs found in
   * the database.
   *
   * @param uolId the id of the unit-of-learning for which the runs have to be
   *   retrieved
   * @return a Collection containing all RunEntity's for the given uolId
   * @throws FinderException if a database error occurs
   */
  public Collection findByUolId(int uolId) throws FinderException;

  /**
   * Finds the RunEntity record in the database by looking up the specified
   * runId.
   *
   * <p>The method returns the primary key of the record to indicate the found
   * record.
   * <br>If the record cannot be located the method throws a FinderException.
   *
   * @param pk the primary key of the record to find
   * @return the RunEntity found
   * @throws FinderException if the record cannot be located in the database
   */
  public RunEntity findByPrimaryKey(RunEntityPK pk) throws FinderException;

}
