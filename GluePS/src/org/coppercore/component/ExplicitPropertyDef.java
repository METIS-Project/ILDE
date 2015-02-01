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
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.coppercore.common.MessageList;
import org.coppercore.common.Parser;
import org.coppercore.datatypes.LDDataType;
import org.coppercore.dossier.PropertyDefDto;
import org.coppercore.dossier.PropertyDefFacade;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.RestrictionViolationException;
import org.coppercore.exceptions.TypeCastException;
import org.w3c.dom.Element;

/**
 * Constructor for ExplicitPropertyDef. A property belongs to this class when
 * declared explicitely in IMS-LD (level B and C only). Validation rules for
 * restrictions and data types are added. The class is abstract and is
 * implemented by each of the property type known to IMS-LD.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.37 $, $Date: 2005/01/19 16:31:27 $
 */
public abstract class ExplicitPropertyDef extends PropertyDef {
  private static final String CLASSNAME = "org.coppercore.component.ExplicitProperty";

  private static HashMap constraints = null;

  private static final String ENUMERATION = "enumeration";

  private static final String MINLENGTH = "minLength";

  private static final String MAXLENGTH = "maxLength";

  private static final String LENGTH = "length";

  private static final String TOTALDIGITS = "totalDigits";

  private static final String FRACTIONDIGITS = "fractionDigits";

  private static final String PATTERN = "pattern";

  private static final String MININCLUSIVE = "minInclusive";

  private static final String MAXINCLUSIVE = "maxInclusive";

  private static final String MINEXCLUSIVE = "minExclusive";

  private static final String MAXEXCLUSIVE = "maxExclusive";

  private static final int ENUMERATION_ID = 1;

  private static final int MINLENGTH_ID = 2;

  private static final int MAXLENGTH_ID = 3;

  private static final int LENGTH_ID = 4;

  private static final int TOTALDIGITS_ID = 5;

  private static final int FRACTIONDIGITS_ID = 6;

  private static final int PATTERN_ID = 7;

  private static final int MININCLUSIVE_ID = 8;

  private static final int MAXINCLUSIVE_ID = 9;

  private static final int MINEXCLUSIVE_ID = 10;

  private static final int MAXEXCLUSIVE_ID = 11;

  private static HashMap dataTypes = null;

  // NOTE: this property may not be initialized here because class
  // intitialization will overrule any
  // value set during unpacking of the XML blob. Use the onInit() method instead
  // for initializations that
  // are critical for the object creation

  /**
   * The default value for an instance of this PropertyDefinition.
   */
  protected LDDataType defaultValue;

  private String roleId;

  /**
   * contains a collections of restrictions. A restrictions ia a tuple of two
   * Strings, the first contains the restriction type and the second the
   * restriction value.
   */
  protected ArrayList /* String[] */restrictions;

  /**
   * contains a collection of enumeration values basically restricting the value
   * an instance may assume.
   */
  protected Collection enumerationRestrictions;

  /**
   * This constructor creates a PropertyDef based on the parameters passed. If
   * no corresponding PropertyDef was found in the database a FinderException
   * was thrown.
   * 
   * @param uolId
   *          int the unit of learning in which this property was referenced
   * @param propId
   *          String the property id used in the IMS-LD instance for this
   *          property
   * @throws PropertyException
   *           whenever this constructor fails.
   */
  public ExplicitPropertyDef(int uolId, String propId) throws PropertyException {
    super(uolId, propId);
  }

  /**
   * This constructor should be called when creating a new
   * ExplicityPropertyDefinition or when retrieving an updating an excisitng
   * one.
   * 
   * @param uolId
   *          int the database id of the Uol
   * @param propId
   *          String the property id of this defintion
   * @param scope
   *          int the scope of this defintion
   * @param dataType
   *          String the data type of this definition. Must be either integer,
   *          real, string, boolean, text, uri, datetime, duration or file.
   * @param href
   *          String the URI of this definition. This parameter is null when the
   *          scope is not global.
   * @param initialValue
   *          String the initial value for this property as defined in IMS LD.
   * @param restrictions
   *          ArrayList an collection of String[] tuples representing the
   *          restrictions that should be applied to an instance of this
   *          ProipertyDefintion. The first value represents the type of
   *          restriction whilst the second String represents the restriction
   *          value.
   * @param metadata
   *          String the metadata defined for this PropertyDefintion. May be
   *          null if no meta data are present.
   * @param title
   *          String the title of this PropertyDefinition. May be null if no
   *          title was defined
   * @param roleId
   *          String the role id of the role defining this property. This
   *          parameter is null for all PropertyDefintions that have a none role
   *          scope.
   * @throws RestrictionViolationException
   *           when the default values do not meet the opposed restrictions
   * @throws TypeCastException
   *           when the default value can not be cast to the past data type
   * @throws PropertyException
   *           whenever the creation of this property fails.
   */
  public ExplicitPropertyDef(int uolId, String propId, int scope, String dataType, String href, String initialValue,
      ArrayList restrictions, String metadata, String title, String roleId) throws RestrictionViolationException,
      TypeCastException, PropertyException {

    this.uolId = uolId;
    this.propId = propId;

    this.restrictions = restrictions;
    this.title = title;
    this.metadata = metadata;
    this.roleId = roleId;

    // create the default value, if an initial value was passed
    if (initialValue != null) {
      defaultValue = createPropertyValue(initialValue);
    }

    // use dto for better performance
    dto = new PropertyDefDto(scope, dataType, href, toXml(), uolId);

    // check if we are not redefining an existing global definition
    if ((PropertyDef.ISLOCAL & scope) == 0) {
      // ok we are dealing with a global definition. There is only one
      // definition allowed
      // try to find find the property definition

      try {
        PropertyDefFacade pdf = new PropertyDefFacade(href);

        // ok we found that the global definition was defined already.
        // this is fine if it was done in the scope of this uol

        // changed 2004-11-12: changed check for redefinition
        if (uolId != pdf.getDto().getDefinedBy()) {
          throw new PropertyException(
              "Attempting to redefine an global property definition outside the scope of the original unit of learning for property["
                  + propId + "]");
        }
      } catch (FinderException ex) {
        // this is ok so we don't have to throw an exception because the
        // definition did not exists
      }
    }
  }

  /**
   * This constructor should be called when the ProperyDefintion is restrieved
   * from the database.
   * 
   * @param uolId
   *          int the database id of the Uol
   * @param propId
   *          String the property id of this defintion
   * @param dto
   *          PropertyDefDto the data transfer object used for retrieving and
   *          persisting the data of this object
   * @throws PropertyException
   *           when the creation of this PropertyDefintion fails.
   */
  public ExplicitPropertyDef(int uolId, String propId, PropertyDefDto dto) throws PropertyException {
    super(uolId, propId, dto);
  }

  /**
   * Initializes the object. Method is called by parent constructor, just before
   * parsing the data.
   */
  protected void onInit() {
    super.onInit();
    defaultValue = null;
    restrictions = null;
    enumerationRestrictions = null;
    roleId = null;
  }

  /**
   * Returns the data type of this PropertyDefintion as an integer.
   * 
   * @param typeName
   *          String the string representation of the data type
   * @return int unique integer value for the passed data type
   */
  protected static int getPropertyType(String typeName) {
    if (dataTypes == null) {
      // create the datatype lookup table
      dataTypes = new HashMap(10);
      dataTypes.put(IntegerPropertyDef.DATATYPE, new Integer(LDDataType.LDINTEGER));
      dataTypes.put(RealPropertyDef.DATATYPE, new Integer(LDDataType.LDREAL));
      dataTypes.put(StringPropertyDef.DATATYPE, new Integer(LDDataType.LDSTRING));
      dataTypes.put(UriPropertyDef.DATATYPE, new Integer(LDDataType.LDURI));
      dataTypes.put(DateTimePropertyDef.DATATYPE, new Integer(LDDataType.LDDATETIME));
      dataTypes.put(DurationPropertyDef.DATATYPE, new Integer(LDDataType.LDDURATION));
      dataTypes.put(TextPropertyDef.DATATYPE, new Integer(LDDataType.LDTEXT));
      dataTypes.put(BooleanPropertyDef.DATATYPE, new Integer(LDDataType.LDBOOLEAN));
      dataTypes.put(FilePropertyDef.DATATYPE, new Integer(LDDataType.LDFILE));
    }

    Integer result = (Integer) dataTypes.get(typeName);

    if (result != null) {
      return result.intValue();
    }
    // this should never occur !
    Logger logger = Logger.getLogger(CLASSNAME);
    logger.error("Unknown datatype \"" + typeName + "\" requested");
    throw new EJBException("Unknown datatype requested");
  }

  /**
   * Returns the LDDataType object mathching this object on the basis of the
   * passed String value. An TypeCastException is thrown whenever the passed
   * value is not valid.
   * 
   * @param value
   *          String
   * @throws TypeCastException
   * @return LDDataType
   */
  protected abstract LDDataType createPropertyValue(String value) throws TypeCastException;

  /**
   * Returns the collections of restrictions defined for this PropertyDefintion.
   * A restriction is defined by a String[] tuple where the first parameter
   * defines the restriction type and the second String defined the restriction
   * value.
   * 
   * @return Collection of restrictions.
   * @see #setRestrictions(ArrayList)
   */
  protected Collection /* String[] */getRestrictions() {
    return restrictions;
  }

  /**
   * Sets the collections of restrictions for this PropertyDefintion. A
   * restriction is defined by a String[] tuple where the first parameter
   * defines the restriction type and the second String defined the restriction
   * value.
   * 
   * @param restrictions
   *          ArrayList the collection of restrictions
   * @see #getRestrictions()
   */
  protected void setRestrictions(ArrayList restrictions) {
    this.restrictions = restrictions;
  }

  /**
   * Converts the restriction String type into an unique integer value for each
   * restriction type.
   * 
   * @param restrictionName
   *          String the restriction type
   * @return int representing this restriction type
   */
  private int getRestriction(String restrictionName) {
    if (constraints == null) {
      // initiate hashmap with restriction types
      constraints = new HashMap(15);
      constraints.put(ENUMERATION, new Integer(ENUMERATION_ID));
      constraints.put(TOTALDIGITS, new Integer(TOTALDIGITS_ID));
      constraints.put(FRACTIONDIGITS, new Integer(FRACTIONDIGITS_ID));
      constraints.put(PATTERN, new Integer(PATTERN_ID));
      constraints.put(LENGTH, new Integer(LENGTH_ID));
      constraints.put(MAXLENGTH, new Integer(MAXLENGTH_ID));
      constraints.put(MINLENGTH, new Integer(MINLENGTH_ID));
      constraints.put(MININCLUSIVE, new Integer(MININCLUSIVE_ID));
      constraints.put(MAXINCLUSIVE, new Integer(MAXINCLUSIVE_ID));
      constraints.put(MINEXCLUSIVE, new Integer(MINEXCLUSIVE_ID));
      constraints.put(MAXEXCLUSIVE, new Integer(MAXEXCLUSIVE_ID));
    }

    Integer result = (Integer) constraints.get(restrictionName);
    return (result != null ? result.intValue() : -1);
  }

  /**
   * Adds the XML representation of this PropertyDefintion to the passed
   * PrintWriter stream.
   * 
   * @param result
   *          PrintWriter the stream to which the XML representation will be
   *          added.
   */
  protected void toXml(PrintWriter result) {
    result.write("<property>");

    if (title != null) {
      result.write("<title>" + title + "</title>");
    }
    if (metadata != null) {
      result.write("<metadata>" + metadata + "</metadata>");
    }

    if (defaultValue != null) {
      defaultValue.toXml(result);
    }

    if (roleId != null) {
      result.write("<role>" + roleId + "</role>");
    }

    if (restrictions != null) {
      Iterator iter = restrictions.iterator();
      while (iter.hasNext()) {
        String[] tuple = (String[]) iter.next();
        result.write("<restriction restriction-type=\"" + tuple[0] + "\">" + tuple[1] + "</restriction>");
      }
    }

    result.write("</property>");
  }

  /**
   * Processes default elements found in explicit property definition. Because
   * JAVA does not support multiple inheritance this method can be found here
   * and will also be called by the explicit property data objects. This should
   * not cause any problems.
   * 
   * @param node
   *          Node the dom node to be processed
   * @param uolId
   *          int the database id of the Uol defining this object
   * @throws PropertyException
   *           when the method fails
   * @throws TypeCastException
   *           when a value can not be cast to the type of this
   *           PropertyDefintion
   * @return boolean returns true when the child elements require further
   *         processing. False otherwise.
   */
  protected boolean processElement(Element node, int uolId) throws PropertyException, TypeCastException {
    String nodeName = node.getNodeName();

    if ("restriction".equals(nodeName)) {
      if (restrictions == null) {
        restrictions = new ArrayList();
      }

      String[] tuple = { node.getAttribute("restriction-type"), Parser.getTextValue(node) };
      restrictions.add(tuple);
      return true;
    } else if ("title".equalsIgnoreCase(nodeName)) {
      title = Parser.getTextValue(node);
      return true;
    } else if ("metadata".equalsIgnoreCase(nodeName)) {
      metadata = Parser.getTextValue(node);
      return true;
    } else if ("value".equalsIgnoreCase(nodeName)) {
      defaultValue = this.createPropertyValue(Parser.getTextValue(node));
      return true;
    } else if ("role".equals(nodeName)) {
      roleId = Parser.getTextValue(node);
      return true;
    }

    return false;
  }

  /**
   * Returns true if constraint which is passed as parameter is met. Throws
   * RestrictionViolationException when restriction does not apply. Only
   * enumeration restrictions will ever return false.
   * 
   * @param constraintType
   *          tuple tuple of restriction type and restriction value
   * @param constraintValue
   *          String
   * @param value
   *          LDDataType value to be checked against the restriction
   * @throws RestrictionViolationException
   *           when the restriction isn't met
   * @return boolean true when restriction is met (false can only be returned in
   *         case of a enumeration restriction)
   */
  public boolean checkRestriction(String constraintType, String constraintValue, LDDataType value)
      throws RestrictionViolationException {
    try {

      switch (getRestriction(constraintType)) {
      case ENUMERATION_ID: {
        return value.enumeration(constraintValue);
      }
      case MINLENGTH_ID: {
        return value.minLength(Integer.parseInt(constraintValue));
      }
      case MAXLENGTH_ID: {
        return value.maxLength(Integer.parseInt(constraintValue));
      }
      case LENGTH_ID: {
        return value.length(Integer.parseInt(constraintValue));
      }
      case PATTERN_ID: {
        return value.pattern(constraintValue);
      }
      case MININCLUSIVE_ID: {
        return value.minInclusive(constraintValue);
      }
      case MAXINCLUSIVE_ID: {
        return value.maxInclusive(constraintValue);
      }
      case MINEXCLUSIVE_ID: {
        return value.minExclusive(constraintValue);
      }
      case MAXEXCLUSIVE_ID: {
        return value.maxExclusive(constraintValue);
      }
      case TOTALDIGITS_ID: {
        return value.totalDigits(Integer.parseInt(constraintValue));
      }
      case FRACTIONDIGITS_ID: {
        return value.fractionDigits(Integer.parseInt(constraintValue));
      }
      default: {
        throw new RestrictionViolationException("Unknow restriction type encountered");
      }
      }
    } catch (NumberFormatException ex) {
      throw new RestrictionViolationException(ex);
    }
  }

  /**
   * Check if the passed value meets all restriction.
   * 
   * @param value
   *          String value that needs to be checked
   * @throws RestrictionViolationException
   *           when a restriction is not met
   */
  public void checkRestrictions(LDDataType value) throws RestrictionViolationException {
    Boolean result = null;

    if (restrictions != null) {

      Iterator iter = restrictions.iterator();
      while (iter.hasNext()) {
        String[] tuple = (String[]) iter.next();

        if (tuple[0].equalsIgnoreCase("enumeration")) {
          if (result == null) {
            result = new Boolean(false);
          }
          // if one enumeration restriction returns true, the enumeration is
          // valid
          result = Boolean.valueOf(result.booleanValue() | checkRestriction(tuple[0], tuple[1], value));
        } else {
          // always true otherwise RestrictionViolationException would have been
          // thrown
          checkRestriction(tuple[0], tuple[1], value);
        }
      }
    }

    // now we must check if there were any enumeration restrictions. If so see
    // if one of them matched the value
    if ((result != null) && (!result.booleanValue())) {
      throw new RestrictionViolationException(value + " not part of enumeration");
    }
  }

  /**
   * Returns true when the PropertyDefData object associated with this object
   * meets all the restrictions that are defined for this object. Otherwise
   * false is returned and the cause(s) is stored in the passed MessageList
   * 
   * @param messages
   *          MessageList contains the descriptions of the restriction
   *          violations if they have occured
   * @return boolean true when no restriction violation were encountered, false
   *         otherwise
   */
  public boolean isValid(MessageList messages) {
    boolean result = true;
    // only when there is a default value, we check the restrictions
    if (defaultValue != null) {
      result = isValid(messages, defaultValue);
    }
    return result;
  }

  /**
   * Returns true if value meets all the restrictions. If restriction violation
   * are detected the description is added to the message list.
   * 
   * @param messages
   *          MessageList containing all the violation messages if they should
   *          occur
   * @param value
   *          String the value that needs to be checked
   * @return boolean true if all restrictions are met, false otherwise
   */
  protected boolean isValid(MessageList messages, LDDataType value) {
    Boolean enumarationResult = null;
    boolean isValid = true;
    try {

      if (restrictions != null) {

        Iterator iter = restrictions.iterator();
        while (iter.hasNext()) {
          String[] tuple = (String[]) iter.next();

          if (tuple[0].equalsIgnoreCase("enumeration")) {
            if (enumarationResult == null) {
              enumarationResult = new Boolean(false);
            }
            // if one enumeration restriction returns true, the enumeration is
            // valid

            enumarationResult = Boolean.valueOf(enumarationResult.booleanValue()
                | checkRestriction(tuple[0], tuple[1], value));
          } else {
            // always true otherwise RestrictionViolationException would have
            // been thrown
            checkRestriction(tuple[0], tuple[1], value);
          }
        }
      }
    } catch (RestrictionViolationException ex) {
      messages.logError(ex.getMessage());
      isValid = false;
    }

    // now we must check if there were any enumeration restrictions. If so see
    // if one of them matched the value
    if ((enumarationResult != null) && (!enumarationResult.booleanValue())) {
      isValid = false;
      messages.logError(value + " not part of enumeration");
    }
    return isValid;
  }

  /**
   * Returns the data type of this PropertyDefinition as an int value.
   * 
   * @return int the data type represented as integer value
   */
  public abstract int getType();

  /**
   * Returns the XML blob representing the default value in XML format to be
   * used when initialising properties based on this PropertyDefintion. This
   * method is called when creating the PropertyDefinition itself. So this is
   * never called when creating an instance of this PropertyDefinition.
   * 
   * @return String the XML default value to used when creating Property
   */
  protected String getDefaultBlobValue() {
    // Note that the toXml() of defaultValue could have been used, but that
    // would have created an undesired dependency
    return (defaultValue != null ? "<value>" + defaultValue + "</value>" : "<no-value/>");
  }

  /**
   * Return the role id of this object. The role id is only defined in case of a
   * local role scope. Null otherwise.
   * 
   * @return String representing the role id
   */
  public String getRoleId() {
    return roleId;
  }

}