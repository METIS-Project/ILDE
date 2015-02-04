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
import java.util.Iterator;

import org.coppercore.common.Parser;
import org.coppercore.exceptions.PropertyException;
import org.coppercore.exceptions.TypeCastException;
import org.w3c.dom.Element;

/**
 * This is the abstract root class for all component defintions that have local personal
 * content.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.12 $, $Date: 2005/01/20 16:12:04 $
 */
public abstract class LocalPersonalContentDef extends PropertyDef {

  private Collection items = null;

  /**
   * contains the String representation of the XML content part of the implementing component.
   */
  protected String localContentString = null;

  /**
   * contains the visibility attribute of this component. Valid value are "true" and "false".
   */
  protected String isVisible;

  // NOTE: this property may not be initialized here because class intitialization will overrule any
  // value set during unpacking of the XML blob. Use the onInit() method instead for initializations that
  // are critical for the object creation
  /**
   * contains the dom node representation of the content part of the implementing component.
   */
  protected Element localContent;

  /**
   * contains the default value for any instance based of the implementing component.
   */
  protected String defaultValue;

  /**
   * Default constructor to be used during run time.
   *
   * @param uolId int the Uol database id of the IMS LD isntance defining this
   *   Property
   * @param propId String the identifier of the this Property as defined in the
   *   IMS-LD instance
   * @throws PropertyException when the constructor fails
   */
  public LocalPersonalContentDef(int uolId, String propId) throws PropertyException {
    super(uolId, propId);
  }

  /**
   * Called by the constructor just before the unpacking of the
   * XML data. It provides an oppertunity to initialize any properties when
   * needed.
   */
  protected void onInit() {
    super.onInit();

    //reset the initial values of the local content node and the default value
    localContent = null;
    isVisible = null;
    defaultValue = "<value/>";
  }

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
   * @param contentXml
   *          String the String representation of the default XML value for the
   *          content
   * @param items
   *          Collection of String[] tuples representing all the items that are
   *          part of the content. The first element of the tuple contains the
   *          id of the item and the second element contains the visibility of
   *          that item.
   */
  public LocalPersonalContentDef(int uolId, String propId, String contentXml, Collection items) {
    this.uolId = uolId;
    this.propId = propId;
    this.localContentString = contentXml;
    this.items = items;

    //we can't assign the default value here because child constructors still have
    //to set their value when this constructor is called. So every leaf in the hierarchy
    //must set their own default value. A default value will be give in case there is no
    //default value
    defaultValue = "<value/>";
  }

  /**
   * Constructor to be used during publishing. This constructor should be used
   * for components that have a visibilty property.
   * 
   * <p>
   * This constructor creates a new LocalContentDef object based on the passed
   * parameters.
   * 
   * @param uolId
   *          int the database id of the Uol for which this property definition
   * @param propId
   *          String the identifier of this component as defined in IMS LD
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
  public LocalPersonalContentDef(int uolId, String propId, String isVisible, String contentXml, Collection items) {
    this(uolId, propId, contentXml, items);
    this.isVisible = isVisible;
  }

  /**
   * Returns a deep copy of the content as Dom Element.
   * 
   * @return Element containing a deep copy of the content.
   */
  protected Element getContent() {
    //we must clone the content node because it will be personalized
    return (Element) localContent.cloneNode(true);
  }

  /**
   * Processes default elements found in explicit property definition. Because
   * JAVA does not support multiple inheritance this method can be found here
   * and will also be called by the explicit property data objects. This should
   * not cause any problems.
   * 
   * @param node
   *          Node the dom node to be processed
   * @param anUolId
   *          int the database id of the Uol defining this object
   * @throws PropertyException
   *           when the method fails
   * @throws TypeCastException
   *           when a value can not be cast to the type of this
   *           PropertyDefintion
   * @return boolean returns true when the child elements require further
   *         processing. False otherwise.
   */
  protected boolean processElement(Element node, int anUolId) throws PropertyException, TypeCastException {
    boolean result = false;
    if (node.getNodeName().equals("value")) {
      //this will be the default content for new content components
      defaultValue = Parser.documentToString(node);
      result = true;
    } else if (node.getNodeName().equals("content")) {
      //this is the content part
      localContent = node;
      //localContentString = Parser.documentToString((Element)node.getFirstChild());
      result = true;
    }
    return result;
  }

  /**
   * Adds the XML representation of this PropertyDefintion to the passed
   * PrintWriter stream. This method may be overload be implementing components.
   * 
   * @param result
   *          PrintWriter the stream to which the XML representation will be
   *          added.
   */
  protected void toXmlBody(PrintWriter result) {
    result.write(defaultValue);
    result.write("<content>" + localContentString + "</content>");
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
    result.write("<" + getDataType() + ">");
    toXmlBody(result);
    result.write("</" + getDataType() + ">");
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
    if (isVisible != null) {
      //add the visibility with a visibility date as far in the past as possible
      result.append("<isvisible visible-since=\"" + 0 + "\">" + isVisible + "</isvisible>");
    }

    if (items != null) {
      Iterator iter = items.iterator();
      while (iter.hasNext()) {
        String[] tuple = (String[]) iter.next();
        result.append("<item identifier=\"" + tuple[0] + "\" isvisible=\"" + tuple[1] + "\"/>");
      }
    }
  }

  /**
   * This method build the default value based on succesive call to the
   * setDefaultValue method. Each class in the class hierarchy may add its
   * specific data to the default value.
   * 
   * @return String the resulting default value
   * @see #setDefaultValue(StringBuffer)
   */
  protected String buildDefaultValue() {
    StringBuffer result = new StringBuffer();
    result.append("<value>");
    setDefaultValue(result);
    result.append("</value>");
    return result.toString();
  }

  /**
   * Returns the String representation of the data type of this component.
   * 
   * @return String the data type of this component
   */
  public abstract String getDataType();

  /**
   * Returns the default value of this components as an XML fragment.
   * @return String the default value for any instance of this component as a XML fragment
   */
  protected String getDefaultBlobValue() {
    return defaultValue;
  }

}
