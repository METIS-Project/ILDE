/*
 * CopperCore , an IMS-LD level C engine
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

package org.coppercore.validator;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Extends the DefaultHandler in order to retreive the level of the learning-
 * design.
 *
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.4 $, $Date: 2005/01/11 13:15:02 $
 */
public class LevelHandler
    extends DefaultHandler {
  private String level = null;

  /**
   * Overloads the default startElement of the DefaultHandler to check if the
   * learning-design node was encountered. This node has an level attribute
   * indicating the level of the learning design (A,B or C).
   *
   * @param uri String
   * @param localName String
   * @param qName String
   * @param attributes Attributes
   * @throws SAXException
   */
  public void startElement(java.lang.String uri,
                           java.lang.String localName,
                           java.lang.String qName,
                           Attributes attributes) throws SAXException {
    if (org.coppercore.common.Parser.IMSLDNS.equals(uri) &&
        "learning-design".equals(localName)) {
      //we have found the start tag so we can determine the level
      level = attributes.getValue("level");
    }
  }

  /**
   * Returns the level of the learning-design that was determined during parsing.
   * @return String the level of the learning-design (A,B or C)
   */
  public String getLevel() {
    return level;
  }
}
