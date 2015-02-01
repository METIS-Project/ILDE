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

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.coppercore.dossier.PropertyDefDto;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.w3c.dom.Element;

/**
 * This component represents the definition of an environment tree. Because all
 * information defined for an environment tree is equal for all users the scope
 * is global.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.12 $, $Date: 2005/01/21 10:58:49 $
 */
public class EnvironmentTreePropertyDef extends PropertyDef {
  private static final long serialVersionUID = 42L;

  private String environmentTreeString = null;

  /**
   * Data type of this component.
   */
  protected final static String DATATYPE = "environment-tree";

  /**
   * defines the prefix used for defining the unique database id of this
   * component.
   */
  protected final static String IDPREFIX = "_tree_";

  /**
   * defines the scope of this component.
   */
  protected final static int SCOPE = PropertyDef.GLOBAL;

  // NOTE: this property may not be initialized here because class
  // intitialization will overrule any
  // value set during unpacking of the XML blob. Use the onInit() method instead
  // for initializations that
  // are critical for the object creation
  private Element environmentTree;

  /**
   * Default constructor for this component definition during run time.
   * 
   * @param uolId
   *          int the database id of the Uol defining this component
   * @param propId
   *          String the identifier of this component as defined in IMS LD
   * @throws PropertyException
   *           whenever the constructor fails
   * @throws TypeCastException
   */
  public EnvironmentTreePropertyDef(int uolId, String propId) throws PropertyException {
    super(uolId, propId);
  }

  /**
   * Called by the constructor just before the unpacking of the XML data. It
   * provides an oppertunity to initialize any properties when needed.
   */
  protected void onInit() {
    super.onInit();

    // reset the dom node containing the environment tree
    environmentTree = null;
  }

  /**
   * Default constructor for this component definition during publication.
   * 
   * @param uolId
   *          int the database id of the Uol defining this component
   * @param propId
   *          String the identifier of this component as defined in IMS LD
   * @param tree
   *          String the XML representation of the environment tree
   */
  public EnvironmentTreePropertyDef(int uolId, String propId, String tree) {

    this.uolId = uolId;
    // add the prefix to the propId ensuring that it is unique within the uol
    this.propId = IDPREFIX + propId;
    this.environmentTreeString = tree;

    // use dto for better performance
    dto = new PropertyDefDto(SCOPE, DATATYPE, null, toXml(), uolId);
  }

  /**
   * This method is called for each element encountered in the XML <value>
   * </value> part, when loading properties from the database. This way
   * Properties may be instantiated with the corrected value. The method will
   * return true if the children of this element should parsed as well. False is
   * returned otherwise.
   * 
   * @param node
   *          Element the element encountered in the XML data stream
   * @param anUolId
   *          int the database id of the Uol for which the data are retrieved
   * @throws PropertyException
   *           if the operation fails.
   * @return boolean true indicating that the children should be parsed as well.
   *         False is returned otherwise.
   */
  protected boolean processElement(Element node, int anUolId) throws PropertyException {

    if (node.getNodeName().equals("value")) {
      // environmentTree = (Element) node.getFirstChild();
      environmentTree = node;
      return true;
    }
    return false;
  }

  /**
   * Adds the XML representation of the data part of this Property to a
   * PrintWriter. StaticProperties do not have any instance data and therefore
   * nothing is added to the stream.
   * 
   * @param result
   *          PrintWriter
   */
  protected void toXml(PrintWriter result) {
    result.write("<value>" + environmentTreeString + "</value>");
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
    // should never be called as instances of this property should be static
    Logger logger = Logger.getLogger(getClass());
    logger.error("Invalid call, static properties are not instantiated");
    throw new EJBException("Invalid call, static properties are not instantiated");
  }

  /**
   * Returns a DOM copy of the activity tree. The result may be manipulated
   * without affecting the component itself.
   * 
   * @return Element the DOM copy of the activity tree
   */
  protected Element getTree() {
    // clone the node to protect the defintion from being modified during
    // personalization
    return (Element) environmentTree.cloneNode(true);
  }
}
