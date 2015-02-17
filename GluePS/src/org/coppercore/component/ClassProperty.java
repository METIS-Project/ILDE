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
 * Copyright (C) 2003, 2004 Harrie Martens and Hubert Vogten
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
import java.util.HashMap;
import java.util.Iterator;

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.common.Parser;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;

/**
 * This class represents the visibility of the defined classes in the IMS LD
 * instance for each user. The ClassDefintion will keep track of the affected
 * components. For each user there will be only one instance of this Property.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.14 $, $Date: 2005/01/20 16:16:57 $
 */
public class ClassProperty extends Property {
  private static final long serialVersionUID = 1L;

  // NOTE: this property may not be initialized here because class intitialization will overrule any
  // value set during unpacking of the XML blob. Use the onInit() method instead for initializations that
  // are critical for the object creation
  private HashMap classes;

  /**
   * Default constructor during run time.
   *
   * @param uol int the unit of learning database id
   * @param run int the run database id
   * @param user String the user id
   * @param propId String the property id as defined in IMS LD
   * @throws PropertyException when something goes wrong with the constructor
   */
  public ClassProperty(Uol uol, Run run, User user, String propId) throws
      PropertyException {
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
    return new ClassPropertyDef(uolId, propId);
  }

  /**
   * Called by the constructor just before the unpacking of the
   * XML data. It provides an oppertunity to initialize any properties when
   * needed.
   */
  protected void onInit() {
    super.onInit();
    classes = new HashMap();
  }

  /**
   * Returns true if the passed class identifier is part of the set of classes
   * that are tracked by this componentent.
   *
   * @param aClass String the class identifier that is queried
   * @return boolean true if the queried class is part of the set of classes
   *   tracked by this component. False otherwise.
   */
  public boolean hasClass(String aClass) {
    return classes.containsKey(aClass);
  }

  /**
   * Returns the title that was defined for a class in the IMS LD instance.
   * Returns null if the class could not be found.
   *
   * @param aClass String the requested class
   * @return String the title that was defined for a class in the IMS LD
   *   instance. Returns null if the class could not be found.
   */
  public String getTitle(String aClass) {
    String result = null;

    if (hasClass(aClass)) {
      result = ( (String[]) classes.get(aClass))[0];
    }

    return result;
  }

  /**
   * Returns 'true' or 'false' depending if the class has a control associated
   * with it in the IMS LD instance. If the class can not be found NULL is
   * returned.
   *
   * @param aClass String the requested class
   * @return String 'true' when the class has a control or false when the class
   *   does not has a control. Null is returned when the class was not found
   */
  public String getWithControl(String aClass) {
    String result = null;

    if (hasClass(aClass)) {
      result = ( (String[]) classes.get(aClass))[1];
    }

    return result;
  }

  /**
   * Returns 'true' or 'false' depending if the class is visible or not. If the
   * class can not be found NULL is returned.
   *
   * @param aClass String the requested class
   * @return String 'true' when the class is visible or false when the class is
   *   not visible. Null is returned when the class was not found
   */
  public String getIsVisible(String aClass) {
    String result = null;

    if (hasClass(aClass)) {
      result = ( (String[]) classes.get(aClass))[2];
    }

    return result;
  }

  /**
   * This method is called for each element encountered in the XML
   * <value></value> part, when loading properties from the database. This way
   * Properties may be instantiated with the corrected value. The method will
   * return true if the children of this element should parsed as well. False is
   * returned otherwise.
   *
   * @param node Element the element encountered in the XML data stream
   * @param uolId int the database id of the Uol for which the data are
   *   retrieved
   * @throws PropertyException if the operation fails.
   * @return boolean true indicating that the children should be parsed as
   *   well. False is returned otherwise.
   */
  protected boolean processElement(Element node, int uolId) throws
      PropertyException {
    String nodeName = node.getNodeName();

    if ("class".equals(nodeName)) {
      String classId = node.getAttribute("identifier");
      String[] data = {null, null, null};

      data[0] = Parser.getNamedAttribute(node, "title");
      data[1] = Parser.getNamedAttribute(node, "with-control");
      data[2] = Parser.getNamedAttribute(node, "isvisible");

      classes.put(classId, data);

      return true;
    }
    return false;
  }

  /**
   * Return a collection of components that have associated the passed class
   * with it. These components will be effected by the class operations.
   *
   * @param classId String representing the class name for which the components
   *   are retrieved
   * @throws PropertyException when the creation of a component fails
   * @return Collection of components effeced by the class
   */
  public Collection getComponents(String classId) throws PropertyException {
    return ( (ClassPropertyDef) getPropertyDef()).getComponents(uol, run, user, classId);
  }


  /**
   * Adds the XML representation of the data part of this Property to a
   * PrintWriter. StaticProperties do not have any instance data and therefore
   * nothing is added to the stream.
   *
   * @param result PrintWriter
   */
  protected void toXml(PrintWriter result) {
    result.write("<value>");

    Iterator iter = classes.keySet().iterator();
    while (iter.hasNext()) {
      String classId = (String) iter.next();
      String data[] = (String[]) classes.get(classId);

      result.write("<class identifier=\"" + classId + "\"");
      if (data[0] != null) {
        result.write("  title=\"" + data[0] + "\"");
      }
      if (data[1] != null) {
        result.write(" with-control=\"" + data[1] + "\"");
      }
      if (data[2] != null) {
        result.write(" isvisible=\"" + data[2] + "\"");
      }
      result.write("/>");
    }

    result.write("</value>");
  }

  /**
   * Sets the visibility for the passed class.
   *
   * @param classId String the class to be set
   * @param title String the title to be associated with the class. Note that
   *   IMS LD can have multiple titles associated with a single class. Which
   *   action modifies the class visibility will determine which title will be
   *   used.
   * @param withControl boolean the with-control to be associated with the
   *   class. Note that IMS LD can have multiple with-controls associated with
   *   a single class. Which action modifies the class visibility will
   *   determine which with-control will be used.
   * @param isVisible boolean indicates if the class is visible or not
   */
  public void setVisibility(String classId, String title, boolean withControl, boolean isVisible) {
    String[] data = {null, null, null};
    data[0] = title;
    data[1] = Boolean.valueOf(withControl).toString();
    data[2] = Boolean.valueOf(isVisible).toString();
    classes.put(classId, data);
    persist();
  }
}
