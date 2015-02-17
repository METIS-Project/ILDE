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
import java.io.Serializable;
import java.util.Collection;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;
import org.coppercore.business.Event;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.exceptions.PropertyException;
import org.w3c.dom.Element;

/**
 * Abstract class which is the root for all persistent business components. A
 * persistent business components is threated very simular to an IMS LD
 * property. It has a PropertyDefinition associated with it and it has a scope.
 * Depending on the scope of this component (global, global-personal, local,
 * local-personal or global), the key for accessing this object is formed by the
 * Uol, Run and User. A component can be considered to be a property with
 * multiple values and an associated behaviour. Children of this class contain
 * the data that are specific for the property. The PropertyDef contains all
 * data that is common to all instances. By design, StaticProperties do not
 * contain any instance data.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.22 $, $Date: 2009/01/21 07:53:35 $
 */
public abstract class StaticProperty extends PropertyData implements Serializable {

  private static final long serialVersionUID = 42L;

/**
   * The identfier for this Component/Property as defined in the IMS LD.
   */
  protected String propId = null;

  /**
   * The database id of the Uol defining this Property.
   */
  protected int uolId;

  /**
   * The Uol defining this Property.
   */
  protected Uol uol = null;

  /**
   * The PropertyDefinition for this Property containing all data that is common
   * for all Property instances.
   */
  protected PropertyDef propertyDef = null;

  /**
   * Constructor that should only be called be the implementing sub classes.
   */
  protected StaticProperty() {
    //default constructor    
  }

  /**
   * Constructor creating a StaticProperty based on the passed parameters.
   *
   * @param uol int the unit of learning db id
   * @param propId String the id of this component/property
   */
  public StaticProperty(Uol uol, String propId) {
    this.uol = uol;
    this.uolId = uol.getId();
    this.propId = propId;
  }

  /**
   * Returns the PropertyDef defining this property instance. The PropertyDef
   * contains the data that are common to all instances.
   *
   * @return PropertyDef defining this property instance
   */
  public PropertyDef getPropertyDef() {
    try {
      if (propertyDef == null) {
        propertyDef = findPropertyDef();
      }
      return propertyDef;
    }
    catch (PropertyException ex) {
      //we couldn't even find a definition for the requested property.
      Logger logger = Logger.getLogger(this.getClass());
      logger.error(
          "Failed to retrieve property definition." +
          ex);

      //should never occur
      throw new EJBException(ex);

    }
  }

  /**
   * Returns the Uol which defined this Property instance.
   *
   * @return Uol defining this Property instance.
   */
  protected Uol getUol() {
    return uol;
  }

  /**
   * Return the XML blob representing the default value for this property
   * instance.
   *
   * @return String containing the XML representation of the default value for
   *   this property instance.
   */
  public String getXmlBlobValue() {
    return getPropertyDef().getXmlBlobValue();
  }

  /**
   * Returns PropertyDef after fetching it from the database.
   *
   * @return PropertyDef retrieved from database
   * @throws PropertyException if this operation fails.
   */
  protected abstract PropertyDef findPropertyDef() throws
      PropertyException;

  /**
   * Adds the XML representation of the data part of this Property to a
   * PrintWriter. StaticProperties do not have any instance data and therefore
   * nothing is added to the stream.
   *
   * @param result PrintWriter
   */
  protected void toXml(PrintWriter result) {
    //do nothing as static properies only have a definition
  }

  /**
   * This method is called for each element encountered in the XML
   * <value></value> part, when loading properties from the database. This way
   * Properties may be instantiated with the corrected value. The method will
   * return true if the children of this element should parsed as well. False is
   * returned otherwise.
   *
   * @param node Element the element encountered in the XML data stream
   * @param uolid int the database id of the Uol for which the data are
   *   retrieved
   * @throws PropertyException if the operation fails.
   * @return boolean true indicating that the children should be parsed as
   *   well. False is returned otherwise.
   */
  protected boolean processElement(Element node, int uolid) throws
      PropertyException {
    //do nothing as static properies only have a definition

    return true;

  }

  /**
   * Returns the identifier of this property. This identifier corresponds with
   * the identifier of the component/property as defined in IMS LD.
   *
   * @return String the identifier for this Property.
   */
  public String getIdentifier() {
    return propId;
  }

  /**
   * Processes event for which this property is the receiver. By default nothing
   * is done with the incomming event.
   *
   * @param uol the Uol raising the event
   * @param run the Run raising the event
   * @param user User the User which raised the event
   * @param event Event that has be raised
   * @param sender StaticProperty the component raising the event
   * @param firedActions Collection a collection of events already raised as
   *   reaction to an initial triggering event. The collection prevents ending
   *   up in an endless of events. Each event is only processed once.
   * @throws PropertyException when an event could not be processed
   */
  public void processEvent(Uol uol, Run run, User user, Event event,
                           StaticProperty sender, Collection firedActions) throws
      PropertyException {
    //default do nothing
  }

  /**
   * Returns the primary database key of the PropertyDefinition assiociated with
   * this property.
   *
   * @return int the database id of the associated PropertyDefinition
   * @throws PropertyException if the operation fails
   */
  public int getPropDefPK() throws PropertyException {
    return getPropertyDef().getPropDefPK();
  }

  /**
   * Returns the scope of this property represented by an Integer value. The
   * following values are bitwise exclusive value representing the different
   * scope aspects: GLOBAL = 0, LOCAL = 1, PERSONAL = 2, ROLE = 4. So for
   * example a local personal property would be represented by value 3.
   *
   * @return int the integer value representing the scope of this Property.
   * @throws PropertyException whenever the scope could not be returned
   */
  public int getScope() throws PropertyException {
    return getPropertyDef().getScope();
  }

  /**
   * Returns a collection of users that are in the scope with this Property. In
   * effect this method will return all known users if the scope is global or
   * all users in the run if the scope is local or all users in role if the
   * scope is role or an empty collection if the scope is personal.
   *
   * @throws PropertyException whenever the method fails to return the
   *   requested users.
   * @return Collection of users that are in the same scope
   */
  public abstract Collection /* User */ getUsersInScope() throws PropertyException;

}
