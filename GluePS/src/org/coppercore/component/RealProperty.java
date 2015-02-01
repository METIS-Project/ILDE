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

package org.coppercore.component;

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.datatypes.LDReal;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;

/**
 * This Property represents an IMS LD real Property. It extends
 * ExplicitProperty adding type cast and restriction checking to the standard
 * Property mechanism.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.10 $, $Date: 2005/01/19 16:31:26 $
 */
public class RealProperty extends ExplicitProperty {
  private static final long serialVersionUID = 42L;

  /**
   * Default constructor during run time.
   *
   * @param uol Uol the Uol in which this Property was declared
   * @param run Run the Run in which this Property was referenced
   * @param user User the User referring to this Property
   * @param propId String the identifier of this Property as defined in the IMS
   *   LD instance
   * @throws PropertyException when the constructor fails to create this
   *   Property
   * @throws TypeCastException when the stored value can not be type casted to
   *   the type of the Property. This may occur especially after republication
   *   of the IMS LD instance.
   */
  public RealProperty(Uol uol, Run run, User user, String propId) throws
      PropertyException, TypeCastException {
    super(uol, run, user, propId);
  }

  /**
   * Returns the corresponding PropertyDefinition belonging to this component.
   *
   * @throws PropertyException when this operation fails
   * @return PropertyDef the PropertyDefinition for this component
   */
  protected PropertyDef findPropertyDef() throws
      PropertyException {
    return new RealPropertyDef(uolId, propId);
  }

  /**
   * Creates the LDDataType object corresponding with the object and its String
   * value.
   *
   * @param aValue String the value of this object
   * @throws TypeCastException when the passed value can not be converted to
   *   the required LDDataType
   * @return LDDataType the LDDataType object representing the value
   */
  protected LDDataType createPropertyValue(String aValue) throws
      TypeCastException {
    LDReal result = null;
    if (aValue != null) {
      result = new LDReal(aValue);
    }
    return result;
  }

  /**
   * Returns the passed value in the correct LDDataType by casting it.
   *
   * @param aValue LDDataType the value to be type casted
   * @throws TypeCastException if type cast fails
   * @return LDDataType the type casted value
   */
  public LDDataType typeCast(LDDataType aValue) throws TypeCastException {
    return aValue.toLDReal();
  }
}
