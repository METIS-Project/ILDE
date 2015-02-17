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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.coppercore.common.Parser;
import org.coppercore.dossier.PropertyDefDto;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;

/**
 * The class represent the IMS LD defintion of an SupportActivity. An SupportActivityPropertyDef is an
 * instance of a CompletionComponentDef.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.13 $, $Date: 2005/01/20 16:12:03 $
 */
public class SupportActivityPropertyDef extends CompletionComponentDef {
  private static final long serialVersionUID = 42L;

  private final int SCOPE = PropertyDef.LOCALPERSONAL;

  /**
   * Data type of this component.
   */
  public static final String DATATYPE = "support-activity";

  // NOTE: this property may not be initialized here because class intitialization will overrule any
  // value set during unpacking of the XML blob. Use the onInit() method instead for initializations that
  // are critical for the object creation
  private ArrayList roleIdsToSupport;

  /**
   * Default constructor for this component definition during run time.
   *
   * @param uolId int the database id of the Uol defining this component
   * @param propId String the identifier of this component as defined in IMS LD
   * @throws PropertyException whenever the constructor fails
   */
  public SupportActivityPropertyDef(int uolId, String propId) throws PropertyException {
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
   * @param roleIdsToSupport ArrayList of role ids of roles that are supported by this component         
   */
  public SupportActivityPropertyDef(int uolId, String propId, String completed, String isVisible, String contentXml,
      Collection items, ArrayList roleIdsToSupport) {
    super(uolId, propId, completed, isVisible, contentXml, items);
    this.roleIdsToSupport = roleIdsToSupport;

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

    roleIdsToSupport = new ArrayList();
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
  protected boolean processElement(Element node, int anUolId) throws PropertyException {
    String nodeName = node.getNodeName();

    boolean result = super.processElement(node, anUolId);

    if (!result && "role-to-support".equals(nodeName)) {
      roleIdsToSupport.add(Parser.getTextValue(node));
      return true;
    }
    return false;
  }

  /**
   * Adds the XML representation of the data part of this Property to a
   * PrintWriter. StaticProperties do not have any instance data and therefore
   * nothing is added to the stream.
   *
   * @param result PrintWriter
   */
  protected void toXmlBody(PrintWriter result) {
    super.toXmlBody(result);

    //add the roles that should be supported
    Iterator iter = roleIdsToSupport.iterator();
    while (iter.hasNext()) {
      String roleId = (String) iter.next();
      result.write("<role-to-support>" + roleId + "</role-to-support>");
    }
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
