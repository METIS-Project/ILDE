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

import java.io.PrintWriter;
import java.util.Collection;

import org.coppercore.dossier.PropertyDefDto;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;

/**
 * The class represent the IMS LD defintion of an Act. An ActPropertyDef is an
 * instance of a CompletionComponentDef.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.16 $, $Date: 2005/01/20 16:12:04 $
 */
public class ActPropertyDef extends CompletionComponentDef {
  private static final long serialVersionUID = 1L;
  /**
   * Data type of this component.
   */
  public final static String DATATYPE = "act";

  private final static int SCOPE = PropertyDef.LOCAL; //changed 2004-11-20: used to be LOCALPERSONAL

  // NOTE: this property may not be initialized here because class intitialization will overrule any
  // value set during unpacking of the XML blob. Use the onInit() method instead for initializations that
  // are critical for the object creation
  private String previousActId;

  /**
   * Default constructor for this component definition during run time.
   *
   * @param uolId int the database id of the Uol defining this component
   * @param propId String the identifier of this component as defined in IMS LD
   * @throws PropertyException whenever the constructor fails
   */
  public ActPropertyDef(int uolId, String propId) throws PropertyException {
    super(uolId, propId);
  }

  /**
   * Default constructor for this component definition during publication.
   *
   * @param uolId int the database id of the Uol defining this component
   * @param propId String the identifier of this component as defined in IMS LD
   * @param completed String representation of the completion value. Valid
   *   values are true, false and unlimited
   * @param previousActId String the identifier of the previous in the sequence
   *   of act as defined in the IMS LD instance
   * @param contentXml String an XML blob representing the content of this
   *   component
   * @param items Collection of String[] tuples containing the items defined in
   *   this component. The first String in the array represents the item
   *   identifier as defined in IMS LD and the second String in the tuple
   *   represents the visibility of that item (true of false).
   */
  public ActPropertyDef(int uolId, String propId, String completed, String previousActId, String contentXml,
                        Collection items) {
    super(uolId, propId, completed, contentXml, items);

    this.previousActId = previousActId;

    //construct the default value here. This may be overridden by the superconstructor
    this.defaultValue = buildDefaultValue();

    //use dto for better performance
    dto = new PropertyDefDto(SCOPE, DATATYPE, null, toXml(), uolId);
  }

  /**
   * Called by the constructor just before the unpacking of the
   * XML data. It provides an oppertunity to initialize any properties when
   * needed.
   */
  protected void onInit() {
    super.onInit();

    //reset the initial values of the previousActId
    previousActId = null;
  }

  /**
   * This method is called for each element encountered in the XML
   * <value></value> part, when loading properties from the database. This way
   * Properties may be instantiated with the corrected value. The method will
   * return true if the children of this element should parsed as well. False is
   * returned otherwise.
   *
   * @param node Element the element encountered in the XML data stream
   * @param anUolId int the database id of the Uol for which the data are
   *   retrieved
   * @throws PropertyException if the operation fails.
   * @return boolean true indicating that the children should be parsed as
   *   well. False is returned otherwise.
   */
  protected boolean processElement(Element node, int anUolId) throws
      PropertyException {

    boolean result = super.processElement(node, anUolId);
    if (!result && "previous-act-id".equals(node.getNodeName())) {
      //this is the id of the previous act
      previousActId = node.getFirstChild().getNodeValue();
      result = true;
    }
    return result;
  }

  /**
   * Adds the XML representation of the data part of this Property to a
   * PrintWriter. StaticProperties do not have any instance data and therefore
   * nothing is added to the stream.
   *
   * @param result PrintWriter
   */
  protected void toXmlBody(PrintWriter result) {
    if (previousActId != null) {
      result.write("<previous-act-id>" + previousActId + "</previous-act-id>");
    }
    super.toXmlBody(result);
  }

  /**
   * Returns the datatype of this property definition.
   *
   * @return String the datatype represented as string value
   */
  public String getDataType() {
    return DATATYPE;
  }

  /**
   * Returns the identifier of the previous Act. If this is the first Act null
   * will be returned.
   *
   * @return String the identifier of the previous Act or null if this is the
   *   first Act
   */
  protected String getPreviousActId() {
    return previousActId;
  }
}
