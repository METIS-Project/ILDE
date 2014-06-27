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


package org.coppercore.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.coppercore.common.Parser;
import org.coppercore.exceptions.SemanticException;
import org.w3c.dom.Node;

/**
 * This class represents an ims learning design time-limit element.
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/01/11 13:15:03 $
 */
public class IMSTimeLimit extends IMSObject {
  final String patternStr = "P((\\d+)Y)?+((\\d+)M)?+((\\d+)D)?+T?((\\d+)H)?+((\\d+)M)?((\\d+)S)?";
  //"P(\\d+)Y(\\d+)M(\\d+)DT(\\d+)H(\\d+)M(\\d+)S";

  final Pattern pattern = Pattern.compile(patternStr);

  private String timeLimit = null;
  private String timeLimitPropertyDef = null;

  public IMSTimeLimit() {
    // default constructor
  }

  protected void initialize(Node anItemNode, IMSLDManifest aManifest) {
    super.initialize(anItemNode, aManifest);

    timeLimit = Parser.getTextValue(anItemNode);
    timeLimitPropertyDef = Parser.getNamedAttribute(anItemNode,"property-ref");

  }

  protected void isValid() throws SemanticException {

    if (timeLimitPropertyDef != null) {
      Node referedNode = node.getOwnerDocument().getElementById(timeLimitPropertyDef);

      if (!(("loc-property".equals(referedNode.getLocalName())) ||
          ("locpers-property".equals(referedNode.getLocalName())) ||
          ("locrole-property".equals(referedNode.getLocalName())) ||
          ("glob-property".equals(referedNode.getLocalName())) ||
          ("globpers-property".equals(referedNode.getLocalName())))) {
        throw new SemanticException("time-limit[" + timeLimitPropertyDef +
                             "] should refer to an property"
                             + " but refers to " + referedNode.getLocalName());
      }
    }
    Matcher matcher = pattern.matcher(timeLimit);

    if (!matcher.find()) {
      throw new SemanticException("Invalid time limit format: " + timeLimit);
    }
  }
}
