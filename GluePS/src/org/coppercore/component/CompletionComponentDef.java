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

import org.coppercore.exceptions.PropertyException;

/**
 * Implementations of this component have a completion state. Initial state of the completion will be made part of the default value of the component defintion. This class extends the LocalPersonalContentDef class as all components with a completion state are expected to have local personal content.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/01/20 16:12:04 $
 */
public abstract class CompletionComponentDef
    extends LocalPersonalContentDef {
  private String completed = null;

  /**
   * Constructor to be used during publishing. This constructor should be used
   * for components that do not have a visibilty property.
   * 
   * <p>
   * This constructor creates a new LocalContentDef object based on the passed
   * parameters.
   * 
   * @param uolId
   *          int the database id of the Uol for which this property definition
   * @param propId
   *          String the identifier of this component as defined in IMS LD
   * @param completed String the initial completion state of an instance based on the implementation of this component. Valid value are "true" and "false".         
   * @param contentXml
   *          String the String representation of the default XML value for the
   *          content
   * @param items
   *          Collection of String[] tuples representing all the items that are
   *          part of the content. The first element of the tuple contains the
   *          id of the item and the second element contains the visibility of
   *          that item.
   */
  public CompletionComponentDef(int uolId, String propId, String completed,
                                String contentXml, Collection items) {
    super(uolId, propId, contentXml, items);
    this.completed = completed;

    //construct the default value here. This may be overridden by the superconstructor
    this.defaultValue = buildDefaultValue();
  }

  /**
   * Constructor to be used during publishing. This constructor should be used
   * for components that do have a visibilty property.
   * 
   * <p>
   * This constructor creates a new LocalContentDef object based on the passed
   * parameters.
   * 
   * @param uolId
   *          int the database id of the Uol for which this property definition
   * @param propId
   *          String the identifier of this component as defined in IMS LD
   * @param completed String the initial completion state of an instance based on the implementation of this component. Valid value are "true" and "false".
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
  public CompletionComponentDef(int uolId, String propId, String completed,
                                String isVisible, String contentXml,
                                Collection items) {
    super(uolId, propId, isVisible, contentXml, items);
    this.completed = completed;

    //construct the default value here. This may be overridden by the superconstructor
    this.defaultValue = buildDefaultValue();
  }

  /**
   * Default constructor to be used during run time.
   *
   * @param uolId int the Uol database id of the IMS LD isntance defining this
   *   Property
   * @param propId String the identifier of the this Property as defined in the
   *   IMS-LD instance
   * @throws PropertyException when the constructor fails
   */
  public CompletionComponentDef(int uolId, String propId) throws
      PropertyException {
    super(uolId, propId);
  }

  /**
   * Returns the completed state of the implementations of this components as a String. Valid values are "true" and "false".
   * @return String the completed state of this component
   */
  protected String getCompleted() {
    return completed;
  }

  /**
   * This method builds the default value as XML fragment and adds it to the
   * passed StringBuffer. This method may be overloaded by implementing classes
   * in order to add more specific data to it.
   * 
   * @param result
   *          StringBuffer to which the resulting XML fragment should be
   *          appended.
   * @see #buildDefaultValue()
   */  
  protected void setDefaultValue(StringBuffer result) {
    super.setDefaultValue(result);
    result.append("<completed>" + getCompleted() + "</completed>");
  }

}
