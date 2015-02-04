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

package org.coppercore.parser;

import org.coppercore.common.URL;
import org.coppercore.exceptions.URLSyntaxException;
import org.w3c.dom.Node;

/**
 * This class represents an IMS Content Package Resource element.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.11 $, $Date: 2005/05/31 07:40:04 $
 */
public class IMSCPResourceNode extends IMSLDNode implements IIMSCPResource {

  private URL url = null;

  private String type = null;

  /**
   * Constructs an IMSCPResourceNode instance for the passed xml dom element.
   * 
   * @param aNode
   *          Node the xml dom node to parse
   * @param aParent
   *          IMSLDNode the parsed IMS learing design element that is the parent
   *          element of this object
   */
  public IMSCPResourceNode(Node aNode, IMSLDNode aParent) {
    super(aNode, aParent);

    type = getNamedAttribute("type");
    String baseURLAttr = getNamedAttribute("base", org.coppercore.common.Parser.XMLNS);
    String urlAttr = getNamedAttribute("href");

    URL baseURL = null;

    try {
      if (baseURLAttr != null) {
        baseURL = new URL(((IMSCPResourcesNode) parent).getBaseURL(), baseURLAttr);
      }
      else {
        baseURL = ((IMSCPResourcesNode) parent).getBaseURL();
      }
      url = new URL(baseURL, urlAttr);
      baseURL = new URL(baseURLAttr);
    }
    catch (URLSyntaxException ex) {
      System.out.println(ex);
    }
  }

  /**
   * Returns the url of this resource.
   * 
   * @return String the url of this resource
   */
  public String getURL() {
    return url.toString();
  }

  /**
   * Returns the type of this resource.
   * 
   * @return String the type of this resource
   */
  public String getType() {
    return type;
  }

  /**
   * Returns true if the type of this resource is imsldcontent as specified by
   * the type attribute of the resource element in the IMS content package
   * specification.
   * 
   * @return boolean true if the type of this resource is imsldcontent
   */
  public boolean isIMSLDContent() {
    return (type == null ? false : type.equals("imsldcontent"));
  }
  
  /**
   * Return the identifier of this resource
   * 
   * @return String the identifier of this resource
   */
  public String getResourceIdentifier() {
    return getIdentifier();
  }  
}