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

import java.util.Collection;

import org.coppercore.dossier.PropertyDefDto;
import org.coppercore.exceptions.PropertyException;

/**
 * The class represent the IMS LD defintion of an Environment. An
 * EnvironmentPropertyDef is an instance of a LocalPersonalContentDef.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/01/21 10:58:49 $
 */
public class LearningObjectPropertyDef extends LocalPersonalContentDef {
  private static final long serialVersionUID = 42L;
  private final int SCOPE = PropertyDef.LOCALPERSONAL;
  /**
   * Data type of this component.
   */
  public static final String DATATYPE = "learning-object";

  /**
   * Default constructor for this component definition during run time.
   * 
   * @param uolId
   *          int the database id of the Uol defining this component
   * @param propId
   *          String the identifier of this component as defined in IMS LD
   * @throws PropertyException
   *           whenever the constructor fails
   */
  public LearningObjectPropertyDef(int uolId, String propId) throws PropertyException {
    super(uolId, propId);
  }

  /**
   * Default constructor for this component definition during publication.
   * 
   * @param uolId
   *          int the database id of the Uol defining this component
   * @param propId
   *          String the identifier of this component as defined in IMS LD
   * @param isVisible
   *          String contains the visibility of this component. Valid values are
   *          "true" and "false"          
   * @param contentXml
   *          String an XML blob representing the content of this component
   * @param items
   *          Collection of String[] tuples containing the items defined in this
   *          component. The first String in the array represents the item
   *          identifier as defined in IMS LD and the second String in the tuple
   *          represents the visibility of that item (true of false).
   */
  public LearningObjectPropertyDef(int uolId, String propId, String isVisible, String contentXml, Collection items) {
    super(uolId, propId, isVisible, contentXml, items);

    defaultValue = buildDefaultValue();

    //use dto for better performance
    dto = new PropertyDefDto(SCOPE, DATATYPE, null, toXml(), uolId);
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
