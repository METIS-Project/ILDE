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
* Defines the factory interface for creating and finding PropertyLookUpEntity
* beans.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.10 $, $Date: 2005/01/11 13:15:04 $
 */
public interface PropertyLookUpEntityHome extends javax.ejb.EJBLocalHome {

  /**
   * Creates a new property lookup record in the lookup table in the database.
   *
   * @param uolId int the id of the unit of learning the property belongs to
   * @param propId String the learning design id of the property
   * @param propDefPK int the primary key of the property definition of a
   *   property
   * @param type String the data type of the property
   * @throws CreateException when there is an error creating the lookup record
   *   in tha database
   * @return PropertyLookUpEntity the just created lookup record
   */
  public PropertyLookUpEntity create(int uolId, String propId, int propDefPK, String type) throws
      CreateException;

  /**
   * Locates the lookup record with the given primary key in the database.
   *
   * @param pk PropertyLookUpEntityPK the primary key of the record to find
   * @throws FinderException when no record could be found for the specified
   *   primary key
   * @return PropertyLookUpEntity the found lookup record
   */
  public PropertyLookUpEntity findByPrimaryKey(PropertyLookUpEntityPK pk) throws
      FinderException;

  /**
   * Finds all lookup records for the specified property definition.
   *
   * <p>The method returns a Collection of PropertyLookUpEntity, all found
   * records, or an empty collection if no records where found.
   *
   * @param propDefForeignPk int the id of the property definition to find the
   *   records for
   * @throws FinderException when there is an error finding the lookup records
   * @return Collection of PropertyLookUpEntity, all found lookup records
   */
  public Collection /* PropertyLookUpEntity */ findByPropDefForeignPk(int propDefForeignPk) throws
      FinderException;

  /**
   * Finds all lookup records for the specified unit of learning.
   *
   * <p>The method returns a Collection of PropertyLookUpEntity, all found
   * records, or an empty collection if no records where found.
   *
   * @param uolId int the id of the unit of learning to find all lookup records
   *   for
   * @throws FinderException when there is an error locating the lookp records
   * @return Collection of PropertyLookUpEntity, all found lookup records, or
   *   an empty collection if no records where found
   */
  public Collection /* PropertyLookUpEntity */ findByUol(int uolId) throws FinderException;
}
