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

package org.coppercore.common;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Utility class for creating xml.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.6 $, $Date: 2005/01/11 13:15:10 $
 */
public class XMLTag {
  private String tagName = null;
  private HashMap attributes = new HashMap();

  /**
   * Creates a nwe XMLTag with the given tagname.
   * @param tagName String the xml tag name of this xml element
   */
  public XMLTag(String tagName) {
    setTagName(tagName);
  }

  /**
   * Returns the value of the tagname of this instance.
   * @return String the value of the tagname
   * @see #setTagName
   */
  public String getTagName() {
    return tagName;
  }

  /**
   * Sets the tag name of this instance to the specified value.
   * @param tagName String the new value for the tagname
   * @see #getTagName
   */
  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  /**
   * Adds a new attribute to the xml tag.
   *
   * <p>If attribute value is null the attribute is not added. If the attribute
   * already exists its value is not changed.
   *
   * @param attrName String the name of the attribute
   * @param attrValue String the value of the attribute
   */
  public void addAttribute(String attrName, String attrValue) {
    if ( (attrValue != null) && (!attributes.containsKey(attrName))) {
      attributes.put(attrName, attrValue);
    }
  }

  /**
   * Adds a new attribute to the xml tag.
   *
   * <p>If the new attribute value is null the default value is assigned to the
   * attribute.
   *
   * <p>If the attribute already existed its value is not overwritten.
   *
   * @param attrName String the name of the new attribute
   * @param attrValue String the value of the attribute. If this value is null,
   *   the default value is assigned
   * @param defaultValue String the value of the attribute if the specified
   *   attrvalue is null
   */
  public void addAttribute(String attrName, String attrValue,
                           String defaultValue) {
    if (!attributes.containsKey(attrName)) {
      if (attrValue != null) {
        attributes.put(attrName, attrValue);
      }
      else {
        attributes.put(attrName, defaultValue);
      }
    }
  }

  /**
   * Adds a multiple value attribute to the xml tag.
   * @param attrName String the name of the new tag
   * @param values Collection the list of values of this attribute
   * @todo why no check if attribute already exists or its value is null?
   */
  public void addAttribute(String attrName, Collection values) {
    attributes.put(attrName, values);
  }

  /**
   * Returns a HashMap containing all attributes of this xml tag.
   * @return HashMap all attributes of this xml tag
   */
  private HashMap getAttributes() {
    return attributes;
  }

  /**
   * Writes the opening tag of the xml tag to the specified stream.
   *
   * @param output PrintWriter the output stream to write the tag to
   * @param isEmpty boolean specifies if this tag is an empty tag, if true
   *   closes the tag
   */
  private void writeOpenTag(PrintWriter output, boolean isEmpty) {
    output.print("<" + getTagName());

    HashMap attr = getAttributes();

    Iterator myIterator = attr.keySet().iterator();
    while (myIterator.hasNext()) {
      String attrName = (String) myIterator.next();
      Object attrValue = attr.get(attrName);

      output.print(" " + attrName + "=" + "\"");

      if (attrValue instanceof Collection) {

        Iterator iter = ( (Collection) attrValue).iterator();
        while (iter.hasNext()) {
          String value = (String) iter.next();
          output.print(value + (iter.hasNext() ? " " : ""));
        }
      }
      else {
        output.print(attrValue);
      }
      output.print("\"");
    }

    if (isEmpty) {
      output.print("/>");
    }
    else {
      output.print(">");
    }
  }

  /**
   * Writes the close tag for this xml element to the stream.
   * @param output PrintWriter the output stream to write the tag to
   */
  public void writeCloseTag(PrintWriter output) {
    output.print("</" + getTagName() + ">");
  }

  /**
   * Writes the tag as an empty tag to the specified stream.
   *
   * <p> An empty tag does contain all attributs, but cannot contain any other
   * content like PCDATA or other xml tags.
   *
   * @param output PrintWriter the outputstream to write the tag to
   */
  public void writeEmptyTag(PrintWriter output) {
    writeOpenTag(output, true);
  }

  /**
   * Writes the start tag of this element to the specified stream.<p>
   * The start tags includes all attributes.
   * @param output PrintWriter the outputstream to write the tag to
   */
  public void writeOpenTag(PrintWriter output) {
    writeOpenTag(output, false);
  }
}
