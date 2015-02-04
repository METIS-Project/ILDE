/*
 * CopperCore , an IMS-LD level C engine
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
import org.coppercore.dossier.PropertyFacade;
import org.coppercore.entity.UserEntity;
import org.coppercore.entity.UserEntityHome;
import org.coppercore.exceptions.AlreadyExistsException;
import org.coppercore.exceptions.NotFoundException;
import org.coppercore.exceptions.ServiceLocatorException;

/**
 * The User business object represents the persisted users of CopperCore.
 *
 * <p> Instances of this class can only be created by calling either the create or finder class
 * methods.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.15 $, $Date: 2006/12/05 16:26:05 $
 */
public class User implements Serializable {
  private static final long serialVersionUID = 42L;
  private String id;
  private static UserEntityHome userEntityHome = null;

  /**
   * The contructor for a new User instance.
   *
   * <p>The constructor is private to enforce the usage of the class factory methods to create a new
   * user. Use User.create to create a new user in the database, use the User.find* methods to
   * lookup existing users from the database.
   *
   * @param userId a String identifying this user.
   */
  private User(String userId) {
  this.id = userId;
  }

  /**
   * Returns the user id value.
   *
   * @return a String containing the userid
   */
  public String getId() {
    return id;
  }

  /**
   * Removes the user from the system.
   *
   * <p>Removing the user involves removing all properties for that user.</p>
   */
  public void remove() {
    try {

      //first remove this user from any runparticipations
      Collection rps = RunParticipation.findByUser(this);

      Iterator iter = rps.iterator();
      while (iter.hasNext()) {
        RunParticipation rp = (RunParticipation)iter.next();
        rp.remove();
      }

      //remove all remaining user properties which should be global
      PropertyFacade.removeUserProperties(this.getId());


      //finally remove the user
      UserEntity userEntity = getUserEntityHome().findByPrimaryKey(this.getId());
      userEntity.remove();
      
      ComponentFactory.getPropertyFactory().clearCache();      
    }
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }


//   Factory section
//   The methods in this section are class methods that enable the creation of User objects.

  /**
   * Creates a new User object.
   *
   * <p> This new user is also persisted in the database. The user is only created if there is no
   * other user with the specified userid.
   *
   * @param userId String
   * @throws AlreadyExistsException if a user with userId already exists.
   * @return User representing the newly created user.
   */
  public static User create(String userId) throws AlreadyExistsException {
    try {
      // check if user already exists
      findByPrimaryKey(userId);
      // user already exists, throw exception
      throw new AlreadyExistsException("User " + userId + " already exists.");
    }
    catch (NotFoundException nfex) {
      try {
        // user does not exist, so create a new user
        getUserEntityHome().create(userId);
        return new User(userId);
      }
      // inform ejb container about application exceptions by rethrowing them as an EJBException.
      catch (Exception ex) {
        throw new EJBException(ex);
      }
    }
  }

  /**
   * Returns a <code>User</code> object representing the user userId.
   *
   * @param userId a String
   * @throws NotFoundException
   * @return a User object representing the user userId
   */
  public static User findByPrimaryKey(String userId) throws NotFoundException {
  try {
      getUserEntityHome().findByPrimaryKey(userId);
      return new User(userId);
    }
    // inform ejb container about application exceptions by rethowing them as an EJBException.
    catch (ServiceLocatorException ex) {
      throw new EJBException(ex);
    }
    catch (FinderException ex) {
      throw new NotFoundException(ex);
    }
  }

  /**
   * Returns a collection of all users in the specified run.
   *
   * <p> The users are returned as User objects. If there are no users, the method returns an empty
   * collection.
   *
   * @param run Run  - is the run to retrieve all users for
   * @return a Collection of User objects for all users in the specified run
   */
  public static Collection findByRunId(Run run) {
    ArrayList result = new ArrayList();
    Collection rps = RunParticipation.findByRun(run);
    Iterator it = rps.iterator();
    while (it.hasNext()) {
      // Create a User directly from userId, works only if a User has no additional data
      result.add( new User(( (RunParticipation) it.next()).getDto().getUserId()));
    }
    return result;
  }


  /**
   * Returns a collection of all users in CoperCore.
   *
   * <p> The users are returned as User objects. If there are no users, the method returns an empty
   * collection.
   *
   * @return a Collection of User objects for all users.
   */
  public static Collection findAllUsers() {
  ArrayList result = new ArrayList();
    try {
      Collection allUsers = getUserEntityHome().findAllUsers();
      Iterator it = allUsers.iterator();
      while (it.hasNext()) {
        UserEntity item = (UserEntity) it.next();
        result.add(new User(item.getUserId()));
      }
      return result;
    }
    // inform ejb container about application exceptions by rethowing them as an EJBException.
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Finds all users that match the specified LD role and run.
   *
   * <p> If no users match these criteria, the method returns an empty collection.
   *
   * @param runId int - element representing the run.
   * @param roleId String - element representing the role
   * @return a Collection of User objects that match the search criteria.
   */
  public static Collection findByRoleId(int runId, String roleId) {
    ArrayList result = new ArrayList();
    try {
      Collection allUsers = getUserEntityHome().findByRoleId(runId, roleId);
      Iterator it = allUsers.iterator();
      while (it.hasNext()) {
        UserEntity item = (UserEntity) it.next();
        result.add(new User(item.getUserId()));
      }
      return result;
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }
  
  /**
   * Finds all users that match the specified role instances and run.
   *
   * <p> If no users match these criteria, the method returns an empty collection.
   *
   * @param runId int - element representing the run.
   * @param roleInstanceIds String - element representing the role instances
   * @return a Collection of User objects that match the search criteria.
   */
  public static Collection findByRoleInstanceId(int runId, String roleInstanceIds) {
    ArrayList result = new ArrayList();
    try {
      Collection allUsers = getUserEntityHome().findByRoleInstanceId(runId, roleInstanceIds);
      Iterator it = allUsers.iterator();
      while (it.hasNext()) {
        UserEntity item = (UserEntity) it.next();
        result.add(new User(item.getUserId()));
      }
      return result;
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }


  /**
   * Returns the home interface for the UserEntity bean.
   *
   * @throws ServiceLocatorException
   * @return the home interface for the UserEntity bean
   */
  private static UserEntityHome getUserEntityHome() throws ServiceLocatorException {
// Because looking up a home interface is an expensive operation, fetch it only once.
    if (userEntityHome == null) {
      // First time, fetch a new home interface
        ServiceLocator locator = ServiceLocator.getInstance();
        userEntityHome = (UserEntityHome) locator.getEjbLocalHome(HomeFactory.USERENTITY);
    }
    return userEntityHome;
  }

  /**
   * Return true if the passed object equals this object which is true when we
   * are dealing with the same object or if the id is the same.
   *
   * @param obj Object the object to compare
   * @return boolean true when this object equals the passed object
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (! (obj instanceof User)) {
      return false;
    }
    User that = (User) obj;

    if (! (that.id == null ? this.id == null :
           that.id.equals(this.id))) {
      return false;
    }
    return true;
  }
}
