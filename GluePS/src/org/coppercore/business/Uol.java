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
import org.coppercore.dossier.PropertyDefFacade;
import org.coppercore.dossier.PropertyFacade;
import org.coppercore.dossier.PropertyLookUpFacade;
import org.coppercore.dto.UnitOfLearningPK;
import org.coppercore.dto.UolDto;
import org.coppercore.entity.UnitOfLearning;
import org.coppercore.entity.UnitOfLearningHome;
import org.coppercore.exceptions.NotFoundException;
import org.coppercore.exceptions.ServiceLocatorException;

/**
 * The <code>Uol</code> business object represents the persisted unit of learnings of CopperCore.
 *
 * <p> Instances of this class can only be created by calling either the create or finder class
 * methods.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.14 $, $Date: 2005/06/17 10:37:53 $
 */
public class Uol implements Serializable {
  private static final long serialVersionUID = 42L;
  private UolDto dto;
  private static UnitOfLearningHome uolHome = null;

  /**
   * The contructor for a new Uol instance.
   *
   * <p>The constructor is private to enforce the usage of the class factory methods to create a new
   * run. Use Uol.create to create a new rnu in the database, use the Uol.find* methods to lookup
   * existing runs from the database.
   *
   * @param dto a UolDto containing the unit of learning properties.
   */
  private Uol(UolDto dto) {
    this.dto = dto;
  }

  /**
   * Returns the dto of this unit of learning.
   *
   * @return a UolDto containing all properties of this unit of learning.
   */
  public UolDto getDto() {
  return dto;
  }

  /**
   * Returns the title of the unit of learning.
   *
   * @return a String containing the title.
   * @see #setTitle
   */
  public String getTitle() {
    return dto.getTitle();
  }


  /**
   * Returns the content uri of the unit of learning.
   * @return a String containing the content uri.
   * @see #setContentUri
   */
  public String getContentUri() {
    return dto.getContentUri();
  }

  /**
   * Returns the uri of the unit of learning.
   *
   * <p>The uri represents the globally unique identifier of the unit of learning. The id of the
   * unit of learning, as retrieved with <code>getId()</code>, is only unique in the scope of an
   * installation of CopperCore.
   *
   * @return a String containing the uri of the unit of learning.
   */
  public String getUri() {
    return dto.getUri();
  }

  /**
   * Returns the id of the unit of learning.
   *
   * <p>The scope of this id is the installation of CopperCore. To retrieve the globally unique
   * identifier of the unit of learning use <code>getUri()</code>.
   *
   * @return an int containing the id of the unit of learning.
   */
  public int getId() {
    return dto.getId();
  }

  /**
   * Sets the title of the unit of learning.
   *
   * @param title a String containing the new title.
   * @see #getTitle
   */
  public void setTitle(String title) {
  dto.setTitle(title);
    persist();
  }

  /**
   * Sets the content uri of the unit of learning.
   *
   * <p>The content uri is the web offset of the location where the local webcontent is stored.
   *
   * @param contentUri a String containing the new content uri.
   * @see #getContentUri
   */
  public void setContentUri(String contentUri) {
  dto.setContentUri(contentUri);
    persist();
  }

  private void persist() {
    UnitOfLearningPK pk =  new UnitOfLearningPK(dto.getId());
    try {
      UnitOfLearning uolEntity = getUolHome().findByPrimaryKey(pk);
      uolEntity.setUolDto(dto);
    }
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Remove all runs that are associated with this uol
   */
  private void removeAllRuns() {

    //get all the runs associated with this uol
    Collection runs = Run.findByUol(this);

    //now remove them
    Iterator iter = runs.iterator();
    while (iter.hasNext()) {
      Run run = (Run)iter.next();
      run.remove();
    }
  }

  /**
   * Removes the uol from the system.
   *
   * <p>Removing the uol from the system involves removing all runs and the
   * removal of several properties, property definitions and property lookups.
   * and all run properties first.</p>
   */
  public void remove() {
    UnitOfLearningPK pk = new UnitOfLearningPK(dto.getId());
    try {
      //(1) remove all runs that are associated with this uol
      removeAllRuns();

      //(2) remove all property values associated with this uol
      PropertyFacade.removeUolProperties(dto.getId());

      //(3) now we remove all property definitions associated with this uol
      PropertyDefFacade.removeDefinitions(this.getId());

      //(4) now we remove all the property lookup associated with this uol
      PropertyLookUpFacade.removeLookups(this.getId());

      //(5) now remove the events associated with this uol
      Event.remove(this);

      // (6) remove this uol
      UnitOfLearning re = getUolHome().findByPrimaryKey(pk);
      re.remove();
      
      ComponentFactory.getPropertyFactory().clearCache();      
    }
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }


  /**
   * Creates a new unit of learning business object.
   *
   * <p>The unit of learning is created according to the parameters that are specified. The new unit
   * of learning is persisted in the database.
   *
   * @param uri a String specifying the uri of the unit of learning.
   * @param title a String specifying the title of the unit of learning.
   * @param contentUri a String specifying the offset of the local web content.
   * @return the newly created Uol.
   */
  public static Uol create(String uri, String title, String contentUri) {
    try {
      UolDto dto = new UolDto(uri, title, "", contentUri);
      UnitOfLearning uolEntity = getUolHome().create(dto);
      return new Uol(uolEntity.getUolDto());
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }


  /**
   * Returns a <code>Uol</code> object representing the unit of learning with id uolId.
   *
   * <p>If the specified unit of learning cannot be located a NotFoundException is thrown.
   *
   * @param uolId an int specifying the unit of learning to find.
   * @throws NotFoundException when the specified unit of learning cannot be located.
   * @return a Uol object representing the unit of learning.
   */
  public static Uol findByPrimaryKey(int uolId) throws NotFoundException {
    UnitOfLearningPK pk = new UnitOfLearningPK(uolId);
    try {
      UnitOfLearning uolEntity = getUolHome().findByPrimaryKey(pk);
      return new Uol(uolEntity.getUolDto());
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (ServiceLocatorException slex) {
      throw new EJBException(slex);
    }
    catch (FinderException fex) {
      throw new NotFoundException(fex);
    }
  }

  /**
   * Find the unit of learning with the specified uri.
   *
   * <p>If the specified unit of learning cannot be located a NotFoundException is thrown.
   *
   * @param uri a String specifying the uri to look for.
   * @throws NotFoundException when the specified unit of learning cannot be located.
   * @return a Uol object representing the unit of learning.
   */
  public static Uol findByURI(String uri) throws NotFoundException {
      UnitOfLearning uolEntity = null;
      try {
        uolEntity = getUolHome().findByURI(uri);
        return new Uol(uolEntity.getUolDto());
      }
      // inform ejb container about application exceptions by rethrowing them as an EJBException.
      catch (ServiceLocatorException ex) {
        throw new EJBException(ex);
      }
      catch (FinderException ex) {
        throw new NotFoundException(ex);
      }
  }

  /**
   * Returns a <code>Collection</code> containing all unit of learnings present in the system.
   *
   * <p> If no unit of learning can be found, the method returns an empty collection.
   *
   * @return a Collection of Uol objects representing all unit ol learnings in the system.
   */
  public static Collection findAllUnitOfLearnings() {
  ArrayList result = new ArrayList();
    try {
      Collection allUols = getUolHome().findAllUnitOfLearnings();
      Iterator i = allUols.iterator();
      while (i.hasNext()) {
        UnitOfLearning item = (UnitOfLearning) i.next();
        result.add(new Uol(item.getUolDto()));
      }
      return result;
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
  * Returns a <code>Collection</code> of all Uol objects a particular user is assigned to.
  *
  * <p> If the user is not assigned to any unit of learning, the method returns an empy collection.
  *
  * @param user a User object representing the user.
  * @return a Collection of all unit of learnings the user is assigned to.
  */
 public static Collection findByUser(User user) {
    return findByUser(user.getId());
  }

  /**
   * Returns a <code>Collection</code> of all Uol objects a particular user is assigned to.
   *
   * <p> If the user is not assigned to any unit of learning, the method returns an empy collection.
   *
   * @param userId a String specifying the user.
   * @return a Collection of all unit of learnings the user is assigned to.
   */
  private static Collection findByUser(String userId) {
  ArrayList result = new ArrayList();
    try {
      Collection allUols = getUolHome().findByUser(userId);
      Iterator i = allUols.iterator();
      while (i.hasNext()) {
        UnitOfLearning item = (UnitOfLearning) i.next();
        result.add(new Uol(item.getUolDto()));
      }
      return result;
    }
    // inform ejb container about application exceptions by rethrowing them as an EJBException.
    catch (Exception ex) {
      throw new EJBException(ex);
    }
  }


  private static UnitOfLearningHome getUolHome() throws
      ServiceLocatorException {
    // Because looking up a home interface is an expensive operation, fetch it only once.
    if (uolHome == null) {
      // First time, fetch a new home interface
      ServiceLocator locator = ServiceLocator.getInstance();
      uolHome = (UnitOfLearningHome) locator.getEjbLocalHome(HomeFactory.
          UNITOFLEARNING);
    }
    return uolHome;
  }

  /**
   * Returns a String representation of this Uol instance.
   * @return a String representation of this Uol instance
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return "Uol[uolId=" + getId() + ", title=" + getTitle() + "]";
  }

}
