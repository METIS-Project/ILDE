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

package org.coppercore.entity;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import org.coppercore.dto.RoleInstanceDto;
import org.coppercore.dto.RoleInstanceEntityPK;

/**
 * Defines the factory interface for creating and finding RoleInstanceEntity beans.
 * <p>For all roles in the learning design one RoleInstance is created when a run is created. For
 * roles having the create-new attribute set to allowed more instances can be created.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.3 $, $Date: 2005/01/11 13:15:12 $
 */
public interface RoleInstanceEntityHome extends javax.ejb.EJBLocalHome {

  /**
   * Creates a new RoleInstance entity in the database, based on the specified parameters.
   *
   * @param roleInstanceDto RoleInstanceDto a data transfer object defining all creation parameters
   * @throws CreateException when the entity could not be created
   * @return RoleInstanceEntity the new created role instance entity
   */
  public RoleInstanceEntity create(RoleInstanceDto roleInstanceDto) throws
      CreateException;

  /**
    * Loads the RoleInstanceEntity with the given primary key exists in the database.
    *
    * <p>If the entity is found the method returns RoleInstanceEntity, else it throws a FinderExceptoin.
    *
    * @param pk RoleInstanceEntityPK the key of the RoleInstanceEntity to locate
    * @throws FinderException when the entity could not be located
    * @return RoleInstanceEntity the entity with the given primary key
    */
  public RoleInstanceEntity findByPrimaryKey(RoleInstanceEntityPK pk) throws
      FinderException;

  /**
   * Finds all instances of the specified role in the specified run.
   *
   * <p>For all roles in the learning design one RoleInstance is created when a run is created. For
   * roles having the create-new attribute set to allowed more instances can be created.
   *
   * <p>If no instances are found, the method returns an empty Collection.
   *
   *
   * @param runId int the id of the run the role instances belong to
   * @param roleId String the learning design role id of the role these instances are created for
   * @throws FinderException never thrown, but required by the spec
   * @return Collection of RoleInstanceEntity's of the specified role. If no
   *   instances are found the method returns an empty Collection
   */
  public Collection findByRoleId(int runId, String roleId) throws
      FinderException;
}
