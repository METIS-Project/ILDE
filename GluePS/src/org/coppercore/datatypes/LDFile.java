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

package org.coppercore.datatypes;

import org.coppercore.exceptions.TypeCastException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.8 $, $Date: 2005/01/11 13:15:16 $
 */
public class LDFile extends LDDataType {

  private String value = null;
  private Document xmlDom = null;

  public LDFile(String value) throws TypeCastException {
    //the value passed is an XML blob containing the characteristics of the uploaded file
    this.value = value;
    try {
      this.xmlDom = org.coppercore.common.Parser.createDomInstance(value);
    }
    catch (Exception ex) {
      throw new TypeCastException("File metadata contains no valid xml", ex);
    }
  }

  public int getType() {
    return LDFILE;
  }

  public String getTypeName() {
    return LDFILE_NAME;
  }

  public LDFile toLDFile() throws TypeCastException {
    return this;
  }

  /**
   * Returns this datatype as a DOM Node.
   * @param owner Document context for which this node is created
   * @return Node the created Node
   */
  public Node asDomNode(Document owner) {
    Node result = owner.importNode(xmlDom.getDocumentElement(), true);
    return  result;
  }

  /**
   * Returns the value of this datatype in the native format
   *
   * @return String
   */
  public String getValue() {
    return value;
  }

  public boolean equals(LDDataType obj) {
    if (this == obj) {
      return true;
    }
    if (! (obj instanceof LDFile)) {
      return false;
    }
    LDFile that = (LDFile) obj;
    if (! (that.value == null ? this.value == null :
           that.value.equals(this.value))) {
      return false;
    }
    return true;
  }

  /**
   * toString
   *
   * @return String
   */
  public String toString() {
    return value;
  }

}
