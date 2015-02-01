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

import org.apache.log4j.Logger;
import org.coppercore.alfanet.Tracker;
import org.coppercore.business.RoleInstance;
import org.coppercore.business.RoleParticipation;
import org.coppercore.business.Run;
import org.coppercore.business.Uol;
import org.coppercore.business.User;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.events.EventDispatcher;
import org.coppercore.exceptions.NotFoundException;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * ExplicitProperty is the root of all explicit properties. Explicit properties
 * are properties that are explicitly defined in the IMS Learning Design. This
 * class is abstract. Implementation represent the types of properties known in
 * IMS LD like string, integer, boolean, real, uri, text, datetime, duration and
 * file.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.39 $, $Date: 2007/03/30 08:23:29 $
 */
public abstract class ExplicitProperty extends Property {

  /**
   * The value in the corresponding LDDataType.
   */
  protected LDDataType value;

  /**
   * Returns an ExplicitProperty instance by retrieving the stored data from the
   * database. The passed paramters are used to find this data in the database.
   *
   * @param uol int the unit of learning database id
   * @param run int the run database id
   * @param user String the user id
   * @param propId String the property id as defined in IMS LD
   * @throws PropertyException when something goes wrong with the constructor
   * @throws TypeCastException when stored data caused a type cast error
   */
  public ExplicitProperty(Uol uol, Run run, User user, String propId) throws
      PropertyException, TypeCastException {
    super(uol, run, user, propId);
  }

  /**
   * Returns the owner id of this property.
   *
   * <p>In most cases this is the userid of the owner of the property. Only when
   * dealing with role properties, this is the roleinstace id of the role. Only
   * explicit properties can be role properties therefore we must check the
   * PropertyDefinition to see if this applies.</p>
   *
   * @return String the owner of this property, either the user id or the role
   *   id
   * @throws PropertyException when the operation fails
   */
  protected String getOwnerId() throws PropertyException {
    //fetch definition to check if this is a role property
    propertyDef = getPropertyDef();

    //check if we are dealing with a role property
    if ( (PropertyDef.ISROLE & propertyDef.getScope()) == PropertyDef.ISROLE) {
      String roleId = ( (ExplicitPropertyDef) propertyDef).getRoleId();
      int roleInstanceId;

      //now we are going the determine the appropriate owner id. This is either
      //the role instance id for the specified user and the specified role or the
      //first instance of the role (every LD role has at least one instance
      //created during run creation).
      if (roleId != null) {
        try {
          RoleParticipation rp = RoleParticipation.findRoleParticipation(run,
              user, roleId);
          roleInstanceId = rp.getRole().getId();
        }
        catch (NotFoundException ex) {
          //get the collection of all role instances of a specified IMS LD role.
          Collection roleInstances = RoleInstance.findByRoleId(roleId, run);

          //make sure we found at least one
          if (!roleInstances.isEmpty()) {
            //pick the first one of this list. This is arbitrary. A IMS LD construct making use of such
            //a role in this situation is ambigious
            roleInstanceId = ( (RoleInstance) roleInstances.iterator().next()).getId();
          }
          else {
            Logger logger = Logger.getLogger(this.getClass());
            logger.error("Expecting at least one role instance for role \"" + roleId + "\"");
            throw new PropertyException("Expecting at least one role instance for role \"" + roleId + "\"");
          }
        }
      }
      else {
        Logger logger = Logger.getLogger(this.getClass());
        logger.error(
            "Missing role id in property definition for a role property");
        throw new PropertyException(
            "Missing role id in property definition for a role property");
      }
      //return the owner id
      return Integer.toString(roleInstanceId);

    }
    //no role property, so default behaviour
    return super.getOwnerId();
  }

  /**
   * Creates the LDDataType object corresponding with the object and its String
   * value.
   *
   * @param aValue String the value of this object
   * @throws TypeCastException when the passed value can not be converted to
   *   the required LDDataType
   * @return LDDataType the LDDataType object representing the value
   */
  protected abstract LDDataType createPropertyValue(String aValue) throws
      TypeCastException;

  /**
   * This method is called for each element encountered in the XML
   * <value></value> part, when loading properties from the database. This way
   * Properties may be instantiated with the corrected value. The mehtod will
   * return true if the children of this element should parsed as well. False is
   * returned otherwise.
   *
   * @param node Element the element encountered in the XML data stream
   * @param uolid int the database id of the Uol for which the data are
   *   retrieved
   * @throws PropertyException if the operation fails.
   * @throws TypeCastException
   * @return boolean true indicating that the children should be parsed as
   *   well. False is returned otherwise.
   */
  protected boolean processElement(Element node, int uolid) throws
      PropertyException, TypeCastException {
    String nodeName = node.getNodeName();
    if ("value".equals(nodeName)) {

      /** @todo: should we unescape the propertValue here? */
      String stringValue = getNodeValue(node);
      value = createPropertyValue(stringValue);
      return true;
    }
    else if ("no-value".equals(nodeName)) {
      //there was no default value, so value is empty. See NoValue expression
      value = null;
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
    value.toXml(result);
  }

  /**
   * Changes the value of this Property. If the value is changed the Property
   * with the new value is persisted.
   *
   * @param aValue LDDataType the new value
   * @throws PropertyException if something went wrong or a restriction was
   *   violated
   * @throws TypeCastException when the value can not be type casted
   * @return boolean true if the value was changed, false otherwise
   */
  private boolean changeValue(LDDataType aValue) throws PropertyException, TypeCastException {
    boolean isChanged = false;

    //check if there needs to happen anything at all
    /** @todo evaluate all conditions after republishing  */
    if (! ( (value == null ? aValue == null : value.equals(aValue)))) {
      //type cast the passed value to the correct type
      LDDataType convertedValue = typeCast(aValue);

      //check if there were any restriction violations
      ( (ExplicitPropertyDef) getPropertyDef()).checkRestrictions(convertedValue);

      //we can now assign the value
      value = convertedValue;

      //persist the new value
      persist();

      //we have changed the value
      isChanged = true;
    }
    return isChanged;
  }

  /**
   * Sets the value of the property to the value passed as String after
   * converting it to an LDDataType.
   *
   * @param aValue String the value passed as String
   * @throws PropertyException if something went wrong or a restriction was
   *   violated
   * @throws TypeCastException when the value can not be type casted
   * @see #getValue()
   */
  public void setValue(String aValue) throws PropertyException, TypeCastException {

    //first try to convert the String value to the appropriate LDDataType
    LDDataType convertedValue = createPropertyValue(aValue);
    
    if (changeValue(convertedValue)) {
      // Start of Alfanet Specific Code
      String params[][] = { {"cc-value", aValue}
      };
      Tracker.trackEngine(uol, user, propId, getTypeName(), "change-property", params);
      // End of Alfanet Specific Code


      //notify the event dispatcher about this event.
      EventDispatcher.getEventDispatcher().postMessage(uol, run, user, this, EventDispatcher.PROPERTY_EVENT);
    }
  }

  /**
   * Changes the value of this Property. If the value is changed the Property
   * with the new value is persisted. This method is called as a result in the
   * event chain and never directly as result of a user interaction.
   *
   * @param aValue LDDataType the new value
   * @param conditionsFired Collection of conditions that already have
   *   evaluated true. In one event chain each Condition is evaluated once.
   * @throws PropertyException if something went wrong or a restriction was
   *   violated
   * @throws TypeCastException when the value can not be type casted
   * @see #getValue()
   */
  public void setValue(LDDataType aValue, Collection conditionsFired) throws PropertyException, TypeCastException {
    ( (ExplicitPropertyDef) getPropertyDef()).checkRestrictions(aValue);

    if (changeValue(aValue)) {
      // Start of Alfanet Specific Code
      if (propId.startsWith("sync_")) { // Test if this property needs to be synchronized by checking it's name
        String params[][] = { {"cc-value", aValue.toString()}
        }; // cast LDDataType to a String

        Tracker.trackEngine(uol, user, propId, getTypeName(), "change-property", params);
      }
      // End of Alfanet Specific Code

      //notify the event disptacher about this event.
      EventDispatcher.getEventDispatcher().postMessage(uol, run, user, this, EventDispatcher.PROPERTY_EVENT,
          conditionsFired);
    }
  }

  /**
   * Returns the passed value in the correct LDDataType by casting it.
   *
   * @param aValue LDDataType the value to be type casted
   * @throws TypeCastException if type cast fails
   * @return LDDataType the type casted value
   */
  public abstract LDDataType typeCast(LDDataType aValue) throws TypeCastException;

  /**
   * Returns the value of this property as LDDataType. Returns null if no value
   * was defined for this property.
   *
   * @return LDDataType value of this property if defined or null otherwise
   * @see #setValue(String)
   */
  public LDDataType getValue() {
    return value;
  }

  /**
   * Returns the title of this Property or null if no title was defined. The
   * title should be used as label in the user interface.
   *
   * @return String the title
   */
  public String getTitle(){
    return ( (ExplicitPropertyDef) getPropertyDef()).getTitle();
  }

  /**
   * Returns a Collecion of restrictions that apply for this Property. Each
   * Restriction is stored as an array of two Strings where the first String
   * contains the restriction type and the second String contains the
   * restriction value.
   *
   * @return Collection of restriction tuples
   */
  protected Collection getRestrictions() {
    return ( (ExplicitPropertyDef) getPropertyDef()).getRestrictions();
  }

  /**
   * Returns the data type name as String for this Property.
   *
   * @return String data type of this Property
   */
  public String getTypeName() {
    return getPropertyDef().getDataType();
  }

  /**
   * Returns the XML representation of the Property data as String. This method
   * only covers Elements, Attributes and Text nodes.
   *
   * @param root Node start of node tree
   * @return String the XML representation of the Property data
   */
  private String getNodeValue(Node root) {
    StringBuffer result = new StringBuffer();

    getNodeValue(root, result);

    return result.toString();
  }

  /**
   * Recursively writes the XML representation of each DOM node of the data part
   * of this Property into the passed StribgBuffer.
   *
   * @param node Node the node to be written
   * @param result StringBuffer to which to XML representation is added
   */
  private void getNodeValue(Node node, StringBuffer result) {

    //recursively process all the chiildren
    Node child = node.getFirstChild();

    while (child != null) {
      switch (child.getNodeType()) {
        case Node.ELEMENT_NODE: {

          result.append("<" + child.getNodeName());

          NamedNodeMap attributes = child.getAttributes();
          if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
              result.append(" " + attributes.item(i).getLocalName() + " \"" +
                            attributes.item(i).getNodeValue() +
                            "\"");
            }
          }

          result.append(">");

          getNodeValue(child, result);

          result.append("</" + child.getNodeName() + ">");

          break;
        }
        case Node.TEXT_NODE: {
          result.append(child.getNodeValue());
          break;
        }
      }

      child = child.getNextSibling();
    }
  }
}
