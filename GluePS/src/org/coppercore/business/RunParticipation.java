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

package org.coppercore.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.coppercore.common.HomeFactory;
import org.coppercore.common.ServiceLocator;
import org.coppercore.component.ComponentFactory;
import org.coppercore.component.RolesTreeProperty;
import org.coppercore.component.RolesTreePropertyDef;
import org.coppercore.dossier.PropertyFacade;
import org.coppercore.dto.RunParticipationDTO;
import org.coppercore.dto.RunParticipationEntityPK;
import org.coppercore.entity.RunParticipationEntity;
import org.coppercore.entity.RunParticipationEntityHome;
import org.coppercore.exceptions.ActiveRoleException;
import org.coppercore.exceptions.AlreadyExistsException;
import org.coppercore.exceptions.NotFoundException;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.ServiceLocatorException;

/**
 * The <code>RunParticipation</code> business object represents the
 * assignement of users to a run.
 * 
 * <p>
 * Instances of this class can only be created by calling either the create or
 * find class methods.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.15 $, $Date: 2009/01/08 09:21:57 $
 */
public class RunParticipation implements Serializable {
	private static final long serialVersionUID = 42L;
	private RunParticipationDTO dto;

	private static RunParticipationEntityHome runParticipationHome = null;

	/**
	 * The contructor for a new RunParticipation instance.
	 * 
	 * <p>
	 * The constructor is private to enforce the usage of the class factory
	 * methods to create a new user. Use RunParticipation.create to create a new
	 * runparticipation in the database, use the RunParticipation.find* methods
	 * to lookup existing runparticipations from the database.
	 * 
	 * @param dto
	 *            a RunParticipationDTO containing the properties of the
	 *            runparticipation.
	 */
	private RunParticipation(RunParticipationDTO dto) {
		this.dto = dto;
	}

	/**
	 * Returns the user of the runparticipation.
	 * 
	 * @return a <code>User</code> representing the assigned person.
	 */
	public User getUser() {
		try {
			return User.findByPrimaryKey(dto.getUserId());
		} catch (NotFoundException ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Returns the run of the runparticipation.
	 * 
	 * @return a Run.
	 */
	public Run getRun() {
		try {
			return Run.findByPrimaryKey(dto.getRunId());
		} catch (NotFoundException ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Returns the role business object representing the active role of the user
	 * in the run.
	 * 
	 * <p>
	 * If the user is not assigned to an active role, the method returns
	 * <code>null</code>.
	 * 
	 * @return a RoleInstance representing the active role.
	 * @see #setActiveRole
	 */
	public RoleInstance getActiveRole() {
		int roleId = dto.getActiveRole();
		// check if user has an active role.
		if (roleId == -1) {
			return null;
		}
		try {
			return RoleInstance.findByPrimaryKey(dto.getActiveRole());
		} catch (NotFoundException ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Sets the active role of the runparticipation.
	 * 
	 * <p>
	 * The active role is the actual role this user performs in this run. The
	 * active role determines the activity tree that is shown to the user.
	 * 
	 * <p>
	 * A role can only be a valid active role if:
	 * <ul>
	 * <li>the user is assigned to this role.</li>
	 * <li>the role is assigned to this run.</li>
	 * <li>the role is not the root role (the ld role called "roles").</li>
	 * </ul>
	 * 
	 * @param activeRole
	 *            a RoleInstance specifying the new active role for this user in
	 *            this run.
	 * @throws ActiveRoleException
	 *             if the user is not assigned to the specified role, or if the
	 *             role does not belong to this run, or if the specified role is
	 *             the root role.
	 * @see #getActiveRole
	 */
	public void setActiveRole(RoleInstance activeRole) throws ActiveRoleException {
		// check if new activeRole actually has changed
		if (activeRole.getId() != dto.getActiveRole()) {
			// check if user is assigned to this role
			try {
				RoleParticipation.findByPrimaryKey(getUser(), activeRole);
				// check if activeRole belongs to the run
				if (activeRole.getRun().getId() != dto.getRunId()) {
					throw new ActiveRoleException("Active role instance " + activeRole.getId() + " for role "
							+ activeRole.getRoleId() + " does not belong to run " + dto.getRunId());
				}

				// CHNGD 2005-13-12 active role should not be set to the root
				// role
				Run run = Run.findByPrimaryKey(dto.getRunId());
				Uol uol = run.getUol();

				RolesTreeProperty roles = new RolesTreeProperty(uol, run, RolesTreePropertyDef.ALL_ROLE_ID);
				if (roles.getParentId(activeRole.getId()) == -1) {
					throw new ActiveRoleException("Active role may not be set to the root role, the specified role ["
							+ activeRole.getId() + "] is the root role");
				}

				// everything ok, store the new activerole
				dto.setActiveRole(activeRole.getId());
				persist();
			} catch (NotFoundException ex) {
				throw new ActiveRoleException("SetActiveRole: User " + getUser().getId() + " is not assigned to role "
						+ activeRole.getId(), ex);
			} catch (PropertyException ex) {
				throw new EJBException(ex);
			}
		}
	}

	/**
	 * Returns the properties of the run participation.
	 * 
	 * @return a RunParticipationDTO containing the properties of the run
	 *         participation
	 */
	public RunParticipationDTO getDto() {
		return dto;
	}

	private void persist() {
		try {
			RunParticipationEntityPK pk = new RunParticipationEntityPK(dto.getRunId(), dto.getUserId());
			RunParticipationEntityHome rpHome = getRunParticipationHome();
			RunParticipationEntity rp = rpHome.findByPrimaryKey(pk);
			rp.setDto(dto);
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Removes the runparticipation from the system.
	 * 
	 * <p>
	 * By removing the runparticipation in effect the user is removed from the
	 * run.
	 */
	public void remove() {
		try {
			// first remove all local properties
			PropertyFacade.removeLocalProperties(dto.getUserId(), dto.getRunId());

			// next remove role particiations
			Collection rps = RoleParticipation.findByUser(User.findByPrimaryKey(dto.getUserId()), Run
					.findByPrimaryKey(dto.getRunId()));
			Iterator iter = rps.iterator();
			while (iter.hasNext()) {
				RoleParticipation rp = (RoleParticipation) iter.next();
				rp.remove();
			}

			// Now remove this run participation
			RunParticipationEntityPK pk = new RunParticipationEntityPK(dto.getRunId(), dto.getUserId());
			RunParticipationEntityHome rpHome = getRunParticipationHome();
			RunParticipationEntity rp = rpHome.findByPrimaryKey(pk);
			rp.remove();

			ComponentFactory.getPropertyFactory().clearCache();

		} catch (Exception ex) {
			throw new EJBException(ex);
		}
	}

	// RunParticipation factory - implemented as class methods

	/**
	 * Creates a new RunParticipation business object.
	 * 
	 * <p>
	 * The runparticipation represents the assignment of a particular user to a
	 * particular run. The new runparticipation is created according to the
	 * specified parameters. The runparticipation is persisted in the database.
	 * 
	 * @param user
	 *            a User to assign to the run.
	 * @param run
	 *            the Run where the user is assigned to.
	 * @return the new created RunParticipation.
	 * @throws AlreadyExistsException
	 *             if a run participation for the given user and run already
	 *             exists.
	 */
	public static RunParticipation create(User user, Run run) throws AlreadyExistsException {
		try {
			// check if user already is assigned to the run
			findByPrimaryKey(user, run);
			// the user is already assigned, throw an exception
			throw new AlreadyExistsException("A RunParticipation for user " + user.getId() + " and Run " + run.getId()
					+ " already exists.");
		} catch (NotFoundException nfex) {
			// user is not assigned to run, so create a new RunParticipation
			try {
				RunParticipationDTO dto = new RunParticipationDTO(user.getId(), run.getId());
				RunParticipationEntity rpe = getRunParticipationHome().create(dto);
				return new RunParticipation(rpe.getDto());
			} catch (Exception ex) {
				// inform ejb container about application exceptions by
				// rethrowing them
				// as an EJBException.
				throw new EJBException(ex);
			}
		}
	}

	/**
	 * Returns a <code>Collection</code> of all RunParticipation objects that
	 * belong to the specified run.
	 * 
	 * <p>
	 * If there are no RunParticipations assigned to the run, the method returns
	 * an empty collection.
	 * 
	 * @param run
	 *            the Run to find all RunParticipations for.
	 * @return a Collection containing all the found run participations.
	 */
	public static Collection findByRun(Run run) {
		return findByRunId(run.getId());
	}

	private static Collection findByRunId(int runId) {
		try {
			Collection rpes = getRunParticipationHome().findByRunId(runId);
			return convertCollection(rpes);
		} catch (Exception ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Returns a <code>Collection</code> of all RunParticipation objects that
	 * belong to the specified user.
	 * 
	 * <p>
	 * If there are no RunParticipations assigned to the user, the method
	 * returns an empty collection.
	 * 
	 * @param user
	 *            a User to find all run participations for.
	 * @return a Collection containing all found run participations for the
	 *         specified user.
	 */
	public static Collection findByUser(User user) {
		try {
			Collection rpes = getRunParticipationHome().findByUserId(user.getId());
			return convertCollection(rpes);
		} catch (Exception ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Returns a <code>Collection</code> of all RunParticipation objects that
	 * belong to the specified user and unit of learning.
	 * 
	 * <p>
	 * If there are no RunParticipations assigned to the user in the specified
	 * unit of learning , the method returns an empty collection.
	 * 
	 * @param user
	 *            a User to find all run participations for.
	 * @param uol
	 *            a Uol to find all run participations for.
	 * @return a Collection containing all found run participations for the
	 *         specified user and unit of learning.
	 */
	public static Collection findByUser(User user, Uol uol) {
		try {
			Collection rpes = getRunParticipationHome().findByUserId(user.getId(), uol.getId());
			return convertCollection(rpes);
		} catch (Exception ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Returns a <code>Collection</code> of all RunParticipation objects that
	 * have the passed roleinstanceid for the passed run.
	 * 
	 * <p>
	 * If there are no RunParticipations found , the method returns an empty
	 * collection.
	 * 
	 * @param runId
	 *            int the id of the run
	 * @param roleInstanceId
	 *            the id of the roleinstance being queried
	 * 
	 * @return a Collection containing all found run participations for the
	 *         specified runId and roleInstanceId, or an empty Collection if no are found.
	 */
	public static Collection findByRoleInstance(int runId, int roleInstanceId) {
		Collection rpes = new ArrayList();
		try {
			rpes = getRunParticipationHome().findByRoleInstanceId(runId, roleInstanceId);
			return convertCollection(rpes);
		} catch (FinderException ex) {
			return rpes;
		}
		catch (Exception ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Retrieves the RunParticipation defined by the user and run parameter.
	 * 
	 * @param user
	 *            the User to look for
	 * @param run
	 *            the Run to look for.
	 * @throws NotFoundException
	 *             if either the user or the run cannot be located.
	 * @return the RunParticipation for the specified user and run.
	 */
	public static RunParticipation findByPrimaryKey(User user, Run run) throws NotFoundException {
		try {
			RunParticipationEntityPK pk = new RunParticipationEntityPK(run.getId(), user.getId());
			RunParticipationEntity rpe = getRunParticipationHome().findByPrimaryKey(pk);
			return new RunParticipation(rpe.getDto());
		} catch (ServiceLocatorException slex) {
			throw new EJBException(slex);
		} catch (FinderException fex) {
			throw new NotFoundException(fex);
		}
	}

	private static Collection convertCollection(Collection rpes) {
		ArrayList result = new ArrayList();
		Iterator it = rpes.iterator();
		while (it.hasNext()) {
			result.add(new RunParticipation(((RunParticipationEntity) it.next()).getDto()));
		}
		return result;
	}

	/**
	 * Returns the home interface for the RunParticipation entity bean.
	 * 
	 * @throws ServiceLocatorException
	 *             if the home interface cannot be located
	 * @return the RunParticipationEntityHome
	 */
	private static RunParticipationEntityHome getRunParticipationHome() throws ServiceLocatorException {
		// Because looking up a home interface is an expensive operation, fetch
		// it
		// only once.
		if (runParticipationHome == null) {
			// First time, fetch a new home interface
			ServiceLocator locator = ServiceLocator.getInstance();
			runParticipationHome = (RunParticipationEntityHome) locator.getEjbLocalHome(HomeFactory.RUNPARTICIPATION);
		}
		return runParticipationHome;
	}
}
