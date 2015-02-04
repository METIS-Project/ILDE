/*
 * CopperCore, an IMS-LD level C engine
 * Copyright (C) 2003 not attributable
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
 * not attributable
 *
 * prof.dr. Rob Koper,
 * director of learning technologies research and development
 *
 */
package org.coppercore.dossier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.coppercore.common.HomeFactory;
import org.coppercore.common.ServiceLocator;
import org.coppercore.exceptions.ServiceLocatorException;

/**
 * This class acts as facade for PropertyLookUpEntity. It encapsulates all the
 * communication with the entity bean.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/01/11 13:15:05 $
 */
public class PropertyLookUpFacade {
  private static PropertyLookUpEntityHome pleHome = null;
  private PropertyLookUpEntity ple = null;

  /**
   * This constructor should be used when creating a new PropertyLookUpEntity. A
   * CreateException is thrown if creation of PropertyLookUpEntity has failed.
   *
   * @param uolId int index of the unit of learning
   * @param propId String the id of the property as defined in the IMS-LD
   *   instance
   * @param propDefPK int the pk of the PropertyDefinitionEntity
   * @param type String the type of the property created
   * @throws CreateException if creation fails
   */
  public PropertyLookUpFacade(int uolId, String propId, int propDefPK,
                              String type) throws CreateException {
    ple = getHome().create(uolId, propId, propDefPK, type);
  }

  /**
   * This constructor should be used whenever a existing PropertyLookUpEntity is
   * queried. A FinderException is thrown is not PropertyLookUpEntity could be
   * found.
   *
   * @param uolId int index of the unit of learning
   * @param propId String the id of the property as defined in the IMS-LD
   *   instance
   * @throws FinderException if no PropertyLookUpEntity is found
   */
  public PropertyLookUpFacade(int uolId, String propId) throws FinderException {
    PropertyLookUpEntityPK pk = new PropertyLookUpEntityPK(uolId, propId);
    ple = getHome().findByPrimaryKey(pk);
  }

  private PropertyLookUpFacade(PropertyLookUpEntity ple) {
    this.ple = ple;
  }

  /**
   * Return the PropertyLookUpDto of corresponding PropertyLookUpEntity.
   *
   * @return PropertyLookUpDto
   */
  public PropertyLookUpDto getDto() {
    return ple.getDto();
  }

  /**
   * Removes this instance from the database.
   * @throws RemoveException when there is an error removing the instance from
   * the database
   */
  public void remove() throws RemoveException {
    pleHome.remove(ple.getPrimaryKey());
  }

  /**
   * Removes all propertiesLookup records forthe specified unit of learning.
   *
   * @param uolId int the id of the unit of learning to remove all lookup
   *   records for
   * @throws FinderException when there is error finding all lookup records for
   *   the specified unit of learning
   * @throws RemoveException when there is an error removing the found records
   */
  public static void removeLookups(int uolId) throws FinderException,
      RemoveException {

    //Collect the PropertyEntities matching the criteria of user and run
    Collection propLookUpPks = getHome().findByUol(uolId);

    //delete each of the properties
    Iterator iter = propLookUpPks.iterator();
    while (iter.hasNext()) {
      PropertyLookUpEntity propLookUpEntity = (PropertyLookUpEntity) iter.next();
      propLookUpEntity.remove();
    }
  }

  /**
   * Returns all PropertyLookupFacades matching the passed unit of lerning.
   *
   * @param propDefPk int the id of unit of learning to find all lookup records
   *   for
   * @throws FinderException when there is an error finding the records
   * @return Collection of PropertyLookUpFacade of all found lookup records
   */
  protected static Collection /* PropertyLookUpFacade */ findLookups(int
      propDefPk) throws FinderException {
    ArrayList result = new ArrayList();

    //Collect the PropertyEntities matching the uol
    Collection propLookUpPks = getHome().findByPropDefForeignPk(propDefPk);

    //Add the corresponding facade to the result
    Iterator iter = propLookUpPks.iterator();
    while (iter.hasNext()) {
      PropertyLookUpEntity propLookUpEntity = (PropertyLookUpEntity) iter.next();

      //create a new facade on the basis of the found entity
      result.add(new PropertyLookUpFacade(propLookUpEntity));
    }

    return result;
  }

  private static PropertyLookUpEntityHome getHome() {
    // Because looking up a home interface is an expensive operation, fetch it only once.
    if (pleHome == null) {
      // First time, fetch a new home interface
      try {
        ServiceLocator locator = ServiceLocator.getInstance();
        pleHome = (PropertyLookUpEntityHome) locator.getEjbLocalHome(
            HomeFactory.PROPERTYLOOKUPENTITY);
      }
      catch (ServiceLocatorException ex) {
        throw new EJBException(ex);
      }
    }
    return pleHome;
  }

}
