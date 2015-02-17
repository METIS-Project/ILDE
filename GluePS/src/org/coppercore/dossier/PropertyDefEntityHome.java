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

package org.coppercore.dossier;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * Defines the factory interface for creating and finding PropertyDefEntity
 * beans.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.12 $, $Date: 2005/01/11 13:15:04 $
 */
public interface PropertyDefEntityHome
    extends javax.ejb.EJBLocalHome {

  /**
   * Creates a new PropertyDefinition record in the database, sets its data
   * to the specified values and returns a PropertyDefEntity instance representing
   * this database entity.
   *
   * @param scope int defines the scope of the property
   * @param dataType String the data type of the property
   * @param defaultValue String the default value for the properties derived
   *   from this definition
   * @param href String the uri of the property if its scop is global
   * @param definedBy int the id of the unit of learning where this property is
   *   defined
   * @throws CreateException when an error occurs during the creation of the
   *   instance
   * @return PropertyDefEntity the new created instance
   */
  public PropertyDefEntity create(int scope, String dataType, String defaultValue, String href, int definedBy) throws
      CreateException;

  /**
   * Finds the property definition with the specified LD property id in the
   * specified unit of learning.
   *
   * @param uolId int the id of the unit of learning this property definition
   *   belongs to
   * @param propId String the learning design id of the property definition to
   *   look for
   * @throws FinderException when no property definition could be found
   * @return PropertyDefEntity the found property definition
   */
  public PropertyDefEntity findByLookUp(int uolId, String propId) throws
      FinderException;

  /**
   * Finds the property definition by searching for the specified uri.
   *
   * <p>If no property definition is found, the method throws a FinderException.
   *
   * @param uri String the uri of the property to look for
   * @throws FinderException when no property definition is found
   * @return PropertyDefEntity the found property definition
   */
  public PropertyDefEntity findByUri(String uri) throws FinderException;

  /**
   * Finds all property definitions for the specified unit of learning.
   *
   * <p>If no property definitions are found the method returns an empty
   * collection/
   *
   * @param uolId int the id of the unit of learning to locate all property
   *   definitions for
   * @throws FinderException when there is an error accessing the database.
   * @return Collection of all found property definitions, or an empty
   *   collection if no property definition is found
   */
  public Collection /* PropertyDefEntity */ findByUol(int uolId) throws
      FinderException;

  /**
   * Finds the property definition with the given primary key in the database.
   *
   * <p>If no property definition is found, the method throws a FinderException.
   *
   * @param pk PropertyDefEntityPK the primary key of the property definition
   *   to look for
   * @throws FinderException when the property with the given primary key could
   *   not be located
   * @return PropertyDefEntity the found PropertyDefEntity definition
   */
  public PropertyDefEntity findByPrimaryKey(PropertyDefEntityPK pk) throws
      FinderException;
}
