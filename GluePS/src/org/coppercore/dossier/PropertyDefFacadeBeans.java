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
import java.util.ArrayList;
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
 * This class acts as facade for PropertyDefEntity. It encapsulates all the
 * communication with the property definition bean.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.2 $, $Date: 2007/04/04 09:04:06 $
 */
public class PropertyDefFacadeBeans
    implements Serializable {
  private static final long serialVersionUID = 42L;
  private static PropertyDefEntityHome pdeHome = null;
  private PropertyDefEntity pde = null;

  /**
   * This constructor should be used when creating a new PropertyDefEntity.
   *
   * <p> A CreateException is thrown if creation of PropertyDefEntity has failed.
   *
   * @param scope int integer representation of the scope
   * @param dataType String data type of this property definition
   * @param href String URI of the property when scope is global, null otherwise
   * @param defaultValue String the default value assigned to a property
   *   whenever a new property of this type is created
   * @param definedBy int the id of the unit of learning this property
   *   definition belongs to
   * @throws CreateException when there is an error accessing the database
   */
  public PropertyDefFacadeBeans(int scope, String dataType, String href,
                           String defaultValue, int definedBy) throws
      CreateException {
    pde = getHome().create(scope, dataType, defaultValue, href, definedBy);
  }

  /**
   * This constructor should be used when creating a new PropertyDefEntity.
   *
   * <p> A CreateException is thrown if creation of PropertyDefEntity has failed.
   *
   * @throws CreateException when there is an error accessing the database
   * @param dto PropertyDefDto the data transfer object containing all parameters
   * for the new instance
   */
  public PropertyDefFacadeBeans(PropertyDefDto dto) throws CreateException {
    pde = getHome().create(dto.getScope(), dto.getDataType(),
                           dto.getDefaultValue(), dto.getHref(),
                           dto.getDefinedBy());
  }

  /**
   * This constructor should be used whenever a existing PropertyDefEntity is
   * queried. A FinderException is thrown whenever a PropertyDefEntity could not
   * be found.
   *
   * @param uolId int unit of learning id of the request context
   * @param propId String propId for the requested property
   * @throws FinderException
   */
  public PropertyDefFacadeBeans(int uolId, String propId) throws FinderException {
    pde = getHome().findByLookUp(uolId, propId);
  }

  /**
   * Finds the property definition for the specified uri.
   * @param uri String the uri of the property definition to search for
   * @throws FinderException when the property definition coul not be found
   */
  public PropertyDefFacadeBeans(String uri) throws FinderException {
    pde = getHome().findByUri(uri);
  }

  private PropertyDefFacadeBeans(PropertyDefEntity pde) {
    this.pde = pde;
  }

  /**
   * Returns the PropertyDto of corresponding PropertyEntity.
   *
   * @return PropertyDto the data transfer object containing the data of this instance
   * @see #setDto
   */
  public PropertyDefDto getDto() {
    return pde.getDto();
  }

  /**
   * Sets the values of a PropertyDefEntity via a data transfer object.
   *
   * @param dto PropertyDefDto the new values of this instance
   * @see #getDto
   */
  public void setDto(PropertyDefDto dto) {
    pde.setDto(dto);
  }

  /**
   * Removes all property definitions belonging to the specified unit of
   * learning.
   *
   * @param uolId int the id of the unit of learning to remove all property
   *   definitions for.
   * @throws FinderException when the specified unit of learning could not be found
   * @throws RemoveException when there is an error removing a property definition
   */
  public static void removeDefinitions(int uolId) throws FinderException,
      RemoveException {

    //Collect the PropertyEntities matching the criteria of user and run
    Collection propDefPks = getHome().findByUol(uolId);

    //delete each of the properties
    Iterator iter = propDefPks.iterator();
    while (iter.hasNext()) {
      boolean remove = true;

      PropertyDefEntity propDefEntity = (PropertyDefEntity) iter.next();

      //check if we are dealing with a global definition
      if ( ( (PropertyDef.ISLOCAL & propDefEntity.getDto().getScope()) == 0)) {
        //we should check if this definition is used by other uol

        //get all facades using this global def
        Collection plfs = PropertyLookUpFacade.findLookups(propDefEntity.getDto().getPropDefPK());

        //check which property are defined by this UOL
        Iterator iter2 = plfs.iterator();
        while (iter2.hasNext()) {
          PropertyLookUpFacade plf = (PropertyLookUpFacade) iter2.next();

          //if this definition was used by another uol the uolId in the lookup
          //table will differ
          if (plf.getDto().getUolId() != uolId) {
            //another uol is using this definition
            remove = false;
            break;
          }
        }
      }

      //check if we have to remove this propertyDef
      if (remove) {
        propDefEntity.remove();
      }
    }
  }

  /**
   * Returns all PropertyDefFacades beloning to the specified unit of leaarning.
   *
   * @param uolId int the id of the unit of learning the facades belong to
   * @throws FinderException if the specified unit of learning could not be located
   * @return Collection of PropertyDefFacade for all found facades, or an empty Collection
   * if no property definition was found
   */
  public static Collection findPropertyDefinitionFacadeByUol(int uolId) throws
      FinderException {
    ArrayList result = new ArrayList();

    //Collect the PropertyDefEntities matching the uol
    Collection propDefPks = getHome().findByUol(uolId);

    //Add the corresponding facade to the result
    Iterator iter = propDefPks.iterator();
    while (iter.hasNext()) {
      PropertyDefEntity propDefEntity = (PropertyDefEntity) iter.next();

      //create a new facade on the basis of the found entity
      result.add(new PropertyDefFacadeBeans(propDefEntity));
    }

    return result;
  }

  private static PropertyDefEntityHome getHome() {
    // Because looking up a home interface is an expensive operation, fetch it only once.
    if (pdeHome == null) {
      // First time, fetch a new home interface
      try {
        ServiceLocator locator = ServiceLocator.getInstance();
        pdeHome = (PropertyDefEntityHome) locator.getEjbLocalHome(HomeFactory.
            PROPERTYDEFENTITY);
      }
      catch (ServiceLocatorException ex) {
        throw new EJBException(ex);
      }
    }
    return pdeHome;
  }
}
