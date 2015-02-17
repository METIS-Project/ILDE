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

package org.coppercore.component;

import java.util.ArrayList;

import org.coppercore.datatypes.LDDataType;
import org.coppercore.datatypes.LDDuration;
import org.coppercore.dossier.PropertyDefDto;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.RestrictionViolationException;
import org.coppercore.exceptions.TypeCastException;

/**
 * This class represents the definition of A IMS-LD duration property.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/01/19 16:31:26 $
 */

public class DurationPropertyDef extends ExplicitPropertyDef {
  private static final long serialVersionUID = 1L;
  
  /**
   * contains the data type for this Property.
   */
  public static final String DATATYPE = LDDataType.LDDURATION_NAME;

  /**
   * Default constructor to be used during run time.
   *
   * @param uolId int the Uol database id of the IMS LD isntance defining this
   *   Property
   * @param propId String the identifier of the this Property as defined in the
   *   IMS-LD instance
   * @throws PropertyException when the constructor fails
   */
  public DurationPropertyDef(int uolId, String propId) throws PropertyException {
    super(uolId, propId);
  }

  /**
   * Default constructor to be used during publication.
   *
   * @param uolId int the Uol database id of the IMS LD isntance defining this
   *   Property
   * @param propId String String the identifier of the this Property as defined
   *   in the IMS-LD instance
   * @param scope int the scope of this property represented by an integer
   *   value. The following values are bitwise exclusive value representing the
   *   different scope aspects: GLOBAL = 0, LOCAL = 1, PERSONAL = 2, ROLE = 4
   * @param dataType String the data type of this Property
   * @param href String the URI of this Property if the scope is global. Should
   *   be null otherwise
   * @param initialValue String the initial value for this Property as defined
   *   in the IMS LD instance
   * @param restrictions ArrayList a collection of String[] tuples representing
   *   the restrictions that apply for instances of this PropertyDefintion. The
   *   first String in the tuple contains the restriction type. The second
   *   String of the tuple conains the restriction value.
   * @param metadata String the meta data for this PropertyDefinition as
   *   defined in the IMS LD instance
   * @param title String the title for this PropertyDefinition
   * @param roleId String the role identifier to be used for this
   *   PropertyDefinition. If the scope is not local role, this parameter may
   *   be null and will be ignored
   * @throws RestrictionViolationException if the default value violates any of
   *   the restrictions
   * @throws TypeCastException if the passed default can not be casted to the
   *   appropriate type
   * @throws PropertyException if the constructor fails
   */
  public DurationPropertyDef(int uolId, String propId, int scope, String dataType,
                             String href, String initialValue,
                             ArrayList restrictions,
                             String metadata, String title, String roleId) throws
      RestrictionViolationException, TypeCastException, PropertyException {
    super(uolId, propId, scope, dataType, href, initialValue, restrictions,
          metadata, title, roleId);
  }

  /**
   * This constructor will be called by any component factory that first needs
   * to load the PropertyDefinition dto to determine the proper Property type.
   *
   * @param uolId int the Uol database id of the IMS LD isntance defining this
   *   Property
   * @param propId String String the identifier of the this Property as defined
   *   in the IMS-LD instance
   * @param dto PropertyDefDto the data transfer object for this
   *   PropertyDefintion
   * @throws PropertyException when this constructors fails
   */
  public DurationPropertyDef(int uolId, String propId, PropertyDefDto dto) throws
      PropertyException {
    super(uolId, propId, dto);
  }

  /**
   * Returns the LDDataType object mathching this object on the basis of the
   * passed String value. An TypeCastException is thrown whenever the passed
   * value is not valid.
   *
   * @param value String
   * @throws TypeCastException
   * @return LDDataType
   */
  protected LDDataType createPropertyValue(String value) throws
      TypeCastException {
    return new LDDuration(value);
  }

  /**
   * Returns the datatype of this property definition.
   *
   * @return int the datatype represented as integer value
   */
  public int getType() {
    return LDDataType.LDDURATION;
  }

  /**
   * Returns the datatype of this property definition.
   *
   * @return String the datatype represented as string value
   */
  public String getDataType() {
    return DATATYPE;
  }
}
