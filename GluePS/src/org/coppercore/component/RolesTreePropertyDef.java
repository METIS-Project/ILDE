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

import org.coppercore.common.Parser;
import org.coppercore.dossier.PropertyDefDto;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.w3c.dom.Element;

/**
 * This component defines the role tree component. A role tree component defines the roles for which a user has been enrolled.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2008/04/28 08:00:36 $
 */
public class RolesTreePropertyDef
    extends PropertyDef {
  private static final long serialVersionUID = 42L;
  
  /**
   * Data type of this component.
   */  
  protected final static String DATATYPE = "roles-tree";
  
  /**
   * defines the unique database id of this component.
   */  
  public final static String ALL_ROLE_ID = "allroles";

  /**
   * defines the scope of this component.
   */  
  protected final static int SCOPE = PropertyDef.LOCAL;

  // NOTE: this property may not be initialized here because class intitialization will overrule any
  // value set during unpacking of the XML blob. Use the onInit() method instead for initializations that
  // are critical for the object creation
  private String defaultValue;

  /**
   * Default constructor for this component definition during run time.
   *
   * @param uolId int the database id of the Uol defining this component
   * @param propId String the identifier of this component as defined in IMS LD
   * @throws PropertyException whenever the constructor fails
   * @throws TypeCastException
   */  
  public RolesTreePropertyDef(int uolId, String propId) throws PropertyException {
    super(uolId, propId);
  }

  /**
   * Called by the constructor just before the unpacking of the
   * XML data. It provides an oppertunity to initialize any properties when
   * needed.
   */
  protected void onInit() {
    super.onInit();

    //reset the default value and the roles tree
    defaultValue = null;
  }

  /**
   * Default constructor for this component definition during publication.
   *
   * @param uolId int the database id of the Uol defining this component
   * @param propId String the identifier of this component as defined in IMS LD
   * @param rolesTree String the XML representation of the roles tree
   */  
  public RolesTreePropertyDef(int uolId, String propId,
                               String rolesTree) {

   this.uolId = uolId;
   this.propId = propId;
    //add the prefix to the propId ensuring that it is unique within the uol
    this.defaultValue = rolesTree;

    //use dto for better performance
    dto = new PropertyDefDto(SCOPE, DATATYPE, null, toXml(), uolId);
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

    if (node.getNodeName().equals("value")) {
      //this will be the default content for new content components
      defaultValue = Parser.documentToString(node);
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
  protected void toXml(PrintWriter result) {
    result.write("<value>" + defaultValue + "</value>");
  }

  /**
   * Returns the XML blob representing the default value in XML format to be
   * used when initialising properties based on this PropertyDefintion. This
   * method is called when creating the PropertyDefinition itself. So this is
   * never called when creating an instance of this PropertyDefinition. Because
   * instances of this component are static, meaning that they are the same for
   * all users this method should never be called by instances of this
   * component. A call of this method will result in an error.
   *
   * @return String nothing because a call will result in an error
   */
  protected String getDefaultBlobValue() {
    return defaultValue;
  }
}
