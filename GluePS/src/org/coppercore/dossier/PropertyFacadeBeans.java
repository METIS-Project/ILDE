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

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import org.coppercore.common.HomeFactory;
import org.coppercore.common.ServiceLocator;
import org.coppercore.component.PropertyDef;
import org.coppercore.exceptions.ServiceLocatorException;

/**
 * This class acts as facade for PropertyEntity. It encapsulates all the
 * communication with the entity bean.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.1 $, $Date: 2006/08/17 15:14:51 $
 */
public class PropertyFacadeBeans implements Serializable {
  private static final long serialVersionUID = 42L;
  private static PropertyEntityHome peHome = null;
  private PropertyEntity pe = null;

  /**
   * This constructor should be used when creating a new PropertyEntity. A
   * CreateException is thrown if creation of PropertyEntity has failed. The
   * scope of this property is determined by the combination of runId and
   * userId. If runId is negative the scope is not limited to the run. If userId
   * is null the property is not associated with a user.
   *
   * @param runId int the run for this property, if negative run is not relevant
   * @param userId String the user id of the user owning this property may be
   *   null
   * @param propId String property id used in property lookup
   * @param scope int the scope of the property
   * @param propValue String property value
   * @param dataType String datatype of the property
   * @param propDefPK int primary key of the property definition belonging to
   *   this property
   * @throws CreateException when there is an error creating the facade
   */
  public PropertyFacadeBeans(int runId, String userId, String propId, int scope, String propValue,
                        String dataType, int propDefPK) throws CreateException {
    int paramRunId = runId;
    String paramUserId = userId;

    //check if we are dealing with a global scope
    if ( (PropertyDef.ISLOCAL & scope) == 0) {
      //negative runId will result in null value in database
      paramRunId = -1;
    }

    //check if we are not dealing with personal or a role scope
    if ( ( (PropertyDef.ISPERSONAL | PropertyDef.ISROLE) & scope) == 0) {
      paramUserId = null;
    }

    //create this property with the parameters determined earlier
    pe = getHome().create(propValue, dataType, paramRunId, paramUserId, propDefPK, scope, propId);
  }

  /**
   * This constructor should be used whenever a existing PropertyEntity is
   * queried.
   *
   * <p> A FinderException is thrown whenever a PropertyEntity could not be
   * found.
   *
   * @param uolId int unit of learning id of the request context
   * @param runId int runId of the request context
   * @param userId String userId of the request context
   * @param propId String propId for the requested property
   * @throws FinderException when the propery could not be found
   */
  public PropertyFacadeBeans(int uolId, int runId, String userId, String propId) throws FinderException, SQLException {
    pe = getHome().findByLookUp(uolId, propId, userId, runId);
  }

  /**
   * Returns the PropertyDto of corresponding PropertyEntity.
   *
   * @return PropertyDto
   */
  public PropertyDto getDto() {
    return pe.getDto();
  }

  /**
   * Sets the value of a PropertyEntity.
   *
   * @param value String the new value of the property
   */
  public void setValue(String value) {
    pe.setPropValue(value);
  }



  private static PropertyEntityHome getHome() {
    // Because looking up a home interface is an expensive operation, fetch it only once.
    if (peHome == null) {
      // First time, fetch a new home interface
      try {
        ServiceLocator locator = ServiceLocator.getInstance();
        peHome = (PropertyEntityHome) locator.getEjbLocalHome(HomeFactory.PROPERTYENTITY);
      }
      catch (ServiceLocatorException ex) {
        throw new EJBException(ex);
      }
    }
    return peHome;
  }

  /**
   * Removes all properties matching for the passed user in the passed run.
   *
   * @param userId String the id of the user to remove all properties for
   * @param runId int the id of the run to remove all properties for
   * @throws FinderException if there is an error finding the properties
   * @throws RemoveException when there is an error removing the found
   *   properties
   */
  public static void removeLocalProperties(String userId, int runId) throws FinderException, RemoveException {   
    //Collect the PropertyEntities matching the criteria of user and run
    Collection propPks = getHome().findByUserRun(userId, runId);

    //delete each of the properties
    Iterator iter = propPks.iterator();
    while (iter.hasNext()) {
      PropertyEntity propEntity = (PropertyEntity) iter.next();
      propEntity.remove();
    }
  }

  /**
   * Removes all properties matching for the passed run.
   *
   * @param runId int the id of the run to remove all properties of
   * @throws FinderException when there is an error finding the properties of
   *   the specified run
   * @throws RemoveException when there is an error deleting the found
   *   properties
   */
  public static void removeLocalProperties(int runId) throws FinderException, RemoveException {
    //Collect the PropertyEntities matching the criteria of user and run
    Collection propPks = getHome().findByRun(runId);

    //delete each of the properties
    Iterator iter = propPks.iterator();
    while (iter.hasNext()) {
      PropertyEntity propEntity = (PropertyEntity) iter.next();
      propEntity.remove();
    }
  }

  /**
   * Removes all properties matching for the specified uol.
   *
   * @param uolId int the id of the unit of learning to remove all properties for
   * @throws FinderException when there is an error finding the properties of
   *   the specified run
   * @throws RemoveException when there is an error deleting the found
   *   properties
   */
  public static void removeUolProperties(int uolId) throws FinderException, RemoveException {
    //Collect the PropertyEntities matching the criteria of user and run
    Collection propPks = getHome().findByUol(uolId);

    //delete each of the properties
    Iterator iter = propPks.iterator();
    while (iter.hasNext()) {
      PropertyEntity propEntity = (PropertyEntity) iter.next();
      propEntity.remove();
    }
  }

  /**
   * Removes all properties belonging to the passed user.
   *
   * @param userId int the id of the user to remove all properties for
   * @throws FinderException when there is an error finding the properties of
   *   the specified run
   * @throws RemoveException when there is an error deleting the found
   *   properties
   */
  public static void removeUserProperties(String userId) throws FinderException, RemoveException {
    //Collect the PropertyEntities matching the criteria of user and run
    Collection propPks = getHome().findByUser(userId);

    //delete each of the properties
    Iterator iter = propPks.iterator();
    while (iter.hasNext()) {
      PropertyEntity propEntity = (PropertyEntity) iter.next();
      propEntity.remove();
    }
  }


}
