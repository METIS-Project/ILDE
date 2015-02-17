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

package org.coppercore.events;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Defines the factory interface for creating and finding EventEntity beans.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.3 $, $Date: 2005/01/11 13:15:06 $
 */
public interface EventEntityHome
    extends EJBLocalHome {

  /**
   * Creates a new Event entity in the database and sets in values to the data
   * specified in the passed data transfer object.
   *
   * @param dto EventDto the data transfer object containing the data of this
   *   instance
   * @throws CreateException when there is an error creating the new entity in
   *   the database
   * @return EventEntity the new created event instance
   */
  public EventEntity create(EventDto dto) throws CreateException;

  /**
   * Finds the EventEntity with the specified primary key in the database.
   *
   * <p>If no entity with the given key can be found the method throws a
   * FinderException.
   *
   * @param pk EventEntityPK the primary key of the EventEntity to find
   * @throws FinderException when no EventEntity with the given primary key can
   *   be found
   * @return EventEntity the found EventEntity
   */
  public EventEntity findByPrimaryKey(EventEntityPK pk) throws FinderException;

  /**
   * Finds all EventEntity's belonging to the specified unit of learning.
   *
   * <p>The method returns a Collection of all EventEntity's found. If no
   * EventEntity is found, the method returns an empty Collection.
   *
   * @param uolId int the unit of learning to find all EventEntity's for
   * @throws FinderException when there is an error accessing the database
   * @return Collection of all EventEntity's for the given unit of learning, or
   *   an empty Collection if no EventEntity's are found
   */
  public Collection findByUol(int uolId) throws FinderException;

  /**
   * Finds all EventEntity's beloning to the specified property definition of
   * the specified type.
   *
   * <p>The method returns a Collection of all EventEntityPK of the
   * EventEntity's found, or an empty Collection if no EventEntity's are found.
   *
   * @param propDefPK int the primary key of the property definition to look for
   * @param type String the type of the property definition to look for
   * @throws FinderException when there is an error accessing the database
   * @return Collection of all EventEntity found, or an empty Collection if no
   *   EventEntity is found
   */
  public Collection findByPropDefPK(int propDefPK, String type) throws
      FinderException;

  /**
   * Finds all EventEntity's of the specified type in the specified unit of
   * learning.
   *
   * <p>The method returns a Collection of all EventEntity's found, or an empty
   * Collection if no EventEntity is found.
   *
   * @param uolId int the unit of learning id being the scope of the search
   * @param type String the type of events to find
   * @throws FinderException when there is error accessing the database
   * @return Collection of all found EventEntity's, or an empty Collection if
   *   no EventEntity is found
   */
  public Collection findByType(int uolId, String type) throws FinderException;

}
