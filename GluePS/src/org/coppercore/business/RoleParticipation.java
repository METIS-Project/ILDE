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
import org.coppercore.dto.RoleParticipationDto;
import org.coppercore.dto.RoleParticipationEntityPK;
import org.coppercore.entity.RoleParticipationEntity;
import org.coppercore.entity.RoleParticipationEntityHome;
import org.coppercore.events.EventDispatcher;
import org.coppercore.exceptions.AlreadyExistsException;
import org.coppercore.exceptions.NotFoundException;
import org.coppercore.exceptions.ServiceLocatorException;

/**
 * The <code>RoleParticipation</code> business object represents the assignement of users to a role.
 *
 * <p> Instances of this class can only be created by calling either the create or find class
 * methods.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.15 $, $Date: 2005/06/17 10:37:53 $
 */
public class RoleParticipation implements Serializable {
  private static final long serialVersionUID = 42L;
  private RoleParticipationDto dto;
  private static RoleParticipationEntityHome home = null;

  /**
   * The contructor for a new RoleParticipation instance.
   *
   * <p>The constructor is private to enforce the usage of the class factory methods to create a new
   * user. Use RoleParticipation.create to create a new roleparticipation in the database, use the
   * RoleParticipation.find* methods to lookup existing roleparticipations from the database.
   *
   * @param dto a RoleParticipationDto containing the properties of the roleparticipation.
   */
  private RoleParticipation(RoleParticipationDto dto) {
    this.dto = dto;
  }

  /**
   * Returns the <code>RoleInstance</code> of this role participation.
   * @return the RoleInstance of this role participation.
   */
  public RoleInstance getRole() {
  try {
      return RoleInstance.findByPrimaryKey(dto.getRoleInstanceId());
    }
    catch (NotFoundException ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Removes the RoleParticipation from the system.
   *
   * <p>In effect this means unassigning the specified user from the specified role.
   */
  public void remove() {
    try {
      /**
       * @todo Removing a user from a roleparticipation can have side effects
       * for all other participants of that role. Rolepart could be completed and
       * conditions using the users-in-role construct may be affected as well.
       * Currently this is not taken into consideration.
       */

      RoleParticipationEntityPK pk = new RoleParticipationEntityPK(dto.getUserId(), dto.getRoleInstanceId());
      RoleParticipationEntityHome rpHome = getRoleParticipationHome();
      RoleParticipationEntity rp = rpHome.findByPrimaryKey(pk);
      rp.remove();
      
      ComponentFactory.getPropertyFactory().clearCache();      
    }
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Creates a new RoleParticipation business object.
   *
   * <p>The roleparticipation represents the assignment of a particular user to a particular role. The
   * new roleparticipation is created according to the specified parameters. The roleparticipation is
   * persisted in the database.
   *
   * @param user a User to assign to the run.
   * @param role the Role the user is assigned to.
   * @return the new created RoleParticipation.
   * @throws AlreadyExistsException if a role participation for the given user and role already
   *   exists.
   */
  public static RoleParticipation create(User user, RoleInstance role) throws
      AlreadyExistsException {
    try {
      // check if user already is assigned to the role
      findByPrimaryKey(user, role);
      // the user is already assigned, throw an exception
      throw new AlreadyExistsException("User " + user.getId() +
                                       " is already assigned to role " +
                                       role.getId());
    }
    catch (NotFoundException nfex) {
      // user is not assigned to role, so create a new RoleParticipation
      try {
        RoleParticipationDto dto = new RoleParticipationDto(user.getId(),
            role.getId());
        RoleParticipationEntity rpe = getRoleParticipationHome().create(dto);

        //fetch the corresponding run
        Run run = Run.findByPrimaryKey(dto.getRunId());

        //notify the event dispatcher about the role assignment
        EventDispatcher.getEventDispatcher().postMessage(run.getUol(),run,user,null,EventDispatcher.ROLE_EVENT);

        return new RoleParticipation(rpe.getDto());
      }
      catch (Exception ex) {
        // inform ejb container about application exceptions by rethrowing them as an EJBException.
        throw new EJBException(ex);
      }
    }
  }

  /**
   * Returns the <code>RoleParticipation</code> that belongs to the specified user and role.
   *
   * @param user the User to find.
   * @param role the RoleInstance the user is assigned to.
   * @throws NotFoundException if the <code>RoleInstance</code> couldn't be located.
   * @return the RoleParticipation defining the assigment of the user to the role.
   */
  public static RoleParticipation findByPrimaryKey(User user, RoleInstance role) throws
      NotFoundException {
    RoleParticipationEntityPK pk = new RoleParticipationEntityPK(user.getId(), role.getId());
    try {
      RoleParticipationEntity rolePart = getRoleParticipationHome().findByPrimaryKey(pk);
      return new RoleParticipation(rolePart.getDto());
    }
    catch (FinderException ex) {
      throw new NotFoundException(ex);
    }
  }

  /**
   * Returns a <code>Collection</code> of <code>RoleParticipation</code>s for all roles the
   * specified user is assigned to in the specfied run.
   *
   * @param user the User to find the role assignments for.
   * @param run the Run the roles are assigned to.
   * @return a Collection of RoleParticipations.
   */
  public static Collection findByUser(User user, Run run) {
  try {
      Collection rpes = getRoleParticipationHome().findByUserId(user.getId(), run.getId());
      return convertCollection(rpes);
    }
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Returns a boolean indicating if a user is part of a role.
   *
   * @param run the Run for which the request is done (same user may have different role
   *   instances for the same LD role in different runs)
   * @param user String the user-id of the user for which this request is made
   * @param roleId String the role-id
   * @return boolean true is the user is a member of this role, false otherwise
   */
  public static boolean isMemberOfRole(Run run, User user, String roleId) {
  try {
      getRoleParticipationHome().findByUserRole(run.getId(),user.getId(), roleId);
      return true;
    }
    catch (FinderException ex) {
      return false;
    }
  }

  /**
   * Returns the RoleParticipation for the passed user, role and run.
   * @param run Run
   * @param user User
   * @param roleId String
   * @throws NotFoundException whenever a user is not assigned to the role
   * @return RoleParticipation
   */
  public static RoleParticipation findRoleParticipation(Run run, User user, String roleId) throws NotFoundException{
  try {
      RoleParticipationEntity rpe = getRoleParticipationHome().findByUserRole(run.getId(),user.getId(), roleId);
      return new RoleParticipation(rpe.getDto());
    }
    catch (FinderException ex) {
      throw new NotFoundException(ex);
    }
  }

  /**
   * Converts a collection of <code>RoleParticipationDto</code>'s to a collection of
   * <code>RoleParticipation</code>s.
   *
   * @param rpes the Collection RoleParticipationDto's to convert.
   * @return the converted Collection of RoleParticipations.
   */
  private static Collection convertCollection(Collection rpes) {
  ArrayList result = new ArrayList();
    Iterator it = rpes.iterator();
    while (it.hasNext()) {
      result.add(new RoleParticipation( ( (RoleParticipationEntity) it.next()).
                                      getDto()));
    }
    return result;
  }


  /**
   * Returns the home interface for the RoleParticipationEntity entity bean.
   *
   * @return the RoleParticipationEntityHome
   */
  private static RoleParticipationEntityHome getRoleParticipationHome() {
    // Because looking up a home interface is an expensive operation, fetch it only once.
    if (home == null) {
      try {
        // First time, fetch a new home interface
        ServiceLocator locator = ServiceLocator.getInstance();
        home = (RoleParticipationEntityHome) locator.
            getEjbLocalHome(HomeFactory.ROLEPARTICIPATIONENTITY);
      }
      catch (ServiceLocatorException slex) {
        throw new EJBException(slex);
      }
    }
    return home;
  }

}
