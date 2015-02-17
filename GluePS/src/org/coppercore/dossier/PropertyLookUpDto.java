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

/**
 * PropertyLookUpDto is a data transfer object for the PropertyLookup bean.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.7 $, $Date: 2008/05/15 15:32:37 $
 */
public class PropertyLookUpDto implements Serializable {
  private static final long serialVersionUID = 42L;

  private int propDefForeignPK;
  private String type = null;
  private int uolId;

  /**
   * Constructor for PropertyLookUpDto.
   *
   * @param uolId int the is of the unit of learning the property belongs to
   * @param propDefForeignPK int is the pk of the property definition entity
   * @param type String is the type of the property. This is needed for the the
   *   property factory.
   */
  public PropertyLookUpDto(int uolId, int propDefForeignPK, String type) {
    this.uolId = uolId;
    this.type = type;
    this.propDefForeignPK = propDefForeignPK;
  }

  /**
   * Returns the PropDefForeignPK value of this DTO.
   *
   * @return int representing the pk of the property definition entity
   */
  public int getPropDefForeignPK () {
    return propDefForeignPK;
  }

  /**
   * Return the Type value of this DTO.
   *
   * @return String representing the type of the property
   */
  public String getType() {
    return type;
  }

  /**
   * Return the unit of learning id of this DTO.
   *
   * @return int the unit of learning id
   */
  public int getUolId() {
    return uolId;
  }
}
