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
 * The class represent the IMS LD defintion of a Play. A PlayPropertyDef is an
 * instance of a CompletionComponentDef.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.14 $, $Date: 2005/02/10 11:14:45 $
 */
public class PlayPropertyDef extends CompletionComponentDef {
  private static final long serialVersionUID = 42L;

  private final static int SCOPE = PropertyDef.LOCALPERSONAL;

  /**
   * Data type of this component.
   */
  public final static String DATATYPE = "play";

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
  public PlayPropertyDef(int uolId, String propId) throws PropertyException {
    super(uolId, propId);
  }

  /**
   * Constructor to be used during publishing.
   * 
   * <p>
   * This constructor creates a new LocalContentDef object based on the passed
   * parameters.
   * 
   * @param uolId
   *          int the database id of the Uol for which this property definition
   * @param propId
   *          String the identifier of this component as defined in IMS LD
   * @param completed
   *          String the initial completion state of an instance based on the
   *          implementation of this component. Valid value are "true" and
   *          "false".
   * @param isVisible
   *          String contains the visibility of this component. Valid values are
   *          "true" and "false"
   * @param contentXml
   *          String the String representation of the default XML value for the
   *          content
   * @param items
   *          Collection of String[] tuples representing all the items that are
   *          part of the content. The first element of the tuple contains the
   *          id of the item and the second element contains the visibility of
   *          that item.
   */
  public PlayPropertyDef(int uolId, String propId, String completed, String isVisible, String contentXml,
      Collection items) {
    super(uolId, propId, completed, isVisible, contentXml, items);

    defaultValue = buildDefaultValue();

    // use dto for better performance
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
