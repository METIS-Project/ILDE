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

import org.coppercore.dto.RunParticipationDTO;
import org.coppercore.dto.RunParticipationEntityPK;

/**
 * Defines the factory interface for creating and finding RunParticipationEntity
 * beans.
 */
public interface RunParticipationEntityHome extends javax.ejb.EJBLocalHome {

  /**
	 * Creates a new RunParticipation record in the database.
	 * 
	 * @param runParticipationDTO
	 *            RunParticipationDTO the values for this new record
	 * @throws CreateException
	 *             if there is an error creating the RunParticiationEntity
	 * @return RunParticipationEntity the new created bean
	 */
  public RunParticipationEntity create(RunParticipationDTO runParticipationDTO) throws CreateException;

  /**
	 * Find all RunParticipations for the specified run.
	 * 
	 * <p>
	 * If no RunParticipations are found, the method returns an empty
	 * Collection.
	 * 
	 * @param runId
	 *            the id of the run to find the runparticipations for
	 * @return a Collection of RunParticiationEntity
	 * @throws FinderException
	 *             required by spec, but is never thrown
	 */
  public Collection findByRunId(int runId) throws FinderException;

  /**
	 * Finds all RunParticipations for the given user.
	 * 
	 * <p>
	 * If no RunParticipations are found, the method returns an empty
	 * Collection.
	 * 
	 * @param userId
	 *            String the user for whom all RunParticipations are searched
	 * @throws FinderException
	 *             required by the spec, but is never thrown
	 * @return Collection all RunParticipations for the specified user, or an
	 *         empty Collection if none are found
	 */
  public Collection findByUserId(String userId) throws FinderException;

  /**
	 * Finds all RunParticipations in the given unit of learning for the given
	 * user.
	 * 
	 * <p>
	 * If no RunParticipations are found, the method returns an empty
	 * Collection.
	 * 
	 * @param userId
	 *            String the user for whom all RunParticipations are searched
	 * @param uolId
	 *            int the id of the unit of learning the RunParticipations
	 *            should be a part of
	 * @throws FinderException
	 *             required by the spec, but is never thrown
	 * @return Collection all RunParticipations for the specified user, or an
	 *         empty Collection if none are found
	 */
  public Collection findByUserId(String userId, int uolId) throws FinderException;

	/**
	 * Find all RunParticipations based on their participation in role
	 * represented by the passed role instance id.
	 * 
	 * @param runId
	 *            int
	 * @param roleInstanceId
	 *            int representing the role instance id
	 * @throws FinderException
	 * @return Collection
	 */
	public Collection findByRoleInstanceId(int runId, int roleInstanceId) throws FinderException;
  
  
  /**
	 * Returns the RunParticipationEntity for the given key.
	 * 
	 * <p>
	 * If the RunParticipationEntity cannot be located the method throws a
	 * FinderException.
	 * 
	 * @param pk
	 *            RunParticipationEntityPK the key of the record to find
	 * @throws FinderException
	 *             if the key cannot be located
	 * @return RunParticipationEntity the found run participation
	 */
  public RunParticipationEntity findByPrimaryKey(RunParticipationEntityPK pk) throws FinderException;
}
