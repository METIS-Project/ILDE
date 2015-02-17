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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.dossier.PropertyDefDto;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;


/**
 * This component represents the collection of defined classes in the IMS LD
 * instance and their associated IMS LD elements. An element is associated with
 * a class if the class attribute for that element is set in the IMS LD. This
 * component is used to track wich components are effected by the change of the
 * class visibility.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/01/20 16:12:03 $
 */
public class ClassPropertyDef extends PropertyDef {
  private static final long serialVersionUID = 1L;

  /**
   * Data type of this component.
   */
  public final static String DATATYPE = "class";

  /**
   * Database id of this type of component. Only one instance per Uol will be
   * created during publication.
   */
  public final static String ID = "_classes";

  /**
   * The default value for this object, which is basically an empty container.
   */
  protected String defaultValue = "<value/>";
  private ArrayList references = null;
  private String className = null;
  private final int SCOPE = PropertyDef.LOCALPERSONAL;

  // NOTE: this property may not be initialized here because class intitialization will overrule any
  // value set during unpacking of the XML blob. Use the onInit() method instead for initializations that
  // are critical for the object creation
  private HashMap classReferences;

  /**
   * Default constructor for this component definition during run time.
   *
   * @param uolId int the database id of the Uol defining this component
   * @param propId String the identifier of this component as defined in IMS LD
   * @throws PropertyException whenever the constructor fails
   */
  public ClassPropertyDef(int uolId, String propId) throws PropertyException {
    super(uolId, propId);
  }

  /**
   * Called by the constructor just before the unpacking of the
   * XML data. It provides an oppertunity to initialize any properties when
   * needed.
   */
  protected void onInit() {
    super.onInit();

    //reset the hash set containing the visibility of the classes
    classReferences = new HashMap();
  }

  /**
   * Default constructor for this component definition during publication.
   *
   * @param uolId int the database id of the Uol defining this component
   * @param propId String the identifier of this component as defined in IMS LD
   * @param classes HashMap a HashMap containing the class as key and a
   *   collection of String[] tuples as values. The String[] tuple represent a
   *   class member where the first string contains the identifier of the
   *   reference component and the second String contains the data type of the
   *   componennt.
   */
  public ClassPropertyDef(int uolId, String propId, HashMap classes) {
    this.uolId = uolId;
    this.propId = propId;
    this.classReferences = classes;

    //we can't assign the default value here because child constructors still have
    //to set their value when this constructor is called. So every leaf in the hierarchy
    //must set their own default value.
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
   * @param uolId int the database id of the Uol for which the data are
   *   retrieved
   * @throws PropertyException if the operation fails.
   * @return boolean true indicating that the children should be parsed as
   *   well. False is returned otherwise.
   */
  protected boolean processElement(Element node, int uolId) throws
      PropertyException {
    boolean result = false;

    if ("class-definition".equals(node.getNodeName())) {
      //create a new container for the references
      references = new ArrayList();
      className = node.getAttribute("class-name");

      classReferences.put(className, references);
      result = false;
    }
    else if ("reference".equals(node.getNodeName())) {
      //create a value tuple containing the component id and the type of component
      String[] value = {
                       node.getAttribute("ref"), node.getAttribute("type")};

      //store the value on the hashmap
      references.add(value);
      classReferences.put(className, references);

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
  protected void toXml(PrintWriter result) {
    result.write("<" + getDataType() + ">");
    Iterator iter = classReferences.keySet().iterator();
    while (iter.hasNext()) {
      String classId = (String) iter.next();
      result.write("<class-definition class-name=\"" + classId + "\">");

      //add each service/learning object reference for this class
      Iterator iter2 = ( (ArrayList) classReferences.get(classId)).iterator();
      while (iter2.hasNext()) {
        String[] reference = (String[]) iter2.next();
        result.write("<reference ref=\"" + reference[0] + "\" type=\"" + reference[1] + "\"/>");
      }

      result.write("</class-definition>");

    }
    result.write("</" + getDataType() + ">");
  }

  /**
   * Returns a Collection of components (Properties) that are associated with
   * the passed classId.
   *
   * @param uol int the database id of the Uol defining this component
   * @param run Run the Run for which the components have to be retrieved
   * @param user User the User for which the components have to be retrieved
   * @param classId String the identifier of the class for which the components
   *   have to be retrieved
   * @throws PropertyException when the operation fails
   * @return Collection of Properties representing the components that belong
   *   to this class
   */
  protected Collection getComponents(Uol uol, Run run, User user, String classId) throws PropertyException {
    ArrayList result = new ArrayList();

    if (classReferences.containsKey(classId)) {
      Iterator iter = ( (ArrayList) classReferences.get(classId)).iterator();
      while (iter.hasNext()) {
        String[] reference = (String[]) iter.next();

        if (SendMailPropertyDef.DATATYPE.equals(reference[1])) {
          result.add(ComponentFactory.getPropertyFactory().getSendMail(uol, run, user, reference[0]));
        }
        if (MonitorPropertyDef.DATATYPE.equals(reference[1])) {
          result.add(ComponentFactory.getPropertyFactory().getMonitorObject(uol, run, user, reference[0]));
        }

        // @todo deal with index-search-object and conference object here in the future

        else if (LearningObjectPropertyDef.DATATYPE.equals(reference[1])) {
          result.add(ComponentFactory.getPropertyFactory().getLearningObject(uol, run, user, reference[0]));
        }
      }
    }
    return result;
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
  public String getDataType() {
    return DATATYPE;
  }

  /**
   * Returns a DOM copy of the activity tree. The result may be manipulated
   * without affecting the component itself.
   *
   * @return Element the DOM copy of the activity tree
   */
  protected String getDefaultBlobValue() {
    return defaultValue;
  }
}
