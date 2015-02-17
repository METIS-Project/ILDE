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
import org.coppercore.dto.RoleInstanceDto;
import org.coppercore.dto.RoleInstanceEntityPK;
import org.coppercore.entity.RoleInstanceEntity;
import org.coppercore.entity.RoleInstanceEntityHome;
import org.coppercore.exceptions.NotFoundException;
import org.coppercore.exceptions.ServiceLocatorException;


/**
 * The <code>RoleInstance</code> business object represents the persisted roleinstances of
 * CopperCore.
 *
 * <p> Instances of this class can only be created by calling either the create or finder class
 * methods.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.10 $, $Date: 2005/01/12 14:34:31 $
 */
public class RoleInstance implements Serializable {
  private static final long serialVersionUID = 42L;
  private RoleInstanceDto dto;
  private static RoleInstanceEntityHome roleHome = null;

  private RoleInstance(RoleInstanceDto dto) {
    this.dto = dto;
  }

  /**
   * Returns the role instance id.
   * @return an int containing the role instance id.
   */
  public int getId() {
    return dto.getRoleInstanceId();
  }

  /**
   * Returns the LD role id of the role instance.
   * @return a String containing the LD role id.
   */
  public String getRoleId() {
    return dto.getRoleId();
  }

  /**
   * Returns the <code>Run</code> of this role instance.
   * @return a <code>Run</code> object.
   */
  public Run getRun() {
    try {
      return Run.findByPrimaryKey(dto.getRunId());
    }
    catch (NotFoundException ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Creates a new <code>RoleInstance</code> object with the specified properties.
   *
   * <p>The new object is also persisted in the database.
   *
   * @param roleId a String containing the LD role identifier.
   * @param run the Run where the roleinstance belongs to.
   * @return the new created RoleInstance.
   */
  public static RoleInstance create(String roleId, Run run) {
  try {
      RoleInstanceDto dto = new RoleInstanceDto(roleId, run.getId());
      RoleInstanceEntity roleEntity = getRoleHome().create(dto);
      return new RoleInstance(roleEntity.getRoleInstanceDto());
    }
    catch (Exception ex) {
      // inform ejb container about fatal application exceptions by rethrowing them as an EJBException.
      throw new EJBException(ex);
    }
  }

  /**
   * Returns the <code>RoleInstance</code> with the specified id.
   *
   * <p>If the specified role instance cannot be located a <code>NotFoundException</code> is thrown.
   *
   * @param id an int specifying the role instance to look up.
   * @throws NotFoundException if the specified roleinstance cannot be found.
   * @return the RoleInstance representing the specified role instance.
   */
  public static RoleInstance findByPrimaryKey(int id) throws NotFoundException {
  RoleInstanceEntity roleEntity = null;
    try {
      roleEntity = getRoleHome().findByPrimaryKey(new RoleInstanceEntityPK(id));
      return new RoleInstance(roleEntity.getRoleInstanceDto());
    }
    catch (ServiceLocatorException slex) {
      throw new EJBException(slex);
    }
    catch (FinderException fex) {
      throw new NotFoundException(fex);
    }
  }

  /**
   * Returns a <code>Collection</code> of all <code>RoleInstances</code> for the specified roleId.
   *
   * @param roleId an int specifying the role to look for
   * @param run a Run where the role instances belong to
   * @return the RoleInstance representing the specified role instance
   */
  public static Collection findByRoleId(String roleId, Run run) {
    ArrayList result = new ArrayList();

    try {
      Collection roleInstanceEntities = getRoleHome().findByRoleId(run.getId(), roleId);

      Iterator iter = roleInstanceEntities.iterator();
      while (iter.hasNext()) {
        RoleInstanceEntity item = (RoleInstanceEntity)iter.next();
        result.add(new RoleInstance(item.getRoleInstanceDto()));
      }
      return result;
    }
    catch (ServiceLocatorException slex) {
      throw new EJBException(slex);
    }
    catch (FinderException fex) {
      //should not occur as an empty collection is returned whenever no role instances can be found
      return result;
    }
  }

  private static RoleInstanceEntityHome getRoleHome() throws
      ServiceLocatorException {
    // Because looking up a home interface is an expensive operation, fetch it only once.
    if (roleHome == null) {
      // First time, fetch a new home interface
      ServiceLocator locator = ServiceLocator.getInstance();
      roleHome = (RoleInstanceEntityHome) locator.getEjbLocalHome(HomeFactory.ROLEINSTANCEENTITY);
    }
    return roleHome;
  }

}
